

public enum UType
{
	CUSTOMER("CUSTOMER"),
	SALESMAN("SALESMAN"),	
	STOREMANAGER("STOREMANAGER"),
	RETAILER("RETAILER");
    
    private String text = null;
	
	private UType (String text) {
		this.text = text;
	}
	
	public String toString() {
		return this.text;
	}
	
	public static UType fromString(String text) {
	    if (text != null) {
	      for (UType b : UType.values()) {
	        if (text.equalsIgnoreCase(b.text)) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }
}
