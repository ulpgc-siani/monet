package org.monet.space.mobile.events;

import android.net.Uri;

public class HandwritingImageSavedEvent {
  
  public Uri fileUri;
  
  public HandwritingImageSavedEvent(Uri fileUri) {
    this.fileUri = fileUri;
  }


}
