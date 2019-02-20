package org.monet.utils.oclient;

import org.monet.utils.oclient.utils.Db;
import org.monet.utils.oclient.utils.Strings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;

public class Main {

  public static void main(String[] args) throws IOException, SQLException {
    System.out.println("Oracle client for Monet. Version 1.0.2");
    new Main().start(args);
  }

  private String getShellParameter(String[] args, String name) {
    String result = "";
    int x = 0;

    for (String arg : args) {
      if (name.toLowerCase().equals(arg.toLowerCase())) {
        result = args[x + 1];
        break;
      }
      x++;
    }

    return result;
  }

  private String getDbUrl(String parameter) {
    String result;

    if (parameter.startsWith("jdbc:oracle:thin:"))
      result = parameter;
    else {
      Strings strings = new Strings();
      try {
        strings.getRegularExpression("(.*)/(.*)@.*", parameter, 0);
      } catch (Exception e) {
        System.err.println("I can't get username/password");
        System.exit(2);
      }
      result = "jdbc:oracle:thin:" + parameter;
    }


    return result;
  }
/*
  private String readFile(String path) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, Charset.defaultCharset());
  }
*/
  private void start(String[] args) throws SQLException, IOException {
    String url = "";
    String sql = "";
    String machine = "";
    String program = "";
    String osuser = "";
    try {
      url = getShellParameter(args, "-u");
      sql = getShellParameter(args, "-s");
      machine = getShellParameter(args, "-m");
      program = getShellParameter(args, "-p");
      osuser = getShellParameter(args, "-osuser");

      if (url.equals("")) throw new Exception("Data connection failed.");
      if (sql.equals("")) throw new Exception("Sentence SQL or filename not found.");
    } catch (Exception e) {
      System.out.println("Use: java -jar oclient.jar [-u <username>/<password>@<domain>[:<port>:<sid>] | -u <filename>] [-s <sentence sql> | -s <filename>] [-m <local machine name>] [-p <program client name>] [-osuser <local user name>]\n");
      System.exit(1);
    }

    File f = new File(url);
    Db db;
    if (f.exists())
      db = new Db(getDbUrl(org.monet.utils.oclient.utils.Files.readFile(url)), osuser, machine, program);
    else
      db = new Db(getDbUrl(url), osuser, machine, program);

    f = new File(sql);
    Date d1 = new Date();
    if (f.exists()) {
      try {
        db.executeScript(sql);
      } catch (SQLException | IOException e) {
        System.err.println("Error in file: " + sql + ". Message: " + e.getMessage());
        e.printStackTrace();
      }
    } else {
      try {
        System.out.println(db.executeSentenceXML(sql));
      } catch (SQLException | IOException e) {
        System.err.println("Error in sentence: " + sql + ". Message: " + e.getMessage());
        e.printStackTrace();
      }
    }

    Date d2 = new Date();
    double diff = (d2.getTime()-d1.getTime())/1000.0;
    System.out.println("Task ended in: " + diff + " seconds.");

  }
}
