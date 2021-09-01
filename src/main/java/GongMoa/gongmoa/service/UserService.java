package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.OAuth2.UserRepository;
import GongMoa.gongmoa.fileupload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(Long userId, User userParam) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        user.update(userParam.getName(), userParam.getPicture()!=null ? userParam.getPicture() : user.getPicture());
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }

    public User findByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }
}
