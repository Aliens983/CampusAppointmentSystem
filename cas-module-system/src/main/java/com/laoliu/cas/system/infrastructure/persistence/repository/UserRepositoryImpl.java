package com.laoliu.cas.system.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.domain.repository.UserRepository;
import com.laoliu.cas.system.infrastructure.persistence.dataobject.UserDO;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户仓库实现
 *
 * @author forever-king
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id))
                .map(UserDO::toEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Long userId = userMapper.getUserIdByEmail(email);
        if (userId == null) {
            return Optional.empty();
        }
        return findById(userId);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null).stream()
                .map(UserDO::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        UserDO dataObject = UserDO.fromEntity(user);
        if (user.getId() == null) {
            userMapper.insert(dataObject);
            user.setId(dataObject.getId());
        } else {
            userMapper.updateById(dataObject);
        }
        return user;
    }

    @Override
    public void updatePasswordByEmail(String email, String encodedPassword) {
        userMapper.updatePasswordByEmail(email, encodedPassword);
    }

    @Override
    public void updatePasswordById(Long userId, String encodedPassword) {
        userMapper.updatePasswordById(userId, encodedPassword);
    }

    @Override
    public String getEncodePasswordByEmail(String email) {
        return userMapper.getEncodePasswordByEmail(email);
    }

    @Override
    public String getEncodePasswordById(Long userId) {
        return userMapper.getEncodePasswordById(userId);
    }

    @Override
    public String getRoleByUserId(Long userId) {
        return userMapper.getRoleByUserId(userId);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userMapper.getUserIdByEmail(email);
    }

    @Override
    public void updateRoleToCommonUser(Long userId) {
        userMapper.updateRoleToCommonUser(userId);
    }

    @Override
    public void updateRoleToAdmin(Long userId) {
        userMapper.updateRoleToAdmin(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers().stream()
                .map(UserDO::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<User> getAllUsers(int page, int pageSize) {
        Page<UserDO> pageParam = new Page<>(page, pageSize);
        IPage<UserDO> doPage = userMapper.getAllUsersWithPage(pageParam);
        return doPage.convert(UserDO::toEntity);
    }

    @Override
    public List<Map<String, Object>> getAllBookings(Long userId) {
        return userMapper.getAllBookings(userId);
    }
}
