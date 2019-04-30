/*
* Created on :2017年3月14日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.project.payment.core.bean;

import java.io.Serializable;

/**
 * 
 * 自定义扣费对象
 * @author songlin
 * @ Version : V<Ver.No> <2017年3月14日>
 */
public class OrderCostBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4878090661507316193L;

    private String userId;

    private String orderNo;

    private Double preFreight;

    private Double preTax;

    private Double preTotal;

    private Double actualFreight;

    private Double actualAcrossTax;

    private Double actualPostTax;

    private Double actualTotal;

    private String currency;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(Double actualFreight) {
        this.actualFreight = actualFreight;
    }

    public Double getActualAcrossTax() {
        return actualAcrossTax;
    }

    public void setActualAcrossTax(Double actualAcrossTax) {
        this.actualAcrossTax = actualAcrossTax;
    }

    public Double getActualPostTax() {
        return actualPostTax;
    }

    public void setActualPostTax(Double actualPostTax) {
        this.actualPostTax = actualPostTax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Double actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Double getPreFreight() {
        return preFreight;
    }

    public void setPreFreight(Double preFreight) {
        this.preFreight = preFreight;
    }

    public Double getPreTax() {
        return preTax;
    }

    public void setPreTax(Double preTax) {
        this.preTax = preTax;
    }

    public Double getPreTotal() {
        return preTotal;
    }

    public void setPreTotal(Double preTotal) {
        this.preTotal = preTotal;
    }

}
