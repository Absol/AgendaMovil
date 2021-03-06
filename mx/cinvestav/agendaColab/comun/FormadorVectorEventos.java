package mx.cinvestav.agendaColab.comun;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import mx.cinvestav.agendaColab.comun.ActualizacionUsuariosSincronizados;

public class FormadorVectorEventos {

    public Vector formar(DataInputStream input) throws IOException {
        int tamano = input.readInt();
        int tipo = 0;

        Vector vector = new Vector(tamano);

        for (int i = 0; i < tamano; i++) {
            tipo = input.readInt();
            switch (tipo) {
                case ActualizacionUsuariosSincronizados.miTipo: {
                    ActualizacionUsuariosSincronizados act = new ActualizacionUsuariosSincronizados();
                    act.read(input);
                    vector.addElement(act);
                    break;
                }
                case PullRequest.miTipo: {
                    PullRequest pull = new PullRequest();
                    pull.read(input);
                    vector.addElement(pull);
                    break;
                }
                case Sincronizacion.miTipo: {
                    Sincronizacion sincronizacion = new Sincronizacion();
                    sincronizacion.read(input);
                    vector.addElement(sincronizacion);
                    break;
                }
                case CitaPublica.miTipo: {
                    CitaPublica cita = new CitaPublica();
                    cita.read(input);
                    vector.addElement(cita);
                    break;
                }
                case Cancelacion.miTipo: {
                    Cancelacion cancel = new Cancelacion();
                    cancel.read(input);
                    vector.addElement(cancel);
                    break;
                }
                case Respuesta.miTipo: {
                    Respuesta resp = new Respuesta();
                    resp.read(input);
                    vector.addElement(resp);
                    break;
                }
                case Notificacion.miTipo: {
                    throw new UnsupportedOperationException("Not yet implemented");
                }
                case Confirmacion.miTipo: {
                    Confirmacion conf = new Confirmacion();
                    conf.read(input);
                    vector.addElement(conf);
                    break;
                }
                case ConfirmacionReagendado.miTipo: {
                    ConfirmacionReagendado reag = new ConfirmacionReagendado();
                    reag.read(input);
                    vector.addElement(reag);
                    break;
                }
            }
        }

        return vector;
    }
}
