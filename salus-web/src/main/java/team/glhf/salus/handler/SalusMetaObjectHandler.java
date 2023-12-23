package team.glhf.salus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import team.glhf.salus.utils.SalusUtil;


/**
 * MP MetaObjectHandler
 *
 * @author Felix
 * @since 2023/10/30
 */
@Component
public class SalusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter("createTime")) {
            metaObject.setValue("createTime", SalusUtil.date());
        }
        if (metaObject.hasGetter("updateTime")) {
            metaObject.setValue("updateTime", SalusUtil.date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", SalusUtil.date());
    }
}
