package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmAnnotationValue;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.access.impl.ClassURIHelper;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.monet.bpi.ExporterScope;
import org.monet.bpi.Properties;
import org.monet.editor.dsl.collections.RepetitionCounter;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.jvmmodel.inferers.DatastoreBuilderInferer;
import org.monet.editor.dsl.jvmmodel.inferers.DatastoreInferer;
import org.monet.editor.dsl.jvmmodel.inferers.FieldInferer;
import org.monet.editor.dsl.jvmmodel.inferers.RuleInferer;
import org.monet.editor.dsl.jvmmodel.inferers.TaskInferer;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Feature;
import org.monet.editor.dsl.monetModelingLanguage.Function;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.Referenciable;
import org.monet.editor.dsl.monetModelingLanguage.Variable;
import org.monet.editor.dsl.monetModelingLanguage.XTReference;

@SuppressWarnings("all")
public class BehaviourInferer {
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
  private TypeReferences references;
  
  @Inject
  private ClassURIHelper uriHelper;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  @Inject
  private FieldInferer fieldInferer;
  
  @Inject
  private TaskInferer taskInferer;
  
  @Inject
  private RuleInferer ruleInferer;
  
  @Inject
  private DatastoreInferer datastoreInferer;
  
  @Inject
  private DatastoreBuilderInferer datastoreBuilderInferer;
  
  public void inferTaskMethod(final Attribute f, final JvmGenericType taskType, final boolean prelinkingPhase) {
    if ((!prelinkingPhase)) {
      Referenciable ref = this._mMLExtensions.getValueAsReferenciable(f);
      boolean _notEquals = (!Objects.equal(ref, null));
      if (_notEquals) {
        QualifiedName qualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(ref);
        boolean _notEquals_1 = (!Objects.equal(qualifiedName, null));
        if (_notEquals_1) {
          final String methodTypeName = XtendHelper.convertQualifiedNameToGenName(qualifiedName);
          final JvmTypeReference methodTypeRef = this._typeRefCache.resolve(ref, methodTypeName);
          String _id = f.getId();
          final String methodName = JavaHelper.toJavaIdentifier(_id);
          EList<JvmMember> _members = taskType.getMembers();
          final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("return (");
                  ap.append(methodTypeName);
                  ap.append(")genericGet");
                  ap.append(methodName);
                  ap.append("();");
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation _method = this._monetJvmTypesBuilder.toMethod(f, ("get" + methodName), methodTypeRef, _function);
          this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
          EList<JvmMember> _members_1 = taskType.getMembers();
          JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(f);
          final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              EList<JvmFormalParameter> _parameters = it.getParameters();
              JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(f, "node", methodTypeRef);
              BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("this.genericSet");
                  ap.append(methodName);
                  ap.append("((");
                  ap.append(methodTypeName);
                  ap.append(")node);");
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation _method_1 = this._monetJvmTypesBuilder.toMethod(f, ("set" + methodName), _resolveVoidType, _function_1);
          this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
        }
      }
    }
  }
  
  public void infer(final Definition e, final Item definitionItem, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String _definitionType = e.getDefinitionType();
    boolean _equals = Objects.equal(_definitionType, "datastore");
    if (_equals) {
      this.datastoreInferer.infer(e, acceptor, prelinkingPhase);
    } else {
      boolean _or = false;
      String _definitionType_1 = e.getDefinitionType();
      boolean _equals_1 = Objects.equal(_definitionType_1, "form");
      if (_equals_1) {
        _or = true;
      } else {
        String _definitionType_2 = e.getDefinitionType();
        boolean _equals_2 = Objects.equal(_definitionType_2, "document");
        _or = _equals_2;
      }
      if (_or) {
        this.inferMappings(e, acceptor, prelinkingPhase);
        String _definitionType_3 = e.getDefinitionType();
        boolean _equals_3 = Objects.equal(_definitionType_3, "form");
        if (_equals_3) {
          Iterable<Property> _properties = this._mMLExtensions.getProperties(e);
          final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
            public Boolean apply(final Property p) {
              boolean _and = false;
              String _id = p.getId();
              boolean _notEquals = (!Objects.equal(_id, null));
              if (!_notEquals) {
                _and = false;
              } else {
                String _id_1 = p.getId();
                boolean _startsWith = _id_1.startsWith("field-");
                _and = _startsWith;
              }
              return Boolean.valueOf(_and);
            }
          };
          Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties, _function);
          final Procedure1<Property> _function_1 = new Procedure1<Property>() {
            public void apply(final Property p) {
              BehaviourInferer.this.fieldInferer.inferTypeClass(e, p, acceptor);
            }
          };
          IterableExtensions.<Property>forEach(_filter, _function_1);
        }
      }
    }
    this.inferProperties(e, acceptor, prelinkingPhase);
    String _definitionType_4 = e.getDefinitionType();
    boolean _equals_4 = Objects.equal(_definitionType_4, "exporter");
    if (_equals_4) {
      String _inferExporterScopeName = this.classNameInferer.inferExporterScopeName(e);
      JvmGenericType _class = this._monetJvmTypesBuilder.toClass(e, _inferExporterScopeName);
      IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
      final Procedure1<JvmGenericType> _function_2 = new Procedure1<JvmGenericType>() {
        public void apply(final JvmGenericType it) {
          Definition superType = null;
          Definition _superType = e.getSuperType();
          boolean _notEquals = (!Objects.equal(_superType, null));
          if (_notEquals) {
            Definition _superType_1 = e.getSuperType();
            superType = _superType_1;
          } else {
            Definition _replaceSuperType = e.getReplaceSuperType();
            boolean _notEquals_1 = (!Objects.equal(_replaceSuperType, null));
            if (_notEquals_1) {
              Definition _replaceSuperType_1 = e.getReplaceSuperType();
              superType = _replaceSuperType_1;
            }
          }
          boolean _and = false;
          boolean _notEquals_2 = (!Objects.equal(superType, null));
          if (!_notEquals_2) {
            _and = false;
          } else {
            QualifiedName _fullyQualifiedName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(superType);
            boolean _notEquals_3 = (!Objects.equal(_fullyQualifiedName, null));
            _and = _notEquals_3;
          }
          final boolean hasSuperType = _and;
          if (hasSuperType) {
            EList<JvmTypeReference> _superTypes = it.getSuperTypes();
            String _inferExporterScopeName = BehaviourInferer.this.classNameInferer.inferExporterScopeName(superType);
            JvmTypeReference _resolve = BehaviourInferer.this._typeRefCache.resolve(e, _inferExporterScopeName);
            BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolve);
          } else {
            EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
            JvmTypeReference _resolveExporterScopeImplType = BehaviourInferer.this._typeRefCache.resolveExporterScopeImplType(e);
            BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _resolveExporterScopeImplType);
          }
          String typeName = null;
          Attribute _attribute = BehaviourInferer.this._mMLExtensions.getAttribute(e, "target");
          Definition eTarget = BehaviourInferer.this._mMLExtensions.getValueAsDefinition(_attribute);
          boolean _notEquals_4 = (!Objects.equal(eTarget, null));
          if (_notEquals_4) {
            String _inferBehaviourName = BehaviourInferer.this.classNameInferer.inferBehaviourName(((Definition) eTarget));
            typeName = _inferBehaviourName;
          }
          final JvmTypeReference targetDocument = BehaviourInferer.this._typeRefCache.resolve(e, typeName);
          EList<JvmMember> _members = it.getMembers();
          final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
            public void apply(final JvmConstructor it) {
              EList<JvmFormalParameter> _parameters = it.getParameters();
              String _inferBehaviourName = BehaviourInferer.this.classNameInferer.inferBehaviourName(e);
              JvmTypeReference _resolve = BehaviourInferer.this._typeRefCache.resolve(e, _inferBehaviourName);
              JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "instance", _resolve);
              BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              EList<JvmFormalParameter> _parameters_1 = it.getParameters();
              JvmTypeReference _resolveNodeType = BehaviourInferer.this._typeRefCache.resolveNodeType(e);
              JvmFormalParameter _parameter_1 = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "target", _resolveNodeType);
              BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  if (hasSuperType) {
                    ap.append("super(instance, target);");
                  } else {
                    ap.append("instance.super(target);");
                  }
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmConstructor _constructor = BehaviourInferer.this._monetJvmTypesBuilder.toConstructor(e, _function);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
          EList<JvmMember> _members_1 = it.getMembers();
          JvmTypeReference _resolveVoidType = BehaviourInferer.this._typeRefCache.resolveVoidType(e);
          final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              EList<JvmFormalParameter> _parameters = it.getParameters();
              JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "document", targetDocument);
              BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("this.internalToDocument(document);");
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "toDocument", _resolveVoidType, _function_1);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
          EList<JvmMember> _members_2 = it.getMembers();
          final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("return (");
                  String _qualifiedName = targetDocument.getQualifiedName();
                  ap.append(_qualifiedName);
                  ap.append(")internalToNewDocument(\"");
                  String _qualifiedName_1 = targetDocument.getQualifiedName();
                  ap.append(_qualifiedName_1);
                  ap.append("\");");
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation _method_1 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "toNewDocument", targetDocument, _function_2);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
        }
      };
      _accept.initializeLater(_function_2);
    }
    String _inferBehaviourName = this.classNameInferer.inferBehaviourName(e);
    JvmGenericType _class_1 = this._monetJvmTypesBuilder.toClass(e, _inferBehaviourName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept_1 = acceptor.<JvmGenericType>accept(_class_1);
    final Procedure1<JvmGenericType> _function_3 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        String parentBehaviourClass = definitionItem.getParentBehaviourClass();
        Class<?> annotation = SuppressWarnings.class;
        JvmAnnotationReference ann = BehaviourInferer.this._monetJvmTypesBuilder.toAnnotation(e, annotation);
        EList<JvmAnnotationValue> _explicitValues = ann.getExplicitValues();
        JvmAnnotationValue _createAnnotationValue = XtendHelper.createAnnotationValue("value", "all", annotation, BehaviourInferer.this.uriHelper);
        BehaviourInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, _createAnnotationValue);
        EList<JvmAnnotationReference> _annotations = it.getAnnotations();
        BehaviourInferer.this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, ann);
        HashMap<Attribute, String> finalNames = new HashMap<Attribute, String>();
        RepetitionCounter<String, Attribute> board = new RepetitionCounter<String, Attribute>();
        Iterable<Property> _properties = BehaviourInferer.this._mMLExtensions.getProperties(e, "contain", "mapping");
        final Function1<Property, Iterable<Attribute>> _function = new Function1<Property, Iterable<Attribute>>() {
          public Iterable<Attribute> apply(final Property p) {
            Iterable<Attribute> result = null;
            String _id = p.getId();
            boolean _equals = Objects.equal(_id, "mapping");
            if (_equals) {
              Iterable<Attribute> _attributes = BehaviourInferer.this._mMLExtensions.getAttributes(p, "index");
              result = _attributes;
            } else {
              String _id_1 = p.getId();
              boolean _equals_1 = Objects.equal(_id_1, "contain");
              if (_equals_1) {
                Iterable<Attribute> _attributes_1 = BehaviourInferer.this._mMLExtensions.getAttributes(p, "node");
                result = _attributes_1;
              }
            }
            return result;
          }
        };
        Iterable<Iterable<Attribute>> _map = IterableExtensions.<Property, Iterable<Attribute>>map(_properties, _function);
        Iterable<Attribute> _flatten = Iterables.<Attribute>concat(_map);
        List<Attribute> items = IterableExtensions.<Attribute>toList(_flatten);
        int level = 0;
        while ((items.size() > 0)) {
          {
            for (final Attribute item : items) {
              {
                Definition definition = BehaviourInferer.this._mMLExtensions.getValueAsDefinition(item);
                boolean _notEquals = (!Objects.equal(definition, null));
                if (_notEquals) {
                  QualifiedName fullName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(definition);
                  int _segmentCount = fullName.getSegmentCount();
                  int skips = (_segmentCount - (level + 1));
                  if ((skips > 0)) {
                    QualifiedName _skipFirst = fullName.skipFirst(skips);
                    fullName = _skipFirst;
                  }
                  String _invertedName = JavaHelper.toInvertedName(fullName);
                  board.add(_invertedName, item);
                }
              }
            }
            HashMap<String, Attribute> _notRepeated = board.getNotRepeated();
            Set<Map.Entry<String, Attribute>> _entrySet = _notRepeated.entrySet();
            for (final Map.Entry<String, Attribute> entry : _entrySet) {
              Attribute _value = entry.getValue();
              String _key = entry.getKey();
              finalNames.put(_value, _key);
            }
            List<Attribute> _repeated = board.getRepeated();
            items = _repeated;
            level = (level + 1);
            board.clear();
          }
        }
        EList<Feature> _features = e.getFeatures();
        for (final Feature f : _features) {
          boolean _matched = false;
          if (!_matched) {
            if (f instanceof Attribute) {
              _matched=true;
              String _id = ((Attribute)f).getId();
              boolean _matched_1 = false;
              if (!_matched_1) {
                if (Objects.equal(_id, "index")) {
                  _matched_1=true;
                  if ((!prelinkingPhase)) {
                    Definition eIndex = BehaviourInferer.this._mMLExtensions.getValueAsDefinition(((Attribute)f));
                    boolean _notEquals = (!Objects.equal(eIndex, null));
                    if (_notEquals) {
                      final QualifiedName qualifiedName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(eIndex);
                      boolean _notEquals_1 = (!Objects.equal(qualifiedName, null));
                      if (_notEquals_1) {
                        final String indexQualifiedName = XtendHelper.convertQualifiedNameToGenName(qualifiedName);
                        JvmTypeReference indexType = BehaviourInferer.this._typeRefCache.resolve(eIndex, indexQualifiedName);
                        JvmTypeReference iterableType = BehaviourInferer.this._typeRefCache.resolveIterableType(eIndex, indexType);
                        EList<JvmMember> _members = it.getMembers();
                        final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                          public void apply(final JvmOperation it) {
                            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                              public void apply(final ITreeAppendable ap) {
                                ap.append("return (Iterable<");
                                ap.append(indexQualifiedName);
                                ap.append(">)genericGetAll();");
                              }
                            };
                            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                          }
                        };
                        JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "getAll", iterableType, _function_1);
                        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                        EList<JvmMember> _members_1 = it.getMembers();
                        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmTypeReference _resolveOrderExpressionType = BehaviourInferer.this._typeRefCache.resolveOrderExpressionType(e);
                            JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "orderBy", _resolveOrderExpressionType);
                            BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                              public void apply(final ITreeAppendable ap) {
                                ap.append("return (Iterable<");
                                ap.append(indexQualifiedName);
                                ap.append(">)genericGetAll(orderBy);");
                              }
                            };
                            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                          }
                        };
                        JvmOperation _method_1 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "getAll", iterableType, _function_2);
                        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
                        EList<JvmMember> _members_2 = it.getMembers();
                        final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmTypeReference _resolveExpressionType = BehaviourInferer.this._typeRefCache.resolveExpressionType(e);
                            JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "where", _resolveExpressionType);
                            BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                              public void apply(final ITreeAppendable ap) {
                                ap.append("return (Iterable<");
                                ap.append(indexQualifiedName);
                                ap.append(">)genericGet(where);");
                              }
                            };
                            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                          }
                        };
                        JvmOperation _method_2 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "get", iterableType, _function_3);
                        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
                        EList<JvmMember> _members_3 = it.getMembers();
                        final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmTypeReference _resolveExpressionType = BehaviourInferer.this._typeRefCache.resolveExpressionType(e);
                            JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "where", _resolveExpressionType);
                            BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            EList<JvmFormalParameter> _parameters_1 = it.getParameters();
                            JvmTypeReference _resolveOrderExpressionType = BehaviourInferer.this._typeRefCache.resolveOrderExpressionType(e);
                            JvmFormalParameter _parameter_1 = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "orderBy", _resolveOrderExpressionType);
                            BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                              public void apply(final ITreeAppendable ap) {
                                ap.append("return (Iterable<");
                                ap.append(indexQualifiedName);
                                ap.append(">)genericGet(where, orderBy);");
                              }
                            };
                            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                          }
                        };
                        JvmOperation _method_3 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "get", iterableType, _function_4);
                        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_3);
                        EList<JvmMember> _members_4 = it.getMembers();
                        final Procedure1<JvmOperation> _function_5 = new Procedure1<JvmOperation>() {
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmTypeReference _resolveExpressionType = BehaviourInferer.this._typeRefCache.resolveExpressionType(e);
                            JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "where", _resolveExpressionType);
                            BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                              public void apply(final ITreeAppendable ap) {
                                ap.append("return (");
                                ap.append(indexQualifiedName);
                                ap.append(")genericGetFirst(where);");
                              }
                            };
                            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                          }
                        };
                        JvmOperation _method_4 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "getFirst", indexType, _function_5);
                        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_4);
                      }
                    }
                  }
                }
              }
              if (!_matched_1) {
                if (Objects.equal(_id, "target")) {
                  _matched_1=true;
                  boolean _or = false;
                  String _definitionType = e.getDefinitionType();
                  boolean _equals = Objects.equal(_definitionType, "service");
                  if (_equals) {
                    _or = true;
                  } else {
                    String _definitionType_1 = e.getDefinitionType();
                    boolean _equals_1 = Objects.equal(_definitionType_1, "activity");
                    _or = _equals_1;
                  }
                  if (_or) {
                    BehaviourInferer.this.inferTaskMethod(((Attribute)f), it, prelinkingPhase);
                  } else {
                    String _definitionType_2 = e.getDefinitionType();
                    boolean _equals_2 = Objects.equal(_definitionType_2, "importer");
                    if (_equals_2) {
                      Definition targetDefinition = BehaviourInferer.this._mMLExtensions.getValueAsDefinition(((Attribute)f));
                      boolean _notEquals_2 = (!Objects.equal(targetDefinition, null));
                      if (_notEquals_2) {
                        final String schemaTypeName = BehaviourInferer.this.classNameInferer.inferSchemaBehaviourName(targetDefinition);
                        EList<JvmMember> _members_5 = it.getMembers();
                        JvmTypeReference _resolveClassType = BehaviourInferer.this._typeRefCache.resolveClassType(f);
                        final Procedure1<JvmOperation> _function_6 = new Procedure1<JvmOperation>() {
                          public void apply(final JvmOperation it) {
                            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                              public void apply(final ITreeAppendable ap) {
                                ap.append("return ");
                                ap.append(schemaTypeName);
                                ap.append(".class;");
                              }
                            };
                            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                          }
                        };
                        JvmOperation _method_5 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(f, "getTargetSchemaClass", _resolveClassType, _function_6);
                        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_5);
                      }
                    }
                  }
                }
              }
            }
          }
          if (!_matched) {
            if (f instanceof Property) {
              _matched=true;
              String _id = ((Property)f).getId();
              boolean _matched_1 = false;
              if (!_matched_1) {
                if (Objects.equal(_id, "contain")) {
                  _matched_1=true;
                  EList<JvmMember> _members = it.getMembers();
                  BehaviourInferer.this.inferContain(((Property)f), e, _members, prelinkingPhase, finalNames);
                }
              }
              if (!_matched_1) {
                if (Objects.equal(_id, "mapping")) {
                  _matched_1=true;
                  Attribute indexAttb = BehaviourInferer.this._mMLExtensions.getAttribute(((Property)f), "index");
                  EList<JvmMember> _members_1 = it.getMembers();
                  String _get = finalNames.get(indexAttb);
                  BehaviourInferer.this.inferRef(indexAttb, e, _members_1, prelinkingPhase, _get);
                }
              }
              if (!_matched_1) {
                if (Objects.equal(_id, "reference")) {
                  _matched_1=true;
                  BehaviourInferer.this.inferIndexEntryAttributes(((Property)f), it);
                }
              }
              if (!_matched_1) {
                if (Objects.equal(_id, "is-singleton")) {
                  _matched_1=true;
                  BehaviourInferer.this.inferSingleton(e, it);
                }
              }
              if (!_matched_1) {
                if (Objects.equal(_id, "is-abstract")) {
                  _matched_1=true;
                  it.setAbstract(true);
                }
              }
              if (!_matched_1) {
                if (Objects.equal(_id, "operation")) {
                  _matched_1=true;
                  BehaviourInferer.this.inferOperation(((Property)f), it);
                }
              }
              if (!_matched_1) {
                boolean _and = false;
                String _id_1 = ((Property)f).getId();
                boolean _notEquals = (!Objects.equal(_id_1, null));
                if (!_notEquals) {
                  _and = false;
                } else {
                  String _id_2 = ((Property)f).getId();
                  boolean _startsWith = _id_2.startsWith("field-");
                  _and = _startsWith;
                }
                if (_and) {
                  BehaviourInferer.this.fieldInferer.infer(e, ((Property)f), it);
                }
              }
            }
          }
          if (!_matched) {
            if (f instanceof Method) {
              _matched=true;
              String _id = ((Method)f).getId();
              String _attributeJavaIdentifier = JavaHelper.toAttributeJavaIdentifier(_id);
              JvmTypeReference _resolveVoidType = BehaviourInferer.this._typeRefCache.resolveVoidType(f);
              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  XExpression _body = ((Method)f).getBody();
                  BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _body);
                  EList<JvmFormalParameter> _params = ((Method)f).getParams();
                  for (final JvmFormalParameter p : _params) {
                    EList<JvmFormalParameter> _parameters = it.getParameters();
                    String _name = p.getName();
                    JvmTypeReference _parameterType = p.getParameterType();
                    JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
                    BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  }
                }
              };
              final JvmOperation method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(f, _attributeJavaIdentifier, _resolveVoidType, _function_1);
              EList<JvmMember> _members = it.getMembers();
              BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, method);
              boolean _and = false;
              String _id_1 = ((Method)f).getId();
              boolean _equals = Objects.equal(_id_1, "onImportItem");
              if (!_equals) {
                _and = false;
              } else {
                String _definitionType = e.getDefinitionType();
                boolean _equals_1 = Objects.equal(_definitionType, "importer");
                _and = _equals_1;
              }
              if (_and) {
                EList<JvmMember> _members_1 = it.getMembers();
                JvmTypeReference _resolveVoidType_1 = BehaviourInferer.this._typeRefCache.resolveVoidType(f);
                final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                  public void apply(final JvmOperation it) {
                    final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                      public void apply(final ITreeAppendable appender) {
                        appender.append("super.onImportItem(item);\n");
                        appender.append("this.onImportItem((");
                        EList<JvmFormalParameter> _params = ((Method)f).getParams();
                        JvmFormalParameter _head = IterableExtensions.<JvmFormalParameter>head(_params);
                        JvmTypeReference _parameterType = _head.getParameterType();
                        String _qualifiedName = _parameterType.getQualifiedName();
                        appender.append(_qualifiedName);
                        appender.append(")item);");
                      }
                    };
                    BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                    EList<JvmFormalParameter> _parameters = it.getParameters();
                    JvmTypeReference _resolveSchemaType = BehaviourInferer.this._typeRefCache.resolveSchemaType(f);
                    JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(f, "item", _resolveSchemaType);
                    BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  }
                };
                JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(f, "onImportItem", _resolveVoidType_1, _function_2);
                BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                method.setVisibility(JvmVisibility.PRIVATE);
              } else {
                boolean _and_1 = false;
                String _id_2 = ((Method)f).getId();
                boolean _equals_2 = Objects.equal(_id_2, "onExportItem");
                if (!_equals_2) {
                  _and_1 = false;
                } else {
                  String _definitionType_1 = e.getDefinitionType();
                  boolean _equals_3 = Objects.equal(_definitionType_1, "exporter");
                  _and_1 = _equals_3;
                }
                if (_and_1) {
                  EList<JvmMember> _members_2 = it.getMembers();
                  JvmTypeReference _resolveVoidType_2 = BehaviourInferer.this._typeRefCache.resolveVoidType(f);
                  final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                    public void apply(final JvmOperation it) {
                      final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                        public void apply(final ITreeAppendable appender) {
                          appender.append("super.onExportItem(item);\n");
                          appender.append("this.onExportItem((");
                          EList<JvmFormalParameter> _params = ((Method)f).getParams();
                          JvmFormalParameter _head = IterableExtensions.<JvmFormalParameter>head(_params);
                          JvmTypeReference _parameterType = _head.getParameterType();
                          String _qualifiedName = _parameterType.getQualifiedName();
                          appender.append(_qualifiedName);
                          appender.append(")item);");
                        }
                      };
                      BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                      EList<JvmFormalParameter> _parameters = it.getParameters();
                      JvmTypeReference _resolveSchemaType = BehaviourInferer.this._typeRefCache.resolveSchemaType(f);
                      JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(f, "item", _resolveSchemaType);
                      BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    }
                  };
                  JvmOperation _method_1 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(f, "onExportItem", _resolveVoidType_2, _function_3);
                  BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                  method.setVisibility(JvmVisibility.PRIVATE);
                }
              }
            }
          }
          if (!_matched) {
            if (f instanceof Function) {
              _matched=true;
              EList<JvmMember> _members = it.getMembers();
              String _name = ((Function)f).getName();
              JvmTypeReference _type = ((Function)f).getType();
              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  String _documentation = BehaviourInferer.this._monetJvmTypesBuilder.getDocumentation(f);
                  BehaviourInferer.this._monetJvmTypesBuilder.setDocumentation(it, _documentation);
                  EList<JvmFormalParameter> _params = ((Function)f).getParams();
                  for (final JvmFormalParameter p : _params) {
                    EList<JvmFormalParameter> _parameters = it.getParameters();
                    String _name = p.getName();
                    JvmTypeReference _parameterType = p.getParameterType();
                    JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
                    BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  }
                  XExpression _body = ((Function)f).getBody();
                  BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _body);
                }
              };
              JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(f, _name, _type, _function_1);
              BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
            }
          }
          if (!_matched) {
            if (f instanceof Variable) {
              _matched=true;
              boolean _or = false;
              String _definitionType = e.getDefinitionType();
              boolean _equals = Objects.equal(_definitionType, "importer");
              if (_equals) {
                _or = true;
              } else {
                String _definitionType_1 = e.getDefinitionType();
                boolean _equals_1 = Objects.equal(_definitionType_1, "exporter");
                _or = _equals_1;
              }
              if (_or) {
                EList<JvmMember> _members = it.getMembers();
                String _name = ((Variable)f).getName();
                JvmTypeReference _type = ((Variable)f).getType();
                JvmField _field = BehaviourInferer.this._monetJvmTypesBuilder.toField(f, _name, _type);
                BehaviourInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, _field);
                EList<JvmMember> _members_1 = it.getMembers();
                String _name_1 = ((Variable)f).getName();
                JvmTypeReference _type_1 = ((Variable)f).getType();
                JvmOperation _getter = BehaviourInferer.this._monetJvmTypesBuilder.toGetter(f, _name_1, _type_1);
                BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _getter);
                EList<JvmMember> _members_2 = it.getMembers();
                String _name_2 = ((Variable)f).getName();
                JvmTypeReference _type_2 = ((Variable)f).getType();
                JvmOperation _setter = BehaviourInferer.this._monetJvmTypesBuilder.toSetter(f, _name_2, _type_2);
                BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _setter);
              }
            }
          }
        }
        boolean _or = false;
        String _definitionType = e.getDefinitionType();
        boolean _equals = Objects.equal(_definitionType, "service");
        if (_equals) {
          _or = true;
        } else {
          String _definitionType_1 = e.getDefinitionType();
          boolean _equals_1 = Objects.equal(_definitionType_1, "activity");
          _or = _equals_1;
        }
        if (_or) {
          BehaviourInferer.this.taskInferer.inferMethods(e, it);
        } else {
          String _definitionType_2 = e.getDefinitionType();
          boolean _equals_2 = Objects.equal(_definitionType_2, "datastore");
          if (_equals_2) {
            Iterable<Property> _properties_1 = BehaviourInferer.this._mMLExtensions.getProperties(e, "dimension");
            final Procedure1<Property> _function_1 = new Procedure1<Property>() {
              public void apply(final Property dimension) {
                final String dimensionTypeName = BehaviourInferer.this.classNameInferer.inferDimensionName(dimension);
                final JvmTypeReference dimensionType = BehaviourInferer.this.references.getTypeForName(dimensionTypeName, e);
                Code _code = dimension.getCode();
                boolean _notEquals = (!Objects.equal(_code, null));
                if (_notEquals) {
                  Code _code_1 = dimension.getCode();
                  String _value = _code_1.getValue();
                  final String dimensionCode = _value.toString();
                  String _name = dimension.getName();
                  String _javaIdentifier = JavaHelper.toJavaIdentifier(_name);
                  final String dimensionMethodName = ("get" + _javaIdentifier);
                  EList<JvmMember> _members = it.getMembers();
                  final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                    public void apply(final JvmOperation it) {
                      final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                        public void apply(final ITreeAppendable ap) {
                          ap.append("return (");
                          ap.append(dimensionTypeName);
                          ap.append(
                            ((((")this.getDimension(" + dimensionTypeName) + ".class,\"") + dimensionCode) + "\");"));
                        }
                      };
                      BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                    }
                  };
                  JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, dimensionMethodName, dimensionType, _function);
                  BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                }
              }
            };
            IterableExtensions.<Property>forEach(_properties_1, _function_1);
            Iterable<Property> _properties_2 = BehaviourInferer.this._mMLExtensions.getProperties(e, "cube");
            final Procedure1<Property> _function_2 = new Procedure1<Property>() {
              public void apply(final Property cube) {
                final String cubeTypeName = BehaviourInferer.this.classNameInferer.inferCubeName(cube);
                final JvmTypeReference cubeType = BehaviourInferer.this.references.getTypeForName(cubeTypeName, e);
                Code _code = cube.getCode();
                boolean _notEquals = (!Objects.equal(_code, null));
                if (_notEquals) {
                  Code _code_1 = cube.getCode();
                  String _value = _code_1.getValue();
                  final String cubeCode = _value.toString();
                  String _name = cube.getName();
                  String _javaIdentifier = JavaHelper.toJavaIdentifier(_name);
                  final String cubeMethodName = ("get" + _javaIdentifier);
                  EList<JvmMember> _members = it.getMembers();
                  final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                    public void apply(final JvmOperation it) {
                      final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                        public void apply(final ITreeAppendable ap) {
                          ap.append("return (");
                          ap.append(cubeTypeName);
                          ap.append(((((")this.getCube(" + cubeTypeName) + ".class,\"") + cubeCode) + "\");"));
                        }
                      };
                      BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
                    }
                  };
                  JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, cubeMethodName, cubeType, _function);
                  BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                }
              }
            };
            IterableExtensions.<Property>forEach(_properties_2, _function_2);
          } else {
            String _definitionType_3 = e.getDefinitionType();
            boolean _equals_3 = Objects.equal(_definitionType_3, "datastore-builder");
            if (_equals_3) {
              Attribute _attribute = BehaviourInferer.this._mMLExtensions.getAttribute(e, "source");
              Iterable<Property> _properties_3 = BehaviourInferer.this._mMLExtensions.getProperties(e, "to");
              BehaviourInferer.this.datastoreBuilderInferer.inferMethods(e, it, _attribute, _properties_3, prelinkingPhase);
            }
          }
        }
        boolean _isAbstract = e.isAbstract();
        if (_isAbstract) {
          it.setAbstract(true);
        }
        Iterable<Property> _properties_4 = BehaviourInferer.this._mMLExtensions.getProperties(e);
        final Function1<Property, Boolean> _function_3 = new Function1<Property, Boolean>() {
          public Boolean apply(final Property p) {
            boolean _and = false;
            String _id = p.getId();
            boolean _notEquals = (!Objects.equal(_id, null));
            if (!_notEquals) {
              _and = false;
            } else {
              String _id_1 = p.getId();
              boolean _startsWith = _id_1.startsWith("rule");
              _and = _startsWith;
            }
            return Boolean.valueOf(_and);
          }
        };
        Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties_4, _function_3);
        List<Property> rules = IterableExtensions.<Property>toList(_filter);
        int _size = rules.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          BehaviourInferer.this.ruleInferer.infer(e, rules, it);
        }
        Item _child = definitionItem.getChild("operation");
        boolean _notEquals = (!Objects.equal(_child, null));
        if (_notEquals) {
          BehaviourInferer.this.inferCommandOperations(definitionItem, e, it);
        }
        String _token = definitionItem.getToken();
        boolean _equals_4 = Objects.equal(_token, "document");
        if (_equals_4) {
          final String schemaName = BehaviourInferer.this.classNameInferer.inferSchemaBehaviourName(e);
          JvmTypeReference schemaTypeRef = BehaviourInferer.this._monetJvmTypesBuilder.newTypeRef(e, schemaName);
          EList<JvmMember> _members = it.getMembers();
          final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("return (");
                  ap.append(schemaName);
                  ap.append(") this.genericGetSchema();");
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "getSchema", schemaTypeRef, _function_4);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        }
        String _token_1 = definitionItem.getToken();
        boolean _equals_5 = Objects.equal(_token_1, "importer");
        if (_equals_5) {
          BehaviourInferer.this.inferImporterGetInstance(e, it);
        } else {
          String _token_2 = definitionItem.getToken();
          boolean _equals_6 = Objects.equal(_token_2, "exporter");
          if (_equals_6) {
            BehaviourInferer.this.inferExporterGetInstance(e, it);
            EList<JvmMember> _members_1 = it.getMembers();
            JvmTypeReference _resolveExporterScopeType = BehaviourInferer.this._typeRefCache.resolveExporterScopeType(e);
            final Procedure1<JvmOperation> _function_5 = new Procedure1<JvmOperation>() {
              public void apply(final JvmOperation it) {
                EList<JvmFormalParameter> _parameters = it.getParameters();
                JvmTypeReference _resolveNodeType = BehaviourInferer.this._typeRefCache.resolveNodeType(e);
                JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "node", _resolveNodeType);
                BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                  public void apply(final ITreeAppendable ap) {
                    ap.append("return (");
                    String _name = ExporterScope.class.getName();
                    ap.append(_name);
                    ap.append(") new ");
                    String _inferExporterScopeName = BehaviourInferer.this.classNameInferer.inferExporterScopeName(e);
                    ap.append(_inferExporterScopeName);
                    ap.append("(this, node);");
                  }
                };
                BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
              }
            };
            JvmOperation _method_1 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "prepareExportOf", _resolveExporterScopeType, _function_5);
            BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
          } else {
            boolean _and = false;
            boolean _isNodeDefinition = definitionItem.isNodeDefinition();
            if (!_isNodeDefinition) {
              _and = false;
            } else {
              boolean _hasProperty = BehaviourInferer.this._mMLExtensions.hasProperty(e, "is-singleton");
              boolean _not = (!_hasProperty);
              _and = _not;
            }
            if (_and) {
              BehaviourInferer.this.inferNodeCreateInstance(e, it);
            }
          }
        }
        boolean _or_1 = false;
        boolean _and_1 = false;
        if (!(!prelinkingPhase)) {
          _and_1 = false;
        } else {
          boolean _and_2 = false;
          Definition _superType = e.getSuperType();
          boolean _notEquals_1 = (!Objects.equal(_superType, null));
          if (!_notEquals_1) {
            _and_2 = false;
          } else {
            Definition _superType_1 = e.getSuperType();
            QualifiedName _fullyQualifiedName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
            boolean _notEquals_2 = (!Objects.equal(_fullyQualifiedName, null));
            _and_2 = _notEquals_2;
          }
          _and_1 = _and_2;
        }
        if (_and_1) {
          _or_1 = true;
        } else {
          boolean _and_3 = false;
          Definition _replaceSuperType = e.getReplaceSuperType();
          boolean _notEquals_3 = (!Objects.equal(_replaceSuperType, null));
          if (!_notEquals_3) {
            _and_3 = false;
          } else {
            Definition _replaceSuperType_1 = e.getReplaceSuperType();
            QualifiedName _fullyQualifiedName_1 = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_replaceSuperType_1);
            boolean _notEquals_4 = (!Objects.equal(_fullyQualifiedName_1, null));
            _and_3 = _notEquals_4;
          }
          _or_1 = _and_3;
        }
        if (_or_1) {
          JvmTypeReference behaviourSuperType = null;
          Definition definition = e.getSuperType();
          boolean _equals_7 = Objects.equal(definition, null);
          if (_equals_7) {
            Definition _replaceSuperType_2 = e.getReplaceSuperType();
            definition = _replaceSuperType_2;
          }
          String superTypeName = BehaviourInferer.this.classNameInferer.inferBehaviourName(definition);
          JvmTypeReference _typeForName = BehaviourInferer.this.references.getTypeForName(superTypeName, e);
          JvmTypeReference _cloneWithProxies = BehaviourInferer.this._monetJvmTypesBuilder.cloneWithProxies(_typeForName);
          behaviourSuperType = _cloneWithProxies;
          boolean _notEquals_5 = (!Objects.equal(behaviourSuperType, null));
          if (_notEquals_5) {
            EList<JvmTypeReference> _superTypes = it.getSuperTypes();
            BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, behaviourSuperType);
          }
        } else {
          boolean _or_2 = false;
          String _token_3 = definitionItem.getToken();
          boolean _equals_8 = Objects.equal(_token_3, "collection");
          if (_equals_8) {
            _or_2 = true;
          } else {
            String _token_4 = definitionItem.getToken();
            boolean _equals_9 = Objects.equal(_token_4, "catalog");
            _or_2 = _equals_9;
          }
          if (_or_2) {
            EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
            JvmTypeReference _resolve = BehaviourInferer.this._typeRefCache.resolve(e, parentBehaviourClass);
            JvmTypeReference _cloneWithProxies_1 = BehaviourInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolve);
            BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _cloneWithProxies_1);
          } else {
            boolean _or_3 = false;
            String _token_5 = definitionItem.getToken();
            boolean _equals_10 = Objects.equal(_token_5, "index");
            if (_equals_10) {
              _or_3 = true;
            } else {
              String _token_6 = definitionItem.getToken();
              boolean _equals_11 = Objects.equal(_token_6, "datastore-builder");
              _or_3 = _equals_11;
            }
            if (_or_3) {
              JvmTypeReference simpleSuperType = BehaviourInferer.this._typeRefCache.resolve(e, parentBehaviourClass);
              boolean _notEquals_6 = (!Objects.equal(simpleSuperType, null));
              if (_notEquals_6) {
                EList<JvmTypeReference> _superTypes_2 = it.getSuperTypes();
                JvmTypeReference _cloneWithProxies_2 = BehaviourInferer.this._monetJvmTypesBuilder.cloneWithProxies(simpleSuperType);
                BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_2, _cloneWithProxies_2);
              }
            } else {
              EList<JvmTypeReference> _superTypes_3 = it.getSuperTypes();
              JvmTypeReference _resolve_1 = BehaviourInferer.this._typeRefCache.resolve(e, parentBehaviourClass);
              JvmTypeReference _cloneWithProxies_3 = BehaviourInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolve_1);
              BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_3, _cloneWithProxies_3);
            }
          }
        }
      }
    };
    _accept_1.initializeLater(_function_3);
  }
  
  private void inferProperties(final Definition e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    Property eProperties = this._mMLExtensions.getProperty(e, "properties");
    boolean _notEquals = (!Objects.equal(eProperties, null));
    if (_notEquals) {
      String _inferPropertiesName = this.classNameInferer.inferPropertiesName(e);
      this.inferMappingClass(e, acceptor, prelinkingPhase, eProperties, _inferPropertiesName, 
        "getProperties");
    }
  }
  
  private void inferMappings(final Definition e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(e, "mapping");
    final Procedure2<Property, Integer> _function = new Procedure2<Property, Integer>() {
      public void apply(final Property eMapping, final Integer i) {
        String _inferMappingName = BehaviourInferer.this.classNameInferer.inferMappingName(e, (i).intValue());
        BehaviourInferer.this.inferMappingClass(e, acceptor, prelinkingPhase, eMapping, _inferMappingName, 
          "getReference");
      }
    };
    IterableExtensions.<Property>forEach(_properties, _function);
  }
  
  private JvmTypeReference findMappingInParent(final Definition parentDefinition, final Definition currentMappingIndex) {
    Property parentMapping = null;
    int i = 0;
    Iterable<Property> _properties = this._mMLExtensions.getProperties(parentDefinition, "mapping");
    for (final Property px : _properties) {
      {
        Attribute _attribute = this._mMLExtensions.getAttribute(px, "index");
        Referenciable _valueAsReferenciable = this._mMLExtensions.getValueAsReferenciable(_attribute);
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_valueAsReferenciable);
        String parentIndexDefinitionName = _fullyQualifiedName.toString();
        Definition currentOrSuperTypeMappingIndex = currentMappingIndex;
        while (((!Objects.equal(currentOrSuperTypeMappingIndex, null)) && Objects.equal(parentMapping, null))) {
          {
            QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(currentOrSuperTypeMappingIndex);
            String currentMappingIndexName = _fullyQualifiedName_1.toString();
            boolean _equals = Objects.equal(parentIndexDefinitionName, currentMappingIndexName);
            if (_equals) {
              parentMapping = px;
            } else {
              boolean _and = false;
              Definition _superType = currentOrSuperTypeMappingIndex.getSuperType();
              boolean _notEquals = (!Objects.equal(_superType, null));
              if (!_notEquals) {
                _and = false;
              } else {
                Definition _superType_1 = currentOrSuperTypeMappingIndex.getSuperType();
                QualifiedName _fullyQualifiedName_2 = this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
                boolean _notEquals_1 = (!Objects.equal(_fullyQualifiedName_2, null));
                _and = _notEquals_1;
              }
              if (_and) {
                Definition _superType_2 = currentOrSuperTypeMappingIndex.getSuperType();
                currentOrSuperTypeMappingIndex = _superType_2;
              } else {
                currentOrSuperTypeMappingIndex = null;
              }
            }
          }
        }
        boolean _equals = Objects.equal(parentMapping, null);
        if (_equals) {
          i = (i + 1);
        }
      }
    }
    boolean _notEquals = (!Objects.equal(parentMapping, null));
    if (_notEquals) {
      String _inferMappingName = this.classNameInferer.inferMappingName(parentDefinition, i);
      return this._typeRefCache.resolve(parentDefinition, _inferMappingName);
    } else {
      boolean _and = false;
      Definition _superType = parentDefinition.getSuperType();
      boolean _notEquals_1 = (!Objects.equal(_superType, null));
      if (!_notEquals_1) {
        _and = false;
      } else {
        Definition _superType_1 = parentDefinition.getSuperType();
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
        boolean _notEquals_2 = (!Objects.equal(_fullyQualifiedName, null));
        _and = _notEquals_2;
      }
      if (_and) {
        Definition _superType_2 = parentDefinition.getSuperType();
        return this.findMappingInParent(_superType_2, currentMappingIndex);
      } else {
        return null;
      }
    }
  }
  
  private void inferMappingClass(final Definition e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase, final Property eMapping, final String mappingClassName, final String referenceMethodName) {
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(eMapping, mappingClassName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        final Attribute eIndex = BehaviourInferer.this._mMLExtensions.getAttribute(eMapping, "index");
        JvmTypeReference mappingClassSuperType = null;
        boolean _and = false;
        Definition _superType = e.getSuperType();
        boolean _notEquals = (!Objects.equal(_superType, null));
        if (!_notEquals) {
          _and = false;
        } else {
          Definition _superType_1 = e.getSuperType();
          QualifiedName _fullyQualifiedName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
          boolean _notEquals_1 = (!Objects.equal(_fullyQualifiedName, null));
          _and = _notEquals_1;
        }
        if (_and) {
          Definition _superType_2 = e.getSuperType();
          Definition _valueAsDefinition = BehaviourInferer.this._mMLExtensions.getValueAsDefinition(eIndex);
          JvmTypeReference _findMappingInParent = BehaviourInferer.this.findMappingInParent(_superType_2, _valueAsDefinition);
          mappingClassSuperType = _findMappingInParent;
        }
        boolean _equals = Objects.equal(mappingClassSuperType, null);
        if (_equals) {
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          JvmTypeReference _resolveMappingImplType = BehaviourInferer.this._typeRefCache.resolveMappingImplType(e);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _resolveMappingImplType);
        } else {
          EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, mappingClassSuperType);
        }
        final String nodeClassName = BehaviourInferer.this.classNameInferer.inferBehaviourName(e);
        String tempReferenceClassName = Object.class.getName();
        final Method eCalculate = BehaviourInferer.this._mMLExtensions.getMethod(eMapping, "calculate");
        boolean _equals_1 = Objects.equal(eIndex, null);
        if (_equals_1) {
          String _name = Properties.class.getName();
          tempReferenceClassName = _name;
          EList<JvmMember> _members = it.getMembers();
          JvmTypeReference _resolveVoidType = BehaviourInferer.this._typeRefCache.resolveVoidType(e);
          final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  boolean _notEquals = (!Objects.equal(eCalculate, null));
                  if (_notEquals) {
                    ap.append("this.calculate();");
                  }
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation _method = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(eMapping, "calculateMapping", _resolveVoidType, _function);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        } else {
          boolean _and_1 = false;
          AttributeValue _value = eIndex.getValue();
          if (!(_value instanceof XTReference)) {
            _and_1 = false;
          } else {
            AttributeValue _value_1 = eIndex.getValue();
            Referenciable _value_2 = ((XTReference) _value_1).getValue();
            _and_1 = (_value_2 instanceof Definition);
          }
          if (_and_1) {
            AttributeValue _value_3 = eIndex.getValue();
            Referenciable _value_4 = ((XTReference) _value_3).getValue();
            final Definition eIndexDefinition = ((Definition) _value_4);
            String _inferBehaviourName = BehaviourInferer.this.classNameInferer.inferBehaviourName(eIndexDefinition);
            tempReferenceClassName = _inferBehaviourName;
            final String referenceClassName = tempReferenceClassName;
            EList<JvmMember> _members_1 = it.getMembers();
            JvmTypeReference _resolveVoidType_1 = BehaviourInferer.this._typeRefCache.resolveVoidType(e);
            final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
              public void apply(final JvmOperation it) {
                final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                  public void apply(final ITreeAppendable ap) {
                    ap.append("super.calculateMapping();\n");
                    ap.append(nodeClassName);
                    ap.append(" node = (");
                    ap.append(nodeClassName);
                    ap.append(") this.genericGetNode();\n");
                    ap.append(referenceClassName);
                    ap.append(" reference = (");
                    ap.append(referenceClassName);
                    ap.append(") this.genericGetReference();\n");
                    Iterable<Property> _properties = BehaviourInferer.this._mMLExtensions.getProperties(eIndexDefinition, "reference");
                    final Function1<Property, Iterable<Property>> _function = new Function1<Property, Iterable<Property>>() {
                      public Iterable<Property> apply(final Property p) {
                        return BehaviourInferer.this._mMLExtensions.getProperties(p, "attribute");
                      }
                    };
                    Iterable<Iterable<Property>> _map = IterableExtensions.<Property, Iterable<Property>>map(_properties, _function);
                    Iterable<Property> _flatten = Iterables.<Property>concat(_map);
                    final Function1<Property, String> _function_1 = new Function1<Property, String>() {
                      public String apply(final Property p) {
                        return p.getName();
                      }
                    };
                    final Map<String, Property> itemMap = IterableExtensions.<String, Property>toMap(_flatten, _function_1);
                    Iterable<Property> _properties_1 = BehaviourInferer.this._mMLExtensions.getProperties(e);
                    final Function1<Property, Boolean> _function_2 = new Function1<Property, Boolean>() {
                      public Boolean apply(final Property p) {
                        boolean _and = false;
                        String _id = p.getId();
                        boolean _startsWith = _id.startsWith("field-");
                        if (!_startsWith) {
                          _and = false;
                        } else {
                          String _name = p.getName();
                          boolean _containsKey = itemMap.containsKey(_name);
                          _and = _containsKey;
                        }
                        return Boolean.valueOf(_and);
                      }
                    };
                    Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties_1, _function_2);
                    final Function1<Property, String> _function_3 = new Function1<Property, String>() {
                      public String apply(final Property p) {
                        return p.getName();
                      }
                    };
                    Map<String, Property> autoMapping = IterableExtensions.<String, Property>toMap(_filter, _function_3);
                    Set<Map.Entry<String, Property>> _entrySet = autoMapping.entrySet();
                    for (final Map.Entry<String, Property> entry : _entrySet) {
                      {
                        boolean canBeMapped = true;
                        String fieldType = "";
                        Property _value = entry.getValue();
                        String _id = _value.getId();
                        boolean _matched = false;
                        if (!_matched) {
                          if (Objects.equal(_id, "field-boolean")) {
                            _matched=true;
                            fieldType = "BOOLEAN";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-check")) {
                            _matched=true;
                            fieldType = "CHECK";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-date")) {
                            _matched=true;
                            fieldType = "DATE";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-link")) {
                            _matched=true;
                            fieldType = "LINK";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-memo")) {
                            _matched=true;
                            fieldType = "STRING";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-node")) {
                            _matched=true;
                            fieldType = "LINK";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-number")) {
                            _matched=true;
                            fieldType = "REAL";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-summation")) {
                            _matched=true;
                            fieldType = "REAL";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-picture")) {
                            _matched=true;
                            fieldType = "PICTURE";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-select")) {
                            _matched=true;
                            fieldType = "TERM";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-serial")) {
                            _matched=true;
                            fieldType = "STRING";
                          }
                        }
                        if (!_matched) {
                          if (Objects.equal(_id, "field-text")) {
                            _matched=true;
                            fieldType = "STRING";
                          }
                        }
                        String _key = entry.getKey();
                        Property _get = itemMap.get(_key);
                        Attribute _attribute = BehaviourInferer.this._mMLExtensions.getAttribute(_get, "type");
                        String attributeType = BehaviourInferer.this._mMLExtensions.getValueAsString(_attribute);
                        boolean _equals = Objects.equal(attributeType, "INTEGER");
                        if (_equals) {
                          attributeType = "REAL";
                        }
                        boolean _equals_1 = fieldType.equals(attributeType);
                        canBeMapped = _equals_1;
                        if (canBeMapped) {
                          boolean _and = false;
                          Property _value_1 = entry.getValue();
                          String _id_1 = _value_1.getId();
                          boolean _equals_2 = Objects.equal(_id_1, "field-link");
                          if (!_equals_2) {
                            _and = false;
                          } else {
                            boolean _equals_3 = Objects.equal(attributeType, "TERM");
                            _and = _equals_3;
                          }
                          boolean needAsTerm = _and;
                          Property _value_2 = entry.getValue();
                          boolean _hasProperty = BehaviourInferer.this._mMLExtensions.hasProperty(_value_2, "is-multiple");
                          if (_hasProperty) {
                            ap.append("if(node.get");
                            Property _value_3 = entry.getValue();
                            String _name = _value_3.getName();
                            ap.append(_name);
                            ap.append("().size() > 0)\n");
                          }
                          ap.append("reference.set");
                          Property _value_4 = entry.getValue();
                          String _name_1 = _value_4.getName();
                          ap.append(_name_1);
                          ap.append("(node.get");
                          Property _value_5 = entry.getValue();
                          String _name_2 = _value_5.getName();
                          ap.append(_name_2);
                          if (needAsTerm) {
                            ap.append("AsTerm");
                          }
                          ap.append("()");
                          Property _value_6 = entry.getValue();
                          boolean _hasProperty_1 = BehaviourInferer.this._mMLExtensions.hasProperty(_value_6, "is-multiple");
                          if (_hasProperty_1) {
                            ap.append(".get(0)");
                          }
                          ap.append(");\n");
                        }
                      }
                    }
                    boolean _notEquals = (!Objects.equal(eCalculate, null));
                    if (_notEquals) {
                      ap.append("this.calculate();");
                    }
                  }
                };
                BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
              }
            };
            JvmOperation _method_1 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(eMapping, "calculateMapping", _resolveVoidType_1, _function_1);
            BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
          }
        }
        final String referenceClassName_1 = tempReferenceClassName;
        EList<JvmMember> _members_2 = it.getMembers();
        JvmTypeReference _resolve = BehaviourInferer.this._typeRefCache.resolve(e, referenceClassName_1);
        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("return (");
                ap.append(referenceClassName_1);
                ap.append(") this.genericGetReference();");
              }
            };
            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_2 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, referenceMethodName, _resolve, _function_2);
        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
        EList<JvmMember> _members_3 = it.getMembers();
        JvmTypeReference _resolve_1 = BehaviourInferer.this._typeRefCache.resolve(e, nodeClassName);
        final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("return (");
                ap.append(nodeClassName);
                ap.append(") this.genericGetNode();");
              }
            };
            BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method_3 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(e, "getNode", _resolve_1, _function_3);
        BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_3);
        boolean _notEquals_2 = (!Objects.equal(eCalculate, null));
        if (_notEquals_2) {
          EList<JvmMember> _members_4 = it.getMembers();
          String _id = eCalculate.getId();
          JvmTypeReference _resolveVoidType_2 = BehaviourInferer.this._typeRefCache.resolveVoidType(e);
          final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              it.setVisibility(JvmVisibility.PRIVATE);
              XExpression _body = eCalculate.getBody();
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _body);
            }
          };
          JvmOperation _method_4 = BehaviourInferer.this._monetJvmTypesBuilder.toMethod(eMapping, _id, _resolveVoidType_2, _function_4);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_4);
        }
      }
    };
    _accept.initializeLater(_function);
  }
  
  private void inferContain(final Property e, final Definition definition, final EList<JvmMember> members, final boolean prelinkingPhase, final HashMap<Attribute, String> namesMap) {
    Iterable<Attribute> _attributes = this._mMLExtensions.getAttributes(e, "node");
    for (final Attribute containMethod : _attributes) {
      if ((!prelinkingPhase)) {
        final Definition ref = this._mMLExtensions.getValueAsDefinition(containMethod);
        boolean _notEquals = (!Objects.equal(ref, null));
        if (_notEquals) {
          final QualifiedName fullQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(ref);
          String referenceFullQualifiedName = null;
          boolean _notEquals_1 = (!Objects.equal(fullQualifiedName, null));
          if (_notEquals_1) {
            String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(fullQualifiedName);
            referenceFullQualifiedName = _convertQualifiedNameToGenName;
          } else {
            referenceFullQualifiedName = "java.lang.Object";
          }
          JvmTypeReference containTypeReference = null;
          try {
            JvmTypeReference _resolve = this._typeRefCache.resolve(ref, referenceFullQualifiedName);
            containTypeReference = _resolve;
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception ex = (Exception)_t;
              JvmTypeReference _resolveObjectType = this._typeRefCache.resolveObjectType(ref);
              containTypeReference = _resolveObjectType;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
          final JvmTypeReference staticContainTypeReference = containTypeReference;
          String _get = namesMap.get(containMethod);
          String _javaIdentifier = JavaHelper.toJavaIdentifier(_get);
          String _plus = ("get" + _javaIdentifier);
          final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("return (");
                  String _qualifiedName = staticContainTypeReference.getQualifiedName();
                  String _string = _qualifiedName.toString();
                  ap.append(_string);
                  ap.append(")this.getChildNode(\"");
                  String _string_1 = fullQualifiedName.toString();
                  ap.append(_string_1);
                  ap.append("\");");
                }
              };
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmOperation onExecuteMethod = this._monetJvmTypesBuilder.toMethod(e, _plus, staticContainTypeReference, _function);
          onExecuteMethod.setVisibility(JvmVisibility.PUBLIC);
          this._monetJvmTypesBuilder.<JvmOperation>operator_add(members, onExecuteMethod);
        }
      }
    }
  }
  
  private void inferRef(final Attribute indexAttb, final Definition definition, final EList<JvmMember> members, final boolean prelinkingPhase, final String methodName) {
    if (prelinkingPhase) {
      return;
    }
    Referenciable indexAttbValue = this._mMLExtensions.getValueAsReferenciable(indexAttb);
    boolean _notEquals = (!Objects.equal(indexAttbValue, null));
    if (_notEquals) {
      final QualifiedName fullQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(indexAttbValue);
      String referenceFullQualifiedName = null;
      boolean _notEquals_1 = (!Objects.equal(fullQualifiedName, null));
      if (_notEquals_1) {
        String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(fullQualifiedName);
        referenceFullQualifiedName = _convertQualifiedNameToGenName;
      } else {
        referenceFullQualifiedName = "java.lang.Object";
      }
      final JvmTypeReference addTypeReference = this._typeRefCache.resolve(indexAttbValue, referenceFullQualifiedName);
      final String reffullQualifiedName = referenceFullQualifiedName;
      String definitionNameTmp = "";
      boolean _notEquals_2 = (!Objects.equal(fullQualifiedName, null));
      if (_notEquals_2) {
        String _string = fullQualifiedName.toString();
        definitionNameTmp = _string;
      }
      final String definitionName = definitionNameTmp;
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return (");
              ap.append(reffullQualifiedName);
              ap.append(")this.getIndexEntry(\"");
              ap.append(definitionName);
              ap.append("\");");
            }
          };
          BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation onExecuteMethod = this._monetJvmTypesBuilder.toMethod(indexAttb, ("get" + methodName), addTypeReference, _function);
      onExecuteMethod.setVisibility(JvmVisibility.PUBLIC);
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(members, onExecuteMethod);
    }
  }
  
  public boolean inferCommandOperations(final Item definitionItem, final Definition e, final JvmGenericType it) {
    boolean _xblockexpression = false;
    {
      this.inferExecuteCommand(definitionItem, e, it);
      JvmTypeReference _resolveBooleanPrimitiveType = this._typeRefCache.resolveBooleanPrimitiveType(e);
      this.inferExecuteCommandConfirmation(e, it, "when", _resolveBooleanPrimitiveType);
      JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(e);
      _xblockexpression = this.inferExecuteCommandConfirmation(e, it, "onCancel", _resolveVoidType);
    }
    return _xblockexpression;
  }
  
  public boolean inferExecuteCommand(final Item definitionItem, final Definition e, final JvmGenericType it) {
    boolean _xblockexpression = false;
    {
      JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(e);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          EList<JvmFormalParameter> _parameters = it.getParameters();
          JvmTypeReference _resolveStringType = BehaviourInferer.this._typeRefCache.resolveStringType(e);
          JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "operation", _resolveStringType);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          it.setVisibility(JvmVisibility.PUBLIC);
        }
      };
      JvmOperation executeCommandMethod = this._monetJvmTypesBuilder.toMethod(e, "executeCommand", _resolveVoidType, _function);
      final Procedure1<ITreeAppendable> _function_1 = new Procedure1<ITreeAppendable>() {
        public void apply(final ITreeAppendable ap) {
          boolean isFirst = true;
          Iterable<Property> _properties = BehaviourInferer.this._mMLExtensions.getProperties(e, "operation");
          for (final Property p : _properties) {
            {
              if ((!isFirst)) {
                ap.append("else ");
              }
              ap.append("if(operation.equals(\"");
              String _name = p.getName();
              ap.append(_name);
              ap.append("\")) {\n\t");
              ap.append("this.on");
              String _name_1 = p.getName();
              String _javaIdentifier = JavaHelper.toJavaIdentifier(_name_1);
              ap.append(_javaIdentifier);
              ap.append("();\n}");
            }
          }
          if ((!isFirst)) {
            ap.append("else ");
          }
          ap.append("super.executeCommand(operation);");
        }
      };
      this._monetJvmTypesBuilder.setBody(executeCommandMethod, _function_1);
      EList<JvmMember> _members = it.getMembers();
      _xblockexpression = this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, executeCommandMethod);
    }
    return _xblockexpression;
  }
  
  public boolean inferExecuteCommandConfirmation(final Definition definition, final JvmGenericType it, final String methodName, final JvmTypeReference type) {
    boolean _xblockexpression = false;
    {
      String _javaIdentifier = JavaHelper.toJavaIdentifier(methodName);
      String _plus = ("executeCommandConfirmation" + _javaIdentifier);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          EList<JvmFormalParameter> _parameters = it.getParameters();
          JvmTypeReference _resolveStringType = BehaviourInferer.this._typeRefCache.resolveStringType(definition);
          JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(definition, "operation", _resolveStringType);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          it.setVisibility(JvmVisibility.PUBLIC);
        }
      };
      JvmOperation newMethod = this._monetJvmTypesBuilder.toMethod(definition, _plus, type, _function);
      final Procedure1<ITreeAppendable> _function_1 = new Procedure1<ITreeAppendable>() {
        public void apply(final ITreeAppendable ap) {
          boolean isFirst = true;
          Iterable<Property> _properties = BehaviourInferer.this._mMLExtensions.getProperties(definition, "operation");
          for (final Property operation : _properties) {
            {
              final Property confirmation = BehaviourInferer.this._mMLExtensions.getProperty(operation, "confirmation");
              boolean _notEquals = (!Objects.equal(confirmation, null));
              if (_notEquals) {
                Method method = BehaviourInferer.this._mMLExtensions.getMethod(confirmation, methodName);
                boolean _notEquals_1 = (!Objects.equal(method, null));
                if (_notEquals_1) {
                  if ((!isFirst)) {
                    ap.append("else ");
                  }
                  ap.append("if(operation.equals(\"");
                  String _name = operation.getName();
                  ap.append(_name);
                  ap.append("\")) {\n\t");
                  boolean _equals = Objects.equal(methodName, "when");
                  if (_equals) {
                    ap.append("return ");
                  }
                  ap.append(("this." + methodName));
                  String _name_1 = operation.getName();
                  String _javaIdentifier = JavaHelper.toJavaIdentifier(_name_1);
                  ap.append(_javaIdentifier);
                  ap.append("();\n}");
                }
              }
            }
          }
          if ((!isFirst)) {
            ap.append("else ");
          }
          boolean _equals = Objects.equal(methodName, "when");
          if (_equals) {
            ap.append("return ");
          }
          String _javaIdentifier = JavaHelper.toJavaIdentifier(methodName);
          String _plus = ("super.executeCommandConfirmation" + _javaIdentifier);
          String _plus_1 = (_plus + "(operation);");
          ap.append(_plus_1);
        }
      };
      this._monetJvmTypesBuilder.setBody(newMethod, _function_1);
      EList<JvmMember> _members = it.getMembers();
      _xblockexpression = this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, newMethod);
    }
    return _xblockexpression;
  }
  
  private boolean inferImporterGetInstance(final Definition e, final JvmGenericType behaviourClazz) {
    boolean _xblockexpression = false;
    {
      JvmTypeReference returnType = this._typeRefCache.resolveImporterScopeType(e);
      JvmTypeReference _resolveNodeDocumentType = this._typeRefCache.resolveNodeDocumentType(e);
      JvmFormalParameter _parameter = this._monetJvmTypesBuilder.toParameter(e, "node", _resolveNodeDocumentType);
      this.createImporterMethod(e, behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf", _parameter);
      JvmTypeReference _resolveSchemaType = this._typeRefCache.resolveSchemaType(e);
      JvmFormalParameter _parameter_1 = this._monetJvmTypesBuilder.toParameter(e, "schema", _resolveSchemaType);
      this.createImporterMethod(e, behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf", _parameter_1);
      JvmTypeReference _resolveBPIFileType = this._typeRefCache.resolveBPIFileType(e);
      JvmFormalParameter _parameter_2 = this._monetJvmTypesBuilder.toParameter(e, "file", _resolveBPIFileType);
      this.createImporterMethod(e, behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf", _parameter_2);
      JvmTypeReference _resolveStringType = this._typeRefCache.resolveStringType(e);
      JvmFormalParameter _parameter_3 = this._monetJvmTypesBuilder.toParameter(e, "url", _resolveStringType);
      this.createImporterMethod(e, behaviourClazz, "doImportOf", returnType, "ImporterService", "prepareImportOf", _parameter_3);
      JvmTypeReference _resolveCustomerRequestType = this._typeRefCache.resolveCustomerRequestType(e);
      JvmFormalParameter _parameter_4 = this._monetJvmTypesBuilder.toParameter(e, "msg", _resolveCustomerRequestType);
      this.createImporterMethod(e, behaviourClazz, "doImportOf", returnType, _parameter_4);
      JvmTypeReference _resolveContestantRequestType = this._typeRefCache.resolveContestantRequestType(e);
      JvmFormalParameter _parameter_5 = this._monetJvmTypesBuilder.toParameter(e, "msg", _resolveContestantRequestType);
      this.createImporterMethod(e, behaviourClazz, "doImportOf", returnType, _parameter_5);
      JvmTypeReference _resolveProviderResponseType = this._typeRefCache.resolveProviderResponseType(e);
      JvmFormalParameter _parameter_6 = this._monetJvmTypesBuilder.toParameter(e, "msg", _resolveProviderResponseType);
      _xblockexpression = this.createImporterMethod(e, behaviourClazz, "doImportOf", returnType, _parameter_6);
    }
    return _xblockexpression;
  }
  
  private boolean inferExporterGetInstance(final Definition e, final JvmGenericType behaviourClazz) {
    boolean _xblockexpression = false;
    {
      String _inferExporterScopeName = this.classNameInferer.inferExporterScopeName(e);
      JvmTypeReference returnType = this._typeRefCache.resolve(e, _inferExporterScopeName);
      JvmTypeReference _resolveNodeType = this._typeRefCache.resolveNodeType(e);
      JvmFormalParameter _parameter = this._monetJvmTypesBuilder.toParameter(e, "node", _resolveNodeType);
      _xblockexpression = this.createImporterMethod(e, behaviourClazz, "doExportOf", returnType, "ExporterService", "prepareExportOf", _parameter);
    }
    return _xblockexpression;
  }
  
  private boolean inferNodeCreateInstance(final Definition e, final JvmGenericType behaviourClazz) {
    boolean _xblockexpression = false;
    {
      String _inferBehaviourName = this.classNameInferer.inferBehaviourName(e);
      final JvmTypeReference returnType = this._typeRefCache.resolve(e, _inferBehaviourName);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          EList<JvmFormalParameter> _parameters = it.getParameters();
          JvmTypeReference _resolveNodeType = BehaviourInferer.this._typeRefCache.resolveNodeType(e);
          JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "parent", _resolveNodeType);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return (");
              String _qualifiedName = returnType.getQualifiedName();
              ap.append(_qualifiedName);
              ap.append(")org.monet.bpi.NodeService.create(");
              String _inferBehaviourName = BehaviourInferer.this.classNameInferer.inferBehaviourName(e);
              ap.append(_inferBehaviourName);
              ap.append(".class, parent);");
            }
          };
          BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
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
  
  private boolean createImporterMethod(final Definition e, final JvmGenericType behaviourClazz, final String methodName, final JvmTypeReference returnType, final String staticClassName, final String staticClassMethod, final JvmFormalParameter methodParam) {
    boolean _xblockexpression = false;
    {
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          EList<JvmFormalParameter> _parameters = it.getParameters();
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, methodParam);
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return (");
              String _qualifiedName = returnType.getQualifiedName();
              ap.append(_qualifiedName);
              ap.append(")org.monet.bpi.");
              ap.append(staticClassName);
              ap.append(".get(\"");
              QualifiedName _fullyQualifiedName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(e);
              String _string = _fullyQualifiedName.toString();
              ap.append(_string);
              ap.append("\").");
              ap.append(staticClassMethod);
              ap.append("(");
              String _name = methodParam.getName();
              ap.append(_name);
              ap.append(");");
            }
          };
          BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation method = this._monetJvmTypesBuilder.toMethod(e, methodName, returnType, _function);
      method.setStatic(true);
      method.setVisibility(JvmVisibility.PUBLIC);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      _xblockexpression = this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, method);
    }
    return _xblockexpression;
  }
  
  private boolean createImporterMethod(final Definition e, final JvmGenericType behaviourClazz, final String methodName, final JvmTypeReference returnType, final JvmFormalParameter methodParam) {
    boolean _xblockexpression = false;
    {
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          EList<JvmFormalParameter> _parameters = it.getParameters();
          JvmTypeReference _resolveStringType = BehaviourInferer.this._typeRefCache.resolveStringType(e);
          JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(e, "key", _resolveStringType);
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          EList<JvmFormalParameter> _parameters_1 = it.getParameters();
          BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, methodParam);
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return (");
              String _qualifiedName = returnType.getQualifiedName();
              ap.append(_qualifiedName);
              ap.append(")org.monet.bpi.ImporterService.get(\"");
              QualifiedName _fullyQualifiedName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(e);
              String _string = _fullyQualifiedName.toString();
              ap.append(_string);
              ap.append("\").prepareImportOf(key, ");
              String _name = methodParam.getName();
              ap.append(_name);
              ap.append(");");
            }
          };
          BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation method = this._monetJvmTypesBuilder.toMethod(e, methodName, returnType, _function);
      method.setStatic(true);
      method.setVisibility(JvmVisibility.PUBLIC);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      _xblockexpression = this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, method);
    }
    return _xblockexpression;
  }
  
  private boolean inferSingleton(final Definition e, final JvmGenericType behaviourClazz) {
    boolean _xblockexpression = false;
    {
      JvmParameterizedTypeReference _createTypeRef = this.references.createTypeRef(behaviourClazz);
      JvmTypeReference _cloneWithProxies = this._monetJvmTypesBuilder.cloneWithProxies(_createTypeRef);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return (");
              QualifiedName _fullyQualifiedName = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(behaviourClazz);
              String _string = _fullyQualifiedName.toString();
              ap.append(_string);
              ap.append(")org.monet.bpi.NodeService.locate(\"");
              QualifiedName _fullyQualifiedName_1 = BehaviourInferer.this._iQualifiedNameProvider.getFullyQualifiedName(e);
              String _string_1 = _fullyQualifiedName_1.toString();
              ap.append(_string_1);
              ap.append("\");");
            }
          };
          BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation getInstanceMethod = this._monetJvmTypesBuilder.toMethod(e, "getInstance", _cloneWithProxies, _function);
      getInstanceMethod.setStatic(true);
      getInstanceMethod.setVisibility(JvmVisibility.PUBLIC);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      _xblockexpression = this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, getInstanceMethod);
    }
    return _xblockexpression;
  }
  
  private void inferOperation(final Property e, final JvmGenericType behaviourClazz) {
    final Method executeMethod = this._mMLExtensions.getMethod(e, "execute");
    String _name = e.getName();
    String _javaIdentifier = JavaHelper.toJavaIdentifier(_name);
    String _plus = ("on" + _javaIdentifier);
    JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(e);
    final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        boolean _notEquals = (!Objects.equal(executeMethod, null));
        if (_notEquals) {
          XExpression _body = executeMethod.getBody();
          BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _body);
        } else {
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable it) {
            }
          };
          BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      }
    };
    JvmOperation onExecuteMethod = this._monetJvmTypesBuilder.toMethod(e, _plus, _resolveVoidType, _function);
    onExecuteMethod.setVisibility(JvmVisibility.PUBLIC);
    EList<JvmMember> _members = behaviourClazz.getMembers();
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, onExecuteMethod);
    this.inferOperationConfirmation(e, behaviourClazz);
  }
  
  private void inferOperationConfirmation(final Property e, final JvmGenericType behaviourClazz) {
    final Property confirmation = this._mMLExtensions.getProperty(e, "confirmation");
    boolean _notEquals = (!Objects.equal(confirmation, null));
    if (_notEquals) {
      Iterable<Method> _methods = this._mMLExtensions.getMethods(confirmation);
      for (final Method m : _methods) {
        {
          JvmTypeReference type = this._typeRefCache.resolveVoidType(m);
          String _id = m.getId();
          boolean _equals = Objects.equal(_id, "when");
          if (_equals) {
            JvmTypeReference _resolveBooleanPrimitiveType = this._typeRefCache.resolveBooleanPrimitiveType(m);
            type = _resolveBooleanPrimitiveType;
          }
          String _id_1 = m.getId();
          String _name = e.getName();
          String _javaIdentifier = JavaHelper.toJavaIdentifier(_name);
          String _plus = (_id_1 + _javaIdentifier);
          final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              XExpression _body = m.getBody();
              BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _body);
              EList<JvmFormalParameter> _params = m.getParams();
              for (final JvmFormalParameter p : _params) {
                EList<JvmFormalParameter> _parameters = it.getParameters();
                String _name = p.getName();
                JvmTypeReference _parameterType = p.getParameterType();
                JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
                BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              }
            }
          };
          final JvmOperation onMethod = this._monetJvmTypesBuilder.toMethod(m, _plus, type, _function);
          onMethod.setVisibility(JvmVisibility.PUBLIC);
          EList<JvmMember> _members = behaviourClazz.getMembers();
          this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, onMethod);
        }
      }
    }
  }
  
  private void inferIndexEntryAttributes(final Property property, final JvmGenericType behaviourClazz) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(property);
    final Procedure1<Property> _function = new Procedure1<Property>() {
      public void apply(final Property p) {
        BehaviourInferer.this.inferIndexEntryAttribute(p, behaviourClazz);
      }
    };
    IterableExtensions.<Property>forEach(_properties, _function);
  }
  
  private void inferIndexEntryAttribute(final Property property, final JvmGenericType behaviourClazz) {
    String type = null;
    Attribute _attribute = this._mMLExtensions.getAttribute(property, "type");
    String _valueAsString = this._mMLExtensions.getValueAsString(_attribute);
    type = _valueAsString;
    boolean _equals = Objects.equal(type, null);
    if (_equals) {
      return;
    }
    boolean generateParam = true;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(type, "TERM")) {
        _matched=true;
        type = "org.monet.bpi.types.Term";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "LINK")) {
        _matched=true;
        type = "org.monet.bpi.types.Link";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "DATE")) {
        _matched=true;
        type = "org.monet.bpi.types.Date";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "CHECK")) {
        _matched=true;
        type = "org.monet.bpi.types.CheckList";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "STRING")) {
        _matched=true;
        type = "java.lang.String";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "REAL")) {
        _matched=true;
        type = "org.monet.bpi.types.Number";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "INTEGER")) {
        _matched=true;
        type = "org.monet.bpi.types.Number";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "BOOLEAN")) {
        _matched=true;
        type = "java.lang.Boolean";
      }
    }
    if (!_matched) {
      if (Objects.equal(type, "PICTURE")) {
        _matched=true;
        type = "org.monet.bpi.types.Picture";
        generateParam = false;
      }
    }
    if (!_matched) {
      return;
    }
    final String finalType = type;
    String _name = property.getName();
    String attributeNameCapitalized = JavaHelper.toJavaIdentifier(_name);
    if (generateParam) {
      JvmTypeReference _resolveParamType = this._typeRefCache.resolveParamType(property);
      JvmField field = this._monetJvmTypesBuilder.toField(property, attributeNameCapitalized, _resolveParamType);
      field.setStatic(true);
      field.setFinal(true);
      field.setVisibility(JvmVisibility.PUBLIC);
      final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
        public void apply(final ITreeAppendable ap) {
          ap.append("new org.monet.bpi.Param(\"");
          Code _code = property.getCode();
          String _value = _code.getValue();
          ap.append(_value);
          ap.append("\")");
        }
      };
      this._monetJvmTypesBuilder.setInitializer(field, _function);
      EList<JvmMember> _members = behaviourClazz.getMembers();
      this._monetJvmTypesBuilder.<JvmField>operator_add(_members, field);
    }
    EList<JvmMember> _members_1 = behaviourClazz.getMembers();
    JvmTypeReference _resolve = this._typeRefCache.resolve(property, finalType);
    final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("return (");
            ap.append(finalType);
            ap.append(")this.getAttribute(\"");
            Code _code = property.getCode();
            String _value = _code.getValue();
            ap.append(_value);
            ap.append("\");");
          }
        };
        BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation _method = this._monetJvmTypesBuilder.toMethod(property, ("get" + attributeNameCapitalized), _resolve, _function_1);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
    EList<JvmMember> _members_2 = behaviourClazz.getMembers();
    JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(property);
    final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("this.setAttribute(\"");
            Code _code = property.getCode();
            String _value = _code.getValue();
            ap.append(_value);
            ap.append("\", ");
            String _name = property.getName();
            ap.append(_name);
            ap.append(");");
          }
        };
        BehaviourInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        EList<JvmFormalParameter> _parameters = it.getParameters();
        String _name = property.getName();
        JvmTypeReference _resolve = BehaviourInferer.this._typeRefCache.resolve(property, finalType);
        JvmFormalParameter _parameter = BehaviourInferer.this._monetJvmTypesBuilder.toParameter(property, _name, _resolve);
        BehaviourInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
      }
    };
    JvmOperation _method_1 = this._monetJvmTypesBuilder.toMethod(property, ("set" + attributeNameCapitalized), _resolveVoidType, _function_2);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
  }
}
