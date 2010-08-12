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
public class BuscaUsuarioControlelr implements CommandListener{
private BuscarUsuForma formaBusc = new BuscarUsuForma();
private Command buscar = new Command("Buscar", Command.OK, 1);
private Command cancelar = new Command("Cancelar", Command.EXIT, 1);
private BeanUsuario usuario = null;
private FlujoController anterior;
private HttpPostAgenda server;

    public void buscaUsuario(HttpPostAgenda post, FlujoController controllerMain){
        anterior = controllerMain;
        server = post;
        formaBusc.addCommand(buscar);
        formaBusc.addCommand(cancelar);
        controllerMain.display.setCurrent(formaBusc);
    }

    public void commandAction(Command c, Displayable d) {
        if(c == buscar){
            BeanUsuario usu = server.buscaUsuario(new BeanUsuario(0, formaBusc.getLogin(), null));
            if(usu != null)
                anterior.addUsuario(usu);
            else
                anterior.display.setCurrent(formaBusc);
        } else if(c == cancelar)
            anterior.continuaCapturaCita();
    }

}
