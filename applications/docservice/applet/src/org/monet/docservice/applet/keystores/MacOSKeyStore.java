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
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.uji.security.crypto.SupportedKeystore;
import es.uji.security.keystore.IKeyStore;

public class MacOSKeyStore implements IKeyStore {
  private KeyStore keyStore;
  private Logger   log = Logger.getLogger(MsCapiKeyStore.class);

  @SuppressWarnings("rawtypes")
  public MacOSKeyStore() throws KeyStoreException, NoSuchProviderException {
    this.keyStore = KeyStore.getInstance("KeychainStore", "Apple");

    Provider provider = getProvider();
    this.log.debug(provider.getName() + " formally supports the following algorithms:");

    // Step over the list of supported algorithms
    Iterator iter = provider.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry) iter.next();
      this.log.debug("\t" + entry.getKey() + " = " + entry.getValue());
    }
  }

  public String getAliasFromCertificate(Certificate certificate) throws Exception {
    return this.keyStore.getCertificateAlias(certificate);
  }

  public void load(char[] pin) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {
    this.keyStore.load(null, null);
  }

  public Enumeration<String> aliases() throws KeyStoreException, Exception {
    log.debug("Loading aliases from keystore");

    return this.keyStore.aliases();
  }

  public Certificate getCertificate(String alias) throws KeyStoreException {
    log.debug("Loading certificate with alias " + alias);

    return this.keyStore.getCertificate(alias);
  }

  public Certificate[] getUserCertificates() throws KeyStoreException, Exception {
    log.debug("Loading user certificates from keystore " + getName());

    Vector<Certificate> certs = new Vector<Certificate>();
    for (Enumeration<String> e = aliases(); e.hasMoreElements();) {
      String alias = e.nextElement();

      // Ignore entries that aren't certificates
      if (!this.keyStore.isKeyEntry(alias))
        continue;

      log.debug("Found certificate whith alias " + alias);
      certs.add(getCertificate(alias));
    }

    Certificate[] res = new Certificate[certs.size()];
    certs.toArray(res);

    return res;
  }

  public Key getKey(String alias) throws KeyStoreException, Exception {
    String password = "dummy";
    log.debug("getKey with password " + password);
    return this.keyStore.getKey(alias, password.toCharArray());
  }

  public Provider getProvider() {
    return this.keyStore.getProvider();
  }

  public void setProvider(Provider provider) throws Exception {
    // Does nothing, seems non sense by this time.
    throw new Exception("Method not implemented");
  }

  public byte[] signMessage(byte[] toSign, String alias) throws NoSuchAlgorithmException, Exception {
    log.debug(String.format("signMessage(%s, %s)", toSign, alias));

    byte[] res = null;
    PrivateKey privKey = (PrivateKey) this.getKey(alias);

    if (privKey == null) {
      log.error("Private Key is NULL");
      return null;
    }

    Signature rsa = Signature.getInstance("SHA1withRSA");
    rsa.initSign(privKey);
    rsa.update(toSign);
    res = rsa.sign();

    return res;
  }

  public SupportedKeystore getName() {
    return SupportedKeystore.MSCAPI;
  }

  public String getTokenName() {
    return "MacOS Keys";
  }

  public void cleanUp() {
    this.keyStore = null;
    Runtime.getRuntime().gc();
  }
}
