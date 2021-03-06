package mx.cinvestav.agendaColab;

import javax.microedition.lcdui.*;
import mx.cinvestav.agendaColab.comun.Cambio;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.agendaColab.forms.*;
import mx.cinvestav.movil.http.HttpPostAgenda;
import mx.cinvestav.agendaColab.DAO.ContactosDao;
import mx.cinvestav.agendaColab.DAO.EventoColaDAO;
import mx.cinvestav.agendaColab.DAO.GenericEventDAO;
import mx.cinvestav.agendaColab.DAO.UsuarioDAO;
import mx.cinvestav.agendaColab.comun.ActualizacionUsuariosSincronizados;
import mx.cinvestav.agendaColab.comun.CitaPublica;
import mx.cinvestav.agendaColab.comun.Confirmacion;
import mx.cinvestav.agendaColab.comun.Sincronizacion;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;

/**
 *
 * @author eduardogiron
 */
public class FlujoController implements CommandListener {
private GenericEventDAO cola = new EventoColaDAO();
    public Display display;
    Agenda_Colaborativa applic;
    F_Menu menu;
    F_UsersData F_Alta, F_Info;
    F_Contacts F_Lista, F_Lista_Conect,F_Lista_Add;
    F_Buscar f_buscar,F_B_Contacts,f_bus_sinc;
    F_Agenda f_agenda;
    F_Citas f_cita,f_cp;
    F_User f_user;
    F_Agendar_Cita f_agendar;
    private static HttpPostAgenda servidor = null;
    private static BeanUsuario myUsuer = null;

    HttpPostAgenda getServidor() {
        if (servidor == null) {
            servidor = new HttpPostAgenda("belldandy.no-ip.info/AgendaServer/");
        }
        return servidor;
    }

    public BeanUsuario getMyUsuario() {
        if (myUsuer == null) {
            //lo cargo local
            myUsuer = UsuarioDAO.getMyUser();
            if (myUsuer == null) {
                //este metodo captura con GUi
                capturaMyUser();
                //manda al server y obtiene ya con id
                myUsuer = getServidor().registraUsuario(myUsuer);
                //guarda el dao local
                UsuarioDAO.guardaMyUsuario(myUsuer);

            }
        }
        return myUsuer;
    }

    //Comandos Logging
    private Command save = new Command("Guardar", Command.OK,1);
    //Comandos Menu Principal
    private Command exit = new Command("Salir", Command.EXIT, 1);
    private Command aceptar = new Command("Aceptar", Command.OK, 1);
    //Comandos Comunes
    private Command back = new Command("Back", Command.BACK, 1);
    //Comandos Alta Contacto
    private Command guardar = new Command("Save", Command.OK, 1);
    //Comandos Buscar Contacto
    private Command ok = new Command("Abrir Seleccionado", Command.OK, 1);
    private Command buscar = new Command("Buscar", Command.OK, 1);
        //Busqueda personalizada
        private Command busq = new Command("Aceptar",Command.OK,1);
        private Command actualizar = new Command("Actualizar", Command.OK,1);
    //Comandos Sincronizar Contactos
    private Command new_sinc = new Command("Nueva Sincronización", Command.OK, 1);
    private Command des_sinc = new Command("Desincronizar", Command.OK, 1);
        //Comandos Buscar usuario a sincronizar
        private Command bus_usr = new Command("Buscar Contacto", Command.OK, 1);
    //Comandos Ver citas Contactos
    private Command ok_c = new Command("Aceptar", Command.OK, 1);
    //Comandos Agenda 
    private Command ok_a = new Command("Aceptar", Command.OK, 1);
    //Comandos Nueva Cita
     private Command add_cita = new Command("Guardar", Command.OK, 1);
      private Command add_usr = new Command("Añadir Usuario", Command.OK, 1);
       //private Command ok_a = new Command("Aceptar", Command.OK, 1);
    private Command add_contac= new Command("Aceptar", Command.OK,1);
    //Comandos
    public FlujoController(Agenda_Colaborativa app) {
        applic = app;
        display = Display.getDisplay(app);
        menu = new F_Menu(this);
        this.getServidor();
        this.getMyUsuario();
        menu.addCommand(exit);
        menu.addCommand(aceptar);
        if(myUsuer!=null)
            display.setCurrent(menu);
        else
            display.setCurrent(f_user);

    }

    private void cambioFroma(Command c) {
        //Funciones del menu principal
        if (c == aceptar) {
            //Alta contacto
            if (menu.isSelected(0)) {
                F_Alta = new F_UsersData(this, "Alta Contacto",myUsuer);
                F_Alta.addCommand(back);
                F_Alta.addCommand(guardar);
                display.setCurrent(F_Alta);
            }
            //Buscar Contacto
            if (menu.isSelected(1)) {
                F_Lista = new F_Contacts(this, "Contacts List");
                F_Lista.load_contacts();
                F_Lista.addCommand(back);
                F_Lista.addCommand(ok);
                F_Lista.addCommand(buscar);
                display.setCurrent(F_Lista);
            }
            //Sincronizar
            if (menu.isSelected(2)) {
                F_Lista_Conect = new F_Contacts(this, "Connection Contacts");
                F_Lista_Conect.load_conections();
                F_Lista_Conect.addCommand(back);
                F_Lista_Conect.addCommand(new_sinc);
                F_Lista_Conect.addCommand(des_sinc);
                display.setCurrent(F_Lista_Conect);
            }
            //Ver Citas Grupo
            if (menu.isSelected(3)) {
                F_B_Contacts = new F_Buscar(this, "Buscar Contacto");
                F_B_Contacts.addCommand(back);
                F_B_Contacts.addCommand(ok_c);
                display.setCurrent(F_B_Contacts);

            }
            //Agenda personal
            if (menu.isSelected(4)) {
                f_agenda = new F_Agenda(this, "Personal Agenda");
                f_agenda.addCommand(back);
                f_agenda.addCommand(ok_a);
                display.setCurrent(f_agenda);
            }

        }
        // Común
        if (c == back) {
            display.setCurrent(menu);
        }
        //Alta contacto
        if (c == guardar) {
            //Agregar función de almacenamiento
            BeanContacto cont = F_Alta.getDatos();

            ContactosDao.guardaContacto(cont);
            Sincronizacion sincro = new Sincronizacion();
            sincro.add(new Cambio(cont, Cambio.ALTA));
            cola.guardarEvento(sincro);

            display.setCurrent(menu);
        }
        //Buscar Contacto
        if (c == ok) {
            String name = F_Lista.getString(F_Lista.getSelectedIndex());
            F_Info = new F_UsersData(this,"Contact "+name,myUsuer);
            F_Info.addCommand(actualizar);
            F_Info.addCommand(back);
            F_Info.setCommandListener(this);
            display.setCurrent(F_Info);
            //Agregar función de busqueda de contacto

        }
        if (c == buscar) {
            f_buscar= new F_Buscar(this,"Buscar Contacto");
            f_buscar.addCommand(busq);
            f_buscar.addCommand(back);
            f_buscar.setCommandListener(this);
            display.setCurrent(f_buscar);
        }
            if(c== busq){
                F_Info = new F_UsersData(this,"Contact Info",myUsuer);
                F_Info.addCommand(actualizar);
                F_Info.addCommand(back);
                F_Info.setCommandListener(this);
                display.setCurrent(F_Info);
                //F_Info.load(null);
            }
        //Sincronizar
        if (c == new_sinc) {
            f_bus_sinc= new F_Buscar(this,"Nueva Sincronización");
            f_bus_sinc.addCommand(back);
            f_bus_sinc.addCommand(bus_usr);
            f_bus_sinc.setCommandListener(this);
            display.setCurrent(f_bus_sinc);
            
        }
        //Buscar contacto para sincronizacion
        if(c== bus_usr){
            // metodo para busqueda de usuario
            ActualizacionUsuariosSincronizados act
                    = new ActualizacionUsuariosSincronizados(null
                    , ActualizacionUsuariosSincronizados.NUEVA_SINCRO);
            cola.guardarEvento(act);

        }
        if (c == des_sinc) {
            String name_usr= F_Lista_Conect.getString(F_Lista_Conect.getSelectedIndex());
            BeanUsuario b_u= new BeanUsuario(0,name_usr,"");
            ActualizacionUsuariosSincronizados act
                    = new ActualizacionUsuariosSincronizados(b_u
                    , ActualizacionUsuariosSincronizados.BORRA_SINCRO);
            cola.guardarEvento(act);
        }
        //Ver Citas Grupo
        if (c == ok_c) {
            f_cita = new F_Citas(this,"User");
            display.setCurrent(f_cita);
        }
        //Agenda Personal
        if (c == ok_a) {
            if (f_agenda.isSelected(0)) {
                f_agendar = new F_Agendar_Cita(this,"Nueva Cita");
                f_agendar.addCommand(back);
                f_agendar.addCommand(add_cita);
                f_agendar.addCommand(add_usr);
                f_agendar.setCommandListener(this);
                display.setCurrent(f_agendar);
            }
            if (f_agenda.isSelected(1)) {
                f_cp = new F_Citas(this, "Mis citas");
                f_cp.addCommand(back);
                f_cp.setCommandListener(this);
                display.setCurrent(f_cp);
            }

        }
        if(c== add_cita){
            BeanCita citaNueva = f_agendar.getDatos();
            Confirmacion citaCon = new Confirmacion(null, citaNueva);
            cola.guardarEvento(citaCon);
            if(citaNueva.getNivel() != BeanCita.PRIVADA){
                CitaPublica cita = new CitaPublica(citaNueva);
                cola.guardarEvento(cita);
            }
        }
        //Abre la ventana para seleccionar el usuario a añadir
        if(c== add_usr){
            F_Lista_Add = new F_Contacts(this,"Selecciona usuarios a añadir");
            F_Lista_Add.load_contacts();
            F_Lista_Add.addCommand(add_contac);
            F_Lista_Add.setCommandListener(this);
            display.setCurrent(F_Lista_Add);
            }
        if(c== add_contac){
            //Metodo para añadir contacto en el server
            String name;
            name = F_Lista_Add.getString(F_Lista_Add.getSelectedIndex());
            
            display.setCurrent(f_agendar);
        }
        if(c== save){
            myUsuer = f_user.get_data();
            //myUsuer = getServidor().registraUsuario(myUsuer);
                //guarda el dao local
            //DaoUsuario.guardaMyUsuario(myUsuer);
            display.setCurrent(menu);
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exit) {
            this.applic.destroyApp(false);
            this.applic.notifyDestroyed();
        } else {
            cambioFroma(c);
        }
    }

    public void capturaMyUser(){
        f_user= new F_User(this);
        f_user.addCommand(save);
        f_user.setCommandListener(this);
        display.setCurrent(f_user);
       // BeanUsuario usr= new BeanUsuario(4,"","");
        //return f_user.get_data();

    }
}
