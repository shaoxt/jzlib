package com.ebay.perftest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestChar1 extends TestZipBase {
	private static Process perfp;

	@BeforeClass
	public static void setup() throws IOException {
		
		
		readProperties();
		System.out.println(file);
		 try {
			 inbytes = getCharsFromFile(new File(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	   try {
		   perfp = Runtime.getRuntime().exec( perfCmd);
		      
	    }
	    catch (Exception err) {
	      err.printStackTrace();
	    }
			  	
		
	}
	
	@AfterClass
	public  static void destroy() throws InterruptedException, IOException{
		Process killp = Runtime.getRuntime().exec( killperfCmd);
		killp.waitFor();
		if (perfp != null){
			perfp.destroy();
			perfp.waitFor();
			
		}
	}

	@Test
	public void testCharToByte() throws ClassNotFoundException {

		compressGzipFiletoNull(file);

	}
	

	private static char[] inbytes;
	
	private void compressGzipFiletoNull(String file) {
		try {			
			NullOutputStream nos = new NullOutputStream();
			MeteredStream mos = new MeteredStream(nos, 0);
			long t_start = System.currentTimeMillis();
			OutputStreamWriter gzipOS = new OutputStreamWriter(mos, "UTF8");
			
			gzipOS.write(inbytes, 0, inbytes.length);
			
			int size = mos.getWritten();
			gzipOS.close();
			long t_end = System.currentTimeMillis();
			long duration = t_end - t_start;
			System.out.println("ziptime:" + duration + " " + size);

			gzipOS.close();
			nos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
