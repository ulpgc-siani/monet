function InputFiles (eFrame, eList, iMaxFiles, FileNameLength, FileNameCellWidth) {
  this.eList = eList;
  this.eFrame = eFrame;
  this.id = 0;
  if(iMaxFiles > 0)
    this.iMaxFiles = iMaxFiles;
  this.eCurrentInput = null;
  this.iFileNameLength = FileNameLength;
  this.iFileNameCellWidth = FileNameCellWidth;
  this.Init(eFrame);
}

//---------------------------------------------------------------------

InputFiles.prototype.Init = function(eFrame) {
  var eDocument;
  var name = eFrame.name;

  if (eFrame.contentDocument) eDocument = eFrame.contentDocument;
  else if (eFrame.contentWindow) eDocument = eFrame.contentWindow.document;
  else if (window.frames[name]) eDocument = window.frames[name].document;
  
  eDocument.open();
  eDocument.write("<style>body {margin: 3px;}</style>");
  eDocument.close();

  var aBodys = eDocument.getElementsByTagName("BODY");
  aBodys[0].innerHTML = "<form enctype='multipart/form-data' method='post'><input type='text'></form>";

  this.eDocument = eDocument;
  this.eForm = eDocument.forms[0];
  this.eForm.eDocument = eDocument;
  this.eForm.elements[0].style.display = "none";  //IE bug
  
  this.eList.style.display = (this.eList.rows > 0)?"block":"none";

  this.NewInput();
};

//---------------------------------------------------------------------

InputFiles.prototype.NewInput = function() {
  if (this.iMaxFiles && this.eForm.elements.length > this.iMaxFiles) return;

  var eInput = this.eDocument.createElement('input');
  eInput.type = 'file';
  eInput.name = eInput.id = 'file' + this.id++;
  eInput.Handler = this;
  eInput.style.width = "100%";
  eInput.onchange = function() {
    this.Handler.AddList(this);
    this.Handler.NewInput();
    this.style.display = "none";
  };

  this.eForm.insertBefore(eInput, this.eForm.firstChild);

  this.eCurrentInput = eInput;
};

//---------------------------------------------------------------------

InputFiles.prototype.AddList = function (eInput) {
  var eRow = this.eList.insertRow(this.eList.rows.length);

  var eCellFileIcon = eRow.insertCell(0);
  eCellFileIcon.className = GetFileClass(eInput.value);

  var eCellFileName = eRow.insertCell(1);
  eCellFileName.style.width = this.iFileNameCellWidth;
  eCellFileName.innerHTML = this.TruncateFileName(eInput.value);

  var eCellFileDel = eRow.insertCell(2);
  var eRowButton = document.createElement ('a');
  eRowButton.innerHTML = "<img src=\"" + Context.Config.ImagesPath + "/dialog/delete.gif\">";
  eRowButton.style.cursor = "pointer";
  eRowButton.Handler = this;
  eRowButton.onclick= function() { this.Handler.RemoveFromList(this); };
  eCellFileDel.appendChild(eRowButton);
  eCellFileDel.className = "DELFILE";

  this.eList.style.display = (this.eList.rows.length > 0)?"block":"none";

  eRow.eInput = eInput;
};

//---------------------------------------------------------------------

InputFiles.prototype.AddToList = function(sValue) {
  var eInput = this.eDocument.createElement('input');
  eInput.type = 'text';
  eInput.name = eInput.id = 'file' + this.id++;
  eInput.style.width = "100%";
  eInput.style.display = "none";
  eInput.value = sValue;

  this.eForm.insertBefore(eInput, this.eForm.firstChild); 

  if (this.iMaxFiles && this.eForm.elements.length > this.iMaxFiles) {
    this.eCurrentInput.style.display = "none"; //Ocultamos, ya no se permiten más
  }

  this.AddList(eInput);

  return eInput;
};

//---------------------------------------------------------------------

InputFiles.prototype.RemoveAll = function() {
  var length = this.eList.rows.length;
  for(var i=0;i<length;i++) {
	//this.RemoveFromList(this.eList.rows[0]);
	var eRow = this.eList.rows[0];
	eRow.eInput.parentNode.removeChild(eRow.eInput);
	eRow.parentNode.removeChild (eRow);
	delete this.eForm[eRow.eInput.name];

	if (this.eForm.elements.length <= this.iMaxFiles) this.NewInput();

	this.eCurrentInput.style.display = "block"; //Al quitar un elemento, se puede volver a mostrar

  this.eList.style.display = "none";

	return false;
  }
};

//---------------------------------------------------------------------

InputFiles.prototype.RemoveFromList = function(eRow) {
  eRow.parentNode.parentNode.eInput.parentNode.removeChild(eRow.parentNode.parentNode.eInput);
  eRow.parentNode.parentNode.parentNode.removeChild (eRow.parentNode.parentNode);
  delete this.eForm[eRow.parentNode.parentNode.eInput.name];

  if (this.eForm.elements.length <= this.iMaxFiles) this.NewInput();

  this.eCurrentInput.style.display = "block"; //Al quitar un elemento, se puede volver a mostrar

  this.eList.style.display = (this.eList.rows.length > 0)?"block":"none";

  return false;
};

//---------------------------------------------------------------------

InputFiles.prototype.TruncateFileName = function(string) {
  var myString = string;
  var outString;
  if(myString.length >= this.iFileNameLength) {
    outString = myString.substr(0,6);
    outString += '...';
    var tmpString = myString.substr(myString.length - (this.iFileNameLength-4));
    if(tmpString.indexOf("\\") > 0)
      outString += tmpString.substr(tmpString.indexOf("\\"));
    else {          //No hay carpetas en medio.
      splitString = myString.split("\\");
      tmpString = splitString[splitString.length-1];
      if(tmpString.length > (this.iFileNameLength-11)) {
        splitString = tmpString.split(".");

        extensionString = splitString[splitString.length - 1];
        tmpString = tmpString.substr(0,(this.iFileNameLength-22) - extensionString.length) + "..." + tmpString.substr(tmpString.indexOf("." + extensionString) - 4);
      }
      outString += "\\" + tmpString;
    }
  }
  else {
    outString = myString;
  }

  return outString;
};

InputFiles.prototype.GetCount = function() {
  var iResult = 0;
  
  for (var iPos=0; iPos<this.eForm.elements.length; iPos++) {
    if (this.eForm.elements[iPos].value != "") iResult++;
  }
  
  return iResult;
};