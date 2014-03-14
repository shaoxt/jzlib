package com.ebay.perftest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

/**
 * Custom GZIP output stream that forces the compressor to flush.
 * 
 * This implementation is based on the recommendations from this issue:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4255743
 * <p>
 * In the {@link #flush()} method, we basically switch the compression level
 * to force the deflater to flush.
 * <p>
 * This issue is fixed in Java 1.7.
 * <p>
 * This should be only used for flushing of chunks in support of progressive rendering,
 * as there's a performance impact.
 * 
 * @author Thuc Nguyen
 * @see com.ebay.raptor.kernel.cmd.handler.impl.FlushGZipHandler
 *
 */
public class CustomGZIPOutputStream extends GZIPOutputStream {

   public CustomGZIPOutputStream(OutputStream out, int size) throws IOException {
      super(out, size);
      def.setLevel(Deflater.DEFAULT_COMPRESSION);
   }

   @Override
   protected void deflate() throws IOException {
      int len = def.deflate(buf, 0, buf.length);
      while (len > 0) {
         out.write(buf, 0, len);
         len = def.deflate(buf, 0, buf.length);
      }
   }
   
   private static byte[] EMPTYBYTEARRAY = new byte[0];

   /**
    * Force the compressor to flush whatever it has so far.
    * <p>
    * This is invoked from {@link com.ebay.raptor.kernel.cmd.handler.impl.FlushGZipHandler#handleResponse}
    */
   @Override
   public void flush() throws IOException {
      def.setInput(EMPTYBYTEARRAY, 0, 0);

      def.setLevel(Deflater.NO_COMPRESSION);
      deflate();

      def.setLevel(Deflater.DEFAULT_COMPRESSION);
      deflate();

      super.flush();
   }
}
