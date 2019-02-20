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

    <div style="margin:10px;">
      <div class="header" style="height: 65px;">
        <a href="::actionHome::" alt="::home::" style="float:left;margin:10px;"><img class="displayed" src="::logo::" alt="::home::" title="::home::"/></a>
        <div style="float:right;margin:5px 15px;">::changeLanguage::\:&nbsp;<select style="margin-right: 10px;" id="languageOps" name="language">::languageOptions::</select><a href="::urlFederation::/accounts/authorization/?action=logout&token=token">::closeSession::</a></div>
      </div>
      <div class="body">
        <div class="section units">
          <h1>::unitsLabel::</h1>
          ::units::
        </div>
      </div>
      
      <div class="formCLang">
        <div id="tokenReq" style="display: none;">::token::</div>
      </div>
    </div>

  </body>
</html>

@option
<option class="imagebacked"  value=::lang:: ::selected::>::langLabel::</option>
  
@units
<ul class="items">::units::</ul>

@units.empty
<div class="empty">::noServiceEnable::</div>

@unit
<li>
  <a href="::unitURL::"></a>
  <table width="100%" height="100%">
    <tr height="100%">
      <td width="1px"><iframe src="::unitURL::/banner.jsp?view=card" width="500" height="250"></iframe></td>
    </tr>
  </table>
</li>