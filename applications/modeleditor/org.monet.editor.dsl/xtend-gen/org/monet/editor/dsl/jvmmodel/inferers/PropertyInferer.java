package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
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
import org.eclipse.xtext.common.types.JvmUnknownTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.MMLExtensions;
import org.monet.editor.dsl.jvmmodel.inferers.AttributeInferer;
import org.monet.editor.dsl.jvmmodel.inferers.ClassNameInferer;
import org.monet.editor.dsl.jvmmodel.inferers.ModelInferer;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.PropertyFeature;

@SuppressWarnings("all")
public class PropertyInferer extends ModelInferer {
  public static class Index {
    private int value;
    
    public int Index() {
      return this.value = 0;
    }
    
    public void increment() {
      this.value = (this.value + 1);
    }
    
    public int value() {
      return this.value;
    }
    
    public void setValue(final int newValue) {
      this.value = newValue;
    }
  }
  
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
  private AttributeInferer attributeInferer;
  
  @Inject
  private ClassNameInferer classNameInferer;
  
  public void infer(final EObject e, final Property p, final ITreeAppendable x, final Item metacontainerItem, final String context, final PropertyInferer.Index index, final boolean prelinkingPhase, final boolean separateWithInitMethods, final boolean checkInlineProperties) {
    String _id = p.getId();
    final Item itemProperty = metacontainerItem.getChild(_id);
    boolean _equals = Objects.equal(itemProperty, null);
    if (_equals) {
      return;
    }
    String value = "";
    QualifiedName fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(p);
    boolean _or = false;
    boolean _equals_1 = Objects.equal(fullyQualifiedName, null);
    if (_equals_1) {
      _or = true;
    } else {
      String _valueTypeQualifiedName = itemProperty.getValueTypeQualifiedName();
      boolean _equals_2 = Objects.equal(_valueTypeQualifiedName, null);
      _or = _equals_2;
    }
    if (_or) {
      String typeName = null;
      String _valueTypeQualifiedName_1 = itemProperty.getValueTypeQualifiedName();
      boolean _notEquals = (!Objects.equal(_valueTypeQualifiedName_1, null));
      if (_notEquals) {
        String _valueTypeQualifiedName_2 = itemProperty.getValueTypeQualifiedName();
        typeName = _valueTypeQualifiedName_2;
      } else {
        String _valueTypeQualifiedName_3 = metacontainerItem.getValueTypeQualifiedName();
        boolean _notEquals_1 = (!Objects.equal(_valueTypeQualifiedName_3, null));
        if (_notEquals_1) {
          String _valueTypeQualifiedName_4 = metacontainerItem.getValueTypeQualifiedName();
          String _plus = (_valueTypeQualifiedName_4 + ".");
          String _name = itemProperty.getName();
          String _javaIdentifier = JavaHelper.toJavaIdentifier(_name);
          String _plus_1 = (_plus + _javaIdentifier);
          typeName = _plus_1;
        } else {
          String _fullyQualifiedName = itemProperty.getFullyQualifiedName();
          typeName = _fullyQualifiedName;
        }
      }
      String _name_1 = itemProperty.getName();
      String _attributeJavaIdentifier = JavaHelper.toAttributeJavaIdentifier(_name_1);
      int _value = index.value();
      String _valueOf = String.valueOf(_value);
      String varName = (_attributeJavaIdentifier + _valueOf);
      value = varName;
      boolean _or_1 = false;
      boolean _and = false;
      String _token = itemProperty.getToken();
      boolean _startsWith = _token.startsWith("is-");
      if (!_startsWith) {
        _and = false;
      } else {
        Collection<Item> _items = itemProperty.getItems();
        int _size = _items.size();
        boolean _equals_3 = (_size == 0);
        _and = _equals_3;
      }
      if (_and) {
        _or_1 = true;
      } else {
        String _token_1 = itemProperty.getToken();
        boolean _startsWith_1 = _token_1.startsWith("allow-");
        _or_1 = _startsWith_1;
      }
      boolean _not = (!_or_1);
      if (_not) {
        x.append(typeName);
        x.append(" ");
        x.append(varName);
        x.append(" = new ");
        x.append(typeName);
        x.append("();\n");
        EList<PropertyFeature> _features = p.getFeatures();
        for (final PropertyFeature childAttr : _features) {
          boolean _matched = false;
          if (!_matched) {
            if (childAttr instanceof Attribute) {
              _matched=true;
              this.attributeInferer.infer(((Attribute)childAttr), x, itemProperty, varName, true, prelinkingPhase);
            }
          }
        }
      }
      Code _code = p.getCode();
      boolean _notEquals_2 = (!Objects.equal(_code, null));
      if (_notEquals_2) {
        x.append(value);
        x.append(".setCode(\"");
        Code _code_1 = p.getCode();
        String _value_1 = _code_1.getValue();
        x.append(_value_1);
        x.append("\");\n");
      }
      String _name_2 = p.getName();
      boolean _notEquals_3 = (!Objects.equal(_name_2, null));
      if (_notEquals_3) {
        x.append(value);
        x.append(".setName(\"");
        String _name_3 = p.getName();
        x.append(_name_3);
        x.append("\");\n");
      }
      EList<PropertyFeature> _features_1 = p.getFeatures();
      for (final PropertyFeature childProp : _features_1) {
        boolean _matched_1 = false;
        if (!_matched_1) {
          if (childProp instanceof Property) {
            _matched_1=true;
            String _id_1 = ((Property)childProp).getId();
            final Item childItemProperty = itemProperty.getChild(_id_1);
            boolean _and_1 = false;
            if (!separateWithInitMethods) {
              _and_1 = false;
            } else {
              boolean _renderWithInitMethod = this.renderWithInitMethod(((Property)childProp), childItemProperty, checkInlineProperties);
              _and_1 = _renderWithInitMethod;
            }
            if (_and_1) {
              String prefix = "";
              String code = childItemProperty.getName();
              Code _code_2 = ((Property)childProp).getCode();
              boolean _notEquals_4 = (!Objects.equal(_code_2, null));
              if (_notEquals_4) {
                Code _code_3 = ((Property)childProp).getCode();
                String _value_2 = _code_3.getValue();
                code = _value_2;
              }
              boolean _equals_4 = value.equals("this");
              boolean _not_1 = (!_equals_4);
              if (_not_1) {
                String _javaIdentifier_1 = JavaHelper.toJavaIdentifier(value);
                prefix = _javaIdentifier_1;
              }
              int _value_3 = index.value();
              String methodName = ("init" + Integer.valueOf(_value_3));
              x.append((((methodName + "(") + varName) + ");\n"));
              int _value_4 = index.value();
              int _descendantsCount = this.propertyInferer.getDescendantsCount(e, ((Property)childProp), itemProperty, checkInlineProperties);
              int _plus_2 = (_value_4 + _descendantsCount);
              index.setValue(_plus_2);
              index.increment();
            } else {
              this.infer(e, ((Property)childProp), x, itemProperty, value, index, prelinkingPhase, separateWithInitMethods, checkInlineProperties);
              final QualifiedName fullyChildQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(childProp);
              boolean _and_2 = false;
              String _valueTypeQualifiedName_5 = childItemProperty.getValueTypeQualifiedName();
              boolean _notEquals_5 = (!Objects.equal(_valueTypeQualifiedName_5, null));
              if (!_notEquals_5) {
                _and_2 = false;
              } else {
                boolean _notEquals_6 = (!Objects.equal(fullyChildQualifiedName, null));
                _and_2 = _notEquals_6;
              }
              boolean _not_2 = (!_and_2);
              if (_not_2) {
                index.increment();
              }
            }
          }
        }
      }
    } else {
      String _lastSegment = fullyQualifiedName.getLastSegment();
      String _javaIdentifier_1 = JavaHelper.toJavaIdentifier(_lastSegment);
      value = _javaIdentifier_1;
    }
    x.append(context);
    boolean _and_1 = false;
    boolean _isMultiple = itemProperty.isMultiple();
    if (!_isMultiple) {
      _and_1 = false;
    } else {
      boolean _or_2 = false;
      String _valueTypeQualifiedName_5 = itemProperty.getValueTypeQualifiedName();
      boolean _notEquals_4 = (!Objects.equal(_valueTypeQualifiedName_5, null));
      if (_notEquals_4) {
        _or_2 = true;
      } else {
        boolean _hasName = itemProperty.hasName();
        _or_2 = _hasName;
      }
      _and_1 = _or_2;
    }
    if (_and_1) {
      x.append(".add");
    } else {
      boolean _isMultiple_1 = itemProperty.isMultiple();
      if (_isMultiple_1) {
        x.append(".get");
      } else {
        x.append(".set");
      }
    }
    boolean _or_3 = false;
    String _valueTypeQualifiedName_6 = itemProperty.getValueTypeQualifiedName();
    boolean _notEquals_5 = (!Objects.equal(_valueTypeQualifiedName_6, null));
    if (_notEquals_5) {
      _or_3 = true;
    } else {
      boolean _hasName_1 = itemProperty.hasName();
      _or_3 = _hasName_1;
    }
    if (_or_3) {
      String propertyName = null;
      String _valueTypeQualifiedName_7 = itemProperty.getValueTypeQualifiedName();
      boolean _notEquals_6 = (!Objects.equal(_valueTypeQualifiedName_7, null));
      if (_notEquals_6) {
        String _valueTypeQualifiedName_8 = itemProperty.getValueTypeQualifiedName();
        int dollarIndex = _valueTypeQualifiedName_8.lastIndexOf(".");
        if ((dollarIndex < 0)) {
          dollarIndex = 0;
        }
        String _valueTypeQualifiedName_9 = itemProperty.getValueTypeQualifiedName();
        String _substring = _valueTypeQualifiedName_9.substring((dollarIndex + 1));
        propertyName = _substring;
      } else {
        String _token_2 = itemProperty.getToken();
        propertyName = _token_2;
      }
      String _javaIdentifier_2 = JavaHelper.toJavaIdentifier(propertyName);
      x.append(_javaIdentifier_2);
      x.append("(");
      x.append(value);
      x.append(")");
    } else {
      boolean _isMultiple_2 = itemProperty.isMultiple();
      if (_isMultiple_2) {
        String _id_1 = p.getId();
        String _javaIdentifier_3 = JavaHelper.toJavaIdentifier(_id_1);
        x.append(_javaIdentifier_3);
        x.append("List().add(");
        x.append(value);
        x.append(")");
      } else {
        String _id_2 = p.getId();
        String _javaIdentifier_4 = JavaHelper.toJavaIdentifier(_id_2);
        x.append(_javaIdentifier_4);
        x.append("(");
        boolean _or_4 = false;
        boolean _and_2 = false;
        String _token_3 = itemProperty.getToken();
        boolean _startsWith_2 = _token_3.startsWith("is-");
        if (!_startsWith_2) {
          _and_2 = false;
        } else {
          Collection<Item> _items_1 = itemProperty.getItems();
          int _size_1 = _items_1.size();
          boolean _equals_4 = (_size_1 == 0);
          _and_2 = _equals_4;
        }
        if (_and_2) {
          _or_4 = true;
        } else {
          String _token_4 = itemProperty.getToken();
          boolean _startsWith_3 = _token_4.startsWith("allow-");
          _or_4 = _startsWith_3;
        }
        if (_or_4) {
          x.append("true");
        } else {
          x.append(value);
        }
        x.append(")");
      }
    }
    x.append(";\n");
  }
  
  public int getDescendantsCount(final EObject e, final Property p, final Item metacontainerItem, final boolean checkInlineProperties) {
    String _id = p.getId();
    final Item itemProperty = metacontainerItem.getChild(_id);
    int result = 0;
    boolean _equals = Objects.equal(itemProperty, null);
    if (_equals) {
      return result;
    }
    EList<PropertyFeature> _features = p.getFeatures();
    for (final PropertyFeature childProp : _features) {
      boolean _matched = false;
      if (!_matched) {
        if (childProp instanceof Property) {
          _matched=true;
          String _id_1 = ((Property)childProp).getId();
          Item childItemProperty = itemProperty.getChild(_id_1);
          boolean _renderWithInitMethod = this.renderWithInitMethod(((Property)childProp), childItemProperty, checkInlineProperties);
          if (_renderWithInitMethod) {
            result = (result + 1);
            int _descendantsCount = this.getDescendantsCount(e, ((Property)childProp), itemProperty, checkInlineProperties);
            int _plus = (result + _descendantsCount);
            result = _plus;
          }
        }
      }
    }
    return result;
  }
  
  public boolean renderWithInitMethod(final Property p, final Item itemProperty, final boolean checkInlineProperties) {
    final QualifiedName fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(p);
    boolean _and = false;
    boolean _and_1 = false;
    if (!checkInlineProperties) {
      _and_1 = false;
    } else {
      String _valueTypeQualifiedName = itemProperty.getValueTypeQualifiedName();
      boolean _notEquals = (!Objects.equal(_valueTypeQualifiedName, null));
      _and_1 = _notEquals;
    }
    if (!_and_1) {
      _and = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(fullyQualifiedName, null));
      _and = _notEquals_1;
    }
    if (_and) {
      return false;
    }
    boolean _and_2 = false;
    if (!checkInlineProperties) {
      _and_2 = false;
    } else {
      String _valueTypeQualifiedName_1 = itemProperty.getValueTypeQualifiedName();
      boolean _equals = Objects.equal(_valueTypeQualifiedName_1, null);
      _and_2 = _equals;
    }
    if (_and_2) {
      return true;
    }
    boolean _and_3 = false;
    boolean _and_4 = false;
    if (!checkInlineProperties) {
      _and_4 = false;
    } else {
      String _valueTypeQualifiedName_2 = itemProperty.getValueTypeQualifiedName();
      boolean _notEquals_2 = (!Objects.equal(_valueTypeQualifiedName_2, null));
      _and_4 = _notEquals_2;
    }
    if (!_and_4) {
      _and_3 = false;
    } else {
      boolean _hasName = itemProperty.hasName();
      boolean _not = (!_hasName);
      _and_3 = _not;
    }
    if (_and_3) {
      return true;
    }
    if (checkInlineProperties) {
      boolean _isPlaceProperty = this.isPlaceProperty(itemProperty);
      if (_isPlaceProperty) {
        return true;
      }
      return false;
    }
    return true;
  }
  
  public boolean isPlaceProperty(final Item itemProperty) {
    Item parent = itemProperty.getParent();
    boolean _equals = Objects.equal(parent, null);
    if (_equals) {
      return false;
    }
    String _token = parent.getToken();
    boolean _equals_1 = Objects.equal(_token, "place");
    if (_equals_1) {
      return true;
    }
    return this.isPlaceProperty(parent);
  }
  
  public Iterable<JvmOperation> inferMethods(final EObject e, final Property p, final Item metacontainerItem, final String context, final PropertyInferer.Index index, final boolean prelinkingPhase) {
    ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();
    String _id = p.getId();
    final Item itemProperty = metacontainerItem.getChild(_id);
    boolean _equals = Objects.equal(itemProperty, null);
    if (_equals) {
      return methods;
    }
    boolean _renderWithInitMethod = this.renderWithInitMethod(p, itemProperty, true);
    boolean _not = (!_renderWithInitMethod);
    if (_not) {
      return methods;
    }
    QualifiedName fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(p);
    boolean _or = false;
    boolean _equals_1 = Objects.equal(fullyQualifiedName, null);
    if (_equals_1) {
      _or = true;
    } else {
      String _valueTypeQualifiedName = itemProperty.getValueTypeQualifiedName();
      boolean _equals_2 = Objects.equal(_valueTypeQualifiedName, null);
      _or = _equals_2;
    }
    if (_or) {
      String prefix = "";
      String code = itemProperty.getName();
      Code _code = p.getCode();
      boolean _notEquals = (!Objects.equal(_code, null));
      if (_notEquals) {
        Code _code_1 = p.getCode();
        String _value = _code_1.getValue();
        code = _value;
      }
      boolean _equals_3 = context.equals("this");
      boolean _not_1 = (!_equals_3);
      if (_not_1) {
        String _javaIdentifier = JavaHelper.toJavaIdentifier(context);
        prefix = _javaIdentifier;
      }
      int _value_1 = index.value();
      String methodName = ("init" + Integer.valueOf(_value_1));
      final PropertyInferer.Index currentIndex = new PropertyInferer.Index();
      int _value_2 = index.value();
      int _plus = (_value_2 + 1);
      currentIndex.setValue(_plus);
      String _name = itemProperty.getName();
      String _attributeJavaIdentifier = JavaHelper.toAttributeJavaIdentifier(_name);
      int _value_3 = currentIndex.value();
      String _valueOf = String.valueOf(_value_3);
      final String valueVal = (_attributeJavaIdentifier + _valueOf);
      JvmTypeReference _resolveVoidType = this._typeRefCache.resolveVoidType(e);
      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
        public void apply(final JvmOperation it) {
          it.setVisibility(JvmVisibility.PRIVATE);
          boolean _equals = context.equals("this");
          boolean _not = (!_equals);
          if (_not) {
            String typeName = null;
            String _valueTypeQualifiedName = metacontainerItem.getValueTypeQualifiedName();
            boolean _notEquals = (!Objects.equal(_valueTypeQualifiedName, null));
            if (_notEquals) {
              String _valueTypeQualifiedName_1 = metacontainerItem.getValueTypeQualifiedName();
              typeName = _valueTypeQualifiedName_1;
            } else {
              String _fullyQualifiedName = metacontainerItem.getFullyQualifiedName();
              typeName = _fullyQualifiedName;
            }
            JvmTypeReference _newTypeRef = PropertyInferer.this._monetJvmTypesBuilder.newTypeRef(e, typeName);
            JvmFormalParameter parameter = PropertyInferer.this._monetJvmTypesBuilder.toParameter(e, context, _newTypeRef);
            EList<JvmFormalParameter> _parameters = it.getParameters();
            PropertyInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, parameter);
          }
          final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
            public void apply(final ITreeAppendable ap) {
              PropertyInferer.this.infer(it, p, ap, metacontainerItem, context, currentIndex, prelinkingPhase, true, true);
            }
          };
          PropertyInferer.this._monetJvmTypesBuilder.setBody(it, _function);
        }
      };
      JvmOperation _method = this._monetJvmTypesBuilder.toMethod(e, methodName, _resolveVoidType, _function);
      methods.add(_method);
      index.increment();
      EList<PropertyFeature> _features = p.getFeatures();
      for (final PropertyFeature childProp : _features) {
        boolean _matched = false;
        if (!_matched) {
          if (childProp instanceof Property) {
            _matched=true;
            Iterable<JvmOperation> _inferMethods = this.inferMethods(e, ((Property)childProp), itemProperty, valueVal, index, prelinkingPhase);
            Iterables.<JvmOperation>addAll(methods, _inferMethods);
          }
        }
      }
    }
    return methods;
  }
  
  /**
   * def Iterable<JvmOperation> inferMethods(EObject e, Property p, ITreeAppendable x, Item metacontainerItem, String context, Integer index, boolean prelinkingPhase) {
   * var ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();
   * 
   * for (childProp : p.features) {
   * switch childProp {
   * Property: {
   * var methodName = "init" + JavaHelper::toJavaIdentifier(p.name) + index +
   * JavaHelper::toJavaIdentifier(childProp.name) + "();\n";
   * x.append(methodName);
   * 
   * methods += e.toMethod(methodName, e.resolveVoidType) [
   * visibility = JvmVisibility::PRIVATE;
   * body = [ ap |
   * this.infer(childProp, ap, metacontainerItem, "this", 0, prelinkingPhase);
   * ];
   * ];
   * }
   * }
   * }
   * 
   * return methods;
   * }
   */
  public Iterable<Property> inferClass(final Definition d, final Property p, final Item parentItem, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    String _id = p.getId();
    final Item propertyItem = parentItem.getChild(_id);
    final ArrayList<Property> childPropertiesWithClasses = new ArrayList<Property>();
    boolean _equals = Objects.equal(propertyItem, null);
    if (_equals) {
      return new ArrayList<Property>();
    }
    final QualifiedName fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(p);
    Iterable<Property> _properties = this._mMLExtensions.getProperties(p);
    final Consumer<Property> _function = new Consumer<Property>() {
      public void accept(final Property px) {
        Iterable<Property> _inferClass = PropertyInferer.this.inferClass(d, px, propertyItem, acceptor, prelinkingPhase);
        Iterables.<Property>addAll(childPropertiesWithClasses, _inferClass);
      }
    };
    _properties.forEach(_function);
    boolean _and = false;
    String _valueTypeQualifiedName = propertyItem.getValueTypeQualifiedName();
    boolean _notEquals = (!Objects.equal(_valueTypeQualifiedName, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(fullyQualifiedName, null));
      _and = _notEquals_1;
    }
    if (_and) {
      final String name = fullyQualifiedName.getLastSegment();
      final String propertyClazzName = this.classNameInferer.inferPropertyName(p);
      JvmGenericType _class = this._monetJvmTypesBuilder.toClass(p, propertyClazzName);
      IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
      final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
        public void apply(final JvmGenericType it) {
          EList<JvmMember> _members = it.getMembers();
          String _simpleName = it.getSimpleName();
          JvmConstructor _inferDefinitionConstructorMethod = PropertyInferer.this.inferDefinitionConstructorMethod(p, _simpleName, propertyItem, prelinkingPhase, true);
          PropertyInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _inferDefinitionConstructorMethod);
          EList<JvmMember> _members_1 = it.getMembers();
          String _simpleName_1 = it.getSimpleName();
          Iterable<JvmOperation> _inferDefinitionConstructorInitMethods = PropertyInferer.this.inferDefinitionConstructorInitMethods(p, _simpleName_1, propertyItem, prelinkingPhase);
          PropertyInferer.this._monetJvmTypesBuilder.<JvmMember>operator_add(_members_1, _inferDefinitionConstructorInitMethods);
          JvmTypeReference superType = null;
          if ((!prelinkingPhase)) {
            boolean _and = false;
            Definition _superType = d.getSuperType();
            boolean _notEquals = (!Objects.equal(_superType, null));
            if (!_notEquals) {
              _and = false;
            } else {
              Definition _superType_1 = d.getSuperType();
              QualifiedName _fullyQualifiedName = PropertyInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
              boolean _notEquals_1 = (!Objects.equal(_fullyQualifiedName, null));
              _and = _notEquals_1;
            }
            if (_and) {
              Definition _superType_2 = d.getSuperType();
              String _id = p.getId();
              final Iterable<Property> parentProperties = PropertyInferer.this._mMLExtensions.getProperties(_superType_2, _id);
              boolean found = false;
              for (final Property parentProperty : parentProperties) {
                String _name = parentProperty.getName();
                String _name_1 = p.getName();
                boolean _equals = Objects.equal(_name, _name_1);
                if (_equals) {
                  found = true;
                }
              }
              if (found) {
                QualifiedName _fullyQualifiedName_1 = PropertyInferer.this._iQualifiedNameProvider.getFullyQualifiedName(p);
                QualifiedName _fullyQualifiedName_2 = PropertyInferer.this._iQualifiedNameProvider.getFullyQualifiedName(d);
                int _segmentCount = _fullyQualifiedName_2.getSegmentCount();
                QualifiedName relativeQN = _fullyQualifiedName_1.skipFirst(_segmentCount);
                Definition _superType_3 = d.getSuperType();
                QualifiedName _fullyQualifiedName_3 = PropertyInferer.this._iQualifiedNameProvider.getFullyQualifiedName(_superType_3);
                QualifiedName _append = _fullyQualifiedName_3.append(relativeQN);
                String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(_append);
                String _string = _convertQualifiedNameToGenName.toString();
                String superTypeName = (_string + "Property");
                JvmTypeReference _newTypeRef = PropertyInferer.this._monetJvmTypesBuilder.newTypeRef(p, superTypeName);
                superType = _newTypeRef;
              }
            }
          }
          boolean _equals_1 = Objects.equal(superType, null);
          if (_equals_1) {
            String _valueTypeQualifiedName = propertyItem.getValueTypeQualifiedName();
            JvmTypeReference _newTypeRef_1 = PropertyInferer.this._monetJvmTypesBuilder.newTypeRef(p, _valueTypeQualifiedName);
            superType = _newTypeRef_1;
          }
          boolean _notEquals_2 = (!Objects.equal(superType, null));
          if (_notEquals_2) {
            EList<JvmTypeReference> _superTypes = it.getSuperTypes();
            PropertyInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, superType);
          }
          String _id_1 = p.getId();
          boolean _equals_2 = Objects.equal(_id_1, "field-composite");
          if (_equals_2) {
            JvmTypeReference _resolveClassType = PropertyInferer.this._typeRefCache.resolveClassType(p);
            final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
              public void apply(final JvmOperation it) {
                final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                  public void apply(final ITreeAppendable ap) {
                    ap.append("return ");
                    String _convertQualifiedNameToGenName = XtendHelper.convertQualifiedNameToGenName(fullyQualifiedName);
                    ap.append(_convertQualifiedNameToGenName);
                    ap.append(".class;");
                  }
                };
                PropertyInferer.this._monetJvmTypesBuilder.setBody(it, _function);
              }
            };
            JvmOperation behaviourGetter = PropertyInferer.this._monetJvmTypesBuilder.toMethod(p, "getBehaviourClass", _resolveClassType, _function);
            EList<JvmMember> _members_2 = it.getMembers();
            PropertyInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, behaviourGetter);
            EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
            JvmTypeReference _resolveHasBehaviourType = PropertyInferer.this._typeRefCache.resolveHasBehaviourType(p);
            PropertyInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _resolveHasBehaviourType);
          }
          String _name_2 = p.getName();
          boolean _notEquals_3 = (!Objects.equal(_name_2, null));
          if (_notEquals_3) {
            EList<JvmMember> _members_3 = it.getMembers();
            JvmOperation _inferGetName = PropertyInferer.this.inferGetName(p, name);
            PropertyInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_3, _inferGetName);
          }
          Iterable<Property> _properties = PropertyInferer.this._mMLExtensions.getProperties(p);
          Iterables.<Property>addAll(childPropertiesWithClasses, _properties);
          final Consumer<Property> _function_1 = new Consumer<Property>() {
            public void accept(final Property px) {
              String _name = px.getName();
              boolean _notEquals = (!Objects.equal(_name, null));
              if (_notEquals) {
                final String fieldTypeName = PropertyInferer.this.classNameInferer.inferPropertyName(px);
                JvmTypeReference fieldType = PropertyInferer.this._monetJvmTypesBuilder.newTypeRef(px, fieldTypeName);
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
                  JvmField field = PropertyInferer.this._monetJvmTypesBuilder.toField(px, _javaIdentifier, fieldType);
                  field.setFinal(true);
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable ap) {
                      ap.append("new ");
                      ap.append(fieldTypeName);
                      ap.append("()");
                    }
                  };
                  PropertyInferer.this._monetJvmTypesBuilder.setInitializer(field, _function);
                  EList<JvmMember> _members = it.getMembers();
                  PropertyInferer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, field);
                }
              }
            }
          };
          childPropertiesWithClasses.forEach(_function_1);
        }
      };
      _accept.initializeLater(_function_1);
      return new ArrayList<Property>();
    } else {
      Iterable<Property> _properties_1 = this._mMLExtensions.getProperties(p);
      final Function1<Property, Boolean> _function_2 = new Function1<Property, Boolean>() {
        public Boolean apply(final Property px) {
          String _id = px.getId();
          final Item pxItem = propertyItem.getChild(_id);
          final QualifiedName pxfullyQualifiedName = PropertyInferer.this._iQualifiedNameProvider.getFullyQualifiedName(px);
          boolean _and = false;
          boolean _and_1 = false;
          boolean _notEquals = (!Objects.equal(pxItem, null));
          if (!_notEquals) {
            _and_1 = false;
          } else {
            String _valueTypeQualifiedName = pxItem.getValueTypeQualifiedName();
            boolean _notEquals_1 = (!Objects.equal(_valueTypeQualifiedName, null));
            _and_1 = _notEquals_1;
          }
          if (!_and_1) {
            _and = false;
          } else {
            boolean _notEquals_2 = (!Objects.equal(pxfullyQualifiedName, null));
            _and = _notEquals_2;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<Property> _filter = IterableExtensions.<Property>filter(_properties_1, _function_2);
      Iterables.<Property>addAll(childPropertiesWithClasses, _filter);
      return childPropertiesWithClasses;
    }
  }
}
