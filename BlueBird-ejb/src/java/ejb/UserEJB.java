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
 * @author Alan Noonan, Ian Lodovica, Dylan O'Connor Desmond, Gearoid Cremin, Trevor McSweeney
 *
 * All the methods to allow regular users to interact with the database
 */
@Stateless
@LocalBean
public class UserEJB {

    @PersistenceContext
    EntityManager em;

    /**
     * Returns a list of all products in the database.
     *
     * @return the list of all products
     */
    public List<Product> getAllProducts() {
        Query q = em.createNamedQuery("Product.findAll", Product.class);
        return (List<Product>) q.getResultList();
    }

    /**
     * Returns a list of all users in the database.
     *
     * @return the list of all users
     */
    public List<User> getAllUsers() {
        Query q = em.createNamedQuery("User.findAll", User.class);
        return (List<User>) q.getResultList();
    }

    /**
     * Returns a list of all users in the database based on a String search.
     *
     * @param username the username String for the search
     * @return the list of all users matching the input String
     */
    public List<User> getUserByName(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return (List<User>) q.getResultList();
    }

    /**
     * Returns a list of all users in the database based on an ID search.
     *
     * @param ID the ID for the search
     * @return the list of all users matching the input ID
     */
    public List<User> getUserByID(int ID){
        Query q = em.createNamedQuery("User.findById", User.class);
        q.setParameter("id", ID);
        return (List<User>)q.getResultList();
    }

    /**
     * Returns a list of all products in the database based on an ID search.
     *
     * @param ID the ID for the search
     * @return the list of all products matching the input ID
     */
    public List<Product> getProductByID(int ID){
        Query q = em.createNamedQuery("Product.findById", Product.class);
        return (List<Product>)q.getResultList();
    }

    /**
     * Adds records to database for a product purchase:
     * (i)   subtracts the amount from the user's balance
     * (ii)  adjusts the quantity in the Product table
     * (iii) creates an entry in the CustomerOrder table.
     *
     * @param product the product being purchased
     * @param amount the amount of the product being purchased
     * @param user the user making the purchase
     */
    public void purchaseProduct(Product product, int amount, User user) {
        // Find product
        Product prod = em.find(Product.class, product.getId());

        // Find user
        User u = em.find(User.class, user.getId());
         // Subtract total cost from user balance
        double totalCost = amount * prod.getPrice();
        u.setBalance(u.getBalance() - totalCost);
        prod.setQuantityOnHand(prod.getQuantityOnHand() - amount);

        // Merge user and product
        em.merge(u);
        em.merge(prod);

        // Create new order record
        CustomerOrder order = new CustomerOrder();
        order.setCustomerId(u);
        order.setProductId(prod);
        order.setCost(product.getPrice() * amount);
        order.setQuantity(amount);

        System.out.println(order);
        em.persist(order);
    }

    /**
     * Returns a list of all orders in the database.
     *
     * @return the list of all orders
     */
    public List<CustomerOrder> getAllOrders() {
        Query q = em.createNamedQuery("CustomerOrder.findAll", CustomerOrder.class);
        return (List<CustomerOrder>) q.getResultList();
    }

    /**
     * Returns a list of all orders in the database for a given user.
     *
     * @param user the user to get all orders for
     * @return the list of all orders for the input user
     */
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

    /**
     * Updates user details.
     *
     * @param user the user to be updated
     */
    public void updateUser(User user) {
        User u = em.find(User.class, user.getId());

        u.setStatusMessage(user.getStatusMessage());
        System.out.println("Updating user");
        System.out.println(u);

        em.merge(u);
    }

    /**
     * Checks if a user is an admin.
     *
     * @param user the user to check if admin
     * @return if the user is an admin or not
     */
    public boolean isAdmin(User user){
        // Find group based on username
        Usergroup group = em.find(Usergroup.class, user.getUsername());

        // Check if "admin" is in group domain
        if (group.getDomain().toLowerCase().equals("admin")){
            return true;
        }
        return false;
    }

}
