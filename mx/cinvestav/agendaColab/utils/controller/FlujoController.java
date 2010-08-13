package mx.cinvestav.agendaColab.utils.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import mx.cinvestav.agendaColab.*;
import javax.microedition.lcdui.*;
import mx.cinvestav.agendaColab.DAO.CItaDAO;
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
import mx.cinvestav.agendaColab.comun.ConfirmacionReagendado;
import mx.cinvestav.agendaColab.comun.Sincronizacion;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;

/**
 *
 * @author eduardogiron
 */
public class FlujoController implements CommandListener {
private GenericEventDAO cola = new EventoColaDAO();
private CItaDAO cItaDAO=new CItaDAO();
protected Display display;
private int formAct;
Agenda_Colaborativa applic;
MenuPrincipal menu;
F_Cita1 listaCitas = null;
F_User f_user;
private Vector usuarios = new Vector();
private static HttpPostAgenda servidor = null;
//Comandos Menu Principal
private Command exit = new Command("Salir", Command.EXIT, 1);
private Command aceptar = new Command("Aceptar", Command.OK, 1);
//Menu citas propias
private Command regresarALista = new Command("Regresar", Command.EXIT, 1);
private Command verCita = new Command("Ver", Command.SCREEN, 2);
private Command nuevaCita = new Command("Nueva cita", Command.SCREEN, 2);
private Command regresarPrincipal = new Command("Menú principal", Command.EXIT, 1);
//Comandos Login
private Command save = new Command("Guardar", Command.OK,1);
//Captura cita
private FechaForma fCita = null;
private Command guardaCita = new Command("Guardar", Command.SCREEN, 2);
private Command addUsuario = new Command("Agregar usuarios", Command.SCREEN, 2);
private Command cancelar = new Command("Cancelar", Command.EXIT, 1);
//Citas agenas
private CitaFormaReadOnly fMuestraCita = new CitaFormaReadOnly("Detalles Cita");
private ContactosController contactosController = null;

    HttpPostAgenda getServidor() {
        if (servidor == null) {
            servidor = new HttpPostAgenda("belldandy.no-ip.info/AgendaServer/");
        }
        return servidor;
    }

    public BeanUsuario getMyUsuario() {
            BeanUsuario myUsuer = UsuarioDAO.getMyUser();
            if (myUsuer == null) {
                //este metodo captura con GUi
                capturaMyUser();
                //manda al server y obtiene ya con id
                myUsuer = getServidor().registraUsuario(myUsuer);
                //guarda el dao local
                UsuarioDAO.guardaMyUsuario(myUsuer);

            }
        return myUsuer;
    }

    public FlujoController(Agenda_Colaborativa app) {
        applic = app;
        display = Display.getDisplay(app);
        menu = new MenuPrincipal();
        this.getServidor();
        menu.addCommand(exit);
        menu.addCommand(aceptar);
//        if(myUsuer!=null)
//            display.setCurrent(menu);
//        else
//            display.setCurrent(f_user);
        menuPrincipal();
    }

    protected void menuPrincipal(){
        menu.setCommandListener(this);
        display.setCurrent(menu);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exit) {
            this.applic.destroyApp(false);
            this.applic.notifyDestroyed();
        } else if(c == aceptar) {
            switch(menu.getSelectedIndex()){
                case 0:{
                    muestraListaCitas();
                    break;
                }
                case 1:{
                    citasAgenas();
                    break;
                }
                case 2:{
                    if(contactosController == null)
                        contactosController= new ContactosController(this);
                    contactosController.inciaContactos();
                    break;
                }
            }
        } else if(c == regresarPrincipal) {
            display.setCurrent(menu);
            menu.setCommandListener(this);
        } else if(c == cancelar) {
            display.setCurrent(menu);
            menu.setCommandListener(this);
        } else if(c == addUsuario) {
            formAct = 0;
            BuscaUsuarioControlelr busca = new BuscaUsuarioControlelr(servidor
                    , this, fCita);
            busca.buscaUsuario();
        } else if(c == nuevaCita) {
            muestraCapturaCita();
        } else if(c == guardaCita) {
            //Poner dao guardar cita desde fCita
            BeanCita fcita=fCita.getDatos();
            cItaDAO=new CItaDAO();
            Integer id = (Integer) cItaDAO.create(fcita);
            fcita = new BeanCita(id.intValue(), fcita.getAsunto()
                    , fcita.getFechaInicio(), fcita.getFechaTermino()
                    , fcita.getNivel(), 0);
            //Poner dao cola si el nivel es diferente de PRIVADO
            if(fcita.getNivel()!=BeanCita.PRIVADA)
                cola.guardarEvento(new CitaPublica(fcita));
            if(usuarios.size() > 0)
                cola.guardarEvento(new Confirmacion(usuarios, fcita));
            display.setCurrent(menu);
            menu.setCommandListener(this);
        } else if(c == regresarALista) {
            listaCitas.setCommandListener(this);
            display.setCurrent(listaCitas);
        } else if(c == verCita) {
            BeanCita cita = listaCitas.getCitaSelected();
            fMuestraCita.setCommandListener(this);
            fMuestraCita.setDatos(cita);
            fMuestraCita.addCommand(regresarALista);
            display.setCurrent(fMuestraCita);
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

    private void muestraListaCitas() {
        if(listaCitas == null){
            listaCitas = new F_Cita1("Citas Propias");
        } else {
            listaCitas.setTitle("Citas Propias");
        }
        listaCitas.addCommand(nuevaCita);
        listaCitas.addCommand(verCita);
        listaCitas.addCommand(regresarPrincipal);
        listaCitas.setCommandListener(this);
        //Cargar citas almacenadas
        Vector vec = cItaDAO.getLista();
        listaCitas.setElementos(vec);
        display.setCurrent(listaCitas);
    }

    private void muestraCapturaCita(){
        if(fCita == null)
            fCita = new FechaForma("Captura cita");
        else
            fCita.setTitle("Captura cita");
        usuarios = new Vector();
        fCita.setCaptura();
        fCita.addCommand(addUsuario);
        fCita.addCommand(guardaCita);
        fCita.addCommand(cancelar);
        fCita.setCommandListener(this);
        display.setCurrent(fCita);
    }

    void addUsuario(BeanUsuario usu) {
        if(formAct == 0)
        {
            usuarios.addElement(usu);
            Alert alert = new Alert("Usuario agregado", "Se agrego el usuario "
                + usu.getLogin(), null, AlertType.INFO);
            alert.setTimeout(2000);
            display.setCurrent(alert, fCita);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.AUGUST);
            cal.set(Calendar.YEAR, 2000);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date fecIni = cal.getTime();
            cal.set(Calendar.MONTH, Calendar.AUGUST);
            cal.set(Calendar.YEAR, 2012);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            Date fecFin = cal.getTime();
            Vector citasAgn = servidor.getCitasAgenas(usu.getId(), fecIni, fecFin);
            muestraCitasAgenas(citasAgn);
        }
    }

    void continuaCapturaCita(Displayable disp) {
        disp.setCommandListener(this);
        display.setCurrent(disp);
    }

    void citasAgenas(){
        formAct = 1;
        BuscaUsuarioControlelr busca = new BuscaUsuarioControlelr(servidor, this, menu);
        busca.buscaUsuario();
    }

    private void muestraCitasAgenas(Vector citasAgn) {
        if(listaCitas == null){
            listaCitas = new F_Cita1("Citas Ajenas");
        } else {
            listaCitas.setTitle("Citas Ajenas");
        }
        listaCitas.removeCommand(nuevaCita);
        listaCitas.addCommand(verCita);
        listaCitas.addCommand(regresarPrincipal);
        listaCitas.setCommandListener(this);
        //Cargar citas almacenadas
        listaCitas.setElementos(citasAgn);
        display.setCurrent(listaCitas);
    }
}
