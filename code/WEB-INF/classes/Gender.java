

public enum Gender
{
	MALE("MALE"),
	FEMALE("FEMALE");
    
    private String text = null;
	
	private Gender (String text) {
		this.text = text;
	}
	
	public String toString() {
		return this.text;
	}
	
	public static Gender fromString(String text) {
	    if (text != null) {
	      for (Gender b : Gender.values()) {
	        if (text.equalsIgnoreCase(b.text)) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }
}
