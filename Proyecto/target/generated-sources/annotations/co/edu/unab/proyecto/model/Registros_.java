package co.edu.unab.proyecto.model;

import co.edu.unab.proyecto.model.Entradas;
import co.edu.unab.proyecto.model.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2021-08-19T21:34:11")
@StaticMetamodel(Registros.class)
public class Registros_ { 

    public static volatile SingularAttribute<Registros, Date> fecha;
    public static volatile ListAttribute<Registros, Entradas> entradasList;
    public static volatile SingularAttribute<Registros, String> resultado;
    public static volatile SingularAttribute<Registros, Integer> sistema;
    public static volatile SingularAttribute<Registros, Usuario> usuario;
    public static volatile SingularAttribute<Registros, Integer> operacion;
    public static volatile SingularAttribute<Registros, Integer> registro;

}