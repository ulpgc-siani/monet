package org.monet.grided.control.injectors;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.monet.grided.control.guice.annotations.InjectLogger;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class Log4jTypeListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
            if (field.getType() == Logger.class && field.isAnnotationPresent(InjectLogger.class)) {
                typeEncounter.register(new Log4jMembersInjector<I>(field));
            }
        }      
    }
}

