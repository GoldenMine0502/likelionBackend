package kr.goldenmine.likelionbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSerivce {

    private final UserRepository userRepository;

    public UserSerivce(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User add(User user) {
        return userRepository.saveAndFlush(user);
    }

    public void updateUserId(int id, String userIdTo) {
        User user = userRepository.findById(id).get(); // 무조건 있다고 치자

        user.setUserId(userIdTo);

        userRepository.saveAndFlush(user);
    }

    public void delete(int id) {
        User user = userRepository.findById(id).get(); // 무조건 있다고 치자

        userRepository.delete(user);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User get(int id) {
        return userRepository.findById(id).get();
    }
}