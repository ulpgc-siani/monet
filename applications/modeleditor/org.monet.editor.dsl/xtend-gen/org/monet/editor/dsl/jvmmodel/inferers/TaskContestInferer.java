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
public class TaskContestInferer extends BaseTaskInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  public void infer(final Definition taskDefinition, final Property contestProperty, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String className = this.classNameInferer.inferPropertyBehavior(contestProperty);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(contestProperty, className);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _resolveBehaviorTaskContestImplType = TaskContestInferer.this._typeRefCache.resolveBehaviorTaskContestImplType(contestProperty);
        TaskContestInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveBehaviorTaskContestImplType);
        EList<JvmMember> _members = it.getMembers();
        String _inferBehaviourName = TaskContestInferer.this.classNameInferer.inferBehaviourName(taskDefinition);
        JvmTypeReference _resolve = TaskContestInferer.this._typeRefCache.resolve(taskDefinition, _inferBehaviourName);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("return (");
                String _inferBehaviourName = TaskContestInferer.this.classNameInferer.inferBehaviourName(taskDefinition);
                ap.append(_inferBehaviourName);
                ap.append(")this.getGenericTask();");
              }
            };
            TaskContestInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = TaskContestInferer.this._monetJvmTypesBuilder.toMethod(contestProperty, "getTask", _resolve, _function);
        TaskContestInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        final HashMultimap<String, Pair<String, Integer>> methodMap = XtendHelper.createMultimap();
        EList<PropertyFeature> _features = contestProperty.getFeatures();
        final Procedure2<PropertyFeature, Integer> _function_1 = new Procedure2<PropertyFeature, Integer>() {
          public void apply(final PropertyFeature f, final Integer i) {
            boolean _matched = false;
            if (!_matched) {
              if (f instanceof Method) {
                _matched=true;
                EList<JvmMember> _members = it.getMembers();
                String _id = ((Method)f).getId();
                JvmTypeReference _resolveVoidType = TaskContestInferer.this._typeRefCache.resolveVoidType(f);
                final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                  public void apply(final JvmOperation it) {
                    EList<JvmFormalParameter> _params = ((Method)f).getParams();
                    for (final JvmFormalParameter param : _params) {
                      EList<JvmFormalParameter> _parameters = it.getParameters();
                      String _name = param.getName();
                      JvmTypeReference _parameterType = param.getParameterType();
                      JvmFormalParameter _parameter = TaskContestInferer.this._monetJvmTypesBuilder.toParameter(param, _name, _parameterType);
                      TaskContestInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    }
                    XExpression _body = ((Method)f).getBody();
                    TaskContestInferer.this._monetJvmTypesBuilder.setBody(it, _body);
                  }
                };
                JvmOperation _method = TaskContestInferer.this._monetJvmTypesBuilder.toMethod(f, _id, _resolveVoidType, _function);
                TaskContestInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
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
                    TaskContestInferer.this.inferMethod(((Property)f), it, "constructor", (i).intValue(), methodMap);
                  }
                }
                if (!_matched_1) {
                  if (Objects.equal(_id, "response")) {
                    _matched_1=true;
                    TaskContestInferer.this.inferMethod(((Property)f), it, "import", (i).intValue(), methodMap);
                  }
                }
                if (!_matched_1) {
                  if (Objects.equal(_id, "aborted")) {
                    _matched_1=true;
                    TaskContestInferer.this.inferUniqueMethod(((Property)f), it, "onExecute");
                  }
                }
              }
            }
          }
        };
        IterableExtensions.<PropertyFeature>forEach(_features, _function_1);
        EList<JvmMember> _members_1 = it.getMembers();
        JvmTypeReference _resolveVoidType = TaskContestInferer.this._typeRefCache.resolveVoidType(contestProperty);
        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = TaskContestInferer.this._typeRefCache.resolveStringType(taskDefinition);
            JvmFormalParameter _parameter = TaskContestInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "code", _resolveStringType);
            TaskContestInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            EList<JvmFormalParameter> _parameters_1 = it.getParameters();
            JvmTypeReference _resolveTransferRequestType = TaskContestInferer.this._typeRefCache.resolveTransferRequestType(taskDefinition);
            JvmFormalParameter _parameter_1 = TaskContestInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveTransferRequestType);
            TaskContestInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable x) {
                Set<Pair<String, Integer>> declMethods = methodMap.get("constructor");
                x.append("int hash = code.hashCode();\n");
                TaskContestInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
              }
            };
            TaskContestInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_1 = TaskContestInferer.this._monetJvmTypesBuilder.toMethod(contestProperty, "onConstructRequest", _resolveVoidType, _function_2);
        TaskContestInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
        EList<JvmMember> _members_2 = it.getMembers();
        JvmTypeReference _resolveVoidType_1 = TaskContestInferer.this._typeRefCache.resolveVoidType(contestProperty);
        final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = TaskContestInferer.this._typeRefCache.resolveStringType(taskDefinition);
            JvmFormalParameter _parameter = TaskContestInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "code", _resolveStringType);
            TaskContestInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            EList<JvmFormalParameter> _parameters_1 = it.getParameters();
            JvmTypeReference _resolveTransferResponseType = TaskContestInferer.this._typeRefCache.resolveTransferResponseType(taskDefinition);
            JvmFormalParameter _parameter_1 = TaskContestInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveTransferResponseType);
            TaskContestInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable x) {
                Set<Pair<String, Integer>> declMethods = methodMap.get("import");
                x.append("int hash = code.hashCode();\n");
                TaskContestInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
              }
            };
            TaskContestInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_2 = TaskContestInferer.this._monetJvmTypesBuilder.toMethod(contestProperty, "onImportResponse", _resolveVoidType_1, _function_3);
        TaskContestInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
      }
    };
    _accept.initializeLater(_function);
  }
}
