/**
 */
package org.monet.editor.dsl.monetModelingLanguage.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;
import org.monet.editor.dsl.monetModelingLanguage.ManifestFeature;
import org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DistributionModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DistributionModelImpl#getSuperType <em>Super Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DistributionModelImpl#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DistributionModelImpl extends DomainModelImpl implements DistributionModel
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getSuperType() <em>Super Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSuperType()
   * @generated
   * @ordered
   */
  protected ProjectModel superType;

  /**
   * The cached value of the '{@link #getFeatures() <em>Features</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFeatures()
   * @generated
   * @ordered
   */
  protected EList<ManifestFeature> features;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DistributionModelImpl()
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
    return MonetModelingLanguagePackage.Literals.DISTRIBUTION_MODEL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DISTRIBUTION_MODEL__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectModel getSuperType()
  {
    if (superType != null && superType.eIsProxy())
    {
      InternalEObject oldSuperType = (InternalEObject)superType;
      superType = (ProjectModel)eResolveProxy(oldSuperType);
      if (superType != oldSuperType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MonetModelingLanguagePackage.DISTRIBUTION_MODEL__SUPER_TYPE, oldSuperType, superType));
      }
    }
    return superType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectModel basicGetSuperType()
  {
    return superType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSuperType(ProjectModel newSuperType)
  {
    ProjectModel oldSuperType = superType;
    superType = newSuperType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DISTRIBUTION_MODEL__SUPER_TYPE, oldSuperType, superType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ManifestFeature> getFeatures()
  {
    if (features == null)
    {
      features = new EObjectContainmentEList<ManifestFeature>(ManifestFeature.class, this, MonetModelingLanguagePackage.DISTRIBUTION_MODEL__FEATURES);
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
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__FEATURES:
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
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__NAME:
        return getName();
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__SUPER_TYPE:
        if (resolve) return getSuperType();
        return basicGetSuperType();
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__FEATURES:
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
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__NAME:
        setName((String)newValue);
        return;
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__SUPER_TYPE:
        setSuperType((ProjectModel)newValue);
        return;
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__FEATURES:
        getFeatures().clear();
        getFeatures().addAll((Collection<? extends ManifestFeature>)newValue);
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
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__NAME:
        setName(NAME_EDEFAULT);
        return;
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__SUPER_TYPE:
        setSuperType((ProjectModel)null);
        return;
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__FEATURES:
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
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__SUPER_TYPE:
        return superType != null;
      case MonetModelingLanguagePackage.DISTRIBUTION_MODEL__FEATURES:
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
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //DistributionModelImpl
