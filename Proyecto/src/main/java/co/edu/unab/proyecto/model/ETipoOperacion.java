/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.model;

/**
 *
 * @author jank
 */
public enum ETipoOperacion {
    
    SELECCIONAR (0, "Seleccionar"),
    SUMA (1,"Suma"),
    RESTA (2,"Resta"),
    MULTIPLICACION (3,"Multiplicación"),
    DIVICION (4,"División"),
    RADICACION (5,"Radicación"),
    POTENCIACION (5,"Potenciación");
    
    int id;
    String operacion;
    
    ETipoOperacion(int id, String operacion){
        this.id=id;
        this.operacion= operacion;
    }
    
    public static String[] getOperaciones(){
        String[] resultado;
        ETipoOperacion[] objeto =ETipoOperacion.values();
        int longitud =objeto.length;
        resultado= new String[longitud];
        for ( int i=0; i<longitud;i++){
            resultado[i]=objeto[i].operacion;   
        }
        return resultado;    
    }
    

    public static ETipoOperacion getCodigo(String operacion){
        ETipoOperacion[] objeto= ETipoOperacion.values();
        int longitud= objeto.length;
        for (int i=0; i<longitud;i++){
            if(objeto[i].operacion.equals(operacion)){
                return objeto[i];
            }
        }
        return null;
    }
    
    
}
