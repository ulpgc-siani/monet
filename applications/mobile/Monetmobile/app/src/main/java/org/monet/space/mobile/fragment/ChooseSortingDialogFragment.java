package org.monet.space.mobile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.TaskSortingChangedEvent;
import org.monet.space.mobile.model.TasksSorting;
import org.monet.space.mobile.mvp.BusProvider;

public class ChooseSortingDialogFragment extends DialogFragment {

    TasksSorting previousSorting;

    public static class Item {
        public final String text;
        public final int icon;

        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public static ChooseSortingDialogFragment create(TasksSorting previousSorting) {
        ChooseSortingDialogFragment f = new ChooseSortingDialogFragment();
        f.previousSorting = previousSorting;
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int previousSortingCriteria = 0;
        TasksSorting.SortMode previousSortMode = TasksSorting.SortMode.ASC;
        if (previousSorting != null) {
            previousSortingCriteria = previousSorting.getSortCriteria();
            previousSortMode = previousSorting.getSortMode();
        }

        String[] entries = this.getResources().getStringArray(R.array.sort_tasks_entries);
        final Item[] items = new Item[entries.length];
        for (int i = 0; i < entries.length; i++) {
            if (i == previousSortingCriteria) {
                if (previousSortMode == TasksSorting.SortMode.ASC) {
                    items[i] = new Item(entries[i], R.drawable.sort_asc);
                } else {
                    items[i] = new Item(entries[i], R.drawable.sort_desc);
                }
            } else {
                items[i] = new Item(entries[i], R.drawable.sort_none);
            }
        }

        ListAdapter listAdapter = new ArrayAdapter<Item>(getActivity(), android.R.layout.select_dialog_item, android.R.id.text1, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);

                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.ordering)
                .setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TasksSorting tasksSorting;
                        if ((previousSorting != null) && (which == previousSorting.getSortCriteria())) {

                            if (previousSorting.getSortMode() == TasksSorting.SortMode.ASC) {
                                tasksSorting = new TasksSorting(which, TasksSorting.SortMode.DESC);
                            } else {
                                tasksSorting = new TasksSorting(which, TasksSorting.SortMode.ASC);
                            }

                        } else {
                            tasksSorting = new TasksSorting(which, TasksSorting.SortMode.ASC);
                        }

                        BusProvider.get().post(new TaskSortingChangedEvent(tasksSorting));
                    }
                });
        return builder.create();
    }

}
