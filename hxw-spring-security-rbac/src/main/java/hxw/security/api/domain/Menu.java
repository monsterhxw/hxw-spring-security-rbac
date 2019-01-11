package hxw.security.api.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 2589659778220227155L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotBlank
    @Column
    private String name;

    @Column(unique = true)

    private Long sort;

    @Column(name = "path")
    private String path;

    @Column
    private String component;

    @Column
    private String icon;

    /**
     * 上级菜单ID
     */
    @Column(name = "pid", nullable = false)
    private Long pid;

    /**
     * 是否为外链 true/false
     */
    private Boolean iFrame;

    @ManyToMany
    @JoinTable(name = "menus_roles", joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @CreationTimestamp
    private Timestamp createTime;
}
