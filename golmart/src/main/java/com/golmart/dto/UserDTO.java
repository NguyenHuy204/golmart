package com.golmart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.golmart.entity.Account;
import com.golmart.entity.Role;
import com.golmart.utils.Constants;
import lombok.*;
import org.springframework.util.ObjectUtils;

// data to object
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String password;
    private String passwordNew;

    private String fullName;

    private String address;

    private String phone;

    private Role role;

    public Role getRole() {
        return this.role == null ? Role.USER : this.role;
    }

    public UserDTO(Account account) {
        if (account == null) return;
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.fullName = account.getFullName();
        this.address = account.getAddress();
        this.phone = account.getPhone();
        this.id = account.getId();
        this.role = account.getRole();
    }
    public UserDTO(Account account, Double availableBalance) {
        if (account == null) return;
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.fullName = account.getFullName();
        this.address = account.getAddress();
        this.phone = account.getPhone();
        this.id = account.getId();
        this.role = account.getRole();
    }

    public interface CustomerData {
        Long getId();
        String getUsername();
        String getAddress();
        String getPhone();
        String getFullName();
        String getEmail();
        String getCustomerNeeds();
        Boolean getCutomerFromData();
        String getNote();
        Long getStaffId();
        Double getAvailableBalance();
    }

    public UserDTO(CustomerData customerData) {
        this.id = customerData.getId();
        this.email = customerData.getEmail();
        this.username = customerData.getUsername();
        this.fullName = customerData.getFullName();
        this.address = customerData.getAddress();
        this.phone = customerData.getPhone();
    }

    public UserDTO(Long id, String email, String username, String customerNeeds,
                   String fullName, String address, String phone, Boolean isCustomerFromData,
                   String note, Long staffId, Double availableBalance, Long customerId) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
    }

    public UserDTO(Object[] u){
        this.username = ObjectUtils.isEmpty(u[0])?"":(String)u[0] ;
        this.fullName = ObjectUtils.isEmpty(u[1])?"":(String)u[1];
        this.address = ObjectUtils.isEmpty(u[3])?"":(String)u[3];
        this.phone = ObjectUtils.isEmpty(u[4])?"":(String)u[4];
        this.id = ObjectUtils.isEmpty(u[6])?null:Long.parseLong( u[6].toString());

    }
}
