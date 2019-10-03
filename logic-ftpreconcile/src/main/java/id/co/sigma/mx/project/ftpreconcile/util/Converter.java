package id.co.sigma.mx.project.ftpreconcile.util;

public class Converter {
	
	public static final int LEFT_JUSTIFIED = 1;
	
	public static String pad(String s, char c, int length, int justified) {
	    StringBuffer buf = new StringBuffer(s);

	    if (0 == justified) {
	      for (int i = 0; i < length - s.length(); i++) {
	        buf.insert(0, c);
	      }
	    }
	    else if (1 == justified) {
	      for (int i = 0; i < length - s.length(); i++) {
	        buf.append(c);
	      }
	    }

	    return buf.toString();
	  }
}
