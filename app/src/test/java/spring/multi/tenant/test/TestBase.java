package spring.multi.tenant.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import spring.multi.tenant.test.db.repository.UsersRepository;

@SpringBootTest(classes = {AppForTest.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestBase extends AbstractTestNGSpringContextTests {

    @Autowired
    UsersRepository usersRepository;


    @BeforeClass
    public void initialize() throws Exception {
        super.springTestContextPrepareTestInstance();
    }


}