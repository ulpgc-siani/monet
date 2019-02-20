package org.monet.docservice.applet.keystores;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.uji.security.crypto.SupportedKeystore;
import es.uji.security.keystore.IKeyStore;

public class MsCapiKeyStore implements IKeyStore {
    private KeyStore keyStore;
    private Logger log = Logger.getLogger(MsCapiKeyStore.class);

    public MsCapiKeyStore() throws KeyStoreException, NoSuchProviderException {
      this.keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
    }

    public void load(char[] pin) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {
      this.keyStore.load(null, null);
    }

    public Enumeration<String> aliases() throws Exception {
        log.debug("Loading aliases from keystore");
        return this.keyStore.aliases();
    }

    public String getAliasFromCertificate(Certificate certificate) throws Exception {
        log.debug("Loading certificate alias from certificate key " + certificate.getPublicKey());
        return this.keyStore.getCertificateAlias(certificate);
    }

    public Certificate getCertificate(String alias) throws KeyStoreException {
        log.debug("Loading certificate with alias " + alias);
        return this.keyStore.getCertificate(alias);
    }

    public Key getKey(String alias) throws Exception {
        log.debug("Get Key certificate from alias " + alias);
        return this.keyStore.getKey(alias, "".toCharArray());
    }

    public Certificate[] getUserCertificates() throws Exception {
        log.debug("Loading user certificates from keystore " + getName());
                
        Vector<Certificate> certs = new Vector<Certificate>();
        for (Enumeration<String> e = aliases(); e.hasMoreElements();) {
            String alias = (String) e.nextElement();
            
            try {
	            if(this.getKey(alias) != null) {
		            log.debug("Found certificate whith alias " + alias);
		            certs.add(getCertificate(alias));
	            }
            } catch(Exception ex) {
            	log.error(ex);
            }
        }

        Certificate[] res = new Certificate[certs.size()];
        certs.toArray(res);

        return res;
    }

    public byte[] signMessage(byte[] toSign, String alias) throws Exception {
        log.debug("Signing message with certificate with alias " + alias);

        byte[] res = null;
        PrivateKey privKey = (PrivateKey) this.getKey(alias);

        if (privKey == null)
        return null;

        Signature rsa = Signature.getInstance("SHA1withRSA", getProvider());
        rsa.initSign(privKey);
        rsa.update(toSign);
        res = rsa.sign();

        return res;
}

    public SupportedKeystore getName() {
        return SupportedKeystore.MSCAPI;
    }

    public String getTokenName() {
        return "Windows Capi";
    }

    public Provider getProvider() {
        log.debug("Get Provider");
        return this.keyStore.getProvider();
    }

    public void setProvider(Provider provider) throws Exception {
        throw new Exception("Method not implemented");
    }

    public void cleanUp() {
        this.keyStore = null;
        Runtime.getRuntime().gc();
    }
}