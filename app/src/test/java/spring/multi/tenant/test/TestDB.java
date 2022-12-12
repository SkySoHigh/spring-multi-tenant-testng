package spring.multi.tenant.test;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import spring.multi.tenant.test.db.domain.UsersEntity;
import spring.multi.tenant.test.db.tenants.TenantContext;

public class TestDB extends TestBase {

    @Test(description = "User created in first tenant should not be present in th second tenant")
    public void EntityShouldBePresentOnlyInTheFirstTenant() {
        // Arrange
        UsersEntity user = UsersEntity.builder()
                .username("testUsername")
                .password("testPassword")
                .build();
        // Act
        System.out.println(TenantContext.getCurrentTenant());
        this.usersRepository.save(user);
        // Assert
        Assertions.assertThat(TenantContext.getCurrentTenant()).isEqualTo("main").as("Current tenant");





    }
}
