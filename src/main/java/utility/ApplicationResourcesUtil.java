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

	final public static String USERNAME = System.getProperty("user.name");
	final public static String USERHOME = System.getProperty("user.home");

	final static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
	
	final public static String DBDriver = resourceBundle.getString("DBDriver");
	final public static String DBUserId = resourceBundle.getString("DBUserId");
	final public static String DBPassword = resourceBundle.getString("DBPassword");
	final public static String DBURL = resourceBundle.getString("DBURL");
	final public static String shotwellImportFolder = resourceBundle.getString("shotwellImportFolder");
	final public static String imageFilesImportFolder = resourceBundle.getString("imageFilesImportFolder");
	final public static String length = resourceBundle.getString("length");
	
}

