/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author keita
 */
@Entity
@Table(name = "usergroup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usergroup.findAll", query = "SELECT u FROM Usergroup u")
    , @NamedQuery(name = "Usergroup.findByGroup", query = "SELECT u FROM Usergroup u WHERE u.usergroupPK.group = :group")
    , @NamedQuery(name = "Usergroup.findByUserId", query = "SELECT u FROM Usergroup u WHERE u.usergroupPK.userId = :userId")})
public class Usergroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsergroupPK usergroupPK;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Usergroup() {
    }

    public Usergroup(UsergroupPK usergroupPK) {
        this.usergroupPK = usergroupPK;
    }

    public Usergroup(String group, int userId) {
        this.usergroupPK = new UsergroupPK(group, userId);
    }

    public UsergroupPK getUsergroupPK() {
        return usergroupPK;
    }

    public void setUsergroupPK(UsergroupPK usergroupPK) {
        this.usergroupPK = usergroupPK;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usergroupPK != null ? usergroupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usergroup)) {
            return false;
        }
        Usergroup other = (Usergroup) object;
        if ((this.usergroupPK == null && other.usergroupPK != null) || (this.usergroupPK != null && !this.usergroupPK.equals(other.usergroupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Usergroup[ usergroupPK=" + usergroupPK + " ]";
    }
    
}
