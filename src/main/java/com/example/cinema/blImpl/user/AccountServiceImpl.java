package com.example.cinema.blImpl.user;

import com.example.cinema.bl.user.AccountService;
import com.example.cinema.data.management.ClerkMapper;
import com.example.cinema.data.user.AccountMapper;
import com.example.cinema.po.Clerk;
import com.example.cinema.po.User;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huwen
 * @date 2019/3/23
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final static String ACCOUNT_EXIST = "账号已存在";
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private ClerkMapper clerkMapper;

    @Override
    public ResponseVO registerAccount(UserForm userForm) {
        try {
            accountMapper.createNewAccount(userForm.getRole(),userForm.getUsername(),userForm.getPassword());
            if (userForm.getRole().equals("clerk")){
                clerkMapper.insertClerkRank(accountMapper.getAccountByName(userForm.getUsername()).getId(),1);
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseVO.buildFailure(ACCOUNT_EXIST);
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public UserVO login(UserForm userForm) {
        User user = accountMapper.getAccountByName(userForm.getUsername());
        if (null == user || !user.getPassword().equals(userForm.getPassword())||!user.getRole().equals(userForm.getRole())) {
            return null;
        }
        return new UserVO(user);
    }

}
