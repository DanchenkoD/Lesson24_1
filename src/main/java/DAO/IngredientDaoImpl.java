package DAO;

import DAO.utils.IngredientMapper;
import model.Ingredient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public class IngredientDaoImpl implements IngredientDao {
    private ApplicationContext context ;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;


    public IngredientDaoImpl() {
        context = ApplicationContextHolder.getApplicationContext();
        jdbcTemplate= (JdbcTemplate) context.getBean("jdbcTemplate");
        simpleJdbcInsert= (SimpleJdbcInsert) context.getBean("simpleJdbcInsert");
    }


    /*@Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;*/

    private static final String QUERY_DELETE_BY_PK = "DELETE FROM Ingredient WHERE id = ?;";
    private static final String QUERY_UPDATE ="UPDATE Ingredient SET name = ? WHERE id = ?;";
    private static final String QUERY_GET_ALL = "SELECT id, name FROM Ingredient;";
    private static final String QUERY_GET_BY_PK = "SELECT id, name FROM Ingredient WHERE id=?;";
    private static final String QUERY_INSERT = "INSERT INTO Ingredient(name) VALUES (?);";


    @Override
    public void insert(Ingredient ingredient) {
        if (ingredient.getId() != null) {
            System.out.println("Ингредиент id " + ingredient.getId() + " уже сохранен в базе");
            return;
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", ingredient.getName());
        Object[] params = { ingredient.getName() };
        int[] types = {Types.VARCHAR};
        int rows = simpleJdbcInsert.withTableName("Ingredient").execute(parameters);
        ingredient.setId(rows);
        System.out.println("Добавлен " + rows + " ингредиент: " + ingredient.getName());
    }

    @Override
    public Ingredient getByPK(Integer primaryKey) {
        Object[] params = { primaryKey };
        int[] types = {Types.INTEGER};
        List <Ingredient> ingredients = jdbcTemplate.query(QUERY_GET_BY_PK, params, types, new IngredientMapper());
        if (ingredients.isEmpty()) {
            return null;
        }
        return ingredients.get(0);
    }

    @Override
    public List<Ingredient> getAll() {
        List <Ingredient> ingredients = jdbcTemplate.query(QUERY_GET_ALL, new IngredientMapper());
        return ingredients;
    }

    @Override
    public void deleteByPK(Integer primaryKey) {
        Object[] params = { primaryKey };
        int[] types = {Types.INTEGER};
        int rows = jdbcTemplate.update(QUERY_DELETE_BY_PK, params, types);
        System.out.println("Удален " + rows + " ингредиент ");

    }

    @Override
    public void update(Ingredient ingredient) {
        if (ingredient.getId() == null) return;
        int rows = jdbcTemplate.update(QUERY_UPDATE, ingredient.getName(), ingredient.getId());
        System.out.println("Обновлен " + rows + " ингредиент " + ingredient.getName());
    }
}
