<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

  <title>$PAGE_TITLE</title>

  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <meta http-equiv="Cache-Control" content="no-cache"/>
  <meta http-equiv="Pragma" content="no-cache"/>

  <!-- Meta information -->
  <meta name="company" content="ULPGC Software Engeniering Group"/>
  <meta name="author" content="Jose Juan Hernández Cabrera"/>
  <meta name="author" content="Mario Caballero Ramírez"/>
  <meta name="keywords" content="html, rtf, pdf, gisc, ulpgc"/>

  <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>

  <link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/loading.css"/>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/lightbox.css" media="screen"/>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/ext-all.css"/>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/xtheme-aero.css"/>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/office.${LANGUAGE}.css"/>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/office.css"/>
  <link rel="stylesheet" type="text/css" title="base" href="${API_URL}?op=loadthemefile&path=main.css"/>

  <script type="text/javascript" src="${JAVASCRIPT_URL}/prototype.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/scriptaculous.js?load=effects"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/ext-prototype-adapter.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/ext-all.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/ext-lang-${LANGUAGE}.js"></script>
  <script type="text/javascript" src="${API_URL}?op=loadbusinessmodeldefinition"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/office.${LANGUAGE}.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/office.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/lightbox.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/geoxml3.js"></script>
  <script src="https://maps.googleapis.com/maps/api/js?v=3&libraries=visualization&key=${GOOGLE_API_KEY}"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/gmaps_oms.min.js"></script>
  <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.0.3/cookieconsent.min.css" />
  <script src="//cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.0.3/cookieconsent.min.js"></script>
  <script>
  	var CookieConsentDictionary = {
  		"es" : {
  			message : "Utilizamos cookies para registrar tu configuración y ofrecerte un mejor servicio",
  		},
  		"en" : {
  			message : "We use cookies to register your configuration and offer you a better service",
  		}
  	}

    var language = "${LANGUAGE}";
    if (CookieConsentDictionary[language] == null) language = "es";

    window.addEventListener("load", function(){
		window.cookieconsent.initialise({
			"palette": {
				"popup": {
				"background": "#edeff5",
				"text": "#838391"
				},
				"button": {
					"background": "#4b81e8"
				}
			},
			"theme": "classic",
			"content": {
				"message": CookieConsentDictionary[language].message,
				"dismiss": "Ok",
				"link": ""
			}
		})
	});
  </script>
</head>

<body>
<table id="BusinessUnitLoad" width="100%" height="100%">
  <tr>
    <td align="center">
      <div id="loadingAni">
        <table>
          <tr height="250px">
            <td width="30%" style="vertical-align:middle;">
              <img class="federation logosplash" src="${FEDERATION_LOGO_SPLASH}" title="${FEDERATION_LABEL}"/>
            </td>
            <td style="vertical-align:middle;">
              <div class="subtitle">${SUBTITLE}</div>
              <div class="title">${TITLE}</div>
            </td>
          </tr>
          <tr height="150px">
            <td width="100px">&nbsp;</td>
            <td style="vertical-align:bottom;"><img src="${MODEL_LOGO_SPLASH}" title="${TITLE}" class="model logosplash"></td>
          </tr>
          <tr height="70px">
            <td colspan="2">
              <div class="footer left">
                <img src="${MONET_LOGO_SPLASH}" title="powered by Monet">
              </div>
            </td>
          </tr>
        </table>
      </div>
    </td>
  </tr>
</table>

<div id="DataInit" style="display:none;">
  <div id="Config.BusinessUnit">${BUSINESS_UNIT}</div>
  <div id="Config.Domain">${DOMAIN}</div>
  <div id="Config.Url">${URL}</div>
  <div id="Config.Host">${URL}</div>
  <div id="Config.HostPort">${PORT}</div>
  <div id="Config.Api">${API_URL}</div>
  <div id="Config.ApiPort">${API_PORT}</div>
  <div id="Config.Push">${PUSH_API_URL}</div>
  <div id="Config.PushEnabled">${PUSH_ENABLED}</div>
  <div id="Config.AnalyticsHost">${ANALYTICS_URL}</div>
  <div id="Config.EnterpriseLoginUrl">${ENTERPRISE_LOGIN_URL}</div>
  <div id="Config.ImagesPath">${IMAGES_PATH}</div>
  <div id="Config.FederationLogoUrl">${FEDERATION_LOGO}</div>
  <div id="Config.FederationUrl">${FEDERATION_URL}</div>
  <div id="Config.FederationLabel">${FEDERATION_LABEL}</div>
  <div id="Config.SpaceLabel">${SUBTITLE}</div>
  <div id="Config.ModelLabel">${TITLE}</div>
  <div id="Config.ModelLogoSplashUrl">${MODEL_LOGO}</div>
  <div id="Config.ApplicationFmsImagesUploadUrl">${APPLICATION_FMS_URL}/?op=uploadimages&nid=::idnode::</div>
  <div id="Config.ApplicationFmsImageDownloadUrl">${APPLICATION_FMS_URL}/?op=downloadimage&nid=::idnode::&f=::id::</div>
  <div id="Config.ApplicationFmsFilesUploadUrl">${APPLICATION_FMS_URL}/?op=uploaddocuments&nid=::idnode::</div>
  <div id="Config.ApplicationFmsFileDownloadUrl">${APPLICATION_FMS_URL}/?op=downloaddocument&nid=::idnode::&f=::id::</div>
  <div id="Config.Language">${LANGUAGE}</div>
  <div id="Config.Layer.Name">OfficeApp</div>
  <div id="Config.Layer.Width">100%</div>
  <div id="Config.Layer.Height">100%</div>
  <div id="Config.EncriptData">${ENCRIPT_DATA}</div>
  <div id="Config.TestCase">${TEST_CASE}</div>
  <div id="Config.DefaultLabel">${LBL_NO_LABEL}</div>
  <div id="Pages.History">${URL}/history.html</div>
  <div id="Config.AttributePathSeparator">.</div>
  <div id="Config.DefaultLocation.Latitude">${DEFAULT_LOCATION_LATITUDE}</div>
  <div id="Config.DefaultLocation.Longitude">${DEFAULT_LOCATION_LONGITUDE}</div>
  <div id="Config.SignatureApplication.DownloadUrl">${APPLICATION_SIGNATORY_URL}/app</div>
  <div id="Config.SignatureApplication.RetrieveUrl">${APPLICATION_SIGNATORY_URL}/retrieve</div>
  <div id="Config.SignatureApplication.StorageUrl">${APPLICATION_SIGNATORY_URL}/store</div>
</div>

</body>

</html>