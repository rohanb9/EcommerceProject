import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.*;

public class LoginPage extends HttpServlet {
  
  private CommonUtils store = new CommonUtils();
  private MySQLDataStoreUtilities mySQLStore = new MySQLDataStoreUtilities();  
  private  HashMap<String, User> usersFromDb = new HashMap<String, User>();

  public void init() throws ServletException {
	  HashMap<String, User> users = (HashMap<String, User>) mySQLStore.fetchAllUsers();
	  usersFromDb = users;
	  store.setUsers(users);
  }
  
	public void prinUsertMap(HashMap<String, User> mapInFile) throws ServletException {
	  
	  for(Entry<String, User> m :mapInFile.entrySet()){
		    System.out.println(m.getKey());
			User c = m.getValue();
			System.out.println("\t Name : "+c.getName());
			System.out.println("\t type : "+c.getUtype());
	  }
  }

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HashMap<String, User> usersDb = mySQLStore.fetchAllUsers();
		// get request parameters for userID and password
		String user = request.getParameter("user");
		String pwd = request.getParameter("password");

		UType utype = UType.fromString(request.getParameter("utype"));
		
		prinUsertMap(usersDb);
		User userFromDb = usersDb.get(user);
		
		//System.out.println("---((usersDb.get(user)).getPassword())" + ((usersDb.get(user)).getPassword()));
		if(!usersDb.containsKey(user)) {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/register?User-not-found-REGISTER-FIRST."));
		} else if(!(userFromDb.getPassword()).equals(pwd)) {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/login?Incorrect-password-TRY-AGAIN."));
		} else if(userFromDb.getUtype() != utype) {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/login?Incorrect-user-type-TRY-AGAIN."));
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("username", user);
			session.setAttribute("usertype", utype.toString());
			
			// Configure: setting session to expiry in 30 mins
			session.setMaxInactiveInterval(30*60);
			Cookie userName = new Cookie("user", user);
			userName.setMaxAge(30*60);
			response.addCookie(userName);
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/home"));
			System.out.println("-----------------------------success");
		}
	
	}
	
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	store.setBasicWithCSS("login");
	String content = 

	"<div class=\"ribbon\"></div><div class=\"login\">"+
	"<h1>Login.</h1>"+
  	"<p>Lets login</p>"+
	"<form action=\"login\" method=\"post\">"+
    		"<div class=\"input\">"+
		"      <div class=\"blockinput\">"+
		"        <i class=\"icon-envelope-alt\"></i><input type=\"text\" name=\"user\" placeholder=\"Username\">"+
		"      </div><br>"+
		"      <div class=\"blockinput\">"+
		"        <i class=\"icon-unlock\"></i><input type=\"password\" placeholder=\"Password\" name=\"password\">"+
		"      </div><br>"+
		
		"      <div class=\"blockinput\">"+
		"        <i class=\"icon-unlock\"></i>"+
				"<select name=\"utype\">"+
				"    <option value=\"CUSTOMER\">CUSTOMER</option>"+
				"    <option value=\"STOREMANAGER\">STOREMANAGER</option>"+
				"    <option value=\"SALESMAN\">SALESMAN</option>"+
				"    <option value=\"RETAILER\">RETAILER</option>"+
				"  </select>"+
		"      </div>"+
				
		"    </div>"+
		"<button type=\"submit\" value=\"Login\">Login</button>"+
	"  </form></div>";

		HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
	
	store.setHeader(headerval);
						
    store.setContent(content);
    out.println(store.getWholeHTML());
  }
}
