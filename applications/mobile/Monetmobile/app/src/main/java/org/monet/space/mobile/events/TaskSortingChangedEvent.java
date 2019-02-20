package org.monet.space.mobile.events;

import org.monet.space.mobile.model.TasksSorting;

public class TaskSortingChangedEvent {

    public TasksSorting newSorting;


    public TaskSortingChangedEvent(TasksSorting newSorting) {
        this.newSorting = newSorting;
    }

}
