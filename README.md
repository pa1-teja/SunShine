# SunShine
Developed an Android weather forecast app as part of the Nanodegree course. Which displays 14 days of weather forecast information. The app displays weather forecast information such as High and low temperatures, humidity, wind speed etc. on Android Phones and Tablets.

## Functionality

The app displays the High and Low temperatures, along with type of weather on each day, for 14 days on Main Screen, when the app is launched on the Mobile device.
<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/Main_Screen.png" width="200" height = "350">
<br/>
* Users can set their location in the settings screen which can be opened by tapping on the **overflow options menu** --> **settings**. 
<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/overflow%20menu.png" width="200" height = "350">
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/settings_menu.png" width="200" height = "350">
<br/>
* The **Settings** screen allows the user to change the Location, temperature metrics, icon packs and weather notifications.

<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/Settings_screen.png" width="200" height = "350">
<br/>

* User can set a location by tapping on the "Location" in the Settings screen which will popup a text dialog where the user can enter name of the location. 

<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/Settings_screen_loc.png" width="200" height = "350">
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/loc_text.png" width="200" height = "350">
<br/>
or
* the user can simply tap on the Location icon on the right, which will GPS to locate the device, and use those coordinates to get the weather information of that place.

<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/Settings_screen_loc_icon_hi.png" width="200" height = "350">
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/loc_api.png" width="200" height = "350">
<br/>
* The seetings screen allows users to switch between metric and imperial temperature units.
<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/temp_settings.png" width="200" height = "350">
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/temp_dialog.png" width="200" height = "350">
<br/>
* It also allows its users to switch between the icon packs that they can use in place of weather images in the app.
<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/icon_setting.png" width="200" height = "350">
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/icon_dialog.png" width="200" height = "350">
<br/>
* Users can also enable or disable the app notifications to show up in the device's notification panel.
<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/notification_bool.png" width="200" height = "350">
<br/>

Coming back to the Main Screen from the Settings screen. The user will be navigated to the Detail screen where the app dislays a detail weather information such wind speed, humidity, pressure along with the date, High and Low temperatures of that particular day, when the user taps on any list item in the Main Screen.
<br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/Deatail_details.png" width="200" height = "350">
<br/>
 Users can also share the weather information through any messaging application from this by tapping the Share icon in the details screen.
 <br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/Detail_Share.png" width="200" height = "350">
<br/>

 The app also supports all the Android tablets where the users have all the 14 days of temperature data on the left and the user can tap on any of the list items to see the detailed information on the right side of the screen.
  <br/>
<img src="https://github.com/pa1-teja/SunShine/blob/master/app/src/main/res/drawable/tablet_screen.png" width="500" height = "350">
<br/>

The app also provides support to the round and square Android wear devices. Users can stay informed about the High and Low temperatures along with weather image which describes the type of weather on that particular day, on the wearable device's watch face. <br/>
Round watch  &nbsp &nbsp Square Watch
 <br/>
 
<img src="https://github.com/pa1-teja/SunShine/blob/master/sunshinewear/src/main/res/drawable-nodpi/round%20watch%20face.png" width="220" height = "270">

<img src="https://github.com/pa1-teja/SunShine/blob/master/sunshinewear/src/main/res/drawable-nodpi/square%20watch%20face.png" width="220" height = "270">
<br/>
 
 ## Project Details
 The app checks for the given location and based on that, the app makes an API call when the app is launched. The app fetches movies details from the [OpenWeatherMap API](https://openweathermap.org/appid) and store those details in the SQLite Database using COntent Providers.
 
 ### Dependencies used in the Project
 
 Below are the dependencies used in the project for mobile : 
 
`com.github.bumptech.glide:glide:3.5.2` <br/>
`com.android.support:support-annotations:23.2.0` <br/>
`com.android.support:gridlayout-v7:23.2.0` <br/>
`com.android.support:cardview-v7:23.2.0` <br/>
`com.android.support:appcompat-v7:23.2.0` <br/>
`com.android.support:design:23.2.0` <br/>
`com.android.support:recyclerview-v7:23.2.0` <br/>
`com.google.android.apps.muzei:muzei-api:2.0` <br/>
`com.google.android.gms:play-services-location:7.5.0` <br/>
`com.google.android.apps.muzei:muzei-api:2.0` <br/>
`com.google.android.gms:play-services:10.0.1` <br/>
`com.google.android.gms:play-services-wearable:8.3.0` <br/>
 
 Below are the dependencies used in the project for wearable :
 
`com.google.android.support:wearable:2.0.0-alpha3` <br/>
`com.google.android.gms:play-services-wearable:8.3.0` <br/>
 `com.android.support:appcompat-v7:25.0.0` <br/>
