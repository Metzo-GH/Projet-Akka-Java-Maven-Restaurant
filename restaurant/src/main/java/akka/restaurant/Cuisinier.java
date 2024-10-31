package akka.restaurant;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Cuisinier extends AbstractActor {
    static public Props props() {
        return Props.create(Cuisinier.class, Cuisinier::new);
    }

    static public class CuisinerPlat {
        public final String plat;
        public final String idCommande;
        public final ActorRef serveur;
        public CuisinerPlat(String plat, String idCommande, ActorRef serveur) {
            this.plat = plat;
            this.idCommande = idCommande;
            this.serveur = serveur;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CuisinerPlat.class, commande -> {
                    System.out.println("Cuisinier : Préparation du plat " + commande.plat + " [ID: " + commande.idCommande + "]");
                    Thread.sleep(1000);
                    getSender().tell(new Chef.PlatPret(commande.plat, commande.idCommande, commande.serveur), getSelf());
                })
                .build();
    }
}