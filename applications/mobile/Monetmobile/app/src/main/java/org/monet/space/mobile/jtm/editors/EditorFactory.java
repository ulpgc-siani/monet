package org.monet.space.mobile.jtm.editors;

import java.util.HashMap;

import org.monet.mobile.model.TaskDefinition.Step.Edit.Type;
import roboguice.RoboGuice;
import android.content.Context;

public class EditorFactory {

  private HashMap<Type, Class<? extends EditHolder<?>>> holders = new HashMap<Type, Class<? extends EditHolder<?>>>();

  public EditorFactory() {
    holders.put(Type.BOOLEAN, EditBooleanHolder.class);
    holders.put(Type.TEXT, EditTextHolder.class);
    holders.put(Type.DATE, EditDateHolder.class);
    holders.put(Type.NUMBER, EditNumberHolder.class);
    holders.put(Type.MEMO, EditMemoHolder.class);
    holders.put(Type.CHECK, EditCheckHolder.class);
    holders.put(Type.SELECT, EditSelectHolder.class);
    holders.put(Type.POSITION, EditPositionHolder.class);
    holders.put(Type.PICTURE, EditPictureHolder.class);
    holders.put(Type.PICTURE_HAND, EditPictureHandHolder.class);
    holders.put(Type.VIDEO, EditVideoHolder.class);
  }

  public EditHolder<?> build(Type editorType, Context context) {

    EditHolder<?> editHolder = null;
    try {
      editHolder = (EditHolder<?>) RoboGuice.getInjector(context).getInstance(this.holders.get(editorType));
    } catch (NullPointerException e) {
      throw new RuntimeException(String.format("Editor type %s not registered", editorType.toString()));
    } catch (Exception e) {
      throw new RuntimeException(String.format("Error creaing editor %s: %s", editorType.toString(), e.getMessage()), e);
    }
    return editHolder;
  }

}
