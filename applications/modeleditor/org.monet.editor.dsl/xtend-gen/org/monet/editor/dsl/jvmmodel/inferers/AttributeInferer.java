package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral;
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral;
import org.monet.editor.dsl.monetModelingLanguage.FloatLiteral;
import org.monet.editor.dsl.monetModelingLanguage.IntLiteral;
import org.monet.editor.dsl.monetModelingLanguage.LocalizedText;
import org.monet.editor.dsl.monetModelingLanguage.Referenciable;
import org.monet.editor.dsl.monetModelingLanguage.Resource;
import org.monet.editor.dsl.monetModelingLanguage.StringLiteral;
import org.monet.editor.dsl.monetModelingLanguage.TimeLiteral;
import org.monet.editor.dsl.monetModelingLanguage.XTReference;
import org.monet.metamodel.Definition;

@SuppressWarnings("all")
public class AttributeInferer {
  private static class MonetTypeReferences {
  }
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  private TypeReferences references;
  
  public void infer(final Attribute a, final ITreeAppendable x, final Item parentMetadefinition, final String context, final boolean useSetter, final boolean prelinkingPhase) {
    try {
      AttributeValue v = a.getValue();
      String _id = a.getId();
      Item metaattribute = parentMetadefinition.getChild(_id);
      String _valueTypeQualifiedName = metaattribute.getValueTypeQualifiedName();
      boolean _startsWith = _valueTypeQualifiedName.startsWith("Expression:");
      if (_startsWith) {
        return;
      }
      x.append(context);
      boolean _isMultiple = metaattribute.isMultiple();
      if (_isMultiple) {
        x.append(".get");
        String _id_1 = a.getId();
        String _javaIdentifier = JavaHelper.toJavaIdentifier(_id_1);
        x.append(_javaIdentifier);
        x.append("().add(");
      } else {
        if (useSetter) {
          x.append(".set");
          String _id_2 = a.getId();
          String _javaIdentifier_1 = JavaHelper.toJavaIdentifier(_id_2);
          x.append(_javaIdentifier_1);
          x.append("(");
        } else {
          x.append("._");
          String _id_3 = a.getId();
          String _attributeJavaIdentifier = JavaHelper.toAttributeJavaIdentifier(_id_3);
          x.append(_attributeJavaIdentifier);
          x.append(" = ");
        }
      }
      boolean _equals = Objects.equal(metaattribute, null);
      if (_equals) {
        return;
      }
      boolean _matched = false;
      if (!_matched) {
        if (v instanceof LocalizedText) {
          _matched=true;
          IProject project = XtendHelper.getIProject(v);
          String packageBase = ProjectHelper.getPackageBase(project);
          JvmTypeReference languageClass = this.references.getTypeForName((packageBase + ".Language"), v);
          boolean _equals_1 = Objects.equal(languageClass, null);
          if (_equals_1) {
            x.append("null/** No default language file found */");
          } else {
            String _qualifiedName = languageClass.getQualifiedName();
            x.append(_qualifiedName);
            x.append(".");
            String _value = ((LocalizedText)v).getValue();
            x.append(_value);
          }
        }
      }
      if (!_matched) {
        if (v instanceof Resource) {
          _matched=true;
          IProject project = XtendHelper.getIProject(v);
          String packageBase = ProjectHelper.getPackageBase(project);
          x.append(packageBase);
          x.append(".Resources.");
          String _type = ((Resource)v).getType();
          String _javaIdentifier_2 = JavaHelper.toJavaIdentifier(_type);
          x.append(_javaIdentifier_2);
          x.append(".");
          String _value = ((Resource)v).getValue();
          String _javaIdentifier_3 = JavaHelper.toJavaIdentifier(_value);
          x.append(_javaIdentifier_3);
        }
      }
      if (!_matched) {
        if (v instanceof StringLiteral) {
          _matched=true;
          String _valueTypeQualifiedName_1 = metaattribute.getValueTypeQualifiedName();
          boolean isURI = Objects.equal(_valueTypeQualifiedName_1, "java.net.URI");
          if (isURI) {
            x.append("java.net.URI.create(");
          }
          x.append("\"");
          String _value = ((StringLiteral)v).getValue();
          String _stringLiteral = JavaHelper.toStringLiteral(_value);
          x.append(_stringLiteral);
          x.append("\"");
          if (isURI) {
            x.append(")");
          }
        }
      }
      if (!_matched) {
        if (v instanceof TimeLiteral) {
          _matched=true;
          x.append("new org.monet.metamodel.internal.Time(\"");
          String _value = ((TimeLiteral)v).getValue();
          x.append(_value);
          x.append("\")");
        }
      }
      if (!_matched) {
        if (v instanceof IntLiteral) {
          _matched=true;
          x.append("(long) ");
          boolean _isNegative = ((IntLiteral)v).isNegative();
          if (_isNegative) {
            x.append("-");
          }
          int _value = ((IntLiteral)v).getValue();
          String _valueOf = String.valueOf(_value);
          x.append(_valueOf);
        }
      }
      if (!_matched) {
        if (v instanceof FloatLiteral) {
          _matched=true;
          boolean _isNegative = ((FloatLiteral)v).isNegative();
          if (_isNegative) {
            x.append("-");
          }
          float _value = ((FloatLiteral)v).getValue();
          String _valueOf = String.valueOf(_value);
          x.append(_valueOf);
          x.append("F");
        }
      }
      if (!_matched) {
        if (v instanceof DoubleLiteral) {
          _matched=true;
          boolean _isNegative = ((DoubleLiteral)v).isNegative();
          if (_isNegative) {
            x.append("-");
          }
          double _value = ((DoubleLiteral)v).getValue();
          String _valueOf = String.valueOf(_value);
          x.append(_valueOf);
        }
      }
      if (!_matched) {
        if (v instanceof EnumLiteral) {
          _matched=true;
          String _valueTypeQualifiedName_1 = metaattribute.getValueTypeQualifiedName();
          String _replaceAll = _valueTypeQualifiedName_1.replaceAll("\\$", ".");
          String _plus = (_replaceAll + ".");
          String _value = ((EnumLiteral)v).getValue();
          String _plus_1 = (_plus + _value);
          x.append(_plus_1);
        }
      }
      if (!_matched) {
        if (v instanceof XTReference) {
          _matched=true;
          if (prelinkingPhase) {
            x.append("null/*Prelinking Phase!*/");
          } else {
            Referenciable _value = ((XTReference)v).getValue();
            QualifiedName refName = this._iQualifiedNameProvider.getFullyQualifiedName(_value);
            boolean _notEquals = (!Objects.equal(refName, null));
            if (_notEquals) {
              x.append("new org.monet.metamodel.internal.Ref(\"");
              String _valueTypeQualifiedName_1 = metaattribute.getValueTypeQualifiedName();
              JvmTypeReference definitionTypeReference = this.references.getTypeForName(_valueTypeQualifiedName_1, a);
              Class<?> definitionTypeClazz = null;
              JvmType _type = definitionTypeReference.getType();
              boolean _notEquals_1 = (!Objects.equal(_type, null));
              if (_notEquals_1) {
                JvmType _type_1 = definitionTypeReference.getType();
                String identifier = _type_1.getIdentifier();
                boolean _and = false;
                boolean _notEquals_2 = (!Objects.equal(identifier, null));
                if (!_notEquals_2) {
                  _and = false;
                } else {
                  boolean _isEmpty = identifier.isEmpty();
                  boolean _not = (!_isEmpty);
                  _and = _not;
                }
                if (_and) {
                  JvmType _type_2 = definitionTypeReference.getType();
                  String _identifier = _type_2.getIdentifier();
                  Class<?> _forName = Class.forName(_identifier);
                  definitionTypeClazz = _forName;
                }
              }
              boolean _and_1 = false;
              boolean _notEquals_3 = (!Objects.equal(definitionTypeClazz, null));
              if (!_notEquals_3) {
                _and_1 = false;
              } else {
                boolean _isAssignableFrom = Definition.class.isAssignableFrom(definitionTypeClazz);
                _and_1 = _isAssignableFrom;
              }
              if (_and_1) {
                String _string = refName.toString();
                x.append(_string);
              } else {
                EObject parentDefinition = ((XTReference)v).getValue();
                while ((!(parentDefinition instanceof org.monet.editor.dsl.monetModelingLanguage.Definition))) {
                  EObject _eContainer = parentDefinition.eContainer();
                  parentDefinition = _eContainer;
                }
                String _lastSegment = refName.getLastSegment();
                x.append(_lastSegment);
                x.append("\",\"");
                QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(parentDefinition);
                String _string_1 = _fullyQualifiedName.toString();
                x.append(_string_1);
                x.append("\",\"");
                String _string_2 = refName.toString();
                x.append(_string_2);
              }
              x.append("\")");
            } else {
              x.append("null/*Ref not compiled!*/");
            }
          }
        }
      }
      boolean _or = false;
      boolean _isMultiple_1 = metaattribute.isMultiple();
      if (_isMultiple_1) {
        _or = true;
      } else {
        _or = useSetter;
      }
      if (_or) {
        x.append(")");
      }
      x.append(";\n");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
