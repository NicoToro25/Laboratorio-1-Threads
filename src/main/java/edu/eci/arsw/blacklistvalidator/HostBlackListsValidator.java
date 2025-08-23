/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nect25
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */

    public List<Integer> checkHost(String ipaddress, int N) {

        LinkedList<Integer> blackListOcurrences = new LinkedList<>();
        int ocurrencesCount = 0;
        int checkedListsCount = 0;

        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        int totalServers = skds.getRegisteredServersCount();

        int size = totalServers / N;
        int remainder = totalServers % N;

        List<HostBlackListThread> threads = new ArrayList<>();

        // 1. Crear y lanzar los hilos
        int start = 0;
        for (int i = 0; i < N; i++) {
            int end = start + size - 1;

            // Manejo de par/impar
            if (i < remainder) {
                end++;
            }

            // Límite final
            if (end >= totalServers) {
                end = totalServers - 1;
            }

            HostBlackListThread thread = new HostBlackListThread(ipaddress, start, end);
            threads.add(thread);
            thread.start();

            start = end + 1;
        }

        // 2. join()
        for (HostBlackListThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Logger.getLogger(HostBlackListsValidator.class.getName()).log(Level.SEVERE, "Thread interrupted", e);
                Thread.currentThread().interrupt();
            }
        }

        // 3. Recolectar resultados
        for (HostBlackListThread thread: threads) {
            blackListOcurrences.addAll(thread.getBlackListOccurrences());
            ocurrencesCount += thread.getServersCount();
        }

        // 4. ¿Es confiable o no?
        if (ocurrencesCount >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }

        // 5. LOG
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}",
                new Object[]{checkedListsCount, totalServers});

        return blackListOcurrences;
    }

    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
}
