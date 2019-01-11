package hxw.security.api.service.mapper;

import hxw.security.api.domain.Menu;
import hxw.security.api.service.dto.MenuDTO;
import hxw.security.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends EntityMapper<MenuDTO, Menu> {
}
