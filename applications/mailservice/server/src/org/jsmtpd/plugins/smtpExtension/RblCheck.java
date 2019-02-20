package org.jsmtpd.plugins.smtpExtension;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.InputSizeToBig;
import org.jsmtpd.core.common.smtpExtension.IProtocolHandler;
import org.jsmtpd.core.common.smtpExtension.ISmtpExtension;
import org.jsmtpd.core.common.smtpExtension.SmtpExtensionException;
import org.jsmtpd.plugins.AbstractRblPlugin;

public class RblCheck extends AbstractRblPlugin implements ISmtpExtension {
	protected Log log = LogFactory.getLog(RblCheck.class);
	
	public String getWelcome() {
		return null;
	}

	public boolean smtpPreTrigger(String command, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, BareLFException {
		if ("data".equalsIgnoreCase(command)) {
			if (!protocol.isRelayed()) {
				boolean rbl = super.checkHost(protocol.getSock().getInetAddress());
				if (!rbl) {
					BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(protocol.getSock().getOutputStream()));
					wr.write("550 Your host is present on RBL lists, I won't take your mail.\r\n");
					wr.write("221 Closing channel. Good Bye " + protocol.getSock().getInetAddress().toString() + "\r\n");
					wr.flush();
					protocol.getSock().close();
					throw new SmtpExtensionException ();
				}
			}
		}
		return false;
	}

	public boolean smtpTrigger(String command, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, BareLFException {
		return false;
	}

	public String getPluginName() {
		return "RBL check on DATA";
	}

	public void shutdownPlugin() {
	}
}
