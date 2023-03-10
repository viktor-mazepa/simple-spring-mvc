package com.mazasoft.springcourse.dao;

import com.mazasoft.springcourse.config.DbManager;
import com.mazasoft.springcourse.models.Person;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class PersonDAO {

    Logger LOGGER = Logger.getLogger(PersonDAO.class);

    @Autowired
    DbManager dbManager;

    public Collection<Person> index() {
        Collection<Person> people = new ArrayList<>();
        try {
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_PEOPLE_QUERY);
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
                people.add(person);
            }
        } catch (SQLException e) {
            LOGGER.error("Exception in PersonDAO.index", e);
            throw new RuntimeException(e);
        }
        return people;
    }


    public Person show(int id) {
        try {
            PreparedStatement statement = dbManager.getConnection().prepareStatement(GET_PERSON_BY_ID_QUERY);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));
            return person;
        } catch (SQLException e) {
            LOGGER.error("Exception in PersonDAO.show", e);
            throw new RuntimeException(e);
        }
    }

    public void save(Person person) {
        try {
            PreparedStatement statement = dbManager.getConnection().prepareStatement(INSERT_NEW_PERSON_QUERY);
            statement.setString(1, person.getName());
            statement.setInt(2, person.getAge());
            statement.setString(3, person.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exception in PersonDAO.save", e);
            throw new RuntimeException(e);
        }

    }

    public void update(int id, Person person) {
        try {
            PreparedStatement statement = dbManager.getConnection().prepareStatement(UPDATE_PERSON_BY_ID_QUERY);
            statement.setString(1, person.getName());
            statement.setInt(2, person.getAge());
            statement.setString(3, person.getEmail());
            statement.setInt(4, person.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exception in PersonDAO.update", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement statement = dbManager.getConnection().prepareStatement(DELETE_PERSON_BY_ID_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exception in PersonDAO.delete", e);
            throw new RuntimeException(e);
        }
        //people.removeIf(person -> person.getId()==id);
    }

    private String GET_ALL_PEOPLE_QUERY = "select * from person";
    private String INSERT_NEW_PERSON_QUERY = "insert into person values((select max(id)+1 as id from person), ?,?,?)";
    private String GET_PERSON_BY_ID_QUERY = "select * from person where id = ?";
    private String UPDATE_PERSON_BY_ID_QUERY = "update person set name = ?, age = ?, email = ? where id = ?";
    private String DELETE_PERSON_BY_ID_QUERY = "delete from person where id = ?";
}
