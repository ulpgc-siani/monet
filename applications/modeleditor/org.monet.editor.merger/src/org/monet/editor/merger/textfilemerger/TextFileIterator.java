package org.monet.editor.merger.textfilemerger;

import org.monet.editor.merger.textfilemerger.textfile.Line;
import org.monet.editor.merger.textfilemerger.textfile.TextFile;

public class TextFileIterator {

    private TextFile textFile;
    private int index = 0;

    public TextFileIterator(TextFile textFile) {
        this.textFile = textFile;
        this.index = 0;
    }

    public Line line() {
		if (index >= textFile.size()) return null;
        return this.textFile.get(index);
    }

    public int getIndex() {
        return index;
    }

    public boolean isFinished() {
        return this.index >= this.textFile.size();
    }

    public void next() {
        this.index++;
    }
}
