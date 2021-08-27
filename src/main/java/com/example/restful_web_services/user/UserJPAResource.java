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
@RequestMapping("/jpa/users")
public class UserJPAResource {

  private final UserRepository userRepository;
  private final UserDaoService userDaoService;

  @Autowired
  public UserJPAResource(UserRepository userRepository, UserDaoService userDaoService) {
    this.userRepository = userRepository;
    this.userDaoService = userDaoService;
  }

  @GetMapping()
  public List<User> retrieveAllUsers() {
    return this.userRepository.findAll();
  }

  @GetMapping("/{id}")
  public EntityModel<User> retrieveUser(@PathVariable int id) {
    User user = userRepository.findById(id).orElseGet(null);

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
    final User savedUser = userRepository.save(user);

    final URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedUser.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable int id) {
    userRepository.deleteById(id);
  }

}
