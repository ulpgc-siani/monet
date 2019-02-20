package org.monet.editor.dsl.jvmmodel.inferers

import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Property
import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.monet.editor.dsl.helper.XtendHelper
class ClassNameInferer {
	
	@Inject extension IQualifiedNameProvider
	
	def String inferDefinition(Definition definition) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName.append("$Definition$"));
	}
	
	def String inferSchemaBehaviourName(Definition definition) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName.append("Schema"));
	}
	
	def String inferIndicatorFormulaName(Definition definition, String indicatorName) {
		var definitionName = XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName); 
		return definitionName + indicatorName + "Formula";
	}
	
	def String inferTaxonomyClassifierName(Definition definition, String indicatorName) {
		var definitionName = XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName); 
		return definitionName + indicatorName + "Classifier";
	}

	def String inferBehaviourName(Definition definition) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName);
	}
	
	def String inferExporterScopeName(Definition definition) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName.append("ExporterScope"));
	}
	
	def String inferTaskLockClass(Definition definition) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName.append("Lock"));
	}
	
	def String inferTaskPlaceClass(Definition definition) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName.append("Place"));
	}
	
	def String inferDimensionName(Property dimension) {
		return XtendHelper::convertQualifiedNameToGenName(dimension.fullyQualifiedName) + "Dimension";
	}

	def String inferComponentName(Property dimension) {
		return XtendHelper::convertQualifiedNameToGenName(dimension.fullyQualifiedName) + "DimensionComponent";
	}
		
	def String inferCubeName(Property cube) {
		return XtendHelper::convertQualifiedNameToGenName(cube.fullyQualifiedName) + "Cube";
	}
	
	def String inferFactName(Property cube) {
		return XtendHelper::convertQualifiedNameToGenName(cube.fullyQualifiedName) + "CubeFact";
	}
	
	def String inferPropertyName(Property property) {
		return XtendHelper::convertQualifiedNameToGenNameWithSuffix(property.fullyQualifiedName, "Property");
	}
	
	def String inferPropertyBehavior(Property property) {
		var fullyQN = property.fullyQualifiedName;
		if(fullyQN == null) {
			fullyQN = property.eContainer.fullyQualifiedName.append(property.id);
		}
		return XtendHelper::convertQualifiedNameToGenNameWithSuffix(fullyQN, "Behavior");
	}
	
	def String inferMappingName(Definition definition, int index) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName.append("Mapping__" + String::valueOf(index)));
	}
	
	def String inferPropertiesName(Definition definition) {
		return XtendHelper::convertQualifiedNameToGenName(definition.fullyQualifiedName.append("$Properties$"));
	}
	
}