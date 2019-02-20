package org.monet.space.mobile.mvp.content;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

public class CursorLoaderCallbacks implements LoaderCallbacks<Cursor> {

    private CursorAdapter adapter;
    private LoaderFactory loaderFactory;

    public interface LoaderFactory {
        Loader<Cursor> build(Bundle args);
    }

    public CursorLoaderCallbacks(CursorAdapter adapter, LoaderFactory loaderFactory) {
        this.adapter = adapter;
        this.loaderFactory = loaderFactory;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        try {
            return this.loaderFactory.build(args);
        } catch (Exception e) {
            throw new RuntimeException("No valid loader class provided.", e);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
