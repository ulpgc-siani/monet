package org.monet.editor.dsl.jvmmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral;
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral;
import org.monet.editor.dsl.monetModelingLanguage.Feature;
import org.monet.editor.dsl.monetModelingLanguage.FloatLiteral;
import org.monet.editor.dsl.monetModelingLanguage.IntLiteral;
import org.monet.editor.dsl.monetModelingLanguage.LocalizedText;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.PropertyFeature;
import org.monet.editor.dsl.monetModelingLanguage.Referenciable;
import org.monet.editor.dsl.monetModelingLanguage.Resource;
import org.monet.editor.dsl.monetModelingLanguage.Schema;
import org.monet.editor.dsl.monetModelingLanguage.StringLiteral;
import org.monet.editor.dsl.monetModelingLanguage.TimeLiteral;
import org.monet.editor.dsl.monetModelingLanguage.XTReference;

@SuppressWarnings("all")
public class MMLExtensions {
  /**
   * DEFINITIONS
   */
  public Iterable<Definition> getDefinitions(final Definition e) {
    EList<Feature> _features = e.getFeatures();
    return Iterables.<Definition>filter(_features, Definition.class);
  }
  
  /**
   * SCHEMA
   */
  public Schema getSchema(final Definition e) {
    EList<Feature> _features = e.getFeatures();
    Iterable<Schema> _filter = Iterables.<Schema>filter(_features, Schema.class);
    return IterableExtensions.<Schema>head(_filter);
  }
  
  /**
   * PROPERTIES
   */
  public boolean hasProperty(final Property e, final String propertyId) {
    Iterable<Property> _properties = this.getProperties(e, propertyId);
    int _size = IterableExtensions.size(_properties);
    return (_size > 0);
  }
  
  public boolean hasProperties(final Property e, final String... propertyIds) {
    Iterable<Property> _properties = this.getProperties(e, propertyIds);
    int _size = IterableExtensions.size(_properties);
    return (_size > 0);
  }
  
  public Property getProperty(final Property e, final String propertyId) {
    Iterable<Property> _properties = this.getProperties(e, propertyId);
    return IterableExtensions.<Property>head(_properties);
  }
  
  public boolean hasProperty(final Definition e, final String propertyId) {
    Iterable<Property> _properties = this.getProperties(e, propertyId);
    int _size = IterableExtensions.size(_properties);
    return (_size > 0);
  }
  
  public Property getProperty(final Definition e, final String propertyId) {
    Iterable<Property> _properties = this.getProperties(e, propertyId);
    return IterableExtensions.<Property>head(_properties);
  }
  
  public Iterable<Property> getProperties(final Property e, final String... propertyIds) {
    Iterable<Property> _properties = this.getProperties(e);
    final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
      public Boolean apply(final Property p) {
        String _id = p.getId();
        return Boolean.valueOf(MMLExtensions.this.check(_id, propertyIds));
      }
    };
    return IterableExtensions.<Property>filter(_properties, _function);
  }
  
  public Iterable<Property> getProperties(final Definition e, final String... propertyIds) {
    Iterable<Property> _properties = this.getProperties(e);
    final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
      public Boolean apply(final Property p) {
        String _id = p.getId();
        return Boolean.valueOf(MMLExtensions.this.check(_id, propertyIds));
      }
    };
    return IterableExtensions.<Property>filter(_properties, _function);
  }
  
  public Iterable<Property> getProperties(final Property e) {
    EList<PropertyFeature> _features = e.getFeatures();
    return Iterables.<Property>filter(_features, Property.class);
  }
  
  public Iterable<Property> getProperties(final Definition e) {
    EList<Feature> _features = e.getFeatures();
    return Iterables.<Property>filter(_features, Property.class);
  }
  
  /**
   * METHODS
   */
  public boolean hasMethod(final Property e, final String methodId) {
    Iterable<Method> _methods = this.getMethods(e, methodId);
    int _size = IterableExtensions.size(_methods);
    return (_size > 0);
  }
  
  public Method getMethod(final Property e, final String methodId) {
    Iterable<Method> _methods = this.getMethods(e, methodId);
    return IterableExtensions.<Method>head(_methods);
  }
  
  public boolean hasMethod(final Definition e, final String methodId) {
    Iterable<Method> _methods = this.getMethods(e, methodId);
    int _size = IterableExtensions.size(_methods);
    return (_size > 0);
  }
  
  public Method getMethod(final Definition e, final String methodId) {
    Iterable<Method> _methods = this.getMethods(e, methodId);
    return IterableExtensions.<Method>head(_methods);
  }
  
  public Iterable<Method> getMethods(final Property e, final String... methodIds) {
    Iterable<Method> _methods = this.getMethods(e);
    final Function1<Method, Boolean> _function = new Function1<Method, Boolean>() {
      public Boolean apply(final Method p) {
        String _id = p.getId();
        return Boolean.valueOf(MMLExtensions.this.check(_id, methodIds));
      }
    };
    return IterableExtensions.<Method>filter(_methods, _function);
  }
  
  public Iterable<Method> getMethods(final Definition e, final String... methodIds) {
    Iterable<Method> _methods = this.getMethods(e);
    final Function1<Method, Boolean> _function = new Function1<Method, Boolean>() {
      public Boolean apply(final Method p) {
        String _id = p.getId();
        return Boolean.valueOf(MMLExtensions.this.check(_id, methodIds));
      }
    };
    return IterableExtensions.<Method>filter(_methods, _function);
  }
  
  public Iterable<Method> getMethods(final Property e) {
    EList<PropertyFeature> _features = e.getFeatures();
    return Iterables.<Method>filter(_features, Method.class);
  }
  
  public Iterable<Method> getMethods(final Definition e) {
    EList<Feature> _features = e.getFeatures();
    return Iterables.<Method>filter(_features, Method.class);
  }
  
  /**
   * ATTRIBUTES
   */
  public boolean hasAttribute(final Property e, final String attributeId) {
    Iterable<Attribute> _attributes = this.getAttributes(e, attributeId);
    int _size = IterableExtensions.size(_attributes);
    return (_size > 0);
  }
  
  public Attribute getAttribute(final Property e, final String attributeId) {
    Iterable<Attribute> _attributes = this.getAttributes(e, attributeId);
    return IterableExtensions.<Attribute>head(_attributes);
  }
  
  public boolean hasAttribute(final Definition e, final String attributeId) {
    Iterable<Attribute> _attributes = this.getAttributes(e, attributeId);
    int _size = IterableExtensions.size(_attributes);
    return (_size > 0);
  }
  
  public Attribute getAttribute(final Definition e, final String attributeId) {
    Iterable<Attribute> _attributes = this.getAttributes(e, attributeId);
    return IterableExtensions.<Attribute>head(_attributes);
  }
  
  public Iterable<Attribute> getAttributes(final Property e, final String... attributeIds) {
    Iterable<Attribute> _attributes = this.getAttributes(e);
    final Function1<Attribute, Boolean> _function = new Function1<Attribute, Boolean>() {
      public Boolean apply(final Attribute p) {
        String _id = p.getId();
        return Boolean.valueOf(MMLExtensions.this.check(_id, attributeIds));
      }
    };
    return IterableExtensions.<Attribute>filter(_attributes, _function);
  }
  
  public Iterable<Attribute> getAttributes(final Definition e, final String... attributeIds) {
    Iterable<Attribute> _attributes = this.getAttributes(e);
    final Function1<Attribute, Boolean> _function = new Function1<Attribute, Boolean>() {
      public Boolean apply(final Attribute p) {
        String _id = p.getId();
        return Boolean.valueOf(MMLExtensions.this.check(_id, attributeIds));
      }
    };
    return IterableExtensions.<Attribute>filter(_attributes, _function);
  }
  
  public Iterable<Attribute> getAttributes(final Property e) {
    EList<PropertyFeature> _features = e.getFeatures();
    return Iterables.<Attribute>filter(_features, Attribute.class);
  }
  
  public Iterable<Attribute> getAttributes(final Definition e) {
    EList<Feature> _features = e.getFeatures();
    return Iterables.<Attribute>filter(_features, Attribute.class);
  }
  
  /**
   * ATTRIBUTE VALUES
   */
  public Property getValueAsProperty(final Attribute a) {
    Referenciable value = this.getValueAsReferenciable(a);
    if ((value instanceof Property)) {
      return ((Property) value);
    }
    return null;
  }
  
  public Definition getValueAsDefinition(final Attribute a) {
    Referenciable value = this.getValueAsReferenciable(a);
    if ((value instanceof Definition)) {
      return ((Definition) value);
    }
    return null;
  }
  
  public Referenciable getValueAsReferenciable(final Attribute a) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(a, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      AttributeValue _value = a.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and_1 = _notEquals_1;
    }
    if (!_and_1) {
      _and = false;
    } else {
      AttributeValue _value_1 = a.getValue();
      _and = (_value_1 instanceof XTReference);
    }
    if (_and) {
      AttributeValue _value_2 = a.getValue();
      return ((XTReference) _value_2).getValue();
    }
    return null;
  }
  
  public LocalizedText getValueAsLocalizedText(final Attribute a) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(a, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      AttributeValue _value = a.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and_1 = _notEquals_1;
    }
    if (!_and_1) {
      _and = false;
    } else {
      AttributeValue _value_1 = a.getValue();
      _and = (_value_1 instanceof LocalizedText);
    }
    if (_and) {
      AttributeValue _value_2 = a.getValue();
      return ((LocalizedText) _value_2);
    }
    return null;
  }
  
  public Resource getValueAsResource(final Attribute a) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(a, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      AttributeValue _value = a.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and_1 = _notEquals_1;
    }
    if (!_and_1) {
      _and = false;
    } else {
      AttributeValue _value_1 = a.getValue();
      _and = (_value_1 instanceof Resource);
    }
    if (_and) {
      AttributeValue _value_2 = a.getValue();
      return ((Resource) _value_2);
    }
    return null;
  }
  
  public String getValueAsString(final Attribute a) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(a, null));
    if (!_notEquals) {
      _and = false;
    } else {
      AttributeValue _value = a.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and = _notEquals_1;
    }
    if (_and) {
      AttributeValue literal = a.getValue();
      boolean _matched = false;
      if (!_matched) {
        if (literal instanceof EnumLiteral) {
          _matched=true;
          return ((EnumLiteral)literal).getValue();
        }
      }
      if (!_matched) {
        if (literal instanceof StringLiteral) {
          _matched=true;
          return ((StringLiteral)literal).getValue();
        }
      }
      if (!_matched) {
        if (literal instanceof TimeLiteral) {
          _matched=true;
          return ((TimeLiteral)literal).getValue();
        }
      }
    }
    return null;
  }
  
  public Integer getValueAsInteger(final Attribute a) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(a, null));
    if (!_notEquals) {
      _and = false;
    } else {
      AttributeValue _value = a.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and = _notEquals_1;
    }
    if (_and) {
      AttributeValue literal = a.getValue();
      boolean _matched = false;
      if (!_matched) {
        if (literal instanceof IntLiteral) {
          _matched=true;
          int value = ((IntLiteral)literal).getValue();
          boolean _isNegative = ((IntLiteral)literal).isNegative();
          if (_isNegative) {
            value = (value * (-1));
          }
          return Integer.valueOf(value);
        }
      }
      if (!_matched) {
        if (literal instanceof FloatLiteral) {
          _matched=true;
          float value = ((FloatLiteral)literal).getValue();
          boolean _isNegative = ((FloatLiteral)literal).isNegative();
          if (_isNegative) {
            value = (value * (-1));
          }
          return Integer.valueOf(((int) value));
        }
      }
      if (!_matched) {
        if (literal instanceof DoubleLiteral) {
          _matched=true;
          double value = ((DoubleLiteral)literal).getValue();
          boolean _isNegative = ((DoubleLiteral)literal).isNegative();
          if (_isNegative) {
            value = (value * (-1));
          }
          return Integer.valueOf(((int) value));
        }
      }
    }
    return null;
  }
  
  public Double getValueAsDouble(final Attribute a) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(a, null));
    if (!_notEquals) {
      _and = false;
    } else {
      AttributeValue _value = a.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and = _notEquals_1;
    }
    if (_and) {
      AttributeValue literal = a.getValue();
      boolean _matched = false;
      if (!_matched) {
        if (literal instanceof IntLiteral) {
          _matched=true;
          int value = ((IntLiteral)literal).getValue();
          boolean _isNegative = ((IntLiteral)literal).isNegative();
          if (_isNegative) {
            value = (value * (-1));
          }
          return Double.valueOf(((double) value));
        }
      }
      if (!_matched) {
        if (literal instanceof FloatLiteral) {
          _matched=true;
          float value = ((FloatLiteral)literal).getValue();
          boolean _isNegative = ((FloatLiteral)literal).isNegative();
          if (_isNegative) {
            value = (value * (-1));
          }
          return Double.valueOf(((double) value));
        }
      }
      if (!_matched) {
        if (literal instanceof DoubleLiteral) {
          _matched=true;
          double value = ((DoubleLiteral)literal).getValue();
          boolean _isNegative = ((DoubleLiteral)literal).isNegative();
          if (_isNegative) {
            value = (value * (-1));
          }
          return Double.valueOf(value);
        }
      }
    }
    return null;
  }
  
  /**
   * UTILS
   */
  private boolean check(final String id, final String[] featuresIds) {
    for (final String fId : featuresIds) {
      boolean _equals = Objects.equal(id, fId);
      if (_equals) {
        return true;
      }
    }
    return false;
  }
}
