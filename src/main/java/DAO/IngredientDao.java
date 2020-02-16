package DAO;

import model.Ingredient;

import java.util.List;

public interface IngredientDao {
    void insert(Ingredient ingredient);

    Ingredient getByPK(Integer primaryKey);

    List<Ingredient> getAll();

    void deleteByPK(Integer primaryKey);

    void update(Ingredient ingredient);
}
