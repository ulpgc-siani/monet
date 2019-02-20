package org.monet.editor.ui.decorators;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.monet.editor.Constants;

public class MonetFolderDecorator extends LabelProvider implements ILabelDecorator {

  private Image srcFolderImage = null;
  private Image libsFolderImage = null;
  private Image distFolderImage = null;
  private Image resFolderImage = null;
  private Image projectImage = null;
  private Image distributionDefinitionImage = null;
  private Image distributionFolderImage = null;
  
  public MonetFolderDecorator() {
    super();
    
    srcFolderImage = ImageDescriptor.createFromFile(MonetFolderDecorator.class, "/icons/src.folder.png").createImage();
    libsFolderImage = ImageDescriptor.createFromFile(MonetFolderDecorator.class, "/icons/libs.folder.png").createImage();
    distFolderImage = ImageDescriptor.createFromFile(MonetFolderDecorator.class, "/icons/dist.folder.png").createImage();
    resFolderImage = ImageDescriptor.createFromFile(MonetFolderDecorator.class, "/icons/res.folder.png").createImage();
    projectImage = ImageDescriptor.createFromFile(MonetFolderDecorator.class, "/icons/project.png").createImage();
    distributionDefinitionImage = ImageDescriptor.createFromFile(MonetFolderDecorator.class, "/icons/distribution.png").createImage();
    distributionFolderImage = ImageDescriptor.createFromFile(MonetFolderDecorator.class, "/icons/distribution.folder.png").createImage();
  }

  @Override
  public Image decorateImage(Image image, Object element) {
    if(isTargetFolder(element, Constants.SOURCE_FOLDER))
      return srcFolderImage;
    else if(isTargetFolder(element, Constants.LIBS_FOLDER))
      return libsFolderImage;
    else if(isTargetFolder(element, Constants.DIST_FOLDER))
      return distFolderImage;
    else if(isTargetFolder(element, Constants.RES_FOLDER, false))
      return resFolderImage;
    else if(isDistributionFolder(element))
      return distributionFolderImage;
    else if(isTargetDefinition(element, Constants.PROJECT_FILE))
      return projectImage;
    else if(isTargetDefinition(element, Constants.DISTRIBUTION_FILE))
      return distributionDefinitionImage;
    return image;
  }

  @Override
  public String decorateText(String text, Object element) {
    return text;
  }
  
  private boolean isTargetFolder(Object element, String name) {
    return isTargetFolder(element, name, true);
  }
  
  private boolean isTargetFolder(Object element, String name, boolean isRoot) {
    if(element instanceof IFolder) {
      IFolder folder = (IFolder)element;
      return (!isRoot || folder.getParent() instanceof IProject) && folder.getName().equals(name);
    }
    return false;
  }
  
  private boolean isDistributionFolder(Object element) {
    if(element instanceof IFolder) {
      IFolder folder = (IFolder)element;
      return isTargetFolder(folder.getParent(), Constants.DIST_FOLDER, true);
    }
    return false;
  }
  
  private boolean isTargetDefinition(Object element, String name) {
    if(element instanceof IFile) {
      IFile file = (IFile)element;
      return file.getName().equals(name);
    }
    return false;
  }

}
