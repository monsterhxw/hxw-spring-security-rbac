package me.zhengjie.system.rest;

import hxw.security.api.service.UserService;
import hxw.security.api.service.dto.UserDTO;
import hxw.security.api.service.query.UserQueryService;
import hxw.security.common.exception.BadRequestException;
import hxw.security.common.utils.RequestHolder;
import hxw.security.core.security.JwtUser;
import hxw.security.core.utils.EncryptUtils;
import hxw.security.core.utils.JwtTokenUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

//    @Autowired
//    private PictureService pictureService;

//    @Autowired
//    private VerificationCodeService verificationCodeService;


    private static final String ENTITY_NAME = "user";

    @GetMapping(value = "/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ResponseEntity getUser(@PathVariable Long id) {
        return new ResponseEntity(userService.findById(id), HttpStatus.OK);
    }

    //    @Log(description = "查询用户")
    @GetMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ResponseEntity getUsers(UserDTO userDTO, Pageable pageable) {
        return new ResponseEntity(userQueryService.queryAll(userDTO, pageable), HttpStatus.OK);
    }

    //    @Log(description = "新增用户")
    @PostMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody hxw.security.api.domain.User resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity(userService.create(resources), HttpStatus.CREATED);
    }

    //    @Log(description = "修改用户")
    @PutMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody hxw.security.api.domain.User resources) {
        if (resources.getId() == null) {
            throw new BadRequestException(ENTITY_NAME + " ID Can not be empty");
        }
        userService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //    @Log(description = "删除用户")
    @DeleteMapping(value = "/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 验证密码
     */
    @GetMapping(value = "/users/validPass/{pass}")
    public ResponseEntity validPass(@PathVariable String pass) {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(jwtTokenUtil.getUserName(RequestHolder.getHttpServletRequest()));
        Map map = new HashMap();
        map.put("status", 200);
        if (!jwtUser.getPassword().equals(EncryptUtils.encryptPassword(pass))) {
            map.put("status", 400);
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }

    /**
     * 修改密码
     */
    @GetMapping(value = "/users/updatePass/{pass}")
    public ResponseEntity updatePass(@PathVariable String pass) {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(jwtTokenUtil.getUserName(RequestHolder.getHttpServletRequest()));
        if (jwtUser.getPassword().equals(EncryptUtils.encryptPassword(pass))) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(jwtUser, EncryptUtils.encryptPassword(pass));
        return new ResponseEntity(HttpStatus.OK);
    }

//    /**
//     * 修改头像
//     */
//    @PostMapping(value = "/users/updateAvatar")
//    public ResponseEntity updateAvatar(@RequestParam MultipartFile file) {
//        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(jwtTokenUtil.getUserName(RequestHolder.getHttpServletRequest()));
//        Picture picture = pictureService.upload(file, jwtUser.getUsername());
//        userService.updateAvatar(jwtUser, picture.getUrl());
//        return new ResponseEntity(HttpStatus.OK);
//    }

//    /**
//     * 修改邮箱
//     */
//    @PostMapping(value = "/users/updateEmail/{code}")
//    public ResponseEntity updateEmail(@PathVariable String code, @RequestBody User user) {
//        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(jwtTokenUtil.getUserName(RequestHolder.getHttpServletRequest()));
//        if (!jwtUser.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword()))) {
//            throw new BadRequestException("密码错误");
//        }
//        VerificationCode verificationCode = new VerificationCode(code, ElAdminConstant.RESET_MAIL, "email", user.getEmail());
//        verificationCodeService.validated(verificationCode);
//        userService.updateEmail(jwtUser, user.getEmail());
//        return new ResponseEntity(HttpStatus.OK);
//    }
}
