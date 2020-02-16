package DAO;

import DAO.utils.StructureEntryMapper;
import model.Structure;
import model.StructureEntry;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureDaoImpl implements  StructureDao{
    private ApplicationContext context ;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private UnitDao unitDao;
    private StructureEntryMapper structureEntryMapper;

    public StructureDaoImpl() {
        context = ApplicationContextHolder.getApplicationContext();
        jdbcTemplate= (JdbcTemplate) context.getBean("jdbcTemplate");
        simpleJdbcInsert= (SimpleJdbcInsert) context.getBean("simpleJdbcInsert");
        simpleJdbcInsert.withTableName("Structure");
        recipeDao= (RecipeDao) context.getBean("recipeDao");
        ingredientDao= (IngredientDao) context.getBean("ingredientDao");
        unitDao= (UnitDao) context.getBean("unitDao");
        structureEntryMapper= (StructureEntryMapper) context.getBean("structureEntryMapper");
    }

    private static final String QUERY_INSERT =
            "INSERT INTO Structure(recipe_id, ingredient_id, amount, unit_id)\n" +
                    "VALUES (:recipe_id, :ingredient_id, :amount, :unit_id);";

    private static final String QUERY_GET_BY_RECIPE_ID = "SELECT ingredient_id, amount, unit_id FROM Structure WHERE recipe_id=?;";

    private static final String QUERY_GET_COMPOSITIONS_ID_BY_RECIPE_ID = "SELECT id FROM Structure WHERE recipe_id=?;";

    private static final String QUERY_GET_ALL_RECIPE_ID = "SELECT id FROM Recipe;";

    private static final String QUERY_DELETE = "DELETE FROM Structure WHERE recipe_id=?;";

    private static final String QUERY_SEARCH_BY_NAME = "SELECT ID FROM RECIPE WHERE NAME LIKE ?;";

    @Transactional
    @Override
    public void insert(Structure structure) {
        if (structure.getIdList() != null) {
            System.out.println("Состав рецепта id " + structure.getRecipeRef().getId() + " уже сохранен в базе");
            return;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        List<Integer> idList = new ArrayList<>();
        int result = 0;

        recipeDao.insert(structure.getRecipeRef());

        for (StructureEntry entry : structure.getEntryList()) {

            ingredientDao.insert(entry.getIngredientRef());
            unitDao.insert(entry.getUnitRef());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("recipe_id", structure.getRecipeRef().getId());
            parameters.put("ingredient_id", entry.getIngredientRef().getId());
            parameters.put("amount", entry.getAmount());
            parameters.put("unit_id", entry.getUnitRef().getId());
            /*if (simpleJdbcInsert.getTableName()=="")
            {simpleJdbcInsert.withTableName("Structure");}*/
            int rows = simpleJdbcInsert.execute(parameters);
            result +=rows;
            //idList.add(keyHolder.getKey().intValue());
            idList.add(rows);
            //structure.setId(rows);

            /*SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("recipe_id", structure.getRecipeRef().getId())
                    .addValue("ingredient_id", entry.getIngredientRef().getId())
                    .addValue("amount", entry.getAmount())
                    .addValue("unit_id", entry.getUnitRef().getId());

            result += namedParameterJdbcTemplate.update(QUERY_INSERT, param, keyHolder);
            idList.add(keyHolder.getKey().intValue());*/
        }

        structure.setIdList(idList);

        System.out.println("Добавлен состав рецепта: '" + structure.getRecipeRef().getName()
                + "', кол-во ингредиентов: " + result);
    }

    @Override
    public Structure getByRecipeID(Integer recipeID) {
        Object[] params = { recipeID };
        int[] types = {Types.INTEGER};
        //SqlParameterSource param = new MapSqlParameterSource("recipe_id", recipeID);

        Structure structure = new Structure();
        structure.setRecipeRef(recipeDao.getByPK(recipeID));

        //Получаем состав рецепта
        List<StructureEntry> structureEntryList = jdbcTemplate.query(QUERY_GET_BY_RECIPE_ID, params,  structureEntryMapper);
        structure.setEntryList(structureEntryList);

        //Получаем айдишники отдельно
        List<Integer> idList = jdbcTemplate.queryForList(QUERY_GET_COMPOSITIONS_ID_BY_RECIPE_ID, params, Integer.class);
        structure.setIdList(idList);

        return structure;
    }

    @Override
    public List<Structure> getAll() {
        List<Structure> structureList = new ArrayList<>();

        //JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        List<Integer> recipeIdList = jdbcTemplate.queryForList(QUERY_GET_ALL_RECIPE_ID, Integer.class);

        // Перебираем все рецепты и по id получаем полные рецепты
        for (Integer recipeId : recipeIdList) {
            structureList.add(getByRecipeID(recipeId));
        }

        return structureList;
    }

    @Transactional
    @Override
    public void deleteByRecipeID(Integer recipeID) {
        // Удаляем рецепт из Recipe и его составляющие из Composition
        Object[] params = { recipeID };
        int[] types = {Types.INTEGER};
        int result = jdbcTemplate.update(QUERY_DELETE, params, types);

        recipeDao.deleteByPK(recipeID);
        System.out.println("Удалены составляющие рецепта id: " + recipeID + " (" + result + " строк)");
    }

    @Override
    public List<Structure> searchByName(String name) {
        //SqlParameterSource param = new MapSqlParameterSource("name", "%" + name + "%");
        Object[] params = { "%" + name + "%" };
        //int[] types = {Types.INTEGER};
        List<Integer> idList = jdbcTemplate.queryForList(QUERY_SEARCH_BY_NAME, params, Integer.class);

        List<Structure> structureList = new ArrayList<>();
        for (Integer id : idList) {
            structureList.add(getByRecipeID(id));
        }

        return structureList;
    }
}
