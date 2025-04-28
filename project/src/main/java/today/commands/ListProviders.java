package today.commands;

import java.util.List;
import picocli.CommandLine.Command;
import today.EventManager;

@Command(name = "listproviders", description = "Shows a list of event providers")
public class ListProviders implements Runnable {

    @Override
    public void run() {
        //System.out.println("Listing event provider IDs");
        EventManager manager = EventManager.getInstance();

        List<String> providerIds = manager.getEventProviderIdentifiers();
        for (String id : providerIds) {
            System.out.println(id);
        }
    }
}
