/**
 */
package org.monet.editor.dsl.monetModelingLanguage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguageFactory
 * @model kind="package"
 * @generated
 */
public interface MonetModelingLanguagePackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "monetModelingLanguage";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.monet.org/editor/dsl/MonetModelingLanguage";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "monetModelingLanguage";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MonetModelingLanguagePackage eINSTANCE = org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl.init();

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DomainModelImpl <em>Domain Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.DomainModelImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDomainModel()
   * @generated
   */
  int DOMAIN_MODEL = 0;

  /**
   * The number of structural features of the '<em>Domain Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOMAIN_MODEL_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DistributionModelImpl <em>Distribution Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.DistributionModelImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDistributionModel()
   * @generated
   */
  int DISTRIBUTION_MODEL = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DISTRIBUTION_MODEL__NAME = DOMAIN_MODEL_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Super Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DISTRIBUTION_MODEL__SUPER_TYPE = DOMAIN_MODEL_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Features</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DISTRIBUTION_MODEL__FEATURES = DOMAIN_MODEL_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Distribution Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DISTRIBUTION_MODEL_FEATURE_COUNT = DOMAIN_MODEL_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ProjectModelImpl <em>Project Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.ProjectModelImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getProjectModel()
   * @generated
   */
  int PROJECT_MODEL = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_MODEL__NAME = DOMAIN_MODEL_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Features</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_MODEL__FEATURES = DOMAIN_MODEL_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Project Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_MODEL_FEATURE_COUNT = DOMAIN_MODEL_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ManifestFeatureImpl <em>Manifest Feature</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.ManifestFeatureImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getManifestFeature()
   * @generated
   */
  int MANIFEST_FEATURE = 3;

  /**
   * The number of structural features of the '<em>Manifest Feature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MANIFEST_FEATURE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionModelImpl <em>Definition Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionModelImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDefinitionModel()
   * @generated
   */
  int DEFINITION_MODEL = 4;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION_MODEL__ELEMENTS = DOMAIN_MODEL_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Definition Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION_MODEL_FEATURE_COUNT = DOMAIN_MODEL_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ImportImpl <em>Import</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.ImportImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getImport()
   * @generated
   */
  int IMPORT = 5;

  /**
   * The feature id for the '<em><b>Imported Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__IMPORTED_NAMESPACE = 0;

  /**
   * The number of structural features of the '<em>Import</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.CodeImpl <em>Code</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.CodeImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getCode()
   * @generated
   */
  int CODE = 6;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CODE__VALUE = 0;

  /**
   * The number of structural features of the '<em>Code</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CODE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.PackageDeclarationImpl <em>Package Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.PackageDeclarationImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getPackageDeclaration()
   * @generated
   */
  int PACKAGE_DECLARATION = 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PACKAGE_DECLARATION__NAME = 0;

  /**
   * The feature id for the '<em><b>Definition</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PACKAGE_DECLARATION__DEFINITION = 1;

  /**
   * The number of structural features of the '<em>Package Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PACKAGE_DECLARATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.FeatureImpl <em>Feature</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.FeatureImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getFeature()
   * @generated
   */
  int FEATURE = 9;

  /**
   * The number of structural features of the '<em>Feature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FEATURE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl <em>Definition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDefinition()
   * @generated
   */
  int DEFINITION = 8;

  /**
   * The feature id for the '<em><b>Code</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__CODE = FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__ABSTRACT = FEATURE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Extensible</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__EXTENSIBLE = FEATURE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__NAME = FEATURE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Definition Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__DEFINITION_TYPE = FEATURE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Super Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__SUPER_TYPE = FEATURE_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Replace Super Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__REPLACE_SUPER_TYPE = FEATURE_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Features</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__FEATURES = FEATURE_FEATURE_COUNT + 7;

  /**
   * The number of structural features of the '<em>Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION_FEATURE_COUNT = FEATURE_FEATURE_COUNT + 8;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.AttributeImpl <em>Attribute</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.AttributeImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getAttribute()
   * @generated
   */
  int ATTRIBUTE = 10;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE__ID = MANIFEST_FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE__VALUE = MANIFEST_FEATURE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Attribute</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_FEATURE_COUNT = MANIFEST_FEATURE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.AttributeValueImpl <em>Attribute Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.AttributeValueImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getAttributeValue()
   * @generated
   */
  int ATTRIBUTE_VALUE = 11;

  /**
   * The number of structural features of the '<em>Attribute Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_VALUE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.LocalizedTextImpl <em>Localized Text</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.LocalizedTextImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getLocalizedText()
   * @generated
   */
  int LOCALIZED_TEXT = 12;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCALIZED_TEXT__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Localized Text</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOCALIZED_TEXT_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ResourceImpl <em>Resource</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.ResourceImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getResource()
   * @generated
   */
  int RESOURCE = 13;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE__TYPE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Resource</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.StringLiteralImpl <em>String Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.StringLiteralImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getStringLiteral()
   * @generated
   */
  int STRING_LITERAL = 14;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_LITERAL__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>String Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_LITERAL_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.TimeLiteralImpl <em>Time Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.TimeLiteralImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getTimeLiteral()
   * @generated
   */
  int TIME_LITERAL = 15;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Time Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIME_LITERAL_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.IntLiteralImpl <em>Int Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.IntLiteralImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getIntLiteral()
   * @generated
   */
  int INT_LITERAL = 16;

  /**
   * The feature id for the '<em><b>Negative</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INT_LITERAL__NEGATIVE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INT_LITERAL__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Int Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INT_LITERAL_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.FloatLiteralImpl <em>Float Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.FloatLiteralImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getFloatLiteral()
   * @generated
   */
  int FLOAT_LITERAL = 17;

  /**
   * The feature id for the '<em><b>Negative</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FLOAT_LITERAL__NEGATIVE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FLOAT_LITERAL__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Float Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FLOAT_LITERAL_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DoubleLiteralImpl <em>Double Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.DoubleLiteralImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDoubleLiteral()
   * @generated
   */
  int DOUBLE_LITERAL = 18;

  /**
   * The feature id for the '<em><b>Negative</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_LITERAL__NEGATIVE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_LITERAL__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Double Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_LITERAL_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ExpressionLiteralImpl <em>Expression Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.ExpressionLiteralImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getExpressionLiteral()
   * @generated
   */
  int EXPRESSION_LITERAL = 19;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_LITERAL__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Expression Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_LITERAL_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ReferenciableImpl <em>Referenciable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.ReferenciableImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getReferenciable()
   * @generated
   */
  int REFERENCIABLE = 20;

  /**
   * The number of structural features of the '<em>Referenciable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCIABLE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.XTReferenceImpl <em>XT Reference</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.XTReferenceImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getXTReference()
   * @generated
   */
  int XT_REFERENCE = 21;

  /**
   * The feature id for the '<em><b>Value</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XT_REFERENCE__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>XT Reference</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XT_REFERENCE_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.EnumLiteralImpl <em>Enum Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.EnumLiteralImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getEnumLiteral()
   * @generated
   */
  int ENUM_LITERAL = 22;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_LITERAL__VALUE = ATTRIBUTE_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Enum Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_LITERAL_FEATURE_COUNT = ATTRIBUTE_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.PropertyImpl <em>Property</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.PropertyImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getProperty()
   * @generated
   */
  int PROPERTY = 23;

  /**
   * The feature id for the '<em><b>Code</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__CODE = MANIFEST_FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ID = MANIFEST_FEATURE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__NAME = MANIFEST_FEATURE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Features</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__FEATURES = MANIFEST_FEATURE_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_FEATURE_COUNT = MANIFEST_FEATURE_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.PropertyFeatureImpl <em>Property Feature</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.PropertyFeatureImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getPropertyFeature()
   * @generated
   */
  int PROPERTY_FEATURE = 24;

  /**
   * The number of structural features of the '<em>Property Feature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_FEATURE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.MethodImpl <em>Method</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MethodImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getMethod()
   * @generated
   */
  int METHOD = 25;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD__ID = MANIFEST_FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD__PARAMS = MANIFEST_FEATURE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD__BODY = MANIFEST_FEATURE_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Method</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_FEATURE_COUNT = MANIFEST_FEATURE_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.VariableImpl <em>Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.VariableImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getVariable()
   * @generated
   */
  int VARIABLE = 26;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__NAME = FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__TYPE = FEATURE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_FEATURE_COUNT = FEATURE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefineImpl <em>Define</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.DefineImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDefine()
   * @generated
   */
  int DEFINE = 27;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINE__NAME = MANIFEST_FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINE__VALUE = MANIFEST_FEATURE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Define</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINE_FEATURE_COUNT = MANIFEST_FEATURE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaImpl <em>Schema</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchema()
   * @generated
   */
  int SCHEMA = 28;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__ID = FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA__PROPERTIES = FEATURE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Schema</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_FEATURE_COUNT = FEATURE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaFeatureImpl <em>Schema Feature</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaFeatureImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaFeature()
   * @generated
   */
  int SCHEMA_FEATURE = 29;

  /**
   * The feature id for the '<em><b>Many</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_FEATURE__MANY = 0;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_FEATURE__ID = 1;

  /**
   * The number of structural features of the '<em>Schema Feature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_FEATURE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaSectionImpl <em>Schema Section</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaSectionImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaSection()
   * @generated
   */
  int SCHEMA_SECTION = 30;

  /**
   * The feature id for the '<em><b>Many</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_SECTION__MANY = SCHEMA_FEATURE__MANY;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_SECTION__ID = SCHEMA_FEATURE__ID;

  /**
   * The feature id for the '<em><b>Features</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_SECTION__FEATURES = SCHEMA_FEATURE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Schema Section</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_SECTION_FEATURE_COUNT = SCHEMA_FEATURE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyImpl <em>Schema Property</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaProperty()
   * @generated
   */
  int SCHEMA_PROPERTY = 31;

  /**
   * The feature id for the '<em><b>Many</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_PROPERTY__MANY = SCHEMA_FEATURE__MANY;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_PROPERTY__ID = SCHEMA_FEATURE__ID;

  /**
   * The feature id for the '<em><b>Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_PROPERTY__SOURCE = SCHEMA_FEATURE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Schema Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_PROPERTY_FEATURE_COUNT = SCHEMA_FEATURE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyOfValueImpl <em>Schema Property Of Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyOfValueImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaPropertyOfValue()
   * @generated
   */
  int SCHEMA_PROPERTY_OF_VALUE = 32;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_PROPERTY_OF_VALUE__TYPE = 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_PROPERTY_OF_VALUE__BODY = 1;

  /**
   * The number of structural features of the '<em>Schema Property Of Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCHEMA_PROPERTY_OF_VALUE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.FunctionImpl <em>Function</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.FunctionImpl
   * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getFunction()
   * @generated
   */
  int FUNCTION = 33;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION__NAME = FEATURE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION__PARAMS = FEATURE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION__TYPE = FEATURE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION__BODY = FEATURE_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Function</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_FEATURE_COUNT = FEATURE_FEATURE_COUNT + 4;


  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.DomainModel <em>Domain Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Domain Model</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DomainModel
   * @generated
   */
  EClass getDomainModel();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel <em>Distribution Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Distribution Model</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DistributionModel
   * @generated
   */
  EClass getDistributionModel();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getName()
   * @see #getDistributionModel()
   * @generated
   */
  EAttribute getDistributionModel_Name();

  /**
   * Returns the meta object for the reference '{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getSuperType <em>Super Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Super Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getSuperType()
   * @see #getDistributionModel()
   * @generated
   */
  EReference getDistributionModel_SuperType();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getFeatures <em>Features</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Features</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getFeatures()
   * @see #getDistributionModel()
   * @generated
   */
  EReference getDistributionModel_Features();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.ProjectModel <em>Project Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project Model</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.ProjectModel
   * @generated
   */
  EClass getProjectModel();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.ProjectModel#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.ProjectModel#getName()
   * @see #getProjectModel()
   * @generated
   */
  EAttribute getProjectModel_Name();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.ProjectModel#getFeatures <em>Features</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Features</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.ProjectModel#getFeatures()
   * @see #getProjectModel()
   * @generated
   */
  EReference getProjectModel_Features();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.ManifestFeature <em>Manifest Feature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Manifest Feature</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.ManifestFeature
   * @generated
   */
  EClass getManifestFeature();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.DefinitionModel <em>Definition Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Definition Model</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DefinitionModel
   * @generated
   */
  EClass getDefinitionModel();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.DefinitionModel#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DefinitionModel#getElements()
   * @see #getDefinitionModel()
   * @generated
   */
  EReference getDefinitionModel_Elements();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Import</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Import
   * @generated
   */
  EClass getImport();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Import#getImportedNamespace <em>Imported Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Imported Namespace</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Import#getImportedNamespace()
   * @see #getImport()
   * @generated
   */
  EAttribute getImport_ImportedNamespace();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Code <em>Code</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Code</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Code
   * @generated
   */
  EClass getCode();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Code#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Code#getValue()
   * @see #getCode()
   * @generated
   */
  EAttribute getCode_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration <em>Package Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Package Declaration</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration
   * @generated
   */
  EClass getPackageDeclaration();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration#getName()
   * @see #getPackageDeclaration()
   * @generated
   */
  EAttribute getPackageDeclaration_Name();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration#getDefinition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Definition</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.PackageDeclaration#getDefinition()
   * @see #getPackageDeclaration()
   * @generated
   */
  EReference getPackageDeclaration_Definition();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Definition</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition
   * @generated
   */
  EClass getDefinition();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getCode <em>Code</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Code</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#getCode()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_Code();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#isAbstract <em>Abstract</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Abstract</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#isAbstract()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_Abstract();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#isExtensible <em>Extensible</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Extensible</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#isExtensible()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_Extensible();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#getName()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_Name();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getDefinitionType <em>Definition Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Definition Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#getDefinitionType()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_DefinitionType();

  /**
   * Returns the meta object for the reference '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getSuperType <em>Super Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Super Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#getSuperType()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_SuperType();

  /**
   * Returns the meta object for the reference '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getReplaceSuperType <em>Replace Super Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Replace Super Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#getReplaceSuperType()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_ReplaceSuperType();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getFeatures <em>Features</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Features</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Definition#getFeatures()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_Features();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Feature <em>Feature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Feature</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Feature
   * @generated
   */
  EClass getFeature();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Attribute <em>Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Attribute</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Attribute
   * @generated
   */
  EClass getAttribute();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Attribute#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Attribute#getId()
   * @see #getAttribute()
   * @generated
   */
  EAttribute getAttribute_Id();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.Attribute#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Attribute#getValue()
   * @see #getAttribute()
   * @generated
   */
  EReference getAttribute_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.AttributeValue <em>Attribute Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Attribute Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.AttributeValue
   * @generated
   */
  EClass getAttributeValue();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.LocalizedText <em>Localized Text</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Localized Text</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.LocalizedText
   * @generated
   */
  EClass getLocalizedText();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.LocalizedText#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.LocalizedText#getValue()
   * @see #getLocalizedText()
   * @generated
   */
  EAttribute getLocalizedText_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Resource <em>Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Resource</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Resource
   * @generated
   */
  EClass getResource();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Resource#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Resource#getType()
   * @see #getResource()
   * @generated
   */
  EAttribute getResource_Type();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Resource#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Resource#getValue()
   * @see #getResource()
   * @generated
   */
  EAttribute getResource_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.StringLiteral <em>String Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Literal</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.StringLiteral
   * @generated
   */
  EClass getStringLiteral();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.StringLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.StringLiteral#getValue()
   * @see #getStringLiteral()
   * @generated
   */
  EAttribute getStringLiteral_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.TimeLiteral <em>Time Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Time Literal</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.TimeLiteral
   * @generated
   */
  EClass getTimeLiteral();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.TimeLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.TimeLiteral#getValue()
   * @see #getTimeLiteral()
   * @generated
   */
  EAttribute getTimeLiteral_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.IntLiteral <em>Int Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Int Literal</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.IntLiteral
   * @generated
   */
  EClass getIntLiteral();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.IntLiteral#isNegative <em>Negative</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Negative</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.IntLiteral#isNegative()
   * @see #getIntLiteral()
   * @generated
   */
  EAttribute getIntLiteral_Negative();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.IntLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.IntLiteral#getValue()
   * @see #getIntLiteral()
   * @generated
   */
  EAttribute getIntLiteral_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.FloatLiteral <em>Float Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Float Literal</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.FloatLiteral
   * @generated
   */
  EClass getFloatLiteral();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#isNegative <em>Negative</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Negative</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#isNegative()
   * @see #getFloatLiteral()
   * @generated
   */
  EAttribute getFloatLiteral_Negative();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#getValue()
   * @see #getFloatLiteral()
   * @generated
   */
  EAttribute getFloatLiteral_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral <em>Double Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Double Literal</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral
   * @generated
   */
  EClass getDoubleLiteral();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral#isNegative <em>Negative</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Negative</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral#isNegative()
   * @see #getDoubleLiteral()
   * @generated
   */
  EAttribute getDoubleLiteral_Negative();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral#getValue()
   * @see #getDoubleLiteral()
   * @generated
   */
  EAttribute getDoubleLiteral_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral <em>Expression Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Expression Literal</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral
   * @generated
   */
  EClass getExpressionLiteral();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.ExpressionLiteral#getValue()
   * @see #getExpressionLiteral()
   * @generated
   */
  EReference getExpressionLiteral_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Referenciable <em>Referenciable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Referenciable</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Referenciable
   * @generated
   */
  EClass getReferenciable();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.XTReference <em>XT Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>XT Reference</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.XTReference
   * @generated
   */
  EClass getXTReference();

  /**
   * Returns the meta object for the reference '{@link org.monet.editor.dsl.monetModelingLanguage.XTReference#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.XTReference#getValue()
   * @see #getXTReference()
   * @generated
   */
  EReference getXTReference_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.EnumLiteral <em>Enum Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Enum Literal</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.EnumLiteral
   * @generated
   */
  EClass getEnumLiteral();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.EnumLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.EnumLiteral#getValue()
   * @see #getEnumLiteral()
   * @generated
   */
  EAttribute getEnumLiteral_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Property <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Property
   * @generated
   */
  EClass getProperty();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.Property#getCode <em>Code</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Code</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Property#getCode()
   * @see #getProperty()
   * @generated
   */
  EReference getProperty_Code();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Property#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Property#getId()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_Id();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Property#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Property#getName()
   * @see #getProperty()
   * @generated
   */
  EAttribute getProperty_Name();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.Property#getFeatures <em>Features</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Features</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Property#getFeatures()
   * @see #getProperty()
   * @generated
   */
  EReference getProperty_Features();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.PropertyFeature <em>Property Feature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property Feature</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.PropertyFeature
   * @generated
   */
  EClass getPropertyFeature();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Method <em>Method</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Method</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Method
   * @generated
   */
  EClass getMethod();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Method#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Method#getId()
   * @see #getMethod()
   * @generated
   */
  EAttribute getMethod_Id();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.Method#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Method#getParams()
   * @see #getMethod()
   * @generated
   */
  EReference getMethod_Params();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.Method#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Method#getBody()
   * @see #getMethod()
   * @generated
   */
  EReference getMethod_Body();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Variable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Variable</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Variable
   * @generated
   */
  EClass getVariable();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Variable#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Variable#getName()
   * @see #getVariable()
   * @generated
   */
  EAttribute getVariable_Name();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.Variable#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Variable#getType()
   * @see #getVariable()
   * @generated
   */
  EReference getVariable_Type();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Define <em>Define</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Define</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Define
   * @generated
   */
  EClass getDefine();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Define#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Define#getName()
   * @see #getDefine()
   * @generated
   */
  EAttribute getDefine_Name();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Define#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Define#getValue()
   * @see #getDefine()
   * @generated
   */
  EAttribute getDefine_Value();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Schema <em>Schema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Schema</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Schema
   * @generated
   */
  EClass getSchema();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Schema#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Schema#getId()
   * @see #getSchema()
   * @generated
   */
  EAttribute getSchema_Id();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.Schema#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Properties</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Schema#getProperties()
   * @see #getSchema()
   * @generated
   */
  EReference getSchema_Properties();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaFeature <em>Schema Feature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Schema Feature</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaFeature
   * @generated
   */
  EClass getSchemaFeature();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaFeature#isMany <em>Many</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Many</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaFeature#isMany()
   * @see #getSchemaFeature()
   * @generated
   */
  EAttribute getSchemaFeature_Many();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaFeature#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaFeature#getId()
   * @see #getSchemaFeature()
   * @generated
   */
  EAttribute getSchemaFeature_Id();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaSection <em>Schema Section</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Schema Section</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaSection
   * @generated
   */
  EClass getSchemaSection();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaSection#getFeatures <em>Features</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Features</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaSection#getFeatures()
   * @see #getSchemaSection()
   * @generated
   */
  EReference getSchemaSection_Features();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaProperty <em>Schema Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Schema Property</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaProperty
   * @generated
   */
  EClass getSchemaProperty();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaProperty#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Source</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaProperty#getSource()
   * @see #getSchemaProperty()
   * @generated
   */
  EReference getSchemaProperty_Source();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue <em>Schema Property Of Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Schema Property Of Value</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue
   * @generated
   */
  EClass getSchemaPropertyOfValue();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue#getType()
   * @see #getSchemaPropertyOfValue()
   * @generated
   */
  EReference getSchemaPropertyOfValue_Type();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.SchemaPropertyOfValue#getBody()
   * @see #getSchemaPropertyOfValue()
   * @generated
   */
  EReference getSchemaPropertyOfValue_Body();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetModelingLanguage.Function <em>Function</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Function
   * @generated
   */
  EClass getFunction();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetModelingLanguage.Function#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Function#getName()
   * @see #getFunction()
   * @generated
   */
  EAttribute getFunction_Name();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetModelingLanguage.Function#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Function#getParams()
   * @see #getFunction()
   * @generated
   */
  EReference getFunction_Params();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.Function#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Function#getType()
   * @see #getFunction()
   * @generated
   */
  EReference getFunction_Type();

  /**
   * Returns the meta object for the containment reference '{@link org.monet.editor.dsl.monetModelingLanguage.Function#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see org.monet.editor.dsl.monetModelingLanguage.Function#getBody()
   * @see #getFunction()
   * @generated
   */
  EReference getFunction_Body();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  MonetModelingLanguageFactory getMonetModelingLanguageFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DomainModelImpl <em>Domain Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.DomainModelImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDomainModel()
     * @generated
     */
    EClass DOMAIN_MODEL = eINSTANCE.getDomainModel();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DistributionModelImpl <em>Distribution Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.DistributionModelImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDistributionModel()
     * @generated
     */
    EClass DISTRIBUTION_MODEL = eINSTANCE.getDistributionModel();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DISTRIBUTION_MODEL__NAME = eINSTANCE.getDistributionModel_Name();

    /**
     * The meta object literal for the '<em><b>Super Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DISTRIBUTION_MODEL__SUPER_TYPE = eINSTANCE.getDistributionModel_SuperType();

    /**
     * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DISTRIBUTION_MODEL__FEATURES = eINSTANCE.getDistributionModel_Features();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ProjectModelImpl <em>Project Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.ProjectModelImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getProjectModel()
     * @generated
     */
    EClass PROJECT_MODEL = eINSTANCE.getProjectModel();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROJECT_MODEL__NAME = eINSTANCE.getProjectModel_Name();

    /**
     * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT_MODEL__FEATURES = eINSTANCE.getProjectModel_Features();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ManifestFeatureImpl <em>Manifest Feature</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.ManifestFeatureImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getManifestFeature()
     * @generated
     */
    EClass MANIFEST_FEATURE = eINSTANCE.getManifestFeature();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionModelImpl <em>Definition Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionModelImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDefinitionModel()
     * @generated
     */
    EClass DEFINITION_MODEL = eINSTANCE.getDefinitionModel();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION_MODEL__ELEMENTS = eINSTANCE.getDefinitionModel_Elements();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ImportImpl <em>Import</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.ImportImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getImport()
     * @generated
     */
    EClass IMPORT = eINSTANCE.getImport();

    /**
     * The meta object literal for the '<em><b>Imported Namespace</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPORT__IMPORTED_NAMESPACE = eINSTANCE.getImport_ImportedNamespace();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.CodeImpl <em>Code</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.CodeImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getCode()
     * @generated
     */
    EClass CODE = eINSTANCE.getCode();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CODE__VALUE = eINSTANCE.getCode_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.PackageDeclarationImpl <em>Package Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.PackageDeclarationImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getPackageDeclaration()
     * @generated
     */
    EClass PACKAGE_DECLARATION = eINSTANCE.getPackageDeclaration();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PACKAGE_DECLARATION__NAME = eINSTANCE.getPackageDeclaration_Name();

    /**
     * The meta object literal for the '<em><b>Definition</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PACKAGE_DECLARATION__DEFINITION = eINSTANCE.getPackageDeclaration_Definition();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl <em>Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDefinition()
     * @generated
     */
    EClass DEFINITION = eINSTANCE.getDefinition();

    /**
     * The meta object literal for the '<em><b>Code</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__CODE = eINSTANCE.getDefinition_Code();

    /**
     * The meta object literal for the '<em><b>Abstract</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__ABSTRACT = eINSTANCE.getDefinition_Abstract();

    /**
     * The meta object literal for the '<em><b>Extensible</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__EXTENSIBLE = eINSTANCE.getDefinition_Extensible();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__NAME = eINSTANCE.getDefinition_Name();

    /**
     * The meta object literal for the '<em><b>Definition Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__DEFINITION_TYPE = eINSTANCE.getDefinition_DefinitionType();

    /**
     * The meta object literal for the '<em><b>Super Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__SUPER_TYPE = eINSTANCE.getDefinition_SuperType();

    /**
     * The meta object literal for the '<em><b>Replace Super Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__REPLACE_SUPER_TYPE = eINSTANCE.getDefinition_ReplaceSuperType();

    /**
     * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__FEATURES = eINSTANCE.getDefinition_Features();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.FeatureImpl <em>Feature</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.FeatureImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getFeature()
     * @generated
     */
    EClass FEATURE = eINSTANCE.getFeature();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.AttributeImpl <em>Attribute</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.AttributeImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getAttribute()
     * @generated
     */
    EClass ATTRIBUTE = eINSTANCE.getAttribute();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE__ID = eINSTANCE.getAttribute_Id();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ATTRIBUTE__VALUE = eINSTANCE.getAttribute_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.AttributeValueImpl <em>Attribute Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.AttributeValueImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getAttributeValue()
     * @generated
     */
    EClass ATTRIBUTE_VALUE = eINSTANCE.getAttributeValue();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.LocalizedTextImpl <em>Localized Text</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.LocalizedTextImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getLocalizedText()
     * @generated
     */
    EClass LOCALIZED_TEXT = eINSTANCE.getLocalizedText();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LOCALIZED_TEXT__VALUE = eINSTANCE.getLocalizedText_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ResourceImpl <em>Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.ResourceImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getResource()
     * @generated
     */
    EClass RESOURCE = eINSTANCE.getResource();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE__TYPE = eINSTANCE.getResource_Type();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE__VALUE = eINSTANCE.getResource_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.StringLiteralImpl <em>String Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.StringLiteralImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getStringLiteral()
     * @generated
     */
    EClass STRING_LITERAL = eINSTANCE.getStringLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_LITERAL__VALUE = eINSTANCE.getStringLiteral_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.TimeLiteralImpl <em>Time Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.TimeLiteralImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getTimeLiteral()
     * @generated
     */
    EClass TIME_LITERAL = eINSTANCE.getTimeLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TIME_LITERAL__VALUE = eINSTANCE.getTimeLiteral_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.IntLiteralImpl <em>Int Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.IntLiteralImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getIntLiteral()
     * @generated
     */
    EClass INT_LITERAL = eINSTANCE.getIntLiteral();

    /**
     * The meta object literal for the '<em><b>Negative</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INT_LITERAL__NEGATIVE = eINSTANCE.getIntLiteral_Negative();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INT_LITERAL__VALUE = eINSTANCE.getIntLiteral_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.FloatLiteralImpl <em>Float Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.FloatLiteralImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getFloatLiteral()
     * @generated
     */
    EClass FLOAT_LITERAL = eINSTANCE.getFloatLiteral();

    /**
     * The meta object literal for the '<em><b>Negative</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FLOAT_LITERAL__NEGATIVE = eINSTANCE.getFloatLiteral_Negative();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FLOAT_LITERAL__VALUE = eINSTANCE.getFloatLiteral_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DoubleLiteralImpl <em>Double Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.DoubleLiteralImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDoubleLiteral()
     * @generated
     */
    EClass DOUBLE_LITERAL = eINSTANCE.getDoubleLiteral();

    /**
     * The meta object literal for the '<em><b>Negative</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOUBLE_LITERAL__NEGATIVE = eINSTANCE.getDoubleLiteral_Negative();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOUBLE_LITERAL__VALUE = eINSTANCE.getDoubleLiteral_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ExpressionLiteralImpl <em>Expression Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.ExpressionLiteralImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getExpressionLiteral()
     * @generated
     */
    EClass EXPRESSION_LITERAL = eINSTANCE.getExpressionLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION_LITERAL__VALUE = eINSTANCE.getExpressionLiteral_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.ReferenciableImpl <em>Referenciable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.ReferenciableImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getReferenciable()
     * @generated
     */
    EClass REFERENCIABLE = eINSTANCE.getReferenciable();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.XTReferenceImpl <em>XT Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.XTReferenceImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getXTReference()
     * @generated
     */
    EClass XT_REFERENCE = eINSTANCE.getXTReference();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference XT_REFERENCE__VALUE = eINSTANCE.getXTReference_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.EnumLiteralImpl <em>Enum Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.EnumLiteralImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getEnumLiteral()
     * @generated
     */
    EClass ENUM_LITERAL = eINSTANCE.getEnumLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENUM_LITERAL__VALUE = eINSTANCE.getEnumLiteral_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.PropertyImpl <em>Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.PropertyImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getProperty()
     * @generated
     */
    EClass PROPERTY = eINSTANCE.getProperty();

    /**
     * The meta object literal for the '<em><b>Code</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY__CODE = eINSTANCE.getProperty_Code();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__ID = eINSTANCE.getProperty_Id();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PROPERTY__NAME = eINSTANCE.getProperty_Name();

    /**
     * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY__FEATURES = eINSTANCE.getProperty_Features();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.PropertyFeatureImpl <em>Property Feature</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.PropertyFeatureImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getPropertyFeature()
     * @generated
     */
    EClass PROPERTY_FEATURE = eINSTANCE.getPropertyFeature();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.MethodImpl <em>Method</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MethodImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getMethod()
     * @generated
     */
    EClass METHOD = eINSTANCE.getMethod();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METHOD__ID = eINSTANCE.getMethod_Id();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHOD__PARAMS = eINSTANCE.getMethod_Params();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHOD__BODY = eINSTANCE.getMethod_Body();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.VariableImpl <em>Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.VariableImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getVariable()
     * @generated
     */
    EClass VARIABLE = eINSTANCE.getVariable();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE__NAME = eINSTANCE.getVariable_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VARIABLE__TYPE = eINSTANCE.getVariable_Type();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefineImpl <em>Define</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.DefineImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getDefine()
     * @generated
     */
    EClass DEFINE = eINSTANCE.getDefine();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINE__NAME = eINSTANCE.getDefine_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINE__VALUE = eINSTANCE.getDefine_Value();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaImpl <em>Schema</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchema()
     * @generated
     */
    EClass SCHEMA = eINSTANCE.getSchema();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SCHEMA__ID = eINSTANCE.getSchema_Id();

    /**
     * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA__PROPERTIES = eINSTANCE.getSchema_Properties();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaFeatureImpl <em>Schema Feature</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaFeatureImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaFeature()
     * @generated
     */
    EClass SCHEMA_FEATURE = eINSTANCE.getSchemaFeature();

    /**
     * The meta object literal for the '<em><b>Many</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SCHEMA_FEATURE__MANY = eINSTANCE.getSchemaFeature_Many();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SCHEMA_FEATURE__ID = eINSTANCE.getSchemaFeature_Id();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaSectionImpl <em>Schema Section</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaSectionImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaSection()
     * @generated
     */
    EClass SCHEMA_SECTION = eINSTANCE.getSchemaSection();

    /**
     * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA_SECTION__FEATURES = eINSTANCE.getSchemaSection_Features();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyImpl <em>Schema Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaProperty()
     * @generated
     */
    EClass SCHEMA_PROPERTY = eINSTANCE.getSchemaProperty();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA_PROPERTY__SOURCE = eINSTANCE.getSchemaProperty_Source();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyOfValueImpl <em>Schema Property Of Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.SchemaPropertyOfValueImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getSchemaPropertyOfValue()
     * @generated
     */
    EClass SCHEMA_PROPERTY_OF_VALUE = eINSTANCE.getSchemaPropertyOfValue();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA_PROPERTY_OF_VALUE__TYPE = eINSTANCE.getSchemaPropertyOfValue_Type();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCHEMA_PROPERTY_OF_VALUE__BODY = eINSTANCE.getSchemaPropertyOfValue_Body();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetModelingLanguage.impl.FunctionImpl <em>Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.FunctionImpl
     * @see org.monet.editor.dsl.monetModelingLanguage.impl.MonetModelingLanguagePackageImpl#getFunction()
     * @generated
     */
    EClass FUNCTION = eINSTANCE.getFunction();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FUNCTION__NAME = eINSTANCE.getFunction_Name();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION__PARAMS = eINSTANCE.getFunction_Params();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION__TYPE = eINSTANCE.getFunction_Type();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION__BODY = eINSTANCE.getFunction_Body();

  }

} //MonetModelingLanguagePackage
