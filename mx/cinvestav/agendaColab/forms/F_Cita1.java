/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.cinvestav.agendaColab.forms;
import java.util.Enumeration;
import javax.microedition.lcdui.*;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import java.util.Vector;

/**
 *
 * @author rockderick
 */
public class F_Cita1 extends List implements CommandListener{

    private Vector citas;

    public F_Cita1(String title){
            super(title,Choice.IMPLICIT);
            //Añadir citas
            //this.append("Alta Contacto",null);
           
            this.setCommandListener(this);

    }
    public void setElementos(Vector c){
        citas=c;
        //this.append(cita.toString(), null);
        //this.append("28/07/10 14:30-16:30 Ocupado", null);
        //this.append("29/07/10 11:00-12:00 Reunion", null);
        //this.append("29/07/10 14:30-16:30 Inventario", null);

        Enumeration e=citas.elements();
        while(e.hasMoreElements()){
            BeanCita citas=(BeanCita)e.nextElement();
            this.append(citas.getAsunto(),null);
        }
    }

    public BeanCita getCitaSelected()
          {return (BeanCita) citas.elementAt(this.getSelectedIndex());
    }

    public void load_contac(String contact){

    }
    public void commandAction(Command c, Displayable d) {
		//comm.commandAction(c, d);
	}

}

