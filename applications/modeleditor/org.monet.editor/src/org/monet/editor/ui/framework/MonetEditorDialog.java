package org.monet.editor.ui.framework;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class MonetEditorDialog extends TitleAreaDialog {
	private static final int HEIGHT = 500;
	private static final int WIDTH = 500;

	
	public MonetEditorDialog(Shell parentShell) {
		super(parentShell);
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
