package org.monet.grided.control.guice.modules;

/**
 * License:
 * 
 * free to use, modify, rename, redistribute, make money, etc. 
 * 
 * You don't need to repeat this header in your code.  
 * 
 * ... but ...
 * 
 *  do attribute my name and email as an author somewhere in your copy of this code (header, javadoc author tag, etc.).
 * 
 * Adrian Cole ( adrian@jclouds.org )
 */
import static com.google.common.collect.Sets.filter;
import static com.google.inject.matcher.Matchers.any;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.monet.grided.control.log.Logger;
import org.monet.grided.control.log.impl.LoggerImpl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * Any class injected from a guice {@link Injector injector} using this module will have a log4j
 * Logger if either of the following conditions are met.
 * 
 * <ol>
 * <li>A constructor is present with a Logger parameter annotated with @Inject</li>
 * ex.
 * 
 * <pre>
 * public static class A{
 *    private final Logger logger;
 *    
 *    \@Inject public A(Logger logger) {
 *       this.logger = logger; 
 *    } 
 * }
 * </pre>
 * 
 * <li>A field is present of type Logger annotated with @Inject</li>
 * 
 * <pre>
 * public static class B{
 *    \@Inject private  Logger logger;
 * }
 * </pre>
 * 
 * </ol>
 * 
 * <h3>Example Usage</h3>
 * 
 * <pre>
 * Injector i = Guice.createInjector(new BindLog4JLoggersNameClassNameModule());
 * A instance = i.getInstance(A.class);
 * </pre>
 * 
 * @author Adrian Cole adrian@jclouds.org
 */
public class LoggerModule extends AbstractModule {
   @Override
   protected void configure() {
      bindListener(any(), new BindLoggers());
   }

   @Provides
   Logger provideDefaultLoggerToSatisfyGuiceProvisionCheck() {
      return LoggerImpl.getRootLogger();
   }

   @VisibleForTesting
   static class BindLoggers implements TypeListener {

      static class AssignLoggerToField<I> implements InjectionListener<I> {
         private final org.monet.grided.control.log.Logger logger;
         private final Field field;

         AssignLoggerToField(org.monet.grided.control.log.Logger logger, Field field) {
            this.logger = logger;
            this.field = field;
         }

         public void afterInjection(I injectee) {
            try {
               field.setAccessible(true);
               field.set(injectee, logger);
            } catch (IllegalAccessException e) {
               throw new ProvisionException(e.getMessage(), e);
            }
         }
      }

      public <I> void hear(TypeLiteral<I> injectableType, TypeEncounter<I> encounter) {
         Class<? super I> type = injectableType.getRawType();
         Set<Field> loggerFields;
         if (hasInjectableConstructorWithLoggerParameter(type))
            loggerFields = getAllLoggerFieldsFrom(type);
         else
            loggerFields = getInjectableLoggerFieldsFrom(type);

         if (loggerFields.size() == 0)
            return;
         // assign the correct scope to the logger based on the classname
         org.monet.grided.control.log.Logger logger = getCorrectLoggerForType(type);

         assignLoggerAfterInjection(encounter, loggerFields, logger);
      }

      @VisibleForTesting
      static Set<Field> getInjectableLoggerFieldsFrom(Class<?> type) {
         return onlyInjectableFields(onlyLoggerFields(allFieldsFrom(type)));
      }

      @VisibleForTesting
      static Set<Field> getAllLoggerFieldsFrom(Class<?> type) {
         return onlyLoggerFields(allFieldsFrom(type));
      }

      private static Logger getCorrectLoggerForType(Class<?> type) {
         return new LoggerImpl(type.getName());
      }

      private static <I> void assignLoggerAfterInjection(TypeEncounter<I> encounter,
               Set<Field> loggerFields, org.monet.grided.control.log.Logger logger) {
         for (Field field : loggerFields) {
            encounter.register(new AssignLoggerToField<I>(logger, field));
         }
      }

      @VisibleForTesting
      static boolean hasInjectableConstructorWithLoggerParameter(Class<?> declaredType) {
         // iterate through class and superclass constructors.
         Class<?> type = declaredType;
         while (type != null) {
            for (Constructor<?> constructor : type.getConstructors()) {
               // only inject guiced constructors
               if (!constructor.isAnnotationPresent(Inject.class))
                  continue;
               if (hasLoggerParameter(constructor))
                  return true;
            }
            type = type.getSuperclass();
         }
         return false;
      }

      private static boolean hasLoggerParameter(Constructor<?> constructor) {
         return filter(Sets.newHashSet(constructor.getParameterTypes()), new Predicate<Class<?>>() {
            public boolean apply(Class<?> input) {
               return input.isAssignableFrom(org.monet.grided.control.log.Logger.class);
            }
         }).size() > 0;
      }

      private static Set<Field> allFieldsFrom(Class<?> declaredType) {
         Set<Field> fields = new HashSet<Field>();
         // find all the fields for this type.
         Class<?> type = declaredType;
         while (type != null) {
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
            type = type.getSuperclass();
         }
         return fields;
      }

      private static Set<Field> onlyInjectableFields(Set<Field> fields) {
         return filter(fields, new Predicate<Field>() {
            public boolean apply(Field input) {
               return input.isAnnotationPresent(Inject.class);
            }
         });
      }

      private static Set<Field> onlyLoggerFields(Set<Field> fields) {
         return filter(fields, new Predicate<Field>() {
            public boolean apply(Field input) {
               return input.getType().isAssignableFrom(org.monet.grided.control.log.Logger.class);
            }
         });
      }
   }
}
