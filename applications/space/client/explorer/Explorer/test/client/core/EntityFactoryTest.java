package client.core;

import client.core.EntityBuilder.EntityFactory;
import client.core.model.fields.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EntityFactoryTest {

    @Test
    public void testCreateFieldByClassName() {
        EntityFactory factory = new EntityFactory();
        assertTrue(factory.createFieldByClassName("code", "label", CheckField.CLASS_NAME) instanceof CheckField);
        assertTrue(factory.createFieldByClassName("code", "label", CompositeField.CLASS_NAME) instanceof CompositeField);
        assertTrue(factory.createFieldByClassName("code", "label", DateField.CLASS_NAME) instanceof DateField);
        assertTrue(factory.createFieldByClassName("code", "label", FileField.CLASS_NAME) instanceof FileField);
        assertTrue(factory.createFieldByClassName("code", "label", LinkField.CLASS_NAME) instanceof LinkField);
        assertTrue(factory.createFieldByClassName("code", "label", MemoField.CLASS_NAME) instanceof MemoField);
        assertTrue(factory.createFieldByClassName("code", "label", NumberField.CLASS_NAME) instanceof NumberField);
        assertTrue(factory.createFieldByClassName("code", "label", PictureField.CLASS_NAME) instanceof PictureField);
        assertTrue(factory.createFieldByClassName("code", "label", SelectField.CLASS_NAME) instanceof SelectField);
        assertTrue(factory.createFieldByClassName("code", "label", SerialField.CLASS_NAME) instanceof SerialField);
        assertTrue(factory.createFieldByClassName("code", "label", TextField.CLASS_NAME) instanceof TextField);
    }
}
