package hxw.security.api.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String avatar;

    private String email;

    private Boolean enabled;

    @JsonIgnore
    private String password;

    private Timestamp createTime;

    private Date lastPasswordResetTime;

    private Set<RoleDTO> roles;

}
