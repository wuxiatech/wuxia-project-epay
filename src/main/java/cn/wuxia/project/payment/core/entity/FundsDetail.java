package cn.wuxia.project.payment.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import cn.wuxia.project.payment.core.enums.FundsType;
import cn.wuxia.project.common.model.CommonEntity;

import org.hibernate.validator.constraints.Length;

import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.NoGenerateUtil;

/**
* The persistent class for the pay_funds_detail database table.
* @author songlin.li
* @since 2017-02-16
*/
@Entity
@Table(name = "pay_funds_detail")
public class FundsDetail extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数)不能为空")
    private BigDecimal amount; // 交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数) - 必填	

    private BigDecimal totalAmount; // 当前账户总金额,等于（可用+已用+冻结） - 可为空	

    private BigDecimal usedAmount; //当前账户已使用的金额

    private BigDecimal availableAmount; // 当前账户可用金额 - 可为空	

    private BigDecimal frozenAmount; // 冻结金额 - 可为空	

    @NotNull(message = "交易类型不能为空")
    private FundsType fundsType; // 交易类型 - 必填

    @NotNull(message = "交易时间不能为空")
    private Timestamp tradeTime; //交易时间 - 必填

    private String ip; // 交易ip - 可为空	

    @NotNull(message = "所属卖家id不能为空")
    private String userId; // 所属卖家id - 必填	

    @NotNull(message = "交易号不能为空")
    private String tradeNo; // 交易号 - 必填	

    @NotNull(message = "币种不能为空")
    private String currency; // 币种 - 必填

    private String remark; // 资金备注 - 可为空	

    private String description; // 资金描述 - 可为空	

    private Long order_;

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "total_amount")
    public BigDecimal getTotalAmount() {
        if (totalAmount == null) {
            totalAmount = new BigDecimal(0);
            if (availableAmount != null) {
                totalAmount = totalAmount.add(availableAmount);
            }
            if (usedAmount != null) {
                totalAmount = totalAmount.add(usedAmount);
            }
            if (frozenAmount != null) {
                totalAmount = totalAmount.add(frozenAmount);
            }
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Column(name = "used_amount")
    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    @Column(name = "available_amount")
    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    @Column(name = "frozen_amount")
    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "FUNDS_TYPE")
    public FundsType getFundsType() {
        return fundsType;
    }

    public void setFundsType(FundsType fundsType) {
        this.fundsType = fundsType;
    }

    @Length(max = 32)
    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Length(max = 22)
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Length(max = 32)
    @Column(name = "trade_no")
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Length(max = 128)
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Length(max = 255)
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FundsDetail() {
        super();
        this.tradeNo = NoGenerateUtil.generateNo(22);
        this.tradeTime = DateUtil.newInstanceDate();
    }

    public void setTradeTime(Timestamp tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Column(name = "TRADE_TIME")
    public Timestamp getTradeTime() {
        return tradeTime;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getOrder_() {
        return order_;
    }

    public void setOrder_(Long order_) {
        this.order_ = order_;
    }

}
