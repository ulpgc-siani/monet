function Load() {
  oInput = window.document.getElementById('name');
  if (oInput) oInput.focus();
  oFrameSet = window.parent.document.getElementById("app_frameset");
  if (oFrameSet) oFrameSet.rows = "0%,100%";
}

function ShowAni() {
  oFrameSet = window.parent.document.getElementById("app_frameset");
  if (oFrameSet) oFrameSet.rows = "100%,0%";
}