/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Gearoid
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "mdb")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class debuggingMessages implements MessageListener {
    
    public debuggingMessages() {
    }
    
    @Override
    public void onMessage(Message message) {
        try{
        System.out.println("Received from queue: " + message.getStringProperty("log"));
        }catch(Exception QueueError){
            System.out.println("Error received from message queue: " + QueueError.toString());
        }
    }
    
}
