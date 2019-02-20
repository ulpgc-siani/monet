package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import java.util.ArrayList
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmConstructor
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.metamodel.Item
import org.monet.editor.dsl.metamodel.MetaModelStructure
import org.monet.editor.dsl.monetModelingLanguage.Attribute
import org.monet.editor.dsl.monetModelingLanguage.Define
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel
import org.monet.editor.dsl.monetModelingLanguage.Property

class ModelInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension TypeRefCache

	@Inject protected MetaModelStructure structure
	@Inject private AttributeInferer attributeInferer
	@Inject protected PropertyInferer propertyInferer

	def JvmConstructor inferDefinitionConstructorMethod(EObject e, String className, Item contextItem,
		boolean prelinkingPhase, boolean separateWithInitMethods) {
		var constructor = e.toConstructor() [
			visibility = JvmVisibility::PUBLIC;
			simpleName = className;
		];
		if (!prelinkingPhase) {
			val String code = this.getCode(e, prelinkingPhase);
			val String name = this.getName(e, prelinkingPhase);
			val String parent = this.getParent(e, prelinkingPhase);
			val Iterable<EObject> features = this.getFeatures(e, prelinkingPhase);

			constructor.setBody(
				[ x |
					x.append("super();");
					if (code != null) {
						x.append("this._code = \"");
						x.append(code);
						x.append("\";\n");
					}
					if (name != null) {
						x.append("this._name = \"");
						x.append(name);
						x.append("\";\n");
					}
					if (parent != null) {
						x.append("this._parent = \"");
						x.append(parent);
						x.append("\";\n");
					}
					if (e instanceof Definition) {
						if ((e as Definition).isAbstract()) {
							x.append("this._isAbstract = new IsAbstract();")
						} else {
							x.append("this._isAbstract = null;")
						}
					}
					var index = new PropertyInferer.Index();
					var PropertyInferer.Index inlineIndex = new PropertyInferer.Index();
					for (a : features) {
						switch a {
							Attribute: {
								this.attributeInferer.infer(a, x, contextItem, "this", false, prelinkingPhase);
							}
							Property: {
								var Item itemProperty = contextItem.getChild(a.id);
								
								if (itemProperty != null) {
									if (separateWithInitMethods && propertyInferer.renderWithInitMethod(a, itemProperty, true)) {
										var methodName = "init" + /*JavaHelper::toJavaIdentifier(itemProperty.name) +*/ index.value();
										x.append(methodName + "();\n");
										index.setValue(index.value() + propertyInferer.getDescendantsCount(e, a, contextItem, true));
										index.increment();
									} else {
										this.propertyInferer.infer(e, a, x, contextItem, "this", inlineIndex, prelinkingPhase, separateWithInitMethods, true);
										inlineIndex.increment();
									}									
								}
								
							}
							Define: {
								x.append("this.defineMap.put(\"");
								x.append(a.name);
								x.append("\",\"");
								x.append(a.value);
								x.append("\");");
							}
						}
					}
				]);
		}
		return constructor;
	}

	def Iterable<JvmOperation> inferDefinitionConstructorInitMethods(EObject e, String className, Item contextItem,
		boolean prelinkingPhase) {
		var Iterable<EObject> features = this.getFeatures(e, prelinkingPhase);
		var ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();
		var index = new PropertyInferer.Index();

		for (a : features) {
			switch a {
				Property: {
					methods += this.propertyInferer.inferMethods(e, a, contextItem, "this", index, prelinkingPhase);
				}
			}
		}

		return methods;
	}

	/*
		def Iterable<JvmOperation> inferDefinitionConstructorInitMethods(EObject e, String className, Item contextItem, boolean prelinkingPhase) {
		var Iterable<EObject> features = this.getFeatures(e, prelinkingPhase);
		val ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();
		var Integer i = 0;
		
		for (a : features) {
			switch a {
				Property: {
					var Item itemProperty = contextItem.getChild(a.id);
					if (itemProperty != null) {
						var methodName = "init" + JavaHelper::toJavaIdentifier(itemProperty.name) + i;
						
						methods += e.toMethod(methodName, e.resolveVoidType) [
							visibility = JvmVisibility::PRIVATE;
							body = [ ap |								
								this.propertyInferer.inferMethods(e, a, ap, contextItem, "this", 0, prelinkingPhase);
							];
						];
						i = i + 1;
					}
				}
			}
		}

		return methods;
	}
	*/
	def JvmOperation inferGetName(EObject e, String name) {
		var nameMethod = e.toMethod("static_getName", e.resolveStringType) [
			body = [ ap |
				ap.append("return \"");
				ap.append(name);
				ap.append("\";");
			]
		];
		nameMethod.setStatic(true);
		nameMethod.setVisibility(JvmVisibility::PUBLIC);
		return nameMethod;
	}

	def String getName(EObject e, boolean prelinkingPhase) {
		var String name = null;

		if (e instanceof Definition) {
			var Definition d = e as Definition;
			name = d.fullyQualifiedName.toString;
		} else if (e instanceof Property) {
			var Property p = e as Property;
			if (p.name != null)
				name = p.name;
		} else if (e instanceof DistributionModel) {
			var s = e as DistributionModel;
			name = s.name;
		} else if (e instanceof ProjectModel) {
			var m = e as ProjectModel;
			name = m.name;
		}

		return name;
	}

	def String getCode(EObject e, boolean prelinkingPhase) {
		var String code = null;

		if (e instanceof Definition) {
			var Definition d = e as Definition;
			if (d.code != null)
				code = d.code.value;
		} else if (e instanceof Property) {
			var Property p = e as Property;
			if (p.code != null)
				code = p.code.value;
		}

		return code;
	}

	def String getParent(EObject e, boolean prelinkingPhase) {
		var String parent = null;

		if (e instanceof Definition) {
			var Definition d = e as Definition;
			if (!prelinkingPhase && d != null && d.superType != null && d.superType.fullyQualifiedName != null)
				parent = d.superType.fullyQualifiedName.toString;
		}

		return parent;
	}

	def Iterable<EObject> getFeatures(EObject e, boolean prelinkingPhase) {
		var Iterable<EObject> features = null;

		if (e instanceof Definition) {
			var Definition d = e as Definition;
			features = d.features.filter(typeof(EObject));
		} else if (e instanceof Property) {
			var Property p = e as Property;
			features = p.features.filter(typeof(EObject));
		} else if (e instanceof DistributionModel) {
			var s = e as DistributionModel;
			features = s.features.filter(typeof(EObject));
		} else if (e instanceof ProjectModel) {
			var m = e as ProjectModel;
			features = m.features.filter(typeof(EObject));
		}

		return features;
	}

}
