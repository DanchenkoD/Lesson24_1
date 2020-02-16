package DAO;

import DAO.utils.UnitMapper;
import model.Unit;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitDaoImpl implements UnitDao {

    private ApplicationContext context ;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;


    public UnitDaoImpl() {
        context = ApplicationContextHolder.getApplicationContext();
        jdbcTemplate= (JdbcTemplate) context.getBean("jdbcTemplate");
        simpleJdbcInsert= (SimpleJdbcInsert) context.getBean("simpleJdbcInsert");
    }

    private static final String QUERY_INSERT =
            "INSERT INTO Unit(name)\n" +
                    "VALUES (:name);";

    private static final String QUERY_GET_BY_PK = "SELECT id, name FROM Unit WHERE id=?;";
    private static final String QUERY_GET_ALL = "SELECT id, name FROM Unit;";
    private static final String QUERY_DELETE_BY_PK = "DELETE FROM Unit WHERE id=?;";
    private static final String QUERY_UPDATE = "UPDATE Unit SET name = ? WHERE id = ?;";

    @Override
    public void insert(Unit unit) {
        if (unit.getId() != null) {
            System.out.println("Еденица измерения id " + unit.getId() + " уже сохранена в базе");
            return;
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", unit.getName());
        int rows = simpleJdbcInsert.withTableName("Unit").execute(parameters);
        unit.setId(rows);
        System.out.println("Добавлена " + rows + " еденица измерения: " + unit.getName());
    }

    @Override
    public Unit getByPK(Integer primaryKey) {
        Object[] params = { primaryKey };
        int[] types = {Types.INTEGER};
        List <Unit> units = jdbcTemplate.query(QUERY_GET_BY_PK, params, types, new UnitMapper());
        if (units.isEmpty()) {
            return null;
        }
        return units.get(0);
    }

    @Override
    public List<Unit> getAll() {
        List <Unit> units = jdbcTemplate.query(QUERY_GET_ALL, new UnitMapper());
        return units;
    }

    @Override
    public void deleteByPK(Integer primaryKey) {
        Object[] params = { primaryKey };
        int[] types = {Types.INTEGER};
        int rows = jdbcTemplate.update(QUERY_DELETE_BY_PK, params, types);
        System.out.println("Удалена " + rows + " еденица измерения ");
    }

    @Override
    public void update(Unit unit) {
        if (unit.getId() == null) return;
        int rows = jdbcTemplate.update(QUERY_UPDATE, unit.getName(), unit.getId());
        System.out.println("Обновлена " + rows + " еденица измерения " + unit.getName());
    }
}
