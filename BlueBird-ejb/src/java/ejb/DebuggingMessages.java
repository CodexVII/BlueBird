/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Gearoid, Trevor, Alan and Ian
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "mdb")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class DebuggingMessages implements MessageListener {
    FileOutputStream f1;
    public DebuggingMessages() {
        try{
           this.f1 = new FileOutputStream("log.txt", true);
        }catch(FileNotFoundException e){
            System.err.println("MDB File Error: " + e.toString());
        }
    }
    
    /**
     * writes messages received from JMS queue to log.txt file
     * @param message message to be written to file
     */
    @Override
    public void onMessage(Message message) {
        try{
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // Get current timestamp
            String toSend = timeStamp +": " + message.getStringProperty("log") + "\n"; //Build debug message with timestamp
            this.f1.write(toSend.getBytes()); // Write to file
            this.f1.flush(); // Flush to file
        }catch(Exception QueueError){
            System.out.println("Error received from message queue: " + QueueError.toString());
        }
    }
    
}
