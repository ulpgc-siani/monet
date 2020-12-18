package org.monet.deployservice.main;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.engine.Engine;
import org.monet.deployservice.services.ServiceHTTPS;
import org.monet.deployservice.services.ServiceTCP;
import org.monet.deployservice.services.ServiceUDP;

public class DeployService {

	private static Engine engine;
	private static Logger logger;

	private void initialize() {
		initializeLogger();
		initializeConfiguration();
		initializeEngine();
	}

	private void initializeLogger() {
		PropertyConfigurator.configure(Configuration.getHomePath() + "/log4j.properties");
		logger = Logger.getLogger(DeployService.class);
	}

	private void initializeConfiguration() {
		Configuration.getInstance();
	}

	private void initializeEngine() {
		engine = new Engine();
	}

	public static void main(String args[]) throws IOException {
    new DeployService().start();
	}



  public void start() {
//    final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

    Runnable serverTask = new Runnable() {
      @Override
      public void run() {
        try {

          initialize();
          logger.info("Initializing the application");
          logger.info("Charset: " + java.nio.charset.Charset.defaultCharset().name());
          logger.info("Version: " +Configuration.CONST_Version);
          System.out.println(Configuration.CONST_AppCaption + " v" + Configuration.CONST_Version);

          if (!Configuration.isConfigUpdated()) {
            String message = "Configuration file is obsolete. Please, updated file.";
            System.out.println(message);
            logger.error(message);
            return;
          }

          if (Configuration.getValue(Configuration.VAR_ServerUDP).equals("true")) {
            ServiceUDP sendUDP = new ServiceUDP(engine);
            sendUDP.start();
          }

          logger.info("Monet version: " +Configuration.MonetVersionMayor() + "." + Configuration.MonetVersionMinor());
          System.out.println("Monet version: " +Configuration.MonetVersionMayor() + "." + Configuration.MonetVersionMinor());

          if (Configuration.getValue(Configuration.VAR_ServerHTTPS).equals("true")) {
            ServiceHTTPS receiveHTTPS = new ServiceHTTPS(engine);
            receiveHTTPS.start();
          } else {
            ServiceTCP receiveTCP = new ServiceTCP(engine);
            receiveTCP.start();
          }
          System.exit(0);


        } catch (IOException e) {
          System.err.println("Unable to process client request");
          e.printStackTrace();
        }
      }
    };
    Thread serverThread = new Thread(serverTask);
    serverThread.start();

  }


/*
	public void start() throws IOException {
    initialize();

    logger.info("Initializing the application");
    logger.info("Charset: " +java.nio.charset.Charset.defaultCharset().name());
    logger.info("Version: " +Configuration.CONST_Version);
    System.out.println(Configuration.CONST_AppCaption + " v" + Configuration.CONST_Version);

    if (!Configuration.isConfigUpdated()) {
      System.out.println("Configuration file is obsolete. Please, updated file.");
      return;
    }

    if (Configuration.getValue(Configuration.VAR_ServerUDP).equals("true")) {
      ServiceUDP sendUDP = new ServiceUDP(engine);
      sendUDP.start();
    }

    logger.info("Monet version: " +Configuration.MonetVersionMayor() + "." + Configuration.MonetVersionMinor());
    System.out.println("Monet version: " +Configuration.MonetVersionMayor() + "." + Configuration.MonetVersionMinor());

    if (Configuration.getValue(Configuration.VAR_ServerHTTPS).equals("true")) {
      ServiceHTTPS receiveHTTPS = new ServiceHTTPS(engine);
      receiveHTTPS.start();
    } else {
      ServiceTCP receiveTCP = new ServiceTCP(engine);
      receiveTCP.start();
    }
    System.exit(0);
  }
*/
}
