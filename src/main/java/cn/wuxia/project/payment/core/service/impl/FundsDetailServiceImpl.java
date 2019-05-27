package cn.wuxia.project.payment.core.service.impl;

import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.NumberUtil;
import cn.wuxia.project.basic.core.conf.service.CurrencyService;
import cn.wuxia.project.common.dao.CommonDao;
import cn.wuxia.project.common.service.impl.CommonServiceImpl;
import cn.wuxia.project.payment.core.bean.MyAmountDetail;
import cn.wuxia.project.payment.core.bean.OrderCostBean;
import cn.wuxia.project.payment.core.dao.FundsDetailDao;
import cn.wuxia.project.payment.core.dao.FundsTradeDetailDao;
import cn.wuxia.project.payment.core.entity.FundsDetail;
import cn.wuxia.project.payment.core.entity.FundsTradeDetail;
import cn.wuxia.project.payment.core.enums.ExpenseType;
import cn.wuxia.project.payment.core.enums.FundsType;
import cn.wuxia.project.payment.core.enums.TradeType;
import cn.wuxia.project.payment.core.service.FundsDetailService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

/**
 * 用户资金明细表 Service Implement class.
 * @author songlin.li
 * @since 2017-02-16
 */
@Service
public class FundsDetailServiceImpl extends CommonServiceImpl<FundsDetail, String> implements FundsDetailService {

    @Autowired
    private FundsDetailDao fundsDetailDao;

    @Autowired
    private FundsTradeDetailDao fundsTradeDetailDao;

    @Autowired
    private CurrencyService currencyService;

    @Override
    protected CommonDao getCommonDao() {
        return fundsDetailDao;
    }

    /**
    * availableAmount & frozenAmount
    * @author songlin
    * @param userId
    * @return
    */
    @Override
    public synchronized MyAmountDetail findMyAvailableAmount(String userId) {
        return fundsDetailDao.findMyAvailableAmount(userId);
    }

    /**
     * 充值
     * @author songlin
     */
    @Override
    public synchronized void saveChargeTopup(String userId, String currency, BigDecimal amount, String serialNumber, ExpenseType transfer, String remark) {
        logger.info("开始充值{}费用，充值总费用为：{}{}", userId, amount, currency);
        /**
         * 如果充值货币为CNY，并用户使用的货币并非为CNY,则需要汇率转换
         */
//        if (!StringUtil.equalsIgnoreCase(currency, userDetail.getCurrencyCode()) && StringUtil.equalsIgnoreCase(currency, "CNY")) {
//            CurrencyDto dto = currencyService.getByCurrencyCode(userDetail.getCurrencyCode());
//            amount = (BigDecimal) NumberUtil.multiply(amount, dto.getExchangeRateFromCNY());
//            logger.info("开始充值{}费用，充值总费用为：{}{}", userId, amount, userDetail.getCurrencyCode());
//        }
        MyAmountDetail amountDetail = fundsDetailDao.findMyAvailableAmount(userId);
        BigDecimal availableAmount = amountDetail.getAvailableAmount();
        BigDecimal frozenAmount = amountDetail.getFrozenAmount();
        BigDecimal usedAmount = amountDetail.getUsedAmount();
        logger.debug("充值前：{}", amountDetail);
        /**
         * 定义系统最大金额为999999999.99
         */
        BigDecimal valid = new BigDecimal(999999999.99).subtract(availableAmount);
        if (NumberUtil.compare(valid, amount) < 0) {
            throw new AppServiceException("超出可充值金额：" + valid + "，当前为：" + amount);
        }
        FundsDetail fundsDetail = new FundsDetail();
        fundsDetail.setAmount(amount);
        fundsDetail.setAvailableAmount(amount.add(availableAmount));
        fundsDetail.setFrozenAmount(frozenAmount);
        fundsDetail.setUsedAmount(usedAmount);
        fundsDetail.setTotalAmount(fundsDetail.getTotalAmount());
        fundsDetail.setCurrency(amountDetail.getCurrency());
        //充值
        fundsDetail.setFundsType(FundsType.charge);
        fundsDetail.setUserId(userId);
        fundsDetail.setRemark(remark);
        fundsDetailDao.save(fundsDetail);

        FundsTradeDetail tradeDetail = new FundsTradeDetail();
        //交易运费
        tradeDetail.setAmount(amount);
        tradeDetail.setCurrency(fundsDetail.getCurrency());
        tradeDetail.setTradeNo(fundsDetail.getTradeNo());
        //充值
        tradeDetail.setTradeType(TradeType.topup);
        //付款
        tradeDetail.setExpenseType(transfer);
        tradeDetail.setUserId(userId);
        tradeDetail.setOrderNo(serialNumber);
        tradeDetail.setRemark(remark);
        fundsTradeDetailDao.save(tradeDetail);
        logger.debug("充值后可用金额：{}，充值后冻结金额：{}", fundsDetail.getAvailableAmount(), fundsDetail.getFrozenAmount());
    }

    /**
     * 退款
     * @author songlin
     */
    @Override
    public synchronized void saveChargeRefund(String userId, String currency, BigDecimal amount, String serialNumber, ExpenseType transfer,
                                              String remark) {
        logger.info("开始退款{}费用，退款费用为：{}{}", userId, amount, currency);
//        NormalUserDetails userDetail = UserContextUtil.getUserDetailById(userId);
//        if (StringUtil.isBlank(userDetail.getCurrencyCode())) {
//            throw new AppServiceException("请先设置货币！");
//        }
        /**
         * 如果充值货币为RMB，并用户使用的货币并非为CNY,则需要汇率转换
         */
//        if (!StringUtil.equalsIgnoreCase(currency, userDetail.getCurrencyCode()) && StringUtil.equalsIgnoreCase(currency, "CNY")) {
//            CurrencyDto dto = currencyService.getByCurrencyCode(userDetail.getCurrencyCode());
//            amount = (BigDecimal) NumberUtil.multiply(amount, dto.getExchangeRateFromRmb());
//            logger.info("开始退款{}费用，退款总费用为：{}{}", userId, amount, userDetail.getCurrencyCode());
//        }
        MyAmountDetail amountDetail = fundsDetailDao.findMyAvailableAmount(userId);
        BigDecimal availableAmount = amountDetail.getAvailableAmount();
        BigDecimal frozenAmount = amountDetail.getFrozenAmount();
        BigDecimal usedAmount = amountDetail.getUsedAmount();
        logger.debug("退款前：{}", amountDetail);
        /**
         * 定义可退最大金额为999999999.99
         */
        if (NumberUtil.compare(availableAmount, amount) < 0) {
            throw new AppServiceException("超出可退金额：" + availableAmount + "，当前为：" + amount);
        }
        FundsDetail fundsDetail = new FundsDetail();

        /**
         * 定义负数
         */
        BigDecimal fushuAmount = new BigDecimal(0).subtract(amount);

        fundsDetail.setAmount(fushuAmount);
        fundsDetail.setAvailableAmount(availableAmount.subtract(amount));
        fundsDetail.setFrozenAmount(frozenAmount);
        fundsDetail.setUsedAmount(usedAmount);
        fundsDetail.setTotalAmount(fundsDetail.getTotalAmount());
        fundsDetail.setCurrency(amountDetail.getCurrency());
        //退款
        fundsDetail.setFundsType(FundsType.refund);
        fundsDetail.setUserId(userId);
        fundsDetail.setRemark(remark);
        fundsDetailDao.save(fundsDetail);

        FundsTradeDetail tradeDetail = new FundsTradeDetail();
        //交易运费
        tradeDetail.setAmount(fushuAmount);
        tradeDetail.setCurrency(fundsDetail.getCurrency());
        tradeDetail.setTradeNo(fundsDetail.getTradeNo());
        //退款
        tradeDetail.setTradeType(TradeType.refund);
        //付款
        tradeDetail.setExpenseType(transfer);
        tradeDetail.setUserId(userId);
        tradeDetail.setOrderNo(serialNumber);
        tradeDetail.setRemark(remark);
        fundsTradeDetailDao.save(tradeDetail);
        logger.debug("退款后可用金额：{}，退款后冻结金额：{}", fundsDetail.getAvailableAmount(), fundsDetail.getFrozenAmount());
    }

    /**
     * 提交预告单到ueq之后冻结可用金额
     * 必须是已经运算了价格的invoiceDto
     * 这个时候invoiceDto 预付价格不能为空
     * @author songlin
     * @param orderDtos
     */
    public synchronized void saveWhenOrderFinsh(List<OrderCostBean> orderDtos) {
        if (ListUtil.isEmpty(orderDtos))
            return;

        MyAmountDetail amountDetail = fundsDetailDao.findMyAvailableAmount(orderDtos.get(0).getUserId());
        logger.debug("冻结前：{}", amountDetail);

        BigDecimal availableAmount = amountDetail.getAvailableAmount();
        BigDecimal frozenAmount = amountDetail.getFrozenAmount();
        BigDecimal usedAmount = amountDetail.getUsedAmount();

        FundsDetail fundsDetail = new FundsDetail();

        BigDecimal frozenOrderAmount = new BigDecimal(0);
        List<FundsTradeDetail> tradeDetails = Lists.newArrayList();
        for (OrderCostBean orderDto : orderDtos) {
            //解冻运费记录
            FundsTradeDetail frozenTradeDetail = new FundsTradeDetail();
            //交易运费
            frozenTradeDetail.setAmount(new BigDecimal(-orderDto.getPreFreight()));
            frozenTradeDetail.setCurrency(orderDto.getCurrency());
            frozenTradeDetail.setOrderNo(orderDto.getOrderNo());
            frozenTradeDetail.setTradeNo(fundsDetail.getTradeNo());
            frozenTradeDetail.setTradeType(TradeType.frozen);
            frozenTradeDetail.setExpenseType(ExpenseType.prefreight);
            frozenTradeDetail.setUserId(orderDto.getUserId());

            tradeDetails.add(frozenTradeDetail);
            /**
             * 理应为减，实际因为为负数则直接相加
             */
            frozenOrderAmount = frozenOrderAmount.add(frozenTradeDetail.getAmount());

            if (orderDto.getPreTax() != null && orderDto.getPreTax() > 0) {
                //解冻税费
                FundsTradeDetail forozenTaxTradeDetail = new FundsTradeDetail();
                //交易税费
                forozenTaxTradeDetail.setAmount(new BigDecimal(-orderDto.getPreTax()));
                forozenTaxTradeDetail.setCurrency(orderDto.getCurrency());
                forozenTaxTradeDetail.setOrderNo(orderDto.getOrderNo());
                forozenTaxTradeDetail.setTradeNo(fundsDetail.getTradeNo());
                forozenTaxTradeDetail.setTradeType(TradeType.frozen);
                forozenTaxTradeDetail.setExpenseType(ExpenseType.pretaxes);
                forozenTaxTradeDetail.setUserId(orderDto.getUserId());

                tradeDetails.add(forozenTaxTradeDetail);
                /**
                 * 理应为减，实际因为为负数则直接相加
                 */
                frozenOrderAmount = frozenOrderAmount.add(forozenTaxTradeDetail.getAmount());
            }
        }

        fundsTradeDetailDao.batchSave(tradeDetails);

        /**
         * frozenOrderAmount为负数
         */
        logger.info("开始冻结订单{}费用，冻结总费用为：{}", fundsDetail.getTradeNo(), frozenOrderAmount);
        //货币
        fundsDetail.setCurrency(orderDtos.get(0).getCurrency());
        fundsDetail.setUserId(orderDtos.get(0).getUserId());
        //当前订单扣费总额, 扣款为负数
        fundsDetail.setAmount(frozenOrderAmount);
        //冻结的额度为现数据库冻结+现订单扣款的总额(负负得正)
        fundsDetail.setFrozenAmount(frozenAmount.subtract(frozenOrderAmount));
        //可用额度等于现可用额度减去冻结的额度
        /**
         * 理应扣减，因为已是负数，则直接相加
         */

        BigDecimal avaAmount = availableAmount.add(frozenOrderAmount);
        fundsDetail.setAvailableAmount(avaAmount);

        fundsDetail.setUsedAmount(usedAmount);
        //预告单，冻结金额状态
        fundsDetail.setFundsType(FundsType.order_finish);

        fundsDetailDao.save(fundsDetail);

        logger.debug("冻结后可用金额：{}，冻结后冻结金额：{}", fundsDetail.getAvailableAmount(), fundsDetail.getFrozenAmount());
    }


    private synchronized Hashtable<String, MyAmountDetail> getUserAmount(List<OrderCostBean> orderDtos) {
        Hashtable<String, MyAmountDetail> amountMap = new Hashtable<>();

        for (OrderCostBean orderDto : orderDtos) {
            if (amountMap.get(orderDto.getUserId()) == null) {
                MyAmountDetail amountDetail = fundsDetailDao.findMyAvailableAmount(orderDto.getUserId());
                amountMap.put(orderDto.getUserId(), amountDetail);
            }
        }

        return amountMap;
    }

}
