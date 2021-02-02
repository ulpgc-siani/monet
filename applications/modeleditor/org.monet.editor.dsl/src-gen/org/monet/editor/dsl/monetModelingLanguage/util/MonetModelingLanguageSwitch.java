/**
 */
package org.monet.editor.dsl.monetModelingLanguage.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.monet.editor.dsl.monetModelingLanguage.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage
 * @generated
 */
public class MonetModelingLanguageSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static MonetModelingLanguagePackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MonetModelingLanguageSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = MonetModelingLanguagePackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case MonetModelingLanguagePackage.DOMAIN_MODEL:
      {
        DomainModel domainModel = (DomainModel)theEObject;
        T result = caseDomainModel(domainModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL:
      {
        DistributionModel distributionModel = (DistributionModel)theEObject;
        T result = caseDistributionModel(distributionModel);
        if (result == null) result = caseDomainModel(distributionModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.PROJECT_MODEL:
      {
        ProjectModel projectModel = (ProjectModel)theEObject;
        T result = caseProjectModel(projectModel);
        if (result == null) result = caseDomainModel(projectModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.MANIFEST_FEATURE:
      {
        ManifestFeature manifestFeature = (ManifestFeature)theEObject;
        T result = caseManifestFeature(manifestFeature);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.DEFINITION_MODEL:
      {
        DefinitionModel definitionModel = (DefinitionModel)theEObject;
        T result = caseDefinitionModel(definitionModel);
        if (result == null) result = caseDomainModel(definitionModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.IMPORT:
      {
        Import import_ = (Import)theEObject;
        T result = caseImport(import_);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.CODE:
      {
        Code code = (Code)theEObject;
        T result = caseCode(code);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.PACKAGE_DECLARATION:
      {
        PackageDeclaration packageDeclaration = (PackageDeclaration)theEObject;
        T result = casePackageDeclaration(packageDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.DEFINITION:
      {
        Definition definition = (Definition)theEObject;
        T result = caseDefinition(definition);
        if (result == null) result = caseFeature(definition);
        if (result == null) result = caseReferenciable(definition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.FEATURE:
      {
        Feature feature = (Feature)theEObject;
        T result = caseFeature(feature);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.ATTRIBUTE:
      {
        Attribute attribute = (Attribute)theEObject;
        T result = caseAttribute(attribute);
        if (result == null) result = caseManifestFeature(attribute);
        if (result == null) result = caseFeature(attribute);
        if (result == null) result = casePropertyFeature(attribute);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.ATTRIBUTE_VALUE:
      {
        AttributeValue attributeValue = (AttributeValue)theEObject;
        T result = caseAttributeValue(attributeValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.LOCALIZED_TEXT:
      {
        LocalizedText localizedText = (LocalizedText)theEObject;
        T result = caseLocalizedText(localizedText);
        if (result == null) result = caseAttributeValue(localizedText);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.RESOURCE:
      {
        Resource resource = (Resource)theEObject;
        T result = caseResource(resource);
        if (result == null) result = caseAttributeValue(resource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.STRING_LITERAL:
      {
        StringLiteral stringLiteral = (StringLiteral)theEObject;
        T result = caseStringLiteral(stringLiteral);
        if (result == null) result = caseAttributeValue(stringLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.TIME_LITERAL:
      {
        TimeLiteral timeLiteral = (TimeLiteral)theEObject;
        T result = caseTimeLiteral(timeLiteral);
        if (result == null) result = caseAttributeValue(timeLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.INT_LITERAL:
      {
        IntLiteral intLiteral = (IntLiteral)theEObject;
        T result = caseIntLiteral(intLiteral);
        if (result == null) result = caseAttributeValue(intLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.FLOAT_LITERAL:
      {
        FloatLiteral floatLiteral = (FloatLiteral)theEObject;
        T result = caseFloatLiteral(floatLiteral);
        if (result == null) result = caseAttributeValue(floatLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.DOUBLE_LITERAL:
      {
        DoubleLiteral doubleLiteral = (DoubleLiteral)theEObject;
        T result = caseDoubleLiteral(doubleLiteral);
        if (result == null) result = caseAttributeValue(doubleLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.EXPRESSION_LITERAL:
      {
        ExpressionLiteral expressionLiteral = (ExpressionLiteral)theEObject;
        T result = caseExpressionLiteral(expressionLiteral);
        if (result == null) result = caseAttributeValue(expressionLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.REFERENCIABLE:
      {
        Referenciable referenciable = (Referenciable)theEObject;
        T result = caseReferenciable(referenciable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.XT_REFERENCE:
      {
        XTReference xtReference = (XTReference)theEObject;
        T result = caseXTReference(xtReference);
        if (result == null) result = caseAttributeValue(xtReference);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.ENUM_LITERAL:
      {
        EnumLiteral enumLiteral = (EnumLiteral)theEObject;
        T result = caseEnumLiteral(enumLiteral);
        if (result == null) result = caseAttributeValue(enumLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.PROPERTY:
      {
        Property property = (Property)theEObject;
        T result = caseProperty(property);
        if (result == null) result = caseManifestFeature(property);
        if (result == null) result = caseFeature(property);
        if (result == null) result = caseReferenciable(property);
        if (result == null) result = casePropertyFeature(property);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.PROPERTY_FEATURE:
      {
        PropertyFeature propertyFeature = (PropertyFeature)theEObject;
        T result = casePropertyFeature(propertyFeature);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.METHOD:
      {
        Method method = (Method)theEObject;
        T result = caseMethod(method);
        if (result == null) result = caseManifestFeature(method);
        if (result == null) result = caseFeature(method);
        if (result == null) result = casePropertyFeature(method);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.VARIABLE:
      {
        Variable variable = (Variable)theEObject;
        T result = caseVariable(variable);
        if (result == null) result = caseFeature(variable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.DEFINE:
      {
        Define define = (Define)theEObject;
        T result = caseDefine(define);
        if (result == null) result = caseManifestFeature(define);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.SCHEMA:
      {
        Schema schema = (Schema)theEObject;
        T result = caseSchema(schema);
        if (result == null) result = caseFeature(schema);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.SCHEMA_FEATURE:
      {
        SchemaFeature schemaFeature = (SchemaFeature)theEObject;
        T result = caseSchemaFeature(schemaFeature);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.SCHEMA_SECTION:
      {
        SchemaSection schemaSection = (SchemaSection)theEObject;
        T result = caseSchemaSection(schemaSection);
        if (result == null) result = caseSchemaFeature(schemaSection);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.SCHEMA_PROPERTY:
      {
        SchemaProperty schemaProperty = (SchemaProperty)theEObject;
        T result = caseSchemaProperty(schemaProperty);
        if (result == null) result = caseSchemaFeature(schemaProperty);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.SCHEMA_PROPERTY_OF_VALUE:
      {
        SchemaPropertyOfValue schemaPropertyOfValue = (SchemaPropertyOfValue)theEObject;
        T result = caseSchemaPropertyOfValue(schemaPropertyOfValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MonetModelingLanguagePackage.FUNCTION:
      {
        Function function = (Function)theEObject;
        T result = caseFunction(function);
        if (result == null) result = caseFeature(function);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Domain Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Domain Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDomainModel(DomainModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Distribution Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Distribution Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDistributionModel(DistributionModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectModel(ProjectModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Manifest Feature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Manifest Feature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseManifestFeature(ManifestFeature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Definition Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Definition Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDefinitionModel(DefinitionModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Import</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Import</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseImport(Import object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Code</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Code</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCode(Code object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Package Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Package Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePackageDeclaration(PackageDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDefinition(Definition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Feature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFeature(Feature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Attribute</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Attribute</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAttribute(Attribute object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Attribute Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Attribute Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAttributeValue(AttributeValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Localized Text</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Localized Text</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLocalizedText(LocalizedText object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Resource</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Resource</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseResource(Resource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringLiteral(StringLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Time Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Time Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTimeLiteral(TimeLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Int Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Int Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIntLiteral(IntLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Float Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Float Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFloatLiteral(FloatLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Double Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Double Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDoubleLiteral(DoubleLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpressionLiteral(ExpressionLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Referenciable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Referenciable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseReferenciable(Referenciable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>XT Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>XT Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseXTReference(XTReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Enum Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Enum Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEnumLiteral(EnumLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Property</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Property</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProperty(Property object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Property Feature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Property Feature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePropertyFeature(PropertyFeature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Method</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Method</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMethod(Method object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariable(Variable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Define</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Define</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDefine(Define object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Schema</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Schema</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSchema(Schema object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Schema Feature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Schema Feature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSchemaFeature(SchemaFeature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Schema Section</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Schema Section</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSchemaSection(SchemaSection object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Schema Property</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Schema Property</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSchemaProperty(SchemaProperty object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Schema Property Of Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Schema Property Of Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSchemaPropertyOfValue(SchemaPropertyOfValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Function</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Function</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFunction(Function object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //MonetModelingLanguageSwitch
