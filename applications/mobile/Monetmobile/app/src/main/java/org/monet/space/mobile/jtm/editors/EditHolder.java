package org.monet.space.mobile.jtm.editors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.inject.Inject;
import org.monet.mobile.model.TaskDefinition.Step.Edit;
import org.monet.space.mobile.R;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.view.validator.FormValidator;

import java.util.ArrayList;
import java.util.List;

public abstract class EditHolder<V> implements OnClickListener {
    protected long sourceId;
    protected long taskId;
    protected String taskContext;
    protected String stepName;
    protected Edit edit;
    private boolean readOnly;
    private int nextEditorId = 0;

    private LinearLayout editors;
    protected View add_new_editor;
    protected LayoutInflater inflater;
    protected FragmentManager fragmentManager;
    protected ActivityManager activityManager;
    protected EditorValueManager editorValueManager;
    protected FormValidator validator;

    @Inject
    protected Repository repository;

    public interface ActivityManager {
        void startActivityForResult(Intent intent, int requestCode, EditHolder<?> holder);

        void startActivity(Intent intent);

        LoaderManager getLoaderManager();
    }

    public interface EditorValueManager {
        String getEditorValue(String editorName);
    }

    protected abstract View createEditor(V value, ViewGroup container);

    protected abstract void clearEditor(View editor);

    protected abstract V onExtractItem(View editor);

    protected abstract V onLoadValue(Schema schema);

    protected abstract List<V> onLoadValueArray(Schema schema);

    protected abstract void onSaveValue(Schema schema, V value);

    protected abstract void onSaveValueArray(Schema schema, List<V> value);

    public abstract void onRestoreInstanceState(Bundle holderBundle);

    public abstract void onSaveInstanceState(Bundle holderBundle);

    public void onDestroy() {
    }

    public void onActivityForResult(int requestCode, int resultCode, Intent data) {
    }

    public View onCreateView(ViewGroup container) {
        View editorContainer = inflater.inflate(R.layout.editor_container, container, false);
        editorContainer.setTag(this);

        this.editors = (LinearLayout) editorContainer.findViewById(R.id.editors);

        this.add_new_editor = editorContainer.findViewById(R.id.add_editor);
        if (this.edit.isMultiple && (!this.edit.isReadOnly && !this.readOnly)) {
            this.add_new_editor.setVisibility(View.VISIBLE);
            this.add_new_editor.setOnClickListener(this);
        } else {
            this.add_new_editor.setVisibility(View.GONE);
        }

        TextView label = (TextView) editorContainer.findViewById(R.id.field_label);
        label.setText(edit.label);

        return editorContainer;
    }

    private void addNewEditor(V value) {
        View editor = this.createEditor(value, this.editors);
        editor.setTag(this.nextEditorId++);
        attachClearButtonBehaviorAndVisibility(editor, this.edit.isMultiple & !this.isReadOnly());
        this.editors.addView(editor);
        checkLastEditorIsEmpty();
        editor.requestFocus();
    }

    private void addNewEditorQrMode(V value) {
        View oldEditor = this.editors.getChildAt(0);
        if (oldEditor != null)
            this.editors.removeView(oldEditor);
        View editor = this.createEditor(value, this.editors);
        editor.setTag(this.nextEditorId++);
        attachClearButtonBehaviorAndVisibility(editor, this.edit.isMultiple & !this.isReadOnly());
        this.editors.addView(editor);
        checkLastEditorIsEmpty();
        editor.requestFocus();
    }

    private void attachClearButtonBehaviorAndVisibility(View editor, boolean isEnabled) {
        View clearBtn = editor.findViewById(R.id.clear_editor);
        if (clearBtn == null)
            return;

        if (isEnabled) {
            clearBtn.setOnClickListener(this);
        } else {
            clearBtn.setVisibility(View.GONE);
        }
    }

    protected void onClearEditor(View v) {
        if (editors.getChildCount() == 1)
            this.clearEditor(this.editors.getChildAt(0));
        else {
            View editor = (View) v.getParent();
            if (editor.getTag() != null) {
                editor.setTag(null);
                this.editors.removeView(editor);
            }
        }
        checkLastEditorIsEmpty();
    }

    public void checkLastEditorIsEmpty() {
        if (!this.edit.isMultiple)
            return;

        boolean isEmpty = onExtractItem(this.editors.getChildAt(this.editors.getChildCount() - 1)) == null;

        if (!isEmpty && (!this.edit.isReadOnly && !this.readOnly))
            this.add_new_editor.setVisibility(View.VISIBLE);
        else
            this.add_new_editor.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_editor:
                this.addNewEditor(null);
                checkLastEditorIsEmpty();
                break;
            case R.id.clear_editor:
                this.onClearEditor(v);
                break;
        }
    }

    public void onLoad(Schema schema) {
        if (edit.isMultiple) {
            List<V> values = this.onLoadValueArray(schema);
            if (values == null || values.size() == 0) {
                addNewEditor(null);
            } else {
                for (V value : values)
                    addNewEditor(value);
                checkLastEditorIsEmpty();
            }
        } else {
            V value = onLoadValue(schema);
            addNewEditor(value);
        }
    }

    public void onLoadQrMode(Schema schema) {
        if (edit.isMultiple) {
            //TODO multiple field implementation is not checked
            List<V> values = this.onLoadValueArray(schema);
            if (values == null || values.size() == 0) {
                addNewEditor(null);
            } else {
                for (V value : values)
                    addNewEditor(value);
                checkLastEditorIsEmpty();
            }
        } else {
            V value = onLoadValue(schema);
            addNewEditorQrMode(value);
        }
    }

    public void onSave(Schema schema) {
        int childCount = this.editors.getChildCount();
        List<V> items = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View childView = this.editors.getChildAt(i);
            V value = onExtractItem(childView);
            if (value != null)
                items.add(value);
        }
        if (this.edit.isMultiple)
            this.onSaveValueArray(schema, items);
        else
            this.onSaveValue(schema, items.size() == 1 ? items.get(0) : null);
    }

    public void init(Context context, long sourceId, Edit edit, String stepName, long taskId, String taskContext, ActivityManager activityManager, EditorValueManager editorValueManager, FormValidator validator, boolean readOnly) {
        this.inflater = ((FragmentActivity) context).getLayoutInflater();
        this.fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        this.activityManager = activityManager;
        this.editorValueManager = editorValueManager;
        this.validator = validator;
        this.edit = edit;
        this.stepName = stepName;
        this.taskId = taskId;
        this.taskContext = taskContext;
        this.sourceId = sourceId;
        this.readOnly = readOnly;
    }

    public void load(Schema schema) {
        this.onLoad(schema);
    }

    public void loadQrMode(Schema schema) {
        this.onLoadQrMode(schema);
    }

    protected Context getContext() {
        return this.editors.getContext();
    }

    public void requestFocus() {
        if (this.editors.getChildCount() > 0)
            this.editors.getChildAt(0).requestFocus();
    }

    public String getName() {
        return this.edit.name;
    }

    protected View getEditor(int index) {
        return this.editors.getChildAt(index);
    }

    protected int getEditorIndex(View editor) {
        return this.editors.indexOfChild(editor);
    }

    protected boolean isReadOnly() {
        return this.readOnly || this.edit.isReadOnly;
    }
}