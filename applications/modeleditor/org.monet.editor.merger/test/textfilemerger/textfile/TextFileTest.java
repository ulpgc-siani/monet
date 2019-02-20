package textfilemerger.textfile;

import textfilemerger.textfile.TextFile;
import textfilemerger.textfile.Line;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextFileTest {

    @Test
    public void emptyDocuments() {
        assertEquals(createDocument(0), createDocument(0));
    }

    @Test
    public void oneLineDocument() {
        assertEquals(createDocument(1), createDocument(1));
    }

    @Test
    public void differentSizeDocuments() {
        assertFalse(createDocument(1).equals(createDocument(2)));
    }

    @Test
    public void differentLineDocuments() {
        assertFalse(change(createDocument(10), 4).equals(createDocument(10)));
    }

    private TextFile createDocument(int lines) {
        TextFile document = new TextFile();
        for (int i = 0; i < lines; i++) {
            document.add(new Line("linea " + i));
        }
        return document;
    }

    private TextFile change(TextFile document, int... lines) {
        for (int line : lines) {
            document.set(line, new Line("otra linea"));
        }
        return document;
    }
}