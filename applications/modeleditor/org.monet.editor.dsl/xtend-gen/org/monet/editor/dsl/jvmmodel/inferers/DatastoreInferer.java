package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericArrayTypeReference;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;

@SuppressWarnings("all")
public class DatastoreInferer {
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
  private TypeReferences references;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  public void infer(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    this.inferDimensions(definition, acceptor, prelinkingPhase);
    this.inferComponents(definition, acceptor, prelinkingPhase);
    this.inferCubes(definition, acceptor, prelinkingPhase);
    this.inferFacts(definition, acceptor, prelinkingPhase);
  }
  
  public void inferDimensions(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition, "dimension");
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property dimension) {
        DatastoreInferer.this.inferDimensionClass(definition, dimension, acceptor, prelinkingPhase);
      }
    };
    _properties.forEach(_function);
  }
  
  public void inferDimensionClass(final Definition definition, final Property dimension, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String _inferDimensionName = this.classNameInferer.inferDimensionName(dimension);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(dimension, _inferDimensionName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _resolveDimensionImplType = DatastoreInferer.this._typeRefCache.resolveDimensionImplType(dimension);
        DatastoreInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveDimensionImplType);
        final String componentTypeName = DatastoreInferer.this.classNameInferer.inferComponentName(dimension);
        final JvmTypeReference componentType = DatastoreInferer.this.references.getTypeForName(componentTypeName, definition);
        EList<JvmMember> _members = it.getMembers();
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveStringType = DatastoreInferer.this._typeRefCache.resolveStringType(dimension);
            JvmFormalParameter _parameter = DatastoreInferer.this._monetJvmTypesBuilder.toParameter(dimension, "id", _resolveStringType);
            DatastoreInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append((("return (" + componentTypeName) + ")"));
                ap.append((("this.insertComponentImpl(" + componentTypeName) + ".class,id);"));
              }
            };
            DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(dimension, "insertComponent", componentType, _function);
        DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
      }
    };
    _accept.initializeLater(_function);
  }
  
  public void inferCubes(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition, "cube");
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property cube) {
        DatastoreInferer.this.inferCubeClass(definition, cube, acceptor, prelinkingPhase);
      }
    };
    _properties.forEach(_function);
  }
  
  public void inferCubeClass(final Definition definition, final Property cube, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String _inferCubeName = this.classNameInferer.inferCubeName(cube);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(cube, _inferCubeName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _resolveCubeImplType = DatastoreInferer.this._typeRefCache.resolveCubeImplType(cube);
        DatastoreInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveCubeImplType);
        final String factTypeName = DatastoreInferer.this.classNameInferer.inferFactName(cube);
        final JvmTypeReference factType = DatastoreInferer.this.references.getTypeForName(factTypeName, definition);
        EList<JvmMember> _members = it.getMembers();
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmTypeReference _resolveDateType = DatastoreInferer.this._typeRefCache.resolveDateType(cube);
            JvmFormalParameter _parameter = DatastoreInferer.this._monetJvmTypesBuilder.toParameter(cube, "date", _resolveDateType);
            DatastoreInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("return (");
                ap.append(factTypeName);
                ap.append(((")this.insertFactImpl(" + factTypeName) + ".class, date);"));
              }
            };
            DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(cube, "insertFact", factType, _function);
        DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
      }
    };
    _accept.initializeLater(_function);
  }
  
  public void inferComponents(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition, "dimension");
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property dimension) {
        DatastoreInferer.this.inferComponent(definition, dimension, acceptor, prelinkingPhase);
      }
    };
    _properties.forEach(_function);
  }
  
  public void inferComponent(final Definition d, final Property dimension, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String _inferComponentName = this.classNameInferer.inferComponentName(dimension);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(d, _inferComponentName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        Definition _superType = d.getSuperType();
        boolean _notEquals = (!Objects.equal(_superType, null));
        if (_notEquals) {
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          String _inferComponentName = DatastoreInferer.this.classNameInferer.inferComponentName(dimension);
          JvmTypeReference _typeForName = DatastoreInferer.this.references.getTypeForName(_inferComponentName, d);
          DatastoreInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeForName);
        } else {
          EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
          JvmTypeReference _resolveDimensionComponentImplType = DatastoreInferer.this._typeRefCache.resolveDimensionComponentImplType(d);
          DatastoreInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _resolveDimensionComponentImplType);
        }
        Iterable<Property> _properties = DatastoreInferer.this._mMLExtensions.getProperties(dimension, "feature");
        final Consumer<Property> _function = new Consumer<Property>() {
          public void accept(final Property p) {
            final Code featureCode = p.getCode();
            Attribute _attribute = DatastoreInferer.this._mMLExtensions.getAttribute(p, "type");
            final String type = DatastoreInferer.this._mMLExtensions.getValueAsString(_attribute);
            final String featureTypeName = DatastoreInferer.this.inferFeatureType(type);
            boolean _and = false;
            boolean _notEquals = (!Objects.equal(featureCode, null));
            if (!_notEquals) {
              _and = false;
            } else {
              boolean _notEquals_1 = (!Objects.equal(featureTypeName, null));
              _and = _notEquals_1;
            }
            if (_and) {
              String _name = p.getName();
              String methodSuffix = JavaHelper.toJavaIdentifier(_name);
              final JvmTypeReference featureType = DatastoreInferer.this.references.getTypeForName(featureTypeName, p);
              EList<JvmMember> _members = it.getMembers();
              final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("return (");
                      ap.append(featureTypeName);
                      ap.append(")getFeatureValue(\"");
                      String _value = featureCode.getValue();
                      ap.append(_value);
                      ap.append("\");");
                    }
                  };
                  DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(d, (("get" + methodSuffix) + "Value"), featureType, _function);
              DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
              final JvmGenericArrayTypeReference featureTypeArray = DatastoreInferer.this.references.createArrayType(featureType);
              EList<JvmMember> _members_1 = it.getMembers();
              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("return getFeatureValues(\"");
                      String _value = featureCode.getValue();
                      ap.append(_value);
                      ap.append((("\").toArray(new " + featureTypeName) + "[0]);"));
                    }
                  };
                  DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method_1 = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(d, (("get" + methodSuffix) + "Values"), featureTypeArray, _function_1);
              DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
              boolean _equals = Objects.equal(type, "TERM");
              if (_equals) {
                EList<JvmMember> _members_2 = it.getMembers();
                JvmTypeReference _resolveVoidType = DatastoreInferer.this._typeRefCache.resolveVoidType(d);
                final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                  public void apply(final JvmOperation it) {
                    EList<JvmFormalParameter> _parameters = it.getParameters();
                    JvmFormalParameter _parameter = DatastoreInferer.this._monetJvmTypesBuilder.toParameter(d, "value", featureType);
                    DatastoreInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    EList<JvmFormalParameter> _parameters_1 = it.getParameters();
                    JvmTypeReference _resolveArrayListType = DatastoreInferer.this._typeRefCache.resolveArrayListType(d, featureType);
                    JvmFormalParameter _parameter_1 = DatastoreInferer.this._monetJvmTypesBuilder.toParameter(d, "ancestors", _resolveArrayListType);
                    DatastoreInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                    final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                      public void apply(final ITreeAppendable ap) {
                        ap.append("this.addFeature(\"");
                        String _value = featureCode.getValue();
                        ap.append(_value);
                        ap.append("\", value, ancestors);");
                      }
                    };
                    DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                  }
                };
                JvmOperation _method_2 = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(d, ("add" + methodSuffix), _resolveVoidType, _function_2);
                DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
              } else {
                EList<JvmMember> _members_3 = it.getMembers();
                JvmTypeReference _resolveVoidType_1 = DatastoreInferer.this._typeRefCache.resolveVoidType(d);
                final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                  public void apply(final JvmOperation it) {
                    EList<JvmFormalParameter> _parameters = it.getParameters();
                    JvmFormalParameter _parameter = DatastoreInferer.this._monetJvmTypesBuilder.toParameter(d, "value", featureType);
                    DatastoreInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                      public void apply(final ITreeAppendable ap) {
                        ap.append("this.addFeature(\"");
                        String _value = featureCode.getValue();
                        ap.append(_value);
                        ap.append("\", value);");
                      }
                    };
                    DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                  }
                };
                JvmOperation _method_3 = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(d, ("add" + methodSuffix), _resolveVoidType_1, _function_3);
                DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_3);
              }
            }
          }
        };
        _properties.forEach(_function);
      }
    };
    _accept.initializeLater(_function);
  }
  
  public String inferFeatureType(final String type) {
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(type, "STRING")) {
        _matched=true;
        return "java.lang.String";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "TERM")) {
        _matched=true;
        return "org.monet.bpi.types.Term";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "BOOLEAN")) {
        _matched=true;
        return "java.lang.Boolean";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "INTEGER")) {
        _matched=true;
        return "org.monet.bpi.types.Number";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "REAL")) {
        _matched=true;
        return "org.monet.bpi.types.Number";
      }
    }
    return null;
  }
  
  public void inferFacts(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition, "cube");
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property cube) {
        DatastoreInferer.this.inferFact(definition, cube, acceptor, prelinkingPhase);
      }
    };
    _properties.forEach(_function);
  }
  
  public void inferFact(final Definition e, final Property cube, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String _inferFactName = this.classNameInferer.inferFactName(cube);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(e, _inferFactName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _resolveCubeFactImplType = DatastoreInferer.this._typeRefCache.resolveCubeFactImplType(e);
        DatastoreInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveCubeFactImplType);
        Iterable<Attribute> _attributes = DatastoreInferer.this._mMLExtensions.getAttributes(cube, "use");
        final Consumer<Attribute> _function = new Consumer<Attribute>() {
          public void accept(final Attribute use) {
            final Property dimension = DatastoreInferer.this._mMLExtensions.getValueAsProperty(use);
            boolean _notEquals = (!Objects.equal(dimension, null));
            if (_notEquals) {
              final String dimensionName = dimension.getName();
              String methodSuffix = JavaHelper.toJavaIdentifier(dimensionName);
              EList<JvmMember> _members = it.getMembers();
              JvmTypeReference _resolveStringType = DatastoreInferer.this._typeRefCache.resolveStringType(use);
              final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("return ");
                      ap.append("getComponentId(");
                      ap.append((("\"" + dimensionName) + "\""));
                      ap.append(");");
                    }
                  };
                  DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(use, ("get" + methodSuffix), _resolveStringType, _function);
              DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
              EList<JvmMember> _members_1 = it.getMembers();
              JvmTypeReference _resolveVoidType = DatastoreInferer.this._typeRefCache.resolveVoidType(use);
              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  EList<JvmFormalParameter> _parameters = it.getParameters();
                  JvmTypeReference _resolveStringType = DatastoreInferer.this._typeRefCache.resolveStringType(use);
                  JvmFormalParameter _parameter = DatastoreInferer.this._monetJvmTypesBuilder.toParameter(use, "componentId", _resolveStringType);
                  DatastoreInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("this.setComponent(\"");
                      ap.append(dimensionName);
                      ap.append("\", componentId);");
                    }
                  };
                  DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method_1 = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(use, ("set" + methodSuffix), _resolveVoidType, _function_1);
              DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
            }
          }
        };
        _attributes.forEach(_function);
        Iterable<Property> _properties = DatastoreInferer.this._mMLExtensions.getProperties(cube, "metric");
        final Consumer<Property> _function_1 = new Consumer<Property>() {
          public void accept(final Property m) {
            Code _code = m.getCode();
            boolean _notEquals = (!Objects.equal(_code, null));
            if (_notEquals) {
              String _name = m.getName();
              String methodSuffix = JavaHelper.toJavaIdentifier(_name);
              EList<JvmMember> _members = it.getMembers();
              JvmTypeReference _resolveDoubleType = DatastoreInferer.this._typeRefCache.resolveDoubleType(m);
              final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("return getMeasure(\"");
                      Code _code = m.getCode();
                      String _value = _code.getValue();
                      ap.append(_value);
                      ap.append("\");");
                    }
                  };
                  DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(m, ("get" + methodSuffix), _resolveDoubleType, _function);
              DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
              EList<JvmMember> _members_1 = it.getMembers();
              JvmTypeReference _resolveVoidType = DatastoreInferer.this._typeRefCache.resolveVoidType(m);
              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  EList<JvmFormalParameter> _parameters = it.getParameters();
                  JvmTypeReference _resolveDoubleType = DatastoreInferer.this._typeRefCache.resolveDoubleType(m);
                  JvmFormalParameter _parameter = DatastoreInferer.this._monetJvmTypesBuilder.toParameter(m, "value", _resolveDoubleType);
                  DatastoreInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("this.setMeasure(\"");
                      Code _code = m.getCode();
                      String _value = _code.getValue();
                      ap.append(_value);
                      ap.append("\", value);");
                    }
                  };
                  DatastoreInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method_1 = DatastoreInferer.this._monetJvmTypesBuilder.toMethod(m, ("set" + methodSuffix), _resolveVoidType, _function_1);
              DatastoreInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
            }
          }
        };
        _properties.forEach(_function_1);
      }
    };
    _accept.initializeLater(_function);
  }
}
