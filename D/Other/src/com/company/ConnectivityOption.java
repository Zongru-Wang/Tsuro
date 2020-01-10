package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList.*;
import java.util.List;

public class ConnectivityOption implements IConnectivityOption {

    String ipAdress = "";
    String port = "";
    String name = "";

    ConnectivityOption(String[] args) {

       if (args.length == 0) {
           this.ipAdress = "127.0.0.1";
           this.port = "8000";
           this.name = "John Doe";
       }

       else if (args.length == 1 ) {
           if (!this.ipValidator(args[0])) {
               throw new IllegalArgumentException("Please give a valid Ip address!");
           }
           this.ipAdress = args[0];
           this.port = "8000";
           this.name = "John Doe";

       }

       else if (args.length == 2) {
           if ((!ipValidator(args[0])) || (!portValidator(args[1]))) {
               throw new IllegalArgumentException("Please input a valid ip address and port number");
           }
           this.port = args[1];
           this.ipAdress = args[0];
           this.port = args[1];
           this.name = "John Doe";
       }

       else if (args.length >= 3) {
           if ((!ipValidator(args[0])) || (!portValidator(args[1]))) {
               throw new IllegalArgumentException("Please input a valid ip address and port number");
           }
           this.ipAdress = args[0];
           this.port = args[1];

           String wholeName = "";
           if (args.length == 3) {
               this.name = args[2];

           } else {
               for (int i = 2; i < args.length; i++) {
                   wholeName +=  args[i] + " ";
               }


               this.name = wholeName.trim();
           }



           if (this.name.isEmpty()) {
               throw new IllegalArgumentException("if you input a name the name can't be empty");
           }
       }




    }

    private boolean ipValidator(String ipAddress) {

        String zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

        String IP_REGEXP = zeroTo255 + "\\." + zeroTo255 + "\\."
                + zeroTo255 + "\\." + zeroTo255;

        final boolean match = ipAddress.matches("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
        return match;

    }

    private boolean portValidator(String portNumber) {
        try {
            int port = Integer.parseInt(portNumber);
            if (0 < port && port <= 65535) {
                return true;
            } else {
                return false;
            }
        } catch ( Exception e ) {
            return false;
        }


    }

    @Override
    public String getIP() {

        return this.ipAdress;

    }

    @Override
    public int getPort() {
        return Integer.valueOf(this.port);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
