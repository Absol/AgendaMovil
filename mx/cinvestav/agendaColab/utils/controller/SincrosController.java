package mx.cinvestav.agendaColab.utils.controller;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import mx.cinvestav.agendaColab.DAO.EventoColaDAO;
import mx.cinvestav.agendaColab.DAO.SincronizacionDAO;
import mx.cinvestav.agendaColab.comun.ActualizacionUsuariosSincronizados;
import mx.cinvestav.agendaColab.comun.Cambio;
import mx.cinvestav.agendaColab.comun.Sincronizacion;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.agendaColab.forms.F_ListaSincros;

/**
 * @author absol
 */
public class SincrosController implements CommandListener, Controller{
private EventoColaDAO colaDao = new EventoColaDAO();
private SincronizacionDAO sincDao = new SincronizacionDAO();
private F_ListaSincros listaSincros = new F_ListaSincros("Sincronizaciones");
private Command saveNuevo = new Command("Guardar", Command.OK,1);
private Command saveCambio = new Command("Guardar", Command.OK,1);
private Command cancelar = new Command("Cancelar", Command.EXIT, 1);
private Command regresa = new Command("Menú principal", Command.EXIT, 1);
private Command borrar = new Command("Borrar", Command.SCREEN,2);
private Command nuevo = new Command("Nuevo", Command.SCREEN,2);
private FlujoController controller;
public Display display;
private Sincronizacion sincro;
BuscaUsuarioControlelr busca = null;

    public SincrosController(FlujoController controller){
        this.controller = controller;
        this.display = controller.display;
    }

    public void inciaSincros(){
        listaSincros.addCommand(regresa);
        listaSincros.addCommand(borrar);
        listaSincros.addCommand(nuevo);
        listaSincros.setCommandListener(this);
        listaSincros.setElementos(sincDao.getLista());
        display.setCurrent(listaSincros);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == regresa) {
            controller.menuPrincipal();
        } else if(c == borrar) {
            BeanUsuario usu = listaSincros.getSincSelected();
            //TO-DO hacer el dado de Sincro
//            sincDao.borrar(new Integer(usu.getId()));
            ActualizacionUsuariosSincronizados act
                    = new ActualizacionUsuariosSincronizados(controller.getMyUsuario()
                    , ActualizacionUsuariosSincronizados.BORRA_SINCRO);
            colaDao.guardarEvento(act);
            listaSincros.setElementos(sincDao.getLista());
        } else if(c == nuevo) {
            if(busca == null)
                busca = new BuscaUsuarioControlelr(controller.getServidor()
                        , this, listaSincros);
            busca.buscaUsuario();
        }
    }

    public void continuaCapturaCita(Displayable anterior) {
        listaSincros.setCommandListener(this);
        listaSincros.setElementos(sincDao.getLista());
        display.setCurrent(listaSincros);
    }

    public void addUsuario(BeanUsuario usu) {
        ActualizacionUsuariosSincronizados act
                = new ActualizacionUsuariosSincronizados(controller.getMyUsuario()
                , ActualizacionUsuariosSincronizados.BORRA_SINCRO);
        usu = new BeanUsuario(usu.getId(), usu.getLogin(), "-->");
        sincDao.create(usu);
        colaDao.guardarEvento(act);
        listaSincros.setCommandListener(this);
        listaSincros.setElementos(sincDao.getLista());
        display.setCurrent(listaSincros);
    }

    public Display getDisplay() {
        return display;
    }

}
