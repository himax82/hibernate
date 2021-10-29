package store;

import static org.junit.Assert.*;

import model.Order;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void wipeTables() throws SQLException {
        pool.getConnection().prepareStatement("DROP TABLE orders;").executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveFiveOrderAndFindAll() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        store.save(Order.of("name3", "description3"));
        store.save(Order.of("name4", "description4"));
        store.save(Order.of("name5", "description5"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(5));
        assertThat(all.get(4).getName(), is("name5"));
    }

    @Test
    public void whenUpdateOrder() {
        OrdersStore store = new OrdersStore(pool);
        Order order1 = Order.of("name1", "description1");
        Order order2 = Order.of("name2", "description2");
        store.save(order1);
        store.update(order2, order1.getId());
        assertThat(store.findById(order1.getId()).getName(), is("name2"));
    }

    @Test
    public void whenFindByNameOrder() {
        OrdersStore store = new OrdersStore(pool);
        Order order1 = Order.of("name1", "description1");
        Order order2 = Order.of("name2", "description2");
        Order order3 = Order.of("name3", "description3");
        store.save(order1);
        store.save(order2);
        store.save(order3);
        assertThat(store.findByName(order2.getName()).getDescription(), is("description2"));
    }
}