package DAO.utils;

import DAO.ApplicationContextHolder;
import DAO.IngredientDao;
import DAO.UnitDao;
import model.Ingredient;
import model.StructureEntry;
import model.Unit;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StructureEntryMapper implements RowMapper<StructureEntry> {
    private final IngredientDao ingredientDao;
    private final UnitDao unitDao;

    private ApplicationContext context ;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public StructureEntryMapper() {
        context = ApplicationContextHolder.getApplicationContext();
        jdbcTemplate= (JdbcTemplate) context.getBean("jdbcTemplate");
        simpleJdbcInsert= (SimpleJdbcInsert) context.getBean("simpleJdbcInsert");
        this.ingredientDao = (IngredientDao) context.getBean("ingredientDao");
        this.unitDao = (UnitDao) context.getBean("unitDao");
    }

    public StructureEntryMapper(IngredientDao ingredientDao, UnitDao unitDao) {

        this.ingredientDao = ingredientDao;
        this.unitDao = unitDao;
    }

    @Override
    public StructureEntry mapRow(ResultSet resultSet, int i) throws SQLException {
        Integer ingredientId = resultSet.getInt("ingredient_id");
        Integer unitId = resultSet.getInt("unit_id");

        Ingredient ingredient = ingredientDao.getByPK(ingredientId);
        Unit unit = unitDao.getByPK(unitId);

        StructureEntry structureEntry = new StructureEntry();
        structureEntry.setIngredientRef(ingredient);
        structureEntry.setAmount(resultSet.getDouble("amount"));
        structureEntry.setUnitRef(unit);

        return structureEntry;
    }
}
