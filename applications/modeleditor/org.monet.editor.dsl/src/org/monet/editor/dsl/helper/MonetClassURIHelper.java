package org.monet.editor.dsl.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.common.types.access.impl.URIHelperConstants;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class MonetClassURIHelper {

  public static URI createResourceURI(Type type) {
    StringBuilder uriBuilder = createURIBuilder();
    createResourceURI(type, uriBuilder);
    return createURI(uriBuilder);
  }

  public static URI createResourceURI(String withoutProtocol) {
    StringBuilder uriBuilder = new StringBuilder(URIHelperConstants.PROTOCOL.length() + 1 + withoutProtocol.length());
    uriBuilder.append(URIHelperConstants.PROTOCOL).append(":").append(withoutProtocol);
    return createURI(uriBuilder);
  }

  protected static StringBuilder createURIBuilder() {
    StringBuilder builder = new StringBuilder(48);
    builder.append(URIHelperConstants.PROTOCOL);
    builder.append(':');
    return builder;
  }

  protected static URI createURI(StringBuilder uriBuilder) {
    return URI.createURI(uriBuilder.toString());
  }

  public static URI getFullURI(Type type) {
    StringBuilder uriBuilder = createURIBuilder();
    createResourceURI(type, uriBuilder);
    uriBuilder.append('#');
    createFragment(type, uriBuilder);
    return createURI(uriBuilder);
  }
  
  public static URI getFullURI(Member member) {
    if (member instanceof Type)
      return getFullURI((Type) member);
    StringBuilder uriBuilder = createURIBuilder();
    createResourceURI(member.getDeclaringClass(), uriBuilder);
    uriBuilder.append('#');
    createFragmentForMember(member, uriBuilder);
    return createURI(uriBuilder);
  }

  public static String getFragment(Type type) {
    StringBuilder uriBuilder = new StringBuilder(32);
    createFragment(type, uriBuilder);
    return uriBuilder.toString();
  }

  protected static void createFragment(Type type, StringBuilder uriBuilder) {
    if (type instanceof Class<?>) {
      Class<?> clazz = (Class<?>) type;
      createFragmentForClass(clazz, uriBuilder);
    }
    else if (type instanceof TypeVariable<?>) {
      TypeVariable<?> variable = (TypeVariable<?>) type;
      createFragmentForTypeVariable(variable, uriBuilder);
    }
    else if (type instanceof GenericArrayType) {
      createFragment(((GenericArrayType) type).getGenericComponentType(), uriBuilder);
      uriBuilder.append("[]");
    }
    else {
      throw new IllegalStateException("Unexpected type: " + type);
    }
  }

  protected static void createFragmentForTypeVariable(TypeVariable<?> variable, StringBuilder uriBuilder) {
    Object declaration = variable.getGenericDeclaration();
    if (declaration instanceof Type) {
      Type declaringType = (Type) declaration;
      createFragment(declaringType, uriBuilder);
    }
    else if (declaration instanceof Member) {
      Member member = (Member) declaration;
      createFragmentForMember(member, uriBuilder);
    }
    else {
      throw new IllegalArgumentException(variable + " / " + declaration);
    }
    uriBuilder.append('/');
    uriBuilder.append(variable.getName());
  }

  protected static void createFragmentForMember(Member member, StringBuilder uriBuilder) {
    Class<?> declaringClass = member.getDeclaringClass();
    createFragmentForClass(declaringClass, uriBuilder);
    uriBuilder.append('.');
    if (member instanceof Constructor<?>) {
      uriBuilder.append(declaringClass.getSimpleName());
    } else {
      uriBuilder.append(member.getName());
    }
    if (member instanceof Field)
      return;
    uriBuilder.append('(');
    Type[] parameterTypes = null;
    if (member instanceof java.lang.reflect.Constructor<?>) {
      parameterTypes = ((java.lang.reflect.Constructor<?>) member).getGenericParameterTypes();
    }
    else if (member instanceof Method) {
      parameterTypes = ((Method) member).getGenericParameterTypes();
    }
    else {
      throw new IllegalStateException("unknown member type: " + member);
    }
    for (int i = 0; i < parameterTypes.length; i++) {
      if (i != 0) {
        uriBuilder.append(',');
      }
      computeTypeName(parameterTypes[i], uriBuilder);
    }
    uriBuilder.append(')');
  }

  public static String computeTypeName(Type type) {
    StringBuilder result = new StringBuilder(64);
    computeTypeName(type, result);
    return result.toString();
  }

  public static void computeTypeName(Type type, StringBuilder uriBuilder) {
    if (type instanceof Class<?>) {
      Class<?> clazz = (Class<?>) type;
      if (clazz.isArray()) {
        computeTypeName(clazz.getComponentType(), uriBuilder);
        uriBuilder.append("[]");
      }
      else {
        uriBuilder.append(clazz.getName());
      }
    }
    else if (type instanceof GenericArrayType) {
      computeTypeName(((GenericArrayType) type).getGenericComponentType(), uriBuilder);
      uriBuilder.append("[]");
    }
    else if (type instanceof TypeVariable<?>) {
      uriBuilder.append(((TypeVariable<?>) type).getName());
    }
    else if (type instanceof ParameterizedType) {
      Type rawType = ((ParameterizedType) type).getRawType();
      computeTypeName(rawType, uriBuilder);
    }
    else {
      throw new IllegalStateException("unknown type: " + type);
    }
  }

  public static String computeParameterizedTypeName(Type type) {
    StringBuilder result = new StringBuilder(64);
    computeParameterizedTypeName(type, result);
    return result.toString();
  }

  public static void computeParameterizedTypeName(Type type, StringBuilder uriBuilder) {
    computeTypeName(type, uriBuilder);
    if (type instanceof ParameterizedType) {
      uriBuilder.append('<');
      ParameterizedType parameterized = (ParameterizedType) type;
      Type[] actualTypeArguments = parameterized.getActualTypeArguments();
      for (int i = 0; i < actualTypeArguments.length; i++) {
        if (i!=0) {
          uriBuilder.append(',');
        }
        computeParameter(actualTypeArguments[i], uriBuilder);
      }
      uriBuilder.append('>');
    }
  }

  public static void computeParameter(Type type, StringBuilder uriBuilder) {
    if (type instanceof WildcardType) {
      WildcardType wildcard = (WildcardType) type;
      boolean wrote = false;
      if (wildcard.getUpperBounds().length != 0) {
        uriBuilder.append("? extends ");
        wrote = true;
        for (int i = 0; i < wildcard.getUpperBounds().length; i++) {
          if (i != 0)
            uriBuilder.append(" & extends ");
          Type upperBound = wildcard.getUpperBounds()[i];
          computeParameterizedTypeName(upperBound, uriBuilder);
        }
      }
      if (wildcard.getLowerBounds().length != 0) {
        if (!wrote)
          uriBuilder.append("? super ");
        for (int i = 0; i < wildcard.getLowerBounds().length; i++) {
          if (i != 0 || wrote)
            uriBuilder.append(" & super ");
          Type lowerBound = wildcard.getLowerBounds()[i];
          computeParameterizedTypeName(lowerBound, uriBuilder);
        }
      }
    }
    else {
      computeParameterizedTypeName(type, uriBuilder);
    }
  }

  protected static void createFragmentForClass(Class<?> clazz, StringBuilder uriBuilder) {
    if (clazz.isArray()) {
      createFragmentForClass(clazz.getComponentType(), uriBuilder);
      uriBuilder.append("[]");
    }
    else {
      uriBuilder.append(clazz.getName());
    }
  }

  protected static void createResourceURI(Type type, StringBuilder uriBuilder) {
    if (type instanceof Class<?>) {
      Class<?> clazz = (Class<?>) type;
      createResourceURIForClass(clazz, uriBuilder);
    }
    else if (type instanceof TypeVariable<?>) {
      TypeVariable<?> variable = (TypeVariable<?>) type;
      createResourceURIForTypeVariable(variable, uriBuilder);
    }
    else if (type instanceof GenericArrayType) {
      createResourceURI(((GenericArrayType) type).getGenericComponentType(), uriBuilder);
    }
    else {
      throw new IllegalStateException("unexpected type: " + type);
    }
  }

  protected static void createResourceURIForTypeVariable(TypeVariable<?> variable, StringBuilder uriBuilder) {
    Object declaration = variable.getGenericDeclaration();
    if (declaration instanceof Class<?>) {
      Class<?> declaringClass = (Class<?>) declaration;
      createResourceURIForClass(declaringClass, uriBuilder);
    }
    else if (declaration instanceof Member) {
      Member member = (Member) declaration;
      Class<?> declaringClass = member.getDeclaringClass();
      createResourceURIForClass(declaringClass, uriBuilder);
    }
    else {
      throw new IllegalArgumentException(variable + " / " + declaration);
    }
  }

  protected static void createResourceURIForClass(Class<?> clazz, StringBuilder uriBuilder) {
    if (clazz.isArray()) {
      createResourceURIForClass(clazz.getComponentType(), uriBuilder);
    }
    else if (clazz.isMemberClass()) {
      createResourceURIForClass(clazz.getDeclaringClass(), uriBuilder);
    }
    else if (clazz.isPrimitive()) {
      uriBuilder.append(URIHelperConstants.PRIMITIVES);
    }
    else {
      uriBuilder.append(URIHelperConstants.OBJECTS).append(clazz.getName());
    }
  }

}
