package snmp;
import java.util.*;
import com.sun.spot.peripheral.Spot;
import java.util.Enumeration;
import snmp.utils.SNMPUtils;



public class snmpTreeRouteTable<T> extends SNMPObjectDispatcher {

    /**
     * MEMBERS
     */

    private final String sensor1 = "sensor1 name";
    private final String sensor2 = "sensor2 name";
    private final String sensor3 = "sensor3 name";
    private final String sensor4 = "sensor4 name";
    private String communityName = "public";
	//random ones for trials
    private GenericTreeNode<T> root,s1,s2,s3,s4,s5,s6;

    private static snmpTreeRouteTable  instance = new snmpTreeRouteTable();



    /**
     * CTOR
     */
    private snmpTreeRouteTable(){
        OID = "1.3.6.1.2.1.55";
        NAME = "MIB II snmp table Group 1.3.6.1.2.1.55";
        SNMPUtils.DebugMessage(this, "Setting up " + NAME);


        ISNMPObjectHolder holder = null;
        ISNMPObjectHolder previous = null;


//        //SYSTEM-Description
//        holder = new SNMPStaticObjectHolder(getSystemDescription());
//        previous = addEntry(OID + ".1", holder, null);
//
//        //SYSTEM-Object ID
//        holder = new SNMPStaticObjectHolder(new SNMPOctetString(System.getProperty("IEEE_ADDRESS")));
//        previous = addEntry(OID + ".2", holder, previous);
//
//        //SYSTEM-uptime
//        holder = new SNMPDynamicObjectHolder(new SystemUpTime());
//        previous = addEntry(OID + ".3", holder, previous);





        holder = new SNMPStaticObjectHolder(new SNMPOctetString(sensor1), false);
        previous = addEntry(OID + ".1", holder, previous);
        s1.setOid(OID + ".1");

        holder = new SNMPStaticObjectHolder(new SNMPOctetString(sensor2), false);
        previous = addEntry(OID + ".2", holder, previous);
        s2.setOid(OID + ".2");

        holder = new SNMPStaticObjectHolder(new SNMPOctetString(sensor3), false);
        previous = addEntry(OID + ".3", holder, previous);
        s3.setOid(OID + ".3");

        holder = new SNMPStaticObjectHolder(new SNMPOctetString(sensor4), false);
        previous = addEntry(OID + ".4", holder, previous);
        s4.setOid(OID + ".4");

        holder = new SNMPStaticObjectHolder(new SNMPOctetString(sensor5), false);
        previous = addEntry(OID + ".5", holder, previous);
        s5.setOid(OID + ".5");

        holder = new SNMPStaticObjectHolder(new SNMPOctetString(sensor6), false);
        previous = addEntry(OID + ".6", holder, previous);
        s6.setOid(OID + ".6");



        Sensoradd(root,s1);
        Sensoradd(root,s2);
        Sensoradd(s1,s3);
        Sensoradd(s3,s4);
        Sensoradd(root,s5);
        Sensoradd(s5,s6);
    }


    //this will simulate adding a sensor under a specific parent
    private void Sensoradd(GenericTreeNode<T> parent, GenericTreeNode<T> sensor){
        parent.addChild(sensor);
    }



    public GenericTreeNode<T> getRoot() {
        return this.root;
    }
    //setter for the root
    public void setRoot(GenericTreeNode<T> root) {
        this.root = root;
    }

    public int getNumberOfNodes() {
        int numberOfNodes = 0;

        if(root != null) {
            numberOfNodes = auxiliaryGetNumberOfNodes(root) + 1; //1 for the root!
        }

        return numberOfNodes;
    }
    //recursive finding the children of the node
    private int auxiliaryGetNumberOfNodes(GenericTreeNode<T> node) {
        int numberOfNodes = node.getNumberOfChildren();

        for(GenericTreeNode<T> child : node.getChildren()) {
            numberOfNodes += auxiliaryGetNumberOfNodes(child);
        }

        return numberOfNodes;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public List<GenericTreeNode<T>> build(GenericTreeTraversalOrderEnum traversalOrder) {
        List<GenericTreeNode<T>> returnList = null;

        if(root != null) {
            returnList = build(root, traversalOrder);
        }

        return returnList;
    }


    public List<GenericTreeNode<T>> build(GenericTreeNode<T> node, GenericTreeTraversalOrderEnum traversalOrder) {
        List<GenericTreeNode<T>> traversalResult = new ArrayList<GenericTreeNode<T>>();

        if(traversalOrder == GenericTreeTraversalOrderEnum.PRE_ORDER) {
            buildPreOrder(node, traversalResult);
        }

        else if(traversalOrder == GenericTreeTraversalOrderEnum.POST_ORDER) {
            buildPostOrder(node, traversalResult);
        }

        return traversalResult;
    }

    private void buildPreOrder(GenericTreeNode<T> node, List<GenericTreeNode<T>> traversalResult) {
        traversalResult.add(node);

        for(GenericTreeNode<T> child : node.getChildren()) {
            buildPreOrder(child, traversalResult);
        }
    }

    private void buildPostOrder(GenericTreeNode<T> node, List<GenericTreeNode<T>> traversalResult) {
        for(GenericTreeNode<T> child : node.getChildren()) {
            buildPostOrder(child, traversalResult);
        }

        traversalResult.add(node);
    }








    public static snmpTreeRouteTable  Instance(){
        if(instance == null){
            instance = new snmpTreeRouteTable();
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
