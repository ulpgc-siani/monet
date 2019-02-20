package org.monet.editor.ui.framework;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.monet.editor.core.parsers.form.Property;
import org.monet.editor.core.parsers.form.PropertyList;

public class SelectFieldsDialog extends MonetEditorDialog {
	private PropertyList fieldList;

	private Table table;
	private Button openButton;
	private Menu popupMenu;
	
	private PropertyList selection;

	public SelectFieldsDialog(Shell parentShell) {
		super(parentShell);
		this.selection = new PropertyList();
	}

	@Override
	public void create() {
		super.create();
	}

	@Override
	protected Control createDialogArea(Composite container) {
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK | SWT.MULTI);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateSelection();
			}
		});

		popupMenu = new Menu(getShell(), SWT.POP_UP);
		MenuItem item;
		item = new MenuItem(popupMenu, SWT.PUSH);
		item.setText("Check");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				select(true);
			}
		});
		item = new MenuItem(popupMenu, SWT.PUSH);
		item.setText("Uncheck");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				select(false);
			}
		});
		table.setMenu(popupMenu);

		TableColumn tableColumn;
		tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Name");
		tableColumn.setWidth(250);
		tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Type");
		tableColumn.setWidth(150);
		tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Multiple");
		tableColumn.setWidth(100);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		refreshTable();

		container.pack(true);
		return container;
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.TITLE);
		setBlockOnOpen(true);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		openButton = createButton(parent, OK, "Generate", true);
		openButton.setEnabled(false);

		createButton(parent, CANCEL, "Cancel", false);
		refresh();
	}

	protected void refresh() {
		for (MenuItem item : popupMenu.getItems())
			item.setEnabled(table.getSelectionCount() > 0);
		if (openButton != null)
			openButton.setEnabled(selection.size() > 0);
	}

	private void refreshTable() {
		if (fieldList == null)
			return;

		for (Property field : fieldList) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { field.getName(), field.getType(), field.isMultiple().toString() });
			item.setChecked(true);
			item.setData(field);
		}
		updateSelection();
	}

	private void updateSelection() {
		selection.clear();
		for (TableItem item : table.getItems()) {
			if (!item.getChecked())
				continue;
			selection.add((Property) item.getData());
		}
		refresh();
	}

	private void select(boolean value) {
		TableItem[] selection = table.getSelection();
		for (TableItem item : selection)
			item.setChecked(value);
		updateSelection();
	}

	public PropertyList getFieldList() {
		return fieldList;
	}

	public void setFieldList(PropertyList fieldList) {
		this.fieldList = fieldList;
	}

	public PropertyList getSelection() {
		return selection;
	}

	public void setSelection(PropertyList selection) {
		this.selection = selection;
	}

}
