package org.monet.editor.dsl.jvmmodel.inferers;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.TypeRefCache;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.jvmmodel.inferers.ModelInferer;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;

@SuppressWarnings("all")
public class DistributionInferer extends ModelInferer {
  @Inject
  @Extension
  private MonetJvmTypesBuilder _monetJvmTypesBuilder;
  
  @Inject
  @Extension
  private TypeRefCache _typeRefCache;
  
  public void infer(final DistributionModel e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    IResource resource = XtendHelper.getIResource(e);
    IProject project = resource.getProject();
    final String packageBase = ProjectHelper.getPackageBase(project);
    IContainer _parent = resource.getParent();
    String _name = _parent.getName();
    String distributionName = JavaHelper.toJavaIdentifier(_name);
    JvmGenericType _class = this._monetJvmTypesBuilder.toClass(e, ((packageBase + ".manifest.Distribution") + distributionName));
    IJvmDeclaredTypeAcceptor.IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
      public void apply(final JvmGenericType it) {
        final Item distributionItem = DistributionInferer.this.structure.getDefinition("distribution");
        EList<JvmMember> _members = it.getMembers();
        String _simpleName = it.getSimpleName();
        JvmConstructor _inferDefinitionConstructorMethod = DistributionInferer.this.inferDefinitionConstructorMethod(e, _simpleName, distributionItem, prelinkingPhase, false);
        DistributionInferer.this._monetJvmTypesBuilder.<JvmConstructor>operator_add(_members, _inferDefinitionConstructorMethod);
        JvmTypeReference _resolveDistributionType = DistributionInferer.this._typeRefCache.resolveDistributionType(e);
        JvmTypeReference superType = DistributionInferer.this._monetJvmTypesBuilder.cloneWithProxies(_resolveDistributionType);
        boolean _notEquals = (!Objects.equal(superType, null));
        if (_notEquals) {
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          DistributionInferer.this._monetJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, superType);
        }
      }
    };
    _accept.initializeLater(_function);
  }
}
