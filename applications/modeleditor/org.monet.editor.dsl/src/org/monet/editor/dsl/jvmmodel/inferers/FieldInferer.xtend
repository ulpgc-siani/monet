package org.monet.editor.dsl.jvmmodel.inferers

import org.eclipse.xtext.common.types.JvmTypeReference
import com.google.inject.Inject
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.helper.XtendHelper
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.common.types.util.Primitives
import org.eclipse.xtext.common.types.util.TypeReferences
import org.monet.bpi.FieldNode


import org.monet.bpi.FieldBoolean
import org.monet.bpi.FieldCheck
import org.monet.bpi.FieldDate
import org.monet.bpi.FieldFile
import org.monet.bpi.FieldLink
import org.monet.bpi.FieldMemo
import org.monet.bpi.FieldNumber
import org.monet.bpi.FieldPicture
import org.monet.bpi.FieldSelect
import org.monet.bpi.FieldSerial
import org.monet.bpi.FieldSummation
import org.monet.bpi.FieldText
import org.monet.editor.dsl.jvmmodel.MMLExtensionsimport org.monet.bpi.java.FieldCompositeImpl
import org.monet.bpi.FieldComposite

class FieldInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache

	@Inject private Primitives primitives
	@Inject private TypeReferences references

	def void infer(Definition d, Property p, JvmGenericType behaviourClazz) {
		if(p.name == null)
			return;
		val fieldName = org::monet::editor::dsl::helper::JavaHelper::toJavaIdentifier(p.name);
		val boolean isMultiple = p.hasProperty("is-multiple");
		val JvmTypeReference fieldValueType = inferValueType(p);
		val JvmTypeReference fieldType = inferType(d, p, fieldValueType);
		
		//Infer getXXXField()
		var getField = p.toMethod("get" + fieldName + "Field", fieldType) [
			body = [ap | 
				ap.append("return (("); 
				ap.append(fieldType.qualifiedName); 
				ap.append(")this.getField(\""); 
				ap.append(d.name); 
				ap.append("\", \""); 
				ap.append(p.name); 
				ap.append("\"));"); 
			]
			visibility = JvmVisibility::PUBLIC;
		];
		behaviourClazz.members += getField;
		
		if(p.id != "field-composite") {
			//Infer getXXX()
			var JvmTypeReference getterReturnType = fieldValueType;
			if(isMultiple)
				 getterReturnType = p.resolveArrayListType(primitives.asWrapperTypeIfPrimitive(fieldValueType)); 
			getField = p.toMethod("get" + fieldName, getterReturnType) [
				body = [ap |  
					ap.append("return ((");
					ap.append(fieldType.qualifiedName);
					ap.append(")this.getField(\"");
					ap.append(d.name);
					ap.append("\", \"");
					ap.append(p.name);
					ap.append("\")).get");
					if(isMultiple)
						ap.append("All");
					ap.append("();");
				]
				visibility = JvmVisibility::PUBLIC;
			];
			behaviourClazz.members += getField;
			
			if(p.id == "field-link" || p.id == "field-node") {
				//Term getXXXAsTerm()
				var returnType = p.resolveTermType;
				if(isMultiple)
					returnType = p.resolveArrayListType(returnType);
				getField = p.toMethod("get" + fieldName + "AsTerm", returnType) [
					body = [ap |  
						ap.append("return ((");
						ap.append(fieldType.qualifiedName);
						ap.append(")this.getField(\"");
						ap.append(d.name);
						ap.append("\", \"");
						ap.append(p.name);
						ap.append("\")).get");
						if(isMultiple)
							ap.append("All");
						ap.append("AsTerm();");
					]
					visibility = JvmVisibility::PUBLIC;
				];
				behaviourClazz.members += getField;
			}
			if(!isMultiple) {
				//setXXX()			
				var setField = p.toMethod("set" + fieldName, p.resolveVoidType) [
					body = [ap |  
						ap.append("this.get");
						ap.append(fieldName);
						ap.append("Field().set(value);");
					]
					visibility = JvmVisibility::PUBLIC;
					parameters += p.toParameter("value", fieldValueType);
				];
				behaviourClazz.members += setField;
				if(p.id=="field-number") {
					setField = p.toMethod("set" + fieldName, p.resolveVoidType) [
						body = [ap |  
							ap.append("this.get");
							ap.append(fieldName);
							ap.append("Field().set(new org.monet.bpi.types.Number(value));");
						]
						visibility = JvmVisibility::PUBLIC;
						parameters += p.toParameter("value", p.resolveDoublePrimitiveType);
					];
					behaviourClazz.members += setField;
				}
			}
		}
	}
	
	def JvmTypeReference inferType(Definition d, Property p, JvmTypeReference fieldValueType) {
		var JvmTypeReference fieldTypeReference;
		var Class<?> baseType = null;
		var JvmTypeReference params = null;
		switch p.id {
			case "field-boolean": { baseType = typeof(FieldBoolean); }
			case "field-check": { baseType = typeof(FieldCheck); }
			case "field-date": { baseType = typeof(FieldDate); }
			case "field-file": { baseType = typeof(FieldFile); }
			case "field-link": { baseType = typeof(FieldLink); }
			case "field-memo": { baseType = typeof(FieldMemo); }
			case "field-node": { baseType = typeof(FieldNode); }
			case "field-number": { baseType = typeof(FieldNumber); }
			case "field-picture": { baseType = typeof(FieldPicture); }
			case "field-select": { baseType = typeof(FieldSelect); }
			case "field-serial": { baseType = typeof(FieldSerial); }
			case "field-text": { baseType = typeof(FieldText); }
			case "field-summation": { baseType = typeof(FieldSummation); }
			case "field-composite": { 
				baseType = typeof(FieldComposite);
			}
				/*
				var superType = findCompositeInParents(d, p.name);
				if(superType == null) {
					fieldTypeReference = p.resolve(XtendHelper::convertQualifiedNameToGenName(p.fullyQualifiedName));
				} else {
					fieldTypeReference = superType;
				}
			}
				*/
		}
		if(fieldTypeReference == null)
			fieldTypeReference = p.resolve(baseType, XtendHelper::createParametersArray(params));
		
		if(p.hasProperty("is-multiple")) {
			return p.resolveFieldMultipleType(XtendHelper::createParametersArray(fieldTypeReference, primitives.asWrapperTypeIfPrimitive(fieldValueType)));
		}
		
		return fieldTypeReference;
	}
	
	def void inferTypeClass(Definition d, Property p, IJvmDeclaredTypeAcceptor acceptor) {
		switch p.id {
			case "field-composite": {
				acceptor.accept(p.toClass(XtendHelper::convertQualifiedNameToGenName(p.fullyQualifiedName))).initializeLater[
					members += p.toConstructor() [ visibility = JvmVisibility::PUBLIC; ];
					
					var superType = findCompositeInParents(d, p.name);	
					superTypes += p.newTypeRef(typeof(FieldComposite));
					if(superType != null) {
						superTypes += superType;
					} else {
						superTypes += p.newTypeRef(typeof(FieldCompositeImpl));
					}
					
					p.properties.filter([px | px.id != null && px.id.startsWith("field")]).forEach([px | infer(d, px, it)]);
				];
				
				p.properties.filter([px | px.id != null && px.id.startsWith("field")]).forEach([px | inferTypeClass(d, px, acceptor)]);
			}
		}
	}
	
	def private JvmTypeReference findCompositeInParents(Definition d, String name) {
		if(d.superType == null)
			return null;
		var composite = d.superType.getProperties("field-composite")
					   .map([p | findCompositeRecursive(p, name)])
					   .filter([p | p != null])
					   .head;
		if(composite == null)
			return findCompositeInParents(d.superType, name);

		return references.getTypeForName(XtendHelper::convertQualifiedNameToGenName(composite.fullyQualifiedName), composite);
	}
	
	def private Property findCompositeRecursive(Property composite, String name) {
		if(composite.name == name) {
			return composite;
		} else {
			return composite.getProperties("field-composite")
						  .map([p | findCompositeRecursive(p, name)]).head;
		}
	}

	def JvmTypeReference inferValueType(Property p) {
		var Class<?> type = null;
		switch p.id {
			case "field-boolean": { type = typeof(boolean); }
			case "field-check": { type = typeof(org.monet.bpi.types.CheckList); }
			case "field-date": { type = typeof(org.monet.bpi.types.Date); }
			case "field-file": { type = typeof(org.monet.bpi.types.File); }
			case "field-link": { type = typeof(org.monet.bpi.types.Link); }
			case "field-memo": { type = typeof(String); }
			case "field-node": { type = typeof(org.monet.bpi.types.Link); }
			case "field-number": { type = typeof(org.monet.bpi.types.Number); }
			case "field-summation": { type = typeof(org.monet.bpi.types.Number); }
			case "field-picture": { type = typeof(org.monet.bpi.types.Picture); }
			case "field-select": { type = typeof(org.monet.bpi.types.Term); }
			case "field-serial": { type = typeof(String); }
			case "field-text": { type = typeof(String); }
			default : { type = typeof(Void); }		
		}
		return p.resolve(type);
	}
	
}