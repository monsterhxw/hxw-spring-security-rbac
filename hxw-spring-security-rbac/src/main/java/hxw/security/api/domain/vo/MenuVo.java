package hxw.security.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

/**
 * 构建前端路由时用到
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo {

    private String name;

    private String path;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MenuMetaVo meta;

    private List<MenuVo> children;
}
