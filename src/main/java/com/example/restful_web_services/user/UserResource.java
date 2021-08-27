package com.example.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserResource {

  private final UserDaoService userDaoService;

  @Autowired
  public UserResource(UserDaoService userDaoService) {
    this.userDaoService = userDaoService;
  }

  @GetMapping()
  public List<User> retrieveAllUsers() {
    return userDaoService.findAll();
  }

  @GetMapping("/{id}")
  public EntityModel<User> retrieveUser(@PathVariable int id) {
    User user = userDaoService.findById(id);

    if (user == null) {
      throw new UserNotFoundException("id-" + id);
    }

    EntityModel<User> model = EntityModel.of(user);

    final WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).retrieveAllUsers());

    model.add(linkToUsers.withRel("all-users"));

    return model;
  }

  @PostMapping()
  public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
    final User savedUser = userDaoService.save(user);

    final URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedUser.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable int id) {
    final User user = userDaoService.deleteById(id);

    if (user == null) {
      throw new UserNotFoundException("id-" + id);
    }
  }

}
