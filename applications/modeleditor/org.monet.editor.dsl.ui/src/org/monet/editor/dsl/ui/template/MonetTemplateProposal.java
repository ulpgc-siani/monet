package org.monet.editor.dsl.ui.template;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.ui.editor.templates.XtextTemplateProposal;

public class MonetTemplateProposal extends XtextTemplateProposal implements ICompletionProposalExtension3 {

  public MonetTemplateProposal(Template template, TemplateContext context, IRegion region, Image image) {
    super(template, context, region, image);
  }

  public MonetTemplateProposal(Template template, TemplateContext context, IRegion region, Image image, int relevance) {
    super(template, context, region, image, relevance);
  }

  public String getAdditionalProposalInfo() {
    return ((org.monet.editor.dsl.ui.template.Template)this.getTemplate()).getHelp();
  }
  
}
