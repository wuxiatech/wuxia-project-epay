package cn.wuxia.project.payment.core.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import cn.wuxia.project.payment.core.enums.ExpenseType;
import cn.wuxia.project.payment.core.enums.TradeType;
import org.hibernate.validator.constraints.NotBlank;

import cn.wuxia.project.common.bean.CommonDto;


/**
 * 交易明细DTO
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
public class FundsTradeDetailDto extends CommonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数))不能为空")
    private BigDecimal amount; // 交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数)) - 必填	

    @NotNull(message = "交易类型不能为空")
    private TradeType tradeType; // 交易类型 - 必填

    @NotNull(message = "交易号不能为空")
    private String tradeNo; // 交易号

    @NotNull(message = "交易时间不能为空")
    private Timestamp tradeTime; //交易时间 - 必填

    @NotNull(message = "费用类型不能为空")
    private ExpenseType expenseType; // 费用类型 - 必填

    @NotNull(message = "订单号不能为空")
    private String orderNo; // 订单号 - 必填	

    private String currency;

    private String remark; // 交易备注 - 可为空	

    @NotBlank(message = "用户id不能为空")
    private String userId; //用户id - 必填，冗余数据

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public Timestamp getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Timestamp tradeTime) {
        this.tradeTime = tradeTime;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

}
