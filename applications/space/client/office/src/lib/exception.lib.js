function RequestException(conn, response, options) {
  var extDialog, extTemplate;

  Desktop.hideReports();
  if (Ext.MessageBox.isVisible()) Ext.MessageBox.hide();

  if (!Context.Debugging) {
 	SendMail(Lang.Exceptions.Request.Title, extDialog.body.dom.innerHTML, extDialog);
	return;
  }

  extDialog = new Ext.BasicDialog(Literals.Dialogs.Exception, {width: 500, height: 400, resizable: false});
  extTemplate = new Ext.Template(Lang.Exceptions.Request.Description + 'url: {url}<br/>' + 'params: {params}<br/>' + 'status: {status}<br/>' + 'response: {response}');
  extDialog.body.dom.innerHTML = extTemplate.applyTemplate({url: options.url ? options.url : Ext.Ajax.url, params: options.params, status: response.status + " " + response.statusText, response: response.responseText});
  extDialog.setTitle(Lang.Exceptions.Request.Title);
  extDialog.addButton(Lang.Buttons.Close, CloseException.bind(this, extDialog));
  extDialog.addButton(Lang.Buttons.SendMail, SendMail.bind(this, Lang.Exceptions.Request.Title, extDialog.body.dom.innerHTML, extDialog));
  extDialog.center();
  extDialog.show();
};

function InternalException(e, dOp, dState) {
  var extDialog, extTemplate;

  Desktop.hideReports();
  if (Ext.MessageBox.isVisible()) Ext.MessageBox.hide();

  extDialog = new Ext.BasicDialog(Literals.Dialogs.Exception, {width: 500, height: 400, resizable: false});
  extTemplate = new Ext.Template(Lang.Exceptions.Internal.Description + 'operation: {op}<br/>' + 'state: {state}<br/>' + 'name: {name}<br/>' + 'message: {message}<br/>');

  extDialog.body.dom.innerHTML = extTemplate.applyTemplate({op: dOp, state: dState, name: e.name, message: e.message});

  if (!Context.Debugging) {
	SendMail(Lang.Exceptions.Request.Title, extDialog.body.dom.innerHTML, extDialog);
	return;
  }

  extDialog.setTitle(Lang.Exceptions.Internal.Title);
  extDialog.addButton(Lang.Buttons.Close, CloseException.bind(this, extDialog));
  extDialog.addButton(Lang.Buttons.SendMail, SendMail.bind(this, Lang.Exceptions.Internal.Title, extDialog.body.dom.innerHTML, extDialog));
  extDialog.center();
  extDialog.show();
};

function RequestExceptionWithMessage(message, stack) {
  var extDialog, extTemplate;

  Desktop.hideReports();
  if (Ext.MessageBox.isVisible()) Ext.MessageBox.hide();

  extDialog = new Ext.BasicDialog(Literals.Dialogs.Exception, {width: 500, height: 400, resizable: false});
  extTemplate = new Ext.Template(Lang.Exceptions.Request.Description + '<b>message:</b><br> {message}<br><br><b>stack:</b><br> {stack}');

  extDialog.body.dom.innerHTML = extTemplate.applyTemplate({message: message, stack: stack});

  if (!Context.Debugging) {
	SendMail(Lang.Exceptions.Request.Title, extDialog.body.dom.innerHTML, extDialog);
	return;
  }

  extDialog.setTitle(Lang.Exceptions.Request.Title);
  extDialog.addButton(Lang.Buttons.Close, CloseException.bind(this, extDialog));
  extDialog.addButton(Lang.Buttons.SendMail, SendMail.bind(this, Lang.Exceptions.Request.Title, extDialog.body.dom.innerHTML, extDialog));
  extDialog.center();
  extDialog.show();
};

function CloseException(extDialog) {
  if (!extDialog) return;
  if (Desktop) Desktop.hideReports();
  extDialog.hide();
  extDialog.destroy();
};


function SendMail(sSubject, sBody, extDialog) {
  if (!extDialog) return;

  CloseException(extDialog);
  Kernel.sendMail(sSubject, sBody);
};