function Column() {
	var self = this;
	self.order = ko.observable();
	self.columnHtml = ko.observable();
	
	self.init = function(newColumn) {
		self.order(newColumn.order);
		self.columnHtml(newColumn.columnHtml);
	};
	self.serialize = function() {
		var serializedColumn = {};
		serializedColumn.order = self.order();
		serializedColumn.columnHtml = self.columnHtml();
		return serializedColumn;
	};
};
function Row() {
	var self = this;
	self.order = ko.observable();
	self.columns = ko.observableArray([]);
	
	self.init = function(newRow) {
		self.order(newRow.order);
		newRow.columns.forEach(function(column){
			var columnToPush = ko.observable(new Column());
			columnToPush().init(column);
			self.columns().push(columnToPush);
		});
	};
	self.serialize = function() {
		var serializedRow = {};
		serializedRow.order = self.order();
		serializedRow.columns = [];
		self.columns().forEach(function(column){
			serializedRow.columns.push(column().serialize());
		});
		return serializedRow;
	};
};
function Page() {
	var self = this;
	self.page = ko.observable({});
	self.page().siteId = ko.observable();
	self.page().uri = ko.observable();
	self.page().title = ko.observable();
	self.page().parent = ko.observable();
	self.page().rows = ko.observableArray([]);
	
	self.init = function(pageObject) {
		self._id = pageObject._id;
		self.page().siteId(pageObject.page.siteId);
		self.page().uri(pageObject.page.uri);
		self.page().title(pageObject.page.title);
		self.page().parent(pageObject.page.parent);
		pageObject.page.rows.forEach(function(row){
			var rowToPush = ko.observable(new Row());
			rowToPush().init(row);
			self.page().rows().push(rowToPush);
		});
	};
	self.serialize = function() {
		var serializedObject = {};
		serializedObject._id = self._id;
		serializedObject.page = {};
		serializedObject.page.siteId = self.page().siteId();
		serializedObject.page.uri = self.page().uri();
		serializedObject.page.title = self.page().title();
		serializedObject.page.parent = self.page().parent();
		serializedObject.page.rows = [];
		self.page().rows().forEach(function(row) {
			serializedObject.page.rows.push(row().serialize());
		});
		return serializedObject;
	};
};

function PageEditorViewModel() {
	var self = this;
	self.pageObject = ko.observable();
	
	self.editing = ko.observable(false);
	
	self.editing.subscribe(function(newValue) {
		if (newValue) {
			self.pageUndoObject = self.pageObject().serialize();
		}
	});

	self.toggleEditing = function() {
		self.editing(!self.editing());
	};

	self.savePage = function() {
		self.toggleEditing();
		// TODO: promote the edited object to the server and resave to undo
		// object
		delete self.pageUndoObject; // ONLY AFTER SAVE IS SUCCESS
	};

	self.discardChanges = function() {
		setup();
		/*self.pageObject().init(self.pageUndoObject);
		console.log(document.getElementById('insertPoint'));
		ko.applyBindings(self.pageObject, document.getElementById('rows'));*/
		delete self.pageUndoObject;
	};

	self.insertHeader = function() {
		pasteHtmlAtCaret('<h4 id="newHeader">Header Text</h4>');
	};

	self.insertWidget = function(rowNum, widgetType) {
		// TODO: push a new column
	};

	self.addRow = function() {
		// TODO: push a new row
	};

	self.init = function(siteId, uri, pageData) {
		self.pageObject = ko.observable(new Page());
		self.pageObject().init(pageData);
	};
}

var pevm;
function setup() {
	pevm = new PageEditorViewModel();
	$(function() {
		var siteId = $('div[id^="siteId-"]').attr("id").substr(7);
		var pageUri = $('div[id^="pageUri-"]').attr("id").substr(8);
		getPageFromUri(siteId, pageUri, function(data) {
			pevm.init(siteId, pageUri, data);
			if (ko) {
				ko.bindingHandlers.editableText = {
						init : function(element, valueAccessor) {
							$(element).on('blur', function() {
								var observable = valueAccessor();
								observable($(this).html());
							});
						},
						update : function(element, valueAccessor) {
							var value = ko.utils.unwrapObservable(valueAccessor());
							$(element).html(value);
						}
				};
				ko.applyBindings(pevm);
			}
		});
		
	});
}
setup();

function getPageFromUri(siteId, uri, callback) {
	jsRoutes.controllers.SitesApi.getPageByUri(siteId, uri).ajax({
		success : callback
	});
}

var widgetHtml = {
	textWidget : '<div class="textWidget newTextWidget"><div class="textWidgetTextBox" data-bind="attr:{\'contenteditable\':editing()}">Type your text here</div></div>'
};

function insertHtmlAtLoc(loc, html) {
	$('#' + loc).append(html);
}

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