package client.core.system.types;

public class Uri implements client.core.model.types.Uri {
    private String value;

	private static final int DEFAULT_PORT = 80;

    public Uri() {
    }

    public Uri(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public final String getProtocol() {
        String value = getValue();
        if (!value.contains("://")) return null;
        return value.substring(0, value.indexOf("://"));
    }

    @Override
    public final String getHost() {

        if (getProtocol() == null)
            return null;

        String uriWithoutProtocol = getUriWithoutProtocol();
        if (endOfHost(uriWithoutProtocol) > 0)
            return uriWithoutProtocol.substring(0, endOfHost(uriWithoutProtocol));

        return uriWithoutProtocol;
    }

    @Override
    public final int getPort() {
        if (getProtocol() == null)
            return DEFAULT_PORT;

        String uriWithoutProtocol = getUriWithoutProtocol();
        if (!uriWithoutProtocol.contains(":"))
            return DEFAULT_PORT;

        if (uriWithoutProtocol.contains("/"))
            return Integer.valueOf(uriWithoutProtocol.substring(uriWithoutProtocol.indexOf(":") + 1, uriWithoutProtocol.indexOf("/")));

        return Integer.valueOf(uriWithoutProtocol.substring(uriWithoutProtocol.indexOf(":") + 1));
    }

    @Override
    public final String getPath() {
        if (getProtocol() == null)
            return "";

        String uriWithoutProtocol = getUriWithoutProtocol();
        if (!uriWithoutProtocol.contains("/"))
            return "";

        return uriWithoutProtocol.substring(uriWithoutProtocol.indexOf("/") + 1);
    }

    @Override
    public final String toString() {

        String protocol = getProtocol();
        if (protocol == null)
            return "";

        String host = getHost();
        if (host == null)
            return "";

        int port = getPort();
        String path = getPath();

        return protocol + "://" + host + (port != DEFAULT_PORT?":" + port:"") + ((path != null && !path.isEmpty())?"/" + path:"");
    }

    private String getUriWithoutProtocol() {
        String protocol = getProtocol();
        String value = getValue();
        return value.substring(value.indexOf(protocol) + (protocol + "://").length());
    }

    private int endOfHost(String uriWithoutProtocol) {
        if (uriWithoutProtocol.contains(":"))
            return uriWithoutProtocol.indexOf(":");
        return uriWithoutProtocol.indexOf("/");
    }

}
