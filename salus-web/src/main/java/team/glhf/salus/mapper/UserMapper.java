package team.glhf.salus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.glhf.salus.entity.User;

/**
 * UserMapper
 *
 * @author Steveny
 * @since 2023/9/22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
