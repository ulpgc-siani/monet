package org.monet.space.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.monet.mobile.model.TaskDefinition;
import org.monet.space.mobile.R;
import org.monet.space.mobile.model.GlossaryTerm;
import org.monet.space.mobile.model.schema.Check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PickCheckAdapter extends BaseExpandableListAdapter implements CompoundButton.OnCheckedChangeListener {

    public enum SaveMode {SAVE_ALL, SAVE_CHECKED}

    protected final List<GlossaryTerm> glossaryTerms;
    protected Map<String, Check> checks;
    protected SaveMode saveMode;
    protected Context context;
    protected TaskDefinition.Step.Edit.UseGlossary.Flatten flatten;

    public PickCheckAdapter(Context context, TaskDefinition.Step.Edit.UseGlossary.Flatten flatten, SaveMode saveMode) {
        this(context, flatten, saveMode, new ArrayList<GlossaryTerm>(), new HashMap<String, Check>());
    }

    PickCheckAdapter(Context context, TaskDefinition.Step.Edit.UseGlossary.Flatten flatten, SaveMode saveMode, List<GlossaryTerm> terms, Map<String, Check> checks) {
        this.context = context;
        this.flatten = flatten;
        this.saveMode = saveMode;
        this.checks = checks;
        this.glossaryTerms = terms;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    protected String getTermLabel(GlossaryTerm term) {
        if (flatten.equals(TaskDefinition.Step.Edit.UseGlossary.Flatten.ALL))
            return term.FlattenLabel;
        return term.Label;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.glossaryTerms.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.glossaryTerms.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return renderGroup(groupPosition, isExpanded, glossaryTerms.get(groupPosition));
    }

    private View renderGroup(int groupPosition, boolean isExpanded, GlossaryTerm group) {
        View itemView = getGroupView(group);
        setUpIndicator(groupPosition, isExpanded, (ImageView) itemView.findViewById(R.id.explist_indicator));
        return itemView;
    }

    private void setUpIndicator(int groupPosition, boolean isExpanded, ImageView indicator) {
        if (indicator == null) {
            return;
        }
        if (getChildrenCount(groupPosition) == 0) {
            indicator.setVisibility(View.INVISIBLE);
        } else {
            indicator.setVisibility(View.VISIBLE);
            indicator.setImageResource(isExpanded ? R.drawable.navigation_collapse : R.drawable.navigation_expand);
        }
    }

    protected void addCheck(GlossaryTerm term) {
        Check check = this.checks.get(term.Code);
        if (check != null) return;
        check = new Check();
        check.code = term.Code;
        check.label = this.getTermLabel(term);
        check.isChecked = false;
        this.checks.put(term.Code, check);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return glossaryTerms.get(groupPosition).Childs.get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        GlossaryTerm group = this.glossaryTerms.get(groupPosition);
        if ((group.Childs != null) && (group.Childs.size() > 0))
            return group.Childs.size();

        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    protected View getGroupView(GlossaryTerm group) {
        if (group.Type == GlossaryTerm.TYPE_TERM) {
            return this.inflateTermView(group);
        }
        if (group.Type == GlossaryTerm.TYPE_CATEGORY) {
            return this.inflateCategoryView(group);
        }
        return this.inflateSuperTermView(group);
    }

    protected View inflateTermView(GlossaryTerm term) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.edit_check_term, null);

        CheckBox checkView = (CheckBox) itemView.findViewById(R.id.term);
        checkView.setText(this.getTermLabel(term));

        Check check = this.checks.get(term.Code);
        checkView.setChecked(check.isChecked);
        checkView.setTag(check);
        checkView.setOnCheckedChangeListener(this);

        return itemView;
    }

    protected View inflateCategoryView(GlossaryTerm term) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.edit_check_category, null);

        TextView textView = (TextView) itemView.findViewById(R.id.category);
        textView.setText(this.getTermLabel(term));

        return itemView;
    }

    protected View inflateSuperTermView(GlossaryTerm term) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.edit_check_superterm, null);

        CheckBox checkView = (CheckBox) itemView.findViewById(R.id.term);
        checkView.setText(this.getTermLabel(term));

        Check check = this.checks.get(term.Code);
        checkView.setChecked(check.isChecked);
        checkView.setTag(check);
        checkView.setOnCheckedChangeListener(this);

        return itemView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag();
        if ((tag != null) && (tag instanceof Check)) {
            Check check = (Check) buttonView.getTag();
            check.isChecked = isChecked;
        }
    }

    public List<Check> getChecksToSave() {
        return getChecksToSave(glossaryTerms);
    }

    private List<Check> getChecksToSave(List<GlossaryTerm> glossary) {
        List<Check> checksToSave = new ArrayList<>();

        for (GlossaryTerm term : glossary) {
            Check check = checks.get(term.Code);

            if (check == null) continue;
            if ((this.saveMode.equals(SaveMode.SAVE_ALL)) || (check.isChecked)) {
                checksToSave.add(check);
            }
            if (term.Childs != null) {
                checksToSave.addAll(getChecksToSave(term.Childs));
            }
        }
        return checksToSave;
    }

    protected void markAllCheckViews(boolean value, ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View viewChild = view.getChildAt(i);
            if (viewChild instanceof CheckBox) {
                ((CheckBox) viewChild).setChecked(value);
                return;
            }

            if (viewChild instanceof ViewGroup) {
                this.markAllCheckViews(value, (ViewGroup) viewChild);
            }
        }

    }
}
