package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.MemoField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class MemoFieldBuilderTest extends ApplicationTestCase {

    public static final String JSON = "{\"code\":\"mt_f_cg\",\"type\":\"memo\",\"label\":\"Campo memo\",\"value\":\"\"}";

    @Test
    public void testBuildMemoField() {
        final MemoFieldBuilder builder = new MemoFieldBuilder();
        final MemoField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mt_f_cg", field.getCode());
        assertEquals("Campo memo", field.getLabel());
        assertEquals("", field.getValue());
    }
}
