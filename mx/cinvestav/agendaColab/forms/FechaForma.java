package mx.cinvestav.agendaColab.forms;

import java.util.TimeZone;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 * Forma para mostrar y capturar citas
 * @author absol
 */
public class FechaForma extends Form {
    private TextField asunto;
    private DateField fechaIni;
    private DateField fechaFin;
    private ChoiceGroup nivel;
    private int idNivel = -1;
    private int idCita;
    private int idServidor;

    public FechaForma(String title) {
        super(title);
        asunto = new TextField("Asunto", "", 30, TextField.ANY);
        fechaIni = new DateField("Hora de inicio:", DateField.DATE_TIME, TimeZone.getTimeZone("GMT"));
        fechaFin = new DateField("Hora de término", DateField.DATE_TIME, TimeZone.getTimeZone("GMT"));
        this.append(asunto);
        this.append(fechaIni);
        this.append(fechaFin);
        idNivel = ponNivel();
    }

    public BeanCita getDatos() {
        int nivelInt = BeanCita.PRIVADA;
        if(nivel.getSelectedIndex() == BeanCita.OCUPADO)
            nivelInt = BeanCita.OCUPADO;;
        if(nivel.getSelectedIndex() == BeanCita.PUBLICA)
            nivelInt = BeanCita.PUBLICA;
        BeanCita cita = new BeanCita(idCita, asunto.getString(), fechaIni.getDate()
                , fechaFin.getDate(), nivelInt, idServidor);
        return cita;
    }

    public void setDatos(BeanCita cita){
        asunto.setString(cita.getAsunto());
        fechaIni.setDate(cita.getFechaInicio());
        fechaFin.setDate(cita.getFechaTermino());
        int elementNum = BeanCita.PRIVADA;
        if(cita.getNivel() == BeanCita.OCUPADO)
            elementNum = 1;
        if(cita.getNivel() == BeanCita.PUBLICA)
            elementNum = 2;
        nivel.setSelectedIndex(elementNum, true);
        idCita = cita.getIdCita();
        idServidor = cita.getIdServidor();
    }

    public void setSoloFecha(){
        asunto.setConstraints(TextField.UNEDITABLE);
        this.delete(idNivel);
    }

    public void setCaptura(){
        asunto.setConstraints(TextField.ANY);
        if(idNivel == -1)
            idNivel = ponNivel();
    }

    private int ponNivel(){
        nivel = new ChoiceGroup("Nivel", Choice.EXCLUSIVE);
        nivel.append("Privada", null);
        nivel.append("Mostrar Ocupado", null);
        nivel.append("Pública", null);
        nivel.setSelectedIndex(0, true);
        return this.append(nivel);
    }

}
