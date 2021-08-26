/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.controller;

import co.edu.unab.proyecto.model.Usuario;
import co.edu.unab.proyecto.util.ConnectionHelper;
import co.edu.unab.proyecto.util.Variables;
import co.edu.unab.proyecto.view.Interfaz;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jank
 */
public class ViewControllerInterfaz implements ActionListener{
    Interfaz vista = new Interfaz();
    Variables variables = new Variables();
    ViewControllercalculadore calculadora = new ViewControllercalculadore();
    

   
    public void IniciarInterfaz(){
        this.vista.setTitle("Registro Usuario");
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        this.vista.btnContinuar.addActionListener(this);
        
    }
    
    
    @Override
    public void actionPerformed (ActionEvent e) {
        if(e.getActionCommand().equals("Continuar")){

            System.out.println(e.getActionCommand());

            String nombre = this.vista.txtNombre.getText().toString();
            String apellido = this.vista.txtApellido.getText().toString();
            String idString = this.vista.txtId.getText().toString();
            int id = Integer.parseInt(idString);

            Usuario usuarioo = new Usuario();
            usuarioo.setApellido(apellido);
            usuarioo.setNombre(nombre);
            usuarioo.setId(id);

            UsuarioJpaController controlador = new UsuarioJpaController(ConnectionHelper.getEmf());
            try {
                controlador.create(usuarioo);
                variables.usuario=usuarioo; 
            } catch (Exception ex) {
                Logger.getLogger(ViewControllerInterfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.vista.setVisible(false);
            calculadora.iniciarCalculadora();
        }
        else if(e.getActionCommand().equals("Salir")){
            System.exit(0);
        }
        
    }
    
}
