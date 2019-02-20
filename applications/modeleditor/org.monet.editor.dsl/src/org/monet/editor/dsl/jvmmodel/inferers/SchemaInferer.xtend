package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.access.impl.ClassURIHelper
import org.eclipse.xtext.common.types.util.Primitives
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XConstructorCall
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Schema
import org.monet.editor.dsl.monetModelingLanguage.SchemaFeature
import org.monet.editor.dsl.monetModelingLanguage.SchemaProperty
import org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue
import org.monet.editor.dsl.monetModelingLanguage.SchemaSection
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

class SchemaInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension TypeRefCache

	@Inject private TypeReferences references
	@Inject private XbaseCompiler xbaseCompiler
	@Inject private Primitives primitives
	@Inject private ClassURIHelper uriHelper
	@Inject private ClassNameInferer classNameInferer

	def void infer(Schema schema, Definition definition, IJvmDeclaredTypeAcceptor acceptor) {
		var EObject b;

		if (schema == null) {
			b = definition;
		} else {
			b = schema;
		}
		val schemaClass = b.toClass(this.classNameInferer.inferSchemaBehaviourName(definition));	

		if (schema != null)
			schema.properties.filter(typeof(SchemaSection)).forEach(
				[ss|inferSchemaSectionClass(ss, acceptor, schemaClass)]);

		acceptor.accept(schemaClass).initializeLater [
			var hasSuper = false;
			var Definition superType = null;
			if (definition.superType != null) {
				superType = definition.superType;
			} else if (definition.replaceSuperType != null) {
				superType = definition.replaceSuperType;
			}
			if (superType != null && superType.fullyQualifiedName != null) {
				var parentSchemaType = references.getTypeForName(
					this.classNameInferer.inferSchemaBehaviourName(superType), definition).cloneWithProxies;
				hasSuper = parentSchemaType != null;
				it.superTypes += parentSchemaType;
			}
			if (!hasSuper)
				it.superTypes += definition.resolveSchemaType.cloneWithProxies;
			if (schema != null) {
				for (p : schema.properties) {
					switch p {
						SchemaProperty: {
							inferProperty(p, it);
						}
					}
				}
				inferMethods(schema.properties, it);
			}
			var annType = typeof(Root);
			var ann = definition.toAnnotation(annType);
			var rootAnnotationValue = XtendHelper::createAnnotationValue("name", "schema", annType, uriHelper);
			ann.explicitValues += rootAnnotationValue;
			rootAnnotationValue = XtendHelper::createAnnotationValue("strict", false, annType, uriHelper);
			ann.explicitValues += rootAnnotationValue;
			it.annotations += ann;
		];
	}

	def void inferSchemaSectionClass(SchemaSection p, IJvmDeclaredTypeAcceptor acceptor, JvmGenericType scope) {
		var sectionQualifiedName = QualifiedName::create(scope.qualifiedName, p.id);
		var sectionName = XtendHelper::convertQualifiedNameToGenName(sectionQualifiedName);
		val sectionClass = p.toClass(sectionName);
		acceptor.accept(sectionClass).initializeLater [
			var Class<?> annotation;
			var JvmTypeReference typeRef = references.createTypeRef(it);
			it.superTypes += p.resolveSchemaSectionType.cloneWithProxies;
			if (p.many) {
				annotation = typeof(ElementList);
				typeRef = p.resolveArrayListType(typeRef);
			} else {
				annotation = typeof(Element);
			}
			val sectionTypeRef = typeRef;
			for (f : p.features) {
				switch f {
					SchemaProperty: {
						inferProperty(f, it);
					}
				}
			}
			inferMethods(p.features, it);
			var annType = typeof(Root);
			var ann = p.toAnnotation(annType);
			var rootAnnotationValue = XtendHelper::createAnnotationValue("name", p.id, annType, uriHelper);
			ann.explicitValues += rootAnnotationValue;
			rootAnnotationValue = XtendHelper::createAnnotationValue("strict", false, annType, uriHelper);
			ann.explicitValues += rootAnnotationValue;
			it.annotations += ann;
			var field = p.toField(p.id, sectionTypeRef)[
				setInitializer(
					[x|
						{
							x.append("new ");
							x.append(sectionTypeRef.qualifiedName);
							x.append("()");
						}]);]
			ann = p.toAnnotation(annotation);
			ann.explicitValues += XtendHelper::createAnnotationValue("name", p.id, annotation, uriHelper);
			ann.explicitValues += XtendHelper::createAnnotationValue("required", false, annotation, uriHelper);
			field.annotations += ann;
			scope.members += field;
			scope.members += p.toGetter(p.id, sectionTypeRef)
			scope.members += p.toSetter(p.id, sectionTypeRef)
		];
		p.features.filter(typeof(SchemaSection)).forEach([ss|inferSchemaSectionClass(ss, acceptor, sectionClass)]);
	}

	def void inferMethods(EList<SchemaFeature> properties, JvmGenericType scope) {
		
		var getAllMethod = scope.eClass.toMethod("getAll", scope.resolveMapType(scope.resolveStringType, scope.resolveObjectType)) [
			visibility = JvmVisibility::PUBLIC;
		];
		
		scope.members += getAllMethod;

		getAllMethod.body = [ appender |
			appender.append("HashMap<String, Object> result = new HashMap<>();\n");

			for (p : properties) {
				appender.append("result.put(\"" + p.id + "\", this." + p.id + ");\n");
			}
			
			appender.append("return result;\n");
		];

		var setMethod = scope.eClass.toMethod("set", scope.resolveVoidType) [
			visibility = JvmVisibility::PUBLIC;
			parameters += scope.toParameter("key", scope.resolveStringType);
			parameters += scope.toParameter("value", scope.resolveObjectType);
		];
		
		scope.members += setMethod;

		setMethod.body = [ appender |
			for (p : properties) {
				
				var typeFormat = "%s";				
				if (p.many) {
					typeFormat = "java.util.ArrayList<%s>";
				}
				
				var type = "";				
				switch p {				
					SchemaProperty: {
						type = String.format(typeFormat, p.source.type.qualifiedName);
					}
					default: {
						var sectionQualifiedName = QualifiedName::create(scope.qualifiedName, p.id);
						type = String.format(typeFormat, XtendHelper::convertQualifiedNameToGenName(sectionQualifiedName));
					}					
				}
				
				appender.append("if (key.equalsIgnoreCase(\"" + p.id + "\")) { this." + p.id + " = (" + type + ")value; return; }\n");						
			}
		];

	}

	def void inferProperty(SchemaProperty p, JvmGenericType scope) {
		var Class<?> annotation
		var JvmTypeReference typeRef
		var source = p.source;
		var XExpression bodyExp;
		var many = p.many;
		switch (source) {
			SchemaPropertyOfValue: {
				typeRef = source.type;
				bodyExp = source.body;
			}
		}

		if (many) {
			annotation = typeof(ElementList);
			typeRef = p.resolveArrayListType(typeRef);
		} else {
			annotation = typeof(Element);
		}
		var field = p.toField(p.id, typeRef);

		var ann = p.toAnnotation(annotation);
		ann.explicitValues += XtendHelper::createAnnotationValue("name", p.id, annotation, uriHelper);
		ann.explicitValues += XtendHelper::createAnnotationValue("required", false, annotation, uriHelper);
		field.annotations += ann;

		if (bodyExp != null && !many) {
			val bodyExpF = bodyExp;
			field.setInitializer(
				[x|
					{
						var body = bodyExpF as XBlockExpression
						if (body.expressions.get(0) instanceof XConstructorCall)
							xbaseCompiler.toJavaStatement(bodyExpF, x, false)
						else
							xbaseCompiler.toJavaExpression(bodyExpF, x)
					}]);
		} else if (many) {
			val typeRefF = typeRef;
			field.setInitializer(
				[x|
					{
						x.append("new ");
						x.append(primitives.asWrapperTypeIfPrimitive(typeRefF).qualifiedName);
						x.append("()");
					}]);
		}
		scope.members += field
		scope.members += p.toGetter(p.id, typeRef)
		scope.members += p.toSetter(p.id, typeRef)
	}

}
