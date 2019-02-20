package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.CheckField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class CheckFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON_INLINE = "{\"code\":\"mltszxq\",\"type\":\"check\",\"label\":\"Campo check\",\"value\":{\"checks\":{\"totalCount\":0,\"items\":[]}}}";
    private static final String JSON_SOURCE = "{\"code\":\"mcygzhg\",\"type\":\"check\",\"label\":\"Campo check source\",\"value\":{\"checks\":{\"totalCount\":0,\"items\":[]},\"source\":{\"id\":\"8\",\"label\":\"Categorías A\",\"type\":\"thesaurus\",\"definition\":{\"code\":\"m60by_a\",\"name\":\"micv.TesauroCategoriaA\",\"type\":\"thesaurus\"}}}}";

    @Test
    public void testBuildCheckField() {
        final CheckFieldBuilder builder = new CheckFieldBuilder();
        final CheckField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON_INLINE));

        assertEquals("mltszxq", field.getCode());
        assertEquals("Campo check", field.getLabel());
        assertTrue(field.getValue().isEmpty());
    }

    @Test
    public void testBuildCheckFieldWithSource() {
        final CheckFieldBuilder builder = new CheckFieldBuilder();
        final CheckField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON_SOURCE));

        assertEquals("mcygzhg", field.getCode());
        assertEquals("Campo check source", field.getLabel());
        assertTrue(field.getValue().isEmpty());
        assertNotNull(field.getSource());
        assertEquals("8", field.getSource().getId());
        assertEquals("Categorías A", field.getSource().getLabel());
        assertEquals("m60by_a", field.getSource().getDefinition().getCode());
        assertEquals("micv.TesauroCategoriaA", field.getSource().getDefinition().getName());
    }
}
