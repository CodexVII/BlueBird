/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Product;
import entity.User;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author keita
 */
@Stateless
@LocalBean
public class AdminEJB {
    @PersistenceContext
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    public void addProduct(Product product) {
        em.merge(product);
    }
    
    public void updateProduct(Product product) {
        em.merge(product);
    }
     
    public void removeProduct(Product product){
        em.remove(product);
    }
    
    public void addUser(User user){
        em.merge(user);
    }
    
    public void updateUser(User user){
        em.merge(user);
    }
}
