/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.CustomerOrder;
import entity.Product;
import entity.User;
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

    public List<Product> getAllProducts() {
        Query q = em.createNamedQuery("Product.findAll", Product.class);
        return (List<Product>) q.getResultList();
    }

    public List<User> getAllUsers() {
        Query q = em.createNamedQuery("User.findAll", User.class);
        return (List<User>) q.getResultList();
    }

    public List<User> getUserByName(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return (List<User>) q.getResultList();
    }

    public List<User> getUserByID(int ID){
        Query q = em.createNamedQuery("User.findById", User.class);
        q.setParameter("id", ID);
        return (List<User>)q.getResultList();
    }

    public void purchaseProduct(Product product, int amount, User user) {
        Product prod = em.find(Product.class, product.getId());
        System.out.println(prod);
        User u = em.find(User.class, user.getId());
        System.out.println(u);
        prod.setQuantityOnHand(prod.getQuantityOnHand() - amount);
        em.merge(prod);
        
        CustomerOrder order = new CustomerOrder();
        order.setCustomerId(u);
        order.setProductId(prod);
        order.setCost(product.getPrice() * amount);
        order.setQuantity(amount);

        System.out.println(order);
        em.persist(order);

    }

    public List<CustomerOrder> getAllOrders() {
        Query q = em.createNamedQuery("CustomerOrder.findAll", CustomerOrder.class);
        return (List<CustomerOrder>) q.getResultList();
    }

    public void updateUser(User user) {
        User u = em.find(User.class, user.getId());

        u.setStatusMessage(user.getStatusMessage());
        System.out.println("Updating user");
        System.out.println(u);

        em.merge(u);
    }

}
