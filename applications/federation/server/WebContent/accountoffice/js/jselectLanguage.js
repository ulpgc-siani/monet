$(document).ready(function (){
		 $("#languageOps").change(function () {
			var val = $("#languageOps option:selected").attr("value");
			var actionName = "changelanguage";
			var token = $("#tokenReq").attr();
			if(token == null) token = "null";
			window.location = "?action=" + actionName + "&oauth_token=" + token + "&language=" + val;
		 });
});