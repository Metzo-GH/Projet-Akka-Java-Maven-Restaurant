package akka.restaurant;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("restaurant-system");

        // Créer les cuisiniers
        List<ActorRef> cuisiniers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cuisiniers.add(system.actorOf(Cuisinier.props(), "cuisinier" + i));
        }

        // Créer le chef
        ActorRef chef = system.actorOf(Chef.props(cuisiniers), "chef");

        // Créer les serveurs
        List<ActorRef> serveurs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            serveurs.add(system.actorOf(Serveur.props(chef), "serveur" + i));
        }

        // Créer les clients sans assignation à une variable
        for (int i = 0; i < 5; i++) {
            String nomClient = "Client " + i;
            system.actorOf(Client.props(serveurs.get(i % serveurs.size()), nomClient), "client" + i);
        }

        // Arrêter le système après quelques secondes pour observer les logs
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            system.terminate();
        }
    }
}