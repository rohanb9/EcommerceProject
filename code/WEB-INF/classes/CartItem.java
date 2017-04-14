
import java.util.*;

public class CartItem implements java.io.Serializable{
	private String name;
	private String description;
	private String buyer;
	
	public CartItem(String name, String description, String buyer){
		this.name = name;
		this.setDescription(description);
		this.setBuyer(buyer);
	}
	
	public CartItem(){}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

}
