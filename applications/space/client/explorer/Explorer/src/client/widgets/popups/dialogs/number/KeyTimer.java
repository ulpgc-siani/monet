package client.widgets.popups.dialogs.number;

import com.google.gwt.user.client.Timer;

public class KeyTimer extends Timer {

    private final Action firstAction;
    private final Action repeatAction;
    private final int delay;
    private boolean firstRun = true;

    public KeyTimer(int delay, Action firstAction, Action repeatAction) {
        this.delay = delay;
        this.firstAction = firstAction;
        this.repeatAction = repeatAction;
    }

    @Override
    public void run() {
        if (firstRun) {
            firstRun = false;
            firstAction.execute();
        }
        repeatAction.execute();
        schedule(delay);
    }

    public void schedule(int delay) {
        firstRun = true;
        super.schedule(delay);
    }

    public interface Action {
        void execute();
    }
}
