package com.ossecurity.people;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class DataExporter {
	private static String TAG = "DataExporter";

    public static final String DUMP_FILE_DIRECTORY_NAME = "contactdump";

    public static final String OUT_FILE_SUFFIX = "-contacts.txt";
    
    /**
     * Compress all files under the app data dir into a single zip file, and return the content://
     * URI to the file, which can be read via {@link DumpFileProvider}.
     */
    public static String exportData(Context context) throws IOException {
        final String fileName = "export" + OUT_FILE_SUFFIX;
        final File outFile = getOutputFile(context, fileName);
                
        // Remove all existing ones.
        removeDumpFiles(context);

        Log.i(TAG, "Dump started...");
        
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
    	//final String FILENAME = "";
    	FileWriter fw;
    	BufferedWriter bw;
    	
    	// Make sure all directories are intact.
  	  	ensureOutputDirectory(context);
  	  	
  	  	try {
  	  		
  	  		// if file doesnt exists, then create it
  			if (!outFile.exists()) {
  				outFile.createNewFile();
  			}
  			
  	  		fw = new FileWriter(outFile.getAbsoluteFile());
  	  		bw = new BufferedWriter(fw);
  	  		bw.write("NAME							PHONE NUMBER\n");
  	  	
	    	while (phones.moveToNext())
	    	{
	    	  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	    	  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	    	  
	    	  Log.v(TAG, "Name : "+name);
	    	  Log.v(TAG, "Phone number : "+phoneNumber);
	    	  
	    	  bw.append(name);
	    	  bw.append("						");
	    	  bw.append(phoneNumber+"\n");
	    	  
	    	}
	    	bw.close();
	    	fw.close();
  	  	} catch( IOException e) {
  	  		e.printStackTrace();
  	  	} finally {
  	    	phones.close();
  	  	}

        ensureOutputDirectory(context);
        
        Log.i(TAG, "Dump finished.");
        return getOutputDirectory(context)+"/"+fileName;
    }

    private static File getOutputDirectory(Context context) {
    	Log.i("DataExporter", "Db File Location : "+context.getCacheDir().getAbsolutePath());
        return new File(context.getCacheDir(), DUMP_FILE_DIRECTORY_NAME);
    }

    private static void ensureOutputDirectory(Context context) {
        final File directory = getOutputDirectory(context);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static File getOutputFile(Context context, String fileName) {
        return new File(getOutputDirectory(context), fileName);
    }

    public static boolean dumpFileExists(Context context) {
        return getOutputDirectory(context).exists();
    }

    public static void removeDumpFiles(Context context) {
        removeFileOrDirectory(getOutputDirectory(context));
    }

    private static void removeFileOrDirectory(File file) {
        if (!file.exists()) return;

        if (file.isFile()) {
            Log.i(TAG, "Removing " + file);
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                removeFileOrDirectory(child);
            }
            Log.i(TAG, "Removing " + file);
            file.delete();
        }
    }
}
