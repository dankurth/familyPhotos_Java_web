package add;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.commons.codec.digest.DigestUtils;

import utility.ApplicationResourcesUtil;

public class ImportFromFolder {
	
	String computeMD5(String fileName) {
		String md5 = null;
		File file = new File((fileName));
		byte[] b = new byte[(int) file.length()];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(b);
			fis.close();
			md5 =  DigestUtils.md5Hex(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5;
	}
	
	ArrayList<String> getListOfPictureFileNames(String directory) {
		ArrayList<String> list = null;
		FileLister fl = new FileLister(); // create our file/dir lister
		list = fl.picsOnly(fl.listAllFiles(directory));
		return list;
	}
	
    int howMany(String md5) { // is specified MD5 already present in pictures table
		String url = ApplicationResourcesUtil.DBURL;
		String user = ApplicationResourcesUtil.DBUserId;
		String password = ApplicationResourcesUtil.DBPassword;
		String query = "select count(*) from pictures where md5 = '"+md5+"'";
		int rows = 0;

		try {
			Connection con = DriverManager.getConnection(url, user, password);
			if (con != null) {
				PreparedStatement pst = con.prepareStatement(query);
				ResultSet rs = pst.executeQuery();
				rs.next();
				rows = rs.getInt("count");
				con.close();
			}
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
		System.out.println(md5 + " count:" + rows);
		return rows;
	}

	int insertIntoPictures(String md5, String viewGroup, String filenameMedium, String filenameSmall) {

		String url = ApplicationResourcesUtil.DBURL;
		String user = ApplicationResourcesUtil.DBUserId;
		String password = ApplicationResourcesUtil.DBPassword;
		String update = "insert into pictures (md5,owner,viewgroup,mediumblob,smallblob) values (?,?,?,?,?)";
		
    	int rows = 0;

    	try {
			Connection con = DriverManager.getConnection(url, user, password);
			if (con != null) {
				PreparedStatement pst = con.prepareStatement(update);
					pst.setString(1, md5);
					pst.setString(2, user);
					pst.setString(3, viewGroup);

					File fileMedium = new File(filenameMedium);
					FileInputStream finMedium = new FileInputStream(fileMedium);
					pst.setBinaryStream(4, finMedium, fileMedium.length());

					File fileSmall = new File(filenameSmall);
					FileInputStream finSmall = new FileInputStream(fileSmall);
					pst.setBinaryStream(5, finSmall, fileSmall.length());

					int row = pst.executeUpdate();
					rows += row;
					System.out.println("insert: "+md5+","+user+","+viewGroup);
				con.close();
			}
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    	return rows;
	}

	public static void main(String args[]) {
		final String directory = ApplicationResourcesUtil.USERHOME + "/" + ApplicationResourcesUtil.imageFilesImportFolder;
		ImportFromFolder imp = new ImportFromFolder();
		final String mediumBlobFileName = "/tmp/1024x768.jpg"; // temporary converted file
		final String smallBlobFileName = "/tmp/165x165.jpg";

		ImageConverter converter = new ImageConverter();
		ArrayList<String> picsList = imp.getListOfPictureFileNames(directory);
		ListIterator<String> iter = picsList.listIterator();
		while (iter.hasNext()) {
			String picFileName = iter.next();
			String md5 = imp.computeMD5(picFileName);
			System.out.println(md5 + " " + picFileName);
			converter.convert(picFileName, "1024x768", mediumBlobFileName);
			converter.convert(picFileName, "165x165", smallBlobFileName);
			if (imp.howMany(md5)==0) { // might be more efficient to just try to insert and throw exception instead?
				String viewGroup = "public"; // default
				if (picFileName.contains("/family/")) {
					viewGroup = "family";
				}
				imp.insertIntoPictures(md5, viewGroup, mediumBlobFileName, smallBlobFileName);
			}
			
//			System.exit(0); // for testing only
		}
		
	}
	
	

}
