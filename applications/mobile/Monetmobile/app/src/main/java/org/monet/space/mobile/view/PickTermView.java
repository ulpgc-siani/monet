package org.monet.space.mobile.view;

import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.mvp.View;

import android.content.Intent;
import android.database.Cursor;

public interface PickTermView extends View {

  void setFlatten(Flatten flatten);

  void setTerms(Cursor data);

  void setResult(int result, Intent intent);

  void setIsMaxDepth(boolean isMaxDepth);


}
