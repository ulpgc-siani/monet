package org.monet.space.mobile.view;

import org.monet.space.mobile.mvp.View;

import android.content.Intent;
import android.os.Bundle;

public interface FederationAccountAuthenticatorView extends View {

  void setUsername(String username);

  void setServerUrl(String serverurl);

  void setAccountAuthenticatorResult(Bundle extras);

  void setResult(int resultCanceled);

  void setResult(int resultOk, Intent intent);

  void showLoading();

  void showForm();

}
