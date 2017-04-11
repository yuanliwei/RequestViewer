function getClient(){
	return jsObj;
}

String.prototype.startsWith = function(str) {
	return (this.match("^" + str) == str)
};

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

var Client = {
	saveData: function (key, value) {
		getClient().saveData(key, value);
	},
	loadData: function (key) {
		return getClient().loadData(key);
	}
};
