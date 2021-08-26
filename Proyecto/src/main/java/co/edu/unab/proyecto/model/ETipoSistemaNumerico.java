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
public enum ETipoSistemaNumerico {
    SELECCIONAR (0,"Seleccionar"),    
    BINARIO (1,"Binario"),
    DECIMAL (2,"Decimal"),
    HEXADECIMAL (3,"Hexadecimal");
    
    int id;
    String sistema;
    
    ETipoSistemaNumerico(int id, String sistema){
        this.id=id;
        this.sistema=sistema;
    }
   public static String[] getTipoSistema(){
        String[] resultado;
        ETipoSistemaNumerico[] objeto =ETipoSistemaNumerico.values();
        int longitud =objeto.length;
        resultado= new String[longitud];
        for ( int i=0; i<longitud;i++){
            resultado[i]=objeto[i].sistema;   
        }
        return resultado;    
    }
    public static ETipoSistemaNumerico getCodigoSistema(String sistema){
        ETipoSistemaNumerico[] objeto= ETipoSistemaNumerico.values();
        int longitud= objeto.length;
        for (int i=0; i<longitud;i++){
            if(objeto[i].sistema.equals(sistema)){
                return objeto[i];
            }
        }
        return null;
    }
}
