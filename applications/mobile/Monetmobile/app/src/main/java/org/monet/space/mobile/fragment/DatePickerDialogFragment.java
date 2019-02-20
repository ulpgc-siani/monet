package org.monet.space.mobile.fragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerDialogFragment extends DialogFragment implements OnDateSetListener {

  private TextView view;
  private Date     defaultValue;

  public DatePickerDialogFragment() {
  }

  public static DatePickerDialogFragment create(TextView view, Date defaultValue) {
    DatePickerDialogFragment f = new DatePickerDialogFragment();
    f.view = view;
    f.defaultValue = defaultValue;
    return f;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Calendar cal = Calendar.getInstance();
    if (this.defaultValue != null)
      cal.setTime(this.defaultValue);

    return new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
  }

  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, monthOfYear, dayOfMonth);
    Date value = calendar.getTime();
    this.view.setTag(value);
    this.view.setText(DateFormat.getDateInstance().format(value));
  }

}
