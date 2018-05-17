/* File FishStickClient.java
 * Author: Stanley Pieda
 * Created On: Jan 2018
 * Description: Client used to insert FishStick records on server.
 */
package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.UUID;

import datatransfer.FishStick;
import remoteinterface.FishStickService;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.NotBoundException;

/**
 * Class FishStickClient is used to start the client-side code and insert fish stick data to database
 * @author Weikai Li
 */
public class FishStickClient {
	/**
	 * main method to start the program
	 * @param args
	 */
	public static void main(String[] args) {
		
		int port = 1099;
		String serverName = new String("localhost");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String myHostName = "localhost";
		String isTrue = null;  // isTrue is used to check client want to insert another fish stick or not
		switch (args.length) {
		case 0:
			break;
		case 1: 
			serverName = args[0];
			break;
		case 2:
			serverName = args[0];
			port = Integer.parseInt(args[1]);
			break;
		default:
			System.out.println("usage: FishStickClient [hostname [portnum]]");
			break;
		}
		try {
			InetAddress myHost = Inet4Address.getLocalHost();    //get internet ip address
			myHostName = myHost.getHostName();      //get host name
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			String message;
			System.out.println("FishStickClient by: Weikai Li");
			System.out.println("Attempting to connect to rmi://"+serverName+":"+port+"/EchoService");
			FishStickService fishStickService = (FishStickService) 
					Naming.lookup("rmi://"+serverName+":"+port+"/FishStickService");  // it is looking up the service name 
																					//saving in the register 
			
			do {
				FishStick fishStick = new FishStick();  // hold the new data input from client
				System.out.println("Enter data for new FishStick:");

				System.out.print("Please enter record number: ");  
				message = br.readLine();
				if (message == null || message.isEmpty()) {
					message = null; // do not append host name, send null to server to start disconnect.
				}
				else {
					fishStick.setRecordNumber(Integer.parseInt(message));   //set the record number of the new data
				}
				
				System.out.print("Please enter omega: ");
				message = br.readLine();
				if (message == null || message.isEmpty()) {
					message = null; // do not append host name, send null to server to start disconnect.
				}
				else {
					fishStick.setOmega(message); //set omega of the new data
				}
				
				System.out.print("Please enter lambda: ");
				message = br.readLine();
				if (message == null || message.isEmpty()) {
					message = null; // do not append host name, send null to server to start disconnect.
				}
				else {
					fishStick.setLambda(message);//set the lambda of the new data
				}
				
				String uuid = UUID.randomUUID().toString();
				fishStick.setUUID(uuid);  //set the generated uuid of the new data
				
				fishStickService.insert(fishStick);  //fishStickService call the remote mothod insert from server to insert data into database
				System.out.println("return FishStick:" + fishStickService.findByUUID(uuid).toString());  //use remote method findByUUID() to return the whole data inserted into database
				System.out.print("Do you want to insert another fish stick?(y or n): ");  
				isTrue = br.readLine().toString();
				if(isTrue.equals("y")) {
					continue;
				}
				else break;
			} while (true);
		}
		catch (MalformedURLException e) {
			System.out.println();
			System.out.println("MalformedURLException");
			System.out.println(e);
		}
		catch (RemoteException e) {
			System.out.println();
			System.out.println("Server failed to perform requested operation.");
		}
		catch (NotBoundException e) {
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(e);
		}
		catch (Exception e) {
			System.out.println();
			System.out.println("Server failed to perform requested operation.");
		}
		finally {
			System.out.println("Client shutting down");
		}
	}
}
