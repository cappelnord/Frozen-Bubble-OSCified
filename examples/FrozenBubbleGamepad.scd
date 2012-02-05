
/* 

	Frozen Bubble OSCified Gamepad Controll Example
	Written by Patrick Borgeat <patrick@borgeat.de>, 2008
	http://www.cappel-nord.de

*/

( 	// Execute these lines to get the HID Product Name.
	// Also may be needed to give GeneralHID time to initialize
	GeneralHID.buildDeviceList;
	GeneralHID.postDevices;
	"Built Device List, waiting some time ...".postln;
);

(
 	// Execute these lines to get it on :>

	// You can config some stuff here
	
	var deviceProduct = "Dual Analog Pad"; // HID Productname
	var net = NetAddr("127.0.0.1",57180); // NetAddr to Frozen Bubble OSCified
	var horizontalAxis = [48,53]; // Gamepad Axis you want to use as Left/Right Controll
	
	//
	//
	//
	
	var deviceList, device, pad;
	var buttonFunction, axisFunction;
	
	"... initalizing Gamepad!".postln;

	
	buttonFunction = {|button|
		if(button.value == 1, {
			net.sendMsg("/fire");
			// "Fire!".postln;
		});
	};
	
	axisFunction = {|button|
		if(button.value == 1, {
		
			net.sendMsg("/stay/left");
			// "Left!".postln;
		});
		
		if(button.value == 0, {
		
			net.sendMsg("/stay/right");
			// "Right!".postln;
			
		});
		
		if((button.value != 0) && (button.value != 1), {
		
			net.sendMsg("/stay/release");
			// "Center!".postln;
		
		});
	};
	
	GeneralHID.buildDeviceList;
	
	deviceList = GeneralHID.deviceList;
	
	deviceList.do{|item|
		
		if(item.product == deviceProduct,
		{
			device = item;
		});		
	};
	
	pad = GeneralHID.open(device);
	
	GeneralHID.startEventLoop;
	
	pad.slots[1].do {|item|
		item.action_(buttonFunction);
	};
	
	horizontalAxis.do {|item|
		pad.slots[3].at(item).action_(axisFunction);
	};	
)