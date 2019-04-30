package cn.wuxia.project.payment.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import cn.wuxia.project.basic.core.conf.service.CurrencyService;
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
import cn.wuxia.project.common.dao.CommonDao;
import cn.wuxia.project.common.service.impl.CommonServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.NumberUtil;

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
    public synchronized MyAmountDetail findMyAvailableAmount(String userId) {
        return fundsDetailDao.findMyAvailableAmount(userId);
    }

    /**
     * 充值
     * @author songlin
     */
    public synchronized void saveChargeTopup(String userId, String currency, BigDecimal amount, String orderNo, ExpenseType transfer, String remark) {
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
        tradeDetail.setOrderNo(orderNo);
        tradeDetail.setRemark(remark);
        fundsTradeDetailDao.save(tradeDetail);
        logger.debug("充值后可用金额：{}，充值后冻结金额：{}", fundsDetail.getAvailableAmount(), fundsDetail.getFrozenAmount());
    }

    /**
     * 退款
     * @author songlin
     */
    public synchronized void saveChargeRefund(String userId, String currency, BigDecimal amount, String orderNo, ExpenseType transfer,
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
        tradeDetail.setOrderNo(orderNo);
        tradeDetail.setRemark(remark);
        fundsTradeDetailDao.save(tradeDetail);
        logger.debug("退款后可用金额：{}，退款后冻结金额：{}", fundsDetail.getAvailableAmount(), fundsDetail.getFrozenAmount());
    }

    /**
     * 提交预告单到ueq之后冻结可用金额
     * 必须是已经运算了价格的invoiceDto
     * 这个时候invoiceDto 预付价格不能为空
     * @author songlin
     * @param orderDto
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

    /**
     * 出仓之后解冻原冻结运费金额
     * @author songlin
     * @param orderDto
     */
    @Override
    public synchronized void unfrozenFreightWhenOrderFinsh(List<OrderCostBean> orderDtos) {
        if (ListUtil.isEmpty(orderDtos))
            return;

        Hashtable<String, MyAmountDetail> amountMap = getUserAmount(orderDtos);
        List<FundsTradeDetail> tradeDetails = Lists.newArrayList();
        List<FundsDetail> fundsDetails = Lists.newArrayList();
        for (OrderCostBean orderCost : orderDtos) {
            if (orderCost.getPreFreight() == null || orderCost.getPreFreight() == 0) {
                continue;
            }

            MyAmountDetail amountDetail = amountMap.get(orderCost.getUserId());
            logger.info("开始解冻订单{}费用，解冻运费用为：{}", orderCost.getOrderNo(), orderCost.getPreFreight());
            BigDecimal availableAmount = amountDetail.getAvailableAmount();
            BigDecimal frozenAmount = amountDetail.getFrozenAmount();
            BigDecimal usedAmount = amountDetail.getUsedAmount();
            logger.debug("解冻前已用金额：{}，解冻前可用金额：{}，解冻前冻结金额：{}", usedAmount, availableAmount, frozenAmount);
            FundsDetail fundsDetail = new FundsDetail();

            /**
             * start 不解冻税费，税费在另外的接口扣费，将当期交易金额过滤税费，后期按照实际情况计算
             */
            fundsDetail.setAmount(new BigDecimal(orderCost.getPreFreight()));
            fundsDetail.setUsedAmount(usedAmount);
            fundsDetail.setFrozenAmount(frozenAmount.subtract(new BigDecimal(orderCost.getPreFreight())));
            BigDecimal avaAmount = availableAmount.add(new BigDecimal(orderCost.getPreFreight()));

            fundsDetail.setAvailableAmount(avaAmount);
            //货币
            fundsDetail.setCurrency(orderCost.getCurrency());
            fundsDetail.setUserId(orderCost.getUserId());
            //预告单，冻结金额状态
            fundsDetail.setFundsType(FundsType.order_unfreeze);
            fundsDetails.add(fundsDetail);

            //解冻运费记录
            FundsTradeDetail unfrozenTradeDetail = new FundsTradeDetail();
            //解冻交易运费
            unfrozenTradeDetail.setAmount(new BigDecimal(orderCost.getPreFreight()));
            unfrozenTradeDetail.setCurrency(fundsDetail.getCurrency());
            unfrozenTradeDetail.setOrderNo(orderCost.getOrderNo());
            unfrozenTradeDetail.setTradeNo(fundsDetail.getTradeNo());
            unfrozenTradeDetail.setTradeType(TradeType.unfreeze);
            unfrozenTradeDetail.setExpenseType(ExpenseType.prefreight);
            unfrozenTradeDetail.setUserId(fundsDetail.getUserId());

            tradeDetails.add(unfrozenTradeDetail);

            amountDetail.setAvailableAmount(fundsDetail.getAvailableAmount());
            amountDetail.setFrozenAmount(fundsDetail.getFrozenAmount());
            logger.debug("解冻后已用金额：{}，解冻后可用金额：{}，解冻后冻结金额：{}", fundsDetail.getUsedAmount(), fundsDetail.getAvailableAmount(),
                    fundsDetail.getFrozenAmount());
        }

        fundsDetailDao.batchSave(fundsDetails);

        fundsTradeDetailDao.batchSave(tradeDetails);

    }

    /**
     * 出仓之后解冻原冻结税费金额
     * @author songlin
     * @param orderCosts
     */
    @Override
    public synchronized void unfrozenTaxWhenOrderFinsh(List<OrderCostBean> orderCosts) {
        if (ListUtil.isEmpty(orderCosts)) {
            return;
        }
        Hashtable<String, MyAmountDetail> amountMap = getUserAmount(orderCosts);
        List<FundsTradeDetail> tradeDetails = Lists.newArrayList();
        List<FundsDetail> fundsDetails = Lists.newArrayList();

        for (OrderCostBean ordercost : orderCosts) {
            if (ordercost.getPreTax() == null || ordercost.getPreTax() == 0) {
                continue;
            }
            MyAmountDetail amountDetail = amountMap.get(ordercost.getUserId());
            logger.info("开始解冻订单{}费用，解冻税费用为：{}", ordercost.getOrderNo(), ordercost.getPreTax());
            BigDecimal availableAmount = amountDetail.getAvailableAmount();
            BigDecimal frozenAmount = amountDetail.getFrozenAmount();
            BigDecimal usedAmount = amountDetail.getUsedAmount();
            logger.debug("解冻前已用金额：{}，解冻前可用金额：{}，解冻前冻结金额：{}", usedAmount, availableAmount, frozenAmount);
            FundsDetail fundsDetail = new FundsDetail();

            /**
             * start 解冻税费
             */
            fundsDetail.setAmount(new BigDecimal(ordercost.getPreTax()));
            fundsDetail.setFrozenAmount(frozenAmount.subtract(new BigDecimal(ordercost.getPreTax())));
            BigDecimal avaAmount = availableAmount.add(new BigDecimal(ordercost.getPreTax()));
            /**
             * end 解冻税费
             *  songlin.li
             */
            fundsDetail.setUsedAmount(usedAmount);
            fundsDetail.setAvailableAmount(avaAmount);
            //货币
            fundsDetail.setCurrency(ordercost.getCurrency());
            fundsDetail.setUserId(ordercost.getUserId());
            //预告单，冻结金额状态
            fundsDetail.setFundsType(FundsType.order_unfreeze);
            fundsDetails.add(fundsDetail);
            //解冻税费
            FundsTradeDetail unforozenTaxTradeDetail = new FundsTradeDetail();
            //解冻税费
            unforozenTaxTradeDetail.setAmount(new BigDecimal(ordercost.getPreTax()));
            unforozenTaxTradeDetail.setCurrency(fundsDetail.getCurrency());
            unforozenTaxTradeDetail.setOrderNo(ordercost.getOrderNo());
            unforozenTaxTradeDetail.setTradeNo(fundsDetail.getTradeNo());
            unforozenTaxTradeDetail.setTradeType(TradeType.unfreeze);
            unforozenTaxTradeDetail.setExpenseType(ExpenseType.pretaxes);
            unforozenTaxTradeDetail.setUserId(fundsDetail.getUserId());

            tradeDetails.add(unforozenTaxTradeDetail);

            amountDetail.setAvailableAmount(fundsDetail.getAvailableAmount());
            amountDetail.setFrozenAmount(fundsDetail.getFrozenAmount());
            amountDetail.setUsedAmount(usedAmount);
            logger.debug("解冻后已用金额：{}，解冻后可用金额：{}，解冻后冻结金额：{}", fundsDetail.getUsedAmount(), fundsDetail.getAvailableAmount(),
                    fundsDetail.getFrozenAmount());
        }
        fundsDetailDao.batchSave(fundsDetails);
        fundsTradeDetailDao.batchSave(tradeDetails);
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

    /**
     * 当ueq已经完成订单，并称重出仓，则解冻预付款并实际扣款，按照最新重量的价格
     * 这个时候orderDto 真实价格不能为空
     * @author songlin
     */
    public synchronized void updateActualFreightWhenOrderComplete(List<OrderCostBean> orderDtos) {
        if (ListUtil.isEmpty(orderDtos))
            return;
        Hashtable<String, MyAmountDetail> amountMap = getUserAmount(orderDtos);
        List<FundsTradeDetail> tradeDetails = Lists.newArrayList();
        List<FundsDetail> fundsDetails = Lists.newArrayList();
        for (OrderCostBean orderCost : orderDtos) {
            if (orderCost.getActualFreight() == null || orderCost.getActualFreight() == 0) {
                continue;
            }
            /**
             * 扣费前先解冻被冻结部分资产
             * 解冻资产的时候有最新的可用资金及冻结资金，无需查表
             */
            logger.info("开始结算订单{}费用，结算总费用为：{}", orderCost.getOrderNo(), orderCost.getActualFreight());
            MyAmountDetail amountDetail = amountMap.get(orderCost.getUserId());

            BigDecimal availableAmount = amountDetail.getAvailableAmount();
            BigDecimal frozenAmount = amountDetail.getFrozenAmount();
            BigDecimal usedAmount = amountDetail.getUsedAmount();

            logger.debug("结算前可用金额：{}，结算前冻结金额：{}", availableAmount, frozenAmount);
            FundsDetail fundsDetail = new FundsDetail();

            /**
             * start 不扣取税费，税费在另外的接口扣费，将当期交易金额过滤税费，后期按照实际情况计算
             */
            //当前订单扣费总额, 扣款为负数
            //fundsDetail.setAmount(new BigDecimal(-orderDto.getActualTotal()));
            //冻结的额度不变
            //fundsDetail.setFrozenAmount(frozenAmount);
            //可用额度等于现可用额度减去真是扣费
            //BigDecimal avaAmount = availableAmount.subtract(new BigDecimal(orderDto.getActualTotal()));
            fundsDetail.setAmount(new BigDecimal(-orderCost.getActualFreight()));
            fundsDetail.setUsedAmount(usedAmount.add(new BigDecimal(orderCost.getActualFreight())));
            fundsDetail.setFrozenAmount(frozenAmount);
            BigDecimal avaAmount = availableAmount.subtract(new BigDecimal(orderCost.getActualFreight()));
            /**
             * 结束不扣取税费
             * songlin.li
             */

            fundsDetail.setAvailableAmount(avaAmount);
            //货币
            fundsDetail.setCurrency(orderCost.getCurrency());
            /**
             * 解冻比结算延后2s
             */
            fundsDetail.setTradeTime(DateUtil.utilDateToTimestamp(DateUtil.addSeconds(new Date(), 2)));
            fundsDetail.setUserId(orderCost.getUserId());
            //预告单，冻结金额状态
            fundsDetail.setFundsType(FundsType.order_complete);

            fundsDetails.add(fundsDetail);
            //for (OrderDto orderDto : invoiceDto.getOrderDtoList()) {
            if (orderCost.getActualFreight() == null) {
                throw new AppServiceException(orderCost.getOrderNo() + "ActualFreight不能为空");
            }
            FundsTradeDetail tradeDetail = new FundsTradeDetail();
            //交易运费
            tradeDetail.setAmount(new BigDecimal(-orderCost.getActualFreight()));
            tradeDetail.setCurrency(fundsDetail.getCurrency());
            tradeDetail.setOrderNo(orderCost.getOrderNo());
            tradeDetail.setTradeTime(fundsDetail.getTradeTime());
            tradeDetail.setTradeNo(fundsDetail.getTradeNo());
            tradeDetail.setTradeType(TradeType.expense);
            tradeDetail.setExpenseType(ExpenseType.freight);
            tradeDetail.setUserId(fundsDetail.getUserId());

            tradeDetails.add(tradeDetail);

            amountDetail.setAvailableAmount(fundsDetail.getAvailableAmount());
            amountDetail.setFrozenAmount(fundsDetail.getFrozenAmount());
            amountDetail.setUsedAmount(fundsDetail.getUsedAmount());
            logger.debug("结算后可用金额：{}，结算后冻结金额：{}", fundsDetail.getAvailableAmount(), fundsDetail.getFrozenAmount());
        }

        fundsDetailDao.batchSave(fundsDetails);

        fundsTradeDetailDao.batchSave(tradeDetails);
    }

    /**
     * 当ueq已经完成订单，并称重出仓，则解冻预付款并实际扣款，按照最新重量的价格
     * 这个时候orderDto 真实价格不能为空
     * @author songlin
     */
    @Override
    public synchronized void updateActualTaxWhenOrderComplete(List<OrderCostBean> orderCosts) {
        if (ListUtil.isEmpty(orderCosts))
            return;
        Hashtable<String, MyAmountDetail> amountMap = getUserAmount(orderCosts);
        List<FundsTradeDetail> tradeDetails = Lists.newArrayList();
        List<FundsDetail> fundsDetails = Lists.newArrayList();
        for (OrderCostBean orderCost : orderCosts) {
            Double actualTax = orderCost.getActualAcrossTax() == null ? orderCost.getActualPostTax() : orderCost.getActualAcrossTax();

            if (actualTax == null || actualTax == 0)
                continue;

            MyAmountDetail amountDetail = amountMap.get(orderCost.getUserId());
//            NormalUserDetails userDetail = UserContextUtil.getUserDetailById(orderCost.getUserId());
//            CurrencyDto currency = currencyService.getByCurrencyCode(userDetail.getCurrencyCode());
//            /**
//             * 税费返回的为人民币，扣费需要结算为用户使用的货币
//             */
//            actualTax = actualTax * currency.getExchangeRateFromRmb();
            /**
             * 扣费前先解冻被冻结部分资产
             * 解冻资产的时候有最新的可用资金及冻结资金，无需查表
             */
            logger.info("开始结算订单{}费用，结算总费用为：{}", orderCost.getOrderNo(), actualTax);

            BigDecimal availableAmount = amountDetail.getAvailableAmount();
            BigDecimal frozenAmount = amountDetail.getFrozenAmount();
            BigDecimal usedAmount = amountDetail.getUsedAmount();
            logger.debug("结算前已用金额：{}，结算前可用金额：{}", usedAmount, availableAmount);
            FundsDetail fundsDetail = new FundsDetail();

            /**
             * start 扣取税费,需要以人民币结算为当前用户的货币
             * 
             */

            //可用额度等于现可用额度减去真是扣费
            fundsDetail.setAmount(new BigDecimal(-actualTax));
            fundsDetail.setUsedAmount(usedAmount.add(new BigDecimal(actualTax)));
            fundsDetail.setFrozenAmount(frozenAmount);
            BigDecimal avaAmount = availableAmount.subtract(new BigDecimal(actualTax));

            fundsDetail.setAvailableAmount(avaAmount);
            //货币
            fundsDetail.setCurrency(orderCost.getCurrency());
            /**
             * 解冻比结算延后2s
             */
            fundsDetail.setTradeTime(DateUtil.utilDateToTimestamp(DateUtil.addSeconds(new Date(), 2)));
            fundsDetail.setUserId(orderCost.getUserId());
            //预告单，交易状态
            fundsDetail.setFundsType(FundsType.order_complete);

            fundsDetails.add(fundsDetail);

            FundsTradeDetail tradeDetail = new FundsTradeDetail();
            //交易税费
            tradeDetail.setAmount(new BigDecimal(-actualTax));
            tradeDetail.setCurrency(fundsDetail.getCurrency());
            tradeDetail.setOrderNo(orderCost.getOrderNo());
            tradeDetail.setTradeTime(fundsDetail.getTradeTime());
            tradeDetail.setTradeNo(fundsDetail.getTradeNo());
            tradeDetail.setTradeType(TradeType.expense);
            if (orderCost.getActualAcrossTax() != null) {
                tradeDetail.setExpenseType(ExpenseType.acrosstax);
            } else if (orderCost.getActualPostTax() != null) {
                tradeDetail.setExpenseType(ExpenseType.posttax);
            }
            tradeDetail.setUserId(fundsDetail.getUserId());

            tradeDetails.add(tradeDetail);
            logger.debug("结算后已用金额：{}，结算后可用金额：{}", fundsDetail.getUsedAmount(), fundsDetail.getAvailableAmount());
        }
        fundsDetailDao.batchSave(fundsDetails);
        fundsTradeDetailDao.batchSave(tradeDetails);
    }
}
