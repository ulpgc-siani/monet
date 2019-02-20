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
    "application.js" => array("constants.js", "indicator.js", "attribute.js", "common.lang.js", "signer.lib.js", "state.js", "helper.js", "eventmanager.js", "common.lib.js", "cookies.lib.js", "date.lib.js", "decimalformat.lib.js", "documentviewer.lib.js", "toolhandler.lib.js", "exception.lib.js", "commandlistener.js", "commanddispatcher.js", "behaviourdispatcher.js", "kernel.js", "account.js", "desktop.js", "tablegrid.lib.js", "extension.js", "bpi.js", "widgetmanager.js", "cache.js")
  );

  //---------------------------------------------------------------
  $Control[PATH] = "../src/control/";
  $Control[FILES] = array(
    "process.js" => SINGLE,
    "account.process.js" => array("process.js"),
    "node.process.js" => array("process.js"),
    "tasklist.process.js" => array("process.js"),
    "dom.process.js" => array("process.js", "tasklist.process.js"),
    "task.process.js" => array("process.js"),
    "team.process.js" => array("process.js"),
    "bpi.process.js" => array("process.js", "account.process.js", "node.process.js"),
    "source.process.js" => array("process.js"),
    "helper.process.js" => array("process.js", "rememberpreference.dialog.js"),
    "bpi.js" => array("bpi.process.js", "bpinode.js", "bpifieldboolean.js", "bpifielddate.js", "bpifieldfile.js", "bpifieldlink.js", "bpifieldnode.js", "bpifieldserial.js", "bpifieldlocation.js", "bpifieldsummation.js", "bpifieldnumber.js", "bpifieldpicture.js", "bpifieldcomposite.js", "bpifieldselect.js", "bpifieldtext.js", "bpifieldcheck.js", "bpitask.js", "bpilistener.js"),
    "action.js" => array("commandfactory.js", "refreshtasklist.js", "process.js"),
    "base.action.js" => array("action.js"),
    "tasklist.action.js" => array("action.js", "dom.process.js", "tasklist.process.js", "selecttaskowner.dialog.js"),
    "task.action.js" => array("base.action.js", "dom.process.js", "task.process.js", "alertentity.dialog.js", "selecttaskowner.dialog.js", "taskcomments.dialog.js"),
    "team.action.js" => array("base.action.js", "dom.process.js", "team.process.js"),
    "helper.action.js" => array("action.js", "helper.process.js"),
    "init.action.js" => array("action.js", "pushclient.js", "pushlistener.js"),
    "dom.action.js" => array("process.js"),
    "news.action.js" => array("action.js", "dom.process.js", "pagenews.view.js", "news.view.js"),
    "node.action.js" => array("base.action.js", "node.process.js", "dom.process.js", "nodeviewlist.dialog.js", "sortnodesby.dialog.js", "listviewer.lib.js", "filterswizard.lib.js", "nodelocation.view.js", "maplayer.view.js", "field.js", "documentlist.viewer.js", "printentity.dialog.js"),
    "account.action.js" => array("action.js", "account.process.js"),
    "trash.action.js" => array("process.js"),
    "dashboard.action.js" => array("process.js"),
    "sourcelist.action.js" => array("process.js", "listviewerfactory.js", "navigationviewcontroller.lib.js"),
    "source.action.js" => array("process.js", "source.process.js", "source.js", "term.js"),
    "rolelist.action.js" => array("process.js", "listviewerfactory.js", "navigationviewcontroller.lib.js"),
    "role.action.js" => array("role.process.js", "roledefinition.js", "role.js"),
    "role.process.js" => array("process.js"),
    "notification.action.js" => array("action.js", "user.view.js"),
    "notificationlist.action.js" => array("action.js"),
    "analysisboard.action.js" => array("process.js"),
    "commandfactory.js" => SINGLE,
    "commanddispatcher.js" => SINGLE,
    "commandlistener.js" => array("commanddispatcher.js", "init.action.js", "dom.action.js", "news.action.js", "node.action.js", "helper.action.js", "tasklist.action.js", "task.action.js", "team.action.js", "account.action.js", "trash.action.js", "dashboard.action.js", "analysisboard.action.js", "sourcelist.action.js", "source.action.js", "rolelist.action.js", "role.action.js", "notification.action.js", "notificationlist.action.js"),
    "behaviourdispatcher.js" => array ("viewnode.behaviour.js", "viewtasklist.behaviour.js", "viewtask.behaviour.js", "viewtrash.behaviour.js")
  );
  
  //---------------------------------------------------------------
  $CoreModel[PATH] = "../src/core/model/";
  $CoreModel[FILES] = array(
    "basemodel.js" => SINGLE,
    "state.js" => SINGLE,
    "cache.js" => SINGLE,
    "bpilistener.js" => SINGLE,
    "bpifieldfactory.js" => SINGLE,
    "bpinodereference.js" => SINGLE,
    "bpinode.js" => array("basemodel.js", "bpinodereference.js", "bpifieldfactory.js"),
    "bpifield.js" => array("basemodel.js", "bpifieldfactory.js"),
    "bpifieldboolean.js" => array("bpifield.js"),
    "bpifielddate.js" => array("bpifield.js"),
    "bpifieldfile.js" => array("bpifield.js"),
    "bpifieldlink.js" => array("bpifield.js"),
    "bpifieldnumber.js" => array("bpifield.js"),
    "bpifieldpicture.js" => array("bpifield.js"),
    "bpifieldcomposite.js" => array("bpifield.js"),
    "bpifieldselect.js" => array("bpifield.js"),
    "bpifieldtext.js" => array("bpifield.js"),
    "bpifieldcheck.js" => array("bpifield.js"),
    "bpifieldnode.js" => array("bpifield.js"),
    "bpifieldserial.js" => array("bpifield.js"),
    "bpifieldlocation.js" => array("bpifield.js"),
    "bpifieldsummation.js" => array("bpifield.js"),
    "bpitask.js" => array("basemodel.js"),
    "command.js" => array("basemodel.js"),
    "task.js" => array("basemodel.js"),
    "tasklist.js" => array("basemodel.js", "task.js"),
    "tasksearchrequest.js" => array("basemodel.js"),
    "team.js" => array("basemodel.js"),
    "report.js" => array("basemodel.js"),
    "node.js" => array("basemodel.js", "node.reference.js", "attributelist.js"),
    "field.js" => array("basemodel.js"),
    "nodelist.js" => array("basemodel.js", "node.js"),
    "node.reference.js" => array("basemodel.js"),
    "attribute.js" => array("basemodel.js", "indicatorlist.js"),
    "attributelist.js" => array("basemodel.js", "attribute.js"),
    "indicator.js" => array("basemodel.js", "htmlutil.lib.js"),
    "indicatorlist.js" => array("basemodel.js", "indicator.js"),
    "user.info.js" => array("basemodel.js"),
    "user.js" => array("basemodel.js", "user.info.js"),
    "userlist.js" => array("basemodel.js", "user.js"),
    "account.js" => array("basemodel.js", "user.js", "tasklist.js"),
    "page.js" => array("basemodel.js"),
    "refreshtask.js" => array("basemodel.js"),
    "refreshtasklist.js" => array("basemodel.js", "refreshtask.js"),
    "behaviourinfo.js" => array("basemodel.js"),
    "nodetype.list.js" => array("basemodel.js"),
    "nodetypeview.list.js" => array("basemodel.js"),
    "referencedescriptor.list.js" => array("basemodel.js"),
    "extension.js" => array("node.constructor.js", "helperitem.constructor.js", "task.constructor.js", "team.constructor.js", "datalink.js"),
    "eventmanager.js" => SINGLE,
    "helper.js" => SINGLE,
    "operation.js" => SINGLE,
    "pushlistener.js" => SINGLE,
    "listviewerfactory.js" => SINGLE,
    "roledefinition.js" => SINGLE,
    "role.js" => SINGLE,
    "source.js" => array("serializerdata.js"),
    "term.js" => SINGLE,
    "summationitem.js" => SINGLE,
    "summationitemlist.js" => array("summationitem.js"),
    "serializerdata.js" => SINGLE,
    "documentviewerfactory.js" => SINGLE,
    "furniture.js" => SINGLE
  );

  //---------------------------------------------------------------
  $CoreIterators[PATH] = "../src/core/iterators/";
  $CoreIterators[FILES] = array(
  );

  //---------------------------------------------------------------
  $CoreProducer[PATH] = "../src/core/producer/";
  $CoreProducer[FILES] = array(
  );

  //---------------------------------------------------------------
  $CoreKernel[PATH] = "../src/core/kernel/";
  $CoreKernel[FILES] = array(
    "stub.js" => SINGLE,
    "stubajax.js" => array("stub.js"),
    "kernel.js" => array("stubajax.js","node.js","base64.lib.js"),
    "datalink.js" => array("request.lib.js"),
    "pushclient.js" => SINGLE
  );

  //---------------------------------------------------------------
  $Interface[PATH] = "../src/interface/";
  $Interface[FILES] = array(
    "desktop.js" => array("desktop.lang.js", "Desktop.html", "header.layout.js", "main.layout.js", "footer.layout.js", "widgetfactory.js")
  );

  //---------------------------------------------------------------
  $InterfaceConstructor[PATH] = "../src/interface/constructor/";
  $InterfaceConstructor[FILES] = array(
    "constructor.js" => array("behaviourinfo.js"),
    "helperitem.constructor.js" => array("constructor.js", "decorator.lang.js", "helperitem.decorator.js", "helperitemtasklist.decorator.js"),
    "node.constructor.js" => array("constructor.js", "decorator.lang.js", "collection.decorator.js", "field.decorator.js", "form.decorator.js", "node.decorator.js", "nodereference.decorator.js", "section.decorator.js", "widget.decorator.js"),
    "task.constructor.js" => array("constructor.js", "decorator.lang.js", "task.decorator.js", "routemap.decorator.js", "widget.decorator.js"),
    "team.constructor.js" => array("constructor.js", "decorator.lang.js", "team.decorator.js")
  );

  //---------------------------------------------------------------
  $InterfaceDecorator[PATH] = "../src/interface/decorator/";
  $InterfaceDecorator[FILES] = array(
    "decorator.js" => array("helpertoolbar.viewer.js"),
    "collection.decorator.js" => array("decorator.js"),
    "fieldboolean.decorator.js" => array("decorator.js"),
    "fieldcheck.decorator.js" => array("decorator.js"),
    "fielddate.decorator.js" => array("decorator.js"),
    "fieldfile.decorator.js" => array("decorator.js"),
    "fieldformula.decorator.js" => array("decorator.js"),
    "fieldlink.decorator.js" => array("decorator.js"),
    "fieldnode.decorator.js" => array("decorator.js"),
    "fieldnumber.decorator.js" => array("decorator.js"),
    "fieldpicture.decorator.js" => array("decorator.js"),
    "fieldcomposite.decorator.js" => array("decorator.js"),
    "fieldselect.decorator.js" => array("decorator.js"),
    "fieldboolean.decorator.js" => array("decorator.js"),
    "fieldtext.decorator.js" => array("decorator.js"),
    "fielddescriptor.decorator.js" => array("decorator.js"),
    "fieldserial.decorator.js" => array("decorator.js"),
    "fieldlocation.decorator.js" => array("decorator.js"),
    "fieldsummation.decorator.js" => array("decorator.js"),
    "field.decorator.js" => array("fieldboolean.decorator.js", "fieldcheck.decorator.js", "fielddate.decorator.js", "fieldfile.decorator.js", "fieldformula.decorator.js", "fieldlink.decorator.js", "fieldnode.decorator.js", "fieldnumber.decorator.js", "fieldpicture.decorator.js", "fieldcomposite.decorator.js", "fieldselect.decorator.js", "fieldboolean.decorator.js", "fieldtext.decorator.js", "fielddescriptor.decorator.js", "fieldserial.decorator.js", "fieldlocation.decorator.js", "fieldsummation.decorator.js"),
    "collection.decorator.js" => array("decorator.js"),
    "form.decorator.js" => array("decorator.js"),
    "node.decorator.js" => array("decorator.js"),
    "task.decorator.js" => array("decorator.js"),
    "team.decorator.js" => array("decorator.js"),
    "helperitem.decorator.js" => array("decorator.js"),
    "helperitemtasklist.decorator.js" => array("decorator.js"),
    "routemap.decorator.js" => array("decorator.js"),
    "nodereference.decorator.js" => array("decorator.js"),
    "section.decorator.js" => array("decorator.js"),
    "widgetcomposite.decorator.js" => array("decorator.js"),
    "widget.decorator.js" => array("decorator.js", "widgetcomposite.decorator.js")
  );

  //---------------------------------------------------------------
  $InterfaceBehaviour[PATH] = "../src/interface/behaviour/";
  $InterfaceBehaviour[FILES] = array(
    "behaviour.js" => SINGLE,
    "viewnode.behaviour.js" => array("behaviour.js"),
    "viewtasklist.behaviour.js" => array("behaviour.js"),
    "viewtask.behaviour.js" => array("behaviour.js"),
    "viewtrash.behaviour.js" => array("behaviour.js")
  );

  //---------------------------------------------------------------
  $InterfaceDialog[PATH] = "../src/interface/dialog/";
  $InterfaceDialog[FILES] = array(
    "dialog.js" => array("dialog.lang.js"),
    "wizard.js" => array("Wizard.html", "wizard.lang.js"),
    "searchnodes.dialog.js" => array("dialog.js", "dialogsearchnodes.lang.js", "DialogSearchNodes.html"),
    "addnode.dialog.js" => array("dialog.js", "dialogaddnode.lang.js", "DialogAddNode.html", "inputfiles.lib.js", "wizard.js", "dataimporter.lib.js"),
    "editnodedocument.dialog.js" => array("dialog.js", "dialogeditnodedocument.lang.js", "DialogEditNodeDocument.html", "inputfiles.lib.js"),
    "generatereport.dialog.js" => array("dialog.js", "dialoggeneratereport.lang.js", "dialoggeneratereporttype.lang.js", "DialogGenerateReport.html", "DialogGenerateReportType.html"),
    "rememberpreference.dialog.js" => array("dialog.js", "dialogrememberpreference.lang.js", "DialogRememberPreference.html"),
    "editnode.dialog.js" => array("dialog.js", "dialogeditnode.lang.js", "DialogEditNode.html"),
    "editnodedescriptors.dialog.js" => array("dialog.js", "dialogeditnodedescriptors.lang.js", "DialogEditNodeDescriptors.html"),
    "nodeviewlist.dialog.js" => array("dialog.js"),
    "sortnodesby.dialog.js" => array("dialog.js"),
    "selecttaskowner.dialog.js" => array("dialog.js", "dialogselecttaskowner.lang.js", "DialogSelectTaskOwner.html"),
    "printentity.dialog.js" => array("dialog.js", "dialogprintentity.lang.js", "DialogPrintEntity.html"),
    "searchusers.dialog.js" => array("dialog.js", "dialogsearchusers.lang.js", "dialogsearchusersitem.lang.js", "DialogSearchUsers.html", "DialogSearchUsersItem.html", "userlist.js", "commandinfo.lib.js"),
    "searchroles.dialog.js" => array("dialog.js", "dialogsearchroles.lang.js", "dialogsearchrolesitem.lang.js", "DialogSearchRoles.html", "DialogSearchRolesItem.html", "userlist.js", "commandinfo.lib.js"),
    "alertentity.dialog.js" => array("dialog.js", "dialogalertentity.lang.js", "dialogalertentityuser.lang.js", "DialogAlertEntity.html", "DialogAlertEntityUser.html", "searchusers.dialog.js", "searchroles.dialog.js", "commandinfo.lib.js", "listview.lib.js"),
    "sharenode.dialog.js" => array("dialog.js", "dialogsharenode.lang.js", "dialogsharenodeuser.lang.js", "DialogShareNode.html", "DialogShareNodeUser.html", "searchusers.dialog.js", "commandinfo.lib.js"),
    "createtask.dialog.js" => array("dialog.js", "dialogcreatetask.lang.js", "DialogCreateTask.html"),
    "taskcomments.dialog.js" => array("dialog.js", "dialogtaskcomments.lang.js", "DialogTaskComments.html"),
  );

  //---------------------------------------------------------------
  $InterfaceLang[PATH] = "../src/interface/lang/<language>/";
  $InterfaceLang[FILES] = array(
    "message.lang.js" => SINGLE,
    "error.lang.js" => SINGLE,
    "decorator.lang.js" => SINGLE,
    "common.lang.js" => array("error.lang.js", "message.lang.js"),
    "desktop.lang.js" => SINGLE,
    "wizard.lang.js" => SINGLE,

    "layoutheader.lang.js" => SINGLE,
    "layoutmain.lang.js" => SINGLE,
    "layoutmainheader.lang.js" => SINGLE,
    "layoutmaincenter.lang.js" => SINGLE,
    "layoutmaincenterheader.lang.js" => SINGLE,
    "layoutmaincenterbody.lang.js" => SINGLE,
    "layoutmainright.lang.js" => SINGLE,
    "layoutfooter.lang.js" => SINGLE,

    "viewnode.lang.js" => SINGLE,
    "viewfurnitureset.lang.js" => SINGLE,
    "viewnodenotes.lang.js" => SINGLE,
    "viewnodelocation.lang.js" => SINGLE,
    "viewmaplayer.lang.js" => SINGLE,
    "viewtask.lang.js" => SINGLE,
    "viewtasklist.lang.js" => SINGLE,
    "viewteam.lang.js" => SINGLE,
    "viewuser.lang.js" => SINGLE,
    "viewtrash.lang.js" => SINGLE,
    "viewdashboard.lang.js" => SINGLE,
    "viewanalysisboard.lang.js" => SINGLE,
    "viewsourcelist.lang.js" => SINGLE,
    "viewrolelist.lang.js" => SINGLE,
    "viewnotificationlist.lang.js" => SINGLE,
    
    "viewerhelpertoolbar.lang.js" => SINGLE,
    "viewerhelpersidebar.lang.js" => SINGLE,
    "viewerhelperpage.lang.js" => SINGLE,
    "viewerhelpereditors.lang.js" => SINGLE,
    "viewerhelperpreview.lang.js" => SINGLE,
    "viewerhelperobservers.lang.js" => SINGLE,
    "viewerhelperlist.lang.js" => SINGLE,
    "viewerhelperrevisionlist.lang.js" => SINGLE,
    "viewerhelperchat.lang.js" => SINGLE,
    "viewerhelpermap.lang.js" => SINGLE,
    "viewerhelpersource.lang.js" => SINGLE,
    "viewerhelperrole.lang.js" => SINGLE,
    "viewerhelperlist.lang.js" => SINGLE,
    "viewerdocumentlist.lang.js" => SINGLE,

    "dialog.lang.js" => SINGLE,
    "dialogsearchnodes.lang.js" => SINGLE,
    "dialogaddnode.lang.js" => SINGLE,
    "dialogeditnodedocument.lang.js" => SINGLE,
    "dialoggeneratereport.lang.js" => SINGLE,
    "dialoggeneratereporttype.lang.js" => SINGLE,
    "dialogrememberpreference.lang.js" => SINGLE,
    "dialogeditnode.lang.js" => SINGLE,
    "dialogeditnodedescriptors.lang.js" => SINGLE,
    "dialogselecttaskowner.lang.js" => SINGLE,
    "dialogprintentity.lang.js" => SINGLE,
    "dialogsearchusers.lang.js" => SINGLE,
    "dialogsearchusersitem.lang.js" => SINGLE,
    "dialogsearchroles.lang.js" => SINGLE,
    "dialogsearchrolesitem.lang.js" => SINGLE,
    "dialogsharenode.lang.js" => SINGLE,
    "dialogsharenodeuser.lang.js" => SINGLE,
    "dialogalertentity.lang.js" => SINGLE,
    "dialogalertentityuser.lang.js" => SINGLE,
    "dialogcreatetask.lang.js" => SINGLE,
    "dialogtaskcomments.lang.js" => SINGLE,

    "editor.lang.js" => SINGLE,
    "editortabsource.lang.js" => SINGLE,

    "widget.lang.js" => SINGLE

  );

  //---------------------------------------------------------------
  $InterfaceLayout[PATH] = "../src/interface/layout/";
  $InterfaceLayout[FILES] = array(
    "header.layout.js" => array("layoutheader.lang.js", "user.view.js"),
    "main.layout.js" => array("layoutmain.lang.js", "mainheader.layout.js", "maincenter.layout.js", "mainright.layout.js"),
    "mainheader.layout.js" => array("layoutmainheader.lang.js", "header.toolbar.js"),
    "maincenter.layout.js" => array("layoutmaincenter.lang.js", "maincenterheader.layout.js", "maincenterbody.layout.js"),
    "maincenterheader.layout.js" => array("layoutmaincenterheader.lang.js", "searchnodes.dialog.js", "main.toolbar.js"),
    "maincenterbody.layout.js" => array("layoutmaincenterbody.lang.js", "node.view.js", "task.view.js", "team.view.js", "pagecontrol.lib.js", "helpertoolbar.viewer.js", "nodenotes.view.js"),
    "mainright.layout.js" => array("layoutmainright.lang.js", "helpersidebar.viewer.js", "tasklist.view.js", "trash.view.js", "dashboard.view.js", "analysisboard.view.js", "pagenews.view.js", "sourcelist.view.js", "source.view.js", "rolelist.view.js", "role.view.js", "notificationlist.view.js"),
    "footer.layout.js" => array("layoutfooter.lang.js", "furnitureset.view.js")
  );

  //---------------------------------------------------------------
  $InterfaceToolbar[PATH] = "../src/interface/toolbar/<language>/";
  $InterfaceToolbar[FILES] = array(
    "header.toolbar.js" => SINGLE,
    "main.toolbar.js" => SINGLE,
  );

  //---------------------------------------------------------------
  $InterfaceView[PATH] = "../src/interface/view/";
  $InterfaceView[FILES] = array(
    "view.js" => SINGLE,
    "tasklist.view.js" => array("viewtasklist.lang.js"),
    "news.view.js" => SINGLE,
    "node.view.js" => array("view.js", "nodelist.js", "node.js", "viewnode.lang.js", "ViewNode.html", "editnode.dialog.js", "editnodedescriptors.dialog.js", "addnode.dialog.js", "editnodedocument.dialog.js", "generatereport.dialog.js", "sharenode.dialog.js"),
    "nodelocation.view.js" => array("view.js", "locationpicker.lib.js", "ViewNodeLocationEditionToolbar.html", "ViewNodeLocationNavigationToolbar.html", "ViewNodeLocationSearch.html", "viewnodelocation.lang.js"),
    "maplayer.view.js" => array("view.js", "ViewPlacemarkInfoWindow.html", "ViewMapLayer.html", "ViewMapLayerSearch.html", "viewmaplayer.lang.js"),
    "task.view.js" => array("view.js", "task.js", "viewtask.lang.js", "ViewTask.html"),
    "team.view.js" => array("view.js", "team.js", "viewteam.lang.js", "ViewTeam.html"),
    "furnitureset.view.js" => array("furniture.js", "viewfurnitureset.lang.js", "ViewFurnitureSet.html"),
    "nodenotes.view.js" => array("viewnodenotes.lang.js", "ViewNodeNotes.html"),
    "user.view.js" => array("user.js", "viewuser.lang.js", "ViewUser.html"),
    "trash.view.js" => array("viewtrash.lang.js"),
    "dashboard.view.js" => array("viewdashboard.lang.js"),
    "analysisboard.view.js" => array("viewanalysisboard.lang.js"),
    "pagenews.view.js" => SINGLE,
    "sourcelist.view.js" => array("viewsourcelist.lang.js"),
    "source.view.js" => SINGLE,
    "rolelist.view.js" => array("viewrolelist.lang.js"),
    "role.view.js" => SINGLE,
    "notificationlist.view.js" => array("viewnotificationlist.lang.js"),
  );

  //---------------------------------------------------------------
  $InterfaceViewer[PATH] = "../src/interface/viewer/";
  $InterfaceViewer[FILES] = array(
    "helpereditors.viewer.js" => array("editorsfactory.js", "ViewerHelperEditors.html", "viewerhelpereditors.lang.js"),
    "helperpreview.viewer.js" => array("ViewerHelperPreview.html", "viewerhelperpreview.lang.js"),
    "helperobservers.viewer.js" => array("ViewerHelperObservers.html", "ViewerHelperObserversItem.html", "viewerhelperobservers.lang.js"),
    "helperpage.viewer.js" => array("page.js", "ViewerHelperPage.html", "viewerhelperpage.lang.js"),
    "helperrevisionlist.viewer.js" => array("ViewerHelperRevisionList.html", "ViewerHelperRevisionListItem.html", "viewerhelperrevisionlist.lang.js"),
    "helperchat.viewer.js" => array("ViewerHelperChat.html", "viewerhelperchat.lang.js"),
    "helpermap.viewer.js" => array("ViewerHelperMap.html", "viewerhelpermap.lang.js"),
    "helpersource.viewer.js" => array("ViewerHelperSource.html", "viewerhelpersource.lang.js", "serializerdata.js"),
    "helperrole.viewer.js" => array("ViewerHelperRole.html", "ViewerHelperRoleUser.html", "viewerhelperrole.lang.js"),
    "helperlist.viewer.js" => array("ViewerHelperList.html", "viewerhelperlist.lang.js"),
    "helpersidebar.viewer.js" => array("ViewerHelperSidebar.html", "viewerhelpersidebar.lang.js", "helperpage.viewer.js", "helpereditors.viewer.js", "helperpreview.viewer.js", "helperobservers.viewer.js", "helperrevisionlist.viewer.js", "helperchat.viewer.js", "helpermap.viewer.js", "helpersource.viewer.js", "helperrole.viewer.js", "helperlist.viewer.js", "operation.js"),
    "helpertoolbar.viewer.js" => array("ViewerHelperToolbar.html", "viewerhelpertoolbar.lang.js", "operation.js"),
    "documentlist.viewer.js" => array("ViewerDocumentList.html", "viewerdocumentlist.lang.js", "documentviewer.lib.js", "documentviewerfactory.js")
  );

  //---------------------------------------------------------------
  $InterfaceWidget[PATH] = "../src/interface/widget/";
  $InterfaceWidget[FILES] = array(
    "widget.js" => array("widget.lang.js", "attribute.js"),
    "widgetmanager.js" => SINGLE,
    "check.widget.js" => array("widget.js"),
    "date.widget.js" => array("widget.js"),
    "file.widget.js" => array("widget.js"),
    "formula.widget.js" => array("widget.js"),
    "link.widget.js" => array("widget.js"),
    "node.widget.js" => array("widget.js"),
    "list.widget.js" => array("widget.js","dragdrop.lib.js"),
    "table.widget.js" => array("widget.js","dragdrop.lib.js"),
    "number.widget.js" => array("widget.js"),    
    "picture.widget.js" => array("widget.js"),
    "required.widget.js" => array("widget.js"),
    "composite.widget.js" => array("widget.js"),
    "select.widget.js" => array("widget.js"),
    "text.widget.js" => array("widget.js"),
    "descriptor.widget.js" => array("widget.js"),
    "boolean.widget.js" => array("select.widget.js"),
    "serial.widget.js" => array("widget.js"),
    "location.widget.js" => array("widget.js"),
    "summation.widget.js" => array("widget.js", "summationitemlist.js"),
    "widgetfactory.js" => array("boolean.widget.js", "check.widget.js", "date.widget.js", "file.widget.js", "formula.widget.js", "link.widget.js", "node.widget.js", "list.widget.js", "table.widget.js", "number.widget.js", "picture.widget.js", "required.widget.js", "composite.widget.js", "select.widget.js", "text.widget.js", "descriptor.widget.js", "serial.widget.js", "location.widget.js", "summation.widget.js"),
  );

  //---------------------------------------------------------------
  $InterfaceEditor[PATH] = "../src/interface/editor/";
  $InterfaceEditor[FILES] = array(
    "editordialog.js" => SINGLE,

    "date.editordialog.js" => array("editordialog.js", "datepicker.lib.js"),
    "fileupload.editordialog.js" => array("editordialog.js"),
    "grid.editordialog.js" => array("editordialog.js"),
    "list.editordialog.js" => array("editordialog.js","dragdrop.lib.js"),
    "pictureupload.editordialog.js" => array("editordialog.js","imagecrop.lib.js"),
    "location.editordialog.js" => array("editordialog.js", "locationpicker.lib.js"),
    "locations.editordialog.js" => array("editordialog.js", "locationpicker.lib.js"),
    "source.editordialog.js" => array("editordialog.js"),

    "editor.js" => array("editor.lang.js"),
    "boolean.editor.js" => array("editor.js", "grid.editordialog.js"),
    "check.editor.js" => array("editor.js", "grid.editordialog.js"),
    "date.editor.js" => array("date.editordialog.js"),
    "file.editor.js" => array("editor.js", "fileupload.editordialog.js"),
    "link.editor.js" => array("select.editor.js", "locations.editordialog.js"),
    "node.editor.js" => array("editor.js"),
    "list.editor.js" => array("editor.js", "list.editordialog.js"),
    "number.editor.js" => array("editor.js"),
    "picture.editor.js" => array("editor.js", "pictureupload.editordialog.js"),
    "composite.editor.js" => array("editor.js"),
    "select.editor.js" => array("editor.js", "grid.editordialog.js", "source.editordialog.js"),
    "text.editor.js" => array("editor.js", "grid.editordialog.js"),
    "serial.editor.js" => array("editor.js"),
    "location.editor.js" => array("editor.js", "location.editordialog.js"),
    "summation.editor.js" => array("editor.js"),
    
    "editorsfactory.js" => array("boolean.editor.js", "check.editor.js", "date.editor.js", "file.editor.js", "link.editor.js", "node.editor.js", "list.editor.js", "number.editor.js", "picture.editor.js", "composite.editor.js", "select.editor.js", "text.editor.js", "serial.editor.js", "location.editor.js", "summation.editor.js")
  );

  //---------------------------------------------------------------
  $Library[PATH] = "../src/lib/";
  $Library[FILES] = array(
    "common.lib.js" => SINGLE,
    "cookies.lib.js" => SINGLE,
    "date.lib.js" => SINGLE,
    "decimalformat.lib.js" => SINGLE,
    "documentviewer.lib.js" => SINGLE,
    "exception.lib.js" => SINGLE,
    "commandinfo.lib.js" => SINGLE,
    "functioninfo.lib.js" => SINGLE,
    "base64.lib.js" => SINGLE,
    "htmlutil.lib.js" => SINGLE,
    "pagecontrol.lib.js" => SINGLE,
    "dragdrop.lib.js" => SINGLE,
    "imagecrop.lib.js" => SINGLE,
    "listviewer.lib.js" => array("ListViewer.css"),
    "filterswizard.lib.js" => array("ListViewer.css"),
    "datepicker.lib.js" => array("DatePicker.css"),
    "locationpicker.lib.js" => array("LocationPicker.css"),
    "navigationviewcontroller.lib.js" => array("NavigationViewController.css")
  );

  //---------------------------------------------------------------
  $LibraryEdi[PATH] = "../src/lib/edi/";
  $LibraryEdi[FILES] = array(
    "buzz.lib.js" => SINGLE,
    "listview.lib.js" => SINGLE,
    "inputfiles.lib.js" => SINGLE,
    "toolhandler.lib.js" => array("buzz.lib.js"),
    "request.lib.js" => SINGLE,
    "signer.lib.js" => SINGLE
  );

  //---------------------------------------------------------------
  $LibraryExt[PATH] = "../src/lib/ext/";
  $LibraryExt[FILES] = array(
    "tablegrid.lib.js" => SINGLE
  );

  //---------------------------------------------------------------
  $LibraryIsi[PATH] = "../src/lib/isi/";
  $LibraryIsi[FILES] = array(
    "dataimporter.lib.js" => array("buzz.lib.js", "DataImporter.css")
  );

  //---------------------------------------------------------------
  $LibraryView[PATH] = "../src/lib/view/";
  $LibraryView[FILES] = array(
  );

  //---------------------------------------------------------------
  $ResourcesStyles[PATH] = "../src/_resources/styles/";
  $ResourcesStyles[FILES] = array( 
    "Desktop.css" => SINGLE,
    "Desktop.dialogs.css" => SINGLE,
    "Desktop.fonts.css" => SINGLE,
    "Desktop.input.css" => SINGLE,
    "Desktop.reset.css" => SINGLE,
    
    "Wizard.css" => SINGLE,
    
    "Desktop.widgets.css" => SINGLE,

    "ViewNode.css" => SINGLE,
    "ViewFurnitureSet.css" => SINGLE,
    "ViewNodeNotes.css" => SINGLE,
    "ViewTask.css" => SINGLE,
    "ViewTeam.css" => SINGLE,
    "ViewUser.css" => SINGLE,
    "ViewPlacemarkInfoWindow.css" => SINGLE,
    "ViewMapLayer.css" => SINGLE,

    "ViewerHelper.css" => SINGLE,
    "ViewerHelperPage.css" => SINGLE,
    "ViewerHelperEditors.css" => SINGLE,
    "ViewerHelperPreview.css" => SINGLE,
    "ViewerHelperObservers.css" => SINGLE,
    "ViewerHelperList.css" => SINGLE,
    "ViewerHelperRevisionList.css" => SINGLE,
    "ViewerHelperChat.css" => SINGLE,
    "ViewerHelperMap.css" => SINGLE,
    "ViewerDocumentList.css" => SINGLE,

    "DialogSearchNodes.css" => SINGLE,
    "DialogGenerateReport.css" => SINGLE,
    "DialogRememberPreference.css" => SINGLE,
    "DialogAddNode.css" => SINGLE,
    "DialogEditNodeDocument.css" => SINGLE,
    "DialogEditNode.css" => SINGLE,
    "DialogEditNodeDescriptors.css" => SINGLE,
    "DialogSelectTaskOwner.css" => SINGLE,
    "DialogPrintEntity.css" => SINGLE,
    "DialogSearchUsers.css" => SINGLE,
    "DialogSearchUsersItem.css" => SINGLE,
    "DialogSearchRoles.css" => SINGLE,
    "DialogSearchRolesItem.css" => SINGLE,
    "DialogShareNode.css" => SINGLE,
    "DialogAlertEntity.css" => SINGLE,
    "DialogCreateTask.css" => SINGLE,
    "DialogTaskComments.css" => SINGLE,

    "DataImporter.css" => SINGLE,
    "ListViewer.css" => SINGLE,
    "DatePicker.css" => SINGLE,
    "LocationPicker.css" => SINGLE,
    "NavigationViewController.css" => SINGLE

  );

  //---------------------------------------------------------------
  $ResourcesStylesExt[PATH] = "../src/_resources/styles/ext/";
  $ResourcesStylesExt[FILES] = array( 
  );

  //---------------------------------------------------------------
  $ResourcesStylesLang[PATH] = "../src/_resources/styles/<language>/";
  $ResourcesStylesLang[FILES] = array( 
    "Images.css" => SINGLE
  );

  //---------------------------------------------------------------
  $ResourcesTemplates[PATH] = "../src/_resources/templates/";
  $ResourcesTemplates[FILES] = array(
    "Desktop.html" => array("Desktop.reset.css", "Desktop.dialogs.css", "Desktop.fonts.css", "Desktop.input.css", "Desktop.widgets.css", "Desktop.css"),

    "Wizard.html" => array("Wizard.css"),
    
    "ViewNode.html" => array("ViewNode.css", "Images.css"),
    "ViewFurnitureSet.html" => array("ViewFurnitureSetItem.html", "ViewFurnitureSetBannerItem.html", "ViewFurnitureSet.css"),
    "ViewFurnitureSetItem.html" => SINGLE,
    "ViewFurnitureSetBannerItem.html" => SINGLE,
    "ViewNodeNotes.html" => array("ViewNodeNotesItem.html", "ViewNodeNotes.css"),
    "ViewNodeNotesItem.html" => SINGLE,
    "ViewTask.html" => array("ViewTask.css"),
    "ViewTeam.html" => array("ViewTeam.css"),
    "ViewUser.html" => array("ViewUserUnitItem.html", "ViewUser.css"),
    "ViewUserUnitItem.html" =>  array("ViewUser.css"),
    "ViewPlacemarkInfoWindow.html" => array("ViewPlacemarkInfoWindow.css"),
    "ViewMapLayer.html" => array("ViewMapLayer.css"),
    "ViewMapLayerSearch.html" => SINGLE,
    "ViewNodeLocationEditionToolbar.html" => SINGLE,
    "ViewNodeLocationNavigationToolbar.html" => SINGLE,
    "ViewNodeLocationSearch.html" => SINGLE,
    
    "ViewerHelperToolbar.html" => array("ViewerHelperToolbarNavigation.html", "ViewerHelperToolbarAdd.html", "ViewerHelperToolbarCopy.html", "ViewerHelperToolbarDownload.html", "ViewerHelperToolbarEdit.html", "ViewerHelperToolbarPrint.html", "ViewerHelperToolbarTool.html", "ViewerHelperToolbarCustom.html", "ViewerHelperToolbarSimple.html", "ViewerHelperToolbarMultiple.html", "ViewerHelperToolbarMultipleItem.html", "ViewerHelper.css"),
    "ViewerHelperToolbarNavigation.html" => SINGLE,
    "ViewerHelperToolbarAdd.html" => SINGLE,
    "ViewerHelperToolbarCopy.html" => SINGLE,
    "ViewerHelperToolbarDownload.html" => SINGLE,
    "ViewerHelperToolbarEdit.html" => SINGLE,
    "ViewerHelperToolbarPrint.html" => SINGLE,
    "ViewerHelperToolbarTool.html" => SINGLE,
    "ViewerHelperToolbarCustom.html" => SINGLE,
    "ViewerHelperToolbarSimple.html" => SINGLE,
    "ViewerHelperToolbarMultiple.html" => SINGLE,
    "ViewerHelperToolbarMultipleItem.html" => SINGLE,
    
    "ViewerHelperSidebar.html" => array("ViewerHelperSidebarAdd.html", "ViewerHelperSidebarCopy.html", "ViewerHelperSidebarDownload.html", "ViewerHelperSidebarPrint.html", "ViewerHelperSidebarTool.html", "ViewerHelperSidebarCustom.html", "ViewerHelper.css"),
    "ViewerHelperSidebarAdd.html" => SINGLE,
    "ViewerHelperSidebarCopy.html" => SINGLE,
    "ViewerHelperSidebarDownload.html" => SINGLE,
    "ViewerHelperSidebarPrint.html" => SINGLE,
    "ViewerHelperSidebarTool.html" => SINGLE,
    "ViewerHelperSidebarCustom.html" => SINGLE,
    
    "ViewerHelperPage.html" => array("ViewerHelperPage.css"),
    "ViewerHelperEditors.html" => array("ViewerHelperEditors.css"),
    "ViewerHelperPreview.html" => array("ViewerHelperPreview.css"),
    "ViewerHelperObservers.html" => array("ViewerHelperObservers.css"),
    "ViewerHelperObserversItem.html" => array("ViewerHelperObservers.css"),
    "ViewerHelperList.html" => array("ViewerHelperList.css"),
    "ViewerHelperRevisionList.html" => array("ViewerHelperRevisionList.css"),
    "ViewerHelperRevisionListItem.html" => SINGLE,
    "ViewerHelperChat.html" => array("ViewerHelperChatItem.html", "ViewerHelperChatNoItems.html", "ViewerHelperChat.css"),
    "ViewerHelperChatItem.html" => SINGLE,
    "ViewerHelperChatNoItems.html" => SINGLE,
    "ViewerHelperMap.html" => array("ViewerHelperMap.css"),
    "ViewerHelperSource.html" => array("ViewerHelper.css"),
    "ViewerHelperRole.html" => array("ViewerHelper.css"),
    "ViewerHelperRoleUser.html" => array("ViewerHelper.css"),
    "ViewerHelperList.html" => array("ViewerHelper.css"),
    "ViewerDocumentList.html" => array("ViewerDocumentList.css"),

    "DialogSearchNodes.html" => array("DialogSearchNodes.css"),
    "DialogGenerateReport.html" => array("DialogGenerateReport.css"),
    "DialogGenerateReportType.html" => array("DialogGenerateReport.css"),
    "DialogRememberPreference.html" => array("DialogRememberPreference.css"),
    "DialogAddNode.html" => array("DialogAddNode.css"),
    "DialogEditNodeDocument.html" => array("DialogEditNodeDocument.css"),
    "DialogEditNode.html" => array("DialogEditNode.css"),
    "DialogEditNodeDescriptors.html" => array("DialogEditNodeDescriptors.css"),
    "DialogSelectTaskOwner.html" => array("DialogSelectTaskOwner.css"),
    "DialogPrintEntity.html" => array("DialogPrintEntityAttribute.html", "DialogPrintEntityDateAttribute.html", "DialogPrintEntity.css"),
    "DialogPrintEntityAttribute.html" => SINGLE,
    "DialogPrintEntityDateAttribute.html" => SINGLE,
    "DialogSearchUsers.html" => array("DialogSearchUsers.css"),
    "DialogSearchUsersItem.html" => array("DialogSearchUsersItem.css"),
    "DialogSearchRoles.html" => array("DialogSearchRoles.css"),
    "DialogSearchRolesItem.html" => array("DialogSearchRolesItem.css"),
    "DialogShareNode.html" => array("DialogShareNode.css"),
    "DialogShareNodeUser.html" => array("DialogShareNode.css"),
    "DialogAlertEntity.html" => array("DialogAlertEntity.css"),
    "DialogAlertEntityUser.html" => array("DialogAlertEntity.css"),
    "DialogCreateTask.html" => array("DialogCreateTask.css"),
    "DialogTaskComments.html" => array("DialogTaskComments.css"),

  );

?>