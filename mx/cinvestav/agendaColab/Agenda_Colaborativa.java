package mx.cinvestav.agendaColab;

import javax.microedition.midlet.MIDlet;
import mx.cinvestav.agendaColab.DAO.EventoColaDAO;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.agendaColab.utils.controller.FlujoController;

/**
 * @author eduardogiron
 */
public class Agenda_Colaborativa extends MIDlet {
    public void startApp() {

        new FlujoController(this);
        
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
