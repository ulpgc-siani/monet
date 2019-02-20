package org.monet.editor.dsl.validation;

import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.validation.Check;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.monetLocalizationLanguage.DomainModel;
import org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage;
import org.monet.editor.dsl.monetLocalizationLanguage.StringResource;
import org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage;

import com.google.inject.Inject;
 

public class MonetLocalizationLanguageJavaValidator extends AbstractMonetLocalizationLanguageJavaValidator {

  @Inject
  private TypeReferences references;

  @Check
  public void checkDuplicates(DomainModel language) {
    
    this.checkFeatures(language);
    
    // Check filename
    if (!language.getCode().equals("default")) {
      String filename = language.eResource().getURI().trimFileExtension().lastSegment();
      String expectedName = String.format("%s", language.getCode());
      if (!filename.endsWith(expectedName)) {
        warning("Language filename is recommended to include locale code as " + filename + expectedName, MonetLocalizationLanguagePackage.Literals.DOMAIN_MODEL__CODE);
      }
    }
  }

  @Check
  public void checkPackageNameEqualsPath(DomainModel _package) {
    List<String> fileParts = _package.eResource().getURI().segmentsList();
    // removes "resource", project folder, source folder and filename
    StringBuilder path = new StringBuilder();
    int filePartsSize = fileParts.size();
    for (int i = 0; i < filePartsSize; i++) {
      if (i < 3 || i + 1 == filePartsSize)
        continue;
      path.append(fileParts.get(i));
      path.append(".");
    }
    
    IProject project = XtendHelper.getIProject(_package);
    String packageBase = ProjectHelper.getPackageBase(project);
    
    if (!(packageBase + ".").equals(path.toString()))
      warning("Language files should be on project base package directory", MonetLocalizationLanguagePackage.Literals.DOMAIN_MODEL__CODE);
  }
  
  private void checkFeatures(DomainModel model) {
    HashSet<String> parentFeaturesSet = new HashSet<String>();
    HashSet<String> names = new HashSet<String>();
    
    if (!model.getCode().equals("default")) {
      IProject project = XtendHelper.getIProject(model);
      String packageBase = ProjectHelper.getPackageBase(project);

      JvmTypeReference language = references.getTypeForName(packageBase + ".Language", model);
      if (language != null) {
        for (JvmMember member : ((JvmGenericType) language.getType()).getMembers()) {
          if (member instanceof JvmField)
            parentFeaturesSet.add(member.getSimpleName());
        }
      } else {
        error("No language file found.", MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
      }      
    } 
    else {
      for (StringResource feature : model.getFeatures())
        parentFeaturesSet.add(feature.getName());
    }

    for (StringResource feature : model.getFeatures()) {
      String featureName = feature.getName();
      
      if (!parentFeaturesSet.contains(featureName))
        error(String.format("Declare %s in default language file, then overrides", featureName), feature, MonetLocalizationLanguagePackage.Literals.STRING_RESOURCE__NAME, 0);
      
      if (names.contains(featureName))
        error(String.format("Duplicated variable %s ", featureName), feature, MonetLocalizationLanguagePackage.Literals.STRING_RESOURCE__NAME, 0);

      names.add(featureName);
    }
  }
  
}
