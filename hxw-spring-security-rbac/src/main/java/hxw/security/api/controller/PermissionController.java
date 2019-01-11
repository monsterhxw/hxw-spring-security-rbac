package hxw.security.api.controller;

import hxw.security.api.domain.Permission;
import hxw.security.api.service.PermissionService;
import hxw.security.api.service.dto.PermissionDTO;
import hxw.security.api.service.query.PermissionQueryService;
import hxw.security.common.exception.BadRequestException;
import java.util.List;
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
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionQueryService permissionQueryService;

    private static final String ENTITY_NAME = "permission";

    @GetMapping(value = "/permissions/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_SELECT')")
    public ResponseEntity getPermissions(@PathVariable Long id) {
        return new ResponseEntity(permissionService.findById(id), HttpStatus.OK);
    }

    /**
     * 返回全部的权限，新增角色时下拉选择
     */
    @GetMapping(value = "/permissions/tree")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_SELECT','ROLES_SELECT','ROLES_ALL')")
    public ResponseEntity getRoleTree() {
        return new ResponseEntity(permissionService.getPermissionTree(permissionService.findByPid(0L)), HttpStatus.OK);
    }

//    @Log(description = "查询权限")
    @GetMapping(value = "/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_SELECT')")
    public ResponseEntity getPermissions(@RequestParam(required = false) String name) {
        List<PermissionDTO> permissionDTOS = permissionQueryService.queryAll(name);
        return new ResponseEntity(permissionService.buildTree(permissionDTOS), HttpStatus.OK);
    }

//    @Log(description = "新增权限")
    @PostMapping(value = "/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Permission resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity(permissionService.create(resources), HttpStatus.CREATED);
    }

//    @Log(description = "修改权限")
    @PutMapping(value = "/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Permission resources) {
        if (resources.getId() == null) {
            throw new BadRequestException(ENTITY_NAME + " ID Can not be empty");
        }
        permissionService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @Log(description = "删除权限")
    @DeleteMapping(value = "/permissions/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        permissionService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
