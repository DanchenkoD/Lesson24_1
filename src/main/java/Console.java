import DAO.*;
import model.*;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console {
    private ApplicationContext context ;
    private final StructureDao structureDao;
    private final IngredientDao ingredientDao;
    private final RecipeDao recipeDao;
    private final UnitDao unitDao;

    private Scanner scanner = new Scanner(System.in);

    private static final String MAIN_MENU =
            "\n# База рецептов #\n" +
                    "1. Поиск рецепта по имени (или части имени);\n" +
                    "2. Добавление рецепта;\n" +
                    "3. Удаление рецепта;\n" +
                    "4. Выход из программы.\n" +
                    "\nВведите номер операции: ";


    public Console() {
        this.context  = ApplicationContextHolder.getApplicationContext();;
        this.structureDao = (StructureDao) context.getBean("structureDao");
        this.ingredientDao = (IngredientDao) context.getBean("ingredientDao");
        this.recipeDao = (RecipeDao) context.getBean("recipeDao");
        this.unitDao = (UnitDao) context.getBean("unitDao");
    }

    public void start() {

        while (true) {
            System.out.print(MAIN_MENU);

            int userInput = 0;
            try {
                userInput = Integer.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите цифру 1-4");
                continue;
            }

            switch (userInput) {
                case 1:
                    searchRecipe();
                    break;
                case 2:
                    addRecipe();
                    break;
                case 3:
                    deleteRecipe();
                    break;
                case 4:
                    System.out.println("Всего доброго!");
                    return;
                default:
                    System.out.println("Введите цифру 1-4");
            }
        }
    }

    private void deleteRecipe() {
        System.out.println("\nУдаление рецепта");
        showAllRecipes();
        System.out.print("Введите id рецепта, который требуется удалить (0 - отмена): ");
        Integer userInput = null;
        try {
            userInput = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод");
            return;
        }
        if (userInput == 0) return;
        structureDao.deleteByRecipeID(userInput);
        System.out.println("Рецепт удален");
    }

    private void showAllRecipes() {
        List<Recipe> recipeList = recipeDao.getAll();
        for (Recipe recipe : recipeList) {
            System.out.println(recipe.getId() + ". " + recipe.getName());
        }
    }

    private void addRecipe() {
        Recipe recipe = new Recipe();

        System.out.print("Введите название рецепта: ");
        String recipeName = scanner.nextLine();
        recipe.setName(recipeName);

        System.out.print("Введите описание рецепта: ");
        String recipeDescription = scanner.nextLine();
        recipe.setDescription(recipeDescription);

        Structure structure = new Structure();
        structure.setRecipeRef(recipe);

        List<StructureEntry> compositionEntryList = new ArrayList<>();
        structure.setEntryList(compositionEntryList);

        while (true) {

            System.out.println("\nВвод состава рецепта");
            showIngredients();
            System.out.print("Введите id требуемого ингредиента, n для ввода нового, q для завершения редактирования: ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("q")) break;

            Ingredient ingredient;
            if (userInput.equalsIgnoreCase("n")){
                ingredient = createNewIngredient();
            } else {
                int ingredientId = 0;
                try {
                    ingredientId = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный ввод");
                    continue;
                }
                ingredient = ingredientDao.getByPK(ingredientId);
            }

            System.out.println("Введите единицу измерения ингредиента " + ingredient.getName() + " :");
            showUnits();
            System.out.print("Введите id требуемой ед. изм., n для ввода новой, q для завершения редактирования: ");
            userInput = scanner.nextLine();

            Unit unit;
            if (userInput.equalsIgnoreCase("q")) break;
            if (userInput.equalsIgnoreCase("n")){
                unit = createNewUnit();
            } else {
                int unitId = 0;
                try {
                    unitId = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный ввод");
                    continue;
                }
                unit = unitDao.getByPK(unitId);
            }

            System.out.println("Введите требуемое количество ингредиента (в " + unit.getName() + "): " );

            Double amount = null;
            try {
                amount = Double.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод");
                continue;
            }

            compositionEntryList.add(new StructureEntry(ingredient, amount, unit));

            System.out.println("Ингредиент добавлен (" + ingredient.getName() + " " + amount + " " + unit.getName() + ")");
        }

        structureDao.insert(structure);
        System.out.println("Новый рецепт успешно сохранен.");
    }

    private Unit createNewUnit() {
        Unit unit = new Unit();
        System.out.print("\nВведите наименование новой ед. изм.: ");
        String name = scanner.nextLine();
        unit.setName(name);
        return unit;
    }

    private Ingredient createNewIngredient() {
        Ingredient ingredient = new Ingredient();
        System.out.print("\nВведите наименование нового ингредиента: ");
        String name = scanner.nextLine();
        ingredient.setName(name);
        return ingredient;
    }

    private void showUnits() {
        List<Unit> unitList = unitDao.getAll();
        for (Unit unit : unitList) {
            System.out.println(unit.getId() + ". " + unit.getName());
        }
    }

    private void showIngredients() {
        List<Ingredient> ingredientList = ingredientDao.getAll();
        for (Ingredient ingredient : ingredientList) {
            System.out.println(ingredient.getId() + ". " + ingredient.getName());
        }
    }

    private void searchRecipe() {
        System.out.print("Введите название рецепта (или его часть): ");
        String searchName = scanner.nextLine();
        System.out.println("Поиск рецептов, название которых содержит: " + searchName);

        List<Structure> structureList = structureDao.searchByName(searchName);
        System.out.println("Найдено " + structureList.size() + " рецепт(ов)");
        printCompositionList(structureList);
    }

    private void printCompositionList(List<Structure> structureList) {
        for (Structure structure : structureList) {
            System.out.println("\nРецепт: " + structure.getRecipeRef().getName());
            System.out.println("Описание: " + structure.getRecipeRef().getDescription());
            System.out.println("Состав рецепта:");

            int i=0;
            for (StructureEntry structureEntry : structure.getEntryList()) {
                System.out.println((++i) + ". " + structureEntry.getIngredientRef().getName() +
                        " (" + structureEntry.getAmount() + " " + structureEntry.getUnitRef().getName() + ");");
            }
        }
    }
}
