/**
 */
package org.monet.editor.dsl.monetModelingLanguage;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Int Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.IntLiteral#isNegative <em>Negative</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.IntLiteral#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getIntLiteral()
 * @model
 * @generated
 */
public interface IntLiteral extends AttributeValue
{
  /**
   * Returns the value of the '<em><b>Negative</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Negative</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Negative</em>' attribute.
   * @see #setNegative(boolean)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getIntLiteral_Negative()
   * @model
   * @generated
   */
  boolean isNegative();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.IntLiteral#isNegative <em>Negative</em>}' attribute.
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
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(int)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getIntLiteral_Value()
   * @model
   * @generated
   */
  int getValue();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.IntLiteral#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(int value);

} // IntLiteral
