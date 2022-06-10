package com.statemachine.demo.statemachine.payments;

import com.statemachine.demo.entity.Payment;
import com.statemachine.demo.entity.PaymentAnalysis;
import com.statemachine.demo.entity.PaymentAnalysis.AnalysisType;
import com.statemachine.demo.repository.PaymentAnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static com.statemachine.demo.entity.PaymentAnalysis.AnalysisType.CREDIT_CARD;
import static com.statemachine.demo.entity.PaymentAnalysis.AnalysisType.SERASA;
import static com.statemachine.demo.statemachine.payments.PaymentCommandType.EXECUTE_PAYMENT;
import static com.statemachine.demo.statemachine.payments.PaymentCommandType.FINISH_CREDIT_CARD_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentCommandType.FINISH_SERASA_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentCommandType.FRAUD_DETECTED;
import static com.statemachine.demo.statemachine.payments.PaymentCommandType.START_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.ANALYSIS_FINISHED;
import static com.statemachine.demo.statemachine.payments.PaymentState.AUTHORIZED;
import static com.statemachine.demo.statemachine.payments.PaymentState.CREDIT_ANALYSIS_FINISHED;
import static com.statemachine.demo.statemachine.payments.PaymentState.CREDIT_CARD_IN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.IN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.JOIN_ALL_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.NOT_AUTHORIZED;
import static com.statemachine.demo.statemachine.payments.PaymentState.REJECTED_BY_FRAUD;
import static com.statemachine.demo.statemachine.payments.PaymentState.SERASA_ANALYSIS_FINISHED;
import static com.statemachine.demo.statemachine.payments.PaymentState.SERASA_IN_ANALYSIS;
import static com.statemachine.demo.statemachine.payments.PaymentState.STARTED;
import static com.statemachine.demo.statemachine.payments.PaymentState.TRY_TO_PAY;
import static java.util.function.Function.identity;

@Configuration
//@EnableStateMachine
@EnableStateMachineFactory(contextEvents = false)
public class PaymentAdapterConfig extends EnumStateMachineConfigurerAdapter<PaymentState, PaymentCommandType> {

    private static Logger LOG = LoggerFactory.getLogger(PaymentAdapterConfig.class);

    @Autowired
    private PaymentAnalysisRepository paymentAnalysisRepository;
    @Autowired
    private Teste teste;

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentCommandType> config) throws Exception {
        config.withConfiguration()
//                .taskExecutor(taskExecutor())
                .machineId("paymentListenner")
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentCommandType> states) throws Exception {
        states.withStates()
            .initial(STARTED)
            .fork(IN_ANALYSIS)
            .join(JOIN_ALL_ANALYSIS)
            .choice(TRY_TO_PAY)
            .state(ANALYSIS_FINISHED, checkFraud())
            .end(REJECTED_BY_FRAUD)
            .end(AUTHORIZED)
            .end(NOT_AUTHORIZED)
            .and()
            .withStates()
                .parent(IN_ANALYSIS)
                .initial(SERASA_IN_ANALYSIS)
                .state(SERASA_IN_ANALYSIS, executeSerasaAnalysis())
                .stateDo(SERASA_ANALYSIS_FINISHED, saveSerasaAnalysis())
                .end(SERASA_ANALYSIS_FINISHED)
                .and()
            .withStates()
                .parent(IN_ANALYSIS)
                .initial(CREDIT_CARD_IN_ANALYSIS)
                .state(CREDIT_CARD_IN_ANALYSIS, executeCreditCardAnalysis())
                .stateDo(CREDIT_ANALYSIS_FINISHED, saveCreditCardAnalysis())
                .end(CREDIT_ANALYSIS_FINISHED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentCommandType> transitions) throws Exception {
        transitions.withExternal()
            .source(STARTED).target(IN_ANALYSIS).event(START_ANALYSIS)
        .and()
           .withExternal()
           .source(SERASA_IN_ANALYSIS).target(SERASA_ANALYSIS_FINISHED).event(FINISH_SERASA_ANALYSIS)
        .and()
           .withExternal()
           .source(CREDIT_CARD_IN_ANALYSIS).target(CREDIT_ANALYSIS_FINISHED).event(FINISH_CREDIT_CARD_ANALYSIS)
        .and().withExternal()
            .source(JOIN_ALL_ANALYSIS).target(ANALYSIS_FINISHED)
        .and().withExternal()
            .source(ANALYSIS_FINISHED).target(TRY_TO_PAY).event(EXECUTE_PAYMENT)
        .and()
            .withExternal().source(ANALYSIS_FINISHED).target(REJECTED_BY_FRAUD).event(FRAUD_DETECTED)
        .and()
            .withFork()
            .source(IN_ANALYSIS)
            .target(SERASA_IN_ANALYSIS)
            .target(CREDIT_CARD_IN_ANALYSIS)
        .and()
            .withJoin()
            .source(SERASA_ANALYSIS_FINISHED)
            .source(CREDIT_ANALYSIS_FINISHED)
            .target(JOIN_ALL_ANALYSIS)
        .and()
            .withChoice()
            .source(TRY_TO_PAY)
            .then(AUTHORIZED, executePayment())
            .last(NOT_AUTHORIZED);
    }

    @Bean
    @Transactional
    public Action<PaymentState, PaymentCommandType> checkFraud() {
        return ctx -> {

            try {
                LOG.info("=================== > Detecting Possible Frauds... < ===============================");
                Payment payment = ctx.getExtendedState().get("payment", Payment.class);

                Map<AnalysisType, PaymentAnalysis> analysisMap = paymentAnalysisRepository.findByPaymentId(payment.getId())
                        .stream()
                        .collect(Collectors.toMap(PaymentAnalysis::getAnalysisType, identity()));

                Boolean serasaApproved = analysisMap.get(SERASA).isApproved();
                Boolean creditCardApproved = analysisMap.get(CREDIT_CARD).isApproved();

                LOG.info("Serasa Approved: {}", serasaApproved);
                LOG.info("Credit Card Approved: {}", creditCardApproved);

                if (!serasaApproved || !creditCardApproved) {
                    ctx.getStateMachine().sendEvent(FRAUD_DETECTED);
                } else {
                    ctx.getStateMachine().sendEvent(EXECUTE_PAYMENT);
                }

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        };
    }

    @Bean
    public Guard<PaymentState, PaymentCommandType> executePayment() {
        return ctx -> {
            LOG.info("=================== > Executing Payment... < ===============================");
            var result = new Random().nextBoolean();
            LOG.info("=================== > Is Payment Authorized? {} < ===============================", result);

            return result;
        };
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        return taskExecutor;
    }

    @Bean
    public Action<PaymentState, PaymentCommandType> executeSerasaAnalysis() {
        return ctx -> LOG.info("=================== > Executing Serasa Analysis... < ===============================");
    }

    @Bean
    @Transactional
    public Action<PaymentState, PaymentCommandType> saveSerasaAnalysis() {
        return ctx -> {
            LOG.info("=================== > Saving Serasa Analysis... < =================================");
            Payment payment = ctx.getExtendedState().get("payment", Payment.class);
            boolean resultOfAnalysis = new Random().nextBoolean();
            paymentAnalysisRepository.save(new PaymentAnalysis(payment.getId(), SERASA, resultOfAnalysis));

            ctx.getExtendedState().getVariables().put("SerasaApproved", resultOfAnalysis);
        };
    }

    @Bean
    public Action<PaymentState, PaymentCommandType> executeCreditCardAnalysis() {
        return ctx -> LOG.info("=================== > Executing CreditCard Analysis... < =============================");
    }

    @Bean
    @Transactional
    public Action<PaymentState, PaymentCommandType> saveCreditCardAnalysis() {
        return ctx -> {
            LOG.info("=================== > Saving Credit Card Analysis... < =================================");
            Payment payment = ctx.getExtendedState().get("payment", Payment.class);
            boolean resultOfAnalysis = new Random().nextBoolean();
            paymentAnalysisRepository.save(new PaymentAnalysis(payment.getId(), CREDIT_CARD, resultOfAnalysis));

            ctx.getExtendedState().getVariables().put("CreditCardApproved",  resultOfAnalysis);
        };
    }
}
