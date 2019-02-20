var signer = null;
var certificate = null;

function addMasterFromSignature() {
  var signer = document.querySelector("cotton-signatory-signer");
  var message = "msg_" + Math.random();

  signer.init();
  signer.signText(message, function(signature, certificate) {
    var form = document.querySelector("#addmasterfromsignature");
    form.signature.value = signature;
    form.submit();
  }, function(event) {
    var response = event.details.response;
    document.querySelector("#addmasterfromsignatureresponse").innerHTML = response.message;
  });
}

function uploadDistribution(defaultMessage) {
  $("#uploaddistributionresponse").html(defaultMessage);
  $("#uploaddistributionresponse").get(0).style.display = "block";
  $("#uploaddistribution").ajaxSubmit({
    success: function (responseText, statusText, xhr, jForm) {
      $("#uploaddistributionresponse").html(responseText);
    }
  });
};

function toggleVisibility(id) {
  var jElement = $("#" + id);

  if (jElement.is(":visible"))
    jElement.hide("blind", { direction: "vertical" }, 300);
  else
    jElement.show("blind", { direction: "vertical" }, 300);
};