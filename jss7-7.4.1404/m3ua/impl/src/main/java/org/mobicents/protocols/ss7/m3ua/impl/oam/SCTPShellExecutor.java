package org.mobicents.protocols.ss7.m3ua.impl.oam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.Server;
import org.mobicents.ss7.management.console.ShellExecutor;

public class SCTPShellExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(SCTPShellExecutor.class);

    private FastMap<String, Management> sctpManagements = new FastMap<String, Management>();

    private Management sctpManagement = null;

    public Map<String, Management> getSctpManagements() {
        return sctpManagements;
    }

    public void setSctpManagements(Map<String, Management> sctpManagementsTemp) {
        if (sctpManagementsTemp != null) {
            synchronized (this) {
                FastMap<String, Management> newsctpManagements = new FastMap<String, Management>();
                newsctpManagements.putAll(sctpManagementsTemp);
                this.sctpManagements = newsctpManagements;
            }
        }
    }

    private void setDefaultValue() {
        if (this.sctpManagement == null) {
            Map.Entry<String, Management> sctpManagementsTmp = this.sctpManagements.entrySet().iterator().next();
            this.sctpManagement = sctpManagementsTmp.getValue();
        }
    }

    private String showServers(String[] args) {

        // sctp server show stackname <stack-name>

        String sctpStackName = null;
        if (args.length > 3) {
            if (!args[3].equals("stackname")) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            sctpStackName = args[4];
            Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

            if (sctpManagementtmp == null) {
                return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
            }

            this.sctpManagement = sctpManagementtmp;
        } else {
            this.setDefaultValue();
        }

        List<Server> servers = this.sctpManagement.getServers();
        if (servers.size() == 0) {
            return String.format(SCTPOAMMessages.NO_SERVER_DEFINED_YET, this.sctpManagement.getName());
        }

        StringBuffer sb = new StringBuffer();
        for (Server server : servers) {
            sb.append(M3UAOAMMessages.NEW_LINE);
            sb.append("SERVER ").append(server.getIpChannelType().getType()).append(" name=").append(server.getName())
                    .append(" started=").append(server.isStarted()).append(" hostIp=").append(server.getHostAddress())
                    .append(" hostPort=").append(server.getHostport());

            String[] secondaryHostAdd = server.getExtraHostAddresses();

            if (secondaryHostAdd != null && secondaryHostAdd.length > 0) {
                sb.append(" secondaryHost=");
                for (int i = 0; i < secondaryHostAdd.length; i++) {
                    sb.append(secondaryHostAdd[i]).append(" ");
                }
            }

            List<String> associations = server.getAssociations();
            sb.append(M3UAOAMMessages.NEW_LINE);
            sb.append("Associations:");
            for (String s : associations) {
                sb.append(M3UAOAMMessages.TAB);
                sb.append(s).append(M3UAOAMMessages.NEW_LINE);
            }
            sb.append(M3UAOAMMessages.NEW_LINE);
        }
        return sb.toString();
    }

    private String showAssociations(String[] args) {
        // sctp association show stackname <stack-name>

        String sctpStackName = null;
        if (args.length > 3) {
            if (!args[3].equals("stackname")) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            sctpStackName = args[4];
            Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

            if (sctpManagementtmp == null) {
                return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
            }

            this.sctpManagement = sctpManagementtmp;
        } else {
            this.setDefaultValue();
        }

        Map<String, Association> associations = this.sctpManagement.getAssociations();
        if (associations.size() == 0) {
            return SCTPOAMMessages.NO_ASSOCIATION_DEFINED_YET;
        }

        StringBuffer sb = new StringBuffer();
        for (String s : associations.keySet()) {
            Association asso = associations.get(s);
            sb.append(M3UAOAMMessages.NEW_LINE);
            sb.append("ASSOCIATION ").append(asso.getIpChannelType().getType()).append(" name=").append(asso.getName())
                    .append(" started=").append(asso.isStarted()).append(" peerIp=").append(asso.getPeerAddress())
                    .append(" peerPort=").append(asso.getPeerPort());
            if (asso.getAssociationType() == AssociationType.CLIENT) {
                sb.append(" hostIp=").append(asso.getHostAddress()).append(" hostPort=").append(asso.getHostPort());
            } else {
                sb.append(" server=").append(asso.getServerName());
            }

            sb.append(" type=").append(asso.getAssociationType());

            String[] secondaryHostAdd = asso.getExtraHostAddresses();

            if (secondaryHostAdd != null && secondaryHostAdd.length > 0) {
                sb.append(" secondaryHost=");
                for (int i = 0; i < secondaryHostAdd.length; i++) {
                    sb.append(secondaryHostAdd[i]).append(" ");
                }
            }

            sb.append(M3UAOAMMessages.NEW_LINE);
        }

        return sb.toString();
    }

    @Override
    public String execute(String[] args) {
        try {
            if (this.sctpManagements.size() == 0) {
                return M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN;
            }

            if (args.length < 2 || args.length > 13) {
                // any command will have atleast 3 args
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (args[1] == null) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (args[1].equals("server")) {
                if (args.length < 3 ){
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                String command = args[2];

                if (command == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                } else if (command.equals("create")) {
                    // command is sctp server create <sever-name> <host-ip> <host-port> sockettype <socket-type> stackname
                    // <stack-name>

                    if (args.length < 6 || args.length > 10) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String hostAddress = args[4];
                    if (hostAddress == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String[] hostAddresses = hostAddress.split(",");
                    String primaryAddress = hostAddresses[0];
                    String[] secondaryAddresses = null;

                    if (hostAddresses.length > 1) {
                        secondaryAddresses = new String[(hostAddresses.length - 1)];
                        for (int i = 0; i < secondaryAddresses.length; i++) {
                            secondaryAddresses[i] = hostAddresses[(i + 1)];
                        }
                    }

                    int hostPort = Integer.parseInt(args[5]);

                    IpChannelType ipChnnelType = null;

                    String sctpStackName = null;

                    int i = 6;
                    if (args.length > 6) {

                        while (i < args.length) {

                            if (args[i].equals("stackname")) {
                                sctpStackName = args[i + 1];

                                Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

                                if (sctpManagementtmp == null) {
                                    return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
                                }

                                this.sctpManagement = sctpManagementtmp;
                            } else if (args[i].equals("sockettype")) {
                                ipChnnelType = IpChannelType.getInstance(args[i + 1]);
                            }

                            i = i + 2;

                        }// while loop
                    }

                    this.setDefaultValue();

                    if (ipChnnelType == null) {
                        ipChnnelType = IpChannelType.SCTP;
                    }

                    this.sctpManagement.addServer(serverName, primaryAddress, hostPort, ipChnnelType, secondaryAddresses);

                    return String.format(SCTPOAMMessages.ADD_SERVER_SUCCESS, serverName, this.sctpManagement.getName());

                } else if (command.equals("destroy")) {
                    // sctp server destroy <sever-name> stackname <stack-name>
                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String sctpStackName = null;
                    if (args.length > 4) {
                        if (!args[4].equals("stackname")) {
                            return M3UAOAMMessages.INVALID_COMMAND;
                        }

                        sctpStackName = args[5];
                        Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

                        if (sctpManagementtmp == null) {
                            return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
                        }

                        this.sctpManagement = sctpManagementtmp;
                    } else {
                        this.setDefaultValue();
                    }

                    this.sctpManagement.removeServer(serverName);
                    return String.format(SCTPOAMMessages.REMOVE_SERVER_SUCCESS, serverName, this.sctpManagement.getName());

                } else if (command.equals("start")) {
                    // sctp server start <sever-name> stackname <stack-name>

                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String sctpStackName = null;
                    if (args.length > 4) {
                        if (!args[4].equals("stackname")) {
                            return M3UAOAMMessages.INVALID_COMMAND;
                        }

                        sctpStackName = args[5];
                        Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

                        if (sctpManagementtmp == null) {
                            return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
                        }

                        this.sctpManagement = sctpManagementtmp;
                    } else {
                        this.setDefaultValue();
                    }

                    this.sctpManagement.startServer(serverName);
                    return String.format(SCTPOAMMessages.START_SERVER_SUCCESS, serverName, this.sctpManagement.getName());
                } else if (command.equals("stop")) {
                    // sctp server stop <sever-name> stackname <stack-name>

                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String sctpStackName = null;
                    if (args.length > 4) {
                        if (!args[4].equals("stackname")) {
                            return M3UAOAMMessages.INVALID_COMMAND;
                        }

                        sctpStackName = args[5];
                        Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

                        if (sctpManagementtmp == null) {
                            return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
                        }

                        this.sctpManagement = sctpManagementtmp;
                    } else {
                        this.setDefaultValue();
                    }

                    this.sctpManagement.stopServer(serverName);
                    return String.format(SCTPOAMMessages.STOP_SERVER_SUCCESS, serverName, this.sctpManagement.getName());
                } else if (command.equals("show")) {
                    return this.showServers(args);
                }

                return M3UAOAMMessages.INVALID_COMMAND;

            } else if (args[1].equals("association")) {
                if (args.length < 3 ){
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                String command = args[2];

                if (command == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                } else if (command.equals("create")) {
                    // sctp association create <assoc-name> <CLIENT | SERVER> <server-name> <peerip> <peer-port> <host- ip>
                    // <host-port> sockettype <socket-type> stackname <stack-name>

                    if (args.length < 8 || args.length > 13) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String assocName = args[3];
                    if (assocName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String type = args[4];
                    if (type == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    } else if (type.equals("CLIENT")) {
                        if (args.length < 9) {
                            return M3UAOAMMessages.INVALID_COMMAND;
                        }

                        String peerIp = args[5];
                        int peerPort = Integer.parseInt(args[6]);

                        String hostIp = args[7];

                        String[] hostAddresses = hostIp.split(",");
                        String primaryAddress = hostAddresses[0];
                        String[] secondaryAddresses = null;

                        if (hostAddresses.length > 1) {
                            secondaryAddresses = new String[(hostAddresses.length - 1)];
                            for (int i = 0; i < secondaryAddresses.length; i++) {
                                secondaryAddresses[i] = hostAddresses[(i + 1)];
                            }
                        }

                        int hostPort = Integer.parseInt(args[8]);

                        IpChannelType ipChnnelType = null;

                        String sctpStackName = null;

                        int i = 9;
                        if (args.length > 9) {

                            while (i < args.length) {

                                if (args[i].equals("stackname")) {
                                    sctpStackName = args[i + 1];

                                    Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

                                    if (sctpManagementtmp == null) {
                                        return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
                                    }

                                    this.sctpManagement = sctpManagementtmp;
                                } else if (args[i].equals("sockettype")) {
                                    ipChnnelType = IpChannelType.getInstance(args[i + 1]);
                                }

                                i = i + 2;

                            }// while loop
                        }

                        this.setDefaultValue();

                        if (ipChnnelType == null) {
                            ipChnnelType = IpChannelType.SCTP;
                        }

                        this.sctpManagement.addAssociation(primaryAddress, hostPort, peerIp, peerPort, assocName, ipChnnelType,
                                secondaryAddresses);

                        return String.format(SCTPOAMMessages.ADD_CLIENT_ASSOCIATION_SUCCESS, assocName,
                                this.sctpManagement.getName());
                    } else if (type.equals("SERVER")) {
                        String serverName = args[5];

                        String peerIp = args[6];
                        int peerPort = Integer.parseInt(args[7]);

                        IpChannelType ipChnnelType = null;

                        String sctpStackName = null;

                        int i = 8;
                        if (args.length > 8) {

                            while (i < args.length) {

                                if (args[i].equals("stackname")) {
                                    sctpStackName = args[i + 1];

                                    Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

                                    if (sctpManagementtmp == null) {
                                        return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
                                    }

                                    this.sctpManagement = sctpManagementtmp;
                                } else if (args[i].equals("sockettype")) {
                                    ipChnnelType = IpChannelType.getInstance(args[i + 1]);
                                }

                                i = i + 2;

                            }// while loop
                        }

                        if (this.sctpManagement == null) {
                            this.setDefaultValue();
                        }

                        if (ipChnnelType == null) {
                            ipChnnelType = IpChannelType.SCTP;
                        }

                        this.sctpManagement.addServerAssociation(peerIp, peerPort, serverName, assocName, ipChnnelType);
                        return String.format(SCTPOAMMessages.ADD_SERVER_ASSOCIATION_SUCCESS, assocName,
                                this.sctpManagement.getName());
                    }

                    return M3UAOAMMessages.INVALID_COMMAND;

                } else if (command.equals("destroy")) {
                    // sctp association destroy <assoc-name> stackname <stack-name>

                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String assocName = args[3];
                    if (assocName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String sctpStackName = null;

                    if (args.length > 4) {
                        if (!args[4].equals("stackname")) {
                            return M3UAOAMMessages.INVALID_COMMAND;
                        }

                        sctpStackName = args[5];
                        Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

                        if (sctpManagementtmp == null) {
                            return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
                        }

                        this.sctpManagement = sctpManagementtmp;
                    } else {
                        this.setDefaultValue();
                    }

                    this.sctpManagement.removeAssociation(assocName);
                    return String.format(SCTPOAMMessages.REMOVE_ASSOCIATION_SUCCESS, assocName, this.sctpManagement.getName());

                } else if (command.equals("show")) {
                    return this.showAssociations(args);
                }

                return M3UAOAMMessages.INVALID_COMMAND;
            } else if (args[1].equals("set")) {
                return this.manageSet(args);
            } else if (args[1].equals("get")) {
                return this.manageGet(args);
            }

            return M3UAOAMMessages.INVALID_COMMAND;
        } catch (Exception e) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), e);
            return e.toString();
        } catch (Throwable t) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), t);
            return t.toString();
        }
    }

    private String manageSet(String[] options) throws Exception {
        // Minimum 4 needed. Show

        // sctp set <command> <value> stackname <stack-name>
        if (options.length < 4 || options.length > 6) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        String sctpStackName = null;
        if (options.length > 4) {
            if (!options[4].equals("stackname")) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            sctpStackName = options[5];
            Management sctpManagementtmp = this.sctpManagements.get(sctpStackName);

            if (sctpManagementtmp == null) {
                return String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, sctpStackName);
            }

            this.sctpManagement = sctpManagementtmp;
        } else {
            this.setDefaultValue();
        }

        String parName = options[2].toLowerCase();

        if (parName.equals("connectdelay")) {
            int val = Integer.parseInt(options[3]);
            this.sctpManagement.setConnectDelay(val);
//        } else if (parName.equals("singlethread")) {
//            boolean val = Boolean.parseBoolean(options[3]);
//            this.sctpManagement.setSingleThread(val);
//        } else if (parName.equals("workerthreads")) {
//            int val = Integer.parseInt(options[3]);
//            this.sctpManagement.setWorkerThreads(val);
        } else {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        return String.format(M3UAOAMMessages.PARAMETER_SUCCESSFULLY_SET, this.sctpManagement.getName());
    }

    private String manageGet(String[] options) throws Exception {
        // sctp get <command>

        // Minimum 2 needed. Show
        if (options.length < 2 || options.length > 3) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        if (options.length == 3) {
            this.setDefaultValue();

            String parName = options[2].toLowerCase();

            StringBuilder sb = new StringBuilder();
            sb.append("stack=").append(this.sctpManagement.getName()).append(" ");

            sb.append(options[2]);
            sb.append(" = ");
            if (parName.equals("connectdelay")) {
                sb.append(this.sctpManagement.getConnectDelay());
            } else if (parName.equals("singlethread")) {
                sb.append(this.sctpManagement.isSingleThread());
            } else if (parName.equals("workerthreads")) {
                sb.append(this.sctpManagement.getWorkerThreads());
            } else {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            return sb.toString();
        } else {

            StringBuilder sb = new StringBuilder();
            for (FastMap.Entry<String, Management> e = this.sctpManagements.head(), end = this.sctpManagements.tail(); (e = e
                    .getNext()) != end;) {

                Management managementImplTmp = e.getValue();
                String stackname = e.getKey();

                sb.append("Properties for ");
                sb.append(stackname);
                sb.append("\n");
                sb.append("*******************");
                sb.append("\n");

                sb.append("connectdelay = ");
                sb.append(managementImplTmp.getConnectDelay());
                sb.append("\n");

                sb.append("singlethread = ");
                sb.append(managementImplTmp.isSingleThread());
                sb.append("\n");

                sb.append("workerthreads = ");
                sb.append(managementImplTmp.getWorkerThreads());
                sb.append("\n");

                sb.append("*******************");
                sb.append("\n");
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang. String)
     */
    @Override
    public boolean handles(String command) {
        return command.startsWith("sctp");
    }

}
