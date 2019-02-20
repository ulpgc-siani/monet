package org.monet.space.mobile.mvp.content;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public abstract class PartialLoadingAdapter<T> extends BaseAdapter {
  
  private ArrayAdapter<T> baseAdapter;
  private View loadingView;
  private AtomicBoolean hasMoreItems = new AtomicBoolean(true);
  private int loadingPosition = -1;
  private int pageSize = 10;
  
  public PartialLoadingAdapter(ArrayAdapter<T> baseAdapter, boolean hasMoreItems, int pageSize) {
    super();
    this.pageSize = pageSize;
    this.baseAdapter = baseAdapter;
    this.baseAdapter.registerDataSetObserver(new DataSetObserver() {
      @Override
      public void onChanged() {
        notifyDataSetChanged();
      }
      
      @Override
      public void onInvalidated() {
        notifyDataSetInvalidated();
      }
    });
    this.hasMoreItems.set(hasMoreItems);
  }
  
  @Override
  public int getViewTypeCount() {
    return this.baseAdapter.getViewTypeCount();
  }

  @Override
  public int getItemViewType(int position) {
    return this.baseAdapter.getItemViewType(position);
  }

  @Override
  public boolean areAllItemsEnabled() {
    return this.baseAdapter.areAllItemsEnabled();
  }

  @Override
  public boolean isEnabled(int position) {
    return this.baseAdapter.isEnabled(position);
  }
  
  @Override
  public int getCount() {
    int count = this.baseAdapter.getCount();
    return this.hasMoreItems.get() ? count + 1 : count;
  }

  @Override
  public Object getItem(int position) {
    return this.baseAdapter.getItem(position);
  }

  @Override
  public long getItemId(int position) {
    return this.baseAdapter.getItemId(position);
  }
  
  @Override
  public boolean hasStableIds() {
    return this.baseAdapter.hasStableIds();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if(position == this.baseAdapter.getCount() && hasMoreItems.get()) {
      if(this.loadingView == null) {
        this.loadingView = this.getLoadingView(parent);
        
        loadingPosition = position;
        (new LoadMoreItemsTask()).execute(position);
      }
      return this.loadingView;
    } else if(convertView == this.loadingView) {
      return this.baseAdapter.getView(position, null, parent);
    } else if(position > this.baseAdapter.getCount() && !hasMoreItems.get()) {
      return this.getLoadingView(parent);
    }
    
    return this.baseAdapter.getView(position, convertView, parent);
  }
 
  abstract protected ArrayList<T> loadItems(int offset, int numberOfItems);

  private View getLoadingView(ViewGroup parent) {
    View loadingView = this.baseAdapter.getView(-1, null, parent);
   // loadingView.findViewById(R.id.loading_item).setVisibility(View.VISIBLE);
   // loadingView.findViewById(R.id.list_item).setVisibility(View.GONE);
    return loadingView;
  }
  
  private void rebindPendingView(int loadingPosition, View loadingView) {
   // loadingView.findViewById(R.id.loading_item).setVisibility(View.GONE);
   // loadingView.findViewById(R.id.list_item).setVisibility(View.VISIBLE);
  }
  
  private class LoadMoreItemsTask extends AsyncTask<Integer, Integer, ArrayList<T>> {

    @Override
    protected ArrayList<T> doInBackground(Integer... args) {
      int startIndex = args[0];
      return loadItems(startIndex, pageSize);
    }
    
    @Override
    protected void onPostExecute(ArrayList<T> result) {
      if(result != null) {
        PartialLoadingAdapter.this.hasMoreItems.set(result.size() == pageSize);
        for(T item : result)
          baseAdapter.add(item);
        rebindPendingView(loadingPosition, loadingView);
        loadingView = null;
        loadingPosition = -1;
      }
    }
  }
}
