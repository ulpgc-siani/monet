package org.monet.space.mobile.view;

import android.content.Intent;
import org.monet.space.mobile.jtm.editors.EditHolder;
import org.monet.space.mobile.jtm.editors.EditHolder.ActivityManager;
import org.monet.space.mobile.mvp.View;

import java.util.Map;

public interface StepView extends View, ActivityManager {

    class Result {
        public String Name;
        public int RequestCode;
        public int ResultCode;
        public Intent Data;

        public Result(String name, int requestCode, int resultCode, Intent data) {
            this.Name = name;
            this.RequestCode = requestCode;
            this.ResultCode = resultCode;
            this.Data = data;
        }
    }

    Map<String, EditHolder<?>> getEditHolders();

    void addEditor(EditHolder<?> holder);

    void setStepDescription(String description);

    void setTitle(String title);

    void setStepLabel(String label);

    void previousButton(boolean enable);

    void nextButton(boolean enable);

    void finishButtonVisibility(boolean visible);

    void buttonsVisibility(boolean visible);

    void dispatchPendingResults();


}
