package com.telusko;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

// Second level cache implementation
public class App {

    public static void main(String[] args) {
        Alien a = null;

        // Creating session object
        Configuration con = new Configuration().configure().addAnnotatedClass(Alien.class);
        ServiceRegistry reg = new StandardServiceRegistryBuilder().applySettings(con.getProperties()).build();
        SessionFactory sessionFactory = con.buildSessionFactory(reg);

        System.out.println(sessionFactory);

        // First session
        try (Session session1 = sessionFactory.openSession()) {
            Transaction tx = session1.beginTransaction();
            a = (Alien) session1.get(Alien.class, 2);
            System.out.println(a);
            tx.commit();
        }

        // Second session (this will hit the second-level cache)
        try (Session session2 = sessionFactory.openSession()) {
            Transaction tx1 = session2.beginTransaction();
            a = (Alien) session2.get(Alien.class, 2);
            System.out.println(a);
            tx1.commit();
        }

        sessionFactory.close(); // Close the factory at the end of the application
    }
}
