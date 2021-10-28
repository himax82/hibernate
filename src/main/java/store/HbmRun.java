package store;

import model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {

    public static void main(String[] args) {
        Candidate candidat = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Vacancy vacancy1 = Vacancy.of("Tester", "Sberbank");
            Vacancy vacancy2 = Vacancy.of("Frontend", "Roscomnadzor");
            Vacancy vacancy3 = Vacancy.of("Backend", "Government");

            Base base = new Base();
            base.addVacancy(vacancy1);
            base.addVacancy(vacancy2);
            base.addVacancy(vacancy3);
            session.save(base);
            Candidate candidate = Candidate.of("Petr Java Backend", "1 year", 150_000);
            candidate.setBase(base);
            session.save(candidate);

            candidat = session.createQuery("select distinct c from Candidate c "
                    + "join fetch c.base vb "
                    + "join fetch vb.vacancies v "
                    + "where c.id = :ids", Candidate.class
            ).setParameter("ids", 10).uniqueResult();
            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        System.out.println(candidat);
    }
}