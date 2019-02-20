package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import java.util.ArrayList
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.JvmUnknownTypeReference
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.JavaHelper
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.jvmmodel.MMLExtensions
import org.monet.editor.dsl.metamodel.Item
import org.monet.editor.dsl.monetModelingLanguage.Attribute
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.JvmFormalParameter

class PropertyInferer extends ModelInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache

	@Inject private AttributeInferer attributeInferer
	@Inject private ClassNameInferer classNameInferer

	static class Index {
		private int value;

		def Index() {
			value = 0;
		}

		def void increment() {
			value = value + 1;
		}

		def int value() {
			return value;
		}

		def void setValue(int newValue) {
			value = newValue;
		}
	}

	def void infer(EObject e, Property p, ITreeAppendable x, Item metacontainerItem, String context, Index index, boolean prelinkingPhase, boolean separateWithInitMethods, boolean checkInlineProperties) {
		val Item itemProperty = metacontainerItem.getChild(p.id);
		if (itemProperty == null)
			return;
		var String value = "";
		var fullyQualifiedName = p.fullyQualifiedName;

		if (fullyQualifiedName == null || itemProperty.valueTypeQualifiedName == null) {
			var String typeName = null;
			if (itemProperty.valueTypeQualifiedName != null) {
				typeName = itemProperty.valueTypeQualifiedName;
			} else if (metacontainerItem.valueTypeQualifiedName != null) {
				typeName = metacontainerItem.valueTypeQualifiedName + "." +
					JavaHelper::toJavaIdentifier(itemProperty.getName());
			} else {
				typeName = itemProperty.getFullyQualifiedName();
			}

			var String varName = JavaHelper::toAttributeJavaIdentifier(itemProperty.getName()) + String::valueOf(index.value());
			value = varName;
			if (!((itemProperty.getToken.startsWith("is-") && itemProperty.items.size == 0) ||
				itemProperty.getToken.startsWith("allow-"))) {
				x.append(typeName);
				x.append(" ");
				x.append(varName);
				x.append(" = new ");
				x.append(typeName);
				x.append("();\n");

				for (childAttr : p.features) {
					switch childAttr {
						Attribute: {
							attributeInferer.infer(childAttr, x, itemProperty, varName, true, prelinkingPhase);
						}
					}
				}
			}
			if (p.code != null) {
				x.append(value);
				x.append(".setCode(\"");
				x.append(p.code.value);
				x.append("\");\n");
			}
			if (p.name != null) {
				x.append(value);
				x.append(".setName(\"");
				x.append(p.name);
				x.append("\");\n");
			}

			for (childProp : p.features) {
				switch childProp {
					Property: {
						val Item childItemProperty = itemProperty.getChild(childProp.id);
						
						if (separateWithInitMethods && renderWithInitMethod(childProp, childItemProperty, checkInlineProperties)) {
							var prefix = "";
							var code = childItemProperty.name;

							if (childProp.code != null) {
								code = childProp.code.value;
							}

							if (!value.equals("this")) {
								prefix = JavaHelper::toJavaIdentifier(value);
							}

							var methodName = "init" + /*prefix + JavaHelper::toJavaIdentifier(code) +*/ index.value();
							x.append(methodName + "(" + varName + ");\n");
							index.setValue(index.value()+propertyInferer.getDescendantsCount(e, childProp, itemProperty, checkInlineProperties));
							index.increment();
						} else {
							infer(e, childProp, x, itemProperty, value, index, prelinkingPhase, separateWithInitMethods, checkInlineProperties);
							
							val fullyChildQualifiedName = childProp.getFullyQualifiedName();							
							if (!(childItemProperty.getValueTypeQualifiedName() != null && fullyChildQualifiedName != null)) {
								index.increment();
							}
						}											
					}					
				}	
			}
		} else {
			value = JavaHelper::toJavaIdentifier(fullyQualifiedName.lastSegment);
		}

		x.append(context);
		if (itemProperty.multiple && (itemProperty.valueTypeQualifiedName != null || itemProperty.hasName)) {
			x.append(".add")
		} else if (itemProperty.multiple) {
			x.append(".get");
		} else {
			x.append(".set");
		}

		if (itemProperty.valueTypeQualifiedName != null || itemProperty.hasName) {
			var String propertyName;
			if (itemProperty.valueTypeQualifiedName != null) {
				var int dollarIndex = itemProperty.valueTypeQualifiedName.lastIndexOf(".");
				if (dollarIndex < 0)
					dollarIndex = 0;
				propertyName = itemProperty.valueTypeQualifiedName.substring(dollarIndex + 1);
			} else {
				propertyName = itemProperty.getToken();
			}

			x.append(JavaHelper::toJavaIdentifier(propertyName));
			x.append("(");
			x.append(value);
			x.append(")");
		} else if (itemProperty.multiple) {
			x.append(JavaHelper::toJavaIdentifier(p.id));
			x.append("List().add(");
			x.append(value);
			x.append(")");
		} else {
			x.append(JavaHelper::toJavaIdentifier(p.id));
			x.append("(");
			if ((itemProperty.getToken.startsWith("is-") && itemProperty.items.size == 0) ||
				itemProperty.getToken.startsWith("allow-")) {
				x.append("true");
			} else {
				x.append(value);
			}
			x.append(")");
		}
		x.append(";\n");
	}

	def int getDescendantsCount(EObject e, Property p, Item metacontainerItem, boolean checkInlineProperties) {
		val Item itemProperty = metacontainerItem.getChild(p.id);
		var int result = 0;

		if (itemProperty == null)
			return result;

		for (childProp : p.features) {
			switch childProp {
				Property: {
					var Item childItemProperty = itemProperty.getChild(childProp.id);
					
					if (renderWithInitMethod(childProp, childItemProperty, checkInlineProperties)) {
						result = result+1;
						result = result + getDescendantsCount(e, childProp, itemProperty, checkInlineProperties);	
					}
				}				
			}
			
		}

		return result;
	}
		
	def boolean renderWithInitMethod(Property p, Item itemProperty, boolean checkInlineProperties) {		
		val fullyQualifiedName = p.getFullyQualifiedName();
		
		if (checkInlineProperties && itemProperty.getValueTypeQualifiedName() != null && fullyQualifiedName != null) {
			return false;				
		}

		if (checkInlineProperties && itemProperty.getValueTypeQualifiedName() == null) {
			return true;
		}

		if (checkInlineProperties && itemProperty.getValueTypeQualifiedName() != null && !itemProperty.hasName) {
			return true;
		}
		
		if (checkInlineProperties) {
			if (isPlaceProperty(itemProperty)) {
				return true;
			}
			return false;
		}
			
		return true;
	}
	
	def boolean isPlaceProperty(Item itemProperty) {
		var Item parent = itemProperty.parent;
		
		if (parent == null)
			return false;
		
		if (parent.token == "place")
			return true;
		
		return isPlaceProperty(parent);
	}

	def Iterable<JvmOperation> inferMethods(EObject e, Property p, Item metacontainerItem, String context, Index index,
		boolean prelinkingPhase) {
		var ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();
		val Item itemProperty = metacontainerItem.getChild(p.id);

		if (itemProperty == null)
			return methods;
			
		if (!renderWithInitMethod(p, itemProperty, true))
			return methods;

		var fullyQualifiedName = p.fullyQualifiedName;

		if (fullyQualifiedName == null || itemProperty.valueTypeQualifiedName == null) {
			var prefix = "";
			var code = itemProperty.name;

			if (p.code != null) {
				code = p.code.value;
			}

			if (!context.equals("this")) {
				prefix = JavaHelper::toJavaIdentifier(context);
			}

			var methodName = "init" + /*prefix + JavaHelper::toJavaIdentifier(code) +*/ index.value();
			val currentIndex = new Index();
			currentIndex.setValue(index.value() + 1);
			val valueVal = JavaHelper::toAttributeJavaIdentifier(itemProperty.getName()) + String::valueOf(currentIndex.value());

			methods += e.toMethod(methodName, e.resolveVoidType) [
				visibility = JvmVisibility::PRIVATE;
				if (!context.equals("this")) {
					var String typeName = null;

					if (metacontainerItem.valueTypeQualifiedName != null) {
						typeName = metacontainerItem.valueTypeQualifiedName;
					} else {
						typeName = metacontainerItem.fullyQualifiedName;
					}

					var JvmFormalParameter parameter = e.toParameter(context, e.newTypeRef(typeName));
					parameters += parameter;
				}
				body = [ ap |
					infer(p, ap, metacontainerItem, context, currentIndex, prelinkingPhase, true, true);
				];
			];
			index.increment();

			for (childProp : p.features) {
				switch childProp {
					Property: {
						methods += inferMethods(e, childProp, itemProperty, valueVal, index, prelinkingPhase);
					}
				}
			}
		}

		return methods;
	}

	/* 	
		def Iterable<JvmOperation> inferMethods(EObject e, Property p, ITreeAppendable x, Item metacontainerItem, String context, Integer index, boolean prelinkingPhase) {
		var ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();

		for (childProp : p.features) {
			switch childProp {
				Property: {
					var methodName = "init" + JavaHelper::toJavaIdentifier(p.name) + index +
						JavaHelper::toJavaIdentifier(childProp.name) + "();\n";
					x.append(methodName);

					methods += e.toMethod(methodName, e.resolveVoidType) [
						visibility = JvmVisibility::PRIVATE;
						body = [ ap |
							this.infer(childProp, ap, metacontainerItem, "this", 0, prelinkingPhase);
						];
					];
				}
			}
		}
		
		return methods;
	}
	*/
	def Iterable<Property> inferClass(Definition d, Property p, Item parentItem, IJvmDeclaredTypeAcceptor acceptor,
		boolean prelinkingPhase) {
		val Item propertyItem = parentItem.getChild(p.id);
		val ArrayList<Property> childPropertiesWithClasses = new ArrayList<Property>();

		if (propertyItem == null)
			return new ArrayList<Property>();
		val fullyQualifiedName = p.fullyQualifiedName;

		p.properties.forEach(
			[px|childPropertiesWithClasses.addAll(inferClass(d, px, propertyItem, acceptor, prelinkingPhase))])

		if (propertyItem.getValueTypeQualifiedName() != null && fullyQualifiedName != null) {
			val name = fullyQualifiedName.lastSegment;
			val propertyClazzName = classNameInferer.inferPropertyName(p);

			acceptor.accept(p.toClass(propertyClazzName)).initializeLater [
				members += inferDefinitionConstructorMethod(p, it.simpleName, propertyItem, prelinkingPhase, true);
				members += inferDefinitionConstructorInitMethods(p, it.simpleName, propertyItem, prelinkingPhase);
				var JvmTypeReference superType = null;
				if (!prelinkingPhase) {
					if (d.superType != null && d.superType.fullyQualifiedName != null) {
						val parentProperties = d.superType.getProperties(p.id);
						var found = false;

						for (parentProperty : parentProperties)
							if (parentProperty.name == p.name)
								found = true;

						if (found) {
							var relativeQN = p.fullyQualifiedName.skipFirst(d.fullyQualifiedName.getSegmentCount());
							var superTypeName = XtendHelper::convertQualifiedNameToGenName(
								d.superType.fullyQualifiedName.append(relativeQN)).toString + "Property";
							superType = p.newTypeRef(superTypeName);
						}
					}
				}
				if (superType == null)
					superType = p.newTypeRef(propertyItem.valueTypeQualifiedName);
				if (superType != null)
					superTypes += superType;
				if (p.id == "field-composite") {
					var behaviourGetter = p.toMethod("getBehaviourClass", p.resolveClassType) [
						body = [ ap |
							ap.append("return ");
							ap.append(XtendHelper::convertQualifiedNameToGenName(fullyQualifiedName));
							ap.append(".class;");
						];
					];
					members += behaviourGetter;
					superTypes += p.resolveHasBehaviourType
				}
				if (p.name != null) {
					members += inferGetName(p, name);
				}
				childPropertiesWithClasses.addAll(p.properties);
				childPropertiesWithClasses.forEach(
					[ px |
						if (px.name != null) {
							val fieldTypeName = classNameInferer.inferPropertyName(px);
							var fieldType = px.newTypeRef(fieldTypeName);
							if (fieldType != null && !(fieldType instanceof JvmUnknownTypeReference)) {
								var field = px.toField(JavaHelper::toJavaIdentifier(px.name), fieldType);
								field.setFinal(true);
								field.setInitializer(
									[ ap |
										ap.append("new ");
										ap.append(fieldTypeName);
										ap.append("()");
									]);
								members += field;
							}
						}
					]);
			];
			return new ArrayList<Property>();
		} else {
			childPropertiesWithClasses.addAll(
				p.properties.filter(
					[ px |
						val Item pxItem = propertyItem.getChild(px.id);
						val pxfullyQualifiedName = px.fullyQualifiedName;
						return (pxItem != null && pxItem.getValueTypeQualifiedName() != null &&
							pxfullyQualifiedName != null);
					]));
			return childPropertiesWithClasses;
		}
	}

}
