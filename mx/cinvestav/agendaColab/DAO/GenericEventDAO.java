package mx.cinvestav.agendaColab.DAO;

import java.util.Vector;
import mx.cinvestav.agendaColab.comun.Evento;

public interface GenericEventDAO {
     public void guardarEvento(Evento e);
     public Vector getListaEventos();
}
