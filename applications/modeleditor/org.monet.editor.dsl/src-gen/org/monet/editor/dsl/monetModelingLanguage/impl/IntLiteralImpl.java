/**
 */
package org.monet.editor.dsl.monetModelingLanguage.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.monet.editor.dsl.monetModelingLanguage.IntLiteral;
import org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Int Literal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.IntLiteralImpl#isNegative <em>Negative</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.IntLiteralImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IntLiteralImpl extends AttributeValueImpl implements IntLiteral
{
  /**
   * The default value of the '{@link #isNegative() <em>Negative</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isNegative()
   * @generated
   * @ordered
   */
  protected static final boolean NEGATIVE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isNegative() <em>Negative</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isNegative()
   * @generated
   * @ordered
   */
  protected boolean negative = NEGATIVE_EDEFAULT;

  /**
   * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected static final int VALUE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected int value = VALUE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IntLiteralImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return MonetModelingLanguagePackage.Literals.INT_LITERAL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isNegative()
  {
    return negative;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNegative(boolean newNegative)
  {
    boolean oldNegative = negative;
    negative = newNegative;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.INT_LITERAL__NEGATIVE, oldNegative, negative));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setValue(int newValue)
  {
    int oldValue = value;
    value = newValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.INT_LITERAL__VALUE, oldValue, value));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case MonetModelingLanguagePackage.INT_LITERAL__NEGATIVE:
        return isNegative();
      case MonetModelingLanguagePackage.INT_LITERAL__VALUE:
        return getValue();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case MonetModelingLanguagePackage.INT_LITERAL__NEGATIVE:
        setNegative((Boolean)newValue);
        return;
      case MonetModelingLanguagePackage.INT_LITERAL__VALUE:
        setValue((Integer)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case MonetModelingLanguagePackage.INT_LITERAL__NEGATIVE:
        setNegative(NEGATIVE_EDEFAULT);
        return;
      case MonetModelingLanguagePackage.INT_LITERAL__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case MonetModelingLanguagePackage.INT_LITERAL__NEGATIVE:
        return negative != NEGATIVE_EDEFAULT;
      case MonetModelingLanguagePackage.INT_LITERAL__VALUE:
        return value != VALUE_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (negative: ");
    result.append(negative);
    result.append(", value: ");
    result.append(value);
    result.append(')');
    return result.toString();
  }

} //IntLiteralImpl
