ViewerHelperSource = new Object;
ViewerHelperSource.extLayer = null;
ViewerHelperSource.AddedTermTemplate = "<li><input id='#{id}' type='checkbox' name='addedterm' value='#{code}'><label for='#{id}'>#{label}</label></input></li>";
ViewerHelperSource.termExists = false;

ViewerHelperSource.init = function (extLayer) {
	ViewerHelperSource.extLayer = extLayer;
	ViewerHelperSource.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperSource, Lang.ViewerHelperSource);
	this.addListeners();
};

ViewerHelperSource.addListeners = function () {
	var extDialogTerm = ViewerHelperSource.extLayer.select(".dialog.term").first();
	var extDialogChildren = ViewerHelperSource.extLayer.select(".dialog.children").first();

	var extPublish = ViewerHelperSource.extLayer.select(".button.publish").first();
	extPublish.on("click", ViewerHelperSource.atPublish, ViewerHelperSource);

	var extSelectAll = ViewerHelperSource.extLayer.select("a.selectall").first();
	extSelectAll.on("click", ViewerHelperSource.atSelectAll, ViewerHelperSource);

	var extSelectNone = ViewerHelperSource.extLayer.select("a.selectnone").first();
	extSelectNone.on("click", ViewerHelperSource.atSelectNone, ViewerHelperSource);

	var extLabel = extDialogTerm.select(".field.label .component").first();
	extLabel.on("change", ViewerHelperSource.atLabelChange, ViewerHelperSource);

	var extEnable = ViewerHelperSource.extLayer.select(".button.enable").first();
	extEnable.on("click", ViewerHelperSource.atEnableTerm, ViewerHelperSource);

	var extDisable = ViewerHelperSource.extLayer.select(".button.disable").first();
	extDisable.on("click", ViewerHelperSource.atDisableTerm, ViewerHelperSource);

	var extDelete = ViewerHelperSource.extLayer.select(".button.delete").first();
	extDelete.on("click", ViewerHelperSource.atDeleteTerm, ViewerHelperSource);

	var extSuperTerm = extDialogTerm.select(".field.superterm .component").first();
	extSuperTerm.on("change", ViewerHelperSource.atSuperTermChange, ViewerHelperSource);

	var extChildCode = extDialogChildren.select(".field.childcode .component").first();
	extChildCode.on("keyup", ViewerHelperSource.atChildKeyUp, ViewerHelperSource);
	extChildCode.on("change", ViewerHelperSource.atChildCodeChange, ViewerHelperSource);

	var extChildLabel = extDialogChildren.select(".field.childlabel .component").first();
	extChildLabel.on("keyup", ViewerHelperSource.atChildKeyUp, ViewerHelperSource);

	var extTypeList = extDialogTerm.select(".field.type .component .type");
	extTypeList.each(function (extType) {
		extType.on("change", ViewerHelperSource.atTypeChange, ViewerHelperSource);
	}, this);

	var extAddTag = ViewerHelperSource.extLayer.select(".addtag").first();
	extAddTag.on("click", ViewerHelperSource.atAddTag, ViewerHelperSource);

	var extAddTerm = ViewerHelperSource.extLayer.select(".button.add").first();
	extAddTerm.on("click", ViewerHelperSource.atAddTerm, ViewerHelperSource);
};

ViewerHelperSource.getTarget = function () {
	return this.Target;
};

ViewerHelperSource.setTarget = function (Target) {
	this.Target = Target;
};

ViewerHelperSource.show = function () {
	ViewerHelperSource.extLayer.dom.style.display = "block";
};

ViewerHelperSource.hide = function () {
	ViewerHelperSource.extLayer.dom.style.display = "none";
};

ViewerHelperSource.refreshPublishDialog = function (NewTermList) {

	if (NewTermList == null) return;

	var extDialogPublish = ViewerHelperSource.extLayer.select(".dialog.publish").first();
	var extContainer = extDialogPublish.select(".container").first();
	var extButtonPublish = extDialogPublish.select(".button.publish").first();
	var AddedTermTemplate = new Template(ViewerHelperSource.AddedTermTemplate);

	extDialogPublish.dom.style.display = NewTermList.rows.length > 0 ? "block" : "none";
	extButtonPublish.dom.disabled = true;

	extContainer.dom.innerHTML = "";
	for (var i = 0; i < NewTermList.rows.length; i++) {
		var Term = NewTermList.rows[i];
		var idInput = Ext.id();
		new Insertion.Bottom(extContainer.dom, AddedTermTemplate.evaluate({id: idInput, code: Term.code, label: Term.label}));
		Ext.get(idInput).on("change", ViewerHelperSource.atNewTermChange, ViewerHelperSource);
	}
};

ViewerHelperSource.refreshTermDialog = function (Term) {
	var extDialogTerm = ViewerHelperSource.extLayer.select(".dialog.term").first();
	var extCode = extDialogTerm.select(".field.code .component").first();
	var extLabel = extDialogTerm.select(".field.label .component").first();
	var extButtonEnable = extDialogTerm.select(".button.enable").first();
	var extButtonDisable = extDialogTerm.select(".button.disable").first();
	var extButtonDelete = extDialogTerm.select(".button.delete").first();
	var extTypeList = ViewerHelperSource.extLayer.select(".field.type .component .type");
	var extSuperTerm = ViewerHelperSource.extLayer.select(".field.superterm .component").first();

	if (this.Target.Source == null || (this.Target.Source != null && !this.Target.Source.Editable) || Term == null) {
		extDialogTerm.dom.style.display = "none";
		return;
	}

	extDialogTerm.dom.style.display = "block";

	extTypeList.each(function (extType) {
		if (Term.Type == CGTerm.SUPER_TERM && extType.dom.value == CGTerm.TERM) extType.dom.checked = true;
		else if (Term.Type == extType.dom.value) extType.dom.checked = true;
		else extType.dom.checked = false;
	}, this);

	extCode.dom.value = Term.Code;
	extLabel.dom.value = Term.sLabel;
	extSuperTerm.dom.checked = (Term.Type == CGTerm.SUPER_TERM || Term.Type == CGTerm.CATEGORY);
	extSuperTerm.dom.disabled = (Term.Type == CGTerm.CATEGORY);

	if (this.Target.Source.Type == CGSource.Type.Glossary) {
		this.setFieldDisplay(extDialogTerm, "code", "none");
		this.setFieldDisplay(extDialogTerm, "label", "none");
		this.setFieldDisplay(extDialogTerm, "type", "none");
		this.setFieldDisplay(extDialogTerm, "superterm", "none");
	}
	else {
		this.setFieldDisplay(extDialogTerm, "code", "");
		this.setFieldDisplay(extDialogTerm, "label", "");
		this.setFieldDisplay(extDialogTerm, "type", "");
		this.setFieldDisplay(extDialogTerm, "superterm", "");
	}

	extButtonEnable.dom.style.display = (Term.Enable) ? "none" : "block";
	extButtonDisable.dom.style.display = (!Term.IsNew && Term.Enable) ? "block" : "none";
	extButtonDelete.dom.style.display = (Term.IsNew) ? "block" : "none";

	this.refreshTermDialogTags(Term);
};

ViewerHelperSource.setFieldDisplay = function (dialog, field, display) {
	dialog.select(".field." + field).first().dom.style.display = display;
};

ViewerHelperSource.refreshTermDialogTags = function (Term) {
	var extFieldTags = ViewerHelperSource.extLayer.select(".field.tags").first();
	var extTags = extFieldTags.select(".component ul").first();

	extTags.dom.innerHTML = "";

	if (Term != null) {
		for (var key in Term.aTags) {
			if (isFunction(Term.aTags[key])) continue;
			this.addTag(Term.aTags[key]);
		}
	}

	this.checkNoTags();
};

ViewerHelperSource.getDefaultChildCode = function (Term) {
	if (Term == null) return "";
	return Term.Code + ".";
};

ViewerHelperSource.refreshChildrenDialog = function (Term) {
	var extDialogChildren = ViewerHelperSource.extLayer.select(".dialog.children").first();
	var isThesaurus = this.Target.Source != null && this.Target.Source.Type == CGSource.Type.Thesaurus && this.Target.Source.Editable;

	extDialogChildren.dom.style.display = (isThesaurus && (Term == null || Term.Type != CGTerm.TERM)) ? "block" : "none";
};

ViewerHelperSource.refresh = function () {
	var extDialogTerm = ViewerHelperSource.extLayer.select(".dialog.term").first();
	var extDialogChildren = ViewerHelperSource.extLayer.select(".dialog.children").first();
	var extTitleTerm = extDialogTerm.select(".title").first();
	var extTitleChildren = extDialogChildren.select(".title").first();
	var extMessageList = ViewerHelperSource.extLayer.select(".message");

	if (this.Target == null) {
		ViewerHelperSource.extLayer.dom.style.display = "none";
		return;
	}

	ViewerHelperSource.extLayer.dom.style.display = "block";

	this.refreshPublishDialog(this.Target.NewTermList);
	this.refreshTermDialog(this.Target.Term);
	this.refreshChildrenDialog(this.Target.Term);

	extMessageList.each(function (extMessage) {
		extMessage.dom.style.display = "none";
	}, this);

	if (this.Target.Source == null) return;

	var sLabel = this.Target.Term != null ? this.Target.Term.sLabel : this.Target.Source.sLabel;
	extTitleTerm.dom.innerHTML = sLabel;
	extTitleChildren.dom.innerHTML = Lang.ViewerHelperSource.AddTo + sLabel;
};

ViewerHelperSource.checkNoTags = function () {
	var data = this.getTagsSerialization();
	if (data == "") {
		var extFieldTags = ViewerHelperSource.extLayer.select(".field.tags").first();
		var extList = extFieldTags.select(".component ul").first();
		extList.dom.innerHTML = Lang.ViewerHelperSource.NoTags;
		extFieldTags.addClass("empty");
	}
};

ViewerHelperSource.addTag = function (sTag) {
	var extFieldTags = ViewerHelperSource.extLayer.select(".field.tags").first();
	var extList = extFieldTags.select(".component ul").first();
	var extTemplate = extFieldTags.select(".component .template").first();

	if (extFieldTags.hasClass("empty")) {
		extList.dom.innerHTML = "";
		extFieldTags.removeClass("empty");
	}

	var extTag = Ext.get(new Insertion.Bottom(extList.dom, extTemplate.dom.innerHTML).element.immediateDescendants().last());

	var extTagName = extTag.select("input.name").first();
	extTagName.on("change", ViewerHelperSource.atTagChange, ViewerHelperSource);
	extTagName.on("keydown", ViewerHelperSource.atTagKeyDown, ViewerHelperSource);

	var extTagValue = extTag.select("input.value").first();
	extTagValue.on("change", ViewerHelperSource.atTagChange, ViewerHelperSource);
	extTagValue.on("keydown", ViewerHelperSource.atTagKeyDown, ViewerHelperSource);

	if (sTag != null) {
		aTag = sTag.split("=");
		extTagName.dom.value = aTag.length > 1 ? aTag[0] : "";
		extTagValue.dom.value = aTag.length > 1 ? aTag[1] : aTag[0];
	}

	var extDeleteTag = extTag.select(".deletetag").first();
	extDeleteTag.on("click", ViewerHelperSource.atDeleteTag.bind(this, extDeleteTag), ViewerHelperSource);

	extTagName.dom.focus();
};

ViewerHelperSource.deleteTag = function (extDeleteTag) {
	var extTag = extDeleteTag.up("li");
	extTag.dom.remove();
	this.checkNoTags();
	this.saveTermAttribute("tags", this.getTagsSerialization());
};

ViewerHelperSource.getTagsSerialization = function () {
	var extTagList = ViewerHelperSource.extLayer.select(".field.tags .component ul li");
	var tags = new Array();

	extTagList.each(function (extTag) {
		var extTagName = extTag.select("input.name").first();
		var extTagValue = extTag.select("input.value").first();
		var value = extTagName.dom.value;
		if (value != "") value += "=";
		tags.push(value + extTagValue.dom.value);
	}, this);

	return SerializerData.serializeSet(tags);
};

ViewerHelperSource.addTerm = function () {
	var extChildCode = ViewerHelperSource.extLayer.select(".field.childcode .component").first();
	var extChildLabel = ViewerHelperSource.extLayer.select(".field.childlabel .component").first();
	var Code = this.getDefaultChildCode(this.Target.Term) + extChildCode.dom.value;
	var sLabel = extChildLabel.dom.value;

	if (ViewerHelperSource.termExists) return;
	if (sLabel == "") return;

	CommandListener.throwCommand("addsourceterm(" + this.Target.Source.Id + "," + Code + "," + escape(sLabel) + ")");
	extChildCode.dom.value = "";
	extChildLabel.dom.value = "";
	extChildCode.focus();
};

ViewerHelperSource.saveTermAttribute = function (Code, sValue) {
	CommandListener.throwCommand("savesourcetermattribute(" + this.Target.Term.IdSource + "," + this.Target.Term.Code + "," + Code + "," + escape(sValue) + ")");
};

ViewerHelperSource.saveTermType = function (PreviousType, Type) {
	CommandListener.throwCommand("savesourcetermtype(" + this.Target.Term.IdSource + "," + this.Target.Term.Code + "," + PreviousType + "," + Type + ")");
	this.Target.Term.Type = Type;
};

ViewerHelperSource.deleteTerm = function () {
	CommandListener.throwCommand("deletesourceterm(" + this.Target.Term.IdSource + "," + this.Target.Term.Code + ")");
};

ViewerHelperSource.checkExistsTerm = function (Code) {
	CommandListener.throwCommand("checkexistssourceterm(" + this.Target.Source.Id + "," + Code + ")");
};

ViewerHelperSource.publish = function (Code, sValue) {
	var extContainer = ViewerHelperSource.extLayer.select(".dialog.publish .container").first();
	var extInputList = extContainer.select("input");
	var terms = "";

	extInputList.each(function (extInput) {
		if (extInput.dom.checked) terms += extInput.dom.value + ",";
	}, this);
	if (terms.length > 0) terms = terms.substring(0, terms.length - 1);

	CommandListener.throwCommand("publishsourceterms(" + this.Target.Source.Id + "," + escape(terms) + ")");
};

ViewerHelperSource.showTermExists = function () {
	var extMessageExists = ViewerHelperSource.extLayer.select(".message.term.exists").first();
	extMessageExists.dom.style.display = "inline";
	ViewerHelperSource.termExists = true;
};

ViewerHelperSource.hideTermExists = function () {
	var extMessageExists = ViewerHelperSource.extLayer.select(".message.term.exists").first();
	extMessageExists.dom.style.display = "none";
	ViewerHelperSource.termExists = false;
};

//#############################################################################################################

ViewerHelperSource.atAddTag = function () {
	this.addTag();
};

ViewerHelperSource.atDeleteTag = function (extTag) {
	this.deleteTag(extTag);
};

ViewerHelperSource.atTagChange = function (extInput) {
	this.saveTermAttribute("tags", this.getTagsSerialization());
};

ViewerHelperSource.atTagKeyDown = function (oEvent, DOMInput) {
	var extInput = Ext.get(DOMInput);
	var codeKey = oEvent.getKey();

	if (codeKey != oEvent.TAB) return;
	if (oEvent.shiftKey) return;
	if (DOMInput.value == "") return;

	if (extInput.hasClass("value")) {
		var extTag = extInput.up("li");
		if (extTag.getNextSibling() == null) {
			this.addTag();
			return false;
		}
	}

};

ViewerHelperSource.atAddTerm = function () {
	this.addTerm();
};

ViewerHelperSource.atLabelChange = function () {
	var sLabel = ViewerHelperSource.extLayer.select(".field.label .component").first().dom.value;
	this.saveTermAttribute("label", sLabel);
};

ViewerHelperSource.atTypeChange = function () {
	var extTypeList = ViewerHelperSource.extLayer.select(".field.type .component .type");
	var extDialogChildren = ViewerHelperSource.extLayer.select(".dialog.children").first();
	var extSuperTerm = ViewerHelperSource.extLayer.select(".field.superterm .component").first();
	var Type = "";

	extTypeList.each(function (extType) {
		if (extType.dom.checked) Type = extType.dom.value;
	}, this);

	extSuperTerm.dom.checked = (Type == 2);
	extSuperTerm.dom.disabled = (Type == 2);

	extDialogChildren.dom.style.display = (Type != CGTerm.TERM) ? "block" : "none";
	this.saveTermType(this.Target.Term.Type, Type);
};

ViewerHelperSource.atSuperTermChange = function () {
	var extDialogChildren = ViewerHelperSource.extLayer.select(".dialog.children").first();
	var Checked = ViewerHelperSource.extLayer.select(".field.superterm .component").first().dom.checked;

	if (Checked) this.saveTermType(this.Target.Term.Type, CGTerm.SUPER_TERM);
	else this.saveTermType(this.Target.Term.Type, CGTerm.TERM);

	extDialogChildren.dom.style.display = (Checked) ? "block" : "none";
};

ViewerHelperSource.atChildKeyUp = function (oEvent) {
	var codeKey = oEvent.getKey();
	if (codeKey == oEvent.ENTER) this.addTerm();
	return false;
};

ViewerHelperSource.atChildCodeChange = function () {
	var Code = ViewerHelperSource.extLayer.select(".field.childcode .component").first().dom.value;
	this.hideTermExists();
	this.checkExistsTerm(Code);
};

ViewerHelperSource.atPublish = function () {
	this.publish();
};

ViewerHelperSource.atEnableTerm = function () {
	this.saveTermAttribute("enable", true);
};

ViewerHelperSource.atDisableTerm = function () {
	this.saveTermAttribute("enable", false);
};

ViewerHelperSource.atDeleteTerm = function () {
	this.deleteTerm();
};

ViewerHelperSource.atNewTermChange = function () {
	var extDialogPublish = ViewerHelperSource.extLayer.select(".dialog.publish").first();
	var extNewTermList = extDialogPublish.select(".container input");
	var extButtonPublish = extDialogPublish.select(".button.publish").first();
	var disabled = true;

	for (var i = 0; i < extNewTermList.elements.length; i++) {
		var DOMNewTerm = extNewTermList.elements[i];
		if (DOMNewTerm.checked) {
			disabled = false;
			break;
		}
	}

	extButtonPublish.dom.disabled = disabled;
};

ViewerHelperSource.atSelectAll = function () {
	var extDialogPublish = ViewerHelperSource.extLayer.select(".dialog.publish").first();
	var extNewTermList = extDialogPublish.select(".container input");
	var extButtonPublish = extDialogPublish.select(".button.publish").first();

	for (var i = 0; i < extNewTermList.elements.length; i++) {
		var DOMNewTerm = extNewTermList.elements[i];
		DOMNewTerm.checked = true;
	}

	extButtonPublish.dom.disabled = false;
};

ViewerHelperSource.atSelectNone = function () {
	var extDialogPublish = ViewerHelperSource.extLayer.select(".dialog.publish").first();
	var extNewTermList = extDialogPublish.select(".container input");
	var extButtonPublish = extDialogPublish.select(".button.publish").first();

	for (var i = 0; i < extNewTermList.elements.length; i++) {
		var DOMNewTerm = extNewTermList.elements[i];
		DOMNewTerm.checked = false;
	}

	extButtonPublish.dom.disabled = true;
};
