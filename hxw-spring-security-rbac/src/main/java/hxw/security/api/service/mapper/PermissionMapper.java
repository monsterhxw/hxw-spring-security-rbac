package hxw.security.api.service.mapper;

import hxw.security.api.domain.Permission;
import hxw.security.api.service.dto.PermissionDTO;
import hxw.security.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
}
