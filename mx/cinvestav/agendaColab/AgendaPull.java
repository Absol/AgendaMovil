package mx.cinvestav.agendaColab;

import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import mx.cinvestav.agendaColab.DAO.EventoColaDAO;
//import mx.cinvestav.agendaColab.pruebas.EventoColaDAO;
import mx.cinvestav.agendaColab.DAO.UsuarioDAO;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;

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

//          HttpPostAgenda servidor
//                    = new HttpPostAgenda("belldandy.no-ip.info/AgendaServer/");
            vecRecibir = vecEnviar;

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
