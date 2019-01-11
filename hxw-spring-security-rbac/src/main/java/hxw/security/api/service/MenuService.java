package hxw.security.api.service;

import hxw.security.api.domain.Menu;
import hxw.security.api.domain.Role;
import hxw.security.api.service.dto.MenuDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "menu")
public interface MenuService {

    /**
     * get
     */
    @Cacheable(key = "#p0")
    MenuDTO findById(long id);

    /**
     * create
     */
    @CacheEvict(allEntries = true)
    MenuDTO create(Menu resources);

    /**
     * update
     */
    @CacheEvict(allEntries = true)
    void update(Menu resources);

    /**
     * delete
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * permission tree
     */
    @Cacheable(key = "'tree'")
    Object getMenuTree(List<Menu> menus);

    /**
     * findByPid
     */
    @Cacheable(key = "'pid:'+#p0")
    List<Menu> findByPid(long pid);

    /**
     * build Tree
     */
    Map buildTree(List<MenuDTO> menuDTOS);

    /**
     * findByRoles
     */
    List<MenuDTO> findByRoles(Set<Role> roles);

    /**
     * buildMenus
     */
    Object buildMenus(List<MenuDTO> byRoles);
}
