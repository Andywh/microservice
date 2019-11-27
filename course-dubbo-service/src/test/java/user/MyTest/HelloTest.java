package user.MyTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by SongLiang on 2019-08-21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloTest {

//    @Autowired
//    private UserInfoMapper userInfoMapper;

    @Test
    public void testAAA() {
        System.out.println("test123");
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUsername("songliang");
//        userInfo.setPassword("root");
//        userInfo.setOpenid("1234");
//        userInfo.setRole("buyer");
//        userInfoMapper.insert(userInfo);
    }

    @Test
    public void testBBB() {
        System.out.println("test1232");
    }

}
