function PageEditorViewModel() {
	var self = this;
	
	self.widgetTypes = ko.observableArray([{widgetType:'textWidget',menuText:'Text Widget'}]);
	self.numRows = ko.observable(0);
	self.editing = ko.observable(false);
	
	self.toggleEditing = function() {
		self.editing(!self.editing());
	};
	
	self.savePage = function() {
		//TODO: save all the stuff
		self.toggleEditing();
	};
	self.discardChanges = function() {
		//TODO: any discard actions
		self.toggleEditing();
	}

	self.insertHeader = function() {
		pasteHtmlAtCaret("<h4>Header Text</h4>");
	};
	self.insertWidget = function(rowNum,widgetType) {
		var loc = 'row'+rowNum;
		var cols = $('#'+loc).find('div[class^="col-lg-"]');
		var colCount = 0;
		cols.each(function() {
			colCount++;
		});
		cols.each(function() {
			$(this).removeClass('col-lg-'+12/colCount).addClass('col-lg-'+Math.floor(12/(colCount+1)));
		});
		insertHtmlAtLoc(loc, '<div class="col-lg-'+Math.floor(12/(colCount+1))+'">'+widgetHtml[widgetType.widgetType]+'</div>');
	};
	self.addRow = function() {
		var newRowCount = self.numRows() + 1;
		insertHtmlAtLoc('insertPoint', '<div class="container"><div class="row" id="row'+newRowCount+'"></div></div>');
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
	textWidget : '<div contenteditable="true">Type your text here</div>'
};

function insertHtmlAtLoc(loc, html) {
	$('#'+loc).append(html);
}

/*function insertHtmlAtBottom(html) {
	$('#insertPoint').append(html);
}*/

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