package hxw.security.api.service;

import hxw.security.api.domain.User;
import hxw.security.api.service.dto.UserDTO;
import hxw.security.core.security.JwtUser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "user")
public interface UserService {

    /**
     * get
     */
    @Cacheable(key = "#p0")
    UserDTO findById(long id);

    /**
     * create
     */
    @CacheEvict(allEntries = true)
    UserDTO create(User resources);

    /**
     * update
     */
    @CacheEvict(allEntries = true)
    void update(User resources);

    /**
     * delete
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * findByName
     */
    @Cacheable(key = "'findByName'+#p0")
    User findByName(String userName);

    /**
     * 修改密码
     */
    void updatePass(JwtUser jwtUser, String encryptPassword);

    /**
     * 修改头像
     */
    void updateAvatar(JwtUser jwtUser, String url);

    /**
     * 修改邮箱
     */
    void updateEmail(JwtUser jwtUser, String email);
}
