import DAO.*;
import model.Ingredient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringJdbcConfiguration.class);
        ApplicationContextHolder applicationContextHolder =new ApplicationContextHolder(context);

        TableUtils tableUtils = context.getBean(TableUtils.class);
        tableUtils.createTables();
        Console console = new Console();// context.getBean("console", Console.class);
        console.start();


       /* createNewIngredient();
        createNewIngredient();
        createNewIngredient();
        showIngredients();*/


    }
   /* private static Ingredient createNewIngredient() {
        Scanner scanner = new Scanner(System.in);
        Ingredient ingredient = new Ingredient();
        System.out.print("\nВведите наименование нового ингредиента: ");
        String name = scanner.nextLine();
        ingredient.setName(name);
        return ingredient;
    }
    private static void showIngredients() {
        IngredientDao ingredientDao =new IngredientDaoImpl();
        List<Ingredient> ingredientList = ingredientDao.getAll();
        for (Ingredient ingredient : ingredientList) {
            System.out.println(ingredient.getId() + ". " + ingredient.getName());
        }
    }*/

}
