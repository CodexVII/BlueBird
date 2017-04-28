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
import javax.jms.Queue;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alan Noonan, Ian Lodovica, Dylan O'Connor Desmond, Gearoid Cremin, Trevor McSweeney
 *
 * Stores all the session information for the logged in user and performs all
 * the functions for the logic of the web site front-end
 */
@Named(value = "profile")
@SessionScoped

public class Profile implements Serializable {
    // Inject required beans
    
    @Resource(mappedName="jms/__defaultConnectionFactory")
    private QueueConnectionFactory factory;

    @Resource(mappedName="mdb")
    private Queue queue;
  
    QueueConnection connection = null;
    QueueSender sender = null;
    QueueSession session = null;
    
    @Inject
    private UserEJB user;
    
    @Inject
    private AdminEJB admin;
    
    // List variables
    private List<User> users;  // List of all users in datatable
    private List<Product> shoppingList;  // List of products in shopping list
    private List<Product> adminProducts;  // List of products available to admin
    private List<CustomerOrder> orders;  // List of customer orders - TODO not used!!
    
    // Map variable for quantity of items
    private Map<Integer, Integer> quantityOfItem;
    
    // Variables for searching for items
    private String searchProductBy = "";  // Store search box text
    private String searchProductByName = "";  // Current name we are searching by
    private int searchProductByID = 0;  // Current product id we are searching by
    private int sortingOption = 0;  // Current column we wish to sort by
    private boolean sortingDirection = true;  // Direction we're sorting by, true-ascending, false-descending
    
    // Variables for searching for users
    private String searchUserBy = "";
    private String searchUserByName = "";
    private int searchUserByID = 0;
    
    // Variables for administrator to edit products
    private String newProductName = "New Product";  // Stores the name entered for a new product
    private String newProductDescription;  // Stores description entered for a new product
    private int newProductQuantity;  // Store quantity on hand for new product
    private double newProductPrice;  // Stores price for new product

    // List of logged in user
    private User loggedInUser;
    private User viewUser;

    // Fields for the logged in user
    private int id = 0;
    private String username = null;
    private String password = null;
    private String statusMessage = null;
    private double balance  = 0.0;
    private Boolean userIsAdministrator = false;
    private String userStatus = null;
    
    // Fields for the user profile being viewed
    private int viewUserId = 0;
    private String viewUserUsername = null;
    private String viewUserStatusMessage = null;
    private Boolean viewUserIsAdministrator = false;
    private String viewUserStatus = null;
    
    // Variable for new status message for editing profile
    private String newStatusMessage = null;
    
    // Constants for navigating to web pages
    private final String ADMIN_PRODUCT = "adminProduct";
    private final String BROWSE_USERS = "browseUsers";
    private final String EDIT_USER_PROFILE = "editUserProfile";
    private final String LOGIN = "login";
    private final String SHOPPING_CART = "shoppingCart";
    private final String USER_PRODUCT = "userProduct";
    private final String VIEW_PROFILE = "viewProfile";
    
    // Variables for Shopping Order error messages
    private Boolean orderStockErrorDisplay = false;
    private Boolean orderMoneyErrorDisplay = false;
    
    // Secure login method
    /**
     * 
     * @return redirect Redirect to the userProduct web page on successful log
     * in, or the index web page if log in fails
     */
    public String login(){
        String redirect = this.USER_PRODUCT;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try{
            // Login user
            request.login(this.username, this.password);
            
            // Get list of all users
            this.users = user.getAllUsers();
            
            // Get the logged-in user's details
            this.username = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            this.loggedInUser = queryUserByName(this.username).get(0);
            this.id = this.loggedInUser.getId();
            this.statusMessage = this.loggedInUser.getStatusMessage();
            this.balance = this.loggedInUser.getBalance();
            this.newStatusMessage = this.statusMessage;
            this.userIsAdministrator = user.isAdmin(this.loggedInUser);
            this.userStatus = this.userIsAdministrator ? "Admin" : "User";
            
        } catch (ServletException se) {
            context.addMessage(null, new FacesMessage("Login failed"));

            redirect = this.LOGIN;
        }
        
        // Return redirect to the appropriate web page
        return redirect;
    }

    /**
     * Secure logout method
     * @return INDEX Redirect to the index web page
     */
    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try{
            // Logout user
            request.logout();
        } catch (ServletException se) {
            context.addMessage(null, new FacesMessage("Login failed"));
        }
        
        return this.LOGIN;
    }
    
    /**
     * Method to save changes to user profile
     * @return EDIT_USER_PROFILE Redirect to the editUserProfile web page
     */
    public String saveChanges(){
        // Get new status message
        this.statusMessage = this.newStatusMessage;
        this.loggedInUser.setStatusMessage(this.newStatusMessage);
        
        // Update user profile
        user.updateUser(this.loggedInUser);
        
        // Return editUserProfile page
        return this.EDIT_USER_PROFILE;
    }
    
    /**
     * Updates the product hash map with any new products and initializes
     * the quantity to 0
     */
    public void updateProducts() {
        // Create a new list of all the products
        List<Product> allProducts = user.getAllProducts();
        
        // Loop through lists adding new products to hash map
        for(int i =0; i<allProducts.size(); i++){
            if(!this.quantityOfItem.containsKey(allProducts.get(i).getId())){
                this.quantityOfItem.put(allProducts.get(i).getId(), 1);
            }
        }
        
        // Return updated product list
        this.adminProducts = allProducts;
    }
    
    /**
     * Sets the page to search by the product name box
     * @return USER_PRODUCT Redirect to the userProduct web page
     */
    public String searchForProductByName(){
        if("".equals(this.searchProductBy)){
            // Unable to parse, default to browse all products
            browseAllProducts();
        } else {
            // Set the search parammeter for product name to the entered value
            this.searchProductByName = this.searchProductBy;
            
            // Set the search parameter for ID to 0 as we do not require it
            this.searchProductByID = 0;
        }
        
        System.out.println("Searching for Product with Name: " + this.searchProductByName);
        
        // Return userProduct page
        return this.USER_PRODUCT;
    }
    
    /**
     * Sets the page to search by the product id box
     * @return USER_PRODUCT Redirect to the userProduct web page
     */
    public String searchForProductById(){
        try{
            // Set the search parammeter for product Ids to the entered value
            this.searchProductByID = Integer.parseInt(this.searchProductBy);
            
            // Set the search parameter for name to empty as we do not require it
            this.searchProductByName = "";
        } catch (Exception e) {
            // No entry in name field, default to browse all products
            browseAllProducts();
        }
        
        System.out.println("Searching for Product with Id: " + this.searchProductByID);
        
        // Return userProduct page with new search details
        return this.USER_PRODUCT;
    }
    
    /**
     * Sets the userProduct page to display all the products and ignore search 
     * options
     * @return USER_PRODUCT Redirect to the userProduct web page with no search
     * options. Called when the user selects the browse all menu option
     */
    public String browseAllProducts(){
        // Clear all possible search parameters
        this.searchProductByID = 0;
        this.searchProductByName = "";
        this.searchProductBy = "";
        
        // Return userProduct page
        return this.USER_PRODUCT;
    }
    
    /**
     * Gets a list of filtered products if a filter is applied. Otherwise, the
     * list is returned without any filtering
     * @return filteredProducts List of products in the order they are to be displayed
     */
    public List<Product> getUpdatedProducts() {
        // Update the product/quantity hashmap
        this.updateProducts();
        
        // Create a filtered list to contain products after the search
        List<Product> filteredProducts = new ArrayList<Product>();
        
        // If no search parameters are entered return the entire list
        if("".equals(this.searchProductByName) && this.searchProductByID == 0){
            this.sortProducts(this.adminProducts,this.sortingOption, this.sortingDirection);
            
            // Return unfiltered list
            return this.adminProducts;
        }
        
        System.out.println("Performing a product search operation now");
        
        // Begin the process of searching through all the products by the search parameters
        for(int i = 0; i < this.adminProducts.size(); i++){
            // If searchProductByName isn't empty and the product name matches the search name
            if( !"".equals(this.searchProductByName) && this.adminProducts.get(i).getName().toLowerCase().contains(this.searchProductByName.toLowerCase())){
                filteredProducts.add(this.adminProducts.get(i));  // Add to list of filtered products
            }
            // If searchProductByID isn't 0 and the product ID matches the search ID
            else if(this.searchProductByID != 0 && this.adminProducts.get(i).getId() == this.searchProductByID){
                filteredProducts.add(this.adminProducts.get(i));  // Add to list of filtered products           
            }
        }
        
        this.searchProductBy="";  // Reset searchProductBy
        this.sortProducts(filteredProducts, this.sortingOption, this.sortingDirection);  // Sort products
        
        return filteredProducts;
    }
    
    /**
     * Adds a new product to the list of products
     * @return ADMIN_PRODUCT Redirect to the adminProduct web page 
     */
    public String addProduct(){
        System.out.println("Adding a new product: " + this.newProductName);
        
        // Create a new product and add the user entered parameters
        Product newProduct = new Product();
        newProduct.setName(newProductName);
        newProduct.setDescription(newProductDescription);
        newProduct.setQuantityOnHand(newProductQuantity);
        newProduct.setPrice(newProductPrice);
        
        // Call EJB function to add a new product
        admin.addProduct(newProduct);
        
        // Reset user fields to empty
        this.newProductDescription="";
        this.newProductName="New Product";
        this.newProductPrice=0.00;
        this.newProductQuantity=0;
        this.updateProducts();
        //Send logging message to notify
        this.sendMessage("Administrator " + this.username + " adding product: " + newProduct.getName());
        //  Refresh the admin product page
        return this.ADMIN_PRODUCT;
    }
    
    /**
     * Remove a product from the shopping cart
     * @param p the product being removed
     * @return SHOPPING_CART Redirect to the shoppingCart web page
     */
    public String removeFromBasket(Product p){
        this.shoppingList.remove(p);
        this.sendMessage("Administrator " + this.username + " removing product");
        return this.SHOPPING_CART;
    }
    
    /**
     * Add a product to the shopping cart(basket)
     * @param p the product being added
     * @return USER_PRODUCT Redirect to the userProduct web page
     */
    public String addToBasket(Product p){
        if(this.shoppingList.contains(p)){
            this.shoppingList.remove(p);
        }
        
        System.out.println("Adding " + this.quantityOfItem.get(p.getId()) + " of item to basket: " + p.getName());
        this.shoppingList.add(p);
        return this.USER_PRODUCT;
    }

    /**
     * Change information for an item that is already in the database
     * @param p product being changed
     * @return ADMIN_PRODUCT Redirect to the adminProduct web page
     */
    public String changeItem(Product p) {
        System.out.println("EJB will change: " + p.getName() + " with Id " + p.getId()+ " and description " + p.getDescription());
        
        // Get product in row and call ejb merge function
        admin.updateProduct(p);
        
        // Update the list of products to find the changed details
        this.updateProducts();
        
        // Refresh
        return this.ADMIN_PRODUCT;
    }
     
    /**
     * Remove an item from the database
     * @param p product being removed
     * @return ADMIN_PRODUCT Redirect to the adminProduct web page
     */
    public String removeItem(Product p){
        System.out.println("Removing product # " + p.getId() + " - " + p.getName());
        
        // Pass product and call function to remove it
        admin.removeProduct(p);
        
        // Update product list to reflect changes
        this.updateProducts();
        this.sendMessage("Administrator " + this.username + " removing product: " + p.getName());
        // Refresh
        return this.ADMIN_PRODUCT; //refresh
    }
    
    /**
     * Go to the shopping cart page and refresh without the error message
     * @return SHOPPING_CART Redirect to the shoppingCart web page
     */
    public String goToCart(){
        orderStockErrorDisplay = false;
        return this.SHOPPING_CART; //refresh
    }
    
    /**
     * Calculate the total value of the shopping cart
     * @return total Total cost of items
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
     * @return SHOPPING_CART Redirect to the shoppingCart web page
     */
    public String processOrder(){
        this.orderStockErrorDisplay = false;
        this.orderMoneyErrorDisplay = false;
        
        // Check if the required amount of product are in stock
        for (Product p : shoppingList) {
            if (p.getQuantityOnHand() < Integer.parseInt("" + this.quantityOfItem.get(p.getId()))){
                System.out.println("Order is impossible");
                
                // Set error flag
                this.orderStockErrorDisplay = true;
            }
        }
        
        // Total cost of items
        double totalCost = 0.0;
        
        // Check if User can afford all items in shopping cart
        for (Product p : shoppingList) {
            int amount = Integer.parseInt("" + this.quantityOfItem.get(p.getId()));
            totalCost += amount * p.getPrice();
        }
        
        // If not, set error flag
        if (totalCost > this.loggedInUser.getBalance()){
            this.orderMoneyErrorDisplay = true;
        }
        
        // If User can check out shopping cart, proceed
        if (this.orderStockErrorDisplay == false && this.orderMoneyErrorDisplay == false) {
            for (Product p : shoppingList) {            
                    System.out.println("Sending an order");
                    user.purchaseProduct(p, Integer.parseInt("" + this.quantityOfItem.get(p.getId())), user.getUserByName(this.getUsername()).get(0));            
                    this.sendMessage("Order from " + this.username + " for item " + p.getName());
            }
            
            System.out.println("Finished sending orders");
            shoppingList = new ArrayList<Product>();
            
            // Get updated User object and update local balance variable
            this.loggedInUser = queryUserByName(this.username).get(0);
            this.balance = this.loggedInUser.getBalance();  
        } 

        // Return shoppingCart page
        return this.SHOPPING_CART;
    }
    
    /**
     * Cancel the order of items by clearing the shopping cart
     * @return SHOPPING_CART Redirect to the shoppingCart web page
     */
    public String cancelOrder(){
        this.orderMoneyErrorDisplay = false;
        this.orderStockErrorDisplay = false;
        System.out.println("Clearing the shopping list");
        shoppingList = new ArrayList<Product>();
        this.sendMessage("Cancelling order from " + this.username);
        return this.SHOPPING_CART;
    }

    /**
     * Setting the sorting configuration
     * @param ord column to be sorted
     * @param dir direction(ascending or descending)
     * @return USER_PRODUCT Redirect to the userProduct web page
     */
    public String sortingOrder(int ord, boolean dir){
        // Set the sorting order to given options
        this.sortingOption = ord;
        this.sortingDirection = dir;
        
        // Refresh page and sorting will be implemented on page load
        return this.USER_PRODUCT;
    }
    
    /**
     * Reverse a list for descending sorting
     * @param list to be sorted
     * @return list Sorted list
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
    public void sortProducts(List<Product> sortThis, int option, boolean descending){
        // Invert positive and negative return values in order to 
        // allow this function to sort descending or ascending
        // i.e. when negOne =1 and posOne=-1, items will be sorted in
        // descending order.
        int negOne = (descending)?(1):(-1);
        int posOne = (descending)?(-1):(1);
        
        // Switch case for the column by which we wish to sort
        switch(option){
                case 0:  // Sort by the Id number
                    // Create a new comparator to sort by Id
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getId() < c2.getId()) return negOne;
                            if (c1.getId() > c2.getId()) return posOne;
                            return 0;
                          }});
                    break;
                case 1: // Sort by Name
                    // Create a string comparator
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            return c1.getName().compareTo(c2.getName());
                          }});
                    if(descending)
                        sortThis = reverse(sortThis);
                    break;
                case 2:  // No need to sort by description, legacy code
                    break;
                case 3:  // Sort by  Quantity on Hand
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getQuantityOnHand()< c2.getQuantityOnHand()) return negOne;
                            if (c1.getQuantityOnHand()> c2.getQuantityOnHand()) return posOne;
                            return 0;
                          }});
                    break;
                case 4:  // Sort by Price
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
     * Sets the page to search by the user name box
     * @return 
     */
    public String searchForUserByName(){
        if("".equals(this.searchUserBy)){
            // No entry in name field, default to search for all Users
            searchForAllUsers();
        } else {
            // Set the search parammeter for User name to the entered value
            this.searchUserByName = this.searchUserBy;
            
            // Set the search parameter for User ID to 0 as we do not require it
            this.searchUserByID = 0;
        }
        
        System.out.println("Searching for User with Name: " + this.searchUserByName);
        
        // Return browseUser page
        return this.BROWSE_USERS;
    }
    
    /**
     * Sets the page to search by the user id box
     * @return 
     */
    public String searchForUserById(){
        try{
            // Set the search parammeter for User name to empty as we do not require it
            this.searchUserByName = "";
            
            // Set the search parameter for User ID to the entered value
            this.searchUserByID = Integer.parseInt(this.searchUserBy);
        } catch (Exception e) {
            // Unable to parse, default to search for all users
            searchForAllUsers();
        }

        System.out.println("Searching for User with Id: " + this.searchUserByID);
        
        // Return browseUser page
        return this.BROWSE_USERS;
    }
    
    /**
     * Sets the browseUser page to display all the users and ignore search 
     * options
     * @return BROWSE_USERS Redirect to the browseUser web page with no search
     * options
     */
    public String searchForAllUsers(){
        this.searchUserBy = "";
        this.searchUserByName = "";
        this.searchUserByID = 0;
        
        // Return browseUser page
        return this.BROWSE_USERS;
    }
    
    /**
     * Gets a list of users that match the search parameters
     * @return searchResults List of users that match the search results
     */
    public List<User> getUpdatedUserSearch() {
        List<User> searchResults = new ArrayList<User>();
        
        if("".equals(this.searchUserByName) && this.searchUserByID == 0){
            searchResults = this.user.getAllUsers();
        }
        else {
            for(int i =0 ; i < this.users.size(); i++){
                if( !"".equals(this.searchUserByName) && this.users.get(i).getUsername().toLowerCase().contains(this.searchUserByName.toLowerCase())){
                    searchResults.add(this.users.get(i));
                }
                else if(this.searchUserByID != 0 && this.users.get(i).getId() == this.searchUserByID){
                    searchResults.add(this.users.get(i));          
                }
            }
        }
        
        this.searchUserBy = "";
        
        // Return search results
        return searchResults;
    }
    
    /**
     * View another User's profile
     * @param u The User profile to be viewed
     * @return redirect Redirect to the viewProfile web page
     */
    public String viewOtherProfile(User u){
        String redirect = this.VIEW_PROFILE;
        
        this.viewUser = queryUserByName(u.getUsername()).get(0);
        this.viewUserId = this.viewUser.getId();
        this.viewUserUsername = this.viewUser.getUsername();
        this.viewUserStatusMessage = this.viewUser.getStatusMessage();
        this.viewUserIsAdministrator = user.isAdmin(u);
        this.viewUserStatus = this.viewUserIsAdministrator ? "Admin" : "User";

        // Return redirect
        return redirect;
    }
    
    /**
     * Gets the logged-in user by name
     * @return userByName The logged-in user
     */
    public List<User> queryUserByName(String name){
        List<User> userByName = user.getUserByName(name);
        
        return userByName;
    }
    
    /**
     * Gets the logged-in user by ID
     * @return user.getUserByID(this.id): The logged-in user
     */
    public List<User> queryUserByID(){
        List<User> userByID = user.getUserByID(this.id);
        
        return userByID;
    }
    
    /**
     * All getter and setter methods
     */
    
    // Getter for users
    public List<User> getUsers() {
        return users;
    }

    //  Setter for sers
    public void setUsers() {
        this.users = user.getAllUsers();
    }

    // Getter for newStatusMessage
    public String getNewStatusMessage() {
        return newStatusMessage;
    }

    // Setter for newStatusMessage
    public void setNewStatusMessage(String newStatusMessage) {
        this.newStatusMessage = newStatusMessage;
    }

    // Getter for OrderStockErrorDisplay
    public Boolean getOrderStockErrorDisplay() {
        return orderStockErrorDisplay;
    }

    // Setter for OrderStockErrorDisplay
    public void setOrderStockErrorDisplay(Boolean orderStockErrorDisplay) {
        this.orderStockErrorDisplay = orderStockErrorDisplay;
    }

    // Getter for OrderMoneyErrorDisplay
    public Boolean getOrderMoneyErrorDisplay() {
        return orderMoneyErrorDisplay;
    }

    // Setter for OrderMoneyErrorDisplay
    public void setOrderMoneyErrorDisplay(Boolean orderMoneyErrorDisplay) {
        this.orderMoneyErrorDisplay = orderMoneyErrorDisplay;
    }
    
    
    
    // Getter for loggedInUser
    public User getLoggedInUser() {
        return loggedInUser;
    }

    // Setter for loggedInUser
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
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
    
    // Getter for userIsAdministrator
    public Boolean getUserIsAdministrator() {
        return userIsAdministrator;
    }

    // Setter for userIsAdministrator
    public void setUserIsAdministrator(Boolean userIsAdministrator) {
        this.userIsAdministrator = userIsAdministrator;
    }

    // Getter for userStatus
    public String getUserStatus() {
        return userStatus;
    }

    // Getter for userStatus
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    // Getter for viewUser
    public User getViewUser() {
        return viewUser;
    }

    // Setter for viewUser
    public void setViewUser(User viewUser) {
        this.viewUser = viewUser;
    }

    // Getter for viewUserId
    public int getViewUserId() {
        return viewUserId;
    }

    // Setter for viewUserId
    public void setViewUserId(int viewUserId) {
        this.viewUserId = viewUserId;
    }

    // Getter for viewUserUsername
    public String getViewUserUsername() {
        return viewUserUsername;
    }

    // Setter for viewUserUsername
    public void setViewUserUsername(String viewUserUsername) {
        this.viewUserUsername = viewUserUsername;
    }

    // Getter for viewUserStatusMessage
    public String getViewUserStatusMessage() {
        return viewUserStatusMessage;
    }

    // Setter for viewUserStatusMessage
    public void setViewUserStatusMessage(String viewUserStatusMessage) {
        this.viewUserStatusMessage = viewUserStatusMessage;
    }

    // Getter for viewUserIsAdministrator
    public Boolean getViewUserIsAdministrator() {
        return viewUserIsAdministrator;
    }

    // Setter for viewUserIsAdministrator
    public void setViewUserIsAdministrator(Boolean viewUserIsAdministrator) {
        this.viewUserIsAdministrator = viewUserIsAdministrator;
    }

    // Getter for viewUserStatus
    public String getViewUserStatus() {
        return viewUserStatus;
    }

    // Setter for viewUserStaus
    public void setViewUserStatus(String viewUserStatus) {
        this.viewUserStatus = viewUserStatus;
    }
    
    // Getter for sortingDirection
    public boolean isSortingDirection() {
        return sortingDirection;
    }

    // Setter for sortingDirection
    public void setSortingDirection(boolean sortingDirection) {
        this.sortingDirection = sortingDirection;
    }

    // Getter for searchProductBy
    public String getSearchProductBy() {
        return searchProductBy;
    }

    // Setter for searchProductBy
    public void setSearchProductBy(String searchProductBy) {
        this.searchProductBy = searchProductBy;
    }

    // Getter for searchProductByName
    public String getSearchProductByName() {
        return searchProductByName;
    }

    // Setter for searchProductByName
    public void setSearchProductByName(String searchProductByName) {
        this.searchProductByName = searchProductByName;
    }

    // Getter for searchProductByID
    public int getSearchProductByID() {
        return searchProductByID;
    }

    // Setter for searchProductByID
    public void setSearchProductByID(int searchProductByID) {
        this.searchProductByID = searchProductByID;
    }

    // Getter for searchUserBy
    public String getSearchUserBy() {
        return searchUserBy;
    }

    // Setter for searchUserBy
    public void setSearchUserBy(String searchUserBy) {
        this.searchUserBy = searchUserBy;
    }

    // Getter for searchUserByName
    public String getSearchUserByName() {
        return searchUserByName;
    }

    // Setter for searchUserByName
    public void setSearchUserByName(String searchUserByName) {
        this.searchUserByName = searchUserByName;
    }

    // Getter for searchUserByID
    public int getSearchUserByID() {
        return searchUserByID;
    }

    // Setter for searchUserByID
    public void setSearchUserByID(int searchUserByID) {
        this.searchUserByID = searchUserByID;
    }
    
    // Getter for quantityOfItem
    public Map<Integer, Integer> getQuantityOfItem() {
        return quantityOfItem;
    }

    // Getter for quantityOfItem
    public void setQuantityOfItem(Map<Integer, Integer> quantityOfItem) {
        this.quantityOfItem = quantityOfItem;
    }
    
    // Getter for shoppingList
    public List<Product> getShoppingList() {
        return shoppingList;
    }

    // Setter for shoppingList
    public void setShoppingList(List<Product> shoppingList) {
        this.shoppingList = shoppingList;
    }

    // Getter for adminProducts
    public List<Product> getAdminProducts() {
        return this.adminProducts;
    }

    // Setter for adminProducts
    public void setAdminProducts(List<Product> adminProducts) {
        this.adminProducts = adminProducts;
    }
    
    // Getter for newProductName
    public String getNewProductName() {
        return newProductName;
    }

    // Setter for newProductName
    public void setNewProductName(String newProductName) {
        this.newProductName = newProductName;
    }

    // Getter for newProductDescription
    public String getNewProductDescription() {
        return newProductDescription;
    }

    // Setter for newProductDescription
    public void setNewProductDescription(String newProductDescription) {
        this.newProductDescription = newProductDescription;
    }

    // Getter for newProductQuantity
    public int getNewProductQuantity() {
        return newProductQuantity;
    }

    // Setter for newProductQuantity
    public void setNewProductQuantity(int newProductQuantity) {
        this.newProductQuantity = newProductQuantity;
    }

    // Getter for newProductPrice
    public double getNewProductPrice() {
        return newProductPrice;
    }

    // Setter for newProductPrice
    public void setNewProductPrice(double newProductPrice) {
        this.newProductPrice = newProductPrice;
    }    

    // Getter for sortingOption
    public int getSortingOption() {
        return sortingOption;
    }

    // Setter for sortingOption
    public void setSortingOption(int sortingOption) {
        this.sortingOption = sortingOption;
    }

    // Getter for orders
    public List<CustomerOrder> getOrders() {
        this.orders = new ArrayList<CustomerOrder>();
        List<CustomerOrder> ord =  user.getAllOrders();
        
        for(int i =0; i < ord.size();i++){
            if(ord.get(i).getCustomerId().getId() == this.loggedInUser.getId()){
                this.orders.add(ord.get(i));
            }
        }
        
        return this.orders;
    }

    // Setter for orders
    public void setOrders(List<CustomerOrder> orders) {
        this.orders = orders;
    }
    
    /**
     * Creates a new instance of Profile
     */
    public Profile() {
        // Initialise lists and hashmaps required to hold variables
        this.shoppingList = new ArrayList<Product>();
        this.userIsAdministrator = false;
        this.quantityOfItem = new HashMap<Integer, Integer>();
        this.adminProducts = new ArrayList<Product>();
        
    }
    
    public void sendMessage(String message){
        // Initialise the JMS Connection and send the message to the JMS Queue
        try{
            connection = factory.createQueueConnection();
            session = connection.createQueueSession(false, 
            QueueSession.AUTO_ACKNOWLEDGE);
            sender = session.createSender(queue);
            //create and set a message to send
            TextMessage msg = session.createTextMessage();
            msg.setStringProperty("log", message);
            sender.send(msg);
            System.out.println("Sending message"); 
            session.close ();
        }catch (Exception idontcare){
            System.out.println("Error sending mdb from client: " + idontcare.toString() );
        }

    }
}
