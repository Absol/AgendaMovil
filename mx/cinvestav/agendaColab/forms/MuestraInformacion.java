package mx.cinvestav.agendaColab.forms;

import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 * Pantalla para mostrar los avisos
 * @author absol
 */
public class MuestraInformacion extends TextBox {

    public MuestraInformacion(String titulo){
        super(titulo, null, 127, TextField.UNEDITABLE);
    }
}
