Lang.Editor = {

  MoreInfo: "[Más información]",
  Option: "Valor",
  Code: "Código",
  Empty: "No existen opciones",
  ShowHistory: "Ver valores recientes",
  HideHistory: "Ocultar valores recientes",
  FilterEmpty: "Introduzca aquí los términos para la búsqueda",
  FilterEmptyHistory: "Valores recientes. Introduzca aquí los términos para la búsqueda",
  Check: "Marcar",
  Uncheck: "Desmarcar",
  Up: "Subir",
  Down: "Bajar",

  Dialogs: {
    Other: {
      Title: "Añadir...",
      Description: "Por favor, indique el nuevo valor:"
    },
    FileUpload: {
      Uploading: "Subiendo el fichero. Por favor, espere...",
      UploadingFailed: "No se ha podido subir el fichero"
    },
    PictureUpload: {
      Processing: "Reconociendo la imagen. Por favor, espere...",
      Uploading: "Subiendo el archivo de imagen. Por favor, espere...",
      UploadingFailed: "No se ha podido subir el archivo de imagen"
    },
    Number: {
      Accepts: "Acepta ",
      Decimals: " decimales",
      MultipleDecimals: "Acepta tantos decimales como quiera",
      Increments: "Aumentará y disminuirá en #count#",
      Range: "Acepta valores entre #min# y #max#",
      Equivalences: "Valor en otras unidades: #equivalences#"
    },
    Text: {
      Length: "Acepta valores con una longitud entre #min# y #max#",
      Undefined: "la longitud máxima que se quiera"
    },
    CheckReload: {
      Reloading: "Actualizando. Por favor, espere...",
      ReloadingFailed: "No se ha podido obtener la lista de términos para la fuente indicada"
    }
  },

  Templates: {
    DialogListItem: '<li class="element"><table width="100%"><tr><td width="1px"><input name="#{id}" style="margin-top:4px;*margin-top:2px;" type="checkbox"/></td><td><label for="#{id}" style="cursor:pointer;display:block;padding:4px;*padding-top:5px;">#{title}</div></td><td width="35px"><div class="elementoptions"><a class="delete"><img src=#{ImagesPath}/s.gif?a=1" class="trigger" alt="eliminar" title="eliminar"/></a><a class="move"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"/></a></div></td></tr></table></li>'
  }
};
