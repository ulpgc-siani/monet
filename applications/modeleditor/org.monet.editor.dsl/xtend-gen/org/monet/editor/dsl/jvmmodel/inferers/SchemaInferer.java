package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmAnnotationValue;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.access.impl.ClassURIHelper;
import org.eclipse.xtext.common.types.util.Primitives;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Schema;
import org.monet.editor.dsl.monetModelingLanguage.SchemaFeature;
import org.monet.editor.dsl.monetModelingLanguage.SchemaProperty;
import org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue;
import org.monet.editor.dsl.monetModelingLanguage.SchemaSection;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@SuppressWarnings("all")
public class SchemaInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  @Inject
  private TypeReferences references;
  
  @Inject
  private XbaseCompiler xbaseCompiler;
  
  @Inject
  private Primitives primitives;
  
  @Inject
  private ClassURIHelper uriHelper;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  public void infer(final Schema schema, final Definition definition, final IJvmDeclaredTypeAcceptor acceptor) {
    EObject b = null;
    boolean _equals = Objects.equal(schema, null);
    if (_equals) {
      b = definition;
    } else {
      b = schema;
    }
    String _inferSchemaBehaviourName = this.classNameInferer.inferSchemaBehaviourName(definition);
    final JvmGenericType schemaClass = this._monetJvmTypesBuilder.toClass(b, _inferSchemaBehaviourName);
    boolean _notEquals = (!Objects.equal(schema, null));
    if (_notEquals) {
      EList<SchemaFeature> _properties = schema.getProperties();
      Iterable<SchemaSection> _filter = Iterables.<SchemaSection>filter(_properties, SchemaSection.class);
      final Consumer<SchemaSection> _function = new Consumer<SchemaSection>() {
        public void accept(final SchemaSection ss) {
          SchemaInferer.this.inferSchemaSectionClass(ss, acceptor, schemaClass);
        }
      };
      _filter.forEach(_function);
    }
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(schemaClass);
    final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        boolean hasSuper = false;
        Definition superType = null;
        Definition _superType = definition.getSuperType();
        boolean _notEquals = (!Objects.equal(_superType, null));
        if (_notEquals) {
          Definition _superType_1 = definition.getSuperType();
          superType = _superType_1;
        } else {
          Definition _replaceSuperType = definition.getReplaceSuperType();
          boolean _notEquals_1 = (!Objects.equal(_replaceSuperType, null));
          if (_notEquals_1) {
            Definition _replaceSuperType_1 = definition.getReplaceSuperType();
            superType = _replaceSuperType_1;
          }
        }
        boolean _and = false;
        boolean _notEquals_2 = (!Objects.equal(superType, null));
        if (!_notEquals_2) {
          _and = false;
        } else {
          QualifiedName _fullyQualifiedName = SchemaInferer.this._iQualifiedNameProvider.getFullyQualifiedName(superType);
          boolean _notEquals_3 = (!Objects.equal(_fullyQualifiedName, null));
          _and = _notEquals_3;
        }
        if (_and) {
          String _inferSchemaBehaviourName = SchemaInferer.this.classNameInferer.inferSchemaBehaviourName(superType);
          JvmTypeReference _typeForName = SchemaInferer.this.references.getTypeForName(_inferSchemaBehaviourName, definition);
          JvmTypeReference parentSchemaType = SchemaInferer.this._monetJvmTypesBuilder.cloneWithProxies(_typeForName);
          boolean _notEquals_4 = (!Objects.equal(parentSchemaType, null));
          hasSuper = _notEquals_4;
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          SchemaInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, parentSchemaType);
        }
        if ((!hasSuper)) {
          EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
          JvmTypeReference _resolveSchemaType = SchemaInferer.this._typeRefCache.resolveSchemaType(definition);
          JvmTypeReference _cloneWithProxies = SchemaInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolveSchemaType);
          SchemaInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _cloneWithProxies);
        }
        boolean _notEquals_5 = (!Objects.equal(schema, null));
        if (_notEquals_5) {
          EList<SchemaFeature> _properties = schema.getProperties();
          for (final SchemaFeature p : _properties) {
            boolean _matched = false;
            if (!_matched) {
              if (p instanceof SchemaProperty) {
                _matched=true;
                SchemaInferer.this.inferProperty(((SchemaProperty)p), it);
              }
            }
          }
          EList<SchemaFeature> _properties_1 = schema.getProperties();
          SchemaInferer.this.inferMethods(_properties_1, it);
        }
        Class<Root> annType = Root.class;
        JvmAnnotationReference ann = SchemaInferer.this._monetJvmTypesBuilder.toAnnotation(definition, annType);
        JvmAnnotationValue rootAnnotationValue = XtendHelper.createAnnotationValue("name", "schema", annType, SchemaInferer.this.uriHelper);
        EList<JvmAnnotationValue> _explicitValues = ann.getExplicitValues();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, rootAnnotationValue);
        JvmAnnotationValue _createAnnotationValue = XtendHelper.createAnnotationValue("strict", Boolean.valueOf(false), annType, SchemaInferer.this.uriHelper);
        rootAnnotationValue = _createAnnotationValue;
        EList<JvmAnnotationValue> _explicitValues_1 = ann.getExplicitValues();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_1, rootAnnotationValue);
        EList<JvmAnnotationReference> _annotations = it.getAnnotations();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, ann);
      }
    };
    _accept.initializeLater(_function_1);
  }
  
  public void inferSchemaSectionClass(final SchemaSection p, final IJvmDeclaredTypeAcceptor acceptor, final JvmGenericType scope) {
    String _qualifiedName = scope.getQualifiedName();
    String _id = p.getId();
    QualifiedName sectionQualifiedName = QualifiedName.create(_qualifiedName, _id);
    String sectionName = XtendHelper.convertQualifiedNameToGenName(sectionQualifiedName);
    final JvmGenericType sectionClass = this._monetJvmTypesBuilder.toClass(p, sectionName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(sectionClass);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        Class<?> annotation = null;
        JvmTypeReference typeRef = SchemaInferer.this.references.createTypeRef(it);
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _resolveSchemaSectionType = SchemaInferer.this._typeRefCache.resolveSchemaSectionType(p);
        JvmTypeReference _cloneWithProxies = SchemaInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolveSchemaSectionType);
        SchemaInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _cloneWithProxies);
        boolean _isMany = p.isMany();
        if (_isMany) {
          annotation = ElementList.class;
          JvmTypeReference _resolveArrayListType = SchemaInferer.this._typeRefCache.resolveArrayListType(p, typeRef);
          typeRef = _resolveArrayListType;
        } else {
          annotation = Element.class;
        }
        final JvmTypeReference sectionTypeRef = typeRef;
        EList<SchemaFeature> _features = p.getFeatures();
        for (final SchemaFeature f : _features) {
          boolean _matched = false;
          if (!_matched) {
            if (f instanceof SchemaProperty) {
              _matched=true;
              SchemaInferer.this.inferProperty(((SchemaProperty)f), it);
            }
          }
        }
        EList<SchemaFeature> _features_1 = p.getFeatures();
        SchemaInferer.this.inferMethods(_features_1, it);
        Class<Root> annType = Root.class;
        JvmAnnotationReference ann = SchemaInferer.this._monetJvmTypesBuilder.toAnnotation(p, annType);
        String _id = p.getId();
        JvmAnnotationValue rootAnnotationValue = XtendHelper.createAnnotationValue("name", _id, annType, SchemaInferer.this.uriHelper);
        EList<JvmAnnotationValue> _explicitValues = ann.getExplicitValues();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, rootAnnotationValue);
        JvmAnnotationValue _createAnnotationValue = XtendHelper.createAnnotationValue("strict", Boolean.valueOf(false), annType, SchemaInferer.this.uriHelper);
        rootAnnotationValue = _createAnnotationValue;
        EList<JvmAnnotationValue> _explicitValues_1 = ann.getExplicitValues();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_1, rootAnnotationValue);
        EList<JvmAnnotationReference> _annotations = it.getAnnotations();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, ann);
        String _id_1 = p.getId();
        final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
          public void apply(final JvmField it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable x) {
                x.append("new ");
                String _qualifiedName = sectionTypeRef.getQualifiedName();
                x.append(_qualifiedName);
                x.append("()");
              }
            };
            SchemaInferer.this._monetJvmTypesBuilder.setInitializer(it, _function);
          }
        };
        JvmField field = SchemaInferer.this._monetJvmTypesBuilder.toField(p, _id_1, sectionTypeRef, _function);
        JvmAnnotationReference _annotation = SchemaInferer.this._monetJvmTypesBuilder.toAnnotation(p, annotation);
        ann = _annotation;
        EList<JvmAnnotationValue> _explicitValues_2 = ann.getExplicitValues();
        String _id_2 = p.getId();
        JvmAnnotationValue _createAnnotationValue_1 = XtendHelper.createAnnotationValue("name", _id_2, annotation, SchemaInferer.this.uriHelper);
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_2, _createAnnotationValue_1);
        EList<JvmAnnotationValue> _explicitValues_3 = ann.getExplicitValues();
        JvmAnnotationValue _createAnnotationValue_2 = XtendHelper.createAnnotationValue("required", Boolean.valueOf(false), annotation, SchemaInferer.this.uriHelper);
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_3, _createAnnotationValue_2);
        EList<JvmAnnotationReference> _annotations_1 = field.getAnnotations();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations_1, ann);
        EList<JvmMember> _members = scope.getMembers();
        SchemaInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, field);
        EList<JvmMember> _members_1 = scope.getMembers();
        String _id_3 = p.getId();
        JvmOperation _getter = SchemaInferer.this._monetJvmTypesBuilder.toGetter(p, _id_3, sectionTypeRef);
        SchemaInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _getter);
        EList<JvmMember> _members_2 = scope.getMembers();
        String _id_4 = p.getId();
        JvmOperation _setter = SchemaInferer.this._monetJvmTypesBuilder.toSetter(p, _id_4, sectionTypeRef);
        SchemaInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _setter);
      }
    };
    _accept.initializeLater(_function);
    EList<SchemaFeature> _features = p.getFeatures();
    Iterable<SchemaSection> _filter = Iterables.<SchemaSection>filter(_features, SchemaSection.class);
    final Consumer<SchemaSection> _function_1 = new Consumer<SchemaSection>() {
      public void accept(final SchemaSection ss) {
        SchemaInferer.this.inferSchemaSectionClass(ss, acceptor, sectionClass);
      }
    };
    _filter.forEach(_function_1);
  }
  
  public void inferMethods(final EList<SchemaFeature> properties, final JvmGenericType scope) {
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
        appender.append("HashMap<String, Object> result = new HashMap<>();\n");
        for (final SchemaFeature p : properties) {
          String _id = p.getId();
          String _plus = ("result.put(\"" + _id);
          String _plus_1 = (_plus + "\", this.");
          String _id_1 = p.getId();
          String _plus_2 = (_plus_1 + _id_1);
          String _plus_3 = (_plus_2 + ");\n");
          appender.append(_plus_3);
        }
        appender.append("return result;\n");
      }
    };
    this._monetJvmTypesBuilder.setBody(getAllMethod, _function_1);
    EClass _eClass_1 = scope.eClass();
    JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(scope);
    final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        it.setVisibility(JvmVisibility.PUBLIC);
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmTypeReference _resolveStringType = SchemaInferer.this._typeRefCache.resolveStringType(scope);
        JvmFormalParameter _parameter = SchemaInferer.this._monetJvmTypesBuilder.toParameter(scope, "key", _resolveStringType);
        SchemaInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
        JvmTypeReference _resolveObjectType = SchemaInferer.this._typeRefCache.resolveObjectType(scope);
        JvmFormalParameter _parameter_1 = SchemaInferer.this._monetJvmTypesBuilder.toParameter(scope, "value", _resolveObjectType);
        SchemaInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
      }
    };
    JvmOperation setMethod = this._monetJvmTypesBuilder.toMethod(_eClass_1, "set", _resolveVoidType, _function_2);
    EList<JvmMember> _members_1 = scope.getMembers();
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, setMethod);
    final Procedure1<ITreeAppendable> _function_3 = new Procedure1<ITreeAppendable>() {
      public void apply(final ITreeAppendable appender) {
        for (final SchemaFeature p : properties) {
          {
            String typeFormat = "%s";
            boolean _isMany = p.isMany();
            if (_isMany) {
              typeFormat = "java.util.ArrayList<%s>";
            }
            String type = "";
            boolean _matched = false;
            if (!_matched) {
              if (p instanceof SchemaProperty) {
                _matched=true;
                SchemaPropertyOfValue _source = ((SchemaProperty)p).getSource();
                JvmParameterizedTypeReference _type = _source.getType();
                String _qualifiedName = _type.getQualifiedName();
                String _format = String.format(typeFormat, _qualifiedName);
                type = _format;
              }
            }
            if (!_matched) {
              {
                String _qualifiedName = scope.getQualifiedName();
                String _id = p.getId();
                QualifiedName sectionQualifiedName = QualifiedName.create(_qualifiedName, _id);
                String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(sectionQualifiedName);
                String _format = String.format(typeFormat, _convertQualifiedNameToGenName);
                type = _format;
              }
            }
            String _id = p.getId();
            String _plus = ("if (key.equalsIgnoreCase(\"" + _id);
            String _plus_1 = (_plus + "\")) { this.");
            String _id_1 = p.getId();
            String _plus_2 = (_plus_1 + _id_1);
            String _plus_3 = (_plus_2 + " = (");
            String _plus_4 = (_plus_3 + type);
            String _plus_5 = (_plus_4 + ")value; return; }\n");
            appender.append(_plus_5);
          }
        }
      }
    };
    this._monetJvmTypesBuilder.setBody(setMethod, _function_3);
  }
  
  public void inferProperty(final SchemaProperty p, final JvmGenericType scope) {
    Class<?> annotation = null;
    JvmTypeReference typeRef = null;
    SchemaPropertyOfValue source = p.getSource();
    XExpression bodyExp = null;
    boolean many = p.isMany();
    boolean _matched = false;
    if (!_matched) {
      if (source instanceof SchemaPropertyOfValue) {
        _matched=true;
        JvmParameterizedTypeReference _type = source.getType();
        typeRef = _type;
        XExpression _body = source.getBody();
        bodyExp = _body;
      }
    }
    if (many) {
      annotation = ElementList.class;
      JvmTypeReference _resolveArrayListType = this._typeRefCache.resolveArrayListType(p, typeRef);
      typeRef = _resolveArrayListType;
    } else {
      annotation = Element.class;
    }
    String _id = p.getId();
    JvmField field = this._monetJvmTypesBuilder.toField(p, _id, typeRef);
    JvmAnnotationReference ann = this._monetJvmTypesBuilder.toAnnotation(p, annotation);
    EList<JvmAnnotationValue> _explicitValues = ann.getExplicitValues();
    String _id_1 = p.getId();
    JvmAnnotationValue _createAnnotationValue = XtendHelper.createAnnotationValue("name", _id_1, annotation, this.uriHelper);
    this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, _createAnnotationValue);
    EList<JvmAnnotationValue> _explicitValues_1 = ann.getExplicitValues();
    JvmAnnotationValue _createAnnotationValue_1 = XtendHelper.createAnnotationValue("required", Boolean.valueOf(false), annotation, this.uriHelper);
    this._monetJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues_1, _createAnnotationValue_1);
    EList<JvmAnnotationReference> _annotations = field.getAnnotations();
    this._monetJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, ann);
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(bodyExp, null));
    if (!_notEquals) {
      _and = false;
    } else {
      _and = (!many);
    }
    if (_and) {
      final XExpression bodyExpF = bodyExp;
      final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
        public void apply(final ITreeAppendable x) {
          XBlockExpression body = ((XBlockExpression) bodyExpF);
          EList<XExpression> _expressions = body.getExpressions();
          XExpression _get = _expressions.get(0);
          if ((_get instanceof XConstructorCall)) {
            SchemaInferer.this.xbaseCompiler.toJavaStatement(bodyExpF, x, false);
          } else {
            SchemaInferer.this.xbaseCompiler.toJavaExpression(bodyExpF, x);
          }
        }
      };
      this._monetJvmTypesBuilder.setInitializer(field, _function);
    } else {
      if (many) {
        final JvmTypeReference typeRefF = typeRef;
        final Procedure1<ITreeAppendable> _function_1 = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable x) {
            x.append("new ");
            JvmTypeReference _asWrapperTypeIfPrimitive = SchemaInferer.this.primitives.asWrapperTypeIfPrimitive(typeRefF);
            String _qualifiedName = _asWrapperTypeIfPrimitive.getQualifiedName();
            x.append(_qualifiedName);
            x.append("()");
          }
        };
        this._monetJvmTypesBuilder.setInitializer(field, _function_1);
      }
    }
    EList<JvmMember> _members = scope.getMembers();
    this._monetJvmTypesBuilder.<JvmField>operator_add(_members, field);
    EList<JvmMember> _members_1 = scope.getMembers();
    String _id_2 = p.getId();
    JvmOperation _getter = this._monetJvmTypesBuilder.toGetter(p, _id_2, typeRef);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _getter);
    EList<JvmMember> _members_2 = scope.getMembers();
    String _id_3 = p.getId();
    JvmOperation _setter = this._monetJvmTypesBuilder.toSetter(p, _id_3, typeRef);
    this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, _setter);
  }
}
