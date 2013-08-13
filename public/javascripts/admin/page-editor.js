function PageEditorViewModel() {
	var self = this;

	self.widgetTypes = ko.observableArray([ {
		widgetType : 'textWidget',
		menuText : 'Text Widget'
	} ]);
	self.numRows = ko.observable(0);
	self.editing = ko.observable(false);
	self.pageId = -1;

	self.toggleEditing = function() {
		self.editing(!self.editing());
	};

	self.savePage = function() {
		if (thingsToAdd.rows.length) {
			jsRoutes.controllers.Sites.addRowsToPage(self.pageId,
					thingsToAdd.rows.length).ajax({
				success : function(data) {
					discardRows();
				}
			});
		}
		if (thingsToAdd.textWidgets.length) {
			var textWidgetsToSend = [];
			var htmlToSave = '';
			for ( var i = 0; i < thingsToAdd.textWidgets.length; i++) {
				htmlToSave = $('#' + thingsToAdd.textWidgets[i]).children(
						':first').html();
				textWidgetsToSend.push({
					'textWidgetId' : parseInt(thingsToAdd.textWidgets[i]),
					'savedHtml' : htmlToSave
				});
			}
			jsRoutes.controllers.Widgets.updateTextWidgetById().ajax({
				data : JSON.stringify(textWidgetsToSend),
				contentType : 'text/json',
				error : function(e) {
					console.error(JSON.stringify(e));
				},
				success : function(data) {
					thingsToAdd.textWidgets = [];
				}
			});
		}
		self.toggleEditing();
	};

	self.discardChanges = function() {
		discardRows();
		discardTextWidgetChanges();
		self.toggleEditing();
	}

	self.insertHeader = function() {
		pasteHtmlAtCaret('<h4 id="newHeader">Header Text</h4>');
		var widgetNode = $('#newHeader').parents('.textWidget:first');
		$('#newHeader').removeAttr('id');
		var textWidgetId = parseInt(widgetNode.attr('id'));
		if (thingsToAdd.textWidgets.indexOf(textWidgetId) === -1) {
			thingsToAdd.textWidgets.push(textWidgetId);
		}
	};

	self.insertWidget = function(rowNum, widgetType) {
		var loc = 'row' + rowNum;
		var cols = $('#' + loc).find('div[class^="col-lg-"]');
		var colCount = 0;
		cols.each(function() {
			colCount++;
		});
		cols.each(function() {
			$(this).removeClass('col-lg-' + 12 / colCount).addClass(
					'col-lg-' + Math.floor(12 / (colCount + 1)));
		});
		insertHtmlAtLoc(loc, '<div class="col-lg-'
				+ Math.floor(12 / (colCount + 1)) + '">'
				+ widgetHtml[widgetType.widgetType] + '</div>');
	};

	self.addRow = function() {
		var newRowCount = self.numRows() + 1;
		insertHtmlAtLoc(
				'insertPoint',
				'<div class="container" id="newRow">'
						+ '<div class="row" id="row'
						+ newRowCount
						+ '"></div>'
						+ '<div data-bind="if:editing"><div class="row"><div class="col-lg-12"><div class="btn-group pull-right">'
						+ '<button type="button" class="btn btn-default btn-mini dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-edit"></span></button>'
						+ '<ul class="dropdown-menu" data-bind="foreach: widgetTypes">'
						+ '<li><a tabindex="-1" href="#" data-bind="text: menuText,click:function(data, event){$parent.insertWidget('
						+ newRowCount + ',data)}">Text Widget</a></li>'
						+ '</ul></div></div></div></div>' + '</div>');
		ko.applyBindings(self, document.getElementById('newRow'));
		$('#newRow').removeAttr('id');
		self.numRows(newRowCount);
		thingsToAdd.rows.push(newRowCount);
	};

	self.init = function() {
		self.pageId = parseInt($('div[id^="pageId"]').attr("id").substr(7));
		self.numRows(countRows());
		$(function() {
			$('.textWidgetTextBox').bind("propertychange keyup input paste", function(e) {
				var textWidgetId = parseInt(e.currentTarget.parentNode.attributes[0].value);
				if (thingsToAdd.textWidgets.indexOf(textWidgetId) === -1) {
					thingsToAdd.textWidgets.push(textWidgetId);
				}
			});
		});
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
	$('#' + loc).append(html);
}

function countRows() {
	var count = 0;
	$('div[id^="row"]').each(function() {
		count++;
	});
	return count;
}

function discardRows() {
	thingsToAdd.rows.forEach(function(row) {
		$('#row' + row).parent().remove();
	});
	thingsToAdd.rows = [];
}
function discardTextWidgetChanges() {
	thingsToAdd.textWidgets.forEach(function(widgetId) {
		jsRoutes.controllers.Widgets.getTextWidgetHtmlByIdAsJSON(widgetId).ajax({
			success : function(textWidget) {
				$('#' + widgetId).children(':first').html(textWidget.text)
			}
		});
	});
	thingsToAdd.textWidgets = [];
}

var thingsToAdd = {
	rows : [],
	textWidgets : []
};

function pasteHtmlAtCaret(html) {
	var sel, range;
	if (window.getSelection) {
		// IE9 and non-IE
		sel = window.getSelection();
		if (sel.getRangeAt && sel.rangeCount) {
			range = sel.getRangeAt(0);
			range.deleteContents();
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

if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(obj, fromIndex) {
		if (fromIndex == null) {
			fromIndex = 0;
		} else if (fromIndex < 0) {
			fromIndex = Math.max(0, this.length + fromIndex);
		}
		for ( var i = fromIndex, j = this.length; i < j; i++) {
			if (this[i] === obj)
				return i;
		}
		return -1;
	};
}