package cn.wuxia.project.payment.core.bean;

import cn.wuxia.project.common.bean.CommonDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 费用明细dto
 *
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
@Getter
@Setter
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


}
