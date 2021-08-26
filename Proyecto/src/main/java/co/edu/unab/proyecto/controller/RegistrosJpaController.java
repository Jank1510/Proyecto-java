/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.controller;

import co.edu.unab.proyecto.controller.exceptions.IllegalOrphanException;
import co.edu.unab.proyecto.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.unab.proyecto.model.Usuario;
import co.edu.unab.proyecto.model.Entradas;
import co.edu.unab.proyecto.model.Registros;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jank
 */
public class RegistrosJpaController implements Serializable {

    public RegistrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Registros registros) {
        if (registros.getEntradasList() == null) {
            registros.setEntradasList(new ArrayList<Entradas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = registros.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                registros.setUsuario(usuario);
            }
            List<Entradas> attachedEntradasList = new ArrayList<Entradas>();
            for (Entradas entradasListEntradasToAttach : registros.getEntradasList()) {
                entradasListEntradasToAttach = em.getReference(entradasListEntradasToAttach.getClass(), entradasListEntradasToAttach.getIdValor());
                attachedEntradasList.add(entradasListEntradasToAttach);
            }
            registros.setEntradasList(attachedEntradasList);
            em.persist(registros);
            if (usuario != null) {
                usuario.getRegistrosList().add(registros);
                usuario = em.merge(usuario);
            }
            for (Entradas entradasListEntradas : registros.getEntradasList()) {
                Registros oldIdRegistroOfEntradasListEntradas = entradasListEntradas.getIdRegistro();
                entradasListEntradas.setIdRegistro(registros);
                entradasListEntradas = em.merge(entradasListEntradas);
                if (oldIdRegistroOfEntradasListEntradas != null) {
                    oldIdRegistroOfEntradasListEntradas.getEntradasList().remove(entradasListEntradas);
                    oldIdRegistroOfEntradasListEntradas = em.merge(oldIdRegistroOfEntradasListEntradas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Registros registros) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Registros persistentRegistros = em.find(Registros.class, registros.getRegistro());
            Usuario usuarioOld = persistentRegistros.getUsuario();
            Usuario usuarioNew = registros.getUsuario();
            List<Entradas> entradasListOld = persistentRegistros.getEntradasList();
            List<Entradas> entradasListNew = registros.getEntradasList();
            List<String> illegalOrphanMessages = null;
            for (Entradas entradasListOldEntradas : entradasListOld) {
                if (!entradasListNew.contains(entradasListOldEntradas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entradas " + entradasListOldEntradas + " since its idRegistro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                registros.setUsuario(usuarioNew);
            }
            List<Entradas> attachedEntradasListNew = new ArrayList<Entradas>();
            for (Entradas entradasListNewEntradasToAttach : entradasListNew) {
                entradasListNewEntradasToAttach = em.getReference(entradasListNewEntradasToAttach.getClass(), entradasListNewEntradasToAttach.getIdValor());
                attachedEntradasListNew.add(entradasListNewEntradasToAttach);
            }
            entradasListNew = attachedEntradasListNew;
            registros.setEntradasList(entradasListNew);
            registros = em.merge(registros);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getRegistrosList().remove(registros);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getRegistrosList().add(registros);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Entradas entradasListNewEntradas : entradasListNew) {
                if (!entradasListOld.contains(entradasListNewEntradas)) {
                    Registros oldIdRegistroOfEntradasListNewEntradas = entradasListNewEntradas.getIdRegistro();
                    entradasListNewEntradas.setIdRegistro(registros);
                    entradasListNewEntradas = em.merge(entradasListNewEntradas);
                    if (oldIdRegistroOfEntradasListNewEntradas != null && !oldIdRegistroOfEntradasListNewEntradas.equals(registros)) {
                        oldIdRegistroOfEntradasListNewEntradas.getEntradasList().remove(entradasListNewEntradas);
                        oldIdRegistroOfEntradasListNewEntradas = em.merge(oldIdRegistroOfEntradasListNewEntradas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = registros.getRegistro();
                if (findRegistros(id) == null) {
                    throw new NonexistentEntityException("The registros with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Registros registros;
            try {
                registros = em.getReference(Registros.class, id);
                registros.getRegistro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registros with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Entradas> entradasListOrphanCheck = registros.getEntradasList();
            for (Entradas entradasListOrphanCheckEntradas : entradasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Registros (" + registros + ") cannot be destroyed since the Entradas " + entradasListOrphanCheckEntradas + " in its entradasList field has a non-nullable idRegistro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuario = registros.getUsuario();
            if (usuario != null) {
                usuario.getRegistrosList().remove(registros);
                usuario = em.merge(usuario);
            }
            em.remove(registros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Registros> findRegistrosEntities() {
        return findRegistrosEntities(true, -1, -1);
    }

    public List<Registros> findRegistrosEntities(int maxResults, int firstResult) {
        return findRegistrosEntities(false, maxResults, firstResult);
    }

    private List<Registros> findRegistrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Registros.class));
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

    public Registros findRegistros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Registros.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegistrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Registros> rt = cq.from(Registros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
