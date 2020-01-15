package cn.wuxia.project.payment.core.entity;

import cn.wuxia.common.util.DateUtil;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.payment.core.enums.ExpenseType;
import cn.wuxia.project.payment.core.enums.TradeType;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
* The persistent class for the pay_funds_trade_detail database table.
* @author songlin.li
* @since 2017-02-16
*/
@Entity
@Table(name = "pay_funds_trade_detail")
public class FundsTradeDetail extends ModifyInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数))不能为空")
    private BigDecimal amount; // 交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数)) - 必填	

    @NotNull(message = "交易类型不能为空")
    private TradeType tradeType; // 交易类型 - 必填

    @NotNull(message = "交易时间不能为空")
    private Timestamp tradeTime; //交易时间 - 必填

    @NotNull(message = "交易号不能为空")
    private String tradeNo; // 交易号 - 必填 

    @NotNull(message = "费用类型不能为空")
    private ExpenseType expenseType; // 费用类型 - 必填

    private String orderNo; // 订单号 - 必填	

    @NotNull(message = "币种不能为空")
    private String currency; // 币种 - 必填

    private String remark; // 交易备注 - 可为空	

    @NotBlank(message = "用户id不能为空")
    private String userId; //用户id - 必填，冗余数据

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type")
    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    @Column(name = "trade_time")
    public Timestamp getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Timestamp tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Length(max = 32)
    @Column(name = "trade_no")
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_type")
    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    @Length(max = 32)
    @Column(name = "order_no")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Length(max = 128)
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public FundsTradeDetail() {
        super();
        this.tradeTime = DateUtil.newInstanceDate();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
