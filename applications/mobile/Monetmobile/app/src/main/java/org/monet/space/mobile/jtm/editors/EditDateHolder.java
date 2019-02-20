package org.monet.space.mobile.jtm.editors;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import org.monet.space.mobile.fragment.DatePickerDialogFragment;
import org.monet.space.mobile.fragment.TimePickerDialogFragment;
import org.monet.space.mobile.model.schema.Schema;
import org.monet.space.mobile.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditDateHolder extends EditHolder<Date> implements OnClickListener {
    private static DateFormat DATE_FORMATTER = DateFormat.getDateInstance();
    private static DateFormat TIME_FORMATTER = DateFormat.getTimeInstance(DateFormat.SHORT);

    @Override
    protected View createEditor(Date value, ViewGroup container) {
        View editorView = inflater.inflate(R.layout.edit_date, container, false);

        if (value == null)
            value = new Date();

        TextView dateEditor = (TextView) editorView.findViewById(R.id.field_date_value);
        dateEditor.setText(DATE_FORMATTER.format(value));
        dateEditor.setTag(value);

        TextView timeEditor = (TextView) editorView.findViewById(R.id.field_time_value);
        timeEditor.setText(TIME_FORMATTER.format(value));
        timeEditor.setTag(value);

        if (!this.isReadOnly()) {
            dateEditor.setOnClickListener(this);
            timeEditor.setOnClickListener(this);
        }

        return editorView;
    }

    @Override
    protected void clearEditor(View editor) {

    }

    @Override
    protected Date onExtractItem(View editor) {
        TextView dateEditor = (TextView) editor.findViewById(R.id.field_date_value);
        TextView timeEditor = (TextView) editor.findViewById(R.id.field_time_value);

        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime((Date) dateEditor.getTag());

        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime((Date) timeEditor.getTag());

        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        dateCalendar.set(Calendar.SECOND, 0);
        dateCalendar.set(Calendar.MILLISECOND, 0);

        return dateCalendar.getTime();
    }

    @Override
    protected Date onLoadValue(Schema schema) {
        return schema.getDate(this.edit.name);
    }

    @Override
    protected List<Date> onLoadValueArray(Schema schema) {
        return schema.getDateList(this.edit.name);
    }

    @Override
    protected void onSaveValue(Schema schema, Date value) {
        if (this.isReadOnly()) return;
        schema.putDate(this.edit.name, value);
    }

    @Override
    protected void onSaveValueArray(Schema schema, List<Date> value) {
        if (this.isReadOnly()) return;
        schema.putDateList(this.edit.name, value);
    }

    @Override
    public void onClick(View v) {
        Fragment prev;
        FragmentTransaction ft;
        DialogFragment newFragment;

        switch (v.getId()) {
            case R.id.field_date_value:
                ft = this.fragmentManager.beginTransaction();

                prev = this.fragmentManager.findFragmentByTag("date_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                newFragment = DatePickerDialogFragment.create((TextView) v, (Date) v.getTag());
                newFragment.show(ft, "date_dialog");
                break;
            case R.id.field_time_value:
                ft = this.fragmentManager.beginTransaction();

                prev = this.fragmentManager.findFragmentByTag("time_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                newFragment = TimePickerDialogFragment.create((TextView) v, (Date) ((View) v.getParent()).findViewById(R.id.field_time_value).getTag());
                newFragment.show(ft, "time_dialog");
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle holderBundle) {

    }

    @Override
    public void onRestoreInstanceState(Bundle holderBundle) {

    }

}
