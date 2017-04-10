var list = null;

var NetCapture = {
		getDevices: function(){
			return getClient().findAllDevs();
		},
		start: function(devName){
			return getClient().startCapture(devName);
		},
		stop: function(){
			return getClient().stopCapture();
		},
		onCapture: function(callback){
			getClient().registerCaptureCallback({onHttpPacket: callback});
		}
};

function registerCaptureCallback(){
	NetCapture.onCapture(function(header, content){
		console.log(header);
		console.log(content);
		HttpRequestHandle.handle(header, content, function (url, request, response) {
			list.append({url: url, request:request, response: response});
		});
	});
	console.log('register capture callback.');
}

var devName;
function getDevs() {
	var devs = NetCapture.getDevices();
	var devArr = JSON.parse(devs);
	devName = devArr[0].name;
	console.log(devs);
}

function startCapture() {
	var result = NetCapture.start(devName);
	console.log('start capture : ' + result);
}

function stopCapture() {
	NetCapture.stop();
	console.log('stop capture.');
}

function initList() {
	var root = $("#list");
	list = new NormalList(root);
	list.setHandle({
		createViewHolder: function (view) {
			var holder = {
				wordNo: $(view.find('.wordNo')),
				request: $(view.find('.request')),
				response: $(view.find('.response')),
				url: $(view.find('.url')),
				captureContent: $(view.find('.capture-content')),
				item: $(view.find('.row'))
			}
			return holder;
		},
		bindData: function (position, holder, data) {
			holder.wordNo.text(data.wordNo);
			holder.request.text(data.request);
			holder.response.text(data.response);
			holder.url.text(data.url);
			holder.url.click(function (view) {
				if (holder.captureContent.hasClass("d-none")) {
					holder.captureContent.removeClass("d-none");
				} else {
					holder.captureContent.addClass("d-none");
				}
			});
		}
	});
}

var HttpRequestHandle = {
	request: null,
	url: null,
	handle: function (header, content, callback) {
		if (header.startsWith('POST ')
		|| header.startsWith('GET ')
		|| header.startsWith('OPTION ')) {
			this.request = header + content;
			this.url = this.getUrl(header);
		}
		if (header.startsWith('HTTP')) {
			try {
				var json = JSON.parse(content.trim());
				content = js_beautify(content.trim(), {});
			} catch (e) {
				console.log("content : " + content);
				console.log("EEE == = = = = = = = = =" + content);
				console.log("PPPPP = =  : " + (typeof js_beautify));
				console.log(JSON.stringify(e));
			}
			var response = header + content;
			callback(this.url, this.request, response);
		}
	},
	getUrl: function (header) {
		var headers = header.split('\n');
		var params = {};
		var firstLine = headers[0].trim();
		var path = firstLine.split(' ')[1];
		for (var i = 1; i < headers.length; i++) {
			var paramKV = headers[i].trim();
			paramKV = paramKV.split(': ');
			params[paramKV[0]] = paramKV[1];
		}
		var host = params["Host"];
		var url = "http://" + host + path;
		return url;
	}
};

$(document).ready(function () {
	initList();
});
