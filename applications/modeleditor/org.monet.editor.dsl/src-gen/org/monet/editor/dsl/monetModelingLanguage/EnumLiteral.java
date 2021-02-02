/**
 */
package org.monet.editor.dsl.monetModelingLanguage;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.EnumLiteral#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getEnumLiteral()
 * @model
 * @generated
 */
public interface EnumLiteral extends AttributeValue
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getEnumLiteral_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.EnumLiteral#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // EnumLiteral
