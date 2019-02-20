package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.FileField;
import client.core.model.fields.MultipleFileField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class FileFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mknyzsa\",\"type\":\"file\",\"label\":\"Campo file\"}";
    private static final String JSON_WITH_MULTIPLE_NULL_VALUES = "{\"code\":\"UNIAcreditarIndexada\",\"type\":\"file\",\"multiple\":true,\"label\":\"Acreditar\",\"value\":[null,null,null]}";

    @Test
    public void testBuildFileField() {
        FileFieldBuilder builder = new FileFieldBuilder();
        FileField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mknyzsa", field.getCode());
        assertEquals("Campo file", field.getLabel());
    }

    @Test
    public void testBuildMultipleFileFieldWithNullValues() {
        MultipleFileFieldBuilder builder = new MultipleFileFieldBuilder();
        MultipleFileField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON_WITH_MULTIPLE_NULL_VALUES));

        assertTrue(field.isNullOrEmpty());
    }
}
