package org.monet.docservice.applet.keystores;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

import es.uji.security.crypto.SupportedKeystore;
import es.uji.security.keystore.pkcs11.PKCS11HelperException;
import es.uji.security.keystore.pkcs11.PKCS11KeyStore;

public class MozillaKeyStore extends PKCS11KeyStore {

  public MozillaKeyStore(String arg0) throws PKCS11HelperException {
    super(arg0);
  }

  public MozillaKeyStore(InputStream arg0, String arg1, String arg2) throws PKCS11HelperException {
    super(arg0, arg1, arg2);
  }

  public MozillaKeyStore(InputStream arg0, String arg1, boolean arg2) throws PKCS11HelperException {
    super(arg0, arg1, arg2);
  }

  public MozillaKeyStore(InputStream arg0, String arg1) throws PKCS11HelperException {
    super(arg0, arg1);
  }

  public SupportedKeystore getName() {
    return SupportedKeystore.MOZILLA;
  }
  
  public byte[] signMessage(byte[] toSign, String alias) throws Exception {
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
}
