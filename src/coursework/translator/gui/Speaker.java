/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework.translator.gui;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Speaker {
    
    private ArrayList<Listener> listeners;

    public Speaker() {
        listeners = new ArrayList<>();
    }
    
    public void addListener(Listener listener)
    {
        listeners.add(listener);
    }
     
    public void performAction(int id)
    {
        for(int i = 0; i < listeners.size(); i++)
        {
            listeners.get(i).actionPerformed(id);
        }
    }

}
