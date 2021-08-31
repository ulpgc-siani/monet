/**
 */
package org.monet.editor.dsl.monetModelingLanguage;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Attribute#getId <em>Id</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Attribute#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getAttribute()
 * @model
 * @generated
 */
public interface Attribute extends ManifestFeature, Feature, PropertyFeature
{
  /**
   * Returns the value of the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Id</em>' attribute.
   * @see #setId(String)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getAttribute_Id()
   * @model
   * @generated
   */
  String getId();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Attribute#getId <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id</em>' attribute.
   * @see #getId()
   * @generated
   */
  void setId(String value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference.
   * @see #setValue(AttributeValue)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getAttribute_Value()
   * @model containment="true"
   * @generated
   */
  AttributeValue getValue();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Attribute#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
  void setValue(AttributeValue value);

} // Attribute
