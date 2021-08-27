package com.example.restful_web_services.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserDaoService {

  private final List<User> users = new ArrayList<>();
  private int userCount = 0;

  {
    users.add(new User(++userCount, "Adam", new Date()));
    users.add(new User(++userCount, "Erik", new Date()));
    users.add(new User(++userCount, "Jack", new Date()));
  }


  public List<User> findAll() {
    return Collections.unmodifiableList(users);
  }

  public User save(User user) {

    if (user.getId() == null) {
      user.setId(++userCount);
      users.add(user);
    } else {
      final User foundUser = findById(user.getId());

      if (foundUser == null) {
        throw new RuntimeException();
      }

      foundUser.setBirthDate(user.getBirthDate());
      foundUser.setName(user.getName());

    }

    return user;
  }

  public User findById(int id) {
    User user = users.stream().filter(tempUser -> tempUser.getId() == id).findFirst().orElse(null);

    if (user == null) {
      throw new UserNotFoundException("id-" + id);
    }

    return user;

  }

  public User deleteById(int id) {
    Iterator<User> iterator = users.iterator();

    while (iterator.hasNext()) {
      User user = iterator.next();

      if (user.getId() == id) {
        iterator.remove();
        return user;
      }
    }
    return null;
  }
}
