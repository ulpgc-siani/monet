package org.monet.monitor.utils.messages;

public class Message {

  private SlackManage slack = null;
  private TeamsManage teams = null;
  private String channel;

  public Message(String slackToken, String slackChannel, String teamsUrl) {
    try {
      if (slackToken != null && slackChannel != null) {
        this.channel = slackChannel;
        slack = new SlackManage(slackToken);
      }
    } catch (SlackManage.SlackManageException ignore) { }

    if (teamsUrl != null) {
        teams = new TeamsManage(teamsUrl);
    }
  }

  public void send(String message) throws Exception {
    Integer maxSize=10000;
    Integer size = message.length();
    if (size > maxSize) {
      message = extractWarningErrors(message);
    }

    if (slack != null) slack.sendMessageToAChannel(channel, message);
    if (teams != null) teams.send(message.replaceAll("\n", "<br />"));

  }

  private String extractWarningErrors(String message) {
    StringBuilder result = new StringBuilder();

    String[] lines = message.split(System.getProperty("line.separator"));

    int x=0;
    int first_error=-1;
    for (String line: lines) {
      if (x < 7) result.append(line).append("\n");
      if (x == 7) result.append("...").append("\n");
//      if (line.contains("[WARNING]")) result.append(line).append("\n");

      if (line.contains("[ERROR]")) {
        if (first_error < 0) {
          first_error = x;
          int start_line = first_error - 10;
          if (start_line > 7) {
            for (int i=start_line; i<first_error; i++) {
              result.append(lines[i]).append("\n");
            }
          }
        }
        result.append(line).append("\n");
      }
      x++;
    }

    return result.toString();
  }
}
