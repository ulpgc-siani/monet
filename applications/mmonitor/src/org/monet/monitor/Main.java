package org.monet.monitor;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.monet.monitor.configuration.Configuration;
import org.monet.monitor.utils.messages.Message;

public class Main {

//  private static Logger logger;

  private Main() { initializeLogger(); }

  private void initializeLogger() {
    try {
      if (new File(Configuration.getFileLog2()).exists())
        System.setProperty("log4j.configurationFile", Configuration.getFileLog2());

    } catch (IOException ignore) {
    }
//    logger = LogManager.getLogger(this.getClass());
  }

  public static void main(String[] args) throws Exception {
    new Main().start();
  }

  private void start() throws Exception {
    System.out.println(Configuration.appCaption() + " v" + Configuration.version() + "\n");
//    logger.info("Initializing the application");

    String disks = Configuration.getDisks();
    String[] aDisks = disks.split(";");
    for(String disk : aDisks){
      CheckSizeDisk(disk);
    }
  }

  private static void CheckSizeDisk(String disk) throws Exception {
    long MinDiskSize = Configuration.getMinSizeGB();

//    logger.info("Check size disk " + disk);

    File file = new File(disk);
    long usableSpace = file.getUsableSpace() / 1024 / 1024 / 1024;


/*    String info = "";
    info += "Project: *" + Configuration.getProject() + "*\n";
    info += "Host: " + InetAddress.getLocalHost().getHostName() + "\n";
    info += "Space free in '" + disk + "': _" + usableSpace + " GB_\n";
*/
    String info = "mMonitor ["+Configuration.getProject()+", "+InetAddress.getLocalHost().getHostName()+"] Alert: " + "Space free in '" + disk + "': " + usableSpace + " GB";
    if (usableSpace < MinDiskSize) {
      System.out.println(info);
//      logger.info(info);
      new Message(Configuration.getSlackToken(), Configuration.getSlackChannel(), Configuration.getTeamsURL()).send(info);
    }
  }

}
