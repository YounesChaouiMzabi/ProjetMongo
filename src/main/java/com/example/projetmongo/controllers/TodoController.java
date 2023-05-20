package com.example.projetmongo.controllers;


import com.example.projetmongo.models.Todo;
import com.example.projetmongo.models.TodoItem;
import com.example.projetmongo.models.User;
import com.example.projetmongo.repositories.TodoItemRepository;
import com.example.projetmongo.repositories.TodoRepository;
import com.example.projetmongo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TodoRepository todoRepository;
    @Autowired
    TodoItemRepository todoItemRepository;

    //////////////////////////////////////////////////////
    ////////////TODO USERS APIS/////////////////
    //////////////////////////////////////////////////////

    //Fonction pour creer un utilisateur

    @PostMapping("/register")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    //cette fonction verifie si l'utilisateur existe dans la bdd

    @PostMapping("/authenticate")
    public ResponseEntity<User> loginUser(@RequestHeader String Authorization) {
        String[] up = Authorization.split(":");
        User user = userRepository.findOneUser(up[0]);
        if (user != null){
            System.out.println("ana daba");
            return ResponseEntity.ok().body(user);
        }
        else
            System.out.println("ana t excutit");
            return ResponseEntity.notFound().build();

    }

    //////////////////////////////////////////////////////
    ////////////TODO LISTS APIS/////////////////
    //////////////////////////////////////////////////////

    // retourne une liste des todos en fonction de l'utilisateur entré en parametre
    @GetMapping("/todos/{ownerid}")
    public List<Todo> getAllUsersTodo(@PathVariable("ownerid") String ownerid) {
        return todoRepository.findAllUsersList(ownerid);
    }

    //fonction pour créer un toddo

    @PostMapping("/todos")
    public ResponseEntity createTodo(@RequestBody Todo todo) {
        try {
            todoRepository.insert(todo);
            return ResponseEntity.ok().body(true);
        }catch (Exception ignored){
            return ResponseEntity.ok().body(false);
        }
    }

    @GetMapping(value="/todo/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") String id) {
        return todoRepository.findById(id)
                .map(todo -> ResponseEntity.ok().body(todo))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value="/todos/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable("id") String id,
                                       @RequestBody Todo todo) {
        return todoRepository.findById(id)
                .map(todoData -> {
                    todoData = todo;
                    Todo updatedTodo = todoRepository.save(todoData);
                    return ResponseEntity.ok().body(true);
                }).orElse(ResponseEntity.ok().body(false));
    }

    @DeleteMapping(value="/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.deleteById(id);
                    return ResponseEntity.ok().body(true);
                }).orElse(ResponseEntity.ok().body(false));
    }

    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    ////////////TODO ITEMS APIS/////////////////
    //////////////////////////////////////////////////////

    @GetMapping(value="/getitems/{parentListId}")
    public List<TodoItem> getTodoItemByOwnerId(@PathVariable("parentListId") String id) {
        return todoItemRepository.findAllListsItems(id);
    }

    @PostMapping("/additem")
    public ResponseEntity createTodoItem(@RequestBody TodoItem todoItem) {
        try {
            if (!todoItem.getLinkedItemId().equals("")){
                Optional<TodoItem> tempitem = todoItemRepository.findById(todoItem.getLinkedItemId());
                if(todoItem.getCompleted() == tempitem.get().getCompleted() || tempitem.get().getCompleted()){
                    todoItem.setLinkedItemName(tempitem.get().getTitle());
                    todoItemRepository.save(todoItem);
                    return ResponseEntity.ok().body(true);
                }else {
                    return ResponseEntity.ok().body(false);
                }
            } else {
                todoItemRepository.save(todoItem);
                return ResponseEntity.ok().body(true);
            }
        }catch (Exception ignored){
            System.out.println("je suis executé");
            return ResponseEntity.ok().body(false);
        }
    }

    @GetMapping(value="/oneitem/{id}")
    public ResponseEntity<TodoItem> getTodoItemById(@PathVariable("id") String id) {
        return todoItemRepository.findById(id)
                .map(todoItem -> ResponseEntity.ok().body(todoItem))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value="/upitem/{id}")
    public ResponseEntity<?> updateTodoItem(@PathVariable("id") String id,
                                             @RequestBody TodoItem todoItem) {
        if (todoItem.getId().equals(todoItem.getLinkedItemId()) ){
            return ResponseEntity.ok().body(false);
        }
        if (todoItem.getLinkedItemId() != null){
            Optional<TodoItem> tempitem = todoItemRepository.findById(todoItem.getLinkedItemId());
            if(todoItem.getCompleted() == tempitem.get().getCompleted() || tempitem.get().getCompleted()){
                return todoItemRepository.findById(id)
                        .map(todoItemData -> {
                            todoItemData = todoItem;
                            todoItemData.setLinkedItemName(tempitem.get().getTitle());
                            TodoItem updatedTodoItem = todoItemRepository.save(todoItemData);
                            return ResponseEntity.ok().body(true);
                        }).orElse(ResponseEntity.ok().body(false));
            }else {
                return ResponseEntity.ok().body(false);
            }
        }
        else {
            return todoItemRepository.findById(id)
                    .map(todoItemData -> {
                        todoItemData = todoItem;
                        todoItemData.setLinkedItemName("");
                        TodoItem updatedTodoItem = todoItemRepository.save(todoItemData);
                        return ResponseEntity.ok().body(true);
                    }).orElse(ResponseEntity.ok().body(false));
        }
    }

    @DeleteMapping(value="/delitem/{id}")
    public ResponseEntity<?> deleteTodoItem(@PathVariable("id") String id) {
        return todoItemRepository.findById(id)
                .map(todoItem -> {
                    todoItemRepository.deleteById(id);
                    return ResponseEntity.ok().body(true);
                }).orElse(ResponseEntity.ok().body(false));
    }
}
