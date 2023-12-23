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
public enum GameMessageType implements Serializable {
    NORMAL(1, "消息1"),
    MESSAGE(3, "消息3"),
    OVER(4, "消息4");

    private final Integer code;
    private final String name;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;

    GameMessageType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}

