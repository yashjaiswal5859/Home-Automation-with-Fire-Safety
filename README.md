# Home-Automation-with-Fire-Safety
An IoT based project in which we can control our home appliances from all over the world by just saying to android application. And it can detect fire in our home and notify us. <br/>
# Hardware used:<br/>
1.  Node MCU Micro-Controller<br/>
2.  DHT-11 Temperature Sensor<br/>
3.  MQ2 Gas Sensor<br/>
4.  5V Relay<br/>
5.  Bulb<br/>
6.  Wires<br/>
# Software Used:<br/>
1.  Arduino IDE<br/>
2.  Android Studio<br/>
# API:<br/>
1.  Firebase API<br/>
2.  Weather API<br/>
## **Youtube Video Link:** https://youtu.be/JsMkileXSw0<br/>
Our project is based on Android and IoT. The aim of our project is to convert home into smart home. We have designed an Android app named NATASHA which work as a voice assistant and that app is connected to Google Firebase via Firebase API.<br/> And that firebase is connected to Node MCU(Micro-controller) via API. So whenever we say "Turn on light", so that message will transfer to firebase and then Node MCU will fetch that message and then it will close the circuit using 5V Relay. So after that, Bulb will start glowing.  And the same is for turning off lights <br/>                                                                                                                        
Apart from this, if we talk about fire safety, then we have used MQ2 gas sensor, DHT11 temperature sensor and weather API. So whenever the difference between the temperature sensor's reading and the Weather API data is greater than 30 and the gas sensor's reading is more than 500, the alarm will start sounding and the node MCU will send the a mail to owners mail ID using the SMTP protocol.
