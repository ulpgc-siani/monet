package org.monet.editor.core.commands;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.monet.editor.dsl.monetModelingLanguage.DefinitionModel;
import org.monet.editor.dsl.monetModelingLanguage.DomainModel;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;
import org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration;
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;
import org.monet.editor.library.EclipseHelper;

public class RefactorCommand implements ICommand {
	private IResource resource;
	private IPath oldPath;
	
	public RefactorCommand(IResource resource, IPath oldPath) {
		super();
		this.resource = resource;
		this.oldPath = oldPath;
	}

	@Override
	public void execute() {
	  if (resource.getFileExtension() == null) return;
    if (!resource.getFileExtension().equals("mml")) return;
    
    //TODO: No funciona con algunas definiciones, da error al serializar algunas operaciones...
//		if (resource.getProjectRelativePath().lastSegment().equals(oldPath.lastSegment()))
//			refactorPackage();
//		else
//			refactorName();
	}
	
	private void refactorPackage() {
		DomainModel model = (DomainModel)EclipseHelper.getModelForResource(resource);
		if(model instanceof DefinitionModel) {
		  DefinitionModel definitionModel = (DefinitionModel)model;
		  for(EObject element : definitionModel.getElements()) {
		    if(element instanceof PackageDeclaration) {
		      PackageDeclaration packageDeclaration = (PackageDeclaration)element;
		      packageDeclaration.setName(resource.getProjectRelativePath()
		                                         .removeFirstSegments(1)
		                                         .removeLastSegments(1)
		                                         .toString()
		                                         .replaceAll("/", "."));
		    }
		  }
		} else if(model instanceof ProjectModel || model instanceof DistributionModel) {
		  return;
		}
		EclipseHelper.saveModelToResource(model);
	}
	
	private void refactorName() {
	  DomainModel model = (DomainModel)EclipseHelper.getModelForResource(resource);
    if(model instanceof DefinitionModel) {
      DefinitionModel definitionModel = (DefinitionModel)model;
      for(EObject element : definitionModel.getElements()) {
        if(element instanceof PackageDeclaration) {
          PackageDeclaration packageDeclaration = (PackageDeclaration)element;
          packageDeclaration.getDefinition().setName(resource.getName());
        }
      }
    } else if(model instanceof ProjectModel || model instanceof DistributionModel) {
      return;
    }
    EclipseHelper.saveModelToResource(model);
	}
	
	

}
