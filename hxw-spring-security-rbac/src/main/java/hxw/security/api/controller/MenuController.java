package hxw.security.api.controller;

import hxw.security.api.domain.Menu;
import hxw.security.api.domain.User;
import hxw.security.api.service.MenuService;
import hxw.security.api.service.UserService;
import hxw.security.api.service.dto.MenuDTO;
import hxw.security.api.service.query.MenuQueryService;
import hxw.security.common.exception.BadRequestException;
import hxw.security.core.utils.JwtTokenUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class MenuController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuQueryService menuQueryService;

    @Autowired
    private UserService userService;

    private static final String ENTITY_NAME = "menu";

    @GetMapping(value = "/menus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT')")
    public ResponseEntity getMenus(@PathVariable Long id) {
        return new ResponseEntity(menuService.findById(id), HttpStatus.OK);
    }

    /**
     * 构建前端路由所需要的菜单
     */
    @GetMapping(value = "/menus/build")
    public ResponseEntity buildMenus(HttpServletRequest request) {
        User user = userService.findByName(jwtTokenUtil.getUserName(request));
        List<MenuDTO> menuDTOList = menuService.findByRoles(user.getRoles());
        return new ResponseEntity(menuService.buildMenus((List<MenuDTO>) menuService.buildTree(menuDTOList).get("content")), HttpStatus.OK);
    }

    /**
     * 返回全部的菜单
     */
    @GetMapping(value = "/menus/tree")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT')")
    public ResponseEntity getMenuTree() {
        return new ResponseEntity(menuService.getMenuTree(menuService.findByPid(0L)), HttpStatus.OK);
    }

    //    @Log(description = "查询菜单")
    @GetMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT')")
    public ResponseEntity getMenus(@RequestParam(required = false) String name) {
        List<MenuDTO> menuDTOList = menuQueryService.queryAll(name);
        return new ResponseEntity(menuService.buildTree(menuDTOList), HttpStatus.OK);
    }

    //    @Log(description = "新增菜单")
    @PostMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Menu resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity(menuService.create(resources), HttpStatus.CREATED);
    }

    //    @Log(description = "修改菜单")
    @PutMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Menu resources) {
        if (resources.getId() == null) {
            throw new BadRequestException(ENTITY_NAME + " ID Can not be empty");
        }
        menuService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //    @Log(description = "删除菜单")
    @DeleteMapping(value = "/menus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        menuService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
