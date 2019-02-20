package org.monet.monitor;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monet.monitor.configuration.Configuration;
import org.monet.monitor.utils.SlackManage;

public class Main {

  private static Logger logger;

  private Main() { initializeLogger(); }

  private void initializeLogger() {
    try {
      if (new File(Configuration.getFileLog2()).exists())
        System.setProperty("log4j.configurationFile", Configuration.getFileLog2());

    } catch (IOException ignore) {
    }
    logger = LogManager.getLogger(this.getClass());
  }

  public static void main(String[] args) throws IOException, Configuration.ConfigurationException, SlackManage.SlackManageException {
    new Main().start();
  }

  private void start() throws Configuration.ConfigurationException, UnknownHostException, SlackManage.SlackManageException {
    System.out.println(Configuration.appCaption() + " v" + Configuration.version() + "\n");
    logger.info("Initializing the application");

    String disks = Configuration.getDisks();
    String[] aDisks = disks.split(";");
    for(String disk : aDisks){
      CheckSizeDisk(disk);
    }
  }

  private static void CheckSizeDisk(String disk) throws Configuration.ConfigurationException, UnknownHostException, SlackManage.SlackManageException {
    long MaxDiskSize = Configuration.getMaxSizeGB();

    logger.info("Check size disk " + disk);

    File file = new File(disk);
    long usableSpace = file.getUsableSpace() / 1024 / 1024 / 1024;


    String info = "";
    info += "Project: *" + Configuration.getProject() + "*\n";
    info += "Host: " + InetAddress.getLocalHost().getHostName() + "\n";
    info += "Space free in '" + disk + "': _" + usableSpace + " GB_\n";
    logger.info(info);

    if (usableSpace < MaxDiskSize) {
      logger.info("Detected problem. Send alert.");
      new SlackManage(Configuration.getSlackToken()).sendMessageToAChannel(Configuration.getSlackChannel(), info);
    }
  }

}
