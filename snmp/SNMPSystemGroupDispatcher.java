package snmp;

import com.sun.spot.peripheral.Spot;
import java.util.Enumeration;
import snmp.utils.SNMPUtils;

/**
 * The system-group dispatcher.
 * @author eduard.margulies
 */
public class SNMPSystemGroupDispatcher extends SNMPObjectDispatcher {

    /**
     * MEMBERS
     */

    private final String CONTACT = "Eduard R. Margulies";
    private final String SYSTEMNAME = "Sun SPOT";
    private final String SYSTEMLOCATION = "Anywhere";
    private final String SYSTEMSERVICES = "SNMP - Device";
    private String communityName = "public";
    
    private static SNMPSystemGroupDispatcher instance = new SNMPSystemGroupDispatcher();
    
    /**
     * CTOR
     */
    private SNMPSystemGroupDispatcher(){
        OID = "1.3.6.1.2.1.1";
        NAME = "MIB II system Group 1.3.6.1.2.1.1";   
        SNMPUtils.DebugMessage(this, "Setting up " + NAME);
        
        ISNMPObjectHolder holder = null;
        ISNMPObjectHolder previous = null; 
        
        //SYSTEM-Description
        holder = new SNMPStaticObjectHolder(getSystemDescription());
        previous = addEntry(OID + ".1", holder, null);
       
        //SYSTEM-Object ID
        holder = new SNMPStaticObjectHolder(new SNMPOctetString(System.getProperty("IEEE_ADDRESS")));
        previous = addEntry(OID + ".2", holder, previous);

        //SYSTEM-uptime
        holder = new SNMPDynamicObjectHolder(new SystemUpTime());
        previous = addEntry(OID + ".3", holder, previous);
        
        //SYSTEM-contact
        holder = new SNMPStaticObjectHolder(new SNMPOctetString(CONTACT), false);
        previous = addEntry(OID + ".4", holder, previous);
        
        //SYSTEM-name
        holder = new SNMPStaticObjectHolder(new SNMPOctetString(SYSTEMNAME), false);
        previous = addEntry(OID + ".5", holder, previous);
        
        //SYSTEM-location
        holder = new SNMPStaticObjectHolder(new SNMPOctetString(SYSTEMLOCATION), false);
        previous = addEntry(OID + ".6", holder, previous);
        
        //SYSTEM-services
        holder = new SNMPStaticObjectHolder(new SNMPOctetString(SYSTEMSERVICES));
        previous = addEntry(OID + ".7", holder, previous);
    }

    
    public static SNMPSystemGroupDispatcher Instance(){
        if(instance == null){
            instance = new SNMPSystemGroupDispatcher();
        }
        return instance;
    }

    /**
     * GEN-Functions
     */
    protected SNMPOctetString getSystemDescription(){
        String revision = Spot.getInstance().getPersistentProperty("spot.hardware.rev");
        String firmwareVersion = Spot.getInstance().getPersistentProperty("spot.powercontroller.firmware.version");
        String sdkVersion = Spot.getInstance().getPersistentProperty("spot.sdk.version");
        
        return new SNMPOctetString("SUN Microsystem SUN SPOT, Revision: " + revision + ", Firmware version: " +  
                firmwareVersion + ", SDK - Version: " +  sdkVersion );
    }
}
