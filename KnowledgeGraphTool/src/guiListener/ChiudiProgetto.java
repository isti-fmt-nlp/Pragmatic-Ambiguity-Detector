/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiListener;

import controllers.ControlloreProgetto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 *
 * @author Peppe
 */
public class ChiudiProgetto extends Observable implements ActionListener{
    ControlloreProgetto cp=ControlloreProgetto.getInstance();
    @Override
    public void actionPerformed(ActionEvent ae) {
             cp.chiudiProgetto();
             this.setChanged();
             this.notifyObservers();
    }
}