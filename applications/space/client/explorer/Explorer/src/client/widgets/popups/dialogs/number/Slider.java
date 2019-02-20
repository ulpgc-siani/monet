package client.widgets.popups.dialogs.number;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static client.utils.KeyBoardUtils.isArrowRight;
import static client.utils.KeyBoardUtils.isHorizontalArrow;
import static com.google.gwt.dom.client.Style.Unit;

public class Slider extends FocusPanel implements HasValue<java.lang.Number> {

    private final HTMLPanel container;
    private final KeyTimer keyTimer;
    private final List<Label> labels = new ArrayList<>();
    private final List<HTML> ticks = new ArrayList<>();
    private final Map<Integer, Function> eventFunctions;
    private HTML line;
    private Image knobImage;
    private int lineLeftOffset = 0;
    private int numberOfLabels = 0;
    private int numberOfTicks = 0;
    private boolean slidingKeyboard = false;
    private boolean slidingMouse = false;

    private double stepSize;
    private double value;
    private double maxValue;
    private double minValue;
    private boolean shiftRight;

    public Slider(double minValue, double maxValue) {
        super();
        container = new HTMLPanel("");
        this.minValue = minValue;
        this.maxValue = maxValue;
        add(container);
        setStyleName(StyleName.SLIDER);

        createComponents();

        sinkEvents(Event.MOUSEEVENTS | Event.KEYEVENTS | Event.FOCUSEVENTS);

        eventFunctions = new HashMap<>();
        createFunctions();

        keyTimer = new KeyTimer(30, firstAction(), repeatAction());
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<java.lang.Number> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public Double getValue() {
        return value;
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (eventFunctions.containsKey(event.getTypeInt()))
            eventFunctions.get(event.getTypeInt()).apply(event);
    }

    public void draw() {
        if (!isAttached()) return;
        lineLeftOffset = (getElement().getClientWidth() / 2) - (line.getOffsetWidth() / 2);
        line.getElement().getStyle().setLeft(lineLeftOffset, Unit.PX);

        drawLabels();
        drawTicks();
        drawKnob();
    }

    public void setNumberOfLabels(int numberOfLabels) {
        this.numberOfLabels = numberOfLabels;
        if (!isAttached()) return;
        drawLabels();
    }

    public void setNumberOfTicks(int numberOfTicks) {
        this.numberOfTicks = numberOfTicks;
        if (!isAttached()) return;
        drawTicks();
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
        setValue(getValue(), true);
    }

    @Override
    public void setValue(java.lang.Number value) {
        this.value = Math.max(minValue, Math.min(maxValue, value.doubleValue()));
        double remainder = (this.value - minValue) % stepSize;
        this.value -= remainder;

        if ((remainder > (stepSize / 2)) && ((this.value + stepSize) <= maxValue)) this.value += stepSize;

        drawKnob();
    }

    public void setValue(java.lang.Number value, boolean fireEvent) {
        setValue(value);
        if (fireEvent) ValueChangeEvent.fire(this, this.value);
    }

    public void shift(boolean right) {
        shiftRight = right;
        setValue(getValue() + ((right ? 1 : -1) * stepSize), true);
    }

    private String formatLabel(double value) {
        return String.valueOf((int) value);
    }

    private void createFunctions() {
        eventFunctions.put(Event.ONBLUR, new Function() {
            @Override
            public void apply(Event event) {
                onBlur(event.getClientX());
            }
        });
        eventFunctions.put(Event.ONKEYDOWN, new Function() {
            @Override
            public void apply(Event event) {
                onKeyDown(event);
            }
        });
        eventFunctions.put(Event.ONKEYUP, new Function() {
            @Override
            public void apply(Event event) {
                onKeyUp();
            }
        });
        eventFunctions.put(Event.ONMOUSEDOWN, new Function() {
            @Override
            public void apply(Event event) {
                onMouseDown(event);
            }
        });
        eventFunctions.put(Event.ONMOUSEUP, new Function() {
            @Override
            public void apply(Event event) {
                onMouseUp(event);
            }
        });
        eventFunctions.put(Event.ONMOUSEMOVE, new Function() {
            @Override
            public void apply(Event event) {
                if (slidingMouse) slideKnob(event.getClientX());
            }
        });
    }

    private KeyTimer.Action firstAction() {
        return new KeyTimer.Action() {
            @Override
            public void execute() {
                startSliding();
            }
        };
    }

    private KeyTimer.Action repeatAction() {
        return new KeyTimer.Action() {
            @Override
            public void execute() {
                shift(shiftRight);
            }
        };
    }

    private void createComponents() {
        line = new HTML();
        line.addStyleName(StyleName.LINE);
        container.add(line);

        knobImage = new Image();
        knobImage.addStyleName(StyleName.KNOB);
        container.add(knobImage);
    }

    private void onBlur(int eventClientX) {
        keyTimer.cancel();
        if (slidingMouse || slidingKeyboard) stopSliding();
        if (slidingMouse) {
            slidingMouse = false;
            slideKnob(eventClientX);
        } else if (slidingKeyboard)
            slidingKeyboard = false;
    }

    private void onKeyDown(Event event) {
        if (slidingKeyboard || !isHorizontalArrow(event.getKeyCode())) return;
        event.preventDefault();
        slidingKeyboard = true;
        shift(isArrowRight(event.getKeyCode()));
        keyTimer.schedule(400);
    }

    private void onKeyUp() {
        if (!slidingKeyboard) return;
        keyTimer.cancel();
        slidingKeyboard = false;
        stopSliding();
    }

    private void onMouseUp(Event event) {
        if (!slidingMouse) return;
        slidingMouse = false;
        slideKnob(event.getClientX());
        stopSliding();
    }

    private void onMouseDown(Event event) {
        setFocus(true);
        slidingMouse = true;
        startSliding();
        event.preventDefault();
        slideKnob(event.getClientX());
    }

    private double getKnobPercent() {
        if (maxValue <= minValue) return 0;

        double percent = (value - minValue) / (maxValue - minValue);
        return Math.max(0.0, Math.min(1.0, percent));
    }

    private void drawKnob() {
        int lineWidth = line.getOffsetWidth();
        int knobWidth = knobImage.getOffsetWidth();
        int knobLeftOffset = (int) (lineLeftOffset + (getKnobPercent() * lineWidth) - (knobWidth / 2));
        knobLeftOffset = Math.min(knobLeftOffset, lineLeftOffset + lineWidth - (knobWidth / 2) - 1);
        knobImage.getElement().getStyle().setLeft(knobLeftOffset, Unit.PX);
    }

    private void drawLabels() {
        for (int i = 0; i <= numberOfLabels; i++)
            setUpLabel(i, (i < labels.size() ? labels.get(i) : createLabel()));
    }

    private Label createLabel() {
        Label label = new Label();
        label.setVisible(false);
        label.addStyleName(StyleName.LABEL);
        container.add(label);
        labels.add(label);
        return label;
    }

    private void setUpLabel(int i, Label label) {
        double value = minValue + (getTotalRange() * i / numberOfLabels);
        label.setText(formatLabel(value));

        int labelWidth = label.getOffsetWidth();
        int labelLeftOffset = lineLeftOffset + (line.getOffsetWidth() * i / numberOfLabels) - (labelWidth / 2);
        labelLeftOffset = Math.min(labelLeftOffset, lineLeftOffset + line.getOffsetWidth() - labelWidth);
        labelLeftOffset = Math.max(labelLeftOffset, lineLeftOffset);
        label.getElement().getStyle().setLeft(left(formatLabel(value), labelLeftOffset), Unit.PX);
        label.setVisible(true);
    }

    private int left(String value, int labelLeftOffset) {
        return labelLeftOffset - (2 * value.length() + value.length());
    }

    private void drawTicks() {
        for (int i = 0; i <= numberOfTicks; i++)
            setUpTick(line.getOffsetWidth(), i, i < ticks.size() ? ticks.get(i) : createTick());
    }

    private HTML createTick() {
        HTML tick;
        tick = new HTML();
        container.add(tick);
        ticks.add(tick);
        tick.addStyleName(StyleName.TICK);
        return tick;
    }

    private void setUpTick(int width, int elementPosition, HTML element) {
        element.setVisible(false);
        int leftOffset = lineLeftOffset + (width * elementPosition / numberOfTicks) - (element.getOffsetWidth() / 2);
        leftOffset = Math.min(leftOffset, lineLeftOffset + width - 1);
        setUpElement(element, leftOffset);
    }

    private void setUpElement(HTML element, int leftOffset) {
        element.getElement().getStyle().setLeft(leftOffset, Unit.PX);
        element.setVisible(true);
    }

    private double getTotalRange() {
        return minValue > maxValue ? 0 : maxValue - minValue;
    }

    private void slideKnob(int eventClientX) {
        if (eventClientX <= 0) return;
        int lineWidth = line.getOffsetWidth();
        int lineLeft = line.getAbsoluteLeft();
        double percent = (double) (eventClientX - lineLeft) / lineWidth * 1.0;
        setValue(getTotalRange() * percent + minValue, true);
    }

    private void startSliding() {
        line.addStyleName(StyleName.LINE_SLIDING);
        knobImage.addStyleName(StyleName.KNOB_SLIDING);
    }

    private void stopSliding() {
        line.removeStyleName(StyleName.LINE_SLIDING);
        knobImage.removeStyleName(StyleName.KNOB_SLIDING);
    }

    public interface StyleName {
        String SLIDER = "slider";
        String LINE = "line";
        String LINE_SLIDING = "line-sliding";
        String KNOB = "knob";
        String KNOB_SLIDING = "knob-sliding";
        String LABEL = "label";
        String TICK = "tick";
    }

    private interface Function {
        void apply(Event event);
    }
}
