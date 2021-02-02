/**
 */
package org.monet.editor.dsl.monetLocalizationLanguage;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Domain Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetLocalizationLanguage.DomainModel#getCode <em>Code</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetLocalizationLanguage.DomainModel#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage#getDomainModel()
 * @model
 * @generated
 */
public interface DomainModel extends EObject
{
  /**
   * Returns the value of the '<em><b>Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Code</em>' attribute.
   * @see #setCode(String)
   * @see org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage#getDomainModel_Code()
   * @model
   * @generated
   */
  String getCode();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetLocalizationLanguage.DomainModel#getCode <em>Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Code</em>' attribute.
   * @see #getCode()
   * @generated
   */
  void setCode(String value);

  /**
   * Returns the value of the '<em><b>Features</b></em>' containment reference list.
   * The list contents are of type {@link org.monet.editor.dsl.monetLocalizationLanguage.StringResource}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Features</em>' containment reference list.
   * @see org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage#getDomainModel_Features()
   * @model containment="true"
   * @generated
   */
  EList<StringResource> getFeatures();

} // DomainModel
