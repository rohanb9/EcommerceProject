import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.*;

public class RegisterPage extends HttpServlet {
  
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

		// get request parameters for userID and password
		String user = request.getParameter("user");
		String pwd = request.getParameter("password");
		String rpwd = request.getParameter("rpassword");
		UType utype = UType.fromString(request.getParameter("utype"));

		System.out.println("YOO.." + user + pwd);
		
		if(!pwd.equals(rpwd) ) {
			System.out.println("Password mismatch..");
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/register?"+"password-mismatch-TRY-AGAIN"));
		} else if(user == null || user == "") {
			System.out.println("Username required..");
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/register?"+"Username-required-TRY-AGAIN"));
		} else {
			System.out.println("User-registered-WELCOME..");
			User userObj = new User(user, pwd);
			userObj.setName(user);
			userObj.setUtype(utype);
			
			usersFromDb = mySQLStore.fetchAllUsers();

			if(usersFromDb == null || usersFromDb.isEmpty()) {
				usersFromDb = new HashMap<String, User>();
			} else {
				//prinUsertMap(usersFromDb);
			}
			usersFromDb.put(user, userObj);
			store.setUsers(usersFromDb);
			
			mySQLStore.insertUser(userObj);
			
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/home?"+"User-registered-WELCOME-"+user));
		}
		
	}
	
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	store.setBasicWithCSS("login");
	String param1 = request.getParameter("param1");
		System.out.println("-----------------------------" + param1);
		
	String content = 

	"<div class=\"ribbon\"></div><div class=\"login\">"+
	"<h1>New User?</h1>"+
  	"<p>Lets register..</p>"+
	"<form action=\"register\" method=\"post\">"+
    		"<div class=\"input\">"+
		"      <div class=\"blockinput\">"+
		"        <i class=\"icon-envelope-alt\"></i><input type=\"text\" name=\"user\" placeholder=\"Username\">"+
		"      </div>"+
		"      <div class=\"blockinput\">"+
		"        <i class=\"icon-unlock\"></i><input type=\"password\" placeholder=\"Password\" name=\"password\">"+
		"      </div>"+
		
		"      <div class=\"blockinput\">"+
		"        <i class=\"icon-unlock\"></i><input type=\"password\" placeholder=\"Reenter Password\" name=\"rpassword\">"+
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
		"<button type=\"submit\" value=\"Register\">Register</button>"+
	"  </form></div>";
						
	HttpSession session = request.getSession();
	String headerval = (String)session.getAttribute("headerval");
	
	store.setHeader(headerval);
    store.setContent(content);
    out.println(store.getWholeHTML());
  }
}
