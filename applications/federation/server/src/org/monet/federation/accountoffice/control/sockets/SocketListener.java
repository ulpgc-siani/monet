package org.monet.federation.accountoffice.control.sockets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.federation.accountoffice.core.components.unitcomponent.BusinessUnitComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.guice.InjectorFactory;
import org.monet.federation.setupservice.core.model.FederationStatus;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SocketListener implements Runnable, FinishedListener {

	private Logger logger;
    private ServerSocket server;
    private Integer maxClients;
	private final String host;
    private final int port;
	private final Set<String> allowedAddresses;
    private List<Socket> sockets;
    private Thread serverThread;
    private boolean terminate;
    private FederationStatus federationStatus;
    private Provider<ConnectionHandler> connectionHandlerProvider;

    @Inject
    public SocketListener(Logger logger, Configuration configuration, FederationStatus status, Provider<ConnectionHandler> connectionHandlerProvider) {
        this.logger = logger;
        logger.info("SocketListener()");
        this.maxClients = configuration.getSocketMaxSessions();
	    this.host = configuration.getSocketHost();
        this.port = configuration.getSocketPort();
	    this.allowedAddresses = configuration.getSocketAllowedAddresses();
        this.sockets = new ArrayList<Socket>();
        this.federationStatus = status;
        this.connectionHandlerProvider = connectionHandlerProvider;

        connect(this.host, this.port);

        serverThread = new Thread(this, "SocketListener");
        serverThread.start();
    }

    @Override
    public void run() {
        this.logger.info("SocketListener.run()");
        while (true) {
            try {
                if (this.maxClients == 0) {
                    this.logger.warn("Max number of clients reach.");
                    synchronized (this) {
                        this.wait();
                        if (this.server.isClosed())
                            break;
                    }
                }
                if (this.server.isClosed())
                    break;
                Socket socket = this.server.accept();
                if (!isAllowAddress(socket.getInetAddress()) || !federationStatus.isRunning()) {
                    socket.close();
                    continue;
                }
                synchronized (this) {
                    this.sockets.add(socket);
                }
                synchronized (this.maxClients) {
                    this.maxClients--;
                    this.logger.debug("Remove client slot, Total free:" + this.maxClients);
                }
                ConnectionHandler handler = connectionHandlerProvider.get();
                handler.setSocket(socket);
                handler.setFinishedListener(this);
                handler.start();
            } catch (Exception e) {
                if (!terminate)
                    this.logger.error(e.getMessage(), e);
            } finally {
                if (this.server.isClosed() && !terminate) {
                    getBusinessUnitComponent().disableAllBusinessUnits();
                    connect(this.host, this.port);
                }
            }
        }
    }

    private void connect(String host, int port) {
        try {
	        InetAddress address = InetAddress.getByName(host);
            this.server = new ServerSocket(port, 50, address);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    private boolean isAllowAddress(InetAddress inetAddress) {
	    boolean isAllowed = this.allowedAddresses.contains(inetAddress.getHostAddress()); 
	    
	    if (!isAllowed)
		    this.logger.error("Error creating connection with not valid host %s", inetAddress.toString());

        return isAllowed;
    }

    public void close() {
        try {
            terminate = true;

            if (this.server != null && !this.server.isClosed())
                this.server.close();

            synchronized (this) {
                for (Socket socket : this.sockets) {
                    try {
                        if (!socket.isClosed())
                            socket.close();
                    } catch (Exception e) {
                        this.logger.debug(e.getMessage(), e);
                    }
                }
            }

        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void onSocketClosed(Socket socket) {
        synchronized (this.maxClients) {
            this.maxClients++;
            this.logger.debug("Add client slot, Total free:" + this.maxClients);
        }
        synchronized (this) {
            this.sockets.remove(socket);
            this.notifyAll();
        }
    }

    private BusinessUnitComponent getBusinessUnitComponent() {
        return InjectorFactory.get().getInstance(BusinessUnitComponent.class);
    }

}
