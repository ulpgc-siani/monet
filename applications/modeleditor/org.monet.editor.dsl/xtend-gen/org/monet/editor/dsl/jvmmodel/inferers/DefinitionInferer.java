package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmUnknownTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.monet.editor.dsl.generator.JavaScriptGenerator;
import org.monet.editor.dsl.generator.JvmStaticBlock;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.jvmmodel.inferers.BehaviourInferer;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.jvmmodel.inferers.DashboardInferer;
import org.monet.editor.dsl.jvmmodel.inferers.JobSchemaInferer;
import org.monet.editor.dsl.jvmmodel.inferers.ModelInferer;
import org.monet.editor.dsl.jvmmodel.inferers.SchemaInferer;
import org.monet.editor.dsl.jvmmodel.inferers.TaskInferer;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.metamodel.MetaModelStructure;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.Schema;
import org.monet.metamodel.interfaces.HasBehaviour;
import org.monet.metamodel.interfaces.HasClientBehaviour;
import org.monet.metamodel.interfaces.HasSchema;

@SuppressWarnings("all")
public class DefinitionInferer extends ModelInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private MMLExtensions _mMLExtensions;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  @Inject
  private MetaModelStructure structure;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  @Inject
  private SchemaInferer schemaInferer;
  
  @Inject
  private JobSchemaInferer jobSchemaInferer;
  
  @Inject
  private DashboardInferer dashboardInferer;
  
  @Inject
  private BehaviourInferer behaviourInferer;
  
  @Inject
  private TaskInferer taskInferer;
  
  public void infer(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String _name = definition.getName();
    boolean _equals = Objects.equal(_name, null);
    if (_equals) {
      return;
    }
    String _definitionType = definition.getDefinitionType();
    final Item definitionItem = this.structure.getDefinition(_definitionType);
    boolean _equals_1 = Objects.equal(definitionItem, null);
    if (_equals_1) {
      return;
    }
    final ArrayList<Property> childPropertiesWithClasses = new ArrayList<Property>();
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition);
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property p) {
        Iterable<Property> _inferClass = DefinitionInferer.this.propertyInferer.inferClass(definition, p, definitionItem, acceptor, prelinkingPhase);
        Iterables.<Property>addAll(childPropertiesWithClasses, _inferClass);
      }
    };
    _properties.forEach(_function);
    Iterable<Definition> _definitions = this._mMLExtensions.getDefinitions(definition);
    final Consumer<Definition> _function_1 = new Consumer<Definition>() {
      public void accept(final Definition d) {
        DefinitionInferer.this.infer(d, acceptor, prelinkingPhase);
      }
    };
    _definitions.forEach(_function_1);
    Item _child = definitionItem.getChild("schema");
    boolean _notEquals = (!Objects.equal(_child, null));
    if (_notEquals) {
      Schema _schema = this._mMLExtensions.getSchema(definition);
      this.schemaInferer.infer(_schema, definition, acceptor);
    }
    boolean _or = false;
    String _definitionType_1 = definition.getDefinitionType();
    boolean _equals_2 = Objects.equal(_definitionType_1, "job");
    if (_equals_2) {
      _or = true;
    } else {
      String _definitionType_2 = definition.getDefinitionType();
      boolean _equals_3 = Objects.equal(_definitionType_2, "sensor");
      _or = _equals_3;
    }
    if (_or) {
      this.jobSchemaInferer.infer(definition, acceptor);
    }
    String _definitionType_3 = definition.getDefinitionType();
    boolean _equals_4 = Objects.equal(_definitionType_3, "dashboard");
    if (_equals_4) {
      this.dashboardInferer.infer(definition, acceptor);
    }
    final String parentBehaviourClass = definitionItem.getParentBehaviourClass();
    boolean _notEquals_1 = (!Objects.equal(parentBehaviourClass, null));
    if (_notEquals_1) {
      this.behaviourInferer.infer(definition, definitionItem, acceptor, prelinkingPhase);
      boolean _or_1 = false;
      String _definitionType_4 = definition.getDefinitionType();
      boolean _equals_5 = Objects.equal(_definitionType_4, "service");
      if (_equals_5) {
        _or_1 = true;
      } else {
        String _definitionType_5 = definition.getDefinitionType();
        boolean _equals_6 = Objects.equal(_definitionType_5, "activity");
        _or_1 = _equals_6;
      }
      if (_or_1) {
        this.taskInferer.inferClasses(definition, acceptor, prelinkingPhase);
      }
    }
    final String definitionClassName = this.classNameInferer.inferDefinition(definition);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(definition, definitionClassName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function_2 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        String parentDefinitionClass = definitionItem.getParentDefinitionClass();
        EList<JvmMember> _members = it.getMembers();
        String _simpleName = it.getSimpleName();
        JvmConstructor _inferDefinitionConstructorMethod = DefinitionInferer.this.inferDefinitionConstructorMethod(definition, _simpleName, definitionItem, prelinkingPhase, true);
        DefinitionInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _inferDefinitionConstructorMethod);
        EList<JvmMember> _members_1 = it.getMembers();
        String _simpleName_1 = it.getSimpleName();
        Iterable<JvmOperation> _inferDefinitionConstructorInitMethods = DefinitionInferer.this.inferDefinitionConstructorInitMethods(definition, _simpleName_1, definitionItem, prelinkingPhase);
        DefinitionInferer.this._monetJvmTypesBuilder.<JvmMember>operator_add(_members_1, _inferDefinitionConstructorInitMethods);
        EList<JvmMember> _members_2 = it.getMembers();
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable appender) {
                appender.append("org.monet.metamodel.Dictionary.getCurrentInstance().");
                String parentName = "null";
                Definition _replaceSuperType = definition.getReplaceSuperType();
                boolean _notEquals = (!Objects.equal(_replaceSuperType, null));
                if (_notEquals) {
                  Definition _replaceSuperType_1 = definition.getReplaceSuperType();
                  QualifiedName _fullyQualifiedName = DefinitionInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_replaceSuperType_1);
                  String _string = _fullyQualifiedName.toString();
                  String _plus = ("\"" + _string);
                  String _plus_1 = (_plus + "\"");
                  parentName = _plus_1;
                  appender.append("replace(");
                } else {
                  Definition _superType = definition.getSuperType();
                  boolean _notEquals_1 = (!Objects.equal(_superType, null));
                  if (_notEquals_1) {
                    Definition _superType_1 = definition.getSuperType();
                    QualifiedName _fullyQualifiedName_1 = DefinitionInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
                    String _string_1 = _fullyQualifiedName_1.toString();
                    String _plus_2 = ("\"" + _string_1);
                    String _plus_3 = (_plus_2 + "\"");
                    parentName = _plus_3;
                  }
                  appender.append("register(");
                }
                appender.append(definitionClassName);
                appender.append(".class,\"");
                QualifiedName _fullyQualifiedName_2 = DefinitionInferer.this._iQualifiedNameProvider.getFullyQualifiedName(definition);
                String _string_2 = _fullyQualifiedName_2.toString();
                appender.append(_string_2);
                appender.append("\",");
                appender.append(parentName);
                appender.append(");\n");
                String _definitionType = definition.getDefinitionType();
                boolean _equals = Objects.equal(_definitionType, "dashboard");
                if (_equals) {
                  Iterable<Property> _properties = DefinitionInferer.this._mMLExtensions.getProperties(definition, "indicator");
                  final Consumer<Property> _function = new Consumer<Property>() {
                    public void accept(final Property indicator) {
                      Property level = DefinitionInferer.this._mMLExtensions.getProperty(indicator, "level");
                      boolean _and = false;
                      boolean _notEquals = (!Objects.equal(level, null));
                      if (!_notEquals) {
                        _and = false;
                      } else {
                        boolean _hasProperty = DefinitionInferer.this._mMLExtensions.hasProperty(level, "secondary");
                        _and = _hasProperty;
                      }
                      if (_and) {
                        String _name = indicator.getName();
                        String _inferIndicatorFormulaName = DefinitionInferer.this.classNameInferer.inferIndicatorFormulaName(definition, _name);
                        String formulaClass = (_inferIndicatorFormulaName + ".class");
                        Code _code = indicator.getCode();
                        String _value = _code.getValue();
                        String _plus = ((("org.monet.metamodel.Dictionary.getCurrentInstance().registerDashboardIndicatorFormulaClass(" + formulaClass) + ",\"") + _value);
                        String _plus_1 = (_plus + "\");\n");
                        appender.append(_plus_1);
                      }
                    }
                  };
                  _properties.forEach(_function);
                  Iterable<Property> _properties_1 = DefinitionInferer.this._mMLExtensions.getProperties(definition, "taxonomy");
                  final Consumer<Property> _function_1 = new Consumer<Property>() {
                    public void accept(final Property taxonomy) {
                      boolean _hasProperty = DefinitionInferer.this._mMLExtensions.hasProperty(taxonomy, "explicit");
                      if (_hasProperty) {
                        String _name = taxonomy.getName();
                        String _inferTaxonomyClassifierName = DefinitionInferer.this.classNameInferer.inferTaxonomyClassifierName(definition, _name);
                        String classifierClass = (_inferTaxonomyClassifierName + ".class");
                        Code _code = taxonomy.getCode();
                        String _value = _code.getValue();
                        String _plus = ((("org.monet.metamodel.Dictionary.getCurrentInstance().registerDashboardTaxonomyClassifierClass(" + classifierClass) + ",\"") + _value);
                        String _plus_1 = (_plus + "\");\n");
                        appender.append(_plus_1);
                      }
                    }
                  };
                  _properties_1.forEach(_function_1);
                }
              }
            };
            DefinitionInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmStaticBlock _staticBlock = DefinitionInferer.this._monetJvmTypesBuilder.toStaticBlock(definition, _function);
        DefinitionInferer.this._monetJvmTypesBuilder.<JvmStaticBlock>operator_add(_members_2, _staticBlock);
        JvmTypeReference superType = null;
        boolean _and = false;
        Definition _superType = definition.getSuperType();
        boolean _notEquals = (!Objects.equal(_superType, null));
        if (!_notEquals) {
          _and = false;
        } else {
          Definition _superType_1 = definition.getSuperType();
          QualifiedName _fullyQualifiedName = DefinitionInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
          boolean _notEquals_1 = (!Objects.equal(_fullyQualifiedName, null));
          _and = _notEquals_1;
        }
        if (_and) {
          Definition _superType_2 = definition.getSuperType();
          String _inferDefinition = DefinitionInferer.this.classNameInferer.inferDefinition(_superType_2);
          JvmTypeReference _resolve = DefinitionInferer.this._typeRefCache.resolve(definition, _inferDefinition);
          JvmTypeReference _cloneWithProxies = DefinitionInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolve);
          superType = _cloneWithProxies;
        } else {
          boolean _and_1 = false;
          Definition _replaceSuperType = definition.getReplaceSuperType();
          boolean _notEquals_2 = (!Objects.equal(_replaceSuperType, null));
          if (!_notEquals_2) {
            _and_1 = false;
          } else {
            Definition _replaceSuperType_1 = definition.getReplaceSuperType();
            QualifiedName _fullyQualifiedName_1 = DefinitionInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_replaceSuperType_1);
            boolean _notEquals_3 = (!Objects.equal(_fullyQualifiedName_1, null));
            _and_1 = _notEquals_3;
          }
          if (_and_1) {
            Definition _replaceSuperType_2 = definition.getReplaceSuperType();
            String _inferDefinition_1 = DefinitionInferer.this.classNameInferer.inferDefinition(_replaceSuperType_2);
            JvmTypeReference _resolve_1 = DefinitionInferer.this._typeRefCache.resolve(definition, _inferDefinition_1);
            JvmTypeReference _cloneWithProxies_1 = DefinitionInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolve_1);
            superType = _cloneWithProxies_1;
          }
        }
        boolean _equals = Objects.equal(superType, null);
        if (_equals) {
          JvmTypeReference _resolve_2 = DefinitionInferer.this._typeRefCache.resolve(definition, parentDefinitionClass);
          JvmTypeReference _cloneWithProxies_2 = DefinitionInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolve_2);
          superType = _cloneWithProxies_2;
        }
        boolean _notEquals_4 = (!Objects.equal(superType, null));
        if (_notEquals_4) {
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          DefinitionInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, superType);
        }
        EList<JvmMember> _members_3 = it.getMembers();
        String _name = definition.getName();
        JvmOperation _inferGetName = DefinitionInferer.this.inferGetName(definition, _name);
        DefinitionInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_3, _inferGetName);
        boolean _notEquals_5 = (!Objects.equal(parentBehaviourClass, null));
        if (_notEquals_5) {
          JvmTypeReference _resolveClassType = DefinitionInferer.this._typeRefCache.resolveClassType(definition);
          final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("return ");
                  String _inferBehaviourName = DefinitionInferer.this.classNameInferer.inferBehaviourName(definition);
                  ap.append(_inferBehaviourName);
                  ap.append(".class;");
                }
              };
              DefinitionInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation behaviourGetter = DefinitionInferer.this._monetJvmTypesBuilder.toMethod(definition, "getBehaviourClass", _resolveClassType, _function_1);
          EList<JvmMember> _members_4 = it.getMembers();
          DefinitionInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_4, behaviourGetter);
          EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
          JvmTypeReference _newTypeRef = DefinitionInferer.this._monetJvmTypesBuilder.newTypeRef(definition, HasBehaviour.class);
          DefinitionInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _newTypeRef);
        }
        JvmTypeReference _resolveStringType = DefinitionInferer.this._typeRefCache.resolveStringType(definition);
        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("return \"");
                String _buildClientBehaviour = JavaScriptGenerator.buildClientBehaviour(definition, null);
                ap.append(_buildClientBehaviour);
                ap.append("\";");
              }
            };
            DefinitionInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation clientBehaviourGetter = DefinitionInferer.this._monetJvmTypesBuilder.toMethod(definition, "getClientBehaviour", _resolveStringType, _function_2);
        EList<JvmMember> _members_5 = it.getMembers();
        DefinitionInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_5, clientBehaviourGetter);
        EList<JvmTypeReference> _superTypes_2 = it.getSuperTypes();
        JvmTypeReference _newTypeRef_1 = DefinitionInferer.this._monetJvmTypesBuilder.newTypeRef(definition, HasClientBehaviour.class);
        DefinitionInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_2, _newTypeRef_1);
        boolean _or = false;
        boolean _or_1 = false;
        Item _child = definitionItem.getChild("schema");
        boolean _notEquals_6 = (!Objects.equal(_child, null));
        if (_notEquals_6) {
          _or_1 = true;
        } else {
          String _definitionType = definition.getDefinitionType();
          boolean _equals_1 = Objects.equal(_definitionType, "job");
          _or_1 = _equals_1;
        }
        if (_or_1) {
          _or = true;
        } else {
          String _definitionType_1 = definition.getDefinitionType();
          boolean _equals_2 = Objects.equal(_definitionType_1, "sensor");
          _or = _equals_2;
        }
        if (_or) {
          String _inferSchemaBehaviourName = DefinitionInferer.this.classNameInferer.inferSchemaBehaviourName(definition);
          final JvmTypeReference schemaTypeRef = DefinitionInferer.this._monetJvmTypesBuilder.newTypeRef(definition, _inferSchemaBehaviourName);
          JvmTypeReference _resolveClassType_1 = DefinitionInferer.this._typeRefCache.resolveClassType(definition);
          final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable x) {
                  x.append("return ");
                  String _qualifiedName = schemaTypeRef.getQualifiedName();
                  String _string = _qualifiedName.toString();
                  x.append(_string);
                  x.append(".class;");
                }
              };
              DefinitionInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation schemaGetter = DefinitionInferer.this._monetJvmTypesBuilder.toMethod(definition, "getSchemaClass", _resolveClassType_1, _function_3);
          EList<JvmMember> _members_6 = it.getMembers();
          DefinitionInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_6, schemaGetter);
          EList<JvmTypeReference> _superTypes_3 = it.getSuperTypes();
          JvmTypeReference _newTypeRef_2 = DefinitionInferer.this._monetJvmTypesBuilder.newTypeRef(definition, HasSchema.class);
          DefinitionInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_3, _newTypeRef_2);
        }
        boolean _or_2 = false;
        String _token = definitionItem.getToken();
        boolean _equals_3 = Objects.equal(_token, "service");
        if (_equals_3) {
          _or_2 = true;
        } else {
          String _token_1 = definitionItem.getToken();
          boolean _equals_4 = Objects.equal(_token_1, "activity");
          _or_2 = _equals_4;
        }
        if (_or_2) {
          EList<JvmMember> _members_7 = it.getMembers();
          JvmTypeReference _resolveClassType_2 = DefinitionInferer.this._typeRefCache.resolveClassType(definition);
          final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              EList<JvmFormalParameter> _parameters = it.getParameters();
              JvmTypeReference _resolveStringType = DefinitionInferer.this._typeRefCache.resolveStringType(definition);
              JvmFormalParameter _parameter = DefinitionInferer.this._monetJvmTypesBuilder.toParameter(definition, "name", _resolveStringType);
              DefinitionInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("int hash = name.hashCode();\n ");
                  ap.append("switch(hash) {\n ");
                  Iterable<Property> _properties = DefinitionInferer.this._mMLExtensions.getProperties(definition, "customer", "contestants", "contest", "provider");
                  final Consumer<Property> _function = new Consumer<Property>() {
                    public void accept(final Property p) {
                      String name = null;
                      String _name = p.getName();
                      boolean _equals = Objects.equal(_name, null);
                      if (_equals) {
                        String _id = p.getId();
                        name = _id;
                      } else {
                        String _name_1 = p.getName();
                        name = _name_1;
                      }
                      String _id_1 = p.getId();
                      boolean _equals_1 = Objects.equal(_id_1, "provider");
                      if (_equals_1) {
                        Iterable<Property> _properties = DefinitionInferer.this._mMLExtensions.getProperties(p, "internal", "external");
                        final Consumer<Property> _function = new Consumer<Property>() {
                          public void accept(final Property px) {
                            String _id = px.getId();
                            String _name = p.getName();
                            String _plus = (_id + _name);
                            DefinitionInferer.this.toCaseLine(px, ap, _plus);
                          }
                        };
                        _properties.forEach(_function);
                      } else {
                        DefinitionInferer.this.toCaseLine(p, ap, name);
                      }
                    }
                  };
                  _properties.forEach(_function);
                  ap.append("default: return null;\n}");
                }
              };
              DefinitionInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation _method = DefinitionInferer.this._monetJvmTypesBuilder.toMethod(definition, "getSubBehaviorClass", _resolveClassType_2, _function_4);
          DefinitionInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method);
        }
        boolean _or_3 = false;
        String _token_2 = definitionItem.getToken();
        boolean _equals_5 = Objects.equal(_token_2, "form");
        if (_equals_5) {
          _or_3 = true;
        } else {
          String _token_3 = definitionItem.getToken();
          boolean _equals_6 = Objects.equal(_token_3, "document");
          _or_3 = _equals_6;
        }
        if (_or_3) {
          DefinitionInferer.this.inferProperties(definition, it);
          DefinitionInferer.this.inferMappings(definition, it);
        }
        Iterable<Property> _properties = DefinitionInferer.this._mMLExtensions.getProperties(definition);
        Iterables.<Property>addAll(childPropertiesWithClasses, _properties);
        final Consumer<Property> _function_5 = new Consumer<Property>() {
          public void accept(final Property px) {
            String _name = px.getName();
            boolean _notEquals = (!Objects.equal(_name, null));
            if (_notEquals) {
              final String fieldTypeName = DefinitionInferer.this.classNameInferer.inferPropertyName(px);
              JvmTypeReference fieldType = DefinitionInferer.this._monetJvmTypesBuilder.newTypeRef(px, fieldTypeName);
              boolean _and = false;
              boolean _notEquals_1 = (!Objects.equal(fieldType, null));
              if (!_notEquals_1) {
                _and = false;
              } else {
                _and = (!(fieldType instanceof JvmUnknownTypeReference));
              }
              if (_and) {
                String _name_1 = px.getName();
                String _javaIdentifier = JavaHelper.toJavaIdentifier(_name_1);
                JvmField field = DefinitionInferer.this._monetJvmTypesBuilder.toField(px, _javaIdentifier, fieldType);
                field.setFinal(true);
                final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                  public void apply(final ITreeAppendable ap) {
                    ap.append("new ");
                    ap.append(fieldTypeName);
                    ap.append("()");
                  }
                };
                DefinitionInferer.this._monetJvmTypesBuilder.setInitializer(field, _function);
                EList<JvmMember> _members = it.getMembers();
                DefinitionInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, field);
              }
            }
          }
        };
        childPropertiesWithClasses.forEach(_function_5);
      }
    };
    _accept.initializeLater(_function_2);
  }
  
  private void inferProperties(final Definition e, final JvmGenericType jvmType) {
    final boolean hasProperties = this._mMLExtensions.hasProperty(e, "properties");
    if (hasProperties) {
      final String propertiesClassName = this.classNameInferer.inferPropertiesName(e);
      EList<JvmMember> _members = jvmType.getMembers();
      JvmTypeReference _resolveClassType = this._typeRefCache.resolveClassType(e);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return ");
              ap.append(propertiesClassName);
              ap.append(".class;");
            }
          };
          DefinitionInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation _method = this._monetJvmTypesBuilder.toMethod(e, "getPropertiesClass", _resolveClassType, _function);
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
      EList<JvmTypeReference> _superTypes = jvmType.getSuperTypes();
      JvmTypeReference _resolveHasPropertiesType = this._typeRefCache.resolveHasPropertiesType(e);
      this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveHasPropertiesType);
    }
  }
  
  private void inferMappings(final Definition e, final JvmGenericType jvmType) {
    EList<JvmMember> _members = jvmType.getMembers();
    JvmTypeReference _resolveClassType = this._typeRefCache.resolveClassType(e);
    final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = DefinitionInferer.this._typeRefCache.resolveStringType(e);
        JvmFormalParameter _parameter = DefinitionInferer.this._monetJvmTypesBuilder.toParameter(e, "code", _resolveStringType);
        DefinitionInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("switch(code.hashCode()) {\n");
            Iterable<Property> _properties = DefinitionInferer.this._mMLExtensions.getProperties(e, "mapping");
            final Procedure2<Property, Integer> _function = new Procedure2<Property, Integer>() {
              public void apply(final Property eMapping, final Integer i) {
                Attribute _attribute = DefinitionInferer.this._mMLExtensions.getAttribute(eMapping, "index");
                Definition eIndex = DefinitionInferer.this._mMLExtensions.getValueAsDefinition(_attribute);
                boolean _notEquals = (!Objects.equal(eIndex, null));
                if (_notEquals) {
                  ap.append("case ");
                  Code _code = eIndex.getCode();
                  String _value = _code.getValue();
                  int _hashCode = _value.hashCode();
                  String _valueOf = String.valueOf(_hashCode);
                  ap.append(_valueOf);
                  ap.append(":\nreturn ");
                  String _inferMappingName = DefinitionInferer.this.classNameInferer.inferMappingName(e, (i).intValue());
                  ap.append(_inferMappingName);
                  ap.append(".class;");
                }
              }
            };
            IterableExtensions.<Property>forEach(_properties, _function);
            ap.append("\n}\nreturn super.getMappingClass(code);");
          }
        };
        DefinitionInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method = this._monetJvmTypesBuilder.toMethod(e, "getMappingClass", _resolveClassType, _function);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
    EList<JvmTypeReference> _superTypes = jvmType.getSuperTypes();
    JvmTypeReference _resolveHasMappingsType = this._typeRefCache.resolveHasMappingsType(e);
    this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveHasMappingsType);
  }
  
  public void toCaseLine(final Property p, final ITreeAppendable ap, final String name) {
    ap.append("case ");
    int _hashCode = name.hashCode();
    String _valueOf = String.valueOf(_hashCode);
    ap.append(_valueOf);
    ap.append(" :\nreturn ");
    String _inferPropertyBehavior = this.classNameInferer.inferPropertyBehavior(p);
    ap.append(_inferPropertyBehavior);
    ap.append(".class;\n");
  }
}
