package org.monet.mocks.singlesignon.service.federation;

import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import java.io.*;
import java.net.Socket;

public class ConnectionClientHandler {

  public static final String ERROR = "error";

  private PrintWriter out;
  private BufferedReader in;

  private Socket socket;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;

  public ConnectionClientHandler(Socket socket) throws Exception {
    this.socket = socket;

    ois = new ObjectInputStream(this.socket.getInputStream());
    oos = new ObjectOutputStream(this.socket.getOutputStream());

    out = new PrintWriter(this.socket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
  }

  public String execute(String message, String id, boolean waitResponse) throws IOException {
    synchronized (this.socket) {
      try {
        out.println(message.trim());

        while (true) {
          String ret = in.readLine();
          if (waitResponse) {
            int msgIdFirst = ret.indexOf("=");
            int msgIdLast = ret.indexOf(";");
            if (msgIdFirst == -1 || msgIdLast == -1)
              continue;
            String msgID = ret.substring(msgIdFirst + 1, msgIdLast);
            if (!msgID.equals(id))
              continue;
            ret = ret.substring(msgIdLast + 1);
          }
          return ret;
        }
      } catch (IOException e) {
        try {
          this.close();
        } catch (Exception ex) {
        }
        throw e;
      }
    }
  }

  public SocketResponseMessage execute(SocketMessageModel message, boolean waitResponse) throws Exception {
    synchronized (this.socket) {
      try {
        oos.writeObject(message);

        while (true) {
          SocketResponseMessage ret = (SocketResponseMessage) ois.readObject();
          if (waitResponse) {
            String msgID = ret.getId();
            if (!msgID.equals(message.getId()))
              continue;
          }
          if(ret.isError())
            throw new RuntimeException(ret.getResponse());
          return ret;
        }
      } catch (IOException e) {
        try {
          this.close();
        } catch (Exception ex) {
        }
        throw e;
      }
    }
  }

  public void close() throws IOException {
    this.out.close();
    this.in.close();
    this.socket.close();
  }

  public boolean isOpen() {
    return this.socket != null && !this.socket.isClosed();
  }
}
