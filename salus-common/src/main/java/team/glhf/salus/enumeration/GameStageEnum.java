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
public enum GameStageEnum implements Serializable {
    INIT(0, "初始化游戏"),
    PREPARE(1, "准备游戏"),
    LOADING(2, "加载游戏"),
    PLAYING(3, "正在游戏"),
    OVER(999, "游戏结束");

    private final Integer status;
    private final String message;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;

    GameStageEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
