package edu.sjsu.cmpe.customerfeedback.repository;

import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import edu.sjsu.cmpe.customerfeedback.domain.Owner;
//import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class OwnerRepository implements OwnerRepositoryInterface {
	
	//private final ConcurrentHashMap<Integer, Owner> ownerInMemoryMap;
	private int ownerId;
	
	/*public OwnerRepository() {
		checkNotNull(concurrentHashMap, "ownerMap must not be NULL");
		ownerInMemoryMap = concurrentHashMap;
		ownerId = 0;		
	}*/
	public OwnerRepository(){}
	
	private final int generateOwnerId()	{
		return ++ownerId;
	}

	@Override
	public Owner saveOwner(Owner newOwner) {
			try {
			MongoClient client = new MongoClient(new ServerAddress(	"localhost", 27017));
			DB database = client.getDB("customerfeedback");
			checkNotNull(newOwner, "newOwner instance cannot be null");
			int id = generateOwnerId();
			newOwner.setOwnerId(id);
			DBCollection collection = database.getCollection("Owners");
			BasicDBObject document = new BasicDBObject("_id",newOwner.getOwnerId()).append("ownerName", newOwner.getOwnerName());
			collection.insert(document);
			DBCursor cursor = collection.find();
			while (cursor.hasNext())
				System.out.println(cursor.next());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return newOwner ;
	}

	@Override
	public Owner getOwnerbyOwnerID(int OwnerId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public Owner getOwnerbyOwnerID(int ownerId) {
		checkArgument(ownerId>0,"ownerId was "+ownerId+", but expected a greater than zero value");
		return ownerInMemoryMap.get(ownerId);
		
	}*/

}
