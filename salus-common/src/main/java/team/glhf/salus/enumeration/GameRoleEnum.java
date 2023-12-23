package team.glhf.salus.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/12/11
 */
@Getter
@ToString
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GameRoleEnum implements Serializable {
    NULL(0, "未选择角色"),
    MOUSE(1, "老鼠"),
    CAT(2, "猫"),
    CAUGHT(999, "被抓住的老鼠");

    private final Integer code;
    private final String name;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;

    GameRoleEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GameRoleEnum getRoleByCode(Integer code) {
        for (GameRoleEnum value : GameRoleEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NULL;
    }
}

