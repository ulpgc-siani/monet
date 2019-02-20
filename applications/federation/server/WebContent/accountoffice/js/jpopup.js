function federationPlugin(div,url){
	
	var divForRender = document.getElementById(div);

	//var bar = "<div id='bar'></div>";
	//var pop = "<div id='pop'></div>";
	
	var barDiv = document.createElement("div");
	barDiv.id="bar";
	
	var popDiv = document.createElement("div");
	popDiv.id="pop";
	
	var popup_w = 150;
	var popup_h = 0;
	popDiv.style.width = popup_w + 'px';
	popDiv.style.height = popup_h + 'px';
	$("#pop").hide();
	
	//var w = $(this).width(); 
    //var h = $(this).height(); 
    
	w = w - 10 - popup_w;
	h = -20; 
	popDiv.style.left = w + 'px';
	
	barDiv.appendChild(popDiv);
	divForRender.appendChild(barDiv);
	
	Ext.Ajax.method = "GET";
	
	Ext.Ajax.url = url;
	  Ext.Ajax.request({
	    params: writeServerRequest(this.Mode, "op=" + sOperation + "&sender=ajax" + ((aParameters != null)?serializeParameters(aParameters):"")),
	    callback: CGStubAjax.prototype.atRequestResponse.bind(this, Action, sOperation, aParameters, bCheckConnection)
	  }, this);
}




function hidePop() { 
   $("#pop").hide('slow'); 
}

function showPop() { 
   $("#pop").show('slow'); 
}

function openApp(appURL) { 
   window.open(appURL);
}

function closeSession(appURL) { 
   window.open(appURL);
}


$(document).ready(function (){
	var urlFederation = $("#urlFdr").text();
	$("<div id='bar'></div>").prependTo("body");
	$("<div id='pop'></div>").appendTo("#bar");
	
	
	$('#images').focus(function() {
		alert('Handler for .focus() called.');
	});

	var popup_w = 150;
	var popup_h = 0;
	$("#pop").css('width', popup_w + 'px'); 
	$("#pop").css('height',popup_h + 'px'); 
	$("#pop").hide();
	
	var w = $(this).width(); 
//    var h = $(this).height(); 
    
	w = w - 10 - popup_w;
	h = -20; 
	$("#pop").css("left",w + "px"); 
	//$("#pop").css("top",h + "px");
	
	var idUnit = $("#idUnit").text();
	var reqDataUrl = urlFederation+"applications/api/jsonservice?idUnit="+ idUnit+"&jsoncallback=?" ;
	$.getJSON(reqDataUrl,
		function(data){
			$("<p onclick='showPop()'>" + data.user + "</p>").appendTo("#bar");
		    $.each(data.feders, function(key, val) {
				$("<p onClick=openApp(\""+ val.url + "\")>" + val.label + "</p>").appendTo("#pop");
				popup_h += 45;
		    });
		    
		    $("<hr class='separatorLine' />").appendTo("#pop");
		    $.each(data.roles, function(key, val) {
				$("<p"  + val.role + "</p>").appendTo("#pop");
				popup_h += 45;
		    });
			
			$("<hr class='separatorLine' />").appendTo("#pop");
			$("<p  onClick=closeSession(\""+data.closeUrl+"\")>" +data.close + "</p>").appendTo("#pop");
			popup_h += 45;
			 
			$("#pop").css('height',popup_h + 'px'); 
         });
});