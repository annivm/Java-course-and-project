package viikko13.commands;

import java.util.List;
import picocli.CommandLine.Command;
import viikko13.EventManager;

@Command(name = "listproviders")
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
