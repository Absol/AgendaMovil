/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.cinvestav.agendaColab.forms;

import javax.microedition.lcdui.*;
import mx.cinvestav.agendaColab.DAO.ContactoDAO;
import mx.cinvestav.agendaColab.comun.beans.BeanContacto;

/**
 *
 * @author rockderick
 */
public class F_Contactos extends Form {
      private TextField name,apPaterno,apMaterno,mail,tel;
      private int id;
      private int idUsuario;
      BeanContacto contacto;

   public F_Contactos(String title){
        super(title);
        name = new TextField("Nombre:    ", "", 12, TextField.ANY);
        apPaterno = new TextField("A. Paterno:", "", 12, TextField.ANY);
        apMaterno = new TextField("A. Materno:", "", 12, TextField.ANY);
        mail = new TextField("Mail","", 25,TextField.EMAILADDR);
        tel = new TextField("Phone","", 10, TextField.PHONENUMBER);
        this.append(name);
        this.append(apPaterno);
        this.append(apMaterno);
        this.append(mail);
        this.append(tel);
                
    }

   public void setContacto(BeanContacto contacto){
       this.contacto=contacto;
       id=contacto.getidContacto();
       idUsuario=contacto.getIdUsuario();
       name.setString(contacto.getnombre());
       apPaterno.setString(contacto.getapPaterno());
       apMaterno.setString(contacto.getapMaterno());
       mail.setString(contacto.getEmail());
       tel.setString(contacto.getTelefono());
   }

   public BeanContacto getContacto(){
       return new BeanContacto(id, 0, name.getString(),apPaterno.getString(),apMaterno.getString(), mail.getString(), tel.getString());
       
   }
}
