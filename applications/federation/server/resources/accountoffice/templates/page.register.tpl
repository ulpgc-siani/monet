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
      
      <form method="post" id="formLogin" action="::action::" accept-charset="ISO-8859-1">
        <table class="dialogs box">
          <tr>
							<td class="register dialog" style="width:50%;"> 
              <input type="hidden" name="pkcs10" value="" />

              <h1>::registerTitle::</h1>
  
              <div class="tab_container">
	              <div id="UsernameTab" class="tab_content">
	                <table border="0" width="100%">
                    <tr>
                      <td>::fullname::</td>
                      <td><input class="textbox" type="text" name="fullname" /></td>
                    </tr>
	                  <tr>
	                    <td>::user::</td>
	                    <td><input class="textbox" type="text" name="username" /></td>
	                  </tr>
	                  <tr>
	                    <td>::password::</td>
	                    <td><input class="textbox" type="password" name="password" /></td>
	                  </tr>
	                  <tr>
	                    <td>::rpassword::</td>
	                    <td><input class="textbox" type="password" name="rpassword" /></td>
	                  </tr>
	                  <tr>
	                    <td>Email</td>
	                    <td><input class="textbox" type="text" name="email" /></td>
	                  </tr>
	                  ::errorExistsUser|<tr><td>&nbsp;</td><td><div class="error login">*</div></td></tr>::
	                  ::errorCreateUser|<tr><td>&nbsp;</td><td><div class="error login">*</div></td></tr>::
	                  ::registerSave|<tr><td>&nbsp;</td><td><div class="error login">*</div></td></tr>::
	                </table>
	                <div class="buttons">
	                  <button class="button accept" type="submit">::register::</button>
	                </div>
	               </div>
              </div>
               ::certificateAuthentication::
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
  <object id="objCertEnrollClassFactory" classid="clsid:884e2049-217d-11da-b2a4-000e7bbb2b09"></object>
  </body>
</html>

@option
<option class="imagebacked"  value=::lang:: ::selected::>::langLabel::</option>

@certificateAuthentication
<ul class="tabs">
  <li><a href="\#UsernameTab">::usernamePassword::</a></li>
  ::certificate|<li><a href="\#CertificateTab">*</a></li>::
  ::openID|<li><a href="\#OpenIdTab">*</a></li>::
</ul>