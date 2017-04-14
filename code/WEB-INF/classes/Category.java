

public enum Category
{
	SMARTPHONES("SmartPhones"),
	TABLETS("Tablets"),
    LAPTOPS("Laptops"),
    TV("TV"),
    OTHER("Other");
    
    private String text = null;
	
	private Category (String text) {
		this.text = text;
	}
	
	public String toString() {
		return this.text;
	}
	
	public static Category fromString(String text) {
	    if (text != null) {
	      for (Category b : Category.values()) {
	        if (text.equalsIgnoreCase(b.text)) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }
}
