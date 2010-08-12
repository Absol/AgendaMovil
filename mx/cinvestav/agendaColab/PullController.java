package mx.cinvestav.agendaColab;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import mx.cinvestav.agendaColab.DAO.EventoColaDAO;
import mx.cinvestav.agendaColab.DAO.UsuarioDAO;
import mx.cinvestav.agendaColab.comun.Cancelacion;
import mx.cinvestav.agendaColab.comun.Respuesta;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.agendaColab.forms.MuestraInformacion;

/**
 * @author absol
 */
class PullController implements CommandListener {
private IteradorEventos iterador = null;
private AgendaPull applic;
private MuestraInformacion aviso;
private EventoColaDAO dao = new EventoColaDAO();
protected Display display = null;
private Command continuar = new Command("Aceptar", Command.OK, 1);
private Command aceptar = new Command("Aceptar", Command.OK, 1);
private Command cancel = new Command("Cancelar cita", Command.EXIT, 1);
private Command modificar = new Command("Cambiar horario", Command.OK, 1);
private Command rechazar = new Command("Rechazar", Command.EXIT, 1);
protected BeanUsuario usuario;
protected BeanCita cita;

    public PullController(AgendaPull midlet, Vector eventos) {
        iterador = new IteradorEventos(eventos, this);
        display = Display.getDisplay(midlet);
        aviso = new MuestraInformacion("");
        applic = midlet;

        siguiente();
    }

    public void commandAction(Command c, Displayable d) {
        if(c == continuar){
            siguiente();
        } else if(c == cancel){
            dao.guardarEvento(new Cancelacion(cita));
            siguiente();
        } else if(c == aceptar){
            dao.guardarEvento(new Respuesta(cita, usuario, true));
            siguiente();
        } else if(c == rechazar){
            dao.guardarEvento(new Respuesta(cita, usuario, false));
            siguiente();
        } else if(c == modificar){
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    void avisaCancelacion(BeanCita cita) {
        aviso.setCommandListener(this);
        limpiaComandos();
        aviso.setTitle("Cita Cancelada");
        String avisoS;
        avisoS = "La cita con asunto \"" + cita.getAsunto() + "\" fue cancelada ";
        aviso.setString(avisoS);
        aviso.addCommand(continuar);
        display.setCurrent(aviso);
    }

    void avisarRespuesta(BeanCita cita, BeanUsuario usu, boolean respuesta) {
        limpiaComandos();
        aviso.setCommandListener(this);
        aviso.setTitle("Respuesta Recibida");
        String avisoS;
        avisoS = "El usuario \"" + usu.getLogin() +"\" ha respondido a la " +
                "cita con asunto \"" + cita.getAsunto() + "\" ";
        if(respuesta){
            avisoS = avisoS + "afirmativamente";
            aviso.addCommand(continuar);
        }else{
            avisoS = avisoS + "negativamente";
            aviso.addCommand(cancel);
            aviso.addCommand(modificar);
            this.cita = cita;
        }
        aviso.setString(avisoS);
        display.setCurrent(aviso);
    }

    void pedirConfirmacion(BeanCita cita, BeanUsuario usu) {
        this.cita = cita;
        this.usuario = UsuarioDAO.getMyUser();
        limpiaComandos();
        aviso.setCommandListener(this);
        aviso.setTitle("Invitacion Recibida");
        String avisoS;
        avisoS = "El usuario \"" + usu.getLogin() +"\" te ha agregado a la " +
                "cita con asunto \"" + cita.getAsunto() + "\" de " +
                cita.getFechaInicio() + " a " + cita.getFechaTermino();
        aviso.addCommand(aceptar);
        aviso.addCommand(rechazar);
        aviso.setString(avisoS);
        display.setCurrent(aviso);
    }

    protected void siguiente() {
        if(iterador.hasMoreElements()){
            iterador.procesa();
        }
        else
        {
            this.applic.destroyApp(false);
            this.applic.notifyDestroyed();
        }
    }

    private void limpiaComandos() {
        aviso.removeCommand(continuar);
        aviso.removeCommand(cancel);
        aviso.removeCommand(rechazar);
        aviso.removeCommand(modificar);
    }
}
