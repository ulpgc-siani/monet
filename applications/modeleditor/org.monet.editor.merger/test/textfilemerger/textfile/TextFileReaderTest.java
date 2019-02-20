package textfilemerger.textfile;

import textfilemerger.textfile.TextFile;
import textfilemerger.textfile.TextFileReader;
import textfilemerger.textfile.Line;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextFileReaderTest {

    @Test
    public void modifyLabel() {
        assertEquals(createDocument(), readDocument("3.ModifyLabel"));
    }

    private TextFile createDocument() {
        TextFile document = new TextFile();
        document.add(new Line("package com.prueba {"));
        document.add(new Line(""));
        document.add(new Line("[\"mjcrqzq\"]"));
        document.add(new Line("definition Home is desktop {"));
        document.add(new Line("label = \"Inicio\";"));
        document.add(new Line(""));
        document.add(new Line("for {"));
        document.add(new Line("role = ref common.User;"));
        document.add(new Line("}"));
        document.add(new Line(""));
        document.add(new Line("[\"mdzqsnw\"]"));
        document.add(new Line("view {"));
        document.add(new Line("label =\"Last news\";"));
        document.add(new Line("is-default;"));
        document.add(new Line("show {"));
        document.add(new Line("news;"));
        document.add(new Line("}"));
        document.add(new Line("}"));
        document.add(new Line("}"));
        document.add(new Line(""));
        document.add(new Line("}"));
        return document;
    }

    private TextFile readDocument(String test) {
        TextFileReader reader = new TextFileReader(getStream(test, "specific.mml"));
        return reader.read();
    }

    private InputStream getStream(String test, String fileName) {
        try {
            return new FileInputStream(new File("testfiles/" + test + "/" + fileName));
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
}