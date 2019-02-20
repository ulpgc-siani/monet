package org.monet.federation.accountoffice.control.sockets;

import java.net.Socket;

public interface FinishedListener {

  void onSocketClosed(Socket socket);
  
}
