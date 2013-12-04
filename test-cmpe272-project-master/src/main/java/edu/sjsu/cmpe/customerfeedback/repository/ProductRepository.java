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

import edu.sjsu.cmpe.customerfeedback.domain.Product;

/**
 * @author Rajiv
 *
 */
public class ProductRepository implements ProductRepositoryInterface {
	//private final ConcurrentHashMap<Integer, Product> productInMemoryMap;
	private int productId;
	
	/**
	 * 
	 */
	/*public ProductRepository(ConcurrentHashMap<Integer, Product> concurrentHashMap) {
		checkNotNull(concurrentHashMap, "ownerMap must not be NULL");
		productInMemoryMap = concurrentHashMap;
		productId = 0;
	}*/
	public ProductRepository(){}
	private final int generateProductId() {
		return ++productId;		
	}
	
	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.customerfeedback.repository.ProductRepositoryInterface#saveProduct(edu.sjsu.cmpe.customerfeedback.domain.Product)
	 */
	
	@Override
	public Product saveProduct(Product newProduct) {
		
		try {
			MongoClient client = new MongoClient(new ServerAddress(	"localhost", 27017));
			DB database = client.getDB("customerfeedback");
			checkNotNull(newProduct , "newProduct instance cannot be null");
			int id = generateProductId();
			newProduct.setProductId(id);
			int OwnerId= newProduct.getOwnerId();
			DBCollection collection = database.getCollection("Products");
			
			
			BasicDBObject findOwner = new BasicDBObject();
			findOwner.put("_id", OwnerId); // search query for the customer email

			// construct your newAddress object the same way you already are
			//BasicDBObject newdocument = new BasicDBObject().append("Products", new BasicDBObject("_id",newProduct.getProductId()).append("ProductName", newProduct.getProductName()));
			//BasicDBObject newdocument = new BasicDBObject().append("_id",newProduct.getProductId()).append("ProductName", newProduct.getProductName());
			BasicDBObject newdocument = new BasicDBObject("_id",newProduct.getProductId()).append("ProductName", newProduct.getProductName()).append("OwnerId",OwnerId);
			//BasicDBObject productMode = new BasicDBObject();
			/*productMode.put("$addToSet", newdocument);
			collection.update(findOwner, productMode, true /* upsert *///, false /* multi */ );*/
			//collection.insert(productMode);
			
			
			//BasicDBObject document = new BasicDBObject().append("_id",newProduct.getOwnerId());
			//collection.updateMulti(newdocument,document);
			collection.insert(newdocument);
			/**to update Owner collection */
			DBCollection collection1 = database.getCollection("Owners");
			
			
			BasicDBObject updatedocument = new BasicDBObject().append("Products", new BasicDBObject("ProductId",newProduct.getProductId()).append("ProductName", newProduct.getProductName()));
			BasicDBObject productMode = new BasicDBObject();
			productMode.put("$addToSet", updatedocument);
			collection1.update(findOwner, productMode, true /* upsert */, false /* multi */ );
			//collection.insert(productMode);
			
			
			//BasicDBObject document = new BasicDBObject().append("_id",newProduct.getOwnerId());
			//collection.updateMulti(newdocument,document);
			collection.insert(newdocument);
			DBCursor cursor = collection.find();
			while (cursor.hasNext())
				System.out.println(cursor.next());
		} catch (Exception e) {
			// TODO: handle exception
		}
		//productInMemoryMap.putIfAbsent(id, newProduct);
		return newProduct;
	}

	@Override
	public Product getProductbyProductId(int productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getallProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.customerfeedback.repository.ProductRepositoryInterface#getProductbyProductId(int)
	 */
	/*@Override
	public Product getProductbyProductId(int productId) {
		checkArgument(productId>0, "productId was "+productId+", but expected a greater than zero value");
		return productInMemoryMap.get(productId);
	}
	
	public List<Product> getallProducts() {
		return new ArrayList<Product>(productInMemoryMap.values());
		
	}
*/
}
