package org.monet.space.mobile.content;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import com.google.inject.Inject;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.mvp.content.DbCursorLoader;

public class SourceListLoader extends DbCursorLoader {

    @Inject
    Repository repositoryProvider;

    public SourceListLoader(Context context, Bundle args) {
        super(context, args);
    }

    @Override
    protected org.monet.space.mobile.mvp.content.SimpleDataLoader.SimpleDataObserver getObserver() {
        return repositoryProvider;
    }

    @Override
    public Cursor loadInBackground() {
        return repositoryProvider.getSources();
    }

}
