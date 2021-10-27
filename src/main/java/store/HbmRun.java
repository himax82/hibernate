package store;

import model.Brand;
import model.Car;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmRun {

    public static void main(String[] args) {
        List<Brand> list;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Brand honda = Brand.of("Honda");
            Car crv = Car.of("CRV", honda);
            Car accord = Car.of("Accord", honda);
            Car pilot = Car.of("Pilot", honda);
            Brand mazda = Brand.of("Mazda");
            Car cx5 = Car.of("CX-5", mazda);
            Car rx8 = Car.of("RX-8", mazda);
            honda.addCar(crv);
            honda.addCar(accord);
            honda.addCar(pilot);
            mazda.addCar(cx5);
            mazda.addCar(rx8);
            session.save(honda);
            session.save(mazda);
            session.save(crv);
            session.save(accord);
            session.save(pilot);
            session.save(cx5);
            session.save(rx8);
            list = session.createQuery("from Brand ").list();
            for (Brand brand : list) {
                for (Car car : brand.getCars()) {
                    System.out.println(car);
                }
            }
            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}