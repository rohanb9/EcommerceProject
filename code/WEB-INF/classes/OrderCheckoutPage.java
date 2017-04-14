import java.io.*; 
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;


public class OrderCheckoutPage extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();
  

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
		HashMap<String, Product> cartproducts = (HashMap<String, Product>)session.getAttribute("salesmanSavedOrder");
		HashMap<String, Accessory> cartacc = (HashMap<String, Accessory>)session.getAttribute("salesmanAccOrder");
		
		String act = request.getParameter("act");
		String name = request.getParameter("name");
		String priceString = request.getParameter("price");
		String discountString = request.getParameter("discount");

  	if(act != null && act.equals("removewarranty")) {
			if(cartproducts == null || cartproducts.isEmpty()) {
				cartproducts = new HashMap<String, Product>();
			} else {
				Product p = cartproducts.get(name);
				p.setRwarranty(0.0);
				cartproducts.put(name, p);
				session.setAttribute("salesmanSavedOrder", cartproducts);
			System.out.println("remove warranty");
			}
			
		} else if(act != null && act.equals("remove")) {
			if(cartproducts == null || cartproducts.isEmpty()) {
				cartproducts = new HashMap<String, Product>();
			} 
			cartproducts.remove(name);
			session.setAttribute("salesmanSavedOrder", cartproducts);
			System.out.println("remove product");
			
		} else if (act != null && act.equals("removeacc")) {
			if(cartacc == null || cartacc.isEmpty()) {
				cartacc = new HashMap<String, Accessory>();
			} else {

				String accname = request.getParameter("accname");
				cartacc.remove(accname);
				
			}
			session.setAttribute("salesmanSavedOrder", cartacc);
			System.out.println("remove accessory.");
			
		}  else if (act != null && act.equals("addacc")) {
			if(cartacc == null || cartacc.isEmpty()) {
				cartacc = new HashMap<String, Accessory>();
			} 
			
			String accname = request.getParameter("accname");
			double price = Double.parseDouble(priceString) ;
			double discount = Double.parseDouble(discountString);

				
			Accessory a = new Accessory(accname,price, discount);
			
			 cartacc.put(accname, a);
			
			session.setAttribute("salesmanAccOrder", cartacc);
			System.out.println("Add acc");
			
		} else {
			if(cartproducts == null || cartproducts.isEmpty()) {
				cartproducts = new HashMap<String, Product>();
			} 
			

			String rwarrantyString = request.getParameter("rwarranty");
			String rdiscountString = request.getParameter("rdiscount");

			double price = Double.parseDouble(priceString) ;
			double discount = Double.parseDouble(discountString);
			double warranty = Double.parseDouble(rwarrantyString) ;
			double specialdiscount = Double.parseDouble(rdiscountString);
		
	
			Product cartp = new Product(name, price, discount);
			cartp.setRdiscount(specialdiscount);
			cartp.setRwarranty(warranty);
			cartproducts.put(name, cartp);
			session.setAttribute("salesmanSavedOrder", cartproducts);
		}
	
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/myaccount/salesman/checkoutorder"));
	}

  
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    
    HttpSession session = request.getSession();
    HashMap<String, Product> orderproducts = (HashMap<String, Product>) session.getAttribute("salesmanSavedOrder");
	HashMap<String, Accessory> cartacc = (HashMap<String, Accessory>)session.getAttribute("salesmanAccOrder");
	
    Order orderDetails= new Order();
    String ordername = "ORDER: ";
    double totalprice = 0.0;
    
    String contentStr = "<hr><h1 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Add Order By Salesman</h1> <hr>";
    if((orderproducts == null || orderproducts.isEmpty()) && (cartacc == null || cartacc.isEmpty())) {
    	System.out.println("NO product");
    } else {
    	System.out.println("Session product");
    	prinProducttMap(orderproducts);
	  
    if(orderproducts != null && !orderproducts.isEmpty()) {
    	for(Entry<String, Product> m :orderproducts.entrySet()){
		  	System.out.println(m.getKey());		
		  	Product c = m.getValue();
		  		//		  		contentStr = contentStr +buildString(m, contentStr);
	  		 contentStr = contentStr +
	 					"<div class=\"prod_box_big\">"+
		 			"        <div class=\"top_prod_box_big\"></div>"+
		 			"        <div class=\"center_prod_box_big\">"+
		 			"          <div class=\"product_img_big\"> <a href=\"javascript:popImage('images/"+c.getCategory()+".jpg','Some Title')\" title=\"\"><img width=\"150\" height=\"150\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a>"+
		 			"            <div class=\"thumbs\"> <a href=\"#\" title=\"\"><img width=\"30\" height=\"30\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a> <a href=\"#\" title=\"header=[Thumb2] body=[ ] fade=[on]\"><img width=\"30\" height=\"30\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a> <a href=\"#\" title=\"header=[Thumb3] body=[ ] fade=[on]\"><img width=\"30\" height=\"30\" src=\"images/"+c.getCategory()+".jpg\" alt=\"\" border=\"0\"></a> </div>"+
		 			"          </div>"+
	 			"          <div class=\"details_big_box\">"+
	 			"            <div class=\"product_title_big\">"+m.getKey()+"</div>"+
	 			"            <div class=\"specifications\"> "+
		 			"              Spectial discount: <span class=\"blue\">$"+c.getRdiscount()+"</span><br>"+
		 			"              Warranty Offered: <span class=\"blue\">$"+c.getRwarranty()+"</span><br>"+
		 			"              Discount: <span class=\"blue\">$"+c.getDiscount()+"</span><br>"+
		 			"            </div>"+
	 			"            <div class=\"prod_price_big\"> price : <span class=\"price\">$"+c.getPrice()+"</span></div>"+
				 			 "<form action=\"/csj/myaccount/salesman/checkoutorder?act=remove\" method = \"post\">"+
					 			"  <input type=\"hidden\" name=\"name\" value= \""+c.getName()+"\">"+
					 			"  <input type=\"hidden\" name=\"price\" value=\""+c.getPrice()+"\">"+
					 			"  <input type=\"hidden\" name=\"discount\" value=\""+c.getDiscount()+"\">"+
					 			"  <button type = \"submit\" value= \"Remove From Cart\" >Remove From Order List</button>"+
					 			"</form>"+
					 			 "<form action=\"/csj/myaccount/salesman/checkoutorder?act=removewarranty\" method = \"post\">"+
						 			"  <input type=\"hidden\" name=\"name\" value= \""+c.getName()+"\">"+
						 			"  <button type = \"submit\" value= \"Remove Warranty\" >Remove Warranty</button>"+
						 		"</form>"+
		 		
	 			"       </div> </div>";
	  		 ordername = ordername + " :Product:: " + m.getKey();
	  		totalprice = totalprice + c.getPrice() - c.getDiscount()-c.getRdiscount() + c.getRwarranty();
	 	
	 						contentStr = contentStr +	"        <div class=\"bottom_prod_box_big\"></div>"+
	 							"      </div>";
	  	}		
    }
    
    if(cartacc != null && !cartacc.isEmpty()) {
    	
		for(Entry<String, Accessory> ma :cartacc.entrySet()){
			Accessory a = ma.getValue();
			contentStr = contentStr +"<div class=\"prod_box\">"+
					"        <div class=\"top_prod_box\"></div>"+
					"        <div class=\"center_prod_box\">"+
					"          <div class=\"product_title\"><a href=\"details.html\">"+a.getName()+"</a></div>"+
					"          <div class=\"product_img\"><a href=\"details.html\"><img width=\"130\" height=\"130\" src=\"images/Others-acc.jpg\" alt=\"\" border=\"0\"></a></div>"+
		 							
					"             <div class=\"specifications\"> Discount: <span class=\"blue\"> $"+a.getDiscount()+" </span><br></div>"+
					
					"          <div class=\"prod_price\">price : <span class=\"price\">$"+a.getPrice()+"</span></div>"+
					
					"        </div>"+
					"        <div class=\"bottom_prod_box\"></div>"+
					"        <div class=\"prod_details_tab\"> "
					 +"<form action=\"/csj/myaccount/salesman/checkoutorder?act=removeacc\" method = \"post\">"+
				
			 			"  <input type=\"hidden\" name=\"accname\" value= \""+a.getName()+"\">"+
			 			"  <input type=\"hidden\" name=\"price\" value=\""+a.getPrice()+"\">"+
			 			"  <input type=\"hidden\" name=\"discount\" value=\""+a.getDiscount()+"\">"+
			 			"  <button type = \"submit\" value= \"Remove\" >Remove</button>"+
			 			"</form>"+
					 " </div>"+
					"      </div>";
			totalprice = totalprice + a.getPrice() - a.getDiscount();
					
			ordername = ordername + a.getName();	
		}		
 	
    }
    	orderDetails.setName(ordername);
    	orderDetails.setPrice(totalprice);
    	
    	contentStr = contentStr 
    			+"<div class=\"prod_box_big\">"+
	 			"        <div class=\"top_prod_box_big\"></div>"+
	 			"        <div class=\"center_prod_box_big\">"+
	 			"          <div> <hr><h4 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Order Details</h4> <hr></div>"+
	 			"          <div class=\"details_big_box\">"+
	 			"            <div class=\"product_title_big\">"+ordername+"</div>"+
	 			"          <div class=\"prod_price\">Total Price : <span class=\"price\">$"+totalprice+"</span></div>"+
	 							
				"          </div>"+
	 			"   	</div>     </div>";
    	
    	contentStr = contentStr + 	
    			"<div class=\"center_content\">"+
    			"      <div class=\"center_title_bar\">Buyer details</div>"+
    			"      <div class=\"prod_box_big\">"+
    			"        <div class=\"top_prod_box_big\"></div>"+
    			"        <div class=\"center_prod_box_big\">"+
    			"          <div class=\"contact_form\">"+
    						"<form action=\"/csj/myaccount/salesman/orders\" method=\"post\">"+
    			
    			"            <div class=\"form_row\">"+
    			"              <label class=\"contact\"><strong>Buyer Name:</strong></label>"+
    			"              <input type=\"text\" name=\"name\" class=\"contact_input\" />"+
    			"            </div>"+

    						"  <input type=\"hidden\" name=\"ordername\" value= \""+ordername+"\">"+
					 		"  <input type=\"hidden\" name=\"totalprice\" value=\""+totalprice+"\">"+
					 

    			"             <button type=\"submit\" value=\"Add order\" style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">ADD ORDER</button> "+
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
