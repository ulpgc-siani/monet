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

public class EditNumberHolder extends EditHolder<Double> {

    @Override
    protected View createEditor(Double value, final ViewGroup container) {
        View editor = this.inflater.inflate(R.layout.edit_number, container, false);
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

        itemEditor.setText(value != null ? value.toString() : "");
        return editor;
    }

    @Override
    protected void clearEditor(View editor) {
        if (this.isReadOnly()) return;

        EditText itemEditor = (EditText) editor.findViewById(R.id.item_editor);
        itemEditor.setText("");
    }

    @Override
    protected Double onExtractItem(View editor) {
        EditText itemEditor = (EditText) editor.findViewById(R.id.item_editor);
        Double value = null;
        try {
            value = Double.parseDouble(itemEditor.getText().toString());
        } catch (Exception ex) {
        }
        return value;
    }

    @Override
    protected Double onLoadValue(Schema schema) {
        return schema.getNumber(this.edit.name);
    }

    @Override
    protected List<Double> onLoadValueArray(Schema schema) {
        return schema.getNumberList(this.edit.name);
    }

    @Override
    protected void onSaveValue(Schema schema, Double value) {
        if (this.isReadOnly()) return;

        if (value != null)
            schema.putNumber(this.edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<Double> value) {
        if (this.isReadOnly()) return;

        schema.putNumberList(this.edit.name, value);
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {

    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {

    }

}
