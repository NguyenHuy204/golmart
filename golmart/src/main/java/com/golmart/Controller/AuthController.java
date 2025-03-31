package com.golmart.Controller;

import com.golmart.config.jwt.JwtTokenUtil;
import com.golmart.dto.*;
import com.golmart.entity.Account;
import com.golmart.service.AuthService;
import com.golmart.service.UserService;
import com.golmart.utils.MessageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;


//API xử lý phần xác thực
@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthController {
    // Injecting the services into the controller.
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;

    private static boolean isPasswordLengthInvalid(String password) {
        return password.length() < 4;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDto) {
        try {
            Account account = userService.registerUser(userDto);
            return ResponseEntity.ok().body(account);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
        }
    }

    @PostMapping("/register-staff")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerStaff(@Valid @RequestBody UserDTO userDto) {
        try{
            Account account = userService.registerStaff(userDto);
            return ResponseEntity.ok().body(account);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            Assert.notNull(authenticationRequest.getUsername(), MessageUtils.getMessage("not.null", "username"));
            Assert.notNull(authenticationRequest.getPassword(), MessageUtils.getMessage("not.null", "password"));
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            final UserDetails userDetails = userService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);
            UserDTO userDTO = userService.findUserByEmail(authenticationRequest.getUsername());
//            if(userDTO.getUsername().equals("thdtt"))
//                userDTO.setRole(Constants.ROLE.ADMIN);
            return ResponseEntity.ok(new JwtResponse(token, userDTO));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
//    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
//        try {
//            return ResponseEntity.ok(ResponseError.builder().message(userService.changePassword(changePasswordDTO)).build());
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
//        }
//    }
//
//    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
//    public ResponseEntity<?> forgotPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
//        try {
//            return ResponseEntity.ok(ResponseError.builder().message(userService.forgotPassword(changePasswordDTO)).build());
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
//        }
//    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Sai tài khoản hoặc mật khẩu", e);
        }
    }

}
