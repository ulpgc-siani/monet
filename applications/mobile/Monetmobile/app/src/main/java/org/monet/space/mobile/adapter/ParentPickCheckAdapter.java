package org.monet.space.mobile.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.model.GlossaryTerm;
import org.monet.space.mobile.model.schema.Check;
import org.monet.space.mobile.widget.SubExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParentPickCheckAdapter extends PickCheckAdapter {

    private final Map<GlossaryTerm, SubPickCheckAdapter> subPickCheckAdapters = new HashMap<>();
    private ExpandableListView listView;


    public ParentPickCheckAdapter(Context context, ExpandableListView listView, Cursor glossaryTermsCursor, List<Check> checkedChecks, Flatten flatten, SaveMode saveMode) {
        super(context, flatten, saveMode);
        this.listView = listView;
        this.loadCheckedChecks(checkedChecks);
        this.loadGlossaryTerms(glossaryTermsCursor);
    }

    private void loadGlossaryTerms(Cursor cursor) {
        Map<String, GlossaryTerm> termsMap = new HashMap<>();

        if (cursor == null) return;

        try {
            int indexCode = cursor.getColumnIndex("code");
            int indexType = cursor.getColumnIndex("type");
            int indexParentCode = cursor.getColumnIndex("parent_code");
            int indexLabel = cursor.getColumnIndex("label");
            int indexFlattenLabel = cursor.getColumnIndex("flatten_label");

            while (cursor.moveToNext()) {
                GlossaryTerm term = new GlossaryTerm();
                term.Code = cursor.getString(indexCode);
                term.Label = cursor.getString(indexLabel);
                term.Type = cursor.getInt(indexType);
                term.FlattenLabel = cursor.getString(indexFlattenLabel);

                addCheck(term);

                termsMap.put(term.Code, term);

                String parentCode = cursor.getString(indexParentCode);
                if (parentCode != null) {
                    GlossaryTerm parent = termsMap.get(parentCode);
                    if (parent != null) {
                        if (parent.Childs == null)
                            parent.Childs = new ArrayList<>();
                        parent.Childs.add(term);
                    }
                } else {
                    this.glossaryTerms.add(term);
                }
            }
        } finally {
            cursor.close();
        }
        setIsLeaf(glossaryTerms);
    }

    private void setIsLeaf(List<GlossaryTerm> terms) {
        for (GlossaryTerm term : terms) {
            term.isLeaf = term.Childs == null || term.Childs.isEmpty();
            if (term.Childs != null) setIsLeaf(term.Childs);
        }
    }

    private void loadCheckedChecks(List<Check> checkedChecks) {
        if (checkedChecks == null) return;

        for (Check check : checkedChecks) {
            checks.put(check.code, check);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        GlossaryTerm group = this.glossaryTerms.get(groupPosition);
        GlossaryTerm term = group.Childs.get(childPosition);
        if (term.isLeaf)
            return this.inflateTermView(term);
        SubPickCheckAdapter adapter = new SubPickCheckAdapter(context, flatten, saveMode, term, checks);
        final SubExpandableListView expandableListView = new SubExpandableListView(context);
        expandableListView.setAdapter(adapter);
        subPickCheckAdapters.put(term, adapter);
        return expandableListView;
    }

    private boolean mustShowGroupExpanded(GlossaryTerm group) {
        if (group.Childs == null) return false;

        for (int i = 0; i < group.Childs.size(); i++) {
            Check check = this.checks.get(group.Childs.get(i).Code);
            if ((check != null) && (check.isChecked))
                return true;
        }
        return false;
    }

    public void expandGroups() {
        for (int i = 0; i < this.glossaryTerms.size(); i++) {
            GlossaryTerm group = this.glossaryTerms.get(i);
            if (this.mustShowGroupExpanded(group)) {
                this.listView.expandGroup(i);
            } else {
                this.listView.collapseGroup(i);
            }
        }
    }

    public void selectAll() {
        this.markAllItems(true);
    }

    public void selectNone() {
        this.markAllItems(false);
    }

    private void markAllItems(boolean value) {
        this.markAllChecks(value);
        this.markAllCheckViews(value, this.listView);
        this.expandGroups();
    }

    private void markAllChecks(boolean value) {
        for (Check check : this.checks.values()) {
            check.isChecked = value;
        }
    }
}
