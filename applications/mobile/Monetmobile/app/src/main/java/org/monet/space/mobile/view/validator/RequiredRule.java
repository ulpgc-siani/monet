package org.monet.space.mobile.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class RequiredRule extends BaseRule implements TextWatcher {

  private TextView controlToValidate;
  private int      errorMessageResId;

  public RequiredRule(TextView controlToValidate) {
    this.controlToValidate = controlToValidate;
    this.controlToValidate.addTextChangedListener(this);
  }

  public void atErrorShow(int errorMessageResId) {
    this.errorMessageResId = errorMessageResId;
  }

  public boolean check() {
    boolean isValid = controlToValidate.getText().length() > 0;
    if (!isValid)
      controlToValidate.setError(controlToValidate.getContext().getText(errorMessageResId));
    return isValid;
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    check();
  }

  @Override
  public void afterTextChanged(Editable s) {
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }
}
