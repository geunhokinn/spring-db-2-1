package hello.itemservicedb;

import hello.itemservicedb.config.JdbcTemplateV1Config;
import hello.itemservicedb.config.JdbcTemplateV2Config;
import hello.itemservicedb.config.MemoryConfig;
import hello.itemservicedb.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
@Import(JdbcTemplateV2Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservicedb.web")
public class ItemServiceDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceDbApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(ItemRepository itemRepository) {
        return new TestDataInit(itemRepository);
    }

}

