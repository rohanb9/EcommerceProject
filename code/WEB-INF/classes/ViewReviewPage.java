import java.io.*; 
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;


public class ViewReviewPage extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();
  
  
  public void prinProducttMap(HashMap<String, Product> mapInFile) throws ServletException {
	  
  }
  
  
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
    CommonUtils store = new CommonUtils();

	MongoDBDataStoreUtilities mongoStore = new MongoDBDataStoreUtilities();
		
		String productName = "",condPrice = "", condRating="", rCity = "", groupbyString="", groupbyAgg = "";
	    double productPrice = 0.0;
	    int productRating = 0;
	    		
	productName = request.getParameter("pname");
			
		/*
		 * Covers 1,2,3 
		 */
		
		HashMap<String, ArrayList<Review>> allProdyctReviews ;
		
		String contentStr ="";
		
		
	allProdyctReviews = mongoStore.selectReviewsDyanamically(true, productName, 
			false, productPrice, condPrice, 
			false, productRating, condRating, 
			false, rCity,
			false, "");

	contentStr = printReviewMap(allProdyctReviews, false, "Product", true, true);
	store.setBasicWithCSS("styles1");
	store.setContent(contentStr);

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

}
