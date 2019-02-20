Lang.Widget = {

  EmptyNode: "No se ha definido el elemento",

  Boolean: {
    Yes: "Si",
    No: "No"
  },

  Table: {
    ElementLabel: "Sin etiqueta"
  },

  Templates: {
    Observer: '<div class="observer"><span></span> editando...&nbsp;<a class="command" href="requestnodefieldcontrol()">tomar el control</a></div>',
    WidgetOptions: '<div class="options"><a class="clearvalue" alt="borrar" title="borrar"/></div>',
    Loading: '<div class="wloading"><img src="#{ImagesPath}/icons/loading.gif" alt="Cargando..." title="Cargando..."/><span>Cargando...</span></div>',
    ListTableElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"></a></div></td><td class="label">#{label}</td></tr></table></li>',
    ListElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"></a></div></td><td><div class="elementwidget"><a class="delete" alt="borrar" title="borrar">&nbsp;</a>#{widget}</div></td></tr></table></li>',
    CompositeExtensibleOptions: '<div class="options extensible"><a class="expand"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Ver todos los campos" title="Ver todos los campos"/><span title="Ver todos los campos">Más</span></a><a class="collapse"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Ver solo los campos mínimos" title="Ver solo los campos mínimos"/><span title="Ver solo los campos mínimos">Menos</span></a></div>',
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
    Required: '<img class="icon wrequired" src="#{ImagesPath}/icons/required.gif" title="Campo obligatorio incompleto" alt="*" /><span class="msgwhenrequired">#{messageWhenRequired}</span>',
    Waiting: '<img src="#{ImagesPath}/s.gif" class="#{cls}" alt="#{message}" title="#{message}"><span>#{message}</span>',
    FileSizeExceeded: '<span style="color:red">Tamaño de archivo excedido. El tamaño máximo es de #{size} megabytes (MB)</span>',
    FileOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Descargar documento" title="Descargar documento">Descargar documento</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>',
    ImageOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Descargar imagen" title="Descargar imagen">Descargar imagen</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>',
    LinkOnlineMenu: '<div class="onlinemenu">Ir al elemento: <a class="hiperlink"></a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>'
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