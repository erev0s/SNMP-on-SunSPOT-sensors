package snmp;

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

/**
 * This interface provides a scheme for a basic SNMP object holder. A key - value
 * pair list should be generated. So the object can refer to each other (***NextObject).
 * @author eduard.margulies
 */
public interface ISNMPObjectHolder {
    /**
     * Returns the OID of this object-holder
     * @return The OID
     */
    public String getOID();
    
    public void setOID(String OID);
    
    /**
     * Returns the encapsulated SNMPObject
     * @return
     */
    public SNMPObject getObject();
    
    /**
     * Returns the object -holder for the next linked object
     * @return
     */
    public ISNMPObjectHolder getNextObject();
    
    /**
     * Indicates if that stored SNMPObject is readonly or not
     * @return true if the stored SNMP-Object is readonly, false otherwise
     */
    public boolean isReadOnly();
    
    /**
     * Sets the encapsulated SNMP-object
     * @param object The object which shall be stored in the holder
     */
    public void setObject(SNMPObject object) throws SNMPException;
    
    /**
     * Sets the reference to next linked object.
     * @param object The object which shall be referenced as next object
     */
    public void setNextObject(ISNMPObjectHolder object); 
}
