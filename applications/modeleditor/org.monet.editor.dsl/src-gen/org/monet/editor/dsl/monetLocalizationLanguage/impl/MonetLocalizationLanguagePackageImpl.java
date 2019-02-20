/**
 */
package org.monet.editor.dsl.monetLocalizationLanguage.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.monet.editor.dsl.monetLocalizationLanguage.DomainModel;
import org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguageFactory;
import org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage;
import org.monet.editor.dsl.monetLocalizationLanguage.StringResource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MonetLocalizationLanguagePackageImpl extends EPackageImpl implements MonetLocalizationLanguagePackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass domainModelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringResourceEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage#eNS_URI
   * @see #init()
   * @generated
   */
  private MonetLocalizationLanguagePackageImpl()
  {
    super(eNS_URI, MonetLocalizationLanguageFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link MonetLocalizationLanguagePackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static MonetLocalizationLanguagePackage init()
  {
    if (isInited) return (MonetLocalizationLanguagePackage)EPackage.Registry.INSTANCE.getEPackage(MonetLocalizationLanguagePackage.eNS_URI);

    // Obtain or create and register package
    MonetLocalizationLanguagePackageImpl theMonetLocalizationLanguagePackage = (MonetLocalizationLanguagePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MonetLocalizationLanguagePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MonetLocalizationLanguagePackageImpl());

    isInited = true;

    // Create package meta-data objects
    theMonetLocalizationLanguagePackage.createPackageContents();

    // Initialize created meta-data
    theMonetLocalizationLanguagePackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theMonetLocalizationLanguagePackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(MonetLocalizationLanguagePackage.eNS_URI, theMonetLocalizationLanguagePackage);
    return theMonetLocalizationLanguagePackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDomainModel()
  {
    return domainModelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDomainModel_Code()
  {
    return (EAttribute)domainModelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDomainModel_Features()
  {
    return (EReference)domainModelEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStringResource()
  {
    return stringResourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringResource_Name()
  {
    return (EAttribute)stringResourceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringResource_Value()
  {
    return (EAttribute)stringResourceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MonetLocalizationLanguageFactory getMonetLocalizationLanguageFactory()
  {
    return (MonetLocalizationLanguageFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    domainModelEClass = createEClass(DOMAIN_MODEL);
    createEAttribute(domainModelEClass, DOMAIN_MODEL__CODE);
    createEReference(domainModelEClass, DOMAIN_MODEL__FEATURES);

    stringResourceEClass = createEClass(STRING_RESOURCE);
    createEAttribute(stringResourceEClass, STRING_RESOURCE__NAME);
    createEAttribute(stringResourceEClass, STRING_RESOURCE__VALUE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes

    // Initialize classes and features; add operations and parameters
    initEClass(domainModelEClass, DomainModel.class, "DomainModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDomainModel_Code(), ecorePackage.getEString(), "code", null, 0, 1, DomainModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDomainModel_Features(), this.getStringResource(), null, "features", null, 0, -1, DomainModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringResourceEClass, StringResource.class, "StringResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStringResource_Name(), ecorePackage.getEString(), "name", null, 0, 1, StringResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStringResource_Value(), ecorePackage.getEString(), "value", null, 0, 1, StringResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //MonetLocalizationLanguagePackageImpl
