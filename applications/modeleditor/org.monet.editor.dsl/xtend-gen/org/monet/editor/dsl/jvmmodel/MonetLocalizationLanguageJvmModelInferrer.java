package org.monet.editor.dsl.jvmmodel;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.generator.JvmStaticBlock;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.monetLocalizationLanguage.DomainModel;
import org.monet.editor.dsl.monetLocalizationLanguage.StringResource;
import org.monet.metamodel.interfaces.Language;

@SuppressWarnings("all")
public class MonetLocalizationLanguageJvmModelInferrer extends AbstractModelInferrer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  protected void _infer(final DomainModel m, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    IProject project = XtendHelper.getIProject(m);
    String packageBase = ProjectHelper.getPackageBase(project);
    String _code = m.getCode();
    String _javaIdentifier = JavaHelper.toJavaIdentifier(_code);
    final String qualifiedName = ((packageBase + ".Language") + _javaIdentifier);
    final String defaultLngClassname = (packageBase + ".Language");
    JvmGenericType defaultLngClass = null;
    String _code_1 = m.getCode();
    boolean _equals = Objects.equal(_code_1, "default");
    if (_equals) {
      JvmGenericType _class = this._monetJvmTypesBuilder.toClass(m, defaultLngClassname);
      defaultLngClass = _class;
      IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(defaultLngClass);
      final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
        public void apply(final JvmGenericType it) {
          int counter = 0;
          EList<StringResource> _features = m.getFeatures();
          for (final StringResource p : _features) {
            {
              counter = (counter + 1);
              final int id = counter;
              EList<JvmMember> _members = it.getMembers();
              String _name = p.getName();
              JvmTypeReference _newTypeRef = MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.newTypeRef(p, int.class);
              final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
                public void apply(final JvmField it) {
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable appender) {
                      String _valueOf = String.valueOf(id);
                      appender.append(_valueOf);
                    }
                  };
                  MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.setInitializer(it, _function);
                  it.setStatic(true);
                  it.setFinal(true);
                  it.setVisibility(JvmVisibility.PUBLIC);
                }
              };
              JvmField _field = MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.toField(m, _name, _newTypeRef, _function);
              MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.<JvmField>operator_add(_members, _field);
            }
          }
        }
      };
      _accept.initializeLater(_function);
    }
    JvmGenericType _class_1 = this._monetJvmTypesBuilder.toClass(m, qualifiedName);
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept_1 = acceptor.<JvmGenericType>accept(_class_1);
    final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _newTypeRef = MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.newTypeRef(m, Language.class);
        MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _newTypeRef);
        final StringBuilderBasedAppendable x = new StringBuilderBasedAppendable();
        EList<StringResource> _features = m.getFeatures();
        for (final StringResource p : _features) {
          {
            x.append("this.labelMap.put(");
            x.append(defaultLngClassname);
            x.append(".");
            String _name = p.getName();
            x.append(_name);
            x.append(", \"");
            String _value = p.getValue();
            boolean _notEquals = (!Objects.equal(_value, null));
            if (_notEquals) {
              String _value_1 = p.getValue();
              String _stringLiteral = JavaHelper.toStringLiteral(_value_1);
              x.append(_stringLiteral);
            }
            x.append("\");\n");
          }
        }
        EList<JvmMember> _members = it.getMembers();
        JvmTypeReference _resolveVoidType = MonetLocalizationLanguageJvmModelInferrer.this._typeRefCache.resolveVoidType(m);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                String _string = x.toString();
                ap.append(_string);
              }
            };
            MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.toMethod(m, "init", _resolveVoidType, _function);
        MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
        EList<JvmMember> _members_1 = it.getMembers();
        final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable ap) {
                ap.append("org.monet.metamodel.Dictionary.getCurrentInstance().registerLanguage(");
                ap.append(qualifiedName);
                ap.append(".class, \"");
                String _code = m.getCode();
                ap.append(_code);
                ap.append("\");");
              }
            };
            MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmStaticBlock _staticBlock = MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.toStaticBlock(m, _function_1);
        MonetLocalizationLanguageJvmModelInferrer.this._monetJvmTypesBuilder.<JvmStaticBlock>operator_add(_members_1, _staticBlock);
      }
    };
    _accept_1.initializeLater(_function_1);
    return;
  }
  
  public void infer(final EObject m, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    if (m instanceof DomainModel) {
      _infer((DomainModel)m, acceptor, prelinkingPhase);
      return;
    } else if (m != null) {
      _infer(m, acceptor, prelinkingPhase);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(m, acceptor, prelinkingPhase).toString());
    }
  }
}
