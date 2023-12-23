package team.glhf.salus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.glhf.salus.dto.user.*;
import team.glhf.salus.entity.User;

import java.util.List;

/**
 * User service interface
 *
 * @author Steveny
 * @since 2023/9/22
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    LoginRes loginUser(LoginReq loginUserDto);

    /**
     * 用户注册
     */
    RegisterRes registerUser(RegisterReq registerUserDto);

    /**
     * 用户忘记密码
     */
    ForgetRes forgetUser(ForgetReq forgetUserDto);

    /**
     * 根据ID查找用户
     */
    User getUserById(String userId);

    /**
     * 根据姓名查找用户
     */
    List<User> getUserByName(String nickname);

    /**
     * 获取关注的人的
     */
    List<User> getSubscribers(String userId);

    /**
     * 关注用户或取消关注用户
     */
    void subscribeUser(SubscribeReq subscribeReq, boolean isSubscribe);

    /**
     * 修改用户信息
     */
    void updateUser(UpdateReq updateDto);

    // -------------------------------------------------------------------------------------------

    /**
     * 用户是否存在
     */
    boolean checkHasUser(String userId, String email);

    /**
     * 用户是否关注某用户
     */
    boolean checkHasSubscribeUser(String fromUserId, String toUserId);
}
