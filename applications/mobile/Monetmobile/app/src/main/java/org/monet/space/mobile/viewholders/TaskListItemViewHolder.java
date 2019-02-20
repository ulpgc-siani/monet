package org.monet.space.mobile.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import org.monet.space.mobile.model.TaskDetails;

import org.monet.space.mobile.R;

public class TaskListItemViewHolder extends ViewHolder {

    private final CheckBox selected;
    private final ImageView urgent;
    private final TextView distance;
    private final TextView date;
    private final TextView title;
    private final TextView source;

    public TaskListItemViewHolder(View view) {
        super(view);
        selected = (CheckBox) view.findViewById(R.id.selected);
        urgent = (ImageView) view.findViewById(R.id.urgent);
        distance = (TextView) view.findViewById(R.id.distance);
        date = (TextView) view.findViewById(R.id.date);
        title = (TextView) view.findViewById(android.R.id.text1);
        source = (TextView) view.findViewById(android.R.id.text2);
    }

    public void setOnClickListener(View.OnClickListener l) {
        selected.setOnClickListener(l);
    }

    public TaskListItemViewHolder task(TaskDetails task) {
        title(task.label).source(task.sourceLabel);
        if (task.urgent)
            showUrgent();
        else
            hideUrgent();
        return this;
    }

    public TaskListItemViewHolder addTextToTitle(String text) {
        title.setText(title.getText() + " " + text);
        return this;
    }

    public TaskListItemViewHolder date(String date) {
        this.date.setText(date);
        return this;
    }

    public TaskListItemViewHolder distance(String distance) {
        this.distance.setText(distance);
        return this;
    }

    public TaskListItemViewHolder check(boolean checked) {
        this.selected.setChecked(checked);
        return this;
    }

    public TaskListItemViewHolder selectedTag(int tag) {
        selected.setTag(tag);
        return this;
    }

    public boolean isChecked() {
        return selected.isChecked();
    }

    private TaskListItemViewHolder title(String title) {
	    if (title.contains(": "))
		    title = title.substring(title.indexOf(": ") + 2);
        this.title.setText(title);
        return this;
    }

    private TaskListItemViewHolder source(String source) {
        this.source.setText(source);
        return this;
    }

    private void showUrgent() {
        urgent.setVisibility(View.VISIBLE);
    }

    private void hideUrgent() {
        urgent.setVisibility(View.INVISIBLE);
    }
}
