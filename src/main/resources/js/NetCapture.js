
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

function testRegisterCaptureCallback(){
	NetCapture.onCapture(function(header, content){
		console.log(header);
		console.log(content);
		$('#response').append(header);
		$('#response').append(content);
	});
	console.log('register capture callback.');
}

var devName;
function testGetDevs() {
	var devs = NetCapture.getDevices();
	var devArr = JSON.parse(devs);
	devName = devArr[0].name;
	console.log(devs);
}

function testStartCapture() {
	var result = NetCapture.start(devName);
	console.log('start capture : ' + result);
}

function testStopCapture() {
	NetCapture.stop();
	console.log('stop capture.');
}

$(document).ready(function () {
	var datas = [];
	for (var i = 0; i < 3000; i++) {
		datas.push({'wordNo':i, 'desc': 'descdescdesc', 'wrongNum': 23});
	}
	var list = null;
	setTimeout(function () {
		var root = $("#root");
		list = new FastList(root);
		list.setHandle({
			createViewHolder: function (view) {
				var holder = {
					wordNo: $(view.find('.wordNo')),
					desc: $(view.find('.desc')),
					wrongNum: $(view.find('.wrongNum')),
					descBg: $(view.find('.descBg')),
					item: $(view.find('.row'))
				}
				return holder;
			},
			bindData: function (position, holder, data) {
				holder.wordNo.text(data.wordNo);
				holder.desc.text(data.desc);
				holder.wrongNum.text(data.wrongNum);
				holder.item.removeClass("bg-info bg-primary");
				if (position%2 == 0) {
					holder.item.addClass("bg-info");
				} else {
					holder.item.addClass("bg-primary");
				}
			}
		});
		list.setDatas(datas);
		list.log();
	},1000);
});
