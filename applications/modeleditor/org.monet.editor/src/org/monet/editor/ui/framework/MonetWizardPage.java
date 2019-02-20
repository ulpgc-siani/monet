package org.monet.editor.ui.framework;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;

public abstract class MonetWizardPage extends WizardPage {
	private WizardPage nextPage;

	protected MonetWizardPage(String pageName) {
		super(pageName);
	}
	
	@Override
	public IWizardPage getNextPage() {
		if (nextPage != null) return nextPage;
		if (this.getWizard() instanceof MonetWizard) {
			MonetWizard wizard = (MonetWizard) this.getWizard();
			IWizardPage page = wizard.getNextPage();
			if (page != null) return page;
		}
		return super.getNextPage();
	}
	
	public void setNextPage(WizardPage page) {
		this.nextPage = page;
	}


}
