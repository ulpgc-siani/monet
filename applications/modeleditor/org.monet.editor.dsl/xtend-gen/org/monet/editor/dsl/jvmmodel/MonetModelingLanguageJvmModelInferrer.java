package org.monet.editor.dsl.jvmmodel;

import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.monet.editor.dsl.jvmmodel.inferers.DefinitionInferer;
import org.monet.editor.dsl.jvmmodel.inferers.DistributionInferer;
import org.monet.editor.dsl.jvmmodel.inferers.ProjectInferer;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;

/**
 * <p>Infers a JVM model from the source model.</p>
 * 
 * <p>The JVM model should contain all elements that would appear in the Java code
 * which is generated from the source model. Other models link against the JVM model rather than the source model.</p>
 */
@SuppressWarnings("all")
public class MonetModelingLanguageJvmModelInferrer extends AbstractModelInferrer {
  /**
   * conveninence API to build and initialize JvmTypes and their members.
   */
  @Inject
  private ProjectInferer projectInferer;
  
  @Inject
  private DistributionInferer distributionInferer;
  
  @Inject
  private DefinitionInferer definitionInferer;
  
  protected void _infer(final ProjectModel e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    this.projectInferer.infer(e, acceptor, prelinkingPhase);
  }
  
  protected void _infer(final DistributionModel e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    this.distributionInferer.infer(e, acceptor, prelinkingPhase);
  }
  
  protected void _infer(final Definition e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    this.definitionInferer.infer(e, acceptor, prelinkingPhase);
  }
  
  public void infer(final EObject e, final IJvmDeclaredTypeAcceptor acceptor, final boolean prelinkingPhase) {
    if (e instanceof Definition) {
      _infer((Definition)e, acceptor, prelinkingPhase);
      return;
    } else if (e instanceof DistributionModel) {
      _infer((DistributionModel)e, acceptor, prelinkingPhase);
      return;
    } else if (e instanceof ProjectModel) {
      _infer((ProjectModel)e, acceptor, prelinkingPhase);
      return;
    } else if (e != null) {
      _infer(e, acceptor, prelinkingPhase);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(e, acceptor, prelinkingPhase).toString());
    }
  }
}
