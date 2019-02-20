package org.monet.monitor.utils;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.request.files.FilesUploadRequest;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsListResponse;
import com.github.seratch.jslack.api.model.Channel;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monet.monitor.configuration.Configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class SlackManage {

  private Logger logger;
  private String token = "";
  private Slack session = Slack.getInstance();
  private ChannelsListResponse channelsResponse = null;
  private Socks socks = new Socks();

  public static class SlackManageException extends Exception {
    SlackManageException(Throwable cause) {super(cause);}
    SlackManageException(String cause) {
      super(cause);
    }
  }

  public SlackManage(String token) throws SlackManageException, Configuration.ConfigurationException {
    logger = LogManager.getLogger(this.getClass());
    socks.enable();
    if (! "".equals(token)) {
      this.token = token;
      try {
        channelsResponse = session.methods().channelsList(ChannelsListRequest.builder().token(token).build());
      } catch (Exception e) {
        throw new SlackManageException(e);
      }
    }
  }

  public void sendMessageToAChannel(String channelName, String message) throws SlackManageException, Configuration.ConfigurationException {
    this.sendMessageToAChannel(channelName,message,"");
  }

  private void sendMessageToAChannel(String channelName, String message, String body) throws SlackManageException, Configuration.ConfigurationException {
    socks.enable();

    if ((! "".equals(channelName)) && (session != null)) {
      //WARNING: First bot must be receive a invitation.
      try {
        if (! channelsResponse.isOk())
          throw new SlackManageException(channelsResponse.getError());

        if (channelsResponse.getChannels().stream().noneMatch(c -> c.getName().equals(channelName)))
          throw new SlackManageException("Channel '"+channelName+"' not exits.");

        Channel channel = channelsResponse.getChannels().stream()
            .filter(c -> c.getName().equals(channelName)).findFirst().get();

        if ("".equals(body)) {
          session.methods().chatPostMessage(
              ChatPostMessageRequest.builder()
                  .token(token)
                  .channel(channel.getId())
                  .text(message)
                  .build());
        } else {
          InputStream in = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
          final File tempFile = File.createTempFile("grid-server-slack", ".tmp");
          tempFile.deleteOnExit();
          try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
          }

          session.methods().filesUpload(FilesUploadRequest.builder()
              .token(token)
              .channels(Collections.singletonList(channel.getId()))
              .file(tempFile)
              .filename("info.txt")
              .title(message)
              .build());

        }
      } catch (Exception e) {
        throw new SlackManageException(e);
      }
    } else {
      if ("".equals(channelName)) logger.warn("SlackManage channel name is empty.");
      if (session == null) logger.warn("SlackManage session is null.");
    }
  }
}
