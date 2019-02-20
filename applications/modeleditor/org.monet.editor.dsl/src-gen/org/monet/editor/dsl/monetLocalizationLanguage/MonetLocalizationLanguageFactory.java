/**
 */
package org.monet.editor.dsl.monetLocalizationLanguage;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage
 * @generated
 */
public interface MonetLocalizationLanguageFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MonetLocalizationLanguageFactory eINSTANCE = org.monet.editor.dsl.monetLocalizationLanguage.impl.MonetLocalizationLanguageFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Domain Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Domain Model</em>'.
   * @generated
   */
  DomainModel createDomainModel();

  /**
   * Returns a new object of class '<em>String Resource</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Resource</em>'.
   * @generated
   */
  StringResource createStringResource();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  MonetLocalizationLanguagePackage getMonetLocalizationLanguagePackage();

} //MonetLocalizationLanguageFactory
