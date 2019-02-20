package client.widgets.toolbox;

import client.core.model.List;
import client.core.system.MonetList;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

import static com.google.gwt.dom.client.Style.Unit;

public abstract class NavigableScrollPanel<T extends NavigableScrollPanel.Option> extends ScrollPanel {

    private static final int NO_OPTION_SELECTED = -1;
    private final HTMLPanel container = new HTMLPanel("");

    private final String emptyMessage;
    private final String loadingMessage;
    protected final List<T> options = new MonetList<>();

    private HTML loadingLabel;
    protected int selectedOption = NO_OPTION_SELECTED;
    protected OptionSelectedHandler<T> optionSelectedHandler;
    protected int[] visibleRange = new int[2];

    public NavigableScrollPanel(String emptyMessage, String loadingMessage) {
        this.emptyMessage = emptyMessage;
        this.loadingMessage = loadingMessage;
        container.addStyleName(StyleName.CONTAINER);
        add(container);
        addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {
                if (isLoading())
                    loadingLabel.getElement().getStyle().setTop(getVerticalScrollPosition() + getOffsetHeight() - 20, Unit.PX);
            }
        });
    }

    public void showLoading() {
        if (isLoading()) return;
        loadingLabel = new HTML(loadingMessage);
        loadingLabel.addStyleName(StyleName.LOADING);
        container.add(loadingLabel);
        loadingLabel.getElement().getStyle().setTop(getVerticalScrollPosition() + getOffsetHeight() - 20, Unit.PX);
    }

    public void hideLoading() {
        if (!isLoading()) return;
        container.remove(loadingLabel);
        loadingLabel = null;
    }

    public boolean hasSelection() {
        return selectedOption != NO_OPTION_SELECTED;
    }

    public void refreshOptions(List<T> newOptions) {
        container.clear();
        options.clear();
        if (newOptions.isEmpty())
            showMessageEmpty();
        else {
            for (T option : newOptions)
                addOption(option);
            noOptionSelected();
        }
    }

    public void refreshOptionsAndHideScroll(List<T> options) {
        refreshOptions(options);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                setHeight(container.getOffsetHeight() + "px");
            }
        });
    }

    public void showMessage(String message) {
        container.clear();
        options.clear();
        Label label = new Label(message);
        label.addStyleName(StyleName.MESSAGE);
        container.add(label);
    }

    private void showMessageEmpty() {
        showMessage(emptyMessage);
    }

    public void addOptions(List<T> options) {
        for (T option : options)
            addOption(option);
        if (options.isEmpty()) showMessageEmpty();
    }

    public void addOption(T option) {
        options.add(option);
        container.add(options.get(options.size() - 1));
        setUpOption(option);
    }

    protected void setUpOption(final T option) {
        option.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!option.isSelectable() || optionSelectedHandler == null) return;
                selectedOption = options.indexOf(option);
                refreshSelected();
                optionSelectedHandler.onSelected(option);
            }
        });
    }

    public void selectBottom() {
        selectedOption++;
        if (selectedOption == options.size()) selectedOption = 0;
        refreshView();
    }

    public void selectUpper() {
        selectedOption--;
        if (selectedOption == -1) selectedOption = options.size() - 1;
        refreshView();
    }

    public void removeAll() {
        options.clear();
        container.clear();
    }

    public void select(int option) {
        selectedOption = option;
        refreshView();
    }

    public T getOption(int index) {
        return options.get(index);
    }

    public void setOptionSelectedHandler(NavigableScrollPanel.OptionSelectedHandler<T> optionSelectedHandler) {
        this.optionSelectedHandler = optionSelectedHandler;
    }

    public void noOptionSelected() {
        selectedOption = NO_OPTION_SELECTED;
        focusNone();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                resetScroll();
            }
        });
    }

    public boolean isFirstSelected() {
        return selectedOption <= 0;
    }

    public boolean isLastSelected() {
        return selectedOption == options.size() - 1;
    }

    public T getSelectedOption() {
        if (selectedOption == NO_OPTION_SELECTED || !getOption(selectedOption).isSelectable())
            return null;
        return getOption(selectedOption);
    }

    @Override
    public void clear() {
        container.clear();
        selectedOption = NO_OPTION_SELECTED;
        loadingLabel = null;
        options.clear();
    }

    protected void refreshView() {
        refreshSelected();
        if (visibleRangeIsInvalid())
            resetScroll();
        while (!isVisible(options.get(selectedOption)))
            scroll(options.get(selectedOption).getOffsetHeight() * selectedOption);
    }

    private boolean visibleRangeIsInvalid() {
        return visibleRange[1] <= visibleRange[0];
    }

    private boolean isLoading() {
        return loadingLabel != null;
    }

    private void refreshSelected() {
        int index = 0;
        for (T option : options)
            focusIfIsSelected(index++, option);
    }

    private boolean isVisible(T option) {
        final int height = option.getOffsetHeight();
        return visibleRange[0] <= height * selectedOption && height * selectedOption < (visibleRange[1] - height);
    }

    private void scroll(int selectedOptionVerticalPosition) {
        if (visibleRange[0] >= selectedOptionVerticalPosition)
            scrollUp(options.get(selectedOption).getOffsetHeight());
        else
            scrollDown(options.get(selectedOption).getOffsetHeight());
        setVerticalScrollPosition(calculateScrollPositionForOption());
    }

    protected abstract int calculateScrollPositionForOption();

    private void scrollUp(int scroll) {
        visibleRange[0] -= scroll;
        visibleRange[1] -= scroll;
    }

    private void scrollDown(int scroll) {
        visibleRange[0] += scroll;
        visibleRange[1] += scroll;
    }

    private void focusIfIsSelected(int index, T option) {
        if (index == selectedOption)
            option.addStyleName(StyleName.SELECTED);
        else
            option.removeStyleName(StyleName.SELECTED);
    }

    protected void resetScroll() {
        setVerticalScrollPosition(0);
        visibleRange[0] = 0;
        visibleRange[1] = getElement().getClientHeight() - 20;
    }

    private void focusNone() {
        refreshSelected();
    }

    public interface StyleName {
        String CONTAINER = "container";
        String LOADING = "loading";
        String MESSAGE = "message";
        String SELECTED = "selected";
    }

    public interface Option extends IsWidget, HasClickHandlers {
        boolean isSelectable();
        void addStyleName(String styleName);
        void removeStyleName(String styleName);
        int getOffsetHeight();
    }

    public interface OptionSelectedHandler<T extends Option> {
        void onSelected(T option);
    }
}
