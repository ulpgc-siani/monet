package org.monet.editor.dsl.resources;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.DefinitionModel;
import org.monet.editor.dsl.monetModelingLanguage.Feature;
import org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.PropertyFeature;

public class ResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy implements IDefaultResourceDescriptionStrategy {

  @Override
  public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
    if(eObject instanceof DefinitionModel) {
      DefinitionModel definitionModel = (DefinitionModel) eObject;
      Definition definition = null;
      for(EObject element : definitionModel.getElements()) {
        if(element instanceof PackageDeclaration) {
          definition = ((PackageDeclaration) element).getDefinition();
          break;
        }
      }
      
      if(definition != null) {
        Code code = definition.getCode();
        if(code != null) {
          String codeValue = code.getValue();
          if(codeValue != null && !codeValue.isEmpty())
            acceptor.accept(EObjectDescription.create(QualifiedName.create(codeValue), code));
        }
        
        for(Feature feature : definition.getFeatures()) {
          if(feature instanceof Property)
            createPropertyDescriptions(((Property) feature), acceptor);
        }
        
      }
    }
    
    return super.createEObjectDescriptions(eObject, acceptor);
  }

  protected void createPropertyDescriptions(Property property, IAcceptor<IEObjectDescription> acceptor) {
    Code code = property.getCode();
    if(code != null) {
      String codeValue = code.getValue();
      if(codeValue != null && !codeValue.isEmpty()) {
        acceptor.accept(EObjectDescription.create(QualifiedName.create(codeValue), code));
      }
    }
    
    for(PropertyFeature feature : property.getFeatures()) {
      if(feature instanceof Property) {
        createPropertyDescriptions(((Property) feature), acceptor);
      }
    }
  }
  
  @Override
  public boolean createReferenceDescriptions(EObject eObject, URI exportedContainerURI, IAcceptor<IReferenceDescription> acceptor) {
    return super.createReferenceDescriptions(eObject, exportedContainerURI, acceptor);
  }

}
