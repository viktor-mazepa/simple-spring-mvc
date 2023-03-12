package com.mazasoft.springcourse.dao;

import com.mazasoft.springcourse.models.Person;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    Logger LOGGER = Logger.getLogger(PersonDAO.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Collection<Person> index() {
        return jdbcTemplate.query(GET_ALL_PEOPLE_QUERY, new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query(GET_PERSON_BY_ID_QUERY, new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);

    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query(GET_PERSON_BY_EMAIL_QUERY, new Object[]{email}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update(INSERT_NEW_PERSON_QUERY, person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    public void update(int id, Person person) {
        jdbcTemplate.update(UPDATE_PERSON_BY_ID_QUERY, person.getName(), person.getAge(), person.getEmail(),person.getAddress(), person.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(DELETE_PERSON_BY_ID_QUERY, id);
    }


    /**
     * Batch insert testing
     */
    public void testMultipleUpdate() {
        Collection<Person> people = create1000persons();
        long before = System.currentTimeMillis();
        for (Person person : people) {
            jdbcTemplate.update(INSERT_NEW_PERSON_WITH_ID_QUERY, person.getName(), person.getAge(), person.getEmail(), person.getAddress());
        }
        long after = System.currentTimeMillis();

        LOGGER.info("PersonDAO.testMultipleUpdate: operation_duration = " + (after - before) + " ms");

    }

    private List<Person> create1000persons() {
        List<Person> people = new ArrayList<>();
        for (int i = 10; i < 1010; i++) {
            people.add(new Person(i, "Name" + i, 34, "test" + i + "gmail.com", "some_address"));
        }
        return people;
    }

    public void testBatchUpdate() {
        List<Person> people = create1000persons();
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(INSERT_NEW_PERSON_WITH_ID_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, people.get(i).getName());
                preparedStatement.setInt(2, people.get(i).getAge());
                preparedStatement.setString(3, people.get(i).getEmail());
                preparedStatement.setString(4, people.get(i).getAddress());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long after = System.currentTimeMillis();
        LOGGER.info("PersonDAO.testBatchUpdate: operation_duration = " + (after - before) + " ms");

    }

    private String GET_ALL_PEOPLE_QUERY = "select * from person order by id";
    private String INSERT_NEW_PERSON_QUERY = "insert into person(name, age, email, address) values(?,?,?,?)";
    private String INSERT_NEW_PERSON_WITH_ID_QUERY = "insert into person (name, age, email, address) values( ?,?,?,?)";
    private String GET_PERSON_BY_ID_QUERY = "select * from person where id = ?";
    private String UPDATE_PERSON_BY_ID_QUERY = "update person set name = ?, age = ?, email = ?, address=? where id = ?";
    private String DELETE_PERSON_BY_ID_QUERY = "delete from person where id = ?";
    private String GET_PERSON_BY_EMAIL_QUERY = "select * from person where email=?";

}
