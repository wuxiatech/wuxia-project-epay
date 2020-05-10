/*
 * Created on :Jan 8, 2015
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 www.wuxia.tech All right reserved.
 */
package cn.wuxia.project.payment.handler;

import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.exception.AppPermissionException;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.project.epay.EpayException;
import cn.wuxia.project.epay.EpayService;
import cn.wuxia.project.epay.bean.EpayAfterBean;
import cn.wuxia.project.epay.bean.EpayBeforeBean;
import cn.wuxia.project.epay.trade.enums.PaymentTradeStatusEnum;
import cn.wuxia.project.epay.trade.enums.PaymentTradeTypeEnum;
import cn.wuxia.project.epay.trade.model.PaymentTrade;
import cn.wuxia.project.epay.trade.service.PaymentTradeService;
import cn.wuxia.project.payment.core.enums.ExpenseType;
import cn.wuxia.project.payment.core.service.FundsDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PaymentHandlerService extends EpayService {
    protected final Logger logger = LoggerFactory.getLogger("epay");

    @Autowired
    private PaymentTradeService paymentTradeService;

    @Autowired
    private FundsDetailService fundsDetailService;


    /**
     * 支付前操作
     *
     * @param paymentBean
     * @return
     * @author songlin
     */
    @Override
    public void beforePayment(EpayBeforeBean paymentBean) {
        try {
            paymentTradeService.save(beanToTrade(paymentBean));
        } catch (AppDaoException e) {
            throw new AppServiceException("保存失败", e);
        }
    }

    private PaymentTrade beanToTrade(EpayAfterBean paymentBean) {
        PaymentTrade trade = new PaymentTrade();
        BeanUtils.copyProperties(paymentBean, trade);
        return trade;
    }

    private PaymentTrade beanToTrade(EpayBeforeBean paymentBean) {
        PaymentTrade trade = new PaymentTrade();
        BeanUtils.copyProperties(paymentBean, trade);
        return trade;
    }

    /**
     * 支付完成后的操作
     */
    @Override
    public boolean afterPayment(EpayAfterBean paymentBean) throws EpayException {
        boolean isSuccess = false;
        /**
         * 支付完成后保存交易
         */
        List<PaymentTrade> list = paymentTradeService.findBySerialNoAndStatus(paymentBean.getSerialNumber(), PaymentTradeStatusEnum.DAIFUKUAN);
        // 如果不存在直接插入新的记录,如果存在则更新 
        if (list.isEmpty()) {
            throw new AppPermissionException("非法操作。");
        } else {
            PaymentTrade paymentTrade = list.get(0);
            paymentTrade.setPaymentNumber(paymentBean.getPaymentNumber());
            if (PaymentTradeStatusEnum.CHENGGONG.compareTo(paymentTrade.getStatus()) == 0) {
                throw new AppPermissionException("订单已交易成功！忽略重复：" + paymentBean.getSerialNumber());
            }

            // 判断充值的金额是否在网络上及页面上修改
            boolean sameAmount = paymentTrade.getAmount().compareTo(paymentBean.getAmount()) == 0 ? true : false;

            if (sameAmount) {
                //支付类型
                PaymentTradeTypeEnum paymentType = paymentTrade.getType();
                switch (paymentType) {
                    case DINGDAN:
                        logger.debug("进入订单付款成功后，更新订单.................");

                        break;
                    case CHONGZHI:
                        logger.debug("进入订单充值，充值金额：{}RMB", paymentBean.getAmount());
                        ExpenseType expensetype = null;
                        switch (paymentTrade.getPaymentPlatform()) {
                            case ALIPAY:
                                expensetype = ExpenseType.transfer_alipay;
                                break;
                            case BAIFUBAO:
                                break;
                            case CHINAPAY:
                                break;
                            case EPAYLINKS:
                                break;
                            case WECHATPAY:
                                expensetype = ExpenseType.transfer_wxpay;
                                break;
                            default:
                                break;

                        }
                        fundsDetailService.saveChargeTopup(paymentTrade.getUserId(), "CNY", paymentBean.getAmount(), paymentBean.getSerialNumber(),
                                expensetype, paymentTrade.getRemark());
                        break;
                    default:
                        break;
                }
                paymentTrade.setStatus(PaymentTradeStatusEnum.CHENGGONG);
                // TODO 是否需要邮件通知及短信通知
            } else {
                paymentTrade.setStatus(PaymentTradeStatusEnum.SHIBAI);
                // TODO 失败后的操作
            }
            paymentTrade.setTransDate(DateUtil.newInstanceDate());
            try {
                paymentTradeService.save(paymentTrade);
            } catch (AppDaoException e) {
                throw new EpayException("保存失败", e);
            }
        }
//        // 交易后添加操作记录
//        operationHistoryService.saveUserOperation();
        return isSuccess;
    }

}
