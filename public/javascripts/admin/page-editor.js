function PageEditorViewModel() {
	var self = this;

	self.editing = ko.observable(false);
	self.edited = ko.observable(false);

	self.toggleEditing = function() {
		self.editing(!self.editing());
	};

	self.bindTextBoxes = function() {
		$(function() {
			$('.textWidgetTextBox').bind("propertychange keyup input paste",
					function() {
						self.edited(true);
					});
		});
	};

	self.savePage = function() {
		self.toggleEditing();
		if (self.edited()) {
			setTimeout(function() {
				self.pageSchema.page.rows = [];
				$('div[id^="row"]').each(function(rowsIndex, rowElement) {
					self.pageSchema.page.rows.push({
						order : parseInt(rowsIndex),
						columns : []
					});
					
					$(rowElement).find('div[id^="column"]').each(function(columnIndex, columnElement) {
						self.pageSchema.page.rows[rowsIndex].columns.push({
							order : parseInt(columnIndex),
							columnHtml : $(columnElement).find('.textWidget').find('.textWidgetTextBox').html()
						});
					});
				});
				jsRoutes.controllers.SitesApi.updatePage(self.siteId, self.pageId).ajax({
					data : JSON.stringify(self.pageSchema.page),
					contentType : 'text/json',
					success : function() {
						jsRoutes.controllers.SitesApi.getPageById(self.siteId, self.pageId)
						.ajax({
							success : function(thePage) {
								self.pageSchema = thePage;
								self.edited(false);
							}
						});
					},
					error : function(e) {
						console.error(JSON.stringify(e));
					}
				});
			}, 0);
		}
	};

	self.discardChanges = function() {
		if (self.edited()) {
			var insertPoint = $('#insertPoint');
			insertPoint.html('');
			for(var rowIndex = 0; rowIndex < self.pageSchema.page.rows.length; ++rowIndex) {
				var row = self.pageSchema.page.rows[rowIndex];
				self.addRow();
				for(var columnIndex = 0; columnIndex < row.columns.length; ++columnIndex) {
					var column = row.columns[columnIndex];
					self.insertWidget(rowIndex);
					var columnElem = $('#column'+columnIndex+'row'+rowIndex).find('.textWidget').find('.textWidgetTextBox').html(column.columnHtml);
					columnElem.html(column.columnHtml);
				}
			}
		}
		self.edited(false);
		self.toggleEditing();
	}

	self.insertHeader = function() {
		pasteHtmlAtCaret('<h4 id="newHeader">Header Text</h4>');
		self.edited(true);
	};

	self.insertWidget = function(rowNum) {
		var loc = 'row' + rowNum;
		var cols = $('#' + loc).find('div[class^="col-lg-"]');
		cols.each(function() {
			$(this).removeClass('col-lg-' + 12 / cols.length).addClass(
					'col-lg-' + Math.floor(12 / (cols.length + 1)));
		});
		insertHtmlAtLoc(loc, '<div id="newTextWidget" class="col-lg-'
				+ Math.floor(12 / (cols.length + 1)) + '">'
				+ widgetHtml.textWidget + '</div>');
		ko.applyBindings(self, document.getElementById('newTextWidget'));
		$('#newTextWidget').attr('id', 'column'+cols.length+'row'+rowNum);
		self.bindTextBoxes();
		self.edited(true);
	};

	self.addRow = function() {
		var rows = $('div[id^="row"]');
		insertHtmlAtLoc(
				'insertPoint',
				'<div class="container" id="newRow">'
						+ '<div class="row" id="row'
						+ rows.length
						+ '"></div>'
						+ '<div data-bind="if:editing"><div class="row"><div class="col-lg-12"><div class="btn-group pull-right">'
						+ '<button type="button" class="btn btn-default btn-mini dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-edit"></span></button>'
						+ '<ul class="dropdown-menu">'
						+ '<li><a tabindex="-1" href="#" data-bind="text:\'Insert column\',click:function(){insertWidget('
						+ rows.length
						+ ')}"></a></li>'
						+ '<li><a tabindex="-1" href="#" data-bind="click:insertHeader">Header (h4)</a></li>'
						+ '</ul></div></div></div></div>' + '</div>');
		ko.applyBindings(self, document.getElementById('newRow'));
		$('#newRow').removeAttr('id');
		self.edited(true);
	};

	self.init = function() {
		self.pageId = $('div[id^="pageId"]').attr("id").substr(7);
		self.siteId = $('div[id^="siteId"]').attr("id").substr(7);
		$(function() {
			jsRoutes.controllers.SitesApi.getPageById(self.siteId, self.pageId)
					.ajax({
						success : function(thePage) {
							self.pageSchema = thePage;
						}
					});
		});
		self.bindTextBoxes();
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
	textWidget : '<div class="textWidget"><div class="textWidgetTextBox" data-bind="attr:{\'contenteditable\':editing()}">Type your text here</div></div>'
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