package org.monet.space.mobile.jtm.editors;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import org.monet.space.mobile.model.schema.Schema;

import java.util.List;

import org.monet.space.mobile.R;

public class EditBooleanHolder extends EditHolder<Boolean> {

    protected Boolean onLoadValue(Schema schema) {
        return schema.getBoolean(this.edit.name);
    }

    protected List<Boolean> onLoadValueArray(Schema schema) {
        return schema.getBooleanList(this.edit.name);
    }

    protected View createEditor(Boolean value, final ViewGroup container) {
        View editor = this.inflater.inflate(R.layout.edit_boolean, container, false);
        final CheckBox itemEditor = (CheckBox) editor.findViewById(R.id.item_editor);
        itemEditor.setText(this.edit.label);
        itemEditor.setChecked(value);
        if (this.isReadOnly())
            itemEditor.setEnabled(false);
        return editor;
    }

    protected void clearEditor(View editor) {
        if (!isReadOnly())
            ((CheckBox) editor.findViewById(R.id.item_editor)).setChecked(false);
    }

    @Override
    protected void onSaveValue(Schema schema, Boolean value) {
        if (!isReadOnly())
            schema.putBoolean(edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<Boolean> value) {
        if (!isReadOnly())
            schema.putBooleanList(edit.name, value);
    }

    @Override
    protected Boolean onExtractItem(View itemView) {
        CheckBox itemEditor = (CheckBox) itemView.findViewById(R.id.item_editor);
        return itemEditor.isChecked();
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {

    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {

    }

}
