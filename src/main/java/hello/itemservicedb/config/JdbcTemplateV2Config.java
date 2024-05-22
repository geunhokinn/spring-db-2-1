package hello.itemservicedb.config;

import hello.itemservicedb.repository.ItemRepository;
import hello.itemservicedb.repository.jdbctemplate.JdbcTemplateRepositoryV2;
import hello.itemservicedb.service.ItemService;
import hello.itemservicedb.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV2Config {

    private final DataSource dataSource;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JdbcTemplateRepositoryV2(dataSource);
    }

}
