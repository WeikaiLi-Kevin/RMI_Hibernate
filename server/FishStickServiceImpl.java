/* File FishStickServiceImpl.java
 * Author: Stanley Pieda
 * Modified By: 
 * Modifed On: Jan 2018
 * Description: Remote Object that permits clients to insert
 *              FishStick records into a database, as well as
 *              retrieve them using String based uuid.
 */
package server;

import java.rmi.RemoteException;

import datatransfer.FishStick;

import dataaccesslayer.FishStickDao;
import dataaccesslayer.FishStickDaoImpl;

import remoteinterface.FishStickService;
/**
 * Class FishStickServiceImpl is used to implement the operation to insert new data, 
 * find the record by UUID and shut down
 * @author Weikai Li
 *
 */
public class FishStickServiceImpl implements FishStickService  {
	/**
	 * empty default construcor
	 */
	public FishStickServiceImpl() {}
	/**
	 * insert method to call the FishStickDaoImpl method from FishStickService to insert new fish stick
	 */
	@Override
	public void insert(FishStick fishStick) throws RemoteException {
		// code to use the FishStickDaoImpl to insert a record
		FishStickDaoImpl.INSTANCE.insertFishStick(fishStick);
//		throw new RuntimeException("Method not impemented"); // remove this
	}
	
	/**
	 * findByUUID method is used to lookup and return a FishStick
	 */
	@Override
	public FishStick findByUUID(String uuid) throws RemoteException {
		return FishStickDaoImpl.INSTANCE.findByUUID(uuid);
	}
	/**
	 * shutDownDao method is used to close factory and destroy registry
	 */
	public void shutDownDao() {
		FishStickDaoImpl.INSTANCE.shutdown();
	}
}
