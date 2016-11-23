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

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.squawk.util.Arrays;
import java.io.IOException;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;

/**
 *
 * @author eduard.margulies
 * Provides some very useful methods for our SNMP - package.
 */
public class SNMPUtils {
    private static boolean DEBUG = true;  
    public static final int LED_POSITION_RECEIVING = 6;
    public static final int LED_POSITION_SENDING = 7;
    
    /***
     * Simple prints the message with class-name prefix of object, just if the 
     * class-property DEBUG is set to true
     * @param object The "caller" 
     * @param message The message
     */
    public static void DebugMessage(Object object, String message){
        if(DEBUG){
            System.out.println(object.getClass().getName() + ": " + message); 
        }
    }

    /**
     * Returns a Datagram from the provided connection con, filled with data an addressed to the address
     * @param con Provided connection
     * @param data The data - buffer
     * @param length The length in the buffer
     * @param address the target address 
     * @return Returns a Datagram - Object based on the data stored in "data" and addressed to address
     * @throws java.io.IOException
     */
    public static Datagram newDatagram(DatagramConnection con, byte[] data, int length, String address) throws IOException {
        Datagram datagram = con.newDatagram(length, address);
        datagram.reset();
        datagram.write(data);
        return datagram;
    }

    /**
     * Returns a Datagram from the provided connection con, filled with data
     * @param con Provided connection
     * @param data The data - buffer
     * @param length The length in the buffer
     * @return Returns a Datagram - Object based on the data stored in "data"
     * @throws java.io.IOException
     */
    public static Datagram newDatagram(DatagramConnection con, byte[] data, int length) throws IOException {
        return newDatagram(con, data, 0, length);
    }

    /**
     * Returns a Datagram from the provided connection con, filled with data
     * @param con Provided connection
     * @param data The data - buffer
     * @param offset Result data copy will start at this offset
     * @param length The amount of bytes to copy
     * @return Returns a Datagram - Object based on the data stored in "data"
     * @throws java.io.IOException
     */
    public static Datagram newDatagram(DatagramConnection con, byte[] data, int offset, int length) throws IOException {
        byte[] copy = Arrays.copy(data, offset, length, 0, length);
        Datagram datagram = con.newDatagram(length);
        datagram.reset();
        datagram.write(copy);
        return datagram;
    }
    
    /**
     * Sets all LEDs of the sensor-board to the color specified by red, green, blue
     * @param red 
     * @param green
     * @param blue
     */
    public static void AllLEDsOn(int red, int green, int blue){
        int lCount = LEDCount();
        for(int i = 0; i < lCount; ++i){
            LEDOn(i, red, green, blue);
        }
    }
    
    /**
     * Sets all LEDs of the sensor-board off 
     */
    public static void AllLEDsOff(){
        int lCount = LEDCount();
        for(int i = 0; i < lCount; ++i){
            LEDOff(i);
        }
    }
    
    /**
     * Returns the amount of leds installed on the EDemoBoard
     */
    public static int LEDCount(){
        ITriColorLED [] leds = EDemoBoard.getInstance().getLEDs();
        return leds.length;
    }
                
    /***
     * Turns a led with a color (RGB) on. 
     * @param index The LED - index
     * @param red Color value for red
     * @param green Color value for green
     * @param blue Color value for blue 
     */
    public static void LEDOn(int index, int red, int green, int blue){
        ITriColorLED [] leds = EDemoBoard.getInstance().getLEDs();
        if(index < leds.length)
        {
            leds[index].setRGB(red, green, blue);
            leds[index].setOn();
        }
    }
    
    /**
     * Turns the LED on index "index" off 
     * @param index The LED - index 
     */
    public static void LEDOff(int index){
        ITriColorLED [] leds = EDemoBoard.getInstance().getLEDs();
        if(index < leds.length)
        {
            leds[index].setOff();
        }
    }
    
    /**
     * Lets a led blink up "blinks" - times with "delay" delay between the blink cycle 
     * on given position "position"
     * @param index The led - index position
     * @param delay The delay between blinks 
     * @param position The position of the led
     * @param blinks The amount of blinks
     * @param red 
     * @param green
     * @param blue
     */
    public static void LEDBlink(int index, int delay, int blinks, int red, int green, int blue){
        new LEDSignal(red, green, blue, blinks, delay, index ).Blink();
    }
}
