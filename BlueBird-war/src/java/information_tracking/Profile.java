/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package information_tracking;

import ejb.UserEJB;
import entity.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author Dylan
 */
@Named(value = "profile")
@SessionScoped
public class Profile implements Serializable {
    @Inject
    private UserEJB user;
    
    List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = user.getAllUsers();
    }

    public void queryAllUsers(){
        users = user.getAllUsers();
    }
    
    /**
     * Creates a new instance of Profile
     */
    public Profile() {
    }
    
}
