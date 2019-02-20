package org.monet.space.mobile.presenter;

import java.util.Date;

import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.model.ChatItem;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.TaskChatPageView;

import android.accounts.Account;

import com.google.inject.Inject;

public class TaskChatPagePresenter extends Presenter<TaskChatPageView, Void> {

  @Inject
  private Repository mRepository;
  
  private Account mAccount;
  
  public void initialize() {
  }

  public void resume() {
  }

  public void send(TaskDetails task, String message) {
    ChatItem item = mRepository.addChatItem(task.id, task.serverId, task.sourceId, message, new Date(), true);
    this.view.addChatItem(item);

    if(mAccount == null)
      mAccount = new Account(mRepository.getSource(task.sourceId).accountName, FederationAccountAuthenticator.ACCOUNT_TYPE); 
    
    SyncAccountHelper.syncAccountChats(mAccount);
  }

}
