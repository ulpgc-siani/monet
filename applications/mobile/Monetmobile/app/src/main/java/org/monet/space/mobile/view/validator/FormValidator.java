package org.monet.space.mobile.view.validator;

import java.util.ArrayList;

import android.widget.TextView;

public class FormValidator {

  private ArrayList<BaseRule> validators = new ArrayList<BaseRule>();

  public RequiredRule verifyNotEmpty(TextView controlToValidate) {
    RequiredRule validator = new RequiredRule(controlToValidate);
    validators.add(validator);
    return validator;
  }

  public MinLengthRule verifyMinLength(TextView controlToValidate) {
    MinLengthRule validator = new MinLengthRule(controlToValidate);
    validators.add(validator);
    return validator;
  }

  public URLRule verifyValidURL(TextView controlToValidate) {
    URLRule validator = new URLRule(controlToValidate);
    validators.add(validator);
    return validator;
  }

  public boolean isValid() {
    for (BaseRule validator : this.validators) {
      if (!validator.check())
        return false;
    }
    return true;
  }

}
