
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;

public class MySQLDataStoreUtilities {

	public static  Statement stmt;
	public static  Connection conn ;
	
	public void connectToMySQL(){
    	
        final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
        final String DB_URL="jdbc:mysql://localhost:3306/justbuydb";
        final String USER = "root";
        final String PASS = "rohanborde";
       
	    try {
			Class.forName("com.mysql.jdbc.Driver");
		
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
	
			stmt = conn.createStatement();
	    } catch (Exception e) {
	    	e.printStackTrace();
			System.out.println("*************ERROR in connecting mySQL DB *******************");
			
		}
	   
    }
	
	public static void insertProducts(HashMap<String, Product> products){

		System.out.println("*************insert *******************");
		try{
			
			Connection conn = getConnection();
			PreparedStatement pst = null;
			PreparedStatement pst1 = null;
			
			 for(Entry<String, Product> m :products.entrySet()){
				Product c = m.getValue();
				//System.out.println(c.getName());
				
				String query = "INSERT INTO products(name,price,image, retailer, cond, discount, rdiscount, rwarranty, category, retailerZip, retailerCity, retailerState, productOnSale, manufacturerName) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
				pst = conn.prepareStatement(query);
				
				pst.setString(1,m.getKey());
				pst.setDouble(2,c.getPrice());
				pst.setString(3,c.getImage());
				pst.setString(4, c.getRetailer());
				
				pst.setString(5,c.getCondition());
				pst.setDouble(6,c.getDiscount());
				pst.setDouble(7,c.getRdiscount());
				pst.setDouble(8,c.getRwarranty());
				pst.setString(9,c.getCategory().toString());
				pst.setInt(10, c.getRetailerZip());
				
				pst.setString(11,c.getRetailerCity());
				pst.setString(12,c.getRetailerState());
				pst.setString(13,c.getProductOnSale().toString());
				pst.setString(14,c.getManufacturerName());
				pst.execute();
				
				
				HashMap<String,Accessory> accessories = c.getAccessories();
				for(Entry<String, Accessory> ma :accessories.entrySet()){
					//System.out.println("\t\t\t" + ma.getKey());
					Accessory a = ma.getValue();
					String q = "INSERT INTO accessory(name,pname,price, image, retailer, cond, discount) "
							+ "VALUES (?,?,?,?,?,?,?);";
				    pst1 = conn.prepareStatement(q);
					
					pst1.setString(1,ma.getKey());
					pst1.setString(2,c.getName());
					pst1.setDouble(3,a.getPrice());
					pst1.setString(4, a.getImage());
					
					pst1.setString(5, a.getRetailer());
					pst1.setString(6, a.getCondition());
					pst1.setDouble(7, a.getDiscount());
					pst1.execute();
					
				}
				
		   }
			 pst.close();
			 pst1.close();
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("*************ERROR in insert product *******************");
		}
	}
	
	public static HashMap<String, Product> fetchAllProducts()
	{
		HashMap<String, Product> productsFromDB = new HashMap<String, Product>();
		
		try{
			conn = getConnection();
			String q ="select * from products";
			PreparedStatement pst = conn.prepareStatement(q);
			ResultSet rs = pst.executeQuery();
			
			PreparedStatement pst1 = null;
			ResultSet rs1 = null;
			
			while(rs.next())
			{
				System.out.println(rs.getString("name"));
				Product p= new Product(
						rs.getString("name"), 
						rs.getDouble("price"), 
						rs.getString("image"),
						rs.getString("retailer"),
						rs.getString("cond"),
						rs.getDouble("discount"),
						rs.getDouble("rdiscount"),
						rs.getDouble("rwarranty"),
						Category.fromString(rs.getString("category")),
						rs.getInt("retailerZip"),
						rs.getString("retailerCity"),
						rs.getString("retailerState"),
						YesNo.fromString(rs.getString("productOnSale")),
						rs.getString("manufacturerName"));
				p.setId(rs.getInt("id"));
				
				String q1 ="select * from accessory where pname = ?";
				pst1 = conn.prepareStatement(q1);
				pst1.setString(1,rs.getString("name"));
				
				rs1 = pst1.executeQuery();
				
				
				HashMap<String, Accessory> accFromDB = new HashMap<String, Accessory>();
				
				while(rs1.next())
				{
					//					System.out.println(rs1.getString("name"));
					Accessory a= new Accessory(rs1.getString("name"), 
							rs1.getDouble("price"), 
							rs1.getString("image"),
							rs1.getString("retailer"),
							rs1.getString("cond"),
							rs1.getDouble("discount")
							);
					accFromDB.put(rs1.getString("name"), a);
					
				}
				p.setAccessories(accFromDB);
				productsFromDB.put(rs.getString("name"), p);
			}	
			
			pst.close();
				
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("*************ERROR in fetch all products *******************");
		}
		return productsFromDB;
	}
	
	public static HashMap<String, Product> fetchProductById(int id)
	{
		HashMap<String, Product> productsFromDB = new HashMap<String, Product>();
		
		try{
			conn = getConnection();
			String q ="select * from products WHERE id = ? ";
			PreparedStatement pst = conn.prepareStatement(q);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			
			PreparedStatement pst1 = null;
			ResultSet rs1 = null;
			
			while(rs.next())
			{
				System.out.println(rs.getString("name"));
				Product p= new Product(
						rs.getString("name"), 
						rs.getDouble("price"), 
						rs.getString("image"),
						rs.getString("retailer"),
						rs.getString("cond"),
						rs.getDouble("discount"),
						rs.getDouble("rdiscount"),
						rs.getDouble("rwarranty"),
						Category.fromString(rs.getString("category")),
						rs.getInt("retailerZip"),
						rs.getString("retailerCity"),
						rs.getString("retailerState"),
						YesNo.fromString(rs.getString("productOnSale")),
						rs.getString("manufacturerName"));
				p.setId(rs.getInt("id"));
				
				String q1 ="select * from accessory where pname = ?";
				pst1 = conn.prepareStatement(q1);
				pst1.setString(1,rs.getString("name"));
				
				rs1 = pst1.executeQuery();
				
				
				HashMap<String, Accessory> accFromDB = new HashMap<String, Accessory>();
				
				while(rs1.next())
				{
					//					System.out.println(rs1.getString("name"));
					Accessory a= new Accessory(rs1.getString("name"), 
							rs1.getDouble("price"), 
							rs1.getString("image"),
							rs1.getString("retailer"),
							rs1.getString("cond"),
							rs1.getDouble("discount")
							);
					accFromDB.put(rs1.getString("name"), a);
					
				}
				p.setAccessories(accFromDB);
				productsFromDB.put(rs.getString("name"), p);
			}	
			
			pst.close();
				
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("*************ERROR in fetch all products *******************");
		}
		return productsFromDB;
	}
	
	public static void insertProduct(Product c){
		try{
			
			Connection conn = getConnection();
			String query = "INSERT INTO products(name,price,image, retailer, cond, discount, rdiscount, rwarranty, category, retailerZip, retailerCity, retailerState, productOnSale, manufacturerName) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			PreparedStatement pst = conn.prepareStatement(query);
			
			pst.setString(1,c.getName());
			pst.setDouble(2,c.getPrice());
			pst.setString(3,c.getImage());
			pst.setString(4, c.getRetailer());
			
			pst.setString(5,c.getCondition());
			pst.setDouble(6,c.getDiscount());
			pst.setDouble(7,c.getRdiscount());
			pst.setDouble(8,c.getRwarranty());
			pst.setString(9,c.getCategory().toString());
			pst.setInt(10, c.getRetailerZip());
			
			pst.setString(11,c.getRetailerCity());
			pst.setString(12,c.getRetailerState());
			pst.setString(13,c.getProductOnSale().toString());
			pst.setString(14,c.getManufacturerName());
			pst.execute();
			
		
			pst.close();
			
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("*************ERROR in insert product *******************");
		}
	}
	
	public static void insertAccessory(Accessory a, String pname){
		try{
			
			Connection conn = getConnection();
			String q = "INSERT INTO accessory(name,pname,price, image, retailer, cond, discount) "
					+ "VALUES (?,?,?,?,?,?,?);";
			PreparedStatement pst1 = conn.prepareStatement(q);
			
			pst1.setString(1,a.getName());
			pst1.setString(2,pname);
			pst1.setDouble(3,a.getPrice());
			pst1.setString(4, a.getImage());
			
			pst1.setString(5, a.getRetailer());
			pst1.setString(6, a.getCondition());
			pst1.setDouble(7, a.getDiscount());
			pst1.execute();
			
		
			pst1.close();
			
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("*************ERROR in insert Accessory *******************");
		}
	}
	
	public static void deleteProduct(String name)
	{
		
		try{
			Connection conn = getConnection();
			// remove accessories
			String q1 = "DELETE from accessory WHERE pname = ? ";
			PreparedStatement pst1 = conn.prepareStatement(q1);
			
			pst1.setString(1,name);
			
			pst1.executeUpdate();
			pst1.close();
			
			String q = "DELETE from products WHERE name = ? ";
			PreparedStatement pst = conn.prepareStatement(q);
			
			pst.setString(1,name);
			
			pst.executeUpdate();
			pst.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void deleteAccessory(String name)
	{
		
		try{
			Connection conn = getConnection();
			// remove accessories
			String q1 = "DELETE from accessory WHERE name = ? ";
			PreparedStatement pst1 = conn.prepareStatement(q1);
			
			pst1.setString(1,name);
			
			pst1.executeUpdate();
			pst1.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateProduct(String pname, Product c)
	{
		System.out.println(c.getName() + " ===============================================  " + pname);
		try{
			Connection conn = getConnection();

			HashMap<String, Accessory> accFromDB =fetchAllAccessoriesByProductName(pname);
			removeAllAccessoriesByProductName(pname);
			
			String insertIntoCustomerRegisterQuery = "UPDATE products SET name= ?,price= ?,image= ?, retailer= ?, cond= ?, discount= ?, rdiscount= ?, rwarranty= ?, category= ?, retailerZip= ?, retailerCity= ?, retailerState= ?, productOnSale= ?, manufacturerName= ? "
					+ " WHERE name = ?  ";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setString(1,c.getName());
			pst.setDouble(2,c.getPrice());
			pst.setString(3,c.getImage());
			pst.setString(4, c.getRetailer());
			
			pst.setString(5,c.getCondition());
			pst.setDouble(6,c.getDiscount());
			pst.setDouble(7,c.getRdiscount());
			pst.setDouble(8,c.getRwarranty());
			pst.setString(9,c.getCategory().toString());
			pst.setInt(10, c.getRetailerZip());
			
			pst.setString(11,c.getRetailerCity());
			pst.setString(12,c.getRetailerState());
			pst.setString(13,c.getProductOnSale().toString());
			pst.setString(14,c.getManufacturerName());
			pst.setString(15,pname);
			
			pst.executeUpdate();
			pst.close();
			
//			if(!(c.getName()).equals(pname)) {
				// update accessory table
				for(Entry<String, Accessory> m :accFromDB.entrySet()){
					 Accessory acc = m.getValue();
					insertAccessory(acc, c.getName());
				}
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, Accessory> fetchAllAccessoriesByProductName(String pname)
	{
		HashMap<String, Accessory> accFromDB = new HashMap<String, Accessory>();
		try{
			conn = getConnection();
			
			PreparedStatement pst1 = null;
			ResultSet rs1 = null;
				
			String q1 ="select * from accessory where pname = ?";
			pst1 = conn.prepareStatement(q1);
			pst1.setString(1,pname);
			
			rs1 = pst1.executeQuery();
			
			while(rs1.next())
			{
				//					System.out.println(rs1.getString("name"));
				Accessory a= new Accessory(rs1.getString("name"), 
						rs1.getDouble("price"), 
						rs1.getString("image"),
						rs1.getString("retailer"),
						rs1.getString("cond"),
						rs1.getDouble("discount")
						);
				accFromDB.put(rs1.getString("name"), a);
				
			}
			
			pst1.close();
				
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("*************ERROR in fetch all products *******************");
		}
		return accFromDB;
	}
	
	public static HashMap<String, Accessory> removeAllAccessoriesByProductName(String pname)
	{
		HashMap<String, Accessory> accFromDB = new HashMap<String, Accessory>();
		try{
			conn = getConnection();
			
			PreparedStatement pst1 = null;
			ResultSet rs1 = null;
				
			String q1 ="DELETE from accessory where pname = ?";
			pst1 = conn.prepareStatement(q1);
			pst1.setString(1,pname);
			
			pst1.executeUpdate();
			
			pst1.close();
				
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("*************ERROR in fetch all products *******************");
		}
		return accFromDB;
	}
	
	
	public static void updateAccessory(String accname, Accessory a)
	{
		
		try{
			Connection conn = getConnection();
			String q = "UPDATE accessory SET name= ?,price= ?,image= ?, retailer= ?, cond= ?, discount= ? "
					+ " WHERE name = ?  ";
			PreparedStatement pst1 = conn.prepareStatement(q);
			
			pst1.setString(1,a.getName());
			pst1.setDouble(2,a.getPrice());
			pst1.setString(3, a.getImage());
			
			pst1.setString(4, a.getRetailer());
			pst1.setString(5, a.getCondition());
			pst1.setDouble(6, a.getDiscount());
			pst1.setString(7, accname);
			
			pst1.executeUpdate();
			pst1.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateProductRWarranty(String pname, double rwarranty)
	{
		
		try{
			Connection conn = getConnection();
			
			String insertIntoCustomerRegisterQuery = "UPDATE products SET rwarranty= ? "
					+ " WHERE name = ?  ";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setDouble(1,rwarranty);
			pst.setString(2,pname);
			
			pst.executeUpdate();
			pst.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateProductRDiscount(String pname, double rdiscount)
	{
		
		try{
			Connection conn = getConnection();
			
			String insertIntoCustomerRegisterQuery = "UPDATE products SET rdiscount= ? "
					+ " WHERE name = ?  ";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setDouble(1,rdiscount);
			pst.setString(2,pname);
			
			pst.executeUpdate();
			pst.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void removeAllProducts()
	{
		
		try{
			Connection conn = getConnection();
			String query1 = "DELETE from accessory ";
			PreparedStatement pst = conn.prepareStatement(query1);
			
			pst.executeUpdate();
			pst.close();
			
			String query2 = "TRUNCATE TABLE products ";
			PreparedStatement pst1 = conn.prepareStatement(query2);
			
			pst1.executeUpdate();	
			pst1.close();
			
		}catch(Exception e){
			System.out.println("*************ERROR in remove order *******************");
		}
	}
	
	public static void insertUser(User u){
		try{
			
			Connection conn = getConnection();
			String insertIntoCustomerRegisterQuery = "INSERT INTO users(name,password,utype) "
			+ "VALUES (?,?,?);";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			
			pst.setString(1,u.getName());
			pst.setString(2,u.getPassword());
			pst.setString(3,u.getUtype().toString());
			pst.execute();
			System.out.println(u.getName()+"  "+ u.getPassword()+"    "+ u.getUtype().toString());
			pst.close();
			
		} catch(Exception e){
			System.out.println("*************ERROR in insert user *******************");
		}
	}
	
	public static void insertOrder(Order o){

		System.out.println("*************insert *******************");
		try{
			
			Connection conn = getConnection();
			String insertIntoCustomerRegisterQuery = "INSERT INTO orders(name,description,price, orderDate, status, buyer) "
			+ "VALUES (?,?,?,?,?,?);";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			
			pst.setString(1,o.getName());
			pst.setString(2,o.getDescription());
			pst.setDouble(3,o.getPrice());
			pst.setDate(4, new java.sql.Date(o.getOrderDate().getTime()));
			
			pst.setString(5,o.getStatus().toString());
			pst.setString(6,o.getBuyer());
			pst.execute();
			pst.close();
			
		} catch(Exception e){
			System.out.println("*************ERROR in insert order *******************");
		}
	}
	
	public static HashMap<String, User> fetchAllUsers()
	{
		HashMap<String, User> usersFromDB=new HashMap<String, User>();
		
		try{
			conn = getConnection();
			String q ="select * from users";
			PreparedStatement pst = conn.prepareStatement(q);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				User user= new User(rs.getString("name"), rs.getString("address"), rs.getString("credNo"), UType.fromString(rs.getString("utype"))); 
				user.setId(rs.getInt("id"));
				user.setPassword(rs.getString("password"));
				
				usersFromDB.put(rs.getString("name"), user);
			}	
			
			pst.close();
				
		}catch(Exception e){
			System.out.println("*************ERROR in fetch all users *******************");
		}
		return usersFromDB;
	}
	
	public static HashMap<String, Order> fetchAllOrders()
	{
		HashMap<String, Order> ordersFromDB=new HashMap<String, Order>();
		
		try{
			conn = getConnection();
			String q ="select * from orders";
			PreparedStatement pst = conn.prepareStatement(q);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				Order order= new Order(rs.getString("name"), rs.getString("description"), rs.getString("buyer"), OrderStatus.fromString(rs.getString("status"))); 
				order.setOrderDate(((java.util.Date) rs.getDate("orderDate")));
				order.setId(rs.getInt("id"));
				order.setPrice(rs.getDouble("price"));
				ordersFromDB.put(rs.getString("name"), order);
			}	
			
			pst.close();
				
		}catch(Exception e){
			System.out.println("*************ERROR in fetch all orders *******************");
		}
		return ordersFromDB;
	}
	
	public static void removeOrder(String name)
	{
		
		try{
			Connection conn = getConnection();
			String insertIntoCustomerRegisterQuery = "DELETE from orders WHERE name = ? ";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			
			pst.setString(1,name);
			
			pst.executeUpdate();
			pst.close();
			
		}catch(Exception e){
			System.out.println("*************ERROR in remove order *******************");
		}
	}
	
	public static void updateOrderStatus(String name, String status)
	{
		
		try{
			Connection conn = getConnection();
			String insertIntoCustomerRegisterQuery = "UPDATE orders SET status = ? WHERE name = ?  ";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setString(1,status);
			pst.setString(2,name);
			
			pst.executeUpdate();
			pst.close();
			
		}catch(Exception e){
			System.out.println("*************ERROR in remove order *******************");
		}
	}
	
	public static void updateOrderStatusAndPrice(String name, String status, double price)
	{
		
		try{
			Connection conn = getConnection();
			String insertIntoCustomerRegisterQuery = "UPDATE orders SET status = ? , price = ?  WHERE name = ?  ";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setString(1,status);
			pst.setDouble(2,price);
			pst.setString(3,name);
			
			pst.executeUpdate();
			pst.close();
			
		}catch(Exception e){
			System.out.println("*************ERROR in update order *******************");
		}
	}
	
	public static void updateUserAddressAndCredNo(String name, String address, String credNo)
	{
		System.out.println("*************Update *******************");
		try{
			Connection conn = getConnection();
			String insertIntoCustomerRegisterQuery = "UPDATE users SET address = ? , credNo = ?  WHERE name = ?  ";
			PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
			pst.setString(1,address);
			pst.setString(2,credNo);
			pst.setString(3,name);
			
			pst.executeUpdate();
			pst.close();
			
		}catch(Exception e){
			System.out.println("*************ERROR in update user *******************");
		}
	}
	
    public static Statement getStatement() {
    	return stmt;
    }
    
    public static Connection getConnection() {
    	return conn;
    }
   


}
