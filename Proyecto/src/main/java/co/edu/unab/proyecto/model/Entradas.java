/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jank
 */
@Entity
@Table(name = "entradas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entradas.findAll", query = "SELECT e FROM Entradas e"),
    @NamedQuery(name = "Entradas.findByIdValor", query = "SELECT e FROM Entradas e WHERE e.idValor = :idValor"),
    @NamedQuery(name = "Entradas.findByEntradas", query = "SELECT e FROM Entradas e WHERE e.entradas = :entradas")})
public class Entradas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_valor")
    private Integer idValor;
    @Basic(optional = false)
    @Column(name = "entradas")
    private String entradas;
    @JoinColumn(name = "id_Registro", referencedColumnName = "registro")
    @ManyToOne(optional = false)
    private Registros idRegistro;

    public Entradas() {
    }

    public Entradas(Integer idValor) {
        this.idValor = idValor;
    }

    public Entradas(Integer idValor, String entradas) {
        this.idValor = idValor;
        this.entradas = entradas;
    }

    public Integer getIdValor() {
        return idValor;
    }

    public void setIdValor(Integer idValor) {
        this.idValor = idValor;
    }

    public String getEntradas() {
        return entradas;
    }

    public void setEntradas(String entradas) {
        this.entradas = entradas;
    }

    public Registros getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Registros idRegistro) {
        this.idRegistro = idRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idValor != null ? idValor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entradas)) {
            return false;
        }
        Entradas other = (Entradas) object;
        if ((this.idValor == null && other.idValor != null) || (this.idValor != null && !this.idValor.equals(other.idValor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.unab.proyecto.model.Entradas[ idValor=" + idValor + " ]";
    }
    
}
