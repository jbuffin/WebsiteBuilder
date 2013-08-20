var siteServer;
var pageServer;

function AdminViewModel() {
	var self = this;

	self.currentSite = ko.observable();
	self.currentPage = ko.observable();
	self.currentWidget = ko.observable();
	self.siteList = ko.observableArray([]);
	self.pageList = ko.observableArray([]);
	self.widgetList = ko.observableArray([]);
	self.allPageTypes = ko.observableArray([]);

	self.siteFormVisible = ko.observable(false);
	self.pageFormVisible = ko.observable(false);
	self.widgetFormVisible = ko.observable(false);

	self.currentSite.subscribe(function(newValue) {
		if (newValue) {
			pageServer.getAllBySite(newValue.id, function(data) {
				self.pageList(data);
			});
		} else {
			self.pageList([]);
		}
	});

	self.currentPage.subscribe(function(newValue) {
		if (newValue) {
			widgetServer.getAllByPage(newValue.id, function(data) {
				self.widgetList(data);
			});
		} else {
			self.widgetList([]);
		}
	});

	self.showNewSiteForm = function() {
		self.siteFormVisible(!self.siteFormVisible());
	};
	self.showNewPageForm = function() {
		self.pageFormVisible(!self.pageFormVisible());
		pageServer.getAllPageTypes(function(data) {
			self.allPageTypes(data);
		});
	};
	self.showNewWidgetForm = function() {
		self.widgetFormVisible(!self.widgetFormVisible());
	};

	self.createNewSite = function(formElement) {
		var theFormData = {
			id : -1,
			siteName : $(formElement).find('#siteNameInput').val(),
			hostName : $(formElement).find('#siteHostName').val()
		};
		siteServer.newSite(theFormData, function() {
			siteServer.getAll(function(data) {
				self.siteList(data);
			});
			$(formElement).find('#siteNameInput').val('');
			$(formElement).find('#siteHostName').val('');
			self.showNewSiteForm();
		});
	};
	
	self.createNewPage = function(formElement) {
		var formData = {
			id : -1,
			uri : $(formElement).find('#pageUri').val(),
			title : $(formElement).find('#pageTitle').val(),
			pageType : Number($(formElement).find('#pageType').val()),
			parent : $(formElement).find('#newPageParent').val() === '' ? -1 : Number($(formElement).find('#newPageParent').val()),
			siteId : Number(self.currentSite().id)
		};
		pageServer.newPage(formData, function() {
			pageServer.getAllBySite(self.currentSite().id, function(data) {
				self.pageList(data);
			});
			$(formElement).find('#pageUri').val('');
			$(formElement).find('#pageTitle').val('');
			self.showNewPageForm();
		});
	};

	self.init = function() {
		siteServer = new SiteAccessor();
		pageServer = new PageAccessor();
		widgetServer = new WidgetAccessor();
		siteServer.getAll(function(data) {
			self.siteList(data);
		});
	};
}

var avm = new AdminViewModel();
avm.init();
$(function() {
	if (ko) {
		ko.applyBindings(avm);
	}
});

function SiteAccessor(server) {
	var self = this;

	self.getAll = function(callback) {
		jsRoutes.controllers.SitesApi.getAllSites().ajax({
			success : callback
		});
	};
	self.newSite = function(formData, callback) {
		jsRoutes.controllers.Sites.newSite().ajax({
			data : JSON.stringify(formData),
			contentType : 'text/json',
			success : callback,
			error : function(e) {
				console.error(JSON.stringify(e));
			}
		});
	};
}
function PageAccessor(server) {
	var self = this;

	self.getAllBySite = function(siteId, callback) {
		jsRoutes.controllers.Sites.getAllPagesBySiteAsJson(siteId).ajax({
			success: callback
		});
	};
	self.getAllPageTypes = function(callback) {
		jsRoutes.controllers.Sites.getAllPageTypesAsJson().ajax({
			success: callback
		});
	};
	self.newPage = function(page, callback) {
		jsRoutes.controllers.Sites.newPageFromJson().ajax({
			data : JSON.stringify(page),
			contentType : 'text/json',
			success : callback,
			error : function(e) {
				console.error(JSON.stringify(e));
			}
		});
	};
}