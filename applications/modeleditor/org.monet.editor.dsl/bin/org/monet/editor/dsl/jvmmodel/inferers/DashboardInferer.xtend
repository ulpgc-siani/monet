package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import com.sun.org.glassfish.gmbal.Description
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.jvmmodel.MMLExtensions
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral
import org.monet.editor.dsl.monetModelingLanguage.Property

class DashboardInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache

	@Inject protected PropertyInferer propertyInferer
	@Inject private ClassNameInferer classNameInferer

	def void infer(Definition definition, IJvmDeclaredTypeAcceptor acceptor) {
		inferIndicatorsFormula(definition, acceptor);
		inferTaxonomiesClassifier(definition, acceptor);
	}

	def void inferIndicatorsFormula(Definition definition, IJvmDeclaredTypeAcceptor acceptor) {
		definition.getProperties("indicator").forEach(
			[ indicator |
				var level = indicator.getProperty("level");
				if (level != null && level.hasProperty("secondary")) {
					inferIndicatorFormulaClass(definition, indicator.name, level.getProperty("secondary"), acceptor);
				}
			]);
	}
	
	def void inferIndicatorFormulaClass(Definition definition, String indicatorName, Property property, IJvmDeclaredTypeAcceptor acceptor) {
		val formulaClass = definition.toClass(this.classNameInferer.inferIndicatorFormulaName(definition, indicatorName));

		acceptor.accept(formulaClass).initializeLater [
			
			members += definition.toMethod("getParameters", definition.resolveArrayListType(definition.resolveStringType)) [
				static = true;
				body = [ ap |
					ap.append("ArrayList<String> parameters = new ArrayList<String>();\n");
					for (p : property.getAttributes("use")) {
						ap.append("parameters.add(\"" + p.valueAsProperty.code.value + "\");\n");
					}
					ap.append("return parameters;");
				];
			];
			
			members += definition.toMethod("calculate", definition.resolveDoublePrimitiveType) [
				static = true;
				parameters += definition.toParameter("__map",
					definition.resolveHasMapType(definition.resolveStringType, definition.resolveObjectType));
				body = [ ap |
					var calculateArgs = new StringBuilder();
					for (p : property.getAttributes("use")) {
						var useIndicatorName = p.valueAsProperty.code.value;
						ap.append("Double ");
						ap.append(useIndicatorName);
						ap.append(" = (Double) __map.get(\"");
						ap.append(useIndicatorName);
						ap.append("\");\n");
						calculateArgs.append(useIndicatorName);
						calculateArgs.append(", ");
					}
					if (calculateArgs.length > 0)
						calculateArgs.delete(calculateArgs.length - 2, calculateArgs.length);
					ap.append("return calculate(");
					ap.append(calculateArgs.toString);
					ap.append(");\n");
				];
			];
			
			inferIndicatorFormulaClassExpressionMethod(definition, property, it);
		];
	}

	def private inferIndicatorFormulaClassExpressionMethod(Definition definition, Property property, JvmGenericType behaviourClazz) {
		val formula = property.getAttribute("formula");
		if (formula != null && formula.value instanceof ExpressionLiteral) {
			val formulaExpression = formula.value as ExpressionLiteral;
			var method = formula.toMethod("calculate", formula.resolveDoublePrimitiveType) [
				static = true;
				for (p : property.getAttributes("use")) {
					var useIndicatorName = p.valueAsProperty.name;
					var JvmTypeReference parameterType = null;
					parameterType = p.resolveDoublePrimitiveType;
					parameters += p.toParameter(useIndicatorName, parameterType);
				}
				if (formulaExpression.value != null)
					body = formulaExpression.value;
			];
			behaviourClazz.members += method;
		}
	}

	def void inferTaxonomiesClassifier(Definition definition, IJvmDeclaredTypeAcceptor acceptor) {
		definition.getProperties("taxonomy").forEach(
			[ taxonomy |
				if (taxonomy.hasProperty("explicit")) {
					inferTaxonomyClassifierMethod(definition, taxonomy, taxonomy.getProperty("explicit"), acceptor);
				}
			]);
	}

	def void inferTaxonomyClassifierMethod(Definition definition, Property taxonomy, Property property, IJvmDeclaredTypeAcceptor acceptor) {
		val classifierClass = definition.toClass(this.classNameInferer.inferTaxonomyClassifierName(definition, taxonomy.name));

		acceptor.accept(classifierClass).initializeLater [
			val classifier = property.getAttribute("classifier");
			if (classifier != null && classifier.value instanceof ExpressionLiteral) {
				val classifierExpression = classifier.value as ExpressionLiteral;
				val JvmTypeReference featureType = inferTaxonomyFeatureType(definition, taxonomy);

				members += definition.toMethod("classify", definition.resolveStringType) [
					static = true;
					parameters += definition.toParameter("featureValue", definition.resolveComparableType(featureType));
					body = classifierExpression.value;
				];
			}
		];
	}
	
	def private JvmTypeReference inferTaxonomyFeatureType(Definition definition, Property taxonomy) {
		var featureAttribute = taxonomy.getAttribute("feature");
		
		if (featureAttribute == null)
		  return definition.resolveDoublePrimitiveType;
		   
		var featureProperty = featureAttribute.valueAsProperty;
		if (featureProperty == null)
		  return definition.resolveDoublePrimitiveType;
		
		var String type = featureProperty.getAttribute("type").valueAsString;
		switch type {
			case "STRING": {
				return definition.resolveStringType;
			}
			case "TERM": {
				return definition.resolveStringType;
			}
			case "BOOLEAN": {
				return definition.resolveBooleanPrimitiveType;
			}
			case "INTEGER": {
				return definition.resolveIntegerType;
			}
			case "REAL": {
				return definition.resolveDoublePrimitiveType;
			}
			case "DATE": {
				return definition.resolveDateType;
			}
			default: {
				return definition.resolveDoublePrimitiveType;
			}
		}
		
	}

}
