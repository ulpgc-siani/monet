function RequestException (conn,response,options){
  alert(response.responseText);
};

function InternalException (e,dOp,dState){
  alert(e.message);
};