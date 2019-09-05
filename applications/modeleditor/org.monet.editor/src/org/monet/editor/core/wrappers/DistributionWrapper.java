package org.monet.editor.core.wrappers;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.ManifestFeature;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.PropertyFeature;
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;
import org.monet.editor.dsl.monetModelingLanguage.StringLiteral;
import org.monet.editor.library.EclipseHelper;

public class DistributionWrapper {

  private DistributionModel distributionModel;

  public DistributionWrapper(EObject setupModel) {
    this.distributionModel = (DistributionModel) setupModel;
  }

  public static DistributionWrapper fromFile(IFile setupFile) {
    EObject setupModel = EclipseHelper.getModelForResource(setupFile);
    return new DistributionWrapper(setupModel);
  }

  public String getName() {
    return this.distributionModel.getName();
  }

  public String getDeployUri() {
    for (ManifestFeature p : this.distributionModel.getFeatures()) {
      if (p instanceof Property && ((Property) p).getId().equals("space")) {
        for (PropertyFeature feature : ((Property) p).getFeatures()) {
          if (feature instanceof Attribute) {
            Attribute attribute = ((Attribute) feature);
            if (attribute.getId().equals("deploy-uri")) {
              if (attribute.getValue() instanceof StringLiteral) {
                return ((StringLiteral) attribute.getValue()).getValue();
              }
            }
          }
        }
      }
    }

    return null;
  }

  public String getDeployPath() {
    for (ManifestFeature p : this.distributionModel.getFeatures()) {
      if (p instanceof Property && ((Property) p).getId().equals("space")) {
        for (PropertyFeature feature : ((Property) p).getFeatures()) {
          if (feature instanceof Attribute) {
            Attribute attribute = ((Attribute) feature);
            if (attribute.getId().equals("deploy-path")) {
              if (attribute.getValue() instanceof StringLiteral) {
                return ((StringLiteral) attribute.getValue()).getValue();
              }
            }
          }
        }
      }
    }

    return null;
  }

  public String getLabel() {
    for (ManifestFeature feature : this.distributionModel.getFeatures()) {
      if (feature instanceof Attribute) {
        Attribute attribute = ((Attribute) feature);
        if (attribute.getId().equals("title")) {
          if (attribute.getValue() instanceof StringLiteral) {
            return ((StringLiteral) attribute.getValue()).getValue();
          }
        }
      }
    }

    return this.distributionModel.getName();
  }

}
