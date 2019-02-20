package org.monet.editor.merger.textfilemerger.textfile;

public class Line {

    private final static String WHITE_SPACES = " " + '\t';
    private final String text;
    private final String hash;

    public Line(String text) {
        this.text = text;
        this.hash = clean(text);
    }

    public String getText() {
        return text;
    }

    public boolean isEmpty() {
        return hash.isEmpty();
    }

    @Override
    public boolean equals(Object object) {
        Line line = cast(object);
        if (line == null)
            return false;
        return hash.equals(line.hash);
    }

    @Override
    public String toString() {
        return text;
    }

    public Line withComment() {
        return new Line(putComment(text));
    }

    private Line cast(Object object) {
        if (object instanceof Line)
            return (Line) object;
        if (object instanceof String)
            return new Line((String) object);
        return null;
    }

    private String clean(String text) {
        return text.replaceAll("[\\s|/t|//]", "").toLowerCase();
    }

    private int firstNonWhiteSpace(String text) {
        for (int i = 0; i < text.length(); i++)
            if (isWhiteSpace(text.charAt(i)))
                return i;
        return -1;

    }

    private String putComment(String text) {
        int index = firstNonWhiteSpace(text);
        if (index < 0)
			return "";
		return text.substring(0, index) + "//" + text.substring(index);
    }

    private boolean isWhiteSpace(char charAt) {
        return WHITE_SPACES.indexOf(charAt) < 0;
    }
}
