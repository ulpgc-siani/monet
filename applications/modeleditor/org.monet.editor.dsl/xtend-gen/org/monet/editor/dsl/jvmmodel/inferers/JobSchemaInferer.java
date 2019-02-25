package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmAnnotationValue;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.access.impl.ClassURIHelper;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.bpi.types.CheckList;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.Location;
import org.monet.bpi.types.PictureLink;
import org.monet.bpi.types.Term;
import org.monet.bpi.types.VideoLink;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@SuppressWarnings("all")
public class JobSchemaInferer {
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
  
  public void infer(final Definition definition, final IJvmDeclaredTypeAcceptor acceptor) {
    String _inferSchemaBehaviourName = this.classNameInferer.inferSchemaBehaviourName(definition);
    final JvmGenericType schemaClass = this._monetJvmTypesBuilder.toClass(definition, _inferSchemaBehaviourName);
    Iterable<Property> _properties = this._mMLExtensions.getProperties(definition, "step");
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property ss) {
        JobSchemaInferer.this.inferStepClass(ss, acceptor, schemaClass);
      }
    };
    _properties.forEach(_function);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(schemaClass);
    final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        boolean hasSuper = false;
        JobSchemaInferer.this.inferMethods(it);
        boolean _and = false;
        Definition _superType = definition.getSuperType();
        boolean _notEquals = (!Objects.equal(_superType, null));
        if (!_notEquals) {
          _and = false;
        } else {
          Definition _superType_1 = definition.getSuperType();
          QualifiedName _fullyQualifiedName = JobSchemaInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
          boolean _notEquals_1 = (!Objects.equal(_fullyQualifiedName, null));
          _and = _notEquals_1;
        }
        if (_and) {
          Definition _superType_2 = definition.getSuperType();
          String _inferSchemaBehaviourName = JobSchemaInferer.this.classNameInferer.inferSchemaBehaviourName(_superType_2);
          JvmTypeReference _typeForName = JobSchemaInferer.this.references.getTypeForName(_inferSchemaBehaviourName, definition);
          JvmTypeReference parentSchemaType = JobSchemaInferer.this._monetJvmTypesBuilder.cloneWithProxies(_typeForName);
          boolean _notEquals_2 = (!Objects.equal(parentSchemaType, null));
          hasSuper = _notEquals_2;
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          JobSchemaInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, parentSchemaType);
        }
        if ((!hasSuper)) {
          EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
          JvmTypeReference _resolveSchemaType = JobSchemaInferer.this._typeRefCache.resolveSchemaType(definition);
          JvmTypeReference _cloneWithProxies = JobSchemaInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolveSchemaType);
          JobSchemaInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _cloneWithProxies);
        }
        Class<Root> annType = Root.class;
        JvmAnnotationReference ann = JobSchemaInferer.this._monetJvmTypesBuilder.toAnnotation(definition, annType);
        JvmAnnotationValue rootAnnotationValue = XtendHelper.createAnnotationValue("name", "schema", annType, JobSchemaInferer.this.uriHelper);
        EList<JvmAnnotationValue> _explicitValues = ann.getExplicitValues();
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, rootAnnotationValue);
        JvmAnnotationValue _createAnnotationValue = XtendHelper.createAnnotationValue("strict", Boolean.valueOf(false), annType, JobSchemaInferer.this.uriHelper);
        rootAnnotationValue = _createAnnotationValue;
        EList<JvmAnnotationValue> _explicitValues_1 = ann.getExplicitValues();
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_1, rootAnnotationValue);
        EList<JvmAnnotationReference> _annotations = it.getAnnotations();
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, ann);
      }
    };
    _accept.initializeLater(_function_1);
  }
  
  public void inferMethods(final JvmGenericType scope) {
    EClass _eClass = scope.eClass();
    JvmTypeReference _resolveStringType = this._typeRefCache.resolveStringType(scope);
    JvmTypeReference _resolveObjectType = this._typeRefCache.resolveObjectType(scope);
    JvmTypeReference _resolveMapType = this._typeRefCache.resolveMapType(scope, _resolveStringType, _resolveObjectType);
    final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        it.setVisibility(JvmVisibility.PUBLIC);
      }
    };
    JvmOperation getAllMethod = this._monetJvmTypesBuilder.toMethod(_eClass, "getAll", _resolveMapType, _function);
    EList<JvmMember> _members = scope.getMembers();
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, getAllMethod);
    final Procedure1<ITreeAppendable> _function_1 = new Procedure1<ITreeAppendable>() {
      public void apply(final ITreeAppendable appender) {
        appender.append("return null;");
      }
    };
    this._monetJvmTypesBuilder.setBody(getAllMethod, _function_1);
    EClass _eClass_1 = scope.eClass();
    JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(scope);
    final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        it.setVisibility(JvmVisibility.PUBLIC);
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = JobSchemaInferer.this._typeRefCache.resolveStringType(scope);
        JvmFormalParameter _parameter = JobSchemaInferer.this._monetJvmTypesBuilder.toParameter(scope, "key", _resolveStringType);
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveObjectType = JobSchemaInferer.this._typeRefCache.resolveObjectType(scope);
        JvmFormalParameter _parameter_1 = JobSchemaInferer.this._monetJvmTypesBuilder.toParameter(scope, "value", _resolveObjectType);
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
      }
    };
    JvmOperation setMethod = this._monetJvmTypesBuilder.toMethod(_eClass_1, "set", _resolveVoidType, _function_2);
    EList<JvmMember> _members_1 = scope.getMembers();
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, setMethod);
    final Procedure1<ITreeAppendable> _function_3 = new Procedure1<ITreeAppendable>() {
      public void apply(final ITreeAppendable appender) {
        appender.append("\n");
      }
    };
    this._monetJvmTypesBuilder.setBody(setMethod, _function_3);
  }
  
  public void inferStepClass(final Property step, final IJvmDeclaredTypeAcceptor acceptor, final JvmGenericType scope) {
    Iterable<Property> _properties = this._mMLExtensions.getProperties(step);
    final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
      public Boolean apply(final Property p) {
        boolean _and = false;
        String _id = p.getId();
        boolean _notEquals = (!Objects.equal(_id, null));
        if (!_notEquals) {
          _and = false;
        } else {
          boolean _or = false;
          String _id_1 = p.getId();
          boolean _startsWith = _id_1.startsWith("edit");
          if (_startsWith) {
            _or = true;
          } else {
            String _id_2 = p.getId();
            boolean _startsWith_1 = _id_2.startsWith("capture");
            _or = _startsWith_1;
          }
          _and = _or;
        }
        return Boolean.valueOf(_and);
      }
    };
    Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties, _function);
    int _size = IterableExtensions.size(_filter);
    boolean _equals = (_size == 0);
    if (_equals) {
      return;
    }
    String _qualifiedName = scope.getQualifiedName();
    String _name = step.getName();
    QualifiedName stepQualifiedName = QualifiedName.create(_qualifiedName, _name);
    String stepName = XtendHelper.convertQualifiedNameToGenName(stepQualifiedName);
    final JvmGenericType stepClass = this._monetJvmTypesBuilder.toClass(step, stepName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(stepClass);
    final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        Class<?> annotation = null;
        JvmTypeReference typeRef = JobSchemaInferer.this.references.createTypeRef(it);
        boolean many = JobSchemaInferer.this._mMLExtensions.hasProperty(step, "is-multiple");
        if (many) {
          annotation = ElementList.class;
          JvmTypeReference _resolveArrayListType = JobSchemaInferer.this._typeRefCache.resolveArrayListType(step, typeRef);
          typeRef = _resolveArrayListType;
        } else {
          annotation = Element.class;
        }
        final JvmTypeReference stepTypeRef = typeRef;
        Iterable<Property> _properties = JobSchemaInferer.this._mMLExtensions.getProperties(step);
        for (final Property f : _properties) {
          boolean _and = false;
          String _id = f.getId();
          boolean _notEquals = (!Objects.equal(_id, null));
          if (!_notEquals) {
            _and = false;
          } else {
            boolean _or = false;
            String _id_1 = f.getId();
            boolean _startsWith = _id_1.startsWith("edit");
            if (_startsWith) {
              _or = true;
            } else {
              String _id_2 = f.getId();
              boolean _startsWith_1 = _id_2.startsWith("capture");
              _or = _startsWith_1;
            }
            _and = _or;
          }
          if (_and) {
            JobSchemaInferer.this.inferEdit(f, it);
          }
        }
        String _name = step.getName();
        final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
          public void apply(final JvmField it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable x) {
                x.append("new ");
                String _qualifiedName = stepTypeRef.getQualifiedName();
                x.append(_qualifiedName);
                x.append("()");
              }
            };
            JobSchemaInferer.this._monetJvmTypesBuilder.setInitializer(it, _function);
          }
        };
        JvmField field = JobSchemaInferer.this._monetJvmTypesBuilder.toField(step, _name, stepTypeRef, _function);
        JvmAnnotationReference ann = JobSchemaInferer.this._monetJvmTypesBuilder.toAnnotation(step, annotation);
        EList<JvmAnnotationValue> _explicitValues = ann.getExplicitValues();
        String _name_1 = step.getName();
        JvmAnnotationValue _createAnnotationValue = XtendHelper.createAnnotationValue("name", _name_1, annotation, JobSchemaInferer.this.uriHelper);
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, _createAnnotationValue);
        EList<JvmAnnotationValue> _explicitValues_1 = ann.getExplicitValues();
        JvmAnnotationValue _createAnnotationValue_1 = XtendHelper.createAnnotationValue("required", Boolean.valueOf(false), annotation, JobSchemaInferer.this.uriHelper);
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_1, _createAnnotationValue_1);
        EList<JvmAnnotationReference> _annotations = field.getAnnotations();
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, ann);
        EList<JvmMember> _members = scope.getMembers();
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, field);
        EList<JvmMember> _members_1 = scope.getMembers();
        String _name_2 = step.getName();
        JvmOperation _getter = JobSchemaInferer.this._monetJvmTypesBuilder.toGetter(step, _name_2, stepTypeRef);
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _getter);
        EList<JvmMember> _members_2 = scope.getMembers();
        String _name_3 = step.getName();
        JvmOperation _setter = JobSchemaInferer.this._monetJvmTypesBuilder.toSetter(step, _name_3, stepTypeRef);
        JobSchemaInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _setter);
      }
    };
    _accept.initializeLater(_function_1);
  }
  
  public void inferEdit(final Property edit, final JvmGenericType scope) {
    Class<?> annotation = null;
    JvmTypeReference typeRef = this.inferValueType(edit);
    boolean many = this._mMLExtensions.hasProperty(edit, "is-multiple");
    if (many) {
      annotation = ElementList.class;
      JvmTypeReference _resolveArrayListType = this._typeRefCache.resolveArrayListType(edit, typeRef);
      typeRef = _resolveArrayListType;
    } else {
      annotation = Element.class;
    }
    String _name = edit.getName();
    JvmField field = this._monetJvmTypesBuilder.toField(edit, _name, typeRef);
    JvmAnnotationReference ann = this._monetJvmTypesBuilder.toAnnotation(edit, annotation);
    EList<JvmAnnotationValue> _explicitValues = ann.getExplicitValues();
    String _name_1 = edit.getName();
    JvmAnnotationValue _createAnnotationValue = XtendHelper.createAnnotationValue("name", _name_1, annotation, this.uriHelper);
    this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, _createAnnotationValue);
    EList<JvmAnnotationValue> _explicitValues_1 = ann.getExplicitValues();
    JvmAnnotationValue _createAnnotationValue_1 = XtendHelper.createAnnotationValue("required", Boolean.valueOf(false), annotation, this.uriHelper);
    this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_1, _createAnnotationValue_1);
    EList<JvmAnnotationReference> _annotations = field.getAnnotations();
    this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, ann);
    EList<JvmMember> _members = scope.getMembers();
    this._monetJvmTypesBuilder.<JvmField>operator_add(_members, field);
    EList<JvmMember> _members_1 = scope.getMembers();
    String _name_2 = edit.getName();
    JvmOperation _getter = this._monetJvmTypesBuilder.toGetter(edit, _name_2, typeRef);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _getter);
    EList<JvmMember> _members_2 = scope.getMembers();
    String _name_3 = edit.getName();
    JvmOperation _setter = this._monetJvmTypesBuilder.toSetter(edit, _name_3, typeRef);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _setter);
  }
  
  public JvmTypeReference inferValueType(final Property p) {
    Class<?> type = null;
    String _id = p.getId();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_id, "edit-check")) {
        _matched=true;
        type = CheckList.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-boolean")) {
        _matched=true;
        type = boolean.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "capture-position")) {
        _matched=true;
        type = Location.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "capture-date")) {
        _matched=true;
        type = Date.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-date")) {
        _matched=true;
        type = Date.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-memo")) {
        _matched=true;
        type = String.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-number")) {
        _matched=true;
        type = org.monet.bpi.types.Number.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-position")) {
        _matched=true;
        type = Location.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-picture")) {
        _matched=true;
        type = PictureLink.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-select")) {
        _matched=true;
        type = Term.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-text")) {
        _matched=true;
        type = String.class;
      }
    }
    if (!_matched) {
      if (Objects.equal(_id, "edit-video")) {
        _matched=true;
        type = VideoLink.class;
      }
    }
    if (!_matched) {
      type = Void.class;
    }
    return this._typeRefCache.resolve(p, type);
  }
}
