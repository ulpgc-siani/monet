package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.DateField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;

public class DateFieldBuilderTest extends ApplicationTestCase {

    public static final String JSON = "{\"code\": \"FechaNacimiento\", \"type\": \"date\", \"label\": \"Fecha de nacimiento\", \"value\":\"709171200000\", \"formattedValue\":\"22/06/1992\"}";

    public void testDateFieldIsBuiltUsingOnlyFormattedValue() {
        DateFieldBuilder builder = new DateFieldBuilder();
        final DateField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("FechaNacimiento", field.getCode());
        assertEquals("Fecha de nacimiento", field.getLabel());
        assertEquals("22/06/1992", field.getValueAsString());
        assertNull(field.getValue());
    }
}
