package client.widgets.popups.dialogs.date;

import client.widgets.popups.DatePopupWidget;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import cosmos.types.Date;
import cosmos.types.DatePrecision;

import java.util.ArrayList;
import java.util.List;

import static client.utils.KeyBoardUtils.*;

public class TimeSelectorWidget extends HorizontalPanel {

    private static final int HOURS = 0;
    private static final int MINUTES = 1;
    private static final int SECONDS = 2;
    private final List<Integer> timeDigits;
    private final List<Integer> timeLimits;
    private final List<TextBox> timeComponents;
    private Date date;
    private DatePopupWidget.DateSelectedHandler handler;

    public void setDateSelectedHandler(DatePopupWidget.DateSelectedHandler dateSelectedHandler) {
        this.handler = dateSelectedHandler;
    }

    public TimeSelectorWidget(DatePrecision precision) {
        timeDigits = new ArrayList<>();
        timeLimits = new ArrayList<>();
        timeComponents = new ArrayList<>();
        timeLimits.add(23);
        timeLimits.add(59);
        timeLimits.add(59);
        add(createPanel(HOURS));
        if (precision == DatePrecision.MINUTE|| precision == DatePrecision.SECOND) {
            addSeparator();
            add(createPanel(MINUTES));
        }
        if (precision == DatePrecision.SECOND) {
            addSeparator();
            add(createPanel(SECONDS));
        }
        addStyleName(StyleName.TIME_PANEL);
    }

    private void addSeparator() {
        final InlineHTML separator = new InlineHTML(":");
        add(separator);
        setCellVerticalAlignment(separator, HasVerticalAlignment.ALIGN_MIDDLE);
    }

    private void setUpInput(final int timeComponent) {
        final TextBox input = timeComponents.get(timeComponent);
        input.setText("00");
        input.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                final int keyCode = event.getNativeKeyCode();
                if (isArrowLeft(keyCode) && input.getCursorPos() == 0 && timeComponent != HOURS)
                    focusPreviousComponent(timeComponent, input);
                else if (isArrowRight(keyCode) && input.getCursorPos() == input.getText().length() && timeComponent != timeDigits.size() - 1)
                    focusNextComponent(timeComponent, input);
                if (isDeleteOrBackspaceKey(keyCode) || isHorizontalArrow(keyCode)) return;
                if (!isNumberKey(keyCode)) input.cancelKey();
            }
        });
        input.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (isArrowUp(event.getNativeKeyCode())) incrementValue(timeComponent);
                if (isArrowDown(event.getNativeKeyCode())) decrementValue(timeComponent);
                if (isEnter(event.getNativeKeyCode()) || isTab(event.getNativeKeyCode())) {
                    if (Integer.valueOf(input.getText()) <= timeLimits.get(timeComponent)) {
                        timeDigits.set(timeComponent, Integer.valueOf(input.getText()));
                        if (handler != null) handler.dateSelected(selectedDate());
                        if (timeComponent != timeDigits.size() - 1) focusNextComponent(timeComponent, input);
                    } else input.setText("00");
                }
                if (isVerticalArrow(event.getNativeKeyCode())) timeComponents.get(timeComponent).cancelKey();
            }
        });
        input.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                input.selectAll();
            }
        });
        input.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (Integer.valueOf(input.getText()) <= timeLimits.get(timeComponent)) {
                    timeDigits.remove(timeComponent);
                    updateComponent(Integer.valueOf(input.getText()), timeComponent);
                } else {
                    input.setText(formatValue(timeDigits.get(timeComponent)));
                }
            }
        });
    }

    private void focusPreviousComponent(int timeComponent, TextBox input) {
        timeComponents.get(timeComponent - 1).setFocus(true);
        timeComponents.get(timeComponent - 1).setCursorPos(timeComponents.get(timeComponent - 1).getText().length());
        input.cancelKey();
    }

    private void focusNextComponent(int timeComponent, TextBox input) {
        timeComponents.get(timeComponent + 1).setFocus(true);
        timeComponents.get(timeComponent + 1).setCursorPos(0);
        input.cancelKey();
    }

    private void incrementValue(int timeComponent) {
        final Integer value = Integer.valueOf(timeComponents.get(timeComponent).getText());
        if (!value.equals(timeLimits.get(timeComponent)))
            updateComponent(timeDigits.remove(timeComponent) + 1, timeComponent);
        else if (timeComponent != HOURS && !Integer.valueOf(timeComponents.get(timeComponent - 1).getText()).equals(timeLimits.get(timeComponent - 1))) {
            timeDigits.remove(timeComponent);
            updateComponent(0, timeComponent);
            updateComponent(timeDigits.remove(timeComponent - 1) + 1, timeComponent - 1);
        }
    }

    private void decrementValue(int timeComponent) {
        final Integer value = Integer.valueOf(timeComponents.get(timeComponent).getText());
        if (value != 0)
            updateComponent(timeDigits.remove(timeComponent) - 1, timeComponent);
        else if (timeComponent != HOURS && !Integer.valueOf(timeComponents.get(timeComponent - 1).getText()).equals(0)) {
            timeDigits.remove(timeComponent);
            updateComponent(timeLimits.get(timeComponent), timeComponent);
            updateComponent(timeDigits.remove(timeComponent - 1) - 1, timeComponent - 1);
        }
    }

    private VerticalPanel createPanel(int timePosition) {
        timeDigits.add(0);
        timeComponents.add(new TextBox());
        setUpInput(timePosition);
        VerticalPanel panel = new VerticalPanel();
        panel.add(createArrowUp(timePosition));
        panel.add(timeComponents.get(timePosition));
        panel.add(createArrowDown(timePosition));
        return panel;
    }

    public void setDate(Date date) {
        this.date = date;
        timeDigits.set(0, date.getHours());
        timeComponents.get(HOURS).setText(formatValue(timeDigits.get(0)));
        if (timeDigits.size() > 1) {
            timeDigits.set(1, date.getMinutes());
            timeComponents.get(MINUTES).setText(formatValue(timeDigits.get(1)));
        }
        if (timeDigits.size() > 2) {
            timeDigits.set(2, date.getSeconds());
            timeComponents.get(SECONDS).setText(formatValue(timeDigits.get(2)));
        }
    }

    private String formatValue(int value) {
        return (value < 10 ? "0" : "") + String.valueOf(value);
    }

    private Anchor createArrowUp(final int timePosition) {
        final Anchor arrowUp = createArrow(StyleName.ARROW_UP);
        arrowUp.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                incrementValue(timePosition);
            }
        });
        return arrowUp;
    }

    private Anchor createArrowDown(final int timePosition) {
        final Anchor arrowDown = createArrow(StyleName.ARROW_DOWN);
        arrowDown.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                decrementValue(timePosition);
            }
        });
        return arrowDown;
    }

    private Anchor createArrow(final String arrowStyle) {
        return new Anchor(){{
            addStyleName(arrowStyle);
        }};
    }

    private void updateComponent(int value, int timePosition) {
        timeDigits.add(timePosition, value);
        timeComponents.get(timePosition).setText(formatValue(value));
        if (handler != null) handler.dateSelected(selectedDate());
    }

    private Date selectedDate() {
        if (timeDigits.size() == 3) return new Date(date.getYear(), date.getMonth(), date.getDay(), timeDigits.get(0), timeDigits.get(1), timeDigits.get(2));
        if (timeDigits.size() == 2) return new Date(date.getYear(), date.getMonth(), date.getDay(), timeDigits.get(0), timeDigits.get(1));
        return new Date(date.getYear(), date.getMonth(), date.getDay(), timeDigits.get(0));
    }

    public interface StyleName {
        String TIME_PANEL = "time-panel";
        String ARROW_UP = "up-arrow";
        String ARROW_DOWN = "down-arrow";
    }
}
