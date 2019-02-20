package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.Constants;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.inferers.ModelInferer;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.monetModelingLanguage.ManifestFeature;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;

@SuppressWarnings("all")
public class ProjectInferer extends ModelInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  public void infer(final ProjectModel e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    IProject project = XtendHelper.getIProject(e);
    String packageBase = ProjectHelper.getPackageBase(project);
    String _name = e.getName();
    String projectName = JavaHelper.toJavaIdentifier(_name);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(e, ((packageBase + ".manifest.Project") + projectName));
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        final Item manifestItem = ProjectInferer.this.structure.getDefinition("project");
        JvmTypeReference _resolveProjectType = ProjectInferer.this._typeRefCache.resolveProjectType(e);
        final JvmTypeReference manifestSuperType = ProjectInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolveProjectType);
        EList<JvmMember> _members = it.getMembers();
        String _simpleName = it.getSimpleName();
        JvmConstructor _inferDefinitionConstructorMethod = ProjectInferer.this.inferDefinitionConstructorMethod(e, _simpleName, manifestItem, prelinkingPhase, false);
        ProjectInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _inferDefinitionConstructorMethod);
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        ProjectInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, manifestSuperType);
        EList<JvmMember> _members_1 = it.getMembers();
        JvmTypeReference _resolveStringType = ProjectInferer.this._typeRefCache.resolveStringType(e);
        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
          public void apply(final JvmOperation it) {
            final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
              public void apply(final ITreeAppendable x) {
                x.append("return ");
                x.append("\"");
                x.append(Constants.CURRENT_VERSION);
                x.append("\";");
              }
            };
            ProjectInferer.this._monetJvmTypesBuilder.setBody(it, _function);
          }
        };
        JvmOperation _method = ProjectInferer.this._monetJvmTypesBuilder.toMethod(e, "getMetamodelVersion", _resolveStringType, _function);
        ProjectInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
        EList<ManifestFeature> _features = e.getFeatures();
        for (final ManifestFeature f : _features) {
          boolean _matched = false;
          if (!_matched) {
            if (f instanceof Method) {
              _matched=true;
              String _id = ((Method)f).getId();
              String _attributeJavaIdentifier = JavaHelper.toAttributeJavaIdentifier(_id);
              JvmTypeReference _resolveVoidType = ProjectInferer.this._typeRefCache.resolveVoidType(f);
              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  XExpression _body = ((Method)f).getBody();
                  ProjectInferer.this._monetJvmTypesBuilder.setBody(it, _body);
                  EList<JvmFormalParameter> _params = ((Method)f).getParams();
                  for (final JvmFormalParameter p : _params) {
                    EList<JvmFormalParameter> _parameters = it.getParameters();
                    String _name = p.getName();
                    JvmTypeReference _parameterType = p.getParameterType();
                    JvmFormalParameter _parameter = ProjectInferer.this._monetJvmTypesBuilder.toParameter(p, _name, _parameterType);
                    ProjectInferer.this._monetJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  }
                }
              };
              final JvmOperation method = ProjectInferer.this._monetJvmTypesBuilder.toMethod(f, _attributeJavaIdentifier, _resolveVoidType, _function_1);
              EList<JvmMember> _members_2 = it.getMembers();
              ProjectInferer.this._monetJvmTypesBuilder.<JvmOperation>operator_add(_members_2, method);
            }
          }
        }
      }
    };
    _accept.initializeLater(_function);
  }
}
