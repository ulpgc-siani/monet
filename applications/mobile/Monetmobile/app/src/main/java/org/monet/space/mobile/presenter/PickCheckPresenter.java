package org.monet.space.mobile.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import com.google.inject.Inject;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.events.StartLoadingEvent;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.model.GlossaryTerm;
import org.monet.space.mobile.model.schema.Check;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.content.DbCursorLoader;
import org.monet.space.mobile.view.PickCheckView;

import java.util.ArrayList;
import java.util.List;

public class PickCheckPresenter extends Presenter<PickCheckView, Void> implements LoaderCallbacks<Cursor> {

	public static final String IS_EMBEDDED = "isembedded";
	public static final String GLOSSARY_CODE = "glossarycode";
	public static final String GLOSSARY_CONTEXT = "glossarycontext";
	public static final String DEPTH = "depth";
	public static final String FLATTEN = "flatten";
	public static final String FROM = "from";
	public static final String TAG_FILTERS = "tagfilters";
	public static final String VALUE = "value";
	@Inject
	Repository repository;

	public void initialize() {
		Bundle args = getArgs();

		BusProvider.get().post(new StartLoadingEvent());

		this.getLoaderManager().restartLoader(0, args, this);
	}

	public void restart() {
		this.getLoaderManager().getLoader(0).forceLoad();
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

				if (glossaryCode != null) {
					String parent = args.getString(FROM);
					List<String> tagFilters = args.getStringArrayList(TAG_FILTERS);
					int depth = args.getInt(DEPTH);

					boolean onlyInternal = false;

					if (!args.getBoolean(IS_EMBEDDED)) {
						Flatten flatten = Flatten.valueOf(args.getString(FLATTEN));
						onlyInternal = flatten == Flatten.INTERNAL;
					}
					return repository.getTerms(glossaryCode, glossaryContext, parent, null, tagFilters, onlyInternal, depth);

				} else {
					String[] columns = {"code", "label", "flatten_label", "parent_code", "type"};
					List<Check> checks = args.getParcelableArrayList(PickCheckPresenter.VALUE);
					MatrixCursor cursor = new MatrixCursor(columns, checks.size());

					for (Check check : checks)
						cursor.addRow(new Object[]{check.code, check.label, check.label, null, GlossaryTerm.TYPE_TERM});
					return cursor;
				}

			} catch (Exception ex) {
				Log.error(ex);
				return null;
			}
		}
	}


	@Override
	public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new Loader(this.context, args);
	}

	@Override
	public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
		Bundle args = getArgs();
		view.setFlatten(Flatten.valueOf(args.getString(FLATTEN)));
		view.setTerms(data);
		BusProvider.get().post(new FinishLoadingEvent());
	}

	@Override
	public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
	}

	public void accept(List<Check> checks) {
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(PickCheckPresenter.VALUE, new ArrayList<Parcelable>(checks));

		Intent intent = new Intent();
		intent.putExtras(bundle);

		view.setResult(Activity.RESULT_OK, intent);
		finish();
	}


}
