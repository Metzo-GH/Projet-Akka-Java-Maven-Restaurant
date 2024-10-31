package akka.restaurant;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Serveur extends AbstractActor {
    private final ActorRef chef;

    public Serveur(ActorRef chef) {
        this.chef = chef;
    }

    static public Props props(ActorRef chef) {
        return Props.create(Serveur.class, () -> new Serveur(chef));
    }

    static public class CommanderPlat {
        public final String plat;
        public final String idCommande;
        public CommanderPlat(String plat, String idCommande) { 
            this.plat = plat;
            this.idCommande = idCommande;
        }
    }

    static public class PlatServi {
        public final String plat;
        public final String idCommande;  // ID de la commande
        public PlatServi(String plat, String idCommande) { 
            this.plat = plat;
            this.idCommande = idCommande;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CommanderPlat.class, command -> {
                    System.out.println("Serveur : Commande reçue pour le plat " + command.plat + " [ID: " + command.idCommande + "]");
                    chef.tell(new Chef.PreparerPlat(command.plat, command.idCommande, getSelf()), getSelf());
                })
                .match(PlatServi.class, platServi -> {
                    System.out.println("Serveur : Plat servi - " + platServi.plat + " [ID: " + platServi.idCommande + "]");
                })
                .build();
    }
}