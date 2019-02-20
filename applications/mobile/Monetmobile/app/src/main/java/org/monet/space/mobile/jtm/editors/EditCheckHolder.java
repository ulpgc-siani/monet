package org.monet.space.mobile.jtm.editors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.R;
import org.monet.space.mobile.model.schema.Check;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.presenter.PickCheckPresenter;

import java.util.ArrayList;
import java.util.List;

public class EditCheckHolder extends EditHolder<List<Check>> {

    @Override
    protected View createEditor(final List<Check> checklist, ViewGroup container) {
        final View editorView = this.inflater.inflate(R.layout.edit_check, container, false);

        TextView textView = (TextView) editorView.findViewById(R.id.field_editor_opener);
        textView.setVisibility(View.VISIBLE);
        textView.setText(getText(checklist));
        textView.setTag(checklist);

        if (!isReadOnly())
            textView.setOnClickListener(createPickCheckListener(editorView));

        return editorView;

    }

    private OnClickListener createPickCheckListener(final View editorView) {
        return new OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                boolean isEmbedded = edit.terms != null && edit.terms.size() > 0;
                bundle.putBoolean(PickCheckPresenter.IS_EMBEDDED, isEmbedded);
                String glossaryCode, glossaryContext, from;

                int depth;
                Flatten flatten;
                if (isEmbedded) {
                    glossaryCode = sourceId + edit.code;
                    glossaryContext = "0";
                    from = null;
                    flatten = Flatten.ALL;
                    depth = -1;
                } else if (edit.useGlossary == null) {
                    glossaryCode = null;
                    glossaryContext = null;
                    from = null;
                    flatten = Flatten.ALL;
                    depth = -1;
                } else {
                    UseGlossary use = edit.useGlossary;
                    glossaryCode = sourceId + use.source;
                    glossaryContext = taskContext;
                    from = use.from;
                    flatten = use.flatten;
                    depth = use.depth;

                    ArrayList<String> filterValues = new ArrayList<>();
                    for (String filter : use.filters) {
                        String filterValue = filterValue(filter);
                        if (filterValue != null && filterValue.length() > 0)
                            filterValues.add(filterValue);
                    }
                    bundle.putStringArrayList(PickCheckPresenter.TAG_FILTERS, filterValues);
                }
                bundle.putString(PickCheckPresenter.GLOSSARY_CODE, glossaryCode);
                bundle.putString(PickCheckPresenter.GLOSSARY_CONTEXT, glossaryContext);
                bundle.putString(PickCheckPresenter.FROM, from);
                bundle.putString(PickCheckPresenter.FLATTEN, flatten.toString());
                bundle.putInt(PickCheckPresenter.DEPTH, depth);

                Object tag = v.getTag();
                if ((tag != null) && (tag instanceof ArrayList<?>)) {
                    bundle.putParcelableArrayList(PickCheckPresenter.VALUE, (ArrayList<Check>) tag);
                }

                Intent intent = new Intent("monet.action.PICK_CHECK");
                intent.putExtras(bundle);

                activityManager.startActivityForResult(intent, getEditorIndex(editorView), EditCheckHolder.this);
            }
        };
    }

    private String filterValue(String filter) {
        if (filter.startsWith("$V$")) {
            return filter.substring(3);
        }
        return editorValueManager.getEditorValue(filter.substring(3));
    }

    private String getText(List<Check> checklist) {
        String result = "";
        if (checklist != null) {

            for (Check check : checklist) {
                if (check.isChecked)
                    result += "- " + check.label + "\n";
            }
        }

        if (!result.equals(""))
            return result;

        return getContext().getString(R.string.check_empty);
    }

    @Override
    protected List<Check> onLoadValue(Schema schema) {
        return schema.getCheck(edit.name);
    }

    @Override
    protected List<List<Check>> onLoadValueArray(Schema schema) {
        return null;
    }

    @Override
    protected void clearEditor(View editor) {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Check> onExtractItem(View editor) {
        TextView textView = (TextView) editor.findViewById(R.id.field_editor_opener);

        Object tag = textView.getTag();
        if (tag == null) return null;
        return (List<Check>) tag;
    }

    @Override
    public void onActivityForResult(int requestCode, int resultCode, Intent intent) {
        if (this.isReadOnly() || resultCode != Activity.RESULT_OK) return;
        View editor = this.getEditor(requestCode);

        Bundle bundle = intent.getExtras();

        List<Check> checks = bundle.getParcelableArrayList(PickCheckPresenter.VALUE);

        TextView textView = (TextView) editor.findViewById(R.id.field_editor_opener);
        textView.setText(getText(checks));
        textView.setTag(checks);
        checkLastEditorIsEmpty();
    }

    @Override
    protected void onSaveValue(Schema schema, List<Check> value) {
        if (this.isReadOnly()) return;

        schema.putCheck(this.edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<List<Check>> value) {
    }

    public void checkLastEditorIsEmpty() {
        if (this.add_new_editor.getVisibility() != View.GONE)
            this.add_new_editor.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {

    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {

    }

}
