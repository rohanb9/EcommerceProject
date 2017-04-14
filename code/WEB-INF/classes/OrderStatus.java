


public enum OrderStatus
{
	CANCEL("CANCEL"),
	ORDERED("ORDERED");
    
    private String text = null;
	
	private OrderStatus (String text) {
		this.text = text;
	}
	
	public String toString() {
		return this.text;
	}
	
	public static OrderStatus fromString(String text) {
	    if (text != null) {
	      for (OrderStatus b : OrderStatus.values()) {
	        if (text.equalsIgnoreCase(b.text)) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }
}
