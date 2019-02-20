package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import org.eclipse.emf.common.util.EList
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.access.impl.ClassURIHelper
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.bpi.types.CheckList
import org.monet.bpi.types.Date
import org.monet.bpi.types.Location
import org.monet.bpi.types.Number
import org.monet.bpi.types.PictureLink
import org.monet.bpi.types.Term
import org.monet.bpi.types.VideoLink
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.jvmmodel.MMLExtensions
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.monetModelingLanguage.SchemaFeature
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

class JobSchemaInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache
	
	@Inject private TypeReferences references
	@Inject private ClassURIHelper uriHelper
	@Inject private ClassNameInferer classNameInferer
	
	def void infer(Definition definition, IJvmDeclaredTypeAcceptor acceptor) {
		val schemaClass = definition.toClass(this.classNameInferer.inferSchemaBehaviourName(definition));
		
		definition.getProperties("step")
				  .forEach([ss | inferStepClass(ss, acceptor, schemaClass)]);
		
		acceptor.accept(schemaClass).initializeLater[
			var hasSuper = false;
			
			inferMethods(it);

			if(definition.superType != null && definition.superType.fullyQualifiedName != null) {
				var parentSchemaType = references.getTypeForName(this.classNameInferer.inferSchemaBehaviourName(definition.superType), definition).cloneWithProxies;
				hasSuper = parentSchemaType != null;
				it.superTypes += parentSchemaType;
			}
			if(!hasSuper)
				it.superTypes += definition.resolveSchemaType.cloneWithProxies;
		
			var annType = typeof(Root);
			var ann = definition.toAnnotation(annType);
			var rootAnnotationValue = XtendHelper::createAnnotationValue("name", "schema", annType, uriHelper); 
			ann.explicitValues += rootAnnotationValue;
			rootAnnotationValue = XtendHelper::createAnnotationValue("strict", false, annType, uriHelper); 
			ann.explicitValues += rootAnnotationValue;
			it.annotations += ann;
		];
	}
	
	def void inferMethods(JvmGenericType scope) {
		
		var getAllMethod = scope.eClass.toMethod("getAll", scope.resolveMapType(scope.resolveStringType, scope.resolveObjectType)) [
			visibility = JvmVisibility::PUBLIC;
		];
		
		scope.members += getAllMethod;
		
		getAllMethod.body = [ appender |
			appender.append("return null;");
		];
		
		var setMethod = scope.eClass.toMethod("set", scope.resolveVoidType) [
			visibility = JvmVisibility::PUBLIC;
			parameters += scope.toParameter("key", scope.resolveStringType);
			parameters += scope.toParameter("value", scope.resolveObjectType);
		];
		
		scope.members += setMethod;

		setMethod.body = [ appender |
			appender.append("\n");
		];		
	}
	

	def void inferStepClass(Property step, IJvmDeclaredTypeAcceptor acceptor, JvmGenericType scope) {
		if(step.properties.filter([p | p.id != null && (p.id.startsWith("edit") || p.id.startsWith("capture"))]).size == 0)
			return;
		
		var stepQualifiedName = QualifiedName::create(scope.qualifiedName, step.name);
		var stepName = XtendHelper::convertQualifiedNameToGenName(stepQualifiedName);
		val stepClass = step.toClass(stepName);
		acceptor.accept(stepClass).initializeLater[
			var Class<?> annotation;
			var JvmTypeReference typeRef = references.createTypeRef(it);
			var many = step.hasProperty("is-multiple");
			if(many) {
				annotation = typeof(ElementList);
				typeRef = step.resolveArrayListType(typeRef);
			} else {
				annotation = typeof(Element);
			}
			val stepTypeRef = typeRef;
			
			for(f : step.properties) {
				if(f.id != null && (f.id.startsWith("edit") || f.id.startsWith("capture")))
					inferEdit(f, it);
			}
			var field = step.toField(step.name, stepTypeRef) [ 
				setInitializer([x | { 
					x.append("new ");
					x.append(stepTypeRef.qualifiedName);
					x.append("()");
			}]); ]
			
			var ann = step.toAnnotation(annotation);
			ann.explicitValues += XtendHelper::createAnnotationValue("name", step.name, annotation, uriHelper);
			ann.explicitValues += XtendHelper::createAnnotationValue("required", false, annotation, uriHelper);
			field.annotations += ann;
			
			scope.members += field;
			
			scope.members += step.toGetter(step.name, stepTypeRef)
			scope.members += step.toSetter(step.name, stepTypeRef)
		];
	}
	
	def void inferEdit(Property edit, JvmGenericType scope) {
		var Class<?> annotation;
		var JvmTypeReference typeRef = inferValueType(edit);
		var many = edit.hasProperty("is-multiple");
		
		if(many) {
			annotation = typeof(ElementList);
			typeRef = edit.resolveArrayListType(typeRef);
		} else {
			annotation = typeof(Element);
		}
		var field = edit.toField(edit.name, typeRef);
		
		var ann = edit.toAnnotation(annotation);
		ann.explicitValues += XtendHelper::createAnnotationValue("name", edit.name, annotation, uriHelper);
		ann.explicitValues += XtendHelper::createAnnotationValue("required", false, annotation, uriHelper);
		field.annotations += ann;
		
		scope.members += field
		scope.members += edit.toGetter(edit.name, typeRef)
		scope.members += edit.toSetter(edit.name, typeRef)
	}
	
	def JvmTypeReference inferValueType(Property p) {
		var Class<?> type = null;
		switch p.id {
			case "edit-check": { type = typeof(CheckList); }
			case "edit-boolean": { type = typeof(boolean); }
			case "capture-position": { type = typeof(Location); }
			case "capture-date": { type = typeof(Date); }
			case "edit-date": { type = typeof(Date); }
			case "edit-memo": { type = typeof(String); }
			case "edit-number": { type = typeof(Number); }
			case "edit-position": { type = typeof(Location); }
			case "edit-picture": { type = typeof(PictureLink); }
			case "edit-select": { type = typeof(Term); }
			case "edit-text": { type = typeof(String); }
			case "edit-video": { type = typeof(VideoLink); }
			default : { type = typeof(Void); }		
		}
		return p.resolve(type);
	}
	
}