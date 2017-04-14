import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.*;

public class OfferWarranty extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();
  private MySQLDataStoreUtilities mySQLStore = new MySQLDataStoreUtilities();
  
  private static String WARRANTY = "WARRANTY";
  private static String DISCOUNT = "DISCOUNT";
  
  private  HashMap<String, Product> productsFromDb = new HashMap<String, Product>();
  
  public void init() throws ServletException {
	  HashMap<String, Product> products = mySQLStore.fetchAllProducts();
	  productsFromDb = products;
	  store.setProducts(products);
  }
  
  protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	  	String type = request.getParameter("type");
	  	String name = request.getParameter("name");
	    String warranty = "", discountstr = "";
	    double wamount = 0;
	    double discount = 0;

		if(type.equals(WARRANTY)){
			productsFromDb =  mySQLStore.fetchAllProducts();
			warranty = request.getParameter("warranty");
			wamount = Double.parseDouble(warranty) ;
			
			if(productsFromDb == null || productsFromDb.isEmpty()) {
				// do nothing
			} else {
				Product productDb = productsFromDb.get(name);
				productDb.setRwarranty(wamount);
				
				store.setProducts(productsFromDb);
				
				mySQLStore.updateProductRWarranty(name, wamount);
				//store.writeToFile(productsFromDb, "products");
			
			}
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Warranty has been offered."));
		} else if (type.equals(DISCOUNT)) {
			productsFromDb =  mySQLStore.fetchAllProducts();
			discountstr = request.getParameter("discount");
			discount = Double.parseDouble(discountstr) ;
			
			if(productsFromDb == null || productsFromDb.isEmpty()) {
				// do nothing
			} else {
				Product productDb = productsFromDb.get(name);
				productDb.setRdiscount(discount);
				
				store.setProducts(productsFromDb);
				
				mySQLStore.updateProductRDiscount(name, discount);
				//store.writeToFile(productsFromDb, "products");
			
			}
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Discount has been offered."));

		}
	}
	
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	store.setBasicWithCSS("styles1");
	
	HashMap<String, Product> products = mySQLStore.fetchAllProducts();
	   
	String productSelection = "";
	
	if(products == null || products.isEmpty()) {
    	System.out.println("NO products");
    } else {
		for(Entry<String, Product> m :products.entrySet()){
		  	System.out.println(m.getKey());		
		  	Product c = m.getValue();
		  	productSelection = 	productSelection +" <option value=\""+c.getName()+"\">"+c.getName()+"</option>";
		}	
    }

	String content = 
			
			"<div class=\"center_content\">"+
			"      <div class=\"center_title_bar\">Offer Warranty</div>"+
			"      <div class=\"prod_box_big\">"+
			"        <div class=\"top_prod_box_big\"></div>"+
			"        <div class=\"center_prod_box_big\">"+
			"          <div class=\"contact_form\">"+
						"<form action=\"offer?type=WARRANTY\" method=\"post\">"+
			
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Product Name:</strong></label>"+
								"<select name=\"name\">"+ 
								productSelection +
							"  </select>"+
			"            </div>"+
							
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>1 year warranty price:</strong></label>"+
			"              <input type=\"text\" name=\"warranty\" class=\"contact_input\" />"+
			"            </div>"+
						
			"             <button type=\"submit\" value=\"Offer\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Make Offer</button> "+
						"</form>"+
			"          </div>"+
			"        </div>"+
			"        <div class=\"bottom_prod_box_big\"></div>"+
			"      </div>"+
			"    </div> <br>"+
			
			"<div class=\"center_content\">"+
			"      <div class=\"center_title_bar\">Offer Special Discount</div>"+
			"      <div class=\"prod_box_big\">"+
			"        <div class=\"top_prod_box_big\"></div>"+
			"        <div class=\"center_prod_box_big\">"+
			"          <div class=\"contact_form\">"+
						"<form action=\"offer?type=DISCOUNT\" method=\"post\">"+
			
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Product Name:</strong></label>"+
								"<select name=\"name\">"+ 
								productSelection +
							"  </select>"+
			"            </div>"+
							
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Discount in $ : </strong></label>"+
			"              <input type=\"text\" name=\"discount\" class=\"contact_input\" />"+
			"            </div>"+
						
			"             <button type=\"submit\" value=\"Offer\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Make Offer</button> "+
						"</form>"+
			"          </div>"+
			"        </div>"+
			"        <div class=\"bottom_prod_box_big\"></div>"+
			"      </div>"+
			"    </div>";
						
		HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
	
	store.setHeader(headerval);
						
    store.setContent(content);
    out.println(store.getWholeHTML());
  }
}
