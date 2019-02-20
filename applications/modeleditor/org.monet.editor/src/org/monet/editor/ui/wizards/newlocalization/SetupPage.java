package org.monet.editor.ui.wizards.newlocalization;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
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
import org.monet.editor.ui.framework.MonetWizardPage;
import org.monet.editor.util.LanguageUtil;

public class SetupPage extends MonetWizardPage {
  private Composite container;
  private Table table;

  private String definitionName;

  public SetupPage(IProject project) {
    super("SetupProject");
    setTitle("Monet Localization File");
    setDescription("Create a Monet Localization File in the base package to internationalize the model");
    
    definitionName = "";
  }

  @Override
  public void createControl(Composite parent) {
    container = new Composite(parent, SWT.NULL);
    container.setLayout(new GridLayout());

    table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
    GridData data = new GridData(GridData.FILL_BOTH);
    data.heightHint = 250;
    table.setLayoutData(data);
    TableColumn tableColumnCode = new TableColumn(table, SWT.LEFT);
    tableColumnCode.setText("Code");
    tableColumnCode.setWidth(100);
    TableColumn tableColumnLabel = new TableColumn(table, SWT.LEFT);
    tableColumnLabel.setText("Label");
    tableColumnLabel.setWidth(500);
    table.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        TableItem item = (TableItem) e.item;
        definitionName = (String)item.getText(0);
        validatePage();
      }
    });
    table.addListener(SWT.MouseDoubleClick, new Listener() {
      @Override
      public void handleEvent(Event event) {
        getContainer().showPage(getNextPage());
      }
    });
    addLocalizations();
    
    container.pack(true);
    setControl(container);
    setPageComplete(false);
  }

  private void addLocalizations() {
    table.removeAll();
    
    HashMap<String, String> languages = LanguageUtil.getLanguages();
    for (Entry<String, String> language : languages.entrySet()) {
      TableItem item = new TableItem(table, SWT.NONE);
      item.setText(0, language.getKey());
      item.setText(1, language.getValue());
    }
  }

  protected boolean validatePage() {
    setPageComplete(false);

    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IStatus status = workspace.validateName(getName(), IResource.PROJECT);

    if (!status.isOK()) {
      setErrorMessage(status.getMessage());
      return false;
    }

    if (definitionName.isEmpty()) {
      setErrorMessage("Name is required");
      return false;
    }

    setErrorMessage(null);
    setPageComplete(true);
    return true;
  }

  public String getName() {
    return definitionName;
  }

  @Override
  public void dispose() {
    super.dispose();
  }

}
