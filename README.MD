## ADAS

This an IOT project to help and provide services to self driving car
and it's a part of graduation project

## Authentication

We use authentication to make every user has his own account with it's data
We use two factor authentication in our app to insure high security level
using email and phone number associate with each user using **Firebase** as
our backend service for authentication. The authentication flow work like this .
First when opening the app for the first time the user asked to login if they already
have an account or signup for new car user like this

![Authentication entry screen](http://i.imgur.com/WnM5Wr0.png)

If the user choose to sign in will move to this screen to sign in with their email
and password

![Sign in](http://i.imgur.com/NJTCHwG.png)

If they to signup for new user they move to the signup activity
where they enter their information and the activity has high level of data validation
and all the fields here is required and the screen look look like this

![Signup](http://i.imgur.com/Li64nKx.png)

Then an SMS send to the user phone number to verify their phone number and enter it
in this activity the user can't use the application without phone number verified

![Verify number](http://i.imgur.com/ZgExSJG.png)

After the process done successfully the app take the user to the app main screen

![MainActivity](http://i.imgur.com/tJg7TYb.png)

![MainActivity](http://i.imgur.com/NLzMQkA.png)

The authentication data for the user look like this in database

![database scheme](http://i.imgur.com/3GjsY3m.png)

We use authentication to store this data for each user
* User personal information like name , phone number , email ,location
* Car sensors data
* Mapping data (location , going destination..etc)
* Videos and live streaming information
* Accident information

## Connect
This a screen to make the car and mobile connection via Bluetooth to get the car sensors reading and collect car other information. After the connection done the app take the user to the app main screen to start use the app features

* You can connect a previous connected car
* You can search for new one to connect

![Connect](http://i.imgur.com/V1DWD4O.png)

## Car
This used when driving to show the user the car information like sensors , current location ..etc. It can also used to control the car like open the lights , the doors ..etc. And determine the car current location using google maps APIs and show in the application UI in real time and show the sensors reading in real time also. you can use it to disconnect the current car connection .It's upload the car data to our backend to use it in other areas in the application

![Car](http://i.imgur.com/Kvp2mdq.png)

This the car database scheme

![car scheme](http://i.imgur.com/tcL6v90.png)

## User
This used for control the car over the internet with only the authorized people to control the car main function over the internet(IOT) show the sensors and car current  location in real time and it also can use to show the current user location
two marker in the same time one for the user and one for the car and when accident happen it show to the user a dialog to call the emergency with current accident location but it has limit with control the car it can't disconnect the car from the current connection with app. It can done only by the car driver

![User](http://i.imgur.com/LsySVit.png)

## Directions
This feature using Google direction API. SO if user went to go to place in self driving mode he open the direction activity and select a place to go to and the app make a request to the API and get the place distention information and journey details
and send it to the micro controller and it integration with the AI algorithm and start the self driving

* The user press the add button and a place picker open to make the user select a place to go to
* The request is done and return information to the backend and the micro controller
* The app show the journey information and steps

![Directions](http://i.imgur.com/0666TXl.png)

The journey steps

![Steps](http://i.imgur.com/HbGDqcx.png)

Directions database scheme

![Direction database scheme](http://i.imgur.com/5xXVVVS.png)

## Videos
This activity show the user old live streamed videos using Youtube API we integrate
it with our application to show the old videos as log for any event can happened
and also if an accident happened. All videos saved in private playlist in youtube each user have his one playlist that he can change it form the app settings. The user also can view the video in the app without going to the youtube  app

![Videos](http://i.imgur.com/kxTX7yj.png)   

Watch selected video

![Watch video](http://i.imgur.com/LQ4qb7U.png)

## Live streaming
This show a live streaming for the car in real time the video is show by raspberry pi
camera and upload to the app to display and any authorized user can watch this live streaming and if no current live streaming it show this in the app UI

![Live streaming](http://i.imgur.com/bQzaAT2.png)  

## Accidents
This an log for the accident history you can show the all the car accident location, data and time. all the accident saved offline in the app SQLite database in case for no internet connection and updated when the app online you can show all the accident and show accident details when select a specific accident

![Accidents](http://i.imgur.com/XhCnTbZ.png)

Accident details

![Accident details](http://i.imgur.com/gpmlUAR.png)

When accident is happen it send notification to all the authorized user with the accident information to help the car that had the accident using our custom backend code to do this

![Accident notification](http://i.imgur.com/pnMWuEb.png)

When the user open the notification he view the current accident location and has location and he can also press the direction button to go to the accident location for help  and also a steps for the journey. and the car go to self-driving mode

![Accident activity](http://i.imgur.com/2DSR6eJ.png)

![Accident directions](http://i.imgur.com/X0ADYx4.png)

![Accident steps](http://i.imgur.com/HbGDqcx.png)

Accident database scheme

![Accident scheme](http://i.imgur.com/D3plWl1.png)

## settings
This activity to control some app settings like car temperature sensor unit,
the map viewing angles, Edit your playlist video ...etc.
Press any setting to start edit it to your prefered preference

![Settings](http://i.imgur.com/k3J7MgF.png)

![One Setting](http://i.imgur.com/LpRIZg6.png)

## Help
It's activity to help you use the app. some pictures details

![Help](http://i.imgur.com/wi0kZV5.png)

## About
This some information about the app project developers

![Help](http://i.imgur.com/JSrKtCh.png)

## Car advice assistant
This a notification system that send you a advice notification to help you take car for your car and have save drive. you can enable or disabled it form the settings

![Car advice assistant](http://i.imgur.com/iy5bAqQ.png)

## Warning system
This also a notification system to warn you form some car events like high speed,
temperature ..etc.

![Warnings](http://i.imgur.com/SsWNMeq.png)

## Account
This used to edit your account details like photo , name , location , password ..etc.
All the user data is also saved offline to access them of there is no internet this including the user photo also. Press any item you want to edit and it will open the editor activity for the user. You can verify your email if it's not verified by click on it and it will send a verification email to you then sign out and sign in again

![Account](http://i.imgur.com/xvzu7ev.png)

Edit password

![Edit password](http://i.imgur.com/3TmTSqU.png)

Edit Phone number
* After your edit your phone number you have to verify it to continue use the app

![Edit phone number](http://i.imgur.com/DwjTmPc.png)

Edit location

![Edit location](http://i.imgur.com/ZVcnV9h.png)

Edit name

![Edit name](http://i.imgur.com/jKtjeVN.png)

Verify email

![Email not verified](http://i.imgur.com/3z12nJC.png)

Verification email

![Verification email](http://i.imgur.com/5HVXh2U.png)


## License

Copyright (c) 2017 Ahmed-Abdelmeged.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
