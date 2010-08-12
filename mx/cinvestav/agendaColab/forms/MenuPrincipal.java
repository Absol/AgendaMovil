package mx.cinvestav.agendaColab.forms;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.List;

public class MenuPrincipal extends List{
        public MenuPrincipal(){
            super("Menu Principal",Choice.IMPLICIT);
            this.append("Citas Propias",null);
//            this.append("Buscar Contacto",null);
//            this.append("Sincronizar",null);
//            this.append("Ver Citas Grupo",null);
//            this.append("Agenda Personal",null);
        }
}
