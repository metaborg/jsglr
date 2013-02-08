/*
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of California, Berkeley nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.spoofax.terms.io.binary;

import static org.spoofax.terms.Term.isTermList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;


import org.Logger;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;

import com.google.gwt.core.client.GWT;

/**
 * A term reader that uses IStrategoTerms.
 * 
 * Reconstructs an ATerm from the given (series of) buffer(s). It can be
 * retrieved when the construction of the term is done / when isDone() returns
 * true.<br />
 * <br />
 * For example (yes I know this code is crappy, but it's simple):<blockquote>
 * 
 * <pre>
 * ByteBuffer buffer = ByteBuffer.allocate(8192);
 * BinaryWriter bw = new BinaryWriter(aterm);
 * while (!bw.isDone()) {
 *     int bytesRead = channel.read(buffer); // Read the next chunk of data from the stream.
 *     if (!buffer.hasRemaining() || bytesRead == -1) {
 *         bw.serialize(buffer);
 *         buffer.clear();
 *     }
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * @see TermReader A helper class that supports both binary and textual ATerms.
 * 
 * @author Arnold Lankamp
 * @author Nathan Bruning (ported to Spoofax)
 */
class SAFReader {
    private final static int ISSHAREDFLAG = 0x00000080;

    private final static int TYPEMASK = 0x0000000f;

    private final static int ANNOSFLAG = 0x00000010;

    private final static int ISFUNSHARED = 0x00000040;

    private final static int APPLQUOTED = 0x00000020;

    private final static int INITIALSHAREDTERMSARRAYSIZE = 1024;

    private final static int STACKSIZE = 256;

    private final ITermFactory factory;

    private int sharedTermIndex;

    private IStrategoTerm[] sharedTerms;

    private List<StrategoSignature> applSignatures;

    private ATermConstruct[] stack;

    private int stackPosition;

    private int tempType = -1;

    private byte[] tempBytes = null;

    private int tempBytesIndex = 0;

    private int tempArity = -1;

    private boolean tempIsQuoted = false;

    private ByteBuffer currentBuffer;

    private boolean isDone = false;

    private final boolean debug = false;

    class StrategoSignature {
        IStrategoConstructor cons;

        boolean isString;
    }

    /**
     * Constructor.
     * 
     * @param factory
     *            The factory to use for reconstruction of the ATerm.
     */
    public SAFReader(ITermFactory factory) {
        super();

        this.factory = factory;

        sharedTerms = new IStrategoTerm[INITIALSHAREDTERMSARRAYSIZE];
        applSignatures = new ArrayList<StrategoSignature>();
        sharedTermIndex = 0;

        stack = new ATermConstruct[STACKSIZE];
        stackPosition = -1;
    }

    /**
     * Resizes the shared terms array when needed. When we're running low on
     * space the capacity will be doubled.
     */
    private void ensureSharedTermsCapacity() {
        int sharedTermsArraySize = sharedTerms.length;
        if (sharedTermIndex + 1 >= sharedTermsArraySize) {
            IStrategoTerm[] newSharedTermsArray = new IStrategoTerm[sharedTermsArraySize << 1];
            System.arraycopy(sharedTerms, 0, newSharedTermsArray, 0,
                    sharedTermsArraySize);
            sharedTerms = newSharedTermsArray;
        }
    }

    /**
     * Constructs (a part of) the ATerm from the binary representation present
     * in the given buffer. This method will 'remember' where it was left.
     * 
     * @param buffer
     *            The buffer that contains (a part of) the binary representation
     *            of the ATerm.
     */
    
    int getalletje = 0;
    public void deserialize(ByteBuffer buffer) {
    	getalletje++;
    	try
    	{
    		deserialize_(buffer);
    	} catch (Exception e)
    	{
    		
    		Logger.Log("Caught error at iteration: " + getalletje+ " - " + e.getMessage());
    	}
    }
   
    
    public void deserialize_(ByteBuffer buffer) {
    	
        currentBuffer = buffer;

        if (tempType != -1)
            readData();

        while (buffer.hasRemaining()) {
            byte header = buffer.get();

            if (debug) {
                for (int i = 0; i < (stackPosition + 1); i++) {
                    System.out.print(" ");
                }
            }

            if ((header & ISSHAREDFLAG) == ISSHAREDFLAG) {
                int index = readInt();

                IStrategoTerm term = sharedTerms[index];
                stackPosition++;
                if (debug)
                    System.out.println(term);

                linkTerm(term);
            } else {
                int type = (header & TYPEMASK);

                ATermConstruct ac = new ATermConstruct();
                ac.type = type;
                ac.hasAnnos = ((header & ANNOSFLAG) == ANNOSFLAG);

                ac.termIndex = sharedTermIndex++;
                ensureSharedTermsCapacity();

                stack[++stackPosition] = ac;

                if (debug)
                    System.out.print("parsing " + type + ": ");

                TYPECHECK: switch (type) {
                case ATermConstants.AT_APPL:
                    touchAppl(header);
                    break TYPECHECK;
                case ATermConstants.AT_LIST:
                    touchList();
                    break TYPECHECK;
                case ATermConstants.AT_INT:
                    touchInt();
                    break TYPECHECK;
                case ATermConstants.AT_REAL:
                    touchReal();
                    break TYPECHECK;
                default:
                    throw new RuntimeException("Unknown type id: " + type
                            + ". Current buffer position: "
                            + currentBuffer.position());
                }
                if (debug)
                    System.out.println();
            }

            // Make sure the stack remains large enough
            ensureStackCapacity();
        }
    }

    /**
     * Resizes the stack when needed. When we're running low on stack space the
     * capacity will be doubled.
     */
    private void ensureStackCapacity() {
        int stackSize = stack.length;
        if (stackPosition + 1 >= stackSize) {
            ATermConstruct[] newStack = new ATermConstruct[(stackSize << 1)];
            System.arraycopy(stack, 0, newStack, 0, stack.length);
            stack = newStack;
        }
    }

    /**
     * Checks if we are done serializing.
     * 
     * @return True if we are done; false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the reconstructed ATerm. A RuntimeException will be thrown when
     * we are not yet done with the reconstruction of the ATerm.
     * 
     * @return The reconstructed ATerm.
     */
    public IStrategoTerm getRoot() {
        if (!isDone)
            throw new RuntimeException(
                    "Can't retrieve the root of the tree while it's still being constructed.");

        return sharedTerms[0];
    }

    /**
     * Resets the temporary data. We don't want to hold it if it's not
     * necessary.
     */
    private void resetTemp() {
        tempType = -1;
        tempBytes = null;
        tempBytesIndex = 0;
    }

    /**
     * Reads a series of bytes from the buffer. When the nessecary amount of
     * bytes is read a term of the corresponding type will be constructed and
     * (if possible) linked with it's parent.
     */
    private void readData() {
        int length = tempBytes.length;
        int bytesToRead = (length - tempBytesIndex);
        int remaining = currentBuffer.remaining();
        if (remaining < bytesToRead)
            bytesToRead = remaining;

        currentBuffer.get(tempBytes, tempBytesIndex, bytesToRead);
        tempBytesIndex += bytesToRead;

        if (tempBytesIndex == length) {
            if (tempType == ATermConstants.AT_APPL) {

                ATermConstruct ac = stack[stackPosition];

                StrategoSignature sig = new StrategoSignature();
                sig.cons = factory.makeConstructor(new String(tempBytes),
                        tempArity);
                sig.isString = tempIsQuoted;
                if (debug)
                    System.out.print(new String(tempBytes) + "/" + tempArity);

                applSignatures.add(sig);

                if (tempArity == 0 && !ac.hasAnnos) {
                    IStrategoTerm term = convertAppl(sig);
                    sharedTerms[ac.termIndex] = term;
                    linkTerm(term);
                } else {
                    ac.tempSig = sig;
                    ac.subTerms = new IStrategoTerm[tempArity];
                }

            } else {
                throw new RuntimeException("Unsupported chunkified type: "
                        + tempType);
            }

            resetTemp();
        }
    }

    private IStrategoTerm convertAppl(StrategoSignature sig) {
        return convertAppl(sig, new IStrategoTerm[0]);
    }

    private IStrategoTerm convertAppl(StrategoSignature sig,
            IStrategoTerm[] subterms) {
        if (sig.isString) {
            return factory.makeString(sig.cons.getName());
        } else if (sig.cons.getName().length() == 0) {
            return factory.makeTuple(subterms);
        } else {
            return factory.makeAppl(sig.cons, subterms);
        }
    }

    /**
     * Starts the deserialization process of a appl.
     * 
     * @param header
     *            The header of the appl.
     */
    private void touchAppl(byte header) {

        if ((header & ISFUNSHARED) == ISFUNSHARED) {
            int key = readInt();

            StrategoSignature sig = applSignatures.get(key);
            if (debug)
                System.out.print(sig.cons + " (shared)");

            int arity = sig.cons.getArity();

            ATermConstruct ac = stack[stackPosition];

            if (arity == 0 && !ac.hasAnnos) {

                IStrategoTerm term = convertAppl(sig);
                sharedTerms[ac.termIndex] = term;
                linkTerm(term);

            } else {
                ac.tempSig = sig;
                ac.subTerms = new IStrategoTerm[arity];
            }
        } else {
            tempIsQuoted = ((header & APPLQUOTED) == APPLQUOTED);
            tempArity = readInt();
            int nameLength = readInt();

            tempType = ATermConstants.AT_APPL;
            tempBytes = new byte[nameLength];
            tempBytesIndex = 0;

            readData();
        }
    }

    /**
     * Deserialializes a list.
     */
    private void touchList() {
        int size = readInt();

        ATermConstruct ac = stack[stackPosition];
        ac.subTerms = new IStrategoTerm[size];

        if (size == 0) {
            IStrategoTerm term = factory.makeList();

            if (!ac.hasAnnos) {
                sharedTerms[ac.termIndex] = term;
                linkTerm(term);
            } else {
                ac.tempTerm = term;
            }
        }
    }

    /**
     * Deserialializes an int.
     */
    private void touchInt() {
        int value = readInt();

        ATermConstruct ac = stack[stackPosition];
        IStrategoTerm term = factory.makeInt(value);

        if (!ac.hasAnnos) {
            sharedTerms[ac.termIndex] = term;
            linkTerm(term);
        } else {
            ac.tempTerm = term;
        }
    }

    /**
     * Deserialializes a real.
     */
    private void touchReal() {
        double value = readDouble();

        ATermConstruct ac = stack[stackPosition];
        IStrategoTerm term = factory.makeReal(value);

        if (!ac.hasAnnos) {
            sharedTerms[ac.termIndex] = term;
            linkTerm(term);
        } else {
            ac.tempTerm = term;
        }
    }

    /**
     * Constructs a term from the given structure.
     * 
     * @param ac
     *            A structure that contains all the nessecary data to contruct
     *            the associated term.
     * @return The constructed aterm.
     */
    private IStrategoTerm buildTerm(ATermConstruct ac) {

        IStrategoTerm constructedTerm;
        IStrategoTerm[] subTerms = ac.subTerms;

        int type = ac.type;
        if (type == ATermConstants.AT_APPL) {
            constructedTerm = convertAppl(ac.tempSig, subTerms);
        } else if (type == ATermConstants.AT_LIST) {
            IStrategoList list = factory.makeList();
            for (int i = subTerms.length - 1; i >= 0; i--) {
                list = factory.makeListCons(subTerms[i], list);
            }

            constructedTerm = list;
        } else if (ac.hasAnnos) {
            constructedTerm = ac.tempTerm;
        } else {
            // primitive terms should already be constructed in the touch
            // method.
            throw new RuntimeException("Unable to construct term.\n");
        }

        if (ac.hasAnnos) {
            constructedTerm = factory.annotateTerm(constructedTerm, ac.annos);
        }

        return constructedTerm;
    }

    /**
     * Links the given term with it's parent.
     * 
     * @param aTerm
     *            The term that needs to be linked.
     */
    private void linkTerm(IStrategoTerm aTerm) {
        IStrategoTerm term = aTerm;

        while (stackPosition != 0) {
            ATermConstruct parent = stack[--stackPosition];

            IStrategoTerm[] subTerms = parent.subTerms;
            boolean hasAnnos = parent.hasAnnos;
            if (subTerms != null && subTerms.length > parent.subTermIndex) {
                subTerms[parent.subTermIndex++] = term;

                if (parent.subTerms.length != parent.subTermIndex || hasAnnos)
                    return;

                // Remove?
                if (!hasAnnos)
                    parent.annos = factory.makeList();
            } else if (hasAnnos && isTermList(term)) {
                parent.annos = (IStrategoList) term;
            } else {
                throw new RuntimeException(
                        "Encountered a term that didn't fit anywhere. Type: "
                                + term.getTermType() + ", term " + term);
            }

            term = buildTerm(parent);

            sharedTerms[parent.termIndex] = term;
        }

        if (stackPosition == 0)
            isDone = true;
    }

    /**
     * A structure that contains all information we need for reconstructing a
     * term.
     * 
     * @author Arnold Lankamp
     */
    static class ATermConstruct {
        public int type;

        public int termIndex = 0;

        public IStrategoTerm tempTerm = null;

        public int subTermIndex = 0;

        public IStrategoTerm[] subTerms = null;

        public boolean hasAnnos;

        public IStrategoList annos;

        public StrategoSignature tempSig;
    }

    private final static int SEVENBITS = 0x0000007f;

    private final static int SIGNBIT = 0x00000080;

    private final static int BYTEMASK = 0x000000ff;

    private final static int BYTEBITS = 8;

    private final static int LONGBITS = 8;

    /**
     * Reconstructs an integer from the following 1 to 5 bytes in the buffer
     * (depending on how many we used to represent the value). See the
     * documentation of aterm.binary.BinaryWriter#writeInt(int) for more
     * information.
     * 
     * @return The reconstructed integer.
     */
    private int readInt() {
        byte part = currentBuffer.get();
        int result = (part & SEVENBITS);

        if ((part & SIGNBIT) == 0)
            return result;

        part = currentBuffer.get();
        result |= ((part & SEVENBITS) << 7);
        if ((part & SIGNBIT) == 0)
            return result;

        part = currentBuffer.get();
        result |= ((part & SEVENBITS) << 14);
        if ((part & SIGNBIT) == 0)
            return result;

        part = currentBuffer.get();
        result |= ((part & SEVENBITS) << 21);
        if ((part & SIGNBIT) == 0)
            return result;

        part = currentBuffer.get();
        result |= ((part & SEVENBITS) << 28);
        return result;
    }

    /**
     * Reconstructs a double from the following 8 bytes in the buffer.
     * 
     * @return The reconstructed double.
     */
    private double readDouble() {
    	System.out.println("sabotaged readDouble");
        //long result = readLong();
        return Double.MAX_VALUE;//Double.longBitsToDouble(result);
    }

    /**
     * Reconstructs a long from the following 8 bytes in the buffer.
     * 
     * @return The reconstructed long.
     */
    private long readLong() {
        long result = 0;
        for (int i = 0; i < LONGBITS; i++) {
            result |= ((((long) currentBuffer.get()) & BYTEMASK) << (i * BYTEBITS));
        }
        return result;
    }

    /**
     * Reads the ATerm from the given SAF encoded file.
     * 
     * @param factory
     *            The factory to use.
     * @param file
     *            The file that contains the SAF encoded term.
     * @return The constructed ATerm.
     * @throws IOException
     *             Thrown when an error occurs while reading the given file.
     */
    public static IStrategoTerm readTermFromSAFFile(ITermFactory factory,
            File file) throws IOException {
    	

        return null;
    }

    /**
     * Reads the ATerm from the given SAF encoded data.
     * 
     * @param factory
     *            The factory to use.
     * @param data
     *            The SAF encoded data.
     * @return The constructed ATerm.
     */
    public static IStrategoTerm readTermFromSAFString(ITermFactory factory,
            byte[] data) {
        SAFReader binaryReader = new SAFReader(factory);

        int length = data.length;
        int position = 0;
        do {

            int blockSize = data[position++] & 0x000000ff;
            blockSize += (data[position++] & 0x000000ff) << 8;
            if (blockSize == 0)
                blockSize = 65536;

            ByteBuffer byteBuffer = ByteBuffer.allocate(blockSize);

            byteBuffer.put(data, position, blockSize);
            position += blockSize;
            byteBuffer.flip();

            binaryReader.deserialize(byteBuffer);
        } while (position < length);

        if (!binaryReader.isDone())
            throw new RuntimeException("Term incomplete, missing data.\n");

        return binaryReader.getRoot();
    }

    /**
     * Read from stream. Uses buffer of 65535 bytes.
     * 
     * @throws IOException
     */
    public static IStrategoTerm readTermFromSAFStream(ITermFactory factory,
            InputStream in) throws IOException {
    	System.err.println("fdjkh54klj4h5");

        SAFReader binaryReader = new SAFReader(factory);
        
        
        ReadableChannelTest channel = new ReadableChannelTest(in);
        ByteBuffer byteBuffer = ByteBuffer.allocate(65536);

        // Consume the SAF identification token.
        byteBuffer.limit(1);
        
        
        
        
        char identifier=(char)0;
        try
        {
        	identifier = (char) in.read();
        } catch (Exception e)
        {
        	Logger.Log("Exception: " + e.getMessage());
        }
        //Logger.Log("identifier:" + identifier);
        
        
        if (identifier == -1)
            throw new IOException("Unable to read SAF identification token.\n");
        if (identifier != '?')
            throw new IOException("Not a SAF file.");

        int bytesRead;
        do {
            int size1 = in.read();
            int size2 = in.read();
            //Logger.Log("Availvable in: "+in.available());
            //Logger.Log("Availvable ch: "+channel.available());
            //Logger.Log("Size1:"+size1+"  Size2:"+size2);

            if (size1 < 0 || size2 < 0)
                break;
            int blockSize = size1 + (size2 << 8);
            int test = blockSize;
            
            if (blockSize == 0)
                blockSize = 65536;

            byteBuffer.clear();
            byteBuffer.limit(blockSize);

            // Use multiple reads to fill buffers (the channel apparently only
            // reads 8192 bytes per call)
            bytesRead = 0;
            while (bytesRead < blockSize) {
            	int res = channel.read(byteBuffer);
                if (res <= 0)
                    break; // end of file
                bytesRead += res;
            }
            
            //Logger.Log("bytes read: " + bytesRead);

            if (bytesRead != blockSize)
                throw new IOException("Unable to read bytes from file " + bytesRead + " vs " + blockSize + ".");

            byteBuffer.flip();
            
            //Logger.Log("bytebyffer Len("+bytesRead+") Blocksize("+blockSize+") test("+test+") sz1("+size1+") sz2("+size2+") " + byteBuffer.get(0)+ "." + byteBuffer.get(5)+ "."+ byteBuffer.get(10)+ "."+ byteBuffer.get(50)+ "."+ byteBuffer.get(150)+ "."+ byteBuffer.get(bytesRead-1));
            
            binaryReader.deserialize(byteBuffer);

        } while (bytesRead > 0);

        if (!binaryReader.isDone())
            throw new RuntimeException("Term incomplete, missing data.\n");
        //Logger.Log("BLAABALA");
        return binaryReader.getRoot();
    }

    public static boolean isStreamingATerm(BufferedInputStream stream)
            throws IOException {
        boolean isSaf = false;

        stream.mark(1);
        char ch = (char) stream.read();
        if (ch == '?')
            isSaf = true;

        stream.reset();

        return isSaf;
    }
}

