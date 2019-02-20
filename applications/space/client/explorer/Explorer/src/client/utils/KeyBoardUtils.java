package client.utils;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.GwtEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyBoardUtils {

    private static final List<Integer> NUMBERS = Arrays.asList(
            KeyCodes.KEY_NUM_ZERO, KeyCodes.KEY_ZERO,
            KeyCodes.KEY_NUM_ONE, KeyCodes.KEY_ONE,
            KeyCodes.KEY_NUM_TWO, KeyCodes.KEY_TWO,
            KeyCodes.KEY_NUM_THREE, KeyCodes.KEY_THREE,
            KeyCodes.KEY_NUM_FOUR, KeyCodes.KEY_FOUR,
            KeyCodes.KEY_NUM_FIVE, KeyCodes.KEY_FIVE,
            KeyCodes.KEY_NUM_SIX, KeyCodes.KEY_SIX,
            KeyCodes.KEY_NUM_SEVEN, KeyCodes.KEY_SEVEN,
            KeyCodes.KEY_NUM_EIGHT, KeyCodes.KEY_EIGHT,
            KeyCodes.KEY_NUM_NINE, KeyCodes.KEY_NINE
    );
    private static final int PLUS_KEY = 187;
    private static final int MINUS_KEY = 189;

    public static class KeyFunctionMapper {

        private final Map<Integer, Function> functions;

        public KeyFunctionMapper() {
            functions = new HashMap<>();
        }

        public void addFunction(Integer keyCode, Function function) {
            functions.put(keyCode, function);
        }

        public void executeFunction(Integer keyCode, GwtEvent event) {
            if (functions.containsKey(keyCode))
                functions.get(keyCode).apply(event);
        }

        public boolean isMapped(Integer keyCode) {
            return functions.containsKey(keyCode);
        }

        public interface Function {
            void apply(GwtEvent event);
        }
    }

    public static boolean isArrowKey(int keyCode) {
        return isVerticalArrow(keyCode) || isHorizontalArrow(keyCode);
    }

    public static boolean isVerticalArrow(int keyCode) {
        return isArrowUp(keyCode) || isArrowDown(keyCode);
    }

    public static boolean isArrowUp(int keyCode) {
        return keyCode == KeyCodes.KEY_UP;
    }

    public static boolean isArrowDown(int keyCode) {
        return keyCode == KeyCodes.KEY_DOWN;
    }

    public static boolean isHorizontalArrow(int keyCode) {
        return isArrowLeft(keyCode) || isArrowRight(keyCode);
    }

    public static boolean isArrowLeft(int keyCode) {
        return keyCode == KeyCodes.KEY_LEFT;
    }

    public static boolean isArrowRight(int keyCode) {
        return keyCode == KeyCodes.KEY_RIGHT;
    }

    public static boolean isEnter(int keyCode) {
        return keyCode == KeyCodes.KEY_ENTER;
    }

    public static boolean isEscape(int keyCode) {
        return keyCode == KeyCodes.KEY_ESCAPE;
    }

    public static boolean isTab(int keyCode) {
        return keyCode == KeyCodes.KEY_TAB;
    }

    public static boolean isPlusOrMinusKey(int keyCode) {
        return isPlusKey(keyCode) || isMinusKey(keyCode);
    }

    public static boolean isPlusKey(int keyCode) {
        return keyCode == PLUS_KEY || keyCode == KeyCodes.KEY_NUM_PLUS;
    }

    public static boolean isMinusKey(int keyCode) {
        return keyCode == MINUS_KEY || keyCode == KeyCodes.KEY_NUM_MINUS;
    }

    public static boolean isNumberKey(int keyCode) {
        return NUMBERS.contains(keyCode);
    }

    public static boolean isDeleteOrBackspaceKey(int keyCode) {
        return keyCode == KeyCodes.KEY_DELETE || keyCode == KeyCodes.KEY_BACKSPACE;
    }
}
