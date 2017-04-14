import java.io.*; 
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;


public class TrendingPage extends HttpServlet {
  
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

    //mongoStore.topLikedProductsMethod2();
    HashMap<String, Integer> reviewCountMap1 = mongoStore.topLikedProductsMethod1();
    HashMap<String, Integer> reviewCountMap2 = mongoStore.topZipCodesByReviewCount();
    HashMap<String, Integer> reviewCountMap3 = mongoStore.topProductsByReviewCount();
    
    String contentStr = "";
    contentStr =contentStr + printReviewMapAgg(reviewCountMap1, "Product", "Count", "Top five most liked products");
    contentStr =contentStr + printReviewMapAgg(reviewCountMap2, "Zip Code", "Count", "Top five zip-codes where maximum number of products reviewed");
    contentStr =contentStr + printReviewMapAgg(reviewCountMap3, "Product", "Count", "Top five most reviewed products regardless of the rating");
	
	
	store.setBasicWithCSS("styles1");
	store.setContent(contentStr);

	HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
	
	store.setHeader(headerval);

    out.println(store.getWholeHTML());
  }
  
  public String printReviewMapAgg(HashMap<String,Integer> allProdyctReviews, String para1, String para2, String title) {
	    String contentStr = "<div class=\"center_content\"><div><br><hr><h4 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Trending : "+title+"  </h4> <hr></div>";
		
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
			contentStr = contentStr + "</div>";
	    	
	    }	
	    
	    return contentStr;
 }
  
}
