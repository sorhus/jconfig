package com.github.sorhus.jconfig.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.github.sorhus.jconfig.dao.DAO;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: anton.sorhus@gmail.com
 */
public class Migrator {

    @Parameter(names = {"-f", "--from"}, required = true)
    private String from;

    @Parameter(names = {"-t", "--to"}, required = true)
    private String to;

    public static void main(String[] args) throws Exception {
        Migrator migrator = new Migrator();
        new JCommander(migrator, args);
        migrator.init();
    }

    private Migrator() {}

    private void init() throws Exception {
        String fromContextFile = String.format("%s-context.xml", from);
        String toContextFile = String.format("%s-context.xml", to);
        ClassPathXmlApplicationContext fromContext = new ClassPathXmlApplicationContext(fromContextFile);
        ClassPathXmlApplicationContext toContext = new ClassPathXmlApplicationContext(toContextFile);
        DAO fromDao = fromContext.getBean("dao", DAO.class);
        DAO toDao = toContext.getBean("dao", DAO.class);
        toDao.putAll(fromDao.getAll());
    }
}