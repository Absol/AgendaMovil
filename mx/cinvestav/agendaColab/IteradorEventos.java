/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.agendaColab;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import mx.cinvestav.agendaColab.DAO.ContactoDAO;
import mx.cinvestav.agendaColab.DAO.SincronizacionDAO;
//import mx.cinvestav.agendaColab.pruebas.ContactoDAO;
//import mx.cinvestav.agendaColab.pruebas.SincronizacionDAO;
import mx.cinvestav.agendaColab.comun.ActualizacionUsuariosSincronizados;
import mx.cinvestav.agendaColab.comun.Cambio;
import mx.cinvestav.agendaColab.comun.Cancelacion;
import mx.cinvestav.agendaColab.comun.CitaPublica;
import mx.cinvestav.agendaColab.comun.Confirmacion;
import mx.cinvestav.agendaColab.comun.ConfirmacionReagendado;
import mx.cinvestav.agendaColab.comun.Evento;
import mx.cinvestav.agendaColab.comun.Notificacion;
import mx.cinvestav.agendaColab.comun.PullRequest;
import mx.cinvestav.agendaColab.comun.Respuesta;
import mx.cinvestav.agendaColab.comun.Sincronizacion;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;

/**
 *
 * @author absol
 */
class IteradorEventos implements Enumeration {
private PullController controller;
    private int i;
    private Vector eventos;

    public IteradorEventos(Vector vec, PullController contr) {
        eventos = vec;
        i = 0;
        controller = contr;
    }

    public boolean hasMoreElements() {
        return (i < eventos.size());
    }

    public Object nextElement() {
        if (!hasMoreElements()) {
            throw new NoSuchElementException("Nada mas que procesar");
        }
        Object obj = eventos.elementAt(i);
        i++;
        return obj;
    }

    void procesa() {
        Evento eve = (Evento) nextElement();

        int tipo = eve.getMiTipo();
        switch (tipo) {
            case ActualizacionUsuariosSincronizados.miTipo: {
                ActualizacionUsuariosSincronizados act
                        = (ActualizacionUsuariosSincronizados) eve;
                procesaActSinc(act);
                controller.siguiente();
                break;
            }
            case Sincronizacion.miTipo: {
                Sincronizacion sincronizacion = (Sincronizacion) eve;
                procesaSincro(sincronizacion);
                controller.siguiente();
                break;
            }
            case Cancelacion.miTipo: {
                Cancelacion cancel = (Cancelacion) eve;
                controller.avisaCancelacion(cancel.getCita());
                break;
            }
            case Respuesta.miTipo: {
                Respuesta resp = (Respuesta) eve;
                controller.avisarRespuesta(resp.getCita()
                        , resp.getUsuario(), resp.isRespuesta());
                break;
            }
            case Notificacion.miTipo: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
            case Confirmacion.miTipo: {
                Confirmacion conf = (Confirmacion) eve;
                BeanUsuario usu
                        = (BeanUsuario) conf.getListaUsuarios().elementAt(0);
                controller.pedirConfirmacion(conf.getCita(), usu);
                break;
            }
            case ConfirmacionReagendado.miTipo: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
            case CitaPublica.miTipo: {
                System.out.println("No se debe recibir CitaPublica");
                controller.siguiente();
                break;
            }
            case PullRequest.miTipo: {
                System.out.println("No se debe recibir PullRequest");
                controller.siguiente();
                break;
            }
        }
    }

    private void procesaSincro(Sincronizacion sincronizacion) {
        ContactoDAO dao = new ContactoDAO();
        for(int j = 0; j < sincronizacion.getListaCambios().size(); j++){
            Cambio camb = (Cambio) sincronizacion.getListaCambios().elementAt(j);

            switch(camb.getTipoCambio()){
                case Cambio.ALTA: {
                    dao.create(camb.getContacto());
                    break;
                }
                case Cambio.BAJA: {
                    dao.borrar(new Integer(camb.getContacto().getidContacto()));
                    break;
                }
                case Cambio.MODIFICACION: {
                    dao.modificar(camb.getContacto());
                    break;
                }
            }
        }
    }

    private void procesaActSinc(ActualizacionUsuariosSincronizados act) {
        SincronizacionDAO dao = new SincronizacionDAO();
        if(act.getTipoAct() == ActualizacionUsuariosSincronizados.NUEVA_SINCRO){
        BeanUsuario usu = act.getUsuario();
        usu = new BeanUsuario(usu.getId(), usu.getLogin(), "<--");
        dao.create(usu);
        }
    }
}
