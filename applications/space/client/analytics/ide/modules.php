<?php

  define("PATH", "path");
  define("FILES", "files");
  define("_PROTECTED", "protected");
  define("SINGLE", "");
  define("TAG_LANGUAGE", "<language>");

  $ListLanguages["es"] = "spanish";
  $ListLanguages["en"] = "english";

  $Main[PATH] = "../src/";
  $Main[FILES] = array(
    "constants.js" => SINGLE,
    "application.js" => array("state.js", "htmlutil.lib.js", "graphutil.lib.js", "common.lib.js", "date.lib.js", "exception.lib.js", "jquery.extension.lib.js", "key.lib.js", "commandlistener.js", "commanddispatcher.js", "kernel.js", "desktop.js", "translations.js")
  );

  //---------------------------------------------------------------
  $Control[PATH] = "../src/control/";
  $Control[FILES] = array(
    "process.js" => SINGLE,
    
    "dashboard.process.js" => array("viewdashboardinfo.js", "viewindicators.js", "viewmeasureunits.js", "viewtaxonomies.js", "viewchartfactory.js"),
    "account.process.js" => SINGLE,
    
    "action.js" => array("commandfactory.js", "process.js"),
    "init.action.js" => array("action.js", "viewaccount.js"),
    "account.action.js" => array("action.js", "account.process.js", "serializer.js"),
    "dashboardlist.action.js" => array("action.js"),
    "dashboard.action.js" => array("action.js", "dashboard.process.js", "viewdashboard.js"),
    "indicator.action.js" => array("action.js"),
    
    "commandfactory.js" => SINGLE,
    "commanddispatcher.js" => array("commandinfo.lib.js"),
    "commandlistener.js" => array("commanddispatcher.js", "init.action.js", "account.action.js", "dashboardlist.action.js", "dashboard.action.js", "indicator.action.js")
  );
  
  //---------------------------------------------------------------
  $CoreModel[PATH] = "../src/core/model/";
  $CoreModel[FILES] = array(
    "state.js" => array("indicatorlist.js", "categorylist.js", "rangelist.js"),
    "serializer.js" => SINGLE,
    "list.js" => SINGLE,
    "indicator.js" => SINGLE,
    "indicatorlist.js" => array("list.js", "indicator.js"),
    "range.js" => SINGLE,
    "rangelist.js" => array("list.js", "range.js"),
    "category.js" => SINGLE,
    "categorylist.js" => array("list.js", "category.js"),
    "scale.js" => SINGLE,
    "resolution.js" => SINGLE,
    "timelapse.js" => SINGLE,
    "map.js" => SINGLE
  );

  //---------------------------------------------------------------
  $CoreKernel[PATH] = "../src/core/";
  $CoreKernel[FILES] = array(
    "stub.js" => SINGLE,
    "stubajax.js" => array("stub.js"),
    "kernel.js" => array("stubajax.js", "base64.lib.js"),
    "datalink.js" => array("request.lib.js"),
    "categoriesprovider.js" => SINGLE
  );

  //---------------------------------------------------------------
  $Interface[PATH] = "../src/interface/";
  $Interface[FILES] = array(
    "dialog.js" => SINGLE,
    "view.js" => SINGLE,
    
    "viewaccount.js" => array("viewaccount.html", "view.js"),
    "viewdashboard.js" => array("viewdashboard.html", "view.js"),
    "viewdashboardinfo.js" => array("viewdashboardinfo.html", "view.js"),
    "viewindicators.js" => array("viewindicators.html", "view.js"),
    "viewmeasureunits.js" => array("viewmeasureunits.html", "view.js"),
    "viewtaxonomies.js" => array("viewtaxonomies.html", "view.js", "hierarchymultipleselector.lib.js", "categoriesprovider.js"),
    "viewchart.js" => array("view.js", "viewchart.html", "timelapse.js", "dialogscale.js", "dialogzoom.js"),
    "viewchartfactory.js" => array("viewlinechart.js", "viewbarchart.js", "viewtablechart.js", "viewmapchart.js", "viewbubblechart.js"),
    "viewlinechart.js" => array("viewchart.js", "viewlinechart.html", "dialogtimelapse.js", "dialogrange.js"),
    "viewbarchart.js" => array("viewchart.js", "viewbarchart.html", "dialogtimelapse.js", "dialogrange.js"),
    "viewtablechart.js" => array("viewchart.js", "viewtablechart.html", "dialogtimelapse.js", "dialogrange.js"),
    "viewmapchart.js" => array("viewchart.js", "viewmapchart.html", "dialogtimelapse.js", "dialogrange.js", "dialogmaplayer.js"),
    "viewbubblechart.js" => array("viewchart.js", "viewbubblechart.html", "dialogtimelapse.js", "dialogrange.js"),
    
    "dialogtimelapse.js" => array("dialogtimelapse.html", "dialog.js", "timelapse.js"),
    "dialogrange.js" => array("dialogrange.html", "dialog.js", "timelapse.js"),
    "dialogscale.js" => array("dialogscale.html", "dialog.js", "scale.js", "resolution.js"),
    "dialogzoom.js" => array("dialogzoom.html", "dialog.js"),
    "dialogmaplayer.js" => array("dialogmaplayer.html", "dialog.js", "map.js"),
    "desktop.js" => array("desktop.html")
  );

  //---------------------------------------------------------------
  $Library[PATH] = "../src/lib/";
  $Library[FILES] = array(
    "htmlutil.lib.js" => SINGLE,
    "tabcontrol.lib.js" => array("tabcontrol.lib.css"),
    "graphutil.lib.js" => SINGLE,
    "jquery.extension.lib.js" => SINGLE,
    "common.lib.js" => array("htmlutil.lib.js"),
    "exception.lib.js" => SINGLE,
    "commandinfo.lib.js" => SINGLE,
    "functioninfo.lib.js" => SINGLE,
    "base64.lib.js" => SINGLE,
    "hierarchyselector.lib.js" => SINGLE,
    "navigationselector.lib.js" => SINGLE,
    "hierarchymultipleselector.lib.js" => SINGLE,
    "treeselector.lib.js" => SINGLE,
    "widgetviewer.lib.js" => array("widgetviewer.lib.css"),
    "key.lib.js" => SINGLE,
    "date.lib.js" => SINGLE
  );

  //---------------------------------------------------------------
  $ResourcesLang[PATH] = "../src/_resources/<language>/";
  $ResourcesLang[FILES] = array(
    "translations.js" => SINGLE,
  );

  //---------------------------------------------------------------
  $ResourcesStyles[PATH] = "../src/_resources/styles/";
  $ResourcesStyles[FILES] = array(
    "view.css" => SINGLE, 
    "viewaccount.css" => array("view.css"),
    "viewdashboard.css" => array("view.css"),
    "viewdashboardinfo.css" => array("view.css"),
    "viewindicators.css" => array("view.css"),
    "viewmeasureunits.css" => array("view.css"),
    "viewtaxonomies.css" => array("view.css"),
    "dialogtimelapse.css" => array("view.css"),
    "dialogrange.css" => array("view.css"),
    "dialogscale.css" => array("view.css"),
    "dialogmaplayer.css" => array("view.css"),
    "dialogzoom.css" => array("view.css"),
    "viewchart.css" => array("view.css"),
    "viewlinechart.css" => array("view.css"),
    "viewbarchart.css" => array("view.css"),
    "viewtablechart.css" => array("view.css"),
    "viewmapchart.css" => array("view.css"),
    "viewbubblechart.css" => array("view.css"),
    
    "desktop.css" => SINGLE,
    "desktop.dialogs.css" => SINGLE,
    "desktop.fonts.css" => SINGLE,
    "desktop.input.css" => SINGLE,
    "desktop.reset.css" => SINGLE,
    
    "tabcontrol.lib.css" => SINGLE,
    "widgetviewer.lib.css" => SINGLE
  );

  //---------------------------------------------------------------
  $ResourcesTemplates[PATH] = "../src/_resources/";
  $ResourcesTemplates[FILES] = array(
    "desktop.html" => array("desktop.reset.css", "desktop.dialogs.css", "desktop.fonts.css", "desktop.input.css", "desktop.css"),
    "viewaccount.html" => array("viewaccountenvironment.html", "viewaccountdashboard.html", "viewaccountunit.html", "viewaccount.css"),
    "viewaccountenvironment.html" => SINGLE,
    "viewaccountdashboard.html" => SINGLE,
    "viewaccountunit.html" => SINGLE,
    "viewdashboard.html" => array("viewdashboardcontent.html", "viewdashboard.css"),
    "viewdashboardcontent.html" => SINGLE,
    "viewdashboardinfo.html" => array("viewdashboardinfofilter.html", "viewdashboardinfo.css"),
    "viewdashboardinfofilter.html" => SINGLE,
    "viewindicators.html" => array("viewindicatorsfolder.html", "viewindicatorsitem.html", "viewindicators.css"),
    "viewindicatorsfolder.html" => SINGLE,
    "viewindicatorsitem.html" => SINGLE,
    "viewmeasureunits.html" => array("viewmeasureunitsitem.html", "viewmeasureunits.css"),
    "viewmeasureunitsitem.html" => SINGLE,
    "viewtaxonomies.html" => array("viewtaxonomiesfilter.html", "viewtaxonomies.css"),
    "viewtaxonomiesfilter.html" => SINGLE,
    "viewfilters.html" => array("viewfiltersdimension.html","viewfiltersfiltergroup.html","viewfilters.css"),
    "viewfiltersdimension.html" => SINGLE,
    "viewfiltersfiltergroup.html" => SINGLE,
    "dialogtimelapse.html" => array("dialogtimelapse.css"),
    "dialogrange.html" => array("dialogrange.css"),
    "dialogscale.html" => array("dialogscale.css"),
    "dialogmaplayer.html" => array("dialogmaplayer.css"),
    "dialogzoom.html" => array("dialogzoom.css"),
    "viewchart.html" => array("viewchart.css", "viewchartmessage.html", "viewchartmessagemultiple.html"),
    "viewchartmessage.html" => SINGLE,
    "viewchartmessagemultiple.html" => SINGLE,
    "viewlinechart.html" => array("viewchart.css", "viewlinechart.css"),
    "viewbarchart.html" => array("viewchart.css", "viewbarchart.css"),
    "viewtablechart.html" => array("viewchart.css", "viewtablechart.css"),
    "viewmapchart.html" => array("viewchart.css", "viewmapchart.css"),
    "viewbubblechart.html" => array("viewchart.css", "viewbubblechart.css")
  );

?>