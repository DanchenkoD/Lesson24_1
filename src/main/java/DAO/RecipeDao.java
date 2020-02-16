package DAO;

import model.Recipe;

import java.util.List;

public interface RecipeDao {
    void insert(Recipe recipe);

    Recipe getByPK(Integer primaryKey);

    List<Recipe> getAll();

    void deleteByPK(Integer primaryKey);

    void update(Recipe recipe);
}
