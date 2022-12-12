# spring-multi-tenant-test
Checking the implementation of multi-tenant per database for automated testing tasks using TestNG

## Description
### Disclaimer
- For the most part, this is a concept check, so the tests themselves in this example will not check anything useful.
- To simplify the verification of the concept, h2 in memory is used as a database

### For what purpose?
To access multiple database shards or similar database schemas of different servers in test automation tasks.
The TestNG framework will be used to write autotests.

## Requirements
- Java 17
- All specified deps in build.gradle

## Configuration and Usage
- Add tenants to tenants.json. One ot he tenants should be called **main**. **Main** tenant will be used as default datasource for all DB calls if there is no any tenant set in TenantContext

    ```json
    [
      {
        "name" : "main",
        "dataSourceClassName": "org.h2.Driver",
        "url": "jdbc:h2:mem:demo1;INIT=RUNSCRIPT FROM 'classpath:schema.sql';DB_CLOSE_DELAY=-1;",
        "user": "mockValue",
        "password": "***"
      },
      {...},
      {...}
    ]
    ```
  
- Switch between tenant via TenantContext class
    
  ```java
  // Main tenant will be used as default datasource for all DB calls if there is no any tenant set in TenantContext
  List<UsersEntity> usersFromDefaultTenant = usersRepository.findAll();
  
  // Get via specified tenant (according datasource will be ised)
  TenantContext.setCurrentTenant("tenant1");
  List<UsersEntity> usersFromSpecifiedTenant = usersRepository.findAll();
  ```
  
## Run Test
Run via cmd example:
```bash
 gradle wrapper 
./gradlew test
allure serve allure-results
```
