import java.io.*; 
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;


public class AnalyticsResultPage extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();
  private MongoDBDataStoreUtilities mongoStore = new MongoDBDataStoreUtilities();
  
  
  public void prinProducttMap(HashMap<String, Product> mapInFile) throws ServletException {
	  
  }
  
  protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
	}
	
  
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String productName = "",condPrice = "", condRating="", rCity = "", groupbyString="", groupbyAgg = "";
    double productPrice = 0.0;
    int productRating = 0;
    		
	boolean isNameSelected = Boolean.parseBoolean(request.getParameter("isNameSelected"));
	if(isNameSelected) {
		productName = request.getParameter("productName");
		
	}
	System.out.println(request.getParameter("isNameSelected")+""+isNameSelected+" | "+ productName );
	
	boolean isPriceSelected = Boolean.parseBoolean(request.getParameter("isPriceSelected"));
	if(isPriceSelected) {
		 productPrice = Double.parseDouble(request.getParameter("productPrice"));
		 condPrice = request.getParameter("condPrice");
	}
	System.out.println(isPriceSelected+" - "+productPrice+" | "+ condPrice );
	
	boolean isRatingSelected = Boolean.parseBoolean(request.getParameter("isRatingSelected"));
	if(isRatingSelected) {
		 productRating = Integer.parseInt(request.getParameter("productRating"));
		 condRating = request.getParameter("condRating");
	}
	System.out.println(isRatingSelected+" - "+productRating+" | "+ condRating );

	
	boolean isCitySelected = Boolean.parseBoolean(request.getParameter("isCitySelected"));
	if(isCitySelected) {
		 rCity = request.getParameter("rCity");
	}
	System.out.println(isCitySelected+" - "+rCity );
	
	boolean isSearchSelected = Boolean.parseBoolean(request.getParameter("isSearchSelected"));
	String searchTerm = "";
	if(isSearchSelected) {
		searchTerm = request.getParameter("searchTerm");
	}

	
	String questionId = request.getParameter("qId");
	boolean byQ = false;
	int qId = 0 ;
	if(questionId != null && !questionId.isEmpty()) {
		byQ = true;
		qId = Integer.parseInt(questionId);
	}
	System.out.println(byQ);
	/*
	 * Covers 1,2,3 
	 */
	HashMap<String, Integer> reviewCountMap ;
	HashMap<String, Double> reviewMapDouble ;
	HashMap<String, Review> reviewMap ;
	
	HashMap<String, ArrayList<Review>> allProdyctReviews ;
	
	 String contentStr ="";
	
	if(byQ) {
		if(qId == 4) {
			reviewCountMap = mongoStore.reviewCountEveryProduct();
			contentStr =printReviewMapAgg(reviewCountMap, "Product", "Review Count");
		} else if(qId == 6) {
			reviewMapDouble = mongoStore.highestPriceInCity(true);
			contentStr =printReviewMapAggDouble(reviewMapDouble, "City", "Highest Price");
		}  else if(qId == 7) {
			reviewMapDouble = mongoStore.highestPriceInCity(false);
			contentStr =printReviewMapAggDouble(reviewMapDouble, "Zip", "Highest Price");
		} else if(qId == 8) {
			allProdyctReviews = mongoStore.topLikedProductsEveryCity(5, true,"");
			contentStr = printReviewMap(allProdyctReviews, true, "City", false, true);
		} else if(qId == 9) {
			allProdyctReviews = mongoStore.reviewsByCity();
			contentStr = printReviewMap(allProdyctReviews, true, "City", false, false);
		}  else if(qId == 10) {
			allProdyctReviews = mongoStore.reviewsByRetailerName();
			contentStr = printReviewMap(allProdyctReviews, true, "City", false, false);
		} else if(qId == 11) {
			reviewCountMap = mongoStore.productsWithRating();
			contentStr =printReviewMapAgg(reviewCountMap, "City", "Count");
		} else if(qId == 12) {
			allProdyctReviews = mongoStore.topLikedProductsEveryCity(0,false,"");
			contentStr = printReviewMap(allProdyctReviews, true, "City", false, true);
		}  else if(qId == 13) {
			reviewMapDouble = mongoStore.medianPriceOfCity();
			contentStr =printReviewMapAggDouble(reviewMapDouble, "City", "Median Price");
		}else if(qId == 14) {
			reviewMap = mongoStore.mostLikedExpensive();
			contentStr =printReviewMap(reviewMap, "City");
		} else if(qId == 15) {
			reviewMap = mongoStore.mostDisLikedInZip();
			contentStr =printReviewMap(reviewMap, "Zip");
		} else if(qId == 16) {
			reviewMap = mongoStore.mostLikedExpensive();
			contentStr =printReviewMap(reviewMap, "City");
		} else if(qId == 17) {
			reviewCountMap = mongoStore.zipCodeWithRating();
			contentStr =printReviewMapAgg(reviewCountMap, "Zip", "Count");
		}else if(qId == 18) {
			allProdyctReviews = mongoStore.reviewsByAge();
			contentStr = printReviewMap(allProdyctReviews, false, "City", true, false);
		} 
		else if(qId == 19) {
			allProdyctReviews = mongoStore.topLikedProductsEveryCity(0,false,"MNAME");
			contentStr = printReviewMap(allProdyctReviews, true, "City", false, true);
		}
		
	} else {
		allProdyctReviews = mongoStore.selectReviewsDyanamically(isNameSelected, productName, 
				isPriceSelected, productPrice, condPrice, 
				isRatingSelected, productRating, condRating, 
				isCitySelected, rCity,
				isSearchSelected, searchTerm);

		contentStr = printReviewMap(allProdyctReviews, false, "Product", true, true);
	}

	store.setBasicWithCSS("styles1");
	store.setContent(contentStr);

	HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
	
	store.setHeader(headerval);

    out.println(store.getWholeHTML());
  }
  
  public String printReviewMap (HashMap<String, ArrayList<Review>> allProdyctReviews, boolean withCount, String param1, boolean isRating, boolean wantTitle) {
	    String contentStr = "<hr><h4 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Data Analysis  </h4> <hr>";
	    
		
	    if(allProdyctReviews == null || allProdyctReviews.isEmpty()) {
	    	System.out.println("NO reviews found");
	    } else {
		
			
			for(Entry<String, ArrayList<Review>> m :allProdyctReviews.entrySet()){
				
				ArrayList<Review> reviews = m.getValue();
		    	if(wantTitle) {
		    		contentStr = contentStr +"      <div class=\"center_title_bar\">"+param1+": "+m.getKey()+"</div>";
		    	}
				contentStr = contentStr 
	    			+"<div class=\"prod_box_big\">"+
		 			"        <div class=\"top_prod_box_big\"></div>"+
		 			"        <div class=\"center_prod_box_big\">"+
		 			"          <div class=\"details_big_box\">";
		 			
					for(Review r: reviews) {
//						System.out.println(r);
		 				contentStr = contentStr +
				 			"            <div class=\"specifications\"> ";
		 						if(withCount) {

		 							contentStr = contentStr +	"       Count: <span class=\"blue\">"+r.getCount()+"</span><br>";
		 						}
		 						contentStr = contentStr +"               Price: <span class=\"blue\">"+r.getPrice()+"</span><br>"+
				 					"Product Name: <span class=\"blue\">"+r.getProductModelName()+"</span><br>";
				 			if(isRating) {
				 				contentStr = contentStr +"               Rating: <span class=\"blue\">"+r.getReviewRating()+"</span><br>"+
				 						"				User: <span class=\"blue\">"+r.getUserName()+"</span><br>"
							 			+ "				User Age: <span class=\"blue\">"+r.getUserAge()+"</span><br>"
							 			+ "				User Gender: <span class=\"blue\">"+r.getUserGender()+"</span><br>"
							 			+ "				User Occupation: <span class=\"blue\">"+r.getUserOccupation()+"</span><br>"+
							 			"				Review: <span class=\"blue\">"+r.getReviewText()+"</span><br>"+
							 			"				Review DAte: <span class=\"blue\">"+r.getReviewDate()+"</span><br>"
							 			;
				 			}
				 			
		 						contentStr = contentStr +"               Category: <span class=\"blue\">"+r.getCategory()+"</span><br>"+
		 		 			"               Retailer Name: <span class=\"blue\">"+r.getRetailerName()+"</span><br>"+
		 		 			"               Retailer Zip: <span class=\"blue\">"+r.getRetailerZip()+"</span><br>"+
		 		 			"               Retailer City: <span class=\"blue\">"+r.getRetailerCity()+"</span><br>"+
		 		 			"               Retailer State: <span class=\"blue\">"+r.getRetailerState()+"</span><br>"+
		 			 		
		 					"               Product on Sale?: <span class=\"blue\">"+r.isProductOnSale()+"</span><br>"+
		 					"               Manufacturer Name: <span class=\"blue\">"+r.getManufacturerName()+"</span><br>"+
		 					"               Manufacturer rebate: <span class=\"blue\">"+r.isManufacturerRebate()+"</span><br>"+
				 			"            </div> <hr>";
		 		
		 			}				
		 			contentStr = contentStr +
					"          </div>"+
		 			"   	</div>     "
		 			+ "</div>";
	    	
			}
	    	
	    }	
	    
	    return contentStr;
  }
  
  public String printReviewMap(HashMap<String,Review> allProdyctReviews, String para1) {
	    String contentStr = "<hr><h4 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Data Analysis  </h4> <hr>";
	    
		
	    if(allProdyctReviews == null || allProdyctReviews.isEmpty()) {
	    	System.out.println("NO reviews found");
	    } else {
		
			
			for(Entry<String, Review> m :allProdyctReviews.entrySet()){
				
				Review rev = m.getValue();
		    	
				contentStr = contentStr +"      <div class=\"center_title_bar\">"+para1+": "+m.getKey()+"</div>";
				contentStr = contentStr 
	    			+"<div class=\"prod_box_big\">"+
		 			"        <div class=\"top_prod_box_big\"></div>"+
		 			"        <div class=\"center_prod_box_big\">"+
		 			"          <div class=\"details_big_box\">"+
		 			
				 			"            <div class=\"specifications\"> "+
				 			"               productName: <span class=\"blue\">"+rev.getProductModelName()+"</span><br>"+
				 			"               Price: <span class=\"blue\">"+rev.getPrice()+"</span><br>"+
				 			"               Retailer Name: <span class=\"blue\">"+rev.getRetailerName()+"</span><br>"+
				 			"            </div> "+
		 		
					"          </div>"+
		 			"   	</div>     "
		 			+ "</div>";
			}
	    	
	    }	
	    
	    return contentStr;
 }
  
  public String printReviewMapAgg(HashMap<String,Integer> allProdyctReviews, String para1, String para2) {
	    String contentStr = "<hr><h4 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Data Analysis  </h4> <hr>";
	    
		
	    if(allProdyctReviews == null || allProdyctReviews.isEmpty()) {
	    	System.out.println("NO reviews found");
	    } else {
		
			
			for(Entry<String, Integer> m :allProdyctReviews.entrySet()){
				
				Integer reviewsCount = m.getValue();
		    	
				contentStr = contentStr +"      <div class=\"center_title_bar\">"+para1+": "+m.getKey()+"</div>";
				contentStr = contentStr 
	    			+"<div class=\"prod_box_big\">"+
		 			"        <div class=\"top_prod_box_big\"></div>"+
		 			"        <div class=\"center_prod_box_big\">"+
		 			"          <div class=\"details_big_box\">"+
		 			
				 			"            <div class=\"specifications\"> "+
				 			"               "+para2+": <span class=\"blue\">"+reviewsCount+"</span><br>"+
				 			"            </div> "+
		 		
					"          </div>"+
		 			"   	</div>     "
		 			+ "</div>";
			}
	    	
	    }	
	    
	    return contentStr;
 }
  
  public String printReviewMapAggDouble(HashMap<String,Double> allProdyctReviews, String para1, String para2) {
	    String contentStr = "<hr><h4 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Data Analysis  </h4> <hr>";
	    
		
	    if(allProdyctReviews == null || allProdyctReviews.isEmpty()) {
	    	System.out.println("NO reviews found");
	    } else {
		
			
			for(Entry<String, Double> m :allProdyctReviews.entrySet()){
				
				Double reviewsCount = m.getValue();
		    	
				contentStr = contentStr +"      <div class=\"center_title_bar\">"+para1+": "+m.getKey()+"</div>";
				contentStr = contentStr 
	    			+"<div class=\"prod_box_big\">"+
		 			"        <div class=\"top_prod_box_big\"></div>"+
		 			"        <div class=\"center_prod_box_big\">"+
		 			"          <div class=\"details_big_box\">"+
		 			
				 			"            <div class=\"specifications\"> "+
				 			"               "+para2+": <span class=\"blue\">"+reviewsCount+"</span><br>"+
				 			"            </div> "+
		 		
					"          </div>"+
		 			"   	</div>     "
		 			+ "</div>";
			}
	    	
	    }	
	    
	    return contentStr;
}
}
