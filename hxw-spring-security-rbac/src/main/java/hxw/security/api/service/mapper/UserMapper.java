package hxw.security.api.service.mapper;

import hxw.security.api.domain.User;
import hxw.security.api.service.dto.UserDTO;
import hxw.security.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<UserDTO, User> {
}
