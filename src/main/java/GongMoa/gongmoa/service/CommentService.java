package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Comment;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long createComment(User user, Contest contest, String content) {
        Comment comment = Comment.createComment(user, contest, content);
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long createSubComment(User user, Comment superComment, String content) {
        Contest contest = superComment.getContest();
        Comment subComment = Comment.createComment(user, contest, content);

        superComment.getSubComments().add(subComment);
        subComment.setSuperComment(superComment);

        return commentRepository.save(subComment).getId();
    }
}
