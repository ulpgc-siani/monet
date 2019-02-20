package org.monet.utils.oclientwar;

import org.monet.utils.oclient.Main;
import org.monet.utils.oclient.utils.Db;
import org.monet.utils.oclient.utils.ScriptRunner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {

  public String getHomePath() {
    return System.getProperty("user.home") + "/.oclientwar";
  }


  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

//http://localhost:8080/?u=SourcePre&s=test.sql
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.println("Oracle client for Monet. Version 1.0.2");

    if (request.getParameter("u") != null) {
      Context initContext;
      Connection conn = null;
      try {
        initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/" + request.getParameter("u"));
        conn = ds.getConnection();

        ScriptRunner scriptRunner = new ScriptRunner(conn, false, false);
        Reader reader = new FileReader(getHomePath() + "/" + request.getParameter("s"));
        out.println();
        out.println(org.monet.utils.oclient.utils.Files.readFile(getHomePath() + "/" + request.getParameter("s")));
        out.println();

        Date d1 = new Date();
        out.println(scriptRunner.runScript(reader));
        Date d2 = new Date();
        double diff = (d2.getTime() - d1.getTime()) / 1000.0;
        out.println("Task ended in: " + diff + " seconds.");
      } catch (NamingException | SQLException e) {
        e.printStackTrace();
      } finally {

        if (conn != null) try {
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }

    }
/*    Db db;
    try {
      db = new Db(org.monet.utils.oclient.utils.Files.readFile(getHomePath() + "/" + request.getParameter("u")));
      Date d1 = new Date();
      db.executeScript(getHomePath() + "/" + request.getParameter("s"));
      Date d2 = new Date();
      double diff = (d2.getTime()-d1.getTime())/1000.0;
      out.println("Task ended in: " + diff + " seconds.");

    } catch (SQLException e) {
      e.printStackTrace();
    };
*/

  }
}
