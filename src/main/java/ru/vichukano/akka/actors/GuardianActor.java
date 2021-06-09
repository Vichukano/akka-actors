package ru.vichukano.akka.actors;

import akka.actor.PoisonPill;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GuardianActor extends AbstractBehavior<GuardianActor.Message> {
    private final Set<ActorRef<HelloActor.Command>> hellos;
    private final ActorRef<HelloActor.HelloAnswer> greetRef;

    private GuardianActor(ActorContext<Message> context) {
        super(context);
        greetRef = context.spawn(GreeterActor.create(), "greeter-actor");
        hellos = new HashSet<>();
    }

    public static Behavior<Message> create() {
        return Behaviors.setup(GuardianActor::new);
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartMessage.class, m -> {
                    ActorRef<HelloActor.Command> helloActor = getContext().spawn(HelloActor.create(), "hello-actor" + UUID.randomUUID());
                    helloActor.tell(new HelloActor.HelloMessage(m.text, greetRef));
                    hellos.add(helloActor);
                    return this;
                })
                .onMessage(StopMessage.class, m -> {
                    getContext().getLog().info("Start to stop hello actors");
                    hellos.forEach(a -> a.tell(new HelloActor.Stop()));
                    return this;
                })
                .build();
    }

    public interface Message {
    }

    public static class StartMessage implements Message {
        public final String text;

        public StartMessage(String text) {
            this.text = text;
        }
    }

    public static class StopMessage implements Message {

    }
}
