package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.jvmmodel.inferers.AttributeInferer;
import org.monet.editor.dsl.jvmmodel.inferers.PropertyInferer;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.metamodel.MetaModelStructure;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Define;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;
import org.monet.editor.dsl.monetModelingLanguage.Feature;
import org.monet.editor.dsl.monetModelingLanguage.ManifestFeature;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.PropertyFeature;

@SuppressWarnings("all")
public class ModelInferer {
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
  protected MetaModelStructure structure;
  
  @Inject
  private AttributeInferer attributeInferer;
  
  @Inject
  protected PropertyInferer propertyInferer;
  
  public JvmConstructor inferDefinitionConstructorMethod(final EObject e, final String className, final Item contextItem, final boolean prelinkingPhase, final boolean separateWithInitMethods) {
    final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
      public void apply(final JvmConstructor it) {
        it.setVisibility(JvmVisibility.PUBLIC);
        it.setSimpleName(className);
      }
    };
    JvmConstructor constructor = this._monetJvmTypesBuilder.toConstructor(e, _function);
    if ((!prelinkingPhase)) {
      final String code = this.getCode(e, prelinkingPhase);
      final String name = this.getName(e, prelinkingPhase);
      final String parent = this.getParent(e, prelinkingPhase);
      final Iterable<EObject> features = this.getFeatures(e, prelinkingPhase);
      final Procedure1<ITreeAppendable> _function_1 = new Procedure1<ITreeAppendable>() {
        public void apply(final ITreeAppendable x) {
          x.append("super();");
          boolean _notEquals = (!Objects.equal(code, null));
          if (_notEquals) {
            x.append("this._code = \"");
            x.append(code);
            x.append("\";\n");
          }
          boolean _notEquals_1 = (!Objects.equal(name, null));
          if (_notEquals_1) {
            x.append("this._name = \"");
            x.append(name);
            x.append("\";\n");
          }
          boolean _notEquals_2 = (!Objects.equal(parent, null));
          if (_notEquals_2) {
            x.append("this._parent = \"");
            x.append(parent);
            x.append("\";\n");
          }
          if ((e instanceof Definition)) {
            boolean _isAbstract = ((Definition) e).isAbstract();
            if (_isAbstract) {
              x.append("this._isAbstract = new IsAbstract();");
            } else {
              x.append("this._isAbstract = null;");
            }
          }
          PropertyInferer.Index index = new PropertyInferer.Index();
          PropertyInferer.Index inlineIndex = new PropertyInferer.Index();
          for (final EObject a : features) {
            boolean _matched = false;
            if (!_matched) {
              if (a instanceof Attribute) {
                _matched=true;
                ModelInferer.this.attributeInferer.infer(((Attribute)a), x, contextItem, "this", false, prelinkingPhase);
              }
            }
            if (!_matched) {
              if (a instanceof Property) {
                _matched=true;
                String _id = ((Property)a).getId();
                Item itemProperty = contextItem.getChild(_id);
                boolean _notEquals_3 = (!Objects.equal(itemProperty, null));
                if (_notEquals_3) {
                  boolean _and = false;
                  if (!separateWithInitMethods) {
                    _and = false;
                  } else {
                    boolean _renderWithInitMethod = ModelInferer.this.propertyInferer.renderWithInitMethod(((Property)a), itemProperty, true);
                    _and = _renderWithInitMethod;
                  }
                  if (_and) {
                    int _value = index.value();
                    String methodName = ("init" + Integer.valueOf(_value));
                    x.append((methodName + "();\n"));
                    int _value_1 = index.value();
                    int _descendantsCount = ModelInferer.this.propertyInferer.getDescendantsCount(e, ((Property)a), contextItem, true);
                    int _plus = (_value_1 + _descendantsCount);
                    index.setValue(_plus);
                    index.increment();
                  } else {
                    ModelInferer.this.propertyInferer.infer(e, ((Property)a), x, contextItem, "this", inlineIndex, prelinkingPhase, separateWithInitMethods, true);
                    inlineIndex.increment();
                  }
                }
              }
            }
            if (!_matched) {
              if (a instanceof Define) {
                _matched=true;
                x.append("this.defineMap.put(\"");
                String _name = ((Define)a).getName();
                x.append(_name);
                x.append("\",\"");
                String _value = ((Define)a).getValue();
                x.append(_value);
                x.append("\");");
              }
            }
          }
        }
      };
      this._monetJvmTypesBuilder.setBody(constructor, _function_1);
    }
    return constructor;
  }
  
  public Iterable<JvmOperation> inferDefinitionConstructorInitMethods(final EObject e, final String className, final Item contextItem, final boolean prelinkingPhase) {
    Iterable<EObject> features = this.getFeatures(e, prelinkingPhase);
    ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();
    PropertyInferer.Index index = new PropertyInferer.Index();
    for (final EObject a : features) {
      boolean _matched = false;
      if (!_matched) {
        if (a instanceof Property) {
          _matched=true;
          Iterable<JvmOperation> _inferMethods = this.propertyInferer.inferMethods(e, ((Property)a), contextItem, "this", index, prelinkingPhase);
          Iterables.<JvmOperation>addAll(methods, _inferMethods);
        }
      }
    }
    return methods;
  }
  
  /**
   * def Iterable<JvmOperation> inferDefinitionConstructorInitMethods(EObject e, String className, Item contextItem, boolean prelinkingPhase) {
   * var Iterable<EObject> features = this.getFeatures(e, prelinkingPhase);
   * val ArrayList<JvmOperation> methods = new ArrayList<JvmOperation>();
   * var Integer i = 0;
   * 
   * for (a : features) {
   * switch a {
   * Property: {
   * var Item itemProperty = contextItem.getChild(a.id);
   * if (itemProperty != null) {
   * var methodName = "init" + JavaHelper::toJavaIdentifier(itemProperty.name) + i;
   * 
   * methods += e.toMethod(methodName, e.resolveVoidType) [
   * visibility = JvmVisibility::PRIVATE;
   * body = [ ap |
   * this.propertyInferer.inferMethods(e, a, ap, contextItem, "this", 0, prelinkingPhase);
   * ];
   * ];
   * i = i + 1;
   * }
   * }
   * }
   * }
   * 
   * return methods;
   * }
   */
  public JvmOperation inferGetName(final EObject e, final String name) {
    JvmTypeReference _resolveStringType = this._typeRefCache.resolveStringType(e);
    final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
      public void apply(final JvmOperation it) {
        final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
          public void apply(final ITreeAppendable ap) {
            ap.append("return \"");
            ap.append(name);
            ap.append("\";");
          }
        };
        ModelInferer.this._monetJvmTypesBuilder.setBody(it, _function);
      }
    };
    JvmOperation nameMethod = this._monetJvmTypesBuilder.toMethod(e, "static_getName", _resolveStringType, _function);
    nameMethod.setStatic(true);
    nameMethod.setVisibility(JvmVisibility.PUBLIC);
    return nameMethod;
  }
  
  public String getName(final EObject e, final boolean prelinkingPhase) {
    String name = null;
    if ((e instanceof Definition)) {
      Definition d = ((Definition) e);
      QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(d);
      String _string = _fullyQualifiedName.toString();
      name = _string;
    } else {
      if ((e instanceof Property)) {
        Property p = ((Property) e);
        String _name = p.getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          String _name_1 = p.getName();
          name = _name_1;
        }
      } else {
        if ((e instanceof DistributionModel)) {
          DistributionModel s = ((DistributionModel) e);
          String _name_2 = s.getName();
          name = _name_2;
        } else {
          if ((e instanceof ProjectModel)) {
            ProjectModel m = ((ProjectModel) e);
            String _name_3 = m.getName();
            name = _name_3;
          }
        }
      }
    }
    return name;
  }
  
  public String getCode(final EObject e, final boolean prelinkingPhase) {
    String code = null;
    if ((e instanceof Definition)) {
      Definition d = ((Definition) e);
      Code _code = d.getCode();
      boolean _notEquals = (!Objects.equal(_code, null));
      if (_notEquals) {
        Code _code_1 = d.getCode();
        String _value = _code_1.getValue();
        code = _value;
      }
    } else {
      if ((e instanceof Property)) {
        Property p = ((Property) e);
        Code _code_2 = p.getCode();
        boolean _notEquals_1 = (!Objects.equal(_code_2, null));
        if (_notEquals_1) {
          Code _code_3 = p.getCode();
          String _value_1 = _code_3.getValue();
          code = _value_1;
        }
      }
    }
    return code;
  }
  
  public String getParent(final EObject e, final boolean prelinkingPhase) {
    String parent = null;
    if ((e instanceof Definition)) {
      Definition d = ((Definition) e);
      boolean _and = false;
      boolean _and_1 = false;
      boolean _and_2 = false;
      if (!(!prelinkingPhase)) {
        _and_2 = false;
      } else {
        boolean _notEquals = (!Objects.equal(d, null));
        _and_2 = _notEquals;
      }
      if (!_and_2) {
        _and_1 = false;
      } else {
        Definition _superType = d.getSuperType();
        boolean _notEquals_1 = (!Objects.equal(_superType, null));
        _and_1 = _notEquals_1;
      }
      if (!_and_1) {
        _and = false;
      } else {
        Definition _superType_1 = d.getSuperType();
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_superType_1);
        boolean _notEquals_2 = (!Objects.equal(_fullyQualifiedName, null));
        _and = _notEquals_2;
      }
      if (_and) {
        Definition _superType_2 = d.getSuperType();
        QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(_superType_2);
        String _string = _fullyQualifiedName_1.toString();
        parent = _string;
      }
    }
    return parent;
  }
  
  public Iterable<EObject> getFeatures(final EObject e, final boolean prelinkingPhase) {
    Iterable<EObject> features = null;
    if ((e instanceof Definition)) {
      Definition d = ((Definition) e);
      EList<Feature> _features = d.getFeatures();
      Iterable<EObject> _filter = Iterables.<EObject>filter(_features, EObject.class);
      features = _filter;
    } else {
      if ((e instanceof Property)) {
        Property p = ((Property) e);
        EList<PropertyFeature> _features_1 = p.getFeatures();
        Iterable<EObject> _filter_1 = Iterables.<EObject>filter(_features_1, EObject.class);
        features = _filter_1;
      } else {
        if ((e instanceof DistributionModel)) {
          DistributionModel s = ((DistributionModel) e);
          EList<ManifestFeature> _features_2 = s.getFeatures();
          Iterable<EObject> _filter_2 = Iterables.<EObject>filter(_features_2, EObject.class);
          features = _filter_2;
        } else {
          if ((e instanceof ProjectModel)) {
            ProjectModel m = ((ProjectModel) e);
            EList<ManifestFeature> _features_3 = m.getFeatures();
            Iterable<EObject> _filter_3 = Iterables.<EObject>filter(_features_3, EObject.class);
            features = _filter_3;
          }
        }
      }
    }
    return features;
  }
}
