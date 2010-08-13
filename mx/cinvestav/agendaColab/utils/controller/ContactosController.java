package mx.cinvestav.agendaColab.utils.controller;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import mx.cinvestav.agendaColab.DAO.ContactoDAO;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.forms.F_Contactos;
import mx.cinvestav.agendaColab.forms.ListaContactos;

/**
 * @author absol
 */
public class ContactosController implements CommandListener  {
private ContactoDAO contDao = new ContactoDAO();
private F_Contactos fContacto = new F_Contactos("Contactos");
private ListaContactos listaContactos = new ListaContactos();
private Command saveNuevo = new Command("Guardar", Command.OK,1);
private Command saveCambio = new Command("Guardar", Command.OK,1);
private Command cancelar = new Command("Cancelar", Command.EXIT, 1);
private Command regresa = new Command("Menú principal", Command.EXIT, 1);
private Command borrar = new Command("Borrar", Command.SCREEN,2);
private Command cambiar = new Command("Modificar", Command.SCREEN,2);
private Command nuevo = new Command("Nuevo", Command.SCREEN,2);
private FlujoController controller;
protected Display display;

    public ContactosController(FlujoController controller){
        this.controller = controller;
        this.display = controller.display;
    }

    public void inciaContactos(){
        listaContactos.addCommand(regresa);
        listaContactos.addCommand(borrar);
        listaContactos.addCommand(cambiar);
        listaContactos.addCommand(nuevo);
        listaContactos.setCommandListener(this);
        listaContactos.setElementos(contDao.getLista());
        display.setCurrent(listaContactos);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == regresa) {
            controller.menuPrincipal();
        } else if(c == cancelar) {
            listaContactos.setCommandListener(this);
            listaContactos.setElementos(contDao.getLista());
            display.setCurrent(listaContactos);
        } else if(c == saveNuevo) {
            BeanContacto cont = fContacto.getContacto();
            contDao.create(cont);
            listaContactos.setCommandListener(this);
            listaContactos.setElementos(contDao.getLista());
            display.setCurrent(listaContactos);
        } else if(c == saveCambio) {
            BeanContacto cont = fContacto.getContacto();
            contDao.modificar(cont);
            listaContactos.setCommandListener(this);
            listaContactos.setElementos(contDao.getLista());
            display.setCurrent(listaContactos);
        } else if(c == borrar) {
            BeanContacto cont = listaContactos.getContactoSelected();
            contDao.borrar(new Integer(cont.getidContacto()));
        } else if(c == cambiar) {
            fContacto.setTitle("Cambiar contacto");
            fContacto.addCommand(saveCambio);
            fContacto.removeCommand(saveCambio);
            fContacto.addCommand(cancelar);
            fContacto.setContacto(listaContactos.getContactoSelected());
            fContacto.setCommandListener(this);
            display.setCurrent(fContacto);
        } else if(c == nuevo) {
            fContacto.setTitle("Alta contacto");
            fContacto.addCommand(saveNuevo);
            fContacto.removeCommand(saveCambio);
            fContacto.addCommand(cancelar);
            fContacto.setContacto(new BeanContacto());
            fContacto.setCommandListener(this);
            display.setCurrent(fContacto);
        }
    }


}
