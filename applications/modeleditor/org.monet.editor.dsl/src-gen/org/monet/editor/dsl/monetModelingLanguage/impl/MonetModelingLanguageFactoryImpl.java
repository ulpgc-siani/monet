/**
 */
package org.monet.editor.dsl.monetModelingLanguage.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.monet.editor.dsl.monetModelingLanguage.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MonetModelingLanguageFactoryImpl extends EFactoryImpl implements MonetModelingLanguageFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MonetModelingLanguageFactory init()
  {
    try
    {
      MonetModelingLanguageFactory theMonetModelingLanguageFactory = (MonetModelingLanguageFactory)EPackage.Registry.INSTANCE.getEFactory(MonetModelingLanguagePackage.eNS_URI);
      if (theMonetModelingLanguageFactory != null)
      {
        return theMonetModelingLanguageFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new MonetModelingLanguageFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MonetModelingLanguageFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case MonetModelingLanguagePackage.DOMAIN_MODEL: return createDomainModel();
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL: return createDistributionModel();
      case MonetModelingLanguagePackage.PROJECT_MODEL: return createProjectModel();
      case MonetModelingLanguagePackage.MANIFEST_FEATURE: return createManifestFeature();
      case MonetModelingLanguagePackage.DEFINITION_MODEL: return createDefinitionModel();
      case MonetModelingLanguagePackage.IMPORT: return createImport();
      case MonetModelingLanguagePackage.CODE: return createCode();
      case MonetModelingLanguagePackage.PACKAGE_DECLARATION: return createPackageDeclaration();
      case MonetModelingLanguagePackage.DEFINITION: return createDefinition();
      case MonetModelingLanguagePackage.FEATURE: return createFeature();
      case MonetModelingLanguagePackage.ATTRIBUTE: return createAttribute();
      case MonetModelingLanguagePackage.ATTRIBUTE_VALUE: return createAttributeValue();
      case MonetModelingLanguagePackage.LOCALIZED_TEXT: return createLocalizedText();
      case MonetModelingLanguagePackage.RESOURCE: return createResource();
      case MonetModelingLanguagePackage.STRING_LITERAL: return createStringLiteral();
      case MonetModelingLanguagePackage.TIME_LITERAL: return createTimeLiteral();
      case MonetModelingLanguagePackage.INT_LITERAL: return createIntLiteral();
      case MonetModelingLanguagePackage.FLOAT_LITERAL: return createFloatLiteral();
      case MonetModelingLanguagePackage.DOUBLE_LITERAL: return createDoubleLiteral();
      case MonetModelingLanguagePackage.EXPRESSION_LITERAL: return createExpressionLiteral();
      case MonetModelingLanguagePackage.REFERENCIABLE: return createReferenciable();
      case MonetModelingLanguagePackage.XT_REFERENCE: return createXTReference();
      case MonetModelingLanguagePackage.ENUM_LITERAL: return createEnumLiteral();
      case MonetModelingLanguagePackage.PROPERTY: return createProperty();
      case MonetModelingLanguagePackage.PROPERTY_FEATURE: return createPropertyFeature();
      case MonetModelingLanguagePackage.METHOD: return createMethod();
      case MonetModelingLanguagePackage.VARIABLE: return createVariable();
      case MonetModelingLanguagePackage.DEFINE: return createDefine();
      case MonetModelingLanguagePackage.SCHEMA: return createSchema();
      case MonetModelingLanguagePackage.SCHEMA_FEATURE: return createSchemaFeature();
      case MonetModelingLanguagePackage.SCHEMA_SECTION: return createSchemaSection();
      case MonetModelingLanguagePackage.SCHEMA_PROPERTY: return createSchemaProperty();
      case MonetModelingLanguagePackage.SCHEMA_PROPERTY_OF_VALUE: return createSchemaPropertyOfValue();
      case MonetModelingLanguagePackage.FUNCTION: return createFunction();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DomainModel createDomainModel()
  {
    DomainModelImpl domainModel = new DomainModelImpl();
    return domainModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DistributionModel createDistributionModel()
  {
    DistributionModelImpl distributionModel = new DistributionModelImpl();
    return distributionModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectModel createProjectModel()
  {
    ProjectModelImpl projectModel = new ProjectModelImpl();
    return projectModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ManifestFeature createManifestFeature()
  {
    ManifestFeatureImpl manifestFeature = new ManifestFeatureImpl();
    return manifestFeature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DefinitionModel createDefinitionModel()
  {
    DefinitionModelImpl definitionModel = new DefinitionModelImpl();
    return definitionModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Import createImport()
  {
    ImportImpl import_ = new ImportImpl();
    return import_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Code createCode()
  {
    CodeImpl code = new CodeImpl();
    return code;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PackageDeclaration createPackageDeclaration()
  {
    PackageDeclarationImpl packageDeclaration = new PackageDeclarationImpl();
    return packageDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition createDefinition()
  {
    DefinitionImpl definition = new DefinitionImpl();
    return definition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Feature createFeature()
  {
    FeatureImpl feature = new FeatureImpl();
    return feature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Attribute createAttribute()
  {
    AttributeImpl attribute = new AttributeImpl();
    return attribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttributeValue createAttributeValue()
  {
    AttributeValueImpl attributeValue = new AttributeValueImpl();
    return attributeValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LocalizedText createLocalizedText()
  {
    LocalizedTextImpl localizedText = new LocalizedTextImpl();
    return localizedText;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Resource createResource()
  {
    ResourceImpl resource = new ResourceImpl();
    return resource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StringLiteral createStringLiteral()
  {
    StringLiteralImpl stringLiteral = new StringLiteralImpl();
    return stringLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TimeLiteral createTimeLiteral()
  {
    TimeLiteralImpl timeLiteral = new TimeLiteralImpl();
    return timeLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IntLiteral createIntLiteral()
  {
    IntLiteralImpl intLiteral = new IntLiteralImpl();
    return intLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FloatLiteral createFloatLiteral()
  {
    FloatLiteralImpl floatLiteral = new FloatLiteralImpl();
    return floatLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DoubleLiteral createDoubleLiteral()
  {
    DoubleLiteralImpl doubleLiteral = new DoubleLiteralImpl();
    return doubleLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpressionLiteral createExpressionLiteral()
  {
    ExpressionLiteralImpl expressionLiteral = new ExpressionLiteralImpl();
    return expressionLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Referenciable createReferenciable()
  {
    ReferenciableImpl referenciable = new ReferenciableImpl();
    return referenciable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XTReference createXTReference()
  {
    XTReferenceImpl xtReference = new XTReferenceImpl();
    return xtReference;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EnumLiteral createEnumLiteral()
  {
    EnumLiteralImpl enumLiteral = new EnumLiteralImpl();
    return enumLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Property createProperty()
  {
    PropertyImpl property = new PropertyImpl();
    return property;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PropertyFeature createPropertyFeature()
  {
    PropertyFeatureImpl propertyFeature = new PropertyFeatureImpl();
    return propertyFeature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Method createMethod()
  {
    MethodImpl method = new MethodImpl();
    return method;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable createVariable()
  {
    VariableImpl variable = new VariableImpl();
    return variable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Define createDefine()
  {
    DefineImpl define = new DefineImpl();
    return define;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Schema createSchema()
  {
    SchemaImpl schema = new SchemaImpl();
    return schema;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SchemaFeature createSchemaFeature()
  {
    SchemaFeatureImpl schemaFeature = new SchemaFeatureImpl();
    return schemaFeature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SchemaSection createSchemaSection()
  {
    SchemaSectionImpl schemaSection = new SchemaSectionImpl();
    return schemaSection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SchemaProperty createSchemaProperty()
  {
    SchemaPropertyImpl schemaProperty = new SchemaPropertyImpl();
    return schemaProperty;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SchemaPropertyOfValue createSchemaPropertyOfValue()
  {
    SchemaPropertyOfValueImpl schemaPropertyOfValue = new SchemaPropertyOfValueImpl();
    return schemaPropertyOfValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Function createFunction()
  {
    FunctionImpl function = new FunctionImpl();
    return function;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MonetModelingLanguagePackage getMonetModelingLanguagePackage()
  {
    return (MonetModelingLanguagePackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static MonetModelingLanguagePackage getPackage()
  {
    return MonetModelingLanguagePackage.eINSTANCE;
  }

} //MonetModelingLanguageFactoryImpl
