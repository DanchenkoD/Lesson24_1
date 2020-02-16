package DAO;
import DAO.utils.StructureEntryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

//import java.io.Console;

@Configuration
@ComponentScan
public class SpringJdbcConfiguration {
    /*private static final String DB_DRIVER = "org.h2.Driver";*/
    private static final String DB_URL = "jdbc:h2:~/test";

    @Bean
    DriverManagerDataSource dataSource() {
        return new DriverManagerDataSource(DB_URL, "sa", "");
    }

    @Bean
    @Scope("prototype")
    SimpleJdbcInsert simpleJdbcInsert() {
        return new SimpleJdbcInsert(dataSource()); //.withTableName("Recipe");
    }

   /* @Bean
    Repository repository() {
        return new Repository();
    }*/

    @Bean("jdbcTemplate")
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    @Bean
    TableUtils tableUtils() {
        return new TableUtils();
    }

    @Bean
    StructureEntryMapper structureEntryMapper()
    {return new StructureEntryMapper();}

    @Bean
    IngredientDao ingredientDao()
    {return new IngredientDaoImpl();}

    @Bean
    RecipeDao recipeDao()
    {return new RecipeDaoImpl();}

    @Bean
    UnitDao unitDao()
    {return new UnitDaoImpl();}

    @Bean
    StructureDao structureDao()
    {return new StructureDaoImpl();}

    /*@Bean
    Console console()
    {return new Console();}*/
}
