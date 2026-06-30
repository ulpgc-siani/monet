<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <title>::organization::</title>
    <link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>
    <link href="::urlFederation::/accountoffice/css/styles.css" type="text/css" rel="stylesheet"/>
  </head>

  <body>
    <div class="layout">
      <div>
        <a href="::actionHome::" alt="::home::" style="float:left; margin:10px 0;"><img class="displayed" src="::logo::" alt="::home::" title="::home::"/></a>
      </div>

      <form method="post" action="::action::">
        <table class="dialogs box">
          <tr>
            <td class="login dialog" style="width:70%;">
              <h1 style="margin:0 0 10px;padding:0">::totpTitle::</h1>
              ::loginMessage::
              <br/>
              <p>::totpModeMessage::</p>
              ::totpSetupBlock::
              <table border="0" width="100%">
                <tr>
                  <td width="140">::totpCode::</td>
                  <td><input class="textbox" type="text" name="totpCode" /></td>
                </tr>
              </table>
              <div class="buttons">
                <button class="button login" type="submit">::signin::</button>
              </div>
            </td>
          </tr>
          <tr>
            <td><div class="powered">::poweredBy::</div></td>
          </tr>
        </table>
      </form>

      <div class="formCLang">
        <div id="tokenReq" style="display: none;">::token::</div>
        <select id="languageOps" name="language">::languageOptions::</select>
      </div>

      <span class="copyright">::copyright::</span>
    </div>
  </body>
</html>

@option
<option class="imagebacked"  value=::lang:: ::selected::>::langLabel::</option>

@totpSetupBlock
<table border="0" width="100%">
  <tr>
    <td width="140" valign="top">QR</td>
    <td><img src="::totpQrUrl::" alt="QR Google Authenticator" style="display:block; width:180px; height:180px; border:1px solid #d8d8d8; padding:8px; background:#fff;"/></td>
  </tr>
  <tr>
    <td width="140">::totpSecret::</td>
    <td><strong>::secret::</strong></td>
  </tr>
</table>
