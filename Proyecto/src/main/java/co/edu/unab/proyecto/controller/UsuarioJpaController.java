/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unab.proyecto.controller;

import co.edu.unab.proyecto.controller.exceptions.IllegalOrphanException;
import co.edu.unab.proyecto.controller.exceptions.NonexistentEntityException;
import co.edu.unab.proyecto.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.unab.proyecto.model.Registros;
import co.edu.unab.proyecto.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jank
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getRegistrosList() == null) {
            usuario.setRegistrosList(new ArrayList<Registros>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Registros> attachedRegistrosList = new ArrayList<Registros>();
            for (Registros registrosListRegistrosToAttach : usuario.getRegistrosList()) {
                registrosListRegistrosToAttach = em.getReference(registrosListRegistrosToAttach.getClass(), registrosListRegistrosToAttach.getRegistro());
                attachedRegistrosList.add(registrosListRegistrosToAttach);
            }
            usuario.setRegistrosList(attachedRegistrosList);
            em.persist(usuario);
            for (Registros registrosListRegistros : usuario.getRegistrosList()) {
                Usuario oldUsuarioOfRegistrosListRegistros = registrosListRegistros.getUsuario();
                registrosListRegistros.setUsuario(usuario);
                registrosListRegistros = em.merge(registrosListRegistros);
                if (oldUsuarioOfRegistrosListRegistros != null) {
                    oldUsuarioOfRegistrosListRegistros.getRegistrosList().remove(registrosListRegistros);
                    oldUsuarioOfRegistrosListRegistros = em.merge(oldUsuarioOfRegistrosListRegistros);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getId()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Registros> registrosListOld = persistentUsuario.getRegistrosList();
            List<Registros> registrosListNew = usuario.getRegistrosList();
            List<String> illegalOrphanMessages = null;
            for (Registros registrosListOldRegistros : registrosListOld) {
                if (!registrosListNew.contains(registrosListOldRegistros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Registros " + registrosListOldRegistros + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Registros> attachedRegistrosListNew = new ArrayList<Registros>();
            for (Registros registrosListNewRegistrosToAttach : registrosListNew) {
                registrosListNewRegistrosToAttach = em.getReference(registrosListNewRegistrosToAttach.getClass(), registrosListNewRegistrosToAttach.getRegistro());
                attachedRegistrosListNew.add(registrosListNewRegistrosToAttach);
            }
            registrosListNew = attachedRegistrosListNew;
            usuario.setRegistrosList(registrosListNew);
            usuario = em.merge(usuario);
            for (Registros registrosListNewRegistros : registrosListNew) {
                if (!registrosListOld.contains(registrosListNewRegistros)) {
                    Usuario oldUsuarioOfRegistrosListNewRegistros = registrosListNewRegistros.getUsuario();
                    registrosListNewRegistros.setUsuario(usuario);
                    registrosListNewRegistros = em.merge(registrosListNewRegistros);
                    if (oldUsuarioOfRegistrosListNewRegistros != null && !oldUsuarioOfRegistrosListNewRegistros.equals(usuario)) {
                        oldUsuarioOfRegistrosListNewRegistros.getRegistrosList().remove(registrosListNewRegistros);
                        oldUsuarioOfRegistrosListNewRegistros = em.merge(oldUsuarioOfRegistrosListNewRegistros);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Registros> registrosListOrphanCheck = usuario.getRegistrosList();
            for (Registros registrosListOrphanCheckRegistros : registrosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Registros " + registrosListOrphanCheckRegistros + " in its registrosList field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
