package hxw.security.api.service.impl;

import cn.hutool.core.util.StrUtil;
import hxw.security.api.domain.Menu;
import hxw.security.api.domain.Role;
import hxw.security.api.domain.vo.MenuMetaVo;
import hxw.security.api.domain.vo.MenuVo;
import hxw.security.api.repository.MenuRepository;
import hxw.security.api.service.MenuService;
import hxw.security.api.service.dto.MenuDTO;
import hxw.security.api.service.mapper.MenuMapper;
import hxw.security.common.exception.BadRequestException;
import hxw.security.common.exception.EntityExistException;
import hxw.security.common.utils.ValidationUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public MenuDTO findById(long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        ValidationUtil.isNull(menu, "Menu", "id", id);
        return menuMapper.toDto(menu.get());
    }

    @Override
    public List<MenuDTO> findByRoles(Set<Role> roles) {
        Set<Menu> menus = new LinkedHashSet<>();
        for (Role role : roles) {
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            menus.addAll(menuRepository.findByRolesOrderBySort(roleSet));
        }
        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MenuDTO create(Menu resources) {
        if (menuRepository.findByName(resources.getName()) != null) {
            throw new EntityExistException(Menu.class, "name", resources.getName());
        }
        if (resources.getIFrame()) {
            if (!(resources.getPath().toLowerCase().startsWith("http://") || resources.getPath().toLowerCase().startsWith("https://"))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        return menuMapper.toDto(menuRepository.save(resources));
    }

    @Override
    public void update(Menu resources) {
        Optional<Menu> optionalPermission = menuRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalPermission, "Permission", "id", resources.getId());

        if (resources.getIFrame()) {
            if (!(resources.getPath().toLowerCase().startsWith("http://") || resources.getPath().toLowerCase().startsWith("https://"))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        Menu menu = optionalPermission.get();
        Menu menu1 = menuRepository.findByName(resources.getName());

        if (menu1 != null && !menu1.getId().equals(menu.getId())) {
            throw new EntityExistException(Menu.class, "name", resources.getName());
        }
        menu.setName(resources.getName());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setSort(resources.getSort());
        menu.setRoles(resources.getRoles());
        menuRepository.save(menu);
    }

    @Override
    public void delete(Long id) {
        List<Menu> menuList = menuRepository.findByPid(id);
        for (Menu menu : menuList) {
            menuRepository.delete(menu);
        }
        menuRepository.deleteById(id);
    }

    @Override
    public Object getMenuTree(List<Menu> menus) {
        List<Map<String, Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
                if (menu != null) {
                    List<Menu> menuList = menuRepository.findByPid(menu.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", menu.getId());
                    map.put("label", menu.getName());
                    if (menuList != null && menuList.size() != 0) {
                        map.put("children", getMenuTree(menuList));
                    }
                    list.add(map);
                }
            }
        );
        return list;
    }

    @Override
    public List<Menu> findByPid(long pid) {
        return menuRepository.findByPid(pid);
    }

    @Override
    public Map buildTree(List<MenuDTO> menuDTOS) {
        List<MenuDTO> trees = new ArrayList<MenuDTO>();

        for (MenuDTO menuDTO : menuDTOS) {

            if ("0".equals(menuDTO.getPid().toString())) {
                trees.add(menuDTO);
            }

            for (MenuDTO it : menuDTOS) {
                if (it.getPid().equals(menuDTO.getId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<MenuDTO>());
                    }
                    menuDTO.getChildren().add(it);
                }
            }
        }

        Integer totalElements = menuDTOS != null ? menuDTOS.size() : 0;
        Map map = new HashMap();
        map.put("content", trees.size() == 0 ? menuDTOS : trees);
        map.put("totalElements", totalElements);
        return map;
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDTO> menuDTOS) {
        List<MenuVo> list = new LinkedList<>();
        menuDTOS.forEach(menuDTO -> {
                if (menuDTO != null) {
                    List<MenuDTO> menuDTOList = menuDTO.getChildren();
                    MenuVo menuVo = new MenuVo();
                    menuVo.setName(menuDTO.getName());
                    menuVo.setPath(menuDTO.getPath());

                    // 如果不是外链
                    if (!menuDTO.getIFrame()) {
                        if (menuDTO.getPid().equals(0L)) {
                            //一级目录需要加斜杠，不然访问不了
                            menuVo.setPath("/" + menuDTO.getPath());
                            menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                        } else if (!StrUtil.isEmpty(menuDTO.getComponent())) {
                            menuVo.setComponent(menuDTO.getComponent());
                        }
                    }
                    menuVo.setMeta(new MenuMetaVo(menuDTO.getName(), menuDTO.getIcon()));
                    if (menuDTOList != null && menuDTOList.size() != 0) {
                        menuVo.setAlwaysShow(true);
                        menuVo.setRedirect("noredirect");
                        menuVo.setChildren(buildMenus(menuDTOList));
                        // 处理是一级菜单并且没有子菜单的情况
                    } else if (menuDTO.getPid().equals(0L)) {
                        MenuVo menuVo1 = new MenuVo();
                        menuVo1.setMeta(menuVo.getMeta());
                        // 非外链
                        if (!menuDTO.getIFrame()) {
                            menuVo1.setPath("index");
                            menuVo1.setName(menuVo.getName());
                            menuVo1.setComponent(menuVo.getComponent());
                        } else {
                            menuVo1.setPath(menuDTO.getPath());
                        }
                        menuVo.setName(null);
                        menuVo.setMeta(null);
                        menuVo.setComponent("Layout");
                        List<MenuVo> list1 = new ArrayList<MenuVo>();
                        list1.add(menuVo1);
                        menuVo.setChildren(list1);
                    }
                    list.add(menuVo);
                }
            }
        );
        return list;
    }
}
