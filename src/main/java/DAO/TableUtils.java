package DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.RowSet;

@Component
public class TableUtils {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private static final String SQL_CREATE_TABLES =
            "CREATE TABLE IF NOT EXISTS Ingredient(\n" +
                    "  Id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  Name VARCHAR(100) NOT NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Recipe(\n" +
                    "  id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  name VARCHAR(100) NOT NULL,\n" +
                    "  description VARCHAR(500) \n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Unit(\n" +
                    "  id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  name VARCHAR(50) NOT NULL,\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Structure(\n" +
                    "  id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  recipe_id INT,\n" +
                    "  ingredient_id INT,\n" +
                    "  amount FLOAT NOT NULL,\n" +
                    "  unit_id INT,\n" +
                    "  \n" +
                    "  CONSTRAINT FK_StructureRecipe FOREIGN KEY (recipe_id) REFERENCES Recipe(id),\n" +
                    "  CONSTRAINT FK_StructureIngredient FOREIGN KEY (ingredient_id) REFERENCES Ingredient(id),\n" +
                    "  CONSTRAINT FK_StructureUnit FOREIGN KEY (unit_id) REFERENCES Unit(id)\n" +
                    ");";


    public void createTables()  {
        //System.out.println(SQL_CREATE_TABLES);
        //        System.out.println(jdbcTemplate);
        jdbcTemplate.execute(SQL_CREATE_TABLES);
    }


    /*@Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("-----"+jdbcTemplate);
    }*/
}
