package store;

import model.Brand;
import model.Candidate;
import model.Car;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Candidate candidate1 = Candidate.of("Иван", "1 год", 80_000);
            Candidate candidate2 = Candidate.of("Максим", "6 месяцев", 60_000);
            Candidate candidate3 = Candidate.of("Евгений", "5 лет", 120_000);

            session.save(candidate1);
            session.save(candidate2);
            session.save(candidate3);

            List<Candidate> list = session.createQuery("from Candidate").list();
            list.forEach(System.out::println);

            Candidate evgen = (Candidate) session.createQuery("from Candidate c where c.salary = :sal")
                    .setParameter("sal", "6 месяцев")
                    .uniqueResult();
            System.out.println(evgen);

            session.createQuery("update from Candidate c set c.salary = :salary where c.id = :id")
                    .setParameter("salary", 90_000)
                    .setParameter("id", 1)
                    .executeUpdate();

            session.createQuery("delete from Candidate where id = :id")
                    .setParameter("id", 1)
                    .executeUpdate();

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}