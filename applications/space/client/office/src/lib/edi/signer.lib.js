var Templates = {
  "CERT_ITEM": "<li class=\"thumbItem\"><a href=\"#\" onClick=\"setSelected(::CERT_ID::);\">::CERT_ALIAS::</a></li>"
};

//--------------------------------------------------------------------

/*

 Tiene dos modos de funcionamiento.

 -El estándar, que funciona a base de nuestro applet "certificatesApplet", en el fichero "documentsigner.2.jar".
 En este modo, el applet es el que selecciona la lista de certificados y realiza la firma.

 -El modo platino, que funciona, además de nuestro applet, con los scripts de firma electrónica de platino que cargan otro applet.
 En este modo, el applet de platino se encarga de seleccionar la lista de certificados.


 */
function TCertificate() {
  this.Id = null;
  this.Name = null;
}

//--------------------------------------------------------------------

function TSigner(DOMApplet, language) {
  this.certListNative = null;
  this.certListPlatino = null;
  this.language = language!=null?language:"es";
  this.DOMWebSigner = DOMApplet;

  if (!this.init())
    return;

  this.loadCertList();
}

TSigner.initialized = false;

TSigner.translations = {
  "es": "Para poder realizar la firma, este sitio ha de estar dado de alta en la lista de sitios de confianza. Abra el panel de preferencias de Java e incluya este sitio en la lista.",
  "en": "This site must be included in the trusted sites. Open Java preferences panel and include this site."
}

//--------------------------------------------------------------------

TSigner.prototype.isInitialized = function() {
  return TSigner.initialized;
};

TSigner.prototype.init = function () {
  if (TSigner.initialized)
    return true;

  if (!this.DOMWebSigner.init) {
    alert(TSigner.translations[this.language]);
    return false;
  }

  this.DOMWebSigner.init(navigator.userAgent);
  TSigner.initialized = true;
  return true;
};

TSigner.prototype.loadCertList = function () {
  this.certListNative = eval("(" + this.DOMWebSigner.getAllCertificatesAliases() + ")");
};

//--------------------------------------------------------------------

TSigner.prototype.findIdCertificateByCN_Platino = function (cn) {
  for (var i = 0; i < this.certListPlatino.length; i++) {
    if (this.certListPlatino[i] != null) {
      if (this.certListPlatino[i] != "") {
        if (cn == this.certListPlatino[i][1]) return i;
      }
    }
  }

  return null;
}

//--------------------------------------------------------------------

TSigner.prototype.findIdCertificateByCN_Native = function (cn) {
  var regExp = new RegExp("CN=\"([^\"]*)\"|CN=([^,]*),|CN=([^$]*)$");

  for (var i = 0; i < this.certListNative.length; i++) {
    if (this.certListNative[i] != null) {
      var rvalues = regExp.exec(this.certListNative[i].SubjectDN);
      if (rvalues) {
        if ((rvalues[3] != null) && (rvalues[3] != "")) {
          var current_cn = rvalues[3];
        } else if ((rvalues[2] != null) && (rvalues[2] != "")) {
          var current_cn = rvalues[2];
        } else {
          var current_cn = rvalues[1];
        }
        if (current_cn == cn) return i;
      }
    }
  }

  return null
}

//--------------------------------------------------------------------

TSigner.prototype.GetCertificates = function () {
  var certificates = new Array();

  if (!this.isInitialized())
    return certificates;

  var regExp = new RegExp("CN=\"([^\"]*)\"|CN=([^,]*),|CN=([^$]*)$");

  for (var i = 0; i < this.certListNative.length; i++) {
    if (this.certListNative[i] != null) {
      var rvalues = regExp.exec(this.certListNative[i].SubjectDN);
      if (rvalues) {
        if ((rvalues[3] != null) && (rvalues[3] != "")) {
          var cn = rvalues[3];
        } else if ((rvalues[2] != null) && (rvalues[2] != "")) {
          var cn = rvalues[2];
        } else {
          var cn = rvalues[1];
        }

        var cert = new Object();
        cert.Id = i;
        cert.Name = cn;
        certificates[i] = cert;
      }

    }
  }

  return certificates;
}

//--------------------------------------------------------------------

TSigner.prototype.SignText = function (CertificateCN, Document) {
  var IdCertificate = this.findIdCertificateByCN_Native(CertificateCN);
  if (IdCertificate == null) return null;

  if ((Document == null) || (Document == "")) return null;

  var Certificate = this.certListNative[IdCertificate];
  if (Certificate == null) return null;
  return this.DOMWebSigner.signText(Document, Certificate.KeyStore, Certificate.Alias);
}

//--------------------------------------------------------------------

TSigner.prototype.SignDocument = function (CertificateCN, Hash) {
  //Esto no se hace vía platino porque no se puede generar una firma válida para incrustar en pdf

  var IdCertificate = this.findIdCertificateByCN_Native(CertificateCN);
  if (IdCertificate == null) return null;

  if ((Hash == null) || (Hash == "")) return null;

  var Certificate = this.certListNative[IdCertificate];
  if (Certificate == null) return null;
  return this.DOMWebSigner.signDocument(Hash, Certificate.KeyStore, Certificate.Alias);
}

//--------------------------------------------------------------------

TSigner.prototype.GetCertificateSerialization = function (CertificateCN) {
  //Esto no se hace vía platino porque no se puede generar una firma válida para incrustar en pdf

  var IdCertificate = this.findIdCertificateByCN_Native(CertificateCN);
  if (IdCertificate == null) return null;

  var Certificate = this.certListNative[IdCertificate];
  return this.DOMWebSigner.getCertificate(Certificate.KeyStore, Certificate.Alias);
}

//--------------------------------------------------------------------


