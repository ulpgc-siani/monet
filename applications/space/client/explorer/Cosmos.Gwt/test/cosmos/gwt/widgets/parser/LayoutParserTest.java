package cosmos.gwt.widgets.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LayoutParserTest {

    private LayoutParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new LayoutParser();
    }

    @Test
    public void parseNullReturnsEmptyList() {
        List<Element> elements = parser.process(null);
        assertThat(elements, is(Collections.<Element>emptyList()));
    }

    @Test
    public void parseEmptyStringReturnsEmptyList() {
        List<Element> elements = parser.process("");
        assertThat(elements, is(Collections.<Element>emptyList()));
    }

    @Test
    public void parseStringWithStarsReturnsEmptyList() {
        List<Element> elements = parser.process("***");
        assertThat(elements, is(Collections.<Element>emptyList()));
    }

    @Test
    public void parseStringWithOneAReturnsOneElementInList() {
        List<Element> elements = parser.process("A");
        assertThat(elements, is(Collections.singletonList(
                        new Element("A", new Dimension(1, 1), new Position(0, 0))))
        );
    }

    @Test
    public void parseStringWithFourAReturnsOneElementWithDimension4And1() {
        List<Element> elements = parser.process("AAAA");
        assertThat(elements, is(Collections.singletonList(
                        new Element("A", new Dimension(1, 1), new Position(0, 0))))
        );
    }

    @Test
    public void parseStringWithTwoLinesReturnsOneElementWithHeight2() {
        List<Element> elements = parser.process("AAAA\nAAAA");
        assertThat(elements, is(Collections.singletonList(
                        new Element("A", new Dimension(1, 2), new Position(0, 0))))
        );
    }

    @Test
    public void parseStringWithTwoLinesWithDifferentElementsReturnsAListWith2Elements() {
        List<Element> elements = parser.process("AAAA\nBBBB");
        assertThat(elements, is(Arrays.asList(
                new Element("A", new Dimension(1, 1), new Position(0, 0)),
                new Element("B", new Dimension(1, 1), new Position(1, 0))))
        );
    }

    @Test
    public void parseStringWithTwoElementsOfDifferentDimension() {
        List<Element> elements = parser.process("AAAA\nBB");
        assertThat(elements, is(Arrays.asList(
                new Element("A", new Dimension(1, 1), new Position(0, 0)),
                new Element("B", new Dimension(0.5f, 1), new Position(1, 0)),
                new Element(" ", new Dimension(0.25f, 1), new Position(1, 2)),
                new Element(" ", new Dimension(0.25f, 1), new Position(1, 3))))
        );
    }

    @Test
    public void parseStringWithSameElementSeparatedByAnotherElement() {
        List<Element> elements = parser.process("AAAA\nBB  \nAAAA");
        assertThat(elements, is(Arrays.asList(
                new Element("A", new Dimension(1.0f, 1), new Position(0, 0)),
                new Element("B", new Dimension(0.5f, 1), new Position(1, 0)),
                new Element(" ", new Dimension(0.25f, 1), new Position(1, 2)),
                new Element(" ", new Dimension(0.25f, 1), new Position(1, 3)),
                new Element("A", new Dimension(1.0f, 1), new Position(2, 0))))
        );
    }

    @Test
    public void parseStringWithElementsInDifferentColumns() {
        List<Element> elements = parser.process("AAAA\nBBCC");
        assertThat(elements, is(Arrays.asList(
                new Element("A", new Dimension(1.0f, 1), new Position(0, 0)),
                new Element("B", new Dimension(0.5f, 1), new Position(1, 0)),
                new Element("C", new Dimension(0.5f, 1), new Position(1, 2))))
        );
    }

    @Test
    public void emptyLineBetweenLinesWithCharactersRepresentsSpace() {
        List<Element> elements = parser.process("AAAA\n\n\nBBCC");
        assertThat(elements, is(Arrays.asList(
                        new Element("A", new Dimension(1.0f, 1), new Position(0, 0)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(1, 0)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(1, 1)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(1, 2)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(1, 3)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(2, 0)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(2, 1)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(2, 2)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(2, 3)),
                        new Element("B", new Dimension(0.5f, 1), new Position(3, 0)),
                        new Element("C", new Dimension(0.5f, 1), new Position(3, 2))
                ))
        );
    }

    @Test
    public void addSpacesAtTheEndOfLineIfLineLengthIsNotEqualToMaxLength() {
        List<Element> elements = parser.process("A\nBBCC");
        assertThat(elements, is(Arrays.asList(
                        new Element("A", new Dimension(0.25f, 1), new Position(0, 0)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(0, 1)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(0, 2)),
                        new Element(" ", new Dimension(0.25f, 1), new Position(0, 3)),
                        new Element("B", new Dimension(0.5f, 1), new Position(1, 0)),
                        new Element("C", new Dimension(0.5f, 1), new Position(1, 2))
                ))
        );
    }
}
