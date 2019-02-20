package org.jsmtpd.plugins.inputIPFilters;

import java.net.InetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.inputIPFilter.IFilterIP;

/**
 * For testing core only
 * Do not use it !
 * This will block the current thread during 1 minute, then drop the connection
 */
public class SlowFilter implements IFilterIP {
    private Log log = LogFactory.getLog(SlowFilter.class);
    public boolean checkIP(InetAddress input) {
        log.debug(Thread.currentThread().getName()+" sleeping for 1 min");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            log.error(e);
        }
        log.debug(Thread.currentThread().getName()+" resumed");
        return false;
    }

    public String getPluginName() {
        return "Thread test plugin";
    }

    public void initPlugin() throws PluginInitException {
    }

    public void shutdownPlugin() {
    }

}
