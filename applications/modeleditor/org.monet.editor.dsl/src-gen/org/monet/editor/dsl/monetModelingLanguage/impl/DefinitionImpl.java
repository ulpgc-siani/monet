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

import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Feature;
import org.monet.editor.dsl.monetModelingLanguage.MonetModelingLanguagePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#getCode <em>Code</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#isExtensible <em>Extensible</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#getDefinitionType <em>Definition Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#getSuperType <em>Super Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#getReplaceSuperType <em>Replace Super Type</em>}</li>
 *   <li>{@link org.monet.editor.dsl.monetModelingLanguage.impl.DefinitionImpl#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DefinitionImpl extends FeatureImpl implements Definition
{
  /**
   * The cached value of the '{@link #getCode() <em>Code</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCode()
   * @generated
   * @ordered
   */
  protected Code code;

  /**
   * The default value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAbstract()
   * @generated
   * @ordered
   */
  protected static final boolean ABSTRACT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAbstract()
   * @generated
   * @ordered
   */
  protected boolean abstract_ = ABSTRACT_EDEFAULT;

  /**
   * The default value of the '{@link #isExtensible() <em>Extensible</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isExtensible()
   * @generated
   * @ordered
   */
  protected static final boolean EXTENSIBLE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isExtensible() <em>Extensible</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isExtensible()
   * @generated
   * @ordered
   */
  protected boolean extensible = EXTENSIBLE_EDEFAULT;

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
   * The default value of the '{@link #getDefinitionType() <em>Definition Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinitionType()
   * @generated
   * @ordered
   */
  protected static final String DEFINITION_TYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDefinitionType() <em>Definition Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinitionType()
   * @generated
   * @ordered
   */
  protected String definitionType = DEFINITION_TYPE_EDEFAULT;

  /**
   * The cached value of the '{@link #getSuperType() <em>Super Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSuperType()
   * @generated
   * @ordered
   */
  protected Definition superType;

  /**
   * The cached value of the '{@link #getReplaceSuperType() <em>Replace Super Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReplaceSuperType()
   * @generated
   * @ordered
   */
  protected Definition replaceSuperType;

  /**
   * The cached value of the '{@link #getFeatures() <em>Features</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFeatures()
   * @generated
   * @ordered
   */
  protected EList<Feature> features;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DefinitionImpl()
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
    return MonetModelingLanguagePackage.Literals.DEFINITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Code getCode()
  {
    return code;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCode(Code newCode, NotificationChain msgs)
  {
    Code oldCode = code;
    code = newCode;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__CODE, oldCode, newCode);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCode(Code newCode)
  {
    if (newCode != code)
    {
      NotificationChain msgs = null;
      if (code != null)
        msgs = ((InternalEObject)code).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MonetModelingLanguagePackage.DEFINITION__CODE, null, msgs);
      if (newCode != null)
        msgs = ((InternalEObject)newCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MonetModelingLanguagePackage.DEFINITION__CODE, null, msgs);
      msgs = basicSetCode(newCode, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__CODE, newCode, newCode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isAbstract()
  {
    return abstract_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAbstract(boolean newAbstract)
  {
    boolean oldAbstract = abstract_;
    abstract_ = newAbstract;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__ABSTRACT, oldAbstract, abstract_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isExtensible()
  {
    return extensible;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExtensible(boolean newExtensible)
  {
    boolean oldExtensible = extensible;
    extensible = newExtensible;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__EXTENSIBLE, oldExtensible, extensible));
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
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDefinitionType()
  {
    return definitionType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefinitionType(String newDefinitionType)
  {
    String oldDefinitionType = definitionType;
    definitionType = newDefinitionType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__DEFINITION_TYPE, oldDefinitionType, definitionType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition getSuperType()
  {
    if (superType != null && superType.eIsProxy())
    {
      InternalEObject oldSuperType = (InternalEObject)superType;
      superType = (Definition)eResolveProxy(oldSuperType);
      if (superType != oldSuperType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MonetModelingLanguagePackage.DEFINITION__SUPER_TYPE, oldSuperType, superType));
      }
    }
    return superType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition basicGetSuperType()
  {
    return superType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSuperType(Definition newSuperType)
  {
    Definition oldSuperType = superType;
    superType = newSuperType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__SUPER_TYPE, oldSuperType, superType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition getReplaceSuperType()
  {
    if (replaceSuperType != null && replaceSuperType.eIsProxy())
    {
      InternalEObject oldReplaceSuperType = (InternalEObject)replaceSuperType;
      replaceSuperType = (Definition)eResolveProxy(oldReplaceSuperType);
      if (replaceSuperType != oldReplaceSuperType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MonetModelingLanguagePackage.DEFINITION__REPLACE_SUPER_TYPE, oldReplaceSuperType, replaceSuperType));
      }
    }
    return replaceSuperType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition basicGetReplaceSuperType()
  {
    return replaceSuperType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setReplaceSuperType(Definition newReplaceSuperType)
  {
    Definition oldReplaceSuperType = replaceSuperType;
    replaceSuperType = newReplaceSuperType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MonetModelingLanguagePackage.DEFINITION__REPLACE_SUPER_TYPE, oldReplaceSuperType, replaceSuperType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Feature> getFeatures()
  {
    if (features == null)
    {
      features = new EObjectContainmentEList<Feature>(Feature.class, this, MonetModelingLanguagePackage.DEFINITION__FEATURES);
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
      case MonetModelingLanguagePackage.DEFINITION__CODE:
        return basicSetCode(null, msgs);
      case MonetModelingLanguagePackage.DEFINITION__FEATURES:
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
      case MonetModelingLanguagePackage.DEFINITION__CODE:
        return getCode();
      case MonetModelingLanguagePackage.DEFINITION__ABSTRACT:
        return isAbstract();
      case MonetModelingLanguagePackage.DEFINITION__EXTENSIBLE:
        return isExtensible();
      case MonetModelingLanguagePackage.DEFINITION__NAME:
        return getName();
      case MonetModelingLanguagePackage.DEFINITION__DEFINITION_TYPE:
        return getDefinitionType();
      case MonetModelingLanguagePackage.DEFINITION__SUPER_TYPE:
        if (resolve) return getSuperType();
        return basicGetSuperType();
      case MonetModelingLanguagePackage.DEFINITION__REPLACE_SUPER_TYPE:
        if (resolve) return getReplaceSuperType();
        return basicGetReplaceSuperType();
      case MonetModelingLanguagePackage.DEFINITION__FEATURES:
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
      case MonetModelingLanguagePackage.DEFINITION__CODE:
        setCode((Code)newValue);
        return;
      case MonetModelingLanguagePackage.DEFINITION__ABSTRACT:
        setAbstract((Boolean)newValue);
        return;
      case MonetModelingLanguagePackage.DEFINITION__EXTENSIBLE:
        setExtensible((Boolean)newValue);
        return;
      case MonetModelingLanguagePackage.DEFINITION__NAME:
        setName((String)newValue);
        return;
      case MonetModelingLanguagePackage.DEFINITION__DEFINITION_TYPE:
        setDefinitionType((String)newValue);
        return;
      case MonetModelingLanguagePackage.DEFINITION__SUPER_TYPE:
        setSuperType((Definition)newValue);
        return;
      case MonetModelingLanguagePackage.DEFINITION__REPLACE_SUPER_TYPE:
        setReplaceSuperType((Definition)newValue);
        return;
      case MonetModelingLanguagePackage.DEFINITION__FEATURES:
        getFeatures().clear();
        getFeatures().addAll((Collection<? extends Feature>)newValue);
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
      case MonetModelingLanguagePackage.DEFINITION__CODE:
        setCode((Code)null);
        return;
      case MonetModelingLanguagePackage.DEFINITION__ABSTRACT:
        setAbstract(ABSTRACT_EDEFAULT);
        return;
      case MonetModelingLanguagePackage.DEFINITION__EXTENSIBLE:
        setExtensible(EXTENSIBLE_EDEFAULT);
        return;
      case MonetModelingLanguagePackage.DEFINITION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case MonetModelingLanguagePackage.DEFINITION__DEFINITION_TYPE:
        setDefinitionType(DEFINITION_TYPE_EDEFAULT);
        return;
      case MonetModelingLanguagePackage.DEFINITION__SUPER_TYPE:
        setSuperType((Definition)null);
        return;
      case MonetModelingLanguagePackage.DEFINITION__REPLACE_SUPER_TYPE:
        setReplaceSuperType((Definition)null);
        return;
      case MonetModelingLanguagePackage.DEFINITION__FEATURES:
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
      case MonetModelingLanguagePackage.DEFINITION__CODE:
        return code != null;
      case MonetModelingLanguagePackage.DEFINITION__ABSTRACT:
        return abstract_ != ABSTRACT_EDEFAULT;
      case MonetModelingLanguagePackage.DEFINITION__EXTENSIBLE:
        return extensible != EXTENSIBLE_EDEFAULT;
      case MonetModelingLanguagePackage.DEFINITION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case MonetModelingLanguagePackage.DEFINITION__DEFINITION_TYPE:
        return DEFINITION_TYPE_EDEFAULT == null ? definitionType != null : !DEFINITION_TYPE_EDEFAULT.equals(definitionType);
      case MonetModelingLanguagePackage.DEFINITION__SUPER_TYPE:
        return superType != null;
      case MonetModelingLanguagePackage.DEFINITION__REPLACE_SUPER_TYPE:
        return replaceSuperType != null;
      case MonetModelingLanguagePackage.DEFINITION__FEATURES:
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
    result.append(" (abstract: ");
    result.append(abstract_);
    result.append(", extensible: ");
    result.append(extensible);
    result.append(", name: ");
    result.append(name);
    result.append(", definitionType: ");
    result.append(definitionType);
    result.append(')');
    return result.toString();
  }

} //DefinitionImpl
