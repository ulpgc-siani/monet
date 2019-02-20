Lang.Widget = {

  EmptyNode: "There is not defined element",

  Boolean: {
    Yes: "Yes",
    No: "No"
  },

  Table: {
    ElementLabel: "No label"
  },

  Templates: {
    Observer: '<div class="observer"><span></span> editing...&nbsp;<a class="command" href="requestnodefieldcontrol()">tomar el control</a></div>',
    WidgetOptions: '<div class="options"><a class="clearvalue" alt="delete" title="delete"/></div>',
    Loading: '<div class="wloading"><img src="#{ImagesPath}/icons/loading.gif" alt="Loading..." title="Loading..."/><span>Loading...</span></div>',
    ListTableElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Hold on pushed the left button of the mouse to drag and drop" title="Hold on pushed the left button of the mouse to drag and drop"></a></div></td><td class="label">#{label}</td></tr></table></li>',
    ListElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Hold on pushed the left button of the mouse to drag and drop" title="Hold on pushed the left button of the mouse to drag and drop"></a></div></td><td><div class="elementwidget"><a class="delete" alt="delete" title="delete">&nbsp;</a>#{widget}</div></td></tr></table></li>',
    CompositeExtensibleOptions: '<div class="options extensible"><a class="expand"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Show all the fields" title="Show all the fields"/><span title="Show all the fields">More</span></a><a class="collapse"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Show only the minimum fields" title="Show only the minimum fields"/><span title="Show only the minimum fields">Less</span></a></div>',
    CompositeConditionalOptions: '<span class="options conditional"><input name="compositegroupoptions_#{id}" id="compositegroupoption_#{id}_yes" class="option" style="margin-left:10px;" type="radio" value="yes"/><label style="display:inline;" for="compositegroupoption_#{id}_yes">Si</label><input id="compositegroupoption_#{id}_no" name="compositegroupoptions_#{id}" class="option" style="margin-left:10px;" type="radio" value="no" checked /><label style="display:inline;" for="compositegroupoption_#{id}_no">No</label></span>',
    LinkNodeBox: '<div class="nodecontainer"></div>',
    HistoryStoreUrl: '#{api}?op=loadhistoryterms&code=#{code}&list=#{list}',
    SourceUrl: '#{api}?op=loadsourceterms&list=#{list}&flatten=#{flatten}&depth=#{depth}',
    IndexUrl: '#{api}?op=loadsourceterms&list=#{list}&flatten=#{flatten}&depth=#{depth}',
    DataLinkUrl: '#{api}?op=loadlinknodeitems&domain=#{domain}&code=#{code}&list=#{list}',
    DataLinkLocationsUrl: '#{api}?op=loadlinknodeitemslocations&domain=#{domain}&code=#{code}&list=#{list}',
    DataLinkLocationsCountUrl: '#{api}?op=loadlinknodeitemslocationscount&domain=#{domain}&code=#{code}&list=#{list}',
    AttributeListUrl: '#{api}?op=loadattributes&idnode=#{idnode}',
    CheckReloadSourceUrl: '#{api}?op=loadnodefieldcheckoptions&id=#{id}&source=#{source}&field=#{field}&idfield=#{fieldId}&from=#{from}',
    Required: '<img class="icon wrequired" src="#{ImagesPath}/icons/required.gif" title="Required field is incomplete" alt="*" /><span class="msgwhenrequired">#{messageWhenRequired}</span>',
    Waiting: '<img src="#{ImagesPath}/s.gif" class="#{cls}" alt="#{message}" title="#{message}"><span>#{message}</span>',
    FileSizeExceeded: '<span style="color:red">File size exceeded. Max size is #{size} megabytes (MB)</span>',
    FileOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Download the document" title="Download the document">Download the document</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Delete" title="Delete">Delete</a></div>',
    ImageOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Descargar imagen" title="Delete the image">Delete the image</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Delete" title="Delete">Delete</a></div>',
    LinkOnlineMenu: '<div class="onlinemenu">Go to the element: <a class="hiperlink"></a>&nbsp;-&nbsp;<a class="clearvalue" alt="Delete" title="Delete">Delete</a></div>'
  }

};

WidgetObserverTemplate = new Template(Lang.Widget.Templates.Observer);
WidgetLoadingTemplate = new Template(Lang.Widget.Templates.Loading);
WidgetListTableElementTemplate = new Template(Lang.Widget.Templates.ListTableElement);
WidgetListElementTemplate = new Template(Lang.Widget.Templates.ListElement);
WidgetCompositeExtensibleOptionsTemplate = new Template(Lang.Widget.Templates.CompositeExtensibleOptions);
WidgetCompositeConditionalOptionsTemplate = new Template(Lang.Widget.Templates.CompositeConditionalOptions);
WidgetLinkNodeBoxTemplate = new Template(Lang.Widget.Templates.LinkNodeBox);

WidgetTemplateHistoryStoreUrl = new Template(Lang.Widget.Templates.HistoryStoreUrl);
WidgetTemplateSourceUrl = new Template(Lang.Widget.Templates.SourceUrl);
WidgetTemplateIndexUrl = new Template(Lang.Widget.Templates.IndexUrl);
WidgetTemplateDataLinkUrl = new Template(Lang.Widget.Templates.DataLinkUrl);
WidgetTemplateDataLinkLocationsUrl = new Template(Lang.Widget.Templates.DataLinkLocationsUrl);
WidgetTemplateDataLinkLocationsCountUrl = new Template(Lang.Widget.Templates.DataLinkLocationsCountUrl);
WidgetTemplateAttributeListUrl = new Template(Lang.Widget.Templates.AttributeListUrl);
WidgetTemplateCheckReloadSourceUrl = new Template(Lang.Widget.Templates.CheckReloadSourceUrl);

WidgetOptionsTemplate = new Template(Lang.Widget.Templates.WidgetOptions);
WidgetWaitingTemplate = new Template(Lang.Widget.Templates.Waiting);
WidgetFileSizeExceededTemplate = new Template(Lang.Widget.Templates.FileSizeExceeded);
WidgetFileOnlineMenuTemplate = new Template(Lang.Widget.Templates.FileOnlineMenu);
WidgetImageOnlineMenuTemplate = new Template(Lang.Widget.Templates.ImageOnlineMenu);
WidgetLinkOnlineMenuTemplate = new Template(Lang.Widget.Templates.LinkOnlineMenu);
