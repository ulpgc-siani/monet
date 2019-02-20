package org.monet.space.mobile.view;

import android.content.Intent;
import org.monet.space.mobile.mvp.View;

public interface AccountView extends View {

    void setUsername(String value);

    void setBusinessUnitUrl(String value);

    void setResult(int resultCode, Intent intent);

    void setSyncEnabled(boolean value);

    void backToLoginActivity();

}
