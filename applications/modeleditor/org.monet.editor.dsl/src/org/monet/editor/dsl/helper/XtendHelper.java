package org.monet.editor.dsl.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.LinkedList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.xtext.common.types.JvmAnnotationValue;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.access.impl.ClassURIHelper;
import org.eclipse.xtext.common.types.access.impl.URIHelperConstants;
import org.eclipse.xtext.naming.QualifiedName;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.metamodel.MetaModelStructure;
import org.monet.editor.dsl.metamodel.Pair;
import org.monet.editor.dsl.monetModelingLanguage.Attribute;
import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.DistributionModel;
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral;
import org.monet.editor.dsl.monetModelingLanguage.Function;
import org.monet.editor.dsl.monetModelingLanguage.ProjectModel;
import org.monet.editor.dsl.monetModelingLanguage.Property;
import org.monet.editor.dsl.monetModelingLanguage.Referenciable;
import org.monet.editor.dsl.monetModelingLanguage.Schema;

import com.google.common.collect.HashMultimap;


@SuppressWarnings("restriction")
public class XtendHelper {

  public static String getReferenciableName(Referenciable object) {
    if(object instanceof Definition)
      return ((Definition)object).getName();
    else if(object instanceof Property) {
      Property property = ((Property)object);
      return property.getName() != null ? property.getName() : property.getId();
    }
    return null;
  }
  
  public static IProject getIProject(EObject eObject) {
    URI uri = eObject.eResource().getURI();
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    String s = uri.isPlatformResource() ? uri.toPlatformString( true ) : uri.toString();
    Path path = new Path(s);
    IResource resource = workspace.getRoot().getFile( path );
    IProject project = null == resource ? null : resource.getProject();
    return project;
  }
  
  public static IResource getIResource(EObject eObject) {
    URI uri = eObject.eResource().getURI();
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    String s = uri.isPlatformResource() ? uri.toPlatformString( true ) : uri.toString();
    Path path = new Path(s);
    IResource resource = workspace.getRoot().getFile( path );
    return resource;
  }
    
  public static Class<?> getEnumType(String valueTypeQualifiedName) throws ClassNotFoundException {
    Class<?> enumClazz = null;
    do {
      try {
        enumClazz = Class.forName(valueTypeQualifiedName);
      } catch (ClassNotFoundException e) {
        //Enum type in superclass?
        int separatorIndex = valueTypeQualifiedName.indexOf("$");
        String definitionClassName = valueTypeQualifiedName.substring(0, separatorIndex);
        String propertyNames = valueTypeQualifiedName.substring(separatorIndex);
        Class<?> superClass = Class.forName(definitionClassName).getSuperclass();
        if(superClass == Object.class)
          break;
        valueTypeQualifiedName = superClass.getName() + propertyNames;
      }
    } while(enumClazz == null);
    return enumClazz;
  }
  
  public static String convertQualifiedNameToGenName(QualifiedName qualifiedName) {
    if(qualifiedName == null)
      return null;
    return qualifiedName.skipLast(1).toString().toLowerCase() + "." + JavaHelper.toJavaIdentifier(qualifiedName.getLastSegment());
  }
  
  public static String convertQualifiedNameToGenNameWithSuffix(QualifiedName qualifiedName, String suffix) {
    return qualifiedName.skipLast(1).toString().toLowerCase() + "." + JavaHelper.toJavaIdentifier(qualifiedName.getLastSegment()) + suffix;
  }
  
  public static JvmTypeReference[] createParametersArray(JvmTypeReference ... params) {
    if(params.length == 0 || (params.length == 1 && params[0] == null))
      return null;
    return params;
  }
  
  public static JvmAnnotationValue createAnnotationValue(String name, Object value, Class<?> type, ClassURIHelper uriHelper) {
    Class<?> valueClass = value.getClass();
    JvmAnnotationValue result = createAnnotationValue(valueClass);
    
    if (valueClass.isPrimitive() || String.class.equals(valueClass) || Boolean.class.equals(valueClass)) {
      result.eSet(result.eClass().getEStructuralFeature("values"), Collections.singleton(value));
    }
    
    for(Method method : type.getDeclaredMethods())
      if(method.getName().equals(name)) {
        result.setOperation(createMethodProxy(uriHelper, method));
        break;
      }

    return result;
  }
  
  protected static JvmOperation createMethodProxy(ClassURIHelper uriHelper, Method method) {
    InternalEObject proxy = (InternalEObject) TypesFactory.eINSTANCE.createJvmOperation();
    URI uri = XtendHelper.getFullURI(uriHelper, method);
    proxy.eSetProxyURI(uri);
    return (JvmOperation) proxy;
  }

  protected static JvmAnnotationValue createAnnotationValue(Class<?> type) {
    if (String.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmStringAnnotationValue();
    } else if (Class.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmTypeAnnotationValue();
    } else if (type.isAnnotation()) {
      return TypesFactory.eINSTANCE.createJvmAnnotationAnnotationValue();
    } else if (type.isEnum()) {
      return TypesFactory.eINSTANCE.createJvmEnumAnnotationValue();
    } else if (int.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmIntAnnotationValue();
    } else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmBooleanAnnotationValue();
    } else if (long.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmLongAnnotationValue();
    } else if (byte.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmByteAnnotationValue();
    } else if (short.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmShortAnnotationValue();
    } else if (float.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmFloatAnnotationValue();
    } else if (double.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmDoubleAnnotationValue();
    } else if (char.class.equals(type)) {
      return TypesFactory.eINSTANCE.createJvmCharAnnotationValue();
    } else
      throw new IllegalArgumentException("Unexpected type: " + type.getCanonicalName());
  }

  public static HashMultimap<String, Pair<String, Integer>> createMultimap() {
    return HashMultimap.create();
  }
  
  public static Item checkElement(EObject root, MetaModelStructure metaModelStructure) {
    return checkElement(root, metaModelStructure, false);
  }
  
  public static Item checkElement(EObject root, MetaModelStructure metaModelStructure, boolean stopInMethods) {
    LinkedList<EObject> queue = new LinkedList<EObject>();
    return checkElement(getRootElement(root, queue, metaModelStructure, stopInMethods), queue);
  }

  public static Item getRootElement(EObject current, LinkedList<EObject> queue, MetaModelStructure metaModelStructure, boolean stopInMethods) {
    Item currentItem = null;
    while (!(current instanceof Definition || current instanceof DistributionModel || current instanceof ProjectModel)) {
      if(current == null || current instanceof Function || (stopInMethods && current.eContainer() instanceof org.monet.editor.dsl.monetModelingLanguage.Method)) {
        queue.clear();
        return null;
      }
      queue.push(current);
      current = current.eContainer();
    }
    queue.push(current); //Definition is on the head of the queue
    
    String definitionType = null;
    if(current instanceof Definition)
      definitionType = ((Definition) current).getDefinitionType();
    else if(current instanceof DistributionModel)
      definitionType = "distribution";
    else if(current instanceof ProjectModel)
      definitionType = "project";
    currentItem = metaModelStructure.getDefinition(definitionType);
    return currentItem;
  }

  public static Item checkElement(Item root, LinkedList<EObject> queue) {
    Item currentItem = root;
    EObject current = null;

    while (queue.size() > 0) {
      current = queue.pop();
      int type = 0;
      String key = null;
      if (current instanceof Attribute) {
        type = Item.ATTRIBUTE;
        key = ((Attribute) current).getId();
      } else if (current instanceof EnumLiteral) {
        type = Item.ENUMVALUE;
        key = ((EnumLiteral)current).getValue();
      } else if (current instanceof Property) {
        type = Item.PROPERTY;
        key = ((Property) current).getId();
      } else if (current instanceof Schema) {
        type = Item.PROPERTY;
        key = "schema";
      } else if (current instanceof org.monet.editor.dsl.monetModelingLanguage.Method) {
        type = Item.METHOD;
        key = ((org.monet.editor.dsl.monetModelingLanguage.Method) current).getId();
      } else {
        continue;
      }

      currentItem = currentItem.getChild(key);
      if (!(currentItem != null && currentItem.getType() == type))
        return null;
    }
    return currentItem;
  }
  
  private static URI getFullURI(ClassURIHelper uriHelper, Member member) {
    if (member instanceof Type)
      return uriHelper.getFullURI((Type) member);
    else
      return MonetClassURIHelper.getFullURI(member);
  }
  
}
