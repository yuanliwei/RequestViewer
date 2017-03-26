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
	HttpCLient.GET({
		url:'http://www.baidu.com',
		onSuccess: function(header, body){
			console.log(body);
		},
		onError: function(msg){
			console.log(msg)l
		}
	});
}

function testPost(){
	HttpCLient.POST({
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