package ru.vichukano.akka.actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class GreeterActor extends AbstractBehavior<HelloActor.HelloAnswer> {

    private GreeterActor(ActorContext<HelloActor.HelloAnswer> context) {
        super(context);
    }

    public static Behavior<HelloActor.HelloAnswer> create() {
        return Behaviors.setup(GreeterActor::new);
    }

    @Override
    public Receive<HelloActor.HelloAnswer> createReceive() {
        return newReceiveBuilder()
                .onMessage(HelloActor.HelloAnswer.class, m -> {
                    getContext().getLog().info("Receive answer: " + m.text + " from: " + m.from);
                    return this;
                })
                .build();
    }
}
