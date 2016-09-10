package fr.nocloud.maestro.utils;

import fr.nocloud.maestro.model.Iptable;

/**
 */
public class Iptables {

    public static void accept(Iptable iptable) {

        ProcessUtils.exec("iptables", "-I", "INPUT", "-p", "tcp", "--dport", String.valueOf(iptable.getPort()), "-j", "ACCEPT");
    }

    public static void delete(Iptable iptable) {

        ProcessUtils.exec("iptables", "-D", "INPUT", "-p", "tcp", "--dport", String.valueOf(iptable.getPort()), "-j", "ACCEPT");
    }
}
