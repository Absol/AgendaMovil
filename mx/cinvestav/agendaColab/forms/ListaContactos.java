package mx.cinvestav.agendaColab.forms;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.List;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;

/**
 * @author absol
 */
public class ListaContactos extends List {
private Vector contactos;
    public ListaContactos(){
        super("Contactos", Choice.IMPLICIT);
        contactos = new Vector();
    }

    public void setElementos(Vector lista) {
        contactos = lista;

        this.deleteAll();
        Enumeration e = contactos.elements();
        while(e.hasMoreElements()){
            BeanContacto cont = (BeanContacto)e.nextElement();
            this.append(cont.getnombre() + " " + cont.getapPaterno() + " " + cont.getapMaterno(), null);
        }
    }

    public BeanContacto getContactoSelected(){
        return (BeanContacto) contactos.elementAt(this.getSelectedIndex());
    }
}
