/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package information_tracking;

import ejb.AdminEJB;
import ejb.UserEJB;
import entity.CustomerOrder;
import entity.Product;
import entity.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dylan
 */
@Named(value = "profile")
@SessionScoped

public class Profile implements Serializable {
    @Inject
    UserEJB user;
    
    @Inject
    private AdminEJB admin;
    
    // List of all users in datatable
    List<User> users;
    
    // List of logged in user
    private User loggedInUser;
    // Fields for this user that is logged in
    private int id = 0;
    private String username = null;
    private String password = null;
    private String statusMessage = null;
    private double balance  = 0.0;
    
    // Variables for editing profile
    private String newUsername = null;
    private String newPassword = null;
    private String newStatusMessage = null;
    
    // Constants for navigating to webpages
    private final String INDEX = "index";
    private final String USER_PRODUCT = "userProduct";
    private final String EDIT_USER_PROFILE = "editUserProfile";
    
    // This will verify the user login
    public String verifyLogin(){
        this.users = user.getAllUsers();
        String result = this.INDEX;
        System.out.printf("\n\nSize of users = %d\n\n", users.size());

        for(int i = 0; i < users.size(); i++){
            if(this.users.get(i).getUsername().equals(this.username) && this.users.get(i).getPassword().equals(this.password)){
                this.loggedInUser = this.users.get(i);
                this.id = this.users.get(i).getId();
                this.statusMessage = this.users.get(i).getStatusMessage();
                this.balance = this.users.get(i).getBalance();
                
                // Copy current fields into new fields
                this.newUsername = this.username;
                this.newPassword = this.password;
                this.newStatusMessage = this.statusMessage;

                // Return userProduct page
                result = this.USER_PRODUCT;
            }
        }
        
        return result;
    }
    
    // Proper login method, not yet implemented
    public String login(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try{
            request.login(this.username, this.password);
            // Get list of all users
            // TODO - Should this be here?
            this.users = user.getAllUsers();
            
            // Get the logged-in user's details
            this.username = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            this.loggedInUser = getThisUserByName().get(0);
            this.id = this.loggedInUser.getId();
            this.statusMessage = this.loggedInUser.getStatusMessage();
            this.balance = this.loggedInUser.getBalance();
            this.newStatusMessage = this.statusMessage;
        } catch (ServletException se) {
            context.addMessage(null, new FacesMessage("Login failed"));

            return this.INDEX;
        }
        
        return this.USER_PRODUCT;
    }

    // Proper logout method, not yet implemented
    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try{
            request.logout();
        } catch (ServletException se) {
            context.addMessage(null, new FacesMessage("Login failed"));
        }
        
        return this.INDEX;
    }

    // Method to save changes to user profile
    public String saveChanges(){
        this.statusMessage = this.newStatusMessage;
        this.loggedInUser.setStatusMessage(this.newStatusMessage);
        
        user.updateUser(this.loggedInUser);
        
        // Return editUserProfile page
        return this.EDIT_USER_PROFILE;
    }
    
    // This will return a list of all users
    public List<User> queryAllUsers(){
        return user.getAllUsers();
    }
    
    // This will return the logged in user
    public List<User> getThisUserByName(){
        return user.getUserByName(this.username);
    }
    
    // This will return the logged in user
    public List<User> getThisUserByID(){
        return user.getUserByID(this.id);
    }
    
    // Getter for users
    public List<User> getUsers() {
        return users;
    }

    //  Setter for Users
    public void setUsers() {
        this.users = user.getAllUsers();
    }

    //  Getter for new username
    public String getNewUsername() {
        return newUsername;
    }

    // Setter for new username
    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewStatusMessage() {
        return newStatusMessage;
    }

    public void setNewStatusMessage(String newStatusMessage) {
        this.newStatusMessage = newStatusMessage;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for statusMessage
    public String getStatusMessage() {
        return statusMessage;
    }

    // Setter for statusMessage
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    // Getter for balance
    public double getBalance() {
        return balance;
    }

    // Setter for balance
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    /**
     * Creates a new instance of Profile
     */
    public Profile() {
        this.shoppingList = new ArrayList<Product>();
        this.administrator = true;
        this.quantityOfItem = new HashMap<Integer, Integer>();
        this.adminProducts = new ArrayList<Product>();
    }
    
    private List<Product> shoppingList;
    
    private Boolean administrator;
    
    private Map<Integer, Integer> quantityOfItem;

    private List<Product> adminProducts;
    
    private String searchBy="";
    private String searchName="";
    private int searchPart=0;
    
    private String npName;
    private String npDescription;
    private int npQuantity;
    private double npPrice;
    
    private int sortingOption = 0;
    private boolean sortingDirection = true;
    
    // Constants for navigating to webpages
    private final String userProduct = "userProduct";
    private final String adminProduct = "adminProduct";
    private final String shoppingCart = "shoppingCart";

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }
    
    public boolean isSortingDirection() {
        return sortingDirection;
    }

    public void setSortingDirection(boolean sortingDirection) {
        this.sortingDirection = sortingDirection;
    }
    
    public Map<Integer, Integer> getQuantityOfItem() {
        return quantityOfItem;
    }

    public void setQuantityOfItem(Map<Integer, Integer> quantityOfItem) {
        this.quantityOfItem = quantityOfItem;
    }
    
    @Inject
    UserEJB usr;
    
    /**
     * Updates the product hashmap with any new products and initializes
     * the quantity to 0
     */
    public void updateProducts() {
        List<Product> allProducts = usr.getAllProducts();
        
        for(int i =0; i<allProducts.size(); i++){
            if(!this.quantityOfItem.containsKey(allProducts.get(i).getId())){
                this.quantityOfItem.put(allProducts.get(i).getId(), 1);
            }
        }
        
        this.adminProducts = allProducts;
    }
  
    public List<Product> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Product> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<Product> getAdminProducts() {
        return this.adminProducts;
    }

    /**
     * Gets a list of filtered products if a filter is applied. Otherwise, the
     * list is returned without any filtering
     * @return list of products in they are to be displayed
     */
    public List<Product> getUpdatedProducts() {
        this.updateProducts();
        List<Product> filteredProducts = new ArrayList<Product>();
        
        if(this.searchName == "" && this.searchPart==0){
            this.sortProducts(this.adminProducts,this.sortingOption, this.sortingDirection);
            return this.adminProducts;
        }
        
        System.out.println("Performing a search operation now");
        
        for(int i =0; i<this.adminProducts.size();i++){
            if(this.searchName != "" && this.adminProducts.get(i).getName().toLowerCase().contains(this.searchName.toLowerCase())){
                filteredProducts.add(this.adminProducts.get(i));
            }
            else if(this.searchPart != 0 && this.adminProducts.get(i).getId() == this.searchPart){
                filteredProducts.add(this.adminProducts.get(i));            
            }
        }
        
        this.searchBy="";
        this.sortProducts(filteredProducts, this.sortingOption, this.sortingDirection);
        return filteredProducts;
    }
    
    public void setAdminProducts(List<Product> adminProducts) {
        this.adminProducts = adminProducts;
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    public String getNpName() {
        return npName;
    }

    public void setNpName(String npName) {
        this.npName = npName;
    }

    public String getNpDescription() {
        return npDescription;
    }

    public void setNpDescription(String npDescription) {
        this.npDescription = npDescription;
    }

    public int getNpQuantity() {
        return npQuantity;
    }

    public void setNpQuantity(int npQuantity) {
        this.npQuantity = npQuantity;
    }

    public double getNpPrice() {
        return npPrice;
    }

    public void setNpPrice(double npPrice) {
        this.npPrice = npPrice;
    }

    public int getSortingOption() {
        return sortingOption;
    }

    public void setSortingOption(int sortingOption) {
        this.sortingOption = sortingOption;
    }
      
    /**
     * Adds a new product to the list of products
     * @return adminProduct redirect to the webpage 
     */
    public String addProduct(){
        System.out.println("Adding a new product: " + this.npName);
        Product newProduct = new Product();
        newProduct.setName(npName);
        newProduct.setDescription(npDescription);
        newProduct.setQuantityOnHand(npQuantity);
        newProduct.setPrice(npPrice);
        ad.addProduct(newProduct);
        this.npDescription="";
        this.npName="";
        this.npPrice=0.00;
        this.npQuantity=0;
        this.updateProducts();
        return this.adminProduct;
    }
    
    /**
     * Remove a product from the shopping cart
     * @param p the product being removed
     * @return shoppingCart redirect to webpage
     */
    public String removeFromBasket(Product p){
        this.shoppingList.remove(p);
        return this.shoppingCart;
    }
    
    /**
     * Add a product to the shopping cart(basket)
     * @param p the product being added
     * @return userProduct redirect to webpage
     */
    public String addToBasket(Product p){
        if(this.shoppingList.contains(p)){
            this.shoppingList.remove(p);
        }
        
        System.out.println("Adding " + this.quantityOfItem.get(p.getId()) + " of item to basket: " + p.getName());
        this.shoppingList.add(p);
        return this.userProduct;
    }
    
    @Inject
    AdminEJB ad;
    
    /**
     * Change information for an item that is already in the database
     * @param p product being changed
     * @return adminProduct redirect to webpage
     */
    public String changeItem(Product p) {
        System.out.println("EJB will change: " + p.getName() + " with Id " + p.getId()+ " and description " + p.getDescription());
        ad.updateProduct(p);
        this.updateProducts();
        return this.adminProduct;
    }
     
    /**
     * Remove an item from the database
     * @param p product being removed
     * @return adminProduct redirect to webpage
     */
    public String removeItem(Product p){
        System.out.println("Removing product # " + p.getId() + " - " + p.getName());
        ad.removeProduct(p);
        this.updateProducts();
        return this.adminProduct;
    }
    
    /**
     * Calculate the total value of the shopping cart
     * @return total cost of items
     */
    public double getShoppingTotal(){
        double total = 0.0;
        int quant;
        for (Product p : shoppingList) {
            quant = Integer.parseInt(""+this.quantityOfItem.get(p.getId()));
            total += p.getPrice()*quant;
        }
        return total;
    }
    
    /**
     * Process the order of items in the shopping from the user's profile
     * @return shoppingCart redirect to webpage
     */
    public String processOrder(){
        for (Product p : shoppingList) {
            System.out.println("Sending an order");
            usr.purchaseProduct(p, Integer.parseInt("" + this.quantityOfItem.get(p.getId())), usr.getUserByName(this.getUsername()).get(0));
            
        }
        System.out.println("Finished sending orders");
        shoppingList = new ArrayList<Product>();
        return this.shoppingCart;
    }

    /**
     * Setting the sorting configuration
     * @param ord column to be sorted
     * @param dir direction(ascending or descending)
     * @return userProduct redirect to webpage
     */
    public String sortingOrder(int ord, boolean dir){
        this.sortingOption = ord;
        this.sortingDirection = dir;
        
        return this.userProduct;
    }
    
    /**
     * Reverse a list for descending sorting
     * @param list to be sorted
     * @return sorted list
     */
    public List<Product> reverse(List<Product> list) {
        for(int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }
        
        return list;
}
    /**
     * Sorts the list of products
     * @param sortThis List being sorted
     * @param option controls which column is being sorted
     * @param descending defines sort direction
     */
    public void sortProducts(List<Product> sortThis,int option, boolean descending){
        int negOne = (descending)?(1):(-1);
        int posOne = (descending)?(-1):(1);
        
        switch(option){
                case 0: //Id
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getId() < c2.getId()) return negOne;
                            if (c1.getId() > c2.getId()) return posOne;
                            return 0;
                          }});
                    break;
                case 1: //Name
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            return c1.getName().compareTo(c2.getName());
                          }});
                    if(descending)
                        sortThis = reverse(sortThis);
                    break;
                case 2: //Description?
                    break;
                case 3: //Quantity on Hand
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getQuantityOnHand()< c2.getQuantityOnHand()) return negOne;
                            if (c1.getQuantityOnHand()> c2.getQuantityOnHand()) return posOne;
                            return 0;
                          }});
                    break;
                case 4: //Price
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getPrice() < c2.getPrice()) return negOne;
                            if (c1.getPrice() > c2.getPrice()) return posOne;
                            return 0;
                          }});
                    break;
        }
    }
    
    /**
     * Sets the page to search by the search box string
     * @return userProduct redirect to webpage
     */
    public String searchName(){
        this.searchName=this.searchBy;
        this.searchPart=0;
        System.out.println("Searching for Name: " + this.searchName);
        
        // Return userProduct page
        return this.userProduct;
    }
    
    /**
     * Sets the page to search by the id box
     * @return userProduct redirect to webpage
     */
    public String searchId(){
        this.searchPart=Integer.parseInt(this.searchBy);
        this.searchName="";
        System.out.println("Searching for Id: " + this.searchPart);
        
        // Return userProduct page
        return this.userProduct;
    }
    
    /**
     * Sets the userProduct page to display all the products and ignore search 
     * options
     * @return userProduct redirect to webpage with no search options
     */
    public String browseAllProducts(){
        this.searchPart=0;
        this.searchName="";
        this.searchBy="";
        
        // Return userProduct page
        return this.userProduct+"?faces-redirect=true";
    }
    
    private List<CustomerOrder> orders;

    public List<CustomerOrder> getOrders() {
        return usr.getAllOrders();
    }

    public void setOrders(List<CustomerOrder> orders) {
        this.orders = orders;
    }
    
    
}
