package com.golmart.service;

import com.golmart.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AuthService {

    /**
     * Lấy id của người dùng hiện tại từ security context
     *
     * @return The current user's id.
     */
    Long getCurrentUserId();


    /**
     * Lấy ID của người dùng hiện tại, sau đó tìm người dùng có ID đó và nếu bạn không thể tìm thấy, hãy ném 404.
     *
     * @return The current user's id.
     */
    UserDTO getCurrentUser();
    UserDTO getCurrentUserNewest();
    UserDTO getCurrentUser(HttpServletRequest request);
    UserDTO getCurrentUser(Long accountId);
}