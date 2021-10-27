package store;

import model.Author;
import model.Book;
import model.Brand;
import model.Car;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book abc = Book.of("First Book");
            Book chemistry = Book.of("Chemistry textbook");
            Book physics  = Book.of("Physics textbook");
            Book maths = Book.of("Math textbook");

            Author ivan = Author.of("Ivanov Ivan");
            ivan.addBook(abc);
            ivan.addBook(maths);

            Author petr = Author.of("Petrov Petr");
            petr.addBook(maths);
            petr.addBook(chemistry);
            petr.addBook(physics);

            session.persist(ivan);
            session.persist(petr);

            Author author = session.get(Author.class, ivan.getId());
            session.remove(author);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}