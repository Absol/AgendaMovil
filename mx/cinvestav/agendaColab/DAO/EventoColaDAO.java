package mx.cinvestav.agendaColab.DAO;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import mx.cinvestav.agendaColab.comun.ActualizacionUsuariosSincronizados;
import mx.cinvestav.agendaColab.comun.Cambio;
import mx.cinvestav.agendaColab.comun.Cancelacion;
import mx.cinvestav.agendaColab.comun.CitaPublica;
import mx.cinvestav.agendaColab.comun.Confirmacion;
import mx.cinvestav.agendaColab.comun.ConfirmacionReagendado;
import mx.cinvestav.agendaColab.comun.Evento;
import mx.cinvestav.agendaColab.comun.PullRequest;
import mx.cinvestav.agendaColab.comun.Respuesta;
import mx.cinvestav.agendaColab.comun.Sincronizacion;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.agendaColab.dataStorage.SimpleDataSource;
import mx.cinvestav.agendaColab.utils.Utils;


public class EventoColaDAO  extends AbstractEventoColaDAO{
	private static final String dataStorage="evento";

	public EventoColaDAO() {
		// TODO Auto-generated constructor stub
		datasource=SimpleDataSource.getInstance(dataStorage);
	}

	public void guardarEvento(Evento e) {
	      RecordStore rs=datasource.getRecordStore();
              ByteArrayOutputStream baos=new ByteArrayOutputStream();
	      DataOutputStream dos = new DataOutputStream(baos);
	      
	     

	     try {
	    	if(e instanceof ActualizacionUsuariosSincronizados){
                  ActualizacionUsuariosSincronizados aus=(ActualizacionUsuariosSincronizados)e;
                  dos.writeUTF(aus.toString());
	    	  //System.out.println("Soy una ActualizacionUsuariosSincronizados "+aus);
	      }
              if(e instanceof Cancelacion){
                  Cancelacion c=(Cancelacion)e;
                  dos.writeUTF(c.toString());
              }

	      if (e instanceof CitaPublica){
                  CitaPublica cp=(CitaPublica)e;
                  dos.writeUTF(cp.toString());
	    	  //System.out.println("Soy una cita Publica" +cp);
	      }
	      if (e instanceof Confirmacion){
                  Confirmacion c=(Confirmacion)e;
                  dos.writeUTF(c.toString());
	    	  //System.out.println("Soy una Confirmacion "+c);
	      }
              if (e instanceof ConfirmacionReagendado){
                  ConfirmacionReagendado cr=(ConfirmacionReagendado)e;
                  dos.writeUTF(cr.toString());
              }
	      if (e instanceof Respuesta){
                  Respuesta r=(Respuesta)e;
                  dos.writeUTF(r.toString());
	    	  //System.out.println("Soy una Respuesta "+r);
	      }
             if (e instanceof Sincronizacion){
                 Sincronizacion s=(Sincronizacion)e;
                 dos.writeUTF(s.toString());
        	 //System.out.println("Soy una Sincronizacion "+s);
	      }
	     dos.flush();
             byte[] datos = baos.toByteArray();

             int id = rs.addRecord(datos, 0, datos.length);

             dos.close();

          baos.close();
		} catch (IOException ioe) {

			ioe.printStackTrace();
		}catch(RecordStoreException rse){
			rse.printStackTrace();
		}finally{
			//datasource.closeRecordStore();
		}
	   	
		
	}


        public Vector getListaEventos(BeanUsuario user){
    	        RecordStore rs=datasource.getRecordStore();
		         String arr[],users[],citas[];
		         BeanContacto usr;
                BeanUsuario usuario;
                BeanCita cita;
                String usuarioString,citaString;
                //Se añade al principio del Vector un usuario
                vec.addElement(new PullRequest(user));

		 try{
                    RecordEnumeration re = rs.enumerateRecords(null, null, false);

                     while(re.hasNextElement()){
	            //for(int i =1; i <= rs.getNumRecords();i++){
                          //String aux=new String(re.nextRecord());
                           String aux = getRecord(re.nextRecordId(),dataStorage);
                           System.out.println("aux: "+aux);
	                //String aux = getRecord(i,dataStorage);
                        
	                arr=Utils.split(aux,"*");
                        
                        int tipo=new Integer(Integer.parseInt(arr[0])).intValue();
                        switch(tipo){
                            case ActualizacionUsuariosSincronizados.miTipo:
                                //Formo Usuario
                                usuarioString=arr[1];
                                usuario=Utils.stringToUsuario(usuarioString);
                                //Formo tipo Actualizacion
                                int tipoAct=new Integer(Integer.parseInt(arr[2])).intValue();
                                ActualizacionUsuariosSincronizados aus=new ActualizacionUsuariosSincronizados(usuario, tipo);
                                vec.addElement(aus);
                                break;
                            case Cancelacion.miTipo:
                                citaString=arr[1];
                                cita=Utils.stringToCita(citaString);
                                Cancelacion can=new Cancelacion(cita);
                                vec.addElement(can);
                                break;
                            case CitaPublica.miTipo:
                                //Formo Cita
                                citaString=arr[1];
                                cita=Utils.stringToCita(citaString);
                                CitaPublica cp=new CitaPublica(cita);
                                vec.addElement(cp);
                                break;
                            case Confirmacion.miTipo:
                                //Formo Cita
                                citaString=arr[1];
                                cita=Utils.stringToCita(citaString);
                                //Formo vector de usuarios
                                Vector vecUsrs=Utils.stringToUserVector(arr[2]);
                                Confirmacion c=new Confirmacion(vecUsrs, cita);
                                vec.addElement(c);
                                break;
                            case ConfirmacionReagendado.miTipo:
                                //Formo Cita
                                citaString=arr[1];
                                cita=Utils.stringToCita(citaString);
                                //Formo vector de usuarios
                                Vector vecUsrs2=Utils.stringToUserVector(arr[2]);
                                ConfirmacionReagendado c2=new ConfirmacionReagendado(vecUsrs2, cita);
                                vec.addElement(c2);
                                break;
                            case Respuesta.miTipo:
                                //Forma cita
                                citaString=arr[1];
                                cita=Utils.stringToCita(citaString);

                                //Forma usuario
                                usuarioString=arr[2];
                                usuario=Utils.stringToUsuario(usuarioString);

                                //respuesta
                                boolean respuesta;
                                if(arr[3].equals("true"))
                                    respuesta=true;

                                else
                                    respuesta=false;
                                Respuesta res=new Respuesta(cita, usuario, respuesta);
                                vec.addElement(res);
                                break;
                            case Sincronizacion.miTipo:
                                System.out.println("lista de cambios: "+arr[1]);
                                Vector vecCambio=Utils.stringToCambioVector(arr[1]);
                                Sincronizacion s=new Sincronizacion();
                                s.setListaCambios(vecCambio);
                                vec.addElement(s);
                                System.out.println("sincro: "+s);
                                break;
                        }

                     }
	            //}
	        } catch (InvalidRecordIDException ex) {
            ex.printStackTrace();
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
	        return vec;

        }


	

}
