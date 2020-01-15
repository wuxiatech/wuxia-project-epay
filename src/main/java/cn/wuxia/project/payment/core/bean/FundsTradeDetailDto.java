package cn.wuxia.project.payment.core.bean;

import cn.wuxia.project.common.bean.CommonDto;
import cn.wuxia.project.payment.core.enums.ExpenseType;
import cn.wuxia.project.payment.core.enums.TradeType;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 交易明细DTO
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
@Getter
@Setter
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


}
