package textfilemerger.textfile;

import org.junit.Test;
import static org.junit.Assert.*;

public class LineTest {

    @Test
    public void testEqualEmpty() {
        assertEquals(new Line(""), new Line(""));
    }

    @Test
    public void testEqualLines() {
        assertEquals(new Line("Hola"), new Line("Hola"));
    }

    @Test
    public void testNotEqual() {
        assertFalse(new Line("Hola").equals(new Line("Hola!")));
    }

    @Test
    public void testEqualLinesWithSpaces() {
        assertEquals(new Line("Hola "), new Line(" Hola"));
    }

    @Test
    public void testEqualLinesWithTabs() {
        assertEquals(new Line("Hola\tmundo"), new Line("\tHola mundo"));
    }

    @Test
    public void testRaw() {
        assertEquals(new Line("holamundo"), new Line("HOLA \t\t MUN //DO").toString());
    }
    
    @Test
    public void testEqualLinesIgnoreCase() {
        assertEquals(new Line("HOLA\t\tMUNDO"), new Line("hola mundo\t\t"));
    }
    
    @Test
    public void testCommentLine() {
        assertEquals("//hola mundo", new Line("hola mundo").withComment().toString());
    }    

    @Test
    public void testCommentEmptyLine() {
        assertEquals("", new Line("").withComment().toString());
    }    
    
    @Test
    public void testCommentBlankLine() {
        assertEquals("", new Line("          ").withComment().toString());
    }    
	
    @Test
    public void testCommentLineWithTabs() {
        assertEquals("\t\t //hola mundo", new Line("\t\t hola mundo").withComment().toString());
    }    

}