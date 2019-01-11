package hxw.security.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 2346494407367548699L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotBlank
    @Column
    private String name;

    /**
     * 上级类目
     */
    @NotNull
    @Column(name = "pid", nullable = false)
    private Long pid;

    @NotBlank
    @Column
    private String alias;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    @CreationTimestamp
    @Column
    private Timestamp createTime;

    @Override
    public String toString() {
        return "Permission{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", pid=" + pid +
            ", alias='" + alias + '\'' +
            ", createTime=" + createTime +
            '}';
    }

}
