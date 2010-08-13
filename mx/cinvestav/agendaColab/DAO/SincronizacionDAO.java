/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.cinvestav.agendaColab.DAO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import mx.cinvestav.agendaColab.comun.Evento;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;
import mx.cinvestav.agendaColab.dataStorage.SimpleDataSource;
import mx.cinvestav.agendaColab.utils.Utils;


/**
 *
 * @author rockderick
 */
public class SincronizacionDAO extends AbstractDataDAO{
   
    private static final String dataStorage="sincro";

    public SincronizacionDAO() {
		// TODO Auto-generated constructor stub
		datasource=SimpleDataSource.getInstance(dataStorage);
	}

    public Object create(Object obj) {
                  RecordStore rs=datasource.getRecordStore();

                  BeanUsuario user =(BeanUsuario)obj;
                  ByteArrayOutputStream baos=new ByteArrayOutputStream();
                  DataOutputStream dos = new DataOutputStream(baos);

                  try {
                    dos.writeUTF(user.getPass()+" "+user.getLogin());

                            dos.flush();

                            byte[] datos = baos.toByteArray();

                int id = rs.addRecord(datos, 0, datos.length);

                dos.close();

                baos.close();
                    } catch (IOException e) {

                            e.printStackTrace();
                    }catch(RecordStoreException e){
                            e.printStackTrace();
                    }finally{
                            //datasource.closeRecordStore();
                    }

            return user;
    }

    public Object cargar(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector getLista() {
        RecordStore rs=datasource.getRecordStore();
		String arr[];
		BeanUsuario usuario = null;
                vec = new Vector();
		 try{

	            for(int i =1; i <= rs.getNumRecords();i++){

	                String aux = getRecord(i,dataStorage);
	                //System.out.println("aux: "+aux);
	                
                        vec.addElement(aux);
	            }
	        }catch(RecordStoreNotOpenException e){
	            System.out.println(e.getMessage());
	        }
	     return vec;
    }








}
