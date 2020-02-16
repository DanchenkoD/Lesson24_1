package model;

public class StructureEntry {
    private Ingredient ingredientRef;
    private Double amount;
    private Unit unitRef;

    public StructureEntry(Ingredient ingredientRef, Double amount, Unit unitRef) {
        this.ingredientRef = ingredientRef;
        this.amount = amount;
        this.unitRef = unitRef;
    }

    public StructureEntry() {

    }

    public Ingredient getIngredientRef() {
        return ingredientRef;
    }

    public void setIngredientRef(Ingredient ingredientRef) {
        this.ingredientRef = ingredientRef;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Unit getUnitRef() {
        return unitRef;
    }

    public void setUnitRef(Unit unitRef) {
        this.unitRef = unitRef;
    }

    @Override
    public String toString() {
        return "StructureEntry{" +
                "ingredientRef=" + ingredientRef +
                ", amount=" + amount +
                ", unitRef=" + unitRef +
                '}';
    }
}
