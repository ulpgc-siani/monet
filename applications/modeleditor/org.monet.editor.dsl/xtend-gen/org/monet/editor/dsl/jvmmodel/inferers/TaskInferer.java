package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.jvmmodel.inferers.BaseTaskInferer;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.jvmmodel.inferers.TaskContestInferer;
import org.monet.editor.dsl.jvmmodel.inferers.TaskContestantsInferer;
import org.monet.editor.dsl.jvmmodel.inferers.TaskCustomerInferer;
import org.monet.editor.dsl.jvmmodel.inferers.TaskProviderExternalInferer;
import org.monet.editor.dsl.jvmmodel.inferers.TaskProviderInternalInferer;
import org.monet.editor.dsl.metamodel.Pair;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.Property;

@SuppressWarnings("all")
public class TaskInferer extends BaseTaskInferer {
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
  private TaskContestInferer contestInferer;
  
  @Inject
  private TaskCustomerInferer customerInferer;
  
  @Inject
  private TaskContestantsInferer contestantsInferer;
  
  @Inject
  private TaskProviderExternalInferer providerExternalInferer;
  
  @Inject
  private TaskProviderInternalInferer providerInternalInferer;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  public void inferClasses(final Definition taskDefinition, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(taskDefinition);
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property p) {
        String _id = p.getId();
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(_id, "contest")) {
            _matched=true;
            TaskInferer.this.contestInferer.infer(taskDefinition, p, acceptor, prelinkingPhase);
          }
        }
        if (!_matched) {
          if (Objects.equal(_id, "customer")) {
            _matched=true;
            TaskInferer.this.customerInferer.infer(taskDefinition, p, acceptor, prelinkingPhase);
          }
        }
        if (!_matched) {
          if (Objects.equal(_id, "contestants")) {
            _matched=true;
            TaskInferer.this.contestantsInferer.infer(taskDefinition, p, acceptor, prelinkingPhase);
          }
        }
        if (!_matched) {
          if (Objects.equal(_id, "provider")) {
            _matched=true;
            Iterable<Property> _properties = TaskInferer.this._mMLExtensions.getProperties(p);
            final Consumer<Property> _function = new Consumer<Property>() {
              public void accept(final Property px) {
                String _id = px.getId();
                boolean _matched = false;
                if (!_matched) {
                  if (Objects.equal(_id, "internal")) {
                    _matched=true;
                    TaskInferer.this.providerInternalInferer.infer(taskDefinition, px, acceptor, prelinkingPhase);
                  }
                }
                if (!_matched) {
                  if (Objects.equal(_id, "external")) {
                    _matched=true;
                    TaskInferer.this.providerExternalInferer.infer(taskDefinition, px, acceptor, prelinkingPhase);
                  }
                }
              }
            };
            _properties.forEach(_function);
          }
        }
        if (!_matched) {
          if (Objects.equal(_id, "place")) {
            _matched=true;
          }
        }
      }
    };
    _properties.forEach(_function);
    String className = this.classNameInferer.inferTaskLockClass(taskDefinition);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(taskDefinition, className);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _resolveLockType = TaskInferer.this._typeRefCache.resolveLockType(taskDefinition);
        TaskInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveLockType);
        EList<JvmMember> _members = it.getMembers();
        final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
          public void apply(final JvmConstructor it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
            JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "place", _resolveStringType);
            TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            EList<JvmFormalParameter> _parameters_1 = it.getParameters();
            JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
            JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "id", _resolveStringType_1);
            TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("super(place, id);");
              }
            };
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmConstructor _constructor = TaskInferer.this._monetJvmTypesBuilder.toConstructor(taskDefinition, _function);
        TaskInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
        Iterable<Property> _properties = TaskInferer.this._mMLExtensions.getProperties(taskDefinition, "place");
        final Function1<Property, Iterable<Property>> _function_1 = new Function1<Property, Iterable<Property>>() {
          public Iterable<Property> apply(final Property p) {
            Iterable<Property> _properties = TaskInferer.this._mMLExtensions.getProperties(p, "door", "line");
            final Function1<Property, Iterable<Property>> _function = new Function1<Property, Iterable<Property>>() {
              public Iterable<Property> apply(final Property px) {
                return TaskInferer.this._mMLExtensions.getProperties(px, "exit", "stop");
              }
            };
            Iterable<Iterable<Property>> _map = IterableExtensions.<Property, Iterable<Property>>map(_properties, _function);
            return Iterables.<Property>concat(_map);
          }
        };
        Iterable<Iterable<Property>> _map = IterableExtensions.<Property, Iterable<Property>>map(_properties, _function_1);
        Iterable<Property> _flatten = Iterables.<Property>concat(_map);
        final Consumer<Property> _function_2 = new Consumer<Property>() {
          public void accept(final Property exit) {
            EObject _eContainer = exit.eContainer();
            EObject _eContainer_1 = _eContainer.eContainer();
            Property place = ((Property) _eContainer_1);
            final String placeName = place.getName();
            Code _code = place.getCode();
            boolean _notEquals = (!Objects.equal(_code, null));
            if (_notEquals) {
              Code _code_1 = place.getCode();
              final String placeCode = _code_1.getValue();
              EList<JvmMember> _members = it.getMembers();
              String _name = exit.getName();
              String _plus = ((placeName + "_") + _name);
              JvmTypeReference _newTypeRef = TaskInferer.this._monetJvmTypesBuilder.newTypeRef(it);
              final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
                public void apply(final JvmField it) {
                  it.setStatic(true);
                  it.setVisibility(JvmVisibility.PUBLIC);
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("new Lock(\"");
                      ap.append(placeCode);
                      ap.append("\",\"");
                      Code _code = exit.getCode();
                      boolean _notEquals = (!Objects.equal(_code, null));
                      if (_notEquals) {
                        Code _code_1 = exit.getCode();
                        String _value = _code_1.getValue();
                        ap.append(_value);
                      }
                      ap.append("\")");
                    }
                  };
                  TaskInferer.this._monetJvmTypesBuilder.setInitializer(it, _function);
                }
              };
              JvmField _field = TaskInferer.this._monetJvmTypesBuilder.toField(exit, _plus, _newTypeRef, _function);
              TaskInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, _field);
            }
          }
        };
        _flatten.forEach(_function_2);
        Iterable<Property> _properties_1 = TaskInferer.this._mMLExtensions.getProperties(taskDefinition, "place");
        final Function1<Property, Boolean> _function_3 = new Function1<Property, Boolean>() {
          public Boolean apply(final Property p) {
            boolean _hasProperties = TaskInferer.this._mMLExtensions.hasProperties(p, "door", "line");
            return Boolean.valueOf((!_hasProperties));
          }
        };
        Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties_1, _function_3);
        final Consumer<Property> _function_4 = new Consumer<Property>() {
          public void accept(final Property place) {
            final String placeName = place.getName();
            final Code placeCode = place.getCode();
            EList<JvmMember> _members = it.getMembers();
            JvmTypeReference _newTypeRef = TaskInferer.this._monetJvmTypesBuilder.newTypeRef(it);
            final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
              public void apply(final JvmField it) {
                it.setStatic(true);
                it.setVisibility(JvmVisibility.PUBLIC);
                final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                  public void apply(final ITreeAppendable ap) {
                    String codeValue = null;
                    boolean _notEquals = (!Objects.equal(placeCode, null));
                    if (_notEquals) {
                      String _value = placeCode.getValue();
                      codeValue = _value;
                    }
                    ap.append("new Lock(\"");
                    ap.append(codeValue);
                    ap.append("\",\"");
                    ap.append(codeValue);
                    ap.append("\")");
                  }
                };
                TaskInferer.this._monetJvmTypesBuilder.setInitializer(it, _function);
              }
            };
            JvmField _field = TaskInferer.this._monetJvmTypesBuilder.toField(place, placeName, _newTypeRef, _function);
            TaskInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, _field);
          }
        };
        _filter.forEach(_function_4);
      }
    };
    _accept.initializeLater(_function_1);
    String _inferTaskPlaceClass = this.classNameInferer.inferTaskPlaceClass(taskDefinition);
    JvmGenericType _class_1 = this._monetJvmTypesBuilder.toClass(taskDefinition, _inferTaskPlaceClass);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept_1 = acceptor.<JvmGenericType>accept(_class_1);
    final Procedure1<JvmGenericType> _function_2 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmMember> _members = it.getMembers();
        final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
          public void apply(final JvmConstructor it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
            JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "id", _resolveStringType);
            TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("this.id = id;");
              }
            };
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmConstructor _constructor = TaskInferer.this._monetJvmTypesBuilder.toConstructor(taskDefinition, _function);
        TaskInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
        JvmTypeReference idFieldType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        EList<JvmMember> _members_1 = it.getMembers();
        JvmField _field = TaskInferer.this._monetJvmTypesBuilder.toField(taskDefinition, "id", idFieldType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members_1, _field);
        EList<JvmMember> _members_2 = it.getMembers();
        JvmOperation _getter = TaskInferer.this._monetJvmTypesBuilder.toGetter(taskDefinition, "id", idFieldType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _getter);
        Iterable<Property> _properties = TaskInferer.this._mMLExtensions.getProperties(taskDefinition);
        final Consumer<Property> _function_1 = new Consumer<Property>() {
          public void accept(final Property p) {
            String _id = p.getId();
            boolean _matched = false;
            if (!_matched) {
              if (Objects.equal(_id, "place")) {
                _matched=true;
                EList<JvmMember> _members = it.getMembers();
                String _name = p.getName();
                JvmTypeReference _newTypeRef = TaskInferer.this._monetJvmTypesBuilder.newTypeRef(it);
                final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
                  public void apply(final JvmField it) {
                    it.setStatic(true);
                    it.setVisibility(JvmVisibility.PUBLIC);
                    final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                      public void apply(final ITreeAppendable ap) {
                        ap.append("new Place(\"");
                        String _name = p.getName();
                        ap.append(_name);
                        ap.append("\")");
                      }
                    };
                    TaskInferer.this._monetJvmTypesBuilder.setInitializer(it, _function);
                  }
                };
                JvmField _field = TaskInferer.this._monetJvmTypesBuilder.toField(p, _name, _newTypeRef, _function);
                TaskInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, _field);
              }
            }
          }
        };
        _properties.forEach(_function_1);
      }
    };
    _accept_1.initializeLater(_function_2);
  }
  
  public void addAllMethods(final Property p, final ArrayList<Method> methodList) {
    Iterable<Method> _methods = this._mMLExtensions.getMethods(p);
    Iterables.<Method>addAll(methodList, _methods);
    Iterable<Property> _properties = this._mMLExtensions.getProperties(p);
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property px) {
        TaskInferer.this.addAllMethods(px, methodList);
      }
    };
    _properties.forEach(_function);
  }
  
  public void inferMethods(final Definition taskDefinition, final JvmGenericType behaviourClazz) {
    EList<JvmMember> _members = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        String lockClassName = TaskInferer.this.classNameInferer.inferTaskLockClass(taskDefinition);
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolve = TaskInferer.this._typeRefCache.resolve(taskDefinition, lockClassName);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "lock", _resolve);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("this.unlock(lock);");
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method = this._monetJvmTypesBuilder.toMethod(taskDefinition, "doUnlock", _resolveVoidType, _function);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
    EList<JvmMember> _members_1 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_1 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        String lockClassName = TaskInferer.this.classNameInferer.inferTaskLockClass(taskDefinition);
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolve = TaskInferer.this._typeRefCache.resolve(taskDefinition, lockClassName);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "lock", _resolve);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("this.lock(lock);");
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_1 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "doLock", _resolveVoidType_1, _function_1);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
    EList<JvmMember> _members_2 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_2 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        String placeClassName = TaskInferer.this.classNameInferer.inferTaskPlaceClass(taskDefinition);
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolve = TaskInferer.this._typeRefCache.resolve(taskDefinition, placeClassName);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "place", _resolve);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "historyText", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("this._goto(place.getId(), historyText);");
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_2 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "doGoto", _resolveVoidType_2, _function_2);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
    this.inferCreateInstance(taskDefinition, behaviourClazz);
    final ArrayList<Method> propertyMethods = new ArrayList<Method>();
    Iterable<Property> _properties = this._mMLExtensions.getProperties(taskDefinition, "place");
    final Consumer<Property> _function_3 = new Consumer<Property>() {
      public void accept(final Property p) {
        TaskInferer.this.addAllMethods(p, propertyMethods);
      }
    };
    _properties.forEach(_function_3);
    final HashMultimap<String, Pair<String, Integer>> methodMap = XtendHelper.createMultimap();
    final Procedure2<Method, Integer> _function_4 = new Procedure2<Method, Integer>() {
      public void apply(final Method pm, final Integer i) {
        EObject _eContainer = pm.eContainer();
        Property parent = ((Property) _eContainer);
        String _id = pm.getId();
        String name = (_id + i);
        EList<JvmMember> _members = behaviourClazz.getMembers();
        JvmTypeReference _resolveVoidType = TaskInferer.this._typeRefCache.resolveVoidType(pm);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            XExpression _body = pm.getBody();
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _body);
            EList<JvmFormalParameter> _params = pm.getParams();
            for (final JvmFormalParameter p : _params) {
              EList<JvmFormalParameter> _parameters = it.getParameters();
              String _name = p.getName();
              JvmTypeReference _parameterType = p.getParameterType();
              JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
              TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            }
            it.setVisibility(JvmVisibility.PRIVATE);
          }
        };
        JvmOperation _method = TaskInferer.this._monetJvmTypesBuilder.toMethod(pm, name, _resolveVoidType, _function);
        TaskInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        StringBuilder parentName = new StringBuilder();
        while ((!Objects.equal(parent.getId(), "place"))) {
          {
            Code _code = parent.getCode();
            boolean _notEquals = (!Objects.equal(_code, null));
            if (_notEquals) {
              Code _code_1 = parent.getCode();
              String _value = _code_1.getValue();
              parentName.append(_value);
            }
            EObject _eContainer_1 = parent.eContainer();
            parent = ((Property) _eContainer_1);
          }
        }
        Code _code = parent.getCode();
        boolean _notEquals = (!Objects.equal(_code, null));
        if (_notEquals) {
          Code _code_1 = parent.getCode();
          String _value = _code_1.getValue();
          parentName.append(_value);
        }
        String _id_1 = pm.getId();
        String _string = parentName.toString();
        int _hashCode = _string.hashCode();
        EList<JvmFormalParameter> _params = pm.getParams();
        Pair<String, Integer> _pair = new Pair<String, Integer>(name, Integer.valueOf(_hashCode), _params);
        methodMap.put(_id_1, _pair);
      }
    };
    IterableExtensions.<Method>forEach(propertyMethods, _function_4);
    EList<JvmMember> _members_3 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_3 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_5 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onArrive");
            x.append("int hash = (placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_3 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onArrivePlace", _resolveVoidType_3, _function_5);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_3);
    EList<JvmMember> _members_4 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_4 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_6 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onTimeout");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_4 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onTimeoutPlace", _resolveVoidType_4, _function_6);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_4);
    EList<JvmMember> _members_5 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_5 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_7 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveStringType_2 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "routeCode", _resolveStringType_2);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onTake");
            x.append("int hash = (routeCode + actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_5 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onTakePlace", _resolveVoidType_5, _function_7);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_5);
    EList<JvmMember> _members_6 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_6 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_8 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveNodeType = TaskInferer.this._typeRefCache.resolveNodeType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveNodeType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSolve");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_6 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSolveAction", _resolveVoidType_6, _function_8);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_6);
    EList<JvmMember> _members_7 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_7 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_9 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveJobRequestType = TaskInferer.this._typeRefCache.resolveJobRequestType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveJobRequestType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onCreate");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_7 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onCreateJobAction", _resolveVoidType_7, _function_9);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_7);
    EList<JvmMember> _members_8 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_8 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_10 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveJobType = TaskInferer.this._typeRefCache.resolveJobType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveJobType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onCreated");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_8 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onCreatedJobAction", _resolveVoidType_8, _function_10);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_8);
    EList<JvmMember> _members_9 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_9 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_11 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveJobResponseType = TaskInferer.this._typeRefCache.resolveJobResponseType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveJobResponseType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onFinished");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_9 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onFinishedJobAction", _resolveVoidType_9, _function_11);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_9);
    EList<JvmMember> _members_10 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_10 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_12 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveRoleChooserType = TaskInferer.this._typeRefCache.resolveRoleChooserType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveRoleChooserType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSelectJobRole");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_10 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSelectJobRole", _resolveVoidType_10, _function_12);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_10);
    EList<JvmMember> _members_11 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_11 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_13 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveRoleType = TaskInferer.this._typeRefCache.resolveRoleType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveRoleType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSelectJobRoleComplete");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_11 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSelectJobRoleComplete", _resolveVoidType_11, _function_13);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_11);
    EList<JvmMember> _members_12 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_12 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_14 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveJobSetupType = TaskInferer.this._typeRefCache.resolveJobSetupType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveJobSetupType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSetupJob");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_12 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSetupJob", _resolveVoidType_12, _function_14);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_12);
    EList<JvmMember> _members_13 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_13 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_15 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveStringType_2 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveStringType_2);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        EList<JvmFormalParameter> _parameters_3 = it.getParameters();
        JvmTypeReference _resolveDateType = TaskInferer.this._typeRefCache.resolveDateType(taskDefinition);
        JvmFormalParameter _parameter_3 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p1", _resolveDateType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
        EList<JvmFormalParameter> _parameters_4 = it.getParameters();
        JvmTypeReference _resolveDateType_1 = TaskInferer.this._typeRefCache.resolveDateType(taskDefinition);
        JvmFormalParameter _parameter_4 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p2", _resolveDateType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
        EList<JvmFormalParameter> _parameters_5 = it.getParameters();
        JvmTypeReference _resolveStringType_3 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_5 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p3", _resolveStringType_3);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_5, _parameter_5);
        EList<JvmFormalParameter> _parameters_6 = it.getParameters();
        JvmTypeReference _resolveBooleanPrimitiveType = TaskInferer.this._typeRefCache.resolveBooleanPrimitiveType(taskDefinition);
        JvmFormalParameter _parameter_6 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p4", _resolveBooleanPrimitiveType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_6, _parameter_6);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSetupJobComplete");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_13 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSetupJobComplete", _resolveVoidType_13, _function_15);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_13);
    EList<JvmMember> _members_14 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_14 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_16 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveRoleChooserType = TaskInferer.this._typeRefCache.resolveRoleChooserType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveRoleChooserType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSelectRole");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_14 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSelectDelegationRole", _resolveVoidType_14, _function_16);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_14, _method_14);
    EList<JvmMember> _members_15 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_15 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_17 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveDelegationSetupType = TaskInferer.this._typeRefCache.resolveDelegationSetupType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveDelegationSetupType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSetup");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_15 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSetupDelegation", _resolveVoidType_15, _function_17);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_15, _method_15);
    EList<JvmMember> _members_16 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_16 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_18 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveStringType_2 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveStringType_2);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        EList<JvmFormalParameter> _parameters_3 = it.getParameters();
        JvmTypeReference _resolveDateType = TaskInferer.this._typeRefCache.resolveDateType(taskDefinition);
        JvmFormalParameter _parameter_3 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p1", _resolveDateType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
        EList<JvmFormalParameter> _parameters_4 = it.getParameters();
        JvmTypeReference _resolveDateType_1 = TaskInferer.this._typeRefCache.resolveDateType(taskDefinition);
        JvmFormalParameter _parameter_4 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p2", _resolveDateType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
        EList<JvmFormalParameter> _parameters_5 = it.getParameters();
        JvmTypeReference _resolveStringType_3 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_5 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p3", _resolveStringType_3);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_5, _parameter_5);
        EList<JvmFormalParameter> _parameters_6 = it.getParameters();
        JvmTypeReference _resolveBooleanPrimitiveType = TaskInferer.this._typeRefCache.resolveBooleanPrimitiveType(taskDefinition);
        JvmFormalParameter _parameter_6 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p4", _resolveBooleanPrimitiveType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_6, _parameter_6);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSetupComplete");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_16 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSetupDelegationComplete", _resolveVoidType_16, _function_18);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_16, _method_16);
    EList<JvmMember> _members_17 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_17 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_19 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveWaitSetupType = TaskInferer.this._typeRefCache.resolveWaitSetupType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveWaitSetupType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSetup");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_17 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSetupWait", _resolveVoidType_17, _function_19);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_17, _method_17);
    EList<JvmMember> _members_18 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_18 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_20 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveTimeoutSetupType = TaskInferer.this._typeRefCache.resolveTimeoutSetupType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveTimeoutSetupType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSetup");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_18 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSetupTimeout", _resolveVoidType_18, _function_20);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_18, _method_18);
    EList<JvmMember> _members_19 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_19 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_21 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveNodeType = TaskInferer.this._typeRefCache.resolveNodeType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveNodeType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onSetup");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_19 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onSetupEdition", _resolveVoidType_19, _function_21);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_19, _method_19);
    EList<JvmMember> _members_20 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_20 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_22 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        EList<JvmFormalParameter> _parameters_2 = it.getParameters();
        JvmTypeReference _resolveNodeType = TaskInferer.this._typeRefCache.resolveNodeType(taskDefinition);
        JvmFormalParameter _parameter_2 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p0", _resolveNodeType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
        EList<JvmFormalParameter> _parameters_3 = it.getParameters();
        JvmTypeReference _resolveValidationResultType = TaskInferer.this._typeRefCache.resolveValidationResultType(taskDefinition);
        JvmFormalParameter _parameter_3 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "p1", _resolveValidationResultType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onValidate");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_20 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onValidateForm", _resolveVoidType_20, _function_22);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_20, _method_20);
    EList<JvmMember> _members_21 = behaviourClazz.getMembers();
    JvmTypeReference _resolveBooleanPrimitiveType = this._typeRefCache.resolveBooleanPrimitiveType(taskDefinition);
    final Procedure1<JvmOperation> _function_23 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            x.append("int hash = (actionCode + placeCode).hashCode();\nswitch(hash) {\n");
            Iterable<Property> _properties = TaskInferer.this._mMLExtensions.getProperties(taskDefinition, "place");
            final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
              public Boolean apply(final Property p) {
                return Boolean.valueOf(TaskInferer.this._mMLExtensions.hasProperty(p, "back-enable"));
              }
            };
            Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties, _function);
            final Function1<Property, Property> _function_1 = new Function1<Property, Property>() {
              public Property apply(final Property p) {
                return TaskInferer.this._mMLExtensions.getProperty(p, "back-enable");
              }
            };
            Iterable<Property> _map = IterableExtensions.<Property, Property>map(_filter, _function_1);
            final Consumer<Property> _function_2 = new Consumer<Property>() {
              public void accept(final Property p) {
                EObject _eContainer = p.eContainer();
                Property place = ((Property) _eContainer);
                Code _code = place.getCode();
                String placeCode = _code.getValue();
                String placeName = place.getName();
                Attribute _attribute = TaskInferer.this._mMLExtensions.getAttribute(p, "code");
                String actionCode = TaskInferer.this._mMLExtensions.getValueAsString(_attribute);
                final Attribute whenAttribute = TaskInferer.this._mMLExtensions.getAttribute(p, "when");
                boolean _and = false;
                boolean _notEquals = (!Objects.equal(whenAttribute, null));
                if (!_notEquals) {
                  _and = false;
                } else {
                  AttributeValue _value = whenAttribute.getValue();
                  _and = (_value instanceof ExpressionLiteral);
                }
                if (_and) {
                  x.append("case ");
                  int _hashCode = (actionCode + placeCode).hashCode();
                  String _valueOf = String.valueOf(_hashCode);
                  x.append(_valueOf);
                  x.append(": return isBackEnableWhen");
                  x.append(placeName);
                  x.append("();\n");
                }
              }
            };
            _map.forEach(_function_2);
            x.append("}\n return false;");
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_21 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onIsBackEnable", _resolveBooleanPrimitiveType, _function_23);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_21, _method_21);
    EList<JvmMember> _members_22 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType_21 = this._typeRefCache.resolveVoidType(taskDefinition);
    final Procedure1<JvmOperation> _function_24 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            Set<Pair<String, Integer>> declMethods = methodMap.get("onBack");
            x.append("int hash = (actionCode + placeCode).hashCode();\n");
            TaskInferer.this.inferSwitchMethodBody(x, "hash", declMethods);
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_22 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onBack", _resolveVoidType_21, _function_24);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_22, _method_22);
    EList<JvmMember> _members_23 = behaviourClazz.getMembers();
    JvmTypeReference _resolveStringType = this._typeRefCache.resolveStringType(taskDefinition);
    final Procedure1<JvmOperation> _function_25 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "placeCode", _resolveStringType);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveStringType_1 = TaskInferer.this._typeRefCache.resolveStringType(taskDefinition);
        JvmFormalParameter _parameter_1 = TaskInferer.this._monetJvmTypesBuilder.toParameter(taskDefinition, "actionCode", _resolveStringType_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            x.append("int hash = (actionCode + placeCode).hashCode();\nswitch(hash) {\n");
            Iterable<Property> _properties = TaskInferer.this._mMLExtensions.getProperties(taskDefinition, "place");
            final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
              public Boolean apply(final Property p) {
                return Boolean.valueOf(TaskInferer.this._mMLExtensions.hasProperty(p, "enroll"));
              }
            };
            Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties, _function);
            final Function1<Property, Property> _function_1 = new Function1<Property, Property>() {
              public Property apply(final Property p) {
                return TaskInferer.this._mMLExtensions.getProperty(p, "enroll");
              }
            };
            Iterable<Property> _map = IterableExtensions.<Property, Property>map(_filter, _function_1);
            final Consumer<Property> _function_2 = new Consumer<Property>() {
              public void accept(final Property p) {
                EObject _eContainer = p.eContainer();
                Property place = ((Property) _eContainer);
                Code _code = place.getCode();
                String placeCode = _code.getValue();
                String placeName = place.getName();
                Attribute _attribute = TaskInferer.this._mMLExtensions.getAttribute(p, "code");
                String actionCode = TaskInferer.this._mMLExtensions.getValueAsString(_attribute);
                final Attribute classificateAttribute = TaskInferer.this._mMLExtensions.getAttribute(p, "classificate");
                boolean _and = false;
                boolean _notEquals = (!Objects.equal(classificateAttribute, null));
                if (!_notEquals) {
                  _and = false;
                } else {
                  AttributeValue _value = classificateAttribute.getValue();
                  _and = (_value instanceof ExpressionLiteral);
                }
                if (_and) {
                  x.append("case ");
                  int _hashCode = (actionCode + placeCode).hashCode();
                  String _valueOf = String.valueOf(_hashCode);
                  x.append(_valueOf);
                  x.append(": return classificate");
                  x.append(placeName);
                  x.append("();\n");
                }
              }
            };
            _map.forEach(_function_2);
            x.append("}\n return null;");
          }
        };
        TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method_23 = this._monetJvmTypesBuilder.toMethod(taskDefinition, "onCalculateClassificatorPlace", _resolveStringType, _function_25);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_23, _method_23);
    Iterable<Property> _properties_1 = this._mMLExtensions.getProperties(taskDefinition, "place");
    final Function1<Property, Boolean> _function_26 = new Function1<Property, Boolean>() {
      public Boolean apply(final Property p) {
        return Boolean.valueOf(TaskInferer.this._mMLExtensions.hasProperty(p, "enroll"));
      }
    };
    Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties_1, _function_26);
    final Function1<Property, Property> _function_27 = new Function1<Property, Property>() {
      public Property apply(final Property p) {
        return TaskInferer.this._mMLExtensions.getProperty(p, "enroll");
      }
    };
    Iterable<Property> _map = IterableExtensions.<Property, Property>map(_filter, _function_27);
    final Consumer<Property> _function_28 = new Consumer<Property>() {
      public void accept(final Property p) {
        EObject _eContainer = p.eContainer();
        Property place = ((Property) _eContainer);
        String placeName = place.getName();
        TaskInferer.this.inferClassificateExpression(placeName, p, behaviourClazz);
      }
    };
    _map.forEach(_function_28);
    Iterable<Property> _properties_2 = this._mMLExtensions.getProperties(taskDefinition, "place");
    final Function1<Property, Boolean> _function_29 = new Function1<Property, Boolean>() {
      public Boolean apply(final Property p) {
        return Boolean.valueOf(TaskInferer.this._mMLExtensions.hasProperty(p, "back-enable"));
      }
    };
    Iterable<Property> _filter_1 = IterableExtensions.<Property>filter(_properties_2, _function_29);
    final Function1<Property, Property> _function_30 = new Function1<Property, Property>() {
      public Property apply(final Property p) {
        return TaskInferer.this._mMLExtensions.getProperty(p, "back-enable");
      }
    };
    Iterable<Property> _map_1 = IterableExtensions.<Property, Property>map(_filter_1, _function_30);
    final Consumer<Property> _function_31 = new Consumer<Property>() {
      public void accept(final Property p) {
        EObject _eContainer = p.eContainer();
        Property place = ((Property) _eContainer);
        String placeName = place.getName();
        TaskInferer.this.inferIsBackEnableExpression(placeName, p, behaviourClazz);
      }
    };
    _map_1.forEach(_function_31);
    Iterable<Property> _properties_3 = this._mMLExtensions.getProperties(taskDefinition, "shortcut");
    final Consumer<Property> _function_32 = new Consumer<Property>() {
      public void accept(final Property p) {
        EList<JvmMember> _members = behaviourClazz.getMembers();
        String _name = p.getName();
        String _javaIdentifier = JavaHelper.toJavaIdentifier(_name);
        String _plus = ("get" + _javaIdentifier);
        JvmTypeReference _resolveNodeType = TaskInferer.this._typeRefCache.resolveNodeType(p);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable appender) {
                appender.append("return this.getShortCut(\"");
                String _name = p.getName();
                appender.append(_name);
                appender.append("\");");
              }
            };
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = TaskInferer.this._monetJvmTypesBuilder.toMethod(p, _plus, _resolveNodeType, _function);
        TaskInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        EList<JvmMember> _members_1 = behaviourClazz.getMembers();
        String _name_1 = p.getName();
        String _javaIdentifier_1 = JavaHelper.toJavaIdentifier(_name_1);
        String _plus_1 = ("set" + _javaIdentifier_1);
        JvmTypeReference _resolveVoidType = TaskInferer.this._typeRefCache.resolveVoidType(p);
        final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveNodeType = TaskInferer.this._typeRefCache.resolveNodeType(p);
            JvmFormalParameter _parameter = TaskInferer.this._monetJvmTypesBuilder.toParameter(p, "node", _resolveNodeType);
            TaskInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable appender) {
                appender.append("this.setShortCut(\"");
                String _name = p.getName();
                appender.append(_name);
                appender.append("\", node);");
              }
            };
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_1 = TaskInferer.this._monetJvmTypesBuilder.toMethod(p, _plus_1, _resolveVoidType, _function_1);
        TaskInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
        EList<JvmMember> _members_2 = behaviourClazz.getMembers();
        String _name_2 = p.getName();
        String _javaIdentifier_2 = JavaHelper.toJavaIdentifier(_name_2);
        String _plus_2 = ("remove" + _javaIdentifier_2);
        JvmTypeReference _resolveVoidType_1 = TaskInferer.this._typeRefCache.resolveVoidType(p);
        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable appender) {
                appender.append("this.removeShortCut(\"");
                String _name = p.getName();
                appender.append(_name);
                appender.append("\");");
              }
            };
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_2 = TaskInferer.this._monetJvmTypesBuilder.toMethod(p, _plus_2, _resolveVoidType_1, _function_2);
        TaskInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
      }
    };
    _properties_3.forEach(_function_32);
  }
  
  private boolean inferCreateInstance(final Definition e, final JvmGenericType behaviourClazz) {
    boolean _xblockexpression = false;
    {
      String _inferBehaviourName = this.classNameInferer.inferBehaviourName(e);
      final JvmTypeReference returnType = this._typeRefCache.resolve(e, _inferBehaviourName);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return (");
              String _qualifiedName = returnType.getQualifiedName();
              ap.append(_qualifiedName);
              ap.append(")org.monet.bpi.TaskService.create(");
              String _inferBehaviourName = TaskInferer.this.classNameInferer.inferBehaviourName(e);
              ap.append(_inferBehaviourName);
              ap.append(".class);");
            }
          };
          TaskInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation method = this._monetJvmTypesBuilder.toMethod(e, "createNew", returnType, _function);
      method.setStatic(true);
      method.setVisibility(JvmVisibility.PUBLIC);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      _xblockexpression = this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, method);
    }
    return _xblockexpression;
  }
  
  private boolean inferClassificateExpression(final String placeName, final Property transference, final JvmGenericType behaviourClazz) {
    final Attribute classificateAttribute = this._mMLExtensions.getAttribute(transference, "classificate");
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(classificateAttribute, null));
    if (!_notEquals) {
      _and = false;
    } else {
      AttributeValue _value = classificateAttribute.getValue();
      _and = (_value instanceof ExpressionLiteral);
    }
    if (_and) {
      AttributeValue _value_1 = classificateAttribute.getValue();
      final ExpressionLiteral classificateExpression = ((ExpressionLiteral) _value_1);
      JvmTypeReference _resolveStringType = this._typeRefCache.resolveStringType(classificateAttribute);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          it.setVisibility(JvmVisibility.PROTECTED);
          XExpression _value = classificateExpression.getValue();
          boolean _notEquals = (!Objects.equal(_value, null));
          if (_notEquals) {
            XExpression _value_1 = classificateExpression.getValue();
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _value_1);
          }
        }
      };
      JvmOperation classificateMethod = this._monetJvmTypesBuilder.toMethod(classificateAttribute, ("classificate" + placeName), _resolveStringType, _function);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, classificateMethod);
    }
    return (!Objects.equal(classificateAttribute, null));
  }
  
  private boolean inferIsBackEnableExpression(final String placeName, final Property backEnable, final JvmGenericType behaviourClazz) {
    final Attribute backEnableAttribute = this._mMLExtensions.getAttribute(backEnable, "when");
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(backEnableAttribute, null));
    if (!_notEquals) {
      _and = false;
    } else {
      AttributeValue _value = backEnableAttribute.getValue();
      _and = (_value instanceof ExpressionLiteral);
    }
    if (_and) {
      AttributeValue _value_1 = backEnableAttribute.getValue();
      final ExpressionLiteral backEnableExpression = ((ExpressionLiteral) _value_1);
      JvmTypeReference _resolveBooleanPrimitiveType = this._typeRefCache.resolveBooleanPrimitiveType(backEnableAttribute);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          it.setVisibility(JvmVisibility.PROTECTED);
          XExpression _value = backEnableExpression.getValue();
          boolean _notEquals = (!Objects.equal(_value, null));
          if (_notEquals) {
            XExpression _value_1 = backEnableExpression.getValue();
            TaskInferer.this._monetJvmTypesBuilder.setBody(it, _value_1);
          }
        }
      };
      JvmOperation backEnableMethod = this._monetJvmTypesBuilder.toMethod(backEnableAttribute, ("isBackEnableWhen" + placeName), _resolveBooleanPrimitiveType, _function);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, backEnableMethod);
    }
    return (!Objects.equal(backEnableAttribute, null));
  }
}
