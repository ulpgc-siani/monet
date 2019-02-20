/**
 */
package org.monet.editor.dsl.monetLocalizationLanguage.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.monet.editor.dsl.monetLocalizationLanguage.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MonetLocalizationLanguageFactoryImpl extends EFactoryImpl implements MonetLocalizationLanguageFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MonetLocalizationLanguageFactory init()
  {
    try
    {
      MonetLocalizationLanguageFactory theMonetLocalizationLanguageFactory = (MonetLocalizationLanguageFactory)EPackage.Registry.INSTANCE.getEFactory(MonetLocalizationLanguagePackage.eNS_URI);
      if (theMonetLocalizationLanguageFactory != null)
      {
        return theMonetLocalizationLanguageFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new MonetLocalizationLanguageFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MonetLocalizationLanguageFactoryImpl()
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
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL: return createDomainModel();
      case MonetLocalizationLanguagePackage.STRING_RESOURCE: return createStringResource();
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
  public StringResource createStringResource()
  {
    StringResourceImpl stringResource = new StringResourceImpl();
    return stringResource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MonetLocalizationLanguagePackage getMonetLocalizationLanguagePackage()
  {
    return (MonetLocalizationLanguagePackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static MonetLocalizationLanguagePackage getPackage()
  {
    return MonetLocalizationLanguagePackage.eINSTANCE;
  }

} //MonetLocalizationLanguageFactoryImpl
