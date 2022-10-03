#include <SPI.h>
#include <Ethernet.h>
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};
EthernetServer server(1984);
byte instructionBuffer[3];
byte dataBuffer[16];
int aPin[]={A0,A1,A2,A3,A4,A5};
EthernetClient client;
int index;
int read;
int value;
int up;
int down;
void setup() {
  // You can use Ethernet.init(pin) to configure the CS pin
  //Ethernet.init(10);  // Most Arduino shields
  //Ethernet.init(5);   // MKR ETH Shield
  //Ethernet.init(0);   // Teensy 2.0
  //Ethernet.init(20);  // Teensy++ 2.0
  //Ethernet.init(15);  // ESP8266 with Adafruit FeatherWing Ethernet
  //Ethernet.init(33);  // ESP32 with Adafruit FeatherWing Ethernet
  for(index=0;index<6;index++)
    pinMode(aPin[index], INPUT);

  // Open serial communications and wait for port to open:
  //Serial.begin(9600);
  //while (!Serial) {
  //  ; // wait for serial port to connect. Needed for native USB port only
  //}
  // start the Ethernet connection:
  //Serial.println("Trying to get an IP address using DHCP");
  if (Ethernet.begin(mac) == 0) {
    //Serial.println("Failed to configure Ethernet using DHCP");
    // Check for Ethernet hardware present
    if (Ethernet.hardwareStatus() == EthernetNoHardware) {
      //Serial.println("Ethernet shield was not found.  Sorry, can't run without hardware. :(");
      while (true) {
        delay(1); // do nothing, no point running without Ethernet hardware
      }
    }
    if (Ethernet.linkStatus() == LinkOFF) {
      //Serial.println("Ethernet cable is not connected.");
    }
  }
  // print your local IP address:
  //Serial.print("My IP address: ");
  //Serial.println(Ethernet.localIP());
  // start listening for clients
  server.begin();
}

void loop() {
  if(!client||client.connected()==0){
    // wait for a new client:
    //Serial.println("wait for new connection");
    delay(500);
    client = server.available();
    read=0;
  }
  else {
    if(client.available()==0)
        return;    
    //Serial.println(client.available(),DEC);    
    // read the bytes incoming from the client:
    instructionBuffer[read] = client.read();
    //Serial.println(instructionBuffer[read],DEC);
    read++;
    if(read==3)
    {
      //蒐集到了一個完整的指令, 來看看要幹甚麼
      switch(instructionBuffer[0])
      {
        case 1:
            //來下達數位輸出指令
            pinMode(instructionBuffer[1], OUTPUT);
            digitalWrite(instructionBuffer[1], instructionBuffer[2]);            
            break;
        case 2:
            //來下達pwm輸出指令
            pinMode(instructionBuffer[1], OUTPUT);
            analogWrite(instructionBuffer[1], instructionBuffer[2]);
            break;
        case 3:
            //來拉全部腳位資料
            dataBuffer[0]=3;
            for(index=0;index<6;index++)
            {
              value = analogRead(aPin[index]);
              up=(value>>8)&0xff;
              down=value&0xff;
              dataBuffer[(index*2)+1]=up;
              dataBuffer[(index*2)+2]=down;
            }
            dataBuffer[13]=PIND;
            dataBuffer[14]=PINB;
            dataBuffer[15]=PINC;
            client.write(dataBuffer,16);
            break;
      }
      read=0;
    }
    //Serial.print(thisChar);
    //Ethernet.maintain();//這個function會告訴dhcp可不可以更新Ip
  }
}
