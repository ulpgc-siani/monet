package client.core.system.fields;

import client.core.system.MonetList;
import client.core.system.types.Composite;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldTest {

    @Test
    public void testResolveSimpleField() {
        TextField field = new TextField();
        field.setCode("text");

        assertEquals("text", field.getPath());
    }

    @Test
    public void testResolveMultipleField() {
        MultipleTextField field = new MultipleTextField();
        TextField field1 = new TextField("text", "");
        field.setCode("text");

        field.add(new TextField("text", "text"));
        field.add(field1);

        assertEquals("text", field.getPath());
    }

    @Test
    public void testResolveMultipleFieldItem() {
        MultipleTextField field = new MultipleTextField();
        field.setCode("text");

        field.add(new TextField("text", ""));
        field.add(new TextField("text", ""));

        assertEquals("text:0", field.get(0).getPath());
        assertEquals("text:1", field.get(1).getPath());
    }

    @Test
    public void testResolveCompositeField() {
        CompositeField field = new CompositeField();
        field.setCode("composite");

        field.setValue(new Composite() {{
            add(new TextField("text", ""));
            add(new BooleanField("boolean", ""));
        }});

        assertEquals("composite/text", field.getValue().get(0).getPath());
        assertEquals("composite/boolean", field.getValue().get(1).getPath());
    }

    @Test
    public void testResolveCompositeFieldWithMultipleField() {
        CompositeField field = new CompositeField();
        field.setCode("composite");

        field.setValue(new Composite() {{
            add(new TextField("text", ""));
            add(new MultipleSelectField("select", "", new MonetList<client.core.model.fields.SelectField>() {{
                add(new SelectField("select", ""));
                add(new SelectField("select", ""));
                add(new SelectField("select", ""));
                add(new SelectField("select", ""));
                add(new SelectField("select", ""));
            }}));
        }});

        assertEquals("composite/text", field.getValue().get(0).getPath());
        assertEquals("composite/select", field.getValue().get(1).getPath());
        assertEquals("composite/select:3", ((MultipleField)field.getValue().get(1)).get(3).getPath());
    }

    @Test
    public void testResolveCompositeFieldWithMultipleCompositeField() {
        CompositeField field = new CompositeField();
        field.setCode("composite");

        field.setValue(new Composite() {{
            add(new TextField("text", ""));
            add(new MultipleCompositeField("composite", "", new MonetList<client.core.model.fields.CompositeField>() {{
                add(new CompositeField("composite", "") {{
                    setValue(new Composite() {{
                        add(new TextField("text", ""));
                        add(new TextField("boolean", ""));
                    }});
                }});
                add(new CompositeField("composite", "") {{
                    setValue(new Composite() {{
                        add(new TextField("text", ""));
                        add(new TextField("boolean", ""));
                    }});
                }});
            }}));
        }});

        assertEquals("composite/text", field.getValue().get(0).getPath());
        assertEquals("composite/composite", field.getValue().get(1).getPath());
        assertEquals("composite/composite:0/text", ((MultipleCompositeField)field.getValue().get(1)).get(0).getValue().get(0).getPath());
        assertEquals("composite/composite:0/boolean", ((MultipleCompositeField)field.getValue().get(1)).get(0).getValue().get(1).getPath());
        assertEquals("composite/composite:1/text", ((MultipleCompositeField)field.getValue().get(1)).get(1).getValue().get(0).getPath());
        assertEquals("composite/composite:1/boolean", ((MultipleCompositeField)field.getValue().get(1)).get(1).getValue().get(1).getPath());
    }
}
