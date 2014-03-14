package com.ebay.perftest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

public class TestZipBase {

	protected static String file;
	protected static String fileSize;
	protected static String  perfCmd = "cmd /c  typeperf \"\\Processor(_Total)\\% Processor Time\" -sc 2000 -o Report.csv";	
	protected static String killperfCmd = "taskkill /F /IM typeperf.exe ";

	protected static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "	+ file.getName());
		}
		is.close();
		return bytes;
	}
	
	protected static char[] getCharsFromFile(File file) throws IOException {
		Reader is = new FileReader(file);
		long length = file.length();
		char[] bytes = new char[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "	+ file.getName());
		}
		is.close();
		return bytes;
	}

	public static class MeteredStream extends OutputStream {
		OutputStream out;
        int written;

        public MeteredStream(OutputStream out, int written) {
            this.out = out;
            this.written = written;
        }

        public void write(int b) throws IOException {
            out.write(b);
            written++;
        }

        public void write(byte buff[]) throws IOException {
            out.write(buff);
            written += buff.length;
        }

        public void write(byte buff[], int off, int len) throws IOException {
            out.write(buff,off,len);
            written += len;
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
        }
        
        public int getWritten() {
        	return written;
        }
	}

	protected static void readProperties() throws IOException {

		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("com/ebay/perftest/config.properties");
		prop.load(stream);
		file = prop.getProperty("infile");
		fileSize = prop.getProperty("filesize");


	}

}
