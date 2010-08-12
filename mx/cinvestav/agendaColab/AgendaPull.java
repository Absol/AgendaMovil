package mx.cinvestav.agendaColab;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import mx.cinvestav.agendaColab.DAO.EventoColaDAO;
//import mx.cinvestav.agendaColab.pruebas.EventoColaDAO;
import mx.cinvestav.agendaColab.DAO.UsuarioDAO;
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
            cola.deleteAllRecordStore("evento");

          HttpPostAgenda servidor
                    = new HttpPostAgenda("belldandy.no-ip.info/AgendaServer/");
          vecRecibir = servidor.pullEventos(vecEnviar);
             Enumeration enumeration=vecEnviar.elements();
             while(enumeration.hasMoreElements()){
                 System.out.println(enumeration.nextElement());
             }
            //new PullController(this, vecRecibir);
        }else{
            destroyApp(true);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
