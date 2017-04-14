

public enum YesNo
{
	YES("YES"),
	NO("NO");
    
    private String text = null;
	
	private YesNo (String text) {
		this.text = text;
	}
	
	public String toString() {
		return this.text;
	}
	
	public static YesNo fromString(String text) {
	    if (text != null) {
	      for (YesNo b : YesNo.values()) {
	        if (text.equalsIgnoreCase(b.text)) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }
}
