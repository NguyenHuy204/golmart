package com.golmart.repos;

import com.golmart.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByEmail(String email);

    Account findByEmail(String username);
    @Query(value = "select * from accounts where email = :emailOrUserName or username = :emailOrUserName limit 1", nativeQuery = true)
    Account findByEmailOrUsername(@Param("emailOrUserName") String emailOrUserName);

    Account findByUsername(String username);

    @Query(value = "select count(*) from accounts where account.username LIKE CONCAT('%',:username,'%')", nativeQuery = true)
    Long countDataAccount(@Param("username") String username);

    @Query(value = "SELECT * from accounts a where (:username is null or a.username = :username ) " +
            "and ( :name is null or a.full_name like :name) " +
            "and (:role is null or a.`role` = :role)", nativeQuery = true)
    List<Account> findAllByOption(@Param("username") String username, @Param("name") String name,@Param("role") Long role);


    @Query(value = "SELECT a.*, \n" +
            "s.id as staffId, s.account_id as s0, s.create_date as s1, s.create_user as s2, s.role as s5, s.update_date as s3, s.update_user as s4, \n" +
            "c.id as cus_id, c.account_id, c.available_balance, c.create_date as c1, c.create_user as c2, c.customer_needs, c.customer_from_data, c.note, c.staff_id, c.update_date as c3, c.update_user as c4 \n" +
            "from accounts a \n" +
            "left join staff s on s.account_id = a.id \n" +
            "left join customer c on c.account_id  = a.id \n" +
            "WHERE a.id =:accountId", nativeQuery = true)
    List<String[]> findInfoById(@Param("accountId") Long id);

}
