package org.monet.grided.control.injectors;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.google.inject.MembersInjector;

public class Log4jMembersInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final Logger logger;

    Log4jMembersInjector(Field field) {
        this.field = field;
        this.logger = Logger.getLogger(field.getDeclaringClass());
        field.setAccessible(true);
    }
    
    @Override
    public void injectMembers(T t) {
        try {
            field.set(t,  logger);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}

