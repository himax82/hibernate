package store;

import model.Brand;
import model.Car;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {

    public static void main(String[] args) {
        Brand toyota = Brand.of("Toyota");

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Car one = Car.of("Corolla");
            session.save(one);

            Car two = Car.of("Camry");
            session.save(two);

            Car three = Car.of("Rav4");
            session.save(three);

            Car four = Car.of("Prado");
            session.save(four);

            Car five = Car.of("TLC 300");
            session.save(five);

            toyota.addCar(session.load(Car.class, 1));
            toyota.addCar(session.load(Car.class, 2));
            toyota.addCar(session.load(Car.class, 3));
            toyota.addCar(session.load(Car.class, 4));
            toyota.addCar(session.load(Car.class, 5));

            session.save(toyota);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        System.out.println(toyota.getBrands().size());
    }
}