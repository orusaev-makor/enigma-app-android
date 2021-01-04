package com.example.enigmaapp.web.accounts;

import com.google.gson.annotations.SerializedName;

public class AccountsItemResult {
    private String username;

    @SerializedName("company_name")
    private String companyName;

    @SerializedName("account_type")
    private String accountType;

    @SerializedName("account_name")
    private String accountName;

    @SerializedName("is_default")
    private int isDefault;

    private int status;

    @SerializedName("account_id")
    private int accountId;

    private String currency;

    @SerializedName("crypto_currency")
    private String cryptoCurrency;

    @SerializedName("account_details")
    private String accountDetails;

    @SerializedName("account_type_id")
    private int accountTypeId;

    public AccountsItemResult(AccountsItemResult account) {
        this.username = account.username;
        this.companyName = account.companyName;
        this.accountType = account.accountType;
        this.accountName = account.accountName;
        this.isDefault = account.isDefault;
        this.status = account.status;
        this.accountId = account.accountId;
        this.currency = account.currency;
        this.cryptoCurrency = account.cryptoCurrency;
        this.accountDetails = account.accountDetails;
        this.accountTypeId = account.accountTypeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public int getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(int accountTypeId) {
        this.accountTypeId = accountTypeId;
    }
}
