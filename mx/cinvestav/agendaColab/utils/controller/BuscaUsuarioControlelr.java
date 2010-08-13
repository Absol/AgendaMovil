package mx.cinvestav.agendaColab.utils.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.movil.http.HttpPostAgenda;
import javax.microedition.lcdui.CommandListener;
import mx.cinvestav.agendaColab.forms.BuscarUsuForma;

/**
 *
 * @author absol
 */
public class BuscaUsuarioControlelr implements CommandListener {
private BuscarUsuForma formaBusc = new BuscarUsuForma();
private Command buscar = new Command("Buscar", Command.OK, 1);
private Command cancelar = new Command("Cancelar", Command.EXIT, 1);
private BeanUsuario usuario = null;
private Controller controller;
private HttpPostAgenda server;
private Displayable anterior;

    public BuscaUsuarioControlelr(HttpPostAgenda post, Controller controllerMain, Displayable ant) {
        controller = controllerMain;
        server = post;
        anterior = ant;
        formaBusc.setCommandListener(this);
    }

    public void buscaUsuario() {
        formaBusc.addCommand(buscar);
        formaBusc.addCommand(cancelar);
        controller.getDisplay().setCurrent(formaBusc);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == buscar) {
            formaBusc.removeCommand(buscar);
            formaBusc.removeCommand(cancelar);
            (new Thread(new hilo())).start();
        } else if (c == cancelar) {
            controller.continuaCapturaCita(anterior);
        }
    }

    class hilo implements Runnable {

        public hilo() {
        }

        public void run() {
            BeanUsuario usu = server.buscaUsuario(new BeanUsuario(0, formaBusc.getLogin(), null));
            if (usu != null) {
                controller.addUsuario(usu);
            } else {
                formaBusc.addCommand(buscar);
                formaBusc.addCommand(cancelar);
                Alert alert = new Alert("Usuario no encontrado"
                        , "No se encontró el usuario "
                        + formaBusc.getLogin(), null, AlertType.INFO);
                alert.setTimeout(1500);
                controller.getDisplay().setCurrent(alert, formaBusc);
            }
        }
    }
}
