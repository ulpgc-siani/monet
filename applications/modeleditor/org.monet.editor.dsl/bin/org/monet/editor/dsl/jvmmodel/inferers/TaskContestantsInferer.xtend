package org.monet.editor.dsl.jvmmodel.inferers

import com.google.inject.Inject
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.generator.MonetJvmTypesBuilder
import org.monet.editor.dsl.monetModelingLanguage.Property
import org.monet.editor.dsl.monetModelingLanguage.Method
import org.monet.editor.dsl.monetModelingLanguage.Definition
import org.monet.editor.dsl.helper.XtendHelper
import org.monet.editor.dsl.helper.TypeRefCache

class TaskContestantsInferer extends BaseTaskInferer {
	
	@Inject extension MonetJvmTypesBuilder
	@Inject extension TypeRefCache
	
	@Inject private ClassNameInferer classNameInferer
	
	def void infer(Definition taskDefinition, Property contestantsProperty, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {		
		var className = this.classNameInferer.inferPropertyBehavior(contestantsProperty);
		
		acceptor.accept(contestantsProperty.toClass(className)).initializeLater[
			superTypes += contestantsProperty.resolveBehaviorTaskContestantImplType;
			
			members += contestantsProperty.toMethod("getTask", taskDefinition.resolve(classNameInferer.inferBehaviourName(taskDefinition)))[
				body = [ap | 
					ap.append("return (");
					ap.append(classNameInferer.inferBehaviourName(taskDefinition));
					ap.append(")this.getGenericTask();");
				];
			];
			
			val methodMap = XtendHelper::createMultimap();
			
			contestantsProperty.features
								.forEach([f,i |
									switch(f) {
										Method : {
											members += f.toMethod(f.id, f.resolveVoidType) [
												for(param : f.params)
													parameters += param.toParameter(param.name, param.parameterType);
												body = f.body;
											]
										}
										Property : {
											switch(f.id) {
												case "request": {
													f.inferMethod(it, "import", i, methodMap);
												}
												case "response": {
													f.inferMethod(it, "constructor", i, methodMap);
												}
												case "aborted" : {
													f.inferUniqueMethod(it, "onExecute");
												}
												case "expiration" : {
													f.inferUniqueMethod(it, "onExecute");
												}
											}
										}
									} 
								]);
			
			members += contestantsProperty.toMethod("onImportRequest", contestantsProperty.resolveVoidType)[
				parameters += taskDefinition.toParameter("code", taskDefinition.resolveStringType);
				parameters += taskDefinition.toParameter("p0", taskDefinition.resolveContestantRequestType);
				
				body = [x | 
					var declMethods = methodMap.get("import");
					x.append("int hash = code.hashCode();\n");
					inferSwitchMethodBody(x, "hash", declMethods);
				];
			];
			
			members += contestantsProperty.toMethod("onConstructResponse", contestantsProperty.resolveVoidType)[
				parameters += taskDefinition.toParameter("code", taskDefinition.resolveStringType);
				parameters += taskDefinition.toParameter("p0", taskDefinition.resolveContestantResponseType);
				
				body = [x | 
					var declMethods = methodMap.get("constructor");
					x.append("int hash = code.hashCode();\n");
					inferSwitchMethodBody(x, "hash", declMethods);
				];
			];
			
		];
	}
	
}