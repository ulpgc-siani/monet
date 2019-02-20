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
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.inferers.BaseTaskInferer;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.metamodel.Pair;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.PropertyFeature;

@SuppressWarnings("all")
public class TaskCustomerInferer extends BaseTaskInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  public void infer(final Definition taskDefinition, final Property customerProperty, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String className = this.classNameInferer.inferPropertyBehavior(customerProperty);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(customerProperty, className);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _resolveBehaviorTaskCustomerImplType = TaskCustomerInferer.this._typeRefCache.resolveBehaviorTaskCustomerImplType(customerProperty);
        TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveBehaviorTaskCustomerImplType);
        EList<JvmMember> _members = it.getMembers();
        String _inferBehaviourName = TaskCustomerInferer.this.classNameInferer.inferBehaviourName(taskDefinition);
        JvmTypeReference _resolve = TaskCustomerInferer.this._typeRefCache.resolve(taskDefinition, _inferBehaviourName);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("return (");
                String _inferBehaviourName = TaskCustomerInferer.this.classNameInferer.inferBehaviourName(taskDefinition);
                ap.append(_inferBehaviourName);
                ap.append(")this.getGenericTask();");
              }
            };
            TaskCustomerInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = TaskCustomerInferer.this._monetJvmTypesBuilder.toMethod(customerProperty, "getTask", _resolve, _function);
        TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        final HashMultimap<String, Pair<String, Integer>> methodMap = XtendHelper.createMultimap();
        EList<PropertyFeature> _features = customerProperty.getFeatures();
        final Procedure2<PropertyFeature, Integer> _function_1 = new Procedure2<PropertyFeature, Integer>() {
          public void apply(final PropertyFeature f, final Integer i) {
            boolean _matched = false;
            if (!_matched) {
              if (f instanceof Method) {
                _matched=true;
                EList<JvmMember> _members = it.getMembers();
                String _id = ((Method)f).getId();
                JvmTypeReference _resolveVoidType = TaskCustomerInferer.this._typeRefCache.resolveVoidType(f);
                final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                  public void apply(final JvmOperation it) {
                    EList<JvmFormalParameter> _params = ((Method)f).getParams();
                    for (final JvmFormalParameter param : _params) {
                      EList<JvmFormalParameter> _parameters = it.getParameters();
                      String _name = param.getName();
                      JvmTypeReference _parameterType = param.getParameterType();
                      JvmFormalParameter _parameter = TaskCustomerInferer.this._monetJvmTypesBuilder.toParameter(param, _name, _parameterType);
                      TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    }
                    XExpression _body = ((Method)f).getBody();
                    TaskCustomerInferer.this._monetJvmTypesBuilder.setBody(it, _body);
                  }
                };
                JvmOperation _method = TaskCustomerInferer.this._monetJvmTypesBuilder.toMethod(f, _id, _resolveVoidType, _function);
                TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
              }
            }
            if (!_matched) {
              if (f instanceof Property) {
                _matched=true;
                String _id = ((Property)f).getId();
                boolean _matched_1 = false;
                if (!_matched_1) {
                  if (Objects.equal(_id, "request")) {
                    _matched_1=true;
                    TaskCustomerInferer.this.inferMethod(((Property)f), it, "import", (i).intValue(), methodMap);
                  }
                }
                if (!_matched_1) {
                  if (Objects.equal(_id, "response")) {
                    _matched_1=true;
                    TaskCustomerInferer.this.inferMethod(((Property)f), it, "constructor", (i).intValue(), methodMap);
                  }
                }
                if (!_matched_1) {
                  if (Objects.equal(_id, "aborted")) {
                    _matched_1=true;
                    TaskCustomerInferer.this.inferUniqueMethod(((Property)f), it, "onExecute");
                  }
                }
                if (!_matched_1) {
                  if (Objects.equal(_id, "expiration")) {
                    _matched_1=true;
                    TaskCustomerInferer.this.inferUniqueMethod(((Property)f), it, "onExecute");
                  }
                }
              }
            }
          }
        };
        IterableExtensions.<PropertyFeature>forEach(_features, _function_1);
        EList<JvmMember> _members_1 = it.getMembers();
        JvmTypeReference _resolveVoidType = TaskCustomerInferer.this._typeRefCache.resolveVoidType(customerProperty);
        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = TaskCustomerInferer.this._typeRefCache.resolveStringType(taskDefinition);
            JvmFormalParameter _parameter = TaskCustomerInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "code", _resolveStringType);
            TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            EList<JvmFormalParameter> _parameters_1 = it.getParameters();
            JvmTypeReference _resolveCustomerRequestType = TaskCustomerInferer.this._typeRefCache.resolveCustomerRequestType(taskDefinition);
            JvmFormalParameter _parameter_1 = TaskCustomerInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveCustomerRequestType);
            TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable x) {
                Set<Pair<String, Integer>> declMethods = methodMap.get("import");
                x.append("int hash = code.hashCode();\n");
                TaskCustomerInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
              }
            };
            TaskCustomerInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_1 = TaskCustomerInferer.this._monetJvmTypesBuilder.toMethod(customerProperty, "onImportRequest", _resolveVoidType, _function_2);
        TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
        EList<JvmMember> _members_2 = it.getMembers();
        JvmTypeReference _resolveVoidType_1 = TaskCustomerInferer.this._typeRefCache.resolveVoidType(customerProperty);
        final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = TaskCustomerInferer.this._typeRefCache.resolveStringType(taskDefinition);
            JvmFormalParameter _parameter = TaskCustomerInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "code", _resolveStringType);
            TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            EList<JvmFormalParameter> _parameters_1 = it.getParameters();
            JvmTypeReference _resolveCustomerResponseType = TaskCustomerInferer.this._typeRefCache.resolveCustomerResponseType(taskDefinition);
            JvmFormalParameter _parameter_1 = TaskCustomerInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveCustomerResponseType);
            TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable x) {
                Set<Pair<String, Integer>> declMethods = methodMap.get("constructor");
                x.append("int hash = code.hashCode();\n");
                TaskCustomerInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
              }
            };
            TaskCustomerInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_2 = TaskCustomerInferer.this._monetJvmTypesBuilder.toMethod(customerProperty, "onConstructResponse", _resolveVoidType_1, _function_3);
        TaskCustomerInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
      }
    };
    _accept.initializeLater(_function);
  }
}
