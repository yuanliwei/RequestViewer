
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
