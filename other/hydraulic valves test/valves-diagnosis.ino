#include<UTFT.h>
int out=14;
int i,j,k,l;
float diferenta;
String dif,ind1,ind2,ma1,ma2;

#define vref 5

volatile uint16_t indice1=0;
volatile uint16_t indice2=0;
volatile uint16_t indice3=0;
volatile float minim=1024;
volatile float max1=0;
volatile float max2=0;
volatile float tensfilt[322];
volatile float tens[322];
volatile uint16_t timps[322];
bool ok=0;
extern uint8_t SmallFont[];
UTFT mlcd(ILI9325D_16,38,39,40,41);

void setup() {
 Serial.begin(9600);
  pinMode(out ,OUTPUT);
  pinMode(12 ,OUTPUT);
  pinMode(8 ,INPUT_PULLUP);
 // pinMode(9 ,INPUT_PULLUP);
  // put your setup code here, to run once:
  mlcd.InitLCD();
  mlcd.setFont(SmallFont);
  mlcd.clrScr();
  mlcd.setColor(200,0,0);
mlcd.clrScr();
//mlcd.print("              main code here      ,:",5,5);
//delay(2000);
mlcd.print( "start  ",10,10);
digitalWrite(12,0);
digitalWrite(out,0);
}

void drawgraph(float max1,float max2,float minim,uint16_t indice1,uint16_t indice2,uint16_t indice3){
      
}

void drawresults(uint16_t indice1,uint16_t indice2,float max1,float max2){
       
  }
float tensiune(uint16_t adc)
{
  return (adc*vref)/1024;

}
void loop() {
if(digitalRead(8)==0)
  { ok=0;
    for(i=0;i<320;i++)
      { tens[i]=analogRead(A2);
        timps[i]=millis();
        digitalWrite(out,1);
      }
      digitalWrite(out,0);
  }
  if(ok==0)
  { ok=1;
   // drawgraph( max1, max2, minim, indice1,indice2, indice3);
   // drawresults(indice1,indice2,max1,max2); 
    mlcd.clrScr();
    mlcd.setColor(0,200,0);
    mlcd.drawLine(0,200,315,200);
    mlcd.drawLine(0,0,0,200);
    mlcd.setColor(200,0,0);
  
  for(i=0;i<320;i++)
  {  
    if(max1<=tens[i]){ max1=tens[i];indice1=i;}
    mlcd.drawPixel(i,1024-((tens[i]+tens[i-1]+tens[i+1])/3));
    //tensfilt[i]+=((tens[i]+tens[i-1]+tens[i+1])/3);
   // mlcd.drawPixel(i,tensfilt[i]);
    }
    k=indice1;
    do{
      
        if((minim>=tens[i])){minim=tens[k];indice3=i;}
    k++;
    }while(tens[k]<(tens[k+2]+tens[k+1]+tens[k+3])/3);
    
   /* for(i=indice1;i<=319;i++)
    {
           
    }
    */
     for(i=indice1+50;i<=319;i++)
   
 
    {
      if(max2<=tens[i]){max2=tens[i];indice2=i;}     
     
     }

        mlcd.setColor(0,0,200);
        mlcd.drawLine(indice1,200,indice1,0);
        mlcd.drawLine(indice2,200,indice2,0);
        diferenta=timps[indice2]-timps[indice1];
        dif=(String)diferenta;
        ind1=(String)(indice1);
        ind2=(String)(indice2);
        ma1=(String)tensiune(max1);
        ma2=(String)tensiune(max2);
        mlcd.setColor(0,200,200);
        mlcd.print ("interval =",0,210);
        mlcd.print(dif,80,210);
        mlcd.print ("maxim 1 =",0,220);
        mlcd.print(ma1,80,220);
        mlcd.print ("maxim 2 =",120,220);
        mlcd.print(ma2,200,220);
  
   
   }
}

