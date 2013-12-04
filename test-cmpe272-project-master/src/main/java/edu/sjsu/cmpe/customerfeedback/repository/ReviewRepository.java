/**
 * 
 */
package edu.sjsu.cmpe.customerfeedback.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import edu.sjsu.cmpe.customerfeedback.domain.Review;

/**
 * @author Rajiv
 *
 */
public class ReviewRepository extends ReviewRepositoryInterface {
	
	private final ConcurrentHashMap<Integer, Review> reviewInMemoryMap;
	private int reviewId;
	/**
	 * @param reviewInMemoryMap
	 */
	public ReviewRepository(ConcurrentHashMap<Integer, Review> concurrentHashMap) {
		checkNotNull(concurrentHashMap, "ownerMap must not be NULL");
		reviewInMemoryMap = concurrentHashMap;
		reviewId = 0;
	}
	
	private final int generateReviewId() {
		return ++reviewId;		
	}
	
	 public void saveReview(Review newReview) {
		//checkNotNull(newReview, "The Review cannot be null");
		//int id = generateReviewId();
		//newReview.setReviewId(id);
		//reviewInMemoryMap.putIfAbsent(id, newReview);
		try {
			MongoClient client = new MongoClient(new ServerAddress(	"localhost", 27017));
			DB database = client.getDB("customerfeedback");
			checkNotNull(newReview, "The Review cannot be null");
			int id = generateReviewId();
			newReview.setReviewId(id);
			int ProductId= newReview.getProductId();
			
			int OwnerId= newReview.getOwnerId();
			
			DBCollection collection = database.getCollection("Products");
			
			BasicDBObject findOwner = new BasicDBObject();
			findOwner.put("_id", OwnerId); 
			
			BasicDBObject findProduct = new BasicDBObject();
			findProduct.put("_id", ProductId); 
						
			/*BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			obj.add(new BasicDBObject("_id", newReview.getProductId()));
			obj.add(new BasicDBObject("_id", newReview.getOwnerId()));
			andQuery.put("$and", obj);
		 		*/ 	
		 	
			//System.out.println(andQuery.toString());
		 
			
			//BasicDBObject newdocument = new BasicDBObject().append("Products", new BasicDBObject("REviews", new BasicDBObject("Review", newReview.getReviewText()).append("_id", newReview.getProductId()).append("_id", newReview.getOwnerId())));
			//BasicDBObject newdocument = new BasicDBObject("REviews", new BasicDBObject("Review", newReview.getReviewText()).append("_id", newReview.getProductId()).append("_id", newReview.getOwnerId()));
			BasicDBObject newdocument = new BasicDBObject("REVIEW", new BasicDBObject("ReviewText", newReview.getReviewText()).append("ReviewId",id));
			BasicDBObject reviewMode = new BasicDBObject();
			reviewMode.put("$addToSet", newdocument);
			collection.update(findProduct, reviewMode,true,false);
			//collection.insert(newdocument);
			
			
			//BasicDBObject document = new BasicDBObject().append("_id",newReview.getOwnerId());
			//collection.updateMulti(newdocument,document);
			//collection.insert(newdocument);
			DBCursor cursor = collection.find();
			while (cursor.hasNext())
				System.out.println(cursor.next());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	 }
	 
	 public Review getReview(int reviewId){
		checkArgument(reviewId>0,"reviewId was "+reviewId+", but expected a greater than zero value");
		return reviewInMemoryMap.get(reviewId); 
	 }
	 
	 public ArrayList<Review> getAllReviews(){
		 return new ArrayList<Review>(reviewInMemoryMap.values());
	 }

}
