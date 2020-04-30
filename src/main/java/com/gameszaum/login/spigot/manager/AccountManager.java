package com.gameszaum.login.spigot.manager;

import com.gameszaum.login.spigot.account.Account;

import java.util.LinkedList;
import java.util.List;

public class AccountManager {

    private List<Account> accounts;

    public AccountManager() {
        accounts = new LinkedList<>();
    }

    public void addAccount(Account account) {
        if (getAccount(account.getName()) == null)
            accounts.add(account);
    }

    public Account getAccount(String playerName) {
        return accounts.stream().filter(account -> account.getName().equals(playerName)).findFirst().orElse(null);
    }

    public void removeAccount(String playerName) {
        accounts.remove(getAccount(playerName));
    }

}
