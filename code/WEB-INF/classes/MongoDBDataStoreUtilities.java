
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBDataStoreUtilities {

	static DBCollection myReviews;
	static MongoCollection<Document> myReviewsCollection ;
	static MongoClient mongo;
	static String ALL = "ALL";
	static String GREATER = "GREATER";
	static String LESS = "LESS";
	static String EQUALS = "EQUALS";
	static String COUNT = "COUNT";
	
	public static void getConnection()
	{
		
		mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("justbuymongodb");
		myReviews= db.getCollection("myReviews");
		
	}
	
	public static void destroyMongoConnection()
	{
		mongo.close();	
	}
	
	
	public static HashMap<String, ArrayList<Review>> selectReview()
	{
		HashMap<String, ArrayList<Review>> reviewHashmap=new HashMap<String, ArrayList<Review>>();
		DBCursor cursor = myReviews.find();
		
		while (cursor.hasNext())
		{
			BasicDBObject obj = (BasicDBObject) cursor.next();
			if(! reviewHashmap.containsKey(obj.getString("productModelName")))
			{
				ArrayList<Review> arr = new ArrayList<Review>();
				reviewHashmap.put(obj.getString("productModelName"), arr);
			}
			ArrayList<Review> listReview = reviewHashmap.get(obj.getString("productModelName"));
			Review review = new Review(obj.getString("productModelName"), Category.fromString(obj.getString("category")), obj.getDouble("price"),
					obj.getString("retailerName"), obj.getInt("retailerZip"), obj.getString("retailerCity"), obj.getString("retailerState"),
					YesNo.fromString(obj.getString("productOnSale")), obj.getString("manufacturerName"), YesNo.fromString(obj.getString("manufacturerRebate")),
					obj.getString("userName"), obj.getInt("userAge"), Gender.fromString(obj.getString("userGender")),
					obj.getString("userOccupation"), obj.getInt("reviewRating"), obj.getDate("reviewDate"), obj.getString("reviewText"));
			listReview.add(review);

		}
		return reviewHashmap;
	}	
	
	public static HashMap<String, ArrayList<Review>> selectReviewsDyanamically(boolean isNameSelected, String productName, 
			boolean isPriceSelected, double productPrice, String condPrice, 
			boolean isRatingSelected, int productRating, String condRating,
			boolean isCitySelected, String rCity,
			boolean isSearchSelected, String searchTerm)
	{
		HashMap<String, ArrayList<Review>> reviewHashmap=new HashMap<String, ArrayList<Review>>();
		
		BasicDBObject query = new BasicDBObject();
		
		if(isNameSelected) {
			if(productName.equals(ALL)) {
				
			} else {
				query.put("productModelName", productName);
			}
		} 
		
		if(isPriceSelected) {
			if(condPrice.equals(EQUALS)) {
				query.put("price", productPrice);
			} else if(condPrice.equals(GREATER)) {
				query.put("price", new BasicDBObject("$gt", productPrice));
			} else if(condPrice.equals(LESS)) {
				query.put("price", new BasicDBObject("$lt", productPrice));
			}
				
		}
		
		if(isRatingSelected) {
			if(condRating.equals(EQUALS)) {
				query.put("reviewRating", productRating);
			} else if(condRating.equals(GREATER)) {
				query.put("reviewRating", new BasicDBObject("$gt", productRating));
			} else if(condRating.equals(LESS)) {
				query.put("reviewRating", new BasicDBObject("$lt", productRating));
			}
				
		}
		
		if(isCitySelected) {
			if(rCity.equals("")) {
				
			} else {
				query.put("retailerCity", rCity);
			}
		} 
		
		if(isSearchSelected) {
			query.put("reviewText", java.util.regex.Pattern.compile(searchTerm));
		}
		
		DBCursor cursor = myReviews.find(query);
		
		System.out.println(query);
		while (cursor.hasNext())
		{
			BasicDBObject obj = (BasicDBObject) cursor.next();
			if(! reviewHashmap.containsKey(obj.getString("productModelName")))
			{
				ArrayList<Review> arr = new ArrayList<Review>();
				reviewHashmap.put(obj.getString("productModelName"), arr);
			}
			ArrayList<Review> listReview = reviewHashmap.get(obj.getString("productModelName"));
			Review review = new Review(obj.getString("productModelName"), Category.fromString(obj.getString("category")), obj.getDouble("price"),
					obj.getString("retailerName"), obj.getInt("retailerZip"), obj.getString("retailerCity"), obj.getString("retailerState"),
					YesNo.fromString(obj.getString("productOnSale")), obj.getString("manufacturerName"), YesNo.fromString(obj.getString("manufacturerRebate")),
					obj.getString("userName"), obj.getInt("userAge"), Gender.fromString(obj.getString("userGender")),
					obj.getString("userOccupation"), obj.getInt("reviewRating"), obj.getDate("reviewDate"), obj.getString("reviewText"));
			listReview.add(review);
			System.out.println(review);
			System.out.println(obj.getString("productModelName")+" :::: " +listReview);

		}
		return reviewHashmap;
	}	

	/*
	 * TRENDING 
	 */
	public static HashMap<String, Integer> topLikedProductsMethod1()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productModelName", "$_id");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productModelName");
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("reviewLikes",-1);
		
		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 5));
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		limit=new BasicDBObject("$limit",5);
		
		AggregationOutput aggregate = myReviews.aggregate(match,  group, project, limit, orderby);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productModelName"));
			reviewLikes = bobj.getString("reviewLikes");
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			reviewHashmap.put(bobj.getString("productModelName"), reviewLikesCount);

		}
		
		return reviewHashmap;
	}	
	
	public static HashMap<String, Integer> topZipCodesByReviewCount()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerZip");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("retailerZip", "$_id");
		projectFields.put("count", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("count",-1);
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		limit=new BasicDBObject("$limit",5);
		
		AggregationOutput aggregate = myReviews.aggregate(group, project, limit, orderby);
		
		String reviewCount = null;
		int reviewCountCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("retailerZip"));
			reviewCount = bobj.getString("count");
			if(reviewCount == null || reviewCount.isEmpty()) {
				reviewCountCount=0;
			} else {
				reviewCountCount=Integer.parseInt(reviewCount);
			}
			System.out.println(reviewCountCount);
			reviewHashmap.put(bobj.getString("retailerZip"), reviewCountCount);

		}
		
		return reviewHashmap;
	}	

	public static HashMap<String, Integer> topProductsByReviewCount()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productModelName");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productModelName", "$_id");
		projectFields.put("count", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("count",-1);
		
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		limit=new BasicDBObject("$limit",5);
		
		AggregationOutput aggregate = myReviews.aggregate(group, project, limit, orderby);
		
		String reviewCount = null;
		int reviewCountCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productModelName"));
			reviewCount = bobj.getString("count");
			if(reviewCount == null || reviewCount.isEmpty()) {
				reviewCountCount=0;
			} else {
				reviewCountCount=Integer.parseInt(reviewCount);
			}
			System.out.println(reviewCountCount);
			reviewHashmap.put(bobj.getString("productModelName"), reviewCountCount);

		}
		
		return reviewHashmap;
	}	

	public static HashMap<String, Integer> topLikedProductsMethod2()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productModelName", "$_id");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productModelName");
		groupFields.put("likes", new BasicDBObject("$sum", "$reviewRating"));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("reviewLikes",-1);
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		limit=new BasicDBObject("$limit",5);
		
		AggregationOutput aggregate = myReviews.aggregate( group, project, orderby, limit);
		
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productModelName"));
			System.out.println(bobj.getString("reviewLikes"));
			reviewHashmap.put(bobj.getString("productModelName"), Integer.parseInt(bobj.getString("reviewLikes")));
		}
		
		return reviewHashmap;
	}	
	
	
		
	public static HashMap<String, Integer> fetchProductsByReviewCountAndRating()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productModelName", "$_id");
		projectFields.put("reviewCount", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productModelName");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("reviewCount",-1);
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 5));
		
		
		AggregationOutput aggregate = myReviews.aggregate(match, group, project, orderby, limit);
		
		String reviewCount = null;
		int reviewCountCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productModelName"));
			reviewCount = bobj.getString("reviewCount");
			if(reviewCount == null || reviewCount.isEmpty()) {
				reviewCountCount=0;
			} else {
				reviewCountCount=Integer.parseInt(reviewCount);
			}
			System.out.println(reviewCountCount);
			reviewHashmap.put(bobj.getString("productModelName"), reviewCountCount);

		}
		
		return reviewHashmap;
	}	

	
	public static HashMap<String, Integer> reviewCountEveryProduct()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productModelName", "$_id");
		projectFields.put("reviewCount", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productModelName");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		
		AggregationOutput aggregate = myReviews.aggregate( group, project );
		
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productModelName"));
			System.out.println(bobj.getString("reviewCount"));
			reviewHashmap.put(bobj.getString("productModelName"), Integer.parseInt(bobj.getString("reviewCount")));
		}
		
		return reviewHashmap;
	}
	
	public static HashMap<String, ArrayList<Review>> topLikedProductsEveryCity(int topcount, boolean isTopCount, String sortBy)
	{
			HashMap<String, Integer> reviewHashmap = fetchTopLikedProductsMethod1(sortBy);
		HashMap<String, Review> reviews = fetchReviews();
		HashMap<String, ArrayList<Review>> outputReviews = new HashMap<String, ArrayList<Review>>();
		
		String productName = "";
		String city = "";
		Review review ;
		int likecount;
		System.out.println("reviewHashmap-->> "+reviewHashmap);
		System.out.println("\n reviews-->> "+reviews);
		for(Entry<String, Integer> r: reviewHashmap.entrySet()) {
			
			productName = r.getKey();
			likecount = r.getValue();
			review = reviews.get(productName);
			review.setCount(likecount);
			city = review.getRetailerCity();
			
			if(! outputReviews.containsKey(city))
			{
				ArrayList<Review> arr = new ArrayList<Review>();
				outputReviews.put(city, arr);
			}
			ArrayList<Review> listReview = outputReviews.get(city);
			if(isTopCount) {
				if(listReview.size() <= topcount) {
					listReview.add(review);
				}
			} else {
				listReview.add(review);
			}
			
		}
		System.out.println("==============>"+outputReviews);
		return outputReviews;
	}
	
	public HashMap<String, ArrayList<Review>> reviewsByCity() {
		HashMap<String, ArrayList<Review>> reviewHashmap=new HashMap<String, ArrayList<Review>>();
		DBCursor cursor = myReviews.find();
		
		while (cursor.hasNext())
		{
			BasicDBObject obj = (BasicDBObject) cursor.next();
			if(! reviewHashmap.containsKey(obj.getString("retailerCity")))
			{
				ArrayList<Review> arr = new ArrayList<Review>();
				reviewHashmap.put(obj.getString("retailerCity"), arr);
			}
			ArrayList<Review> listReview = reviewHashmap.get(obj.getString("retailerCity"));
			Review review = new Review(obj.getString("productModelName"), Category.fromString(obj.getString("category")), obj.getDouble("price"),
					obj.getString("retailerName"), obj.getInt("retailerZip"), obj.getString("retailerCity"), obj.getString("retailerState"),
					YesNo.fromString(obj.getString("productOnSale")), obj.getString("manufacturerName"), YesNo.fromString(obj.getString("manufacturerRebate")),
					obj.getString("userName"), obj.getInt("userAge"), Gender.fromString(obj.getString("userGender")),
					obj.getString("userOccupation"), obj.getInt("reviewRating"), obj.getDate("reviewDate"), obj.getString("reviewText"));
			listReview.add(review);
		}
		return reviewHashmap;
	}
	
	public HashMap<String, ArrayList<Review>> reviewsByRetailerName() {
		HashMap<String, ArrayList<Review>> reviewHashmap=new HashMap<String, ArrayList<Review>>();
		DBCursor cursor = myReviews.find();
		
		while (cursor.hasNext())
		{
			BasicDBObject obj = (BasicDBObject) cursor.next();
			if(! reviewHashmap.containsKey(obj.getString("retailerName")))
			{
				ArrayList<Review> arr = new ArrayList<Review>();
				reviewHashmap.put(obj.getString("retailerName"), arr);
			}
			ArrayList<Review> listReview = reviewHashmap.get(obj.getString("retailerName"));
			Review review = new Review(obj.getString("productModelName"), Category.fromString(obj.getString("category")), obj.getDouble("price"),
					obj.getString("retailerName"), obj.getInt("retailerZip"), obj.getString("retailerCity"), obj.getString("retailerState"),
					YesNo.fromString(obj.getString("productOnSale")), obj.getString("manufacturerName"), YesNo.fromString(obj.getString("manufacturerRebate")),
					obj.getString("userName"), obj.getInt("userAge"), Gender.fromString(obj.getString("userGender")),
					obj.getString("userOccupation"), obj.getInt("reviewRating"), obj.getDate("reviewDate"), obj.getString("reviewText"));
			listReview.add(review);
		}
		return reviewHashmap;
	}

	public static HashMap<String, Review> fetchReviews()
	{
		HashMap<String, Review> reviewHashmap=new HashMap<String, Review>();
		
		DBCursor cursor = myReviews.find();
		while (cursor.hasNext())
		{
			BasicDBObject obj = (BasicDBObject) cursor.next();
		
			Review review = new Review(obj.getString("productModelName"), Category.fromString(obj.getString("category")), obj.getDouble("price"),
					obj.getString("retailerName"), obj.getInt("retailerZip"), obj.getString("retailerCity"), obj.getString("retailerState"),
					YesNo.fromString(obj.getString("productOnSale")), obj.getString("manufacturerName"), YesNo.fromString(obj.getString("manufacturerRebate")),
					obj.getString("userName"), obj.getInt("userAge"), Gender.fromString(obj.getString("userGender")),
					obj.getString("userOccupation"), obj.getInt("reviewRating"), obj.getDate("reviewDate"), obj.getString("reviewText"));
			
			reviewHashmap.put(obj.getString("productModelName"), review);

		}
		return reviewHashmap;
	}	
	
	public static HashMap<String, Integer> fetchTopLikedProductsMethod1(String sortBy)
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productModelName");
		if(sortBy.equals("MNAME")) {
			groupFields.put("mname", new BasicDBObject("$first", "$manufacturerName"));
		}
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productModelName", "$_id");
		projectFields.put("reviewLikes", "$likes");
		if(sortBy.equals("MNAME")) {
			projectFields.put("mname", "$mname");
		}
		DBObject project = new BasicDBObject("$project", projectFields);
	
		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("reviewLikes",-1);
		if(sortBy.equals("MNAME")) {
			sort.put("mname",-1);
		}
		
		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 5));
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		
		AggregationOutput aggregate = myReviews.aggregate(match, group, project, orderby);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productModelName"));
			reviewLikes = bobj.getString("reviewLikes");
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			if(sortBy.equals("MNAME")) {
				System.out.println("++++"+bobj.getString("mname"));
			}
			reviewHashmap.put(bobj.getString("productModelName"), reviewLikesCount);

		}
		
		return reviewHashmap;
	}	
	
	public static HashMap<String, Integer> zipCodeWithRating()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerZip");
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("retailerZip", "$_id");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);


		DBObject limit=new BasicDBObject("$limit",2);
		DBObject orderby=new BasicDBObject();
		DBObject sort = new BasicDBObject();
		sort.put("reviewLikes",-1);
		orderby=new BasicDBObject("$sort",sort);
		
		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 5));
		
		AggregationOutput aggregate = myReviews.aggregate(match, group, project, limit, orderby);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("retailerZip"));
			reviewLikes = bobj.getString("reviewLikes");
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			reviewHashmap.put(bobj.getString("retailerZip"), reviewLikesCount);

		}
		
		return reviewHashmap;
	}	

	public static HashMap<String, Integer> productsWithRating()
	{
		HashMap<String, Integer> reviewHashmap=new HashMap<String, Integer>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("retailerCity", "$_id");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerCity");
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 5));
		
		AggregationOutput aggregate = myReviews.aggregate(match, group, project);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("retailerCity"));
			reviewLikes = bobj.getString("reviewLikes");
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			reviewHashmap.put(bobj.getString("retailerCity"), reviewLikesCount);

		}
		
		return reviewHashmap;
	}	
	
	public static HashMap<String, Review> mostLikedExpensive()
	{
		HashMap<String, Review> reviewHashmap=new HashMap<String, Review>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("retailerCity", "$_id");
		projectFields.put("price", "$price");
		projectFields.put("pname", "$pname");
		projectFields.put("rname", "$rname");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerCity");
		groupFields.put("price", new BasicDBObject("$first", "$price"));

		groupFields.put("pname", new BasicDBObject("$first", "$productModelName"));
		groupFields.put("rname", new BasicDBObject("$first", "$retailerName"));
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 5));
		
		DBObject sort = new BasicDBObject();
		sort.put("reviewLikes",-1);
		sort.put("price",-1);
		sort.put("rname",-1);
		BasicDBObject orderby=new BasicDBObject("$sort",sort);
		
		AggregationOutput aggregate = myReviews.aggregate(match, group, project,orderby);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("retailerCity"));

			System.out.println(bobj.getString("rname"));
			System.out.println(bobj.getString("price"));
			reviewLikes = bobj.getString("reviewLikes");
			Review r = new Review();
			r.setProductModelName(bobj.getString("pname"));
			r.setPrice(Double.parseDouble(bobj.getString("price")));
			
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			r.setCount(reviewLikesCount);
			r.setRetailerName(bobj.getString("rname"));
			reviewHashmap.put(bobj.getString("retailerCity"), r);

		}
		
		return reviewHashmap;
	}	
	
	public static HashMap<String, Double> medianPriceOfCity()
	{
		HashMap<String, Double> reviewHashmap1=new HashMap<String, Double>();
		
		
		HashMap<String, ArrayList<Review>> reviewHashmap=new HashMap<String, ArrayList<Review>>();
		
		BasicDBObject orderby=new BasicDBObject("price", -1);
				
		DBCursor cursor = myReviews.find();	
		cursor.sort(orderby);
		
		while (cursor.hasNext())
		{
			BasicDBObject obj = (BasicDBObject) cursor.next();
			if(! reviewHashmap.containsKey(obj.getString("retailerCity")))
			{
				ArrayList<Review> arr = new ArrayList<Review>();
				reviewHashmap.put(obj.getString("retailerCity"), arr);
			}
			ArrayList<Review> listReview = reviewHashmap.get(obj.getString("retailerCity"));
			Review review = new Review(obj.getString("productModelName"), Category.fromString(obj.getString("category")), obj.getDouble("price"), obj.getString("retailerName"), obj.getInt("retailerZip"), obj.getString("retailerCity"), obj.getString("retailerState"),
					YesNo.fromString(obj.getString("productOnSale")), obj.getString("manufacturerName"), YesNo.fromString(obj.getString("manufacturerRebate")),
					obj.getString("userName"), obj.getInt("userAge"), Gender.fromString(obj.getString("userGender")),
					obj.getString("userOccupation"), obj.getInt("reviewRating"), obj.getDate("reviewDate"), obj.getString("reviewText"));
			listReview.add(review);

		}
		System.out.println(reviewHashmap);
		
		double medianPrice;
		int reviewSize;
		int halfreviewSize;
		for(Entry<String, ArrayList<Review>> m :reviewHashmap.entrySet()){
			ArrayList<Review> reviews = m.getValue();
			reviewSize = reviews.size();
			halfreviewSize = reviewSize/2;
			if(reviewSize%2 == 0){
				medianPrice = (reviews.get(halfreviewSize-1).getPrice() + reviews.get(halfreviewSize).getPrice())/2;
			} else {
				medianPrice = reviews.get(halfreviewSize).getPrice();
			}
			reviewHashmap1.put(m.getKey(), medianPrice);
		}
		
		return reviewHashmap1;
	}	

	public static HashMap<String, Review> mostDisLikedInCity()
	{
		HashMap<String, Review> reviewHashmap=new HashMap<String, Review>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("retailerCity", "$_id");
		projectFields.put("price", "$price");
		projectFields.put("pname", "$pname");
		projectFields.put("rname", "$rname");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerCity");
		groupFields.put("price", new BasicDBObject("$first", "$price"));

		groupFields.put("pname", new BasicDBObject("$first", "$productModelName"));
		groupFields.put("rname", new BasicDBObject("$first", "$retailerName"));
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 1));
		
		DBObject sort = new BasicDBObject();
		sort.put("reviewLikes",-1);
		sort.put("price",-1);
		sort.put("rname",-1);
		BasicDBObject orderby=new BasicDBObject("$sort",sort);
		
		AggregationOutput aggregate = myReviews.aggregate(match, group, project,orderby);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("retailerCity"));

			System.out.println(bobj.getString("rname"));
			System.out.println(bobj.getString("price"));
			reviewLikes = bobj.getString("reviewLikes");
			Review r = new Review();
			r.setProductModelName(bobj.getString("pname"));
			r.setPrice(Double.parseDouble(bobj.getString("price")));
			
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			r.setCount(reviewLikesCount);
			r.setRetailerName(bobj.getString("rname"));
			reviewHashmap.put(bobj.getString("retailerCity"), r);

		}
		
		return reviewHashmap;
	}	
	
	public static HashMap<String, Review> mostDisLikedInZip()
	{
		HashMap<String, Review> reviewHashmap=new HashMap<String, Review>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("retailerZip", "$_id");
		projectFields.put("price", "$price");
		projectFields.put("pname", "$pname");
		projectFields.put("rname", "$rname");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerZip");
		groupFields.put("price", new BasicDBObject("$first", "$price"));

		groupFields.put("pname", new BasicDBObject("$first", "$productModelName"));
		groupFields.put("rname", new BasicDBObject("$first", "$retailerName"));
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewRating", 1));
		
		DBObject sort = new BasicDBObject();
		sort.put("reviewLikes",-1);
		sort.put("price",-1);
		sort.put("rname",-1);
		BasicDBObject orderby=new BasicDBObject("$sort",sort);
		
		AggregationOutput aggregate = myReviews.aggregate(match, group, project,orderby);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("retailerZip"));

			System.out.println(bobj.getString("rname"));
			System.out.println(bobj.getString("price"));
			reviewLikes = bobj.getString("reviewLikes");
			Review r = new Review();
			r.setProductModelName(bobj.getString("pname"));
			r.setPrice(Double.parseDouble(bobj.getString("price")));
			
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			r.setCount(reviewLikesCount);
			r.setRetailerName(bobj.getString("rname"));
			reviewHashmap.put(bobj.getString("retailerZip"), r);

		}
		
		return reviewHashmap;
	}	

	public HashMap<String, Double> highestPriceInCity(boolean byCity) {
		HashMap<String, Double> reviewHashmap=new HashMap<String, Double>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		if(byCity) {
			projectFields.put("retailerCity", "$_id");
		} else {
			projectFields.put("retailerZip", "$_id");
		}
		projectFields.put("maxCount", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		if(byCity) {
			groupFields.put("_id", "$retailerCity");
		} else {
			groupFields.put("_id", "$retailerZip");
		}
		
		groupFields.put("count", new BasicDBObject("$max", "$price"));
		DBObject group = new BasicDBObject("$group", groupFields);

		
		AggregationOutput aggregate = myReviews.aggregate( group, project );
		
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			if(byCity) {
				reviewHashmap.put(bobj.getString("retailerCity"), Double.parseDouble(bobj.getString("maxCount")));
			} else {
				reviewHashmap.put(bobj.getString("retailerZip"), Double.parseDouble(bobj.getString("maxCount")));		
			}
			
		}
		
		return reviewHashmap;
	}
	
	public static HashMap<String, ArrayList<Review>> reviewsByAge()
	{
		HashMap<String, ArrayList<Review>> reviewHashmap=new HashMap<String, ArrayList<Review>>();
		
		BasicDBObject query = new BasicDBObject();
		query.put("userAge", new BasicDBObject("$gt", 50));
		
		//DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		//sort.put("userAge",-1);
		//Adding sort object in DbObject
		BasicDBObject orderby=new BasicDBObject("userAge", -1);
		
				
		DBCursor cursor = myReviews.find(query);	
		cursor.sort(orderby);
		
		System.out.println(query);
		while (cursor.hasNext())
		{
			BasicDBObject obj = (BasicDBObject) cursor.next();
			if(! reviewHashmap.containsKey(obj.getString("retailerCity")))
			{
				ArrayList<Review> arr = new ArrayList<Review>();
				reviewHashmap.put(obj.getString("retailerCity"), arr);
			}
			ArrayList<Review> listReview = reviewHashmap.get(obj.getString("retailerCity"));
			Review review = new Review(obj.getString("productModelName"), Category.fromString(obj.getString("category")), obj.getDouble("price"), obj.getString("retailerName"), obj.getInt("retailerZip"), obj.getString("retailerCity"), obj.getString("retailerState"),
					YesNo.fromString(obj.getString("productOnSale")), obj.getString("manufacturerName"), YesNo.fromString(obj.getString("manufacturerRebate")),
					obj.getString("userName"), obj.getInt("userAge"), Gender.fromString(obj.getString("userGender")),
					obj.getString("userOccupation"), obj.getInt("reviewRating"), obj.getDate("reviewDate"), obj.getString("reviewText"));
			listReview.add(review);

		}
		System.out.println(reviewHashmap);
		return reviewHashmap;
	}	
	
	public static HashMap<String, Review> selectReviewsWithAggregation(boolean isNameSelected, String productName, 
			boolean isPriceSelected, double productPrice, String condPrice, 
			boolean isRatingSelected, int productRating, String condRating,
			boolean isCitySelected, String rCity,
			boolean isGroupBySelected, String groupbyString, String groupbyAgg )
	{
		HashMap<String, Review> reviewHashmap=new HashMap<String, Review>();
		
		BasicDBObject query = new BasicDBObject();
		
		if(isNameSelected) {
			if(productName.equals(ALL)) {
				
			} else {
				query.put("productModelName", productName);
			}
		} 
		
		if(isPriceSelected) {
			if(condPrice.equals(EQUALS)) {
				query.put("price", productPrice);
			} else if(condPrice.equals(GREATER)) {
				query.put("price", new BasicDBObject("$gt", productPrice));
			} else if(condPrice.equals(LESS)) {
				query.put("price", new BasicDBObject("$lt", productPrice));
			}
				
		}
		
		if(isRatingSelected) {
			if(condRating.equals(EQUALS)) {
				query.put("reviewRating", productRating);
			} else if(condRating.equals(GREATER)) {
				query.put("reviewRating", new BasicDBObject("$gt", productRating));
			} else if(condRating.equals(LESS)) {
				query.put("reviewRating", new BasicDBObject("$lt", productRating));
			}
				
		}
		
		if(isCitySelected) {
			if(rCity.equals("")) {
				
			} else {
				query.put("retailerCity", rCity);
			}
		} 
			
		/*
		 * SELECT = projectFields
		 */
	/*	DBObject projectFields = new BasicDBObject();
		projectFields.put("city", "$retailerCity");
		//projectFields.put("Review Count", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);
	*/	
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("city", "$_id");
		projectFields.put("productModelName", "$productModelName");
		projectFields.put("price", "$price");
		projectFields.put("reviewRating", "$reviewRating");
		projectFields.put("reviewText", "$reviewText");
		//projectFields.put("Review Count", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerCity");
		groupFields.put("productModelName", new BasicDBObject("$first", "$productModelName"));
		groupFields.put("price", new BasicDBObject("$first", "$price"));
		groupFields.put("reviewRating", new BasicDBObject("$first", "$reviewRating"));
		groupFields.put("reviewText", new BasicDBObject("$first", "$reviewText"));
		//groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		

		
		AggregationOutput aggregate = myReviews.aggregate( group, project );
		
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("city"));
			System.out.println(bobj.getString("productModelName"));
			System.out.println(bobj.getString("price"));
			System.out.println(bobj.getString("reviewText"));
			System.out.println(bobj.getString("reviewRating"));
//			System.out.println(bobj.getString("Review Count"));
		}
		
		return reviewHashmap;
	}	
	
	public static void insertReview(Review r)
	{
			BasicDBObject doc = new BasicDBObject("title", "myReviews").
			append("productModelName", r.getProductModelName()).
			append("category", r.getCategory().toString()).
			append("price", r.getPrice()).
			append("retailerName", r.getRetailerName()).
			append("retailerZip",r.getRetailerZip()).
			append("retailerCity", r.getRetailerCity()).
			append("retailerState", r.getRetailerState()).
			append("productOnSale", r.isProductOnSale().toString()).
			append("manufacturerName", r.getManufacturerName()).
			append("manufacturerRebate", r.isManufacturerRebate().toString()).
			append("userName", r.getUserName()).
			append("userAge", r.getUserAge()).
			append("userGender", r.getUserGender().toString()).
			append("userOccupation", r.getUserOccupation()).
			append("reviewRating", r.getReviewRating()).
			append("reviewDate", r.getReviewDate()).
			append("reviewText", r.getReviewText());
			myReviews.insert(doc);
	}

}
