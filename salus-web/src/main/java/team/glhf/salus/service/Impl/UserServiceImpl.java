package team.glhf.salus.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import team.glhf.salus.dto.user.*;
import team.glhf.salus.entity.User;
import team.glhf.salus.entity.relation.UserSubscribeUser;
import team.glhf.salus.enumeration.FilePathEnum;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.UserException;
import team.glhf.salus.mapper.UserMapper;
import team.glhf.salus.mapper.UserSubscribeUserMapper;
import team.glhf.salus.service.CommonService;
import team.glhf.salus.service.UserService;
import team.glhf.salus.service.VerifyService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/9/22
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final TransactionTemplate transactionTemplate;
    private final UserSubscribeUserMapper userSubUserMapper;
    private final VerifyService verifyService;
    private final CommonService commonService;

    public UserServiceImpl(TransactionTemplate transactionTemplate, UserSubscribeUserMapper userSubUserMapper, VerifyService verifyService, CommonService commonService) {
        this.transactionTemplate = transactionTemplate;
        this.userSubUserMapper = userSubUserMapper;
        this.verifyService = verifyService;
        this.commonService = commonService;
    }

    @Override
    public LoginRes loginUser(LoginReq loginUserDto) {
        User user = getOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, loginUserDto.getEmail().trim())
                .eq(User::getPassword, loginUserDto.getPassword().trim())
        );
        // if user is NOT exist in the database
        if (null == user) {
            throw new UserException(HttpCodeEnum.ACCOUNT_PASSWORD_ERROR);
        }
        return LoginRes.builder()
                .userId(user.getId())
                .token(JWT.create()
                        .setExpiresAt(DateUtil.tomorrow())
                        .setKey("GLHF".getBytes())
                        .setPayload("id", user.getId())
                        .sign()
                )
                .build();
    }

    @Override
    public RegisterRes registerUser(RegisterReq registerUserDto) {
        // check if verifyCode is correct
        verifyService.checkVerifyCode(registerUserDto.getEmail(), registerUserDto.getVerifyCode());
        // check has the user existed
        if (checkHasUser(null, registerUserDto.getEmail())) {
            throw new UserException(HttpCodeEnum.USER_REGISTERED);
        }
        // generate entity object
        User user = User.builder()
                .email(registerUserDto.getEmail().trim())
                .password(registerUserDto.getPassword().trim())
                .nickname(RandomUtil.randomString(15))
                .build();
        save(user);
        verifyService.deleteCode(registerUserDto.getEmail());
        return RegisterRes.builder().userId(user.getId()).build();
    }

    @Override
    public ForgetRes forgetUser(ForgetReq forgetUserDto) {
        // check if verifyCode is correct
        verifyService.checkVerifyCode(forgetUserDto.getEmail(), forgetUserDto.getVerifyCode());
        User user = getOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, forgetUserDto.getEmail().trim())
        );
        // if user is NOT exist in the database
        if (null == user) {
            throw new UserException(HttpCodeEnum.USER_NOT_REGISTERED);
        }
        update(Wrappers.lambdaUpdate(User.class).eq(User::getEmail, forgetUserDto.getEmail())
                .set(User::getPassword, forgetUserDto.getNewPassword())
        );
        return ForgetRes.builder()
                .userId(user.getId())
                .build();
    }

    @Override
    public User getUserById(String userId) {
        if (!checkHasUser(userId, null)) {
            throw new UserException(HttpCodeEnum.USER_NOT_REGISTERED);
        }
        return getById(userId);
    }

    @Override
    public List<User> getUserByName(String nickname) {
        return list(Wrappers.lambdaQuery(User.class)
                .like(User::getNickname, nickname)
        );
    }

    @Override
    public List<User> getSubscribers(String userId) {
        List<UserSubscribeUser> subscribeUsers = userSubUserMapper.selectList(Wrappers.lambdaQuery(UserSubscribeUser.class)
                .eq(UserSubscribeUser::getFromUserId, userId)
        );
        List<String> idList = subscribeUsers.stream().map(UserSubscribeUser::getToUserId).toList();
        if (idList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return listByIds(idList);
        }
    }

    @Override
    public void subscribeUser(SubscribeReq subscribeReq, boolean isSubscribe) {
        if (subscribeReq.getUserId().equals(subscribeReq.getToUserId())) {
            throw new UserException(HttpCodeEnum.USER_SUBSCRIBED_ITSELF);
        }
        if (!checkHasUser(subscribeReq.getToUserId(), null)) {
            throw new UserException(HttpCodeEnum.USER_NOT_REGISTERED);
        }
        if (isSubscribe) {
            if (checkHasSubscribeUser(subscribeReq.getUserId(), subscribeReq.getToUserId())) {
                throw new UserException(HttpCodeEnum.USER_ALREADY_SUBSCRIBED);
            }
            // start a transaction
            transactionTemplate.execute(status -> {
                // generate new userSubUser instance
                UserSubscribeUser userSubUser = UserSubscribeUser.builder()
                        .fromUserId(subscribeReq.getUserId())
                        .toUserId(subscribeReq.getToUserId())
                        .build();
                userSubUserMapper.insert(userSubUser);
                // update user table
                update(Wrappers.lambdaUpdate(User.class)
                        .eq(User::getId, subscribeReq.getToUserId())
                        .setSql("fans = fans + 1")
                );
                return Boolean.TRUE;
            });
        } else {
            if (!checkHasSubscribeUser(subscribeReq.getUserId(), subscribeReq.getToUserId())) {
                throw new UserException(HttpCodeEnum.USER_NOT_SUBSCRIBED);
            }
            // start a transaction
            transactionTemplate.execute(status -> {
                userSubUserMapper.delete(Wrappers.lambdaQuery(UserSubscribeUser.class)
                        .eq(UserSubscribeUser::getFromUserId, subscribeReq.getUserId())
                        .eq(UserSubscribeUser::getToUserId, subscribeReq.getToUserId())
                );
                // update user table
                update(Wrappers.lambdaUpdate(User.class)
                        .eq(User::getId, subscribeReq.getToUserId())
                        .setSql("fans = fans - 1")
                );
                return Boolean.TRUE;
            });
        }
    }

    @Override
    public void updateUser(UpdateReq updateDto) {
        User user = User.builder().id(updateDto.getUserId()).build();
        BeanUtil.copyProperties(updateDto, user, "avatar");
        if (null != updateDto.getAvatar()) {
            String url = commonService.uploadImage(updateDto.getAvatar(), FilePathEnum.USER_AVATAR);
            user.setAvatar(url);
        }
        updateById(user);
    }

    @Override
    public boolean checkHasUser(String userId, String email) {
        return null != getOne(Wrappers.lambdaQuery(User.class)
                .eq(null != email, User::getEmail, email)
                .eq(null != userId, User::getId, userId)
        );
    }

    @Override
    public boolean checkHasSubscribeUser(String fromUserId, String toUserId) {
        UserSubscribeUser selected = userSubUserMapper.selectOne(Wrappers.lambdaQuery(UserSubscribeUser.class)
                .eq(UserSubscribeUser::getFromUserId, fromUserId)
                .eq(UserSubscribeUser::getToUserId, toUserId)
        );
        return null != selected;
    }
}
