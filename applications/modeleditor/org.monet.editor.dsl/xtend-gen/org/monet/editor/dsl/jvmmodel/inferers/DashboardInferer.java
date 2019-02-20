package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.jvmmodel.inferers.PropertyInferer;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral;
import org.monet.editor.dsl.monetModelingLanguage.Property;

@SuppressWarnings("all")
public class DashboardInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private MMLExtensions _mMLExtensions;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  @Inject
  protected PropertyInferer propertyInferer;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  public void infer(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor) {
    this.inferIndicatorsFormula(definition, acceptor);
    this.inferTaxonomiesClassifier(definition, acceptor);
  }
  
  public void inferIndicatorsFormula(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition, "indicator");
    final Procedure1<Property> _function = new Procedure1<Property>() {
      public void apply(final Property indicator) {
        Property level = DashboardInferer.this._mMLExtensions.getProperty(indicator, "level");
        boolean _and = false;
        boolean _notEquals = (!Objects.equal(level, null));
        if (!_notEquals) {
          _and = false;
        } else {
          boolean _hasProperty = DashboardInferer.this._mMLExtensions.hasProperty(level, "secondary");
          _and = _hasProperty;
        }
        if (_and) {
          String _name = indicator.getName();
          Property _property = DashboardInferer.this._mMLExtensions.getProperty(level, "secondary");
          DashboardInferer.this.inferIndicatorFormulaClass(definition, _name, _property, acceptor);
        }
      }
    };
    IterableExtensions.<Property>forEach(_properties, _function);
  }
  
  public void inferIndicatorFormulaClass(final Definition definition, final String indicatorName, final Property property, final IJvmDeclaredTypeAcceptor acceptor) {
    String _inferIndicatorFormulaName = this.classNameInferer.inferIndicatorFormulaName(definition, indicatorName);
    final JvmGenericType formulaClass = this._monetJvmTypesBuilder.toClass(definition, _inferIndicatorFormulaName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(formulaClass);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmMember> _members = it.getMembers();
        JvmTypeReference _resolveStringType = DashboardInferer.this._typeRefCache.resolveStringType(definition);
        JvmTypeReference _resolveArrayListType = DashboardInferer.this._typeRefCache.resolveArrayListType(definition, _resolveStringType);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            it.setStatic(true);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("ArrayList<String> parameters = new ArrayList<String>();\n");
                Iterable<Attribute> _attributes = DashboardInferer.this._mMLExtensions.getAttributes(property, "use");
                for (final Attribute p : _attributes) {
                  Property _valueAsProperty = DashboardInferer.this._mMLExtensions.getValueAsProperty(p);
                  Code _code = _valueAsProperty.getCode();
                  String _value = _code.getValue();
                  String _plus = ("parameters.add(\"" + _value);
                  String _plus_1 = (_plus + "\");\n");
                  ap.append(_plus_1);
                }
                ap.append("return parameters;");
              }
            };
            DashboardInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = DashboardInferer.this._monetJvmTypesBuilder.toMethod(definition, "getParameters", _resolveArrayListType, _function);
        DashboardInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        EList<JvmMember> _members_1 = it.getMembers();
        JvmTypeReference _resolveDoublePrimitiveType = DashboardInferer.this._typeRefCache.resolveDoublePrimitiveType(definition);
        final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            it.setStatic(true);
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = DashboardInferer.this._typeRefCache.resolveStringType(definition);
            JvmTypeReference _resolveObjectType = DashboardInferer.this._typeRefCache.resolveObjectType(definition);
            JvmTypeReference _resolveHasMapType = DashboardInferer.this._typeRefCache.resolveHasMapType(definition, _resolveStringType, _resolveObjectType);
            JvmFormalParameter _parameter = DashboardInferer.this._monetJvmTypesBuilder.toParameter(definition, "__map", _resolveHasMapType);
            DashboardInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                StringBuilder calculateArgs = new StringBuilder();
                Iterable<Attribute> _attributes = DashboardInferer.this._mMLExtensions.getAttributes(property, "use");
                for (final Attribute p : _attributes) {
                  {
                    Property _valueAsProperty = DashboardInferer.this._mMLExtensions.getValueAsProperty(p);
                    Code _code = _valueAsProperty.getCode();
                    String useIndicatorName = _code.getValue();
                    ap.append("Double ");
                    ap.append(useIndicatorName);
                    ap.append(" = (Double) __map.get(\"");
                    ap.append(useIndicatorName);
                    ap.append("\");\n");
                    calculateArgs.append(useIndicatorName);
                    calculateArgs.append(", ");
                  }
                }
                int _length = calculateArgs.length();
                boolean _greaterThan = (_length > 0);
                if (_greaterThan) {
                  int _length_1 = calculateArgs.length();
                  int _minus = (_length_1 - 2);
                  int _length_2 = calculateArgs.length();
                  calculateArgs.delete(_minus, _length_2);
                }
                ap.append("return calculate(");
                String _string = calculateArgs.toString();
                ap.append(_string);
                ap.append(");\n");
              }
            };
            DashboardInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_1 = DashboardInferer.this._monetJvmTypesBuilder.toMethod(definition, "calculate", _resolveDoublePrimitiveType, _function_1);
        DashboardInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
        DashboardInferer.this.inferIndicatorFormulaClassExpressionMethod(definition, property, it);
      }
    };
    _accept.initializeLater(_function);
  }
  
  private boolean inferIndicatorFormulaClassExpressionMethod(final Definition definition, final Property property, final JvmGenericType behaviourClazz) {
    boolean _xblockexpression = false;
    {
      final Attribute formula = this._mMLExtensions.getAttribute(property, "formula");
      boolean _xifexpression = false;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(formula, null));
      if (!_notEquals) {
        _and = false;
      } else {
        AttributeValue _value = formula.getValue();
        _and = (_value instanceof ExpressionLiteral);
      }
      if (_and) {
        boolean _xblockexpression_1 = false;
        {
          AttributeValue _value_1 = formula.getValue();
          final ExpressionLiteral formulaExpression = ((ExpressionLiteral) _value_1);
          JvmTypeReference _resolveDoublePrimitiveType = this._typeRefCache.resolveDoublePrimitiveType(formula);
          final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              it.setStatic(true);
              Iterable<Attribute> _attributes = DashboardInferer.this._mMLExtensions.getAttributes(property, "use");
              for (final Attribute p : _attributes) {
                {
                  Property _valueAsProperty = DashboardInferer.this._mMLExtensions.getValueAsProperty(p);
                  String useIndicatorName = _valueAsProperty.getName();
                  JvmTypeReference parameterType = null;
                  JvmTypeReference _resolveDoublePrimitiveType = DashboardInferer.this._typeRefCache.resolveDoublePrimitiveType(p);
                  parameterType = _resolveDoublePrimitiveType;
                  EList<JvmFormalParameter> _parameters = it.getParameters();
                  JvmFormalParameter _parameter = DashboardInferer.this._monetJvmTypesBuilder.toParameter(p, useIndicatorName, parameterType);
                  DashboardInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                }
              }
              XExpression _value = formulaExpression.getValue();
              boolean _notEquals = (!Objects.equal(_value, null));
              if (_notEquals) {
                XExpression _value_1 = formulaExpression.getValue();
                DashboardInferer.this._monetJvmTypesBuilder.setBody(it, _value_1);
              }
            }
          };
          JvmOperation method = this._monetJvmTypesBuilder.toMethod(formula, "calculate", _resolveDoublePrimitiveType, _function);
          EList<JvmMember> _members = behaviourClazz.getMembers();
          _xblockexpression_1 = this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, method);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public void inferTaxonomiesClassifier(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition, "taxonomy");
    final Procedure1<Property> _function = new Procedure1<Property>() {
      public void apply(final Property taxonomy) {
        boolean _hasProperty = DashboardInferer.this._mMLExtensions.hasProperty(taxonomy, "explicit");
        if (_hasProperty) {
          Property _property = DashboardInferer.this._mMLExtensions.getProperty(taxonomy, "explicit");
          DashboardInferer.this.inferTaxonomyClassifierMethod(definition, taxonomy, _property, acceptor);
        }
      }
    };
    IterableExtensions.<Property>forEach(_properties, _function);
  }
  
  public void inferTaxonomyClassifierMethod(final Definition definition, final Property taxonomy, final Property property, final IJvmDeclaredTypeAcceptor acceptor) {
    String _name = taxonomy.getName();
    String _inferTaxonomyClassifierName = this.classNameInferer.inferTaxonomyClassifierName(definition, _name);
    final JvmGenericType classifierClass = this._monetJvmTypesBuilder.toClass(definition, _inferTaxonomyClassifierName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(classifierClass);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        final Attribute classifier = DashboardInferer.this._mMLExtensions.getAttribute(property, "classifier");
        boolean _and = false;
        boolean _notEquals = (!Objects.equal(classifier, null));
        if (!_notEquals) {
          _and = false;
        } else {
          AttributeValue _value = classifier.getValue();
          _and = (_value instanceof ExpressionLiteral);
        }
        if (_and) {
          AttributeValue _value_1 = classifier.getValue();
          final ExpressionLiteral classifierExpression = ((ExpressionLiteral) _value_1);
          final JvmTypeReference featureType = DashboardInferer.this.inferTaxonomyFeatureType(definition, taxonomy);
          EList<JvmMember> _members = it.getMembers();
          JvmTypeReference _resolveStringType = DashboardInferer.this._typeRefCache.resolveStringType(definition);
          final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              it.setStatic(true);
              EList<JvmFormalParameter> _parameters = it.getParameters();
              JvmTypeReference _resolveComparableType = DashboardInferer.this._typeRefCache.resolveComparableType(definition, featureType);
              JvmFormalParameter _parameter = DashboardInferer.this._monetJvmTypesBuilder.toParameter(definition, "featureValue", _resolveComparableType);
              DashboardInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              XExpression _value = classifierExpression.getValue();
              DashboardInferer.this._monetJvmTypesBuilder.setBody(it, _value);
            }
          };
          JvmOperation _method = DashboardInferer.this._monetJvmTypesBuilder.toMethod(definition, "classify", _resolveStringType, _function);
          DashboardInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        }
      }
    };
    _accept.initializeLater(_function);
  }
  
  private JvmTypeReference inferTaxonomyFeatureType(final Definition definition, final Property taxonomy) {
    Attribute featureAttribute = this._mMLExtensions.getAttribute(taxonomy, "feature");
    boolean _equals = Objects.equal(featureAttribute, null);
    if (_equals) {
      return this._typeRefCache.resolveDoublePrimitiveType(definition);
    }
    Property featureProperty = this._mMLExtensions.getValueAsProperty(featureAttribute);
    boolean _equals_1 = Objects.equal(featureProperty, null);
    if (_equals_1) {
      return this._typeRefCache.resolveDoublePrimitiveType(definition);
    }
    Attribute _attribute = this._mMLExtensions.getAttribute(featureProperty, "type");
    String type = this._mMLExtensions.getValueAsString(_attribute);
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(type, "STRING")) {
        _matched=true;
        return this._typeRefCache.resolveStringType(definition);
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "TERM")) {
        _matched=true;
        return this._typeRefCache.resolveStringType(definition);
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "BOOLEAN")) {
        _matched=true;
        return this._typeRefCache.resolveBooleanPrimitiveType(definition);
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "INTEGER")) {
        _matched=true;
        return this._typeRefCache.resolveIntegerType(definition);
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "REAL")) {
        _matched=true;
        return this._typeRefCache.resolveDoublePrimitiveType(definition);
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "DATE")) {
        _matched=true;
        return this._typeRefCache.resolveDateType(definition);
      }
    }
    return this._typeRefCache.resolveDoublePrimitiveType(definition);
  }
}
