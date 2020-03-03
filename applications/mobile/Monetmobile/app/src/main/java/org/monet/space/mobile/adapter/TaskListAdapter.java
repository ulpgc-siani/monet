package org.monet.space.mobile.adapter;

import android.location.Location;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.monet.space.mobile.R;
import org.monet.space.mobile.activity.TaskActivity;
import org.monet.space.mobile.helpers.DateUtils;
import org.monet.space.mobile.model.Coordinate;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TaskTray;
import org.monet.space.mobile.presenter.TaskListPresenter;
import org.monet.space.mobile.viewholders.TaskGroupViewHolder;
import org.monet.space.mobile.viewholders.TaskListItemViewHolder;
import org.siani.cluster.Item;

import java.util.ArrayList;
import java.util.List;


public class TaskListAdapter extends BaseAdapter {

    private final TaskActivity activity;
    private final ListView listView;
    private final TextView emptyText;
    private TaskTray currentTray;
    private ActionMode mode;
    private TaskListPresenter presenter;

    private Coordinate currentCoordinate;
    private long selectedId;
    private List<Item> items = new ArrayList<Item>();

    public TaskListAdapter(TaskActivity activity, ListView listView, TextView emptyText) {
        this.activity = activity;
        this.listView = listView;
        this.emptyText = emptyText;
    }

    public void updateTasks(List<Item> items) {
        this.items = items;
        if (this.items.isEmpty())
            emptyText.setText(currentTray.emptyMessage());
        notifyDataSetChanged();
    }

    public TaskListAdapter taskTray(TaskTray tray) {
        currentTray = tray;
        return this;
    }

    public TaskListAdapter taskListPresenter(TaskListPresenter presenter) {
        this.presenter = presenter;
        return this;
    }

    public TaskListAdapter currentCoordinate(Coordinate coordinate) {
        currentCoordinate = coordinate;
        return this;
    }

    public TaskListAdapter selectedId(long id) {
        selectedId = id;
        return this;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (items.get(position).isGroup())
            return position;
        return ((TaskDetails) items.get(position).get()).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = items.get(position).isGroup() ? createGroupView(parent) : createTaskView(parent);
        if (items.get(position).isGroup()) {
            if (!(convertView.getTag() instanceof TaskGroupViewHolder))
                convertView = createGroupView(parent);
            setUpGroupView(position, (TaskGroupViewHolder) convertView.getTag());
        }
        else {
            if (!(convertView.getTag() instanceof TaskListItemViewHolder))
                convertView = createTaskView(parent);
            setUpView(convertView, position);
        }
        return convertView;
    }

    private void addTasksBefore(List<Item> newItems, int beforeIndex) {
        items.addAll(beforeIndex + 1, newItems);
        notifyDataSetChanged();
    }

    private void setUpGroupView(final int position, TaskGroupViewHolder viewHolder) {
        viewHolder
                .group(items.get(position))
                .setOnOpenListener(new TaskGroupViewHolder.OnOpenListener() {

                    @Override
                    public void open() {
                        addTasksBefore(items.get(position).items().edit(), position);
                    }
                })
                .setOnCloseListener(new TaskGroupViewHolder.OnCloseListener() {

                    @Override
                    public void close() {
                        removeTasks(items.get(position).items().edit());
                    }
                });
        if (items.containsAll(items.get(position).items().edit()))
            viewHolder.isOpen();
        else
            viewHolder.isClosed();
    }

    private void removeTasks(List<Item> items) {
        for (Item item : items) {
            this.items.remove(item);
            if (item.isGroup()) removeTasks(item.items().edit());
        }
        notifyDataSetChanged();
    }

    private View createGroupView(ViewGroup parent) {
        return new TaskGroupViewHolder(activity.getLayoutInflater().inflate(R.layout.task_group_list_item, parent, false)).view();
    }

    private View createTaskView(ViewGroup parent) {
        final TaskListItemViewHolder holder = new TaskListItemViewHolder(activity.getLayoutInflater().inflate(R.layout.task_list_item, parent, false));
        holder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                boolean currentState = !listView.getCheckedItemPositions().get(position);
                listView.setItemChecked(position, currentState);

                int checkedItemCount = 0;
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.valueAt(i))
                        checkedItemCount++;
                }
                if (checkedItemCount > 0) {
                    if (mode == null)
                        mode = activity.startActionMode(new TaskActionMode());
                    mode.setTitle(activity.getResources().getQuantityString(R.plurals.selected_items, checkedItemCount,
                            checkedItemCount));
                    mode.invalidate();
                } else if (mode != null) {
                    mode.finish();
                }
            }
        });
        return holder.view();
    }

    private void setUpView(View view, int position) {
        TaskListItemViewHolder holder = (TaskListItemViewHolder) view.getTag();
        holder.selectedTag(position);
        holder.check(listView.getCheckedItemPositions().get(position));

        TaskDetails taskDetails = (TaskDetails) items.get(position).get();
        holder.task(taskDetails);

        if (activity.isDualPane()) {
            if (selectedId == taskDetails.id)
                view.setBackgroundResource(R.drawable.dualpane_list_item_background_selected);
            else
                view.setBackgroundResource(R.drawable.dualpane_list_item_background);
        }

        float results[] = new float[1];
        double lat = taskDetails.position.latitude;
        double lng = taskDetails.position.longitude;

        Location.distanceBetween(currentCoordinate.getLat(), currentCoordinate.getLon(), lat, lng, results);
        float distanceRawInMeters = results[0];
        int distanceString = R.string.distance_in_meters;
        if (distanceRawInMeters >= 1000) {
            distanceRawInMeters /= 1000;
            distanceString = R.string.distance_in_kilometers;
        }
        holder.distance(activity.getString(distanceString, distanceRawInMeters))
                .date(DateUtils.formatAsShortDate(activity, taskDetails.suggestedEndDate));

        if (taskDetails.notReadChats > 0)
            holder.addTextToTitle("(" + String.valueOf(taskDetails.notReadChats) + ")");
    }

    private final class TaskActionMode implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            activity.getMenuInflater().inflate(currentTray.menu(), menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() != R.id.menu_abandon && item.getItemId() != R.id.menu_assign) return false;
            if (item.getItemId() == R.id.menu_abandon) {
                SparseBooleanArray itemsId = listView.getCheckedItemPositions();
                presenter.abandonTasks(getItemsChecked(itemsId));
            }
            if (item.getItemId() == R.id.menu_assign){
                SparseBooleanArray itemsId = listView.getCheckedItemPositions();
                presenter.assignTasks(getItemsChecked(itemsId));
            }
            mode.finish();
            return true;
        }

        private long[] getItemsChecked(SparseBooleanArray itemsId){
            ArrayList<Long> checked = new ArrayList<Long>();
            for (int i = 0; i<itemsId.size(); i++)
                if (itemsId.valueAt(i))
                    checked.add((long)getItemId(itemsId.keyAt(i)));
            return arrayListToLongArray(checked);
        }

        private long[] arrayListToLongArray(ArrayList<Long> arrayList){
            long[] longArray = initializeLongArray(arrayList.size());
            for (int i=0; i< arrayList.size();i++)
                longArray[i] = arrayList.get(i);
            return longArray;
        }

        private long[] initializeLongArray(int size){
            long[] longArray = new long[size];
            for (int i=0; i< size;i++)
                longArray[i] = -1;
            return longArray;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            for (int i = 0; i < checked.size(); i++)
                listView.setItemChecked(i, false);
            TaskListAdapter.this.mode = null;
        }
    }
}
