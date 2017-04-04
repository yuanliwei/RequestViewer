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
		headers:[{key:'key1',value:'v1'},{key:'key2',value:'v2'},{key:'key3',value:'v3'}],
		params: 'name=yyy&age=13&height=165',
	    onSuccess: function(headers, body) {
	        console.log(body);
	    },
	    onError: function(msg) {
	    	console.log(msg);
	    }
	});
}

function testLogin() {
	HttpClient.POST({
		url: 'http://sso.host.cn/v1/tickets',
		headers:[{key:'X-Requested-With', value:'android'}],
		params: 'username=up0174&password=123456',
		onSuccess: function (headers, body) {
			console.log('login - '+body);
			genST();
		},
		onError: function (msg) {
			console.log('login error - ' + msg);
		}
	});
}

function genST() {
	HttpClient.POST({
		url: 'http://sso.host.cn/v1/serviceTicket',
		headers:[{key:'X-Requested-With', value:'android'}],
		params: 'service='+encodeURIComponent('http://school.host.cn/client/course/list-of-student?status=1'),
		onSuccess: function (headers, body) {
			console.log('get st - ' + body);
			getCourseList(JSON.parse(body).data.serverTicket);
		},
		onError: function (msg) {
			console.log("get st error - " + msg);
		}
	});
}

function getCourseList(ticket) {
	HttpClient.GET({
		url: 'http://school.host.cn/client/course/list-of-student?status=1&ticket='+ticket,
		headers:[{key:'X-Requested-With', value:'android'}],
		onSuccess: function (headers, body) {
			console.log("getCourseList - " + body);
		},
		onError: function (msg) {
			console.log("getCourseList error - " + msg);
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
