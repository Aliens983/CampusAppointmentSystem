package com.laoliu.cas.system.application.service;

import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.common.exception.BusinessException;
import com.laoliu.cas.common.exception.code.LoginErrorCode;
import com.laoliu.cas.common.exception.code.UserErrorCode;
import com.laoliu.cas.common.util.PasswordUtils;
import com.laoliu.cas.redis.util.RedisUtil;
import com.laoliu.cas.security.util.JWTUtils;
import com.laoliu.cas.system.application.service.vo.UserRegisterVO;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author forever-king
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JWTUtils jwtUtils;
    private final PasswordUtils passwordUtils;
    private final RedisUtil redisUtil;

    public String login(String email, String password) {
        if (email == null || password == null) {
            throw new BusinessException(LoginErrorCode.EMAIL_OR_PASSWORD_EMPTY);
        }

        String encodePassword = userMapper.getEncodePasswordByEmail(email);
        if (encodePassword == null) {
            throw new BusinessException(LoginErrorCode.USER_NOT_EXIST);
        }
        if (passwordUtils.matches(password, encodePassword)) {
            Long userId = userMapper.getUserIdByEmail(email);
            return jwtUtils.generateToken(userId);
        }
        throw new BusinessException(LoginErrorCode.PASSWORD_ERROR);
    }

    public String resetPassword(String email, String code, String password) {
        if (email == null || email.isEmpty()) {
            throw new BusinessException(LoginErrorCode.EMAIL_EMPTY);
        }
        if (code == null || code.isEmpty()) {
            throw new BusinessException(LoginErrorCode.VERIFICATION_CODE_EMPTY);
        }
        if (password == null || password.isEmpty()) {
            throw new BusinessException(LoginErrorCode.PASSWORD_EMPTY);
        }

        String storedCode = redisUtil.getVerificationCode(email);
        if (storedCode == null) {
            throw new BusinessException(LoginErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (!storedCode.equals(code)) {
            throw new BusinessException(LoginErrorCode.VERIFICATION_CODE_ERROR);
        }

        Long userId = userMapper.getUserIdByEmail(email);
        if (userId == null) {
            throw new BusinessException(LoginErrorCode.USER_NOT_EXIST_BY_EMAIL);
        }

        String encodedPassword = passwordUtils.encode(password);
        userMapper.updatePasswordByEmail(email, encodedPassword);
        redisUtil.removeVerificationCode(email);

        return jwtUtils.generateToken(userId);
    }

    public Long register(UserRegisterVO request) {
        String email = request.getEmail();
        String code = request.getCode();

        if (email == null || code == null) {
            throw new BusinessException(UserErrorCode.EMAIL_OR_CODE_EMPTY);
        }

        Long ifUserId = userMapper.getUserIdByEmail(email);
        if (ifUserId != null) {
            throw new BusinessException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        String storedCode = redisUtil.getVerificationCode(email);
        if (storedCode == null) {
            throw new BusinessException(UserErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (!storedCode.equals(code)) {
            throw new BusinessException(UserErrorCode.VERIFICATION_CODE_ERROR);
        }

        redisUtil.removeVerificationCode("verification_code:" + email);

        String password = request.getPassword();
        String encodedPassword = passwordUtils.encode(password);

        User user = new User();
        user.setName(request.getName());
        user.setGrade(request.getGrade());
        user.setSex(request.getSex());
        user.setAge(request.getAge());
        user.setRole(request.getRole());
        user.setEmail(email);
        user.setPassword(encodedPassword);

        userMapper.insert(user);
        return user.getId();
    }
}
