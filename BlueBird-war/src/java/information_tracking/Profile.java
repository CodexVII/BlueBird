/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package information_tracking;

import ejb.AdminEJB;
import ejb.UserEJB;
import entity.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
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
    
    // This will verify the user login
    public String verifyLogin(){
        this.users = user.getAllUsers();
        String result = "index";
        System.out.printf("\n\nSize of users = %d\n\n", users.size());

        for(int i = 0; i < users.size(); i++){
            if(this.users.get(i).getUsername().equals(this.username) && this.users.get(i).getPassword().equals(this.password)){
                this.id = this.users.get(i).getId();
                this.statusMessage = this.users.get(i).getStatusMessage();
                this.balance = this.users.get(i).getBalance();
                
                // Copy current fields into new fields
                this.newUsername = this.username;
                this.newPassword = this.password;
                this.newStatusMessage = this.statusMessage;

                // Return userProduct page
                result = "userProduct";
            }
        }
        
        return result;
    }
    
    // Login method
    public String login(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try{
            request.login(this.username, this.password);
        } catch (ServletException se) {
            context.addMessage(null, new FacesMessage("Login failed"));
            return "index";
        }
        
        return "userProduct";
    }
    
    // Logout method
    public void logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try{
            request.login(this.username, this.password);
        } catch (ServletException se) {
            context.addMessage(null, new FacesMessage("Login failed"));
        }
    }
    
    // Method to save changes to user profile
    public String saveChanges(){
        this.username = this.newUsername;
        this.password = this.newPassword;
        this.statusMessage = this.newStatusMessage;
        
        
        
        return "editUserProfile";
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

    public void setUsers() {
        this.users = user.getAllUsers();
    }

    public String getNewUsername() {
        return newUsername;
    }

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
        setUsers();
        
    }
    
    
    
}
