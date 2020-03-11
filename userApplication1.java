//import ithakimodem.Modem;
import java.io.*;
import javax.comm.*;
import java.lang.*;

public class userApplication {
	
	public static void main(String[] param) {
		(new userApplication()).demo();
	}
	
	public void demo() {
		int k;
		Modem modem;
		modem=new Modem();
		modem.setSpeed(5000);
		modem.setTimeout(50000);
		modem.open("ithaki");
		
		char[] endlessbreak = new char[4];
		char[] nocarriercheck = new char[10];
		for (;;) {
			try {
				k=modem.read();
				if (k==-1) break;
				System.out.print((char)k);
				
				nocarriercheck[0]=nocarriercheck[1];
				nocarriercheck[1]=nocarriercheck[2];
				nocarriercheck[2]=nocarriercheck[3];
				nocarriercheck[3]=nocarriercheck[4];
				nocarriercheck[4]=nocarriercheck[5];
				nocarriercheck[5]=nocarriercheck[6];
				nocarriercheck[6]=nocarriercheck[7];
				nocarriercheck[7]=nocarriercheck[8];
				nocarriercheck[8]=nocarriercheck[9];
				nocarriercheck[9]=(char) k;
				if(nocarriercheck[0]=='N' && nocarriercheck[1]=='O' && nocarriercheck[2]==' ' && nocarriercheck[3]=='C' && nocarriercheck[4]=='A' && nocarriercheck[5]=='R' && nocarriercheck[6]=='R' && nocarriercheck[7]=='I' && nocarriercheck[8]=='E' && nocarriercheck[9]=='R'){
					System.out.println("No carrier");
					modem.close();
					break;
				}
				
				endlessbreak[0]=endlessbreak[1];
				endlessbreak[1]=endlessbreak[2];
				endlessbreak[2]=endlessbreak[3];
				endlessbreak[3]=(char) k;
				if(endlessbreak[0]=='\r' && endlessbreak[1]=='\n' && endlessbreak[2]=='\n' && endlessbreak[3]=='\n') break;
			} catch (Exception x) {
				break;
			}
		}
		
		/*    //------------------------------------------------------
		//Apla paketa kai latency
		try{
			PrintWriter writer = new PrintWriter("D:\\auth\\ithaki\\latency.txt");
			long time1=0,time2=0,latency=0;
			for(int j=0;j<10000;j++){
				modem.write("E7160\r".getBytes());
				time1=System.currentTimeMillis();
				for(int i=0;i<35;i++){
					k=modem.read();
					if (k==-1) break;
					System.out.print((char)k);
//					writer.print((char)k);		//mporw me "//" na dialegw th na grafw se console kai arxeio
				}
				time2=System.currentTimeMillis();
				latency=time2-time1;
				System.out.println(" "+latency);
				writer.println(" "+latency);
			}
			writer.close();
		}catch(Exception x){
			System.out.println(x);
		}
		System.out.println("Got the requested packets.");
		
		//Egnatia xwris sfalmata
		try{
			File file = new File("D:\\auth\\ithaki\\goodimg.jpg");
			FileOutputStream fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			modem.write("M4293\r".getBytes());
			int[] imgchecker =new int[2];
			for(;;){
				k=modem.read();
				if (k==-1) break;
				imgchecker[0]=imgchecker[1];
				imgchecker[1]= k;
				if(imgchecker[0]==0xFF && imgchecker[1]==0xD8) break;		//3ekina h eggrafh me  to break
			}
			fop.write((byte)0xFF);
			fop.write((byte)0xD8);
			for(;;) {
				k=modem.read();
				if (k==-1) break;
				fop.write((byte)k);
				imgchecker[0]=imgchecker[1];
				imgchecker[1]= k;
				if(imgchecker[0]==0xFF && imgchecker[1]==0xD9) break;
			}
			fop.flush();
			fop.close();
		}catch(Exception x){
			System.out.println(x);
		}
		System.out.println("Got the good one!");
		
		//Egnatia me sfalmata
		try{
			File file = new File("D:\\auth\\ithaki\\badimg.jpg");
			FileOutputStream fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			modem.write("G0995\r".getBytes());
			int[] imgchecker =new int[2];
			for(;;){
				k=modem.read();
				if (k==-1) break;
				imgchecker[0]=imgchecker[1];
				imgchecker[1]= k;
				if(imgchecker[0]==0xFF && imgchecker[1]==0xD8) break;		//3ekina h eggrafh me  to break
			}
			fop.write((byte)0xFF);
			fop.write((byte)0xD8);
			for(;;) {
				k=modem.read();
				if (k==-1) break;
				fop.write((byte)k);
				imgchecker[0]=imgchecker[1];
				imgchecker[1]= k;
				if(imgchecker[0]==0xFF && imgchecker[1]==0xD9) break;
			}
			fop.flush();
			fop.close();
		}catch(Exception x){
			System.out.println(x);
		}
		System.out.println("Got the bad one!");
		
		
		//Stigmata gps 
		try{
			PrintWriter writer = new PrintWriter("D:\\auth\\ithaki\\gpsroute.txt");
			for(int j=0;j<1;j++){	//to afhsa se periptwsh poy 8elw na parw polla random gps stigmata -> vazw sto j posa 8elw kai vgazw to R=XPPPPLL
				modem.write("P3584R=1010085\r".getBytes());
				for(int i=0;i<26;i++){			//epeidh to START text exei NG\r\n sto telos to vazw manualy (24 xarakthres + \r\n), alliws xala to endless break
					k=modem.read();
					if (k==-1) break;
					System.out.print((char)k);
					writer.print((char)k);
				}
				for(;;){
					k=modem.read();
					if (k==-1) break;
					System.out.print((char)k);
					writer.print((char)k);
					endlessbreak[0]=endlessbreak[1];
					endlessbreak[1]=endlessbreak[2];
					endlessbreak[2]=endlessbreak[3];
					endlessbreak[3]=(char)k;
					if(endlessbreak[0]=='N' && endlessbreak[1]=='G' && endlessbreak[2]=='\r' && endlessbreak[3]=='\n') break;
				}
				System.out.println();
				writer.println();
			}
			writer.close();
		}catch(Exception x){
			System.out.println(x);
		}
		System.out.println("Wrote the gps coords!");
		
		*/    //------------------------------------------------------
		
		//Eikona diadromhs gps, panta meta ta alla dioti xreiazetai to prohgoymeno
		try{
			File file = new File("C:\\Users\\Bletsos\\Desktop\\trace.jpeg");
			FileOutputStream fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			modem.write("P3584T=225731403739T=225728403742T=225728403743T=225734403744T=225732403747T=225733403745T=2257344037744\r".getBytes());
			int[] imgchecker =new int[2];
			for(;;){
				k=modem.read();
				if (k==-1) break;
				imgchecker[0]=imgchecker[1];
				imgchecker[1]= k;
				if(imgchecker[0]==0xFF && imgchecker[1]==0xD8) break;
			}
			fop.write((byte)0xFF);
			fop.write((byte)0xD8);
			for(;;) {
				k=modem.read();
				if (k==-1) break;
				fop.write((byte)k);
				imgchecker[0]=imgchecker[1];
				imgchecker[1]= k;
				if(imgchecker[0]==0xFF && imgchecker[1]==0xD9) break;
			}
			fop.flush();
			fop.close();
		}catch(Exception x){
			System.out.println(x);
		}
		System.out.println("Got the GPS points!");
		
		/*    //------------------------------------------------------
		//Paketa me sfalmata kai epanalh4eis
		try{
			PrintWriter writer = new PrintWriter("D:\\auth\\ithaki\\errlatency.txt");
			long time1=0,time2=0,latency=0;
			int packcheck = 0, XORproduct=0, goodresendcheck=0;
			char[] fcs = new char[3];
			for(int j=0;j<10000;j++){
				goodresendcheck=0;
				modem.write("Q6353\r".getBytes());
				time1=System.currentTimeMillis();
				for(int i=0;i<58;i++){
					k=modem.read();
					if (k==-1) break;
					System.out.print((char)k);
//					writer.print((char)k);
					if(i==31) XORproduct=k;
					if(i>=32 && i<=46){
						XORproduct=XORproduct^k;			//kanw diadoxika ta XOR
					}
					switch(i){								//apo8hkeuw ta 3 4hfia tou frame check sequence
					case 49:
						fcs[0]=(char)k;
					case 50:
						fcs[1]=(char)k;
					case 51:
						fcs[2]=(char)k;
					}
				}
				packcheck=Integer.valueOf(String.valueOf(fcs[0]) + String.valueOf(fcs[1]) + String.valueOf(fcs[2]));		//enopoiw to frame check sequence 3xint -> string -> int
				if(XORproduct==packcheck){																					//ola kala
					System.out.print(" OK  ");
//					writer.print(" OK  ");
					System.out.print(" XORproduct: " + XORproduct);
//					writer.print(" XORproduct: " + XORproduct);
					time2=System.currentTimeMillis();
					latency=time2-time1;
					System.out.println(" latency: "+latency);
//					writer.print(" latency: ");
					writer.println(latency);
				}
				if(XORproduct!=packcheck){																					//sfalma
					System.out.print(" NOPE");
//					writer.print(" NOPE");
					System.out.println(" XORproduct: " + XORproduct);
//					writer.println(" XORproduct: " + XORproduct);
					while(goodresendcheck!=1){																				//mexri na er8ei ena kalo 3anazhtaw
						modem.write("R8341\r".getBytes());
						for(int i=0;i<58;i++){
							k=modem.read();
							if (k==-1) break;
							System.out.print((char)k);
//							writer.print((char)k);
							if(i==31) XORproduct=k;
							if(i>=32 && i<=46){
								XORproduct=XORproduct^k;
							}
							switch(i){
							case 49:
								fcs[0]=(char)k;
							case 50:
								fcs[1]=(char)k;
							case 51:
								fcs[2]=(char)k;
							}
						}
						packcheck=Integer.valueOf(String.valueOf(fcs[0]) + String.valueOf(fcs[1]) + String.valueOf(fcs[2]));
						if(XORproduct==packcheck){
							System.out.print(" Good resend");
//							writer.print(" Good resend");
							goodresendcheck=1;															//hr8e kalo, ara den 3anatrexw thn while
							time2=System.currentTimeMillis();
							latency=time2-time1;
							System.out.println(" latency: "+latency);
//							writer.print(" latency: ");
							writer.println(latency);
						}
						if(XORproduct!=packcheck){														// ekane resend me sfalma, pame pali...
							System.out.println(" Bad resend, AGAIN!");
//							writer.println(" Bad resend, AGAIN!");
						}
					}
				}
			}
			writer.close();
		}catch(Exception x){
			System.out.println(x);
		}
		
		*/    //------------------------------------------------------
		System.out.println("All uncorrupted packets accounted for!");
		
		System.out.println("***DONE!***");
		
		modem.close();
	}
}