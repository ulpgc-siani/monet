package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral;
import org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.metamodel.DesktopDefinitionBase;
import org.monet.metamodel.FormDefinitionBase;
import org.monet.metamodel.NodeDefinitionBase;

@SuppressWarnings("all")
public class RuleInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private MMLExtensions _mMLExtensions;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  public void infer(final Definition e, final List<Property> rules, final JvmGenericType behaviourClazz) {
    final StringBuilderBasedAppendable listenerAppender = new StringBuilderBasedAppendable();
    for (final Property rule : rules) {
      {
        Code _code = rule.getCode();
        final String ruleCode = _code.getValue();
        final String ruleName = JavaHelper.toJavaIdentifier(ruleCode);
        final boolean hasExpression = this.inferWhenExpression(ruleName, rule, behaviourClazz);
        JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(rule);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            it.setVisibility(JvmVisibility.PROTECTED);
          }
        };
        JvmOperation ruleMethod = this._monetJvmTypesBuilder.toMethod(rule, ("rule" + ruleName), _resolveVoidType, _function);
        final Procedure1<ITreeAppendable> _function_1 = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable appender) {
            appender.append("if(");
            if (hasExpression) {
              appender.append("when");
              appender.append(ruleName);
              appender.append("()");
            } else {
              appender.append("true");
            }
            appender.append(") {\n");
            Iterable<Attribute> _attributes = RuleInferer.this._mMLExtensions.getAttributes(rule, "add-flag");
            for (final Attribute flag : _attributes) {
              {
                appender.append("  this.setFlag(\"");
                appender.append(ruleCode);
                appender.append("\", ");
                Class<?> _addFlagEnumClass = RuleInferer.this.getAddFlagEnumClass(rule);
                JvmTypeReference _resolve = RuleInferer.this._typeRefCache.resolve(rule, _addFlagEnumClass);
                JvmType _type = _resolve.getType();
                appender.append(_type);
                appender.append(".");
                AttributeValue _value = flag.getValue();
                EnumLiteral flagLiteral = ((EnumLiteral) _value);
                String _value_1 = flagLiteral.getValue();
                appender.append(_value_1);
                appender.append(");\n");
              }
            }
            appender.append("}");
          }
        };
        this._monetJvmTypesBuilder.setBody(ruleMethod, _function_1);
        EList<JvmMember> _members = behaviourClazz.getMembers();
        this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, ruleMethod);
        listenerAppender.append("rule");
        listenerAppender.append(ruleName);
        listenerAppender.append("();\n");
      }
    }
    JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(e);
    final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        it.setVisibility(JvmVisibility.PUBLIC);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable appender) {
            String _string = listenerAppender.toString();
            appender.append(_string);
          }
        };
        RuleInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation evaluateRulesMethod = this._monetJvmTypesBuilder.toMethod(e, "evaluateRules", _resolveVoidType, _function);
    EList<JvmMember> _members = behaviourClazz.getMembers();
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, evaluateRulesMethod);
  }
  
  private boolean inferWhenExpression(final String ruleName, final Property rule, final JvmGenericType behaviourClazz) {
    final Attribute whenAttribute = this._mMLExtensions.getAttribute(rule, "when");
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(whenAttribute, null));
    if (!_notEquals) {
      _and = false;
    } else {
      AttributeValue _value = whenAttribute.getValue();
      _and = (_value instanceof ExpressionLiteral);
    }
    if (_and) {
      AttributeValue _value_1 = whenAttribute.getValue();
      final ExpressionLiteral whenExpression = ((ExpressionLiteral) _value_1);
      JvmTypeReference _resolveBooleanPrimitiveType = this._typeRefCache.resolveBooleanPrimitiveType(whenAttribute);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          it.setVisibility(JvmVisibility.PROTECTED);
          XExpression _value = whenExpression.getValue();
          boolean _notEquals = (!Objects.equal(_value, null));
          if (_notEquals) {
            XExpression _value_1 = whenExpression.getValue();
            RuleInferer.this._monetJvmTypesBuilder.setBody(it, _value_1);
          }
        }
      };
      JvmOperation whenMethod = this._monetJvmTypesBuilder.toMethod(whenAttribute, ("when" + ruleName), _resolveBooleanPrimitiveType, _function);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, whenMethod);
    }
    return (!Objects.equal(whenAttribute, null));
  }
  
  private Class<?> getAddFlagEnumClass(final Property rule) {
    String _id = rule.getId();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_id, "rule:node")) {
        _matched=true;
        return NodeDefinitionBase.RuleNodeProperty.AddFlagEnumeration.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "rule:form")) {
        _matched=true;
        return FormDefinitionBase.RuleFormProperty.AddFlagEnumeration.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "rule:view")) {
        _matched=true;
        return NodeDefinitionBase.RuleViewProperty.AddFlagEnumeration.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "rule:link")) {
        _matched=true;
        return DesktopDefinitionBase.RuleLinkProperty.AddFlagEnumeration.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "rule:operation")) {
        _matched=true;
        return NodeDefinitionBase.RuleOperationProperty.AddFlagEnumeration.class;
      }
    }
    return null;
  }
}
