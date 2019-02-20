package org.monet.editor.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.monet.editor.Constants;

public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		defineActions(layout);
		defineLayout(layout);
	}

	public void defineActions(IPageLayout layout) {
		layout.addNewWizardShortcut("MonetEditor.wizards.newproject");
		layout.addNewWizardShortcut("MonetEditor.wizards.newdefinition");
		layout.addNewWizardShortcut("MonetEditor.wizards.newdistribution");
		layout.addNewWizardShortcut("MonetEditor.wizards.newlayout");
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"); 
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); 
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"); 
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard"); 
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");

		layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
	}

	public void defineLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.addView(Constants.ID_MODEL_EXPLORER_VIEW, IPageLayout.LEFT, 0.20f, editorArea);

		IFolderLayout folder = layout.createFolder("problems", IPageLayout.BOTTOM, 0.70f, editorArea);
		folder.addView(IPageLayout.ID_PROBLEM_VIEW);
		folder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		
		IFolderLayout bottomRight = layout.createFolder("deploy", IPageLayout.RIGHT, 0.50f, "problems");
		bottomRight.addView(Constants.ID_DEPLOY_VIEW);
	}
}
