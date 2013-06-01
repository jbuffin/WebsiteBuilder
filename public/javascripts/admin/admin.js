var siteServer;
var pageServer;
var widgetServer;
var server;

function AdminViewModel() {
	var self = this;

	self.currentSite = ko.observable();
	self.currentPage = ko.observable();
	self.currentWidget = ko.observable();

	self.init = function() {
		console.log('AdminViewModel init()');
		server = new FakeServerAccessor("");
		siteServer = new SiteAccessor(server);
		pageServer = new PageAccessor(server);
		widgetServer = new WidgetAccessor(server);
	}
}

var avm;
avm = new AdminViewModel();
avm.init();
$(function() {
	if (typeof ko !== "undefined") {
		ko.applyBindings(avm);
	}
});

function SiteAccessor(server) {
	var self = this;
	self.server = server;

	self.getAll = function(callback) {

	};
	self.newSite = function(site, callback) {

	};
}
function PageAccessor(server) {
	var self = this;
	self.server = server;

	self.getAllBySite = function(callback) {

	};
	self.newPage = function(page, callback) {

	};
}
function WidgetAccessor(server) {
	var self = this;
	self.server = server;

	self.getAllByPage = function(callback) {

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
