package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.metamodel.Pair
import org.monet.editor.dsl.monetModelingLanguage.Attribute
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Method
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.monetModelingLanguage.XTReference
import java.util.List

class DatastoreBuilderInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension IQualifiedNameProvider
	@Inject extension TypeRefCache
	
	def void inferMethods(Definition d, JvmGenericType behaviourClazz, Attribute source, Iterable<Property> toProperties, boolean prelinkingPhase) {
		try {
			val result = new StringBuilderBasedAppendable();
			var sourceValue = source.value;
			
			if(sourceValue == null  || (!(sourceValue instanceof XTReference))) {
				return;
			}
			var sourceRef = sourceValue as XTReference;
			
			if(!prelinkingPhase && sourceRef.value.fullyQualifiedName != null) {
				
				val toPairs = toProperties.map([p |
					var pair = new Pair<Attribute, Method>(null, null);
					for(f : p.features) {
						switch(f) {
							Attribute : {
								pair.setFirst(f);
							}
							Method : {
								pair.setSecond(f);
							}
						}
					}
					return pair;
				]).toList();
				
				for(to : toPairs) {
					inferOnBuild(d, behaviourClazz, sourceRef, to, result);
				}
			}
			
			behaviourClazz.members += d.toMethod("onBuild", d.resolveVoidType) [
				parameters += d.toParameter("source", d.resolveNodeType);
				body = [appender | appender.append(result.toString) ];
			];
			
		} catch(Exception ex) {
			ex.printStackTrace;
		}
	}
	
	def void inferOnBuild(Definition definition, JvmGenericType behaviourClazz, XTReference sourceRef, Pair<Attribute, Method> toPair, StringBuilderBasedAppendable result) {
		val nodeFullyQualifiedName = XtendHelper::convertQualifiedNameToGenName(sourceRef.value.fullyQualifiedName);
		
		var destinationValue = toPair.first.value;
		if(destinationValue != null && destinationValue instanceof XTReference) {
			var destinationRef = destinationValue as XTReference;
			var destinationName = XtendHelper::getReferenciableName(destinationRef.value);
			var destinationFullyQualifiedName = XtendHelper::convertQualifiedNameToGenName(destinationRef.value.fullyQualifiedName);
			//var destinationReferenciableName = XtendHelper::getReferenciableName(destinationRef.value);
			
			result.append("if(source instanceof ");
			result.append(nodeFullyQualifiedName);
			result.append(")\n  this.onBuildTo");
			result.append(destinationName);
			result.append("((");
			result.append(nodeFullyQualifiedName);
			result.append(")source, (");
			result.append(destinationFullyQualifiedName);
			result.append(")this.loadDatastore");
			result.append("(" + destinationFullyQualifiedName + ".class,\"");
			result.append(destinationFullyQualifiedName);
			result.append("\"));\n");
			
			behaviourClazz.members += toPair.second.toMethod(String::format("onBuildTo%s", destinationName), definition.resolveVoidType) [
				for(p : toPair.second.params) {
					parameters += p.toParameter(p.name, p.parameterType);
				}
				body = toPair.second.body;
			];
		}
	}
	
}