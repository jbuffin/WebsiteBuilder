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
	
	self.editorCommands = ko.observableArray([
		{
			'name' : 'bold',
			'icon' : 'glyphicon glyphicon-bold',
			'text' : '',
			'exec' : function() {
				document.execCommand('bold', false, null);
			}
		}, {
			'name' : 'link',
			'icon' : 'glyphicon glyphicon-link',
			'text' : '',
			'exec' : function() {
				var href = prompt('Url for link', '');
				document.execCommand('createLink', false, href);
			}
		}, {
			'name' : 'unlink',
			'icon' : '',
			'text' : 'unlink',
			'exec' : function() {
				
			}
		}, {
			'name' : 'fontSize',
			'icon' : 'glyphicon glyphicon-text-height',
			'text' : '',
			'exec' : function() {
				// 1 to 7
				var size = prompt('text size (1-7)', '');
				document.execCommand('fontSize', false, size);
			}
		}, {
			'name' : 'foreColor',
			'icon' : 'glyphicon glyphicon-tint',
			'text' : '',
			'exec' : function() {
				// string for the color
				var color = prompt('color', '');
				document.execCommand('foreColor', false, color);
			}
		}, {
			'name' : 'h1',
			'icon' : 'glyphicon glyphicon-header',
			'text' : '1',
			'exec' : function() {
				// formatBlock: "<h1>"
				document.execCommand('formatBlock', false, '<h1>');
			}
		}, {
			'name' : 'h2',
			'icon' : 'glyphicon glyphicon-header',
			'text' : '2',
			'exec' : function() {
				// formatBlock: "<h2>"
				document.execCommand('formatBlock', false, '<h2>');
			}
		}, {
			'name' : 'h3',
			'icon' : 'glyphicon glyphicon-header',
			'text' : '3',
			'exec' : function() {
				// formatBlock: "<h3>"
				document.execCommand('formatBlock', false, '<h3>');
			}
		}, {
			'name' : 'h4',
			'icon' : 'glyphicon glyphicon-header',
			'text' : '4',
			'exec' : function() {
				// formatBlock: "<h4>"
				document.execCommand('formatBlock', false, '<h4>');
			}
		}, {
			'name' : 'h5',
			'icon' : 'glyphicon glyphicon-header',
			'text' : '5',
			'exec' : function() {
				// formatBlock: "<h5>"
				document.execCommand('formatBlock', false, '<h5>');
			}
		}, {
			'name' : 'h6',
			'icon' : 'glyphicon glyphicon-header',
			'text' : '6',
			'exec' : function() {
				// formatBlock: "<h6>"
				document.execCommand('formatBlock', false, '<h6>');
			}
		}, {
			'name' : 'indent',
			'icon' : 'glyphicon glyphicon-indent-left',
			'text' : '',
			'exec' : function() {
				document.execCommand('indent', false, null);
			}
		}, {
			'name' : 'insertHorizontalRule',
			'icon' : '',
			'text' : 'hr',
			'exec' : function() {
				document.execCommand('insertHorizontalRule', false, null);
			}
		}, {
			'name' : 'insertImage',
			'icon' : 'glyphicon glyphicon-picture',
			'text' : '',
			'exec' : function() {
				// String for the src
				var src = prompt('location of image', '');
				document.execCommand(this.name, false, src);
			}
		}, {
			'name' : 'insertOrderedList',
			'icon' : '',
			'text' : 'ol',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'insertUnorderedList',
			'icon' : '',
			'text' : 'ul',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'italic',
			'icon' : 'glyphicon glyphicon-italic',
			'text' : '',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'justifyCenter',
			'icon' : 'glyphicon glyphicon-align-center',
			'text' : '',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'justifyFull',
			'icon' : 'glyphicon glyphicon-align-justify',
			'text' : '',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'justifyLeft',
			'icon' : 'glyphicon glyphicon-align-left',
			'text' : '',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'justifyRight',
			'icon' : 'glyphicon glyphicon-align-right',
			'text' : '',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'removeFormat',
			'icon' : 'glyphicon glyphicon-ban-circle',
			'text' : '',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'strikeThrough',
			'icon' : '',
			'text' : 'strikethrough',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'subscript',
			'icon' : '',
			'text' : 'subscript',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'superscript',
			'icon' : '',
			'text' : 'superscript',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}, {
			'name' : 'underline',
			'icon' : '',
			'text' : 'underline',
			'exec' : function() {
				document.execCommand(this.name, false, null);
			}
		}
	]);

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
						+ '<button type="button" class="btn btn-default btn-mini" data-bind="click:function(){insertWidget('+rows.length+')}">'
						+ '<span class="glyphicon glyphicon-plus"></span>'
						+ '</button>'
						+ '</div></div></div></div></div>');
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