package hxw.security.api.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

@Data
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 2752468336833585375L;

    private Long id;

    private String name;

    private Long pid;

    private String alias;

    private Timestamp createTime;

    private List<PermissionDTO> children;

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
