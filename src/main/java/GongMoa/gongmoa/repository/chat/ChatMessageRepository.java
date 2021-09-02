package GongMoa.gongmoa.repository.chat;

import GongMoa.gongmoa.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
