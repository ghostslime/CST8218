/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cst8218.onei0212.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.security.enterprise.identitystore.PasswordHash;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author denge
 */
@Entity
@Table(name = "APPUSER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppUser.findAll", query = "SELECT c FROM AppUser c"),
    @NamedQuery(name = "AppUser.findByUserId", query = "SELECT c FROM AppUser c WHERE c.userid = :userid"),
    @NamedQuery(name = "AppUser.findByPassword", query = "SELECT c FROM AppUser c WHERE c.password = :password"),
    @NamedQuery(name = "AppUser.findByGroupName", query = "SELECT c FROM AppUser c WHERE c.groupname = :groupname")})
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USERID")
    private String userid;
    
    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "GROUPNAME")
    private String groupname;

    public AppUser() {
    }

    public AppUser(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return "";
    }

    public void setPassword(String password) {
        String hashedPassword = null;
        Instance<? extends PasswordHash> instance = CDI.current()
                .select(Pbkdf2PasswordHash.class);
        
        PasswordHash passwordHasher = instance.get();
        passwordHasher.initialize(new HashMap<String, String>());
        hashedPassword = passwordHasher.generate(password.toCharArray());
        
        this.password = hashedPassword;
    }

    public String getGroupName() {
        return groupname;
    }

    public void setGroupName(String groupname) {
        this.groupname = groupname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userid fields are not set
        if (!(object instanceof AppUser)) {
            return false;
        }
        AppUser other = (AppUser) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.lab4.AppUser[ userid=" + userid + " ]";
    }
    
}
