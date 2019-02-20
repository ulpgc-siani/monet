package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.inject.Inject;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.metamodel.Pair;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.Property;

@SuppressWarnings("all")
public class BaseTaskInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private MMLExtensions _mMLExtensions;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  protected void inferSwitchMethodBody(final ITreeAppendable x, final String switchParam, final Set<Pair<String, Integer>> methods) {
    boolean _notEquals = (!Objects.equal(methods, null));
    if (_notEquals) {
      x.append("switch(");
      x.append(switchParam);
      x.append(") {");
      for (final Pair<String, Integer> m : methods) {
        {
          x.append("case ");
          Integer _second = m.getSecond();
          String _valueOf = String.valueOf(_second);
          x.append(_valueOf);
          x.append(" :\n  ");
          String _first = m.getFirst();
          x.append(_first);
          x.append("(");
          boolean isFirst = true;
          Object[] _extra = m.getExtra();
          Object _get = _extra[0];
          EList<JvmFormalParameter> extraParams = ((EList<JvmFormalParameter>) _get);
          int i = 0;
          for (final JvmFormalParameter extra : extraParams) {
            {
              JvmFormalParameter p = ((JvmFormalParameter) extra);
              if (isFirst) {
                isFirst = false;
              } else {
                x.append(",");
              }
              x.append("(");
              JvmTypeReference _parameterType = p.getParameterType();
              String _qualifiedName = _parameterType.getQualifiedName();
              x.append(_qualifiedName);
              x.append(") p");
              String _valueOf_1 = String.valueOf(i);
              x.append(_valueOf_1);
              i = (i + 1);
            }
          }
          x.append(");\n");
          x.append("break;\n");
        }
      }
      x.append("}");
    }
  }
  
  protected void inferMethod(final Property p, final JvmGenericType type, final String methodName, final int index, final HashMultimap<String, Pair<String, Integer>> methodMap) {
    final Method pm = this._mMLExtensions.getMethod(p, methodName);
    boolean _notEquals = (!Objects.equal(pm, null));
    if (_notEquals) {
      Attribute _attribute = this._mMLExtensions.getAttribute(p, "code");
      String code = this._mMLExtensions.getValueAsString(_attribute);
      String _id = pm.getId();
      String name = (_id + Integer.valueOf(index));
      String _id_1 = pm.getId();
      int _hashCode = code.hashCode();
      EList<JvmFormalParameter> _params = pm.getParams();
      Pair<String, Integer> _pair = new Pair<String, Integer>(name, Integer.valueOf(_hashCode), _params);
      methodMap.put(_id_1, _pair);
      EList<JvmMember> _members = type.getMembers();
      JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(pm);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          XExpression _body = pm.getBody();
          BaseTaskInferer.this._monetJvmTypesBuilder.setBody(it, _body);
          EList<JvmFormalParameter> _params = pm.getParams();
          for (final JvmFormalParameter param : _params) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            String _name = param.getName();
            JvmTypeReference _parameterType = param.getParameterType();
            JvmFormalParameter _parameter = BaseTaskInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
            BaseTaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          }
          it.setVisibility(JvmVisibility.PRIVATE);
        }
      };
      JvmOperation _method = this._monetJvmTypesBuilder.toMethod(pm, name, _resolveVoidType, _function);
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
    }
  }
  
  protected void inferUniqueMethod(final Property p, final JvmGenericType type, final String methodName) {
    final Method pm = this._mMLExtensions.getMethod(p, methodName);
    boolean _notEquals = (!Objects.equal(pm, null));
    if (_notEquals) {
      EList<JvmMember> _members = type.getMembers();
      String _id = p.getId();
      String _plus = ("on-" + _id);
      String _attributeJavaIdentifier = JavaHelper.toAttributeJavaIdentifier(_plus);
      JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(pm);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          XExpression _body = pm.getBody();
          BaseTaskInferer.this._monetJvmTypesBuilder.setBody(it, _body);
          EList<JvmFormalParameter> _params = pm.getParams();
          for (final JvmFormalParameter param : _params) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            String _name = param.getName();
            JvmTypeReference _parameterType = param.getParameterType();
            JvmFormalParameter _parameter = BaseTaskInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
            BaseTaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          }
          it.setVisibility(JvmVisibility.PUBLIC);
        }
      };
      JvmOperation _method = this._monetJvmTypesBuilder.toMethod(pm, _attributeJavaIdentifier, _resolveVoidType, _function);
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
    }
  }
}
