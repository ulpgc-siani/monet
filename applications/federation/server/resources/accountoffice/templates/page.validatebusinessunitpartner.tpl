<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8;"/>
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
      
      <form  method="post" id="formValidate" action="::action::">
        <table class="dialogs box">
          <tr>
              <td class="validation dialog" style="width:50%;"> 
                <h1>::businessUnitPartnerRequest::</h1>
                <div class="tab_container">
                  <div id="VerificationTab" class="tab_content">
                    <div>::businessUnitPartnerRequestSubtitle::</div>
                    ::content::
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
        <select id="languageOps" name="language">::languageOptions::</select>
      </div>
      
      <span class="copyright">::copyright::</span>
    </div>
  </body>
</html>

@content
<table border="0" width="100%">
  <tr>
    <td>::validationCodeTitle::</td>
    <td><input class="textbox" type="text" name="validation_code" /></td>
  </tr>
</table>
<div class="buttons">
  <button class="button accept" type="submit">::send::</button>
</div>

@content.success
<table border="0" width="100%">
  <tr>
    <td><div class="success validation">::result::</div></td>
  </tr>
</table>

@content.failure
<div class="error validation">::result::</div>
<table border="0" width="100%">
  <tr>
    <td>::validationCodeTitle::</td>
    <td><input class="textbox" type="text" name="validation_code" /></td>
  </tr>
</table>
<div class="buttons">
  <button class="button accept" type="submit">::send::</button>
</div>

@option
<option class="imagebacked"  value=::lang:: ::selected::>::langLabel::</option>