import java.io.*;
import java.text.SimpleDateFormat;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;


public class CustTrackOrderPage extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();
  	
  
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    
	HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
  
	
    
	String contentStr = "<div class=\"center_content\">"+
			"      <div class=\"center_title_bar\">Track Orders</div>"+
			"      <div class=\"prod_box_big\">"+
			"        <div class=\"top_prod_box_big\"></div>"+
			"        <div class=\"center_prod_box_big\">"+
			"          <div class=\"contact_form\">"+
						"<form action=\"/csj/myaccount/customer/orders\" method=\"get\">"+
						
			"            <div class=\"form_row\">"+
			"              <label class=\"contact\"><strong>Order id:</strong></label>"+
			"              <input type=\"text\" name=\"oid\" class=\"contact_input\" />"+
			"            </div>"+
		
			"             <div><button  type=\"submit\" value=\"Track\"  style=\"margin-left: 275px; padding: 8px; background: burlywood;\" class=\"contact\">Track</button> </div>"+
						"</form>"+
			"          </div>"+
			"        </div>"+
			"        <div class=\"bottom_prod_box_big\"></div>"+
			"      </div>"+
			"    </div>";
	store.setBasicWithCSS("styles1");
	store.setContent(contentStr);

	store.setHeader(headerval);
    out.println(store.getWholeHTML());
  }
}
