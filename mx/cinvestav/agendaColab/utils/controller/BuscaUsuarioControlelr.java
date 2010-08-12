package mx.cinvestav.agendaColab.utils.controller;

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
private FlujoController anterior;
private HttpPostAgenda server;

    public BuscaUsuarioControlelr(HttpPostAgenda post, FlujoController controllerMain) {
        anterior = controllerMain;
        server = post;
        formaBusc.addCommand(buscar);
        formaBusc.addCommand(cancelar);
        formaBusc.setCommandListener(this);
    }

    public void buscaUsuario() {
        anterior.display.setCurrent(formaBusc);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == buscar) {
            formaBusc.removeCommand(buscar);
            formaBusc.removeCommand(cancelar);
            (new Thread(new hilo())).start();
        } else if (c == cancelar) {
            anterior.continuaCapturaCita();
        }
    }

    class hilo implements Runnable {

        public hilo() {
        }

        public void run() {
            BeanUsuario usu = server.buscaUsuario(new BeanUsuario(0, formaBusc.getLogin(), null));
            if (usu != null) {
                anterior.addUsuario(usu);
            } else {
        formaBusc.addCommand(buscar);
        formaBusc.addCommand(cancelar);
                anterior.display.setCurrent(formaBusc);
            }
        }
    }
}
