package mx.cinvestav.agendaColab;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;

/**
 * @author absol
 */
class PullController {
private IteradorEventos iterador = null;
private AgendaPull applic;

    public PullController(AgendaPull midlet, Vector eventos) {
        iterador = new IteradorEventos(eventos, this);

        while(iterador.hasMoreElements()){
            iterador.procesa();
        }

        this.applic.destroyApp(false);
        this.applic.notifyDestroyed();
    }

    public void commandAction(Command c, Displayable d) {
    }

    void avisaCancelacion(BeanCita cita) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void avisarRespuesta(BeanCita cita, boolean respuesta) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void pedirConfirmacion(BeanCita cita, BeanUsuario usu) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
}
