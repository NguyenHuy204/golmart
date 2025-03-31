package com.golmart.service;

import com.golmart.dto.UserDTO;
import com.golmart.entity.Account;
import com.golmart.entity.Role;
import com.golmart.entity.UserDetailsImpl;
import com.golmart.repos.AccountRepository;
import com.golmart.utils.Constants;
import com.golmart.utils.DataUtils;
import com.golmart.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserService implements UserDetailsService {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AccountRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;
    @Autowired
    private AccountRepository accountRepository;


    public UserService(final AccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user -> mapToDTO(user, new UserDTO())).collect(Collectors.toList());
    }

    public List<UserDTO> findByOption(String username, String name, Long role) {
        username = ObjectUtils.isEmpty(username)?null:username;
        name = ObjectUtils.isEmpty(name)?null:name;
        return userRepository.findAllByOption(username,name,role).stream().map(user -> mapToDTO(user, new UserDTO())).collect(Collectors.toList());
    }

    public Account get(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final UserDTO userDTO) {
        final Account account = new Account();
        mapToEntity(userDTO, account);
        return userRepository.save(account).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        Account account = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        mapToEntity(userDTO, user);

        account.setAddress(userDTO.getAddress());
        account.setPhone(userDTO.getPhone());

        userRepository.save(account);
    }

    public void update(UserDTO userDTO) {
        Account account = userRepository.findById(userDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        account = mapToEntity(userDTO, account);
        if (userDTO.getPasswordNew() != null && !userDTO.getPasswordNew().isEmpty()) {
            account.setPassword(passwordEncoder.encode(userDTO.getPasswordNew()));
        }

        userRepository.save(account);
    }

    public Account registerUser(UserDTO userDto) {
        //validate user
        Assert.notNull(userDto.getPassword(), MessageUtils.getMessage("not.null", "Mật khẩu"));
        Assert.notNull(userDto.getEmail(), MessageUtils.getMessage("not.null", "Email"));
        Assert.isTrue(DataUtils.patternMatches(userDto.getEmail(), Constants.REGEX_MAIL_PATTERN), MessageUtils.getMessage("not.true", "Email"));

        String username = userDto.getUsername();
        Long countNumber = userRepository.countDataAccount(username.toLowerCase(Locale.ROOT));
        String nextVal = "";
        if (0 != countNumber){
            throw new IllegalArgumentException(MessageUtils.getMessage("account.exist.account"));

        }

        Account account = new Account();
        account.setUsername(username.concat(nextVal));
        account.setEmail(userDto.getEmail().toLowerCase(Locale.ROOT));
        account.setFullName(userDto.getFullName());
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        account.setPassword(encryptedPassword);
        account.setAddress(userDto.getAddress());
        account.setPhone(userDto.getPhone());
        account.setStatus(Constants.STATUS.ACTIVE);
        account.setRole(Role.USER);
        account = userRepository.save(account);

        return account;
    }

    public Account registerStaff(UserDTO userDto) {
        //validate user
        Assert.notNull(userDto.getPassword(), MessageUtils.getMessage("not.null", "Mật khẩu"));
        Assert.notNull(userDto.getEmail(), MessageUtils.getMessage("not.null","Email"));
        Assert.notNull(userDto.getPhone(), MessageUtils.getMessage("not.null","Số điện thoại"));
        Assert.notNull(userDto.getRole(), MessageUtils.getMessage("not.null","Quyền nhân viên"));
        Assert.isTrue(!(Constants.ROLE.ADMIN.equals(userDto.getRole())), MessageUtils.getMessage("item.exist","Quyền nhân viên"));
        Assert.isTrue(DataUtils.patternMatches(userDto.getEmail(),Constants.REGEX_MAIL_PATTERN), MessageUtils.getMessage("not.true","Email"));
        Assert.isTrue(DataUtils.isNumber(userDto.getPhone()), MessageUtils.getMessage("not.true","Số điện thoại"));
        List<Account> accountExist = userRepository.findAllByEmail(userDto.getEmail());
        if (!accountExist.isEmpty()){
            throw new IllegalArgumentException(MessageUtils.getMessage("account.exist.account"));
        }
        String username = userDto.getEmail().split("@")[0];
        // đếm số lượng username có tên trùng
        Long countNumber = userRepository.countDataAccount(username.toLowerCase(Locale.ROOT));
        String nextVal = "";
        if (0 != countNumber){
            // cộng 1 đơn vị cho user hiện tại
             nextVal = String.valueOf(countNumber);
        }

        Account account = new Account();
        account.setUsername(username.concat(nextVal));
        account.setEmail(userDto.getEmail().toLowerCase(Locale.ROOT));
        account.setFullName(userDto.getFullName());
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        account.setPassword(encryptedPassword);
        account.setPhone(userDto.getPhone());
        account.setStatus(Constants.STATUS.ACTIVE);
        account.setRole(userDto.getRole());
        Account accountSave = userRepository.save(account);
        return account;
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final Account account, UserDTO userDTO) {
        userDTO = modelMapper.map(account, UserDTO.class);

        return userDTO;
    }

    private Account mapToEntity(final UserDTO userDTO, Account account) {
        account = modelMapper.map(userDTO, Account.class);
        return account;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByEmailOrUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(account);
    }

    public UserDTO findUserByEmail(String email) throws UsernameNotFoundException {
        Account account = userRepository.findByEmailOrUsername(email);
        if (account == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDTO userDTO = new UserDTO();
        modelMapper.map(account, userDTO);
        userDTO.setPassword(null);
        return userDTO;
    }
//
//    public String changePassword(ChangePasswordDTO changePasswordDTO){
//        if(ObjectUtils.isEmpty(changePasswordDTO.getEmail())){
//            changePasswordDTO.setEmail(authService.getCurrentUser().getEmail());
//        }
//        Assert.notNull(changePasswordDTO.getEmail(), MessageUtils.getMessage("not.null","username"));
//        Assert.notNull(changePasswordDTO.getNewPassword(), MessageUtils.getMessage("not.null","Mật khẩu mới"));
//        Assert.notNull(changePasswordDTO.getOldPassword(), MessageUtils.getMessage("not.null","Mật khẩu"));
//        Account account = userRepository.findByEmail(changePasswordDTO.getEmail());
//        if (ObjectUtils.isEmpty(account)) {
//            throw new IllegalArgumentException(MessageUtils.getMessage("item.change.password.not.exist",changePasswordDTO.getEmail()));
//        }
//        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), account.getPassword())) {
//            throw new IllegalArgumentException(MessageUtils.getMessage("password.not.match"));
//        }
//        account.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
//        userRepository.save(account);
//        return MessageUtils.getMessage("password.change.success");
//
//    }
//
//    public String forgotPassword(ChangePasswordDTO changePasswordDTO){
//        Assert.notNull(changePasswordDTO.getEmail(), MessageUtils.getMessage("not.null","Email"));
//        Account account = userRepository.findByEmail(changePasswordDTO.getEmail());
//        if (ObjectUtils.isEmpty(account)) {
//            throw new IllegalArgumentException(MessageUtils.getMessage("item.forgot.password.not.exist",changePasswordDTO.getEmail()));
//        }
//        String newPass = RandomPasswordGenerator.generateRandomPassword(12);
//        account.setPassword(passwordEncoder.encode(newPass));
//        userRepository.save(account);
//        String content = MessageUtils.getMessage(Constants.MAIL_CONTENT.SUBJECT_FORGOT_PASS_SUCCESS);
//        String body = MessageUtils.getMessage(Constants.MAIL_CONTENT.BODY_FORGOT_PASS_SUCCESS);
//        String bodyFormat = String.format(body,changePasswordDTO.getEmail(),newPass);
//        sendMail(changePasswordDTO.getEmail(),content,bodyFormat);
//        return MessageUtils.getMessage("password.forgot.success");
//
//    }
//
//    private void sendMail(String email, String content, String bodyFormat) {
//        taskExecutor.execute(() -> mailService.sendMail(email, content, bodyFormat));
//    }
//
//    public Customer updateCustomerData(Customer customer){
//        // Kiem tra quyen
//        UserDTO userDTO = authService.getCurrentUser();
//        List<Long> roles = List.of(Constants.ROLE.ADMIN,Constants.ROLE.NHAN_VIEN_KHO,Constants.ROLE.NHAN_VIEN_TU_VAN,Constants.ROLE.NHAN_VIEN_MUA_HANG);
//        Assert.isTrue((roles.contains(userDTO.getRole())), MessageUtils.getMessage("no.permission"));
//        Optional<Account> accountOptional = accountRepository.findById(customer.getId());
//        if (accountOptional.isPresent()){
//            Customer customerOld = customerRepository.findCustomerByAccountId(accountOptional.get().getId());
//            if (!ObjectUtils.isEmpty(customerOld)){
//                if (!ObjectUtils.isEmpty(customerOld.getStaffId()) && ObjectUtils.isEmpty(customer.getStaffId())){
//                    throw new IllegalArgumentException(MessageUtils.getMessage("khach.hang.da.duoc.gan.nhan.vien"));
//                }
//                customerOld.setNote(ObjectUtils.isEmpty(customer.getNote())?customerOld.getNote():customer.getNote());
//                customerOld.setCustomerNeeds(ObjectUtils.isEmpty(customer.getCustomerNeeds())?customerOld.getCustomerNeeds():customer.getCustomerNeeds());
//                if(Constants.ROLE.ADMIN == userDTO.getRole()){
//                    // cập nhật toàn bộ staff id của đơn hàng
//                    if (customerOld.getStaffId() == null){
//                        orderRepository.updateStaffIdForOrder(customer.getStaffId(), customerOld.getId());
//                    }
//                    // lưu thông tin staff_id
//                    if (!ObjectUtils.isEmpty(customer.getStaffId())) {
//                        customerOld.setStaffId(ObjectUtils.isEmpty(customer.getStaffId())?customerOld.getStaffId():customer.getStaffId());
//                    }
//                }
//                customerOld.setIsCustomerFromData(ObjectUtils.isEmpty(customer.getIsCustomerFromData())?customerOld.getIsCustomerFromData():customer.getIsCustomerFromData());
//                customer = customerRepository.save(customerOld);
//            }
//        }
//        return customer;
//    }
//
//    public PageDTO findCustomerData(SearchDTO searchDTO){
//        // Kiem tra quyen
//        List<Long> roles = List.of(Constants.ROLE.ADMIN,Constants.ROLE.NHAN_VIEN_KHO,Constants.ROLE.NHAN_VIEN_TU_VAN,Constants.ROLE.NHAN_VIEN_MUA_HANG);
//        Assert.isTrue((roles.contains(authService.getCurrentUser().getRole())), MessageUtils.getMessage("no.permission"));
//        List<CustomerDataDTO> customerDataDTOS = new ArrayList<>();
//        Integer pageIndex = ObjectUtils.isEmpty(searchDTO.getPageIndex()) ? 1: searchDTO.getPageIndex();
//        Integer pageSize = ObjectUtils.isEmpty(searchDTO.getPageSize()) ? 10: searchDTO.getPageSize();
//        Pageable pageable = PageRequest.of(pageIndex, pageSize);
//        Page<UserDTO> customerPage = customerRepository.findCustomerData(pageable, searchDTO.getCustomerName());
//        if (!customerPage.isEmpty()){
//            customerPage.forEach(i->{
//                CustomerDataDTO customerDataDTO = new CustomerDataDTO();
//                List<OrderChina> orderChinas = orderRepository.findOrderChinaByCustomerIdFirstAndLast(i.getCustomerDTO().getId());
//                customerDataDTO.setOrderHistory(orderChinas);
//                customerDataDTO.setUserDTO(i);
//                List<Long> idInvens = orderChinas.stream().map(OrderChina::getAddressId).collect(Collectors.toList());
//                List<Inventory> inventories = inventoryRepository.findInventoryByIdAndStatus(idInvens, Constants.STATUS.ACTIVE);
//                customerDataDTO.setInventory(inventories);
//                customerDataDTOS.add(customerDataDTO);
//            });
//        }
//        return PageDTO.builder().data(customerDataDTOS).totalPage(customerPage.getTotalPages()).totalRecord((int) customerPage.getTotalElements()).build();
//    }
//
//    public PageDTO findCustomer(CustomerSearchDTO searchDTO){
//        // Kiem tra quyen
//        UserDTO userDTO = authService.getCurrentUser();
//        List<Long> roles = List.of(Constants.ROLE.ADMIN,Constants.ROLE.NHAN_VIEN_KHO,Constants.ROLE.NHAN_VIEN_TU_VAN,Constants.ROLE.NHAN_VIEN_MUA_HANG);
//        Assert.isTrue((roles.contains(userDTO.getRole())), MessageUtils.getMessage("no.permission"));
//        if (!Constants.ROLE.ADMIN.equals(userDTO.getRole())){
//            searchDTO.setStaffId(userDTO.getId());
//        }
//        if (ObjectUtils.isEmpty(searchDTO.getPageIndex())){
//            searchDTO.setPageIndex(1);
//        }
//        if (ObjectUtils.isEmpty(searchDTO.getPageSize())){
//            searchDTO.setPageSize(10);
//        }
//        List<CustomerDataDTO> customerDataDTOS = new ArrayList<>();
//        BigInteger totalRecord = (BigInteger) customerRepository.findByOption(searchDTO, true);
//        Integer totalPage = (totalRecord.intValue() % searchDTO.getPageSize()) == 0 ? totalRecord.intValue() / searchDTO.getPageSize() : (totalRecord.intValue() / searchDTO.getPageSize()) + 1;
//        List<UserDTO> userDTOS = (List<UserDTO>) customerRepository.findByOption(searchDTO, false);
//        if (!userDTOS.isEmpty()){
//            userDTOS.forEach(i->{
//                CustomerDataDTO customerDataDTO = new CustomerDataDTO();
//                List<OrderChina> orderChinas = orderRepository.findAllByUserIdAndStatus(i.getCustomerDTO().getId(),Constants.ORDER_STATUS.HANG_DA_VE_KHO_VN,Constants.ORDER_TYPE.THUONG);
//                Double amountMustPay = orderChinas.stream().mapToDouble(OrderChina::getNotPaid).sum();
//                customerDataDTO.setAmountMustPay(amountMustPay);
//                if (i.getAvailableBalance() > amountMustPay){
//                    customerDataDTO.setAmountMustAdmit(0.0);
//                }else {
//                    customerDataDTO.setAmountMustAdmit(amountMustPay - i.getAvailableBalance());
//                }
//                customerDataDTO.setOrderHistory(orderChinas);
//                customerDataDTO.setUserDTO(i);
//                customerDataDTO.setTotalOrder(orderChinas.size());
//                customerDataDTOS.add(customerDataDTO);
//            });
//        }
//        return PageDTO.builder().data(customerDataDTOS).totalPage(totalPage).totalRecord(totalRecord.intValue()).build();
//    }
}
