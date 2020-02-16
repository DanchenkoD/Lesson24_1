package DAO;

import DAO.utils.RecipeMapper;
import model.Recipe;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDaoImpl implements RecipeDao {

    private ApplicationContext context ;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public RecipeDaoImpl() {
        context = ApplicationContextHolder.getApplicationContext();
        jdbcTemplate= (JdbcTemplate) context.getBean("jdbcTemplate");
        simpleJdbcInsert= (SimpleJdbcInsert) context.getBean("simpleJdbcInsert");
    }

    private static final String QUERY_GET_BY_PK = "SELECT id, name, description FROM Recipe WHERE id=?;";
    private static final String QUERY_GET_ALL = "SELECT id, name, description FROM Recipe;";
    private static final String QUERY_DELETE_BY_PK = "DELETE FROM Recipe WHERE id=?;";
    private static final String QUERY_UPDATE = "UPDATE Recipe SET name = ?, description = ? WHERE id = ?;";

    @Override
    public void insert(Recipe recipe) {
        if (recipe.getId() != null) {
            System.out.println("Ед. изм. id " + recipe.getId() + " уже сохранена в базе");
            return;
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", recipe.getName());
        parameters.put("description", recipe.getDescription());
        int rows = simpleJdbcInsert.withTableName("Recipe").execute(parameters);
        recipe.setId(rows);
        System.out.println("Добавлен " + rows + " рецепт: " + recipe.getName());
    }

    @Override
    public Recipe getByPK(Integer primaryKey) {
        Object[] params = { primaryKey };
        int[] types = {Types.INTEGER};
        List <Recipe> recipes = jdbcTemplate.query(QUERY_GET_BY_PK, params, types, new RecipeMapper());
        if (recipes.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    @Override
    public List<Recipe> getAll() {
        List <Recipe> recipes = jdbcTemplate.query(QUERY_GET_ALL, new RecipeMapper());
        return recipes;
    }

    @Override
    public void deleteByPK(Integer primaryKey) {
        Object[] params = { primaryKey };
        int[] types = {Types.INTEGER};
        int rows = jdbcTemplate.update(QUERY_DELETE_BY_PK, params, types);
        System.out.println("Удален " + rows + " рецепт ");
    }

    @Override
    public void update(Recipe recipe) {
        if (recipe.getId() == null) return;
        int rows = jdbcTemplate.update(QUERY_UPDATE, recipe.getName(), recipe.getDescription(), recipe.getId());
        System.out.println("Обновлен " + rows + " рецепт " + recipe.getName());
    }
}
