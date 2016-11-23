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
 * This interface provides a basic schemne of a SNMP - object dispatcher class
 * @author eduard.margulies
 */
public interface ISNMPObjectDispatcher {
    
    /**
     * Returns the corresponding SNMP - object for the given OID
     * @param OID
     * @return The snmp - object holder
     */
    public ISNMPObjectHolder getObject(String OID);
    
    /**
     * Returns the offical name of the Dispatcher.
     * @return
     */
    public String getName();
   
    /**
     * Returns the OId of the Dispatcher.
     * @return
     */
    public String getOID();
    
    /**
     * Returns all availabe key - OIDS as String - array.
     * @return
     */
    public String[] getAvailableOIDs();
    
    /**
     * Checks if the Dispatcher provides the key OID
     * @param OID The key to check
     * @return true if the Dispatcher provides that OID, false otherwise
     */
    public boolean providesOID(String OID);
    
    /**
     * Returns the next - object dispatcher
     * @return
     */
    public ISNMPObjectDispatcher getNextDispatcher();
    
    /**
     * Sets the next object dispatcher
     * @return
     */
    public void setNextDispatcher(ISNMPObjectDispatcher nextDispatcher);
    
    /**
     * Sets the valid community - string in the dispatcher
     * @param communityName
     */
    public void setCommunity(String communityName);
    
    /**
     * Returns the current set valid community - name
     * @return 
     */
    public String getCommunity();
    
    /**
     * Returns the first object in the dispatcher list.
     * This method is very useful to retrieve a the next object 
     * of the linked dispatcher to achieve the walk through all elementes with
     * GETNEXT requests. 
     * @return
     */
    public ISNMPObjectHolder getFirstObject();
}
