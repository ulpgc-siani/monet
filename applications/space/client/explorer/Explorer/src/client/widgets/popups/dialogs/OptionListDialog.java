package client.widgets.popups.dialogs;

import client.widgets.toolbox.NavigableScrollPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;

public class OptionListDialog extends NavigableScrollPanel<OptionListDialog.TextOption> {

    private static final int OPTION_PADDING = 4;
    public static final int INDENTATION = 10;
    public static final int INDENTATION_OFFSET = 5;

    public OptionListDialog(String messageWhenEmpty, String loadingMessage) {
        super(messageWhenEmpty, loadingMessage);
    }

    public void add(String option) {
        addOption(new TextOption(option));
    }

    protected int calculateScrollPositionForOption() {
        return visibleRange[0] + ((OPTION_PADDING + TextOption.MARGIN) * selectedOption);
    }

    @Override
    protected void setUpOption(TextOption option) {
        option.getElement().getStyle().setPaddingLeft((INDENTATION * option.getLevel()) + INDENTATION_OFFSET, Unit.PX);
        super.setUpOption(option);
    }

    public static class TextOption extends HTML implements NavigableScrollPanel.Option {

        private static final int MARGIN = 2;
        private final String text;
        private final int indentationLevel;
        private final boolean selectable;

        public TextOption(String text) {
            this(text, true);
        }

        public TextOption(String text, boolean selectable) {
            this(text, 0, selectable);
        }

        public TextOption(String text, int indentationLevel, boolean selectable) {
            super(text);
            this.text = text;
            this.indentationLevel = indentationLevel;
            this.selectable = selectable;
            setStyleName(StyleName.OPTION);
            setTitle(text);
        }

        @Override
        public String getText() {
            return text;
        }

        public int getLevel() {
            return indentationLevel;
        }

        @Override
        public boolean isSelectable() {
            return selectable;
        }

        @Override
        public int getOffsetHeight() {
            return super.getOffsetHeight() - OPTION_PADDING;
        }
    }

    public interface OptionSelectedHandler extends NavigableScrollPanel.OptionSelectedHandler<TextOption> { }

    public interface StyleName {
        String OPTION = "option";
    }
}
