package ut;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Db2Container;
import org.testcontainers.utility.DockerImageName;

//@QuarkusTest
public class Db2RepositoryTest {
    
    static Db2Container db2Container;

    @BeforeAll
    public static void init(){
        db2Container = new Db2Container( DockerImageName.parse("ibmcom/db2:11.5.0.0a")).acceptLicense();
        db2Container.start();
    }
   
    @AfterAll
    public static void close(){
        db2Container.stop();
        db2Container.close();
    }

    @Test
    public void testDbAccess(){
        String url = db2Container.getJdbcUrl();
        Assertions.assertNotNull(url);
        System.out.println(url);
        String pwd = db2Container.getPassword();
        Assertions.assertNotNull(pwd);
    }
}
