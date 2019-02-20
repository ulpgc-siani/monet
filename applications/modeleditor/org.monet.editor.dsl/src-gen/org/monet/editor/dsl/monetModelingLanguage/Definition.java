/**
 */
package org.monet.editor.dsl.monetModelingLanguage;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getCode <em>Code</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#isExtensible <em>Extensible</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getName <em>Name</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getDefinitionType <em>Definition Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getSuperType <em>Super Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getReplaceSuperType <em>Replace Super Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getFeatures <em>Features</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition()
 * @model
 * @generated
 */
public interface Definition extends Feature, Referenciable
{
  /**
   * Returns the value of the '<em><b>Code</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Code</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Code</em>' containment reference.
   * @see #setCode(Code)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_Code()
   * @model containment="true"
   * @generated
   */
  Code getCode();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getCode <em>Code</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Code</em>' containment reference.
   * @see #getCode()
   * @generated
   */
  void setCode(Code value);

  /**
   * Returns the value of the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Abstract</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Abstract</em>' attribute.
   * @see #setAbstract(boolean)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_Abstract()
   * @model
   * @generated
   */
  boolean isAbstract();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#isAbstract <em>Abstract</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Abstract</em>' attribute.
   * @see #isAbstract()
   * @generated
   */
  void setAbstract(boolean value);

  /**
   * Returns the value of the '<em><b>Extensible</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Extensible</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Extensible</em>' attribute.
   * @see #setExtensible(boolean)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_Extensible()
   * @model
   * @generated
   */
  boolean isExtensible();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#isExtensible <em>Extensible</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Extensible</em>' attribute.
   * @see #isExtensible()
   * @generated
   */
  void setExtensible(boolean value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Definition Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Definition Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Definition Type</em>' attribute.
   * @see #setDefinitionType(String)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_DefinitionType()
   * @model
   * @generated
   */
  String getDefinitionType();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getDefinitionType <em>Definition Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Definition Type</em>' attribute.
   * @see #getDefinitionType()
   * @generated
   */
  void setDefinitionType(String value);

  /**
   * Returns the value of the '<em><b>Super Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Super Type</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Super Type</em>' reference.
   * @see #setSuperType(Definition)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_SuperType()
   * @model
   * @generated
   */
  Definition getSuperType();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getSuperType <em>Super Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Super Type</em>' reference.
   * @see #getSuperType()
   * @generated
   */
  void setSuperType(Definition value);

  /**
   * Returns the value of the '<em><b>Replace Super Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Replace Super Type</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Replace Super Type</em>' reference.
   * @see #setReplaceSuperType(Definition)
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_ReplaceSuperType()
   * @model
   * @generated
   */
  Definition getReplaceSuperType();

  /**
   * Sets the value of the '{@link org.monet.editor.dsl.monetModelingLanguage.Definition#getReplaceSuperType <em>Replace Super Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Replace Super Type</em>' reference.
   * @see #getReplaceSuperType()
   * @generated
   */
  void setReplaceSuperType(Definition value);

  /**
   * Returns the value of the '<em><b>Features</b></em>' containment reference list.
   * The list contents are of type {@link org.monet.editor.dsl.monetModelingLanguage.Feature}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Features</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Features</em>' containment reference list.
   * @see org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage#getDefinition_Features()
   * @model containment="true"
   * @generated
   */
  EList<Feature> getFeatures();

} // Definition
