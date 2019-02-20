Lang.Editor = {
  MoreInfo: "[More information]",
  Option: "Value",
  Code: "Code",
  Empty: "No options",
  ShowHistory: "Show recent values",
  HideHistory: "Hide recent values",
  FilterEmpty: "Insert here the search terms",
  FilterEmptyHistory: "Recent values. Insert here the terms for searching",
  Check: "Check",
  Uncheck: "Uncheck",
  Up: "Up",
  Down: "Down",

  Dialogs: {
    Other: {
      Title: "Add...",
      Description: "Please, show the new value:"
    },
    FileUpload: {
      Uploading: "Uploading the file.Please, wait...",
      UploadingFailed: "It could not upload the file"
    },
    PictureUpload: {
      Processing: "Recognizing the image. Please, wait...",
      Uploading: "Uploading the image file. Please, wait...",
      UploadingFailed: "It could not upload the image file"
    },
    Number: {
      Accepts: "Accept ",
      Decimals: " decimals",
      MultipleDecimals: "Accept as many decimals as you want",
      Increments: "It will increase and decrease in #count#",
      Range: "Accept values between #min# and #max#",
      Equivalences: "Value in other unities: #equivalences#"
    },
    Text: {
      Length: "Accept values with a length between #min# and #max#",
      Undefined: "the maxim length that it would be required"
    },
    CheckReload: {
      Reloading: "Reloading. Please, wait...",
      ReloadingFailed: "Could not obtain term list of selected source"
    }
  },

  Templates: {
    DialogListItem: '<li class="element"><table width="100%"><tr><td width="1px"><input name="#{id}" style="margin-top:4px;*margin-top:2px;" type="checkbox"/></td><td><label for="#{id}" style="cursor:pointer;display:block;padding:4px;*padding-top:5px;">#{title}</div></td><td width="35px"><div class="elementoptions"><a class="delete"><img src=#{ImagesPath}/s.gif?a=1" class="trigger" alt="eliminar" title="eliminar"/></a><a class="move"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Hold on the left mouse-button to drag and drop" title="Hold on the left mouse-button to drag and drop"/></a></div></td></tr></table></li>'
  }
};
