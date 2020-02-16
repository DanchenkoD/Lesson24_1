package DAO;

import model.Structure;

import java.util.List;

public interface StructureDao {
    void insert(Structure structure);

    Structure getByRecipeID(Integer recipeID);

    List<Structure> getAll();

    void deleteByRecipeID(Integer recipeID);

    List<Structure> searchByName(String name);
}
