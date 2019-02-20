package org.monet.space.mobile.model;


import org.monet.space.mobile.R;

public enum TaskTray {
    ASSIGNED(R.id.radio_button_assigned, R.string.empty_assigned_task_list, R.menu.cab_assigned_tasks),
    FINISHED(R.id.radio_button_finished, R.string.empty_finished_task_list, R.menu.cab_finished_tasks),
    AVAILABLE(R.id.radio_button_available, R.string.empty_available_task_list, R.menu.cab_available_tasks);

    private final int action;
    private final int emptyMessage;
    private final int menu;

    TaskTray(int action, int emptyMessage, int menu) {
        this.action = action;
        this.emptyMessage = emptyMessage;
        this.menu = menu;
    }

    public int action() {
        return action;
    }

    public int emptyMessage() {
        return emptyMessage;
    }

    public int menu() {
        return menu;
    }

    public static TaskTray forAction(int action) {
        for (TaskTray tray : values())
            if (action == tray.action) return tray;
        return null;
    }
}
