// API1
function api1_clickSend() {
	$.get(getOrigin() + "/", (data) => api1_out(JSON.stringify(data)));
}
const api1Autosend = {isActive: 0, freq: 30};
function api1_clickAutosend(isActive, freq) {
	if(!api1Autosend.isActive && isActive) {
		const autosend = setTimeout(()=>{
			if(api1Autosend.isActive) {
				api1_clickSend();
				setTimeout(autosend, api1Autosend.freq);
			}
		}, 1);
	}
	api1Autosend.isActive = isActive;
	api1Autosend.freq = freq;
}


// API2
function api2_clickSend(roomName) {
	$.get(getOrigin() + "/" + roomName, (data) => api1_out(JSON.stringify(data)));
}
const api1Autosend = {isActive: 0, freq: 30};
function api1_clickAutosend(isActive, freq, roomName) {
	if(!api1Autosend.isActive && isActive) {
		const autosend = setTimeout(()=>{
			if(api1Autosend.isActive) {
				api1_clickSend(roomName);
				setTimeout(autosend, api1Autosend.freq);
			}
		}, 1);
	}
	api1Autosend.isActive = isActive;
	api1Autosend.freq = freq;
}
