<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv='cache-control' content='no-cache'/>
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
      
      <form  method="post" id="formLogin" action="::action::">
        <table class="dialogs box">
          <tr>
							<td class="register dialog" style="width:50%;"> 
              
              <h1>::changePasswordTitle::</h1>
  
              <div class="tab_container">
	              <div id="UsernameTab" class="tab_content">
	                <table border="0" width="100%">
	                  <tr>
	                    <td>::user::</td>
	                    <td><input class="textbox" type="text" name="username" /></td>
	                  </tr>
	                  <tr>
                      <td>::oldPassword::</td>
                      <td><input id="loginPass" class="textbox" type="password" name="oldPassword" /></td>
                    </tr>
                    <tr>
                      <td>::newPassword::</td>
                      <td><input id="loginPass" class="textbox" type="password" name="newPassword" /></td>
                    </tr>
	                  ::changePasswordResult::
	                </table>
	                <div class="buttons">
	                  ::changePasswordResultActions::
	                  <button class="button accept" type="submit">::changePasswordSend::</button>
	                </div>
	               </div>
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
  <object id="objCertEnrollClassFactory" classid="clsid:884e2049-217d-11da-b2a4-000e7bbb2b09"></object>
  </body>
</html>

@option
<option class="imagebacked"  value=::lang:: ::selected::>::langLabel::</option>

@changePasswordResult.success
<tr><td>&nbsp;</td><td><div class="success login">::result::</div></td></tr>

@changePasswordResult.success$actions
<a class="button home" href="::actionHome::" style="margin-left:8px">::home::</a>

@changePasswordResult.failure
<tr><td>&nbsp;</td><td><div class="error login">::result::</div></td></tr>