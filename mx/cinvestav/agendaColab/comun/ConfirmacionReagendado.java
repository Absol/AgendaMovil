package mx.cinvestav.agendaColab.comun;

import java.util.Vector;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;

/**
 * @author absol
 */
public class ConfirmacionReagendado extends Confirmacion{
public static final int miTipo = 14;

    public ConfirmacionReagendado(BeanCita cita) {
        super(new Vector(), cita);
    }

    public int getMiTipo() {
        return this.miTipo;
    }

    public String toString() {
        return "ConfirmacionReagendado{" + "cita=" + cita + "listaUsuarios=" + listaUsuarios + '}';
    }
}
