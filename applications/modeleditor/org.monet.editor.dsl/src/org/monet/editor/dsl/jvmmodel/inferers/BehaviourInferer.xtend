package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import java.lang.SuppressWarnings
import java.util.HashMap
import java.util.List
import org.eclipse.emf.common.util.EList
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmMember
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.access.impl.ClassURIHelper
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.bpi.ExporterScope
import org.monet.bpi.Properties
import org.monet.editor.dsl.collections.RepetitionCounter
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.JavaHelper
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.jvmmodel.MMLExtensions
import org.monet.editor.dsl.metamodel.Item
import org.monet.editor.dsl.monetModelingLanguage.Attribute
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Function
import org.monet.editor.dsl.monetModelingLanguage.Method
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.monetModelingLanguage.Variable
import org.monet.editor.dsl.monetModelingLanguage.XTReference

class BehaviourInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache

	@Inject private TypeReferences references
	@Inject private ClassURIHelper uriHelper
	@Inject private ClassNameInferer classNameInferer
	@Inject private FieldInferer fieldInferer
	@Inject private TaskInferer taskInferer
	@Inject private RuleInferer ruleInferer
	@Inject private DatastoreInferer datastoreInferer
	@Inject private DatastoreBuilderInferer datastoreBuilderInferer

	def void inferTaskMethod(Attribute f, JvmGenericType taskType, boolean prelinkingPhase) {
		if (!prelinkingPhase) {
			var ref = f.valueAsReferenciable;
			if (ref != null) {
				var qualifiedName = ref.fullyQualifiedName;
				if (qualifiedName != null) {
					val methodTypeName = XtendHelper::convertQualifiedNameToGenName(qualifiedName);
					val methodTypeRef = ref.resolve(methodTypeName);
					val methodName = JavaHelper::toJavaIdentifier(f.id);
					taskType.members += f.toMethod("get" + methodName, methodTypeRef) [
						body = [ ap |
							ap.append("return (");
							ap.append(methodTypeName);
							ap.append(")genericGet");
							ap.append(methodName);
							ap.append("();");
						]
					];
					taskType.members += f.toMethod("set" + methodName, f.resolveVoidType) [
						parameters += f.toParameter("node", methodTypeRef);
						body = [ ap |
							ap.append("this.genericSet");
							ap.append(methodName);
							ap.append("((");
							ap.append(methodTypeName);
							ap.append(")node);");
						]
					];
				}
			}
		}
	}

	def void infer(Definition e, Item definitionItem, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		if (e.definitionType == "datastore") {
			this.datastoreInferer.infer(e, acceptor, prelinkingPhase);
		} else if (e.definitionType == "form" || e.definitionType == "document") {
			this.inferMappings(e, acceptor, prelinkingPhase);

			if (e.definitionType == "form") {
				e.properties.filter([p|p.id != null && p.id.startsWith("field-")]).forEach(
					[p|fieldInferer.inferTypeClass(e, p, acceptor)])
			}
		}
		this.inferProperties(e, acceptor, prelinkingPhase);
		if (e.definitionType == "exporter") {
			acceptor.accept(e.toClass(this.classNameInferer.inferExporterScopeName(e))).initializeLater [
				var Definition superType = null;
				if (e.superType != null) {
					superType = e.superType;
				} else if (e.replaceSuperType != null) {
					superType = e.replaceSuperType;
				}
				val hasSuperType = superType != null && superType.fullyQualifiedName != null;
				if (hasSuperType) {
					superTypes += e.resolve(classNameInferer.inferExporterScopeName(superType));
				} else {
					superTypes += e.resolveExporterScopeImplType;
				}
				var String typeName = null;
				var eTarget = e.getAttribute("target").valueAsDefinition;
				if (eTarget != null)
					typeName = this.classNameInferer.inferBehaviourName(eTarget as Definition);
				val JvmTypeReference targetDocument = e.resolve(typeName);
				members += e.toConstructor [
					parameters += e.toParameter("instance", e.resolve(this.classNameInferer.inferBehaviourName(e)));
					parameters += e.toParameter("target", e.resolveNodeType);
					body = [ ap |
						if (hasSuperType) {
							ap.append("super(instance, target);");
						} else {
							ap.append("instance.super(target);");
						}
					]
				];
				members += e.toMethod("toDocument", e.resolveVoidType) [
					parameters += e.toParameter("document", targetDocument);
					body = [ ap |
						ap.append("this.internalToDocument(document);");
					]
				];
				members += e.toMethod("toNewDocument", targetDocument) [
					body = [ ap |
						ap.append("return (");
						ap.append(targetDocument.qualifiedName);
						ap.append(")internalToNewDocument(\"");
						ap.append(targetDocument.qualifiedName);
						ap.append("\");");
					]
				];
			];
		}

		acceptor.accept(e.toClass(this.classNameInferer.inferBehaviourName(e))).initializeLater [
			var parentBehaviourClass = definitionItem.parentBehaviourClass;
			var Class<?> annotation = typeof(SuppressWarnings);
			var ann = e.toAnnotation(annotation);
			ann.explicitValues += XtendHelper::createAnnotationValue("value", "all", annotation, uriHelper);
			annotations += ann;
			var finalNames = new HashMap<Attribute, String>();
			var board = new RepetitionCounter<String, Attribute>();
			var List<Attribute> items = e.getProperties("contain", "mapping").map(
				[ p |
					var Iterable<Attribute> result;
					if (p.id == "mapping") {
						result = p.getAttributes("index");
					} else if (p.id == "contain") {
						result = p.getAttributes("node");
					}
					return result;
				]).flatten.toList;
			var level = 0;
			while (items.size > 0) {
				for (item : items) {
					var definition = item.valueAsDefinition;
					if (definition != null) {
						var fullName = definition.fullyQualifiedName;
						var skips = fullName.segmentCount - (level + 1);
						if (skips > 0)
							fullName = fullName.skipFirst(skips);
						board.add(JavaHelper::toInvertedName(fullName), item);
					}
				}
				for (entry : board.notRepeated.entrySet)
					finalNames.put(entry.value, entry.key);

				items = board.repeated;
				level = level + 1;
				board.clear;
			}
			for (f : e.features) {
				switch f {
					Attribute: {
						switch f.id {
							case "index": {
								if (!prelinkingPhase) {
									var eIndex = f.valueAsDefinition
									if (eIndex != null) {
										val qualifiedName = eIndex.fullyQualifiedName;
										if (qualifiedName != null) {
											val indexQualifiedName = XtendHelper::convertQualifiedNameToGenName(
												qualifiedName);
											var indexType = eIndex.resolve(indexQualifiedName);
											var iterableType = eIndex.resolveIterableType(indexType);
											members += e.toMethod("getAll", iterableType) [
												body = [ ap |
													ap.append("return (Iterable<");
													ap.append(indexQualifiedName);
													ap.append(">)genericGetAll();");
												]
											];
											members += e.toMethod("getAll", iterableType) [
												parameters += e.toParameter("orderBy", e.resolveOrderExpressionType);
												body = [ ap |
													ap.append("return (Iterable<");
													ap.append(indexQualifiedName);
													ap.append(">)genericGetAll(orderBy);");
												]
											];
											members += e.toMethod("get", iterableType) [
												parameters += e.toParameter("where", e.resolveExpressionType);
												body = [ ap |
													ap.append("return (Iterable<");
													ap.append(indexQualifiedName);
													ap.append(">)genericGet(where);");
												]
											];
											members += e.toMethod("get", iterableType) [
												parameters += e.toParameter("where", e.resolveExpressionType);
												parameters += e.toParameter("orderBy", e.resolveOrderExpressionType);
												body = [ ap |
													ap.append("return (Iterable<");
													ap.append(indexQualifiedName);
													ap.append(">)genericGet(where, orderBy);");
												]
											];
											members += e.toMethod("getFirst", indexType) [
												parameters += e.toParameter("where", e.resolveExpressionType);
												body = [ ap |
													ap.append("return (");
													ap.append(indexQualifiedName);
													ap.append(")genericGetFirst(where);");
												]
											];
										}
									}
								}
							}
							case "target": {
								if (e.definitionType == "service" || e.definitionType == "activity") {
									inferTaskMethod(f, it, prelinkingPhase);
								} else if (e.definitionType == "importer") {
									var targetDefinition = f.valueAsDefinition;
									if (targetDefinition != null) {
										val String schemaTypeName = classNameInferer.
											inferSchemaBehaviourName(targetDefinition);
										it.members += f.toMethod("getTargetSchemaClass", f.resolveClassType) [
											body = [ ap |
												ap.append("return ");
												ap.append(schemaTypeName);
												ap.append(".class;");
											];
										];
									}
								}
							}
						}
					}
					Property: {
						switch f.id {
							case "contain": {
								inferContain(f, e, it.members, prelinkingPhase, finalNames);
							}
							case "mapping": {
								var indexAttb = f.getAttribute("index")
								inferRef(indexAttb, e, it.members, prelinkingPhase, finalNames.get(indexAttb));
							}
							case "reference": {
								inferIndexEntryAttributes(f, it);
							}
							case "is-singleton": {
								inferSingleton(e, it);
							}
							case "is-abstract": {
								it.setAbstract(true);
							}
							case "operation": {
								inferOperation(f, it);
							}
							default: {
								if (f.id != null && f.id.startsWith("field-")) {
									fieldInferer.infer(e, f, it);
								}
							}
						}
					}
					Method: {
						val method = f.toMethod(JavaHelper::toAttributeJavaIdentifier(f.id), f.resolveVoidType) [
							body = f.body;
							for (p : f.params) {
								parameters += p.toParameter(p.name, p.parameterType);
							}
						]

						it.members += method;

						if (f.id == "onImportItem" && e.definitionType == "importer") {
							it.members += f.toMethod("onImportItem", f.resolveVoidType) [
								body = [ appender |
									appender.append("super.onImportItem(item);\n");
									appender.append("this.onImportItem((");
									appender.append(f.params.head.parameterType.qualifiedName);
									appender.append(")item);");
								];
								parameters += f.toParameter("item", f.resolveSchemaType);
							]
							method.visibility = JvmVisibility::PRIVATE;
						} else if (f.id == "onExportItem" && e.definitionType == "exporter") {
							it.members += f.toMethod("onExportItem", f.resolveVoidType) [
								body = [ appender |
									appender.append("super.onExportItem(item);\n");
									appender.append("this.onExportItem((");
									appender.append(f.params.head.parameterType.qualifiedName);
									appender.append(")item);");
								];
								parameters += f.toParameter("item", f.resolveSchemaType);
							]
							method.visibility = JvmVisibility::PRIVATE;
						}
					}
					Function: {
						it.members += f.toMethod(f.name, f.type) [
							documentation = f.documentation
							for (p : f.params) {
								parameters += p.toParameter(p.name, p.parameterType)
							}
							body = f.body
						]
					}
					Variable: {
						if (e.definitionType == "importer" || e.definitionType == "exporter") {
							it.members += f.toField(f.name, f.type);
							it.members += f.toGetter(f.name, f.type);
							it.members += f.toSetter(f.name, f.type);
						}
					}
				}
			}
			if (e.definitionType == "service" || e.definitionType == "activity") {
				taskInferer.inferMethods(e, it);
			} else if (e.definitionType == "datastore") {

				e.getProperties("dimension").forEach(
					[ dimension |
						val dimensionTypeName = classNameInferer.inferDimensionName(dimension);
						val dimensionType = references.getTypeForName(dimensionTypeName, e);
						if (dimension.code != null) {
							val dimensionCode = dimension.code.value.toString;
							val dimensionMethodName = "get" + JavaHelper::toJavaIdentifier(dimension.name);
							members += e.toMethod(dimensionMethodName, dimensionType) [
								body = [ ap |
									ap.append("return (");
									ap.append(dimensionTypeName);
									ap.append(
										")this.getDimension(" + dimensionTypeName + ".class,\"" + dimensionCode + "\");")
								];
							];
						}
					]);

				e.getProperties("cube").forEach(
					[ cube |
						val cubeTypeName = classNameInferer.inferCubeName(cube);
						val cubeType = references.getTypeForName(cubeTypeName, e);
						if (cube.code != null) {
							val cubeCode = cube.code.value.toString;
							val cubeMethodName = "get" + JavaHelper::toJavaIdentifier(cube.name);
							members += e.toMethod(cubeMethodName, cubeType) [
								body = [ ap |
									ap.append("return (");
									ap.append(cubeTypeName);
									ap.append(")this.getCube(" + cubeTypeName + ".class,\"" + cubeCode + "\");");
								];
							];
						}
					]);

			} else if (e.definitionType == "datastore-builder") {
				datastoreBuilderInferer.inferMethods(e, it, e.getAttribute("source"),
					e.getProperties("to"), prelinkingPhase);
			}
			if (e.isAbstract()) {
				it.setAbstract(true);
			}
			var rules = e.properties.filter([p|p.id != null && p.id.startsWith("rule")]).toList;
			if (rules.size > 0) {
				ruleInferer.infer(e, rules, it);
			}
			if (definitionItem.getChild("operation") != null) {
				inferCommandOperations(definitionItem, e, it);
			}
			if (definitionItem.getToken() == "document") {
				val schemaName = classNameInferer.inferSchemaBehaviourName(e);
				var schemaTypeRef = e.newTypeRef(schemaName);
				members += e.toMethod("getSchema", schemaTypeRef) [
					body = [ ap |
						ap.append("return (");
						ap.append(schemaName);
						ap.append(") this.genericGetSchema();");
					];
				];
			}
			if (definitionItem.getToken == "importer") {
				e.inferImporterGetInstance(it);
			} else if (definitionItem.getToken == "exporter") {
				e.inferExporterGetInstance(it);

				members += e.toMethod("prepareExportOf", e.resolveExporterScopeType) [
					parameters += e.toParameter("node", e.resolveNodeType);
					body = [ ap |
						ap.append("return (");
						ap.append(typeof(ExporterScope).name);
						ap.append(") new ");
						ap.append(this.classNameInferer.inferExporterScopeName(e));
						ap.append("(this, node);");
					];
				];
			} else if (definitionItem.isNodeDefinition && !e.hasProperty("is-singleton")) {
				e.inferNodeCreateInstance(it);
			}
			if (!prelinkingPhase && (e.superType != null && e.superType.fullyQualifiedName != null) ||
				e.replaceSuperType != null && e.replaceSuperType.fullyQualifiedName != null) {
				var JvmTypeReference behaviourSuperType;
				var Definition definition = e.superType;
				if (definition == null)
					definition = e.replaceSuperType;
				var superTypeName = this.classNameInferer.inferBehaviourName(definition);
				behaviourSuperType = references.getTypeForName(superTypeName, e).cloneWithProxies;
				if (behaviourSuperType != null)
					it.superTypes += behaviourSuperType;
			} else if (definitionItem.getToken == "collection" || definitionItem.getToken == "catalog") {
				it.superTypes += e.resolve(parentBehaviourClass).cloneWithProxies;
			} else if (definitionItem.getToken == "index" || definitionItem.getToken == "datastore-builder") {
				var simpleSuperType = e.resolve(parentBehaviourClass);
				if (simpleSuperType != null)
					it.superTypes += simpleSuperType.cloneWithProxies;
			} else {
				it.superTypes += e.resolve(parentBehaviourClass).cloneWithProxies;
			}
		]

	}
	
	def private void inferProperties(Definition e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		var eProperties = e.getProperty("properties");
		if (eProperties != null)
			inferMappingClass(e, acceptor, prelinkingPhase, eProperties, this.classNameInferer.inferPropertiesName(e),
				"getProperties");
	}

	def private void inferMappings(Definition e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		e.getProperties("mapping").forEach(
			[ eMapping, i |
				inferMappingClass(e, acceptor, prelinkingPhase, eMapping, this.classNameInferer.inferMappingName(e, i),
					"getReference");
			]);
	}

	def private JvmTypeReference findMappingInParent(Definition parentDefinition, Definition currentMappingIndex) {
		var Property parentMapping;
		var i = 0;
		for (px : parentDefinition.getProperties("mapping")) {
			var parentIndexDefinitionName = px.getAttribute("index").valueAsReferenciable.fullyQualifiedName.toString;
			var currentOrSuperTypeMappingIndex = currentMappingIndex;

			while (currentOrSuperTypeMappingIndex != null && parentMapping == null) {
				var currentMappingIndexName = currentOrSuperTypeMappingIndex.fullyQualifiedName.toString;
				if (parentIndexDefinitionName == currentMappingIndexName) {
					parentMapping = px;
				} else if (currentOrSuperTypeMappingIndex.superType != null &&
					currentOrSuperTypeMappingIndex.superType.fullyQualifiedName != null) {
					currentOrSuperTypeMappingIndex = currentOrSuperTypeMappingIndex.superType;
				} else {
					currentOrSuperTypeMappingIndex = null;
				}
			}

			if (parentMapping == null) {
				i = i + 1;
			}
		}

		if (parentMapping != null) {
			return parentDefinition.resolve(this.classNameInferer.inferMappingName(parentDefinition, i));
		} else if (parentDefinition.superType != null && parentDefinition.superType.fullyQualifiedName != null) {
			return findMappingInParent(parentDefinition.superType, currentMappingIndex);
		} else {
			return null;
		}
	}

	def private void inferMappingClass(Definition e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase,
		Property eMapping, String mappingClassName, String referenceMethodName) {
		acceptor.accept(eMapping.toClass(mappingClassName)).initializeLater [
			val eIndex = eMapping.getAttribute("index");
			var JvmTypeReference mappingClassSuperType;
			if (e.superType != null && e.superType.fullyQualifiedName != null) {
				mappingClassSuperType = findMappingInParent(e.superType, eIndex.valueAsDefinition)
			}
			if (mappingClassSuperType == null) {
				superTypes += e.resolveMappingImplType
			} else {
				superTypes += mappingClassSuperType;
			}
			val nodeClassName = this.classNameInferer.inferBehaviourName(e);
			var String tempReferenceClassName = typeof(Object).name;
			val eCalculate = eMapping.getMethod("calculate");
			if (eIndex == null) {
				tempReferenceClassName = typeof(Properties).name;
				members += eMapping.toMethod("calculateMapping", e.resolveVoidType) [
					body = [ ap |
						if (eCalculate != null) {
							ap.append("this.calculate();");
						}
					]
				];
			} else if (eIndex.value instanceof XTReference && (eIndex.value as XTReference).value instanceof Definition) {
				val eIndexDefinition = (eIndex.value as XTReference).value as Definition;
				tempReferenceClassName = this.classNameInferer.inferBehaviourName(eIndexDefinition);
				val referenceClassName = tempReferenceClassName;

				members += eMapping.toMethod("calculateMapping", e.resolveVoidType) [
					body = [ ap |
						ap.append("super.calculateMapping();\n")
						ap.append(nodeClassName);
						ap.append(" node = (");
						ap.append(nodeClassName);
						ap.append(") this.genericGetNode();\n");
						ap.append(referenceClassName);
						ap.append(" reference = (");
						ap.append(referenceClassName);
						ap.append(") this.genericGetReference();\n");
						val itemMap = eIndexDefinition.getProperties("reference").map([p|p.getProperties("attribute")]).
							flatten.toMap([p|p.name]);
						var autoMapping = e.properties.filter(
							[p|p.id.startsWith("field-") && itemMap.containsKey(p.name)]).toMap([p|p.name]);
						for (entry : autoMapping.entrySet()) {
							var boolean canBeMapped = true;

							var fieldType = "";
							switch entry.value.id {
								case "field-boolean": {
									fieldType = "BOOLEAN";
								}
								case "field-check": {
									fieldType = "CHECK";
								}
								case "field-date": {
									fieldType = "DATE";
								}
								case "field-link": {
									fieldType = "LINK";
								}
								case "field-memo": {
									fieldType = "STRING";
								}
								case "field-node": {
									fieldType = "LINK";
								}
								case "field-number": {
									fieldType = "REAL";
								}
								case "field-summation": {
									fieldType = "REAL";
								}
								case "field-picture": {
									fieldType = "PICTURE";
								}
								case "field-select": {
									fieldType = "TERM";
								}
								case "field-serial": {
									fieldType = "STRING";
								}
								case "field-text": {
									fieldType = "STRING";
								}
							}

							var attributeType = itemMap.get(entry.key).getAttribute("type").valueAsString;
							if (attributeType == "INTEGER")
								attributeType = "REAL";

							canBeMapped = fieldType.equals(attributeType);

							if (canBeMapped) {
								var needAsTerm = entry.value.id == "field-link" && attributeType == "TERM";
								if (entry.value.hasProperty("is-multiple")) {
									ap.append("if(node.get")
									ap.append(entry.value.name);
									ap.append("().size() > 0)\n")
								}
								ap.append("reference.set");
								ap.append(entry.value.name);
								ap.append("(node.get")
								ap.append(entry.value.name);
								if (needAsTerm)
									ap.append("AsTerm");
								ap.append("()");
								if (entry.value.hasProperty("is-multiple"))
									ap.append(".get(0)");
								ap.append(");\n");
							}
						}
						if (eCalculate != null) {
							ap.append("this.calculate();");
						}
					];
				];
			}
			val referenceClassName = tempReferenceClassName;
			members += e.toMethod(referenceMethodName, e.resolve(referenceClassName)) [
				body = [ ap |
					ap.append("return (");
					ap.append(referenceClassName);
					ap.append(") this.genericGetReference();");
				];
			];
			members += e.toMethod("getNode", e.resolve(nodeClassName)) [
				body = [ ap |
					ap.append("return (");
					ap.append(nodeClassName);
					ap.append(") this.genericGetNode();");
				];
			];
			if (eCalculate != null) {
				members += eMapping.toMethod(eCalculate.id, e.resolveVoidType) [
					visibility = JvmVisibility::PRIVATE;
					body = eCalculate.body;
				];
			}
		];
	}

	def private void inferContain(Property e, Definition definition, EList<JvmMember> members, boolean prelinkingPhase,
		HashMap<Attribute, String> namesMap) {
		for (containMethod : e.getAttributes("node")) {
			if (!prelinkingPhase) {
				val ref = containMethod.valueAsDefinition;
				if (ref != null) {
					val fullQualifiedName = ref.fullyQualifiedName;
					var String referenceFullQualifiedName = null;

					if (fullQualifiedName != null) {
						referenceFullQualifiedName = XtendHelper::convertQualifiedNameToGenName(fullQualifiedName);
					} else {
						referenceFullQualifiedName = "java.lang.Object";
					}
					var JvmTypeReference containTypeReference = null;
					try {
						containTypeReference = ref.resolve(referenceFullQualifiedName);
					} catch (Exception ex) {
						containTypeReference = ref.resolveObjectType;
					}
					val staticContainTypeReference = containTypeReference;

					var onExecuteMethod = e.toMethod("get" + JavaHelper::toJavaIdentifier(namesMap.get(containMethod)),
						staticContainTypeReference) [
						body = [ ap |
							ap.append("return (");
							ap.append(staticContainTypeReference.qualifiedName.toString);
							ap.append(")this.getChildNode(\"");
							ap.append(fullQualifiedName.toString);
							ap.append("\");");
						];
					];
					onExecuteMethod.setVisibility(JvmVisibility::PUBLIC);
					members += onExecuteMethod;
				}
			}
		}
	}

	def private void inferRef(Attribute indexAttb, Definition definition, EList<JvmMember> members,
		boolean prelinkingPhase, String methodName) {
		if (prelinkingPhase)
			return;

		var indexAttbValue = indexAttb.valueAsReferenciable
		if (indexAttbValue != null) {
			val fullQualifiedName = indexAttbValue.fullyQualifiedName;

			var String referenceFullQualifiedName = null;

			if (fullQualifiedName != null) {
				referenceFullQualifiedName = XtendHelper::convertQualifiedNameToGenName(fullQualifiedName);
			} else {
				referenceFullQualifiedName = "java.lang.Object";
			}
			val addTypeReference = indexAttbValue.resolve(referenceFullQualifiedName);
			val reffullQualifiedName = referenceFullQualifiedName;
			var definitionNameTmp = "";
			if (fullQualifiedName != null) {
				definitionNameTmp = fullQualifiedName.toString;
			}
			val definitionName = definitionNameTmp;

			var onExecuteMethod = indexAttb.toMethod("get" + methodName, addTypeReference) [
				body = [ ap |
					ap.append("return (");
					ap.append(reffullQualifiedName);
					ap.append(")this.getIndexEntry(\"");
					ap.append(definitionName);
					ap.append("\");");
				];
			];
			onExecuteMethod.setVisibility(JvmVisibility::PUBLIC);
			members += onExecuteMethod;
		}
	}
	
	def inferCommandOperations(Item definitionItem, Definition e, JvmGenericType it) {
 		inferExecuteCommand(definitionItem, e, it);
		inferExecuteCommandConfirmation(e, it, "when", e.resolveBooleanPrimitiveType);
		inferExecuteCommandConfirmation(e, it, "onCancel", e.resolveVoidType);
	}

	def inferExecuteCommand(Item definitionItem, Definition e, JvmGenericType it) {
		var executeCommandMethod = e.toMethod("executeCommand", e.resolveVoidType) [
			parameters += e.toParameter("operation", e.resolveStringType);
			visibility = JvmVisibility::PUBLIC;
		];
		executeCommandMethod.body = [ ap |
			var boolean isFirst = true;
			for (p : e.getProperties("operation")) {
				if (!isFirst)
					ap.append("else ")
				ap.append("if(operation.equals(\"");
				ap.append(p.name);
				ap.append("\")) {\n\t")
				ap.append("this.on");
				ap.append(JavaHelper::toJavaIdentifier(p.name));
				ap.append("();\n}")
			}
			if (!isFirst)
				ap.append("else ")
			ap.append("super.executeCommand(operation);");
		];
		it.members += executeCommandMethod;
	}
	
	def inferExecuteCommandConfirmation(Definition definition, JvmGenericType it, String methodName, JvmTypeReference type) {
		var newMethod = definition.toMethod("executeCommandConfirmation" + JavaHelper::toJavaIdentifier(methodName), type) [
			parameters += definition.toParameter("operation", definition.resolveStringType);
			visibility = JvmVisibility::PUBLIC;
		];
		
		newMethod.body = [ ap |
			var boolean isFirst = true;

			for (operation : definition.getProperties("operation")) {
				val confirmation = operation.getProperty("confirmation");
				
				if (confirmation != null) {
					var method = confirmation.getMethod(methodName);
					if (method != null) {
						if (!isFirst)
							ap.append("else ")
						
						ap.append("if(operation.equals(\"");
						ap.append(operation.name);
						ap.append("\")) {\n\t")
						if (methodName == "when") {
							ap.append("return ");
						}
						ap.append("this." + methodName);
						ap.append(JavaHelper::toJavaIdentifier(operation.name)); 
						ap.append("();\n}")
					}
				}
			}
			if (!isFirst)
				ap.append("else ")
				
			if (methodName == "when") {
				ap.append("return ")
			}				
			ap.append("super.executeCommandConfirmation" + JavaHelper::toJavaIdentifier(methodName) + "(operation);");
		];
				
		it.members += newMethod;
	}

	def private inferImporterGetInstance(Definition e, JvmGenericType behaviourClazz) {
		var returnType = e.resolveImporterScopeType;
		e.createImporterMethod(behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf",
			e.toParameter("node", e.resolveNodeDocumentType));
		e.createImporterMethod(behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf",
			e.toParameter("schema", e.resolveSchemaType));
		e.createImporterMethod(behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf",
			e.toParameter("file", e.resolveBPIFileType));
		e.createImporterMethod(behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf",
			e.toParameter("url", e.resolveStringType));
		e.createImporterMethod(behaviourClazz, "doImportOf", returnType,
			e.toParameter("msg", e.resolveCustomerRequestType));
		e.createImporterMethod(behaviourClazz, "doImportOf", returnType,
			e.toParameter("msg", e.resolveContestantRequestType));
		e.createImporterMethod(behaviourClazz, "doImportOf", returnType,
			e.toParameter("msg", e.resolveProviderResponseType));
	}

	def private inferExporterGetInstance(Definition e, JvmGenericType behaviourClazz) {
		var returnType = e.resolve(this.classNameInferer.inferExporterScopeName(e));
		e.createImporterMethod(behaviourClazz, "doExportOf", returnType, "ExporterService", "prepareExportOf",
			e.toParameter("node", e.resolveNodeType));
	}

	def private inferNodeCreateInstance(Definition e, JvmGenericType behaviourClazz) {
		val returnType = e.resolve(this.classNameInferer.inferBehaviourName(e));

		var method = e.toMethod("createNew", returnType) [
			parameters += e.toParameter("parent", e.resolveNodeType);
			body = [ ap |
				ap.append("return (");
				ap.append(returnType.qualifiedName);
				ap.append(")org.monet.bpi.NodeService.create(");
				ap.append(this.classNameInferer.inferBehaviourName(e));
				ap.append(".class, parent);");
			];
		];
		method.setStatic(true);
		method.setVisibility(JvmVisibility::PUBLIC);
		behaviourClazz.members += method;
	}

	def private createImporterMethod(Definition e, JvmGenericType behaviourClazz, String methodName,
		JvmTypeReference returnType, String staticClassName, String staticClassMethod, JvmFormalParameter methodParam) {
		var method = e.toMethod(methodName, returnType) [
			parameters += methodParam;
			body = [ ap |
				ap.append("return (");
				ap.append(returnType.qualifiedName);
				ap.append(")org.monet.bpi.");
				ap.append(staticClassName);
				ap.append(".get(\"");
				ap.append(e.fullyQualifiedName.toString);
				ap.append("\").");
				ap.append(staticClassMethod);
				ap.append("(");
				ap.append(methodParam.name);
				ap.append(");");
			];
		];
		method.setStatic(true);
		method.setVisibility(JvmVisibility::PUBLIC);
		behaviourClazz.members += method;
	}

	def private createImporterMethod(Definition e, JvmGenericType behaviourClazz, String methodName,
		JvmTypeReference returnType, JvmFormalParameter methodParam) {
		var method = e.toMethod(methodName, returnType) [
			parameters += e.toParameter("key", e.resolveStringType);
			parameters += methodParam;
			body = [ ap |
				ap.append("return (");
				ap.append(returnType.qualifiedName);
				ap.append(")org.monet.bpi.ImporterService.get(\"");
				ap.append(e.fullyQualifiedName.toString);
				ap.append("\").prepareImportOf(key, ");
				ap.append(methodParam.name);
				ap.append(");");
			];
		];
		method.setStatic(true);
		method.setVisibility(JvmVisibility::PUBLIC);
		behaviourClazz.members += method;
	}

	def private inferSingleton(Definition e, JvmGenericType behaviourClazz) {
		var getInstanceMethod = e.toMethod("getInstance", references.createTypeRef(behaviourClazz).cloneWithProxies) [
			body = [ ap |
				ap.append("return (");
				ap.append(behaviourClazz.fullyQualifiedName.toString);
				ap.append(")org.monet.bpi.NodeService.locate(\"");
				ap.append(e.fullyQualifiedName.toString);
				ap.append("\");");
			];
		];
		getInstanceMethod.setStatic(true);
		getInstanceMethod.setVisibility(JvmVisibility::PUBLIC);
		behaviourClazz.members += getInstanceMethod;
	}

	def private inferOperation(Property e, JvmGenericType behaviourClazz) {
		val Method executeMethod = e.getMethod("execute");
		var onExecuteMethod = e.toMethod("on" + JavaHelper::toJavaIdentifier(e.name), e.resolveVoidType) [
			if (executeMethod != null) {
				body = executeMethod.body;
			} else {
				body = [];
			}
		];
		onExecuteMethod.setVisibility(JvmVisibility::PUBLIC);
		behaviourClazz.members += onExecuteMethod;
				
		inferOperationConfirmation(e, behaviourClazz);
	}
	
	def private inferOperationConfirmation(Property e, JvmGenericType behaviourClazz) {
		val confirmation = e.getProperty("confirmation");
		
		if (confirmation != null) {
			for (m : confirmation.methods) {
				var type = m.resolveVoidType;
				if (m.id == "when") type = m.resolveBooleanPrimitiveType;
				
				val onMethod = m.toMethod(m.id + JavaHelper::toJavaIdentifier(e.name), type) [
					body = m.body;
					for (p : m.params) {
						parameters += p.toParameter(p.name, p.parameterType);
					}
				]
		
				onMethod.setVisibility(JvmVisibility::PUBLIC);
				behaviourClazz.members += onMethod;
			}
		}
	}

	def private void inferIndexEntryAttributes(Property property, JvmGenericType behaviourClazz) {
		property.properties.forEach([p|inferIndexEntryAttribute(p, behaviourClazz);])
	}

	def private void inferIndexEntryAttribute(Property property, JvmGenericType behaviourClazz) {
		var String type = null;
		type = property.getAttribute("type").valueAsString;
		if (type == null)
			return;

		var generateParam = true;

		switch type {
			case "TERM": {
				type = "org.monet.bpi.types.Term";
			}
			case "LINK": {
				type = "org.monet.bpi.types.Link";
			}
			case "DATE": {
				type = "org.monet.bpi.types.Date";
			}
			case "CHECK": {
				type = "org.monet.bpi.types.CheckList";
			}
			case "STRING": {
				type = "java.lang.String";
			}
			case "REAL": {
				type = "org.monet.bpi.types.Number";
			}
			case "INTEGER": {
				type = "org.monet.bpi.types.Number";
			}
			case "BOOLEAN": {
				type = "java.lang.Boolean";
			}
			case "PICTURE": {
				type = "org.monet.bpi.types.Picture";
				generateParam = false;
			}
			default: {
				return;
			}
		}

		val finalType = type;

		var attributeNameCapitalized = JavaHelper::toJavaIdentifier(property.name);

		if (generateParam) {
			var field = property.toField(attributeNameCapitalized, property.resolveParamType)
			field.setStatic(true)
			field.setFinal(true)
			field.setVisibility(JvmVisibility::PUBLIC);
			field.setInitializer(
				[ ap |
					ap.append("new org.monet.bpi.Param(\"");
					ap.append(property.code.value);
					ap.append("\")");
				]);
			behaviourClazz.members += field
		}

		behaviourClazz.members += property.toMethod("get" + attributeNameCapitalized, property.resolve(finalType)) [
			body = [ ap |
				ap.append("return (");
				ap.append(finalType);
				ap.append(")this.getAttribute(\"");
				ap.append(property.code.value);
				ap.append("\");");
			]
		];
		behaviourClazz.members += property.toMethod("set" + attributeNameCapitalized, property.resolveVoidType) [
			body = [ ap |
				ap.append("this.setAttribute(\"");
				ap.append(property.code.value);
				ap.append("\", ");
				ap.append(property.name);
				ap.append(");");
			]
			parameters += property.toParameter(property.name, property.resolve(finalType));
		];
	}
}
