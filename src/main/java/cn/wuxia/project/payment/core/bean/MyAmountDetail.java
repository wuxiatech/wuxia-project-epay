/*
* Created on :2017年3月7日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.project.payment.core.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单用户的额度情况
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
public class MyAmountDetail implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    BigDecimal availableAmount;

    BigDecimal frozenAmount;

    BigDecimal usedAmount;

    BigDecimal totalAmount;

    String currency;

    String currencyName;

    String currencySign;

    public MyAmountDetail() {
        availableAmount = new BigDecimal(0);
        frozenAmount = new BigDecimal(0);
        usedAmount = new BigDecimal(0);
        totalAmount = new BigDecimal(0);
        currency = "CNY";
        currencyName = "人民币";
        currencySign = "￥";
    }

    public BigDecimal getAvailableAmount() {
        if (availableAmount == null)
            return new BigDecimal(0);
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getFrozenAmount() {
        if (frozenAmount == null)
            return new BigDecimal(0);
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getUsedAmount() {
        if (usedAmount == null)
            return new BigDecimal(0);
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getTotalAmount() {
        if (totalAmount == null)
            return new BigDecimal(0);
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(String currencySign) {
        this.currencySign = currencySign;
    }

    @Override
    public String toString() {
        return String.format("结算货币：%s，账户总额：%g，已用金额：%g，可用金额：%g，冻结金额：%g", this.currencyName, this.totalAmount, this.usedAmount, this.availableAmount,
                this.frozenAmount);
    }
}
