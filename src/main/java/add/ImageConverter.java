package add;

import java.io.*;

/**
 * Utility to do image conversions In current implementation simply runs
 * ImageMagick commands (the bash scripts now work with folders and spaces 
 * in file names so are probably a better choice going forward). 
 * 
 * @author Dan Kurth
 * 
 */
public class ImageConverter {

	public void convert(String infile, String size, String outfile) {
		/*
		 * painfully learned lesson: in linux cannot use "-resize 200x200"
		 * apparently if there is space between option and arg that space needs
		 * to be provided via different arg in String[] args also each option
		 * must have it's own arg (cannot combine as in
		 * "-flatten -resize 200x200" none of the other remedies such as
		 * escaping or quoting space worked, so using exec that takes String[]
		 * rather than String
		 * 
		 * -flatten required for psd, else output multiple files
		 */
		String[] commands = null;
//		final String[] windowsCommands = { "cmd", "/c",
//				"C:\\ImageMagick-6.4.3-Q16\\convert", infile,
//				"-flatten", "-resize", size, outfile };
		final String[] linuxCommands = { "/usr/bin/convert", infile,
				"-flatten", "-resize", size, outfile };
//		System.out.println("os.name is " + System.getProperty("os.name"));
//		if ("Windows".startsWith(System.getProperty("os.name"))) {
//			commands = windowsCommands;
//		} else {
			commands = linuxCommands;
//		}

		String s = null;
		// assumes the input file and directory to contain the output file
		// already exist
		// if not then ImageMagick convert will will fail and report error

		try {
			Process p = Runtime.getRuntime().exec(commands);
			p.waitFor();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
