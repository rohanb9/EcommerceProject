

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupAppListner
               implements ServletContextListener{

	MySQLDataStoreUtilities c = new MySQLDataStoreUtilities();
	MongoDBDataStoreUtilities m = new MongoDBDataStoreUtilities();
	AJAXUtitliy a = new AJAXUtitliy();
	private CommonUtils store = new CommonUtils();
	@Override
	public void contextInitialized(ServletContextEvent arg) {
		c.connectToMySQL();	
		//MongoDb connection
		m.getConnection();
		
	  SAXProductHandler s = new SAXProductHandler();
      HashMap<String, Product> products = null;
		try {
			URL url = getClass().getResource("/ProductCatalog.xml");
			//			File file = new File(url.getPath());
			products = s.readDataFromXML(url.getPath());
			System.out.println("_--------------------");
			
			//store.writeToFile(products, "products");
			
			c.removeAllProducts();
			c.insertProducts(products);
			a.stmt = c.getStatement();
			a.conn = c.getConnection();
			
			
			//HashMap<String, Product> ps = c.fetchAllProducts();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("+================================server started");
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg) {
		
		Connection conn = c.getConnection();
		Statement st = c.getStatement();
		
		try {
			st.close();
		
			conn.close();
			
			m.destroyMongoConnection();
		} catch (Exception e) {
			System.out.println("=========ERROR CLOSING SQL CONNECTION=========");
		}
		
		System.out.println("==================================Server destroyed");
	}

  
}