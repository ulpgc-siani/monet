package client.widgets.toolbox;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

import static client.utils.KeyBoardUtils.isEnter;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepStyles;

public class SearchBoxWidget extends HTMLPanel {

    private final TextBox searchInput = new TextBox();
    private final List<ConditionHandler> handlers = new ArrayList<>();
    private int lastConditionLength = 0;

    public SearchBoxWidget(String layout, Design design) {
        super(layout);
        addStyleName(StyleName.SEARCH_PANEL);
        addStyleName(design.style);
        init();
    }

    public void setCondition(String condition) {
        searchInput.setText(condition);
    }

    public void addConditionHandler(ConditionHandler handler) {
        handlers.add(handler);
    }

    private void init() {
        searchInput.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {

	            if (isEnter(event.getNativeKeyCode())) {
		            notifyOnEnter(searchInput.getText());
		            return;
	            }

	            if (lastConditionLength == searchInput.getText().length()) return;
	            lastConditionLength = searchInput.getText().length();
                notifyOnChange(searchInput.getText());
            }
        });
        bind();
    }

    private void bind() {
        bindWidgetToElementAndKeepStyles(this, searchInput, $(getElement()).children(toRule(StyleName.SEARCH_INPUT)).get(0));

	    onAttach();
	    RootPanel.detachOnWindowClose(this);
    }

    private void notifyOnChange(String condition) {
        for (ConditionHandler handler : handlers)
            handler.onChange(condition);
    }

	private void notifyOnEnter(String condition) {
		for (ConditionHandler handler : handlers)
			handler.onEnter(condition);
	}

    public interface ConditionHandler {
        void onChange(String condition);
        void onEnter(String condition);
    }

    public interface StyleName {
        String COMPACT = "compact";
        String EXPANDED = "expanded";
        String SEARCH_INPUT = "search-input";
        String SEARCH_PANEL = "search-panel";
    }

    public enum Design {
        COMPACT(StyleName.COMPACT), EXPANDED(StyleName.EXPANDED);

        private final String style;

        Design(String style) {
            this.style = style;
        }
    }
}
