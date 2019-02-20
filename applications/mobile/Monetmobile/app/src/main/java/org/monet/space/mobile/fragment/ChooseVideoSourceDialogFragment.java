package org.monet.space.mobile.fragment;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.EditVideoCancelEvent;
import org.monet.space.mobile.events.EditVideoFromCameraEvent;
import org.monet.space.mobile.events.EditVideoFromGalleryEvent;
import org.monet.space.mobile.events.EditVideoViewCurrentEvent;
import org.monet.space.mobile.mvp.BusProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.squareup.otto.Bus;

public class ChooseVideoSourceDialogFragment extends DialogFragment {

  private static final int VIEW_CURRENT   = 0;
  private static final int CAMERA_SOURCE  = 1;
  private static final int GALLERY_SOURCE = 2;

  private String           videoId        = null;
  private boolean          itemSelected   = false;

  public static ChooseVideoSourceDialogFragment create(String videoId) {
    ChooseVideoSourceDialogFragment f = new ChooseVideoSourceDialogFragment();
    f.videoId = videoId;
    return f;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
    builder.setTitle(R.string.choose_video_source_dialog_title);
    int itemsResId = this.videoId == null ? R.array.choose_video_source_dialog_items_emtpy : R.array.choose_video_source_dialog_items_filled;
    builder.setItems(itemsResId, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int itemId) {
        if (videoId == null)
          itemId++;

        Bus bus = BusProvider.get();
        switch (itemId) {
          case VIEW_CURRENT:
            bus.post(new EditVideoViewCurrentEvent());
            break;
          case CAMERA_SOURCE:
            bus.post(new EditVideoFromCameraEvent());
            break;
          case GALLERY_SOURCE:
            bus.post(new EditVideoFromGalleryEvent());
            break;
        }
        itemSelected = true;
      }

    });

    return builder.create();
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);

    if (itemSelected)
      return;

    BusProvider.get().post(new EditVideoCancelEvent());
  }
}
