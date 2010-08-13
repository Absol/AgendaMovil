/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.cinvestav.agendaColab.forms;
import java.util.Enumeration;
import javax.microedition.lcdui.*;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import java.util.Vector;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;

/**
 *
 * @author rockderick
 */
public class F_ListaSincros extends List implements CommandListener{

    private Vector sincros;

    public F_ListaSincros(String title){
            super(title,Choice.IMPLICIT);
            //Añadir citas
            //this.append("Alta Contacto",null);
           
            this.setCommandListener(this);

    }
    public void setElementos(Vector c){
        sincros=c;
        this.deleteAll();

        Enumeration e=sincros.elements();
        while(e.hasMoreElements()){
            String str=(String)e.nextElement();
            this.append(str,null);
        }
    }

    public BeanUsuario getSincSelected(){
        return (BeanUsuario)sincros.elementAt(this.getSelectedIndex());
    }

    public void load_contac(String contact){

    }
    public void commandAction(Command c, Displayable d) {
		//comm.commandAction(c, d);
	}

}

