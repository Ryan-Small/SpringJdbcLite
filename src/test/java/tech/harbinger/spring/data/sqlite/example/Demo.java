package tech.harbinger.spring.data.sqlite.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
public class Demo implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(final String[] args) {
        SpringApplication.run(Demo.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users(name VARCHAR(100), age INT)");
        jdbcTemplate.execute("INSERT INTO users VALUES ('John Doe', 31)");
        jdbcTemplate.execute("INSERT INTO users VALUES ('Jane Doe', 32)");
        final List<User> users = jdbcTemplate.query("SELECT * FROM users", (resultSet, rowNum) ->
                new User(resultSet.getString("name"), resultSet.getInt("age")));
        users.forEach(System.out::println);
    }

    private class User {
        private String name;
        public int age;

        public User(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getAge() {
            return this.age;
        }

        public void setAge(final int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person [name=" + name + ", age=" + age + "]";
        }
    }
}
