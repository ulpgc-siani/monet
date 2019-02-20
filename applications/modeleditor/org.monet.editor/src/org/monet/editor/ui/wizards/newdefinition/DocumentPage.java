package org.monet.editor.ui.wizards.newdefinition;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.monet.editor.MonetPreferences;
import org.monet.editor.core.framework.FileSystemHelper;
import org.monet.editor.ui.framework.MonetWizardPage;
import org.monet.editor.ui.views.DeployView;

public class DocumentPage extends MonetWizardPage {
	private Composite container;
	private Table table;

	private String template;
	private ImageRegistry registry;

	public DocumentPage() {
		super("DefineCollection");
		setTitle("Monet Definition");
		setDescription("Select the document template");
		registry = new ImageRegistry();
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setWidth(500);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				template = item.getText();
				validatePage();
			}
		});
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event event) {
				getContainer().showPage(getNextPage());
			}
			
		});
		addTemplates();
		
		container.pack(true);
		setControl(container);
		setPageComplete(false);
	}

	private void addTemplates() {
		table.removeAll();
		
		File path = new File(MonetPreferences.getTemplateDocumentPath());
		String[] templates = path.list(FileSystemHelper.USER_FILES_FILTER);		
		if (templates == null) return;
		
		for (String name : templates) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(name);
			item.setImage(loadImageDescriptor(getFileExtension(name)).createImage());
		}
	}
	
	private ImageDescriptor loadImageDescriptor(String extension) {
		if (extension == null) return null;
		ImageDescriptor descriptor = registry.getDescriptor(extension);
		if (descriptor == null) {
			descriptor = ImageDescriptor.createFromFile(DeployView.class, "/icons/documents/" + extension + ".png");
			registry.put(extension, descriptor);
		}
		return descriptor;		
	}

	private String getFileExtension(String filename) {
		int index = filename.lastIndexOf(".");
		if (index < 0) return null;
        String extension = filename.substring(index+1);
		return extension;
	}	
	

	protected boolean validatePage() {
		setPageComplete(!template.isEmpty());
		setErrorMessage(null);
		return true;
	}
	
	public String getTemplate() {
		return template;
	}

	@Override
	public void dispose() {
		registry.dispose();
		super.dispose();
	}


}
