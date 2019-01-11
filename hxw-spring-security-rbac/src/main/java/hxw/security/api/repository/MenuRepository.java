package hxw.security.api.repository;

import hxw.security.api.domain.Menu;
import hxw.security.api.domain.Role;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor {

    /**
     * findByName
     * @param name
     * @return
     */
    Menu findByName(String name);

    /**
     * findByRoles
     * @param roleSet
     * @return
     */
    Set<Menu> findByRolesOrderBySort(Set<Role> roleSet);

    /**
     * findByPid
     * @param pid
     * @return
     */
    List<Menu> findByPid(long pid);
}
