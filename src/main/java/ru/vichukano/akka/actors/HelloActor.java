package ru.vichukano.akka.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class HelloActor extends AbstractBehavior<HelloActor.Command> {

    private HelloActor(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(HelloActor::new);
    }

    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(HelloMessage.class, m -> {
                    getContext().getLog().info("Hello {}", m.whom);
                    m.from.tell(new HelloAnswer("Hi! How are you?", getContext().getSelf()));
                    return this;
                })
                .onMessage(Stop.class, m -> Behaviors.stopped())
                .onSignal(PostStop.class, m -> {
                    getContext().getLog().info("Stop actor: {}", getContext().getSelf());
                    return this;
                })
                .build();
    }

    public interface Command {
    }

    public static final class HelloMessage implements Command {
        public final String whom;
        public final ActorRef<HelloAnswer> from;

        public HelloMessage(String whom, ActorRef<HelloAnswer> from) {
            this.whom = whom;
            this.from = from;
        }
    }

    public static final class HelloAnswer {
        public final String text;
        public final ActorRef<Command> from;

        public HelloAnswer(String text, ActorRef<Command> from) {
            this.text = text;
            this.from = from;
        }
    }

    public static final class Stop implements Command {

    }
}
