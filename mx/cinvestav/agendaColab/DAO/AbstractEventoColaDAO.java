package mx.cinvestav.agendaColab.DAO;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import mx.cinvestav.agendaColab.comun.Evento;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.dataStorage.SimpleDataSource;

public abstract class AbstractEventoColaDAO implements GenericEventDAO{
	protected SimpleDataSource datasource;
        protected Vector vec=new Vector();
	
	public String getRecord(int id,String dataStorage){
        String toReturn = "";
        datasource=SimpleDataSource.getInstance(dataStorage);
	    RecordStore rs=datasource.getRecordStore();
        try{
            int tamRecord = rs.getRecordSize(id);

            byte[] dados = new byte[tamRecord];

            ByteArrayInputStream bais = new ByteArrayInputStream(dados);

            DataInputStream dis = new DataInputStream(bais);

            rs.getRecord(id, dados, 0);

            toReturn = dis.readUTF();

            bais.close();
            dis.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println(e.getMessage());
        }catch(InvalidRecordIDException e){
            System.out.println(e.getMessage());
        }catch(RecordStoreNotOpenException e){
            System.out.println(e.getMessage());
        }catch(RecordStoreException e){
            System.out.println(e.getMessage());
        }
        return toReturn;
    }

    public void deleteAllRecordStore()  {
                RecordStore rs=datasource.getRecordStore();


            try {
                RecordEnumeration re = rs.enumerateRecords(null, null, false);
                while(re.hasNextElement()) {
                try {

                    rs.deleteRecord(re.nextRecordId());
                } catch (RecordStoreNotOpenException ex) {
                    ex.printStackTrace();
                } catch (InvalidRecordIDException ex) {
                    ex.printStackTrace();
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }

                }

                } catch (RecordStoreNotOpenException ex) {
                ex.printStackTrace();
                }
    }
	

}
