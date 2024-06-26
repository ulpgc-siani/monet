/************************************************************************************************************
 (C) www.dhtmlgoodies.com, April 2006

 Update log:
 August, 9th, 2006: Added a fix to the preserve aspect ratio feature ( Thanks to Urko Benito)
 August, 11th, 2006: If fixed ratio is set, resize crop area initially

 This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.

 Terms of use:
 You are free to use this script as long as the copyright message is kept intact. However, you may not
 redistribute, sell or repost it without our permission.

 Thank you!

 www.dhtmlgoodies.com
 Alf Magne Kalleland

 ************************************************************************************************************/

var ImageCropLayer = null;

var cropToolBorderWidth = 1;	// Width of dotted border around crop rectangle
var smallSquareWidth = 7;	// Size of small squares used to resize crop rectangle

var crop_minimumPercent = 10;	// Minimum percent - resize
var crop_maximumPercent = 200;	// Maximum percent -resize

var crop_minimumWidthHeight = 15;	// Minimum width and height of crop area

var updateFormValuesAsYouDrag = true;	// This variable indicates if form values should be updated as we drag. This process could make the script work a little bit slow. That's why this option is set as a variable.
if (!document.all) updateFormValuesAsYouDrag = false;	// Enable this feature only in IE

var crop_script_alwaysPreserveAspectRatio = false;
var crop_script_fixedRatio = false;	 // Fixed aspect ratio(example of value: 1.5). Width of cropping area relative to height(1.5 means that the width is 150% of the height)
// Set this variable to false if you don't want a fixed aspect ratio

var crop_script_browserIsOpera = navigator.userAgent.indexOf('Opera') >= 0 ? true : false;
var cropDiv_left = false;
var cropDiv_top = false;
var cropDiv_right = false;
var cropDiv_bottom = false;
var cropDiv_dotted = false;

var crop_currentResizeType = false;

var cropEvent_posX;
var cropEvent_posY;

var cropEvent_eventX;
var cropEvent_eventY;
var crop_resizeCounter = -1;
var crop_moveCounter = -1;

var crop_imageDiv = false;
var imageDiv_currentWidth = false;
var imageDiv_currentHeight = false;
var imageDiv_currentLeft = false;
var imageDiv_currentTop = false;

var smallSquare_tl;
var smallSquare_tc;
var smallSquare_tr;
var smallSquare_lc;
var smallSquare_rc;
var smallSquare_bl;
var smallSquare_bc;
var smallSquare_br;

var offsetSmallSquares = Math.floor(smallSquareWidth / 2);

var cropScriptAjaxObjects = new Array();
var preserveAspectRatio = false;
var cropWidthRatio = false;	// width of cropping area relative to height

function crop_getElementsByClassName(DOMElement, TagName, sClassName) {
  if (DOMElement.getElementsByClassName) return DOMElement.getElementsByClassName(sClassName);
  else {
    aResult = new Array();
    aDOMElements = DOMElement.getElementsByTagName(TagName);
    for (var iPos = 0; iPos < aDOMElements.length; iPos++) {
      DOMElement = aDOMElements[iPos];
      if (DOMElement.className.indexOf(sClassName) != -1) {
        aResult.push(DOMElement);
      }
    }
    return aResult;
  }
}

function crop_createDivElements() {
  crop_imageDiv = crop_getElementsByClassName(ImageCropLayer, "div", "imageContainer")[0];

  cropDiv_left = document.createElement('DIV');
  cropDiv_left.className = 'crop_transparentDiv';
  cropDiv_left.style.visibility = 'visible';
  cropDiv_left.style.left = '0px';
  cropDiv_left.style.top = '0px';
  cropDiv_left.style.height = crop_imageHeight + 'px';
  cropDiv_left.style.width = '0px';
  cropDiv_left.innerHTML = '<span></span>';
  crop_imageDiv.appendChild(cropDiv_left);

  cropDiv_top = document.createElement('DIV');
  cropDiv_top.className = 'crop_transparentDiv';
  cropDiv_top.style.visibility = 'visible';
  cropDiv_top.style.left = '0px';
  cropDiv_top.style.top = '0px';
  cropDiv_top.style.height = '0px';
  cropDiv_top.style.width = crop_imageWidth + 'px';
  cropDiv_top.innerHTML = '<span></span>';
  crop_imageDiv.appendChild(cropDiv_top);

  cropDiv_right = document.createElement('DIV');
  cropDiv_right.className = 'crop_transparentDiv';
  cropDiv_right.style.visibility = 'visible';
  cropDiv_right.style.left = (crop_imageWidth) + 'px';
  cropDiv_right.style.top = '0px';
  cropDiv_right.style.height = crop_imageHeight + 'px';
  cropDiv_right.style.width = '0px';
  cropDiv_right.innerHTML = '<span></span>';
  crop_imageDiv.appendChild(cropDiv_right);

  cropDiv_bottom = document.createElement('DIV');
  cropDiv_bottom.className = 'crop_transparentDiv';
  cropDiv_bottom.style.visibility = 'visible';
  cropDiv_bottom.style.left = '0px';
  cropDiv_bottom.style.top = (crop_imageHeight) + 'px';
  cropDiv_bottom.style.height = '0px';
  cropDiv_bottom.style.width = crop_imageWidth + 'px';
  cropDiv_bottom.innerHTML = '<span></span>';
  crop_imageDiv.appendChild(cropDiv_bottom);

  cropDiv_dotted = document.createElement('DIV');
  cropDiv_dotted.className = 'crop_dottedDiv';
  cropDiv_dotted.style.left = '0px';
  cropDiv_dotted.style.top = '0px';
  cropDiv_dotted.style.width = (crop_imageWidth - (cropToolBorderWidth * 2)) + 'px';
  cropDiv_dotted.style.height = (crop_imageHeight - (cropToolBorderWidth * 2)) + 'px';
  cropDiv_dotted.innerHTML = '<div></div>';
  cropDiv_dotted.style.cursor = 'move';

  if (crop_script_browserIsOpera) {
    var div = cropDiv_dotted.getElementsByTagName('DIV')[0];
    div.style.backgroundColor = 'transparent';
    cropDiv_bottom.style.backgroundColor = 'transparent';
    cropDiv_right.style.backgroundColor = 'transparent';
    cropDiv_top.style.backgroundColor = 'transparent';
    cropDiv_left.style.backgroundColor = 'transparent';

  }

  cropDiv_dotted.onmousedown = cropScript_initMove;

  smallSquare_tl = document.createElement('IMG');
  smallSquare_tl.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_tl.style.position = 'absolute';
  smallSquare_tl.style.left = (-offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tl.style.top = (-offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tl.style.cursor = 'nw-resize';
  smallSquare_tl.id = 'nw-resize';
  smallSquare_tl.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_tl);

  smallSquare_tr = document.createElement('IMG');
  smallSquare_tr.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_tr.style.position = 'absolute';
  smallSquare_tr.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tr.style.top = (-offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tr.style.cursor = 'ne-resize';
  smallSquare_tr.id = 'ne-resize';
  smallSquare_tr.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_tr);

  smallSquare_bl = document.createElement('IMG');
  smallSquare_bl.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_bl.style.position = 'absolute';
  smallSquare_bl.style.left = (-offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bl.style.top = (crop_imageHeight - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bl.style.cursor = 'sw-resize';
  smallSquare_bl.id = 'sw-resize';
  smallSquare_bl.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_bl);

  smallSquare_br = document.createElement('IMG');
  smallSquare_br.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_br.style.position = 'absolute';
  smallSquare_br.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_br.style.top = (crop_imageHeight - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_br.style.cursor = 'se-resize';
  smallSquare_br.id = 'se-resize';
  smallSquare_br.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_br);

  smallSquare_tc = document.createElement('IMG');
  smallSquare_tc.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_tc.style.position = 'absolute';
  smallSquare_tc.style.left = (Math.floor(crop_imageWidth / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tc.style.top = (-offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tc.style.cursor = 's-resize';
  smallSquare_tc.id = 'n-resize';
  smallSquare_tc.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_tc);

  smallSquare_bc = document.createElement('IMG');
  smallSquare_bc.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_bc.style.position = 'absolute';
  smallSquare_bc.style.left = (Math.floor(crop_imageWidth / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bc.style.top = (crop_imageHeight - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bc.style.cursor = 's-resize';
  smallSquare_bc.id = 's-resize';
  smallSquare_bc.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_bc);

  smallSquare_lc = document.createElement('IMG');
  smallSquare_lc.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_lc.style.position = 'absolute';
  smallSquare_lc.style.left = (-offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_lc.style.top = (Math.floor(crop_imageHeight / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_lc.style.cursor = 'e-resize';
  smallSquare_lc.id = 'w-resize';
  smallSquare_lc.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_lc);

  smallSquare_rc = document.createElement('IMG');
  smallSquare_rc.src = Context.Config.ImagesPath + '/icons/small_square.gif';
  smallSquare_rc.style.position = 'absolute';
  smallSquare_rc.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_rc.style.top = (Math.floor(crop_imageHeight / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_rc.style.cursor = 'e-resize';
  smallSquare_rc.id = 'e-resize';
  smallSquare_rc.onmousedown = cropScript_initResize;
  cropDiv_dotted.appendChild(smallSquare_rc);

  crop_imageDiv.appendChild(cropDiv_dotted);
}

function cropScript_initMove(e) {
  if (document.all)e = event;

  if (e.target) source = e.target;
  else if (e.srcElement) source = e.srcElement;
  if (source.nodeType == 3) // defeat Safari bug
    source = source.parentNode;

  if (source.id && source.id.indexOf('resize') >= 0)return;

  imageDiv_currentLeft = cropDiv_dotted.style.left.replace('px', '') / 1;
  imageDiv_currentTop = cropDiv_dotted.style.top.replace('px', '') / 1;
  imageDiv_currentWidth = cropDiv_dotted.style.width.replace('px', '') / 1;
  imageDiv_currentHeight = cropDiv_dotted.style.height.replace('px', '') / 1;
  cropEvent_eventX = e.clientX;
  cropEvent_eventY = e.clientY;

  crop_moveCounter = 0;
  cropScript_timerMove();
  return false;
}

function cropScript_timerMove() {
  if (crop_moveCounter >= 0 && crop_moveCounter < 10) {
    crop_moveCounter++;
    setTimeout('cropScript_timerMove()', 1);
    return;
  }

}

function cropScript_initResize(e) {
  if (document.all)e = event;

  cropDiv_dotted.style.cursor = 'default';
  crop_currentResizeType = this.id;

  cropEvent_eventX = e.clientX;
  cropEvent_eventY = e.clientY;
  crop_resizeCounter = 0;
  imageDiv_currentWidth = cropDiv_dotted.style.width.replace('px', '') / 1;
  imageDiv_currentHeight = cropDiv_dotted.style.height.replace('px', '') / 1;
  imageDiv_currentLeft = cropDiv_dotted.style.left.replace('px', '') / 1;
  imageDiv_currentTop = cropDiv_dotted.style.top.replace('px', '') / 1;


  cropWidthRatio = cropDiv_dotted.offsetWidth / cropDiv_dotted.offsetHeight;
  if (crop_script_fixedRatio)cropWidthRatio = crop_script_fixedRatio;

  if (document.all) {
    var div = cropDiv_dotted.getElementsByTagName('DIV')[0];
    div.style.display = 'none';
  }

  cropScript_timerResize();
  return false;

}

function cropScript_timerResize() {
  if (crop_resizeCounter >= 0 && crop_resizeCounter < 10) {
    crop_resizeCounter = crop_resizeCounter + 1;
    setTimeout('cropScript_timerResize()', 1);
    return;
  }


}

function crop_cancelEvent(e) {
  if (document.all)e = event;
  if (e.target) source = e.target;
  else if (e.srcElement) source = e.srcElement;
  if (source.nodeType == 3) // defeat Safari bug
    source = source.parentNode;

  if (source.tagName && source.tagName.toLowerCase() == 'input')return true;
  return false;
}

var mouseMoveEventInProgress = false;
function getAdjustedCoordinates(coordinates, currentCoordinates, aspectRatio, currentResize) {
  currentResize = currentResize.replace('-resize', '');


  var minWidth = aspectRatio ? crop_minimumWidthHeight * aspectRatio : crop_minimumWidthHeight;
  var minHeight = aspectRatio ? crop_minimumWidthHeight / aspectRatio : crop_minimumWidthHeight;

  if (coordinates.left + coordinates.width + 2 > crop_imageWidth) {
    coordinates.width = crop_imageWidth - coordinates.left - 2;
  }
  if (coordinates.top + coordinates.height + 2 > crop_imageHeight) {
    coordinates.height = crop_imageHeight - coordinates.top - 2;
  }

  if (coordinates.height < minHeight) {
    coordinates.height = currentCoordinates.height;
    coordinates.top = currentCoordinates.top;
  }

  if (coordinates.width < minWidth) {
    coordinates.width = currentCoordinates.width;
    coordinates.left = currentCoordinates.left;
  }


  if (aspectRatio) {


    var currentRatio = coordinates.width / coordinates.height;

    switch (currentResize) {
      case 'n':
        // Height is being resized - set new left coordinate
        var newWidth = Math.round(coordinates.height * aspectRatio);
        coordinates.left += (coordinates.width - newWidth);
        coordinates.width = newWidth;
        break;
      case 'w':
      case 'nw':
      case 'ne':
        // Width is being resized - Set new top coordinate
        var newHeight = Math.round(coordinates.width / aspectRatio);
        coordinates.top += (coordinates.height - newHeight);
        coordinates.height = newHeight;
        break;
      case 'e':
      case 'se':
        coordinates.height = Math.round(coordinates.width / aspectRatio);
        break;
      case 's':
        coordinates.width = Math.round(coordinates.height * aspectRatio);
        break;
      default:
    }

    if (coordinates.left < 0) {

      coordinates.width += coordinates.left;
      coordinates.height = coordinates.width / aspectRatio;
      coordinates.left = 0;
    }
    if (coordinates.top < 0) {
      var origWidth = coordinates.width;
      coordinates.height += coordinates.top;
      coordinates.width = coordinates.height * aspectRatio;
      coordinates.top = 0;
      if (currentResize == 'nw') {
        coordinates.left += (origWidth - coordinates.width);
      }
    }
    if (coordinates.width < minWidth) {
      coordinates.width = minWidth;
      coordinates.height = coordinates.width / aspectRatio;
    }
    if (coordinates.height < minHeight) {
      coordinates.height = minHeight;
      coordinates.width = coordinates.height * aspectRatio;
    }
    if (coordinates.left + coordinates.width + 2 > crop_imageWidth) {
      coordinates.width = crop_imageWidth - coordinates.left - 2;
      coordinates.height = Math.round(coordinates.width / aspectRatio);
    }
    if (coordinates.top + coordinates.height + 2 > crop_imageHeight) {
      coordinates.height = crop_imageHeight - coordinates.top - 2;
      coordinates.width = Math.round(coordinates.height * aspectRatio);
    }


  }
  if (coordinates.height < minHeight) {
    coordinates.height = currentCoordinates.height;
    coordinates.top = currentCoordinates.top;
  }

  if (coordinates.width < minWidth) {
    coordinates.width = currentCoordinates.width;
    coordinates.left = currentCoordinates.left;
  }
  return coordinates;

}

function cropScript_mouseMove(e) {
  if (crop_moveCounter < 10 && crop_resizeCounter < 10)return;
  if (mouseMoveEventInProgress)return;
  if (document.all)mouseMoveEventInProgress = true;
  if (document.all)e = event;

  if (crop_resizeCounter == 10) {
    var cropStyleObj = cropDiv_dotted.style;
    if (e.ctrlKey || crop_script_alwaysPreserveAspectRatio)preserveAspectRatio = true; else preserveAspectRatio = false;

    var currentCoordinates = {
      left: cropStyleObj.left.replace('px', '') / 1,
      top: cropStyleObj.top.replace('px', '') / 1,
      width: cropDiv_dotted.clientWidth,
      height: cropDiv_dotted.clientHeight
    };
    // crop_imageHeight = max y
    // crop_imageWidth = max x
    var newCoordinates = {};
    newCoordinates.left = currentCoordinates.left;
    newCoordinates.top = currentCoordinates.top;
    newCoordinates.width = currentCoordinates.width;
    newCoordinates.height = currentCoordinates.height;

    if (crop_currentResizeType == 'e-resize' || crop_currentResizeType == 'ne-resize' || crop_currentResizeType == 'se-resize') {  /* East resize */
      newCoordinates.width = Math.max(crop_minimumWidthHeight, (imageDiv_currentWidth + e.clientX - cropEvent_eventX));
    }
    if (crop_currentResizeType == 's-resize' || crop_currentResizeType == 'sw-resize' || crop_currentResizeType == 'se-resize') {
      newCoordinates.height = Math.max(crop_minimumWidthHeight, (imageDiv_currentHeight + e.clientY - cropEvent_eventY));
    }

    if (crop_currentResizeType == 'n-resize' || crop_currentResizeType == 'nw-resize' || crop_currentResizeType == 'ne-resize') {
      var newTop = Math.max(0, (imageDiv_currentTop + e.clientY - cropEvent_eventY));
      newCoordinates.height += (currentCoordinates.top - newTop);
      newCoordinates.top = newTop;
    }

    if (crop_currentResizeType == 'w-resize' || crop_currentResizeType == 'sw-resize' || crop_currentResizeType == 'nw-resize') {
      var newLeft = Math.max(0, (imageDiv_currentLeft + e.clientX - cropEvent_eventX));
      newCoordinates.width += (currentCoordinates.left - newLeft);
      newCoordinates.left = newLeft;
    }


    if (newCoordinates && (newCoordinates.left || newCoordinates.top || newCoordinates.width || newCoordinates.height)) {
      newCoordinates = getAdjustedCoordinates(newCoordinates, currentCoordinates, preserveAspectRatio ? cropWidthRatio : false, crop_currentResizeType);

    }
    if (newCoordinates) {
      cropStyleObj.left = newCoordinates.left + 'px';
      cropStyleObj.top = newCoordinates.top + 'px';
      cropStyleObj.width = newCoordinates.width + 'px';
      cropStyleObj.height = newCoordinates.height + 'px';

    }


    if (!crop_script_fixedRatio && !e.ctrlKey)cropWidthRatio = cropDiv_dotted.offsetWidth / cropDiv_dotted.offsetHeight;


  }

  if (crop_moveCounter == 10) {
    var tmpLeft = imageDiv_currentLeft + e.clientX - cropEvent_eventX;
    if (tmpLeft < 0)tmpLeft = 0;
    if ((tmpLeft + imageDiv_currentWidth + (cropToolBorderWidth * 2)) > crop_imageWidth)tmpLeft = crop_imageWidth - imageDiv_currentWidth - (cropToolBorderWidth * 2);
    cropDiv_dotted.style.left = tmpLeft + 'px';
    var tmpTop = imageDiv_currentTop + e.clientY - cropEvent_eventY;
    if (tmpTop < 0)tmpTop = 0;
    if ((tmpTop + imageDiv_currentHeight + (cropToolBorderWidth * 2)) > crop_imageHeight)tmpTop = crop_imageHeight - imageDiv_currentHeight - (cropToolBorderWidth * 2);
    cropDiv_dotted.style.top = tmpTop + 'px';

  }

  repositionSmallSquares();
  resizeTransparentSquares();
  if (updateFormValuesAsYouDrag)cropScript_updateFormValues();
  mouseMoveEventInProgress = false;
}

function repositionSmallSquares() {
  smallSquare_tc.style.left = (Math.floor((cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2)) / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bc.style.left = (Math.floor((cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2)) / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tr.style.left = (cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_rc.style.left = (cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_br.style.left = (cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';

  smallSquare_br.style.top = (cropDiv_dotted.style.height.replace('px', '') / 1 + (cropToolBorderWidth * 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bc.style.top = (cropDiv_dotted.style.height.replace('px', '') / 1 + (cropToolBorderWidth * 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bl.style.top = (cropDiv_dotted.style.height.replace('px', '') / 1 + (cropToolBorderWidth * 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_lc.style.top = (Math.floor((cropDiv_dotted.style.height.replace('px', '') / 1 + cropToolBorderWidth) / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_rc.style.top = (Math.floor((cropDiv_dotted.style.height.replace('px', '') / 1 + cropToolBorderWidth) / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
}

function resizeTransparentSquares() {
  cropDiv_left.style.width = cropDiv_dotted.style.left;
  cropDiv_right.style.width = Math.max(0, crop_imageWidth - (cropToolBorderWidth * 2) - (cropDiv_dotted.style.width.replace('px', '') / 1 + cropDiv_dotted.style.left.replace('px', '') / 1)) + 'px';
  cropDiv_right.style.left = (cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2) + cropDiv_dotted.style.left.replace('px', '') / 1) + 'px';
  cropDiv_bottom.style.height = Math.max(0, crop_imageHeight - (cropToolBorderWidth * 2) - (cropDiv_dotted.style.height.replace('px', '') / 1 + cropDiv_dotted.style.top.replace('px', '') / 1)) + 'px';
  cropDiv_bottom.style.top = (cropDiv_dotted.style.height.replace('px', '') / 1 + (cropToolBorderWidth * 2) + cropDiv_dotted.style.top.replace('px', '') / 1) + 'px';

  cropDiv_top.style.height = cropDiv_dotted.style.top;

  cropDiv_bottom.style.left = cropDiv_dotted.style.left;
  cropDiv_bottom.style.width = (cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2)) + 'px';
  cropDiv_top.style.left = cropDiv_dotted.style.left;
  cropDiv_top.style.width = (cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2)) + 'px';

  if (cropDiv_left.style.width == '0px')cropDiv_left.style.visibility = 'hidden'; else cropDiv_left.style.visibility = 'visible';
  if (cropDiv_right.style.width == '0px')cropDiv_right.style.visibility = 'hidden'; else cropDiv_right.style.visibility = 'visible';
  if (cropDiv_bottom.style.width == '0px')cropDiv_bottom.style.visibility = 'hidden'; else cropDiv_bottom.style.visibility = 'visible';

}

function cropScript_updateFormValues() {
  crop_getElementsByClassName(ImageCropLayer, "input", "slicex")[0].value = Math.round(cropDiv_dotted.style.left.replace('px', '') / 1 * (crop_originalImageWidth / crop_imageWidth));
  crop_getElementsByClassName(ImageCropLayer, "input", "slicey")[0].value = Math.round(cropDiv_dotted.style.top.replace('px', '') / 1 * (crop_originalImageHeight / crop_imageHeight));
  crop_getElementsByClassName(ImageCropLayer, "input", "slicewidth")[0].value = Math.round((cropDiv_dotted.style.width.replace('px', '') / 1 + (cropToolBorderWidth * 2)) * (crop_originalImageWidth / crop_imageWidth));
  crop_getElementsByClassName(ImageCropLayer, "input", "sliceheight")[0].value = Math.round((cropDiv_dotted.style.height.replace('px', '') / 1 + (cropToolBorderWidth * 2)) * (crop_originalImageHeight / crop_imageHeight));
  if (crop_getElementsByClassName(ImageCropLayer, "input", "slicex")[0].onChange) crop_getElementsByClassName(ImageCropLayer, "input", "slicex")[0].onChange();
}

function cropScript_stopResizeMove() {
  crop_resizeCounter = -1;
  crop_moveCounter = -1;
  cropDiv_dotted.style.cursor = 'move';
  cropScript_updateFormValues();
  if (document.all) {
    var div = cropDiv_dotted.getElementsByTagName('DIV')[0];
    div.style.display = 'block';
  }
}

function cropScript_setCropSizeByInput() {
  var obj_x = crop_getElementsByClassName(ImageCropLayer, "input", "slicex")[0];
  var obj_y = crop_getElementsByClassName(ImageCropLayer, "input", "slicey")[0];
  var obj_width = crop_getElementsByClassName(ImageCropLayer, "input", "slicewidth")[0];
  var obj_height = crop_getElementsByClassName(ImageCropLayer, "input", "sliceheight")[0];

  obj_x.value = obj_x.value.replace(/[^0-9]/gi, '');
  obj_y.value = obj_y.value.replace(/[^0-9]/gi, '');
  obj_width.value = obj_width.value.replace(/[^0-9]/gi, '');
  obj_height.value = obj_height.value.replace(/[^0-9]/gi, '');

  if (obj_x.value.length == 0)obj_x.value = 0;
  if (obj_y.value.length == 0)obj_y.value = 0;
  if (obj_width.value.length == 0)obj_width.value = crop_originalImageWidth;
  if (obj_height.value.length == 0)obj_height.value = crop_originalImageHeight;

  if (obj_x.value > (crop_originalImageWidth - crop_minimumWidthHeight))obj_x.value = crop_originalImageWidth - crop_minimumWidthHeight;
  if (obj_y.value > (crop_originalImageHeight - crop_minimumWidthHeight))obj_y.value = crop_originalImageHeight - crop_minimumWidthHeight;

  if (obj_width.value / 1 > crop_originalImageWidth)obj_width.value = crop_originalImageWidth - obj_x.value / 1;
  if (obj_height.value / 1 > crop_originalImageHeight)obj_height.value = crop_originalImageHeight - obj_y.value / 1;

  if (obj_x.value / 1 + obj_width.value / 1 > crop_originalImageWidth)obj_width.value = crop_originalImageWidth - obj_x.value;
  if (obj_y.value / 1 + obj_height.value / 1 > crop_originalImageHeight)obj_height.value = crop_originalImageHeight - obj_y.value;

  cropDiv_dotted.style.left = Math.round((obj_x.value / 1) * (crop_imageWidth / crop_originalImageWidth)) + 'px';
  cropDiv_dotted.style.top = Math.round((obj_y.value / 1) * (crop_imageHeight / crop_originalImageHeight)) + 'px';

  var iWidth = Math.round(((obj_width.value / 1) - (cropToolBorderWidth * 2)) * (crop_imageWidth / crop_originalImageWidth));
  if (iWidth > 0) cropDiv_dotted.style.width = iWidth + 'px';

  var iHeight = Math.round(((obj_height.value / 1) - (cropToolBorderWidth * 2)) * (crop_imageHeight / crop_originalImageHeight));
  if (iHeight > 0) cropDiv_dotted.style.height = iHeight + 'px';

  repositionSmallSquares();
  resizeTransparentSquares();
}

function cropScript_setBasicEvents() {
  document.documentElement.ondragstart = crop_cancelEvent;
  document.documentElement.onselectstart = crop_cancelEvent;
  document.documentElement.onmousemove = cropScript_mouseMove;
  document.documentElement.onmouseup = cropScript_stopResizeMove;

  crop_getElementsByClassName(ImageCropLayer, "input", "slicex")[0].onblur = cropScript_setCropSizeByInput;
  crop_getElementsByClassName(ImageCropLayer, "input", "slicey")[0].onblur = cropScript_setCropSizeByInput;
  crop_getElementsByClassName(ImageCropLayer, "input", "slicewidth")[0].onblur = cropScript_setCropSizeByInput;
  crop_getElementsByClassName(ImageCropLayer, "input", "sliceheight")[0].onblur = cropScript_setCropSizeByInput;
}

function cropScript_validatePercent() {
  this.value = this.value.replace(/[^0-9]/gi, '');
  if (this.value.length == 0)this.value = '1';
  if (this.value / 1 > crop_maximumPercent)this.value = '100';
  if (this.value / 1 < crop_minimumPercent)this.value = crop_minimumPercent;
}

function cropScript_updateImageSize() {
  cropDiv_top.style.width = crop_imageWidth + 'px';
  cropDiv_right.style.left = (crop_imageWidth) + 'px';
  cropDiv_bottom.style.width = crop_imageWidth + 'px';
  cropDiv_dotted.style.width = (crop_imageWidth - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tr.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_br.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tc.style.left = (Math.floor(crop_imageWidth / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bc.style.left = (Math.floor(crop_imageWidth / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_rc.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';

  cropDiv_top.style.width = crop_imageWidth + 'px';
  cropDiv_right.style.left = (crop_imageWidth) + 'px';
  cropDiv_bottom.style.width = crop_imageWidth + 'px';
  cropDiv_dotted.style.width = (crop_imageWidth - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tr.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_br.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_tc.style.left = (Math.floor(crop_imageWidth / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_bc.style.left = (Math.floor(crop_imageWidth / 2) - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';
  smallSquare_rc.style.left = (crop_imageWidth - offsetSmallSquares - (cropToolBorderWidth * 2)) + 'px';

  repositionSmallSquares();
  resizeTransparentSquares();
}

function crop_progressBar() {
  var div = document.getElementById('crop_progressBar');

  var subDiv = document.createElement('DIV');
  div.appendChild(subDiv);
  subDiv.style.position = 'absolute';
  subDiv.className = 'crop_progressBar_parentBox';
  subDiv.style.left = '0px';
  var progressBarSquare = document.createElement('DIV');
  progressBarSquare.className = 'progressBar_square';
  subDiv.appendChild(progressBarSquare);
  var progressBarSquare = document.createElement('DIV');
  progressBarSquare.className = 'progressBar_square';
  subDiv.appendChild(progressBarSquare);
  var progressBarSquare = document.createElement('DIV');
  progressBarSquare.className = 'progressBar_square';
  subDiv.appendChild(progressBarSquare);
  crop_progressBarMove();
  crop_hideProgressBar();

}

function crop_hideProgressBar() {
  document.getElementById('crop_progressBar').style.visibility = 'hidden';

}

function crop_startProgressBar() {
  var div = document.getElementById('crop_progressBar').getElementsByTagName('DIV')[0];
  div.style.left = '0px';
  document.getElementById('crop_progressBar').style.visibility = 'visible';
}

function crop_progressBarMove() {
  var div = document.getElementById('crop_progressBar').getElementsByTagName('DIV')[0];
  var left = div.style.left.replace('px', '') / 1;
  left = left + 1;
  if (left > div.parentNode.clientWidth)left = 0 - div.clientWidth;
  div.style.left = left + 'px';

  setTimeout('crop_progressBarMove()', 20);

}

function crop_initFixedRatio() {
  if (crop_script_fixedRatio > 1) {
    crop_getElementsByClassName(ImageCropLayer, "input", "sliceheight")[0].value = Math.round(crop_getElementsByClassName(ImageCropLayer, "input", "slicewidth")[0].value) / crop_script_fixedRatio;

  } else {
    crop_getElementsByClassName(ImageCropLayer, "input", "slicewidth")[0].value = Math.round(crop_getElementsByClassName(ImageCropLayer, "input", "sliceheight")[0].value) / crop_script_fixedRatio;

  }
  cropScript_setCropSizeByInput();
}

function init_imageCrop(DOMLayer) {
  ImageCropLayer = DOMLayer;
  cropScript_setBasicEvents();
  crop_createDivElements();
  cropScript_updateFormValues();
  if (crop_script_fixedRatio && crop_script_alwaysPreserveAspectRatio) {
    crop_initFixedRatio();
  }

}
