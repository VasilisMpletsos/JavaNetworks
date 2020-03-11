import java.net.*; 
import java.util.*;
import java.io.*;
import javax.comm.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import javax.sound.sampled.*;



public class userApplication {
	
	static final int  loops = 999;
	static final int  serverPort = 38022;
	static final int clientPort = 48022;
	boolean BufferFlag=false;
	private static InetAddress HostName;
	private static DatagramSocket Ssocket,Rsocket;
	static final String EchoRequest = "E6156";
	static final String ImageRequest = "M6038";
	static final String AudioRequest = "A2760F999";
	static final String AudioRequestAQ = "A2760AQF999";
	static final LinkedBlockingQueue<byte[]> buffer= new LinkedBlockingQueue<byte[]>();
	 Thread A;
	 Thread B;
	 
	 
	 public userApplication(int k) {
		 
		 if(k==0) {
				 A = new bufferT();
				 B = new playerT();
			 }
		 if(k==1) {
			 	A = new bufferAQ();
			 	B = new playerAQ();
		 }
		 
		 }
	
	public void join()
	{
		try{
			A.join();
			B.join();
		}catch(Exception d)
		{
			System.out.println("Error");
		}
	}
	
	public static void main(String arrg[])
	{
		System.out.println("Got into main");
		Latency();
		Image();
		Audio();
		userApplication k = new RoutuserApplicationing(0);
		k.join();
		Copter();
		Vehicle();
		userApplication k = new userApplication(1);
		k.join();
			
		
		System.out.println("End Running");
			
	}
	
	public static void Latency() 
	{
		
		
		try
		{
			Ssocket = new DatagramSocket();
			Ssocket.setSoTimeout(15000);
			System.out.println("Created Socket (Send)");
		}catch(Exception ex){
			System.out.print(ex);
		}
		
		try
		{
			Rsocket = new DatagramSocket(clientPort);
			Rsocket.setSoTimeout(15000);
			System.out.println("Created Socket (Receive)");
		}catch(Exception ex){
			System.out.print(ex);
		}
		
		// I have to do try when i create a connection 
		try
		{
			HostName = InetAddress.getByName("ithaki.eng.auth.gr");
			System.out.print("Got ip address = ");
			System.out.println(HostName);
		}catch(Exception e){
			System.out.print(e);
		}
		
		
		byte[] rxbuffer = new byte[2048];
		byte[] txbuffer = EchoRequest.getBytes();
		System.out.println(txbuffer);
		DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length,HostName,serverPort);
		System.out.println("Datagram to send is Established");
		DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
		System.out.println("Datagram to receive is Established");
		String Mess;
		
		try{
			//PrintWriter writer = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Session2\\LatencyE0000.txt");
			long time1=0,time2=0,latency=0;
			for(int i=0;i<5000;i++) 
			{
				time1=System.currentTimeMillis();
					try
					{
						Ssocket.send(p);
						//System.out.println("Packet sent");
					}catch(Exception ec){
						System.out.print(ec);
						System.out.println("Packet didnt sent");
					}
					
					
					try {
							Rsocket.receive(q);
							//System.out.println("Command Receive is executed");
							Mess = new String(rxbuffer,0,q.getLength());
							//System.out.println(Mess);
						} catch (Exception x) {
							System.out.print(x);
						}
					time2=System.currentTimeMillis();
					latency=time2-time1;
					System.out.println(" "+latency);
					//writer.println(" "+latency);
			}//writer.close();
		}catch(Exception x){
			System.out.println(x);
		}
		
		Ssocket.close();
		Rsocket.close();
		System.out.println("Echo Finished");
		
	}
	
	public static void Image()
	{
		try
		{
			Ssocket = new DatagramSocket();
			System.out.println("Created Socket (Send)");
		}catch(Exception ex){
			System.out.print(ex);
		}
		
		try
		{
			Rsocket = new DatagramSocket(clientPort);
			System.out.println("Created Socket (Receive)");
		}catch(Exception ex){
			System.out.print(ex);
		}
		
		try
		{
			HostName = InetAddress.getByName("ithaki.eng.auth.gr");
			System.out.print("Got ip address = ");
			System.out.println(HostName);
		}catch(Exception e){
			System.out.print(e);
		}
		
		byte[] rxbuffer = new byte[128];
		byte[] txbuffer = ImageRequest.getBytes();
		String s = new String(txbuffer);
		System.out.println(s);
		DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length,HostName,serverPort);
		System.out.println("Datagram to send is Established");
		DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
		System.out.println("Datagram to receive is Established");
		boolean flag2=false;
		
		
		
		try {
			Ssocket.send(p);
			
			FileOutputStream fop = new FileOutputStream("C:\\Users\\Bletsos\\Desktop\\image2.jpeg");
			//This is to find the start sequence
			//while(flag2==false)
			//{
			while(flag2==false) {
				Rsocket.receive(q);
				fop.write(rxbuffer);
				for(int i=0;i<rxbuffer.length-1;i++)
					{
						if(rxbuffer[i]==(byte)0xFF && rxbuffer[i+1]==(byte)0xD9)
						{
							System.out.println("Found Stop");
							flag2=true;
							System.out.println(String.format("0x%02X",rxbuffer[i]));
							System.out.println(String.format("0x%02X",rxbuffer[i+1]));
							break;
						}
					}
			
			}
			fop.flush();
			fop.close();
		}catch(Exception d)
		{
			
			System.out.println(d);
		}		
	}	
	
	
	
	public static void Copter(){
			int serverPort = 38048;
			byte[] hostIP = new byte[]{(byte)155,(byte)207,18,(byte)208};
			Socket socket=null;
			OutputStream out=null;
			

	//Initiallizing file transfer and message
		
			String message = "No message";

			

			try {
					PrintWriter writer = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Session2\\Copter2.txt");
					InetAddress hostAddress = InetAddress.getByAddress(hostIP);
					socket = new Socket (hostAddress, serverPort);			
					out = socket.getOutputStream();
					for(int j=150;j<200;j++){
						int f_level = j;
						int motors = j+25;
						String packetInfoToMove =  "AUTO FLIGHTLEVEL="+ f_level +" LMOTOR="+ motors +" RMOTOR="+ motors +" PILOT \r\n";
						out.write (packetInfoToMove.getBytes());
						message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
						writer.println(message);
						System.out.println(message);
						
					}
					writer.close();
					socket.close ();
				}catch (Exception exc){
					System.out.println(exc);
				}
			

		}
		
		
        
	public static void Vehicle(){
		
		int serverPort = 29078;
		byte[] hostIP = new byte[]{(byte)155,(byte)207,18,(byte)208};
		Socket socket=null;
		OutputStream out=null;
		

//Initiallizing file transfer and message
	
		String message = "No message";


		try {
				PrintWriter writer = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Java\\VehicleIntakeAirTemp.txt");
				PrintWriter writer2 = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Java\\VehicleThrottlePosition.txt");
				PrintWriter writer3 = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Java\\VehicleEngineRPM.txt");
				PrintWriter writer4 = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Java\\VehicleVehicleSpeed.txt");
				PrintWriter writer5 = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Java\\VehicleCoolantTemp.txt");
				InetAddress hostAddress = InetAddress.getByAddress(hostIP);
				socket = new Socket (hostAddress, serverPort);			
				out = socket.getOutputStream();
				
				for(int i=0;i<2000;i++){
					System.out.println(i);
				//----------------------------------Engine run time -----------------------------
				String Command =  "01 1F" + (char)13;
				out.write (Command.getBytes());
				message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
				int result=(Integer.valueOf(message.split(" ")[2],16)*256)+Integer.valueOf(message.split(" ")[3],16);
				//System.out.println(result + " sec (Engine Run Time)");
				//writer.println(result);
					
				
				
				
				//----------------------------------Intake air Temprature -----------------------------
				Command =  "01 05" + (char)13;
				out.write (Command.getBytes());
				message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
				//System.out.println(Integer.valueOf(message.split(" ")[2],16)-40 + " °C (Intake air temperature)");
				writer.println(Integer.valueOf(message.split(" ")[2],16)-40);
				
				
				
				
				//----------------------------------Throttle position -----------------------------
				Command =  "01 11" + (char)13;
				out.write (Command.getBytes());
				message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
				result=(Integer.valueOf(message.split(" ")[2],16)*100)/255;
				writer2.println(result);
				//System.out.println(result + " % (Throttle position)");
				
				
				//----------------------------------Engine RPM -----------------------------
				Command =  "01 0C" + (char)13;
				out.write (Command.getBytes());
				message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
				result=((Integer.valueOf(message.split(" ")[2],16)*256)+Integer.valueOf(message.split(" ")[3],16))/4;
				writer3.println(result);
				//System.out.println(result + " RPM  (Engine RPM)");
				
				
				
				//----------------------------------Vehicle speed -----------------------------
				Command =  "01 0D" + (char)13;
				out.write (Command.getBytes());
				message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
				result=Integer.valueOf(message.split(" ")[2],16);
				writer4.println(result);
				//System.out.println(result + " Km/h  (Vehicle speed)");
				
				
				
				//----------------------------------Coolant temperature -----------------------------
				Command =  "01 05" + (char)13;
				out.write (Command.getBytes());
				message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
				result=Integer.valueOf(message.split(" ")[2],16)-40;
				//System.out.println(result + " °C  (Coolant temperature)");
				writer5.println(result);
				}
				writer.close();
				writer2.close();
				writer3.close();
				writer4.close();
				writer5.close();
				
				
				socket.close ();
			}catch (Exception exc){
				System.out.println(exc);
			}
		
		
		

	}
		
		
		
	
	
	
	public static void Audio()
	{
		
		
		try
		{
			Ssocket = new DatagramSocket();
			System.out.println("Created Socket (Send)");
		}catch(Exception ex){
			System.out.print(ex);
		}
		
		try
		{
			Rsocket = new DatagramSocket(clientPort);
			System.out.println("Created Socket (Receive)");
		}catch(Exception ex){
			System.out.print(ex);
		}
		
		try
		{
			HostName = InetAddress.getByName("ithaki.eng.auth.gr");
			System.out.print("Got ip address = ");
			System.out.println(HostName);
		}catch(Exception e){
			System.out.print(e);
		}
		
		
		byte[] rxbuffer = new byte[128];
		byte[] txbuffer = AudioRequest.getBytes();
		DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length,HostName,serverPort);
		System.out.println("Datagram to send is Established");
		DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
		System.out.println("Datagram to receive is Established");
		boolean flag2=false;
		
		
		
		try {
				PrintWriter writer = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Session2\\AudioA9135F999diff.txt");
				Ssocket.send(p);
				byte[][] sound=new byte[loops][256];
				int help1 = 15;
				int help2 = 240;
				int Nibble1;
				int Nibble2;
				byte[] nibbles = new byte[256];
				int difference1;
				int difference2;
				byte[] differences = new byte[256];
				int beta = 10;
				int sample1=0;
				int sample2=0;
				byte[] samples = new byte[256];
				
	      		for(int i=0;i<loops-10;i++) {
					if(i%7==0)
					{
						new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.println("");
						System.out.print("               --------------------------------------------------------->  Writing " + ((i*101)/loops) + "%  <---------------------------------------------------------");
					}
	      			Rsocket.receive(q);
					
					for(int j=0;j<128;j++) {
						
							Nibble1 = (help1 & rxbuffer[j]);//The first nibble
							Nibble2 = (0x0000000F&((help2 & rxbuffer[j])>>>4));//The second nibble
							
							difference1 = (Nibble1-8)*1;
		                    difference2 = (Nibble2-8)*1;
		                    
		                    sample1 = sample2 + difference2;
		                    sample2 = sample1 + difference1;
  
							//nibbles[2*j]=(byte) Nibble2;
							//differences[2*j] = (byte)difference2;
							//samples[2*j] = (byte)sample1;
							
							//nibbles[2*j+1]=(byte)Nibble1;
							//differences[2*j+1] = (byte)difference1;
							//samples[2*j+1] = (byte)sample2;
							
							if(sample1>120) sample1=120;
							if(sample1<-121) sample1=-120;
							if(sample2>120) sample2=120;
							if(sample2<-121) sample2=-120;
							
							sound[i][2*j] = (byte)sample1;
							sound[i][2*j+1] = (byte)sample2;
							
							writer.println(difference1);
							writer.println(difference2);
					}
					
					
	      		}
	      		writer.close();
	      		AudioFormat audioformat=new AudioFormat(8000,8,1,true,false);
				SourceDataLine dataline=AudioSystem.getSourceDataLine(audioformat);
				dataline.open(audioformat,256);
				dataline.start();
	      		
	      		for(int m=0;m<loops;m++) {
					dataline.write(sound[m],0, 256);
	      		}
				dataline.stop();
				dataline.close();
	      		
				}catch(Exception x) {
					System.out.print(x);
				}
	}	


	public class bufferT extends Thread {
		 
		 public bufferT() {
			 this.start();
		 }
		 
		 
		 public void run() {
		 
			
				try
			{
				Ssocket = new DatagramSocket();
				System.out.println("Created Socket (Send)");
			}catch(Exception ex){
				System.out.print(ex);
			}
			
			try
			{
				Rsocket = new DatagramSocket(clientPort);
				System.out.println("Created Socket (Receive)");
			}catch(Exception ex){
				System.out.print(ex);
			}
			
			try
			{
				HostName = InetAddress.getByName("ithaki.eng.auth.gr");
				System.out.print("Got ip address = ");
				System.out.println(HostName);
			}catch(Exception e){
				System.out.print(e);
			}
		
			byte[] rxbuffer = new byte[128];
			byte[] txbuffer = AudioRequest.getBytes();
			DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length,HostName,serverPort);
			System.out.println("Datagram to send is Established");
			DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
			System.out.println("Datagram to receive is Established");
		
		 
		 
		try {
				
				Ssocket.send(p);
				int help1 = 15;
				int help2 = 240;
				int Nibble1;
				int Nibble2;
				byte[] nibbles = new byte[256];
				int difference1;
				int difference2;
				byte[] differences = new byte[256];
				int sample1=0;
				int sample2=0;
				byte[] samples = new byte[256];
				
	      		for(int i=0;i<loops-10;i++) {
					
	      			Rsocket.receive(q);
					
						
					
				
					for(int j=0;j<128;j++) {
						
							Nibble1 = (0x0000000F&(help1 & rxbuffer[j]));//The first nibble
							Nibble2 = (0x0000000F&((help2 & rxbuffer[j])>>>4));//The second nibble
							
							difference1 = (Nibble1-8)*1;
		                    difference2 = (Nibble2-8)*1;
		                    
		                    sample1 = sample2 + difference2;
		                    sample2 = sample1 + difference1;
  
							//nibbles[2*j]=(byte) Nibble2;
							//differences[2*j] = (byte)difference2;
							samples[2*j] = (byte)sample1;
							if(samples[2*j]>120) samples[2*j]=120;
							if(samples[2*j]<-121) samples[2*j]=-120;
							
							//nibbles[2*j+1]=(byte)Nibble1;
							//differences[2*j+1] = (byte)difference1;
							samples[2*j+1] = (byte)sample2;
							if(samples[2*j+1]>120) samples[2*j+1]=120;
							if(samples[2*j+1]<-121) samples[2*j+1]=-120;
							

					}
					
					buffer.offer(samples);
					
					
	      		}

				}catch(Exception x) {
					//System.out.print(x);
				}
		}	
	 }
		 
	 
	public class playerT extends Thread {
		 
			 public playerT() {
				 this.start();
			 }
			 
			 public void run() {
				 
					try{
						
						
					sleep(3000);
					AudioFormat audioformat=new AudioFormat(8000,8,1,true,false);
					SourceDataLine dataline=AudioSystem.getSourceDataLine(audioformat);
					dataline.open(audioformat,256);
					dataline.start();
					byte[] test=new byte[256];
					for (int f=0;f<loops-10;f++){
						
							test=buffer.take();
						
							dataline.write(test,0, 256);
							//System.out.println(buffer.size());
					}
					dataline.stop();
					dataline.close();
					}catch(Exception x) {
						System.out.print(x);
					}
			 }
		}
	
	
	public class bufferAQ extends Thread {
		 
		 public bufferAQ() {
			 this.start();
		 }
		 
		 
		 public void run() {
		 
				try
			{
				Ssocket = new DatagramSocket();
				System.out.println("Created Socket (Send)");
			}catch(Exception ex){
				System.out.print(ex);
			}
			
			try
			{
				Rsocket = new DatagramSocket(clientPort);
				System.out.println("Created Socket (Receive)");
			}catch(Exception ex){
				System.out.print(ex);
			}
			
			try
			{
				HostName = InetAddress.getByName("ithaki.eng.auth.gr");
				System.out.print("Got ip address = ");
				System.out.println(HostName);
			}catch(Exception e){
				System.out.print(e);
			}
		
			byte[] rxbuffer = new byte[132];
			byte[] txbuffer = AudioRequestAQ.getBytes();
			DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length,HostName,serverPort);
			System.out.println("Datagram to send is Established");
			DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
			System.out.println("Datagram to receive is Established");
		
		 
		 
		try {
				PrintWriter writer = new PrintWriter("C:\\Users\\Bletsos\\Desktop\\Session2\\AudioA9135AQF999B2.txt");
				Ssocket.send(p);
				int help1 = 15;
				int help2 = 240;
				int Nibble1;
				int Nibble2;
				byte[] nibbles = new byte[256];
				int difference1;
				int difference2;
				byte[] differences = new byte[256];
				int sample1=0;
				int sample2=0;
				short m;
				short b;
				byte[] samples = new byte[256];
				
	      		for(int i=0;i<loops-10;i++) {
					
	      			Rsocket.receive(q);
					
	      			m=Merge(rxbuffer[0],rxbuffer[1]);
	      			b=Merge(rxbuffer[2],rxbuffer[3]);
					 //System.out.println(b);
					writer.println(b);
							
					
					for(int j=0;j<128;j++) {
						
							Nibble1 = (0x0000000F&(help1 & rxbuffer[j+4]));//The first nibble
							Nibble2 = (0x0000000F&((help2 & rxbuffer[j+4])>>>4));//The second nibble
							
							difference1 = (Nibble1-8)*b;
		                    difference2 = (Nibble2-8)*b;
							
		                    sample1 = sample2 + difference2+m;
		                    sample2 = sample1 + difference1+m;	

														
 
							//nibbles[2*j]=(byte) Nibble2;
							differences[2*j] = (byte)difference2;
							samples[2*j] = (byte)sample1;
							if(samples[2*j]>120) samples[2*j]=120;
							if(samples[2*j]<-121) samples[2*j]=-120;
							//System.out.println(samples[2*j]);
							
							//nibbles[2*j+1]=(byte)Nibble1;
							differences[2*j+1] = (byte)difference1;
							samples[2*j+1] = (byte)sample2;
							if(samples[2*j+1]>120) samples[2*j+1]=120;
							if(samples[2*j+1]<-121) samples[2*j+1]=-120;
							System.out.println(samples[2*j+1]);
							//writer.println(samples[2*j]);
							//writer.println(samples[2*j+1]);

					}
					buffer.offer(samples);
					//System.out.println("offered samples");

					
					
	      		}
				writer.close();

				}catch(Exception x) {
					//System.out.print(x);
				}
		}	
	 }
		 
	 
	public class playerAQ extends Thread {
		 
			 public playerAQ() {
				 this.start();
			 }
			 
			 public void run() {
				 
					try{
					sleep(3000);
					AudioFormat audioformat=new AudioFormat(8000,8,1,true,false);
					SourceDataLine dataline=AudioSystem.getSourceDataLine(audioformat);
					dataline.open(audioformat,256);
					dataline.start();
					byte[] test=new byte[256];
					for (int f=0;f<loops-10;f++){
							test=buffer.take();
							dataline.write(test,0, 256);
					}
					dataline.stop();
					dataline.close();
					}catch(Exception x) {
						System.out.print(x);
					}
			 }
		}
	
	public static short Merge(byte b1, byte b2) {
        return (short) (((int)b1 << 8) | ((int)b2 & 0xFF));
}
	

}

	