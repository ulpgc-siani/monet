package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.metamodel.Pair;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.PropertyFeature;
import org.monet.editor.dsl.monetModelingLanguage.Referenciable;
import org.monet.editor.dsl.monetModelingLanguage.XTReference;

@SuppressWarnings("all")
public class DatastoreBuilderInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  public void inferMethods(final Definition d, final JvmGenericType behaviourClazz, final Attribute source, final Iterable<Property> toProperties, final boolean prelinkingPhase) {
    try {
      final StringBuilderBasedAppendable result = new StringBuilderBasedAppendable();
      AttributeValue sourceValue = source.getValue();
      boolean _or = false;
      boolean _equals = Objects.equal(sourceValue, null);
      if (_equals) {
        _or = true;
      } else {
        _or = (!(sourceValue instanceof XTReference));
      }
      if (_or) {
        return;
      }
      XTReference sourceRef = ((XTReference) sourceValue);
      boolean _and = false;
      if (!(!prelinkingPhase)) {
        _and = false;
      } else {
        Referenciable _value = sourceRef.getValue();
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_value);
        boolean _notEquals = (!Objects.equal(_fullyQualifiedName, null));
        _and = _notEquals;
      }
      if (_and) {
        final Function1<Property, Pair<Attribute, Method>> _function = new Function1<Property, Pair<Attribute, Method>>() {
          public Pair<Attribute, Method> apply(final Property p) {
            Pair<Attribute, Method> pair = new Pair<Attribute, Method>(null, null);
            EList<PropertyFeature> _features = p.getFeatures();
            for (final PropertyFeature f : _features) {
              boolean _matched = false;
              if (!_matched) {
                if (f instanceof Attribute) {
                  _matched=true;
                  pair.setFirst(((Attribute)f));
                }
              }
              if (!_matched) {
                if (f instanceof Method) {
                  _matched=true;
                  pair.setSecond(((Method)f));
                }
              }
            }
            return pair;
          }
        };
        Iterable<Pair<Attribute, Method>> _map = IterableExtensions.<Property, Pair<Attribute, Method>>map(toProperties, _function);
        final List<Pair<Attribute, Method>> toPairs = IterableExtensions.<Pair<Attribute, Method>>toList(_map);
        for (final Pair<Attribute, Method> to : toPairs) {
          this.inferOnBuild(d, behaviourClazz, sourceRef, to, result);
        }
      }
      EList<JvmMember> _members = behaviourClazz.getMembers();
      JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(d);
      final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          EList<JvmFormalParameter> _parameters = it.getParameters();
          JvmTypeReference _resolveNodeType = DatastoreBuilderInferer.this._typeRefCache.resolveNodeType(d);
          JvmFormalParameter _parameter = DatastoreBuilderInferer.this._monetJvmTypesBuilder.toParameter(d, "source", _resolveNodeType);
          DatastoreBuilderInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable appender) {
              String _string = result.toString();
              appender.append(_string);
            }
          };
          DatastoreBuilderInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation _method = this._monetJvmTypesBuilder.toMethod(d, "onBuild", _resolveVoidType, _function_1);
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        ex.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public void inferOnBuild(final Definition definition, final JvmGenericType behaviourClazz, final XTReference sourceRef, final Pair<Attribute, Method> toPair, final StringBuilderBasedAppendable result) {
    Referenciable _value = sourceRef.getValue();
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_value);
    final String nodeFullyQualifiedName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    Attribute _first = toPair.getFirst();
    AttributeValue destinationValue = _first.getValue();
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(destinationValue, null));
    if (!_notEquals) {
      _and = false;
    } else {
      _and = (destinationValue instanceof XTReference);
    }
    if (_and) {
      XTReference destinationRef = ((XTReference) destinationValue);
      Referenciable _value_1 = destinationRef.getValue();
      String destinationName = XtendHelper.getReferenciableName(_value_1);
      Referenciable _value_2 = destinationRef.getValue();
      QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(_value_2);
      String destinationFullyQualifiedName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName_1);
      result.append("if(source instanceof ");
      result.append(nodeFullyQualifiedName);
      result.append(")\n  this.onBuildTo");
      result.append(destinationName);
      result.append("((");
      result.append(nodeFullyQualifiedName);
      result.append(")source, (");
      result.append(destinationFullyQualifiedName);
      result.append(")this.loadDatastore");
      result.append((("(" + destinationFullyQualifiedName) + ".class,\""));
      result.append(destinationFullyQualifiedName);
      result.append("\"));\n");
      EList<JvmMember> _members = behaviourClazz.getMembers();
      Method _second = toPair.getSecond();
      String _format = String.format("onBuildTo%s", destinationName);
      JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(definition);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          Method _second = toPair.getSecond();
          EList<JvmFormalParameter> _params = _second.getParams();
          for (final JvmFormalParameter p : _params) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            String _name = p.getName();
            JvmTypeReference _parameterType = p.getParameterType();
            JvmFormalParameter _parameter = DatastoreBuilderInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
            DatastoreBuilderInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          }
          Method _second_1 = toPair.getSecond();
          XExpression _body = _second_1.getBody();
          DatastoreBuilderInferer.this._monetJvmTypesBuilder.setBody(it, _body);
        }
      };
      JvmOperation _method = this._monetJvmTypesBuilder.toMethod(_second, _format, _resolveVoidType, _function);
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
    }
  }
}
