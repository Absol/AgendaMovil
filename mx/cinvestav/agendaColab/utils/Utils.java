/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.cinvestav.agendaColab.utils;

/**
 *
 * @author rockderick
 */
import java.util.Date;
import java.util.Vector;
import mx.cinvestav.agendaColab.comun.Cambio;
import mx.cinvestav.agendaColab.comun.beans.BeanCita;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;
import mx.cinvestav.agendaColab.comun.beans.BeanUsuario;

public class Utils {


	public static String[] split(String p_text, String p_seperator)
	{
	    Vector vecStrings = new Vector();

	    int index;
	    int prevIdx = 0;

	    while ((index = p_text.indexOf(p_seperator, prevIdx)) > -1)
	    {
	      vecStrings.addElement(p_text.substring(prevIdx, index));
	      prevIdx = index + 1;
	    }
	    vecStrings.addElement(p_text.substring(prevIdx));

	    String[] result = new String[vecStrings.size()];
	    vecStrings.copyInto(result);

	    return result;
	}

        public static BeanCita stringToCita(String citaStr){
            String citas[];
            citas=Utils.split(citaStr,"-");

             int id=new Integer(Integer.parseInt(citas[0])).intValue();
             String asunto=citas[1];
             Date fechaInicio=new Date(Long.parseLong(citas[2]));
             Date fechaTermino=new Date(Long.parseLong(citas[3]));
             int nivel=new Integer(Integer.parseInt(citas[4])).intValue();
             int idServidor=new Integer(Integer.parseInt(citas[5])).intValue();
             BeanCita cita=new BeanCita(id, asunto, fechaInicio, fechaTermino,nivel,idServidor);
             
             return cita;
        }

        public static BeanUsuario stringToUsuario(String userStr){
            String users[];
            users=split(userStr,"-");
            int id=new Integer(Integer.parseInt(users[0])).intValue();
            String usr=users[1];
            String pass=users[2];
            BeanUsuario usuario= new BeanUsuario(id, usr, pass);
            return usuario;

        }

        public static BeanContacto stringToContacto(String contactoStr){
            String contacto[];
            contacto=split(contactoStr,"-");
            contacto[0]=contacto[0].trim();
            int id=new Integer(Integer.parseInt(contacto[0])).intValue();
            contacto[1]=contacto[1].trim();
            int idUsu=new Integer(Integer.parseInt(contacto[1])).intValue();
            String nombre=contacto[2];
            String apPaterno=contacto[3];
            String apMaterno=contacto[4];
            String email=contacto[5];
            String telefono=contacto[6];

            BeanContacto con=new BeanContacto(id, idUsu, nombre, apPaterno, apMaterno, email, telefono);

            return con;
        }

        public static Vector stringToUserVector(String vecStr){
            Vector vecUsrs=new Vector();
            String usrStr[];
            int begin=vecStr.indexOf('[')+1;
            int end=vecStr.indexOf(']');
            String usersString=vecStr.substring(begin, end);

            usrStr=split(usersString, ",");
            for(int i=0;i<usrStr.length;i++){
                usrStr[i]=usrStr[i].trim();
                BeanUsuario usuario=stringToUsuario(usrStr[i]);
                vecUsrs.addElement(usuario);
            }
            return vecUsrs;
        }

        public static Vector stringToCambioVector(String vecStr){
            Vector vecCambio=new Vector();
            String cambioStr[],str[];
            int begin=vecStr.indexOf('[')+1;
            int end=vecStr.indexOf(']');

            String cambioString=vecStr.substring(begin, end);

            cambioStr=split(cambioString, ",");
            for(int i=0;i<cambioStr.length;i++){
                cambioStr[i]=cambioStr[i];
                str=split(cambioStr[i],"?");

                BeanContacto contacto=stringToContacto(str[0]);
                int tipo=new Integer(Integer.parseInt(str[1])).intValue();
                
                Cambio c=new Cambio(contacto, tipo);
                vecCambio.addElement(c);
            }

            return vecCambio;
        }

}
