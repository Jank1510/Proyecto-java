/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.controller;

import co.edu.unab.proyecto.controller.exceptions.NonexistentEntityException;
import co.edu.unab.proyecto.model.Entradas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.unab.proyecto.model.Registros;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jank
 */
public class EntradasJpaController implements Serializable {

    public EntradasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entradas entradas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Registros idRegistro = entradas.getIdRegistro();
            if (idRegistro != null) {
                idRegistro = em.getReference(idRegistro.getClass(), idRegistro.getRegistro());
                entradas.setIdRegistro(idRegistro);
            }
            em.persist(entradas);
            if (idRegistro != null) {
                idRegistro.getEntradasList().add(entradas);
                idRegistro = em.merge(idRegistro);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entradas entradas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entradas persistentEntradas = em.find(Entradas.class, entradas.getIdValor());
            Registros idRegistroOld = persistentEntradas.getIdRegistro();
            Registros idRegistroNew = entradas.getIdRegistro();
            if (idRegistroNew != null) {
                idRegistroNew = em.getReference(idRegistroNew.getClass(), idRegistroNew.getRegistro());
                entradas.setIdRegistro(idRegistroNew);
            }
            entradas = em.merge(entradas);
            if (idRegistroOld != null && !idRegistroOld.equals(idRegistroNew)) {
                idRegistroOld.getEntradasList().remove(entradas);
                idRegistroOld = em.merge(idRegistroOld);
            }
            if (idRegistroNew != null && !idRegistroNew.equals(idRegistroOld)) {
                idRegistroNew.getEntradasList().add(entradas);
                idRegistroNew = em.merge(idRegistroNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entradas.getIdValor();
                if (findEntradas(id) == null) {
                    throw new NonexistentEntityException("The entradas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entradas entradas;
            try {
                entradas = em.getReference(Entradas.class, id);
                entradas.getIdValor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entradas with id " + id + " no longer exists.", enfe);
            }
            Registros idRegistro = entradas.getIdRegistro();
            if (idRegistro != null) {
                idRegistro.getEntradasList().remove(entradas);
                idRegistro = em.merge(idRegistro);
            }
            em.remove(entradas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entradas> findEntradasEntities() {
        return findEntradasEntities(true, -1, -1);
    }

    public List<Entradas> findEntradasEntities(int maxResults, int firstResult) {
        return findEntradasEntities(false, maxResults, firstResult);
    }

    private List<Entradas> findEntradasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entradas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Entradas findEntradas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entradas.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntradasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entradas> rt = cq.from(Entradas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
