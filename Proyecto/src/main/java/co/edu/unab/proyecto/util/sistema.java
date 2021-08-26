/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.util;

import co.edu.unab.proyecto.controller.ViewControllercalculadore;
import co.edu.unab.proyecto.model.ETipoSistemaNumerico;
import co.edu.unab.proyecto.view.calculadora;

/**
 *
 * @author jank
 */
public class sistema {
    
    public String tipo_sistema(String sistema_codigo,String num1,String num2, String tipoOperacion){
        calculadora calculadora = new calculadora();
        Operaciones operaciones = new Operaciones();
        String  resultado = null;
        
        
        if ("Hexadecimal".equals(sistema_codigo)){
            String dato1 = hexStrToIntStr(num1);
            String dato2 = hexStrToIntStr(num2);
            String sinFormato =operaciones.tipo_operacion(dato1,dato2,tipoOperacion);
            
            resultado = intStrToHexStr(sinFormato);
        }
        else if ("Decimal".equals(sistema_codigo)){
            String dato1 =num1;
            String dato2 =num2;
            resultado=operaciones.tipo_operacion(dato1,dato2,tipoOperacion);
        }
        else if ("Binario".equals(sistema_codigo)){
            long num1Long = new Long(num1).longValue();
            int dato1Int= binarioADecimal(num1Long);
            String dato1 = String.valueOf(dato1Int);
            long num2Long =  new Long(num2).longValue();
            int dato2Int= binarioADecimal(num2Long);
            String dato2 = String.valueOf(dato2Int);
            
            String resultadoSinformato =operaciones.tipo_operacion(dato1,dato2,tipoOperacion);
            int resultadoInt = Integer.parseInt(resultadoSinformato);
            int resultadoconversion = (int) decimalABinario(resultadoInt);
            
            resultado = String.valueOf(resultadoconversion);
        }
        return resultado;
    }
    
    //convierte hexadecimal a decimal
    public static String hexStrToIntStr(String hexStr) {
             // devuelve nulo cuando no hay datos
            if (hexStr == null || hexStr == "") {
                    return null;
            }
            String regexStr = "^[A-Fa-f0-9]+$";
             // La cadena excede el número hexadecimal, devuelve nulo
            if (!hexStr.matches(regexStr)) {
                    return null;
            }
             // Realizar conversión de caracteres
            Integer in = Integer.parseInt(hexStr, 16);
            return String.valueOf(in);
    }
    //el que convierte de decimal a hexadecimal
    
    public static String intStrToHexStr(String intStr) {
            // devuelve nulo cuando no hay datos
            if (intStr == null || intStr == "") {
                    return null;
            }
             // Operación de conversión
            StringBuffer Hex=new StringBuffer("");
        String m="0123456789abcdef";
        int i = Integer.parseInt(intStr, 10);
        if(i==0) Hex.append(i);
        while (i!=0) {
            Hex.append(m.charAt(i%16));
            i>>=4;
        }
        return Hex.reverse().toString();
    }
        //convierte de decimal a binario señora
    public static long decimalABinario(int decimal) {
        long binario = 0;
        int digito;
        final int DIVISOR = 2;
        for (int i = decimal, j = 0; i > 0; i /= DIVISOR, j++) {
            digito = i % DIVISOR;
            binario += digito * Math.pow(10, j);
        }
        return binario;
    }
    //y este de binario a decimal
    public static int binarioADecimal(long binario) {

        int decimal = 0;
        int digito;
        final long DIVISOR = 10;
        for (long i = binario, j = 0; i > 0; i /= DIVISOR, j++) {
            digito = (int) (i % DIVISOR);
            if (digito != 0 && digito != 1) {
                return -1;
            }
            decimal += digito * Math.pow(2, j);
        }
        return decimal;

    }
}
