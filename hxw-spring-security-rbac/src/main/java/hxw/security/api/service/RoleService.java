package hxw.security.api.service;

import hxw.security.api.domain.Role;
import hxw.security.api.service.dto.RoleDTO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "role")
public interface RoleService {

    /**
     * get
     */
    @Cacheable(key = "#p0")
    RoleDTO findById(long id);

    /**
     * create
     */
    @CacheEvict(allEntries = true)
    RoleDTO create(Role resources);

    /**
     * update
     */
    @CacheEvict(allEntries = true)
    void update(Role resources);

    /**
     * delete
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * role tree
     */
    @Cacheable(key = "'tree'")
    Object getRoleTree();
}
