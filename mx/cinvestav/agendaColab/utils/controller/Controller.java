package mx.cinvestav.agendaColab.utils.controller;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;

/**
 *
 * @author absol
 */
public interface Controller {
    public Display getDisplay();

    public void continuaCapturaCita(Displayable anterior);

    public void addUsuario(BeanUsuario usu);

}
