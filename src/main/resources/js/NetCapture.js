
var NetCapture = {
		getDevices: function(){
			return getClient().findAllDevice();
		},
		start: function(devName){
			getClient().startCapture(devName);
		},
		stop: function(){
			
		},
		onCapture: function(callback){
			getClient.registerCaptureCallback(callback);
		}
};

function testRegisterCaptureCallback(){
	NetCapture.onCapture(function(header, content){
		console.log(header);
		console.log(content);
	});
}