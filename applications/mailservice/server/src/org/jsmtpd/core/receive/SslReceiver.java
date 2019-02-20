package org.jsmtpd.core.receive;

import java.io.IOException;
import java.net.URL;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.generic.threadpool.GrowingThreadPool;

public class SslReceiver extends Receiver {
	private Log log = LogFactory.getLog(SslReceiver.class);
	
	public SslReceiver(int port, int maxInst) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		initSslEnv();
		p = new GrowingThreadPool(maxInst, "org.jsmtpd.core.receive.ReceiverWorkerImpl","SSL");
		ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
		sock = ssocketFactory.createServerSocket(port);
		log.info("Listening for SSL connections on port "+port);
		this.start();
	}
	
	public void initSslEnv() {
		URL url = this.getClass().getClassLoader().getResource(ReadConfig.getInstance().getProperty("sslKeystore"));
        if (url != null) {
            String ks = url.getFile();
            System.setProperty("javax.net.ssl.keyStore", ks);
            System.setProperty("javax.net.ssl.keyStorePassword", ReadConfig.getInstance().getProperty("sslPassword"));
        } else {
            log.info("keystore file not found, SSL not available");
            throw new RuntimeException();
        }
    }
}