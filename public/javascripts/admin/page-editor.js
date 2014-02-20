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
	
	self.editing.subscribe(function(newValue) {
		if(newValue) {
			$('#pageBody').css({'padding-top' : '150px'});
		} else {
			$('#pageBody').removeAttr('style');
		}
	});	

	self.savePage = function() {
		
		self.toggleEditing();
		if (self.edited()) {
			setTimeout(function() {
				self.pageSchema.page.rows = [];
				$('.rowSelector').each(function(rowsIndex, rowElement) {
					self.pageSchema.page.rows.push({
						order : parseInt(rowsIndex),
						columns : []
					});
					$(this).find('.columnSelector').each(function(columnIndex, columnElement) {
						self.pageSchema.page.rows[rowsIndex].columns.push({
							order : parseInt(columnIndex),
							columnHtml : $(this).find('.textWidget').find('.textWidgetTextBox').html()
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
				(function(rowIndex) {
					self.insertRow($('#insertPoint'));
					for(var columnIndex = 0; columnIndex < self.pageSchema.page.rows[rowIndex].columns.length; ++columnIndex) {
						(function(rowIndex, columnIndex) {
							self.insertWidget('#row'+rowIndex, function insertWidgetCallback(columnIndex, rowIndex) {
								$('#column'+columnIndex+'row'+rowIndex).find('.textWidget').find('.textWidgetTextBox').html(self.pageSchema.page.rows[rowIndex].columns[columnIndex].columnHtml);
							});
						}(rowIndex, columnIndex));
					}
				}(rowIndex));
			}
		}
		self.edited(false);
		self.toggleEditing();
	}
	
	self.insertColumnButton = function(rowNum, theViewModel, event) {
		self.insertWidget($(event.currentTarget).closest('.rowSelector').children().first());
	};

	self.insertWidget = function(loc, callback) {
		(function(loc) {
			$.get(
					'/api/page-editor/templates/column', 
					function(columnTemplate) {
						var rowNum = null;
						if(typeof loc === 'string') {
							rowNum = loc.substr(4, loc.length);
						}
						var cols = $(loc).find('.columnSelector');
						cols.each(function() {
							$(this).removeClass('col-lg-' + 12 / cols.length).addClass(
									'col-lg-' + Math.floor(12 / (cols.length + 1)));
						});
						$(loc).append('<div id="column' + cols.length + 'row' + rowNum + '" class="columnSelector col-lg-'
								+ Math.floor(12 / (cols.length + 1)) + '">'
								+ columnTemplate + '</div>'
								+ '<div id="columnEditor" data-bind="if:editing">'
								+ '<div class="row">'
								+ '<div class="col-lg-12">'
								+ '<div class="btn-group btn-group-xs">'
								+ '<button type="button" class="btn btn-default" data-bind="click:function(theViewModel, theEvent){addRow(theViewModel, theEvent)}">'
								+ '<span class="glyphicon glyphicon-plus"></span> Insert Row'
								+ '</button>'
								+ '</div>'
								+ '</div>'
								+ '</div>'
								+ '</div>'
						);
						ko.applyBindings(self, document.getElementById('column' + cols.length + 'row' + rowNum));
						ko.applyBindings(self, document.getElementById('columnEditor'));
						$('#columnEditor').removeAttr('id');
						self.bindTextBoxes();
						self.edited(true);
						if(typeof callback === 'function') {
							callback(cols.length, rowNum);
						}
					}
			);
		}(loc));
	};

	self.addRow = function(vm, event) {
		var insertPoint;
		console.log(event.currentTarget.id);
		if(event.currentTarget.id !== 'insertPageRowButton') {
			console.log('closest');
			insertPoint = $(event.currentTarget).closest('.columnButtons').prev();
		} else {
			console.log('page row')
			insertPoint = $('#insertPoint')
		}
		console.log(insertPoint);
		self.insertRow(insertPoint);
	};
	
	self.insertRow = function(insertPoint) {
		var rows = $('.rowSelector');
		insertHtmlAtLoc(
				insertPoint,
				'<div class="container rowSelector" id="newRow">'
						+ '<div class="row" id="row'
						+ rows.length
						+ '"></div>'
						+ '<div data-bind="if:editing"><div class="row"><div class="col-lg-12"><div class="btn-group pull-right">'
						+ '<button type="button" class="btn btn-default btn-mini" data-bind="click:function(theViewModel, theEvent){insertColumnButton('+rows.length+', theViewModel, theEvent)}">'
						+ '<span class="glyphicon glyphicon-plus"></span> Insert Column'
						+ '</button>'
						+ '<button type="button" class="btn btn-default btn-mini" data-bind="click:function(){removeRow('+rows.length+')}"><span class="glyphicon glyphicon-minus"></span> Remove Row</button>'
						+ '</div></div></div></div></div>');
		ko.applyBindings(self, document.getElementById('newRow'));
		$('#newRow').removeAttr('id');
		self.edited(true);
	}
	
	self.removeRow = function(rowNum) {
		$('#row'+rowNum).parent().remove();
		self.edited(true);
	};
	self.editorCommands = ko.observableArray([]);

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
var pageEditorViewModel;
$(function() {
	pageEditorViewModel = new PageEditorViewModel();
	pageEditorViewModel.init();
	if (ko) {
		ko.applyBindings(pageEditorViewModel);
	}
});


function insertHtmlAtLoc(loc, html) {
	loc.append(html);
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