import java.io.*;
import java.text.SimpleDateFormat;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;


public class CustOrderPage extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();

  private MySQLDataStoreUtilities mySQLStore = new MySQLDataStoreUtilities();  
  
  
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    
	HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
	String username = (String)session.getAttribute("username");
	
	String orderIdStr = (String)request.getParameter("oid");
	
	Integer orderId = 0;
	boolean searchbyId = false;
	if(orderIdStr != null) {
		orderId = Integer.parseInt(orderIdStr) ;			
		searchbyId = true;
	}
	
    //HashMap<String, Order> ordersFromDb = (HashMap<String, Order>) store.readFromFile("orders");
    HashMap<String, Order> ordersFromDb = (HashMap<String, Order>) mySQLStore.fetchAllOrders();
    
    
    String contentStr = "<hr><h1 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> My Orders</h1> <hr>";
    if(ordersFromDb == null || ordersFromDb.isEmpty()) {
    	System.out.println("NO Orders");
    } else {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	Calendar cal = Calendar.getInstance();
    	
    	String delDate = "";
    	for(Entry<String, Order> m :ordersFromDb.entrySet()){
		  	System.out.println(m.getKey());	
		  	Order c = m.getValue();
		  	cal.setTime(c.getOrderDate()); 
		  	cal.add(Calendar.DATE, 14);
		  	delDate = sdf.format(cal.getTime());
		  	if(username.equals(c.getBuyer())) {
		  		if(searchbyId == false || (searchbyId == true && orderId.intValue() == c.getId())) {
		  		//		  		contentStr = contentStr +buildString(m, contentStr);
		  		contentStr = contentStr +
	  				"<div class=\"prod_box_big\">"+
		 			"        <div class=\"top_prod_box_big\"></div>"+
		 			"        <div class=\"center_prod_box_big\">"+
		 			"          <div> <hr><h4 style=\"background-color: #e9e9e9;padding-left: 20px;padding-top: 8px;\"> Order Details</h4> <hr></div>"+
		 			"          <div class=\"details_big_box\">"+
		 			"            <div class=\"product_title_big\">"+c.getName()+"</div>"+
		 			"          <div class=\"prod_price\">Price : <span class=\"price\">$"+c.getPrice()+"</span></div>"+
		 			"          <div class=\"prod_price\">Order Date : <span class=\"price\">"+c.getOrderDate()+"</span></div>"+
		 			"          <div class=\"prod_price\">Order No : <span class=\"price\">"+c.getId()+"</span></div>"+
		 			"          <div class=\"prod_price\">Order Status : <span class=\"price\">"+c.getStatus()+"</span></div>"+
		 			
		 			"          <div class=\"prod_price\">Delivery Date : <span class=\"price\">"+delDate+"</span></div>"+
		 		
	
							 "<form action=\"/csj/myaccount/salesman/orders?act=cancel\" method = \"post\">"+
								"  <input type=\"hidden\" name=\"ordername\" value= \""+c.getName()+"\">"+
								"  <input type=\"hidden\" name=\"username\" value= \""+c.getName()+"\">"+
								
								"  <button type = \"submit\" value= \"Cancel Order\" class=\"addtocart\">Cancel Order</button>"+
								"</form>"+
					"          </div>"+
		 			"   	</div>     </div>";
	  		 
		  		}
		  	}		
	  	}		
	
    }			

	store.setBasicWithCSS("styles1");
	store.setContent(contentStr);

	store.setHeader(headerval);
    out.println(store.getWholeHTML());
  }
}
