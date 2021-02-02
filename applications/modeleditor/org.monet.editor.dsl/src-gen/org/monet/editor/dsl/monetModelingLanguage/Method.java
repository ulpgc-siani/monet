/**
 */
package org.monet.editor.dsl.monetModelingLanguage;

import org.eclipse.emf.common.util.EList;

import org.eclipse.xtext.common.types.JvmFormalParameter;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Method#getId <em>Id</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Method#getParams <em>Params</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Method#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getMethod()
 * @model
 * @generated
 */
public interface Method extends ManifestFeature, Feature, PropertyFeature
{
  /**
   * Returns the value of the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Id</em>' attribute.
   * @see #setId(String)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getMethod_Id()
   * @model
   * @generated
   */
  String getId();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Method#getId <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id</em>' attribute.
   * @see #getId()
   * @generated
   */
  void setId(String value);

  /**
   * Returns the value of the '<em><b>Params</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.xtext.common.types.JvmFormalParameter}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Params</em>' containment reference list.
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getMethod_Params()
   * @model containment="true"
   * @generated
   */
  EList<JvmFormalParameter> getParams();

  /**
   * Returns the value of the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' containment reference.
   * @see #setBody(XExpression)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getMethod_Body()
   * @model containment="true"
   * @generated
   */
  XExpression getBody();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Method#getBody <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' containment reference.
   * @see #getBody()
   * @generated
   */
  void setBody(XExpression value);

} // Method
