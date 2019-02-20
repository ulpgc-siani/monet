package org.monet.editor.ui.wizards.newdefinition;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

public class SelectionPage extends MonetWizardPage {
	private Composite container;
	private Table table;
	private ImageRegistry registry;

	private String template;
	private String className;

	public SelectionPage() {
		super("SelectTemplate");
		setTitle("Monet Definition");
		setDescription("Select the definition class");
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
				className = (String) item.getData();
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

		File path = new File(MonetPreferences.getTemplateDefinitionPath());
		String[] templates = path.list(FileSystemHelper.USER_FILES_FILTER);
		if (templates == null)
			return;

		for (String name : templates) {
			String className = readClass(new File(path, name + "/.class"));
			if(className == null)
			  continue;
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(name);
			item.setText(name);
			item.setData(className);
			item.setImage(loadImageDescriptor(className).createImage());
		}
	}

	private ImageDescriptor loadImageDescriptor(String className) {
		if (className == null)
			return null;

		ImageDescriptor descriptor = registry.getDescriptor(className);
		if (descriptor == null) {
			descriptor = ImageDescriptor.createFromFile(DeployView.class, "/icons/definitions/" + className + ".png");
			registry.put(className, descriptor);
		}
		
		return descriptor;
	}

	private String readClass(File file) {
		Scanner scan = null;
		String value = null;
		
		try {
			scan = new Scanner(file);
	    scan.useDelimiter("\n");
	    value = scan.next();
		}
		catch (FileNotFoundException e) {
			return null;
		}
		finally {
		  if (scan != null)
		    scan.close();
		}
		
		return value;
	}

	protected boolean validatePage() {
		setPageComplete(template != null);
		setErrorMessage(null);
		return true;
	}

	public String getTemplate() {
		return MonetPreferences.getTemplateDefinitionPath() + "/" + template;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public void dispose() {
		registry.dispose();
		super.dispose();
	}

}
