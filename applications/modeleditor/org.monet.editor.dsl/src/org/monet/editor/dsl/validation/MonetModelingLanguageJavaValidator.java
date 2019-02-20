package org.monet.editor.dsl.validation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.common.types.util.Primitives;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.typesystem.IBatchTypeResolver;
import org.eclipse.xtext.xbase.typesystem.IResolvedTypes;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.monet.editor.core.ProjectHelper;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.metamodel.MetaModelStructure;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.AttributeValue;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;
import org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral;
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral;
import org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral;
import org.monet.editor.dsl.monetModelingLanguage.FloatLiteral;
import org.monet.editor.dsl.monetModelingLanguage.Import;
import org.monet.editor.dsl.monetModelingLanguage.IntLiteral;
import org.monet.editor.dsl.monetModelingLanguage.LocalizedText;
import org.monet.editor.dsl.monetModelingLanguage.Method;
import org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage;
import org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.Resource;
import org.monet.editor.dsl.monetModelingLanguage.Schema;
import org.monet.editor.dsl.monetModelingLanguage.StringLiteral;
import org.monet.editor.dsl.monetModelingLanguage.TimeLiteral;
import org.monet.editor.dsl.monetModelingLanguage.Variable;
import org.monet.editor.dsl.monetModelingLanguage.XTReference;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class MonetModelingLanguageJavaValidator extends AbstractMonetModelingLanguageJavaValidator {

  public static final String           CODE_NOT_FOUND           = "codenotfound";
  public static final String           CODE_DUPLICATED          = "codeduplicated";
  public static final String           NAME_NOT_FOUND           = "namenotfound";
  public static final String           PACKAGE_FOLDER_NOT_MACTH = "packagefoldernotmacth";

  @Inject
  private MetaModelStructure           metaModelStructure;
  @Inject
  private TypeReferences               references;
  @Inject
  private Primitives                   primitives;
  @Inject
  private IJvmTypeProvider.Factory     typeProviderFactory;
  @Inject
  private IContainer.Manager           containermanager;
  @Inject
  private ResourceDescriptionsProvider resourceDescriptionsProvider;

  protected HashMap<String, Integer> extractDeclaredItems(EList<?> eList) {
    HashMap<String, Integer> features = new HashMap<String, Integer>();
    addFeatures(features, eList, false);
    return features;
  }

  protected HashMap<String, Integer> extractDeclaredSetupItems(DistributionModel setup) {
    HashMap<String, Integer> features = new HashMap<String, Integer>();
    if (setup.getSuperType() != null)
      addFeatures(features, setup.getSuperType().getFeatures(), true);
    addFeatures(features, setup.getFeatures(), false);
    return features;
  }

  protected HashMap<String, Integer> extractDeclaredDefinitionItems(Definition definition) {
    HashMap<String, Integer> features = new HashMap<String, Integer>();
    if (definition.getSuperType() != null)
      addFeatures(features, definition.getSuperType().getFeatures(), true);
    else if (definition.getReplaceSuperType() != null)
      addFeatures(features, definition.getReplaceSuperType().getFeatures(), true);
    addFeatures(features, definition.getFeatures(), false);
    return features;
  }

  private void addFeatures(HashMap<String, Integer> features, EList<?> eList, boolean isParent) {
    for (Object feature : eList) {
      String key = null;
      if (feature instanceof Attribute) {
        key = ((Attribute) feature).getId();
      } else if (feature instanceof Property) {
        key = ((Property) feature).getId();
      } else if (feature instanceof Method) {
        key = ((Method) feature).getId();
      }
      Integer value = features.get(key);
      if (value == null || value < 0)
        value = 0;
      if (isParent)
        value--;
      else
        value++;
      features.put(key, value);
    }
  }

  @Check
  public void checkCodes(Code code) {
    String value = code.getValue();
    if (value == null || value.isEmpty()) {
      error("Code must have a value", MonetModelingLanguagePackage.Literals.CODE__VALUE);
    }

    if (value.length() > 15) {
      warning("The code is very long. This may cause poor performance or errors in database", MonetModelingLanguagePackage.Literals.CODE__VALUE);
    }

    if (!value.toLowerCase().equals(value)) {
      warning("The use of uppercase characters in codes are not recommended, can cause problems in database.", MonetModelingLanguagePackage.Literals.CODE__VALUE);
    }

    if (!value.matches("^[a-z|A-Z|0-9|_|\\.]+$")) {
      error("No valid characters found in code. Only use [a..z|0..9|_|.].", MonetModelingLanguagePackage.Literals.CODE__VALUE);
    }
  }

  @Check(CheckType.EXPENSIVE)
  public void getAllEntitiesFor(Code code) {
    IResourceDescriptions resourceDescriptions = resourceDescriptionsProvider.getResourceDescriptions(code.eResource());
    IResourceDescription resourceDescription = resourceDescriptions.getResourceDescription(code.eResource().getURI());
    List<IContainer> visiblecontainers = containermanager.getVisibleContainers(resourceDescription, resourceDescriptions);
    for (IContainer container : visiblecontainers) {
      int appears = 0;
      for (IEObjectDescription eobjectDescription : container.getExportedObjectsByType(MonetModelingLanguagePackage.Literals.CODE)) {
        if (code.getValue().equals(eobjectDescription.getQualifiedName().toString())) {
          appears++;
          if (appears > 1) {
            error("Code must be unique in the business model", MonetModelingLanguagePackage.Literals.CODE__VALUE, CODE_DUPLICATED);
            return;
          }
        }
      }
    }
  }

  @Check
  public void checkVariable(Variable variable) {
    LinkedList<EObject> queue = new LinkedList<EObject>();
    Item definitionItem = XtendHelper.getRootElement(variable, queue, this.metaModelStructure, false);
    if (!definitionItem.getToken().equals("importer") && !definitionItem.getToken().equals("exporter")) {
      error("Variables are only valid in importer/exporter definitions", MonetModelingLanguagePackage.Literals.VARIABLE__NAME);
    }
  }

  @Check
  public void checkAttributes(Attribute attribute) {
    try {
      LinkedList<EObject> queue = new LinkedList<EObject>();
      Item definitionItem = XtendHelper.getRootElement(attribute, queue, this.metaModelStructure, false);
      EObject model = queue.peek();
      Item attributeItem = XtendHelper.checkElement(definitionItem, queue);

      if (attributeItem == null) {
        error(String.format("Attribute '%s' not valid in this context", attribute.getId()), MonetModelingLanguagePackage.Literals.ATTRIBUTE__ID);
        return;
      }

      boolean isAssignable = false;
      String foundType = null;
      AttributeValue attributeValue = attribute.getValue();
      if (attributeValue instanceof XTReference) {
        XTReference attributeReference = (XTReference) attributeValue;

        EObject suggestedModel = EcoreUtil2.resolve(attributeReference.getValue(), attribute);
        if (suggestedModel.eIsProxy()) {
          warning(String.format("Dependent element not compiled. Recompile this definition when the other definition were compiled."), MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
          return;
        }

        final Item suggestedItem = XtendHelper.checkElement(suggestedModel, this.metaModelStructure);

        String referencedName;
        int separatorIndex = 0;
        if ((separatorIndex = attributeItem.getValueTypeQualifiedName().indexOf("|")) > -1)
          referencedName = attributeItem.getValueTypeQualifiedName().substring(separatorIndex + 1);
        else
          referencedName = attributeItem.getValueTypeQualifiedName();

        referencedName = referencedName.substring(referencedName.lastIndexOf(".") + 1);
        isAssignable = this.metaModelStructure.areFamily(JavaHelper.toJavaIdentifier(suggestedItem.getName()), referencedName);
        foundType = suggestedItem.getName();
        if (!isAssignable && suggestedItem.getRefBaseType() != null) {
          isAssignable = this.metaModelStructure.areFamily(suggestedItem.getRefBaseType(), referencedName);
        }
      } else if (attributeValue instanceof Resource) {
        Resource resourceAttribute = (Resource) attributeValue;
        String resourceType = JavaHelper.toJavaIdentifier(resourceAttribute.getType());

        foundType = "resource:" + resourceAttribute.getType();
        String attributeItemvalueType = attributeItem.getValueTypeQualifiedName();
        isAssignable = attributeItemvalueType.equals(foundType);

        IProject project = XtendHelper.getIProject(attribute);
        String packageBase = ProjectHelper.getPackageBase(project);

        JvmTypeReference resourcesClass = references.getTypeForName(packageBase + ".Resources$" + resourceType, model);
        if (resourcesClass != null) {
          boolean resourceFound = false;
          for (JvmMember member : ((JvmGenericType) resourcesClass.getType()).getMembers()) {
            if (member instanceof JvmField) {
              if (member.getSimpleName().equals(resourceAttribute.getValue())) {
                resourceFound = true;
                break;
              }
            }
          }
          if (!resourceFound) {
            error(String.format("No resource with the name '%s' found in 'res/%s' folder.", resourceAttribute.getValue(), resourceAttribute.getType()), MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
          }
        } else {
          error("No resources file found. Try cleaning the project.", MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
        }
      } else if (attributeValue instanceof LocalizedText) {
        foundType = "java.lang.String";
        String attributeItemvalueType = attributeItem.getValueTypeQualifiedName();
        isAssignable = attributeItemvalueType.equals(foundType);

        IProject project = XtendHelper.getIProject(attribute);
        String packageBase = ProjectHelper.getPackageBase(project);

        JvmTypeReference language = references.getTypeForName(packageBase + ".Language", model);
        if (language != null) {
          boolean resourceFound = false;
          for (JvmMember member : ((JvmGenericType) language.getType()).getMembers()) {
            if (member instanceof JvmField) {
              if (member.getSimpleName().equals(((LocalizedText) attributeValue).getValue())) {
                resourceFound = true;
                break;
              }
            }
          }
          if (!resourceFound) {
            error(String.format("Resource '%s' not found in language file.", ((LocalizedText) attributeValue).getValue()), MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
          }
        } else {
          error("No language file found.", MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
        }
      } else if (attributeValue instanceof EnumLiteral) {
        EnumLiteral attributeEnumLiteral = (EnumLiteral) attributeValue;
        String enumValue = attributeEnumLiteral.getValue();
        isAssignable = true;

        boolean enumValueFound = false;
        for (Item itemValue : attributeItem.getItems()) {
          if (itemValue.getName().equals(enumValue)) {
            enumValueFound = true;
            break;
          }
        }

        if (!enumValueFound) {
          error(String.format("Value of enumeration'%s' not valid.", enumValue), MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
        }
      } else if (attributeValue instanceof StringLiteral) {
        foundType = "java.lang.String";
        String attributeItemvalueType;
        if (attributeItem.getValueTypeQualifiedName().equals("java.net.URI")) {
          isAssignable = true;
          try {
            new URI(((StringLiteral) attributeValue).getValue());
          } catch (URISyntaxException e) {
            error(String.format("URI syntax error: %s.", e.getMessage()), MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
          }
        } else {
          int separatorIndex = 0;
          if ((separatorIndex = attributeItem.getValueTypeQualifiedName().indexOf("|")) > -1)
            attributeItemvalueType = attributeItem.getValueTypeQualifiedName().substring(0, separatorIndex);
          else
            attributeItemvalueType = attributeItem.getValueTypeQualifiedName();

          isAssignable = attributeItemvalueType.equals(foundType);
        }
      } else if (attributeValue instanceof TimeLiteral) {
        foundType = "org.monet.metamodel.internal.Time";
        isAssignable = attributeItem.getValueTypeQualifiedName().equals(foundType);
      } else if (attributeValue instanceof IntLiteral) {
        foundType = "java.lang.Integer";
        isAssignable = attributeItem.getValueTypeQualifiedName().equals(foundType);
      } else if (attributeValue instanceof FloatLiteral) {
        foundType = "java.lang.Float";
        isAssignable = attributeItem.getValueTypeQualifiedName().equals(foundType);
      } else if (attributeValue instanceof DoubleLiteral) {
        foundType = "java.lang.Double";
        isAssignable = attributeItem.getValueTypeQualifiedName().equals(foundType);
      } else if (attributeValue instanceof ExpressionLiteral) {
        foundType = "Expression:";
        isAssignable = attributeItem.getValueTypeQualifiedName().startsWith(foundType);
        if (isAssignable) {
          String returnType = attributeItem.getValueTypeQualifiedName();
          returnType = returnType.substring(returnType.indexOf(":") + 1);
          
          XExpression expression = ((ExpressionLiteral) attributeValue).getValue();
          if (expression != null) {            
            JvmTypeReference expressionReturnType = this.primitives.asWrapperTypeIfPrimitive(getType(expression));
            isAssignable = expressionReturnType.getQualifiedName().equals(returnType);
            foundType += expressionReturnType.getQualifiedName();
          }

        }
      }

      if (!isAssignable) {
        error(String.format("Value of attribute '%s' not valid. Expected type %s, found %s", attribute.getId(), attributeItem.getValueTypeQualifiedName(), foundType), MonetModelingLanguagePackage.Literals.ATTRIBUTE__VALUE);
      }

    } catch (Exception e) {
      error(String.format("Unknown error: %s ", e.getMessage()), MonetModelingLanguagePackage.Literals.ATTRIBUTE__ID);
    }
  }

  @Check
  public void checkSchema(Schema schema) {
    Item propertyItem = XtendHelper.checkElement(schema, this.metaModelStructure);

    if (propertyItem == null) {
      error(String.format("Property schema not valid in this context"), MonetModelingLanguagePackage.Literals.SCHEMA__ID);
      return;
    }
  }

  @Check
  public void checkProperties(Property property) {
    try {
      Item propertyItem = XtendHelper.checkElement(property, this.metaModelStructure);

      if (propertyItem == null) {
        error(String.format("Property '%s' not valid in this context", property.getId()), MonetModelingLanguagePackage.Literals.PROPERTY__ID);
        return;
      }

      HashMap<String, Integer> features = this.extractDeclaredItems(property.getFeatures());

      for (Item item : propertyItem.getItems()) {
        Integer instancesOfFeature = features.get(item.getToken());
        if (item.isRequired() && instancesOfFeature == null)
          error(String.format("Property require the element %s", item.getToken()), MonetModelingLanguagePackage.Literals.PROPERTY__ID);
        if (!item.isMultiple() && instancesOfFeature != null && instancesOfFeature > 1)
          error(String.format("Element %s can't appear more than once", item.getToken()), MonetModelingLanguagePackage.Literals.PROPERTY__ID);
      }

      if (propertyItem.hasCode() && propertyItem.isCodeRequired() && property.getCode() == null)
        error(String.format("Property %s must have a code", property.getId()), MonetModelingLanguagePackage.Literals.PROPERTY__ID, CODE_NOT_FOUND);

      if (!propertyItem.hasCode() && property.getCode() != null)
        error(String.format("Code not valid in property %s", property.getId()), MonetModelingLanguagePackage.Literals.PROPERTY__ID);

      if (propertyItem.hasName() && propertyItem.isNameRequired() && property.getName() == null)
        error(String.format("Property %s must have a name", property.getId()), MonetModelingLanguagePackage.Literals.PROPERTY__ID, NAME_NOT_FOUND);

      if (!propertyItem.hasName() && property.getName() != null)
        error(String.format("Name not valid in property %s", property.getId()), MonetModelingLanguagePackage.Literals.PROPERTY__ID);

    } catch (Exception e) {
      error(String.format("Unknown error: %s", e.getMessage()), MonetModelingLanguagePackage.Literals.PROPERTY__ID);
    }
  }

  @Check
  public void checkProject(ProjectModel project) {
    Item manifestItem = this.metaModelStructure.getDefinition("project");

    HashMap<String, Integer> features = this.extractDeclaredItems(project.getFeatures());

    for (Item item : manifestItem.getItems()) {
      Integer instancesOfFeature = features.get(item.getToken());
      if (!item.isMultiple() && instancesOfFeature != null && instancesOfFeature > 1)
        error(String.format("Element %s can't appear more than once", item.getToken()), MonetModelingLanguagePackage.Literals.PROJECT_MODEL__NAME);
    }
  }

  @Check
  public void checkDistribution(DistributionModel distribution) {
    Item setupItem = this.metaModelStructure.getDefinition("distribution");

    HashMap<String, Integer> features = this.extractDeclaredSetupItems(distribution);

    for (Item item : setupItem.getItems()) {
      Integer instancesOfFeature = features.get(item.getToken());
      if (item.isRequired() && instancesOfFeature == null)
        error(String.format("Distribution require the element %s", item.getToken()), MonetModelingLanguagePackage.Literals.DISTRIBUTION_MODEL__NAME);
      if (!item.isMultiple() && instancesOfFeature != null && instancesOfFeature > 1)
        error(String.format("Element %s can't appear more than once", item.getToken()), MonetModelingLanguagePackage.Literals.DISTRIBUTION_MODEL__NAME);
    }
  }

  @Check
  public void checkDefinitions(Definition definition) {
    try {
      if (definition.isExtensible() && definition.isAbstract())
        error("Definition can't be extensible and abstract", MonetModelingLanguagePackage.Literals.DEFINITION__ABSTRACT);
      if (definition.isExtensible() && definition.getReplaceSuperType() != null)
        error("Definition can't be extensible and replace a definition", MonetModelingLanguagePackage.Literals.DEFINITION__REPLACE_SUPER_TYPE);
      if (definition.isAbstract() && definition.getReplaceSuperType() != null)
        error("Definition can't be abstract and replace a definition", MonetModelingLanguagePackage.Literals.DEFINITION__REPLACE_SUPER_TYPE);
      if (definition.getSuperType() != null && definition.getReplaceSuperType() != null)
        error("Definition can't extends and replace a definition, remove one.", MonetModelingLanguagePackage.Literals.DEFINITION__REPLACE_SUPER_TYPE);

      Item definitionItem = this.metaModelStructure.getDefinition(definition.getDefinitionType());

      HashMap<String, Integer> features = this.extractDeclaredDefinitionItems(definition);

      for (Item item : definitionItem.getItems()) {
        Integer instancesOfFeature = features.get(item.getToken());
        if (item.isRequired() && instancesOfFeature == null && !definition.isAbstract())
          error(String.format("Definition require the element %s", item.getToken()), MonetModelingLanguagePackage.Literals.DEFINITION__NAME);
        if (!item.isMultiple() && instancesOfFeature != null && instancesOfFeature > 1)
          error(String.format("Element %s can't appear more than once", item.getToken()), MonetModelingLanguagePackage.Literals.DEFINITION__NAME);
      }

      if (definitionItem.hasCode() && definitionItem.isCodeRequired() && definition.getCode() == null)
        error(String.format("Definition %s must have a code", definition.getName()), MonetModelingLanguagePackage.Literals.DEFINITION__NAME, CODE_NOT_FOUND);

      if (definitionItem.hasName() && definitionItem.isNameRequired() && definition.getName() == null)
        error(String.format("Definition %s must have a name", definition.getName()), MonetModelingLanguagePackage.Literals.DEFINITION__NAME, NAME_NOT_FOUND);
    } catch (Exception e) {
      error(String.format("Unknown error: %s", e.getMessage()), MonetModelingLanguagePackage.Literals.DEFINITION__NAME);
    }
  }

  @Check
  public void checkMethods(Method method) {
    try {
      Item methodItem = XtendHelper.checkElement(method, this.metaModelStructure);

      if (methodItem == null)
        error(String.format("Method '%s' not valid in this context", method.getId()), MonetModelingLanguagePackage.Literals.METHOD__ID);

      // TODO: Check parameters?
    } catch (Exception e) {
      error(String.format("Unknown error: %s", e.getMessage()), MonetModelingLanguagePackage.Literals.METHOD__ID);
    }
  }

  // @Check
  // public void checkGreetingStartsWithCapital(Greeting greeting) {
  // if (!Character.isUpperCase(greeting.getName().charAt(0))) {
  // warning("Name should start with a capital",
  // MyDslPackage.Literals.GREETING__NAME);
  // }
  // }

  @Check
  public void checkDefinitionNameEqualsFilename(Definition definition) {
    if (definition.eContainer() instanceof Definition)
      return;
    String filename = definition.eResource().getURI().trimFileExtension().lastSegment();
    if (!definition.getName().equals(filename))
      error("Definition name must be equal to filename", MonetModelingLanguagePackage.Literals.DEFINITION__NAME);
  }

  @Check
  public void checkPackageNameEqualsPath(PackageDeclaration _package) {
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
    if (!(_package.getName() + ".").equals(path.toString()))
      error("Definition isn't in valid package directory", MonetModelingLanguagePackage.Literals.PACKAGE_DECLARATION__NAME, PACKAGE_FOLDER_NOT_MACTH);
  }

  @Check
  public void checkImports(final Import imp) {
    // don't check wildcard imports
    if (imp.getImportedNamespace().endsWith(".*"))
      return;
    IJvmTypeProvider typeProvider = typeProviderFactory.findOrCreateTypeProvider(imp.eResource().getResourceSet());
    JvmType jvmType = typeProvider.findTypeByName(imp.getImportedNamespace().replaceAll("\\^", ""));
    if (jvmType == null) {
      error("The import " + imp.getImportedNamespace() + " cannot be resolved", MonetModelingLanguagePackage.Literals.IMPORT__IMPORTED_NAMESPACE);
    }
  }
  
  private IResolvedTypes getResolvedTypes(EObject object) {
    IBatchTypeResolver typeResolver = ((XtextResource) object.eResource()).getResourceServiceProvider().get(IBatchTypeResolver.class);
    return typeResolver.resolveTypes(object);
  }
  
  private JvmTypeReference getType(XExpression expression) {
    LightweightTypeReference type = getResolvedTypes(expression).getActualType(expression);
    if (type != null)
      return type.toTypeReference();
    return null;
  }

}
