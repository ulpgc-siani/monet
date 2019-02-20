package org.monet.editor.merger.textfilemerger.textfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextFileReader {

    private BufferedReader reader;

    public TextFileReader(InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
    }

    public TextFile read() {
        TextFile document = new TextFile();
        while (hasNext()) {
            document.add(readLine());
        }
        return document;
    }

    private Line readLine() {
        try {
            return new Line(reader.readLine());
        } 
        catch (IOException ex) {
            return null;
        }
    }

    private boolean hasNext() {
        try {
            return this.reader.ready();
        } catch (IOException ex) {
            return false;
        }
    }
}
