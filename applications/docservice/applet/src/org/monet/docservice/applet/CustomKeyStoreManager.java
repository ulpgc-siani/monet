package org.monet.docservice.applet;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.monet.docservice.applet.keystores.MacOSKeyStore;
import org.monet.docservice.applet.keystores.MozillaKeyStore;
import org.monet.docservice.applet.keystores.MsCapiKeyStore;
import org.monet.docservice.applet.keystores.mozilla.MozillaFixed;

import es.uji.security.crypto.SupportedKeystore;
import es.uji.security.keystore.IKeyStore;
import es.uji.security.keystore.mozilla.Mozilla;
import es.uji.security.util.i18n.LabelManager;

public class CustomKeyStoreManager extends es.uji.security.keystore.KeyStoreManager {

  SupportedBrowser navigator;
  String dllPath;
  private Logger log = Logger.getLogger(CustomKeyStoreManager.class);

  public CustomKeyStoreManager(SupportedBrowser navigator) {
    super();
    this.navigator = navigator;
    prepareDllPath();
  }

  private void prepareDllPath() {
    if (!navigator.equals(SupportedBrowser.MOZILLA)) {
      this.dllPath = null;
      return;
    }
    this.dllPath = System.getenv("TEMP") + File.separator + UUID.randomUUID().toString() + File.separator;
    File dir = new File(dllPath);
    dir.mkdir();
    //TODO: Verify Mozilla Dll for x64 in Windows and copy to temp if necesary
  }

  public void deactivate() {
    if (this.dllPath == null) return;

    File pkcs11 = new File(this.dllPath + "softokn3.dll");
    if(pkcs11.exists())
      pkcs11.deleteOnExit();
  }

  private void loadFromIE() {
    log.debug("Load store from IE");

    MsCapiKeyStore mscapikeystore;
    try {
      mscapikeystore = new MsCapiKeyStore();
      mscapikeystore.load("".toCharArray());
      keystores.put(SupportedKeystore.MSCAPI, mscapikeystore);
    }
    catch(Exception e) {
      log.error("ERR_IE_KEYSTORE_LOAD", e);
    }
  }

  private void loadFromMozilla() {
    log.debug("Load store from Mozilla");

    try {
      Mozilla mozilla = new MozillaFixed(this.dllPath);

      if (mozilla.isInitialized()) {
        InputStream stream = mozilla.getPkcs11ConfigInputStream();
        String filePath = mozilla.getPkcs11FilePath();
        log.debug("Mozilla pkcs11 file path: " + filePath);
        String argsString = mozilla.getPkcs11InitArgsString();
        log.debug("Mozilla pkcs11 args: " + argsString);

        log.debug("Creating MozillaKeyStore");
        IKeyStore p11mozillaks = new MozillaKeyStore(stream, filePath, argsString);
        log.debug("Loading MozillaKeyStore");
        p11mozillaks.load(null);
        keystores.put(SupportedKeystore.MOZILLA, p11mozillaks);
      }
    } catch (Exception e) {
      log.error("ERR_MOZILLA_KEYSTORE_LOAD", e);
    }
  }

  private void loadFromSafari() {
    log.debug("Load store from Safari");

    MacOSKeyStore macOsKeyStore;
    try {
      macOsKeyStore = new MacOSKeyStore();
      macOsKeyStore.load("".toCharArray());
      //We put as MSCAPI
      keystores.put(SupportedKeystore.MSCAPI, macOsKeyStore);
    } catch (Exception e) {
      log.error(LabelManager.get("ERR_SAFARI_KEYSTORE_LOAD"), e);
    }
  }
  
  public void initKeyStoresTable() {
    if (navigator.equals(SupportedBrowser.IEXPLORER)) {
      this.loadFromIE();
    } else if (navigator.equals(SupportedBrowser.MOZILLA)) {
      this.loadFromMozilla();
    } else if(navigator.equals(SupportedBrowser.SAFARI)) {
      this.loadFromSafari();
    }
  }

}
