package org.monet.editor.dsl.jvmmodel.inferers

import java.util.List
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable
import org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral
import org.monet.editor.dsl.helper.TypeRefCache
import com.google.inject.Inject
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.eclipse.xtext.common.types.JvmVisibility
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral
import org.monet.editor.dsl.jvmmodel.MMLExtensions

class RuleInferer {

	@Inject extension MonetJvmTypesBuilder
	@Inject extension MMLExtensions
	@Inject extension TypeRefCache
	
	def void infer(Definition e, List<Property> rules, JvmGenericType behaviourClazz) {
		val listenerAppender = new StringBuilderBasedAppendable(); 
		
		for(rule : rules) {
			val ruleCode = rule.code.value;
			val ruleName = org::monet::editor::dsl::helper::JavaHelper::toJavaIdentifier(ruleCode);
			
			val hasExpression = inferWhenExpression(ruleName, rule, behaviourClazz);	
			
			var ruleMethod = rule.toMethod("rule" + ruleName, rule.resolveVoidType)[
				visibility = JvmVisibility::PROTECTED;
			];
			
			ruleMethod.body = [appender | 
				appender.append("if(");
				if(hasExpression) {
					appender.append("when");
					appender.append(ruleName);
					appender.append("()");
				} else {
					appender.append("true");
				}
				appender.append(") {\n");
				
				for(flag : rule.getAttributes("add-flag")) {
					appender.append("  this.setFlag(\"");
					appender.append(ruleCode);
					appender.append("\", ");
					appender.append(rule.resolve(getAddFlagEnumClass(rule)).type);
					appender.append(".");
					
					var flagLiteral = flag.value as EnumLiteral;
					appender.append(flagLiteral.value);
					appender.append(");\n");
				}
				
				appender.append("}");
			];
			behaviourClazz.members += ruleMethod;
			
			listenerAppender.append("rule");
			listenerAppender.append(ruleName);
			listenerAppender.append("();\n");
		}
		
		var evaluateRulesMethod = e.toMethod("evaluateRules", e.resolveVoidType) [
			visibility = JvmVisibility::PUBLIC;
			
			body = [appender | appender.append(listenerAppender.toString)];
		];
		behaviourClazz.members += evaluateRulesMethod;
	}
	
	def private boolean inferWhenExpression(String ruleName, Property rule, JvmGenericType behaviourClazz) {
		val whenAttribute = rule.getAttribute("when");
		if(whenAttribute != null && whenAttribute.value instanceof ExpressionLiteral) {
			val whenExpression = whenAttribute.value as ExpressionLiteral; 
			var whenMethod = whenAttribute.toMethod("when" + ruleName, whenAttribute.resolveBooleanPrimitiveType) [
				visibility = JvmVisibility::PROTECTED;
				if(whenExpression.value != null)
					body = whenExpression.value;
			];
			behaviourClazz.members += whenMethod;
		}
		return whenAttribute != null;
	}
	
	def private Class<?> getAddFlagEnumClass(Property rule) {
		switch(rule.id) {
			case "rule:node": {
				return typeof(org.monet.metamodel.NodeDefinitionBase$RuleNodeProperty$AddFlagEnumeration);
			}
			case "rule:form" : {
				return typeof(org.monet.metamodel.FormDefinitionBase$RuleFormProperty$AddFlagEnumeration);
			}
			case "rule:view" : {
				return typeof(org.monet.metamodel.NodeDefinitionBase$RuleViewProperty$AddFlagEnumeration);
			}
			case "rule:link" : {
				return typeof(org.monet.metamodel.DesktopDefinitionBase$RuleLinkProperty$AddFlagEnumeration);
			}
			case "rule:operation" : {
				return typeof(org.monet.metamodel.NodeDefinitionBase$RuleOperationProperty$AddFlagEnumeration);
			}
		}
		return null;
	}
	
}