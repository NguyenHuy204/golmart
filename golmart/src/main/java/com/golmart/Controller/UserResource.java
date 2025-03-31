//package com.golmart.Controller;
//
//import com.golmart.service.AuthService;
//import com.golmart.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//
//@RestController
//@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
//public class UserResource {
//
//    private final UserService userService;
//
//    @Autowired
//    private AuthService authService;
//
//    public UserResource(final UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(value = "username", required = false)String username,
//                                                     @RequestParam(value = "name",required = false) String name,
//                                                     @RequestParam(value = "role",required = false) Long role) {
//        return ResponseEntity.ok(userService.findByOption(username,name,role));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Account> getUser(@PathVariable final Long id) {
//        return ResponseEntity.ok(userService.get(id));
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<UserDTO> me() {
//        return ResponseEntity.ok(authService.getCurrentUserNewest());
//    }
//
//    @GetMapping("/info/{id}")
//    public ResponseEntity<UserDTO> infoOf(@PathVariable Long id) {
//        return ResponseEntity.ok(authService.getCurrentUser(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
//        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateUser(@PathVariable final Long id,
//                                           @RequestBody @Valid final UserDTO userDTO) {
//        userService.update(id, userDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
//        userService.update(userDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/staffs")
//    public ResponseEntity<?> staff() {
//        return ResponseEntity.ok(staffRepository.findStaffByRole(Constants.ROLE.NHAN_VIEN_TU_VAN));
//    }
//
//    @GetMapping("/staff-buy")
//    public ResponseEntity<?> staffBuy() {
//        return ResponseEntity.ok(staffRepository.findStaffByRole(Constants.ROLE.NHAN_VIEN_MUA_HANG));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
//        userService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//}
