package ru.vichukano.akka.actors;


import akka.actor.typed.ActorSystem;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem<GuardianActor.Message> system = ActorSystem.create(GuardianActor.create(), "guard");
        system.tell(new GuardianActor.StartMessage("Sarah Connor"));
        system.tell(new GuardianActor.StartMessage("John Doe"));
        system.tell(new GuardianActor.StartMessage("Brother Lui"));
        system.tell(new GuardianActor.StopMessage());
    }

}
