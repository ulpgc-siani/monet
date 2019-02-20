package org.jsmtpd.config;

/**
 * Generic Exception class for loading plugins.
 */
public class PluginLoaderException extends Exception {

    public PluginLoaderException() {
        super();
    }

    public PluginLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoaderException(String message) {
        super(message);
    }

    public PluginLoaderException(Throwable cause) {
        super(cause);
    }

}
