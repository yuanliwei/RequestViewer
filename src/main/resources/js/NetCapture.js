var list = null;

var NetCapture = {
		devArr: [],
		devNo: 0,
		getDevices: function(){
			return getClient().findAllDevs();
		},
		start: function(){
			return getClient().startCapture(this.devArr[this.devNo].name);
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

function getDevs() {
	var devs = NetCapture.getDevices();
	var devArr = JSON.parse(devs);
	devName = devArr[0].name;
	console.log(devs);
}

function startCapture() {
	var result = NetCapture.start();
	console.log('start capture : ' + result);
	registerCaptureCallback();
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
				arrow: $(view.find('.arrow')),
				fa: $(view.find('.fa')),
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
			holder.arrow.click(function (view) {
				holder.fa.removeClass("fa-chevron-right fa-chevron-down");
				if (holder.captureContent.hasClass("d-none")) {
					holder.captureContent.removeClass("d-none");
					holder.fa.addClass("fa-chevron-down");
				} else {
					holder.captureContent.addClass("d-none");
					holder.fa.addClass("fa-chevron-right");
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
		|| header.startsWith('DELETE ')
		|| header.startsWith('PUT ')
		|| header.startsWith('OPTIONS ')) {
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

function initDropdownMenus() {
	var devBtn = $("#dev-name");
	var devDropdown = $("#dev-name-dropdown");
	var netDeviceName = Client.loadData("netDeviceName");
	var devs = NetCapture.getDevices();
	var devArr = JSON.parse(devs);
	NetCapture.devArr = devArr;
	var found = false;
	for (var i = 0; i < devArr.length; i++) {
		var dev = devArr[i];
		if (dev.name == netDeviceName) {
			found = true;
		}
		devDropdown.append('<a class="dropdown-item" href="#" onclick="selNetDevice('+i+')">' + dev.name + '</a>');
	}
	if (!found) {
		netDeviceName = devArr[0].name;
		Client.saveData("netDeviceName", netDeviceName);
		NetCapture.devNo = 0;
	}
	devBtn.text(netDeviceName);

	$("#url-filter").change(function (view) {
		var value = view.target.value;
		// list.datas
	});
}

function selNetDevice(devNo) {
	var devBtn = $("#dev-name");
	devBtn.text(NetCapture.devArr[devNo].name);
	Client.saveData("netDeviceName", devNo);
	NetCapture.devNo = devNo;
}

$(document).ready(function () {
	initList();
	// for (var i = 0; i < 300; i++) {
	// 	list.append({url: 'url', request:'request', response: 'response'});
	// }
	setTimeout(initDropdownMenus, 300);
});
