package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.definition.entity.field.DateFieldDefinition.Precision;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

import static client.core.model.definition.entity.field.DateFieldDefinition.*;

public class DateFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"meeachq\",\"name\":\"FieldDateName\",\"label\":\"Campo fecha\",\"type\":\"date\",\"displays\":{\"totalCount\":0,\"items\":[]}, \"allowLessPrecision\": false}";
    private static final String JSON_ALLOW_LESS_PRECISION = "{\"code\":\"meeachq\",\"name\":\"FieldDateName\",\"label\":\"Campo fecha\",\"type\":\"date\",\"displays\":{\"totalCount\":0,\"items\":[]}, \"allowLessPrecision\": true}";
    private static final String JSON_PURPOSE_AND_PRECISION = "{\"code\":\"meeachq\",\"name\":\"FieldDateName\",\"label\":\"Campo fecha\",\"type\":\"date\",\"displays\":{\"totalCount\":0,\"items\":[]},\"precision\":\"DAYS\",\"purpose\":\"NEAR_DATE\"}";

    @Test
    public void testBuildDateFieldDefinition() {
        final DateFieldDefinitionBuilder builder = new DateFieldDefinitionBuilder();
        final DateFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("meeachq", definition.getCode());
        assertEquals("FieldDateName", definition.getName());
        assertEquals("Campo fecha", definition.getLabel());
        assertFalse(definition.allowLessPrecision());
    }

    @Test
    public void testBuildDateFieldDefinitionWithAllowLessPrecision() {
        final DateFieldDefinitionBuilder builder = new DateFieldDefinitionBuilder();
        final DateFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON_ALLOW_LESS_PRECISION));

        assertEquals("meeachq", definition.getCode());
        assertEquals("FieldDateName", definition.getName());
        assertEquals("Campo fecha", definition.getLabel());
        assertTrue(definition.allowLessPrecision());
    }

    @Test
    public void testBuildDateFieldDefinitionWithPurposeAndPrecision() {
        final DateFieldDefinitionBuilder builder = new DateFieldDefinitionBuilder();
        final DateFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON_PURPOSE_AND_PRECISION));

        assertEquals("meeachq", definition.getCode());
        assertEquals("FieldDateName", definition.getName());
        assertEquals("Campo fecha", definition.getLabel());
        assertEquals(Purpose.NEAR_DATE, definition.getPurpose());
        assertEquals(Precision.DAYS, definition.getPrecision());
    }
}
