package org.monet.space.mobile.events;

public class DetailsPageChangedEvent {

  private boolean showingChatPage = false;
  
  public DetailsPageChangedEvent(boolean showingChatPage) {
    this.showingChatPage = showingChatPage;
  }
  
  public boolean isShowingChatPage() {
    return showingChatPage;
  }
  
  public boolean isShowingDetailsPage() {
    return !showingChatPage;
  }
  
}
