<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Pragma" content="no-cache"/>
  	<title>::organization::</title>
	<link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>
  	<link href="::urlFederation::/accountoffice/css/styles.css" type="text/css" rel="stylesheet"/>
  	<script language="javascript" type="text/javascript" src="::urlFederation::/accountoffice/js/jquery.mjs"></script>
  	<script language="javascript" type="text/javascript" src="::urlFederation::/accountoffice/js/jfederation.js"></script>
  </head>

  <body>

  	<div id="urlFdr" style="display: none;">::urlFederation::</div>

    <div class="layout">
      <div>
        <a href="::actionHome::" alt="::home::" style="float:left; margin:10px 0;"><img class="displayed" src="::logo::" alt="::home::" title="::home::"/></a>
      </div>

  	  <form method="post" id="formLogin" action="::action::">
  	    <table class="dialogs box">
  	      <tr>
      	    <td class="login dialog" style="width:70%;">
              <h1>::loginTitle::</h1>

              <div class="tab_container">
                <div id="UsernameTab" class="tab_content">
                  <table border="0" width="100%">
                    <tr>
                      <td width="120">::user::</td>
                      <td><input id="loginUser" class="textbox" type="text" name="username" /></td>
                    </tr>
                    <tr>
                      <td width="120">::password::</td>
                      <td><input id="loginPass" class="textbox" type="password" name="password" /></td>
                    </tr>
                    <tr>
                      <td width="120">&nbsp;</td>
                      <td><div><input id="rememberme" type="checkbox" name="rememberme" class="rememberme" value="true"><label for="rememberme">::rememberme::</label></div></td>
                    </tr>
                    ::userNotFound|<tr><td>&nbsp;</td><td><div class="error login">*</div></td></tr>::
                    ::retries::
                    ::showCaptcha::
                  </table>
                  <div class="buttons">
                    <button id="loginSign" class="button login" type="submit">::signin::</button>
                  </div>
                </div>
                <div id="CertificateTab" class="tab_content">
                  <select name="certificateOps" id="certificateOps" class="certificate" multiple="multiple"></select>
                  <div class="buttons">
                    <button id="certificateSign" class="button login" type="submit">::signin::</button>
                  </div>
                </div>
                <div id="OpenIdTab" class="tab_content">
                  <div>
	                  <ul>
										  <li><img id="googleProvider" src="::googleLogo::" alt="GoogleLogo" /></li>
										  <li><img id="yahooProvider" src="::yahooLogo::" alt="YahooLogo" /></li>
										</ul>
                  </div>
                  ::otherProvider::
                  <input id="openidinput" class="textbox" type="text" name="opendid" />
                  <div class="buttons">
                    <input type="submit" id="opendIdSign" class="button login">::signin::</input>
                  </div>
                </div>
              </div>

              ::certificateAuthentication::
            </td>
						<td class="dialog" style="border-left:1px dotted \#99BBE8;width:221px;">
						  ::register::
						  <div class="reset">
						    <a class="button block" href="::actionResetPassword::">::resetPassword::</a>
						  </div>
						  <div class="reset">
						    <a class="button block" href="::actionChangePassword::">::changePassword::</a>
						  </div>
						</td>
          </tr>
          <tr>
            <td colspan="2"><div class="powered">::poweredBy::</div></td>
          </tr>
        </table>

  	  </form>

      <div class="formCLang">
        <div id="tokenReq" style="display: none;">::token::</div>
          <select id="languageOps" name="language">::languageOptions::</select>
      </div>

    	<span class="copyright">::copyright::</span><!--&nbsp;-&nbsp;<a href="about.html">::about::</a>&nbsp;|&nbsp;<a href="privacy.html">::privacy::</a>-->
    </div>

  	::applet::
  </body>
</html>

@option
<option class="imagebacked"  value=::lang:: ::selected::>::langLabel::</option>

@retries
<tr>
    <td>&nbsp;</td>
    <td>
        <div class="retries">
            ::count:: ::retries::. ::suspended::
        </div>
    </td>
</tr>

@retries$suspended
::retriesSuspended:: ::time:: ::seconds::.

@showCaptcha
<tr>
  <td>&nbsp;</td>
  <td>
    <div class="captcha">
      <img class="displayed" src=::captcha:: alt="Captcha" />
      <input class="textbox" type="text" name="answerCaptcha" />
    </div>
  </td>
</tr>

@register
<div class="register">
	<div class="message">::registerMessage:: ::organization::</div>
	<a class="button block" href="::actionRegister::">::register::</a>
</div>

@applet
<applet id="idApplet" name="Signer" code="org.monet.docservice.applet.Signer" codebase="::urlFederation::/accountoffice/applet/documentsigner.4.jar" archive="::urlFederation::/accountoffice/applet/documentsigner.4.jar" width="1" height="1"/>

@certificate
<button id="useCertificate" type="submit">::useCertificate::</button>

@certificateAuthentication
<ul class="tabs">
  <li><a href="\#UsernameTab">::usernamePassword::</a></li>
  ::certificate|<li><a href="\#CertificateTab">*</a></li>::
  ::openID|<li><a href="\#OpenIdTab">*</a></li>::
</ul>

@userPass
<button id="useUserPass type="submit">::useUserPass::</button>
