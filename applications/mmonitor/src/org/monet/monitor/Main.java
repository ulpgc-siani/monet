package org.monet.monitor;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.monet.monitor.configuration.Configuration;
import org.monet.monitor.utils.Message;

public class Main {

  private Main() { initializeLogger(); }

  private void initializeLogger() {
    try {
      if (new File(Configuration.getFileLog2()).exists())
        System.setProperty("log4j.configurationFile", Configuration.getFileLog2());

    } catch (IOException ignore) {
    }
  }

  public static void main(String[] args) throws Exception {
    new Main().start();
  }

  private void start() throws Exception {
    System.out.println(Configuration.appCaption() + " v" + Configuration.version() + "\n");

    String disks = Configuration.getDisks();
    String[] aDisks = disks.split(";");
    for(String disk : aDisks){
      CheckSizeDisk(disk);
    }
  }

  private static void CheckSizeDisk(String disk) throws Exception {
    long MinDiskSize = Configuration.getMinSizeGB();

    File file = new File(disk);
    long usableSpace = file.getUsableSpace() / 1024 / 1024 / 1024;


    String info = "mMonitor ["+Configuration.getProject()+", "+InetAddress.getLocalHost().getHostName()+"] Alert: " + "Space free in '" + disk + "': " + usableSpace + " GB";
    if (usableSpace < MinDiskSize) {
      System.out.println(info);
      new Message().send(info);
    }
  }

}
