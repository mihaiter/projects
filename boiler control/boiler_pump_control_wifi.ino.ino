/*
    This sketch establishes a TCP connection to a "quote of the day" service.
    It sends a "hello" message, and then prints received data.
*/

#include <ESP8266WiFi.h>

#ifndef STASSID
//#define STASSID "your-ssid"
//#define STAPSK  "your-password"
#define STASSID "Orange-DGT7-2.4G"
#define STAPSK  "2n26VYBA"
#endif
#define vref 3.3
#define beta 4200
#define rf 10000
#define r0 10000
#define t0 297.15
#define n 1000

const char* ssid     = STASSID;
const char* password = STAPSK;
WiFiClient client;
const char* host = "192.168.100.5";
const uint16_t port = 1235;
const IPAddress server(192, 168, 100, 5);
boolean closed=true;
boolean pump1state=false,pump2state=false;


void setup() {
  Serial.begin(115200);

  // We start by connecting to a WiFi network

  pinMode(D7,OUTPUT);
  pinMode(D6,OUTPUT);
  digitalWrite(D7,LOW);
  digitalWrite(D7,LOW);
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  WiFiClient client;
  closed=true;
  connect();
  delay(500);
//  testdata();
}
void connect() {

  Serial.print("connecting to ");
  Serial.print(server);
  Serial.print(':');
  Serial.println(port);
  boolean flag = true;
  while (flag) {
    if (!client.connect(server, port)) {
      Serial.println("connection failed");
      delay(3000);
      //       return;
    }
    else flag = false;
    Serial.println("connected");
    
  }
//   testdata();
}
float readtemp(){
   float ad;
  float v; 
  float r;
  float t;
  float x;
  // set the cursor to column 0, line 1
  // (note: line 1 is the second row, since counting begins with 0):
  //lcd.setCursor(0, 1);
  // print the number of seconds since reset:
  // lcd.print(millis()/1000);
  x=adc();
  v=tensiune(x);
  r=rezistenta(v);
  t= temperatura(r)+273,15;  
  return(t*(-1));
  }
  float tensiune(float adc)
{
  return (adc*vref)/1024;

}
float rezistenta(float tensiune)
{
  return tensiune*rf/(vref-tensiune); 
}
float temperatura(float r){
  return beta/log(r/r0*exp(-beta/t0));

}
float adc(){
  float adt=0;
  for(int i=0;i<n;i++)
  { 
    adt+=analogRead(A0);
  }
  return adt/n;
}


void testdata() {
  if (client.connected()) {
    for(int i =0;i<3;i++){
    Serial.println("sending message");

    client.println("hello from ESP8266");
    delay(300);
    }

  }
}
void readdata() {
  String mesaj = "";

  while (client.available()) {
    char ch = static_cast<char>(client.read());
    mesaj += ch;
  }
    if (mesaj != "") {
//      client.
      mesaj.trim();
      Serial.println("printing message :" + mesaj);
   
    if(mesaj.equalsIgnoreCase("close")||mesaj=="close"){Serial.println("close received . Closing connection ");client.stop();closed=false;}
    if(mesaj.equalsIgnoreCase("temperatura")){Serial.println("returning temperature");String temp=(String)readtemp();    client.println(temp);}
    if(mesaj.equalsIgnoreCase("pompa1")){Serial.println("pompa 1");   pump1state=!pump1state;Serial.println(pump1state);
    if(pump1state){
    digitalWrite(D7,HIGH);
    }else digitalWrite(D7,LOW);
    }
    if(mesaj.equalsIgnoreCase("pompa2")){Serial.println("pompa 2");   pump2state=!pump2state;Serial.println(pump2state);
    if(pump2state){
    digitalWrite(D6,HIGH);
    }else digitalWrite(D6,LOW);
    }
 }
  
}
void loop() {
  if (!client.connected()&&closed) {
   Serial.println("connecting again"); connect();
  }
  else {
//  testdata();
 readdata();
// Serial.println("reading data");
  }
  delay(500);

 
}
