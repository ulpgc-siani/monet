package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.Constants
import org.monet.editor.core.ProjectHelper
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.helper.JavaHelper
import org.monet.editor.dsl.helper.TypeRefCache
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.metamodel.Item
import org.monet.editor.dsl.monetModelingLanguage.Method
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel

class ProjectInferer extends ModelInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension TypeRefCache
	
	def void infer(ProjectModel e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		var project = XtendHelper::getIProject(e);
        var packageBase = ProjectHelper::getPackageBase(project);
        var projectName = JavaHelper.toJavaIdentifier(e.name);
        
        acceptor.accept(e.toClass(packageBase + ".manifest.Project" + projectName)).initializeLater([
        	val Item manifestItem = structure.getDefinition("project");        
        	val JvmTypeReference manifestSuperType = e.resolveProjectType.cloneWithProxies;
        	
        	members += inferDefinitionConstructorMethod(e, it.simpleName, manifestItem, prelinkingPhase, false);
        	
        	superTypes += manifestSuperType;
        	
        	members += e.toMethod("getMetamodelVersion", e.resolveStringType)[
        		body = [x | 
        			x.append("return ");
					x.append("\"");
					x.append(Constants::CURRENT_VERSION);
					x.append("\";");
        		];
        	];
        	
        	for (f : e.features) {
				switch f {
					Method: {
			        	val method = f.toMethod(JavaHelper::toAttributeJavaIdentifier(f.id), f.resolveVoidType) [
							body = f.body;
							for (p : f.params) {
								parameters += p.toParameter(p.name, p.parameterType);
							}
						]

						it.members += method;
					}
				}
			}
        ]);
	}
	
}