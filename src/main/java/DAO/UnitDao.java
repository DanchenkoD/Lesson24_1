package DAO;

import model.Unit;

import java.util.List;

public interface UnitDao {
    void insert(Unit unit);

    Unit getByPK(Integer primaryKey);

    List<Unit> getAll();

    void deleteByPK(Integer primaryKey);

    void update(Unit unit);
}
