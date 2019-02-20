package textfilemerger;

import textfilemerger.textfile.TextFile;
import textfilemerger.textfile.TextFileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextFileMergerTest {

    @Test
    public void mergeSimpleGenericBigger() {
        String test = "1.GenericBigger";
        assertEquals(getResult(test), merge(test));
    }
	
    @Test
    public void mergeSimpleSpecificBigger() {
        String test = "1.SpecificBigger";
        assertEquals(getResult(test), merge(test));
    }	

    @Test
    public void mergeEqualDefinitions() {
        String test = "2.EqualDefinitions";
        assertEquals(getResult(test), merge(test));
    }

    @Test
    public void modifyLabel() {
        String test = "3.ModifyLabel";
        assertEquals(getResult(test).toString(), merge(test).toString());
    }

    @Test
    public void addRoleInSpecific() {
        String test = "4.AddRoleInSpecific";
        assertEquals(getResult(test), merge(test));
    }

    @Test
    public void addRoleInGeneric() {
        String test = "5.AddRoleInGeneric";
        assertEquals(getResult(test), merge(test));
    }

    @Test
    public void removeRoleInGeneric() {
        String test = "6.RemoveRoleInGeneric";
        assertEquals(getResult(test), merge(test));
    }

    private TextFile merge(String test) {
		TextFile textFile = new TextFileMerger(getSpecific(test), getGeneric(test)).execute();
		saveFile(test, textFile);
        return textFile;
    }

    private TextFile getGeneric(String test) {
        return getTextFile(getStream(test, "generic.mml"));
    }

    private TextFile getSpecific(String test) {
        return getTextFile(getStream(test, "specific.mml"));
    }

    private TextFile getResult(String test) {
        return getTextFile(getStream(test, "specific.result.mml"));
    }

    private TextFile getTextFile(InputStream stream) {
        return new TextFileReader(stream).read();
    }

    private InputStream getStream(String test, String fileName) {
        try {
            return new FileInputStream(new File("testfiles/" + test + "/" + fileName));
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

	private void saveFile(String test, TextFile merge) {
		PrintWriter out = null;
		try {
			out = new PrintWriter("testfiles/" + test + "/specific.saved.mml");
			out.print(merge.toString());
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		finally {
                    if (out != null) out.close();
		}
	}
}