package hxw.security.api.service.mapper;

import hxw.security.api.domain.Role;
import hxw.security.api.service.dto.RoleDTO;
import hxw.security.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

}
