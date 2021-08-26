/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author jank
 */
public class ConnectionHelper {
    private static EntityManagerFactory emf;
    
    public static EntityManagerFactory getEmf(){
        if(emf==null){
            emf = Persistence.createEntityManagerFactory("co.edu.unab_Proyecto_jar_1.0-SNAPSHOTPU");
        }
        return emf;
    }
}
