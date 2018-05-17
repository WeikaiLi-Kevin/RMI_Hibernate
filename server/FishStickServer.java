/* File FishStickServer.java
 * Author: Todd Kelley
 * Modified By: Stanley Pieda
 * Modifed On: Jan 2018
 * Description: RMI Server startup.
 */
package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Class FishStickServer is used to start fish stick server 
 * and listen to the client to call the local method as remote method
 * @author Weikai Li
 *
 */
public class FishStickServer {
	/**
	 * This is the main method to start the code
	 * @param args default argument
	 */
	public static void main(String[] args) {
		try {
			int portNum = 1099;
			if(args.length > 0){
				portNum = Integer.parseInt(args[0]);
			}
			
			// Create the remote service
			FishStickServiceImpl fishStickServiceImpl = new FishStickServiceImpl();
			// create RMI registry, saving reference to it
			Registry registry = LocateRegistry.createRegistry(portNum);
			System.out.println("FishStickServer by: Weikai Li");
			System.out.println( "Registry created" );
			
			// export the remote service
			UnicastRemoteObject.exportObject(fishStickServiceImpl,0);
			// rebind using portNum with service name /FishStickService
			Naming.rebind("//localhost:" + portNum + "/FishStickService", fishStickServiceImpl);
			// message to users (this is done already on next line)
			System.out.println( "Exported" );
			
			
			System.out.println("Press any key to shutdown remote object and end program");
			Scanner input = new Scanner(System.in);
			input.nextLine(); // pause, let server-side admin close down connections
			
			fishStickServiceImpl.shutDownDao(); // close down Hibernate resources
			
			System.out.println("un-exporting remote object");
			UnicastRemoteObject.unexportObject(fishStickServiceImpl, true); // remove object
			
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
			e.printStackTrace();
		}
	}
}
