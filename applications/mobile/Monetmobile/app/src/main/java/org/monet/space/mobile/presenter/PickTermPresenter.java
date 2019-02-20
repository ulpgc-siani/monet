package org.monet.space.mobile.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import com.google.inject.Inject;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.events.StartLoadingEvent;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.model.GlossaryTerm;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.content.DbCursorLoader;
import org.monet.space.mobile.view.PickTermView;

import java.util.List;

public class PickTermPresenter extends Presenter<PickTermView, Void> implements LoaderCallbacks<Cursor> {

    public static final String IS_EMBEDDED = "isembedded";
    public static final String GLOSSARY_CODE = "glossarycode";
    public static final String GLOSSARY_CONTEXT = "glossarycontext";
    public static final String FLATTEN = "flatten";
    public static final String DEPTH = "depth";
    public static final String CURRENT_DEPTH = "currentdepth";
    public static final String FROM = "from";
    public static final String TAG_FILTERS = "tagfilters";
    public static final String RESULT_KEY = "resultkey";
    public static final String RESULT_LABEL = "resultlabel";
    @Inject
    Repository repository;
    private int currentDepth;

    public void initialize() {
        Bundle args = getArgs();

        this.currentDepth = args.getInt(CURRENT_DEPTH, 0);

        BusProvider.get().post(new StartLoadingEvent());

        this.getLoaderManager().restartLoader(0, args, this);
    }

    public void open(Cursor cursor, boolean forceSelect) {
        Bundle args = getArgs();

        GlossaryTerm term = new GlossaryTerm();
        term.Code = cursor.getString(cursor.getColumnIndex("code"));
        term.Label = cursor.getString(cursor.getColumnIndex("label"));
        term.FlattenLabel = cursor.getString(cursor.getColumnIndex("flatten_label"));
        term.isLeaf = cursor.getInt(cursor.getColumnIndex("is_leaf")) > 0;
        boolean hasInternalChilds = cursor.getInt(cursor.getColumnIndex("internal_childs")) > 0;
        boolean hasLeafChilds = cursor.getInt(cursor.getColumnIndex("leaf_childs")) > 0;

        if (!forceSelect) {
            switch (Flatten.valueOf(args.getString(FLATTEN))) {
                case NONE:
                    term.isLeaf = true;
                    break;
                case LEVEL:
                    term.isLeaf = this.currentDepth == args.getInt(DEPTH) || term.isLeaf;
                    break;
                case INTERNAL:
                    term.isLeaf = !hasInternalChilds;
                    break;
                default:
                    break;
            }
        }

        if (forceSelect || !(hasInternalChilds || hasLeafChilds)) {
            Intent intent = new Intent();
            intent.putExtra(RESULT_KEY, term.Code);
            intent.putExtra(RESULT_LABEL, term.FlattenLabel);
            view.setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Bundle pickArgs = (Bundle) getArgs().clone();
            pickArgs.putString(FROM, term.Code);
            pickArgs.putInt(CURRENT_DEPTH, this.currentDepth + 1);
            this.routerHelper.goToPickTerm(this.view, 0, pickArgs);
        }
    }

    public void restart() {
        this.getLoaderManager().getLoader(0).forceLoad();
    }

    public Cursor filterTerms(String constraint) {
        return repository.getTerms(getArgs().getString(GLOSSARY_CODE), getArgs().getString(GLOSSARY_CONTEXT), getArgs().getString(FROM), constraint, getArgs().getStringArrayList(TAG_FILTERS), onlyInternal(getArgs()), 0);
    }

    private static class Loader extends DbCursorLoader {

        @Inject
        Repository repository;
        Bundle args;

        public Loader(Context context, Bundle args) {
            super(context, args);
            this.args = args;
        }

        @Override
        public Cursor loadInBackground() {
            try {
                String glossaryCode = args.getString(GLOSSARY_CODE);
                String glossaryContext = args.getString(GLOSSARY_CONTEXT);
                String parent = args.getString(FROM);
                List<String> tagFilters = args.getStringArrayList(TAG_FILTERS);
                int depth = Flatten.valueOf(args.getString(FLATTEN)) == Flatten.LEVEL ? args.getInt(DEPTH) : 0;
                return repository.getTerms(glossaryCode, glossaryContext, parent, null, tagFilters, onlyInternal(args), depth);
            } catch (Exception ex) {
                Log.error(ex);
                return null;
            }
        }

    }

    private static boolean onlyInternal(Bundle args) {
        return !args.getBoolean(IS_EMBEDDED) && Flatten.valueOf(args.getString(FLATTEN)) == Flatten.INTERNAL;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new Loader(context, args);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Bundle args = getArgs();
        view.setFlatten(Flatten.valueOf(args.getString(FLATTEN)));
        view.setIsMaxDepth(this.currentDepth == args.getInt(DEPTH));
        view.setTerms(data);
        BusProvider.get().post(new FinishLoadingEvent());
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            view.setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

}
