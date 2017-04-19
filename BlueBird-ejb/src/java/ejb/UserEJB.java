/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.CustomerOrder;
import entity.Product;
import entity.User;
import entity.Usergroup;
import java.util.ArrayList;
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
    
    public List<Product> getProductByID(int ID){
        Query q = em.createNamedQuery("Product.findById", Product.class);
        return (List<Product>)q.getResultList();
    }

    public void purchaseProduct(Product product, int amount, User user) {
        Product prod = em.find(Product.class, product.getId());
        
        User u = em.find(User.class, user.getId());
         // Subtract total cost from user balance
        double totalCost = amount * prod.getPrice();
        u.setBalance(u.getBalance() - totalCost);
        prod.setQuantityOnHand(prod.getQuantityOnHand() - amount);
        
        em.merge(u);
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
    
    public List<CustomerOrder> getAllOrdersByUser(User user) {
        Query q = em.createNamedQuery("CustomerOrder.findAll", CustomerOrder.class);
        List<CustomerOrder> fromDatabase, returnList;
        int customerId;
        
        fromDatabase = (List<CustomerOrder>) q.getResultList();
        returnList = new ArrayList<CustomerOrder>();
        
        // Loop through all results to find matches for customer_id
        for(int i = 0; i < fromDatabase.size(); i++) {
            customerId = fromDatabase.get(i).getCustomerId().getId();
            
            // If match, add to returnList
            if(customerId == user.getId()) {
                returnList.add(fromDatabase.get(i));
            }
        }
        
        return returnList;
    }

    public void updateUser(User user) {
        User u = em.find(User.class, user.getId());

        u.setStatusMessage(user.getStatusMessage());
        System.out.println("Updating user");
        System.out.println(u);

        em.merge(u);
    }
    
    public boolean isAdmin(User user){
        Usergroup group = em.find(Usergroup.class, user.getUsername());
        if (group.getDomain().toLowerCase().equals("admin")){
            return true;
        }
        return false;
    }

}
