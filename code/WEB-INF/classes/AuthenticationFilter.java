import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {

	private ServletContext context;
	
	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("Initializion of AuthenticationFilter ");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		this.context.log("Requested Resource::"+uri);
		
		HttpSession session = req.getSession(false);
		
		HttpSession reqsession = req.getSession();
		HashMap<String, Product> cartproducts = (HashMap<String, Product>)reqsession.getAttribute("cart");
		 HashMap<String, Accessory> cartacc = (HashMap<String, Accessory>) reqsession.getAttribute("cartacc");
   
		int cartcount = 0;
		
		if(cartproducts != null ) {
			cartcount = cartproducts.size() ;
		}
		if(cartacc != null ) {
			cartcount = cartcount+ cartacc.size();
		}
		
		String header = "";
		String userBased = "";
		
		header = "<header>"+
					"    	<h1><a href=\"/\">Just<span>Buy</span></a></h1>"+
					"        <h2>find everything with best price</h2>"+
					"    </header>"+
					" <a href=\"/csj/search.html\" class=\"button\">SEARCH</a><br>"+
					"    <nav>"+
					"    	<ul>"+
								"        	<li class=\"start selected\"><a href=\"/csj/home\">Home</a></li>"+
								"                        <li><a href=\"/csj/trending\">Trending</a></li>"+
					"                        <li><a href=\"/csj/home?cat=TV\">TV</a></li>"+
					"                        <li><a href=\"/csj/home?cat=Tablets\">Tablets</a></li>"+
					"                        <li><a href=\"/csj/home?cat=Smart-Phones\">Smart Phones</a></li>"+
					"                        <li><a href=\"/csj/home?cat=Laptops\">Laptops</a></li>"+

					"            <li class=\"\"><a href=\"/csj/login\" style=\"padding-left: 50px;border: 0px;\"><u>Login</u></a></li>"+
					"            <li><a href=\"/csj/register\" style=\"border: 0px;\"><u>Register</u></a></li>"+
					"            <li><a href=\"/csj/cart\" style=\"border: 0px;\"><u>Cart ("+cartcount+")</u></a></li>"+
					"        </ul>"+
					"    </nav>"+
					"<div id=\"body\">";
			
		if(session != null){

			String uname = (String)session.getAttribute("username");
				//res.sendRedirect("login");
			//response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/home"));
			
		
			if(uname != null) {
				String usertype= (String)session.getAttribute("usertype");
			
				System.out.println(" +++ " + usertype.equals((UType.RETAILER).toString()));
				if(usertype.equals((UType.CUSTOMER).toString())) {
					userBased = " <li class=\"start selected\"><a href=\"/csj/myaccount/customer\">My Account</a></li>"+
					" <li class=\"start selected\"><a href=\"/csj/myaccount/customer/orders\">My Orders</a></li>"+
					" <li class=\"start selected\"><a href=\"/csj/myaccount/customer/orders/track\">Track Order</a></li>";
					;
				
				} else if(usertype.equals((UType.STOREMANAGER).toString())) {
					userBased = " <li class=\"start selected\"><a href=\"/csj/myaccount/storemanager\">My Account</a></li>"+
					" <li class=\"start selected\"><a href=\"/csj/myaccount/storemanager/products\">Handle Products</a></li>"+
					"   <li class=\"start selected\"><a href=\"/csj/analytics\">Analytics</a></li>";
					
				} else if(usertype.equals((UType.RETAILER).toString())) {
					userBased =  " <li class=\"start selected\"><a href=\"/csj/myaccount/retailer\">My Account</a></li>"+
							" <li class=\"start selected\"><a href=\"/csj/myaccount/retailer/offer\">Offer Warranty or Discounts</a></li>";
					
				} else if(usertype.equals((UType.SALESMAN).toString())) {

						userBased =  " <li class=\"start selected\"><a href=\"/csj/myaccount/retailer\">My Account</a></li>"+
						" <li class=\"start selected\"><a href=\"/csj/myaccount/salesman/createuser\">Create Accounts</a></li>"+
						" <li class=\"start selected\"><a href=\"/csj/myaccount/salesman/orders\">Orders</a></li>"+
						" <li class=\"start selected\"><a href=\"/csj/myaccount/salesman/addorders\">Add Orders</a></li>";
					
				} 
				header =  "<header>"+
					"    	<h1><a href=\"/\">Just<span>Buy</span></a></h1>"+
					"        <h2>find everything with best price</h2>"+
					"    </header>"+
					" <a href=\"/csj//search.html\" class=\"button\">SEARCH</a><br>"+
					"    <nav>"+
					"    	<ul>"+
					"        	<li class=\"start selected\"><a href=\"/csj/home\">Home</a></li>"+
					"                        <li><a href=\"/csj/trending\">Trending</a></li>"+
						userBased+

					"            <li><a href=\"/csj/register\"  style=\"padding-left: 63px;border: 0px;\"><u>Register</u></a></li>"+
					"            <li class=\"\"><a href=\"/csj/logout\" style=\"border: 0px;\"><u>Logout</u></a></li>"+
					"            <li><a href=\"/csj/cart\"style=\"border: 0px;\"><u>Cart ("+cartcount+") </u></a></li>"+
					"            <li><a href=\"/csj/home\"  style=\"padding-left: 20px;border: 0px; color: black;\">Welcome "+uname+"</a></li>"+
					
					"        </ul>"+
					"    </nav>"+
					"<div id=\"body\">";
			}		

		} else {
					
		}
		
		reqsession.setAttribute("headerval", header);
		chain.doFilter(request, response);
		
	}

	public void destroy() {
		//destroy any resources here
	}

}