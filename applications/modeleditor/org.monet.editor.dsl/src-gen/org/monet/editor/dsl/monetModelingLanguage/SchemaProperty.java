/**
 */
package org.monet.editor.dsl.monetModelingLanguage;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schema Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.SchemaProperty#getSource <em>Source</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getSchemaProperty()
 * @model
 * @generated
 */
public interface SchemaProperty extends SchemaFeature
{
  /**
   * Returns the value of the '<em><b>Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Source</em>' containment reference.
   * @see #setSource(SchemaPropertyOfValue)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getSchemaProperty_Source()
   * @model containment="true"
   * @generated
   */
  SchemaPropertyOfValue getSource();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.SchemaProperty#getSource <em>Source</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Source</em>' containment reference.
   * @see #getSource()
   * @generated
   */
  void setSource(SchemaPropertyOfValue value);

} // SchemaProperty
