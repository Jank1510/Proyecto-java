/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.controller;

import co.edu.unab.proyecto.model.ETipoOperacion;
import co.edu.unab.proyecto.model.ETipoSistemaNumerico;
import co.edu.unab.proyecto.model.Entradas;
import co.edu.unab.proyecto.model.Registros;
import co.edu.unab.proyecto.model.Usuario;
import co.edu.unab.proyecto.util.ConnectionHelper;
import co.edu.unab.proyecto.util.Operaciones;
import co.edu.unab.proyecto.util.Variables;
import co.edu.unab.proyecto.util.sistema;
import co.edu.unab.proyecto.view.calculadora;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author jank
 */
public class ViewControllercalculadore implements ActionListener {
    calculadora calculadora = new calculadora();
    sistema sistema = new sistema();
    Operaciones operaciones = new Operaciones();
    Usuario usuario = new Usuario();
    
    public void iniciarCalculadora(){
        this.calculadora.setTitle("Calculadora");
        this.calculadora.setLocationRelativeTo(null);
        this.calculadora.setVisible(true);
        this.calculadora.cbOperacion.setModel(new DefaultComboBoxModel<>(ETipoOperacion.getOperaciones()));
        this.calculadora.cbSistema.setModel(new DefaultComboBoxModel<>(ETipoSistemaNumerico.getTipoSistema()));
        this.calculadora.btnCalcular.addActionListener(this);
        this.calculadora.btnSalir.addActionListener(this);
        
        
    }
    public Date fecha(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(date);        
        
        return date;        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Calcular")){

            String sistema_codigo= calculadora.cbSistema.getItemAt(calculadora.cbSistema.getSelectedIndex());
            System.out.println(sistema_codigo);
            ETipoSistemaNumerico codigo_Sistema = ETipoSistemaNumerico.getCodigoSistema(sistema_codigo);
            int a = 0;
            ETipoSistemaNumerico[] listaSistema = ETipoSistemaNumerico.values();
            for (int i = 0; i < listaSistema.length; i++) {
                if (codigo_Sistema==listaSistema[i]){
                    a = i;
                }
            }        
            String operacion_codigo= calculadora.cbOperacion.getItemAt(calculadora.cbOperacion.getSelectedIndex());
            ETipoOperacion codigo_Operacion = ETipoOperacion.getCodigo(operacion_codigo);
            int b = 0;
            ETipoOperacion[] listaOperacion = ETipoOperacion.values();
            for (int i = 0; i < listaOperacion.length; i++) {
                if (codigo_Operacion==listaOperacion[i]){
                    b = i;
                }
            }
            String sistema__codigo= calculadora.cbSistema.getItemAt(calculadora.cbSistema.getSelectedIndex());
            String num1 = calculadora.txtdato1.getText();
            String num2 = calculadora.txtdato2.getText();
            String tipoOperacion = calculadora.cbOperacion.getItemAt(calculadora.cbOperacion.getSelectedIndex());
            calculadora.txtResultado.setText(sistema.tipo_sistema( sistema__codigo, num1, num2, tipoOperacion));
            System.out.println(sistema.tipo_sistema( sistema__codigo, num1, num2, tipoOperacion));

            System.out.println(a);  
            Variables variable = new Variables();
            Registros registros = new Registros();

            registros.setUsuario(Variables.usuario);
            registros.setFecha(fecha());
            registros.setSistema(a);    
            registros.setOperacion(b);
            registros.setResultado(sistema.tipo_sistema( sistema__codigo, num1, num2, tipoOperacion));
            RegistrosJpaController controladorRegistros = new RegistrosJpaController(ConnectionHelper.getEmf());
            controladorRegistros.create(registros);

            Entradas entradas = new Entradas();
            entradas.setIdRegistro(registros);
            entradas.setEntradas(num2);
            EntradasJpaController controladorEntradas = new EntradasJpaController(ConnectionHelper.getEmf());
            controladorEntradas.create(entradas);

            entradas.setIdRegistro(registros);
            entradas.setEntradas(num2);
            EntradasJpaController controladorEntradasas = new EntradasJpaController(ConnectionHelper.getEmf());
            controladorEntradasas.create(entradas);
        }
        else if(e.getActionCommand().equals("Salir")){
            System.exit(0);
        }
        
    }
    
    
}
