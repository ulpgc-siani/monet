package org.monet.editor.dsl.jvmmodel.inferers


import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptorimport org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.core.ProjectHelper
import com.google.inject.Inject
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.metamodel.Item
import org.monet.editor.dsl.helper.TypeRefCacheimport org.monet.editor.dsl.monetModelingLanguage.DistributionModel
import org.monet.editor.dsl.helper.JavaHelper

class DistributionInferer extends ModelInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension TypeRefCache
	
	def void infer(DistributionModel e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		var resource = XtendHelper::getIResource(e);
		var project = resource.getProject();
        val packageBase = ProjectHelper::getPackageBase(project);
		var distributionName = JavaHelper.toJavaIdentifier(resource.getParent().getName());
        
        acceptor.accept(e.toClass(packageBase + ".manifest.Distribution" + distributionName))
        		.initializeLater([
        			val Item distributionItem = structure.getDefinition("distribution");	
        			
        			members += inferDefinitionConstructorMethod(e, it.simpleName, distributionItem, prelinkingPhase, false);
        			var superType = e.resolveDistributionType.cloneWithProxies;
					if(superType != null)
						superTypes += superType;
        		]);
	}
	
}