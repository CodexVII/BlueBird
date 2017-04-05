/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Product;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author keita
 */
@Stateless
@LocalBean
public class UserEJB {
    @PersistenceContext
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void addProduct(Product product){
        em.merge(product);
    }
    
    public Product getProduct(String name){
        Query q = em.createNamedQuery("Product.findByName", Product.class);
        return (Product)q.getSingleResult();
    }
    
    public List<Product> getAllProducts(){
        Query q = em.createNamedQuery("Product.findAll", Product.class);
        return (List<Product>)q.getResultList();
    }
}
