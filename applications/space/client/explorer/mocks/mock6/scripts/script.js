var INSIDE_COMPONENT = false;

var ESC_KEY = 27;

var LEFT_ARROW_KEY = 37;
var UP_ARROW_KEY = 38;
var RIGHT_ARROW_KEY = 39;
var DOWN_ARROW_KEY = 40;

var changeFocus = new Object;

changeFocus[LEFT_ARROW_KEY] = function() {
	if ($(".focusable.right:focus").size() === 0) return;
	$(".focusable:focus").parentsUntil(".panel.horizontal").find(".focusable.left").last().focus();
};

changeFocus[UP_ARROW_KEY] = function() {
	if ($(".focusable.right:focus").size() !== 0)
		moveToPrev(".right", ".left");
	else if ($(".focusable.left:focus").size() !== 0)
		moveToPrev(".left", ".right");
	else
		moveToPrev("", ".left");
};

changeFocus[RIGHT_ARROW_KEY] = function() {
	if ($(".focusable.left:focus").size() === 0) return;
 	$(".focusable:focus").parentsUntil(".panel.horizontal").find(".focusable.right").first().focus();
};

changeFocus[DOWN_ARROW_KEY] = function() {
	if ($(".focusable.right:focus").size() !== 0)
		moveToNext(".right", ".left");
	else if ($(".focusable.left:focus").size() !== 0)
		moveToNext(".left", ".right");
	else
		moveToNext("", ".right");
};

function moveToPrev(focused, avoid) {
	$(".focusable" + focused + ":focus").parentsUntil("body").prevAll().has(".focusable:not("+ avoid +")").first().find(".focusable:not("+ avoid +")").last().focus();
}

function moveToNext(focused, avoid) {
	$(".focusable" + focused + ":focus").parentsUntil("body").nextAll().has(".focusable:not("+ avoid +")").first().find(".focusable:not("+ avoid +")").last().focus();
}

$(document).ready(function(){
	$(".focusable").focus(function() {
		INSIDE_COMPONENT = false;
	});
});

$(document).keydown(function(e) {

	if (isArrowKey(e.keyCode) && !INSIDE_COMPONENT)
		changeFocus[e.keyCode]();
	else
		INSIDE_COMPONENT = !(e.keyCode === ESC_KEY);

	function isArrowKey(keyCode) {
		return keyCode >= LEFT_ARROW_KEY && keyCode <= DOWN_ARROW_KEY;
	}
});