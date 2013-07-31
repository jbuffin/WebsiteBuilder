function PageEditorViewModel() {
	var self = this;
	
	self.widgetTypes = ko.observableArray([{widgetType:'textWidget',menuText:'Text Widget'}]);
	self.numRows = ko.observable(0);

	self.insertHeader = function() {
		pasteHtmlAtCaret("<h4>Header Text</h4>");
	};
	self.insertWidget = function(widgetType) {
		insertHtmlAtBottom(widgetHtml[widgetType.widgetType]);
	};
	self.addRow = function() {
		var newRowCount = self.numRows() + 1;
		insertHtmlAtBottom('<div class="row" id="row'+newRowCount+'"></div>');
		self.numRows(newRowCount);
	};

	self.init = function() {
		self.numRows(countRows());
	};
}
var pevm = new PageEditorViewModel();
pevm.init();
$(function() {
	if (ko) {
		ko.applyBindings(pevm);
	}
});

var widgetHtml = {
	textWidget : '<div><div contenteditable="true">Type your text here</div></div>'
};

function insertHtmlAtBottom(html) {
	$('#insertPoint').append(html);
}

function addRow() {
	insertHtmlAtBottom('<div class="row" id="row'+(pevm.numRows()+1)+'"></div>');
}

function countRows() {
	var count = 0;
	$('div[id^="row"]').each(function() {
		count++;
	});
	return count;
}

function pasteHtmlAtCaret(html) {
	var sel, range;
	if (window.getSelection) {
		// IE9 and non-IE
		sel = window.getSelection();
		if (sel.getRangeAt && sel.rangeCount) {
			range = sel.getRangeAt(0);
			range.deleteContents();

			// Range.createContextualFragment() would be useful here but is
			// non-standard and not supported in all browsers (IE9, for one)
			var el = document.createElement("div");
			el.innerHTML = html;
			var frag = document.createDocumentFragment(), node, lastNode;
			while ((node = el.firstChild)) {
				lastNode = frag.appendChild(node);
			}
			range.insertNode(frag);

			// Preserve the selection
			if (lastNode) {
				range = range.cloneRange();
				range.setStartAfter(lastNode);
				range.collapse(true);
				sel.removeAllRanges();
				sel.addRange(range);
			}
		}
	} else if (document.selection && document.selection.type != "Control") {
		// IE < 9
		document.selection.createRange().pasteHTML(html);
	}
}