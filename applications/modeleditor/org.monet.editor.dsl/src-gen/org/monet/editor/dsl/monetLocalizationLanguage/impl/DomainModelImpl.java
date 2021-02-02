/**
 */
package org.monet.editor.dsl.monetLocalizationLanguage.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.monet.editor.dsl.monetLocalizationLanguage.DomainModel;
import org.monet.editor.dsl.monetLocalizationLanguage.MonetLocalizationLanguagePackage;
import org.monet.editor.dsl.monetLocalizationLanguage.StringResource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetLocalizationLanguage.impl.DomainModelImpl#getCode <em>Code</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetLocalizationLanguage.impl.DomainModelImpl#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DomainModelImpl extends MinimalEObjectImpl.Container implements DomainModel
{
  /**
   * The default value of the '{@link #getCode() <em>Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCode()
   * @generated
   * @ordered
   */
  protected static final String CODE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCode() <em>Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCode()
   * @generated
   * @ordered
   */
  protected String code = CODE_EDEFAULT;

  /**
   * The cached value of the '{@link #getFeatures() <em>Features</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFeatures()
   * @generated
   * @ordered
   */
  protected EList<StringResource> features;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DomainModelImpl()
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
    return MonetLocalizationLanguagePackage.Literals.DOMAIN_MODEL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getCode()
  {
    return code;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCode(String newCode)
  {
    String oldCode = code;
    code = newCode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetLocalizationLanguagePackage.DOMAIN_MODEL__CODE, oldCode, code));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<StringResource> getFeatures()
  {
    if (features == null)
    {
      features = new EObjectContainmentEList<StringResource>(StringResource.class, this, MonetLocalizationLanguagePackage.DOMAIN_MODEL__FEATURES);
    }
    return features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__FEATURES:
        return ((InternalEList<?>)getFeatures()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__CODE:
        return getCode();
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__FEATURES:
        return getFeatures();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__CODE:
        setCode((String)newValue);
        return;
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__FEATURES:
        getFeatures().clear();
        getFeatures().addAll((Collection<? extends StringResource>)newValue);
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
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__CODE:
        setCode(CODE_EDEFAULT);
        return;
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__FEATURES:
        getFeatures().clear();
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
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__CODE:
        return CODE_EDEFAULT == null ? code != null : !CODE_EDEFAULT.equals(code);
      case MonetLocalizationLanguagePackage.DOMAIN_MODEL__FEATURES:
        return features != null && !features.isEmpty();
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
    result.append(" (code: ");
    result.append(code);
    result.append(')');
    return result.toString();
  }

} //DomainModelImpl
