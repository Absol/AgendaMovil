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
			}
		}

		return vector;
	}

}
