package org.monet.editor.dsl.jvmmodel.inferers

import org.monet.editor.dsl.jvmmodel.inferers.ModelInferer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.monetModelingLanguage.Definition
import com.google.inject.Inject
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.common.types.JvmTypeReference
import org.monet.editor.dsl.metamodel.MetaModelStructure
import org.monet.editor.dsl.metamodel.Item
import org.monet.metamodel.interfaces.HasClientBehaviour
import org.monet.metamodel.interfaces.HasBehaviourimport org.monet.editor.dsl.generator.JavaScriptGenerator
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.helper.JavaHelper
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.common.types.JvmGenericType
import org.monet.editor.dsl.jvmmodel.MMLExtensions
import org.monet.editor.dsl.helper.TypeRefCache
import java.util.ArrayListimport org.eclipse.xtext.common.types.JvmUnknownTypeReference

class DefinitionInferer extends ModelInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache
	
	@Inject private MetaModelStructure structure
	@Inject private ClassNameInferer classNameInferer
	@Inject private SchemaInferer schemaInferer
	@Inject private JobSchemaInferer jobSchemaInferer
	@Inject private DashboardInferer dashboardInferer
	@Inject private BehaviourInferer behaviourInferer
	@Inject private TaskInferer taskInferer
	
	def void infer(Definition definition, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		if(definition.name == null)
			return;
		val Item definitionItem = structure.getDefinition(definition.definitionType);
		if(definitionItem == null)
			return;
		
		val ArrayList<Property> childPropertiesWithClasses = new ArrayList<Property>();
		
		definition.properties.forEach([p | childPropertiesWithClasses.addAll(propertyInferer.inferClass(definition, p, definitionItem, acceptor, prelinkingPhase))]);
		definition.definitions.forEach([d | infer(d, acceptor, prelinkingPhase)]);
		
		if(definitionItem.getChild("schema") != null) {
			schemaInferer.infer(definition.schema, definition, acceptor);
		}
		
		if(definition.definitionType == "job" || definition.definitionType == "sensor") {
			jobSchemaInferer.infer(definition, acceptor);
		}
		
		if(definition.definitionType == "dashboard") {
			dashboardInferer.infer(definition, acceptor);
		}

		val parentBehaviourClass = definitionItem.parentBehaviourClass;
		if(parentBehaviourClass != null) {
			behaviourInferer.infer(definition, definitionItem, acceptor, prelinkingPhase);
			if(definition.definitionType == "service" || definition.definitionType == "activity") {
				taskInferer.inferClasses(definition, acceptor, prelinkingPhase);
			}
		}
		
		val definitionClassName = classNameInferer.inferDefinition(definition);
		acceptor.accept(definition.toClass(definitionClassName))
				.initializeLater
		[
			var parentDefinitionClass = definitionItem.parentDefinitionClass;
			
			members += inferDefinitionConstructorMethod(definition, it.simpleName, definitionItem, prelinkingPhase, true);
			members += inferDefinitionConstructorInitMethods(definition, it.simpleName, definitionItem, prelinkingPhase);
			members += definition.toStaticBlock()[
				body = [appender | 
					appender.append("org.monet.metamodel.Dictionary.getCurrentInstance().");
					
					var String parentName = "null";
					if (definition.replaceSuperType != null) {
						parentName = "\"" + definition.replaceSuperType.fullyQualifiedName.toString + "\"";
						appender.append("replace(");
					} else {
						if(definition.superType != null)
							parentName = "\"" + definition.superType.fullyQualifiedName.toString + "\"";
						appender.append("register(");
					} 
					
					appender.append(definitionClassName);
					appender.append(".class,\"");
					appender.append(definition.fullyQualifiedName.toString);
					appender.append("\",");
					appender.append(parentName);
					appender.append(");\n");
					
					if (definition.definitionType == "dashboard") {
						definition.getProperties("indicator").forEach([ indicator |
							var level = indicator.getProperty("level");
							if (level != null && level.hasProperty("secondary")) {
								var formulaClass = this.classNameInferer.inferIndicatorFormulaName(definition, indicator.name) + ".class";
								appender.append("org.monet.metamodel.Dictionary.getCurrentInstance().registerDashboardIndicatorFormulaClass(" + formulaClass + ",\"" + indicator.code.value + "\");\n");								
							}
						]);
						
						definition.getProperties("taxonomy").forEach([ taxonomy |
							if (taxonomy.hasProperty("explicit")) {
								var classifierClass = this.classNameInferer.inferTaxonomyClassifierName(definition, taxonomy.name) + ".class";
								appender.append("org.monet.metamodel.Dictionary.getCurrentInstance().registerDashboardTaxonomyClassifierClass(" + classifierClass + ",\"" + taxonomy.code.value + "\");\n");								
							}
						]);
					}
				];
			];
			
			var JvmTypeReference superType = null;
			if(definition.superType != null && definition.superType.fullyQualifiedName != null) {
				superType = definition.resolve(classNameInferer.inferDefinition(definition.superType)).cloneWithProxies;
			} else if(definition.replaceSuperType != null && definition.replaceSuperType.fullyQualifiedName != null) {
				superType = definition.resolve(classNameInferer.inferDefinition(definition.replaceSuperType)).cloneWithProxies;
			}
			if(superType == null)
				superType = definition.resolve(parentDefinitionClass).cloneWithProxies;
			if(superType != null)
				superTypes += superType;
				
			members += inferGetName(definition, definition.name);
			
			if(parentBehaviourClass != null) {
				var behaviourGetter = definition.toMethod("getBehaviourClass", definition.resolveClassType)[
					body = [ap | 
						ap.append("return ");
						ap.append(classNameInferer.inferBehaviourName(definition));
						ap.append(".class;");
					];
				];
				members += behaviourGetter;
				superTypes += definition.newTypeRef(typeof(HasBehaviour));
			}
			
			var clientBehaviourGetter = definition.toMethod("getClientBehaviour", definition.resolveStringType)[
				body = [ap | 
					ap.append("return \"");
					ap.append(JavaScriptGenerator::buildClientBehaviour(definition, null));
					ap.append("\";");
				];
			];
			members += clientBehaviourGetter;
			superTypes += definition.newTypeRef(typeof(HasClientBehaviour));
			
			if(definitionItem.getChild("schema") != null || definition.definitionType == "job" || definition.definitionType == "sensor") {
				val schemaTypeRef = definition.newTypeRef(classNameInferer.inferSchemaBehaviourName(definition));
				var schemaGetter = definition.toMethod("getSchemaClass", definition.resolveClassType)[
					body = [x | 
						x.append("return ");
						x.append(schemaTypeRef.qualifiedName.toString);
						x.append(".class;");
					];
				];
				members += schemaGetter;
				
				superTypes += definition.newTypeRef(typeof(org.monet.metamodel.interfaces.HasSchema))
			}
			
			if(definitionItem.getToken == "service" || definitionItem.getToken == "activity") {
				members += definition.toMethod("getSubBehaviorClass", definition.resolveClassType)[
					parameters += definition.toParameter("name", definition.resolveStringType);
					
					body = [ap | 
						ap.append("int hash = name.hashCode();\n ");
						ap.append("switch(hash) {\n ");
						
						definition.getProperties("customer", "contestants", "contest", "provider")
								  .forEach([p | 
								   		var String name;
								   		if(p.name == null) {
								   			name = p.id;
							   			} else {
							   				name = p.name;
							   			}
							   			if(p.id == "provider") {
							   				p.getProperties("internal", "external")
					   						 .forEach([px | px.toCaseLine(ap, px.id + p.name); ]);
							   			} else {
							   				p.toCaseLine(ap, name);
						   				}
										
									]);
						ap.append("default: return null;\n}");
						
					];
				];
			}
			
			if(definitionItem.getToken == "form" || definitionItem.getToken == "document") {
				definition.inferProperties(it);
				definition.inferMappings(it);
			}
			
			childPropertiesWithClasses.addAll(definition.properties);
			
			childPropertiesWithClasses.forEach([px | 
					  	if(px.name != null) {
						  	val fieldTypeName = classNameInferer.inferPropertyName(px);
						  	var fieldType = px.newTypeRef(fieldTypeName);
						  	if(fieldType != null && !(fieldType instanceof JvmUnknownTypeReference)) {
							  	var field = px.toField(JavaHelper::toJavaIdentifier(px.name), fieldType);
								field.setFinal(true);
								field.setInitializer([ap | 
									ap.append("new ");
									ap.append(fieldTypeName);
									ap.append("()");
								]);
								members += field;
							}
						}
					  ]);
		]
		
	}
	
	def private void inferProperties(Definition e, JvmGenericType jvmType) {
		val hasProperties = e.hasProperty("properties");
		if(hasProperties) {
			val propertiesClassName = this.classNameInferer.inferPropertiesName(e);
								    
			jvmType.members += e.toMethod("getPropertiesClass", e.resolveClassType)[
				body = [ap | 
					ap.append("return ");
					ap.append(propertiesClassName);
					ap.append(".class;");
				];
			];
			jvmType.superTypes += e.resolveHasPropertiesType;
		}
	}
	
	def private void inferMappings(Definition e, JvmGenericType jvmType) {
		jvmType.members += e.toMethod("getMappingClass", e.resolveClassType)[
			parameters += e.toParameter("code", e.resolveStringType);
			body = [ap | 
				
				ap.append("switch(code.hashCode()) {\n");
				
				e.getProperties("mapping")
				 .forEach([eMapping, i | 
	  				var eIndex = eMapping.getAttribute("index").valueAsDefinition;
	  				if(eIndex != null) {
	  					ap.append("case ");
	  					ap.append(String::valueOf(eIndex.code.value.hashCode));
	  					ap.append(":\nreturn ");
						ap.append(this.classNameInferer.inferMappingName(e, i));
	  					ap.append(".class;");
					}
		  		]);
				
				ap.append("\n}\nreturn super.getMappingClass(code);");
			];
		];
		jvmType.superTypes += e.resolveHasMappingsType;
	}
	
	def void toCaseLine(Property p, ITreeAppendable  ap, String name) {
		ap.append("case ");
		ap.append(String::valueOf(name.hashCode));
		ap.append(" :\nreturn ");
		ap.append(classNameInferer.inferPropertyBehavior(p));
		ap.append(".class;\n");
	}
		
}