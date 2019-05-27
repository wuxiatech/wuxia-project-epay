package cn.wuxia.project.payment.core.service;

import cn.wuxia.project.common.service.CommonService;
import cn.wuxia.project.payment.core.bean.MyAmountDetail;
import cn.wuxia.project.payment.core.bean.OrderCostBean;
import cn.wuxia.project.payment.core.entity.FundsDetail;
import cn.wuxia.project.payment.core.enums.ExpenseType;

import java.math.BigDecimal;
import java.util.List;

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
    public void saveChargeTopup(String userId, String currency, BigDecimal amount, String serialNumber, ExpenseType transfer, String remark);

    /**
     *  退款
     * @author songlin
     * @param userId
     * @param currency
     * @param amount
     * @param serialNumber
     * @param transfer
     * @param remark
     */
    public void saveChargeRefund(String userId, String currency, BigDecimal amount, String serialNumber, ExpenseType transfer, String remark);

    /**
     * 提交预告单到ueq之后冻结可用金额
     * 必须是已经运算了价格的orderDtos
     * @author songlin
     * @param orderCosts
     */
    public void saveWhenOrderFinsh(List<OrderCostBean> orderCosts);


}
