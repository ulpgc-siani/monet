package org.monet.federation.accountoffice.control.sockets;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

import org.monet.federation.accountoffice.control.sockets.impl.AccountSocketFactory;
import org.monet.federation.accountoffice.control.sockets.impl.ActionAccountSocket;
import org.monet.federation.accountoffice.core.components.unitcomponent.BusinessUnitComponent;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;
import org.monet.federation.setupservice.core.model.FederationStatus;

import com.google.inject.Inject;

public class ConnectionHandler implements Runnable {

  private Socket               socket;
  private Logger               logger;
  private BusinessUnitComponent        businessUnitComponent;
  private String               idUnit;
  private FinishedListener     finishedListener;
  private FederationStatus     federationStatus;
  private AccountSocketFactory socketActionFactory;

  private ObjectInputStream    ois;
  private ObjectOutputStream   oos;

  @Inject
  public ConnectionHandler(Logger logger, BusinessUnitComponent businessUnitComponent, FederationStatus federationStatus, AccountSocketFactory socketActionFactory) {
    this.logger = logger;
    this.businessUnitComponent = businessUnitComponent;
    this.federationStatus = federationStatus;
    this.socketActionFactory = socketActionFactory;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public void setFinishedListener(FinishedListener finishedListener) {
    this.finishedListener = finishedListener;
  }

  public void start() {
    try {
      oos = new ObjectOutputStream(this.socket.getOutputStream());
      oos.flush();
      ois = new ObjectInputStream(this.socket.getInputStream());
      
      Thread t = new Thread(this, "ConnectionHandler" + UUID.randomUUID().toString());
      t.start();
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }
  }

  public void run() {
    this.logger.debug("ConnectionHandler.run()");
    
    boolean stop = false;
    while (!stop) {
      String msgID = "xx";
      try {
        SocketMessageModel message = (SocketMessageModel) ois.readObject();
        String id = message.getId();
        msgID = message.getId();

        if (federationStatus.isRunning()) {

          String actionName = message.getAction();

          if (actionName.equals(ActionAccountSocket.ACTION_CLOSE)) {
            SocketResponseMessage resp = new SocketResponseMessage();
            resp.setId(id);
            resp.setIsError(true);
            resp.setResponse(SocketResponseMessage.RESPONSE_CLOSE_CONNECTION);
            oos.writeObject(resp);
            break;
          }

          ActionAccountSocket action = this.socketActionFactory.getAction(actionName);
          SocketResponseMessage resp = action.execute(message);
          resp.setId(id);
          oos.writeObject(resp);
        } else {
          SocketResponseMessage resp = new SocketResponseMessage();
          resp.setId(msgID);
          resp.setIsError(true);
          resp.setResponse(SocketResponseMessage.RESPONSE_FEDERATION_IS_STOP);
          oos.writeObject(resp);
          stop = true;
        }
      } catch (SocketException e) {
        logger.error(e.getMessage(), e);
        break;
      } catch (EOFException e) {
        logger.error(e.getMessage(), e);
        break;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        try {
          SocketResponseMessage resp = new SocketResponseMessage();
          resp.setId(msgID);
          resp.setIsError(true);
          resp.setResponse(SocketResponseMessage.RESPONSE_ERROR_UNKNOW);
          oos.writeObject(resp);
        } catch (Exception ex) {
          logger.error(e.getMessage(), e);
          break;
        }
      }
    }
    if (this.idUnit != null)
      this.businessUnitComponent.disableBusinessUnit(this.idUnit);
    if (!this.socket.isClosed()) {
      try {
        this.ois.close();
        this.oos.close();
        this.socket.close();
      } catch (IOException e) {
        logger.error(e.getMessage(), e);
      } finally {
        this.finishedListener.onSocketClosed(this.socket);
      }
    }
  }
}
