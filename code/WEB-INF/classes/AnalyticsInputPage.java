import java.io.*; 
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;


public class AnalyticsInputPage extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();
  private MongoDBDataStoreUtilities mongoStore = new MongoDBDataStoreUtilities();
  private MySQLDataStoreUtilities mySQLStore = new MySQLDataStoreUtilities();
  
  public void prinProducttMap(HashMap<String, Product> mapInFile) throws ServletException {
	  
		//      for(Entry<String, Product> m :mapInFile.entrySet()){
		//			  	System.out.println(m.getKey());
		//			Product c = m.getValue();
		//			System.out.println("\t Name : "+c.getName());
		//			System.out.println("\t Price : "+c.getPrice());
		//			System.out.println("\t Accessories : ");
		//			HashMap<String,Accessory> accessories = c.getAccessories();
		//			for(Entry<String, Accessory> ma :accessories.entrySet()){
		//				System.out.println("\t\t\t" + ma.getKey());
		//				Accessory a = ma.getValue();
		//				System.out.println("\t\t\t\t Name : "+a.getName());
		//				System.out.println("\t\t\t\t Price : "+a.getPrice());
		//			}
		//			
		//	   }
  }
  
  protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		HashMap<String, Product> allProducts = mySQLStore.fetchAllProducts();
		String productName = request.getParameter("pname");
		
		Product product = allProducts.get(productName);
		
		String userName = request.getParameter("uname");
		String uage = request.getParameter("uage");
		Gender userGender = Gender.fromString(request.getParameter("ugender"));
		String userOccupation = request.getParameter("uoccupation");
		String urating = request.getParameter("urating");
		String reviewText = request.getParameter("ureviewtext");
		
		Date reviewDate = new Date();
		
		int userRating = Integer.parseInt(urating);
		int userAge = 18;
		if(!uage.isEmpty()) {
			userAge = Integer.parseInt(uage);
		}
		
		Review reviewObj = new Review(product, userName, userAge, userGender, userOccupation, userRating, reviewDate, reviewText);
		
		System.out.println(reviewObj);
		mongoStore.insertReview(reviewObj);
		//HashMap<String, ArrayList<Review>> reviews = mongoStore.selectReview();
		//mongoStore.selectReview();
		
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/home?review-submitted."));
	}
	
  
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	  PrintWriter out = response.getWriter();
	    
	    String productName = request.getParameter("pname");
	    
	    HashMap<String, Product> allProducts = mySQLStore.fetchAllProducts();
	    
	    Product product = allProducts.get(productName);
	    HttpSession session = request.getSession();
	    
	    String contentStr = "<hr><h1 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Review Product</h1> <hr>";
	    if(allProducts == null || allProducts.isEmpty()) {
	    	System.out.println("NO products found");
	    } else {
	    	String productSelection = " <option value=\"ALL\">ALL</option>";
	    	if(allProducts == null || allProducts.isEmpty()) {
	        	System.out.println("NO products");
	        } else {
	    		for(Entry<String, Product> m :allProducts.entrySet()){
	    		  	Product c = m.getValue();
	    		  	productSelection = 	productSelection +" <option value=\""+c.getName()+"\">"+c.getName()+"</option>";
	    		}	
	        }
		
	    	
	    	contentStr = contentStr + 	
	    			"<div class=\"center_content\">"+
	    			"      <div class=\"center_title_bar\">Analytics</div>"+
	    			"      <div class=\"prod_box_big\">"+
	    			"        <div class=\"top_prod_box_big\"></div>"+
	    			"        <div class=\"center_prod_box_big\">"+
	    			"          <div class=\"contact_form\">"+
	    						"<form action=\"/csj/analytics/result\" method=\"get\">"+
	    			
	    			"            <div class=\"form_row\">"+
	    			"             <input type=\"checkbox\" name=\"isNameSelected\" value=\"true\">Select"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
					"              <label class=\"contact\"><strong>Product Name:</strong></label>"+
										"<select name=\"productName\">"+ 
										productSelection +
									"  </select>"+
					"            </div>"
					+ " <br><hr>"+
					
					"            <div class=\"form_row\">"+
					"             <input type=\"checkbox\" name=\"isPriceSelected\" value=\"true\">Select"+
					"            </div>"+
					"            <div class=\"form_row\">"+
					"              <label class=\"contact\"><strong>Price :</strong></label>"+
	    			"              <input type=\"text\" name=\"productPrice\" value =\"0\"  />"+
					"            </div>"+
					"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"condPrice\" value =\"EQUALS\"  checked/>EQUALS"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"condPrice\" value =\"GREATER\" />GREATER"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"condPrice\" value =\"LESS\"  />LESS"+
	    			"            </div>"+
					
					" <br><hr>"+
					
					"            <div class=\"form_row\">"+
					"             <input type=\"checkbox\" name=\"isRatingSelected\" value=\"true\">Select"+
					"            </div>"+
					"            <div class=\"form_row\">"+
					"              <label class=\"contact\"><strong>Review Rating :</strong></label>"+
									"<select name=\"productRating\">"+
									"    <option value=\"1\">1</option>"+
									"    <option value=\"2\">2</option>"+
									"    <option value=\"3\">3</option>"+
									"    <option value=\"4\">4</option>"+
									"    <option value=\"5\">5</option>"+
									"  </select>"+
					"            </div>"+
					"            <div class=\"form_row\">"+
					"              <input type=\"radio\" name=\"condRating\" value =\"EQUALS\"  checked/>EQUALS"+
					"            </div>"+
					"            <div class=\"form_row\">"+
					"              <input type=\"radio\" name=\"condRating\" value =\"GREATER\"  />GREATER"+
					"            </div>"+
					" <br><hr>"+
									
					"            <div class=\"form_row\">"+
					"             <input type=\"checkbox\" name=\"isCitySelected\" value=\"true\">Select"+
					"            </div>"+
					"            <div class=\"form_row\">"+
					"              <label class=\"contact\"><strong>Retailer City :</strong></label>"+
					"              <input type=\"text\" name=\"rCity\" class=\"contact_input\" />"+
					"            </div>"+
					
					"            <div class=\"form_row\">"+
					"             <input type=\"checkbox\" name=\"isSearchSelected\" value=\"true\">Select"+
					"            </div>"+
					"            <div class=\"form_row\">"+
					"              <label class=\"contact\"><strong>Search Term for reviews text:</strong></label>"+
					"              <input type=\"text\" name=\"searchTerm\" class=\"contact_input\" />"+
					"            </div>"+
	    			
	    			"             <button type=\"submit\" value=\"Submit\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Submit</button> "+
	    						"</form>"+
	    			"          </div>"+
	    			"        </div>"+
	    			"        <div class=\"bottom_prod_box_big\"></div>"+
	    			"      </div>"+
	    			"    </div>";
	    	
	    	contentStr = contentStr + 	
	    			"<div class=\"center_content\">"+
	    			"      <div class=\"center_title_bar\">Data Analytics</div>"+
	    			"      <div class=\"prod_box_big\">"+
	    			"        <div class=\"top_prod_box_big\"></div>"+
	    			"        <div class=\"center_prod_box_big\">"+
	    			"          <div class=\"contact_form\">"+
	    						"<form action=\"/csj/analytics/result\" method=\"get\">"+
	    			
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"4\"  />Print a list of how many reviews for every product"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"6\"  />Find highest price product reviewed in every city"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"7\"  />Find highest price product reviewed in every zip-code"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"8\"  />Get the top 5 list of liked products for every city"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"9\"  />Print a list of reviews grouped by City"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"10\" /> Print a list of reviews grouped by RetailerName"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"11\" />Get the total number of products reviewed and got Rating 5 in Every City"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"12\"  />shows a list of most liked product in every city"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"13\"  /> Print the median product prices per city"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"14\"  />Get top 5 list of most liked and expensive products sorted by retailer name for every city"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"15\"  />Get the top 5 list of most Disliked products sorted by retailer name for every city"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"16\"  />Get the top 5 list of most Disliked products sorted by retailer name for every zip-code"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"17\"  /> Get the top 2 list of zip-codes where highest number of products got review rating 5"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"18\"  />Get a list of reviews where reviewer age greater than 50 and the list is sorted by age in every city"+
	    			"            </div>"+
	    			"            <div class=\"form_row\">"+
	    			"              <input type=\"radio\" name=\"qId\" value =\"19\"  />Get the top 5 list of most liked products sorted by manufacturer name for every city"+
	    			"            </div>"+
	    		
	    			
	    			"             <button type=\"submit\" value=\"Submit\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Submit</button> "+
	    						"</form>"+
	    			"          </div>"+
	    			"        </div>"+
	    			"        <div class=\"bottom_prod_box_big\"></div>"+
	    			"      </div>"+
	    			"    </div>";
	    	
	    	
	    }			

		store.setBasicWithCSS("styles1");
		store.setContent(contentStr);

		String headerval = (String)session.getAttribute("headerval");
		
		store.setHeader(headerval);

	    out.println(store.getWholeHTML());
  }
}
