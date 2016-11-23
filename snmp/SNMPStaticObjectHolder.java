/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package snmp;

import snmp.utils.SNMPUtils;

/**
 *
 * @author eduard.margulies
 */
public class SNMPStaticObjectHolder implements ISNMPObjectHolder{

    protected SNMPObject _object = null;
    protected ISNMPObjectHolder _nextObject = null;
    protected boolean _isReadOnly = true;
    protected String OID;
    
    public SNMPStaticObjectHolder(SNMPObject object){
        _object = object;
    }
    
    public SNMPStaticObjectHolder(SNMPObject object, boolean isReadOnly){
        this(object);
        _isReadOnly = isReadOnly;
    }
    
    public SNMPObject getObject() {
        return _object;
    }

    public ISNMPObjectHolder getNextObject() {
        return _nextObject;
    }

    public boolean isReadOnly() {
        return _isReadOnly;
    }

    public void setObject(SNMPObject object) throws SNMPException{
        if(!_isReadOnly){
            if(object.getClass().isInstance(_object)){
                _object = object;
           } else {
                throw new SNMPException("Cannot set object, because they are different types!");
           }
        }else{
            throw new SNMPException("Cannot set object, because its readonly!");
        }
    }

    public void setNextObject(ISNMPObjectHolder object) {
        _nextObject = object;
    }

    public String getOID() {
        return this.OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }
}
