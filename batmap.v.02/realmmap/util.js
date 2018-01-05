//
// Positioning utility functions in JavaScript for BatMUD maps
// Programmed by Matti 'ccr' Hamalainen <ccr@tnsp.org>
// (C) Copyright 2007-2017 Tecnic Software productions (TNSP)
//

var currLoc = "";
var scrollTimerID = -1;
var scrollPos0, scrollPos1;
var scrollIndex = 0.0;


function jsFindElemCoords(elem)
{
  var xc = yc = 0;
  if (elem.offsetParent)
  {
    xc = elem.offsetLeft;
    yc = elem.offsetTop;
    while (elem = elem.offsetParent)
    {
      xc += elem.offsetLeft;
      yc += elem.offsetTop;
    }
  }

  return [xc, yc];
}


function jsGetWindowSize()
{
  var winW = 0, winH = 0;
  if (typeof(window.innerWidth) == 'number')
  {
    // Non-MSIE
    winW = window.innerWidth;
    winH = window.innerHeight;
  }
  else
  if (document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight))
  {
    // MSIE 6+ in 'standards compliant mode'
    winW = document.documentElement.clientWidth;
    winH = document.documentElement.clientHeight;
  }
  else
  if (document.body && (document.body.clientWidth || document.body.clientHeight))
  {
    // MSIE 4 compatible
    winW = document.body.clientWidth;
    winH = document.body.clientHeight;
  }

  return [winW, winH];
}


function jsClamp10(a)
{
  return (a < 0.0 ? 0.0 : (a > 1.0 ? 1.0 : a));
}


function jsSCurve(t)
{
  return (t * t * (3.0 - 2.0 * t));
}


function jsLerp(t, a, b)
{
  return (a + t * (b - a));
}


function jsScrollToPos()
{
  if (scrollIndex <= 1.0)
  {
    var dim = jsGetWindowSize();
    var n = jsSCurve(jsClamp10(scrollIndex));
    var px = jsLerp(n, scrollPos0[0], scrollPos1[0]);
    var py = jsLerp(n, scrollPos0[1], scrollPos1[1]);

    window.scrollTo(px - (dim[0] / 2), py - (dim[1] / 2));
    scrollIndex += 0.01;
  }
  else
    clearInterval(scrollTimerID);
}


function jsSetActiveLocation(newLoc)
{
  // Change CSS class state for prev/curr/new selected element
  if (currLoc)
  {
    var celem = document.getElementById(currLoc);
    var mcelem = document.getElementById("m"+ currLoc);
    celem.classList = celem.classList.remove("nactive");
    mcelem.classList = mcelem.classList.remove("nactive");
  }

  var nelem = document.getElementById(newLoc);
  var nmelem = document.getElementById("m"+ newLoc);
  nelem.classList.add("nactive");
  nmelem.classList.add("nactive");

  // Set the active item in the location dropdown
  var ssel = document.getElementById("slocation");
  if (ssel)
  {
    var found = false;
    for (var opt, i = 0; opt = ssel.options[i]; i++)
    {
      if (opt.value == newLoc)
      {
        ssel.selectedIndex = i;
        found = true;
        break;
      }
    }

    if (!found)
      ssel.selectedIndex = 0;
  }
}


function jsSetPosToElem(nelem)
{
  var pos = jsFindElemCoords(nelem);
  var dim = jsGetWindowSize();
  window.scrollTo(pos[0] - (dim[0] / 2), pos[1] - (dim[1] / 2));
}


function jsGotoPos()
{
  var newLoc = document.getElementById("slocation").value;
  if (currLoc != newLoc)
  {
    var nelem = document.getElementById(newLoc);
    if (nelem)
    {
      var sscroll = document.getElementById("sscroll").checked;
      var celem = currLoc != "" ? document.getElementById(currLoc) : 0;
      if (sscroll && celem)
      {
        if (scrollTimerID != -1)
          clearInterval(scrollTimerID);

        scrollPos0 = jsFindElemCoords(celem);
        scrollPos1 = jsFindElemCoords(nelem);
        scrollIndex = 0.0;

        scrollTimerID = setInterval(jsScrollToPos, 10);
      }
      else
        jsSetPosToElem(nelem);

      jsSetActiveLocation(newLoc);
      currLoc = newLoc;
    }
  }
}


function jsGotoOnLoadPos()
{
  var s = window.location.href;
  var sp;
  if ((sp = s.indexOf("#")) >= 0)
  {
    var eid = unescape(s.substr(sp + 1).toLowerCase());
    var nelem = document.getElementById(eid);
    if (nelem)
    {
      jsSetPosToElem(nelem);
      jsSetActiveLocation(eid);
      currLoc = eid;
    }
  }
}


function jsToggleLabels()
{
  var vcl = document.all ? document.styleSheets[0].rules[0] : document.styleSheets[0].cssRules[0];
  vcl.style.visibility = document.getElementById("shide").checked ? "visible" : "hidden";
}
