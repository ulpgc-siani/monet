package org.monet.monitor.utils.messages;

import com.andrewthom.microsoft.teams.api.IncomingWebhookRequest;
import com.andrewthom.microsoft.teams.api.MicrosoftTeams;
import com.mashape.unirest.http.exceptions.UnirestException;

class TeamsManage {

  private IncomingWebhookRequest teams = null;

  TeamsManage(String url) {
    teams = MicrosoftTeams.forUrl(() -> url);
  }

  void send(String message) throws UnirestException {
    teams.sendMessage( message);
  }



}
