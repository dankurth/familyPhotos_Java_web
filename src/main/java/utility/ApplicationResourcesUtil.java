package utility;

import java.util.ResourceBundle;

public class ApplicationResourcesUtil {

	public static String escapeBackslashes(String s) {
		return s.replace("\\", "\\\\");	   
	}

	public static String escapeSingleQuotes(String s) {
		return s.replace("\'", "\\'");	   
	}

	public static String escapeEvilChars(String s) {
		return escapeSingleQuotes(escapeBackslashes(s));
	}

	final static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
	
	final public static String DBDriver = resourceBundle.getString("DBDriver");
	final public static String DBUserId = resourceBundle.getString("DBUserId");
	final public static String DBPassword = resourceBundle.getString("DBPassword");
	final public static String DBURL = resourceBundle.getString("DBURL");
	final public static String length = resourceBundle.getString("length");
	
}

