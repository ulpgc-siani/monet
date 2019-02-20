package org.monet.space.mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.R;
import org.monet.space.mobile.model.GlossaryTerm;
import org.monet.space.mobile.presenter.PickTermPresenter;

public class PickTermAdapter extends CursorAdapter implements SectionIndexer, Filterable {

    private boolean initialized = false;
    private int labelIndex;
    private int typeIndex;
    private int internalChildrenIndex;
    private int leafChildrenIndex;

    private Flatten flatten;
    private boolean isMaxDepth;
    private PickTermPresenter presenter;
    private ListView listView;

    private AlphabetIndexer alphabetIndexer;

    public PickTermAdapter(final Activity activity, Cursor c, final PickTermPresenter presenter, ListView listView, final TextView emptyView, Flatten flatten, boolean isMaxDepth) {
        super(activity, c, false);
        this.presenter = presenter;
        this.listView = listView;
        this.flatten = flatten;
        this.isMaxDepth = isMaxDepth;
        setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                final Cursor cursor = presenter.filterTerms(constraint.toString());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        emptyView.setText(activity.getString(text(cursor)));
                    }
                });
                return cursor;
            }
        });
    }

    private int text(Cursor cursor) {
        if (cursor.getCount() == 0)
            return R.string.term_list_empty;
        return R.string.term_list_loading;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (!initialized)
            initialize(cursor);

        ((TextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(labelIndex));

        View selectThisBtn = view.findViewById(R.id.select_this_btn);
        selectThisBtn.setTag(cursor.getPosition());

        int internalChildren = cursor.getInt(internalChildrenIndex);
        int leafChildren = cursor.getInt(leafChildrenIndex);
        if (cursor.getInt(typeIndex) == GlossaryTerm.TYPE_SUPER_TERM &&
                (internalChildren > 0 || leafChildren > 0) &&
                ((flatten == Flatten.INTERNAL && internalChildren > 0) || (flatten == Flatten.LEVEL && !isMaxDepth) || flatten == Flatten.ALL))
            selectThisBtn.setVisibility(View.VISIBLE);
        else
            selectThisBtn.setVisibility(View.GONE);

        alphabetIndexer = new AlphabetIndexer(cursor, labelIndex, " ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.glossary_list_item, parent, false);

        view.findViewById(R.id.select_this_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                presenter.open((Cursor) listView.getItemAtPosition((Integer) v.getTag()), true);
            }
        });

        return view;
    }

    @Override
    public int getPositionForSection(int section) {
        return alphabetIndexer.getPositionForSection(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return alphabetIndexer.getSectionForPosition(position);
    }

    @Override
    public Object[] getSections() {
        return alphabetIndexer.getSections();
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        if (c != null)
            alphabetIndexer = new AlphabetIndexer(c, labelIndex, " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        return super.swapCursor(c);
    }

    private void initialize(Cursor cursor) {
        initialized = true;
        labelIndex = cursor.getColumnIndex("label");
        typeIndex = cursor.getColumnIndex("type");
        internalChildrenIndex = cursor.getColumnIndex("internal_childs");
        leafChildrenIndex = cursor.getColumnIndex("leaf_childs");
    }
}
