var siteServer;
var pageServer;
var widgetServer;
var server;

function AdminViewModel() {
	var self = this;

	self.currentSite = ko.observable();
	self.currentPage = ko.observable();
	self.currentWidget = ko.observable();
	self.siteList = ko.observableArray([]);
	self.pageList = ko.observableArray([]);
	self.widgetList = ko.observableArray([]);
	
	self.siteFormVisible = ko.observable(false);
	self.pageFormVisible = ko.observable(false);
	self.widgetFormVisible = ko.observable(false);

	self.currentSite.subscribe(function(newValue) {
		if(newValue) {
			pageServer.getAllBySite(newValue.id, function(data) {
				self.pageList(data);
			});
		} else {
			self.pageList([]);
		}
	});

	self.currentPage.subscribe(function(newValue) {
		if(newValue) {
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
		self.pageFormVisible(self.pageFormVisible());
	};
	self.showNewWidgetForm = function() {
		self.widgetFormVisible(self.widgetFormVisible());
	};
	
	self.createNewSite = function(formElement) {
		var theFormData = {
			siteName: $(formElement).find('#siteNameInput').val(),
			hostName: $(formElement).find('#siteHostName').val()
		};
		siteServer.newSite(theFormData, function() {
			siteServer.getAll(function(data) {
				self.siteList(data);
			});
			$(formElement).find('#siteNameInput').val('');
			$(formElement).find('#siteHostName').val('');
		});
	};
	
	

	self.init = function() {
		server = new FakeServerAccessor("");
		siteServer = new SiteAccessor(server);
		pageServer = new PageAccessor(server);
		widgetServer = new WidgetAccessor(server);
		siteServer.getAll(function(data) {
			self.siteList(data);
		});
	}
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
	self.server = server;

	self.fakeSites = [ {
		id : 1,
		siteName : 'grace church',
		hostName : 'gracechurch.net'
	}, {
		id : 2,
		siteName : 'my church site',
		hostName : 'mychurchsite.com'
	} ];
	self.getAll = function(callback) {
		callback(self.fakeSites);
	};
	self.newSite = function(formData, callback) {
		formData.id = self.fakeSites.length + 1;
		self.fakeSites.push(formData);
		callback();
	};
}
function PageAccessor(server) {
	var self = this;
	self.server = server;

	self.getAllBySite = function(siteId, callback) {
		callback([ {
			id : 1,
			title : 'Default Page'
		}, {
			id : 2,
			title : 'Our Church'
		}, {
			id : 3,
			title : 'Directions'
		} ]);
	};
	self.newPage = function(page, callback) {

	};
}
function WidgetAccessor(server) {
	var self = this;
	self.server = server;

	self.getAllByPage = function(pageId, callback) {
		callback([ {
			id : 1
		}, {
			id : 2
		}, {
			id : 3
		}, {
			id : 4
		} ]);
	};
	self.newWidget = function(page, callback) {

	};
}

function ServerAccessor(url) {
	var self = this;
	self.where = url;

	self.get = function(what, successCallBack) {

	}

	self.put = function(what, stuff, successCallBack) {

	}
}

function FakeServerAccessor() {
	var self = this;

	self.get = function(what, successCallBack) {

	}

	self.put = function(what, stuff, successCallBack) {

	}
}
