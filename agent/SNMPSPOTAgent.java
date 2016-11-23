package agent;

/* Copyright  (c) 2008-2009 Eduard R. Margulies (eduardmargulies#mac.com)
 *                          Markus E. Toman (magicm#aon.at)
 *                          Manfred Siegl (m.siegl#citem.at)
 * 
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc.
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
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.IAccelerometer3D;
import java.io.IOException;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.DatagramConnection;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.util.Utils;
import snmp.utils.*;
import javax.microedition.midlet.MIDletStateChangeException;
import snmp.*;

/**
 * SNMPSPOTAgent
 *
 * This agent runs on a SunSPOT and waits for incomming SNMP v1 requests
 * and returns, if possible, appropiate responses. 
 * 
 * @author eduard (modifications by markus)
 */
public class SNMPSPOTAgent extends javax.microedition.midlet.MIDlet implements Runnable {

    // MEMBERS
    SNMPv1AgentInterface agentInterface;
    String communityName = "public";
    SNMPOctetString storedSNMPValue;
    Thread readerThread;
    boolean haveReportFile = false;
    ITriColorLED[] leds;

    /**
     * Here we start the app :) 
     * @throws javax.microedition.midlet.MIDletStateChangeException
     */
    protected void startApp() throws MIDletStateChangeException {
        //NOTE: if you dont like the leds blinking uncomment the leds-on parts
        
        System.out.println("STARTING SNMP - Service Agent on SUNSPOT");
        // Initialize and start the application
        // let all LEDs blink
        snmp.utils.SNMPUtils.AllLEDsOn(255, 255, 255);
        Utils.sleep(500);
        snmp.utils.SNMPUtils.AllLEDsOff();
        Utils.sleep(500);
        snmp.utils.SNMPUtils.AllLEDsOn(255, 255, 255);

        // INIT AGENT
        init();

        snmp.utils.SNMPUtils.AllLEDsOff();
        Utils.sleep(250);

        snmp.utils.SNMPUtils.AllLEDsOn(0, 255, 0);
        Utils.sleep(500);
        snmp.utils.SNMPUtils.AllLEDsOff();
        Utils.sleep(500);
        
        readerThread = new Thread(this);
        readerThread.start();
    }

    /***
     * Pause the app, we dont need that. yet.
     */
    protected void pauseApp() {
    }

    /**
     * Called if the MIDlet is terminated by the system.
     * I.e. if startApp throws any exception other than MIDletStateChangeException,
     * if the isolate running the MIDlet is killed with Isolate.exit(), or
     * if VM.stopVM() is called.
     * 
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     *    cleanup and release all resources. If false the MIDlet may throw
     *    MIDletStateChangeException  to indicate it does not want to be destroyed
     *    at this time.
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        ITriColorLED[] leds = EDemoBoard.getInstance().getLEDs();
        for (int i = 0; i < 8; i++) {           // turn off the LEDs when we exit
            leds[i].setOff();
        }
    }

    /***
     * Initializes the SunSPOT SNMP agents
     */
    public void init() {    
        try {            
            int version = 0;    // SNMPv1
            
             SNMPUtils.DebugMessage(this, "SETTING UP DISPATCHER");
             
            // set up dispatchers
            SNMPSystemGroupDispatcher.Instance().setNextDispatcher(null);
            
            String[] arr = SNMPSystemGroupDispatcher.Instance().getAvailableOIDs();
            SNMPUtils.DebugMessage(this, Long.toString(arr.length));
            for(int i = 0; i < arr.length; ++i){
                SNMPUtils.DebugMessage(this, arr[i]);
            }

            //TODO CONC
            DatagramConnection dcc = (DatagramConnection) Connector.open("radiogram://:99");
            SNMPUtils.DebugMessage(this, "Connection established!");
            agentInterface = new SNMPv1AgentInterface(version, dcc);
            SNMPUtils.DebugMessage(this, "Interface created!");
            agentInterface.addRequestListener(SNMPSystemGroupDispatcher.Instance());
            agentInterface.setReceiveBufferSize(5120);
            agentInterface.startReceiving();
            SNMPUtils.DebugMessage(this, "Init successfull!");
        } catch (Exception e) {
            SNMPUtils.DebugMessage(this, e.getMessage());
        }
    }



    /**
     * This method just implements some LED - blinking, yes funny, but nothing
     * more. 
     */
    public void run() {
        int startLed = 1;
        int currentLed = startLed;
        int step = 1;
        int maxLed = 3;

        snmp.utils.SNMPUtils.LEDOn(0, 0, 0, 255);
        snmp.utils.SNMPUtils.LEDOn(4, 0, 0, 255);
        
        try {
            while (true) {
                snmp.utils.SNMPUtils.LEDOn(currentLed, 255, 255, 255);
                Utils.sleep(50);
             
                snmp.utils.SNMPUtils.LEDOff(currentLed);
                
                Utils.sleep(50);
                
                currentLed += step;
                if (currentLed > maxLed) {
                    currentLed = maxLed;
                    step = -1;
                }
                else if(currentLed < startLed){
                    currentLed = startLed;
                    step = 1;
                }
            }
        } catch (Exception e) {}
    }
}
