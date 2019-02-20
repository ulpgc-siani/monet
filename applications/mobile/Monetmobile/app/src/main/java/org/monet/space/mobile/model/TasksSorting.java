package org.monet.space.mobile.model;

import android.location.Location;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TasksSorting {

    public enum SortMode {ASC, DESC}

    private static final int DISTANCE_ASC = 0;
    private static final int DISTANCE_DESC = 1;
    private static final int URGENT_DESC_SUGGESTED_END_DATE_ASC = 2;
    private static final int URGENT_ASC_SUGGESTED_END_DATE_DESC = 3;
    private static final int LABEL_ASC = 4;
    private static final int LABEL_DESC = 5;
    private static final int SUGGESTED_START_DATE_ASC = 6;
    private static final int SUGGESTED_START_DATE_DESC = 7;
    private static final int SUGGESTED_END_DATE_ASC = 8;
    private static final int SUGGESTED_END_DATE_DESC = 9;

    private static final Map<Integer, Comparator<TaskDetails>> comparators = new HashMap<>();
    static {
        comparators.put(DISTANCE_ASC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                float results[] = new float[1];
                Location.distanceBetween(lhs.position.latitude, lhs.position.longitude, rhs.position.latitude, rhs.position.longitude, results);
                return (int) results[0];
            }
        });
        comparators.put(DISTANCE_DESC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                float results[] = new float[1];
                Location.distanceBetween(rhs.position.latitude, rhs.position.longitude, lhs.position.latitude, lhs.position.longitude, results);
                return (int) results[0];
            }
        });
        comparators.put(URGENT_DESC_SUGGESTED_END_DATE_ASC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                if (lhs.urgent && !rhs.urgent) return -1;
                if (!lhs.urgent && rhs.urgent) return 1;
                if (lhs.suggestedStartDate == null && rhs.suggestedStartDate == null) return 0;
                if (lhs.suggestedEndDate == null) return -1;
                if (rhs.suggestedEndDate == null) return 1;
                return lhs.suggestedEndDate.compareTo(rhs.suggestedEndDate);
            }
        });
        comparators.put(URGENT_ASC_SUGGESTED_END_DATE_DESC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                if (lhs.urgent && !rhs.urgent) return 1;
                if (!lhs.urgent && rhs.urgent) return -1;
                if (lhs.suggestedStartDate == null && rhs.suggestedStartDate == null) return 0;
                if (rhs.suggestedEndDate == null) return -1;
                if (lhs.suggestedEndDate == null) return 1;
                return rhs.suggestedEndDate.compareTo(lhs.suggestedEndDate);
            }
        });
        comparators.put(LABEL_ASC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                return lhs.label.compareTo(rhs.label);
            }
        });
        comparators.put(LABEL_DESC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                return rhs.label.compareTo(lhs.label);
            }
        });
        comparators.put(SUGGESTED_START_DATE_ASC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                if (lhs.suggestedStartDate == null && rhs.suggestedStartDate == null) return 0;
                if (lhs.suggestedStartDate == null) return -1;
                if (rhs.suggestedStartDate == null) return 1;
                return lhs.suggestedStartDate.compareTo(rhs.suggestedStartDate);
            }
        });
        comparators.put(SUGGESTED_START_DATE_DESC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                if (rhs.suggestedStartDate == null && lhs.suggestedStartDate == null) return 0;
                if (rhs.suggestedStartDate == null) return -1;
                if (lhs.suggestedStartDate == null) return 1;
                return rhs.suggestedStartDate.compareTo(lhs.suggestedStartDate);
            }
        });
        comparators.put(SUGGESTED_END_DATE_ASC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                if (lhs.suggestedEndDate== null && rhs.suggestedEndDate== null) return 0;
                if (lhs.suggestedEndDate == null) return -1;
                if (rhs.suggestedEndDate== null) return 1;
                return lhs.suggestedEndDate.compareTo(rhs.suggestedEndDate);
            }
        });
        comparators.put(SUGGESTED_END_DATE_DESC, new Comparator<TaskDetails>() {
            @Override
            public int compare(TaskDetails lhs, TaskDetails rhs) {
                if (rhs.suggestedEndDate == null && lhs.suggestedEndDate== null) return 0;
                if (rhs.suggestedEndDate == null) return -1;
                if (lhs.suggestedEndDate == null) return 1;
                return rhs.suggestedEndDate.compareTo(lhs.suggestedEndDate);
            }
        });
    }

    private final int sortCriteria;
    private final SortMode sortMode;

    public TasksSorting(int sortCriteria, SortMode sortMode) {
        this.sortCriteria = sortCriteria;
        this.sortMode = sortMode;
    }

    public int getSortCriteria() {
        return sortCriteria;
    }

    public SortMode getSortMode() {
        return sortMode;
    }

    public Comparator<TaskDetails> getComparator() {
        return comparators.get(comparatorIndex());
    }

    private int comparatorIndex() {
        if (sortMode == SortMode.DESC)
            return sortCriteria * 2 + 1;
        return sortCriteria * 2;
    }
}
