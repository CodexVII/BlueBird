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
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author Dylan
 */
@Named(value = "profile")
@SessionScoped
public class Profile implements Serializable {

    private List<User> userList;

    @Inject
    UserEJB usr;
    /**
     * Creates a new instance of User
     */
    public Profile() {
        userList = usr.getAllUsers();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    
    
    
}
