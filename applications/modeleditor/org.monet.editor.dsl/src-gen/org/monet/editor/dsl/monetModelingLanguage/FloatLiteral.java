/**
 */
package org.monet.editor.dsl.monetModelingLanguage;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Float Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#isNegative <em>Negative</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getFloatLiteral()
 * @model
 * @generated
 */
public interface FloatLiteral extends AttributeValue
{
  /**
   * Returns the value of the '<em><b>Negative</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Negative</em>' attribute.
   * @see #setNegative(boolean)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getFloatLiteral_Negative()
   * @model
   * @generated
   */
  boolean isNegative();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#isNegative <em>Negative</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Negative</em>' attribute.
   * @see #isNegative()
   * @generated
   */
  void setNegative(boolean value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(float)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getFloatLiteral_Value()
   * @model
   * @generated
   */
  float getValue();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.FloatLiteral#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(float value);

} // FloatLiteral
