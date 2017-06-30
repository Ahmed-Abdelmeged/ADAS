const functions = require('firebase-functions');
let admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

//Get reference to the accident node
//Trigger this functions if an accident happen
exports.sendPushNotification = functions.database.
	ref('/users/{userId}/userInfo/accidents/{accidentId}').onWrite(event =>{

		//Flags to determine it's a new accident or old one
		let accidentStateChanged = false;
		let accidentCreated = false;
		console.log("test1");

		//Get the accident data
		let accidentData = event.data.val();

		//Check if the accident exist or not
		if(!event.data.previous.exists()){
			//Do any thing if accident did't exists before
			accidentCreated = true;
		}

		//Check if the accident change
		if(!accidentCreated && event.data.changed()){
			accidentStateChanged= true;
		}

		//The message that will be send to the user if the accident location changed
		let accidentState = true;
		
		//The message that will be send to the user if an accident happen
		if (accidentCreated) {
			 accidentState = false;
			 console.log("test4");
		}

		return loadUsers().then(users =>{
			//Get all users devices tokens
			let tokens = [];
			for(let user of users){
				tokens.push(user.devicePushToken);
			}

			//The FCM body 
			let payload = {
				data:{
					title: 'Accident',
					state : JSON.stringify(accidentState),
					longitude:JSON.stringify(accidentData.accidentLongitude),
					latitude : JSON.stringify(accidentData.accidentLatitude),
					sound : 'default',
				}
			};

			//send the message to all users
			return admin.messaging().sendToDevice(tokens, payload);

		});
			
});

//Function to load the users data and device push token
function loadUsers(){
	let databaseReference = admin.database().ref('/users');
	let defer = new Promise((resolve,reject) =>{
		databaseReference.once('value',(snap) =>{
						let data = snap.val();

		let users = [];
		for(var property in data){
			users.push(data[property]);
		}	

		resolve(users);
	},(err) =>{
			reject(err);
		});
		
	});
	return defer;
}	


	
