package org.monet.editor.core.wrappers;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.ManifestFeature;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;
import org.monet.editor.dsl.monetModelingLanguage.StringLiteral;
import org.monet.editor.library.EclipseHelper;

public class ProjectWrapper {

  private ProjectModel projectModel;
  
  public ProjectWrapper(EObject setupModel) {
    this.projectModel = (ProjectModel)setupModel;
  }
  
  public static ProjectWrapper fromFile(IFile setupFile) {
    EObject setupModel = EclipseHelper.getModelForResource(setupFile);
    return new ProjectWrapper(setupModel);
  }
  
  public String getName() {
    return this.projectModel.getName();
  }
  
  public String getDeployUri() {
    for(ManifestFeature feature : this.projectModel.getFeatures()) {
      if(feature instanceof Attribute) {
        Attribute attribute = ((Attribute) feature); 
        if(attribute.getId().equals("deploy-uri")) {
          if(attribute.getValue() instanceof StringLiteral) {
            return ((StringLiteral)attribute.getValue()).getValue();
          }
        }
      }
    }
    
    return null;
  }

  public String getLabel() {
    for(ManifestFeature feature : this.projectModel.getFeatures()) {
      if(feature instanceof Attribute) {
        Attribute attribute = ((Attribute) feature); 
        if(attribute.getId().equals("title")) {
          if(attribute.getValue() instanceof StringLiteral) {
            return ((StringLiteral)attribute.getValue()).getValue();
          }
        }
      }
    }
    
    return this.projectModel.getName();
  }
  
}
