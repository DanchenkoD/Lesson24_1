package model;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    private List<Integer> idList;
    private Recipe recipeRef;
    private List<StructureEntry> entryList = new ArrayList<>();

    public Structure(List<Integer> idList, Recipe recipeRef, List<StructureEntry> entryList) {
        this.idList = idList;
        this.recipeRef = recipeRef;
        this.entryList = entryList;
    }

    public Structure() {

    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public Recipe getRecipeRef() {
        return recipeRef;
    }

    public void setRecipeRef(Recipe recipeRef) {
        this.recipeRef = recipeRef;
    }

    public List<StructureEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<StructureEntry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "idList=" + idList +
                ", recipeRef=" + recipeRef +
                ", entryList=" + entryList +
                '}';
    }
}
