package akka.restaurant;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Client extends AbstractActor {
    private final ActorRef serveur;
    private final String[] plats = {"Carbonnade_flamande", "Welsh", "Kebab", "Poulet_Mafe"};
    private final String nomClient;

    public Client(ActorRef serveur, String nomClient) {
        this.serveur = serveur;
        this.nomClient = nomClient;
    }

    static public Props props(ActorRef serveur, String nomClient) {
        return Props.create(Client.class, () -> new Client(serveur, nomClient));
    }

    @Override
    public void preStart() {
        String platCommande = plats[(int) (Math.random() * plats.length)];
        serveur.tell(new Serveur.CommanderPlat(platCommande, nomClient), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}