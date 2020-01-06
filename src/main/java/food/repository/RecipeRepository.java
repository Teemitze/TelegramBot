package food.repository;

import dataBase.ConnectionDB;
import food.Ingredient;
import food.Recipe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Random;

public class RecipeRepository {
    private SessionFactory sessionFactory = ConnectionDB.getSessionFactory();

    public void addRecipe(Recipe recipe) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(recipe);
        session.getTransaction().commit();
        session.close();
    }

    public void updateRecipe(String name, List<Ingredient> ingredients) {
        Recipe recipe = new Recipe(name, ingredients);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(recipe);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteRecipe(int id) {
        Session session = sessionFactory.openSession();
        Recipe recipe = session.createQuery("from Recipe where id=:id", Recipe.class).setParameter("id", id).uniqueResult();
        session.beginTransaction();
        session.delete(recipe);
        session.getTransaction().commit();
        session.close();
    }

    public List getAllRecipes() {
        Session session = sessionFactory.openSession();
        List recipes = session.createQuery("from Recipe").getResultList();
        session.close();
        return recipes;
    }

    public Recipe getOneRecipe() {
        List<Recipe> recipes = getAllRecipes();
        // Инициализируем генератор
        Random rnd = new Random(System.currentTimeMillis());
        // Получаем случайное число в диапазоне от min до max (включительно)
        int number = rnd.nextInt(recipes.size());

        return recipes.get(number);
    }

    public Recipe findRecipeName(String name) {
        Session session = sessionFactory.openSession();
        Recipe recipe = session.createQuery("from Recipe where name=:name", Recipe.class).setParameter("name", name).uniqueResult();
        session.close();
        return recipe;
    }

    public Recipe findRecipeByID(int id){
        Session session = sessionFactory.openSession();
        Recipe recipe = session.createQuery("from Recipe where id=:id", Recipe.class).setParameter("id", id).uniqueResult();
        session.close();
        return recipe;
    }
}