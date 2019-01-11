package hxw.security.api.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = -8517628292077801968L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String username;

    @Column
    private String avatar;

    @NotBlank
    @Pattern(regexp = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}", message = "格式错误")
    @Column
    private String email;

    @NotNull
    @Column
    private Boolean enabled;

    @Column
    private String password;

    @CreationTimestamp
    @Column
    private Timestamp createTime;

    @Column
    private Date lastPasswordResetTime;

    @ManyToMany
    @JoinTable(
        name = "users_roles", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", avatar='" + avatar + '\'' +
            ", email='" + email + '\'' +
            ", enabled=" + enabled +
            ", password='" + password + '\'' +
            ", createTime=" + createTime +
            ", lastPasswordResetTime=" + lastPasswordResetTime +
            '}';
    }
}
