package org.monet.space.mobile.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class MinLengthRule extends BaseRule implements TextWatcher {

  private TextView controlToValidate;
  private int      errorMessageResId;
  private int      minLength = 0;

  public MinLengthRule(TextView controlToValidate) {
    this.controlToValidate = controlToValidate;
    this.controlToValidate.addTextChangedListener(this);
  }

  public MinLengthRule withMinLength(int minLength) {
    this.minLength = minLength;
    return this;
  }

  public void atErrorShow(int errorMessageResId) {
    this.errorMessageResId = errorMessageResId;
  }

  public boolean check() {
    boolean isValid = controlToValidate.getText().length() >= this.minLength;
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
