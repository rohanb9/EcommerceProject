
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;

public class AJAXUtitliy {

	public static Statement stmt;
	public static Connection conn ;
	
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
	
	public StringBuffer readdata(String searchId)
	{
		StringBuffer sb = new StringBuffer();
		HashMap<String, Product> products = fetchAllProducts();
		 // check if user sent empty string
        if (!searchId.equals("")) {
                
            for(Entry<String, Product> m :products.entrySet()){
            	Product product = m.getValue();
                if (product.getName().toLowerCase().startsWith(searchId)) {
                    sb.append("<product>");
                    sb.append("<Id>" + product.getId() + "</Id>");
                    sb.append("<Name>" + product.getName() + "</Name>");
                    sb.append("<Price>" + product.getPrice() + "</Price>");
                    sb.append("</product>");
                   
                }
            }
        }
		return sb;
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
	
	
    public static Statement getStatement() {
    	return stmt;
    }
    
    public static Connection getConnection() {
    	return conn;
    }
   


}
