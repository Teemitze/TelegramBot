package food.repository;

import dataBase.ConnectionDB;
import food.Ingredient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class IngredientRepository {
    private SessionFactory sessionFactory = ConnectionDB.getSessionFactory();

    public void addIngredient(Ingredient ingredient) {
        Session session = sessionFactory.openSession();
        session.save(ingredient);
        session.close();
    }

    public void updateIngredient(Ingredient ingredient) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(ingredient);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteIngredient(int id) {
        Session session = sessionFactory.openSession();
        Ingredient ingredient = session.createQuery("from Ingredient where id=:id", Ingredient.class).setParameter("id", id).uniqueResult();
        session.beginTransaction();
        session.delete(ingredient);
        session.getTransaction().commit();
        session.close();
    }
}
