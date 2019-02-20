package org.monet.editor.dsl.jvmmodel

import com.google.inject.Inject
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.monet.editor.dsl.jvmmodel.inferers.DefinitionInferer


import org.monet.editor.dsl.monetModelingLanguage.Definition

import org.monet.editor.dsl.jvmmodel.inferers.ProjectInferer
import org.monet.editor.dsl.jvmmodel.inferers.DistributionInferer
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel

/**
 * <p>Infers a JVM model from the source model.</p> 
 *
 * <p>The JVM model should contain all elements that would appear in the Java code 
 * which is generated from the source model. Other models link against the JVM model rather than the source model.</p>     
 */
class MonetModelingLanguageJvmModelInferrer extends AbstractModelInferrer {

    /**
     * conveninence API to build and initialize JvmTypes and their members.
     */
	@Inject private ProjectInferer projectInferer
	@Inject private DistributionInferer distributionInferer
	@Inject private DefinitionInferer definitionInferer

	def dispatch infer(ProjectModel e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		this.projectInferer.infer(e, acceptor, prelinkingPhase);
	}
	
	def dispatch infer(DistributionModel e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		this.distributionInferer.infer(e, acceptor, prelinkingPhase);
	}
	
	def dispatch infer(Definition e, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		this.definitionInferer.infer(e, acceptor, prelinkingPhase);
	}
	
}

