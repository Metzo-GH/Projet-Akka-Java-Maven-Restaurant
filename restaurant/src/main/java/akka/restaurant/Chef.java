package akka.restaurant;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.List;

public class Chef extends AbstractActor {
    private final List<ActorRef> cuisiniers;
    private int cuisinierIndex = 0;

    public Chef(List<ActorRef> cuisiniers) {
        this.cuisiniers = cuisiniers;
    }

    static public Props props(List<ActorRef> cuisiniers) {
        return Props.create(Chef.class, () -> new Chef(cuisiniers));
    }

    static public class PreparerPlat {
        public final String plat;
        public final String idCommande;
        public final ActorRef serveur;
        public PreparerPlat(String plat, String idCommande, ActorRef serveur) {
            this.plat = plat;
            this.idCommande = idCommande;
            this.serveur = serveur;
        }
    }

    static public class PlatPret {
        public final String plat;
        public final String idCommande;
        public final ActorRef serveur;
        public PlatPret(String plat, String idCommande, ActorRef serveur) {
            this.plat = plat;
            this.idCommande = idCommande;
            this.serveur = serveur;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PreparerPlat.class, commande -> {
                    System.out.println("Chef : Assignation de la commande " + commande.plat + " [ID: " + commande.idCommande + "]");
                    cuisiniers.get(cuisinierIndex).tell(new Cuisinier.CuisinerPlat(commande.plat, commande.idCommande, commande.serveur), getSelf());
                    cuisinierIndex = (cuisinierIndex + 1) % cuisiniers.size();
                })
                .match(PlatPret.class, plat -> {
                    System.out.println("Chef : Plat prêt - " + plat.plat + " [ID: " + plat.idCommande + "]");
                    plat.serveur.tell(new Serveur.PlatServi(plat.plat, plat.idCommande), getSelf());
                })
                .build();
    }
}