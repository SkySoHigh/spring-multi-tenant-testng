import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.mtt.db.domain.UsersEntity;
import ru.mtt.db.tenants.TenantContext;

import java.util.Optional;

public class DBTests extends TestBase {

    @AfterMethod
    public void clean() {
        TenantContext.setCurrentTenant("main");
        usersRepository.deleteAll();
        TenantContext.setCurrentTenant("tenant");
        usersRepository.deleteAll();
    }

    @Test(description = "Default tenant should be main", priority = 1)
    public void defaultTenantShouldBeMain() {
        // Assert
        Assertions.assertThat(TenantContext.getCurrentTenant()).isEqualTo("main").as("Current tenant");
    }

    @Test(description = "User created in first tenant should not be present in the second tenant", priority = 2)
    public void EntityShouldBePresentOnlyInTheFirstTenant1() {
        // Arrange
        TenantContext.setCurrentTenant("main");
        UsersEntity user = UsersEntity.builder()
                .id(10)
                .username("testUsername")
                .password("testPassword")
                .build();
        // Act
        UsersEntity userFromMainTenant = usersRepository.save(user);

        // Assert
        Assertions.assertThat(user).isEqualTo(userFromMainTenant);

        TenantContext.setCurrentTenant("tenant");
        Optional<UsersEntity> userFromSecondTenant = usersRepository.findById(user.getId());
        Assertions.assertThat(userFromSecondTenant).isEqualTo(Optional.empty());

        // TearDown
        TenantContext.setCurrentTenant("main");
        usersRepository.deleteById(userFromMainTenant.getId());
    }

    @Test(description = "User created in the second tenant should not be present in the first tenant", priority = 3)
    public void EntityShouldBePresentOnlyInTheSecondTenant2() {
        // Arrange
        TenantContext.setCurrentTenant("tenant");
        UsersEntity user = UsersEntity.builder()
                .id(20)
                .username("testUsername")
                .password("testPassword")
                .build();
        // Act
        UsersEntity userFromSecondTenant = usersRepository.save(user);
        // Assert
        Assertions.assertThat(user).isEqualTo(userFromSecondTenant);

        TenantContext.setCurrentTenant("main");
        Optional<UsersEntity> userFromMainTenant = usersRepository.findById(user.getId());
        Assertions.assertThat(userFromMainTenant).isEqualTo(Optional.empty());

        // TearDown
        TenantContext.setCurrentTenant("tenant");
        usersRepository.deleteById(userFromSecondTenant.getId());
    }

    @Test(description = "Users created in all tenants are equal", priority = 4)
    public void CreatedUsersShouldBeEqualAmongTenants() {
        // Act
        TenantContext.setCurrentTenant("main");
        UsersEntity user1 = UsersEntity.builder()
                .id(100)
                .username("SameUsername")
                .password("SamePassword")
                .build();
        UsersEntity userFromMainTenant = usersRepository.save(user1);

        TenantContext.setCurrentTenant("tenant");
        UsersEntity user2 = UsersEntity.builder()
                .id(100)
                .username("SameUsername")
                .password("SamePassword")
                .build();
        UsersEntity userFromSecondTenant = usersRepository.save(user2);

        // Assert
        Assertions.assertThat(userFromMainTenant)
                .isEqualTo(userFromSecondTenant)
                .as("Users from different tenants");

        // TearDown
        TenantContext.setCurrentTenant("main");
        usersRepository.deleteById(userFromMainTenant.getId());
        TenantContext.setCurrentTenant("tenant");
        usersRepository.deleteById(userFromSecondTenant.getId());
    }

    @Test(description = "Updating user in the first tenant should not affect created user in the second tenant", priority = 5)
    public void UpdatingUserInOneTenantShouldNotAffectCreatedUserInTheSecondTenant() {
        // Arrange
        TenantContext.setCurrentTenant("main");
        UsersEntity user1 = UsersEntity.builder()
                .id(200)
                .username("testUsername1")
                .password("testPassword1")
                .build();
        UsersEntity userFromMainTenant = usersRepository.save(user1);

        TenantContext.setCurrentTenant("tenant");
        UsersEntity user2 = UsersEntity.builder()
                .id(200)
                .username("testUsername2")
                .password("testPassword2")
                .build();
        UsersEntity userFromSecondTenant = usersRepository.save(user2);

        // Act
        TenantContext.setCurrentTenant("main");
        userFromMainTenant.setUsername("NewUsername");
        UsersEntity UpdatedUserFromMainTenant = usersRepository.save(userFromMainTenant);

        // Assert
        Assertions.assertThat(UpdatedUserFromMainTenant.getUsername()).isEqualTo("NewUsername");
        Assertions.assertThat(userFromSecondTenant.getUsername()).isEqualTo(user2.getUsername());

        // TearDown
        TenantContext.setCurrentTenant("main");
        usersRepository.deleteById(userFromMainTenant.getId());
        TenantContext.setCurrentTenant("tenant");
        usersRepository.deleteById(userFromSecondTenant.getId());
    }


    @Test(description = "Deleting user in the first tenant should not affect created user in the second tenant", priority = 6)
    public void DeletingUserInOneTenantShouldNotAffectCreatedUserInTheSecondTenant() {
        // Arrange
        TenantContext.setCurrentTenant("main");
        UsersEntity user1 = UsersEntity.builder()
                .id(300)
                .username("testUsername1")
                .password("testPassword1")
                .build();
        UsersEntity userFromMainTenant = usersRepository.save(user1);

        TenantContext.setCurrentTenant("tenant");
        UsersEntity user2 = UsersEntity.builder()
                .id(300)
                .username("testUsername2")
                .password("testPassword2")
                .build();
        UsersEntity userFromSecondTenant = usersRepository.save(user2);

        // Act
        TenantContext.setCurrentTenant("main");
        usersRepository.deleteById(userFromMainTenant.getId());

        // Assert
        Optional<UsersEntity> removedUser = usersRepository.findById(userFromMainTenant.getId());
        Assertions.assertThat(removedUser).isEqualTo(Optional.empty());

        TenantContext.setCurrentTenant("tenant");
        UsersEntity notRemovedUserFromSecondTenant = usersRepository.findById(userFromSecondTenant.getId()).orElseThrow();
        Assertions.assertThat(userFromSecondTenant).isEqualTo(notRemovedUserFromSecondTenant);

        // TearDown
        TenantContext.setCurrentTenant("tenant");
        usersRepository.deleteById(notRemovedUserFromSecondTenant.getId());
    }


}
