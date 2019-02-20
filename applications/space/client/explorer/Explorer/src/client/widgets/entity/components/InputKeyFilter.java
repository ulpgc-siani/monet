package client.widgets.entity.components;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputKeyFilter extends TextBox {

    private final List<Integer> validKeys = new ArrayList<>();

    public InputKeyFilter() {
        super();
        this.validKeys.addAll(Arrays.asList(KeyCodes.KEY_TAB, KeyCodes.KEY_ENTER, KeyCodes.KEY_ESCAPE));
        cancelInvalidKeys();
    }

    public InputKeyFilter(List<Integer> validKeys) {
        this();
        this.validKeys.addAll(validKeys);
    }

    public InputKeyFilter allowArrows() {
        return allowHorizontalArrows().allowVerticalArrows();
    }

    public InputKeyFilter allowHorizontalArrows() {
        validKeys.add(KeyCodes.KEY_LEFT);
        validKeys.add(KeyCodes.KEY_RIGHT);
        return this;
    }

    public InputKeyFilter allowVerticalArrows() {
        validKeys.add(KeyCodes.KEY_UP);
        validKeys.add(KeyCodes.KEY_DOWN);
        return this;
    }

    private void cancelInvalidKeys() {
        addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (!isCtrlOrMetaKey(event.getNativeEvent()) && !validKeys.contains(event.getNativeKeyCode())) cancelKey();
            }
        });
    }

    private boolean isCtrlOrMetaKey(NativeEvent event) {
        return event.getCtrlKey() || event.getMetaKey();
    }
}
