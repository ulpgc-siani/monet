/**
 */
package org.monet.editor.dsl.monetModelingLanguage;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getName <em>Name</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getSuperType <em>Super Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDistributionModel()
 * @model
 * @generated
 */
public interface DistributionModel extends DomainModel
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDistributionModel_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Super Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Super Type</em>' reference.
   * @see #setSuperType(ProjectModel)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDistributionModel_SuperType()
   * @model
   * @generated
   */
  ProjectModel getSuperType();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.DistributionModel#getSuperType <em>Super Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Super Type</em>' reference.
   * @see #getSuperType()
   * @generated
   */
  void setSuperType(ProjectModel value);

  /**
   * Returns the value of the '<em><b>Features</b></em>' containment reference list.
   * The list contents are of type {@link org.monet.editor.dsl.monetModelingLanguage.ManifestFeature}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Features</em>' containment reference list.
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDistributionModel_Features()
   * @model containment="true"
   * @generated
   */
  EList<ManifestFeature> getFeatures();

} // DistributionModel
