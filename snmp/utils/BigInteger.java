/* Copyright  (c) 2008-2009 Eduard R. Margulies (eduardmargulies#mac.com)
 *                          Markus E. Toman (magicm#aon.at)
 *                          Manfred Siegl (m.siegl#citem.at)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the 
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 **/

package snmp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author eduardmargulies
 * Represents a BigInteger class. This class is a kind wrapper for the built in 
 * long value-type. This class "replaces" the java.math.BigInteger class which 
 * is not available in the java ME.
 */
public class BigInteger {
    long value = 0; // holds the value
    long max_value = Long.MAX_VALUE; // maximum value
    
    /// constructs a BigInteger - object from a byte-array
    public BigInteger(byte[] bytes) throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);
        this.value = dis.readLong();
    }
    
    /// constructs a BigInteger - object from a string value
    public BigInteger(String value){
        this.value = Long.parseLong(value);
    }
    
    /// constructs a BigInteger - object from a int - value
    public BigInteger(int value){
        this.value = value;
    }
    
    /// constructs a BigInteer - object from a long - value
    public BigInteger(long value){
        this.value = value;
    }
     
    /*
     * Returns a byte - Array representing the value 
     */
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        
        dos.writeLong(this.value);
        dos.flush();
        return bos.toByteArray();
    }
    
    /****
     * Calculates the modulo 
     * @param m value%m
     * @return The rest.
     */
    public BigInteger mod(BigInteger m){
        return new BigInteger(this.value % m.value); 
    }
    
    /****
     * Returns the minimum from m and the current class
     * @param m
     * @return m if value is greater as m, this otherwise
     */
    public BigInteger min(BigInteger m){
        return m.value < this.value ? m:this; 
    }
    
    /****
     * Returns the maximum from m and the current class
     * @param m
     * @return m if value is lesser as m, this otherwise
     */
    public BigInteger max(BigInteger m){
        return m.value > this.value ? m:this; 
    }
    
    /****
     * Returns the long value
     * @return The long value of this instance.
     */
    public long longValue(){
        return this.value; 
    }
    
    /****
     * Returns the int - value
     * @param m
     * @return The int - value of this instance
     */
    public int intValue(){
        return (int)this.value; 
    }
}
