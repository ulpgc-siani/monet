package org.monet.editor.merger.textfilemerger.textfile;

import java.util.ArrayList;
import java.util.Iterator;

public class TextFile extends ArrayList<Line> {

	private static final long serialVersionUID = 1L;

	public TextFile() {
    }

    @Override
    public boolean equals(Object object) {
        TextFile textFile = cast(object);
        if (textFile == null) {
            return false;
        }
        if (size() != textFile.size()) {
            return false;
        }
        return checkLinesEqual(textFile.iterator());
    }

    private TextFile cast(Object object) {
        if (object instanceof TextFile) {
            return (TextFile) object;
        }
        return null;
    }

    private boolean checkLinesEqual(Iterator<Line> iterator) {
        for (Line line : this) {
            if (!line.equals(iterator.next())) {
                return false;
            }
        }
        return true;
    }
	
	@Override
	public String toString() {
		String result = "";
		for (Line line : this)
			result += line.getText() + '\n';
		return result;
	}
}
