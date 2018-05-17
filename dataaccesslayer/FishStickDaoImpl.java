/* File: FishStickDaoImpl.java
 * Author: Stanley Pieda
 * Date: Jan 2018
 * References:
 * Ram N. (2013). Data Access Object Design Pattern or DAO Pattern [blog] Retrieved from
 * http://ramj2ee.blogspot.in/2013/08/data-access-object-design-pattern-or.html
 */
package dataaccesslayer;

import java.util.Calendar;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import datatransfer.FishStick;



/**
 * The FishStickDaoImpl class is used to access data
 * @author Weikai Li
 *
 */
public enum FishStickDaoImpl implements FishStickDao{
	
	/** Only use one constant for Singleton Design Pattern */
	INSTANCE;
	/**
	 * The session factory fot the application
	 */
	private SessionFactory factory;
	/**
	 * The created service registry 
	 */
	private StandardServiceRegistry registry = buildRegistry();
	/**
	 * default constructor to set up session factory for the application
	 */
	private FishStickDaoImpl(){
		try {
			// A SessionFactory is set up once for an application!
			MetadataImplementor meta = 
					(MetadataImplementor) new MetadataSources( registry )
					.addAnnotatedClass(FishStick.class)
					.buildMetadata();
			factory = meta.buildSessionFactory();
		}
		catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, 
			// but if we are here we had trouble building the SessionFactory
			// so destroy it manually.
			if(registry != null) {
				shutdown();
			}
			throw e;
		}
	}
	/**
	 * buildRegistry method is used to build a new registry for server 
	 * @return  the new registry built 
	 */
	private StandardServiceRegistry buildRegistry() {
		StandardServiceRegistry registry = null;
		try {
			// A SessionFactory is set up once for an application!
			registry =  new StandardServiceRegistryBuilder()
					.configure() // configures settings from hibernate.cfg.xml
					.build();
//			System.out.println(141414);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Problem starting hibernate, attempting to shutdown");
			shutdown();
		}
		return registry;
	}
	/**
	 *  insertFishStick is used to insert data into database by using Hibernate
	 */
	@Override
	public void insertFishStick(FishStick fishStick){
		Session s = null;
		Transaction tx = null;
		try {
			s = factory.openSession();
			tx = s.beginTransaction();
			System.out.println("the fishstick insert successfully: "+fishStick);
			s.save(fishStick);
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			throw e;
		}
		finally {
			s.close(); // close resources
		}	

	}

	/**
	 * findByUUID method is used to loop up a record based on UUID by using Hibernate
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FishStick findByUUID(String uuid){
		Session s = null;
		Transaction tx = null;
		try {
			s = factory.openSession();
			tx = s.beginTransaction();
			@SuppressWarnings("unchecked")
			List<FishStick> result = s.createQuery( "from FishStick where uuid = :uuid")
					.setParameter("uuid",uuid) // :client and client could be :tuna and tuna as long as the strings match
					.list();
			tx.commit();

			// return dumpit results to client
			return result.get(0); 
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			throw e;
		}
		finally {
			s.close(); // close resources
		}
	}
	/**
	 * shutdown method is used to close factory and destroy registry
	 */
	@Override
	public void shutdown() {
		System.out.println("Closing factory");
		try {
			if(factory != null && factory.isClosed() == false) {
				factory.close();
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Destroying registry");
		try {
			if(registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
