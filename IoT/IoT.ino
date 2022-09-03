#include <Arduino.h> 
#if defined(ESP32) 
#include <WiFi.h> 
#elif defined(ESP8266)
#include <ESP8266WiFi.h> 
#endif
#include <Firebase_ESP_Client.h>
#include <ESP8266WiFi.h> 
#include "addons/TokenHelper.h" 
#include "addons/RTDBHelper.h"
#define FIREBASE_HOST <FireBase Host>
#define FIREBASE_AUTH <Firebase Auth>

#define API_KEY <FireBase API>

#define DATABASE_URL <Firebase URL>
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
unsigned long sendDataPrevMillis = 0; 
bool signupOK = false;
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h> // http web access library
#include <WiFiClient.h>
#include "DHT.h"
DHT dht;
#define dht_dpin D3 
#include <ArduinoJson.h>
#include <ESP_Mail_Client.h> 
#define SMTP_HOST "smtp.gmail.com" 
#define SMTP_PORT 465
#define AUTHOR_EMAIL <Author Mail ID> 
#define AUTHOR_PASSWORD <Author Mail Password>
#define RECIPIENT_EMAIL <Recipient Mail ID> 
SMTPSession smtp;
unsigned long lastTime = 0; 
unsigned long timerDelay = 600000;
void smtpCallback(SMTP_Status status); 
const char *ssid = <WiFi Name>;
const char *password = <Password>; 
WiFiClient wificlient;
String Location = "Indore, IN";
String API_Key = <Weather API>; 
int count=0;
int temp=0; 
int check=0;
int ledPin = D0; // GPIO13 
int pump=D5;
WiFiServer server(80);
void setup(void)
{
dht.setup(dht_dpin);
Serial.begin(115200); 
delay(1000);
pinMode(ledPin, OUTPUT); 
digitalWrite(ledPin, LOW);
pinMode(D6, OUTPUT); 
digitalWrite(D6, HIGH); 
pinMode(pump, OUTPUT); 
digitalWrite(pump, LOW); 
WiFi.begin(ssid, password);
Serial.print("Connecting.");
while ( WiFi.status() != WL_CONNECTED )
{
delay(500); 
Serial.print(".");
}
Serial.println("connected");
server.begin();
config.api_key = API_KEY;
config.database_url = DATABASE_URL;
if (Firebase.signUp(&config, &auth, "", ""))
{
  Serial.println("ok");
signupOK = true;
}
else{
Serial.printf("%s\n", config.signer.signupError.message.c_str());
}
config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
Firebase.begin(&config, &auth); 
Firebase.reconnectWiFi(true);
}
void loop()
{
light(); 
Serial.println(check); 
if(check==0)
{
digitalWrite(ledPin, LOW);
}
if(check==1)
{
digitalWrite(ledPin, HIGH);
}
if ((millis() - lastTime) > timerDelay || lastTime==0){
if (WiFi.status() == WL_CONNECTED) //Check WiFi connection status
{
HTTPClient http; //Declare an object of class HTTPClient
http.begin(wificlient,"http://api.openweathermap.org/data/2.5/weather?q=" + Location + "&APPID=" + API_Key); // !!
int httpCode = http.GET(); // send the request
if (httpCode > 0) // check the returning code
{
String payload = http.getString(); //Get the request response payload
DynamicJsonBuffer jsonBuffer(512);
JsonObject& root = jsonBuffer.parseObject(payload); 
if (!root.success()) {
Serial.println(F("Parsing failed!")); 
return;
}
temp = (int)(root["main"]["temp"]) - 273.15;
}
http.end(); //Close connection
}
lastTime = millis();
}
Serial.printf("Temperature = %d°C\r\n", temp); 
float tempsen = dht.getTemperature();
Serial.print("temperature sen = "); 
Serial.println(tempsen);
int gas=analogRead(A0); 
Serial.print("  Gas: "); 
Serial.println(gas); 
pri(tempsen);
if(tempsen-temp>10 && gas>100)
{
  count++;
}
if(count<=10 && count >0)
{
  mail();
digitalWrite(D6, HIGH); 
digitalWrite(pump, HIGH);
}
else
{
  count=0;
digitalWrite(D6, LOW); 
digitalWrite(pump, LOW);
}
delay(3000);
}
void smtpCallback(SMTP_Status status)
{
if (status.success())
{
ESP_MAIL_PRINTF("Message sent success: %d\n", status.completedCount()); 
ESP_MAIL_PRINTF("Message sent failled: %d\n", status.failedCount());
struct tm dt;



for (size_t i = 0; i < smtp.sendingResult.size(); i++)

{

SMTP_Result result = smtp.sendingResult.getItem(i); 
time_t ts = (time_t)result.timestamp; 
localtime_r(&ts, &dt);


ESP_MAIL_PRINTF("Message No: %d\n", i + 1); 
ESP_MAIL_PRINTF("Status: %s\n", result.completed ? "success" : "failed");
ESP_MAIL_PRINTF("Date/Time: %d/%d/%d %d:%d:%d\n", dt.tm_year + 1900, dt.tm_mon + 1, dt.tm_mday, dt.tm_hour, dt.tm_min, dt.tm_sec);
 

ESP_MAIL_PRINTF("Recipient: %s\n", result.recipients.c_str()); 
ESP_MAIL_PRINTF("Subject: %s\n", result.subject.c_str());
}

Serial.println("  \n");

smtp.sendingResult.clear();

}

}



void mail()

{

smtp.debug(1);



smtp.callback(smtpCallback);



ESP_Mail_Session session;



session.server.host_name = SMTP_HOST;
 

session.server.port = SMTP_PORT; 
session.login.email = AUTHOR_EMAIL; 
session.login.password = AUTHOR_PASSWORD; 
session.login.user_domain = F("mydomain.net");
session.time.ntp_server = F("pool.ntp.org,time.nist.gov"); 
session.time.gmt_offset = 3;
session.time.day_light_offset = 0; 
SMTP_Message message;


message.sender.name = F("ESP Mail"); 
message.sender.email = AUTHOR_EMAIL; 
message.subject = F("Warning!!!!!!");
message.addRecipient("User", "yashjaiswal88542@gmail.com");

String htmlMsg = "<div style=\"color:#2f4468;\"><h1>Looks like your house is on fire</h1><p>- Do something ASAP</p></div>"; 
message.html.content = htmlMsg.c_str();
message.html.content = htmlMsg.c_str(); 
message.text.charSet = "us-ascii";
 

message.html.transfer_encoding = Content_Transfer_Encoding::enc_7bit;




message.text.charSet = F("us-ascii");

message.text.transfer_encoding = Content_Transfer_Encoding::enc_7bit;



message.priority = esp_mail_smtp_priority::esp_mail_smtp_priority_low;



message.addHeader(F("Message-ID: <abcde.fghij@gmail.com>")); 
if (!smtp.connect(&session))
return;

if (!MailClient.sendMail(&smtp, &message)) Serial.println("Error sending Email, " + smtp.errorReason());
ESP_MAIL_PRINTF("Free Heap: %d\n", MailClient.getFreeHeap());

}

void light()

{
 

WiFiClient client = server.available(); 
if (!client) {
return;

}

while(!client.available())
{
  delay(1);
}

if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 0 || sendDataPrevMillis == 0))
{ sendDataPrevMillis = millis();
if (Firebase.RTDB.getInt(&fbdo, "check")) 
{ if (fbdo.dataType() == "int") {
check = fbdo.intData();

Serial.println(check);



}

else 
{
  Serial.println(fbdo.errorReason());
 

}

}

}

}

void pri(int temp)

{

WiFiClient client = server.available(); 
if (!client) {
return;

}

// Wait until the client sends some data Serial.println("new client");
while (! client.available())

{

delay (1);

}

// Read the first line of the request
 

String req = client.readStringUntil('\r');

// Serial.println(req); client.flush();
// Match the request

// Return the response 
client.println("HTTP/1.1 200 OK"); 
client.println("Content-Type: text/html"); 
client.println("Connection: close"); 
client.println(""); 
client.println("<!DOCTYPE HTML>"); 
client.println("<HTML>"); 
client.println(temp);
client.println("<br />"); 
client.println("</html>");




}
