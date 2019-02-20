package org.monet.editor.ui.wizards.newproject;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.MonetPreferences;
import org.monet.editor.core.framework.FileSystemHelper;
import org.monet.editor.core.util.ProjectHelper;

public class SetupPage extends WizardPage {
	private Composite container;
	private Text textName;
	private Text textBasePackage;
	private Button buttonDerived;
	private Label labelList;
	private Table tableSources;

	private boolean isDerived = false;
	private TableItem selection;

	public SetupPage() {
		super("SetupProject", "Monet Project", null);
		setDescription("Create a Monet Project in the Workspace");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());

		ModifyListener modifyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				validatePage();
			}
		};

		Label label;

		label = new Label(container, SWT.NONE);
		label.setText("Name");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textName = new Text(container, SWT.BORDER);
		textName.setText("");
		textName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textName.addModifyListener(modifyListener);

		buttonDerived = new Button(container, SWT.CHECK);
		buttonDerived.setText("Is a derived project");
		buttonDerived.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonDerived.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.widget;
				isDerived = button.getSelection();
				projectTypeChanged();
				validatePage();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		label = new Label(container, SWT.NONE);
		label.setText("Package base");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textBasePackage = new Text(container, SWT.BORDER);
		textBasePackage.setText("");
		textBasePackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textBasePackage.addModifyListener(modifyListener);

		labelList = new Label(container, SWT.NONE);
		labelList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tableSources = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		TableColumn tableColumn = new TableColumn(tableSources, SWT.LEFT);
		tableColumn.setWidth(500);		
		tableSources.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableSources.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selection = (TableItem) e.item;
				showParentProjectBasePackage();
				validatePage();
			}

		});

		projectTypeChanged();
		container.pack(true);
		setControl(container);
		setPageComplete(false);
	}

	private void projectTypeChanged() {
		this.selection = null;
		if (isDerived) {
			textBasePackage.setEnabled(false);
			
			addProjects();
			labelList.setText("Select a project parent");
		} else {
			textBasePackage.setEnabled(true);
			
			addTemplates();
			labelList.setText("Select a template");
		}
		tableSources.select(0);
		TableItem[] items = tableSources.getSelection();
		selection = (items.length == 0) ? null : tableSources.getSelection()[0];
		showParentProjectBasePackage();
	}

	private void addTemplates() {
		tableSources.removeAll();

		File path = new File(MonetPreferences.getTemplateProjectPath());
		String[] projects = path.list(FileSystemHelper.USER_FILES_FILTER);		
		if (projects == null) return;
		
		for (String name : projects) {
			TableItem projectItem = new TableItem(tableSources, SWT.NONE);
			projectItem.setText(name);
		}
	}

	protected void addProjects() {
		tableSources.removeAll();

		try {
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject project : projects) {
				if (project.isOpen() && project.getNature(Constants.MONET_NATURE_ID) == null)
					continue;
				TableItem item = new TableItem(tableSources, SWT.NONE);
				item.setText(project.getName());
			}
		} catch (CoreException e) {
			MonetLog.print(e.getMessage(), e);
		}
	}

	protected void showParentProjectBasePackage() {
		if (!isDerived) return;

		IProject parentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(selection.getText());
		textBasePackage.setText(ProjectHelper.getProjectPackageBase(parentProject));
  }	
	
	protected boolean validatePage() {
		setPageComplete(false);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus status = workspace.validateName(getName(), IResource.PROJECT);

		if (!status.isOK()) {
			setErrorMessage(status.getMessage());
			return false;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getName());
		if (project.exists()) {
			setErrorMessage("A project with the same name already exists in this workspace");
			return false;
		}

		if(getBasePackage() == null || getBasePackage().isEmpty()) {
		  setErrorMessage("Package base can't be empty");
      return false;
		}
		
		if(!getBasePackage().toLowerCase().equals(getBasePackage())) {
		  setErrorMessage("Package base must be lower case");
      return false;
		}
		
		if (selection == null) {
			if (isDerived)
				setErrorMessage("A parent project must be selected");
			else
				setErrorMessage("A project template must be selected");
			return false;
		}

		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	public String getName() {
		return textName.getText().trim();
	}

	public String getBasePackage() {
		return textBasePackage.getText().trim();
	}

	public String getTemplate() {
		if (isDerived)
			return "";
		return MonetPreferences.getTemplateProjectPath() + "/" + selection.getText();
	}

	public String getParent() {
		if (!isDerived)
			return "";
		return selection.getText();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
