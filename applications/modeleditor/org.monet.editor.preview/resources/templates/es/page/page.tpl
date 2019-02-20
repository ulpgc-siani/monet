<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>Prince2</title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <!-- Meta information -->
    <meta name="company" content="ULPGC Software Engeniering Group" />
    <meta name="author" content="Jose Juan Hernández Cabrera"/>
    <meta name="author" content="Mario Caballero Ramírez"/>
    <meta name="keywords" content="html, rtf, pdf, gisc, ulpgc" />
    
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/loading.css"/>
    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/ext-all.css"/>
    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/xtheme-aero.css"/>
    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/office.es.css"/>
    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/office.css"/>
    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/theme/main.css"/>
    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/ListViewer.css"/>
    <link rel="stylesheet" type="text/css" title="base" href="::path::styles/preview.css"/>
    
    <script type="text/javascript" src="::path::javascript/prototype.js"></script>
    <script type="text/javascript" src="::path::javascript/scriptaculous.js?load=effects"></script>
    <script type="text/javascript" src="::path::javascript/ext-prototype-adapter.js"></script>
    <script type="text/javascript" src="::path::javascript/ext-all.js"></script>
    <script type="text/javascript" src="::path::javascript/ext-lang-::language::.js"></script>
    <script type="text/javascript" src="::path::javascript/listviewer.lib.js"></script>
    <script type="text/javascript" src="::path::javascript/preview.js"></script>
    
    <script type="text/javascript">
      var path = "::path::";
      var home = "::home::";
      var language = "::language::";
      
      var toolbarButtonsList = new Array();
      var viewToolbarButtonsList = new Array();
      toolbarButtonsList["ADD"] = new Array();
      toolbarButtonsList["NAVIGATION"] = new Array();
      toolbarButtonsList["COPY"] = new Array();
      toolbarButtonsList["DOWNLOAD"] = new Array();
      toolbarButtonsList["PRINT"] = new Array();
      toolbarButtonsList["EDIT"] = new Array();
      toolbarButtonsList["TOOL"] = new Array();
      toolbarButtonsList["CUSTOM"] = new Array();
      toolbarButtonsList["CONTEXT"] = new Array();
      toolbarButtonsList["OBSERVER"] = new Array();
      
      var sidebarButtonsList = new Array();
      var viewSidebarButtonsList = new Array();
      sidebarButtonsList["ADD"] = new Array();
      sidebarButtonsList["NAVIGATION"] = new Array();
      sidebarButtonsList["COPY"] = new Array();
      sidebarButtonsList["DOWNLOAD"] = new Array();
      sidebarButtonsList["PRINT"] = new Array();
      sidebarButtonsList["EDIT"] = new Array();
      sidebarButtonsList["TOOL"] = new Array();
      sidebarButtonsList["CUSTOM"] = new Array();
      sidebarButtonsList["CONTEXT"] = new Array();
      sidebarButtonsList["OBSERVER"] = new Array();
    </script>
    
  </head>
  
  <body>
  
    <div id="loading" style="background:white;position:absolute;z-index:1000;display:none;width:100%;height:100%;">
      <table width="100%" height="100%"><tr><td>
        <div id="loadingAni">
          <table>
            <tr height="250px">
              <td width="30%" style="vertical-align:middle;">
                <img class="federation logosplash" src="::organizationLogoSplash::"/>
              </td>
              <td style="vertical-align:middle;">
                <div class="subtitle">::subtitle::</div>
                <div class="title">::title::</div>
              </td>
            </tr>
            <tr height="150px">
              <td width="100px">&nbsp;</td>
              <td style="vertical-align:bottom;"><img src="::modelLogoSplash::" title="::title::" class="model logosplash"></td>
            </tr>
            <tr height="70px">
              <td colspan="2">
                <div class="footer left">
                  <img src="::monetLogoSplash::" title="powered by Monet">
                </div>
              </td>
            </tr>
          </table>
        </div>
      </td></tr></table>
    </div>
  
    <div id="header" class="x-layout-panel x-layout-panel-north" style="height: 58px; left: 0px; top: 0px; width: 99%;"><div class="x-unselectable x-layout-panel-hd x-layout-title-north" unselectable="on" style="display: none;"><span unselectable="on" class="x-unselectable x-layout-panel-hd-text">&nbsp;</span><div unselectable="on" class="x-unselectable x-layout-panel-hd-tools"><div class="x-layout-tools-button" style="display: none;"><div class="x-layout-tools-button-inner x-layout-close">&nbsp;</div></div></div></div>
      <div class="x-layout-panel-body" style="overflow: hidden; height: 58px;">
        <div class="layout header x-layout-active-content" id="LayoutHeader">
          <div class="leftbox">
            <div class="logo" id="HeaderLogo">
              <div class="dialog">
                <ul>
                  <li><a href="javascript:void(null)">ir a la federación</a></li>
                  <li><a href="javascript:showHome()">ir al inicio</a></li>
                </ul>
              </div>
              <table>
                <tr>
                  <td>
                    <a title="go to home" href="javascript:showHome()" class="command home"></a>
                    <img class="federation image" src="::organizationLogo::" title="Organización" alt="Organización">
                  </td>
                  <td style="vertical-align:middle;">
                    <a title="ir al inicio" href="javascript:showHome()" class="command home"></a>
                    <table>
                      <tr><td><div class="subtitle">::subtitle::</div></td></tr>
                      <tr><td><div class="title">::title::</div></td></tr>
                    </table>
                  </td>
                </tr>
              </table>
            </div>
          </div>
          <div class="rightbox">
            <div class="block fright" id="ViewUser">
              <span class="view user"><a class="command text" title="ir al inicio" href="javascript:showHome()">inicio</a>
              <span style="margin-left:7px;">|</span>
              <span class="text" id="Notifications"><a style="white-space:nowrap;" title="Ver las avisos" href="javascript:void(null)" id="NotificationsLabel">Avisos</a></span>
              <span style="margin-left:7px;margin-right:7px;">|</span>
              <span><a class="command" id="cmdSendSuggestion" title="Duda / Sugerencia" href="javascript:void(null)">Duda / Sugerencia</a></span>
              <span style="margin-left:7px;">|</span>
              <span class="text" id="Username">
                <a style="white-space:nowrap;" href="javascript:void(null)" id="UsernameLabel">John Smith</a><img src="::imagesPath::s.gif"></a>
                <div class="panel username active" id="UsernamePanel" style="display:none;">
                  <ul class="options">
                    <li class="section">
                      <div class="title">Escritorios</div>
                      <ul id="Environments">::environments::</ul>
                    </li>
                  </ul>
                  <a id="cmdLogout" title="cerrar sesión" href="javascript:void(null)" class="command logout">cerrar sesión</a>
                </div>                
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="center" class="x-layout-panel x-layout-panel-center" style="left: 0px; top: 60px; width: 99%; height: 93%;">
      <div class="x-layout-panel-body" style="overflow: auto; height: 100%;">
        <div class="layout main x-layout-container x-layout-nested-layout x-layout-active-content " id="LayoutMain" style="position: relative; width: 99%; height: 100%;">
          <div class="x-layout-panel x-layout-panel-east" style="visibility: visible; height: 100%; position: absolute; left: 70%; top: 0px; width: 30%;">
            <div class="x-unselectable x-layout-panel-hd x-layout-title-east" unselectable="on">
              <span unselectable="on" class="x-unselectable x-layout-panel-hd-text">Asistente</span><div unselectable="on" class="x-unselectable x-layout-panel-hd-tools"><div class="x-layout-tools-button" style="display: none;"><div class="x-layout-tools-button-inner x-layout-close">&nbsp;</div></div><div class="x-layout-tools-button"><div class="x-layout-tools-button-inner x-layout-collapse-east">&nbsp;</div></div></div></div>
              <div id="listviewerwizard"></div>
          </div>
          <div class="x-layout-panel x-layout-panel-center" style="left: 0px; top: 0px; height: 100%; width: 70%;">
            <div class=" x-tabs-body x-layout-tabs-body" style="position: relative; overflow: hidden; height: 100%;">
              <div class="page">::page::</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="footer" class="x-layout-panel x-layout-panel-south" style="height: 20px; left: 0px; top: 97.5%; width: 99%;">
      <div class="x-unselectable x-layout-panel-hd x-layout-title-south" unselectable="on" style="display: none;">
        <span unselectable="on" class="x-unselectable x-layout-panel-hd-text">&nbsp;</span>
        <div unselectable="on" class="x-unselectable x-layout-panel-hd-tools">
          <div class="x-layout-tools-button" style="display: none;">
            <div class="x-layout-tools-button-inner x-layout-close">&nbsp;</div>
          </div>
        </div>
      </div>
      <div class="x-layout-panel-body" style="overflow: hidden; height: 20px;">
        <div class="layout footer x-layout-active-content" id="LayoutFooter">
          <div class="leftbox">
            <div id="FooterLogosPanel">
              <img class="model image" src="::modelLogo::" title="::title::" alt="::title::">
            </div>
            <div class="statusbar" id="Statusbar">
              <div id="ViewNodeDetails">
                <div class="view node details">
                  <div class="message" id="ViewNodeDetailsMessage"></div>
                </div>
              </div>
            </div>
          </div>
        <div class="rightbox powered">powered by<span>Monet</span></div>
      </div>
    </div></div>
  </body>
</html>

@environment
<li id="::name::"><a title="iniciar sesión en ::label::" href="\#" onclick="javascript:showEnvironment('::name::');" class="command">::label::<span class="current" style="display:none;">&nbsp;(actual)</span></li>
