/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp;

import java.util.Enumeration;
import snmp.utils.SNMPUtils;

/**
 * A basic implementation of the ISNMPObjectDispatcher interface. This class
 * is of course abstract and provides all basic operation for that interface. 
 * @author eduard.margulies
 */
public abstract class SNMPObjectDispatcher implements ISNMPObjectDispatcher, SNMPRequestListener {

    /**
     * MEMBERS
     */
    protected SNMPObjectDictionary _objects = new SNMPObjectDictionary();
    protected ISNMPObjectDispatcher _nextDispatcher = null;
    protected String communityName = "public";
    protected String OID;
    protected String NAME;

    /**
     * METHODS
     */
    /**
     * Adds an entry to the _object - hashtable and sets a reference to next object.  
     * @param oid The OID for this element (key)
     * @param entry The entry (value) for this element
     * @param previousEntry The reference next-object from "previousEntry" will be set to th entry object. 
     * @return The newly added entry.
     */
    protected ISNMPObjectHolder addEntry(String oid, ISNMPObjectHolder entry, ISNMPObjectHolder previousEntry) {
        if (previousEntry != null) {
            previousEntry.setNextObject(entry);
        }
        entry.setOID(oid);

        SNMPUtils.DebugMessage(this, oid + " added to library...");
        _objects.put(oid, entry);
        SNMPUtils.DebugMessage(this, "CONTROLL: " + ((ISNMPObjectHolder) _objects.get(oid)).getObject().toString());
        return entry;
    }

    public void setCommunity(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunity() {
        return this.communityName;
    }

    public String getName() {
        return NAME;
    }

    protected void setName(String name) {
        this.NAME = name;
    }

    protected void setOID(String OID) {
        this.OID = OID;
    }

    public String getOID() {
        return this.OID;
    }

    /**
     * Returns a String - array containing all available OIDs for this 
     * dispatcher.
     * @return String - array containing all available OIDs for this dispatcher.
     */
    public String[] getAvailableOIDs() {
        String[] keys = new String[_objects.size()];
        Enumeration eKeys = _objects.keys();

        for (int i = 0; i < keys.length && eKeys.hasMoreElements(); ++i) {
            keys[i] = (String) eKeys.nextElement();
        }
        return keys;
    }

    /**
     * Checks if the current object - array provides the String - OID as key 
     * @param OID
     * @return true if the dispatcher provides the OID, false otherwise
     */
    public boolean providesOID(String OID) {
        return _objects.containsKey(OID);
    }

    public ISNMPObjectDispatcher getNextDispatcher() {
        return _nextDispatcher;
    }

    public void setNextDispatcher(ISNMPObjectDispatcher nextDispatcher) {
        _nextDispatcher = nextDispatcher;
    }

    public SNMPSequence processRequest(SNMPPDU pdu, String communityName) throws SNMPGetException, SNMPSetException {
        SNMPUtils.DebugMessage(this, "Got pdu:");
        SNMPUtils.DebugMessage(this, "  community name:     " + communityName);
        SNMPUtils.DebugMessage(this, "  request ID:         " + pdu.getRequestID());
        SNMPUtils.DebugMessage(this, "  pdu type:           ");
        byte pduType = pdu.getPDUType();

        switch (pduType) {
            case SNMPBERCodec.SNMPGETREQUEST: {
                SNMPUtils.DebugMessage(this, "SNMPGETREQUEST");
                break;
            }

            case SNMPBERCodec.SNMPGETNEXTREQUEST: {
                SNMPUtils.DebugMessage(this, "SNMPGETNEXTREQUEST");
                break;
            }

            case SNMPBERCodec.SNMPSETREQUEST: {
                SNMPUtils.DebugMessage(this, "SNMPSETREQUEST");
                break;
            }

            case SNMPBERCodec.SNMPGETRESPONSE: {
                SNMPUtils.DebugMessage(this, "SNMPGETRESPONSE");
                break;
            }

            case SNMPBERCodec.SNMPTRAP: {
                SNMPUtils.DebugMessage(this, "SNMPTRAP");
                break;
            }

            default: {
                SNMPUtils.DebugMessage(this, "unknown\n");
                break;
            }
        }

        SNMPSequence varBindList = pdu.getVarBindList();
        SNMPSequence responseList = new SNMPSequence();

        for (int i = 0; i < varBindList.size(); i++) {
            SNMPSequence variablePair = (SNMPSequence) varBindList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier) variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject) variablePair.getSNMPObjectAt(1);

            SNMPUtils.DebugMessage(this, "       OID:           " + snmpOID);
            SNMPUtils.DebugMessage(this, "       value:         " + snmpValue);


            // check to see if supplied communityName name is ours; if not, we'll just silently
            // ignore the request by not returning anything
            if (!communityName.equals(this.communityName)) {
                continue;
            }

            //check if we INTERNALY provide that OID, otherwise a SunSPOT request is pending
            if (providesOID(snmpOID.toString())) {
                SNMPUtils.DebugMessage(this, "OID - provided from dispatcher");
                ISNMPObjectHolder objectHolder = (ISNMPObjectHolder) _objects.get(snmpOID.toString());
                // is it a set or get request
                if (pduType == SNMPBERCodec.SNMPSETREQUEST) {
                    // READ-ONLY?
                    if (objectHolder.isReadOnly()) {
                        int errorIndex = i + 1;
                        int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                        throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                    } else {
                        try {
                            // Set the new provided value 
                            objectHolder.setObject(snmpValue);
                            try {
                                // return that OID, to let the receiver see that we handeled the OID
                                SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, objectHolder.getObject());
                                responseList.addSNMPObject(newPair);
                            } catch (SNMPBadValueException e) {
                                SNMPUtils.DebugMessage(this, "BadValueException");
                            //should not happen :)) 
                            }
                        } catch (SNMPException se) {
                            int errorIndex = i + 1;
                            int errorStatus = SNMPRequestException.BAD_VALUE;
                            throw new SNMPSetException(se.getMessage(), errorIndex, errorStatus);
                        }
                    }
                } else if (pduType == SNMPBERCodec.SNMPGETREQUEST) {
                    try {
                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), objectHolder.getObject());
                        responseList.addSNMPObject(newPair);
                    } catch (SNMPBadValueException e) {
                        // won't happen...
                    }
                }
            } else {
                // we do not provide that oid, so we asume that the requested
                // OID is for a sun spot
            }
        }
        // return the created list of variable pairs
        return responseList;
    }

    public SNMPSequence processGetNextRequest(SNMPPDU pdu, String communityName) throws SNMPGetException {
        SNMPUtils.DebugMessage(this, "Got pdu:");
        SNMPUtils.DebugMessage(this, "  community name:     " + communityName);
        SNMPUtils.DebugMessage(this, "  request ID:         " + pdu.getRequestID());
        SNMPUtils.DebugMessage(this, "  pdu type:           ");
        byte pduType = pdu.getPDUType();

        switch (pduType) {
            case SNMPBERCodec.SNMPGETREQUEST: {
                SNMPUtils.DebugMessage(this, "SNMPGETREQUEST");
                break;
            }

            case SNMPBERCodec.SNMPGETNEXTREQUEST: {
                SNMPUtils.DebugMessage(this, "SNMPGETNEXTREQUEST");
                break;
            }

            case SNMPBERCodec.SNMPSETREQUEST: {
                SNMPUtils.DebugMessage(this, "SNMPSETREQUEST");
                break;
            }

            case SNMPBERCodec.SNMPGETRESPONSE: {
                SNMPUtils.DebugMessage(this, "SNMPGETRESPONSE");
                break;
            }

            case SNMPBERCodec.SNMPTRAP: {
                SNMPUtils.DebugMessage(this, "SNMPTRAP");
                break;
            }

            default: {
                SNMPUtils.DebugMessage(this, "unknown\n");
                break;
            }
        }

        SNMPSequence varBindList = pdu.getVarBindList();
        SNMPSequence responseList = new SNMPSequence();

        for (int i = 0; i < varBindList.size(); i++) {
            SNMPSequence variablePair = (SNMPSequence) varBindList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier) variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject) variablePair.getSNMPObjectAt(1);

            SNMPUtils.DebugMessage(this, "       OID:           " + snmpOID);
            SNMPUtils.DebugMessage(this, "       value:         " + snmpValue);


            // check to see if supplied communityName name is ours; if not, we'll just silently
            // ignore the request by not returning anything
            if (!communityName.equals(this.communityName)) {
                continue;
            }

            //check if we INTERNALY provide that OID, otherwise a SunSPOT request is pending
            if (providesOID(snmpOID.toString())) {
                SNMPUtils.DebugMessage(this, "OID - provided from dispatcher");
                ISNMPObjectHolder objectHolder = (ISNMPObjectHolder) _objects.get(snmpOID.toString());
                // is it a set or get request
                if (pduType == SNMPBERCodec.SNMPGETNEXTREQUEST) {
                    try {
                        ISNMPObjectHolder nextObj = objectHolder.getNextObject();
                        if (nextObj != null) {
                            SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(nextObj.getOID()),
                                    nextObj.getObject());
                            responseList.addSNMPObject(newPair);
                        } else {
                            ISNMPObjectDispatcher nextDisp = this.getNextDispatcher();
                            if (nextDisp != null) {
                                nextObj = nextDisp.getFirstObject();
                                SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(nextObj.getOID()),
                                        nextObj.getObject());
                                responseList.addSNMPObject(newPair);
                            }
                        }
                    } catch (SNMPBadValueException e) {
                        // won't happen...
                    }
                }
            } else {
                // we do not provide that oid, so we asume that the requested
                // OID is for a sun spot
            }
        }
        // return the created list of variable pairs
        return responseList;
    }

    public ISNMPObjectHolder getObject(String OID) {
        return (ISNMPObjectHolder) _objects.get(OID);
    }

    public ISNMPObjectHolder getFirstObject() {
        Enumeration e = _objects.elements();
        return (ISNMPObjectHolder) e.nextElement();
    }
}
