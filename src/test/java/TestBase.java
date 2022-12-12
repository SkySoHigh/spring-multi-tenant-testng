import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import ru.mtt.AppForTest;
import ru.mtt.db.repository.UsersRepository;

@SpringBootTest(classes = {AppForTest.class})
public class TestBase extends AbstractTestNGSpringContextTests {

    @Autowired
    UsersRepository usersRepository;

    @BeforeClass
    public void initialize() throws Exception {
        springTestContextPrepareTestInstance();
    }

}