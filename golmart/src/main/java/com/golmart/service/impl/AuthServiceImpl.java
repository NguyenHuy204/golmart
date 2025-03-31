package com.golmart.service.impl;

import com.golmart.dto.UserDTO;
import com.golmart.entity.Account;
import com.golmart.entity.UserDetailsImpl;
import com.golmart.repos.AccountRepository;
import com.golmart.service.AuthService;
import com.golmart.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import com.golmart.config.jwt.JwtTokenUtil;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    /**
     * Lấy id của người dùng hiện tại từ security context
     *
     * @return The current user's id.
     */
    @Override
    public Long getCurrentUserId() {
        UserDetailsImpl u = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return u.getUserId();
    }


    /**
     * Lấy ID của người dùng hiện tại, sau đó tìm người dùng có ID đó và nếu bạn không thể tìm thấy, hãy ném 404.
     *
     * @return The current user's id.
     */
    @Override
    public UserDTO getCurrentUser() {
        UserDetailsImpl u = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = u.getAccount();

        return new UserDTO(account);
    }
    @Override
    public UserDTO getCurrentUserNewest() {
        UserDetailsImpl u = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getCurrentUser(u.getAccount().getId());
    }
    @Override
    public UserDTO getCurrentUser(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader("Authorization");
        String username           = null;
        String jwtToken           = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        } else {
            return new UserDTO();
        }
        if (username != null) {
            UserDetails userDetails = loadUserByUsername(username);
            if (!jwtTokenUtil.validateToken(jwtToken, userDetails, username)) {
                return new UserDTO();
            }
            Account account = accountRepository.findByEmailOrUsername(username);
            return getCurrentUser(account.getId());
        }
        return new UserDTO();
    }
    @Override
    public UserDTO getCurrentUser(Long accountId) {
        List<String[]> accountInfos = accountRepository.findInfoById(accountId);
        if (accountInfos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String[] accountInfo = accountInfos.get(0);

        Account account = Account.builder()
                .id(accountId)
                .username(accountInfo[14])
                .email(accountInfo[4])
                .fullName(accountInfo[5])
                .address(accountInfo[1])
                .phone(accountInfo[8])
                .inventoryId(accountInfo[6])
                .serviceTypeId(accountInfo[10])
                .status(Integer.parseInt(accountInfo[11]))
//                .role(Long.parseLong(accountInfo[9]))
                .build();

        UserDTO userDTO = new UserDTO(account);
        return userDTO;
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmailOrUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(account);
    }
}
