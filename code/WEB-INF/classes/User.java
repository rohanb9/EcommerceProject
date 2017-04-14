

public class User implements java.io.Serializable{
	private int id;
	private String name;
	private String address;
	private String credNo;
	private int cartCount;
	private String password;
	
	
	private UType utype = UType.CUSTOMER; 

	
	public User(String name, String address, String credNo, int cartCount,UType utype){
		this.name = name;
		this.address = address;
		this.credNo = credNo;
		this.cartCount = cartCount;
		this.utype = utype;
	}
	
	public User(String name, String address, String credNo,UType utype){
		this.name = name;
		this.address = address;
		this.credNo = credNo;
		this.cartCount = cartCount;
		this.utype = utype;
	}
	
	public User(String name, String password){
		this.name = name;
		this.password = password;
	}
	
	public User() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public UType getUtype() {
		return utype;
	}
	public void setUtype(UType utype) {
		this.utype = utype;
	}
	public String getCredNo() {
		return credNo;
	}
	public void setCredNo(String credNo) {
		this.credNo = credNo;
	}
	public int getCartCount() {
		return cartCount;
	}
	public void setCartCount(int cartCount) {
		this.cartCount = cartCount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
