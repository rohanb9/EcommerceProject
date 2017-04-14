import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;

public class DealMatches {
	private MySQLDataStoreUtilities mySQLStore = new MySQLDataStoreUtilities();

	public String matchAllDeals() throws ServletException, IOException {
		String contentStr = "";
		HashMap<String,Product> selectedproducts=new HashMap<String,Product>();
		HashMap<String,Product> productmap= mySQLStore.fetchAllProducts();
		
		try
		{
			contentStr = contentStr +"      <div class=\"center_title_bar\">Welcome to JustBuy </div>";
			contentStr = contentStr 
	    			+"<div class=\"prod_box_big\">"+
		 			"        <div class=\"top_prod_box_big\"></div>"+
		 			"        <div class=\"center_prod_box_big\">"+
		 			"          <div class=\"details_big_box\">"+
		 			"<h2>We beat our competitors in all aspects. Price-Match Guaranteed</h2>";
					;
					
			String line=null;
			
				for(Map.Entry<String, Product> entry : productmap.entrySet())
			{
				if(selectedproducts.size()<5 && !selectedproducts.containsKey(entry.getKey()))
				{
					BufferedReader reader = new BufferedReader(new FileReader(new File("H:\\csj\\DealMatches.txt")));
					line=reader.readLine();
					if(line==null)
					{ 
						contentStr = contentStr + "<h2 align='center'>No Offers Found</h2>";
						break;
					}
					else {
						do {
							if(line.contains(entry.getKey()))
							{
								contentStr = contentStr + "<h3>"+line+"</h3><br>";
								selectedproducts.put(entry.getKey(),entry.getValue());
								break;
							}
						}while((line = reader.readLine()) != null);
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("ERROR");
		}
		
		contentStr = contentStr + "</div>"+"</div>"+"</div><br>";
		contentStr = contentStr +"      <div class=\"center_title_bar\">Deal Matches </div>";
			
		
		
		 for(Entry<String, Product> m :selectedproducts.entrySet()){
			  	System.out.println(m.getKey());		
			  	Product c = m.getValue();
			  		 contentStr = contentStr +
			 				"<div class=\"prod_box_big\">"+
			 			"        <div class=\"top_prod_box_big\"></div>"+
			 			"        <div class=\"center_prod_box_big\">"+
			 			"          <div class=\"product_img_big\"> <a href=\"javascript:popImage('images/"+c.getCategory()+".jpg','Some Title')\" title=\"\"><img width=\"150\" height=\"150\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a>"+
			 			"            <div class=\"thumbs\"> <a href=\"#\" title=\"\"><img width=\"30\" height=\"30\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a> <a href=\"#\" title=\"header=[Thumb2] body=[ ] fade=[on]\"><img width=\"30\" height=\"30\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a> <a href=\"#\" title=\"header=[Thumb3] body=[ ] fade=[on]\"><img width=\"30\" height=\"30\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a> </div>"+
			 			"          </div>"+
			 			"          <div class=\"details_big_box\">"+
			 			"            <div class=\"product_title_big\">"+m.getKey()+"</div>"+
			 			"            <div class=\"specifications\"> Category: <span class=\"blue\">"+c.getCategory()+"</span><br>"+
			 			"              Spectial discount: <span class=\"blue\">$"+c.getRdiscount()+"</span><br>"+
			 			"              Warranty Offered: <span class=\"blue\">$"+c.getRwarranty()+"</span><br>"+
			 			"              Discount: <span class=\"blue\">$"+c.getDiscount()+"</span><br>"+
			 			"            </div>"+
			 			"            <div class=\"prod_price_big\"> price : <span class=\"price\">$"+c.getPrice()+"</span></div>"+
						 			 "<form action=\"/csj/cart\" method = \"post\">"+
						 			"  <input type=\"hidden\" name=\"name\" value= \""+c.getName()+"\">"+
						 			"  <input type=\"hidden\" name=\"price\" value=\""+c.getPrice()+"\">"+

						 			"  <input type=\"hidden\" name=\"discount\" value=\""+c.getDiscount()+"\">"+
						 			"  <button type = \"submit\" value= \"Add To Cart\" class=\"addtocart\">Add To Cart</button>"+
						 			"</form>"+
						 			 "<form action=\"/csj/preview\" method = \"post\">"+
							 			"  <input type=\"hidden\" name=\"name\" value= \""+c.getName()+"\">"+
							 			"  <input type=\"hidden\" name=\"price\" value=\""+c.getPrice()+"\">"+

						 			"  <input type=\"hidden\" name=\"rdiscount\" value= \""+c.getRdiscount()+"\">"+
						 			"  <input type=\"hidden\" name=\"rwarranty\" value=\""+c.getRwarranty()+"\">"+
							 			"  <input type=\"hidden\" name=\"discount\" value=\""+c.getDiscount()+"\">"+
							 			"  <button type = \"submit\" value= \"Add To Cart\" class=\"addtocart\">Save To Order</button>"+
							 			"</form>"+
							 		"  <a href=\"/csj/review?pname="+m.getKey()+"\"><button type = \"submit\" value= \"Review Product\" class=\"addtocart\">Review Product</button></a>"+
							 		 "<a href=\"/csj/viewreview?pname="+m.getKey()+"\"><button type = 'submit' value= 'View Reviews' class='addtocart'>View Reviews</button></a>"+
										
						"             </div>"+
			 			"        </div>";
			 			  	HashMap<String,Accessory> accessories = c.getAccessories();
			 			  
			 				
			 	
			 						contentStr = contentStr +	"        <div class=\"bottom_prod_box_big\"></div>"+
			 							"      </div>";
	    }		
	
		
		return contentStr;
		
	}
}
