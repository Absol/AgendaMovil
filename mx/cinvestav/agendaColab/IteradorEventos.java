/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.agendaColab;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import mx.cinvestav.agendaColab.DAO.ContactoDAO;
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
                        = new ActualizacionUsuariosSincronizados();
                throw new UnsupportedOperationException("Not yet implemented");
            }
            case Sincronizacion.miTipo: {
                Sincronizacion sincronizacion = new Sincronizacion();
                procesaSincro(sincronizacion);
                break;
            }
            case Cancelacion.miTipo: {
                Cancelacion cancel = new Cancelacion();
                controller.avisaCancelacion(cancel.getCita());
                break;
            }
            case Respuesta.miTipo: {
                Respuesta resp = new Respuesta();
                controller.avisarRespuesta(resp.getCita(), resp.isRespuesta());
                break;
            }
            case Notificacion.miTipo: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
            case Confirmacion.miTipo: {
                Confirmacion conf = new Confirmacion();
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
                break;
            }
            case PullRequest.miTipo: {
                System.out.println("No se debe recibir PullRequest");
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
                }
                case Cambio.BAJA: {
                    dao.borrar(new Integer(camb.getContacto().getidContacto()));
                }
                case Cambio.MODIFICACION: {
                    dao.modificar(camb.getContacto());
                }
            }
        }
    }
}
