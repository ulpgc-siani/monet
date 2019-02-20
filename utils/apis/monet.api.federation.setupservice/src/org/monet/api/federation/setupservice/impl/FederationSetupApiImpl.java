package org.monet.api.federation.setupservice.impl;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.api.federation.setupservice.FederationSetupApi;
import org.monet.api.federation.setupservice.impl.library.LibraryRestfull;
import org.monet.api.federation.setupservice.impl.model.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;

public class FederationSetupApiImpl implements FederationSetupApi {
    private String location;
    private String certificateFilename;
    private String certificatePassword;

    public FederationSetupApiImpl(String location, String certificateFilename, String certificatePassword) {
        this.location = location;
        this.certificateFilename = certificateFilename;
        this.certificatePassword = certificatePassword;
    }

    @Override
    public String getVersion() {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("getversion"));
            return (String) LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0.0.0";
    }

    @Override
    public Status getStatus() {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
        Status status = new Status();
        String statusValue = "";

        try {
            parameters.put("op", toStringBody("getstatus"));
            statusValue = (String) LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
            status.deserializeFromXML(statusValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public void run() {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("run"));
            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("stop"));
            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean ping() {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("ping"));
            String result = LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
            return result.indexOf("true") != -1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FederationSocketInfo getSocketInfo() {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
        FederationSocketInfo federationSocketInfo = new FederationSocketInfo();

        try {
            parameters.put("op", toStringBody("getsocketinfo"));
            String result = LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
            federationSocketInfo.deserializeFromXML(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return federationSocketInfo;
    }

    @Override
    public FederationInfo getInfo() {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
        FederationInfo federationInfo = new FederationInfo();

        try {
            parameters.put("op", toStringBody("getinfo"));
            String result = LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
            federationInfo.deserializeFromXML(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return federationInfo;
    }

    @Override
    public void putLabel(String label) {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("putlabel"));
            parameters.put("label", toStringBody(encode(label)));
            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putLogo(InputStream logo) {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("putlogo"));
            parameters.put("logo", new InputStreamBody(logo, "logo.png"));
            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerPartner(String name, String label, URI uri, Federation federation, ServiceList serviceList, FeederList feederList) {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("registerpartner"));
            parameters.put("name", toStringBody(name));
            parameters.put("label", toStringBody(encode(label)));
            parameters.put("uri", toStringBody(encode(uri.toString())));
            parameters.put("federation", toStringBody(encode(federation.serializeToXML()), ContentType.TEXT_XML));
            parameters.put("services", toStringBody(encode(serviceList.serializeToXML()), ContentType.TEXT_XML));
            parameters.put("feeders", toStringBody(encode(feederList.serializeToXML()), ContentType.TEXT_XML));
            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregisterPartner(String name, Federation federation) {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("unregisterpartner"));
            parameters.put("name", toStringBody(name));
            parameters.put("federation", toStringBody(encode(federation.serializeToXML()), ContentType.TEXT_XML));
            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerTrustedFederation(Federation trustedFederation) {
        HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();

        try {
            parameters.put("op", toStringBody("registertrustedfederation"));
            parameters.put("federation", toStringBody(encode(trustedFederation.serializeToXML()), ContentType.TEXT_XML));
            LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encode(String data) {
        try {
            if (data == null)
                return null;

            return URLEncoder.encode(data, "utf-8");
        } catch (Exception exception) {
            return data;
        }
    }

    private StringBody toStringBody(String content) {
        return toStringBody(content, ContentType.TEXT_PLAIN);
    }

    private StringBody toStringBody(String content, ContentType contentType) {
        return new StringBody(content, contentType);
    }
}
