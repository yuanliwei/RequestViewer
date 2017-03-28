var Client = {
	getClient: function () {
		return jsObj;
	},
	saveData: function (key, value) {
		this.getClient().saveData(key, value);
	},
	loadData: function (key) {
		return this.getClient().loadData(key);
	}
};
var RequestView = {

};
var HttpClient = {
		getClient:function(){
			return jsObj;
		},
		GET: function(reqObj){
			this.getClient().GET(reqObj);
		},
		POST: function(reqObj){
			this.getClient().POST(reqObj);
		}
};

function testGet(){
	HttpClient.GET({
		url:'http://www.baidu.com',
		onSuccess: function(header, body){
			console.log(body);
		},
		onError: function(msg){
			console.log(msg);
		}
	});
}

function testPost(){
	HttpClient.POST({
		url: 'http://www.baidu.com',
		params: 'name=yyy&age=13&height=165',
	    onSuccess: function(headers, body) {
	        console.log(body);
	    },
	    onError: function(msg) {
	    	console.log(msg);
	    }
	});
}

function testOnSaveData() {
	Client.saveData('ylw', 'yuanliwei');
	console.log('save over.');
}

function testOnLoadData() {
	var value = Client.loadData('ylw');
	console.log('load data is : ' + value);
}
