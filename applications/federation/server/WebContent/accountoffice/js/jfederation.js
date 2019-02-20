function sendRequest(value){
	var keyStore = certs[value].KeyStore;
	var alias = certs[value].Alias;
	var d= new Date();
	var timeSign = d.getTime();
	var signText = "&mode=certificate&timeSign="+ timeSign;
	var sign = document.Signer.signText(signText,keyStore, alias);
	var loginForm = $("#formLogin")[0];
	
	sign = sign.replace(/\+/g,"%2B").replace(/$/g,"%24").replace(/&/g,"%26")
	.replace(/,/g,"%2C").replace(/:/g,"%3A").replace(/;/g,"%3B").replace(/=/g,"%3D").replace(/@/g,"%3F");
	
	loginForm.action += signText + "&signature=" + sign;
	loginForm.submit();
}

function GenReq()
{
	var szName = "";

	szName = "";

	if ($("input[name=email]").val() == "") {
		alert("No email Address");
		return false;
	} 
	else szName = "E=" + $("input[name=email]").val();

	if ($("input[name=name]").val() == "") {
		alert("No Common Name");
		return false;
	} 
	else szName = szName + ", CN=" + $("input[name=name]").val();

	if ($("input[name=country]").val() == "") {
		alert("No Country");
		return false;
	}
	else szName = szName + ", C=" + $("input[name=country]").val();

	if ($("input[name=province]").val() == "") {
		alert("No State or Province");
		return false;
	}
	else szName = szName + ", S=" + $("input[name=province]").val();

	if ($("input[name=city]").val() == "") {
		alert("No City");
		return false;
	}
	else szName = szName + ", L=" + $("input[name=city]").val();

	if ($("input[name=organizationName]").val() == "") {
		alert("No Organization");
		return false;
	}
	else szName = szName + ", O=" + $("input[name=organizationName]").val();

	if ($("input[name=departmentName]").val() == "") {
		alert("No Organizational Unit");
		return false;
	}
	else szName = szName + ", OU=" + $("input[name=departmentName]").val();

	return szName;
}

function CreateRequest(szName) 
{                  
  try {
    // Variables
	  var objCSP = new ActiveXObject("X509Enrollment.CCspInformation");
      var objCSPs = new ActiveXObject("X509Enrollment.CCspInformations");
      var objPrivateKey = new ActiveXObject("X509Enrollment.CX509PrivateKey");
      var objRequest = new ActiveXObject("X509Enrollment.CX509CertificateRequestPkcs10");
      var objObjectIds = new ActiveXObject("X509Enrollment.CObjectIds");
      var objObjectId = new ActiveXObject("X509Enrollment.CObjectId");
      var objX509ExtensionEnhancedKeyUsage = new ActiveXObject("X509Enrollment.CX509ExtensionEnhancedKeyUsage");
      //var objExtensionTemplate = new ActiveXObject("X509Enrollment.CX509ExtensionTemplateName");
      var objDn = new ActiveXObject("X509Enrollment.CX500DistinguishedName");
      var objEnroll = new ActiveXObject("X509Enrollment.CX509Enrollment");

    //  Initialize the csp object using the desired Cryptograhic Service Provider (CSP)
    objCSP.InitializeFromName("Microsoft Enhanced Cryptographic Provider v1.0");

    //  Add this CSP object to the CSP collection object
    objCSPs.Add(objCSP);

    //  Provide key container name, key length and key spec to the private key object
    objPrivateKey.Length = 1024;
    objPrivateKey.KeySpec = 1; // AT_KEYEXCHANGE = 1

    //  Provide the CSP collection object (in this case containing only 1 CSP object)
    //  to the private key object
    objPrivateKey.CspInformations = objCSPs;

    // Initialize P10 based on private key
    objRequest.InitializeFromPrivateKey(1, objPrivateKey, ""); // context user = 1

    // 1.3.6.1.5.5.7.3.2 Oid - Extension
    objObjectId.InitializeFromValue("1.3.6.1.5.5.7.3.2");
    objObjectIds.Add(objObjectId);
    objX509ExtensionEnhancedKeyUsage.InitializeEncode(objObjectIds);
    objRequest.X509Extensions.Add(objX509ExtensionEnhancedKeyUsage);

    // DN related stuff
    objDn.Encode(szName, 0); // XCN_CERT_NAME_STR_NONE = 0
    objRequest.Subject = objDn;

    // Enroll
    objEnroll.InitializeFromRequest(objRequest);
    var pkcs10 = objEnroll.CreateRequest(3); // XCN_CRYPT_STRING_BASE64REQUESTHEADER = 3

    return pkcs10;
    
  }
  catch (ex) {
    alert(ex);
    return false;
  }
}       


var certs;
$(document).ready(function (){

	$("#googleProvider").click(function () {
		var server = "https://www.google.com/accounts/o8/id";
		var loginForm = $("#formLogin");
		loginForm.action += "?mode=openid&server=" + server;
	});
	
	$("#yahooProvider").click(function () {
		var server = "http://me.yahoo.com";
		var loginForm = $("#formLogin");
		loginForm.action += "?mode=openid&server=" + server;
	});
	
	$("#opendIdSign").click(function (e) {
		e.preventDefault();
		var loginForm = $("#formLogin");
		loginForm.action += "?mode=openid&server=" + $("#openidinput").val();
	});

	$("#languageOps").change(function () {
		var val = $("#languageOps option:selected").attr("value");
		var actionName = "changelanguage";
		var token = $("#tokenReq").text();
		if(token == null) token = "null";
		window.location.href = "?action=" + actionName + "&oauth_token=" + token + "&language=" + val;
	});
	
	$("#certificateRegister").click(function (e) {
		var ie = (document.all && document.getElementById);
		if(!ie)
			$("#CertificateTab").append("<keygen name=\"SPKAC\" challenge=\"challengePassword\" >");
		else{
			// $("#CertificateTab").append("<object id=\"objCertEnrollClassFactory\" classid=\"clsid:884e2049-217d-11da-b2a4-000e7bbb2b09\"></object>");
			var szName = GenReq();
			var pkcs10 = CreateRequest(szName) ;
		    $("input[name=pkcs10]").val(pkcs10);
			
		}
	});
	

	$(".tab_content").hide();
	$("ul.tabs li:first").addClass("active").show();
	$(".tab_content:first").show();

	$("ul.tabs li").click(function() {
		$("ul.tabs li").removeClass("active");
		$(this).addClass("active");
		$(".tab_content").hide();

		var activeTab = $(this).find("a").attr("href");
		$(activeTab).fadeIn();
		return false;
	});		

	if(document.Signer != null){
		document.Signer.init(navigator.userAgent);
		var certsNoEval = document.Signer.getAllCertificatesAliases();

		certs = eval('(' + certsNoEval + ')');

		var regExp = new RegExp("CN=\"([^\"]*)\"|CN=([^,]*),|CN=([^$]*)$");

		var i=0;
		for (i=0;i < certs.length;i++)
		{
			if (certs[i] != null) {
				var cn = "";
				var rvalues = regExp.exec(certs[i].SubjectDN);
				if (rvalues) {
					if ((rvalues[3] != null) && (rvalues[3] != "")) {
						cn = rvalues[3];
					} else if ((rvalues[2] != null) && (rvalues[2] != "")) {
						cn = rvalues[2];
					} else {
						cn = rvalues[1];
					}				
				}
 
	  		var Alias = certs[i].Alias;
  			var aliasCNP1 =  Alias.substring(Alias.indexOf('CN=')+3);
		  	var aliasIndexComma = aliasCNP1.indexOf(',');
			  var aliasCN = aliasCNP1.substring(0,aliasIndexComma);
			  $('#certificateOps').append("<option value=\""+ i +"\" >"+cn +" "+aliasCN + "</option>");

			}


/*
			var SubjectDN = certs[i].SubjectDN;
			var subjectCNP1 =  SubjectDN.substring(SubjectDN.indexOf('CN=')+3);
			var subjectIndexComma = subjectCNP1.indexOf(',');
			var subjectCN = subjectCNP1.substring(0,subjectIndexComma);
*/

		}
		
		$("#certificateSign").click(function (e) {
			e.preventDefault();
			var val = $("#certificateOps option:selected").attr("value");
			sendRequest(val);
		});
	}
});
