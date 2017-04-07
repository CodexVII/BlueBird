/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author keita
 */
@Embeddable
public class UsergroupPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "group")
    private String group;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;

    public UsergroupPK() {
    }

    public UsergroupPK(String group, int userId) {
        this.group = group;
        this.userId = userId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (group != null ? group.hashCode() : 0);
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsergroupPK)) {
            return false;
        }
        UsergroupPK other = (UsergroupPK) object;
        if ((this.group == null && other.group != null) || (this.group != null && !this.group.equals(other.group))) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UsergroupPK[ group=" + group + ", userId=" + userId + " ]";
    }
    
}
