/**
 */
package org.monet.editor.dsl.monetModelingLanguage;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schema Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.SchemaSection#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getSchemaSection()
 * @model
 * @generated
 */
public interface SchemaSection extends SchemaFeature
{
  /**
   * Returns the value of the '<em><b>Features</b></em>' containment reference list.
   * The list contents are of type {@link org.monet.editor.dsl.monetModelingLanguage.SchemaFeature}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Features</em>' containment reference list.
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getSchemaSection_Features()
   * @model containment="true"
   * @generated
   */
  EList<SchemaFeature> getFeatures();

} // SchemaSection
