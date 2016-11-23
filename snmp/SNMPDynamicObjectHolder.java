/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package snmp;

/**
 *
 * @author eduard.margulies
 */
public class SNMPDynamicObjectHolder extends SNMPStaticObjectHolder {
    protected IDynamicValueGenerator _generator = null;
    
    public SNMPDynamicObjectHolder(IDynamicValueGenerator generator){
        // setting the object null
        super(null);
        _generator = generator;
    }
    
    public SNMPObject getObject() {
        if(_generator != null){
           return  _generator.generateValue();
        }
        else
        {
            return _object;
        }
    }
}
