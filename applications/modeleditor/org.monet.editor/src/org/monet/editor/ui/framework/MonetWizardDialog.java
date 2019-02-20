package org.monet.editor.ui.framework;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class MonetWizardDialog extends WizardDialog {
	private static final int HEIGHT = 500;
	private static final int WIDTH = 500;
	
	public MonetWizardDialog(Shell shell, IWizard wizard) {
		super(shell, wizard);
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		Shell shell = this.getShell();
		Shell parentShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Rectangle bounds = parentShell.getBounds();
		int x = bounds.x + (bounds.width - WIDTH) / 2;
		int y = bounds.y + (bounds.height - WIDTH) / 2;
		shell.setLocation(x, y);
		shell.setSize(WIDTH, HEIGHT);
	}

}
