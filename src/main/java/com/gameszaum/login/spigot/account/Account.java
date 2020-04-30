package com.gameszaum.login.spigot.account;

import com.gameszaum.login.spigot.account.dao.AccountDao;
import lombok.Data;

@Data
public class Account {

    private String name, pass;
    private AccountDao accountDao;
    private boolean premium, logged, registered, captcha;

}
