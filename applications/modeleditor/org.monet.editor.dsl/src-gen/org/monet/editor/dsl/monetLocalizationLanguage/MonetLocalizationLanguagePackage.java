/**
 */
package org.monet.editor.dsl.monetLocalizationLanguage;

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
 * @see org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguageFactory
 * @model kind="package"
 * @generated
 */
public interface MonetLocalizationLanguagePackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "monetLocalizationLanguage";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.monet.org/editor/dsl/MonetLocalizationLanguage";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "monetLocalizationLanguage";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MonetLocalizationLanguagePackage eINSTANCE = org.monet.editor.dsl.monetLocalizationLanguage.impl.MonetLocalizationLanguagePackageImpl.init();

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetLocalizationLanguage.impl.DomainModelImpl <em>Domain Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.DomainModelImpl
   * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.MonetLocalizationLanguagePackageImpl#getDomainModel()
   * @generated
   */
  int DOMAIN_MODEL = 0;

  /**
   * The feature id for the '<em><b>Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOMAIN_MODEL__CODE = 0;

  /**
   * The feature id for the '<em><b>Features</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOMAIN_MODEL__FEATURES = 1;

  /**
   * The number of structural features of the '<em>Domain Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOMAIN_MODEL_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.monet.editor.dsl.monetLocalizationLanguage.impl.StringResourceImpl <em>String Resource</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.StringResourceImpl
   * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.MonetLocalizationLanguagePackageImpl#getStringResource()
   * @generated
   */
  int STRING_RESOURCE = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_RESOURCE__NAME = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_RESOURCE__VALUE = 1;

  /**
   * The number of structural features of the '<em>String Resource</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_RESOURCE_FEATURE_COUNT = 2;


  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetLocalizationLanguage.DomainModel <em>Domain Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Domain Model</em>'.
   * @see org.monet.editor.dsl.monetLocalizationLanguage.DomainModel
   * @generated
   */
  EClass getDomainModel();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetLocalizationLanguage.DomainModel#getCode <em>Code</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Code</em>'.
   * @see org.monet.editor.dsl.monetLocalizationLanguage.DomainModel#getCode()
   * @see #getDomainModel()
   * @generated
   */
  EAttribute getDomainModel_Code();

  /**
   * Returns the meta object for the containment reference list '{@link org.monet.editor.dsl.monetLocalizationLanguage.DomainModel#getFeatures <em>Features</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Features</em>'.
   * @see org.monet.editor.dsl.monetLocalizationLanguage.DomainModel#getFeatures()
   * @see #getDomainModel()
   * @generated
   */
  EReference getDomainModel_Features();

  /**
   * Returns the meta object for class '{@link org.monet.editor.dsl.monetLocalizationLanguage.StringResource <em>String Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Resource</em>'.
   * @see org.monet.editor.dsl.monetLocalizationLanguage.StringResource
   * @generated
   */
  EClass getStringResource();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetLocalizationLanguage.StringResource#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.monet.editor.dsl.monetLocalizationLanguage.StringResource#getName()
   * @see #getStringResource()
   * @generated
   */
  EAttribute getStringResource_Name();

  /**
   * Returns the meta object for the attribute '{@link org.monet.editor.dsl.monetLocalizationLanguage.StringResource#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.monet.editor.dsl.monetLocalizationLanguage.StringResource#getValue()
   * @see #getStringResource()
   * @generated
   */
  EAttribute getStringResource_Value();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  MonetLocalizationLanguageFactory getMonetLocalizationLanguageFactory();

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
     * The meta object literal for the '{@link org.monet.editor.dsl.monetLocalizationLanguage.impl.DomainModelImpl <em>Domain Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.DomainModelImpl
     * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.MonetLocalizationLanguagePackageImpl#getDomainModel()
     * @generated
     */
    EClass DOMAIN_MODEL = eINSTANCE.getDomainModel();

    /**
     * The meta object literal for the '<em><b>Code</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOMAIN_MODEL__CODE = eINSTANCE.getDomainModel_Code();

    /**
     * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOMAIN_MODEL__FEATURES = eINSTANCE.getDomainModel_Features();

    /**
     * The meta object literal for the '{@link org.monet.editor.dsl.monetLocalizationLanguage.impl.StringResourceImpl <em>String Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.StringResourceImpl
     * @see org.monet.editor.dsl.monetLocalizationLanguage.impl.MonetLocalizationLanguagePackageImpl#getStringResource()
     * @generated
     */
    EClass STRING_RESOURCE = eINSTANCE.getStringResource();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_RESOURCE__NAME = eINSTANCE.getStringResource_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_RESOURCE__VALUE = eINSTANCE.getStringResource_Value();

  }

} //MonetLocalizationLanguagePackage
