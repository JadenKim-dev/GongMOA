package GongMoa.gongmoa.chatting;

import lombok.Getter;

import java.time.LocalDateTime;

public interface TimeSaveClass {
    LocalDateTime time = null;

    LocalDateTime getTime();
}
