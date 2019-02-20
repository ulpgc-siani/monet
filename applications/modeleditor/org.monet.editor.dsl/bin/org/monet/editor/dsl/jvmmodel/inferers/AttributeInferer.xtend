package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import java.util.Set
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.monet.editor.core.ProjectHelper
import org.monet.editor.dsl.helper.JavaHelper
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.metamodel.Item
import org.monet.editor.dsl.monetModelingLanguage.Attribute
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral
import org.monet.editor.dsl.monetModelingLanguage.FloatLiteral
import org.monet.editor.dsl.monetModelingLanguage.IntLiteral
import org.monet.editor.dsl.monetModelingLanguage.LocalizedText
import org.monet.editor.dsl.monetModelingLanguage.Resource
import org.monet.editor.dsl.monetModelingLanguage.StringLiteral
import org.monet.editor.dsl.monetModelingLanguage.TimeLiteral
import org.monet.editor.dsl.monetModelingLanguage.XTReference

class AttributeInferer {

	@Inject extension IQualifiedNameProvider

	@Inject private TypeReferences references

	def void infer(Attribute a, ITreeAppendable x, Item parentMetadefinition, String context, boolean useSetter,
		boolean prelinkingPhase) {
		var v = a.value;
		var Item metaattribute = parentMetadefinition.getChild(a.id);
		if (metaattribute.valueTypeQualifiedName.startsWith("Expression:"))
			return;

		x.append(context);
		if (metaattribute.multiple) {
			x.append(".get");
			x.append(JavaHelper::toJavaIdentifier(a.id));
			x.append("().add(");
		} else if (useSetter) {
			x.append(".set");
			x.append(JavaHelper::toJavaIdentifier(a.id));
			x.append("(");
		} else {
			x.append("._");
			x.append(JavaHelper::toAttributeJavaIdentifier(a.id));
			x.append(" = ");
		}
		if (metaattribute == null)
			return;
		switch v {
			LocalizedText: {
				var project = XtendHelper::getIProject(v);
				var packageBase = ProjectHelper::getPackageBase(project);
				var languageClass = references.getTypeForName(packageBase + ".Language", v);
				if (languageClass == null) {
					x.append("null/** No default language file found */");
				} else {
					x.append(languageClass.qualifiedName);
					x.append(".");
					x.append(v.value);
				}
			}
			Resource: {
				var project = XtendHelper::getIProject(v);
				var packageBase = ProjectHelper::getPackageBase(project);

				x.append(packageBase);
				x.append(".Resources.");
				x.append(JavaHelper::toJavaIdentifier(v.type));
				x.append(".");
				x.append(JavaHelper::toJavaIdentifier(v.value));
			}
			StringLiteral: {
				var isURI = metaattribute.valueTypeQualifiedName == "java.net.URI";
				if(isURI) x.append("java.net.URI.create(");

				x.append("\"");
				x.append(JavaHelper::toStringLiteral(v.value));
				x.append("\"");

				if(isURI) x.append(")");
			}
			TimeLiteral: {
				x.append("new org.monet.metamodel.internal.Time(\"");
				x.append(v.value);
				x.append("\")");
			}
			IntLiteral: {
				x.append("(long) ");
				if(v.negative) x.append("-");
				x.append(String::valueOf(v.value));
			}
			FloatLiteral: {
				if(v.negative) x.append("-");
				x.append(String::valueOf(v.value));
				x.append("F");
			}
			DoubleLiteral: {
				if(v.negative) x.append("-");
				x.append(String::valueOf(v.value));
			}
			EnumLiteral: {
				x.append(metaattribute.valueTypeQualifiedName.replaceAll("\\$", ".") + "." + v.value);
			}
			XTReference: {
				if (prelinkingPhase) {
					x.append("null/*Prelinking Phase!*/");
				} else {
					var refName = v.value.fullyQualifiedName;
					if (refName != null) {
						x.append("new org.monet.metamodel.internal.Ref(\"");
						var definitionTypeReference = this.references.getTypeForName(metaattribute.valueTypeQualifiedName, a);
						var Class<?> definitionTypeClazz = null;
						
						if (definitionTypeReference.getType() != null) {
							var String identifier = definitionTypeReference.getType().getIdentifier();
							if (identifier != null && !identifier.isEmpty()) {						
								definitionTypeClazz = Class.forName(definitionTypeReference.getType().getIdentifier());
							}
						}
						
						if (definitionTypeClazz != null && org.monet.metamodel.Definition.isAssignableFrom(definitionTypeClazz)) {
							x.append(refName.toString);
						} else {
							var EObject parentDefinition = v.value;
							while (!(parentDefinition instanceof Definition)) {
								parentDefinition = parentDefinition.eContainer;
							}
							x.append(refName.lastSegment);
							x.append("\",\"");
							x.append(parentDefinition.fullyQualifiedName.toString);
							x.append("\",\"");
							x.append(refName.toString);
						}
						x.append("\")");
					} else {
						x.append("null/*Ref not compiled!*/");
					}
				}
			}
		}
		if (metaattribute.multiple || useSetter)
			x.append(")");
		x.append(";\n");
	}
	
	private static class MonetTypeReferences {
		
	}

}
