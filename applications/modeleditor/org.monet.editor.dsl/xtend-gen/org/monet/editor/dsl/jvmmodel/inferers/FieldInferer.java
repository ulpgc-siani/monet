package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.util.Primitives;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.bpi.FieldBoolean;
import org.monet.bpi.FieldCheck;
import org.monet.bpi.FieldComposite;
import org.monet.bpi.FieldDate;
import org.monet.bpi.FieldFile;
import org.monet.bpi.FieldLink;
import org.monet.bpi.FieldMemo;
import org.monet.bpi.FieldNode;
import org.monet.bpi.FieldNumber;
import org.monet.bpi.FieldPicture;
import org.monet.bpi.FieldSelect;
import org.monet.bpi.FieldSerial;
import org.monet.bpi.FieldSummation;
import org.monet.bpi.FieldText;
import org.monet.bpi.java.FieldCompositeImpl;
import org.monet.bpi.types.CheckList;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.File;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Picture;
import org.monet.bpi.types.Term;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;

@SuppressWarnings("all")
public class FieldInferer {
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
  private Primitives primitives;
  
  @Inject
  private TypeReferences references;
  
  public void infer(final Definition d, final Property p, final JvmGenericType behaviourClazz) {
    String _name = p.getName();
    boolean _equals = Objects.equal(_name, null);
    if (_equals) {
      return;
    }
    String _name_1 = p.getName();
    final String fieldName = JavaHelper.toJavaIdentifier(_name_1);
    final boolean isMultiple = this._mMLExtensions.hasProperty(p, "is-multiple");
    final JvmTypeReference fieldValueType = this.inferValueType(p);
    final JvmTypeReference fieldType = this.inferType(d, p, fieldValueType);
    final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("return ((");
            String _qualifiedName = fieldType.getQualifiedName();
            ap.append(_qualifiedName);
            ap.append(")this.getField(\"");
            String _name = d.getName();
            ap.append(_name);
            ap.append("\", \"");
            String _name_1 = p.getName();
            ap.append(_name_1);
            ap.append("\"));");
          }
        };
        FieldInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        it.setVisibility(JvmVisibility.PUBLIC);
      }
    };
    JvmOperation getField = this._monetJvmTypesBuilder.toMethod(p, (("get" + fieldName) + "Field"), fieldType, _function);
    EList<JvmMember> _members = behaviourClazz.getMembers();
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, getField);
    String _id = p.getId();
    boolean _notEquals = (!Objects.equal(_id, "field-composite"));
    if (_notEquals) {
      JvmTypeReference getterReturnType = fieldValueType;
      if (isMultiple) {
        JvmTypeReference _asWrapperTypeIfPrimitive = this.primitives.asWrapperTypeIfPrimitive(fieldValueType);
        JvmTypeReference _resolveArrayListType = this._typeRefCache.resolveArrayListType(p, _asWrapperTypeIfPrimitive);
        getterReturnType = _resolveArrayListType;
      }
      final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              ap.append("return ((");
              String _qualifiedName = fieldType.getQualifiedName();
              ap.append(_qualifiedName);
              ap.append(")this.getField(\"");
              String _name = d.getName();
              ap.append(_name);
              ap.append("\", \"");
              String _name_1 = p.getName();
              ap.append(_name_1);
              ap.append("\")).get");
              if (isMultiple) {
                ap.append("All");
              }
              ap.append("();");
            }
          };
          FieldInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          it.setVisibility(JvmVisibility.PUBLIC);
        }
      };
      JvmOperation _method = this._monetJvmTypesBuilder.toMethod(p, ("get" + fieldName), getterReturnType, _function_1);
      getField = _method;
      EList<JvmMember> _members_1 = behaviourClazz.getMembers();
      this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, getField);
      boolean _or = false;
      String _id_1 = p.getId();
      boolean _equals_1 = Objects.equal(_id_1, "field-link");
      if (_equals_1) {
        _or = true;
      } else {
        String _id_2 = p.getId();
        boolean _equals_2 = Objects.equal(_id_2, "field-node");
        _or = _equals_2;
      }
      if (_or) {
        JvmTypeReference returnType = this._typeRefCache.resolveTermType(p);
        if (isMultiple) {
          JvmTypeReference _resolveArrayListType_1 = this._typeRefCache.resolveArrayListType(p, returnType);
          returnType = _resolveArrayListType_1;
        }
        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("return ((");
                String _qualifiedName = fieldType.getQualifiedName();
                ap.append(_qualifiedName);
                ap.append(")this.getField(\"");
                String _name = d.getName();
                ap.append(_name);
                ap.append("\", \"");
                String _name_1 = p.getName();
                ap.append(_name_1);
                ap.append("\")).get");
                if (isMultiple) {
                  ap.append("All");
                }
                ap.append("AsTerm();");
              }
            };
            FieldInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            it.setVisibility(JvmVisibility.PUBLIC);
          }
        };
        JvmOperation _method_1 = this._monetJvmTypesBuilder.toMethod(p, (("get" + fieldName) + "AsTerm"), returnType, _function_2);
        getField = _method_1;
        EList<JvmMember> _members_2 = behaviourClazz.getMembers();
        this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, getField);
      }
      if ((!isMultiple)) {
        JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(p);
        final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("this.get");
                ap.append(fieldName);
                ap.append("Field().set(value);");
              }
            };
            FieldInferer.this._monetJvmTypesBuilder.setBody(it, _function);
            it.setVisibility(JvmVisibility.PUBLIC);
            EList<JvmFormalParameter> _parameters = it.getParameters();
            JvmFormalParameter _parameter = FieldInferer.this._monetJvmTypesBuilder.toParameter(p, "value", fieldValueType);
            FieldInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
          }
        };
        JvmOperation setField = this._monetJvmTypesBuilder.toMethod(p, ("set" + fieldName), _resolveVoidType, _function_3);
        EList<JvmMember> _members_3 = behaviourClazz.getMembers();
        this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_3, setField);
        String _id_3 = p.getId();
        boolean _equals_3 = Objects.equal(_id_3, "field-number");
        if (_equals_3) {
          JvmTypeReference _resolveVoidType_1 = this._typeRefCache.resolveVoidType(p);
          final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
            public void apply(final JvmOperation it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable ap) {
                  ap.append("this.get");
                  ap.append(fieldName);
                  ap.append("Field().set(new org.monet.bpi.types.Number(value));");
                }
              };
              FieldInferer.this._monetJvmTypesBuilder.setBody(it, _function);
              it.setVisibility(JvmVisibility.PUBLIC);
              EList<JvmFormalParameter> _parameters = it.getParameters();
              JvmTypeReference _resolveDoublePrimitiveType = FieldInferer.this._typeRefCache.resolveDoublePrimitiveType(p);
              JvmFormalParameter _parameter = FieldInferer.this._monetJvmTypesBuilder.toParameter(p, "value", _resolveDoublePrimitiveType);
              FieldInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
            }
          };
          JvmOperation _method_2 = this._monetJvmTypesBuilder.toMethod(p, ("set" + fieldName), _resolveVoidType_1, _function_4);
          setField = _method_2;
          EList<JvmMember> _members_4 = behaviourClazz.getMembers();
          this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_4, setField);
        }
      }
    }
  }
  
  public JvmTypeReference inferType(final Definition d, final Property p, final JvmTypeReference fieldValueType) {
    JvmTypeReference fieldTypeReference = null;
    Class<?> baseType = null;
    JvmTypeReference params = null;
    String _id = p.getId();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_id, "field-boolean")) {
        _matched=true;
        baseType = FieldBoolean.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-check")) {
        _matched=true;
        baseType = FieldCheck.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-date")) {
        _matched=true;
        baseType = FieldDate.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-file")) {
        _matched=true;
        baseType = FieldFile.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-link")) {
        _matched=true;
        baseType = FieldLink.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-memo")) {
        _matched=true;
        baseType = FieldMemo.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-node")) {
        _matched=true;
        baseType = FieldNode.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-number")) {
        _matched=true;
        baseType = FieldNumber.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-picture")) {
        _matched=true;
        baseType = FieldPicture.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-select")) {
        _matched=true;
        baseType = FieldSelect.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-serial")) {
        _matched=true;
        baseType = FieldSerial.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-text")) {
        _matched=true;
        baseType = FieldText.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-summation")) {
        _matched=true;
        baseType = FieldSummation.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-composite")) {
        _matched=true;
        baseType = FieldComposite.class;
      }
    }
    boolean _equals = Objects.equal(fieldTypeReference, null);
    if (_equals) {
      JvmTypeReference[] _createParametersArray = XtendHelper.createParametersArray(params);
      JvmTypeReference _resolve = this._typeRefCache.resolve(p, baseType, _createParametersArray);
      fieldTypeReference = _resolve;
    }
    boolean _hasProperty = this._mMLExtensions.hasProperty(p, "is-multiple");
    if (_hasProperty) {
      JvmTypeReference _asWrapperTypeIfPrimitive = this.primitives.asWrapperTypeIfPrimitive(fieldValueType);
      JvmTypeReference[] _createParametersArray_1 = XtendHelper.createParametersArray(fieldTypeReference, _asWrapperTypeIfPrimitive);
      return this._typeRefCache.resolveFieldMultipleType(p, _createParametersArray_1);
    }
    return fieldTypeReference;
  }
  
  public void inferTypeClass(final Definition d, final Property p, final IJvmDeclaredTypeAcceptor acceptor) {
    String _id = p.getId();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_id, "field-composite")) {
        _matched=true;
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(p);
        String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
        JvmGenericType _class = this._monetJvmTypesBuilder.toClass(p, _convertQualifiedNameToGenName);
        IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
        final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
          public void apply(final JvmGenericType it) {
            EList<JvmMember> _members = it.getMembers();
            final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
              public void apply(final JvmConstructor it) {
                it.setVisibility(JvmVisibility.PUBLIC);
              }
            };
            JvmConstructor _constructor = FieldInferer.this._monetJvmTypesBuilder.toConstructor(p, _function);
            FieldInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
            String _name = p.getName();
            JvmTypeReference superType = FieldInferer.this.findCompositeInParents(d, _name);
            EList<JvmTypeReference> _superTypes = it.getSuperTypes();
            JvmTypeReference _newTypeRef = FieldInferer.this._monetJvmTypesBuilder.newTypeRef(p, FieldComposite.class);
            FieldInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _newTypeRef);
            boolean _notEquals = (!Objects.equal(superType, null));
            if (_notEquals) {
              EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
              FieldInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, superType);
            } else {
              EList<JvmTypeReference> _superTypes_2 = it.getSuperTypes();
              JvmTypeReference _newTypeRef_1 = FieldInferer.this._monetJvmTypesBuilder.newTypeRef(p, FieldCompositeImpl.class);
              FieldInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_2, _newTypeRef_1);
            }
            Iterable<Property> _properties = FieldInferer.this._mMLExtensions.getProperties(p);
            final Function1<Property, Boolean> _function_1 = new Function1<Property, Boolean>() {
              public Boolean apply(final Property px) {
                boolean _and = false;
                String _id = px.getId();
                boolean _notEquals = (!Objects.equal(_id, null));
                if (!_notEquals) {
                  _and = false;
                } else {
                  String _id_1 = px.getId();
                  boolean _startsWith = _id_1.startsWith("field");
                  _and = _startsWith;
                }
                return Boolean.valueOf(_and);
              }
            };
            Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties, _function_1);
            final Procedure1<Property> _function_2 = new Procedure1<Property>() {
              public void apply(final Property px) {
                FieldInferer.this.infer(d, px, it);
              }
            };
            IterableExtensions.<Property>forEach(_filter, _function_2);
          }
        };
        _accept.initializeLater(_function);
        Iterable<Property> _properties = this._mMLExtensions.getProperties(p);
        final Function1<Property, Boolean> _function_1 = new Function1<Property, Boolean>() {
          public Boolean apply(final Property px) {
            boolean _and = false;
            String _id = px.getId();
            boolean _notEquals = (!Objects.equal(_id, null));
            if (!_notEquals) {
              _and = false;
            } else {
              String _id_1 = px.getId();
              boolean _startsWith = _id_1.startsWith("field");
              _and = _startsWith;
            }
            return Boolean.valueOf(_and);
          }
        };
        Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties, _function_1);
        final Procedure1<Property> _function_2 = new Procedure1<Property>() {
          public void apply(final Property px) {
            FieldInferer.this.inferTypeClass(d, px, acceptor);
          }
        };
        IterableExtensions.<Property>forEach(_filter, _function_2);
      }
    }
  }
  
  private JvmTypeReference findCompositeInParents(final Definition d, final String name) {
    Definition _superType = d.getSuperType();
    boolean _equals = Objects.equal(_superType, null);
    if (_equals) {
      return null;
    }
    Definition _superType_1 = d.getSuperType();
    Iterable<Property> _properties = this._mMLExtensions.getProperties(_superType_1, "field-composite");
    final Function1<Property, Property> _function = new Function1<Property, Property>() {
      public Property apply(final Property p) {
        return FieldInferer.this.findCompositeRecursive(p, name);
      }
    };
    Iterable<Property> _map = IterableExtensions.<Property, Property>map(_properties, _function);
    final Function1<Property, Boolean> _function_1 = new Function1<Property, Boolean>() {
      public Boolean apply(final Property p) {
        return Boolean.valueOf((!Objects.equal(p, null)));
      }
    };
    Iterable<Property> _filter = IterableExtensions.<Property>filter(_map, _function_1);
    Property composite = IterableExtensions.<Property>head(_filter);
    boolean _equals_1 = Objects.equal(composite, null);
    if (_equals_1) {
      Definition _superType_2 = d.getSuperType();
      return this.findCompositeInParents(_superType_2, name);
    }
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(composite);
    String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(_fullyQualifiedName);
    return this.references.getTypeForName(_convertQualifiedNameToGenName, composite);
  }
  
  private Property findCompositeRecursive(final Property composite, final String name) {
    String _name = composite.getName();
    boolean _equals = Objects.equal(_name, name);
    if (_equals) {
      return composite;
    } else {
      Iterable<Property> _properties = this._mMLExtensions.getProperties(composite, "field-composite");
      final Function1<Property, Property> _function = new Function1<Property, Property>() {
        public Property apply(final Property p) {
          return FieldInferer.this.findCompositeRecursive(p, name);
        }
      };
      Iterable<Property> _map = IterableExtensions.<Property, Property>map(_properties, _function);
      return IterableExtensions.<Property>head(_map);
    }
  }
  
  public JvmTypeReference inferValueType(final Property p) {
    Class<?> type = null;
    String _id = p.getId();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_id, "field-boolean")) {
        _matched=true;
        type = boolean.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-check")) {
        _matched=true;
        type = CheckList.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-date")) {
        _matched=true;
        type = Date.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-file")) {
        _matched=true;
        type = File.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-link")) {
        _matched=true;
        type = Link.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-memo")) {
        _matched=true;
        type = String.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-node")) {
        _matched=true;
        type = Link.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-number")) {
        _matched=true;
        type = org.monet.bpi.types.Number.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-summation")) {
        _matched=true;
        type = org.monet.bpi.types.Number.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-picture")) {
        _matched=true;
        type = Picture.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-select")) {
        _matched=true;
        type = Term.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-serial")) {
        _matched=true;
        type = String.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "field-text")) {
        _matched=true;
        type = String.class;
      }
    }
    if (!_matched) {
      type = Void.class;
    }
    return this._typeRefCache.resolve(p, type);
  }
}
