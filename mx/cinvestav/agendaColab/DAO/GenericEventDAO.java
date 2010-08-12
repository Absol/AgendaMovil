package mx.cinvestav.agendaColab.DAO;

import java.util.Vector;
import mx.cinvestav.agendaColab.comun.Evento;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;

public interface GenericEventDAO {
     public void guardarEvento(Evento e);
     public Vector getListaEventos(BeanUsuario usr);
}
