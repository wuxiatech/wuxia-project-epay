package cn.wuxia.project.payment.core.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import cn.wuxia.project.common.bean.CommonDto;



/**
 * 费用明细dto
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
public class FundsDetailDto extends CommonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数)不能为空")
    private BigDecimal amount; // 交易金额(有正负之分，当充值，解冻的情况为正数，扣费，冻结为负数) - 必填	

    private BigDecimal totalAmount; // 当前账户总金额 - 可为空	

    private BigDecimal usedAmount; //当前账户已使用的金额

    private BigDecimal availableAmount; // 当前账户可用金额 - 可为空	

    private BigDecimal frozenAmount; // 冻结金额 - 可为空	

    @NotNull(message = "交易类型不能为空")
    private String tradeType; // 交易类型 - 必填	

    @NotNull(message = "交易时间不能为空")
    private Timestamp tradeDate; //交易时间 - 必填

    private String ip; // 交易ip - 可为空	

    @NotNull(message = "所属卖家id不能为空")
    private String userId; // 所属卖家id - 必填	

    private String tradeNo; // 交易号

    private String remark; // 资金备注 - 可为空	

    private String description; // 资金描述 - 可为空	

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Timestamp tradeDate) {
        this.tradeDate = tradeDate;
    }

}
