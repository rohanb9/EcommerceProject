import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.*;

public class AddProduct extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();

  private MySQLDataStoreUtilities mySQLStore = new MySQLDataStoreUtilities();
  
  private static String ACCESSORY = "ACCESSORY";
  private static String PRODUCT = "PRODUCT";
  private static String ADD = "ADD";
  private static String DELETE = "DELETE";
  private static String UPDATE = "UPDATE";
  
  private  HashMap<String, Product> productsFromDb = new HashMap<String, Product>();
  
  public void init() throws ServletException {
	  HashMap<String, Product> products = mySQLStore.fetchAllProducts();;
	  productsFromDb = products;
	  store.setProducts(products);
  }
  

  public void prinProducttMap(HashMap<String, Product> mapInFile) throws ServletException {
	  
      for(Entry<String, Product> m :mapInFile.entrySet()){
			  	System.out.println(m.getKey());
			Product c = m.getValue();
			System.out.println("\t Name : "+c.getName());
			System.out.println("\t Price : "+c.getPrice());
			System.out.println("\t Accessories : ");
			HashMap<String,Accessory> accessories = c.getAccessories();
			for(Entry<String, Accessory> ma :accessories.entrySet()){
				System.out.println("\t\t\t" + ma.getKey());
				Accessory a = ma.getValue();
				System.out.println("\t\t\t\t Name : "+a.getName());
				System.out.println("\t\t\t\t Price : "+a.getPrice());
			}
			
	   }
  }
  
  protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	  	String type = request.getParameter("type");
	    String op = request.getParameter("op");
		// get request parameters for userID and password
		String name = request.getParameter("name");
		String newname = request.getParameter("newname");
		String accname = request.getParameter("accname");
		int rZip = 60616; 
		YesNo productOnSale;
		
		double price = 0, discount = 0;
		String newaccname = "", priceString= "", discountString= "";
		Category category = Category.OTHER;

		if(!op.equals(DELETE)){
			category = Category.fromString(request.getParameter("category"));
			newaccname = request.getParameter("newaccname");
			priceString = request.getParameter("price");
			discountString = request.getParameter("discount");
			
			if(priceString == null || priceString == "" || discountString == null || discountString == "") {
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Invalid-price-or-discount-TRY-AGAIN"));

			}

			 price = Double.parseDouble(priceString) ;
			 discount = Double.parseDouble(discountString);
		}

		if(name == null || name == "") {
			System.out.println("Product name required..");
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?Product-name-required-TRY-AGAIN"));
		} else {
			productsFromDb = mySQLStore.fetchAllProducts();
			
			if(op.equals(ADD) && type.equals(PRODUCT)) {
				String retailer = request.getParameter("retailer");
				String retailerCity = request.getParameter("retailerCity");
				String retailerState = request.getParameter("retailerState");
				String manufacturerName = request.getParameter("manufacturerName");
				String retailerZip = request.getParameter("retailerZip");
				productOnSale = YesNo.fromString(request.getParameter("productOnSale"));
				if(!retailerZip.isEmpty()) {
					rZip = Integer.parseInt(retailerZip);
				}
				
				System.out.println("PRODUCT.. ADD");
				Product productObj = new Product(name, price, discount, category, retailer, rZip, retailerCity, retailerState, productOnSale, manufacturerName);
				System.out.println("category" + category +  request.getParameter("category"));
				

				if(productsFromDb == null || productsFromDb.isEmpty()) {
					productsFromDb = new HashMap<String, Product>();
				} else {
					//prinUsertMap(usersFromDb);
				}
				productsFromDb.put(name, productObj);
				store.setProducts(productsFromDb);
				
				mySQLStore.insertProduct(productObj);
				//store.writeToFile(productsFromDb, "products");
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Product-"+name+"-added-successfully"));
			
			} else if(op.equals(DELETE) && type.equals(PRODUCT)) {
				System.out.println("PRODUCT..DELETE");
				
				if(productsFromDb == null || productsFromDb.isEmpty()) {
					productsFromDb = new HashMap<String, Product>();
				} else {
					//prinUsertMap(usersFromDb);
					productsFromDb.remove(name);
				}
				store.setProducts(productsFromDb);
				
				mySQLStore.deleteProduct(name);
				//store.writeToFile(productsFromDb, "products");
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Product-"+name+"-deleted-successfully"));
			
			} else if(op.equals(UPDATE) && type.equals(PRODUCT)) {

				String retailer = request.getParameter("retailer");
				String retailerCity = request.getParameter("retailerCity");
				String retailerState = request.getParameter("retailerState");
				String manufacturerName = request.getParameter("manufacturerName");
				String retailerZip = request.getParameter("retailerZip");
				productOnSale = YesNo.fromString(request.getParameter("productOnSale"));
				if(!retailerZip.isEmpty()) {
					rZip = Integer.parseInt(retailerZip);
				}
				
				
				System.out.println("PRODUCT..UPDATE");
				if(newname == null || newname == "") {
					System.out.println("Product new name required..");
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Product-new-name-required-TRY-AGAIN"));
				}
				Product productObj = new Product(newname, price, discount, category, retailer, rZip, retailerCity, retailerState, productOnSale, manufacturerName);
				

				if(productsFromDb == null || productsFromDb.isEmpty()) {
					productsFromDb = new HashMap<String, Product>();
				} else {
					//prinUsertMap(usersFromDb);
					Product productDb = productsFromDb.get(name);
					productObj.setAccessories(productDb.getAccessories());
					productsFromDb.remove(name);
					productsFromDb.put(newname, productObj);
				}
				store.setProducts(productsFromDb);
				mySQLStore.updateProduct(name, productObj);
				// 		store.writeToFile(productsFromDb, "products");
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Product-"+name+"-updated-successfully"));
			
			} else if((op.equals(ADD) && type.equals(ACCESSORY))) {
				
				if((accname == "" && accname == null)) {
					System.out.println("Accessory name required..");
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Accessory-name-required-TRY-AGAIN"));
				} else {
					System.out.println("PRODUCT.. ACCESSORY...ADD" +name+ accname + price + discount);
					Accessory accessoryObj = new Accessory(accname, price, discount);
					
					Product productFromMap = productsFromDb.get(name);

					HashMap<String, Accessory> accessoryMap = productFromMap.getAccessories();
					accessoryMap.put(accname, accessoryObj);
					productFromMap.setAccessories(accessoryMap);

					productsFromDb.put(name, productFromMap);
					store.setProducts(productsFromDb);
					
					mySQLStore.insertAccessory(accessoryObj, name);
					//store.writeToFile(productsFromDb, "products");
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Accessory-"+accname+"-added-successfully"));
				}
			
			} else if((op.equals(DELETE) && type.equals(ACCESSORY))) {
				
				if((accname == "" && accname == null)) {
					System.out.println("Accessory name required..");
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Accessory-name-required-TRY-AGAIN"));
				} else {
					System.out.println("PRODUCT.. ACCESSORY...DELETE");
					
					Product productFromMap = productsFromDb.get(name);
					HashMap<String, Accessory> accessoryMap = productFromMap.getAccessories();
					accessoryMap.remove(accname);
					productFromMap.setAccessories(accessoryMap);

					productsFromDb.put(name, productFromMap);
					
					store.setProducts(productsFromDb);
					
					mySQLStore.deleteAccessory(accname);
					// 			store.writeToFile(productsFromDb, "products");
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Accessory-"+accname+"-deleted-successfully"));
				}
			} else if((op.equals(UPDATE) && type.equals(ACCESSORY))) {
				
				if((accname == "" || accname == null || newaccname == "" || newaccname == null)) {
					System.out.println("Accessory name required..");
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Accessory-name-required-TRY-AGAIN"));
				} else {
					Accessory accessoryObj = new Accessory(newaccname, price, discount);
					
					System.out.println("PRODUCT.. ACCESSORY...UPDATE");
					
					Product productFromMap = productsFromDb.get(name);
					HashMap<String, Accessory> accessoryMap = productFromMap.getAccessories();
					accessoryMap.remove(accname);
					accessoryMap.put(newaccname, accessoryObj);
					productFromMap.setAccessories(accessoryMap);

					//				productsFromDb.put(name, productObj);
					store.setProducts(productsFromDb);
					
					mySQLStore.updateAccessory(accname, accessoryObj);
					// 			store.writeToFile(productsFromDb, "products");
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount?"+"Accessory-"+accname+"-updated-successfully"));
				}
			} 
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
			"      <div class=\"center_title_bar\">Add Product</div>"+
			"      <div class=\"prod_box_big\">"+
			"        <div class=\"top_prod_box_big\"></div>"+
			"        <div class=\"center_prod_box_big\">"+
			"          <div class=\"contact_form\">"+
						"<form action=\"products?op=ADD&type=PRODUCT\" method=\"post\">"+
			
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Name:</strong></label>"+
			"              <input type=\"text\" name=\"name\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Category:</strong></label>"+
							"<select name=\"category\">"+
							"    <option value=\"OTHER\">Other</option>"+
							"    <option value=\"SMARTPHONES\">Smart Phones</option>"+
							"    <option value=\"LAPTOPS\">Laptops</option>"+
							"    <option value=\"TV\">TV</option>"+
							"  </select>"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Price:</strong></label>"+
			"              <input type=\"text\" name=\"price\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Discount:</strong></label>"+
			"              <input type=\"text\" name=\"discount\" class=\"contact_input\" />"+
			"            </div>"+
			
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>manufacturerName:</strong></label>"+
			"              <input type=\"text\" name=\"manufacturerName\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>productOnSale?:</strong></label>"+
							"<select name=\"productOnSale\">"+
							"    <option value=\"NO\">NO</option>"+
							"    <option value=\"YES\">YES</option>"+
							"  </select>"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>retailer Name:</strong></label>"+
			"              <input type=\"text\" name=\"retailer\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>retailerZip:</strong></label>"+
			"              <input type=\"text\" name=\"retailerZip\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>retailerCity:</strong></label>"+
			"              <input type=\"text\" name=\"retailerCity\" class=\"contact_input\" />"+
			"            </div>"+		
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>retailerState:</strong></label>"+
			"              <input type=\"text\" name=\"retailerState\" class=\"contact_input\" />"+
			"            </div>"+			
			
			
			"             <button type=\"submit\" value=\"Add\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Add</button> "+
						"</form>"+
			"          </div>"+
			"        </div>"+
			"        <div class=\"bottom_prod_box_big\"></div>"+
			"      </div>"+
			"    </div>"+

			"<div class=\"center_content\">"+
			"      <div class=\"center_title_bar\">Update Product</div>"+
			"      <div class=\"prod_box_big\">"+
			"        <div class=\"top_prod_box_big\"></div>"+
			"        <div class=\"center_prod_box_big\">"+
			"          <div class=\"contact_form\">"+
						"<form action=\"products?op=UPDATE&type=PRODUCT\" method=\"post\">"+
			
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Name:</strong></label>"+
						"<select name=\"name\">"+ 
						productSelection +
					"  </select>"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>New Name:</strong></label>"+
			"              <input type=\"text\" name=\"newname\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Category:</strong></label>"+
							"<select name=\"category\">"+
							"    <option value=\"OTHER\">Other</option>"+
							"    <option value=\"SMARTPHONES\">Smart Phones</option>"+
							"    <option value=\"LAPTOPS\">Laptops</option>"+
							"    <option value=\"TV\">TV</option>"+
							"  </select>"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Price:</strong></label>"+
			"              <input type=\"text\" name=\"price\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Discount:</strong></label>"+
			"              <input type=\"text\" name=\"discount\" class=\"contact_input\" />"+
			"            </div>"+
			
		"            <div class=\"form_row\">"+
		"              <label class=\"contact\"><strong>manufacturerName:</strong></label>"+
		"              <input type=\"text\" name=\"manufacturerName\" class=\"contact_input\" />"+
		"            </div>"+
		"            <div class=\"form_row\">"+
		"              <label class=\"contact\"><strong>productOnSale?:</strong></label>"+
						"<select name=\"productOnSale\">"+
						"    <option value=\"NO\">NO</option>"+
						"    <option value=\"YES\">YES</option>"+
						"  </select>"+
		"            </div>"+
		"            <div class=\"form_row\">"+
		"              <label class=\"contact\"><strong>retailer Name:</strong></label>"+
		"              <input type=\"text\" name=\"retailer\" class=\"contact_input\" />"+
		"            </div>"+
		"            <div class=\"form_row\">"+
		"              <label class=\"contact\"><strong>retailerZip:</strong></label>"+
		"              <input type=\"text\" name=\"retailerZip\" class=\"contact_input\" />"+
		"            </div>"+
		"            <div class=\"form_row\">"+
		"              <label class=\"contact\"><strong>retailerCity:</strong></label>"+
		"              <input type=\"text\" name=\"retailerCity\" class=\"contact_input\" />"+
		"            </div>"+		
		"            <div class=\"form_row\">"+
		"              <label class=\"contact\"><strong>retailerState:</strong></label>"+
		"              <input type=\"text\" name=\"retailerState\" class=\"contact_input\" />"+
		"            </div>"+	
						
			"             <button type=\"submit\" value=\"Add\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Update</button> "+
						"</form>"+
			"          </div>"+
			"        </div>"+
			"        <div class=\"bottom_prod_box_big\"></div>"+
			"      </div>"+
			"    </div>"+
			
				"<div class=\"center_content\">"+
				"      <div class=\"center_title_bar\">Delete Product</div>"+
				"      <div class=\"prod_box_big\">"+
				"        <div class=\"top_prod_box_big\"></div>"+
				"        <div class=\"center_prod_box_big\">"+
				"          <div class=\"contact_form\">"+
							"<form action=\"products?op=DELETE&type=PRODUCT\" method=\"post\">"+
				
				"            <div class=\"form_row\">"+
				"              <label class=\"contact\"><strong>Name:</strong></label>"+
									"<select name=\"name\">"+ 
									productSelection +
								"  </select>"+
				"            </div>"+
							
				"             <button type=\"submit\" value=\"Delete\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Delete</button> "+
							"</form>"+
				"          </div>"+
				"        </div>"+
				"        <div class=\"bottom_prod_box_big\"></div>"+
				"      </div>"+
				"    </div>"+


			"<div class=\"center_content\">"+
			"      <div class=\"center_title_bar\">Add Accessory</div>"+
			"      <div class=\"prod_box_big\">"+
			"        <div class=\"top_prod_box_big\"></div>"+
			"        <div class=\"center_prod_box_big\">"+
			"          <div class=\"contact_form\">"+
						"<form action=\"products?op=ADD&type=ACCESSORY\" method=\"post\">"+
			
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Product Name:</strong></label>"+
								"<select name=\"name\">"+ 
								productSelection +
							"  </select>"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Accessory Name:</strong></label>"+
			"              <input type=\"text\" name=\"accname\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Accessory Price:</strong></label>"+
			"              <input type=\"text\" name=\"price\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Accessory Discount:</strong></label>"+
			"              <input type=\"text\" name=\"discount\" class=\"contact_input\" />"+
			"            </div>"+
						
			"             <div><button type=\"submit\" value=\"Add\"  style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Add</button> </div>"+
						"</form>"+
			"          </div>"+
			"        </div>"+
			"        <div class=\"bottom_prod_box_big\"></div>"+
			"      </div>"+
			"    </div>"+

			"<div class=\"center_content\">"+
			"      <div class=\"center_title_bar\">Update Accessory</div>"+
			"      <div class=\"prod_box_big\">"+
			"        <div class=\"top_prod_box_big\"></div>"+
			"        <div class=\"center_prod_box_big\">"+
			"          <div class=\"contact_form\">"+
						"<form action=\"products?op=UPDATE&type=ACCESSORY\" method=\"post\">"+
			
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Product Name:</strong></label>"+
								"<select name=\"name\">"+ 
								productSelection +
							"  </select>"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Accessory Name:</strong></label>"+
			"              <input type=\"text\" name=\"accname\" class=\"contact_input\" />"+
			"            </div>"+

			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>New Accessory Name:</strong></label>"+
			"              <input type=\"text\" name=\"newaccname\" class=\"contact_input\" />"+
			"            </div>"+

			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Accessory Price:</strong></label>"+
			"              <input type=\"text\" name=\"price\" class=\"contact_input\" />"+
			"            </div>"+
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Accessory Discount:</strong></label>"+
			"              <input type=\"text\" name=\"discount\" class=\"contact_input\" />"+
			"            </div>"+
						
			"             <div><button type=\"submit\" value=\"Add\"  style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Update</button> </div>"+
						"</form>"+
			"          </div>"+
			"        </div>"+
			"        <div class=\"bottom_prod_box_big\"></div>"+
			"      </div>"+
			"    </div>"+
			
					"<div class=\"center_content\">"+
					"      <div class=\"center_title_bar\">Delete Accessory</div>"+
					"      <div class=\"prod_box_big\">"+
					"        <div class=\"top_prod_box_big\"></div>"+
					"        <div class=\"center_prod_box_big\">"+
					"          <div class=\"contact_form\">"+
								"<form action=\"products?op=DELETE&type=ACCESSORY\" method=\"post\">"+
					
					"            <div class=\"form_row\">"+
					"              <label class=\"contact\"><strong>Name:</strong></label>"+
										"<select name=\"name\">"+ 
										productSelection +
									"  </select>"+
					"            </div>"+
									
					"            <div class=\"form_row\">"+
					"              <label class=\"contact\"><strong>Accessory Name:</strong></label>"+
					"              <input type=\"text\" name=\"accname\" class=\"contact_input\" />"+
					"            </div>"+
								
					"             <button type=\"submit\" value=\"Delete\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Delete</button> "+
								"</form>"+
					"          </div>"+
					"        </div>"+
					"        <div class=\"bottom_prod_box_big\"></div>"+
					"      </div>"+
					"    </div>"
					   ;
						
		HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
	
	store.setHeader(headerval);
						
    store.setContent(content);
    out.println(store.getWholeHTML());
  }
}
