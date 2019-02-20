package org.monet.space.mobile.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.URLUtil;
import android.widget.TextView;

public class URLRule extends BaseRule implements TextWatcher {

  private TextView controlToValidate;
  private int      errorMessageResId;

  public URLRule(TextView controlToValidate) {
    this.controlToValidate = controlToValidate;
    this.controlToValidate.addTextChangedListener(this);
  }

  public void atErrorShow(int errorMessageResId) {
    this.errorMessageResId = errorMessageResId;
  }

  public boolean check() {
    String url = controlToValidate.getText().toString();
    boolean isValid = url.length() > 0 && URLUtil.isValidUrl(URLUtil.guessUrl(url));
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
