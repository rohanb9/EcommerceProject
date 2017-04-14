
import java.util.*;

public class Order implements java.io.Serializable{
	private int id;
	private String name;
	private String description;
	private String buyer;
	private String retailer;
	private double price;
	
	private OrderStatus status = OrderStatus.ORDERED; 
	private Date orderDate = new Date();
	
	public Order(String name, String description, String buyer, OrderStatus status){
		this.name = name;
		this.setDescription(description);
		this.setBuyer(buyer);
		this.setStatus(status);
	}
	
	public Order(){}
	
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getRetailer() {
		return retailer;
	}

	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
