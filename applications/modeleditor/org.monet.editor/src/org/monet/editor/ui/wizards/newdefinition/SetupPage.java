package org.monet.editor.ui.wizards.newdefinition;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.monet.editor.core.util.ProjectHelper;

public class SetupPage extends NewTypeWizardPage {
	private IWorkbenchWindow window;
	private Composite container;
	private Text textDefinitionName;
	private Text textPackage;

	private IProject project;
	
	private String definitionName;
	private String packageName;

	public SetupPage(IProject project) {
		super(true, "SetupDefinition");
		setTitle("Monet Definition");
		setDescription("Create a New Definition in the Monet Project");

		this.project = project;
		definitionName = "";
		packageName = "";

		window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelectionService service = window.getSelectionService();
		ISelection selection = service.getSelection();
		if (selection instanceof IStructuredSelection) {
			IJavaElement element = getInitialJavaElement((IStructuredSelection) selection);
			initContainerPage(element);
			initTypePage(element);
		}
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());

		Label label = new Label(container, SWT.NONE);
		label.setText("Name");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textDefinitionName = new Text(container, SWT.BORDER);
		textDefinitionName.setText("");
		textDefinitionName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textDefinitionName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				definitionName = textDefinitionName.getText().trim();
				validatePage();
			}
		});

		label = new Label(container, SWT.NONE);
		label.setText("Package");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textPackage = new Text(container, SWT.BORDER);
		textPackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		packageName = getCurrentPackage();
		textPackage.setText(packageName);
		textPackage.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				packageName = textPackage.getText().trim();
				validatePage();
			}
		});

		container.pack(true);
		setControl(container);
		setPageComplete(false);
	}

	private String getCurrentPackage() {
	  String currentPackage = null;
		IPackageFragment fragment = getPackageFragment();
		if (fragment != null)
			currentPackage = fragment.getElementName();

		if(currentPackage == null || currentPackage.isEmpty())
		  currentPackage = readEditorInput();
		
		if(currentPackage == null || currentPackage.isEmpty())
		  currentPackage = ProjectHelper.getProjectPackageBase(this.project);
		
		return currentPackage;
	}

	private String readEditorInput() {
		if (window == null)
			return null;

		IEditorPart part = window.getActivePage().getActiveEditor();
		if (part instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) part;
			IDocumentProvider provider = editor.getDocumentProvider();
			IDocument document = provider.getDocument(editor.getEditorInput());
			return search("package", document.get());
		}

		return null;
	}

	private String search(String token, String text) {
		int start = text.indexOf(token);
		if (start < 0)
			return "";
		start += token.length() + 1;
		int finish = text.indexOf('{', start);
		if (finish < 0)
			return "";
		return text.substring(start, finish).trim();
	}

	private boolean validatePage() {
		setPageComplete(false);

		if (definitionName.isEmpty()) {
			setErrorMessage("Name is required");
			return false;
		}

		if (packageName.isEmpty()) {
			setErrorMessage("Package is required");
			return false;
		}

		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	public void setVisible(boolean value) {
		super.setVisible(value);
		if (value)
			setPageComplete(validatePage());
	}

	public String getName() {
		return definitionName;
	}

	public String getPackageName() {
		return packageName;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
