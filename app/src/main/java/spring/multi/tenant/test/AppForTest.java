package spring.multi.tenant.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"spring.multi.tenant.test"},
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        })
public class AppForTest {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AppForTest.class);
        application.run(args);
    }

}
