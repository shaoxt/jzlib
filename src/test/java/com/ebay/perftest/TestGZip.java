package com.ebay.perftest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestGZip extends TestZipBase {
	
	private static Process perfp;

	@BeforeClass
	public static void setup() throws IOException {
		
		
		readProperties();
		System.out.println(file);
		 try {
			 inbytes = getBytesFromFile(new File(file));
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

//	@Test
	public void testGzip() throws ClassNotFoundException {

		String gzipFile = "gziptest-" + fileSize + Thread.currentThread().getName() + ".zip";
		compressGzipFile(file, gzipFile);
		decompress(gzipFile);

	}

	@Test
	public void testGziptoNull() throws ClassNotFoundException {

		compressGzipFiletoNull(file);

	}
	

	private static byte[] inbytes;
	
	static {
	
	}

	private static void compressGzipFile(String file, String gzipFile) {
		try {

			byte[] inbytes = getBytesFromFile(new File(file));
			FileOutputStream fos = new FileOutputStream(gzipFile);

			long t_start = System.currentTimeMillis();
			GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
			gzipOS.write(inbytes, 0, inbytes.length);
			long t_end = System.currentTimeMillis();
			long duration = t_end - t_start;
			System.out.println("ziptime:" + duration);
			// close resources
			gzipOS.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	private void compressGzipFiletoNull(String file) {
		try {			
			NullOutputStream nos = new NullOutputStream();
			MeteredStream mos = new MeteredStream(nos, 0);
			long t_start = System.currentTimeMillis();
			GZIPOutputStream gzipOS = new GZIPOutputStream(mos);
			gzipOS.write(inbytes, 0, inbytes.length);
			gzipOS.finish();
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
	
	private void compressGzipFiletoNull2(String file) {
		try {			
			ByteArrayOutputStream nos = new ByteArrayOutputStream();
			MeteredStream mos = new MeteredStream(nos, 0);
			long t_start = System.currentTimeMillis();
			GZIPOutputStream gzipOS = new GZIPOutputStream(mos);
			gzipOS.write("ABCABCABCABCABC".getBytes());
			gzipOS.finish();
//			int size = mos.getWritten();
			gzipOS.close();
			long t_end = System.currentTimeMillis();
			long duration = t_end - t_start;
			System.out.println("ziptime:" + duration + " " + new BigInteger(nos.toByteArray()).toString(16));

			gzipOS.close();
			nos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void decompress(String filename) {
		try {

			String outFilename = filename + ".txt";
			System.out.println("File for Extracting : " + filename);
			InputStream instream = new FileInputStream(filename);
			
			GZIPInputStream ginstream = new GZIPInputStream(instream);
			FileOutputStream outstream = new FileOutputStream(outFilename);

			
			
			byte[] buf = new byte[1024];
			int len;
			while ((len = ginstream.read(buf)) > 0) {
				outstream.write(buf, 0, len);
			}
			System.out.println("File Successfylly Extract");
			System.out.println("Extract file : " + outFilename);
			ginstream.close();
			outstream.close();
		}

		catch (IOException ioe) {
			System.out.println("Exception has been thrown" + ioe);
		}
	}

}
