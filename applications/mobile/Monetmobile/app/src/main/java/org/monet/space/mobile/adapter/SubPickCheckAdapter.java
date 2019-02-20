package org.monet.space.mobile.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import org.monet.mobile.model.TaskDefinition;
import org.monet.space.mobile.model.GlossaryTerm;
import org.monet.space.mobile.model.schema.Check;

import java.util.Map;

public class SubPickCheckAdapter extends PickCheckAdapter {

    private final GlossaryTerm group;

    public SubPickCheckAdapter(Context context, TaskDefinition.Step.Edit.UseGlossary.Flatten flatten, SaveMode saveMode, GlossaryTerm term, Map<String, Check> checks) {
        super(context, flatten, saveMode, term.Childs, checks);
        group = term;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return inflateTermView(glossaryTerms.get(childPosition));
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return glossaryTerms.size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return inflateCategoryView(group);
    }
}
