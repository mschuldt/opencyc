function resize() {
  var w_newWidth, w_newHeight;
  var w_maxWidth = 1600, w_maxHeight = 1200;
  if (navigator.appName.indexOf("Microsoft") != -1) {
          w_newWidth = document.body.clientWidth;
          w_newHeight = document.body.clientHeight;
          document.body.scroll = "no"
  } else {
          var netscapeScrollWidth = 15;
          w_newWidth = window.innerWidth - netscapeScrollWidth;
          w_newHeight = window.innerHeight - netscapeScrollWidth;
  }
  if (w_newWidth > w_maxWidth)
    w_newWidth = w_maxWidth;
  if (w_newHeight > w_maxHeight)
    w_newHeight = w_maxHeight;
  document.applets[0].setSize(w_newWidth, w_newHeight);
  window.scroll(0,0);
}
window.onResize = resize;
window.onLoad = resize;
