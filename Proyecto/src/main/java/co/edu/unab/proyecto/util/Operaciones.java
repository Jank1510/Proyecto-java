/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.util;

import co.edu.unab.proyecto.view.calculadora;

/**
 *
 * @author jank
 */
public class Operaciones {
    calculadora calculadora = new calculadora();
    
    public String tipo_operacion(String dato1, String dato2,String tipoOperacion){
        float valor1= Integer.parseInt(dato1);
        float valor2= Integer.parseInt(dato2);
        float resultado = 0;
        int a;
        
        if (tipoOperacion=="Suma"){
            resultado = (valor1+valor2);            
        }
        else if (tipoOperacion=="Resta"){
            resultado = (valor1-valor2);            
        }
        else if (tipoOperacion=="Multiplicación"){
            resultado = (valor1*valor2);            
        }
        else if (tipoOperacion=="División"){
            resultado =(valor1/valor2);   
        }
        else if (tipoOperacion=="Radicación"){
            resultado = (short)Math.pow((double)valor2,1/(double)valor1);             
        }
        else if (tipoOperacion=="Potenciación"){
            resultado = (float) Math.pow(valor1,valor2);           
        }  
        
        a = (int)resultado;
        String res = String.valueOf(a);
        return res;
    }
}
