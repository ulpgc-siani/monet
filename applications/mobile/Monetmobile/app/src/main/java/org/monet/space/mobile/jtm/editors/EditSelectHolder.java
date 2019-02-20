package org.monet.space.mobile.jtm.editors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import org.monet.mobile.model.JobDefinition;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.model.schema.Term;
import org.monet.space.mobile.presenter.PickTermPresenter;

import java.util.ArrayList;
import java.util.List;

import org.monet.space.mobile.R;

public class EditSelectHolder extends EditHolder<Term> {

    @Override
    protected View createEditor(Term value, ViewGroup container) {
        final View editorView = inflater.inflate(R.layout.edit_select, container, false);

        Spinner editor = (Spinner) editorView.findViewById(R.id.field_editor);
        if (this.edit.isEmbedded) {
            ArrayAdapter<Term> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            int position = 0;

            int i = 0;
            for (JobDefinition.Step.Edit.Term editTerm : this.edit.terms) {
                Term term = new Term();
                term.code = editTerm.key;
                term.label = editTerm.label;
                adapter.add(term);
                if (value != null && term.code.equals(value.code))
                    position = i;
                i++;
            }

            editor.setAdapter(adapter);
            editor.setSelection(position);
            if (this.isReadOnly())
                editor.setEnabled(false);
        } else {
            editor.setVisibility(View.GONE);
            TextView textView = (TextView) editorView.findViewById(R.id.field_editor_opener);
            textView.setVisibility(View.VISIBLE);
            if (!this.isReadOnly()) {
                textView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();

                        boolean isEmbedded = edit.terms != null && edit.terms.size() > 0;
                        bundle.putBoolean(PickTermPresenter.IS_EMBEDDED, isEmbedded);
                        String glossaryCode, glossaryContext, from;
                        int depth;
                        Flatten flatten;
                        if (isEmbedded) {
                            glossaryCode = sourceId + edit.code;
                            glossaryContext = "0";
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
                                String filterValue;
                                if (filter.startsWith("$V$"))
                                    filterValue = filter.substring(3);
                                else {
                                    String editorName = filter.substring(3);
                                    filterValue = editorValueManager.getEditorValue(editorName);
                                }
                                if (filterValue != null && filterValue.length() > 0)
                                    filterValues.add(filterValue);
                            }
                            bundle.putStringArrayList(PickTermPresenter.TAG_FILTERS, filterValues);
                        }
                        bundle.putString(PickTermPresenter.GLOSSARY_CODE, glossaryCode);
                        bundle.putString(PickTermPresenter.GLOSSARY_CONTEXT, glossaryContext);
                        bundle.putString(PickTermPresenter.FROM, from);
                        bundle.putString(PickTermPresenter.FLATTEN, flatten.toString());
                        bundle.putInt(PickTermPresenter.DEPTH, depth);

                        Intent intent = new Intent("monet.action.PICK_TERM");
                        intent.putExtras(bundle);

                        activityManager.startActivityForResult(intent, getEditorIndex(editorView), EditSelectHolder.this);
                    }
                });
            }
            textView.setText(value != null ? value.label : getContext().getString(R.string.no_select_value));
            textView.setTag(value);
        }
        return editorView;
    }

    @Override
    protected Term onLoadValue(Schema schema) {
        return schema.getTerm(this.edit.name);
    }

    @Override
    protected List<Term> onLoadValueArray(Schema schema) {
        return schema.getTermList(this.edit.name);
    }

    @Override
    protected void onSaveValue(Schema schema, Term value) {
        if (this.isReadOnly()) return;

        schema.putTerm(this.edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<Term> value) {
        if (this.isReadOnly()) return;
        schema.putTermList(this.edit.name, value);
    }

    @Override
    protected void clearEditor(View editor) {
        if (isReadOnly()) return;
        TextView textView = (TextView) editor.findViewById(R.id.field_editor_opener);
        textView.setText(getContext().getString(R.string.no_select_value));
        textView.setTag(null);
    }

    @Override
    protected Term onExtractItem(View editor) {
        if (this.edit.isEmbedded)
            return (Term) ((Spinner) editor.findViewById(R.id.field_editor)).getSelectedItem();
        return (Term) editor.findViewById(R.id.field_editor_opener).getTag();
    }

    @Override
    public void onActivityForResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) return;
        String key = intent.getStringExtra(PickTermPresenter.RESULT_KEY);
        String label = intent.getStringExtra(PickTermPresenter.RESULT_LABEL);
        View editor = this.getEditor(requestCode);
        TextView textView = (TextView) editor.findViewById(R.id.field_editor_opener);
        textView.setText(label);
        Term value = new Term();
        value.code = key;
        value.label = label;
        textView.setTag(value);
        this.checkLastEditorIsEmpty();
    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {

    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {

    }
}
