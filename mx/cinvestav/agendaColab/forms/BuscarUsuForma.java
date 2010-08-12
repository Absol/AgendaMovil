package mx.cinvestav.agendaColab.forms;
import javax.microedition.lcdui.*;
/**
 *
 * @author eduardogiron
 */
public class BuscarUsuForma extends Form {
    private CommandListener comm;
    private TextField bc;
    public BuscarUsuForma(){
            super("Buscar Usuario");
            bc = new TextField("Login: ", "", 15, TextField.ANY);
            this.append(bc);
    }

    public String getLogin(){
        return bc.getString();
    }

    public void clear(){
        bc.setString("");
    }

}
