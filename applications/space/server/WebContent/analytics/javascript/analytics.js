/*
    Monet Analytics Application
    (c) 2009 Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    Monet Analytics is free software under the terms of the GNU Affero General Public License.
    For details, see web site: http://www.gnu.org/licenses/
*/

AppTemplate.viewaccountenvironment='<li class="active_false"><a class="command" href="${command}" title="${label}">${label}</a></li>';
AppTemplate.viewaccountdashboard='<li class="active_${active} ${disabledLabel}"><a class="command" href="${command}" title="${anchorTitle}">${fullLabel}</a></li>';
AppTemplate.viewaccountunit='<li class="active_${active} ${disabledLabel}"><a class="command" href="${command}" title="${anchorTitle}">${fullLabel}</a></li>';
AppTemplate.viewaccount='<a href="showhome()" title="::GotoHome::" class="command text">::Home::</a><span style="margin-left:7px;">|</span><span class="username text"><a href="#" style="white-space:nowrap;" class="label"></a><img src="::ImagesUrl::/s.gif"/><div class="panel username"><ul class="options"><li class="section"><div class="title">::Environments::</div><ul class="environments"></ul></li><li class="section dashboards"><div class="title">::Dashboards::</div><ul class="dashboards"></ul></li><li class="section"><div class="title">::Units::</div><ul class="units"></ul></li></ul><a class="command logout" href="logout()" title="::Logout::" id="cmdLogout" class="command">::Logout::</a></div></span>';
AppTemplate.viewdashboardinfofilter='<li class="filter"><a href="javascript:void(null)" title="::Delete::">${label}</a>${comma}</li>';
AppTemplate.viewdashboardinfo='<div class="view dashboardinfo"><div class="title"><span class="timelapse"></span><span class="indicator"></span></div><ul class="filters"></ul></div>';
AppTemplate.viewindicatorsfolder='<li class="folder closed"><div class="title">${label}</div><ul class="folders"></ul><ul class="indicators"></ul></li>';
AppTemplate.viewindicatorsitem='<li class="indicator ${id}"><input id="input_${id}" type="checkbox"></input><label for="input_${id}" title="${description}">${label}</label><div class="colorbox"></div></li>';
AppTemplate.viewindicators='<div class="view indicators"><!-- add class _expansible to allow accordion behavior --><label>::Title::</label><div class="toolbar"><a class="behaviour clearall" href="javascript:void(null)">::ClearIndicators::</a></div><ul class="folders clear"></ul></div>';
AppTemplate.viewmeasureunitsitem='<li class="measureunit ${id}"><label>${label}</label><div class="dialog"><table><tr><td><label class="title" style="margin-bottom:5px;">::Mode::</label></td><td><input type="radio" class="mode absolute" name="measureunitmode${id}" id="measureunitabsolute${id}" value="absolute" style="margin-bottom:5px;"><label for="measureunitabsolute${id}">::ModeAbsolute::</label></input><input type="radio" class="mode relative" name="measureunitmode${id}" id="measureunitrelative${id}" value="relative" style="margin-bottom:5px;"><label for="measureunitrelative${id}">::ModeRelative::</label></input></td></tr><tr><td><label class="title">::Ranges::</label></td><td><span><input type="text" class="min" value="${minValue}" placeholder="::RangeMinValue::"></input><a href="javascript:void(null)" class="clear min">&nbsp;</a></span><span><input type="text" class="max" value="${maxValue}" placeholder="::RangeMaxValue::"></input><a href="javascript:void(null)" class="clear max">&nbsp;</a></span></td></tr></table></div></li>';
AppTemplate.viewmeasureunits='<div class="view measureunits"><!-- add class _expansible to allow accordion behavior --><label>::Title::</label><ul class="clear" style="display:none;"></ul></div>';
AppTemplate.viewtaxonomiesfilter='<li class="filter ${id}"><a class="togglefilter" href="javascript:void(null)"><label>${label}</label></a><a class="behaviour selectallfilters" href="javascript:void(null)">::SelectAllFilters::</a><div class="categories"></div></li>';
AppTemplate.viewtaxonomies='<div class="view taxonomies"><div class="block compare"><!-- add class _expansible to allow accordion behavior --><label>::CompareBy::</label><div class="toolbar"><a class="behaviour clearcompare" href="javascript:void(null)">::ClearCompare::</a></div><select style="display:none;"></select><div class="taxonomiesbox" style="display:none;"></div></div><div class="block filters"><!-- add class _expansible to allow accordion behavior --><label>::Filters::</label><div class="toolbar"><a class="behaviour clearfilters" href="javascript:void(null)">::ClearFilters::</a></div><div class="clear nofilters" style="display:none;">::NoFilters::</div><ul class="clear" style="display:none;"></ul></div></div>';
AppTemplate.viewchartmessage='<div>${labels} ::IndicatorNotVisiblePart1::.&nbsp;<a class="command" href="changescale(${dashboard},${scale})">::IndicatorNotVisiblePart2::</a>&nbsp;::IndicatorNotVisiblePart3::.</div>';
AppTemplate.viewchartmessagemultiple='<div>${labels} ::IndicatorNotVisiblePart1Multiple::.&nbsp;<a class="command" href="changescale(${dashboard},${scale})">::IndicatorNotVisiblePart2Multiple::</a>&nbsp;::IndicatorNotVisiblePart3Multiple::.</div>';
AppTemplate.viewchart='';
AppTemplate.dialogscale='<div class="dialog scale"><ul class="scalebar"><li class=\'behaviour scale second\'><a href="javascript:void(null)">::ScaleSecond::</a></li><li class=\'behaviour scale minute\'><a href="javascript:void(null)">::ScaleMinute::</a></li><li class=\'behaviour scale hour\'><a href="javascript:void(null)">::ScaleHour::</a></li><li class=\'behaviour scale day\'><a href="javascript:void(null)">::ScaleDay::</a></li><li class=\'behaviour scale month\'><a href="javascript:void(null)">::ScaleMonth::</a></li><li class=\'behaviour scale year\'><a href="javascript:void(null)">::ScaleYear::</a></li></ul></div>';
AppTemplate.dialogzoom='<div class="dialog zoom"><ul><li class=\'behaviour restorezoom\'><a href="javascript:void(null)">::RestoreZoom::</a></li><li class=\'behaviour zoomin\'><a href="javascript:void(null)">::ZoomIn::</a></li></ul></div>';
AppTemplate.viewlinechart='<div class="evolutionloading">::Loading::</div><div class="message selectindicator">::SelectIndicator::</div><div class="message toomuchselectedmetrics">::TooMuchSelectedMetrics::</div><div class="message emptychart">::EmptyChart::</div><div class="dialogzoom"></div><div class="dialogscale"></div><div class="canvasmessages"></div><div class="canvas"></div><div class="dialogtimelapse"></div>';
AppTemplate.dialogtimelapse='<div class="dialog timelapse"><div class="slider"></div><div class="bounds min"></div><div class="bounds max"></div></div>';
AppTemplate.dialogrange='<div class="dialog range"><table style="width:100%"><tr><!--<td width="1%"><ul class="controls"><li class="behaviour play hoverable"><a href="javascript:void(null)" title="::Play::"></a></li><li class="behaviour pause hoverable" style="display:none;"><a href="javascript:void(null)" title="::Pause::"></a></li></ul></td>--><td><div class="slider"></div></td></tr><tr class="controlbox"><td colspan="2"><div class="title"><span></span></div></td></tr></table></div>';
AppTemplate.viewbarchart='<div class="evolutionloading">::Loading::</div><div class="message selectindicator">::SelectIndicator::</div><div class="message toomuchselectedmetrics">::TooMuchSelectedMetrics::</div><div class="message emptychart">::EmptyChart::</div><div class="dialogzoom"></div><div class="dialogscale"></div><div class="canvasmessages"></div><div class="canvas"></div><div class="dialogtimelapse"></div>';
AppTemplate.viewtablechart='<h1>::Details::</h1><div class="evolutionloading">::Loading::</div><div class="message selectindicator">::SelectIndicator::</div><div class="message emptychart">::EmptyChart::</div><div class="layer scale"></div><div class="dialogscale"></div><div class="canvasmessages"></div><div class="canvas"></div><div class="dialogtimelapse"></div>';
AppTemplate.viewmapchart='<div class="evolutionloading">::Loading::</div><div class="message selectindicator">::SelectIndicator::</div><div class="message toomuchselectedmetrics">::TooMuchSelectedMetrics::</div><div class="message toomuchselectedindicators">::TooMuchSelectedIndicators::</div><div class="message comingsoon">::ComingSoon::</div><div class="message emptychart">::EmptyChart::</div><div class="dialogzoom"></div><div class="dialogscale"></div><div class="dialoglayer"></div><div class="canvasmessages"></div><div class="canvas"></div><div class="dialogtimelapse"></div>';
AppTemplate.dialogmaplayer='<div class="dialog maplayer"><ul class="options"><li class=\'behaviour layer pointlayer first\'><a href="javascript:void(null)">::LayerPoint::</a></li><li class=\'behaviour layer heatlayer last\'><a href="javascript:void(null)">::LayerHeat::</a></li></ul></div>';
AppTemplate.viewbubblechart='<div class="evolutionloading">::Loading::</div><div class="message selectindicator">::SelectIndicator::</div><div class="message notenoughselectedindicators">::NotEnoughSelectedIndicators::</div><div class="message toomuchselectedindicators">::TooMuchSelectedIndicators::</div><div class="message toomuchselectedmetrics">::TooMuchSelectedMetrics::</div><div class="message emptychart">::EmptyChart::</div><div class="dialogzoom"></div><div class="dialogscale"></div><div class="canvasmessages"></div><div class="canvas"></div><div class="dialogrange"></div>';
AppTemplate.viewdashboardcontent='<div class="dashboard"><div class="layer info"></div><ul class="toolbar"><li class="chart line active"><a href="javascript:void(null)"><img src="::ImagesUrl::/s.gif" title="::LineChart::"/></a></li><li class="chart bar"><a href="javascript:void(null)"><img src="::ImagesUrl::/s.gif" title="::BarChart::"/></a></li><li class="chart table"><a href="javascript:void(null)"><img src="::ImagesUrl::/s.gif" title="::Table::"/></a></li><li class="chart bubble"><a href="javascript:void(null)"><img src="::ImagesUrl::/s.gif" title="::BubbleChart::"/></a></li><li class="chart map"><a href="javascript:void(null)"><img src="::ImagesUrl::/s.gif" title="::MapChart::"/></a></li><li class="print"><a href="javascript:void(null)"><img src="::ImagesUrl::/s.gif" title="::Print::"/></a></li><li class="download"><a href="javascript:void(null)"><img src="::ImagesUrl::/s.gif" title="::Download::"/></a></li></ul><div class="layer chart"></div></div>';
AppTemplate.viewdashboard='<div class="layer notLoaded" style="display:none;"><div>::NotLoaded::</div><div><a class="reload" href="#">::Reload::</a></div></div><div class="layout"><div class="ui-layout-north header"><div class="leftbox"><div class="logo"><table><tr><td><a class="command home" href="showhome()" title="::ShowHome::"></a><img class="federation image"/></td><td style="vertical-align:middle;"><a class="command home" href="showhome()" title="::ShowHome::"></a><table><tr><td><div class="space label subtitle"></div></td></tr><tr><td><div class="model label title"></div></td></tr></table></td></tr></table><div id="loadingBox" class="loading">::Starting::</div></div></div><div class="rightbox"><div class="accountview block fright"></div></div></div><div class="ui-layout-west" id="westlayer"><label class="helper">::Helper::</label><div class="layer indicators"></div><div class="layer measureunits"></div><div class="layer taxonomies"></div></div><div class="ui-layout-center" id="centerlayer"></div></div>';
AppTemplate.desktop='<div id=\'mainLoading\'></div><div id=\'viewsContainer\'></div>';
function List() {
  this.items = new Array();
  this.positions = new Array();
}

List.prototype.clear = function() {
  this.items = new Array();
  this.positions = new Array();
};

List.prototype.size = function() {
  return this.items.length;
};

List.prototype.getAll = function() {
  return this.items;
};

List.prototype.getKeys = function() {
  var result = new Array();
  for (var i=0; i<this.items.length; i++) {
    var item = this.items[i];
    result.push(item.id);
  }
  return result;
};

List.prototype.get = function(id, value) {
  return this.items[this.positions[id + value]];
};

List.prototype.getItemKey = function(item) {
  return item.id + (item.value!=null?item.value:"");
}

List.prototype.addItem = function(item) {
  this.items.push(item);
  this.positions[this.getItemKey(item)] = this.items.length-1;
};

List.prototype.regeneratePositions = function() {
  this.positions = new Array();
  for (var i=0; i<this.items.length; i++) {
    var item = this.items[i];
    this.positions[this.getItemKey(item)] = i;
  }
};

List.prototype.deleteItem = function(id, value) {
  this.items.splice(this.positions[id + value],1);
  this.regeneratePositions();
};

List.prototype.clone = function(classAction) {
  var list = new classAction;
  for (var i=0; i<this.items.length; i++)
    list.addItem(this.items[i]);
  return list;
};

List.prototype.toJson = function() {
  var rows = "";
  
  for (var i=0; i<this.items.length; i++) {
    if (i != 0) rows += ",";
    rows += this.items[i].toJson();
  }
  
  return "{\"rows\":[" + rows + "],\"nrows\":\"" + this.items.length + "\"}";
};

List.prototype.fromJson = function(jsonObject) {
};

function Indicator(id) {
  this.id = id;
  this.value = "";
}

Indicator.prototype.toJson = function() {
  return "{\"id\":\"" + stringify(this.id) + "\"}";
};

Indicator.prototype.fromJson = function(jsonObject) {
  this.id = jsonObject.id;
};

function IndicatorList() {
  this.base = List;
  this.base();
}

IndicatorList.prototype = new List;
IndicatorList.constructor = IndicatorList;

IndicatorList.prototype.add = function(id) {
  if (this.get(id, "") != null) return;
  var indicator = new Indicator(id);
  this.addItem(indicator);
};

IndicatorList.prototype.fromJson = function(jsonObject) {
  this.clear();
  for (var i=0; i<jsonObject.rows.length; i++) {
    var indicator = new Indicator();
    indicator.fromJson(jsonObject.rows[i]);
    this.addItem(indicator);
  }
};

function Category(taxonomyId, id, label) {
  this.taxonomyId = taxonomyId;
  this.id = id;
  this.label = label;
}

Category.prototype.toJson = function() {
  return "{\"taxonomy\":\"" + stringify(this.taxonomyId) + "\",\"id\":\"" + stringify(this.id) + "\",\"label\":\"" + stringify(this.label) + "\"}";
};

Category.prototype.fromJson = function(jsonObject) {
  this.id = jsonObject.id;
  this.label = jsonObject.label;
  this.taxonomyId = jsonObject.taxonomy;
};

function CategoryList() {
  this.base = List;
  this.base();
}

CategoryList.prototype = new List;
CategoryList.constructor = IndicatorList;

CategoryList.prototype.add = function(taxonomyId, id, label) {
  if (this.get(id, "") != null) return;
  var category = new Category(taxonomyId, id, label);
  this.addItem(category);
};

CategoryList.prototype.getCategoriesOfTaxonomy = function(taxonomyId) {
  var result = new Array();
  
  for (var i=0; i<this.items.length; i++) {
    var category = this.items[i];
    if (category.taxonomyId == taxonomyId)
      result.push(category);
  }
  
  return result;
};

CategoryList.prototype.deleteCategoriesOfTaxonomy = function(taxonomyId) {
  var itemsToDelete = this.getCategoriesOfTaxonomy(taxonomyId);
  for (var i=0; i<itemsToDelete.length; i++)
    this.deleteItem(itemsToDelete[i].id, "");
};

CategoryList.prototype.fromJson = function(jsonObject) {
  this.clear();
  for (var i=0; i<jsonObject.rows.length; i++) {
    var category = new Category();
    category.fromJson(jsonObject.rows[i]);
    this.addItem(category);
  }
};

function Range(measureUnit, mode, min, max) {
  this.measureUnit = measureUnit;
  this.mode = mode!=null?mode:Range.ABSOLUTE;
  this.min = min;
  this.max = max;
}

Range.ABSOLUTE = "absolute";
Range.RELATIVE = "relative";

Range.prototype.toJson = function() {
  var minValue = this.min;
  var maxValue = this.max;
  if (minValue == null) minValue = "";
  if (maxValue == null) maxValue = "";
  return "{\"measureUnit\":\"" + this.measureUnit + "\",\"mode\":\"" + this.mode + "\",\"min\":\"" + minValue + "\",\"max\":\"" + maxValue + "\"}";
};

Range.prototype.fromJson = function(jsonObject) {
  this.measureUnit = jsonObject.measureUnit;
  this.mode = jsonObject.mode;
  this.min = jsonObject.min;
  this.max = jsonObject.max;
};

function RangeList() {
  this.base = List;
  this.base();
}

RangeList.prototype = new List;
RangeList.constructor = RangeList;

RangeList.prototype.add = function(measureUnit, mode, min, max) {
  if (this.get(measureUnit, "") != null) return;
  var range = new Range(measureUnit, mode, min, max);
  this.addItem(range);
};

RangeList.prototype.addItem = function(item) {
  this.items.push(item);
  this.positions[item.measureUnit] = this.items.length-1;
};

RangeList.prototype.get = function(measureUnit) {
  return this.items[this.positions[measureUnit]];
};

RangeList.prototype.fromJson = function(jsonObject) {
  this.clear();
  for (var i=0; i<jsonObject.rows.length; i++) {
    var range = new Range();
    range.fromJson(jsonObject.rows[i]);
    this.addItem(range);
  }
};

State = new Object();
State.account = null;
State.compareTaxonomy = null;
State.indicatorList = new IndicatorList();
State.compareList = new CategoryList();
State.filterList = new CategoryList();
State.timeLapse = { from: null, to: null, scale: 5 };
State.lastCommand = new Object();
State.chartType = null;
State.chartLayer = null;
State.rangeList = new RangeList();

State.Mode = {
  EMBED : "embed",
  PRINT : "print"
};



Encoder = {

  // When encoding do we convert characters into html or numerical entities
  EncodeType : "entity",  // entity OR numerical

  isEmpty : function(val){
    if(val){
      return ((val===null) || val.length==0 || /^\s+$/.test(val));
    }else{
      return true;
    }
  },
  
  // arrays for conversion from HTML Entities to Numerical values
  arr1: ['&nbsp;','&iexcl;','&cent;','&pound;','&curren;','&yen;','&brvbar;','&sect;','&uml;','&copy;','&ordf;','&laquo;','&not;','&shy;','&reg;','&macr;','&deg;','&plusmn;','&sup2;','&sup3;','&acute;','&micro;','&para;','&middot;','&cedil;','&sup1;','&ordm;','&raquo;','&frac14;','&frac12;','&frac34;','&iquest;','&Agrave;','&Aacute;','&Acirc;','&Atilde;','&Auml;','&Aring;','&AElig;','&Ccedil;','&Egrave;','&Eacute;','&Ecirc;','&Euml;','&Igrave;','&Iacute;','&Icirc;','&Iuml;','&ETH;','&Ntilde;','&Ograve;','&Oacute;','&Ocirc;','&Otilde;','&Ouml;','&times;','&Oslash;','&Ugrave;','&Uacute;','&Ucirc;','&Uuml;','&Yacute;','&THORN;','&szlig;','&agrave;','&aacute;','&acirc;','&atilde;','&auml;','&aring;','&aelig;','&ccedil;','&egrave;','&eacute;','&ecirc;','&euml;','&igrave;','&iacute;','&icirc;','&iuml;','&eth;','&ntilde;','&ograve;','&oacute;','&ocirc;','&otilde;','&ouml;','&divide;','&oslash;','&ugrave;','&uacute;','&ucirc;','&uuml;','&yacute;','&thorn;','&yuml;','&quot;','&amp;','&lt;','&gt;','&OElig;','&oelig;','&Scaron;','&scaron;','&Yuml;','&circ;','&tilde;','&ensp;','&emsp;','&thinsp;','&zwnj;','&zwj;','&lrm;','&rlm;','&ndash;','&mdash;','&lsquo;','&rsquo;','&sbquo;','&ldquo;','&rdquo;','&bdquo;','&dagger;','&Dagger;','&permil;','&lsaquo;','&rsaquo;','&euro;','&fnof;','&Alpha;','&Beta;','&Gamma;','&Delta;','&Epsilon;','&Zeta;','&Eta;','&Theta;','&Iota;','&Kappa;','&Lambda;','&Mu;','&Nu;','&Xi;','&Omicron;','&Pi;','&Rho;','&Sigma;','&Tau;','&Upsilon;','&Phi;','&Chi;','&Psi;','&Omega;','&alpha;','&beta;','&gamma;','&delta;','&epsilon;','&zeta;','&eta;','&theta;','&iota;','&kappa;','&lambda;','&mu;','&nu;','&xi;','&omicron;','&pi;','&rho;','&sigmaf;','&sigma;','&tau;','&upsilon;','&phi;','&chi;','&psi;','&omega;','&thetasym;','&upsih;','&piv;','&bull;','&hellip;','&prime;','&Prime;','&oline;','&frasl;','&weierp;','&image;','&real;','&trade;','&alefsym;','&larr;','&uarr;','&rarr;','&darr;','&harr;','&crarr;','&lArr;','&uArr;','&rArr;','&dArr;','&hArr;','&forall;','&part;','&exist;','&empty;','&nabla;','&isin;','&notin;','&ni;','&prod;','&sum;','&minus;','&lowast;','&radic;','&prop;','&infin;','&ang;','&and;','&or;','&cap;','&cup;','&int;','&there4;','&sim;','&cong;','&asymp;','&ne;','&equiv;','&le;','&ge;','&sub;','&sup;','&nsub;','&sube;','&supe;','&oplus;','&otimes;','&perp;','&sdot;','&lceil;','&rceil;','&lfloor;','&rfloor;','&lang;','&rang;','&loz;','&spades;','&clubs;','&hearts;','&diams;'],
  arr2: ['&#160;','&#161;','&#162;','&#163;','&#164;','&#165;','&#166;','&#167;','&#168;','&#169;','&#170;','&#171;','&#172;','&#173;','&#174;','&#175;','&#176;','&#177;','&#178;','&#179;','&#180;','&#181;','&#182;','&#183;','&#184;','&#185;','&#186;','&#187;','&#188;','&#189;','&#190;','&#191;','&#192;','&#193;','&#194;','&#195;','&#196;','&#197;','&#198;','&#199;','&#200;','&#201;','&#202;','&#203;','&#204;','&#205;','&#206;','&#207;','&#208;','&#209;','&#210;','&#211;','&#212;','&#213;','&#214;','&#215;','&#216;','&#217;','&#218;','&#219;','&#220;','&#221;','&#222;','&#223;','&#224;','&#225;','&#226;','&#227;','&#228;','&#229;','&#230;','&#231;','&#232;','&#233;','&#234;','&#235;','&#236;','&#237;','&#238;','&#239;','&#240;','&#241;','&#242;','&#243;','&#244;','&#245;','&#246;','&#247;','&#248;','&#249;','&#250;','&#251;','&#252;','&#253;','&#254;','&#255;','&#34;','&#38;','&#60;','&#62;','&#338;','&#339;','&#352;','&#353;','&#376;','&#710;','&#732;','&#8194;','&#8195;','&#8201;','&#8204;','&#8205;','&#8206;','&#8207;','&#8211;','&#8212;','&#8216;','&#8217;','&#8218;','&#8220;','&#8221;','&#8222;','&#8224;','&#8225;','&#8240;','&#8249;','&#8250;','&#8364;','&#402;','&#913;','&#914;','&#915;','&#916;','&#917;','&#918;','&#919;','&#920;','&#921;','&#922;','&#923;','&#924;','&#925;','&#926;','&#927;','&#928;','&#929;','&#931;','&#932;','&#933;','&#934;','&#935;','&#936;','&#937;','&#945;','&#946;','&#947;','&#948;','&#949;','&#950;','&#951;','&#952;','&#953;','&#954;','&#955;','&#956;','&#957;','&#958;','&#959;','&#960;','&#961;','&#962;','&#963;','&#964;','&#965;','&#966;','&#967;','&#968;','&#969;','&#977;','&#978;','&#982;','&#8226;','&#8230;','&#8242;','&#8243;','&#8254;','&#8260;','&#8472;','&#8465;','&#8476;','&#8482;','&#8501;','&#8592;','&#8593;','&#8594;','&#8595;','&#8596;','&#8629;','&#8656;','&#8657;','&#8658;','&#8659;','&#8660;','&#8704;','&#8706;','&#8707;','&#8709;','&#8711;','&#8712;','&#8713;','&#8715;','&#8719;','&#8721;','&#8722;','&#8727;','&#8730;','&#8733;','&#8734;','&#8736;','&#8743;','&#8744;','&#8745;','&#8746;','&#8747;','&#8756;','&#8764;','&#8773;','&#8776;','&#8800;','&#8801;','&#8804;','&#8805;','&#8834;','&#8835;','&#8836;','&#8838;','&#8839;','&#8853;','&#8855;','&#8869;','&#8901;','&#8968;','&#8969;','&#8970;','&#8971;','&#9001;','&#9002;','&#9674;','&#9824;','&#9827;','&#9829;','&#9830;'],
    
  // Convert HTML entities into numerical entities
  HTML2Numerical : function(s){
    return this.swapArrayVals(s,this.arr1,this.arr2);
  },  

  // Convert Numerical entities into HTML entities
  NumericalToHTML : function(s){
    return this.swapArrayVals(s,this.arr2,this.arr1);
  },


  // Numerically encodes all unicode characters
  numEncode : function(s){
    
    if(this.isEmpty(s)) return "";

    var e = "";
    for (var i = 0; i < s.length; i++)
    {
      var c = s.charAt(i);
      if (c < " " || c > "~")
      {
        c = "&#" + c.charCodeAt() + ";";
      }
      e += c;
    }
    return e;
  },
  
  // HTML Decode numerical and HTML entities back to original values
  htmlDecode : function(s){

    var c,m,d = s;
    
    if(this.isEmpty(d)) return "";

    // convert HTML entites back to numerical entites first
    d = this.HTML2Numerical(d);
    
    // look for numerical entities &#34;
    arr=d.match(/&#[0-9]{1,5};/g);
    
    // if no matches found in string then skip
    if(arr!=null){
      for(var x=0;x<arr.length;x++){
        m = arr[x];
        c = m.substring(2,m.length-1); //get numeric part which is refernce to unicode character
        // if its a valid number we can decode
        if(c >= -32768 && c <= 65535){
          // decode every single match within string
          d = d.replace(m, String.fromCharCode(c));
        }else{
          d = d.replace(m, ""); //invalid so replace with nada
        }
      }     
    }

    return d;
  },    

  // encode an input string into either numerical or HTML entities
  htmlEncode : function(s,dbl){
      
    if(this.isEmpty(s)) return "";

    // do we allow double encoding? E.g will &amp; be turned into &amp;amp;
    dbl = dbl || false; //default to prevent double encoding
    
    // if allowing double encoding we do ampersands first
    if(dbl){
      if(this.EncodeType=="numerical"){
        s = s.replace(/&/g, "&#38;");
      }else{
        s = s.replace(/&/g, "&amp;");
      }
    }

    // convert the xss chars to numerical entities ' " < >
    s = this.XSSEncode(s,false);
    
    if(this.EncodeType=="numerical" || !dbl){
      // Now call function that will convert any HTML entities to numerical codes
      s = this.HTML2Numerical(s);
    }

    // Now encode all chars above 127 e.g unicode
    s = this.numEncode(s);

    // now we know anything that needs to be encoded has been converted to numerical entities we
    // can encode any ampersands & that are not part of encoded entities
    // to handle the fact that I need to do a negative check and handle multiple ampersands &&&
    // I am going to use a placeholder

    // if we don't want double encoded entities we ignore the & in existing entities
    if(!dbl){
      s = s.replace(/&#/g,"##AMPHASH##");
    
      if(this.EncodeType=="numerical"){
        s = s.replace(/&/g, "&#38;");
      }else{
        s = s.replace(/&/g, "&amp;");
      }

      s = s.replace(/##AMPHASH##/g,"&#");
    }
    
    // replace any malformed entities
    s = s.replace(/&#\d*([^\d;]|$)/g, "$1");

    if(!dbl){
      // safety check to correct any double encoded &amp;
      s = this.correctEncoding(s);
    }

    // now do we need to convert our numerical encoded string into entities
    if(this.EncodeType=="entity"){
      s = this.NumericalToHTML(s);
    }

    return s;         
  },

  // Encodes the basic 4 characters used to malform HTML in XSS hacks
  XSSEncode : function(s,en){
    if(!this.isEmpty(s)){
      en = en || true;
      // do we convert to numerical or html entity?
      if(en){
        s = s.replace(/\'/g,"&#39;"); //no HTML equivalent as &apos is not cross browser supported
        s = s.replace(/\"/g,"&quot;");
        s = s.replace(/</g,"&lt;");
        s = s.replace(/>/g,"&gt;");
      }else{
        s = s.replace(/\'/g,"&#39;"); //no HTML equivalent as &apos is not cross browser supported
        s = s.replace(/\"/g,"&#34;");
        s = s.replace(/</g,"&#60;");
        s = s.replace(/>/g,"&#62;");
      }
      return s;
    }else{
      return "";
    }
  },

  // returns true if a string contains html or numerical encoded entities
  hasEncoded : function(s){
    if(/&#[0-9]{1,5};/g.test(s)){
      return true;
    }else if(/&[A-Z]{2,6};/gi.test(s)){
      return true;
    }else{
      return false;
    }
  },

  // will remove any unicode characters
  stripUnicode : function(s){
    return s.replace(/[^\x20-\x7E]/g,"");
    
  },

  // corrects any double encoded &amp; entities e.g &amp;amp;
  correctEncoding : function(s){
    return s.replace(/(&amp;)(amp;)+/,"$1");
  },


  // Function to loop through an array swaping each item with the value from another array e.g swap HTML entities with Numericals
  swapArrayVals : function(s,arr1,arr2){
    if(this.isEmpty(s)) return "";
    var re;
    if(arr1 && arr2){
      //ShowDebug("in swapArrayVals arr1.length = " + arr1.length + " arr2.length = " + arr2.length)
      // array lengths must match
      if(arr1.length == arr2.length){
        for(var x=0,i=arr1.length;x<i;x++){
          re = new RegExp(arr1[x], 'g');
          s = s.replace(re,arr2[x]); //swap arr1 item with matching item from arr2  
        }
      }
    }
    return s;
  },

  inArray : function( item, arr ) {
    for ( var i = 0, x = arr.length; i < x; i++ ){
      if ( arr[i] === item ){
        return i;
      }
    }
    return -1;
  }

}

GraphUtil = function() {
};

GraphUtil.colors = ["#A52A2A","#4169E1","#DC143C","#008B8B","#696969","#006400","#8B008B","#FF8C00","#E8653A","#6F916F","#483D8B","#1E90FF","#DAA520","#BA55D3","#F7E120","#8B4513"];
GraphUtil.templates = new Object();
GraphUtil.templates.percentage = "<div class='bar' style='display:block;height:100%;float:left;'></div><div class='percentage' style='display:block;position:absolute;color:black;margin-right:5px;margin-top:5px;right:0;'></div>";
GraphUtil.templates.differences = "<table width='100%'><tr><td style='width:49.9%;vertical-align:middle;'><div class='leftbar' style='float:right;height:100%;color:white;text-align:right;'></div></td><td class='separator'></td><td style='width:49.9%;'><div style='position:relative;'><div class='rightbar' style='position:absolute;margin-left:-1px;height:100%;color:white;text-align:left;'></div></div></td></tr></table>";
  
GraphUtil.getColors = function(){
  return GraphUtil.colors;
};

GraphUtil.getColor = function(pos){
  while (pos > GraphUtil.colors.length) pos = pos-GraphUtil.colors.length;
  if (pos < 0) pos = 0;
  if (pos == GraphUtil.colors.length) pos = GraphUtil.colors.length-1;
  return GraphUtil.colors[pos];
};

GraphUtil.drawPercentage = function(DOMLayer, percentage, colorPos, animated) {
  var jLayer = $(DOMLayer);
  
  if (!animated) jLayer.html(GraphUtil.templates.percentage);
  
  var jBar = jLayer.find(".bar");
  jBar.css("background-color", GraphUtil.getColor(colorPos));
  
  if (animated) jBar.animate({width:jLayer.width()*(percentage/100)},400);
  else jBar.css("width", jLayer.width()*(percentage/100));
  
  jLayer.find(".percentage").html(Math.round(percentage) + "%");
};

GraphUtil.drawDifferences = function(DOMLayer, value, difference, config, animated) {
  var jLayer = $(DOMLayer);
  var absDifference = Math.abs(difference);
  var total = Math.abs(value) + absDifference;
  var height = jLayer.height();

  if (!animated) jLayer.html(GraphUtil.templates.differences);
  
  var jLeftBar = jLayer.find(".leftbar");
  var jRightBar = jLayer.find(".rightbar");
  var jSeparator = jLayer.find(".separator");
  var percentage = 100*absDifference/total;
  
  jLeftBar.css("background-color", config.negative);
  jRightBar.css("background-color", config.positive);
  jLeftBar.css("height", height*0.50);
  jSeparator.css("height", height);
  jRightBar.css("height", height*0.50);
  
  if (difference == 0) {
    jSeparator.css("border-left", "1px solid black");
    if (animated) { 
      jLeftBar.animate({width:0},400);
      jRightBar.animate({width:0},400);
    }
    else {
      jLeftBar.css("width", 0);
      jRightBar.css("width", 0);
    }
  }
  else { 
    jSeparator.css("border-left", "1px solid black");
    
    if (difference < 0) {
      if (animated) jLeftBar.animate({width:jLayer.width()*(percentage/200)},400); 
      else jLeftBar.css("width", jLayer.width()*(percentage/200));
      
      if (config.showPercentage) jLeftBar.html(Math.round(percentage) + "%&nbsp;");
      jRightBar.css("width", 0);
    }
    else {
      jLeftBar.css("width", 0);
      
      if (animated) jRightBar.animate({width:jLayer.width()*(percentage/200)},400); 
      else jRightBar.css("width", jLayer.width()*(percentage/200));
      
      jRightBar.css("margin-top", "-" + (Math.round(height)/4+1));
      if (config.showPercentage) jRightBar.html("&nbsp;" + Math.round(percentage) + "%");
    }
  }
};

function readData(data, jdata) {
  var jdataList = jdata.find("div");
  jdataList.each(function() {
    var jData = $(this);
    eval("data." + this.id + "= '" + jData.text() + "';");
  }, this);
}

function isFunction(item) {
  return (typeof item == "function");
}

function writeServerRequest(mode, data) {
  var result = (mode) ? "r=" + Base64.encode(data) : data;
  return "?" + result;
};

function translate(text, data, prefix) {
  if (!text) return "";
  if (!prefix) prefix = "";

  if (Context.Config) {
    Expression = new RegExp("::ImagesUrl::", "g");
    text = text.replace(Expression, Context.Config.ImagesUrl);
  }

  for (var index in data) {
    var name = prefix + index;

    var Aux = data[index];
    if (typeof Aux == "object") {
      text = translate(text, Aux, name + ".");
    }
    else  {
      Expression = new RegExp("::" + name + "::", "g");
      text = text.replace(Expression, Aux);
    }
  }
  return text;
};

function serializeParameters(parameters) {
  var result = "";
  for (var index in parameters) {
    if (isFunction(parameters[index])) continue;
    result += "&" + index + "=" + parameters[index];
  }
  return result;
};

function readServerResponse(mode, data) {
  if (mode) return Base64.decode(data);
  return Encoder.htmlDecode(data);
};

function setIdToElementContent(id, content) {
  var pos = 0;
  var leftContent, rightContent;

  while ((content.charAt(pos) != " ") && (pos < content.length)) pos++;
  if (pos >= content.length) return false; 

  leftContent = content.substr(0, pos);
  rightContent = content.substr(pos);

  content = leftContent + " " + "id=" + "'" + id + "'" + rightContent;

  return content;
};

function replaceDOMElement(DOMElement, content) {
  var id = null;
  
  result = content.match(/^<[^>]*id=\"([^\"]*)\"[^>]*>/);
  if (! result) result = content.match(/^<[^>]*id=\'([^\']*)\'[^>]*>/);
  if (! result) result = content.match(/^<[^>]*id=([^ ]*) [^>]*>/);
  
  if (result) 
    id = result[1];
  else {
    id = DOMElement.id;
    if(!id) id = generateId();
    if (content != "") content = setIdToElementContent(id, content);
  }

  if ($.browser.msie) {
    try {
      DOMElement.outerHTML = content;
    } catch (e) {
      alert("HTML Exception", "Check your HTML template. Perhaps you have '<form>' tags. HTML doesn't allow including forms into forms.");
    }
  }
  else {
    try {
      var jElement = $(DOMElement);
      jElement.replaceWith(content);
    }
    catch(e) {}
  }

  DOMElement = $("#" + id).get(0);

  return DOMElement;
};

function generateId(element, prefix) {
  var generatedId;
  var counter = 1;
  do {
    generatedId = (prefix ? prefix + '-' : '') + (counter++);
  } while(document.getElementById(generatedId));

  if (element) {
    $(element).attr('id', generatedId);
  }
  return generatedId;
};

function utf8Encode(data) {
  return unescape(encodeURIComponent(data));
};

function utf8Decode(data) {
  return unescape(decodeURIComponent(data));
};

function size(elementArray) {
  var iLength = 0;
  for (var index in elementArray) { 
    if (isFunction(elementArray[index])) continue;
    iLength++; 
  }
  return iLength;
};

function replaceAll(content, search, replacement) {
  while (content.toString().indexOf(search) != -1)
    content = content.toString().replace(search,replacement);
  return content;
};

Array.prototype.size = function() {
  var iLength = 0;
  for (var index in this) { 
    if (isFunction(this[index])) continue;
    iLength++; 
  }
  return iLength;
};

function shortValue(value, maxLength) {
  if (!value.length) return;
  if (value.length <= maxLength) return value;
  var length = value.length;
  var middleLength = Math.floor(maxLength/2);
  var leftValue = value.substring(0, middleLength);
  var rightValue = value.substring(length-middleLength, length);
  return leftValue + "..." + rightValue;
};

String.prototype.toHtmlId = function() {
  var result = new String();
  result = this.replace(/[^\w]/g,"");
  return result;
};


Number.prototype.formatNumber = function(numberType, language, type) {
  
  if (numberType == "currency") c = 2;
  else if (numberType == "float") c = 3;
  else c = 0;
    
  if (language == "es") {
    d = ",";
    t = ".";
  }
  else {
    d = ".";
    t = ",";
  }
  
  var n = this, c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "," : d, t = t == undefined ? "." : t, s = n < 0 ? "-" : "", i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", j = (j = i.length) > 3 ? j % 3 : 0;
     return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};

//--------------------------------------------------------------------------------
function addSelectOption(DOMSelect, sValue, sText, selected, disabled) {
  var DOMOption = document.createElement('option');
  DOMOption.value = sValue;
  DOMOption.text = sText;
  DOMOption.selected = (selected!=null)?selected:false;
  DOMOption.disabled = (disabled!=null)?disabled:false;
  try {
    DOMSelect.add(DOMOption,null);
  } catch (e) {
    DOMSelect.add(DOMOption); // IE only version.
  }
};

function trim(content) {
  return content.replace(/^\s+|\s+$/g, '');
};

function isNumber(value) {
  var reg = new RegExp(/^\d+$/);
  return reg.test(toNumber(value));
};

function toNumber(value) {
  var valueToTest = value.replace(".", "");
  return valueToTest.replace(",", "");
};

function reload(location) {
  window.location = location + (location.indexOf("?")!=-1?"&":"?") + "m=" + Math.random();
};

function stringify(content) {
	return content.replace(/\"/g, "\\\"");
}

//--------------------------------------------------------------------------------

function parseServerDate(dtDate) {
  return Date.parseDate(dtDate, SERVER_DATE_FORMAT);
};

//--------------------------------------------------------------------------------

function toServerDate(dtDate) {
  if (dtDate == null) return "";
  return dtDate.format(SERVER_DATE_FORMAT);
};

// http://jacwright.com/projects/javascript/date_format/
Date.prototype.format = function(format, language) {
    var returnStr = '';
    var replace = Date.replaceChars[language];
    for (var i = 0; i < format.length; i++) {       
      var curChar = format.charAt(i);         if (i - 1 >= 0 && format.charAt(i - 1) == "\\") {
            returnStr += curChar;
        }
        else if (replace[curChar]) {
            returnStr += replace[curChar].call(this);
        } else if (curChar != "\\"){
            returnStr += curChar;
        }
    }
    return returnStr;
};

Date.timeToLocale = function(milliseconds) {
  var result = new Date(milliseconds);
  return milliseconds + (result.getTimezoneOffset()*60*1000);
}

Date.timeToUTC = function(milliseconds) {
  var result = new Date(milliseconds);
  return milliseconds - (result.getTimezoneOffset()*60*1000);
}

Date.difference = function(from, to, unit) {
  return Math.round((from-to)/unit);
};

Date.getYears = function(from, to) {
  return Date.difference(from, to, 1000 * 60 * 60 * 24 * 365);
};

Date.getMonths = function(from, to) {
  return Date.difference(from, to, 1000 * 60 * 60 * 24 * 30);
};

Date.getDays = function(from, to) {
  return Date.difference(from, to, 1000 * 60 * 60 * 24);
};

Date.getHours = function(from, to) {
  return Date.difference(from, to, 1000 * 60 * 60);
};

Date.getMinutes = function(from, to) {
  return Date.difference(from, to, 1000 * 60);
};

Date.getSeconds = function(from, to) {
  return Date.difference(from, to, 1000);
};

Date.replaceChars = {
  "es": {
    shortMonths: ['ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dec'],
    longMonths: ['enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio', 'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'],
    shortDays: ['dom', 'lun', 'mar', 'mie', 'jue', 'vie', 'sab'],
    longDays: ['domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado'],

    // Day
    d: function() { return (this.getDate() < 10 ? '0' : '') + this.getDate(); },
    D: function() { return Date.replaceChars["es"].shortDays[this.getDay()]; },
    j: function() { return this.getDate(); },
    l: function() { return Date.replaceChars["es"].longDays[this.getDay()]; },
    N: function() { return this.getDay() + 1; },
    S: function() { return (this.getDate() % 10 == 1 && this.getDate() != 11 ? 'st' : (this.getDate() % 10 == 2 && this.getDate() != 12 ? 'nd' : (this.getDate() % 10 == 3 && this.getDate() != 13 ? 'rd' : 'th'))); },
    w: function() { return this.getDay(); },
    z: function() { var d = new Date(this.getFullYear(),0,1); return Math.ceil((this - d) / 86400000); }, // Fixed now
    // Week
    W: function() { var d = new Date(this.getFullYear(), 0, 1); return Math.ceil((((this - d) / 86400000) + d.getDay() + 1) / 7); }, // Fixed now
    // Month
    F: function() { return Date.replaceChars["es"].longMonths[this.getMonth()]; },
    m: function() { return (this.getMonth() < 9 ? '0' : '') + (this.getMonth() + 1); },
    M: function() { return Date.replaceChars["es"].shortMonths[this.getMonth()]; },
    n: function() { return this.getMonth() + 1; },
    t: function() { var d = new Date(); return new Date(d.getFullYear(), d.getMonth(), 0).getDate() }, // Fixed now, gets #days of date
    // Year
    L: function() { var year = this.getFullYear(); return (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)); },   // Fixed now
    o: function() { var d  = new Date(this.valueOf());  d.setDate(d.getDate() - ((this.getDay() + 6) % 7) + 3); return d.getFullYear();}, //Fixed now
    Y: function() { return this.getFullYear(); },
    y: function() { return ('' + this.getFullYear()).substr(2); },
    // Time
    a: function() { return this.getHours() < 12 ? 'am' : 'pm'; },
    A: function() { return this.getHours() < 12 ? 'AM' : 'PM'; },
    B: function() { return Math.floor((((this.getUTCHours() + 1) % 24) + this.getUTCMinutes() / 60 + this.getUTCSeconds() / 3600) * 1000 / 24); }, // Fixed now
    g: function() { return this.getHours() % 12 || 12; },
    G: function() { return this.getHours(); },
    h: function() { return ((this.getHours() % 12 || 12) < 10 ? '0' : '') + (this.getHours() % 12 || 12); },
    H: function() { return (this.getHours() < 10 ? '0' : '') + this.getHours(); },
    i: function() { return (this.getMinutes() < 10 ? '0' : '') + this.getMinutes(); },
    s: function() { return (this.getSeconds() < 10 ? '0' : '') + this.getSeconds(); },
    u: function() { var m = this.getMilliseconds(); return (m < 10 ? '00' : (m < 100 ?'0' : '')) + m; },
    // Timezone
    e: function() { return "Not Yet Supported"; },
    I: function() {
        var DST = null;
            for (var i = 0; i < 12; ++i) {
                    var d = new Date(this.getFullYear(), i, 1);
                    var offset = d.getTimezoneOffset();

                    if (DST === null) DST = offset;
                    else if (offset < DST) { DST = offset; break; }                     else if (offset > DST) break;
            }
            return (this.getTimezoneOffset() == DST) | 0;
        },
    O: function() { return (-this.getTimezoneOffset() < 0 ? '-' : '+') + (Math.abs(this.getTimezoneOffset() / 60) < 10 ? '0' : '') + (Math.abs(this.getTimezoneOffset() / 60)) + '00'; },
    P: function() { return (-this.getTimezoneOffset() < 0 ? '-' : '+') + (Math.abs(this.getTimezoneOffset() / 60) < 10 ? '0' : '') + (Math.abs(this.getTimezoneOffset() / 60)) + ':00'; }, // Fixed now
    T: function() { var m = this.getMonth(); this.setMonth(0); var result = this.toTimeString().replace(/^.+ \(?([^\)]+)\)?$/, '$1'); this.setMonth(m); return result;},
    Z: function() { return -this.getTimezoneOffset() * 60; },
    // Full Date/Time
    c: function() { return this.format("Y-m-d\\TH:i:sP"); }, // Fixed now
    r: function() { return this.toString(); },
    U: function() { return this.getTime() / 1000; }
  },
  "en": {
    shortMonths: ['jan', 'feb', 'mar', 'apr', 'may', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dec'],
    longMonths: ['january', 'february', 'march', 'april', 'may', 'june', 'july', 'august', 'september', 'october', 'november', 'december'],
    shortDays: ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'],
    longDays: ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'],

    // Day
    d: function() { return (this.getDate() < 10 ? '0' : '') + this.getDate(); },
    D: function() { return Date.replaceChars["en"].shortDays[this.getDay()]; },
    j: function() { return this.getDate(); },
    l: function() { return Date.replaceChars["en"].longDays[this.getDay()]; },
    N: function() { return this.getDay() + 1; },
    S: function() { return (this.getDate() % 10 == 1 && this.getDate() != 11 ? 'st' : (this.getDate() % 10 == 2 && this.getDate() != 12 ? 'nd' : (this.getDate() % 10 == 3 && this.getDate() != 13 ? 'rd' : 'th'))); },
    w: function() { return this.getDay(); },
    z: function() { var d = new Date(this.getFullYear(),0,1); return Math.ceil((this - d) / 86400000); }, // Fixed now
    // Week
    W: function() { var d = new Date(this.getFullYear(), 0, 1); return Math.ceil((((this - d) / 86400000) + d.getDay() + 1) / 7); }, // Fixed now
    // Month
    F: function() { return Date.replaceChars["en"].longMonths[this.getMonth()]; },
    m: function() { return (this.getMonth() < 9 ? '0' : '') + (this.getMonth() + 1); },
    M: function() { return Date.replaceChars["en"].shortMonths[this.getMonth()]; },
    n: function() { return this.getMonth() + 1; },
    t: function() { var d = new Date(); return new Date(d.getFullYear(), d.getMonth(), 0).getDate() }, // Fixed now, gets #days of date
    // Year
    L: function() { var year = this.getFullYear(); return (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)); },   // Fixed now
    o: function() { var d  = new Date(this.valueOf());  d.setDate(d.getDate() - ((this.getDay() + 6) % 7) + 3); return d.getFullYear();}, //Fixed now
    Y: function() { return this.getFullYear(); },
    y: function() { return ('' + this.getFullYear()).substr(2); },
    // Time
    a: function() { return this.getHours() < 12 ? 'am' : 'pm'; },
    A: function() { return this.getHours() < 12 ? 'AM' : 'PM'; },
    B: function() { return Math.floor((((this.getUTCHours() + 1) % 24) + this.getUTCMinutes() / 60 + this.getUTCSeconds() / 3600) * 1000 / 24); }, // Fixed now
    g: function() { return this.getHours() % 12 || 12; },
    G: function() { return this.getHours(); },
    h: function() { return ((this.getHours() % 12 || 12) < 10 ? '0' : '') + (this.getHours() % 12 || 12); },
    H: function() { return (this.getHours() < 10 ? '0' : '') + this.getHours(); },
    i: function() { return (this.getMinutes() < 10 ? '0' : '') + this.getMinutes(); },
    s: function() { return (this.getSeconds() < 10 ? '0' : '') + this.getSeconds(); },
    u: function() { var m = this.getMilliseconds(); return (m < 10 ? '00' : (m < 100 ?'0' : '')) + m; },
    // Timezone
    e: function() { return "Not Yet Supported"; },
    I: function() {
        var DST = null;
            for (var i = 0; i < 12; ++i) {
                    var d = new Date(this.getFullYear(), i, 1);
                    var offset = d.getTimezoneOffset();

                    if (DST === null) DST = offset;
                    else if (offset < DST) { DST = offset; break; }                     else if (offset > DST) break;
            }
            return (this.getTimezoneOffset() == DST) | 0;
        },
    O: function() { return (-this.getTimezoneOffset() < 0 ? '-' : '+') + (Math.abs(this.getTimezoneOffset() / 60) < 10 ? '0' : '') + (Math.abs(this.getTimezoneOffset() / 60)) + '00'; },
    P: function() { return (-this.getTimezoneOffset() < 0 ? '-' : '+') + (Math.abs(this.getTimezoneOffset() / 60) < 10 ? '0' : '') + (Math.abs(this.getTimezoneOffset() / 60)) + ':00'; }, // Fixed now
    T: function() { var m = this.getMonth(); this.setMonth(0); var result = this.toTimeString().replace(/^.+ \(?([^\)]+)\)?$/, '$1'); this.setMonth(m); return result;},
    Z: function() { return -this.getTimezoneOffset() * 60; },
    // Full Date/Time
    c: function() { return this.format("Y-m-d\\TH:i:sP"); }, // Fixed now
    r: function() { return this.toString(); },
    U: function() { return this.getTime() / 1000; }
  }
};

//--------------------------------------------------------------------------------

Date.isLeapYear = function(iYear) {
  return ((iYear % 4 == 0) && (iYear % 100 || !(iYear % 400))) ? true : false;
};

Date.getPattern = function(Type) {
  var lowerType = Type.toLowerCase();
  return (this.patterns[lowerType])?Date.patterns[lowerType]:Type;
};

Date.daysInMonth = function() {
  var year = this.getFullYear();
  var month = this.getMonth();
  return 32 - new Date(year, month, 32).getDate();
};

function getPeriod(scale, date) {
  
  if (scale == "year")
    return date.getFullYear();
  
  if (scale == "month")
    return date.getMonth();

  if (scale == "quarter")
    return Math.round(date.getMonth()/4);

  if (scale == "days")
    return date.daysInMonth();

  return 
}

function getScaleOrder(scale) {
  if (scale == "year") return 0;
  if (scale == "quarter") return 1;
  if (scale == "month") return 2;
  if (scale == "week") return 3;
  if (scale == "day") return 4;
  if (scale == "hour") return 5;
  if (scale == "minute") return 6;
  if (scale == "second") return 7;
  return -1; 
}

function getScale(order) {
  if (scale == 0) return "year";
  if (scale == 1) return "quarter";
  if (scale == 2) return "month";
  if (scale == 3) return "week";
  if (scale == 4) return "day";
  if (scale == 5) return "hour";
  if (scale == 6) return "minute";
  if (scale == 7) return "second";
  return -1; 
}

function getScaleForLevel(levelCode) {
  if (levelCode == "year") return "year";
  if (levelCode == "quarter") return "month";
  if (levelCode == "day_week") return "day";
  if (levelCode == "working_day") return "day";
  if (levelCode == "month") return "day";
  if (levelCode == "month_year") return "day";
  if (levelCode == "week") return "day";
  if (levelCode == "day") return "hour";
  if (levelCode == "hour") return "minute";
  if (levelCode == "second") return "second";
  return "year"; 
}

function RequestException (conn,response,options){
  alert(response.responseText);
};

function InternalException (e,dOp,dState){
  alert(e.message);
};

(function ($) {
  var counter = 0;
  $.fn.getStyleObject = function(){
      var dom = this.get(0);
      var style;
      var returns = {};
      if(window.getComputedStyle){
          var camelize = function(a,b){
              return b.toUpperCase();
          };
          style = window.getComputedStyle(dom, null);
          for(var i = 0, l = style.length; i < l; i++){
              var prop = style[i];
              var camel = prop.replace(/\-([a-z])/g, camelize);
              var val = style.getPropertyValue(prop);
              returns[camel] = val;
          };
          return returns;
      };
      if(style = dom.currentStyle){
          for(var prop in style){
              returns[prop] = style[prop];
          };
          return returns;
      };
      return this.css();
  };
  
}(jQuery));

LibraryKey = new Object();

LibraryKey.generateKey = function(dimension, level) {
  var key = Context.Config.KeyTemplate;
  key = key.replace("%d", dimension);
  key = key.replace("%l", level);
  return key;
};

LibraryKey.getFirstCode = function(key) {
  var keyArray = key.split(Context.Config.KeySeparator);
  
  if (keyArray.length < 1)
    return null;

  return keyArray[0];
};

LibraryKey.getSecondCode = function(key) {
  var keyArray = key.split(Context.Config.KeySeparator);
  
  if (keyArray.length == 1)
    return key;

  if (keyArray.length < 2)
    return null;

  return keyArray[1];
};

LibraryKey.getDimension = function(key) {
  return LibraryKey.getFirstCode(key);
};

LibraryKey.getLevel = function(key) {
  return LibraryKey.getSecondCode(key);
};

LibraryKey.getHierarchy = function(key) {
  return LibraryKey.getLevel(key);
};

LibraryKey.getMeasure = function(key) {
  return LibraryKey.getFirstCode(key);
};

LibraryKey.getOperator = function(key) {
  return LibraryKey.getSecondCode(key);
};

function CommandInfo(link) {

  this.operation = null;
  this.parameters = new Array();

  if (link != null) this.proceslink(link);
};

//---------------------------------------------------------------------
CommandInfo.prototype.proceslink = function(link) {

  link = link.replace(/%20/g, "");
  link = link.replace(/%28/g, "(");
  link = link.replace(/%29/g, ")");
  
  var leftBracketPos = link.indexOf("(");
  var rightBracketPos = link.indexOf(")");

  if (leftBracketPos == -1) {
    this.operation = link;
    return true;
  }
  
  var parameters = link.substring(leftBracketPos+1,rightBracketPos);
  parameters = parameters.replace(/\//g, "@bar45@");
  link = link.substring(0,leftBracketPos+1) + parameters + link.substring(rightBracketPos);

  while ((iPos=link.indexOf("/")) != -1) {
    link = link.substring(iPos+1, link.length);
  }

  if ((iPos=link.indexOf("(")) == -1) {
    this.operation = link;
    return true;
  }

  if (link.substring(link.length-1,link.length) != ")") return false;

  this.operation = link.substring(0,iPos);

  link = link.substring(iPos+1,link.length-1);
  link = link.replace(/@bar45@/g, "/");
  var result = link.split(',');
  this.parameters = new Array();
  for (var iPos=0; iPos<result.length; iPos++) {
    this.parameters.push(unescape(result[iPos]));
  }
};

//---------------------------------------------------------------------
CommandInfo.prototype.getOperation = function() { 
	return this.operation; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.setOperation = function(operation) { 
  this.operation = operation; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.getParameters = function() { 
  return this.parameters; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.addParameter = function(parameter) { 
  this.parameters.push(parameter); 
};

//---------------------------------------------------------------------
CommandInfo.prototype.setParameters = function(parameters) { 
  this.parameters = parameters; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.getLink = function(operation, parameters) { 
  var result = operation + "(";
  var parameters = "";

  parameters.each(function(parameter) {
    parameters += parameter + ",";
  }, this);

  if (parameters != "") result += parameters.substring(0, parameters.length-1);

  return result + ")";
};

CommandDispatcher = new Object();
CommandDispatcher.History = new Object();

CommandDispatcher.mainTitle = " - " + parent.document.title;

CommandDispatcher.dispatch = function(command, DOMItem) {
  var commandInfo = new CommandInfo(command);
  var operation = commandInfo.getOperation();
  var parameters = commandInfo.getParameters();
  var useHistory = true;
  
  if (DOMItem && (DOMItem.id == null || DOMItem.id == "")) DOMItem.id = generateId();

  State.lastCommand.Name = operation;
  State.lastCommand.Id = parameters[0];

  if (useHistory && CommandFactory.useHistory(command))
    this.History.addCommand(command, DOMItem);
  else
    this.execute(command, DOMItem);
};

CommandDispatcher.execute = function(command, DOMItem, args) {

  try {
    var Action = CommandFactory.getAction(command);
    if (Action == null) return;
    Action.DOMItem = DOMItem;
    if(args) {
      for(var index in args)
        Action[index] = args[index];
    }
    Action.onTerminate = function() {
      //parent.document.title = Desktop.Main.Center.Body.PageControl.getActivePage().title + CommandDispatcher.mainTitle; 
    };
    Action.execute();
  }
  catch(e){ 
    InternalException(e,Object.inspect(this));
    Desktop.hideProgress();
    Desktop.hideReports();
  }
};

CommandDispatcher.History.addCommand = function(command, DOMItem){
  var DOMFrame = $("#history");
  var monetCommand = Context.Pages.History + "?command=" + command + "&item=" + ((DOMItem)?DOMItem.id:null);
  DOMFrame.monetCommand = monetCommand;
  frames["history"].location = monetCommand;
};

CommandDispatcher.History.executeCommand = function(){
  if (!Application.isRunning) return;
  var DOMFrame = $("#history");
  var link = frames["history"].location.href;
  var DOMItem, linkArray;
  var pos;
  
  if (link == null) link = DOMFrame.monetCommand;

  pos = link.indexOf("?command=");
  if (pos == -1) return;

  link = link.substring(pos+String("?command=").length);
  linkArray = link.split("&item=");
  DOMItem = $(linkArray[1]);
  CommandDispatcher.execute(linkArray[0], DOMItem);
};

function executeMonetCommand(command) {
  CommandDispatcher.execute(command, null);
};

CommandFactory = new Object;
CommandFactory.list = new Array();

CommandFactory.register = function(classAction, parameters, useHistory) {
  if (!classAction) return;
  if (!classAction.prototype.constructor) return;
  var className = this.getClassName(classAction);
  var operation = String(className.substring(String("Action").length)).toLowerCase();
  if (parameters == null) parameters = new Object;
  this.list[operation] = {
    classAction: classAction,
    parameters: parameters,
    useHistory: useHistory
  };
};

CommandFactory.getClassName = function(classAction) {
  var data = classAction.constructor.toString().match(/function\s*(\w+)/);
  if (data && data.length == 2) return data[1];
};

CommandFactory.getAction = function(command){
  var Command = this.parseCommand(command);
  var Item = this.list[Command.operation];
  if (Item == null) return null;
  var Action = new Item.classAction;
  Action.ClassName = this.getClassName(Item.classAction);
  for (var index in Command.parameters) Action[index] = Command.parameters[index];
  Action.OptionalParameters = Command.optionalParameters;
  return Action;
};

CommandFactory.useHistory = function(command) {
  var Command = this.parseCommand(command);
  var Item = this.list[Command.operation];
  if (Item == null) return;
  return Item.useHistory;
};

CommandFactory.parseCommand = function(command) {
  var Command = new Object;

  command = command.replace(/%28/g, "(");
  command = command.replace(/%29/g, ")");

  while ((pos = command.indexOf("/")) != -1) {
    var bracketPos = command.indexOf("(");
    if (pos < bracketPos) command = command.substring(pos+1, command.length);
    else command = command.substring(0,pos, command.length) + "#@@@@#" + command.substring(pos+1, command.length);
  }
  command = command.replace(/#@@@@#/g, "/");

  if ((pos=command.indexOf("(")) == -1) {
    Command.operation = command;
    return Command;
  }

  if (command.substring(command.length-1,command.length) != ")") return false;

  Command.operation = command.substring(0,pos);

  command = command.substring(pos+1,command.length-1);
  
  var i = 0;
  var resultArray = null;
  var aJsonParameters = new Array();
  while ((resultArray = command.match(/\{.*\}/)) != null) {
    aJsonParameters[i] = resultArray[0];
    command = command.replace(resultArray[0], "#" + i + "#");
    i++;
  }
  
  var result = command.split(',');
  for (var i=0; i<aJsonParameters.length; i++) {
    for (var j=0; j<result.length; j++) {
      if (result[j] == "#" + i + "#")
        result[j] = aJsonParameters[i];
    }
  }
  
  var Item = this.list[Command.operation];
  var requiredParameters = new Array();
  Command.parameters = new Object;
  Command.optionalParameters = new Array();
  for (var key in Item.parameters) {
    var index = Item.parameters[key];
    if (result[index] == null) continue;
    Command.parameters[key] = utf8Decode(result[index]);
    requiredParameters[index] = index;
  }
  for (var index in result) {
    if (requiredParameters[index] != null) continue;
    Command.optionalParameters.push(utf8Decode(result[index]));
  }
  return Command;
};

function Process(numStates) {
	this.state = 0;
	this.numStates = numStates;
	this.terminated = false;
	this.success = false;
	this.failureMessage = "";
	this.refreshTaskList = null;
};

Process.prototype.checkOption = function (ButtonResult) {
	if (ButtonResult == "yes") {
		this.execute();
	}
	else this.terminate();
};

Process.prototype.isFirstStep = function () {
	return this.state == 0;
};

Process.prototype.isLastStep = function () {
	return this.state == this.numStates;
};

Process.prototype.gotoStep = function (state) {
	if (state > this.numStates) return;
	this.state = state - 1;
	this.execute();
};

Process.prototype.resetState = function () {
	this.state = 0;
};

Process.prototype.restart = function () {
	this.state = 0;
	this.execute();
};

Process.prototype.terminate = function () {
	this.state = this.numStates;
	this.execute();
};

Process.prototype.terminateOnSuccess = function () {
	this.success = true;
	this.terminate();
};

Process.prototype.terminateOnFailure = function (failureMessage) {
	if (failureMessage != null) this.failureMessage = failureMessage;
	this.success = false;
	this.terminate();
};

Process.prototype.getFailure = function () {
	return this.failureMessage;
};

Process.prototype.success = function () {
	return this.success;
};

Process.prototype.addRefreshTask = function (Type, Target, Sender) {
	if (this.refreshTaskList == null) this.refreshTaskList = new RefreshTaskList();
	var refreshTask = new RefreshTask(Type, Target);
	if (Sender != null) refreshTask.setSender(Sender);
	this.refreshTaskList.addRefreshTask(refreshTask);
};

Process.prototype.doRefreshTaskList = function () {
	if (this.refreshProcessClass == null) return true;
	if (this.refreshTaskList == null) return true;
	this.refreshProcess = new this.refreshProcessClass;
	this.refreshProcess.refreshTaskList = this.refreshTaskList;
	this.refreshProcess.execute();
};

Process.prototype.execute = function () {
	if (this.terminated) return;

	if (this.isLastStep()) {
		this.terminated = true;
		this.doRefreshTaskList();
		if (this.returnProcess) this.returnProcess.execute();
		return;
	}

	var State = this.getNextState();
	try {
		this.method = State.nextMethod;
		this.method();
	}
	catch (e) {
		Kernel.registerException(e);
	}

};

Process.prototype.getNextState = function () {
	if (this.state > this.numStates) return false;

	var state = this.sState;
	this.state++;


	var State = {
		iProgress: Math.round((state / this.numStates) * 100) / 100,
		nextMethod: this["step_" + this.state]
	};

	return State;
};

Process.prototype.onSuccess = function () {
	this.execute();
};

Process.prototype.generateMessage = function (sMessage, Variables) {
	var template = new Template(sMessage);
	return template.evaluate(Variables);
};

Process.prototype.getErrorMessage = function (sMessage, failureMessage) {
	failureMessage = (failureMessage != null) ? failureMessage.substr(failureMessage.indexOf(":") + 1) : "";
	return sMessage.replace("#response#", failureMessage);
};

Process.prototype.onFailure = function (failureMessage) {
	alert(failureMessage);
	this.terminate();
};

Process.prototype.getDashboard = function (modelData) {
	var dashboard = jQuery.parseJSON(this.data);

	var model = null;
	eval(dashboard.model);

	dashboard.model = model;

	return dashboard;
};

Process.prototype.getChartApi = function (chartApiData) {
	var ChartApi = null;
	eval(chartApiData);
	return ChartApi;
};

Process.prototype.updateColors = function () {
	var indicatorList = State.indicatorList;
	var compareList = State.compareList;
	var keys = [];
	var colors = [];
	var context = null;

	if (compareList.size() > 0 && indicatorList.size() == 1) {
		keys = compareList.getKeys();
		context = View.ColorContext.COMPARE;
	}
	else {
		keys = indicatorList.getKeys();
		context = View.ColorContext.INDICATORS;
	}

	for (var i = 0; i < keys.length; i++) {
		var color = View.getColor(context, keys[i]);
		colors.push(color);
	}

	State.colors = colors;
};

function Action(numStates) {
  this.base = Process;
  this.state = 0;
  this.numStates = numStates;
  this.loadingMessage = "";
  this.executed = false;
  this.availableProcessClass = null;
};

Action.prototype = new Process;
Action.constructor = Action;

Action.prototype.resetState = function(){
  this.state = 0;
  Desktop.hideReports();
};

Action.prototype.terminate = function(){
  this.state = this.numStates;
  this.execute();
};

Action.prototype.available = function(){
  if (this.availableProcessClass == null) return true;

  if (this.availableProcess) {
    if (this.availableProcess.success()) { return true; }
    else {
      this.terminate();
      return false;
    }
  }

  this.availableProcess = new this.availableProcessClass;
  this.availableProcess.returnProcess = this;
  this.availableProcess.execute();

  return false;
};

Action.prototype.execute = function(){
  if (this.terminated) return;

  if (this.isFirstStep()) {
    if (! this.available()) return;
  }

  if (this.isLastStep()) {
    this.terminated = true;
    this.doRefreshTaskList();
    if (this.returnProcess) this.returnProcess.execute();
    if (this.onTerminate) this.onTerminate();
    return;
  }

  var State = this.getNextState();    
  try { 
    this.method = State.nextMethod;
    this.method();
  }
  catch(e){ 
    Kernel.registerException(e);
  }
   
};

Action.prototype.onFailure = function(response){
  response = response.substring(response.indexOf(":")+1);

  if (this.returnProcess) {
    this.returnProcess.onFailure(response);
    this.terminate();
  }
  else Desktop.reportError(response);

  this.terminate();
};

View = function() {
  this.DOMLayer = null;
  this.state = null;
};

View.colors = new Object();
View.accountView = null;
View.colorsIndex = -1;

View.ACCOUNT = "::id::_account";
View.DASHBOARDS = "::id::_dashboardlist";
View.DASHBOARD = "::id::_dashboard";
View.DASHBOARD_INFO = "::id::_dashboardinfo";
View.INDICATORS = "::id::_indicators";
View.MEASURE_UNITS = "::id::_measureunits";
View.COMPARE = "::id::_compare";
View.INDICATOR = "::id::_indicator";
View.TAXONOMIES = "::id::_taxonomies";
View.CHART = "::id::_chart_::type::";

View.ColorContext = new Object();
View.ColorContext.INDICATORS = "indicators";
View.ColorContext.COMPARE = "compare";

View.prototype.initTitles = function(DOMLayer) {
  var jLayer = $(this.DOMLayer);
  var jBlocks = jLayer.find(".block");
  var hasBlocks = jBlocks.length > 0;
  
  if (!hasBlocks) {
    jLayer.find("label:first").click(View.prototype.atTitleClick.bind(this));
    return;
  }
  
  for (var i=0; i<jBlocks.length; i++) {
    var jBlock = $(jBlocks[i]);
    jBlock.find("label:first").click(View.prototype.atTitleClick.bind(this, jBlock));
  }
};

View.prototype.init = function(DOMLayer) {
  this.DOMLayer = DOMLayer;
  this.initTitle();
};

View.prototype.initHeader = function() {
  var jHeader = $(this.DOMLayer).find(".header");
  
  var DOMFederationImage = jHeader.find(".federation.image").get(0);
  DOMFederationImage.src = Context.Config.FederationLogoUrl;
  DOMFederationImage.title = Context.Config.FederationLabel;
  DOMFederationImage.alt = Context.Config.FederationLabel;
  
  var DOMSpaceLabel = jHeader.find(".subtitle").get(0);
  DOMSpaceLabel.innerHTML = Context.Config.SpaceLabel;
  
  var DOMModelLabel = jHeader.find(".title").get(0);
  DOMModelLabel.innerHTML = Context.Config.ModelLabel;
  
  CommandListener.capture(jHeader.get(0));
  
  this.initAccountView();
};

View.prototype.initAccountView = function() {
  var jHeader = $(this.DOMLayer).find(".header");
  var jUserView = jHeader.find(".accountview");
  
  if (jUserView == null)
    return;
  
  if (this.accountView == null) {
    this.accountView = new ViewAccount();
    this.accountView.id = View.ACCOUNT.replace("::id::", State.account.id);
    this.accountView.target = State.account;
    this.accountView.init(jUserView.get(0));
  }
};

View.prototype.enableColors = function() {
  $(this.DOMLayer).removeClass("colorsoff");
};

View.prototype.disableColors = function() {
  $(this.DOMLayer).addClass("colorsoff");
};

View.getColor = function(context, id) {
  if (!View.colors[context]) return null;
  return View.colors[context][id];
};

View.freeColors = function(context) {
  View.colors[context] = new Object();
};

View.freeColor = function(context, id) {
  if (!View.colors[context]) return;
  delete View.colors[context][id];
};

View.getColorsIndex = function() {
  View.colorsIndex++;
  return View.colorsIndex;
};

View.retainColor = function(context, id) {
  if (!View.colors[context]) View.colors[context] = new Object();
  View.colors[context][id] = GraphUtil.getColor(View.getColorsIndex());
  return View.colors[context][id];
};

View.setColor = function(context, id, color) {
  if (!View.colors[context]) View.colors[context] = new Object();
  View.colors[context][id] = color;
};

View.setColors = function(colors) {
  View.colors = colors;
};

View.prototype.show = function() {
  if (!this.DOMLayer) return;
  $(this.DOMLayer).show();
};

View.prototype.hide = function() {
  if (!this.DOMLayer) return;
  $(this.DOMLayer).hide();
};

View.prototype.setState = function(state) {
  this.state = state;
};

View.prototype.refresh = function() {
  this.refreshHeader();
};

View.prototype.refreshHeader = function() {
  if (this.accountView != null)
    this.accountView.refresh();
};

View.prototype.destroy = function() {
  $(this.DOMLayer).remove();
};

View.prototype.getRange = function(timeLapse, freeTimeLapseWindowSize) {
  if (timeLapse == null) timeLapse = this.state.timeLapse;
  var bounds = TimeLapse.getBounds(timeLapse, this.target.dashboard.model.range);
  var windowSize = TimeLapse.getWindowSize(timeLapse, bounds, freeTimeLapseWindowSize);
  return TimeLapse.getValues(timeLapse, windowSize, bounds);
};

View.prototype.toggleColorsDialog = function(context, id, DOMLocation) {
  if (this.colourPicker == null || !this.colourPicker.isVisible())
    this.showColorsDialog(context, id, DOMLocation);
  else
    this.hideColorsDialog();
};

View.prototype.showColorsDialog = function(context, id, DOMLocation) {
  
  if (this.jColorPicker == null) {
    var jLayer = $('#jquery-colour-picker-example select');
    this.colourPicker = jLayer.colourPicker({});
    this.colourPicker.onSelect = View.prototype.atChangeColor.bind(this, context, id);
  }
  
  var jLocation = $(DOMLocation);
  var pos = jLocation.offset();
  this.colourPicker.render(pos.left-jLocation.width(), pos.top);
};

View.prototype.hideColorsDialog = function() {
  this.colourPicker.close();
};

View.prototype.toggleView = function(jBlock) {
};

View.prototype.onResize = function() {
};

View.prototype.atChangeColor = function(context, id, color) {
  this.updateColor(context, id, color);
};

View.prototype.atTitleClick = function(jBlock) {
  this.toggleView(jBlock);
};

ViewAccount = function() {
  this.base = View;
  this.base();
};

ViewAccount.prototype = new View;

ViewAccount.prototype.init = function(DOMLayer) {
  var jLayer = $(DOMLayer);

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewaccount, Lang.ViewAccount);
  jLayer.addClass("view account");
  
  if (!this.target) return;

  this.addBehaviours();
};

ViewAccount.prototype.addBehaviours= function() {
  var jLayer = $(this.DOMLayer);
  var jUsername = jLayer.find("span.username");
  
  jUsername.click(ViewAccount.prototype.atUsernameClick.bind(this));
  
  CommandListener.capture(this.DOMLayer);
};

ViewAccount.prototype.refresh = function() {
  $("span.username .label").html(this.target.user.info.fullname);
  this.refreshEnvironments();
  this.refreshDashboards();
  this.refreshUnits();
};

ViewAccount.prototype.refreshEnvironments = function() {
  var jLayer = $(this.DOMLayer);
  var environmentTemplate = translate(AppTemplate.viewaccountenvironment, Lang.ViewAccount);
  var jEnvironments = jLayer.find("ul.environments");
  
  jEnvironments.html("");
  for (var i=0; i<this.target.environments.length; i++) {
    var environment = this.target.environments[i];
    environment.command = "showenvironment(" + environment.id + ")";
    var jEnvironment = $.tmpl(environmentTemplate, environment);
    jEnvironments.append(jEnvironment);
  }
  
  CommandListener.capture(jEnvironments.get(0));
};

ViewAccount.prototype.refreshDashboards = function() {
  var jLayer = $(this.DOMLayer);
  var dashboardTemplate = translate(AppTemplate.viewaccountdashboard, Lang.ViewAccount);
  var jDashboards = jLayer.find("ul.dashboards");
  
  jDashboards.html("");
  for (var i=0; i<this.target.dashboards.length; i++) {
    var dashboard = this.target.dashboards[i];
    
    dashboard.fullLabel = dashboard.label + (dashboard.active?" " + Lang.ViewAccount.Current:"");
    dashboard.anchorTitle = dashboard.active?dashboard.label:Lang.ViewAccount.StartSessionWith + " " + dashboard.label;
    dashboard.disabledLabel = dashboard.disabled?"disabled":"";
    dashboard.command = dashboard.disabled?"javascript:void(null)":"toggledashboard(" + dashboard.id + ")";
    
    var jDashboard = $.tmpl(dashboardTemplate, dashboard);
    jDashboards.append(jDashboard);
  }
  
  CommandListener.capture(jDashboards.get(0));
};

ViewAccount.prototype.refreshUnits = function() {
  var jLayer = $(this.DOMLayer);
  var unitTemplate = translate(AppTemplate.viewaccountunit, Lang.ViewAccount);
  var jUnits = jLayer.find("ul.units");
  
  jUnits.html("");
  for (var i=0; i<this.target.units.length; i++) {
    var unit = this.target.units[i]; 
      
    unit.fullLabel = unit.label + (unit.active?" " + Lang.ViewAccount.Current:"");
    unit.anchorTitle = unit.active?unit.label:Lang.ViewAccount.GotoUnit + " " + unit.label;
    unit.disabledLabel = unit.disabled?"disabled":"";
    unit.command = unit.disabled?"javascript:void(null)":"showunit(" + unit.id + "," + unit.url + ")";
      
    var jUnit = $.tmpl(unitTemplate, unit);
    jUnits.append(jUnit);
  }
  
  CommandListener.capture(jUnits.get(0));
};

ViewAccount.prototype.isUsernamePanelActive = function() {
  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");
  return jUsernamePanel.hasClass("active");
};

ViewAccount.prototype.showUsernamePanel = function() {
  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");
  
  jUsernamePanel.addClass("active");
  $(document.body).bind("click", ViewAccount.prototype.hideUsernamePanel.bind(this));
};

ViewAccount.prototype.hideUsernamePanel = function() {
  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");

  jUsernamePanel.removeClass("active");
  $(document.body).unbind("click", ViewAccount.prototype.hideUsernamePanel.bind(this));
};

//************************************************************************************************************

ViewAccount.prototype.atUsernameClick = function(event) {
  event.stopPropagation();

  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");
  
  if (this.isUsernamePanelActive()) this.hideUsernamePanel();
  else this.showUsernamePanel();
};

//----------------------------------------------------------------------
// Action init
//----------------------------------------------------------------------
function ActionInit() {
  this.base = Action;
  this.base(2);
};

ActionInit.prototype = new Action;
ActionInit.constructor = ActionInit;
CommandFactory.register(ActionInit, null, false);

ActionInit.prototype.onFailure = function(response){
  var action = new ActionLogout();
  action.execute();

  Desktop.hideLoading();
  alert(Lang.InitApplication);

  this.terminate();
};

ActionInit.prototype.step_1 = function() {
  Kernel.loadAccount(this);
};

ActionInit.prototype.step_2 = function() {
  State.account = jQuery.parseJSON(this.data);
  
  Desktop.hideLoading();
  Desktop.refresh();
  CommandListener.throwCommand(Context.Config.Command);
};

//----------------------------------------------------------------------
// Action show home
//----------------------------------------------------------------------
function ActionShowHome() {
  this.base = Action;
  this.base(1);
};

ActionShowHome.prototype = new Action;
ActionShowHome.constructor = ActionShowHome;
CommandFactory.register(ActionShowHome, null, false);

ActionShowHome.prototype.step_1 = function() {
  var rootDashboard = State.account.rootdashboard;
  CommandListener.throwCommand("showdashboard(" + rootDashboard.id + ")");
};

//----------------------------------------------------------------------
// Save Account
//----------------------------------------------------------------------
function ProcessSaveAccount () {
  this.base = Process;
  this.base(2);
};

ProcessSaveAccount.prototype = new Process;
ProcessSaveAccount.constructor = ProcessSaveAccount;

ProcessSaveAccount.prototype.step_1 = function() {
  Kernel.saveAccount(this, this.account);
};

ProcessSaveAccount.prototype.step_2 = function() {
  this.terminateOnSuccess();
};

var Serializer = new Object();
Serializer.ITEM_SEPARATOR      = "#";
Serializer.ITEM_CODE_SEPARATOR = "=";

Serializer.serializeAccount = function(account) {
  var result = "\"id\":\"" + account.id + "\",";
  result += "\"user\":" + Serializer.serializeAccountUser(account.user);
  return "{" + result + "}";
};

Serializer.serializeAccountUser = function(user) {
  var result = "\"id\":\"" + user.id + "\",";
  result += "\"language\":\"" + user.language + "\",";
  result += "\"info\":" + Serializer.serializeAccountUserInfo(user.info);
  return "{" + result + "}";
};

Serializer.serializeAccountUserInfo = function(info) {
  var result = "\"photo\":\"" + info.photo + "\",";
  result += "\"fullname\":\"" + info.fullname + "\",";
  result += "\"preferences\":\"" + info.preferences + "\",";
  result += "\"email\":\"" + info.email + "\",";
  result += "\"occupations\":\"" + info.occupations + "\"";
  return "{" + result + "}";
};

Serializer.serializeMap = function(map) {
  var result = "";
  
  if (map == null) return "";
  
  for (var key in map) {
    if (isFunction(map[key])) continue;
    var value = map[key];
    result += key + Serializer.ITEM_CODE_SEPARATOR + value + Serializer.ITEM_SEPARATOR;
  }
  
  if (result.length > 0)
    result = result.substring(0, result.length-1);
  
  return result;
};

Serializer.deserializeMap = function(data) {
  var result = new Array();
  
  if (data == null || data == "") return result;
  
  var itemsArray = data.split(Serializer.ITEM_SEPARATOR);
  for (var i=0; i<itemsArray.length; i++) {
    var itemArray = itemsArray[i].split(Serializer.ITEM_CODE_SEPARATOR);
    if (itemArray.length == 0)
      continue;
    result[itemArray[0]] = (itemArray.length > 1) ? itemArray[1] : "";
  }
  
  return result;
};

//----------------------------------------------------------------------
// Show Environment
//----------------------------------------------------------------------
function ActionShowEnvironment() {
  this.base = Action;
  this.base(2);
};

ActionShowEnvironment.prototype = new Action;
ActionShowEnvironment.constructor = ActionShowEnvironment;
CommandFactory.register(ActionShowEnvironment, { id : 0 }, false);

ActionShowEnvironment.prototype.step_1 = function(){
  var account = State.account;
  var preferences = Serializer.deserializeMap(account.user.info.preferences);
  
  preferences["rootNode"] = this.id;
  account.user.info.preferences = Serializer.serializeMap(preferences);
  
  var process = new ProcessSaveAccount();
  process.account = account;
  process.returnProcess = this;
  process.execute();
};

ActionShowEnvironment.prototype.step_2 = function() {
  window.location.href = Context.Config.OfficeUrl;
};

//----------------------------------------------------------------------
// Toggle Dashboard
//----------------------------------------------------------------------
function ActionToggleDashboard() {
  this.base = Action;
  this.base(2);
};

ActionToggleDashboard.prototype = new Action;
ActionToggleDashboard.constructor = ActionToggleDashboard;
CommandFactory.register(ActionToggleDashboard, { id : 0 }, false);

ActionToggleDashboard.prototype.step_1 = function() {
  var account = State.account;

  if (this.id == account.rootdashboard.id) {
    this.terminate();
    return;
  }

  var preferences = Serializer.deserializeMap(account.user.info.preferences);
  preferences["rootDashboard"] = this.id;
  account.user.info.preferences = Serializer.serializeMap(preferences);
  
  var process = new ProcessSaveAccount();
  process.account = account;
  process.returnProcess = this;
  process.execute();
};

ActionToggleDashboard.prototype.step_2 = function() {
  window.location.href = Context.Config.Url;
};

//----------------------------------------------------------------------
// Show Unit
//----------------------------------------------------------------------
function ActionShowUnit() {
  this.base = Action;
  this.base(1);
};

ActionShowUnit.prototype = new Action;
ActionShowUnit.constructor = ActionShowUnit;
CommandFactory.register(ActionShowUnit, { id : 0, url : 1 }, false);

ActionShowUnit.prototype.step_1 = function() {

  if (this.id == Context.Config.BusinessUnit) {
    this.terminate();
    return;
  }
  
  window.location.href = this.url;
};

//----------------------------------------------------------------------
// Logout
//----------------------------------------------------------------------
function ActionLogout() {
  this.base = Action;
  this.base(2);
};

ActionLogout.prototype = new Action;
ActionLogout.constructor = ActionLogout;
CommandFactory.register(ActionLogout, null, false);

ActionLogout.prototype.step_1 = function(){
  Kernel.logout(this, State.account.instanceId);
};

ActionLogout.prototype.step_2 = function(){
  var location = Context.Config.EnterpriseLoginUrl;
  if (location == null || location == "") location = Context.Config.Url;

  State.logout = true;
  this.terminate();

  reload(location);
};

//----------------------------------------------------------------------
// Action show dashboard list
//----------------------------------------------------------------------
function ActionShowDashboardList() {
	this.base = Action;
	this.base(3);
};

ActionShowDashboardList.prototype = new Action;
ActionShowDashboardList.constructor = ActionShowDashboardList;
CommandFactory.register(ActionShowDashboardList, null, false);

ActionShowDashboardList.prototype.step_1 = function () {
	Kernel.loadDashboardList(this);
};

ActionShowDashboardList.prototype.step_2 = function () {
	var dashboardList = jQuery.parseJSON(this.data);

	var view = new ViewDashboardList();
	view.id = View.DASHBOARDS.replace("::id::", "dashboardlist");
	view.target = { id: "dashboardlist", dashboardList: dashboardList };
	Desktop.addState(view);

	Desktop.cleanViewsContainer();
	view.init(Desktop.createView());
	view.refresh();

	Desktop.registerView(view.id, view);
	Desktop.hideLoading();
	Desktop.hideWestLayer();

	this.terminate();
};

ViewDashboardInfo = function () {
  this.base = View;
  this.base();
};

ViewDashboardInfo.prototype = new View;

ViewDashboardInfo.prototype.init = function(DOMLayer) {
  var jLayer = $(DOMLayer);

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewdashboardinfo, Lang.ViewDashboardInfo);
  jLayer.addClass(CLASS_VIEW_DASHBOARD);
  
  if (!this.target) return;
  
  if (this.state.mode != null)
    jLayer.find(".view.dashboardinfo").addClass(this.state.mode);
  
  CommandListener.capture(this.DOMLayer);
};

ViewDashboardInfo.prototype.clearView = function() {
  var jLayer = $(this.DOMLayer); 
  jLayer.find(".title .timelapse").html("");
  jLayer.find(".title .indicator").html("");
  jLayer.find(".filters").html("");
};

ViewDashboardInfo.prototype.refreshTimeLapse = function() {
  var jLayer = $(this.DOMLayer);
  var free = this.target.freeTimeLapseWindowSize?this.target.freeTimeLapseWindowSize:false;
  var range = this.getRange(null, free);
  var scale = this.state.timeLapse.scale;
  var minDate = new Date(range.min).format(Lang.DateFormats[scale], Context.Config.Language);
  var maxDate = new Date(range.max).format(Lang.DateFormats[scale], Context.Config.Language);

  jLayer.find(".title .timelapse").html(minDate + "&nbsp;-&nbsp;" + maxDate);
};

ViewDashboardInfo.prototype.refreshIndicator = function() {
  var indicatorList = this.state.indicatorList;

  if (indicatorList.size() != 1) 
    return;
  
  var jLayer = $(this.DOMLayer);
  var keys = indicatorList.getKeys();
  var indicator = this.target.dashboard.model.indicatorMap[keys[0]];
  jLayer.find(".title .indicator").html("&nbsp;:&nbsp;" + indicator.label);
};

ViewDashboardInfo.prototype.refreshFilters = function() {
  var filterList = this.state.filterList;

  if (filterList.size() <= 0)
    return;
  
  var jLayer = $(this.DOMLayer);
  var filters = filterList.getAll();
  var filterTemplate = translate(AppTemplate.viewdashboardinfofilter, Lang.ViewDashboardInfo);
  var jFilters = jLayer.find(".filters");
  
  for (var i=0; i<filters.length; i++) {
    var jFilter = $.tmpl(filterTemplate, { label: filters[i].label, comma: (i!=filters.length-1?",&nbsp;":"")});
    var jLink = jFilter.find("a");

    jLink.click(ViewDashboardInfo.prototype.atDeleteFilterClick.bind(this, filters[i].id));
    jFilters.append(jFilter);
  }
};

ViewDashboardInfo.prototype.refresh = function() {
  
  this.clearView();
  
  var indicatorList = this.state.indicatorList;
  if (indicatorList.size() <= 0)
    return;
  
  this.refreshTimeLapse();
  this.refreshIndicator();
  this.refreshFilters();  
};

//************************************************************************************************************
ViewDashboardInfo.prototype.atDeleteFilterClick = function(id) {
  if (this.state.mode != State.Mode.PRINT)
    CommandListener.throwCommand("deletefilter(" + this.target.dashboard.code + "," + id + ")");
};

ViewIndicators = function () {
  this.base = View;
  this.base();
  this.maxMetrics = 2;
  this.maxIndicators = 1;
};

ViewIndicators.prototype = new View;

ViewIndicators.prototype.init = function(DOMLayer) { 

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewindicators, Lang.ViewIndicators);
  
  if (!this.target) return;

  this.initTitles();
  this.addBehaviours();
};

ViewIndicators.prototype.addBehaviours = function() {
  
  var jClearAll = $(this.DOMLayer).find(".clearall");
  jClearAll.click(ViewIndicators.prototype.atClearAllClick.bind(this));
  
  CommandListener.capture(this.DOMLayer);
};

ViewIndicators.prototype.toggleView = function() {
  var jLayer = $(this.DOMLayer);
  var jLabel = jLayer.find("label:first");
  var jFolders = jLayer.find(".folders");
  var value = jLabel.html();

  if (jFolders.is(":visible")) {
    jLabel.html(value.replace("-", "+"));
    jFolders.hide();
  }
  else {
    jLabel.html(value.replace("+", "-"));
    jFolders.show();
  }
};

ViewIndicators.prototype.isIndicatorSelected = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  return jIndicator.find("input").get(0).checked;
};

ViewIndicators.prototype.unselectIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  var jColorBox = jIndicator.find(".colorbox");

  jInput.get(0).checked = false;
  jColorBox.hide();
  
  this.checkIndicators();
};

ViewIndicators.prototype.unselectIndicators = function() {
  var jIndicators = $(this.DOMLayer).find(".indicator");
  
  for (var i=0; i<jIndicators.length; i++) {
    var jIndicator = $(jIndicators[i]);
    var jInput = jIndicator.find("input");
    var jColorBox = jIndicator.find(".colorbox");
  
    jInput.get(0).checked = false;
    jColorBox.hide();
  }
  
  this.checkIndicators();
};

ViewIndicators.prototype.selectIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  var jColorBox = jIndicator.find(".colorbox");
  var color = View.getColor(View.ColorContext.INDICATORS, id);
  
  if (color == null)
    color = View.retainColor(View.ColorContext.INDICATORS, id);

  jInput.get(0).checked = true;
  jColorBox.show();
  jColorBox.css("background-color", color);
  
  this.checkIndicators();
};

ViewIndicators.prototype.disableIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  
  jIndicator.addClass("disabled");
  jInput.get(0).disabled = true;
};

ViewIndicators.prototype.enableIndicator = function(id) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jInput = jIndicator.find("input");
  
  jIndicator.removeClass("disabled");
  jInput.get(0).disabled = false;
};

ViewIndicators.prototype.getSelectedIndicators = function(id) {
  var indicatorList = this.target.dashboard.model.indicatorList;
  var result = {};
  
  for (var i=0; i<indicatorList.length; i++) {
    var indicator = indicatorList[i];
    if (this.isIndicatorSelected(indicator.id))
      result[indicator.id] = indicator.id;
  };
  
  return result;
};

ViewIndicators.prototype.getSelectedMetrics = function(id) {
  var indicatorList = this.target.dashboard.model.indicatorList;
  var result = {};
  
  for (var i=0; i<indicatorList.length; i++) {
    var indicator = indicatorList[i];
    if (this.isIndicatorSelected(indicator.id))
      result[indicator.metric] = indicator.metric;
  };
  
  return result;
};

ViewIndicators.prototype.checkIndicators = function(id) {
  var metrics = this.getSelectedMetrics();
  var indicators = this.getSelectedIndicators();
  var indicatorList = this.target.dashboard.model.indicatorList;
  
  for (var i=0; i<indicatorList.length; i++) {
    var indicator = indicatorList[i];
    
    if (this.maxMetrics == -1 || size(metrics) < this.maxMetrics) {
      if (this.maxIndicators == -1 || size(indicators) < this.maxIndicators)
        this.enableIndicator(indicator.id);
      else {
        if (indicators[indicator.id] != null)
          this.enableIndicator(indicator.id);
        else
          this.disableIndicator(indicator.id);
      }
    }
    else {
      if (metrics[indicator.metric] != null) {
        if (this.maxIndicators == -1 || size(indicators) < this.maxIndicators)
          this.enableIndicator(indicator.id);
        else {
          if (indicators[indicator.id] != null)
            this.enableIndicator(indicator.id);
          else
            this.disableIndicator(indicator.id);
        }
      }
      else this.disableIndicator(indicator.id);
    }
  };
  
  this.refreshToolbar();
};

ViewIndicators.prototype.setMaxMetrics = function(maxMetrics) {
  this.maxMetrics = maxMetrics;
};

ViewIndicators.prototype.setMaxIndicators = function(maxIndicators) {
  this.maxIndicators = maxIndicators;
};

ViewIndicators.prototype.refreshIndicators = function(indicators, jIndicators) {
  var indicatorTemplate = translate(AppTemplate.viewindicatorsitem, Lang.ViewIndicators);
  
  for (var i=0; i<indicators.length; i++) {
    var indicatorKey = indicators[i];
    var indicator = this.target.dashboard.model.indicatorMap[indicatorKey];
    var jIndicator = $.tmpl(indicatorTemplate, { id: indicator.id, label: indicator.label, description: indicator.description });
    var DOMIndicator = jIndicator.get(0);
    var jInput = jIndicator.find("input");
    var jColorBox = jIndicator.find(".colorbox");
    
    DOMIndicator.id = indicator.id;
    
    jColorBox.click(ViewIndicators.prototype.toggleColorsDialog.bind(this, View.ColorContext.INDICATORS, indicator.id, jColorBox.get(0)));
    jInput.change(ViewIndicators.prototype.atIndicatorClick.bind(this, DOMIndicator));
    jIndicators.append(jIndicator);
  }
};

ViewIndicators.prototype.refreshFolder = function(folder, jFoldersContainer) {
  var folderTemplate = translate(AppTemplate.viewindicatorsfolder, Lang.ViewIndicators);
  var jFolder = $.tmpl(folderTemplate, { label: folder.label });
  var jTitle = jFolder.find(".title");
  var jFolders = jFolder.find(".folders");
  var jIndicators = jFolder.find(".indicators");
  
  jFoldersContainer.append(jFolder);

  jTitle.click(ViewIndicators.prototype.atFolderClick.bind(this, jFolder.get(0)));
  this.refreshFolders(folder.folders, jFolder.find(".folders"));
  this.refreshIndicators(folder.indicators, jIndicators);
};

ViewIndicators.prototype.refreshFolders = function(folders, jFoldersContainer) {
  for (var i=0; i<folders.length; i++)
    this.refreshFolder(folders[i], jFoldersContainer);
};

ViewIndicators.prototype.refresh = function() {
  var folderList = this.target.dashboard.model.folderList;
  var jFolders = $(this.DOMLayer).find(".folders");

  //View.freeColors(View.ColorContext.INDICATORS);
  this.refreshToolbar();
  this.refreshFolders(folderList, jFolders);
};

ViewIndicators.prototype.refreshToolbar = function(context, id, color) {
  var jLayer = $(this.DOMLayer);
  var jClearAll = jLayer.find(".clearall");
  var countSelectedIndicators = jLayer.find(".indicator input:checked").length;
  
  if (countSelectedIndicators > 0)
    jClearAll.show();
  else
    jClearAll.hide();
};

ViewIndicators.prototype.updateColor = function(context, id, color) {
  var jIndicator = $(this.DOMLayer).find(".indicator." + id);
  var jColorBox = jIndicator.find(".colorbox");
  
  jColorBox.css("background-color", color);
  
  CommandListener.throwCommand("setcolor(" + this.target.id + "," + context + "," + id + "," + (color.indexOf("#")==-1?"#":"") + color + ")");
};

ViewIndicators.prototype.atFolderClick = function(DOMFolder) {
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  var jFolder = $(DOMFolder);
  jFolder.removeClass("closed");
  if (jFolder.hasClass("opened")) {
    jFolder.removeClass("opened");
    jFolder.addClass("closed");
  }
  else {
    jFolder.addClass("opened");
    jFolder.removeClass("closed");
  }
};

ViewIndicators.prototype.atIndicatorClick = function(DOMIndicator) {
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  CommandListener.throwCommand("toggleindicator(" + this.target.id + "," + DOMIndicator.id + "," + this.target.dashboard.model.report + ")");
  return false;
};

ViewIndicators.prototype.atClearAllClick = function(DOMIndicator) {
  var countSelectedIndicators = $(this.DOMLayer).find(".indicator input:checked").length;
  
  if (countSelectedIndicators <= 0)
    return false;
  
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  CommandListener.throwCommand("unselectindicators(" + this.target.id + "," + this.target.dashboard.model.report + ")");
  return false;
};

ViewMeasureUnits = function () {
  this.base = View;
  this.base();
};

ViewMeasureUnits.prototype = new View;

ViewMeasureUnits.prototype.init = function(DOMLayer) { 

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewmeasureunits, Lang.ViewMeasureUnits);
  
  if (!this.target) return;

  this.initTitles();
  CommandListener.capture(this.DOMLayer);
};

ViewMeasureUnits.prototype.toggleView = function() {
  var jLayer = $(this.DOMLayer);
  var jLabel = jLayer.find("label:first");
  var jList = jLayer.find("ul:first");
  var value = jLabel.html();

  if (jList.is(":visible")) {
    jLabel.html(value.replace("-", "+"));
    jList.hide();
  }
  else {
    jLabel.html(value.replace("+", "-"));
    jList.show();
  }
};

ViewMeasureUnits.prototype.refresh = function() {
  var jLayer = $(this.DOMLayer);
  var jList = jLayer.find("ul");
  var measureUnitList = this.target.dashboard.model.measureUnitList;
  var measureUnitTemplate = translate(AppTemplate.viewmeasureunitsitem, Lang.ViewMeasureUnits);
  var rangeList = this.state.rangeList;
  
  if (measureUnitList.length == 0) {
    this.hide();
    return;
  }
  
  this.show();

  jList.html("");
  for (var i=0; i<measureUnitList.length; i++) {
    var measureUnit = measureUnitList[i];
    var range = rangeList.get(measureUnit.name);
    var mode = Range.RELATIVE;
    var minValue = "", maxValue = "";
    
    if (range != null) {
      mode = range.mode;
      minValue = range.min;
      maxValue = range.max;
    }
    
    var jMeasureUnit = $.tmpl(measureUnitTemplate, { id: measureUnit.id, label: measureUnit.label, description: measureUnit.description, minValue: minValue, maxValue: maxValue });
    var DOMMeasureUnit = jMeasureUnit.get(0);
    var jInputMode = jMeasureUnit.find("input.mode");
    var jInputMin = jMeasureUnit.find("input.min");
    var jClearMin = jMeasureUnit.find(".clear.min");
    var jInputMax = jMeasureUnit.find("input.max");
    var jClearMax = jMeasureUnit.find(".clear.max");
    
    DOMMeasureUnit.id = measureUnit.id;
    
    jMeasureUnit.find("input.mode.absolute").attr("checked", (mode == Range.ABSOLUTE));
    jMeasureUnit.find("input.mode.relative").attr("checked", (mode == Range.RELATIVE));
    
    jInputMode.change(ViewMeasureUnits.prototype.atMeasureUnitModeChange.bind(this, DOMMeasureUnit));
    jInputMin.keypress(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyPress.bind(this, DOMMeasureUnit));
    jInputMin.keyup(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyUp.bind(this, DOMMeasureUnit));
    jClearMin.click(ViewMeasureUnits.prototype.atMeasureUnitRangeClearMin.bind(this, DOMMeasureUnit));
    jInputMax.keypress(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyPress.bind(this, DOMMeasureUnit));
    jInputMax.keyup(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyUp.bind(this, DOMMeasureUnit));
    jClearMax.click(ViewMeasureUnits.prototype.atMeasureUnitRangeClearMax.bind(this, DOMMeasureUnit));
    jList.append(jMeasureUnit);
    
    this.refreshMeasureInputs(DOMMeasureUnit, mode);
    this.refreshClearButtons(DOMMeasureUnit);
  }
};

ViewMeasureUnits.prototype.refreshMeasureInputs = function(DOMMeasureUnit, mode) {
  var jMeasureUnit = $(DOMMeasureUnit);
  
  jMeasureUnit.removeClass(Range.ABSOLUTE);
  jMeasureUnit.removeClass(Range.RELATIVE);
  jMeasureUnit.addClass(mode);
  
  if (mode == Range.ABSOLUTE) {
    jMeasureUnit.find("input.min").prop("disabled", true);
    jMeasureUnit.find("input.max").prop("disabled", true);
  }
  else if (mode == Range.RELATIVE) {
    jMeasureUnit.find("input.min").prop("disabled", false);
    jMeasureUnit.find("input.max").prop("disabled", false);
  }
};

ViewMeasureUnits.prototype.refreshClearButtons = function(DOMMeasureUnit) {
  var jMeasureUnit = $(DOMMeasureUnit);
  var minValue = jMeasureUnit.find("input.min").val();
  var jMinClear = jMeasureUnit.find(".clear.min");
  var maxValue = jMeasureUnit.find("input.max").val();
  var jMaxClear = jMeasureUnit.find(".clear.max");
  
  if (minValue != "") jMinClear.show();
  else jMinClear.hide();
  
  if (maxValue != "") jMaxClear.show();
  else jMaxClear.hide();
};

ViewMeasureUnits.prototype.setMode = function(DOMMeasureUnit, mode) {
  var jMeasureUnit = $(DOMMeasureUnit);
  
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  this.refreshMeasureInputs(DOMMeasureUnit, mode);
  
  CommandListener.throwCommand("setmeasureunitmode(" + this.target.id + "," + DOMMeasureUnit.id + "," + mode + ")");
};

ViewMeasureUnits.prototype.setRange = function(DOMMeasureUnit) {
  var jMeasureUnit = $(DOMMeasureUnit);
  var minValue = jMeasureUnit.find("input.min").val();
  var maxValue = jMeasureUnit.find("input.max").val();
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  CommandListener.throwCommand("setmeasureunitrange(" + this.target.id + "," + DOMMeasureUnit.id + "," + minValue + "," + maxValue + ")");
};

ViewMeasureUnits.prototype.atMeasureUnitModeChange = function(DOMMeasureUnit, event) {
  this.setMode(DOMMeasureUnit, $(event.currentTarget).val());
  return false;
};

ViewMeasureUnits.prototype.atMeasureUnitRangeKeyPress = function(DOMMeasureUnit, event) {
  var jMeasureUnit = $(DOMMeasureUnit);
  var number = String.fromCharCode(event.charCode);
  
  if (number == "." || number == "," || event.charCode == 0 || event.charCode == 13 || event.charCode == 9 || isNumber(number)) {
    
    if (this.rangeTimeout != null)
      window.clearTimeout(this.rangeTimeout);
    
    this.rangeTimeout = window.setTimeout(ViewMeasureUnits.prototype.setRange.bind(this, DOMMeasureUnit), 1000);
    
    return true;
  }
  
  return false;
};

ViewMeasureUnits.prototype.atMeasureUnitRangeKeyUp = function(DOMMeasureUnit, event) {
  this.refreshClearButtons(DOMMeasureUnit);
};

ViewMeasureUnits.prototype.atMeasureUnitRangeClearMin = function(DOMMeasureUnit, event) {
  var jMeasureUnit = $(DOMMeasureUnit);
  jMeasureUnit.find("input.min").val("");
  this.setRange(DOMMeasureUnit);
  this.refreshClearButtons(DOMMeasureUnit);
};

ViewMeasureUnits.prototype.atMeasureUnitRangeClearMax = function(DOMMeasureUnit, event) {
  var jMeasureUnit = $(DOMMeasureUnit);
  jMeasureUnit.find("input.max").val("");
  this.setRange(DOMMeasureUnit);
  this.refreshClearButtons(DOMMeasureUnit);
};

function isFunction(item) {
  return (typeof item == "function");
}

function HierarchyMultipleSelector(config) {
  this.config = config;
  this.translations = new Object();
  this.translations["es"] = new Object();
  this.translations["es"].All = "todos";
  this.translations["en"] = new Object();
  this.translations["en"].All = "all";
  this.templates = new Object();
  this.templates["es"] = new Object();
  this.templates["es"].Layout = "<div class='layer'><ul class='list'></ul></div>";
  this.templates["es"].ListItem = "<li class='option ${code}'><input type='checkbox' value='${value}'/><a class='hoverable ${leaf}' style='margin-left:4px;' title='${label}'>${label}</a><div class='colorbox'></div><ul class='children' style='display:none;'>Cargando...</ul></li>";
  this.templates["es"].EmptyList = "<li>No existen opciones</li>";
  this.templates["es"].LoadingList = "<li style='padding:5px;'>obteniendo datos...</li>";
  this.templates["en"] = new Object();
  this.templates["en"].Layout = "<div class='layer'><ul class='list'></ul></div>";
  this.templates["en"].ListItem = "<li class='option ${code}'><input type='checkbox' value='${value}'/><a class='hoverable ${leaf}' style='margin-left:4px;' title='${label}'>${label}</a><div class='colorbox'></div><ul class='children' style='display:none;'>Loading...</ul></li>";
  this.templates["en"].EmptyList = "<li>No options</li>";
  this.templates["en"].LoadingList = "<li style='padding:5px;'>retrieving data...</li>";
  this.language = "es";
  this.selection = (config.selection!=null)?config.selection:new Object();
  this.resetOnChange = this.config.resetOnChange != null?this.config.resetOnChange:true;
  if (config.language && this.translations[config.language] != null) this.language = config.language;
};

HierarchyMultipleSelector.prototype.addBehaviours = function(jList) {
  jList.find("a").click(HierarchyMultipleSelector.prototype.atToggleOption.bind(this));
  jList.find("input").change(HierarchyMultipleSelector.prototype.atOptionChange.bind(this));
};

HierarchyMultipleSelector.prototype.cleanCode = function(code) {
  return code.replace(/ /g, ".").replace(/[^\w\s]/gi, "");
};

HierarchyMultipleSelector.prototype.getList = function(parentItem) {
  var jList = this.jList;
  
  if (parentItem != null)
    jList = parentItem.jOption.find(".children");
  
  return jList;
};

HierarchyMultipleSelector.prototype.renderItems = function(parentItem) {
  
  this.jLayer.removeClass("empty");
  
  if (!this.config.provider) return;
  
  var jList = this.getList(parentItem);
  jList.html(this.templates[this.language].LoadingList);
  
  if (parentItem != null) {
    if (parentItem.jOption == null)
      parentItem.jOption = this.jList.find(".option." + parentItem.name);
  }
  
  this.config.provider.load(parentItem!=null?parentItem.name:null, HierarchyMultipleSelector.prototype.renderItemsCallback.bind(this, parentItem));
};
  
HierarchyMultipleSelector.prototype.renderItemsCallback = function(parentItem, result) {  
  var jList = this.getList(parentItem);

  jList.html("");
  
  if (result.rows.length <= 0) {
    jList.html(this.templates[this.language].EmptyList);
    this.jLayer.addClass("empty");
  }
  
  for (var i=0; i<result.rows.length; i++) {
    var item = result.rows[i];
    var cleanCode = this.cleanCode(item.code);

    if (cleanCode == "") continue;

    var listItemTemplate = $.template(null, this.templates[this.language].ListItem);
    var jItem = $.tmpl(listItemTemplate, { code: cleanCode, leaf: "leaf_" + item.leaf, value: item.code, label: item.name, label: item.name });
    var jCheckBox = jItem.find("input");
    var jColorBox = jItem.find(".colorbox");
    
    jItem.code = item.code;
    jItem.cleanCode = cleanCode;
    if (this.selection[item.code] != null) {
      jCheckBox.attr("checked", true);
      this.selection[item.code] = { value: item.code, label: item.name };
    }
    
    jColorBox.click(HierarchyMultipleSelector.prototype.atOptionColorClick.bind(this, item.code));
    jList.append(jItem);
  }
  
  this.addBehaviours(jList);
  
  if (parentItem != null) {
    parentItem.jOption.addClass("loaded");
    this.updateChildrenOptions(parentItem.jOption.find("input:first").get(0));
  }
  
  if (this.onRender) this.onRender(this);
};

HierarchyMultipleSelector.prototype.render = function(DOMLayer) {
  var selectorTemplate = $.template(null, this.templates[this.language].Layout);
  var content = $.tmpl(selectorTemplate, {}).get(0).outerHTML;
  
  this.jLayer = $(DOMLayer);
  this.jLayer.html(content);
  this.jList = this.jLayer.find(".layer .list");

  this.renderItems(null);
};

HierarchyMultipleSelector.prototype.reset = function() {
  this.selection = new Object();
  this.renderItems(null);
};

HierarchyMultipleSelector.prototype.clearColors = function(code) {
  this.jLayer.find(".colorbox").css("background-color", "");
};

HierarchyMultipleSelector.prototype.getOptions = function(codes) {
  var result = [];
  for (var i=0; i<codes.length; i++)
    result.push(this.getOption(codes[i]));
  return result;
};

HierarchyMultipleSelector.prototype.searchOption = function(code) {
  var jOption = this.jList.find(".option." + this.cleanCode(code));
  
  if (jOption.length == 0) {
    var cleanCode = this.cleanCode(code);
    jOption = this.jList.find(".option." + cleanCode);
    if (jOption.length == 0)
      return null;
  }

  return jOption;
};

HierarchyMultipleSelector.prototype.getOption = function(code) {
  var jOption = this.searchOption(code);

  if (jOption == null)
    return null;
  
  var option = {};
  option.code = code;
  option.label = jOption.find("input:first").next("a").html();
  option.jOption = jOption;
  
  option.checked = function() {
    return this.jOption.find("input:first").get(0).checked;
  };
  
  option.setColor = function(color) {
    this.jOption.find(".colorbox:first").css("background-color", color);
  };
  
  return option;
};

HierarchyMultipleSelector.prototype.selectAll = function() {
  var DOMInputList = this.jLayer.find("input");

  for (var i=0; i<DOMInputList.length; i++) {
    var DOMInput = DOMInputList[i];
    var value = DOMInput.value;
    DOMInput.checked = true;
    this.selection[value] = { value: value, label: $(DOMInput).next("a").html() };
  }
};

HierarchyMultipleSelector.prototype.setSelection = function(selection) {
  this.selection = {};
  for (var i=0;i<selection.length;i++) {
    var selected = selection[i];
    this.selection[selected] = { value : selected, label: "" };
  }
};

HierarchyMultipleSelector.prototype.getSelection = function() {
  var result = new Array();
  for (var index in this.selection) {
    if (isFunction(this.selection[index])) continue;
    if (this.selection[index] != null) result.push(this.selection[index].value);
  } 
  return result;
};

HierarchyMultipleSelector.prototype.removeParentsFromSelection = function(DOMInput) {
  var jOption = $(DOMInput).parents("li.option:first");
  var jParent = jOption.parents("li.option:first");

  while (jParent.length > 0) {
    var DOMParentInput = jParent.find("input").get(0);

    if (DOMInput.checked)
      delete this.selection[DOMParentInput.value];
    else if (DOMParentInput.checked)
      this.selection[DOMParentInput.value] = { value: DOMParentInput.value, label: $(DOMParentInput).next("a").html() };

    jParent = jParent.parents("li.option:first");
  }
};

HierarchyMultipleSelector.prototype.highlightParents = function(DOMInput) {
  if (DOMInput.checked) {
    var jParent = $(DOMInput).parents("li.option:first");
    while (jParent.length > 0) {
      jParent.addClass("selected");
      jParent = jParent.parents("li.option:first");
    }
  }
  else {
    var jParent = $(DOMInput).parents("li.option:first");
    while (jParent.length > 0) {
      var jSelectedChildren = jParent.find("input:checked");
      if (jSelectedChildren.length == 0) jParent.removeClass("selected");
      jParent = jParent.parents("li.option:first");
    }
  }
};

HierarchyMultipleSelector.prototype.updateChildrenOptions = function(DOMInput) {
  var jInput = $(DOMInput);
  var jOption = jInput.parents(".option:first");
  var DOMInputList = jOption.find(".children:first").find("input");
  for (var i=0; i<DOMInputList.length; i++) {
    var DOMCurrentInput = DOMInputList[i];
    DOMCurrentInput.checked = DOMInput.checked;
    this.highlightParents(DOMCurrentInput);
    delete this.selection[DOMCurrentInput.value];
  }
};

HierarchyMultipleSelector.prototype.updateOption = function(DOMInput) {
  
  if (DOMInput.checked) this.selection[DOMInput.value] = { value: DOMInput.value, label: $(DOMInput).next("a").html() };
  else delete this.selection[DOMInput.value];

  this.removeParentsFromSelection(DOMInput);
  this.highlightParents(DOMInput);
  this.updateChildrenOptions(DOMInput);
  
  if (this.onChange) this.onChange(this);
};

HierarchyMultipleSelector.prototype.isEmpty = function() {
  return this.jLayer.hasClass("empty");
};

HierarchyMultipleSelector.prototype.isHiddenable = function() {
  return this.jLayer.hasClass("hiddenable");
};

HierarchyMultipleSelector.prototype.atToggleOption = function(jEvent) {
  var jLink = $(jEvent.target);
  var jOption = jLink.parents(".option:first");
  var jCheckbox = jOption.find("input");
  
  if (jLink.hasClass("leaf_true")) {
    var DOMCheckbox = jCheckbox.get(0);
    DOMCheckbox.checked = !DOMCheckbox.checked;
    this.updateOption(jCheckbox.get(0));
    return;
  }
  
  var jChildren = jOption.find(".children:first");

  if (jLink.hasClass("opened"))
    jChildren.hide();
  else {
    jChildren.show();
    if (!jOption.hasClass("loaded")) {
      var parentItem = new Object();
      parentItem.name = jCheckbox.val();
      parentItem.jOption = jOption;
      this.renderItems(parentItem);
    }
  }
  
  jLink.toggleClass("opened");
};

HierarchyMultipleSelector.prototype.atOptionChange = function(jEvent) {
  var DOMInput = jEvent.target;
  this.updateOption(DOMInput);
  return false;
};

HierarchyMultipleSelector.prototype.atOptionColorClick = function(optionCode) {
  if (this.onColorClick) {
    var jOption = this.searchOption(optionCode);
    
    if (jOption == null)
      return null;
    
    var extra = {};
    extra.DOMColorBox = jOption.find(".colorbox:first").get(0);
    extra.getColorDOM = function() {
      return this.DOMColorBox;
    }

    this.onColorClick(this, optionCode, extra);
  }
};

function CategoriesProvider(dashboardName, taxonomyId) {
  this.dashboardName = dashboardName;
  this.taxonomyId = taxonomyId;
};

CategoriesProvider.prototype.load = function(parentCategoryId, callback) {
  
  if (this.dashboardName == null) return;
  if (this.taxonomyId == null) return;
  
  var datasourceUrl = Kernel.getLoadCategoriesUrl(this.dashboardName, this.taxonomyId, parentCategoryId);
  $.ajax({url: datasourceUrl, async:true}).done(CategoriesProvider.prototype.loadCallback.bind(this, callback));
};

CategoriesProvider.prototype.loadCallback = function(callback, data) {
  callback(eval("(" + data + ")"));
};

ViewTaxonomies = function () {
  this.base = View;
  this.base();
  this.compareSelectors = new Object();
  this.filterSelectors = new Object();
  this.hasFilters = false;
};

ViewTaxonomies.prototype = new View;

ViewTaxonomies.prototype.init = function (DOMLayer) {

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewtaxonomies, Lang.ViewTaxonomies);

  if (!this.target) return;

  CommandListener.capture(this.DOMLayer);

  this.initTitles();
  this.addBehaviours();
};

ViewTaxonomies.prototype.isExpanded = function (jBlock) {
  if (jBlock.hasClass("compare") && jBlock.find("label:first").html().indexOf("-") != -1) return true;
  else if (jBlock.hasClass("filters") && jBlock.find("label:first").html().indexOf("-") != -1) return true;
  return false;
};

ViewTaxonomies.prototype.expandView = function (jBlock) {
  if (jBlock.hasClass("compare")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    jLabel.html(value.replace("+", "-"));
    jBlock.find("select:first").show();
    jBlock.find(".taxonomiesbox").show();
  }
  else if (jBlock.hasClass("filters")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    var jNoFilters = jBlock.find(".nofilters:first");
    jLabel.html(value.replace("+", "-"));
    jBlock.find("ul:first").show();
    if (!this.hasFilters) jNoFilters.show();
    else jNoFilters.hide();
  }
};

ViewTaxonomies.prototype.collapseView = function (jBlock) {
  if (jBlock.hasClass("compare")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    jLabel.html(value.replace("-", "+"));
    jBlock.find("select:first").hide();
    jBlock.find(".taxonomiesbox").hide();
  }
  else if (jBlock.hasClass("filters")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    jLabel.html(value.replace("-", "+"));
    jBlock.find("ul:first").hide();
    jBlock.find(".nofilters:first").hide();
  }
};

ViewTaxonomies.prototype.toggleView = function (jBlock) {
    if (this.isExpanded(jBlock))
      this.collapseView(jBlock);
    else
      this.expandView(jBlock);
};

ViewTaxonomies.prototype.addBehaviours = function () {
  var jLayer = $(this.DOMLayer);

  var jSelector = jLayer.find(".block.compare select");
  jSelector.change(ViewTaxonomies.prototype.atTaxonomyCompareChange.bind(this));

  var jClearCompare = jLayer.find(".block.compare .clearcompare");
  jClearCompare.click(ViewTaxonomies.prototype.atClearCompareClick.bind(this));
  jClearCompare.hide();

  var jClearFilters = jLayer.find(".block.filters .clearfilters");
  jClearFilters.click(ViewTaxonomies.prototype.atClearFiltersClick.bind(this));
  jClearFilters.hide();
};

ViewTaxonomies.prototype.refreshColors = function (selector) {
  if (selector == null) return;

  var selection = selector.getSelection();
  var options = selector.getOptions(selection);

  selector.clearColors();

  for (var i = 0; i < options.length; i++) {
    var option = options[i];
    var color = View.getColor(View.ColorContext.COMPARE, option.code);

    if (color == null)
      color = View.retainColor(View.ColorContext.COMPARE, option.code);

    option.setColor(color);
  }

};

ViewTaxonomies.prototype.refresh = function () {
  this.refreshCompare();
  this.refreshFilters();
};

ViewTaxonomies.prototype.refreshCompareCategoriesList = function (taxonomyId) {
  var selection = new Array();
  var jTaxonomiesBox = $(this.DOMLayer).find(".block.compare .taxonomiesbox");
  var jTaxonomyCategories = jTaxonomiesBox.find(".categories." + taxonomyId);

  jTaxonomiesBox.find(".categories").hide();

  if (State.compareTaxonomy == taxonomyId)
    selection = State.compareList.getKeys();

  if (this.compareSelectors[taxonomyId] != null) {
    this.compareSelectors[taxonomyId].setSelection(selection);
    this.compareSelectors[taxonomyId].render(jTaxonomyCategories.get(0));
    jTaxonomyCategories.show();
    return;
  }

  jTaxonomyCategories = $("<div class='categories " + taxonomyId + "'></div>");
  var provider = new CategoriesProvider(this.target.dashboard.code, taxonomyId);
  var selector = new HierarchyMultipleSelector({provider: provider, language: Context.Config.Language});

  jTaxonomiesBox.append(jTaxonomyCategories);
  jTaxonomyCategories.show();

  selector.onChange = ViewTaxonomies.prototype.atSelectorCompareChange.bind(this, taxonomyId);
  selector.onColorClick = ViewTaxonomies.prototype.atSelectorCompareColorClick.bind(this, taxonomyId);
  selector.onRender = ViewTaxonomies.prototype.atSelectorCompareRender.bind(this, taxonomyId);
  selector.taxonomyId = taxonomyId;
  selector.setSelection(selection);
  selector.render(jTaxonomyCategories.get(0));

  this.compareSelectors[taxonomyId] = selector;
};

ViewTaxonomies.prototype.refreshCompare = function () {
  var dimensionList = this.target.dashboard.model.dimensionList;
  var compareTaxonomy = State.compareTaxonomy;
  var jLayer = $(this.DOMLayer);
  var DOMSelector = jLayer.find(".block.compare select").get(0);
  var jTaxonomiesBox = jLayer.find(".block.compare .taxonomiesbox");

  if (State.indicatorList.size() > 1) {
    jLayer.find(".block.compare").hide();
    return;
  }

  jLayer.find(".block.compare").show();
  DOMSelector.innerHTML = "";
  jTaxonomiesBox.find(".categories").hide();

  var first = true;
  for (var i = 0; i < dimensionList.length; i++) {
    var dimension = dimensionList[i];

    for (var j = 0; j < dimension.taxonomies.length; j++) {
      var taxonomy = dimension.taxonomies[j];

      addSelectOption(DOMSelector, taxonomy.id, taxonomy.label, (taxonomy.id == compareTaxonomy));

      if ((first && compareTaxonomy == null) || taxonomy.id == compareTaxonomy)
        this.refreshCompareCategoriesList(taxonomy.id);

      first = false;
    }
  }
};

ViewTaxonomies.prototype.refreshFilters = function () {
  var dimensionList = this.target.dashboard.model.dimensionList;
  var compareTaxonomy = State.compareTaxonomy;
  var jLayer = $(this.DOMLayer);
  var jBlock = jLayer.find(".block.filters");
  var jList = jLayer.find(".block.filters > ul");
  var jNoFilters = jLayer.find(".nofilters");
  var filterTemplate = translate(AppTemplate.viewtaxonomiesfilter, Lang.ViewTaxonomies);

  this.hasFilters = false;

  jList.hide();
  jList.find(".filter").hide();
  jNoFilters.show();

  for (var i = 0; i < dimensionList.length; i++) {
    var dimension = dimensionList[i];

    for (var j = 0; j < dimension.taxonomies.length; j++) {
      var taxonomy = dimension.taxonomies[j];
      var taxonomyId = taxonomy.id;
      var selection = State.filterList.getCategoriesOfTaxonomy(taxonomyId);
      var jFilter = jList.find(".filter." + taxonomyId);
      var jCategories = jFilter.find(".categories");

      if (taxonomyId == compareTaxonomy) {
        jFilter.hide();
        continue;
      }

      this.hasFilters = true;

      if (this.filterSelectors[taxonomyId] != null) {
        this.filterSelectors[taxonomyId].setSelection(selection);
        this.filterSelectors[taxonomyId].render(jCategories.get(0));
        jFilter.show();
        continue;
      }

      var jFilter = $.tmpl(filterTemplate, { id: taxonomyId, label: taxonomy.label });
      jFilter.find("a.togglefilter").click(ViewTaxonomies.prototype.atFilterToggle.bind(this, jFilter));
      jFilter.find("a.togglefilter").mouseover(ViewTaxonomies.prototype.atFilterMouseOver.bind(this, jFilter));
      jFilter.find("a.togglefilter").mouseout(ViewTaxonomies.prototype.atFilterMouseOut.bind(this, jFilter));
      jFilter.find("a.selectallfilters").click(ViewTaxonomies.prototype.atFilterSelectAll.bind(this, jFilter, taxonomyId));
      jFilter.find("a.selectallfilters").mouseover(ViewTaxonomies.prototype.atFilterSelectAllMouseOver.bind(this, jFilter));
      jFilter.find("a.selectallfilters").mouseout(ViewTaxonomies.prototype.atFilterSelectAllMouseOut.bind(this, jFilter));
      jList.append(jFilter);

      var jCategories = jFilter.find(".categories");
      var provider = new CategoriesProvider(this.target.dashboard.code, taxonomyId);
      var selector = new HierarchyMultipleSelector({provider: provider, language: Context.Config.Language});

      selector.onChange = ViewTaxonomies.prototype.atSelectorFilterChange.bind(this, taxonomyId);
      selector.setSelection(selection);
      selector.render(jCategories.get(0));

      this.filterSelectors[taxonomyId] = selector;
    }
  }

  if (this.isExpanded(jBlock)) {
    if (this.hasFilters) {
      jList.show();
      jNoFilters.hide();
    }
  }
  else
    jNoFilters.hide();

};

ViewTaxonomies.prototype.refreshClearFilters = function () {
  var jLayer = $(this.DOMLayer);
  var jBlock = jLayer.find(".block.filters");
  var jClearFilters = jLayer.find(".block.filters .clearfilters");

  if (State.filterList.size() > 0)
    jClearFilters.show();
  else
    jClearFilters.hide();
};

ViewTaxonomies.prototype.compare = function (taxonomyId, selector) {
  var selection = (selector != null) ? selector.getSelection() : new Array();
  var jLayer = $(this.DOMLayer);
  var jClearCompare = jLayer.find(".block.compare .clearcompare");

  if (selection.length <= 0) {
    State.compareTaxonomy = null;
    jClearCompare.hide();
  }
  else {
    State.compareTaxonomy = taxonomyId;
    jClearCompare.show();
  }

  this.refreshFilters();
  this.refreshClearFilters();
  this.refreshColors(selector);

  State.compareList.clear();
  var options = selector != null ? selector.getOptions(selection) : new Array();
  for (var i = 0; i < options.length; i++)
    State.compareList.add(taxonomyId, options[i].code, options[i].label);

  CommandListener.throwCommand("refreshdashboard(" + this.target.dashboard.code + ")");
};

ViewTaxonomies.prototype.delayCompare = function (taxonomyId, selector) {
  if (this.compareTimeout) {
    window.clearTimeout(this.compareTimeout);
    this.compareTimeout = null;
  }
  this.compareTimeout = window.setTimeout(ViewTaxonomies.prototype.compare.bind(this, taxonomyId, selector), 800);
};

ViewTaxonomies.prototype.filter = function (taxonomyId, selector) {
  var selection = selector.getSelection();
  var options = selector.getOptions(selection);

  State.filterList.deleteCategoriesOfTaxonomy(taxonomyId);
  for (var i = 0; i < options.length; i++)
    State.filterList.add(taxonomyId, options[i].code, options[i].label);

  this.refreshClearFilters();

  CommandListener.throwCommand("refreshdashboard(" + this.target.dashboard.code + ")");
};

ViewTaxonomies.prototype.delayFilter = function (taxonomyId, selector) {
  if (this.filterTimeout) {
    window.clearTimeout(this.filterTimeout);
    this.filterTimeout = null;
  }
  this.filterTimeout = window.setTimeout(ViewTaxonomies.prototype.filter.bind(this, taxonomyId, selector), 800);
};

ViewTaxonomies.prototype.updateColor = function (context, id, color) {
  if (this.colorSelector == null) return;

  var option = this.colorSelector.getOption(id);
  option.setColor(color);

  CommandListener.throwCommand("setcolor(" + this.target.id + "," + context + "," + id + "," + (color.indexOf("#") == -1 ? "#" : "") + color + ")");
};

ViewTaxonomies.prototype.atTaxonomyCompareChange = function (selectorChanged) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.compare._expansible").get(0));
  var taxonomyId = $(".block.compare select option:selected").val();
  if (taxonomyId == "-1") return false;
  this.refreshCompareCategoriesList(taxonomyId);
  this.compare(taxonomyId, null);
};

ViewTaxonomies.prototype.atSelectorCompareChange = function (taxonomyId, selectorChanged) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.compare._expansible").get(0));
  this.delayCompare(taxonomyId, selectorChanged);
};

ViewTaxonomies.prototype.atSelectorCompareColorClick = function (taxonomyId, selectorChanged, code, extra) {
  this.toggleColorsDialog(View.ColorContext.COMPARE, code, extra.getColorDOM());
  this.colorSelector = selectorChanged;
};

ViewTaxonomies.prototype.atSelectorCompareRender = function (taxonomyId, selectorChanged) {
  this.refreshColors(selectorChanged);
};

ViewTaxonomies.prototype.atClearCompareClick = function () {
  State.compareTaxonomy = null;
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.compare._expansible").get(0));
  CommandListener.throwCommand("unselectcategories(" + this.target.id + ")");
  $(this.DOMLayer).find(".block.compare .clearcompare").hide();
};

ViewTaxonomies.prototype.atSelectorFilterChange = function (taxonomyId, selectorChanged) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  this.delayFilter(taxonomyId, selectorChanged);
};

ViewTaxonomies.prototype.atFilterToggle = function (jFilter, jEvent) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  jFilter.toggleClass("opened");
};

ViewTaxonomies.prototype.atFilterMouseOver = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "inline";
};

ViewTaxonomies.prototype.atFilterSelectAllMouseOver = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "inline";
};

ViewTaxonomies.prototype.atFilterMouseOut = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "none";
};

ViewTaxonomies.prototype.atFilterSelectAllMouseOut = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "none";
};

ViewTaxonomies.prototype.atFilterSelectAll = function (jFilter, taxonomyId, jEvent) {
  if (!this.hasFilters) return;
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  if (!jFilter.hasClass("opened")) jFilter.addClass("opened");
  this.filterSelectors[taxonomyId].selectAll();
  this.filter(taxonomyId, this.filterSelectors[taxonomyId]);
};

ViewTaxonomies.prototype.atClearFiltersClick = function () {
  if (!this.hasFilters) return;
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  CommandListener.throwCommand("unselectfilters(" + this.target.id + ")");
  $(this.DOMLayer).find(".block.filters .clearfilters").hide();
};

TimeLapse = new Object();

TimeLapse.getBounds = function(timeLapse, range) {
  var scale = timeLapse.scale;
  
  if (scale == Scale.YEAR) factor = YEAR;
  else if (scale == Scale.MONTH) factor = MONTH;
  else if (scale == Scale.DAY) factor = DAY;
  else if (scale == Scale.HOUR) factor = HOUR;
  else if (scale == Scale.MINUTE) factor = MINUTE;
  else if (scale == Scale.SECOND) factor = SECOND;
  
  return { min: range.min-factor, max: range.max+factor }; 
};

TimeLapse.getWindowSizeMinValue = function(bounds, scale, factor) {
  var result = Math.round((bounds.max-bounds.min)/scale);
  result = (result<factor)?result:factor;
  result = result>0?result:10;
  return result;
};

TimeLapse.getWindowSizeMilliseconds = function(milliseconds, maxMilliseconds, freeSize) {
  
  if (milliseconds == null || (!freeSize && milliseconds > maxMilliseconds))
    return Math.round(maxMilliseconds/2);
  
  return milliseconds;
};

TimeLapse.getWindowSize = function(timeLapse, bounds, freeSize) {
  var windowSize = new Object();
  var scale = timeLapse.scale;
  var milliseconds = null;

  
  if (scale == Scale.YEAR) {
    var range = { min: 1, max: 50};
    var minYears = this.getWindowSizeMinValue(bounds, YEAR, range.min);
    windowSize = { min: { years: minYears } };
    if (!freeSize) windowSize.max = { years: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*YEAR, freeSize);
  }
  else if (scale == Scale.MONTH) {
    var range = { min: 1, max: 60};
    var minMonths = this.getWindowSizeMinValue(bounds, MONTH, range.min);
    windowSize = { min: { months: minMonths } };
    if (!freeSize) windowSize.max = { months: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*MONTH, freeSize);
  }
  else if (scale == Scale.DAY) {
    var range = { min: 1, max: 150};
    var minDays = this.getWindowSizeMinValue(bounds, DAY, range.min);
    windowSize = { min: { days: minDays } };
    if (!freeSize) windowSize.max = { days: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*DAY, freeSize);
  }
  else if (scale == Scale.HOUR) {
    var range = { min: 1, max: 120};
    var minHours = this.getWindowSizeMinValue(bounds, HOUR, range.min);
    windowSize = { min: { hours: minHours } };
    if (!freeSize) windowSize.max = { hours: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*HOUR, freeSize);
  }
  else if (scale == Scale.MINUTE) {
    var range = { min: 1, max: 300};
    var minMinutes = this.getWindowSizeMinValue(bounds, MINUTE, range.min);
    windowSize = { min: { minutes: minMinutes } };
    if (!freeSize) windowSize.max = { minutes: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*MINUTE, freeSize);
  }
  else if (scale == Scale.SECOND) {
    var range = { min: 1, max: 400};
    var minSeconds = this.getWindowSizeMinValue(bounds, SECOND, range.min);
    windowSize = { min: { seconds: minSeconds } };
    if (!freeSize) windowSize.max = { seconds: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*SECOND, freeSize);
  }

  return windowSize;
};

TimeLapse.getValues = function(timeLapse, windowSize, bounds) {
  var valueMin = timeLapse.from!=null?timeLapse.from:(bounds.max-windowSize.milliseconds);
  var valueMax = timeLapse.to!=null?timeLapse.to:bounds.max;
  
  if (valueMin < bounds.min) {
    valueMin = bounds.max-windowSize.milliseconds;
    valueMax = bounds.max;
  }
  
  if (valueMax > bounds.max) {
    valueMax = bounds.max;
    valueMin = valueMax-windowSize.milliseconds;
  }

  if (valueMin < bounds.min)
    valueMin = bounds.min;

  return { min: valueMin, max: valueMax };
};

TimeLapse.toScale = function(timeLapse, scale) {
  
  if (timeLapse.from == null)
    return timeLapse;
  
  var fromDate = new Date(timeLapse.from);
  var toDate = new Date(timeLapse.to);
  var isSameYear = (fromDate.getFullYear() == toDate.getFullYear());
  var isSameMonth = isSameYear && (fromDate.getMonth() == toDate.getMonth());
  var isSameDate = isSameYear && isSameMonth && (fromDate.getDate() == toDate.getDate());
  var isSameHour = isSameYear && isSameMonth && isSameDate && (fromDate.getHours() == toDate.getHours());
  var isSameMinute = isSameYear && isSameMonth && isSameDate && isSameHour && (fromDate.getMinutes() == toDate.getMinutes());
  var isSameSecond = isSameYear && isSameMonth && isSameDate && isSameHour && isSameMinute && (fromDate.getSeconds() == toDate.getSeconds());
  var factor = 0;
 
  if (scale == Scale.YEAR && isSameYear) factor = YEAR;
  else if (scale == Scale.MONTH && isSameMonth) factor = MONTH;
  else if (scale == Scale.DAY && isSameDate) factor = DAY;
  else if (scale == Scale.HOUR && isSameHour) factor = HOUR;
  else if (scale == Scale.MINUTE && isSameMinute) factor = MINUTE;
  else if (scale == Scale.SECOND && isSameSecond) factor = SECOND;
  
  return { from: timeLapse.from, to: timeLapse.to+factor, scale: scale };
}

var SECOND = 1000;
var MINUTE = SECOND*60;
var HOUR = MINUTE*60;
var DAY = HOUR*24;
var WEEK = DAY*7;
var MONTH = DAY*30;
var YEAR = MONTH*12;

Dialog = function() {
  this.DOMLayer = null;
};

Dialog.prototype.init = function(DOMLayer) {
  this.DOMLayer = DOMLayer;
};

Dialog.prototype.show = function() {
  if (!this.DOMLayer) return;
  this.DOMLayer.show();
};

Dialog.prototype.hide = function() {
  if (!this.DOMLayer) return;
  this.DOMLayer.hide();
};

Dialog.prototype.refresh = function() {
};

Dialog.prototype.destroy = function() {
  $(this.DOMLayer).remove();
};

Dialog.prototype.atAccept = function() {
  if (this.onAccept) this.onAccept();
};

Dialog.prototype.atCancel = function() {
  this.hide();
  this.destroy();
  if (this.onCancel) this.onCancel();
};

function Scale() {
}

Scale.YEAR = 0;
Scale.MONTH = 1;
Scale.DAY = 2;
Scale.HOUR = 3;
Scale.MINUTE = 4;
Scale.SECOND = 5;

Scale.fromString = function(scale) {
  if (scale == "year") return Scale.YEAR;
  else if (scale == "month") return Scale.MONTH;
  else if (scale == "day") return Scale.DAY;
  else if (scale == "hour") return Scale.HOUR;
  else if (scale == "minute") return Scale.MINUTE;
  else if (scale == "second") return Scale.SECOND;
  return -1;
};

Scale.toString = function(scale) {
  if (scale == Scale.YEAR) return "year";
  else if (scale == Scale.MONTH) return "month";
  else if (scale == Scale.DAY) return "day";
  else if (scale == Scale.HOUR) return "hour";
  else if (scale == Scale.MINUTE) return "minute";
  else if (scale == Scale.SECOND) return "second";
  return "";
};

function Resolution() {
}

Resolution.YEARS = 0;
Resolution.MONTHS = 1;
Resolution.DAYS = 2;
Resolution.HOURS = 3;
Resolution.MINUTES = 4;
Resolution.SECONDS = 5;

Resolution.fromString = function(resolution) {
  if (resolution == "years") return Resolution.YEARS;
  else if (resolution == "months") return Resolution.MONTHS;
  else if (resolution == "days") return Resolution.DAYS;
  else if (resolution == "hours") return Resolution.HOURS;
  else if (resolution == "minutes") return Resolution.MINUTES;
  else if (resolution == "seconds") return Resolution.SECONDS;
  return -1;
};

Resolution.toString = function(resolution) {
  if (resolution == Resolution.YEAR) return "years";
  else if (resolution == Resolution.MONTH) return "months";
  else if (resolution == Resolution.DAY) return "days";
  else if (resolution == Resolution.HOUR) return "hours";
  else if (resolution == Resolution.MINUTE) return "minutes";
  else if (resolution == Resolution.SECOND) return "seconds";
  return "";
};

DialogScale = function() {
  this.base = Dialog;
  this.base();
};

DialogScale.prototype = new Dialog;

DialogScale.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogscale, Lang.DialogScale);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogScale.prototype.isResolutionAvailable = function(resolutions) {
  if (size(this.target.availableResolutions) == 0)
    return true;

  for (var i=0; i<resolutions.length; i++) {
    var resolution = resolutions[i];
    if (this.target.availableResolutions[resolution] != null)
      return true;
  }

  return false;
}

DialogScale.prototype.isSecondScaleVisible = function() {
  return (this.target.resolution == Scale.SECOND && this.isResolutionAvailable([Resolution.SECONDS]));
};

DialogScale.prototype.isMinuteScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  return false;
};

DialogScale.prototype.isHourScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES, Resolution.HOURS]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  if (this.target.resolution == Scale.HOUR && isAvailable) return true;
  return false;
};

DialogScale.prototype.isDayScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES, Resolution.HOURS, Resolution.DAYS]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  if (this.target.resolution == Scale.HOUR && isAvailable) return true;
  if (this.target.resolution == Scale.DAY && isAvailable) return true;
  return false;
};

DialogScale.prototype.isMonthScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES, Resolution.HOURS, Resolution.DAYS, Resolution.MONTHS]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  if (this.target.resolution == Scale.HOUR && isAvailable) return true;
  if (this.target.resolution == Scale.DAY && isAvailable) return true;
  if (this.target.resolution == Scale.MONTH && isAvailable) return true;
  return false;
};

DialogScale.prototype.getAvailableScale = function (scale) {

  while (!this.isResolutionAvailable([scale]) && scale != Scale.YEARS && scale >= 0)
    scale--;

  return scale;
};

DialogScale.prototype.init = function() {
  var jLayer = $(this.DOMLayer);
  
  if (this.state.mode != null)
    jLayer.find(".dialog.scale").addClass(this.state.mode);

  var scale = this.getAvailableScale(this.target.resolution);
  jLayer.find(".scalebar").addClass(Scale.toString(scale));

  this.initBehaviours();
};

DialogScale.prototype.initBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  
  var jScaleSecond = jLayer.find(".scale.second a");
  jScaleSecond.click(DialogScale.prototype.atScaleClick.bind(this, "second"));

  var jScaleMinute = jLayer.find(".scale.minute a");
  jScaleMinute.click(DialogScale.prototype.atScaleClick.bind(this, "minute"));

  var jScaleHour = jLayer.find(".scale.hour a");
  jScaleHour.click(DialogScale.prototype.atScaleClick.bind(this, "hour"));

  var jScaleDay = jLayer.find(".scale.day a");
  jScaleDay.click(DialogScale.prototype.atScaleClick.bind(this, "day"));

  var jScaleMonth = jLayer.find(".scale.month a");
  jScaleMonth.click(DialogScale.prototype.atScaleClick.bind(this, "month"));

  var jScaleYear = jLayer.find(".scale.year a");
  jScaleYear.click(DialogScale.prototype.atScaleClick.bind(this, "year"));
};

DialogScale.prototype.refresh = function(jsonData) {
  var jLayer = $(this.DOMLayer);
  
  jLayer.find(".scale").removeClass("active");

  jLayer.find(".scale.second").attr("style", "display: " + (this.isSecondScaleVisible()?"block":"none"));
  jLayer.find(".scale.minute").attr("style", "display: " + (this.isMinuteScaleVisible()?"block":"none"));
  jLayer.find(".scale.hour").attr("style", "display: " + (this.isHourScaleVisible()?"block":"none"));
  jLayer.find(".scale.day").attr("style", "display: " + (this.isDayScaleVisible()?"block":"none"));
  jLayer.find(".scale.month").attr("style", "display: " + (this.isMonthScaleVisible()?"block":"none"));
  jLayer.find(".scale.year").attr("style", "display: block");

  var jActiveScale = jLayer.find(".scale." + Scale.toString(this.target.scale));
  jActiveScale.addClass("active");
};

DialogScale.prototype.disable = function() {
  var jLayer = $(this.DOMLayer);
  jLayer.find(".scalebar").addClass("disabled");
};

DialogScale.prototype.enable = function() {
  var jLayer = $(this.DOMLayer);
  jLayer.find(".scalebar").removeClass("disabled");
};

//************************************************************************************************************

DialogScale.prototype.atScaleClick = function(scale) {
  var jLayer = $(this.DOMLayer);
  if (jLayer.find(".scalebar").hasClass("disabled")) return;
  if (this.onChange) this.onChange(Scale.fromString(scale));
};

DialogZoom = function() {
  this.base = Dialog;
  this.base();
};

DialogZoom.prototype = new Dialog;

DialogZoom.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogzoom, Lang.DialogZoom);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogZoom.prototype.init = function() {
  this.initBehaviours();
};

DialogZoom.prototype.initBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  
  var jZoomIn = jLayer.find(".zoomin a");
  jZoomIn.click(DialogZoom.prototype.atZoomInClick.bind(this));

  var jRestoreZoom = jLayer.find(".restorezoom a");
  jRestoreZoom.click(DialogZoom.prototype.atRestoreZoomClick.bind(this));
};

DialogZoom.prototype.refresh = function(jsonData) {
};

//************************************************************************************************************

DialogZoom.prototype.atZoomInClick = function() {
  if (this.onZoomIn) this.onZoomIn();
};

DialogZoom.prototype.atRestoreZoomClick = function() {
  if (this.onRestoreZoom) this.onRestoreZoom();
};

ViewChart = function (type, configuration) {
	this.base = View;
	this.base();
	this.dialogTimeLapse = null;
	this.dialogRange = null;
	this.dialogScale = null;
	this.type = type;
	this.configuration = configuration;
	this.freeTimeLapseWindowSize = false;
};

ViewChart.LINE_CHART = "linechart";
ViewChart.BAR_CHART = "barchart";
ViewChart.MAP_CHART = "mapchart";
ViewChart.BUBBLE_CHART = "bubblechart";
ViewChart.TABLE_CHART = "tablechart";

ViewChart.prototype = new View;

ViewChart.prototype.init = function (DOMLayer) {
	this.initMode();
	this.initTimeLapse();
	this.initRange();
	this.initScale();
	this.initMessages();
};

ViewChart.prototype.initMode = function () {
	if (this.state.mode != null)
		$(this.DOMLayer).addClass(this.state.mode);
};

ViewChart.prototype.initTimeLapse = function () {
	if ($(this.DOMLayer).find(".dialogtimelapse").length == 0)
		return;

	this.dialogTimeLapse = new DialogTimeLapse();
	this.dialogTimeLapse.onChange = ViewChart.prototype.atTimeLapseChange.bind(this);
};

ViewChart.prototype.initRange = function () {
	if ($(this.DOMLayer).find(".dialogrange").length == 0)
		return;

	this.dialogRange = new DialogRange();
	this.dialogRange.onChange = ViewChart.prototype.atRangeChange.bind(this);
};

ViewChart.prototype.initScale = function () {
	if ($(this.DOMLayer).find(".dialogscale").length == 0)
		return;

	this.dialogScale = new DialogScale();
	this.dialogScale.onChange = ViewChart.prototype.atScaleChange.bind(this);
};

ViewChart.prototype.initMessages = function () {
};

ViewChart.prototype.hideMessages = function (indicatorList) {
	var jLayer = $(this.DOMLayer);
	jLayer.find(".message.selectindicator").hide();
	jLayer.find(".message.notenoughselectedindicators").hide();
	jLayer.find(".message.toomuchselectedindicators").hide();
	jLayer.find(".message.toomuchselectedmetrics").hide();
	jLayer.find(".message.comingsoon").hide();
	jLayer.find(".message.emptychart").hide();
};

ViewChart.prototype.validateIndicators = function (indicatorList) {
	var jLayer = $(this.DOMLayer);
	var indicatorsCount = indicatorList.size();
	var metricsCount = size(this.getMetrics(indicatorList));

	if (indicatorsCount == 0) {
		jLayer.find(".message.selectindicator").show();
		return false;
	}

	if (this.configuration.indicators.min != -1 && indicatorsCount < this.configuration.indicators.min) {
		jLayer.find(".message.notenoughselectedindicators").show();
		return false;
	}

	if (this.configuration.indicators.max != -1 && indicatorsCount > this.configuration.indicators.max) {
		var jMessage = jLayer.find(".message.toomuchselectedindicators");
		jMessage.html(Lang.ViewChart.TooMuchSelectedIndicators.replace("%d", this.configuration.indicators.max));
		jMessage.show();
		return false;
	}

	if (this.configuration.metrics.max != -1 && metricsCount > this.configuration.metrics.max) {
		var jMessage = jLayer.find(".message.toomuchselectedmetrics");
		jMessage.html(Lang.ViewChart.TooMuchSelectedMetrics.replace("%d", this.configuration.metrics.max));
		jMessage.show();
		return false;
	}

	if (this.configuration.comingSoon) {
		this.hideMessages();
		$(this.DOMLayer).find(".message.comingsoon").show();
	}

	return true;
};

ViewChart.prototype.getMetrics = function (indicatorList) {
	var indicatorKeys = indicatorList.getKeys();
	var usedMetrics = new Object();

	for (var i = 0; i < indicatorKeys.length; i++) {
		var indicator = this.target.dashboard.model.indicatorMap[indicatorKeys[i]];
		var metric = indicator.metric;
		if (usedMetrics[metric] != null) continue;
		usedMetrics[metric] = metric;
	}

	return usedMetrics;
};

ViewChart.prototype.getColors = function () {
	return State.colors;
};

ViewChart.prototype.renderChart = function (configuration) {
	var jLayer = $(this.DOMLayer);
	var jCanvas = jLayer.find(".canvas");
	var indicatorList = this.state.indicatorList;
	var compareList = this.state.compareList;
	var filterList = this.state.filterList;
	var rangeList = this.state.rangeList;
	var type = configuration.type;
	var datasourceUrl = Kernel.getChartUrl(this.target.id, type, indicatorList, compareList, filterList, rangeList, configuration.from, configuration.to, configuration.scale);

	if (configuration.from > configuration.to) {
		jLayer.find(".evolutionloading").hide();
		Desktop.hideLoading();
		return;
	}

	jLayer.find(".evolutionloading").show();
	this.hideMessages();
	Desktop.showLoading(Lang.ViewChart.Loading);

	jCanvas.html("");
	jCanvas.show();

	if (!this.validateIndicators(indicatorList)) {
		jLayer.find(".evolutionloading").hide();
		Desktop.hideLoading();
		return;
	}

	var jChart = $("<div></div>").attr("id", generateId());
	jCanvas.append(jChart);

	configuration.options.width = jCanvas.width();
	configuration.options.height = jCanvas.height();
	configuration.options.colors = this.getColors();
	configuration.options.imagesUrl = Context.Config.ImagesUrl;
	configuration.options.language = Context.Config.Language;
	configuration.options.metricsCount = size(this.getMetrics(indicatorList));

	this.target.api.createChart(type, jChart.get(0), datasourceUrl, configuration.options, ViewChart.prototype.atChartCreated.bind(this));
};

ViewChart.prototype.isBubbleChart = function () {
	return this.type == ViewChart.BUBBLE_CHART;
};

ViewChart.prototype.isTableChart = function () {
	return this.type == ViewChart.TABLE_CHART;
};

ViewChart.prototype.refresh = function () {

	this.refreshTimeLapse();
	this.refreshRange();
	this.refreshScale();
	this.refreshMessages();

	var stateTimeLapse = this.state.timeLapse;
	this.timeLapse = { from: stateTimeLapse.from, to: stateTimeLapse.to, scale: stateTimeLapse.scale };

	var range = this.target.dashboard.model.range;
	this.configuration = { min: range.min, max: range.max };
};

ViewChart.prototype.refreshTimeLapse = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogTimeLapse = jLayer.find(".dialogtimelapse");
	var stateTimeLapse = this.state.timeLapse;
	var jCanvas = jLayer.find(".canvas");

	if (jDialogTimeLapse.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogTimeLapse.hide();
		return;
	}

	jDialogTimeLapse.show();

	if (!this.requireRefreshTimeLapse() && !this.requireRefreshRange())
		return;

	this.dialogTimeLapse.state = this.state;
	this.dialogTimeLapse.target = { resolution: this.target.dashboard.model.resolution, range: this.target.dashboard.model.range, timeLapse: stateTimeLapse, width: jCanvas.width(), left: jCanvas.get(0).offsetLeft, freeWindowSize: this.freeTimeLapseWindowSize };
	this.dialogTimeLapse.render(jDialogTimeLapse.get(0));
};

ViewChart.prototype.refreshRange = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogRange = jLayer.find(".dialogrange");
	var jCanvas = jLayer.find(".canvas");

	if (jDialogRange.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogRange.hide();
		return;
	}

	jDialogRange.show();

	if (!this.requireRefreshRange() && !this.requireRefreshTimeLapse())
		return;

	this.dialogRange.state = this.state;
	this.dialogRange.target = { resolution: this.target.dashboard.model.resolution, range: this.target.dashboard.model.range, timeLapse: this.state.timeLapse, width: jCanvas.width() };
	this.dialogRange.render(jDialogRange.get(0));
};

ViewChart.prototype.refreshScale = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogScale = jLayer.find(".dialogscale");
	var jCanvas = jLayer.find(".canvas");

	if (jDialogScale.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogScale.hide();
		return;
	}

	jDialogScale.show();

	this.dialogScale.state = this.state;
	this.dialogScale.target = { resolution: this.target.dashboard.model.resolution, scale: this.state.timeLapse.scale, availableResolutions: this.getAvailableResolutions() };
	this.dialogScale.render(jDialogScale.get(0));
};

ViewChart.prototype.getAvailableResolutions = function () {
	var stateIndicatorList = this.state.indicatorList;
	var availableResolutions = {};

	for (var i = 0; i < this.target.dashboard.model.indicatorList.length; i++) {
		var indicator = this.target.dashboard.model.indicatorList[i];
		if (stateIndicatorList.get(indicator.id, "") == null) continue;
		for (var j = 0; j < indicator.resolutions.length; j++) {
			var resolution = Resolution.fromString(indicator.resolutions[j]);
			availableResolutions[resolution] = resolution;
		}
	}

	return availableResolutions;
};

ViewChart.prototype.refreshMessages = function () {
	var jMessages = $(this.DOMLayer).find(".canvasmessages");
	var stateIndicatorList = this.state.indicatorList;
	var invisibleIndicators = new Array();

	jMessages.html("");

	for (var i = 0; i < this.target.dashboard.model.indicatorList.length; i++) {
		var indicator = this.target.dashboard.model.indicatorList[i];

		if (stateIndicatorList.get(indicator.id, "") != null) {
			if (indicator.scale < this.state.timeLapse.scale)
				invisibleIndicators.push(indicator);
		}
	}

	if (invisibleIndicators.length <= 0) return;

	var messageTemplate = translate(invisibleIndicators.length == 1 ? AppTemplate.viewchartmessage : AppTemplate.viewchartmessagemultiple, Lang.ViewChart);
	var labels = this.getLabels(invisibleIndicators);
	var scale = this.calculateMaxCommonScale(invisibleIndicators);
	var jMessage = $.tmpl(messageTemplate, { labels: labels, scale: scale, dashboard: this.target.dashboard.code });
	jMessages.append(jMessage);

	CommandListener.capture(jMessages.get(0));
};

ViewChart.prototype.getMinMetrics = function () {
	return this.configuration.metrics.min;
};

ViewChart.prototype.getMaxMetrics = function () {
	return this.configuration.metrics.max;
};

ViewChart.prototype.getMinIndicators = function () {
	return this.configuration.indicators.min;
};

ViewChart.prototype.getMaxIndicators = function () {
	return this.configuration.indicators.max;
};

ViewChart.prototype.requireRefreshTimeLapse = function () {
	var stateTimeLapse = this.state.timeLapse;

	if (this.timeLapse == null) return true;
	if (this.timeLapse.scale != stateTimeLapse.scale) return true;
	if (this.timeLapse.from != null && this.timeLapse.from != stateTimeLapse.from) return true;
	if (this.timeLapse.to != null && this.timeLapse.to != stateTimeLapse.to) return true;

	return false;
};

ViewChart.prototype.requireRefreshRange = function () {
	var range = this.target.dashboard.model.range;

	if (this.configuration == null) return true;
	if (this.configuration.min != range.min) return true;
	if (this.configuration.max != range.max) return true;

	return false;
};

ViewChart.prototype.getLabels = function (invisibleIndicators) {
	var labels = "";
	for (var i = 0; i < invisibleIndicators.length; i++) {
		var indicator = invisibleIndicators[i];
		labels += i > 0 ? ",&nbsp;" : "";
		labels += indicator.label;
	}
	return labels;
}

ViewChart.prototype.calculateMaxCommonScale = function (invisibleIndicators) {
	var maxScale = Scale.YEAR;

	while (maxScale < Scale.SECOND) {
		for (var i = 0; i < invisibleIndicators.length; i++) {
			var indicator = invisibleIndicators[i];
			if (maxScale > indicator.scale)
				return maxScale - 1;
		}
		maxScale++;
	}

	return maxScale;
};

ViewChart.prototype.onResize = function () {
	if (this.state.chartType != this.type) return;
	var timeLapse = this.state.timeLapse;
	CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.type + "," + timeLapse.from + "," + timeLapse.to + ")");
};

//************************************************************************************************************

ViewChart.prototype.atChartCreated = function (chart) {
	var jLayer = $(this.DOMLayer);

	jLayer.find(".evolutionloading").hide();
	Desktop.hideLoading();

	jLayer.find(".message.emptychart").hide();
	if (chart == null)
		return;

	if (chart.info.isEmpty)
		jLayer.find(".message.emptychart").show();

	this.chart = chart;
	this.chart.onSelect = ViewChart.prototype.atChartSelectClick.bind(this);
	this.chart.onUnselect = ViewChart.prototype.atChartUnselectClick.bind(this);

//  if (this.target.center) {
//    this.target.api.select(this.chart, this.target.center);
//  }
//  else {
//    this.target.api.unselect(this.chart);
//  }
};

ViewChart.prototype.atChartSelectClick = function (chart, selection, measureUnit) {
};

ViewChart.prototype.atChartUnselectClick = function () {
};

ViewChart.prototype.atTimeLapseChange = function (timeLapse) {
	this.refreshMessages();
	CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.type + "," + timeLapse.from + "," + timeLapse.to + ")");
};

ViewChart.prototype.atScaleChange = function (scale, event) {
	var timeLapse = TimeLapse.toScale(this.state.timeLapse, scale);
	var values = this.getRange(timeLapse, this.freeTimeLapseWindowSize);
	CommandListener.throwCommand("changescale(" + this.target.dashboard.code + "," + scale + "," + values.min + "," + values.max + ")");
};

ViewChart.prototype.atRangeChange = function (timeStamp) {
	this.refreshMessages();
	var bounds = TimeLapse.getBounds(this.state.timeLapse, this.target.dashboard.model.range);
	var windowSize = TimeLapse.getWindowSize(this.state.timeLapse, bounds, this.freeTimeLapseWindowSize);
	var timeLapse = { from: timeStamp.value, to: timeStamp.value + windowSize.milliseconds, scale: timeStamp.scale };
	CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.type + "," + timeLapse.from + "," + timeLapse.to + ")");
};

DialogTimeLapse = function() {
  this.base = Dialog;
  this.base();
  this.lastTimeLapse = null;
  this.dialogScale = null;
  this.page = 0;
};

DialogTimeLapse.prototype = new Dialog;

DialogTimeLapse.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogtimelapse, Lang.DialogTimeLapse);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;
  
  if (this.target.width) {
    var jLayer = $(this.DOMLayer);
    jLayer.width(this.target.width);
    jLayer.css("margin-left", this.target.left);
  }

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogTimeLapse.prototype.init = function() {
  var jLayer = $(this.DOMLayer);
  var bounds = TimeLapse.getBounds(this.target.timeLapse, this.target.range);
  var windowSize = TimeLapse.getWindowSize(this.target.timeLapse, bounds, this.isFreeWindowSize());
  var values = TimeLapse.getValues(this.target.timeLapse, windowSize, bounds);
  
  if (this.state.mode != null)
    jLayer.find(".dialog.timelapse").addClass(this.state.mode);

  this.jSlider = jLayer.find(".slider");
  
  this.jSlider.dateRangeSlider({
    bounds: { min: new Date(bounds.min), max: new Date(bounds.max) },
    range: windowSize,
    arrows: false,
    defaultValues: { min: new Date(values.min), max: new Date(values.max) },
    formatter: DialogTimeLapse.prototype.getFormattedDate.bind(this),
    step: 1,
    wheelMode: "zoom"
  });
  
  this.jSlider.on("userValuesChanged", DialogTimeLapse.prototype.atChange.bind(this));
  this.jSlider.on("userValuesChanging", DialogTimeLapse.prototype.atChanging.bind(this));
};

DialogTimeLapse.prototype.refresh = function(jsonData) {
  var jLayer = $(this.DOMLayer);
  var bounds = TimeLapse.getBounds(this.target.timeLapse, this.target.range);
  var windowSize = TimeLapse.getWindowSize(this.target.timeLapse, bounds, this.isFreeWindowSize());
  var values = TimeLapse.getValues(this.target.timeLapse, windowSize, bounds);
  
  jLayer.find(".bounds.min").html(this.getFormattedDate(new Date(bounds.min)));
  jLayer.find(".bounds.max").html(this.getFormattedDate(new Date(bounds.max)));

  this.jSlider = jLayer.find(".slider");
  this.jSlider.dateRangeSlider("option", "bounds", { min: new Date(bounds.min), max: new Date(bounds.max) });
  this.jSlider.dateRangeSlider("option", "range", windowSize);
  this.jSlider.dateRangeSlider('values', new Date(values.min), new Date(values.max));
};

DialogTimeLapse.prototype.notifyChange = function(timeLapse) {
  
  if (this.lastTimeLapse != null) {
    var fromTime = timeLapse.from;
    var lastFromTime = this.lastTimeLapse.from;
    var toTime = timeLapse.to;
    var lastToTime = this.lastTimeLapse.to;
    if (fromTime == lastFromTime && toTime == lastToTime && timeLapse.scale == this.lastTimeLapse.scale) return;
  }
  
  if (this.onChange) this.onChange(timeLapse);
  
  this.lastTimeLapse = timeLapse;
};

DialogTimeLapse.prototype.getFormattedDate = function(date) {
  var scale = this.target.timeLapse.scale;
  return date.format(Lang.DateFormats[scale], Context.Config.Language);
};

DialogTimeLapse.prototype.isFreeWindowSize = function() {
  return this.target.freeWindowSize?this.target.freeWindowSize:false;
};

//************************************************************************************************************

DialogTimeLapse.prototype.atChange = function(event, data) {
  if (this.eventsDisabled) return true;
  if (data != null) {
    this.notifyChange({ from: data.values.min.getTime(), to: data.values.max.getTime(), scale: this.target.timeLapse.scale });
    this.refresh();
  }
};

DialogTimeLapse.prototype.atChanging = function(event, data) {
};

var SECOND = 1000;
var MINUTE = SECOND*60;
var HOUR = MINUTE*60;
var DAY = HOUR*24;
var WEEK = DAY*7;
var MONTH = DAY*30;
var YEAR = MONTH*12;

DialogRange = function() {
  this.base = Dialog;
  this.base();
  this.lastTimeStamp = null;
};

DialogRange.prototype = new Dialog;

DialogRange.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogrange, Lang.DialogRange);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;
  
  if (this.target.width) {
    var jLayer = $(this.DOMLayer);
    jLayer.width(this.target.width-parseInt(jLayer.css("margin-left").replace("px",""))+5);
  }

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogRange.prototype.init = function() {
  var jLayer = $(this.DOMLayer);
  var range = this.getRange();

  this.jSlider = jLayer.find(".slider");
  
  this.jSlider.slider({
    min: range.min,
    max: range.max,
    step: 1,
    change: DialogRange.prototype.atChange.bind(this),
    slide: DialogRange.prototype.atSlide.bind(this)
  });
  
  this.initBehaviours();
};

DialogRange.prototype.initBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  
  var jPlay = jLayer.find(".controls .play");
  jPlay.click(DialogRange.prototype.atPlayClick.bind(this));

  var jPause = jLayer.find(".controls .pause");
  jPause.click(DialogRange.prototype.atPauseClick.bind(this));
};

DialogRange.prototype.refresh = function() {
  var timeStamp = this.getTimeStamp();
  
  if (this.target.timeLapse.from != null)
    timeStamp = this.target.timeLapse.from;
  
  this.refreshTitle(timeStamp);
  this.refreshSlider();
  this.refreshControls();
};

DialogRange.prototype.refreshTitle = function(timeStamp) {
  var jLayer = $(this.DOMLayer);
  var jTitle = jLayer.find(".title span");
  jTitle.html(this.getFormattedDate(new Date(timeStamp)));
};

DialogRange.prototype.refreshSlider = function() {
  var range = this.getRange();
  var value = this.getValue();
  
  this.jSlider.slider("option", "min", range.min);
  this.jSlider.slider("option", "max", range.max);
  
  if (value > range.max) value = range.max;
  this.eventsDisabled = true;
  this.jSlider.slider("option", "value", value);
  this.eventsDisabled = false;
  
  this.jSlider.slider("option", "disabled", range.max <= 1);
};

DialogRange.prototype.refreshControls = function() {
  var jLayer = $(this.DOMLayer);
  var jPlay = jLayer.find(".controls .play");
  var jPause = jLayer.find(".controls .pause");
  var value = this.jSlider.slider("option", "value");

  jPlay.css("display", (!this.playing)?"block":"none");
  jPause.css("display", (this.playing)?"block":"none");
  
  jPlay.removeClass("disabled");
  
  var max = this.jSlider.slider("option", "max");
  if (max <= 1)
    jPlay.addClass("disabled");
  else if (!this.playing) {
    if (value >= max)
      jPlay.addClass("disabled");
  }
};

DialogRange.prototype.notifyChange = function(value) {
  var timeStamp = { value: this.toTimeStamp(value), scale: this.target.timeLapse.scale };
  
  if (this.lastTimeStamp != null) {
    var time = timeStamp.value;
    var lastTime = this.lastTimeStamp.value;
    if (time == lastTime && timeStamp.scale == this.lastTimeStamp.scale) return;
  }
  
  this.refreshTitle(this.getTimeStamp(value));
  this.refreshControls();
  
  if (this.onChange) this.onChange(timeStamp);
  this.lastTimeStamp = { value: timeStamp.value, scale: timeStamp.scale };
};

DialogRange.prototype.play = function() {
  
  this.playing = true;

  if (this.canContinuePlaying && !this.canContinuePlaying()) {
    this.playTimeout = window.setTimeout(DialogRange.prototype.play.bind(this), 3000);
    return;
  }
  
  var value = this.jSlider.slider('option', 'value');
  var max = this.jSlider.slider('option', 'max');
  
  value++;
  if (value >= max) {
    this.jSlider.slider('option', 'value', max);
    this.pause();
    return;
  }
  
  this.jSlider.slider('option', 'value', value);
  this.playTimeout = window.setTimeout(DialogRange.prototype.play.bind(this), 1200);
};

DialogRange.prototype.pause = function() {
  window.clearTimeout(this.playTimeout);
  this.playing = false;
  this.refreshControls();
};

DialogRange.prototype.getFormattedDate = function(date) {
  var scale = this.target.timeLapse.scale;
  return date.format(Lang.DateFormats[scale], Context.Config.Language);
};

DialogRange.prototype.getValue = function() {
  var value = this.jSlider.slider("option", "value");
  
  if (this.target.timeLapse.from != null) {
    var factor = this.getTimeStampFactor();
    value = Math.round((this.target.timeLapse.from-this.target.range.min)/factor);
  }
  
  return value;
};

DialogRange.prototype.getTimeStamp = function(value) {
  value = value!=null?value:this.jSlider.slider("option", "value");
  return this.toTimeStamp(value);
};

DialogRange.prototype.getTimeStampFactor = function() {
  var scale = this.target.timeLapse.scale;
  var factor = 1;
  
  if (scale == Scale.YEAR) factor = YEAR;
  else if (scale == Scale.MONTH) factor = MONTH;
  else if (scale == Scale.DAY) factor = DAY;
  else if (scale == Scale.HOUR) factor = HOUR;
  else if (scale == Scale.MINUTE) factor = MINUTE;
  else if (scale == Scale.SECOND) factor = SECOND;

  return factor;
};

DialogRange.prototype.getRange = function() {
  var range = this.target.range;
  var count = range.max-range.min;
  var factor = this.getTimeStampFactor();
  return { min: 0, max: Math.round(count/factor) };
};  

DialogRange.prototype.toTimeStamp = function(value) {
  var factor = this.getTimeStampFactor();
  return this.target.range.min+(value*factor);
};

//************************************************************************************************************

DialogRange.prototype.atChange = function(event, ui) {
  if (this.eventsDisabled) return true;
  this.notifyChange(ui.value);
};

DialogRange.prototype.atSlide = function(event, ui) {
  if (this.eventsDisabled) return true;
  this.refreshTitle(this.getTimeStamp(ui.value));
};

DialogRange.prototype.atPlayClick = function() {
  var value = this.jSlider.slider('option','value');
  var max = this.jSlider.slider('option', 'max');
  if (value >= max) return;
  window.setTimeout(DialogRange.prototype.play.bind(this), 2000);
  this.playing = true;
  this.refreshControls();
};

DialogRange.prototype.atPauseClick = function() {
  this.pause();
};

ViewLineChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.LINE_CHART, { metrics : { min: 1, max: 2 }, indicators: { min: 1, max: -1 } });
};

ViewLineChart.prototype = new ViewChart;

ViewLineChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewlinechart, Lang.ViewLineChart);
  var content = translate(content, Lang.ViewChart);
  
  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart line");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewLineChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewLineChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "linechart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "linechart";
  
  this.renderChart(configuration);
};

ViewBarChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.BAR_CHART, { metrics : { min: 1, max: 2 }, indicators: { min: 1, max: -1 } });
};

ViewBarChart.prototype = new ViewChart;

ViewBarChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewbarchart, Lang.ViewBarChart);
  var content = translate(content, Lang.ViewChart);
  
  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart bar");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewBarChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewBarChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "barchart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "barchart";
  
  this.renderChart(configuration);
};

ViewTableChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.TABLE_CHART, { metrics : { min: 1, max: -1 }, indicators: { min: 1, max: -1 } });
  this.freeTimeLapseWindowSize = true;
};

ViewTableChart.prototype = new ViewChart;

ViewTableChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewtablechart, Lang.ViewTableChart);
  var content = translate(content, Lang.ViewChart);
  
  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart table");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewTableChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewTableChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "tablechart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "tablechart";
  
  this.renderChart(configuration);
};

function Map() {
}

Map.POINT = "pointlayer";
Map.HEAT = "heatlayer";

DialogMapLayer = function () {
	this.base = Dialog;
	this.base();
};

DialogMapLayer.prototype = new Dialog;

DialogMapLayer.prototype.render = function (DOMLayer) {
	var content = translate(AppTemplate.dialogmaplayer, Lang.DialogMapLayer);

	content = replaceAll(content, "${id}", this.id);

	this.DOMLayer = DOMLayer;
	this.DOMLayer.innerHTML = content;

	if (!this.target) return;

	if (this.target.left) {
		var jLayer = $(this.DOMLayer);
		jLayer.css("left", this.target.left);
	}

	CommandListener.capture(this.DOMLayer);

	this.init();
	this.refresh();
};

DialogMapLayer.prototype.init = function () {
	this.initBehaviours();
};

DialogMapLayer.prototype.initBehaviours = function () {
	var jLayer = $(this.DOMLayer);

	var jLayerPoint = jLayer.find(".layer." + Map.POINT + " a");
	jLayerPoint.click(DialogMapLayer.prototype.atLayerClick.bind(this, Map.POINT));

	var jLayerHeat = jLayer.find(".layer." + Map.HEAT + " a");
	jLayerHeat.click(DialogMapLayer.prototype.atLayerClick.bind(this, Map.HEAT));
};

DialogMapLayer.prototype.refresh = function (jsonData) {
	var jLayer = $(this.DOMLayer);

	jLayer.find(".layer").removeClass("active");

	var jActiveLayer = jLayer.find(".layer." + this.target.selected);
	jActiveLayer.addClass("active");
};

//************************************************************************************************************

DialogMapLayer.prototype.atLayerClick = function (layer) {
	if (this.onChange) this.onChange(layer);
};

ViewMapChart = function () {
	this.base = ViewChart;
	this.base(ViewChart.MAP_CHART, { metrics: { min: 1, max: 1 }, indicators: { min: 1, max: 1 }, comingSoon: false });
};

ViewMapChart.prototype = new ViewChart;

ViewMapChart.prototype.init = function (DOMLayer) {
	var content = translate(AppTemplate.viewmapchart, Lang.ViewMapChart);
	var content = translate(content, Lang.ViewChart);

	content = replaceAll(content, "${id}", this.id);

	this.DOMLayer = DOMLayer;
	$(this.DOMLayer).html(content);

	if (!this.target) return;

	var jLayer = $(this.DOMLayer);
	jLayer.addClass("view chart map");

	this.initMode();
	this.initTimeLapse();
	this.initRange();
	this.initScale();
	this.initMapLayer();
	this.initMessages();
};

ViewMapChart.prototype.initMapLayer = function () {
	if ($(this.DOMLayer).find(".dialoglayer").length == 0)
		return;

	this.dialogMapLayer = new DialogMapLayer();
	this.dialogMapLayer.onChange = ViewChart.prototype.atLayerChange.bind(this);
};

ViewMapChart.prototype.refresh = function () {
	this.refreshTimeLapse();
	this.refreshRange();
	this.refreshScale();
	this.refreshMapLayer();
	this.refreshCanvas();
	this.refreshMessages();
};

ViewMapChart.prototype.refreshMapLayer = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogMapLayer = jLayer.find(".dialoglayer");
	var jCanvas = jLayer.find(".canvas");

	if (jDialogMapLayer.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogMapLayer.hide();
		return;
	}

	jDialogMapLayer.show();
	jDialogMapLayer.get(0).style.right = (jLayer.width()-jCanvas.width())/2 + "px";

	this.dialogMapLayer.state = this.state;
	this.dialogMapLayer.target = { selected: this.getChartLayer() };
	this.dialogMapLayer.render(jDialogMapLayer.get(0));
};

ViewMapChart.prototype.refreshCanvas = function () {
	var configuration = new Object();
	var timeLapse = this.state.timeLapse;
	var range = this.getRange(null, this.freeTimeLapseWindowSize);

	configuration.type = "mapchart";
	configuration.from = range.min;
	configuration.to = range.max;
	configuration.scale = timeLapse.scale;
	configuration.options = new Object();
	configuration.options.layerType = this.getChartLayer();

	this.renderChart(configuration);
};

ViewMapChart.prototype.getChartLayer = function () {
	return this.state.chartLayer!=null?this.state.chartLayer:Map.POINT;
};

//************************************************************************************************************

ViewChart.prototype.atLayerChange = function (layer) {
	CommandListener.throwCommand("changemaplayer(" + this.target.dashboard.code + "," + layer + ")");
};

ViewBubbleChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.BUBBLE_CHART, { metrics : { min: 1, max: 4 }, indicators: { min: 2, max: 4 } });
};

ViewBubbleChart.prototype = new ViewChart;

ViewBubbleChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewbubblechart, Lang.ViewBubbleChart);
  var content = translate(content, Lang.ViewChart);

  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart bubble");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewBubbleChart.prototype.getColors = function() {
  return null;
}

ViewBubbleChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewBubbleChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "bubblechart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "bubblechart";
  
  this.renderChart(configuration);
};

ViewChartFactory = new Object();

ViewChartFactory.get = function(type) {
  var view = null;
  
  if (type == ViewChart.LINE_CHART) view = new ViewLineChart();
  else if (type == ViewChart.BAR_CHART) view = new ViewBarChart();
  else if (type == ViewChart.MAP_CHART) view = new ViewMapChart();
  else if (type == ViewChart.BUBBLE_CHART) view = new ViewBubbleChart();
  else if (type == ViewChart.TABLE_CHART) view = new ViewTableChart();
  
  return view;
};

//----------------------------------------------------------------------
// Process render dashboard info
//----------------------------------------------------------------------
function ProcessRenderInfo() {
    this.base = Process;
    this.base(1);
};

ProcessRenderInfo.prototype = new Process;
ProcessRenderInfo.constructor = ProcessRenderInfo;

ProcessRenderInfo.prototype.step_1 = function () {
    var viewInfo = new ViewDashboardInfo();
    var viewId = View.DASHBOARD_INFO.replace("::id::", this.target.id);

    this.layer.id = viewId;
    Desktop.registerView(viewId, viewInfo);

    viewInfo.id = viewId;
    viewInfo.target = this.target;
    Desktop.addState(viewInfo);

    viewInfo.init(this.layer);
    viewInfo.refresh();

    this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render indicators
//----------------------------------------------------------------------
function ProcessRenderIndicators() {
	this.base = Process;
	this.base(1);
};

ProcessRenderIndicators.prototype = new Process;
ProcessRenderIndicators.constructor = ProcessRenderIndicators;

ProcessRenderIndicators.prototype.step_1 = function () {
	var viewIndicators = new ViewIndicators();
	var viewId = View.INDICATORS.replace("::id::", this.target.id);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewIndicators);

	viewIndicators.id = viewId;
	viewIndicators.target = this.target;
	Desktop.addState(viewIndicators);

	viewIndicators.init(this.layer);
	viewIndicators.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render measure units
//----------------------------------------------------------------------
function ProcessRenderMeasureUnits() {
	this.base = Process;
	this.base(1);
};

ProcessRenderMeasureUnits.prototype = new Process;
ProcessRenderMeasureUnits.constructor = ProcessRenderMeasureUnits;

ProcessRenderMeasureUnits.prototype.step_1 = function () {
	var viewMeasureUnits = new ViewMeasureUnits();
	var viewId = View.MEASURE_UNITS.replace("::id::", this.target.id);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewMeasureUnits);

	viewMeasureUnits.id = viewId;
	viewMeasureUnits.target = this.target;
	Desktop.addState(viewMeasureUnits);

	viewMeasureUnits.init(this.layer);
	viewMeasureUnits.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process refresh measure units
//----------------------------------------------------------------------
function ProcessRefreshMeasureUnits() {
	this.base = Process;
	this.base(1);
};

ProcessRefreshMeasureUnits.prototype = new Process;
ProcessRefreshMeasureUnits.constructor = ProcessRefreshMeasureUnits;

ProcessRefreshMeasureUnits.prototype.step_1 = function () {
	var viewId = View.MEASURE_UNITS.replace("::id::", this.dashboardId);
	var viewMeasureUnits = Desktop.getView(viewId);

	if (this.dashboard) viewMeasureUnits.target.dashboard = this.dashboard;
	Desktop.addState(viewMeasureUnits);
	viewMeasureUnits.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render taxonomies
//----------------------------------------------------------------------
function ProcessRenderTaxonomies() {
	this.base = Process;
	this.base(1);
};

ProcessRenderTaxonomies.prototype = new Process;
ProcessRenderTaxonomies.constructor = ProcessRenderTaxonomies;

ProcessRenderTaxonomies.prototype.step_1 = function () {
	var viewTaxonomies = new ViewTaxonomies();
	var viewId = View.TAXONOMIES.replace("::id::", this.target.id);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewTaxonomies);

	viewTaxonomies.id = viewId;
	viewTaxonomies.target = this.target;
	Desktop.addState(viewTaxonomies);

	viewTaxonomies.init(this.layer);
	viewTaxonomies.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process refresh taxonomies
//----------------------------------------------------------------------
function ProcessRefreshTaxonomies() {
	this.base = Process;
	this.base(1);
};

ProcessRefreshTaxonomies.prototype = new Process;
ProcessRefreshTaxonomies.constructor = ProcessRefreshTaxonomies;

ProcessRefreshTaxonomies.prototype.step_1 = function () {
	var viewId = View.TAXONOMIES.replace("::id::", this.dashboardId);
	var viewTaxonomies = Desktop.getView(viewId);

	if (this.dashboard) viewTaxonomies.target.dashboard = this.dashboard;
	Desktop.addState(viewTaxonomies);
	viewTaxonomies.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render chart
//----------------------------------------------------------------------
function ProcessRenderChart() {
	this.base = Process;
	this.base(2);
};

ProcessRenderChart.prototype = new Process;
ProcessRenderChart.constructor = ProcessRenderChart;

ProcessRenderChart.prototype.updateMaxMetrics = function (viewIndicators) {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboardId));
	var viewChartId = viewDashboard.getChartLayer(this.chartType).id;
	var viewIndicatorsId = viewDashboard.getIndicatorsLayer().id;

	var viewChart = Desktop.getView(viewChartId);
	var viewIndicators = Desktop.getView(viewIndicatorsId);

	viewIndicators.setMaxMetrics(viewChart.getMaxMetrics());
	viewIndicators.setMaxIndicators(viewChart.getMaxIndicators());
	viewIndicators.checkIndicators();
};

ProcessRenderChart.prototype.checkColorsActivation = function (viewChart) {
	var viewIndicators = Desktop.getView(View.INDICATORS.replace("::id::", this.dashboardId));
	var viewTaxonomies = Desktop.getView(View.TAXONOMIES.replace("::id::", this.dashboardId));

	if (viewChart.isBubbleChart()) {
		viewIndicators.disableColors();
		viewTaxonomies.disableColors();
	}
	else {
		viewIndicators.enableColors();
		viewTaxonomies.enableColors();
	}
};

ProcessRenderChart.prototype.step_1 = function () {
	if (this.chartType == null) this.chartType = ViewChart.LINE_CHART;
	Kernel.loadChartApi(this, this.chartType);
};

ProcessRenderChart.prototype.step_2 = function () {
	var chartApi = this.getChartApi(this.data);
	var viewChart = ViewChartFactory.get(this.chartType);
	var viewId = View.CHART.replace("::id::", this.target.id).replace("::type::", this.chartType);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewChart);

	viewChart.id = viewId;
	viewChart.target = this.target;
	viewChart.target.api = chartApi;
	Desktop.addState(viewChart);

	viewChart.init(this.layer);
	viewChart.refresh();

	this.checkColorsActivation(viewChart);
	this.updateMaxMetrics();
	State.chartType = this.chartType;

	var viewDashboardInfo = Desktop.getView(View.DASHBOARD_INFO.replace("::id::", this.dashboardId));
	viewDashboardInfo.target.freeTimeLapseWindowSize = viewChart.isTableChart();
	viewDashboardInfo.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process refresh chart
//----------------------------------------------------------------------
function ProcessRefreshChart() {
	this.base = Process;
	this.base(1);
};

ProcessRefreshChart.prototype = new Process;
ProcessRefreshChart.constructor = ProcessRefreshChart;

ProcessRefreshChart.prototype.step_1 = function () {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboardId));
	var chartLayers = this.chartType != null ? [viewDashboard.getChartLayer(this.chartType)] : viewDashboard.getChartLayers();
	var isTableChart = false;

	for (var i = 0; i < chartLayers.length; i++) {
		var viewId = chartLayers[i].id;
		var viewChart = Desktop.getView(viewId);

		if (i == 0)
			isTableChart = viewChart.isTableChart();

		if (this.dashboard) viewChart.target.dashboard = this.dashboard;
		Desktop.addState(viewChart);
		viewChart.refresh();
	}

	var viewIndicators = Desktop.getView(View.INDICATORS.replace("::id::", this.dashboardId));
	if (State.compareList.size() > 0 && State.indicatorList.size() == 1)
		viewIndicators.disableColors();
	else
		viewIndicators.enableColors();

	var viewDashboardInfo = Desktop.getView(View.DASHBOARD_INFO.replace("::id::", this.dashboardId));
	viewDashboardInfo.target.freeTimeLapseWindowSize = isTableChart;
	viewDashboardInfo.refresh();

	this.terminateOnSuccess();
};

CLASS_VIEW_DASHBOARD = "dashboard";
CSS_VIEW_DASHBOARD = ".view.dashboard";

ViewDashboard = function () {
  this.base = View;
  this.base();
};

ViewDashboard.prototype = new View;

ViewDashboard.prototype.init = function(DOMLayer) {
  var jLayer = $(DOMLayer);

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewdashboard, Lang.ViewDashboard);
  jLayer.addClass(CLASS_VIEW_DASHBOARD);
  
  if (!this.target) return;
  
  CommandListener.capture(this.DOMLayer);
  jLayer.find(".dialog").hide();
  
  if (this.state.mode != null)
    jLayer.addClass(this.state.mode);
  
  this.initLayer();
};

ViewDashboard.prototype.initLayer = function() {
  var jLayer = $(this.DOMLayer);
  var eastWidth = screen.width*.40;
  
  if (this.state.mode != State.Mode.PRINT) {
    var layout = jLayer.find(".layout").layout({
      north__size: 60,
      north__spacing_open: -1,
      north__initHidden: this.state.mode == State.Mode.EMBED,
      west__minSize: eastWidth-(eastWidth*0.60),
      west__size: eastWidth-(eastWidth*0.40),
      west__maxSize: eastWidth,
      west__spacing_open: 4,
      west__spacing_closed: 10,
      west__onresize: function (pane, $pane, paneState, paneOptions) {
        Desktop.expandWestLayer(null);
      }
    });
  }

  this.initHeader();
};

ViewDashboard.prototype.getChartType = function(jChart) {
  var className = jChart.attr("class");
  var type = className.replace("chart", "");
  
  type = type.replace("active", "");
  type = trim(type);
  
  if (type == "line") return ViewChart.LINE_CHART;
  else if (type == "bar") return ViewChart.BAR_CHART;
  else if (type == "map") return ViewChart.MAP_CHART;
  else if (type == "bubble") return ViewChart.BUBBLE_CHART;
  else if (type == "table") return ViewChart.TABLE_CHART;

  return "";
};

ViewDashboard.prototype.addBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  var jContainer = jLayer.find(".ui-layout-center");
  
  jLayer.find(".toolbar > .chart a").click(ViewDashboard.prototype.atChartClick.bind(this));
  jLayer.find(".toolbar > .print a").click(ViewDashboard.prototype.atPrintClick.bind(this));
  jLayer.find(".toolbar > .download a").click(ViewDashboard.prototype.atDownloadClick.bind(this));
  jLayer.find(".reload").click(ViewDashboard.prototype.atReloadClick.bind(this));

  CommandListener.capture(jContainer);
};

ViewDashboard.prototype.refresh = function() {
  var dashboardContent = translate(AppTemplate.viewdashboardcontent, Lang.ViewDashboard);
  var jLayer = $(this.DOMLayer);
  var jContainer = jLayer.find(".ui-layout-center");
  
  this.refreshHeader();
  
  jContainer.html(dashboardContent);
  
  this.addBehaviours();
};

ViewDashboard.prototype.showNotLoaded = function() {
  $(this.DOMLayer).find(".layer.notLoaded").get(0).style.display = "block";
  $(this.DOMLayer).find(".layout").get(0).style.display = "none";
};

ViewDashboard.prototype.getIndicatorsLayer = function() {
  return $(this.DOMLayer).find(".layer.indicators").get(0);
};

ViewDashboard.prototype.getMeasureUnitsLayer = function() {
  return $(this.DOMLayer).find(".layer.measureunits").get(0);
};

ViewDashboard.prototype.getCompareLayer = function() {
  return $(this.DOMLayer).find(".layer.compare").get(0);
};

ViewDashboard.prototype.getTaxonomiesLayer = function() {
  return $(this.DOMLayer).find(".layer.taxonomies").get(0);
};

ViewDashboard.prototype.getInfoLayer = function() {
  return $(this.DOMLayer).find(".layer.info").get(0);
};

ViewDashboard.prototype.getChartLayer = function(type) {
  var jChartsContainer = $(this.DOMLayer).find(".layer.chart");
  
  if (this.state.mode != State.Mode.PRINT)
    return jChartsContainer.get(0);
  
  var jChart = jChartsContainer.find("." + type + ".chartlayeritem");
  if (jChart.length == 0) {
    var jChart = $("<div class=\"" + type + " chartlayeritem\"></div>");
    jChartsContainer.append(jChart);
  }
    
  return jChart.get(0);
};

ViewDashboard.prototype.getChartLayers = function() {
  var jChartsContainer = $(this.DOMLayer).find(".layer.chart");
  
  if (this.state.mode != State.Mode.PRINT) 
    return [jChartsContainer.get(0)];
  
  var jCharts = jChartsContainer.find(".chartlayeritem");
  var result = new Array();
  for (var i=0; i<jCharts.length; i++)
    result.push(jCharts[i].get(0));
  
  return result;
};

ViewDashboard.prototype.getUrl = function(operation) {
  var dashboard = this.target.dashboard.code;
  var chartType = State.chartType;
  var indicatorList = State.indicatorList;
  var compareList = State.compareList;
  var filterList = State.filterList;
  var rangeList = State.rangeList;
  var timeLapse = State.timeLapse;
  var range = this.getRange(null, false);
  var url = null;
  
  if (operation == "print")
    url = Kernel.getPrintDashboardUrl(dashboard, chartType, indicatorList, compareList, filterList, rangeList, range.min, range.max, timeLapse.scale, View.colors);
  else if (operation = "download")
    url = Kernel.getDownloadDashboardUrl(dashboard, "xlsformat", indicatorList, compareList, filterList, rangeList, range.min, range.max, timeLapse.scale);

  return url;
};

//************************************************************************************************************
ViewDashboard.prototype.atChartClick = function(event) {
  var jChartLink = $(event.target);
  var jChart = jChartLink.parents("li");
  var jLayer = $(this.DOMLayer);
  var timeLapse = this.state.timeLapse;

  jLayer.find(".toolbar > .chart").removeClass("active");
  jChart.addClass("active");
  
  CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.getChartType(jChart) + "," + State.timeLapse.from + "," + State.timeLapse.to + ")");
};

ViewDashboard.prototype.atPrintClick = function(event) {
  window.open(this.getUrl("print"), "_blank", "menubar=1,scrollbars=1,width=1024");
};

ViewDashboard.prototype.atDownloadClick = function(event) {
  var downloadUrl = this.getUrl("download");
  var jLayer = $(this.DOMLayer);
  var jLink = jLayer.find(".toolbar > .download a").attr("href", downloadUrl); 
  return true;
};

ViewDashboard.prototype.atReloadClick = function(event) {
    window.location.reload(true);
};

//----------------------------------------------------------------------
// Action show dashboard
//----------------------------------------------------------------------
function ActionShowDashboard() {
	this.base = Action;
	this.base(2);
};

ActionShowDashboard.prototype = new Action;
ActionShowDashboard.constructor = ActionShowDashboard;
CommandFactory.register(ActionShowDashboard, { dashboard: 0, view: 1, mode: 2, chartType: 3, scale: 4 }, false);

ActionShowDashboard.prototype.renderInfo = function (view) {
	var process = new ProcessRenderInfo();
	process.target = view.target;
	process.layer = view.getInfoLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderIndicators = function (view) {
	var process = new ProcessRenderIndicators();
	process.target = view.target;
	process.layer = view.getIndicatorsLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderMeasureUnits = function (view) {
	var process = new ProcessRenderMeasureUnits();
	process.target = view.target;
	process.layer = view.getMeasureUnitsLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderTaxonomies = function (view) {
	var process = new ProcessRenderTaxonomies();
	process.target = view.target;
	process.layer = view.getTaxonomiesLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderChart = function (view) {
	var process = new ProcessRenderChart();
	process.dashboardId = this.dashboard;
	process.target = view.target;

	if (this.chartType != null && this.chartType != "" && this.chartType != "null")
		process.chartType = this.chartType;

	process.layer = view.getChartLayer(State.chartType);
	process.execute();
};

ActionShowDashboard.prototype.step_1 = function () {
	var id = View.DASHBOARD.replace("::id::", this.dashboard);
	var view = Desktop.getView(id);

	if (State.chartType == null)
		State.chartType = ViewChart.LINE_CHART;

	if (view != null)
		return;

	Kernel.loadDashboard(this, this.dashboard, State.indicatorList, this.view);
};

ActionShowDashboard.prototype.step_2 = function () {
	var dashboard = (this.data != null && this.data !== "") ? this.getDashboard(this.data) : null;
	var dashboardScale = dashboard != null ? dashboard.model.scale : null;

	State.timeLapse.scale = (this.scale != null && this.scale != "" && this.scale != "null") ? this.scale : dashboardScale;

	if (this.mode != null)
		State.mode = this.mode;

	var view = new ViewDashboard();
	view.id = View.DASHBOARD.replace("::id::", this.dashboard);
	view.target = { id: this.dashboard, dashboard: dashboard };
	Desktop.addState(view);

	Desktop.cleanViewsContainer();
	view.init(Desktop.createView());
	view.refresh();

	Desktop.registerView(view.id, view);
	Desktop.hideLoading();

	if (dashboard == null) {
		view.showNotLoaded();
		return;
    }

	this.renderInfo(view);
	this.renderIndicators(view);
	this.renderMeasureUnits(view);
	this.renderTaxonomies(view);
	this.renderChart(view);

	Desktop.expandWestLayer(Desktop.getWestViews().get(0));

	this.terminate();
};

//----------------------------------------------------------------------
// Action refresh dashboard
//----------------------------------------------------------------------
function ActionRefreshDashboard() {
	this.base = Action;
	this.base(1);
};

ActionRefreshDashboard.prototype = new Action;
ActionRefreshDashboard.constructor = ActionRefreshDashboard;
CommandFactory.register(ActionRefreshDashboard, { dashboard: 0 }, false);

ActionRefreshDashboard.prototype.step_1 = function () {
	this.updateColors();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action refresh chart
//----------------------------------------------------------------------
function ActionRefreshChart() {
	this.base = Action;
	this.base(1);
};

ActionRefreshChart.prototype = new Action;
ActionRefreshChart.constructor = ActionRefreshChart;
CommandFactory.register(ActionRefreshChart, { dashboard: 0, chartType: 1, from: 2, to: 3 }, false);

ActionRefreshChart.prototype.step_1 = function () {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var process;

	if (this.from != "" && this.from != "null") State.timeLapse.from = parseInt(this.from);
	if (this.to != "" && this.to != "null") State.timeLapse.to = parseInt(this.to);

	if (State.chartType == null || State.chartType != this.chartType)
		process = new ProcessRenderChart();
	else
		process = new ProcessRefreshChart();

	process.dashboardId = this.dashboard;
	process.chartType = this.chartType;
	process.target = viewDashboard.target;
	process.layer = viewDashboard.getChartLayer(this.chartType);
	process.execute();
};

//----------------------------------------------------------------------
// Action toggle indicator
//----------------------------------------------------------------------
function ActionToggleIndicator() {
	this.base = Action;
	this.base(2);
};

ActionToggleIndicator.prototype = new Action;
ActionToggleIndicator.constructor = ActionToggleIndicator;
CommandFactory.register(ActionToggleIndicator, { dashboard: 0, indicator: 1, view: 2 }, false);

ActionToggleIndicator.prototype.launchRefreshProcess = function (process, dashboard) {
	process.dashboardId = dashboard.code;
	process.dashboard = dashboard;
	process.execute();
};

ActionToggleIndicator.prototype.updateMaxMetrics = function (viewIndicators) {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var viewId = viewDashboard.getChartLayers()[0].id;
	var viewChart = Desktop.getView(viewId);
	viewIndicators.setMaxMetrics(viewChart.getMaxMetrics());
	viewIndicators.setMaxIndicators(viewChart.getMaxIndicators());
};

ActionToggleIndicator.prototype.getMinScale = function (dashboard) {
	var result = Scale.SECOND;
	var stateIndicatorList = State.indicatorList;

	for (var i = 0; i < dashboard.model.indicatorList.length; i++) {
		var indicator = dashboard.model.indicatorList[i];
		if (stateIndicatorList.get(indicator.id, "") == null) continue;
		for (var j = 0; j < indicator.resolutions.length; j++) {
			var resolution = Resolution.fromString(indicator.resolutions[j]);
			if (resolution < result)
				result = resolution;
		}
	}

	return result;
};

ActionToggleIndicator.prototype.step_1 = function () {
	var viewId = View.INDICATORS.replace("::id::", this.dashboard);
	var view = Desktop.getView(viewId);

	this.updateMaxMetrics(view);

	if (State.indicatorList.get(this.indicator, "")) {
		view.unselectIndicator(this.indicator);
		State.indicatorList.deleteItem(this.indicator, "");
	}
	else {
		view.selectIndicator(this.indicator);
		State.indicatorList.add(this.indicator);
	}

	Kernel.loadDashboard(this, this.dashboard, State.indicatorList, this.view);
};

ActionToggleIndicator.prototype.step_2 = function () {
	var dashboard = this.getDashboard(this.data);
	var minScale = this.getMinScale(dashboard);

	if (State.timeLapse.scale > minScale)
		State.timeLapse.scale = minScale;

	State.compareList.clear();
	this.updateColors();

	this.launchRefreshProcess(new ProcessRefreshChart(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshTaxonomies(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshMeasureUnits(), dashboard);
};

//----------------------------------------------------------------------
// Action unselect indicators
//----------------------------------------------------------------------
function ActionUnselectIndicators() {
	this.base = Action;
	this.base(2);
};

ActionUnselectIndicators.prototype = new Action;
ActionUnselectIndicators.constructor = ActionUnselectIndicators;
CommandFactory.register(ActionUnselectIndicators, { dashboard: 0, view: 1 }, false);

ActionUnselectIndicators.prototype.launchRefreshProcess = function (process, dashboard) {
	process.dashboardId = dashboard.code;
	process.dashboard = dashboard;
	process.execute();
};

ActionUnselectIndicators.prototype.updateMaxMetrics = function (viewIndicators) {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var viewId = viewDashboard.getChartLayers()[0].id;
	var viewChart = Desktop.getView(viewId);
	viewIndicators.setMaxMetrics(viewChart.getMaxMetrics());
	viewIndicators.setMaxIndicators(viewChart.getMaxIndicators());
};

ActionUnselectIndicators.prototype.step_1 = function () {
	var viewId = View.INDICATORS.replace("::id::", this.dashboard);
	var view = Desktop.getView(viewId);

	this.updateMaxMetrics(view);
	State.indicatorList.clear();
	view.unselectIndicators();

	Kernel.loadDashboard(this, this.dashboard, State.indicatorList, this.view);
};

ActionUnselectIndicators.prototype.step_2 = function () {
	var dashboard = this.getDashboard(this.data);

	this.updateColors();

	this.launchRefreshProcess(new ProcessRefreshChart(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshTaxonomies(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshMeasureUnits(), dashboard);
};

//----------------------------------------------------------------------
// Action change scale
//----------------------------------------------------------------------
function ActionChangeScale() {
	this.base = Action;
	this.base(1);
};

ActionChangeScale.prototype = new Action;
ActionChangeScale.constructor = ActionChangeScale;
CommandFactory.register(ActionChangeScale, { dashboard: 0, scale: 1, from: 2, to: 3 }, false);

ActionChangeScale.prototype.step_1 = function () {

	State.timeLapse.scale = parseInt(this.scale);
	if (this.from != null && this.from != "" && this.from != "null") State.timeLapse.from = parseInt(this.from);
	if (this.to != null && this.to != "" && this.to != "null") State.timeLapse.to = parseInt(this.to);

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action change map layer
//----------------------------------------------------------------------
function ActionChangeMapLayer() {
	this.base = Action;
	this.base(1);
};

ActionChangeMapLayer.prototype = new Action;
ActionChangeMapLayer.constructor = ActionChangeMapLayer;
CommandFactory.register(ActionChangeMapLayer, { dashboard: 0, layer: 1 }, false);

ActionChangeMapLayer.prototype.step_1 = function () {

	State.chartLayer = this.layer;

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action delete filter
//----------------------------------------------------------------------
function ActionDeleteFilter() {
	this.base = Action;
	this.base(2);
};

ActionDeleteFilter.prototype = new Action;
ActionDeleteFilter.constructor = ActionDeleteFilter;
CommandFactory.register(ActionDeleteFilter, { dashboard: 0, filterId: 1 }, false);

ActionDeleteFilter.prototype.step_1 = function () {

	State.filterList.deleteItem(this.filterId);

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.returnProcess = this;
	process.execute();
};

ActionDeleteFilter.prototype.step_2 = function () {
	var process = new ProcessRefreshTaxonomies();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action unselect filters
//----------------------------------------------------------------------
function ActionUnselectFilters() {
	this.base = Action;
	this.base(2);
};

ActionUnselectFilters.prototype = new Action;
ActionUnselectFilters.constructor = ActionUnselectFilters;
CommandFactory.register(ActionUnselectFilters, { dashboard: 0 }, false);

ActionUnselectFilters.prototype.step_1 = function () {

	State.filterList.clear();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.returnProcess = this;
	process.execute();
};

ActionUnselectFilters.prototype.step_2 = function () {
	var process = new ProcessRefreshTaxonomies();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
//Action unselect categories
//----------------------------------------------------------------------
function ActionUnselectCategories() {
	this.base = Action;
	this.base(2);
};

ActionUnselectCategories.prototype = new Action;
ActionUnselectCategories.constructor = ActionUnselectCategories;
CommandFactory.register(ActionUnselectCategories, { dashboard: 0 }, false);

ActionUnselectCategories.prototype.step_1 = function () {

	State.compareList.clear();
	this.updateColors();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.returnProcess = this;
	process.execute();
};

ActionUnselectCategories.prototype.step_2 = function () {
	var process = new ProcessRefreshTaxonomies();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action set color
//----------------------------------------------------------------------
function ActionSetColor() {
	this.base = Action;
	this.base(1);
};

ActionSetColor.prototype = new Action;
ActionSetColor.constructor = ActionSetColor;
CommandFactory.register(ActionSetColor, { dashboard: 0, context: 1, id: 2, color: 3 }, false);

ActionSetColor.prototype.step_1 = function () {
	View.setColor(this.context, this.id, this.color);

	this.updateColors();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action set measure unit mode
//----------------------------------------------------------------------
function ActionSetMeasureUnitMode() {
	this.base = Action;
	this.base(1);
};

ActionSetMeasureUnitMode.prototype = new Action;
ActionSetMeasureUnitMode.constructor = ActionSetMeasureUnitMode;
CommandFactory.register(ActionSetMeasureUnitMode, { dashboard: 0, measureUnit: 1, mode: 2 }, false);

ActionSetMeasureUnitMode.prototype.step_1 = function () {
	var range = State.rangeList.get(this.measureUnit);

	if (range == null)
		State.rangeList.add(this.measureUnit, this.mode, null, null);
	else
		range.mode = this.mode;

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action set measure unit range
//----------------------------------------------------------------------
function ActionSetMeasureUnitRange() {
	this.base = Action;
	this.base(1);
};

ActionSetMeasureUnitRange.prototype = new Action;
ActionSetMeasureUnitRange.constructor = ActionSetMeasureUnitRange;
CommandFactory.register(ActionSetMeasureUnitRange, { dashboard: 0, measureUnit: 1, minValue: 2, maxValue: 3 }, false);

ActionSetMeasureUnitRange.prototype.step_1 = function () {
	var range = State.rangeList.get(this.measureUnit);

	if (range == null)
		State.rangeList.add(this.measureUnit, this.mode, this.minValue, this.maxValue);
	else {
		range.min = this.minValue;
		range.max = this.maxValue;
	}

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action print dashboard
//----------------------------------------------------------------------
function ActionPrintDashboard() {
	this.base = Action;
	this.base(2);
};

ActionPrintDashboard.prototype = new Action;
ActionPrintDashboard.constructor = ActionPrintDashboard;
CommandFactory.register(ActionPrintDashboard, { dashboard: 0, view: 1, chartType: 2, indicators: 3, compare: 4, filters: 5, ranges: 6, from: 7, to: 8, scale: 9, colors: 10 }, false);

ActionPrintDashboard.prototype.step_1 = function () {

	if (this.chartType != "" && this.chartType != "null") State.chartType = this.chartType;
	if (this.indicators != "" && this.indicators != "null") State.indicatorList.fromJson(jQuery.parseJSON(this.indicators));
	if (this.compare != "" && this.compare != "null") State.compareList.fromJson(jQuery.parseJSON(this.compare));
	if (this.filters != "" && this.filters != "null") State.filterList.fromJson(jQuery.parseJSON(this.filters));
	if (this.from != "" && this.from != "null") State.timeLapse.from = this.from;
	if (this.to != "" && this.to != "null") State.timeLapse.to = this.to;
	if (this.scale != "" && this.scale != "null") State.timeLapse.scale = this.scale;
	if (this.ranges != "" && this.ranges != "null") State.rangeList.fromJson(jQuery.parseJSON(this.ranges));

	if (this.colors != "" && this.colors != "null") {
		View.setColors(jQuery.parseJSON(this.colors));
		this.updateColors();
	}

	var action = new ActionShowDashboard();
	action.dashboard = this.dashboard;
	action.view = this.view;
	action.mode = State.Mode.PRINT;
	action.chartType = this.chartType;
	action.scale = this.scale;
	action.returnProcess = this;
	action.execute();
};

ActionPrintDashboard.prototype.step_2 = function () {

	if (this.chartType == ViewChart.TABLE_CHART) {
		this.terminate();
		return;
	}

	var view = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var process = new ProcessRenderChart();
	process.dashboardId = this.dashboard;
	process.target = view.target;
	process.chartType = ViewChart.TABLE_CHART;
	process.layer = view.getChartLayer(ViewChart.TABLE_CHART);
	process.execute();

	this.terminate();
};

//----------------------------------------------------------------------
// Action show indicator
//----------------------------------------------------------------------
function ActionShowIndicator() {
  this.base = Action;
  this.base(3);
};

ActionShowIndicator.prototype = new Action;
ActionShowIndicator.constructor = ActionShowIndicator;
CommandFactory.register(ActionShowIndicator, { dashboard: 0 }, false);

ActionShowIndicator.prototype.step_1 = function() {
  CommandListener.throwCommand("showdashboard(" + this.dashboard + ")");
};

CommandListener = new Object;
CommandListener.onUnload = null;

CommandListener.start = function(Dispatcher) {
  this.Dispatcher = Dispatcher;
  $(document.body).bind("keyup", CommandListener.atCommandKey);
  $(window).bind("beforeunload", CommandListener.atBeforeUnload);
  $(window).bind("unload", CommandListener.atUnload);
};

CommandListener.stop = function() {
  this.Dispatcher = null;
};

CommandListener.capture = function(DOMElement){
  if (!this.Dispatcher) return;

  var jElement = $(DOMElement);
  if (! jElement) return;

  if (jElement.hasClass("command")) jElement.bind('click', CommandListener.atCommandClick.bind(CommandListener, DOMElement));

  jCommandList = jElement.find(".command");
  jCommandList.each(function(index, DOMCommand) { 
    $(DOMCommand).click(CommandListener.atCommandClick.bind(CommandListener, DOMCommand));
  });
};

CommandListener.throwCommand = function(command) {
  this.Dispatcher.execute(command, null);
};

CommandListener.dispatchCommand = function(command) {
  this.Dispatcher.dispatch(command, null);
};

CommandListener.getCommand = function(command) {
  var Expression = new RegExp("\\([^\\)]*\\)");
  var resultArray = Expression.exec(command);
  
  if (resultArray != null && resultArray.length > 0)
    command = command.replace(resultArray[0], "#command#");

  command = command.substr(command.lastIndexOf("/")+1);
  if (resultArray != null && resultArray.length > 0) command = command.replace("#command#", resultArray[0]);
  
  return command;
};

CommandListener.atCommandKeyUp = function(event){
  if (event.keyCode != 13) return false;

  var jForm = $(event.target);
  if (!jForm) return false;

  jForm = jForm.parents("form:first").eq(0);
  if (!jForm) return false;

  DOMForm = jForm.get(0);

  var command = DOMForm.action;
  if (command != null) this.Dispatcher.dispatch(command, DOMForm);
  if (event) Event.stop(event);

  return false;
};

CommandListener.atBeforeUnload = function(event){
  if (CommandListener.onUnload) {
    var result = CommandListener.onUnload();
    if ((result != "") && (result != null)) {
      if (typeof event == 'undefined') {
        event = window.event;
      }
      if (event) {
        event.returnValue = result;
      }
      return result;
    }
  }
};

CommandListener.atUnload = function(event){
  if (CommandListener.onUnload)
    CommandListener.onUnload();
};

CommandListener.atCommandClick = function(DOMItem, event){
  var command = null;
  var jItem = $(DOMItem);
  
  if (DOMItem.href) command = DOMItem.href;
  else if (jItem.hasClass("button")) command = DOMItem.name;
  else command = DOMItem.value;

  command = CommandListener.getCommand(command);

  if (command != null) this.Dispatcher.dispatch(command, DOMItem);
  if (event) event.stopPropagation();
  
  return false;
};

function Stub(mode) {
  this.mode = mode;
};

Stub.prototype.showConnectionFailure = function() {
  Desktop.reportProgress(Lang.ConnectionFailure, true);
};

Stub.prototype.hideConnectionFailure = function() {
  Desktop.hideReports();
};

SERVER_ERROR_PREFIX = "err_";
SERVER_SESSION_EXPIRES = "err_session_expires";
SERVER_USER_NOT_LOGGED = "err_user_not_logged";
SERVER_BUSINESSUNIT_STOPPED = "err_businessunit_stopped";

function StubAjax(Mode, idMessageLayer) {
  this.base = Stub;
  this.base(Mode, idMessageLayer);
  this.title = parent.document.title;
};

StubAjax.prototype = new Stub;

StubAjax.prototype.isSessionExpired = function(message) {
  var initialChars;
    
  initialChars = message.substr(0,SERVER_USER_NOT_LOGGED.length);
  if (initialChars.toLowerCase() == SERVER_USER_NOT_LOGGED) return true;

  initialChars = message.substr(0,SERVER_SESSION_EXPIRES.length);
  if (initialChars.toLowerCase() == SERVER_SESSION_EXPIRES) return true;

  return false;
};

StubAjax.prototype.isBusinessUnitStopped = function(message) {
  var initialChars;
    
  initialChars = message.substr(0,SERVER_BUSINESSUNIT_STOPPED.length);
  if (initialChars.toLowerCase() == SERVER_BUSINESSUNIT_STOPPED) return true;

  return false;
};

StubAjax.prototype.existServerError = function(message){
  var initialChars = message.substr(0,SERVER_ERROR_PREFIX.length);
  return (initialChars.toLowerCase() == SERVER_ERROR_PREFIX);
};

StubAjax.prototype.getErrorMessage = function(message){
  var pos = message.indexOf(":");
  return message.substring(pos+1);
};

StubAjax.prototype.getTimeZone = function() {
  var dateVar = new Date();
  return dateVar.getTimezoneOffset()/60 * (-1);
};

StubAjax.prototype.request = function(action, operation, parameters, checkConnection) {
  //Desktop.showLoading();
  
  if (checkConnection == null) checkConnection = true;
  
  $.ajax({
    url: Context.Config.Url + writeServerRequest(this.mode, "op=" + operation + "&sender=ajax&timezone=" + this.getTimeZone() + ((parameters != null)?serializeParameters(parameters):"")),
    complete: StubAjax.prototype.atRequestResponse.bind(this, action, operation, parameters, checkConnection)
  });
};

StubAjax.prototype.atRequestResponse = function(action, operation, parameters, checkConnection, response, state) {
  if (state == "success") {
  
    if ((checkConnection) && (response.responseText == "")) {
      this.ping(action, operation, parameters);
      return;
    }

    action.data = readServerResponse(this.mode, response.responseText);
    if ((this.isSessionExpired(action.data)) || (this.isBusinessUnitStopped(action.data))) {
      if (this.isSessionExpired(action.data)) alert(Lang.SessionExpired);
      if (this.isBusinessUnitStopped(action.data)) alert(Lang.BusinessUnitStopped);
      window.location = window.location + (location.indexOf("?")!=-1?"&":"?") + "m=" + Math.random();
      parent.document.title = this.title;
      return;
    }
    //Desktop.hideLoading();
    try {
      if (action.onFailure && this.existServerError(response.responseText)) action.onFailure(this.getErrorMessage(response.responseText));
      else if (action.onSuccess) action.onSuccess();
    }
    catch(e) { 
      alert(e.message); 
    }
  }
  else {
    //Desktop.hideLoading();
    RequestException(operation, response);
    Desktop.hideReports();
    Desktop.hideProgress();
  }
};

StubAjax.prototype.ping = function(action, operation, parameters) {
  this.hideConnectionFailure();
  $.ajax({
    url: Context.Config.Url + writeServerRequest(this.mode, "op=ping&sender=ajax&timezone=" + this.getTimeZone()),
    complete: StubAjax.prototype.atPingResponse.bind(this, action, operation, parameters)
  }, this);
};

StubAjax.prototype.atPingResponse = function(action, operation, parameters, response, state) {
  if (state == "success") {
    if (response.responseText == "") {
      this.showConnectionFailure();
      window.setTimeout(StubAjax.prototype.ping.bind(this, action, operation, parameters), 5000);
    }
    else {
      this.hideConnectionFailure();
      this.request(action, operation, parameters, false);
    }
  }
  else {
    this.showConnectionFailure();
    window.setTimeout(StubAjax.prototype.ping.bind(this, action, operation, parameters), 5000);
  }
};

StubAjax.prototype.zoombieRequest = function(operation, parameters) {
  //Desktop.showLoading();
  $.ajax({
    url: Context.Config.Url + writeServerRequest(this.mode, "op=" + operation + "&sender=ajax&timezone=" + this.getTimeZone() + ((parameters != null)?serializeParameters(parameters):"")),
    complete: StubAjax.prototype.atZoombieRequestResponse.bind(this)
  });
};

StubAjax.prototype.atZoombieRequestResponse = function(response, state) {
  if (state == "success") {
    var serverResponse = readServerResponse(this.mode, response.responseText);
    if ((this.isSessionExpired(serverResponse)) || (this.isBusinessUnitStopped(serverResponse))) {
      if (this.isSessionExpired(serverResponse)) alert(Lang.SessionExpired);
      if (this.isBusinessUnitStopped(serverResponse)) alert(Lang.BusinessUnitStopped);
      window.location = window.location + (location.indexOf("?")!=-1?"&":"?") + "m=" + Math.random();
      return;
    }
    //Desktop.hideLoading();
  }
  //else Desktop.hideLoading();
};



var Base64 = {

	// private property
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

	// public method for encoding
	encode : function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;

		input = Base64._utf8_encode(input);

		while (i < input.length) {

			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);

			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;

			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}

			output = output +
			this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
			this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

		}

		return output;
	},

	// public method for decoding
	decode : function (input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;

		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

		while (i < input.length) {

			enc1 = this._keyStr.indexOf(input.charAt(i++));
			enc2 = this._keyStr.indexOf(input.charAt(i++));
			enc3 = this._keyStr.indexOf(input.charAt(i++));
			enc4 = this._keyStr.indexOf(input.charAt(i++));

			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;

			output = output + String.fromCharCode(chr1);

			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}

		}

		output = Base64._utf8_decode(output);

		return output;

	},

	// private method for UTF-8 encoding
	_utf8_encode : function (string) {
    if (string == null) return "";

		string = string.replace(/\r\n/g,"\n");
		var utftext = "";

		for (var n = 0; n < string.length; n++) {

			var c = string.charCodeAt(n);

			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}

		}

		return utftext;
	},

	// private method for UTF-8 decoding
	_utf8_decode : function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;

		while ( i < utftext.length ) {

			c = utftext.charCodeAt(i);

			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}

		}

		return string;
	}

};

Kernel = new Object;

Kernel.init = function() {
  Kernel.mode = (Context.Config.EncriptData == "true")?true:false;
  Kernel.stub = new StubAjax(Kernel.mode);
};

Kernel.loadAccount = function(action) {
  Kernel.stub.request(action, "loadaccount", { language: Context.Config.Language });
};

Kernel.saveAccount = function(Action, account) {
  var data = Serializer.serializeAccount(account);
  Kernel.stub.request(Action, "saveaccount", {data: escape(utf8Encode(data))});
};

Kernel.logout = function(Action, instanceId) {
  Kernel.stub.request(Action, "logout", {i: instanceId});
};

Kernel.loadDashboardList = function(action) {
  Kernel.stub.request(action, "loaddashboardlist", { language: Context.Config.Language });
};

Kernel.loadDashboard = function(action, id, indicatorList, reportId) {
  var parameters = new Object();
  parameters.id = id;
  parameters.language = Context.Config.Language;
  parameters.report = reportId;
  
  if (indicatorList != null) 
    parameters.indicators = escape(utf8Encode(indicatorList.toJson()));
  
  Kernel.stub.request(action, "loaddashboard", parameters);
};

Kernel.getLoadCategoriesUrl = function(dashboardName, taxonomyId, parentCategoryId) {

  var parentParemeter = "";
  if (parentCategoryId != null)
    parentParemeter = "&category=" + escape(utf8Encode(parentCategoryId));

  var parameters = "op=loadcategories&id=" + taxonomyId + "&dashboard=" + dashboardName + parentParemeter + "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.loadChartApi = function(action, type) {
  Kernel.stub.request(action, "loadchartapi", { type : type, language : Context.Config.Language });
};

Kernel.getChartUrl = function(dashboard, type, indicatorList, compareList, filterList, rangeList, from, to, scale) {
  var parameters = "op=loadchart&dashboard=" + dashboard + "&type=" + type;
  parameters += "&indicators=" + escape(utf8Encode(indicatorList.toJson()));
  parameters += "&compare=" + escape(utf8Encode(compareList.toJson()));
  parameters += "&filters=" + escape(utf8Encode(filterList.toJson()));
  parameters += "&ranges=" + escape(utf8Encode(rangeList.toJson()));
  parameters += "&from=" + Date.timeToUTC(from);
  parameters += "&to=" + Date.timeToUTC(to);
  parameters += "&scale=" + scale;
  parameters += "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.getPrintDashboardUrl = function(dashboard, type, indicatorList, compareList, filterList, rangeList, from, to, scale, colors) {
  var parameters = "op=printdashboard&id=" + dashboard + "&type=" + type;
  parameters += "&indicators=" + escape(utf8Encode(indicatorList.toJson()));
  parameters += "&compare=" + escape(utf8Encode(compareList.toJson()));
  parameters += "&filters=" + escape(utf8Encode(filterList.toJson()));
  parameters += "&ranges=" + escape(utf8Encode(rangeList.toJson()));
  parameters += "&from=" + Date.timeToUTC(from);
  parameters += "&to=" + Date.timeToUTC(to);
  parameters += "&scale=" + scale;
  parameters += "&colors=" + escape(utf8Encode($.toJSON(colors)));
  parameters += "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.getDownloadDashboardUrl = function(dashboard, format, indicatorList, compareList, filterList, rangeList, from, to, scale) {
  var parameters = "op=downloaddashboard&id=" + dashboard + "&format=" + format;
  parameters += "&indicators=" + escape(utf8Encode(indicatorList.toJson()));
  parameters += "&compare=" + escape(utf8Encode(compareList.toJson()));
  parameters += "&filters=" + escape(utf8Encode(filterList.toJson()));
  parameters += "&ranges=" + escape(utf8Encode(rangeList.toJson()));
  parameters += "&from=" + Date.timeToUTC(from);
  parameters += "&to=" + Date.timeToUTC(to);
  parameters += "&scale=" + scale;
  parameters += "&language=" + Context.Config.Language;
  return Context.Config.Url + writeServerRequest(Kernel.mode, parameters);
};

Kernel.registerException = function(exception) {
  alert(exception.message);
};

Desktop = new Object;
Desktop.views = new Array();
Desktop.jCurrentWestView = null;

Desktop.MAIN_LOADING = "#mainLoading";
Desktop.LOADING_BOX = "#loadingBox";
Desktop.VIEWS_CONTAINER = "#viewsContainer";
Desktop.WEST_LAYER = "#westlayer";

Desktop.init = function () {
	var html = AppTemplate.desktop;
	html = translate(html, Lang.Desktop);

	document.body.innerHTML = html;

	var jMainLoading = $(Desktop.MAIN_LOADING);
	jMainLoading.get(0).innerHTML = Lang.Loading;
	jMainLoading.show();

	$(window).resize(Desktop.resize.bind(this));
};

Desktop.showLoading = function (message) {
	if (message == null) message = Lang.Loading;
	$(Desktop.LOADING_BOX).html(message).show();
};

Desktop.hideLoading = function () {
	$(Desktop.LOADING_BOX).hide();
};

Desktop.showMessageBox = function (title, summary, className, miliseconds) {
	alert(title + " - " + summary);
};

Desktop.hideMessageBox = function () {
};

Desktop.showMask = function () {
};

Desktop.hideMask = function () {
};

Desktop.reportProgress = function (message, modal) {
	this.showMask();
};

Desktop.reportInfo = function (message) {
	Desktop.showMessageBox(Lang.Information, message, 'info', 5000);
};

Desktop.reportError = function (message) {
	Desktop.showMessageBox(Lang.Error, message, 'error', 5000);
};

Desktop.reportWarning = function (message) {
	Desktop.showMessageBox(Lang.Warning, message, 'warning', 5000);
};

Desktop.reportSuccess = function (message) {
	Desktop.showMessageBox(Lang.Information, message, 'success', 5000);
};

Desktop.hideReports = function () {
	this.hideMask();
};

Desktop.hideProgress = function () {
	this.hideMask();
};

Desktop.cleanViewsContainer = function () {
	$(Desktop.VIEWS_CONTAINER).html("");
};

Desktop.createView = function () {
	var idView = generateId();
	$(Desktop.VIEWS_CONTAINER).append("<div id='" + idView + "' class='view'></div>");
	return $("#" + idView).get(0);
};

Desktop.refresh = function () {
};

Desktop.registerView = function (id, view) {
	if (this.views.length == 0) $(Desktop.MAIN_LOADING).hide();
	this.views[id] = view;
};

Desktop.getView = function (id) {
	return this.views[id];
};

Desktop.getTabTitle = function (dimension, indicator) {
	if (indicator != null) return indicator.label;
	return dimension.label;
};

Desktop.getTabSubTitle = function (period, dimension, level) {
	return (level != null) ? level.label : dimension.label;
};

Desktop.getWestViews = function () {
	return $(Desktop.WEST_LAYER).find("._expansible");
};

Desktop.hideWestLayer = function () {
	$(Desktop.WEST_LAYER).hide();
};

Desktop.expandWestLayer = function (DOMView) {
	if (DOMView == null) return;

	var jWest = $(Desktop.WEST_LAYER);
	var jViews = jWest.find("._expansible");
	var jView = DOMView != null ? $(DOMView) : Desktop.jCurrentWestView;
	var height = jWest.height();
	var numViews = jViews.length;
	var reservedPercentage = 0.20 * (numViews - 1);
	var activeViewHeight = Math.round(height * (1 - reservedPercentage - 0.1));
	var inactiveViewHeight = Math.round(height * 0.20);

	jViews.removeClass("expanded");

	if (jView.height() == activeViewHeight)
		return;

	jViews.height(inactiveViewHeight);
	jView.addClass("expanded");
	jView.animate({ height: activeViewHeight });

	Desktop.jCurrentWestView = jView;
};

Desktop.addState = function (object) {
	object.state = new Object();
	object.state.chartType = State.chartType;
	object.state.chartLayer = State.chartLayer;
	object.state.mode = State.mode;
	object.state.indicatorList = State.indicatorList;
	object.state.compareList = State.compareList;
	object.state.filterList = State.filterList;
	object.state.timeLapse = State.timeLapse;
	object.state.rangeList = State.rangeList;
};

Desktop.doResize = function () {
	for (var viewId in this.views) {
		if (isFunction(this.views[viewId])) continue;
		Desktop.addState(this.views[viewId]);
		this.views[viewId].onResize();
	}
};

Desktop.resize = function () {
	if (Desktop.resizeTimeout != null)
		window.clearTimeout(Desktop.resizeTimeout);

	Desktop.resizeTimeout = window.setTimeout(Desktop.doResize.bind(this), 500);
};

var Application = new Object;
var Context = new Object;
Context.Config = new Object;
Context.Operation = new Object();
Context.Debugging = true;

Application.init = function() {
  readData(Context, $("#dataInit"));

  Kernel.init();
  CommandListener.start(CommandDispatcher);
  $(document).click(Application.atMouseClick);
  
  Desktop.init();
  Desktop.refresh();
  
  var action = new ActionInit();
  action.execute();

  this.isRunning = true;
};

Application.atMouseClick = function(event) {
  Application.mousePosition = { left: event.pageX, top: event.pageY };
};

Application.onLinkClick = function(link) {
  alert(link);
};

Application.onOpenIndicator = function(view, code, period) {
  var process = new ProcessOpenIndicator();
  process.view = view;
  process.code = code;
  process.period = period;
  process.execute();
};

$(document).ready(
  function(){
    Application.init();
  }
);

