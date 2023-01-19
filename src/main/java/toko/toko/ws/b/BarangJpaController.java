/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package toko.toko.ws.b;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import toko.toko.ws.b.exceptions.NonexistentEntityException;
import toko.toko.ws.b.exceptions.PreexistingEntityException;

/**
 *
 * @author user
 */
public class BarangJpaController implements Serializable {

    public BarangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("toko_toko.ws.b_jar_0.0.1-SNAPSHOTPU");
    
    public BarangJpaController(){
        
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barang barang) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(barang);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBarang(barang.getId()) != null) {
                throw new PreexistingEntityException("Barang " + barang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barang barang) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            barang = em.merge(barang);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = barang.getId();
                if (findBarang(id) == null) {
                    throw new NonexistentEntityException("The barang with id " + id + " no longer exists.");
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
            Barang barang;
            try {
                barang = em.getReference(Barang.class, id);
                barang.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barang with id " + id + " no longer exists.", enfe);
            }
            em.remove(barang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barang> findBarangEntities() {
        return findBarangEntities(true, -1, -1);
    }

    public List<Barang> findBarangEntities(int maxResults, int firstResult) {
        return findBarangEntities(false, maxResults, firstResult);
    }

    private List<Barang> findBarangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barang.class));
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

    public Barang findBarang(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barang.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barang> rt = cq.from(Barang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
