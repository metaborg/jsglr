package org.spoofax.terms.io.binary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
//import java.nio.channels.ReadableByteChannel;
//import java.nio.channels.spi.AbstractInterruptibleChannel;

 public class ReadableChannelTest 
    {
        InputStream in;
        private static final int TRANSFER_SIZE = 8192;
        private byte buf[] = new byte[0];
        private boolean open = true;
        private Object readLock = new Object();

        ReadableChannelTest(InputStream in) {
            this.in = in;
        }

        public int read(ByteBuffer dst) throws IOException {
            int len = dst.remaining();
            int totalRead = 0;
            int bytesRead = 0;
            synchronized (readLock) {
                while (totalRead < len) {
                    int bytesToRead = Math.min((len - totalRead),
                                               TRANSFER_SIZE);
                    if (buf.length < bytesToRead)
                        buf = new byte[bytesToRead];
                    if ((totalRead > 0) && !(in.available() > 0))
                        break; // block at most once
                    try {
                        //begin();
                        bytesRead = in.read(buf, 0, bytesToRead);
                    } finally {
                        //end(bytesRead > 0);
                    }
                    if (bytesRead < 0)
                        break;
                    else
                        totalRead += bytesRead;
                    dst.put(buf, 0, bytesRead);
                }
                if ((bytesRead < 0) && (totalRead == 0))
                    return -1;

                return totalRead;
            }
        }
        
        public int read() throws IOException {
        	synchronized (readLock) {
        		buf = new byte[1];
        		if (in.read(buf,0,1) == 1)
        			return buf[0];
        		else
        			return -1;
        	}
        }

        protected void implCloseChannel() throws IOException {
            in.close();
            open = false;
        }

		public int available() throws IOException {
			synchronized (readLock) {
				return in.available();
			}
		}
    }
