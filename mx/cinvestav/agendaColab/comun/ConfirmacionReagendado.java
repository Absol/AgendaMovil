package mx.cinvestav.agendaColab.comun;

import java.util.Vector;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;

/**
 * @author absol
 */
public class ConfirmacionReagendado extends Confirmacion{
public static final int miTipo = 14;

    public ConfirmacionReagendado(Vector vec, BeanCita cita) {
        super(vec, cita);
    }

    ConfirmacionReagendado() {
        super();
    }

    public int getMiTipo() {
        return this.miTipo;
    }

    public String toString() {
        return "ConfirmacionReagendado{" + "cita=" + cita + "listaUsuarios=" + listaUsuarios + '}';
    }
}
