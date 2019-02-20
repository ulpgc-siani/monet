package client.core.system.types;

import client.core.TypeBuilder;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UriTest {

    @Test
    public void uriToString() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("http://ulpgc.es/index.php");
        assertThat(uri.toString(), is("http://ulpgc.es/index.php"));
    }

    @Test
    public void uriToStringWithoutProtocolReturnsEmptyString() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("ulpgc.es/index.php");
        assertTrue(uri.toString().isEmpty());
    }

    @Test
    public void uriToStringWithoutHostReturnsEmptyString() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("http://index.php");
        assertTrue(uri.toString().isEmpty());
    }

    @Test
    public void uriToStringWithNullPathReturnsString() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("http://ulpgc.es");
        assertThat(uri.toString(), is("http://ulpgc.es"));
    }

    @Test
    public void uriToStringWithNotDefaultPortReturnsStringWithPort() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("http://ulpgc.es:8080");
        assertThat(uri.toString(), is("http://ulpgc.es:8080"));
    }

    @Test
    public void uriToStringParsingString() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("http://ulpgc.es");
        assertThat(uri.toString(), is("http://ulpgc.es"));
    }

    @Test
    public void uriToStringParsingStringWithoutProtocolReturnsEmptyString() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("ulpgc.es");
        assertTrue(uri.toString().isEmpty());
    }

    @Test
    public void uriToStringParsingStringWithPort() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("http://ulpgc.es:8080");
        assertThat(uri.toString(), is("http://ulpgc.es:8080"));
    }

    @Test
    public void uriToStringParsingStringWithPath() {
        client.core.model.types.Uri uri = TypeBuilder.buildUri("http://ulpgc.es/index");
        assertThat(uri.toString(), is("http://ulpgc.es/index"));
    }
}
