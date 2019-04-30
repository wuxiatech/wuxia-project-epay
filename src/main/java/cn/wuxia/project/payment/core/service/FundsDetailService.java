package cn.wuxia.project.payment.core.service;

import java.math.BigDecimal;
import java.util.List;

import cn.wuxia.project.payment.core.bean.MyAmountDetail;
import cn.wuxia.project.payment.core.bean.OrderCostBean;
import cn.wuxia.project.payment.core.entity.FundsDetail;
import cn.wuxia.project.payment.core.enums.ExpenseType;
import cn.wuxia.project.common.service.CommonService;

/**
 * 用户资金明细表 Service Interface.
 * @author songlin.li
 * @since 2017-02-16
 */
public interface FundsDetailService extends CommonService<FundsDetail, String> {

    /**
     * availableAmount & frozenAmount
     * @author songlin
     * @param userId
     * @return
     */
    public MyAmountDetail findMyAvailableAmount(String userId);

    /**
     * 充值
     * @author songlin
     */
    public void saveChargeTopup(String userId, String currency, BigDecimal amount, String orderNo, ExpenseType transfer, String remark);

    /**
     *  退款
     * @author songlin
     * @param userId
     * @param currency
     * @param amount
     * @param orderNo
     * @param transfer
     * @param remark
     */
    public void saveChargeRefund(String userId, String currency, BigDecimal amount, String orderNo, ExpenseType transfer, String remark);

    /**
     * 提交预告单到ueq之后冻结可用金额
     * 必须是已经运算了价格的orderDtos
     * @author songlin
     * @param orderCosts
     */
    public void saveWhenOrderFinsh(List<OrderCostBean> orderCosts);

    /**
     * 出仓之后解冻原冻结金额
     * @author songlin
     * @param orderCosts
     */
    public void unfrozenFreightWhenOrderFinsh(List<OrderCostBean> orderCosts);

    /**
     * 出仓之后解冻原冻结税费金额
     * @author songlin
     * @param orderCosts
     */
    public void unfrozenTaxWhenOrderFinsh(List<OrderCostBean> orderCosts);

    /**
     * 当ueq已经完成订单，并称重出仓，则解冻预付款并实际扣款，按照最新重量的价格
     * @author songlin
     */
    public void updateActualFreightWhenOrderComplete(List<OrderCostBean> orderCosts);

    /**
     * 当ueq已经完成订单，并称重出仓，则解冻预付税款并实际扣税款
     * @author songlin
     */
    public void updateActualTaxWhenOrderComplete(List<OrderCostBean> orderCosts);
}
