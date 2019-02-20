package org.monet.space.mobile.jtm.editors;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import org.monet.space.mobile.model.schema.Schema;

import java.util.List;

import org.monet.space.mobile.R;

public class EditTextHolder extends EditHolder<String> {

    public int getEditResId() {
        return R.layout.edit_text;
    }

    protected String onLoadValue(Schema schema) {
        return schema.getText(this.edit.name);
    }

    protected List<String> onLoadValueArray(Schema schema) {
        return schema.getTextList(this.edit.name);
    }

    protected View createEditor(String value, final ViewGroup container) {
        View editor = this.inflater.inflate(this.getEditResId(), container, false);
        final EditText itemEditor = (EditText) editor.findViewById(R.id.item_editor);
        itemEditor.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ((EditText) v).getText().length() == 0)
                    onClearEditor(v);
            }
        });
        itemEditor.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (container.getChildAt(container.getChildCount() - 1) == itemEditor.getParent())
                    checkLastEditorIsEmpty();
            }
        });
        if (this.edit.isRequired)
            this.validator.verifyNotEmpty(itemEditor).atErrorShow(R.string.error_empty_editor);

        if (this.isReadOnly())
            itemEditor.setEnabled(false);
        itemEditor.setText(value != null ? value : "");
        return editor;
    }

    protected void clearEditor(View editor) {
        if (this.isReadOnly()) return;

        EditText itemEditor = (EditText) editor.findViewById(R.id.item_editor);
        itemEditor.setText("");
    }

    @Override
    protected void onSaveValue(Schema schema, String value) {
        if (this.isReadOnly()) return;

        schema.putText(this.edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<String> value) {
        if (this.isReadOnly()) return;

        schema.putTextList(this.edit.name, value);
    }

    @Override
    protected String onExtractItem(View itemView) {
        EditText itemEditor = (EditText) itemView.findViewById(R.id.item_editor);
        String value = itemEditor.getText().toString();
        return value.length() == 0 ? null : value;
    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {
    }
}
