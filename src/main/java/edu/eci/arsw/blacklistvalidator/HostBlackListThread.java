package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.ArrayList;
import java.util.List;

public class HostBlackListThread extends Thread {

    private final HostBlacklistsDataSourceFacade facade;
    private final String ipaddress; // Cambiado de int a String
    private final int startServer;
    private final int endServer;
    private int occurrencesMaliciousServers;
    private final List<Integer> blackListOccurrences;

    public HostBlackListThread(String ipaddress, int startServer, int endServer) {

        this.facade = HostBlacklistsDataSourceFacade.getInstance();
        this.ipaddress = ipaddress;
        this.startServer = startServer;
        this.endServer = endServer;
        this.occurrencesMaliciousServers = 0;
        this.blackListOccurrences = new ArrayList<Integer>();
    }

    @Override
    public void run() {
        try {
            for (int i = startServer; i < endServer -1; i++) {
                if (facade.isInBlackListServer(i, ipaddress)) {
                    blackListOccurrences.add(i);
                    occurrencesMaliciousServers++;
                }
            }
            Thread.sleep(4000); // 3 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getServersCount() {
        return occurrencesMaliciousServers;
    }

    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }
}