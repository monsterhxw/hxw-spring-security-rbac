package hxw.security.api.service;

import hxw.security.api.domain.Permission;
import hxw.security.api.service.dto.PermissionDTO;
import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author jie
 * @date 2018-12-08
 */
@CacheConfig(cacheNames = "permission")
public interface PermissionService {

    /**
     * get
     */
    @Cacheable(key = "#p0")
    PermissionDTO findById(long id);

    /**
     * create
     */
    @CacheEvict(allEntries = true)
    PermissionDTO create(Permission resources);

    /**
     * update
     */
    @CacheEvict(allEntries = true)
    void update(Permission resources);

    /**
     * delete
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * permission tree
     */
    @Cacheable(key = "'tree'")
    Object getPermissionTree(List<Permission> permissions);

    /**
     * findByPid
     */
    @Cacheable(key = "'pid:'+#p0")
    List<Permission> findByPid(long pid);

    /**
     * build Tree
     */
    Object buildTree(List<PermissionDTO> permissionDTOS);
}
