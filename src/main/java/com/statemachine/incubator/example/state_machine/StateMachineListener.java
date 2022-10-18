package com.statemachine.incubator.example.state_machine;

import com.statemachine.incubator.example.entity.Payment;
import com.statemachine.incubator.example.entity.PaymentStates;
import com.statemachine.incubator.example.entity.TrafficLight;
import com.statemachine.incubator.example.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.*;

import java.util.Objects;
import java.util.Random;

@WithStateMachine(id = "paymentListenner")
public class StateMachineListener {

    private static final Logger LOG = LoggerFactory.getLogger(StateMachineListener.class);

    /*
    private final PaymentRepository repository;
    @Autowired
    public StateMachineListener(PaymentRepository repository) {
        this.repository = repository;
    }
    */

    /**
     * 2 formas diferentes de fazer:
     * - A primeira (syncronizeStateMachineWithEntityInTheDatabase) faz o sync da status machine com a entity e já grava no BD
     * - A segunda (syncronizeStateMachineWithEntity) apenas faz o sync da status machine com a entity, porém deixa para o service persistir
     */

   /*
    @OnTransitionEnd(source = {"FLASHING_YELLOW", "RED", "YELLOW", "GREEN", "TURNED_OFF"})
    public void syncronizeStateMachineWithEntityInTheDatabase(StateContext context) {
        TrafficLight trafficLightFromStateMachine = context.getExtendedState().get("trafficLight", TrafficLight.class);
        trafficLightFromStateMachine.changeStateBasedOn(context.getStateMachine());
        var trafficLightFromDatabase = repository.save(trafficLightFromStateMachine);
        context.getExtendedState().getVariables().put("trafficLight", trafficLightFromDatabase);
    }
    */

    @OnTransitionEnd(target = {"IN_ANALYSIS","REJECTED_BY_FRAUD","AUTHORIZED", "CHECKING_AUTHORIZATION","NOT_AUTHORIZED"})
    public void syncronizeStateMachineWithEntity(StateContext<PaymentStates, PaymentEvents> context) {
        LOG.info(String.format("Listener OnTransitionEnd %s", context.getStateMachine().getState().getId()));
        var paymentFromStateMachine = context.getExtendedState().get("payment", Payment.class);

        if (Objects.nonNull(paymentFromStateMachine)) {

            paymentFromStateMachine.changeStateBasedOn(context.getStateMachine());

            context.getExtendedState()
                    .getVariables()
                    .put("payment", paymentFromStateMachine);
        }
    }
    @OnStateChanged()
    public void stateChanged(StateMachine<PaymentStates, PaymentEvents> from, StateMachine<PaymentStates, PaymentEvents> to) {
        LOG.info(String.format("State Changed from %s to %s", from.getState().getId(), from.getState().getId()));
    }

    @OnTransitionStart(target = {"IN_ANALYSIS"})
    public void analyzingPayment(StateContext<PaymentStates, PaymentEvents> context) {
        LOG.info(String.format("Listener OnTransitionStart %s", context.getStateMachine().getState().getId()));
        Boolean analysis = new Random().nextBoolean();
        context.getStateMachine()
                .getExtendedState()
                .getVariables()
                .put("isFraud", analysis);
    }

    @OnEventNotAccepted
    public void eventNotAccepted(StateContext<PaymentStates, PaymentEvents> context) {
        LOG.info(String.format("Listener OnEventNotAccepted %s", context.getStateMachine().getState().getId()));
        String messageError = String.format("Event (%s) not accepted for current state (%s)",
                context.getEvent(),
                context.getStateMachine().getState().getId().name());
        context.getExtendedState()
                .getVariables()
                .put("eventErrorMessage", messageError);
        //messagingService.send(routingKeyError, messageError);
    }
}
