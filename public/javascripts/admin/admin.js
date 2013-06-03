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

	self.getAll = function(callback) {
		callback([ {
			id : 1,
			siteName : 'grace church',
			hostName : 'gracechurch.net'
		}, {
			id : 2,
			siteName : 'my church site',
			hostName : 'mychurchsite.com'
		} ]);
	};
	self.newSite = function(site, callback) {

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
