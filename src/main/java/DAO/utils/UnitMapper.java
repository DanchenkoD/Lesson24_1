package DAO.utils;

import model.Unit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitMapper implements RowMapper<Unit> {

    @Override
    public Unit mapRow(ResultSet resultSet, int i) throws SQLException {
        Unit unit = new Unit();
        unit.setId(resultSet.getInt("id"));
        unit.setName(resultSet.getString("name"));
        return unit;
    }
}
