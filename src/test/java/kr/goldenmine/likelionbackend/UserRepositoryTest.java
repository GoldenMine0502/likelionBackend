package kr.goldenmine.likelionbackend;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserSerivce userService;

    @Test
    public void create() {
        User user = new User();
        user.setUserId("test1");
        user.setPassword("test1");

        User newUser = userService.add(user);

        System.out.println("new: " + newUser);
    }

    @Test
    public void update() {
        int id = 3;
        userService.updateUserId(id, "test-1");

        User user = userService.get(id);

        System.out.println("update: " + user);
    }


    @Test
    public void selectAll() {
        List<User> users = userService.list();

        System.out.println("=== All Users ===");
        users.forEach(System.out::println);
        System.out.println("=================");
    }

    @Test
    public void delete() {
        userService.delete(3);
    }

}

