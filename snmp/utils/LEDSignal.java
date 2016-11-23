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

import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.util.Utils;

/**
 * Represents a class which causes a led to blink in a seperate thread. 
 * @author eduardmargulies
 */
public class LEDSignal 
        implements Runnable{
    
    private int blinktimes = 1; 
    private LEDColor color;
    private int blinkdelay = 250;
    private int ledPosition = 0; 
    
    private Thread current; 
    
    /**
     * Constructs a new LEDSignal instance
     * @param red The color value for red
     * @param green The color value for green
     * @param blue The color value for blue
     * @param blinks The value for how often the led shall blink
     * @param blinkdelay The delay between the blinks
     * @param position The led - position
     */
    public LEDSignal(int red, int green, int blue, int blinks, int blinkdelay, int position){
        color = new LEDColor(red, green, blue);
        blinktimes = blinks;
        this.blinkdelay = blinkdelay; 
        ledPosition = position;
        current = new Thread(this);
    }
    
    /**
     * Starts the Blink - process
     */
    public void Blink(){
        if(current != null){
            current.start();
        }
    }
    
    public void run() {
        for(int i = 0; i < blinktimes; ++i){
            SNMPUtils.LEDOn(ledPosition, color.red(), color.green(), color.blue());
            Utils.sleep(blinkdelay);
            SNMPUtils.LEDOff(ledPosition);
            Utils.sleep(blinkdelay);
        }
    }
}
