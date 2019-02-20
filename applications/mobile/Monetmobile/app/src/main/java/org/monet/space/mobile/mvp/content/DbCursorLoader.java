package org.monet.space.mobile.mvp.content;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

public abstract class DbCursorLoader extends SimpleDataLoader<Cursor> {

    public DbCursorLoader(Context ctx, Bundle args) {
        super(ctx, args);
    }

    protected void onReleaseResources(Cursor data) {
        if (data != null)
            data.close();
    }
}