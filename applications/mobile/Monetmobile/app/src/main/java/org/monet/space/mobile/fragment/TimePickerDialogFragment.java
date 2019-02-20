package org.monet.space.mobile.fragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements OnTimeSetListener {

  private TextView view;
  private Date     defaultValue;

  public TimePickerDialogFragment() {
  }

  public static TimePickerDialogFragment create(TextView view, Date defaultValue) {
    TimePickerDialogFragment f = new TimePickerDialogFragment();
    f.view = view;
    f.defaultValue = defaultValue;
    return f;
  }

  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Calendar cal = Calendar.getInstance();
    if (this.defaultValue != null)
      cal.setTime(this.defaultValue);

    return new TimePickerDialog(getActivity(), this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(1900, 1, 1, hourOfDay, minute);
    Date value = calendar.getTime();
    this.view.setTag(value);
    this.view.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(value));
  }

}
