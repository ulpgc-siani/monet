package org.monet.grided.services.federation.impl;

import org.monet.grided.constants.ErrorCode;
import org.monet.grided.control.log.Logger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.exceptions.FederationConnectionException;
import org.monet.grided.services.federation.FederationService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FederationServiceImpl implements FederationService {

//    private boolean isFederationInitialized;
//    private FederationServiceSocketClient client;
//    private Logger logger;
//    private Configuration configuration;
//
//    @Inject
//    private FederationServiceImpl(Logger logger, Configuration configuration) {
//        this.logger = logger;
//        this.configuration = configuration;
//        this.isFederationInitialized = false;
//        this.client = null;
//    }
//
//    public void start() {
//        if (this.isFederationInitialized)
//            return;
//
//        String host = this.configuration.getProperty(Configuration.FEDERATION_SERVICE);
//        String port = this.configuration.getProperty(Configuration.FEDERATION_SERVICE_PORT);
//        String socketPort = this.configuration.getProperty(Configuration.FEDERATION_SERVICE_SOCKET_PORT);
//        String idApp = this.configuration.getProperty(Configuration.API_KEY);
//
//        String xml = "<info>";
//        xml += "<label lang=\"ES\">Setup</label>";
//        xml += "<label lang=\"EN\">Setup</label>";
//        xml += "<description lang=\"ES\"></description><description lang=\"EN\">This is a description</description>";
//        xml += "</info>";
//
//        try {
//            this.client = new FederationServiceSocketClient(host, Integer.valueOf(socketPort), idApp, xml);
//            this.client.init();
//        } catch (Exception e) {
//            logger.error(e.getMessage());            
//            throw new FederationConnectionException(ErrorCode.FEDERATION_CONNECTION, String.format("Domain: %s, port: %s", host, port));
//        }
//
//        this.isFederationInitialized = true;
//    }
//
//    public FederationServiceSocketClient getFederationClient() {
//        return this.client;
//    }
//
//    public void stop() {
//        try {
//            if (this.client == null)
//                return;
//            this.client.close();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }
}