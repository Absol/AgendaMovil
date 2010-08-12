package mx.cinvestav.agendaColab;

import java.util.Date;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import mx.cinvestav.agendaColab.DAO.EventoColaDAO;
import mx.cinvestav.agendaColab.DAO.UsuarioDAO;
import mx.cinvestav.agendaColab.comun.Cambio;
import mx.cinvestav.agendaColab.comun.Cancelacion;
import mx.cinvestav.agendaColab.comun.Confirmacion;
import mx.cinvestav.agendaColab.comun.Respuesta;
import mx.cinvestav.agendaColab.comun.Sincronizacion;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.movil.http.HttpPostAgenda;

/**
 * @author absol
 */
public class AgendaPull extends MIDlet {

    public void startApp() {
        BeanUsuario usu = UsuarioDAO.getMyUser();

        if (usu != null) {
            EventoColaDAO cola = new EventoColaDAO();
            Vector vecEnviar, vecRecibir;
             vecEnviar = cola.getListaEventos(usu);

            HttpPostAgenda servidor
                    = new HttpPostAgenda("belldandy.no-ip.info/AgendaServer/");

             vecRecibir = servidor.pullEventos(vecEnviar);

            new PullController(this, vecRecibir);
        }else{
            destroyApp(true);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
