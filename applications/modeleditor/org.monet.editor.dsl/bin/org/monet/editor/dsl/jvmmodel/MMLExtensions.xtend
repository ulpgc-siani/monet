package org.monet.editor.dsl.jvmmodel

import org.monet.editor.dsl.monetModelingLanguage.Attribute
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.monetModelingLanguage.XTReference
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.monetModelingLanguage.Referenciable
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral
import org.monet.editor.dsl.monetModelingLanguage.StringLiteral
import org.monet.editor.dsl.monetModelingLanguage.TimeLiteral
import org.monet.editor.dsl.monetModelingLanguage.IntLiteral
import org.monet.editor.dsl.monetModelingLanguage.FloatLiteral
import org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral
import org.monet.editor.dsl.monetModelingLanguage.LocalizedText
import org.monet.editor.dsl.monetModelingLanguage.Resource
import org.monet.editor.dsl.monetModelingLanguage.Schema
import org.monet.editor.dsl.monetModelingLanguage.Method

class MMLExtensions {
	
	/* DEFINITIONS */
		
	def Iterable<Definition> getDefinitions(Definition e) {
		return e.features.filter(typeof(Definition));
	}
	
	/* SCHEMA */
		
	def Schema getSchema(Definition e) {
		return e.features.filter(typeof(Schema)).head;
	}
	
	/* PROPERTIES */
	
	def boolean hasProperty(Property e, String propertyId) {
		return e.getProperties(propertyId).size > 0;
	}
	
	def boolean hasProperties(Property e, String... propertyIds) {
		return e.getProperties(propertyIds).size > 0;
	}
	
	def Property getProperty(Property e, String propertyId) {
		return e.getProperties(propertyId).head;
	}
	
	def boolean hasProperty(Definition e, String propertyId) {
		return e.getProperties(propertyId).size > 0;
	}
	
	def Property getProperty(Definition e, String propertyId) {
		return e.getProperties(propertyId).head;
	}
	
	def Iterable<Property> getProperties(Property e, String... propertyIds) {
		return e.properties.filter([p | check(p.id, propertyIds)]);
	}
	
	def Iterable<Property> getProperties(Definition e, String... propertyIds) {
		return e.properties.filter([p | check(p.id, propertyIds)]);
	}
	
	def Iterable<Property> getProperties(Property e) {
		return e.features.filter(typeof(Property));
	}
	
	def Iterable<Property> getProperties(Definition e) {
		return e.features.filter(typeof(Property));
	}
	
	/* METHODS */
	
	def boolean hasMethod(Property e, String methodId) {
		return e.getMethods(methodId).size > 0;
	}
	
	def Method getMethod(Property e, String methodId) {
		return e.getMethods(methodId).head;
	}
	
	def boolean hasMethod(Definition e, String methodId) {
		return e.getMethods(methodId).size > 0;
	}
	
	def Method getMethod(Definition e, String methodId) {
		return e.getMethods(methodId).head;
	}
	
	def Iterable<Method> getMethods(Property e, String... methodIds) {
		return e.methods.filter([p | check(p.id, methodIds)]);
	}
	
	def Iterable<Method> getMethods(Definition e, String... methodIds) {
		return e.methods.filter([p | check(p.id, methodIds)]);
	}
	
	def Iterable<Method> getMethods(Property e) {
		return e.features.filter(typeof(Method));
	}
	
	def Iterable<Method> getMethods(Definition e) {
		return e.features.filter(typeof(Method));
	}
	
	/* ATTRIBUTES */
	
	def boolean hasAttribute(Property e, String attributeId) {
		return e.getAttributes(attributeId).size > 0;
	}
	
	def Attribute getAttribute(Property e, String attributeId) {
		return e.getAttributes(attributeId).head;
	}
	
	def boolean hasAttribute(Definition e, String attributeId) {
		return e.getAttributes(attributeId).size > 0;
	}
	
	def Attribute getAttribute(Definition e, String attributeId) {
		return e.getAttributes(attributeId).head;
	}
	
	def Iterable<Attribute> getAttributes(Property e, String... attributeIds) {
		return e.attributes.filter([p | check(p.id, attributeIds)]);
	}
	
	def Iterable<Attribute> getAttributes(Definition e, String... attributeIds) {
		return e.attributes.filter([p | check(p.id, attributeIds)]);
	}
	
	def Iterable<Attribute> getAttributes(Property e) {
		return e.features.filter(typeof(Attribute));
	}
	
	def Iterable<Attribute> getAttributes(Definition e) {
		return e.features.filter(typeof(Attribute));
	}
	
	/* ATTRIBUTE VALUES */
	
	def Property getValueAsProperty(Attribute a) {
		var value = a.valueAsReferenciable;
		if(value instanceof Property)
			return value as Property;
		return null;
	}
	
	def Definition getValueAsDefinition(Attribute a) {
		var value = a.valueAsReferenciable;
		if(value instanceof Definition)
			return value as Definition;
		return null;
	}
	
	def Referenciable getValueAsReferenciable(Attribute a) {
		if(a != null && a.value != null && a.value instanceof XTReference) {
			return (a.value as XTReference).value;
		}
		return null;
	}
	
	def LocalizedText getValueAsLocalizedText(Attribute a) {
		if(a != null && a.value != null && a.value instanceof LocalizedText) {
			return a.value as LocalizedText;
		}
		return null;
	}
	
	def Resource getValueAsResource(Attribute a) {
		if(a != null && a.value != null && a.value instanceof Resource) {
			return a.value as Resource;
		}
		return null;
	}
	
	def String getValueAsString(Attribute a) {
		if(a != null && a.value != null) {
			var literal = a.value;
			switch(literal) {
				EnumLiteral: { return literal.value; }
				StringLiteral: { return literal.value; }
				TimeLiteral: { return literal.value; }
			}
		}
		return null;
	}
	
	def Integer getValueAsInteger(Attribute a) {
		if(a != null && a.value != null) {
			var literal = a.value;
			switch(literal) {
				IntLiteral: { 
					var value = literal.value;
					if(literal.negative)
						value = value * -1;
					return value;
				}
				FloatLiteral: { 
					var value = literal.value;
					if(literal.negative)
						value = value * -1;
					return value as int;
				}
				DoubleLiteral: { 
					var value = literal.value;
					if(literal.negative)
						value = value * -1;
					return value as int;
				}
			}
		}
		return null;
	}
	
	def Double getValueAsDouble(Attribute a) {
		if(a != null && a.value != null) {
			var literal = a.value;
			switch(literal) {
				IntLiteral: { 
					var value = literal.value;
					if(literal.negative)
						value = value * -1;
					return value as double;
				}
				FloatLiteral: { 
					var value = literal.value;
					if(literal.negative)
						value = value * -1;
					return value as double;
				}
				DoubleLiteral: { 
					var value = literal.value;
					if(literal.negative)
						value = value * -1;
					return value;
				}
			}
		}
		return null;
	}
	
	/* UTILS */
	
	def private boolean check(String id, String[] featuresIds) {
		for(fId : featuresIds) { 
			if(id == fId) {
				return true;
			}
		}
		return false;
	}
}