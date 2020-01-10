package com.company;

import com.company.client.ILabClient;
import com.company.client.LabClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            IConnectivityOption option = option = new ConnectivityOption(args);
            ILabClient client = new LabClient(System.in, System.out);
            client.start(option.getIP(), option.getPort(), option.getName());
        } catch (Exception ignored) {}
    }
}
