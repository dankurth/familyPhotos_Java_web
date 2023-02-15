package add;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import utility.ApplicationResourcesUtil;

public class FileLister {

	/**
	 * Get all files in directory as absolute path names.
	 * @param directory
	 * @return
	 */
	public ArrayList<String> listAllFiles(String directory) {
		ArrayList<String> allFiles = new ArrayList<String>();
		listFiles(directory,allFiles);
		return allFiles;
	}
	
	/**
	 * Get all files in directory as path names relative to that directory
	 * (the directory short name itself and all contents of it and subdirectories).
	 * Listing is intended to be easy to use as context relative names in web app.
	 * @param String directory
	 * @return ArrayList
	 */
	public ArrayList<String> listAllFilesAsRelativeTo(String directory) {
		int startIndex = directory.lastIndexOf('/') + 1; 
		ArrayList<String> relPaths = new ArrayList<String>();
		ArrayList<String> absPaths = new ArrayList<String>();
		listFiles(directory,absPaths); //populate absPaths		
		Iterator<String> it = absPaths.iterator();
		while (it.hasNext()) {
			String file = (String) it.next();
			String relativeFile = file.substring(startIndex);
			relPaths.add(relativeFile);
		}
		return relPaths;
	}
	
	/**
	 * Given directory and existing ArrayList adds all files in that directory and subdirectories to the ArrayList.
	 * Adds files only, not directories. 
	 * Does not create or return the Arraylist. Also does not remove any prior elements or check for duplicates.
	 * @param directory
	 * @param fileList
	 */
	private void listFiles(String directory, ArrayList<String> fileList) {
		File dir = new File(directory);
		if (!dir.isDirectory())
			throw new IllegalArgumentException(
					"FileLister: no such directory:\n" + directory);
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			File f = new File(directory, files[i]); // Convert to a File
			String fullname = f.getAbsolutePath();
			if (f.isDirectory()) {
				listFiles(fullname, fileList); // recurse down into dir
			} else {
				fileList.add(f.getAbsolutePath());
			}
		}
	}
	
	/**
	 * Given an ArrayList of files returns ArrayList of only those with pic type file name 
	 * extension (jpg, gif, png). Extension in file name may be either upper or lower case, 
	 * or combination thereof :). 
	 * @param allFiles
	 * @return someFiles
	 */
	public ArrayList<String> picsOnly(ArrayList<String> allFiles) {
		ArrayList<String> someFiles = new ArrayList<String>();
		Iterator<String> it = allFiles.iterator();		
		while (it.hasNext()) {
			String file = (String) it.next();
			String ext = file.substring(file.lastIndexOf('.') + 1);
			if (ext.equalsIgnoreCase("jpg") 
					|| ext.equalsIgnoreCase("gif")
					|| ext.equalsIgnoreCase("png")
					|| ext.equalsIgnoreCase("psd")
					|| ext.equalsIgnoreCase("tif")
					|| ext.equalsIgnoreCase("bmp")) {
				someFiles.add(file);
			}
		}
		
		return someFiles;
	}

	public static void main(String args[]) {
		final String directory = ApplicationResourcesUtil.imageFilesImportFolder;

		FileLister fl = new FileLister(); // create our file/dir lister
		ArrayList<String> filesList = fl.listAllFiles(directory);
		int counter = 0;
		Iterator<String> it = filesList.iterator();		
		System.out.println("test listAllFiles(directory)");
		while (it.hasNext()) {
			System.out.println(counter++ +": "+it.next());
		}
		
		System.out.println("test listAllFilesAsRelativeTo(directory)");		
		ArrayList<String> relativeFilesList = fl.listAllFilesAsRelativeTo(directory);
		counter = 0;
		it = relativeFilesList.iterator();
		while (it.hasNext()) {
			System.out.println(counter++ +": "+it.next());
		}
		
		System.out.println("test picsOnly(listAllFilesAsRelativeTo(directory))");		
		ArrayList<String> picsList = fl.picsOnly(fl.listAllFilesAsRelativeTo(directory));
		it = picsList.iterator();
		counter = 0;
		while (it.hasNext()) {
			System.out.println(counter++ +": "+it.next());
		}
	}

}
