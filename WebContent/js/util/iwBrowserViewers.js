/** Provenance Viewer From IWBrowser (http://browser.inference-web.org/iwbrowser/) */
//var iwbrowserProvViewer = "http://browser.inference-web.org/iwbrowser/ViewProvenance?uri=";
var detailpage = null;
function openURL(v) {
	detailpage = window
	.open(
			'/iwbrowser/Lookup?uri='
			+ v.replace(/#/g, "%23").replace(/\+/g, "%2B")
			.replace(/\?/g, "%3F"),
			"ProofDetail",
	"toolbar=0,location=0,status=0,menubar=0,resizable=yes,scrollbars=yes,top=500,left=100,width=600,height=350");
	detailpage.focus();
	return;
}
function openProvenance(v) {
	provenancedetailpage = window
	.open(
			'http://browser.inference-web.org/iwbrowser/ViewProvenance?uri='
			+ v.replace(/#/g, "%23").replace(/\+/g, "%2B")
			.replace(/&/g, "%26").replace(/\?/g,
			"%3F"),
			"ProvenanceDetail",
	"toolbar=0,location=0,status=0,menubar=0,resizable=yes,scrollbars=yes,top=500,left=100,width=600,height=350");
	provenancedetailpage.focus();
	return;
}
function openJustification(v) {
	justificationdetailpage = window
	.open(
			'ViewJustification?uri='
			+ v.replace(/#/g, "%23").replace(/\+/g, "%2B")
			.replace(/&/g, "%26").replace(/\?/g,
			"%3F"),
			"JustificationDetail",
	"toolbar=0,location=0,status=0,menubar=0,resizable=yes,scrollbars=yes,top=430,left=130,width=600,height=350");
	justificationdetailpage.focus();
	return;
}
function openPMLCode(v) {
	pmlcodepage = window
	.open(
			'ViewPMLObjectCode?uri='
			+ v.replace(/#/g, "%23").replace(/\+/g, "%2B")
			.replace(/&/g, "%26").replace(/\?/g,
			"%3F"),
			"PMLCode",
	"toolbar=0,location=0,status=0,menubar=0,resizable=yes,scrollbars=yes,top=450,left=120,width=600,height=350");
	pmlcodepage.focus();
	return;
}
function openContext(v) {
	nscontextpage = window
	.open(
			'ViewContext?uri='
			+ v.replace(/#/g, "%23").replace(/\+/g, "%2B")
			.replace(/&/g, "%26").replace(/\?/g,
			"%3F"),
			"NodeSetContext",
	"toolbar=0,location=0,status=0,menubar=0,resizable=yes,scrollbars=yes,top=480,left=110,width=600,height=350");
	nscontextpage.focus();
	return;
}
function closeDetailPage() {
	detailpage.close();
	detailpage = null;
	return;
}
function openURLSource(url) {
	sourcepage = window
	.open(
			url.replace(/#/g, "%23").replace(/\+/g, "%2B"),
			"SourceCode",
	"location=0,status=0,resizable=yes,scrollbars=yes,top=250,left=100,width=800,height=500");
	sourcepage.focus();
	return;
}
function openURLSourceUsage(url) {
	sourceUsgWin = window
	.open(url.replace(/#/g, "%23"), "SourceDocWin",
	"location=0,status=0,resizable=yes,scrollbars=yes,top=220,left=150");
	sourceUsgWin.focus();
	return;
}
function GotoSelected(menu) {
	var newurl = menu.options[menu.selectedIndex].value;
	var newurl2 = newurl.replace(/#/g, "%23");
	parent.location.href = newurl2;
}
function toggle(showHideDiv, switchTextDiv) {
	var element = document.getElementById(showHideDiv);
	var text = document.getElementById(switchTextDiv);
	if (element.style.display == "block") {
		element.style.display = "none";
		text.innerHTML = "show";
	} else {
		element.style.display = "block";
		text.innerHTML = "hide";
	}
}