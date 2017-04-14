
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;

public class CommonUtils {

	public static HashMap<String, Product> products = new HashMap<String, Product>();
	public static HashMap<String, User> users = new HashMap<String, User>();
	public static HashMap<String, Order> orders = new HashMap<String, Order>();
	public static HashMap<String, CartItem> cartItems = new HashMap<String, CartItem>();
	
	private String CONTENT = "";
	private String BASICS = "<!doctype html>"+
							"<html>"+
							"<head>"+
							"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
							"<title>spigot - Free CSS Tobjlate by ZyPOP</title>"+
							"<link rel=\"stylesheet\" href=\"/csj/styles.css\" type=\"text/css\" />"+

							"<!--[if lt IE 9]>"+
							"<script src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>"+
							"<![endif]-->"+
							"<!--"+
							"spigot, a free CSS web tobjlate by ZyPOP (zypopwebtobjlates.com/)"+
							""+
							"Download: http://zypopwebtobjlates.com/"+
							""+
							"License: Creative Commons Attribution"+
							"//-->"+
							"</head>"+
							"<body>"+
							"<div id=\"container\">";

	private String HEADER  = "<header>"+
							"    	<h1><a href=\"/\">Just<span>Buy</span></a></h1>"+
							"        <h2>find everything with best price</h2>"+
							"    </header>"+
							"    <nav>"+
							"    	<ul>"+
							"        	<li class=\"start selected\"><a href=\"/csj/home\">Home</a></li>"+
							"                        <li><a href=\"/csj/trending\">Trending</a></li>"+
							"                        <li><a href=\"/csj/home?cat=TV\">TV</a></li>"+
							"                        <li><a href=\"/csj/home?cat=Tablets\">Tablets</a></li>"+
							"                        <li><a href=\"/csj/home?cat=Smart-Phones\">Smart Phones</a></li>"+

							"            <li><a href=\"/csj/register?param1=hello\">Register</a></li>"+
							
							"            <li class=\"\"><a href=\"/csj/login\">Login</a></li>"+
							"            <li><a href=\"/csj/cart\">Cart</a></li>"+
							"        </ul>"+
							"    </nav>"+
							"<div id=\"body\">";

	private final String SIDEBAR = "<aside class=\"sidebar\">"+
							"	"+
							"            <ul>	"+
							"               <li>"+
							"                    <h4>Categories</h4>"+
							"                    <ul>"+
							"                        <li><a href=\"/csj/home?cat=TV\">TV</a></li>"+
							"                        <li><a href=\"/csj/home?cat=Tablets\">Tablets</a></li>"+
							"                        <li><a href=\"/csj/home?cat=Smart-Phones\">Smart Phones</a></li>"+

							"                        <li><a href=\"/csj/home?cat=Laptops\">Laptops</a></li>"+
							"                        <li><a href=\"/csj/home?cat=Others\">Others</a></li>"+
							"                    </ul>"+
							"                </li>"+
							"                "+
							"                <li>"+
							"                    <h4>About us</h4>"+
							"                    <ul>"+
							"                        <li class=\"text\">"+
							"                        	<p style=\"margin: 0;\">We are the best sellers in 2016 with great prices and deals. We have above one million happy customers.</p>"+
							"                        </li>"+
							"                    </ul>"+
							"                </li>"+
							"            </ul>"+
							"		"+
							"        </aside>"+
							"    	<div class=\"clear\"></div>"+
							"    </div>";
	

    private final String FOOTER = "<footer>"+
								
								"        <div class=\"footer-bottom\">"+
								"            <p>© JustBuy 2016. <a href=\"http://zypopwebtobjlates.com/\">Free CSS Website Tobjlates</a> by rohan borde</p>"+
								"         </div>"+
								"    </footer>"+
								"</div>"+
								"</body>"+
								"</html>";

    
    public static void writeToFile(Object obj, String filename){
		File ifile=new File(filename);
	   
	    try{
	    FileOutputStream fos=new FileOutputStream(ifile);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        	oos.writeObject(obj);
	        oos.flush();
	        oos.close();
	        fos.close();
	    }catch(Exception e){
			System.out.println("Error writing obj.");
		}
	
	}
	
	// Read the HashMaps from the File GameSpeedDataStore
	public static Object readFromFile(String filename) {
		Object obj = null;
	    try{
	        File filei=new File(filename);
	        FileInputStream fis=new FileInputStream(filei);
	        ObjectInputStream ois=new ObjectInputStream(fis);
	
	        obj=ois.readObject();
	        //	        ArrayList<String> mArray = (ArrayList<String>) ois.readObject();
	        ois.close();
	        fis.close();
	        
	    }catch(Exception e){
	    	System.out.println("Error reading obj.");
	    }
	    return obj;
	}
	
 public int getNextOrderId(HashMap<String, Order> ordersFromDb) {
	  int max = 0;
      for(Entry<String, Order> o :ordersFromDb.entrySet()){
    	  Order order = o.getValue();
    	  if(max < order.getId()) {
    		  max = order.getId();
    	  }
	   }
      return max+1;
  }
  
  public void setContent( String content) {
  	this.CONTENT = "<section id=\"content\">"+
					//	"<article>"+
						content+
					//	"</article>"+
					"</section>";
  }
  
  public void setHeader( String header) {
	  	this.HEADER = header;
	  }
  
  public void setBasicWithCSS( String extCode) {
	  	this.BASICS = "<!doctype html>"+
				"<html>"+
				"<head>"+
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
				"<title>spigot - Free CSS Tobjlate by ZyPOP</title>"+
				"<link rel=\"stylesheet\" href=\"/csj/styles.css\" type=\"text/css\" />"+
				"<link rel=\"stylesheet\" href=\"/csj/"+extCode +".css\" type=\"text/css\" />"+
				"<!--[if lt IE 9]>"+
				"<script src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>"+
				"<![endif]-->"+
				"<!--"+
				"spigot, a free CSS web tobjlate by ZyPOP (zypopwebtobjlates.com/)"+
				""+
				"Download: http://zypopwebtobjlates.com/"+
				""+
				"License: Creative Commons Attribution"+
				"//-->"+
				"</head>"+
				"<body>"+
				"<div id=\"container\">";
  }

  public String getBasics() {
  	 return BASICS;
  }

  public String getHeader() {
  	return HEADER;
  }

  public String getSideBar() {
  	 return SIDEBAR;
  }

  public String getFooter() {
  	return FOOTER;
  }

  public String getContent() {
  	return CONTENT;
  }

  public String getWholeHTML() {
  	return getBasics() + getHeader()  + getContent() + getSideBar() + getFooter();
  }

public static HashMap<String, Product> getProducts() {
	return products;
}

public static void setProducts(HashMap<String, Product> products) {
	CommonUtils.products = products;
}

public static HashMap<String, User> getUsers() {
	return users;
}

public static void setUsers(HashMap<String, User> users) {
	CommonUtils.users = users;
}

public static HashMap<String, Order> getOrders() {
	return orders;
}

public static void setOrders(HashMap<String, Order> orders) {
	CommonUtils.orders = orders;
}

public static HashMap<String, CartItem> getCartItems() {
	return cartItems;
}

public static void setCartItems(HashMap<String, CartItem> cartItems) {
	CommonUtils.cartItems = cartItems;
}


}
