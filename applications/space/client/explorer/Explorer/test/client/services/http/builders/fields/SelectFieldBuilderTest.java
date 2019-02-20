package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.SelectField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class SelectFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{  \n" +
            "            \"code\":\"PaisNacionalidad\",\n" +
            "            \"type\":\"select\",\n" +
            "            \"label\":\"País de nacionalidad\",\n" +
            "            \"value\":{  \n" +
            "               \"value\":\"\",\n" +
            "               \"label\":\"\",\n" +
            "               \"source\":{  \n" +
            "                  \"id\":\"1\",\n" +
            "                  \"label\":\"Lugares\",\n" +
            "                  \"type\":\"thesaurus\",\n" +
            "                  \"definition\":{  \n" +
            "                     \"code\":\"m1rsupg\",\n" +
            "                     \"name\":\"micv.TesauroLugares\",\n" +
            "                     \"type\":\"thesaurus\"\n" +
            "                  }\n" +
            "               }\n" +
            "            }\n" +
            "         }";

    private static final String JSON_WITH_VALUE = "{  \n" +
            "            \"code\":\"PaisNacionalidad\",\n" +
            "            \"type\":\"select\",\n" +
            "            \"label\":\"País de nacionalidad\",\n" +
            "            \"value\":{  \n" +
            "               \"value\":\"004\",\n" +
            "               \"label\":\"Afganistán\",\n" +
            "               \"source\":{  \n" +
            "                  \"id\":\"2\",\n" +
            "                  \"label\":\"Lugares\",\n" +
            "                  \"type\":\"thesaurus\",\n" +
            "                  \"definition\":{  \n" +
            "                     \"code\":\"m1rsupg\",\n" +
            "                     \"name\":\"micv.TesauroLugares\",\n" +
            "                     \"type\":\"thesaurus\"\n" +
            "                  }\n" +
            "               }\n" +
            "            }\n" +
            "         }";

    @Test
    public void testCreateSelectField() {
        final SelectFieldBuilder builder = new SelectFieldBuilder();
        final SelectField selectField = builder.build((HttpInstance) JsonUtils.safeEval(JSON));
        assertEquals("PaisNacionalidad", selectField.getCode());
        assertNotNull(selectField.getSource());
        assertEquals("1", selectField.getSource().getId());
    }

    @Test
    public void testCreateSelectFIeldWithValue() {
        final SelectFieldBuilder builder = new SelectFieldBuilder();
        final SelectField selectField = builder.build((HttpInstance) JsonUtils.safeEval(JSON_WITH_VALUE));
        assertEquals("PaisNacionalidad", selectField.getCode());
        assertEquals("2", selectField.getSource().getId());
        assertEquals("004", selectField.getValue().getValue());
        assertEquals("Afganistán", selectField.getValue().getLabel());
    }
}
