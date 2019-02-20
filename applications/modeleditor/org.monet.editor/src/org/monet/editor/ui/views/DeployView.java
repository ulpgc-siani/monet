package org.monet.editor.ui.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.core.commands.CleanUpSpaceCommand;
import org.monet.editor.core.commands.DeleteCommand;
import org.monet.editor.core.commands.DisableDistributionCommand;
import org.monet.editor.core.commands.EnableDistributionCommand;
import org.monet.editor.core.commands.LaunchMSManagerCommand;
import org.monet.editor.core.commands.PreviewCommand;
import org.monet.editor.core.commands.PublishCommand;
import org.monet.editor.core.commands.RenameCommand;
import org.monet.editor.core.commands.RestartSpaceCommand;
import org.monet.editor.core.commands.RunCommand;
import org.monet.editor.ui.ProjectManager;
import org.monet.editor.ui.WorkspaceManager;
import org.monet.editor.ui.WorkspaceManager.IWorkspaceListener;
import org.monet.editor.ui.framework.MonetJob;
import org.monet.editor.ui.framework.MonetWizardDialog;
import org.monet.editor.ui.wizards.NewDistributionWizard;

public class DeployView extends ViewPart {
  private IWorkbenchWindow window;
  private Shell            shell;
  private TreeViewer       treeViewer;
  private TreeParent       root;
  private Action           actionNew;
  private Action           actionOpen;
  private Action           actionRemove;
  private Action           actionRename;
  private Action 		   actionEnableDistribution;
  private Action 		   actionDisableDistribution;
  private Action           actionRun;
  private Action           actionPublish;
  private Action           actionPreview;
  private Action           actionCleanUpSpace;
  private Action           actionLaunchMSManager;
  private Action           actionRefresh;
  private Action           actionRestartSpace;

  public DeployView() {
  }

  public void createPartControl(Composite parent) {
    window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    shell = window.getShell();
    treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    treeViewer.setContentProvider(new ViewContentProvider());
    treeViewer.setLabelProvider(new ViewLabelProvider());
    treeViewer.setInput(getViewSite());
    createActions();
    createMenus();
    hookTreeViewer();
    hookWorkspace();

  }

  private void hookWorkspace() {
    WorkspaceManager.instance().addListener(new IWorkspaceListener() {
      @Override
      public void changed(IResourceChangeEvent event) {
        refresh();
      }
    });
  }

  private void hookTreeViewer() {
    treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        TreeNode node = getSelection();

        boolean projectSelected = (node != null) ? (node instanceof TreeParent) : false;
        boolean distributionSelected = (node != null) ? !projectSelected : false;
        boolean distributionEnabled = (distributionSelected) ? node.getEnabled() : false;

        actionNew.setEnabled(projectSelected || distributionSelected);
        actionOpen.setEnabled(projectSelected || distributionSelected);
        actionRemove.setEnabled(distributionSelected && distributionEnabled);
        actionRename.setEnabled(distributionSelected && distributionEnabled);
        actionEnableDistribution.setEnabled(distributionSelected && !distributionEnabled);
        actionDisableDistribution.setEnabled(distributionSelected && distributionEnabled);
        
        actionPreview.setEnabled(distributionSelected && distributionEnabled);
        actionRun.setEnabled(distributionSelected && distributionEnabled);
        actionPublish.setEnabled(distributionSelected && distributionEnabled);
        actionCleanUpSpace.setEnabled(distributionSelected && distributionEnabled);
        actionRestartSpace.setEnabled(distributionSelected && distributionEnabled);
      }
    });

    treeViewer.getControl().addMouseListener(new MouseListener() {

      @Override
      public void mouseDoubleClick(MouseEvent event) {
        openDistribution();
      }

      @Override
      public void mouseDown(MouseEvent event) {
      }

      @Override
      public void mouseUp(MouseEvent event) {
      };
    });
  }

  private void createActions() {
    actionNew = new Action() {
      public void run() {
        newDistribution(this);
      }
    };
    actionNew.setText("New distribution...");
    actionNew.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/distribution.new.png"));
    actionNew.setEnabled(false);

    actionOpen = new Action() {
      public void run() {
        openDistribution();
      }
    };
    actionOpen.setText("Open");
    actionOpen.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/open.gif"));
    actionOpen.setEnabled(false);

    actionRemove = new Action() {
      public void run() {
        removeDistribution(this);
      }
    };

    actionEnableDistribution = new Action() {
    	public void run() {
    		enableDistribution(this);
    	}
    };
    actionEnableDistribution.setText("Enable distribution");
    actionEnableDistribution.setEnabled(false);
    
    actionDisableDistribution = new Action() {
    	public void run() {
    		disableDistribution(this);
    	}
    };
    actionDisableDistribution.setText("Disable distribution");
    actionDisableDistribution.setEnabled(false);
    
    actionRemove.setText("Remove...");
    actionRemove.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/distribution.delete.png"));
    actionRemove.setEnabled(false);

    actionRename = new Action() {
      public void run() {
        renameDistribution(this);
      }
    };
    actionRename.setText("Rename...");
    actionRename.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/rename.gif"));
    actionRename.setEnabled(false);

    actionPreview = new Action() {
      public void run() {
        previewDistribution(this);
      }
    };
    actionPreview.setText("Preview...");
    actionPreview.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/preview.gif"));
    actionPreview.setEnabled(false);

    actionRun = new Action() {
      public void run() {
        runCmd(this);
      }
    };
    actionRun.setText("Run...");
    actionRun.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/run.png"));
    actionRun.setEnabled(false);

    actionPublish = new Action() {
      public void run() {
        publish(this);
      }
    };
    actionPublish.setText("Publish...");
    actionPublish.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/publish.png"));
    actionPublish.setEnabled(false);

    actionCleanUpSpace = new Action() {
      public void run() {
        cleanUpSpace(this);
      }
    };
    actionCleanUpSpace.setText("CleanUp space");
    actionCleanUpSpace.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/space.cleanup.png"));
    actionCleanUpSpace.setEnabled(false);

    actionRestartSpace = new Action() {
      public void run() {
        restartSpace(this);
      }
    };
    actionRestartSpace.setText("Restart space");
    actionRestartSpace.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/space.restart.png"));
    actionRestartSpace.setEnabled(false);

    actionLaunchMSManager = new Action() {
      public void run() {
        launchMSManager(this);
      }
    };
    actionLaunchMSManager.setText("Open MS Manager");
    actionLaunchMSManager.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/msmanager.png"));

    actionRefresh = new Action() {
      public void run() {
        refresh();
      }
    };
    actionRefresh.setText("Refresh");
    actionRefresh.setImageDescriptor(ImageDescriptor.createFromFile(DeployView.class, "/icons/refresh.gif"));
  }

  private void createMenus() {
    MenuManager popupMenu = new MenuManager("#PopupMenu");
    Menu menu = popupMenu.createContextMenu(treeViewer.getControl());
    treeViewer.getControl().setMenu(menu);
    IActionBars actionBars = getViewSite().getActionBars();
    IMenuManager toolBarMenu = actionBars.getMenuManager();
    IToolBarManager toolBar = actionBars.getToolBarManager();

    addAction(actionNew, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionOpen, new IContributionManager[] { popupMenu });
    addAction(actionRename, new IContributionManager[] { popupMenu, toolBarMenu });
    addAction(actionRemove, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionEnableDistribution, new IContributionManager[] {popupMenu, toolBarMenu });
    addAction(actionDisableDistribution, new IContributionManager[] {popupMenu, toolBarMenu });
    
    addSeparator(new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    //addAction(actionPreview, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionRun, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionPublish, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    
    addSeparator(new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionLaunchMSManager, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    
    addSeparator(new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionCleanUpSpace, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionRestartSpace, new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    
    addSeparator(new IContributionManager[] { popupMenu, toolBarMenu, toolBar });
    addAction(actionRefresh, new IContributionManager[] { popupMenu, toolBar });
  }

  private void addAction(IAction action, IContributionManager[] menus) {
    for (IContributionManager menu : menus)
      menu.add(action);
  }

  private void addSeparator(IContributionManager[] menus) {
    for (IContributionManager menu : menus)
      menu.add(new Separator());
  }

  private void newDistribution(IAction action) {
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    NewDistributionWizard wizard = new NewDistributionWizard();
    wizard.init(window);

    IProject project = getSelection().getParent().getResource() != null ? getSelection().getParent().getResource().getProject() : getSelection().getResource().getProject();

    wizard.setProject(project);
    WizardDialog dialog = new MonetWizardDialog(window.getShell(), wizard);
    dialog.open();
  }

  private void openDistribution() {
    TreeNode node = getSelection();
    IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(node.getResource().getFullPath());
    IEditorInput editorInput = new FileEditorInput(file);
    IWorkbenchPage page = this.getViewSite().getWorkbenchWindow().getActivePage();
    if (page != null) {
      try {
        page.openEditor(editorInput, "org.monet.editor.dsl.MonetModelingLanguage");
      } catch (Exception e) {
        MonetLog.print(e);
      }
    }
  }

  private void refresh() {
    TreeNode selection = getSelection();
    IResource resource = null;
    if (selection != null)
      resource = selection.getResource();

    loadProjects();
    treeViewer.refresh();
    setSelection(resource);
  }

  private void enableDistribution(IAction action) {
    TreeNode node = getSelection();
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new EnableDistributionCommand(distribution));
  }

  private void disableDistribution(IAction action) {
    TreeNode node = getSelection();
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new DisableDistributionCommand(distribution));
  }

  private void removeDistribution(IAction action) {
    TreeNode node = getSelection();
    String message = "Are you sure that you want to delete " + node.getName() + " distribution?";
    if (!MessageDialog.openConfirm(shell, "Confirm remove", message))
      return;
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new DeleteCommand(distribution));
  }

  private void renameDistribution(IAction action) {
    TreeNode node = getSelection();
    InputDialog dialog = new InputDialog(shell, "Rename distribution", "New name", node.getName(), null);
    if (dialog.open() != Window.OK)
      return;
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new RenameCommand(distribution, dialog.getValue()));
  }

  private void previewDistribution(IAction action) {
    TreeNode node = getSelection();
    IResource manifest = node.getParent().getResource();
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new PreviewCommand(manifest, distribution));
  }

  private void runCmd(IAction action) {
    TreeNode node = getSelection();
    IResource manifest = node.getParent().getResource();
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new RunCommand(manifest, distribution));
  }

  private void publish(IAction action) {
    TreeNode node = getSelection();
    IResource manifest = node.getParent().getResource();
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new PublishCommand(manifest, distribution));
  }

  private void cleanUpSpace(IAction action) {
    TreeNode node = getSelection();
    IResource manifest = node.getParent().getResource();
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new CleanUpSpaceCommand(manifest, distribution));
  }
  
  private void restartSpace(IAction action) {
    TreeNode node = getSelection();
    IResource manifest = node.getParent().getResource();
    IResource distribution = node.getResource();
    MonetJob.execute(action.getText(), new RestartSpaceCommand(manifest, distribution));
  }

  private void launchMSManager(IAction action) {
    MonetJob.execute(action.getText(), new LaunchMSManagerCommand(action));
  }

  @Override
  public void setFocus() {
    treeViewer.getControl().setFocus();
  }

  private TreeNode getSelection() {
    Object obj = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
    if (!(obj instanceof TreeNode))
      return null;
    else
      return (TreeNode) obj;
  }

  private void setSelection(IResource resource) {
    IProject project = (resource != null) ? resource.getProject() : ProjectManager.instance().getActiveProject();
    if (project == null)
      return;

    TreeNode node = root.findResource(project.getFile(Constants.PROJECT_FILE));
    if (node == null)
      return;
    if (node instanceof TreeParent) {
      TreeParent projectNode = (TreeParent) node;
      node = projectNode.findResource(resource);
      if ((node == null) && (projectNode.children.size() > 0))
        node = projectNode.children.get(0);
      if (node == null)
        node = projectNode;
    }

    treeViewer = DeployView.this.treeViewer;
    treeViewer.expandToLevel(node, 2);
    treeViewer.setSelection(new StructuredSelection(node), true);
  }

  public void loadProjects() {
    root = new TreeParent("");
    try {
      IWorkspace workspace = ResourcesPlugin.getWorkspace();

      IProject[] projects = workspace.getRoot().getProjects();
      IFile fileManifest, fileSetup, fileDisabled;
      IFolder folder;

      for (IProject project : projects) {
        if (!project.isOpen())
          continue;
        if (!project.hasNature(Constants.MONET_NATURE_ID))
          continue;

        fileManifest = project.getFile(Constants.PROJECT_FILE);
        if ((fileManifest == null) || (!fileManifest.exists()))
          continue;

        folder = project.getFolder(Constants.DIST_FOLDER);
        if ((folder == null)||(!folder.exists()))
          continue;

        TreeParent projectNode = new TreeParent(project.getName());
        projectNode.setResource(fileManifest);
        root.addChild(projectNode);

        IResource[] distributions = folder.members();
        for (IResource resourceDistribution : distributions) {
          if (resourceDistribution instanceof IFolder) {
            IFolder distribution = (IFolder) resourceDistribution;
            fileSetup = distribution.getFile(Constants.DISTRIBUTION_FILE);
            fileDisabled = distribution.getFile(Constants.DISTRIBUTION_DISABLED_FILE);
            if ((fileSetup == null) || (!fileSetup.exists()))
              continue;
            TreeNode nodeDistribution = new TreeNode(distribution.getName());
            nodeDistribution.setResource(fileSetup);
            nodeDistribution.setEnabled((fileDisabled == null) || (!fileDisabled.exists()));
            projectNode.addChild(nodeDistribution);
          }
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private class TreeNode implements IAdaptable {
    private String     name;
    private TreeParent parent;
    private IResource  resource;
    private Boolean    enabled;

    public TreeNode(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setParent(TreeParent parent) {
      this.parent = parent;
    }

    public TreeParent getParent() {
      return parent;
    }

    public String toString() {
      return getName();
    }

    protected IResource getResource() {
      return resource;
    }

    protected void setResource(IResource resource) {
      this.resource = resource;
    }

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class key) {
      return null;
    }

  }

  class TreeParent extends TreeNode {
    private ArrayList<TreeNode> children;

    public TreeParent(String name) {
      super(name);
      children = new ArrayList<TreeNode>();
    }

    public void addChild(TreeNode child) {
      children.add(child);
      child.setParent(this);
    }

    public void removeChild(TreeNode child) {
      children.remove(child);
      child.setParent(null);
    }

    public TreeNode[] getChildren() {
      return (TreeNode[]) children.toArray(new TreeNode[children.size()]);
    }

    public boolean hasChildren() {
      return children.size() > 0;
    }

    public TreeNode findResource(IResource resource) {
      for (TreeNode node : children)
        if (node.getResource().equals(resource))
          return node;
      return null;
    }

  }

  class ViewContentProvider implements ITreeContentProvider {

    public Object[] getElements(Object parent) {
      if (parent.equals(getViewSite())) {
        if (root == null)
          loadProjects();

        return getChildren(root);
      }
      return getChildren(parent);
    }

    public Object getParent(Object child) {
      if (child instanceof TreeNode)
        return ((TreeNode) child).getParent();

      return null;
    }

    public Object[] getChildren(Object parent) {
      if (parent instanceof TreeParent)
        return ((TreeParent) parent).getChildren();

      return new Object[0];
    }

    public boolean hasChildren(Object parent) {
      if (parent instanceof TreeParent)
        return ((TreeParent) parent).hasChildren();
      return false;
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    public void dispose() {
    }

  }

  class ViewLabelProvider extends LabelProvider {
    public String getText(Object obj) {
      return obj.toString();
    }

    public Image getImage(Object obj) {
      if (obj instanceof TreeParent)
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
      else {
    	  if (obj instanceof TreeNode) {
    		  if (((TreeNode)obj).getEnabled()) 
    		  	return ImageDescriptor.createFromFile(DeployView.class, "/icons/distribution.png").createImage();
    		  else
    			return ImageDescriptor.createFromFile(DeployView.class, "/icons/distribution_disabled.png").createImage();
    	  } else {
    		  return null;
    	  }
      }
        
    }

  }

}
