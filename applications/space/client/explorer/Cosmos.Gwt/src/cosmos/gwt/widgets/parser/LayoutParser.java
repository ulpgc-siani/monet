package cosmos.gwt.widgets.parser;

public class LayoutParser {

    private final char parsedChar;
    private String render;
    private int lineLength;
    private int numberOfNewLines;

    public LayoutParser() {
        this('*');
    }

    public LayoutParser(char parsedChar) {
        this.parsedChar = parsedChar;
    }

    public ElementList process(String input) {
        if (input == null) return new ElementList();
        this.numberOfNewLines = 0;
        this.render = format(input.replaceAll("\r", "").replaceAll("\t", ""));
        if (isEmpty()) return new ElementList();
        lineLength = render.split("\n")[0].length();
        this.render = processNewLines(render);
        ElementList elements = new ElementList();
        for (int i = 0; i < render.length() && !isEmpty(); i++) {
            if (isValidChar(render.charAt(i)))
                elements.add(processChar(i));
        }
        return elements;
    }

    private String format(String input) {
        int length = getLongestLineLength(input);
        String output = "";
        for (String line : input.split("\n"))
            output += lineWithSpaces(line, length - line.length()) + "\n";
        return output.substring(0, output.length() - 1);
    }

    private int getLongestLineLength(String input) {
        int longest = 0;
        for (String line : input.split("\n"))
            if (line.length() > longest) longest = line.length();
        return longest;
    }

    private String lineWithSpaces(String line, int numberOfSpaces) {
        String output = line;
        for (int i = 0; i < numberOfSpaces; i++)
            output += " ";
        return output;
    }

    private String processNewLines(String render) {
        String result = "";
        char lastChar = '\n';
        for (char character : render.toCharArray()) {
            if (!(character == '\n' && lastChar == '\n'))
                result += character;
            else {
                result += generateSpaces(lineLength);
            }
            lastChar = character;
        }
        return result;
    }

    private String generateSpaces(int numberOfSpaces) {
        String spaces = "";
        for (int i = 0; i < numberOfSpaces; i++)
            spaces += " ";
        return spaces;
    }

    private boolean isValidChar(char character) {
        if (character == '\n')
            numberOfNewLines++;
        return character != parsedChar && character != '\n';
    }

    private Element processChar(int position) {
        if (render.charAt(position) == ' ')
            return processSpace(position);
        final String symbol = String.valueOf(render.charAt(position));
        final Dimension dimension = new Dimension(width(position), height(position, width(position)));
        final Position positionObject = new Position((position - numberOfNewLines) / lineLength, ((position - numberOfNewLines)) % lineLength);
        return new Element(symbol, dimension, positionObject);
    }

    private Element processSpace(int position) {
        final Dimension dimension = new Dimension(1 / (float) lineLength, 1);
        render.replaceFirst(" ", String.valueOf(parsedChar))    ;
        final Position positionObject = new Position((position - numberOfNewLines) / lineLength, ((position - numberOfNewLines)) % lineLength);
        return new Element(" ", dimension, positionObject);
    }

    private float width(int position) {
        float width = 0;
        int i = position;
        char character = render.charAt(position);
        String content = render;
        while (i < content.length() && content.charAt(i) == character) {
            content = parseCharAtIndex(content, i);
            width++;
            i++;
        }
        return width / lineLength;
    }

    private int height(int position, float width) {
        int height = 0;
        char character = render.charAt(position);
        boolean exit = false;
        for (int i = position; i < render.length() && !exit; i+=(lineLength + 1)) {
            for (int j = 0; j < (width * lineLength) && !exit; j++) {
                if ((j == 0) && (render.charAt(i) == character))
                    height++;
                if (render.charAt(i + j) == character)
                    render = parseCharAtIndex(render, i + j);
                else
                    exit = true;
            }
        }
        return height;
    }

    private String parseCharAtIndex(String content, int index) {
        return content.substring(0, index) + parsedChar + content.substring(index + 1);
    }

    private boolean isEmpty() {
        return ((render == null) || render.isEmpty() || renderIsParsed());
    }

    private boolean renderIsParsed() {
        for (char character : render.toCharArray())
            if (character != parsedChar && character != '\n') return false;
        return true;
    }
}
