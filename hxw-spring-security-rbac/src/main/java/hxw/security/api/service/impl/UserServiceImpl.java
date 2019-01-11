package hxw.security.api.service.impl;

import hxw.security.api.domain.User;
import hxw.security.api.repository.UserRepository;
import hxw.security.api.service.UserService;
import hxw.security.api.service.dto.UserDTO;
import hxw.security.api.service.mapper.UserMapper;
import hxw.security.common.exception.BadRequestException;
import hxw.security.common.exception.EntityExistException;
import hxw.security.common.exception.EntityNotFoundException;
import hxw.security.common.utils.ValidationUtil;
import hxw.security.core.security.JwtUser;
import hxw.security.core.utils.JwtTokenUtil;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDTO findById(long id) {
        Optional<User> user = userRepository.findById(id);
        ValidationUtil.isNull(user, "User", "id", id);
        return userMapper.toDto(user.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(User resources) {

        if (userRepository.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }

        if (userRepository.findByEmail(resources.getEmail()) != null) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }

        if (resources.getRoles() == null || resources.getRoles().size() == 0) {
            throw new BadRequestException("角色不能为空");
        }

        // 默认密码 123456
        resources.setPassword("e10adc3949ba59abbe56e057f20f883e");
        resources.setAvatar("https://i.loli.net/2018/12/06/5c08894d8de21.jpg");
        return userMapper.toDto(userRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {

        Optional<User> userOptional = userRepository.findById(resources.getId());
        ValidationUtil.isNull(userOptional, "User", "id", resources.getId());

        User user = userOptional.get();

        /**
         * 根据实际需求修改
         */
        if (user.getId().equals(1L)) {
            throw new BadRequestException("该账号不能被修改");
        }

        User user1 = userRepository.findByUsername(user.getUsername());
        User user2 = userRepository.findByEmail(user.getEmail());

        if (resources.getRoles() == null || resources.getRoles().size() == 0) {
            throw new BadRequestException("角色不能为空");
        }

        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }

        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }

        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        /**
         * 根据实际需求修改
         */
        if (id.equals(1L)) {
            throw new BadRequestException("该账号不能被删除");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByName(String userName) {
        User user = null;
        if (ValidationUtil.isEmail(userName)) {
            user = userRepository.findByEmail(userName);
        } else {
            user = userRepository.findByUsername(userName);
        }

        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            return user;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(JwtUser jwtUser, String pass) {
        userRepository.updatePass(jwtUser.getId(), pass);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(JwtUser jwtUser, String url) {
        userRepository.updateAvatar(jwtUser.getId(), url);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(JwtUser jwtUser, String email) {
        userRepository.updateEmail(jwtUser.getId(), email);
    }

}
