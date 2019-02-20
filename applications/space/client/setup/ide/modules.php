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
    "application.js" => array("constants.js", "buzz.lib.js", "inputfiles.lib.js", "common.lib.js", "common.lang.js", "exception.lib.js", "commandlistener.js", "commanddispatcher.js", "behaviourdispatcher.js", "kernel.js", "desktop.js", "updatespacewizard.dialog.js", "util.js", "imageviewer.js", "popup.js")
  );

  //---------------------------------------------------------------
  $Control[PATH] = "../src/control/";
  $Control[FILES] = array(
    "process.js" => SINGLE,
    "action.js" => array("commandfactory.js", "process.js"),
    "init.action.js" => array("action.js"),    
    "commandfactory.js" => SINGLE,
    "commanddispatcher.js" => SINGLE,
    "commandlistener.js" => array("commanddispatcher.js", "init.action.js", "space.action.js", "model.action.js"),
	"space.action.js" => array("action.js", "space.serializer.js"),
    "model.action.js" => array("action.js", "model.serializer.js", "message.dialog.js"),
    "behaviourdispatcher.js" => SINGLE
  );
  
  //---------------------------------------------------------------
  $CoreModel[PATH] = "../src/core/model/";
  $CoreModel[FILES] = array(
    "basemodel.js" => SINGLE,
    "businessmodel.js" => SINGLE,
    "businessspace.js" => SINGLE,
    "thesaurus.js" => array("client.js"),
    "service.js" => array("client.js"),    
    "client.js" => SINGLE,
    "map.js" => array("client.js"),     
    "collection.js" => SINGLE,
    "provider.js" => SINGLE,
    "providerdefinition.js" => array("providerdefinitiontypes.js"),
    "providerdefinitiontypes.js" => SINGLE
  );
  
  //---------------------------------------------------------------
  $CoreIterators[PATH] = "../src/core/iterators/";
  $CoreIterators[FILES] = array(
  );  
  
  //---------------------------------------------------------------
  $CoreSerializers[PATH] = "../src/core/serializer/";
  $CoreSerializers[FILES] = array(          
     "service.list.serializer.js" => array("service.js"),     
     "map.list.serializer.js" => array("map.js"),
     "space.serializer.js" => array("businessspace.js"),
     "model.serializer.js" => array("businessmodel.js"),
     "provider.serializer.js" => array("provider.js")     
  );  

  //---------------------------------------------------------------
  $CoreExceptions[PATH] = "../src/core/exceptions/";
  $CoreExceptions[FILES] = array(     
     "businessspace.exception.js" => SINGLE
  );  

  //---------------------------------------------------------------
  $CoreProducer[PATH] = "../src/core/producer/";
  $CoreProducer[FILES] = array(
  );

  //---------------------------------------------------------------
  $CoreKernel[PATH] = "../src/core/kernel/";
  $CoreKernel[FILES] = array(
    "kernel.js" => SINGLE,
    "kernel-stub.js" => SINGLE
  );

  //---------------------------------------------------------------
  $Interface[PATH] = "../src/interface/";
  $Interface[FILES] = array(
    "desktop.js" => array("desktop.lang.js", "toolbar.js", "top.layout.js", "bottom.layout.js", "Desktop.html", "space.tab.js", "thesaurus.tab.js", "providers.tab.js", "console.tab.js")
  );

  //---------------------------------------------------------------
  $InterfaceAdapter[PATH] = "../src/interface/adapter/";
  $InterfaceAdapter[FILES] = array(
    "items.adapter.js" => SINGLE,
    "providerdefinitiontreenode.adapter.js" => SINGLE 
  );

  //---------------------------------------------------------------
  $InterfaceComponent[PATH] = "../src/interface/component/";
  $InterfaceComponent[FILES] = array(   
    "listcomponent.js" => SINGLE,
    "providers.tree.js" => array("providerstree.lang.js", "providerdefinition.js", "providerdefinitiontreenode.adapter.js"),
    "providers.grid.js" => array("providersgrid.lang.js", "provider.js"),
    "wizard.js" => SINGLE,
    "menu.js" => array("Menu.html"),    
    "toolbar.js" => array("menu.js", "Toolbar.html", "toolbar.lang.js"),
    "imageviewer.js" => array("ImageViewer.html", "imageviewer.lang.js"),
    "popup.js" => array("Popup.html")   
  );

  //---------------------------------------------------------------
  $InterfaceDialog[PATH] = "../src/interface/dialog/";
  $InterfaceDialog[FILES] = array(
    "dialog.js" => array("dialog.lang.js"),
    "updatebusinessmodel.dialog.js" => array("dialog.js", "dialogupdatebusinessmodel.lang.js", "DialogUpdateBusinessModel.html", "model.action.js"),
    "updatebusinessspace.dialog.js" => array("dialog.js", "dialogupdatebusinessspace.lang.js", "DialogUpdateBusinessSpace.html"),
    "confirm.dialog.js" => array("dialog.js", "dialogconfirm.lang.js"),
    "message.dialog.js" => array("DialogMessage.html", "dialogmessage.lang.js"),
    "importthesaurus.dialog.js" => array("DialogImportThesaurus.html", "dialogimportthesaurus.lang.js"),
    "newterm.dialog.js" => array("DialogNewTerm.html", "dialognewterm.lang.js"),
    
    "importdata.dialog.js" => array("DialogImportData.html", "dialogimportdata.lang.js", "DialogImportThesaurus.css"),
    "importdata.dialog.js" => array("DialogImportData.html", "dialogimportdata.lang.js"),
    "importdataprogress.dialog.js" => array("DialogImportDataProgress.html", "dialogimportdataprogress.lang.js"),
    "importdataresult.dialog.js" => array("DialogImportDataResult.html", "dialogimportdataresult.lang.js", "items.adapter.js"),
    "importwizard.dialog.js" => array("dialogimportwizard.lang.js"),
    "importdatawizard.dialog.js" => array("importwizard.dialog.js", "WizardDialogImportData.html", "importdata.dialog.js", "importdataprogress.dialog.js", "importdataresult.dialog.js"),    
    "updatespacewizard.dialog.js" => array("wizard.js", "WizardDialogUpdateSpace.html", "wizarddialogupdatespace.lang.js", "UpdateSpacePage.html", "updatespacepage.lang.js", "UpdateModelPage.html", "updatemodelpage.lang.js")          
  );

  //---------------------------------------------------------------
  $InterfaceLang[PATH] = "../src/interface/lang/<language>/";
  $InterfaceLang[FILES] = array(
    "message.lang.js" => SINGLE,
    "error.lang.js" => SINGLE,
    "common.lang.js" => array("error.lang.js", "message.lang.js"),
    "desktop.lang.js" => SINGLE,
    "bottom.layout.lang.js" => SINGLE,
    "toolbar.lang.js" => SINGLE,
    "imageviewer.lang.js" => SINGLE,  
        
    "dialog.lang.js" => SINGLE,
    "dialogconfirm.lang.js" => array("dialog.lang.js"),
    "dialogmessage.lang.js" => array("dialog.lang.js"),
    "dialogupdatebusinessmodel.lang.js" => SINGLE,
    "dialogupdatebusinessspace.lang.js" => SINGLE,
    "dialogimportdata.lang.js" => SINGLE,
    "dialogimportdataprogress.lang.js" => SINGLE,    
    "dialogimportdataresult.lang.js" => SINGLE,
    "dialogimportthesaurus.lang.js" => SINGLE, 
    "dialognewterm.lang.js" => SINGLE,
    "viewthesaurus.lang.js" => SINGLE,
    "thesaurusexternalpanel.lang.js" => SINGLE,
         
    "confirmdialog.lang.js" => SINGLE,   
    "dialogimportwizard.lang.js" => SINGLE,  
    "wizarddialogupdatespace.lang.js" => SINGLE,
    "updatespacepage.lang.js" => SINGLE,
    "updatemodelpage.lang.js" => SINGLE,
       
    "providerstree.lang.js" => SINGLE,
    "providersgrid.lang.js" => SINGLE,
                
    "viewbusinessmodel.lang.js" => SINGLE,
    "viewbusinessspace.lang.js" => SINGLE,
    "ViewConsole.lang.js" => SINGLE,
    "viewfederation.lang.js" => SINGLE,
    "viewproviders.lang.js" => SINGLE,
    "viewcomponents.lang.js" => SINGLE
  );

  //---------------------------------------------------------------
  $InterfaceLayout[PATH] = "../src/interface/layout/";
  $InterfaceLayout[FILES] = array(
    "top.layout.js" => array("toolhandler.lib.js"),	    
    "bottom.layout.js" => array("bottom.layout.lang.js", "space.tab.js", "thesaurus.tab.js", "providers.tab.js"),   
    "space.tab.js" => array("businessspace.view.js", "updatebusinessmodel.dialog.js", "updatebusinessspace.dialog.js", "SpaceTab.html", "confirm.dialog.js"),
    "thesaurus.tab.js" => array("thesaurus.view.js", "ThesaurusTab.html", "importthesaurus.dialog.js", "newterm.dialog.js", "confirm.dialog.js"),       
    "providers.tab.js" => array("providers.view.js", "ProvidersTab.html"),
    "console.tab.js" => array("console.view.js", "importdata.panel.js", "ConsoleTab.html")
  );

  //---------------------------------------------------------------
  $InterfaceToolbar[PATH] = "../src/interface/toolbar/<language>/";
  $InterfaceToolbar[FILES] = array(
    "eventloggrid.toolbar.js" => SINGLE,
    "thesaurusgrid.toolbar.js" => SINGLE      
  );

  //---------------------------------------------------------------
  $InterfaceView[PATH] = "../src/interface/view/";
  $InterfaceView[FILES] = array(    
    "businessmodel.view.js" => array("businessmodel.js", "viewbusinessmodel.lang.js", "ViewBusinessModel.html", "common.lib.js", "updatebusinessmodel.dialog.js"),
    "businessspace.view.js" => array("businessspace.js", "viewbusinessspace.lang.js", "ViewBusinessSpace.html", "common.lib.js", "updatebusinessspace.dialog.js", "console.view.js", "federation.view.js", "importdata.panel.js", "components.view.js"),
    "thesaurus.view.js" => array("thesaurus.js", "viewthesaurus.lang.js", "ViewThesaurus.html"),
    "thesaurusexternal.panel.js" => array("thesaurus.js", "ThesaurusExternalPanel.html", "thesaurusexternalpanel.lang.js"),
    "thesaurus.view.js" => array("thesaurus.js", "ViewThesaurus.html", "viewthesaurus.lang.js", "thesaurusgrid.toolbar.js", "listcomponent.js", "thesaurusexternal.panel.js"),  
    "console.view.js" => array("businessspace.js", "ViewConsole.html", "ViewConsole.lang.js"),        
    "federation.view.js" => array("businessspace.js", "ViewFederation.html", "viewfederation.lang.js"),
    "importdata.panel.js" => array("ImportDataPanel.html", "importdatawizard.dialog.js"),
    "providers.view.js" => array("ViewProviders.html", "viewproviders.lang.js", "providers.tree.js", "providers.grid.js", "provider.serializer.js"),
    "components.view.js" => array("ViewComponents.html", "viewcomponents.lang.js", "service.js")    	   
  );

  //---------------------------------------------------------------
  $Library[PATH] = "../src/lib/";
  $Library[FILES] = array(
    "common.lib.js" => SINGLE,
    "exception.lib.js" => SINGLE,
    "commandinfo.lib.js" => SINGLE,
    "pagecontrol.lib.js" => SINGLE,
    "util.js" => SINGLE    
  );

  //---------------------------------------------------------------
  $LibraryEdi[PATH] = "../src/lib/edi/";
  $LibraryEdi[FILES] = array(
    "listview.lib.js" => SINGLE,
    "inputfiles.lib.js" => SINGLE,
    "buzz.lib.js" => SINGLE,
    "toolhandler.lib.js" => SINGLE       
  );
  
  //---------------------------------------------------------------
  $ResourcesStyles[PATH] = "../src/_resources/styles/";
  $ResourcesStyles[FILES] = array( 
    "Desktop.css" => SINGLE,            
    "dialog.css" => SINGLE, 
    "Dialog.css" => SINGLE,
    "ImageViewer.css" => SINGLE,   
    
    "DialogMessage.css" => SINGLE,
    "DialogUpdateBusinessModel.css" => array("Dialog.css"),
    "DialogUpdateBusinessSpace.css" => array("Dialog.css"),   
    "DialogImportData.css" => array("Dialog.css"),      
    "DialogImportDataProgress.css" => SINGLE,      
    "DialogImportDataResult.css" => SINGLE,
    "WizardDialogImportData.css" => SINGLE,   
    "WizardDialogUpdateSpace.css" => SINGLE,  
    
    "DialogImportThesaurus.css" => SINGLE,
    "DialogNewTerm.css" => SINGLE, 
        
    "ViewBusinessModel.css" => SINGLE,
    "ViewBusinessSpace.css" => SINGLE,
    "ViewThesaurus.css" => SINGLE, 
    "ViewConsole.css" => SINGLE,              
    "ViewProviders.css" => SINGLE,
    "ViewComponents.css" => SINGLE,   
    "ListComponent.css" => SINGLE,
    "ThesaurusExternalPanel.css" => SINGLE  
  );
  
  //---------------------------------------------------------------
  $ResourcesStylesExt[PATH] = "../src/_resources/styles/ext/";
  $ResourcesStylesExt[FILES] = array();

  //---------------------------------------------------------------
  $ResourcesStylesLang[PATH] = "../src/_resources/styles/<language>/";  
  $ResourcesStylesLang[FILES] = array();
  
  //---------------------------------------------------------------
  $ResourcesTemplates[PATH] = "../src/_resources/templates/";
  $ResourcesTemplates[FILES] = array(
    "Desktop.html" => array("Desktop.css"),		       
    "Toolbar.html" => SINGLE,
    "Popup.html" => SINGLE,
    
    "SpaceTab.html" => SINGLE,
    "ThesaurusTab.html" => SINGLE,
    "ProvidersTab.html" => SINGLE,
    "ConsoleTab.html" => SINGLE,

    "ListItemComponent.html" => SINGLE,    
    "Menu.html" => SINGLE,
    "ImageViewer.html" => array("ImageViewer.css"),
    
    "ViewBusinessModel.html" => array("ViewBusinessModel.css"),
    "ViewBusinessSpace.html" => array("ViewBusinessSpace.css"),
    "ViewThesaurus.html" => array("ViewThesaurus.css", "ListComponent.css"), 
    "ViewConsole.html" => array("ViewConsole.css"),      
    "ViewFederation.html" => SINGLE,
    "ViewProviders.html" => array("ViewProviders.css"),
    "ViewComponents.html" => array("ViewComponents.css"),
    
    "ThesaurusExternalPanel.html" => array("ThesaurusExternalPanel.css"),
    "ImportDataPanel.html" => array("DialogImportData.html","DialogImportDataResult.html", "DialogImportDataProgress.html"),
            
    "DialogImportThesaurus.html" => array("dialog.css", "DialogImportThesaurus.css"),
    "DialogUpdateBusinessModel.html" => array("DialogUpdateBusinessModel.css"),
    "DialogUpdateBusinessSpace.html" => array("DialogUpdateBusinessSpace.css"),   
    "DialogMessage.html" => array("DialogMessage.css"),
    "DialogNewTerm.html" => array("DialogNewTerm.css"),
    
    "DialogImportData.html" => array("DialogImportData.css"),
    "DialogImportDataResult.html" => array("DialogImportDataResult.css", "ListItemComponent.html"),
    "DialogImportDataProgress.html" => array("DialogImportDataProgress.css"),
    "WizardDialogImportData.html" => array("WizardDialogImportData.css"),
    "WizardDialogUpdateSpace.html" => array("WizardDialogUpdateSpace.css"),
    "UpdateSpacePage.html" => SINGLE,
    "UpdateModelPage.html" => SINGLE               
  );

?>
