package mx.cinvestav.agendaColab.forms;

import java.util.TimeZone;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

/**
 * Forma para mostrar citas
 * @author absol
 */
public class CitaFormaReadOnly extends Form {
    private TextField asunto;
    private StringItem fechaIni;
    private StringItem fechaFin;
    private StringItem nivel;

    public CitaFormaReadOnly(String title) {
        super(title);
        asunto = new TextField("Asunto", null, 31, TextField.UNEDITABLE);
        fechaIni = new StringItem("Inicio", null);
        fechaFin = new StringItem("Fin", null);
        nivel = new StringItem("Nivel", null);
        nivel.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
        append(asunto);
        append(fechaIni);
        append(fechaFin);
        append(nivel);
    }

    public void setDatos(BeanCita cita){
        asunto.setString(cita.getAsunto());
        fechaIni.setText(cita.getFechaInicio().toString());
        fechaFin.setText(cita.getFechaTermino().toString());
        switch(cita.getNivel()){
            case BeanCita.PRIVADA:{
                nivel.setText("Privada");
                break;
            }
            case BeanCita.OCUPADO:{
                nivel.setText("Mostrar ocupado");
                break;
            }
            case BeanCita.PUBLICA:{
                nivel.setText("Pública");
                break;
            }
        }
    }
}
