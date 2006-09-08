package org.opennms.install;

public class DbTest extends TemporaryDatabaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Sadly, this doesn't work.  The unique index required on columns that are
     * referenced by foreign keys cannot have a WHERE clause on it.
     */ 
    public void XXXtestConstraintWithWhere() {
        if (!isDBTestEnabled()) {
            return;
        }
        
        executeSQL("create table node (\n"
                   + "nodeID          integer not null,\n"
                   + "dpName          varchar(12),\n"
                   + "nodeCreateTime  timestamp without time zone not null,\n"
                   + "\n"
                   + "constraint pk_nodeID primary key (nodeID)\n"
                   + ");\n"
        );
        

        executeSQL("create table snmpInterface (\n"
                   + "nodeID                  integer not null,\n"
                   + "ipAddr                  varchar(16) not null,\n"
                   + "snmpIfIndex             integer not null,\n"
                   + "\n"
                   + "constraint fk_nodeID2 foreign key (nodeID) references node ON DELETE CASCADE\n"
                   + ");"
        );

        executeSQL("create unique index snmpinterface_nodeid_ifindex_idx on snmpinterface(nodeID, snmpIfIndex);\n");
        
        executeSQL("create table ipInterface (\n"
                   + "nodeID                  integer not null,\n"
                   + "ipAddr                  varchar(16) not null,\n"
                   + "ifIndex                 integer,\n"
                   + "\n"
                   + "CONSTRAINT snmpinterface_fkey1 FOREIGN KEY (nodeID, ifIndex) REFERENCES snmpInterface (nodeID, snmpIfIndex) ON DELETE CASCADE,\n"
                   + "constraint fk_nodeID1 foreign key (nodeID) references node ON DELETE CASCADE\n"
                   + ");"
        );

        executeSQL("create unique index ipinterface_nodeid_ipaddr_ifindex_idx on ipInterface (nodeID, ipAddr, ifIndex);");
//      executeSQL("create unique index ipinterface_nodeid_ipaddr_idx on ipInterface (nodeID, ipAddr);");
//        executeSQL("create index ipinterface_nodeid_ipaddr_idx on ipInterface (nodeID, ipAddr);");
        executeSQL("create unique index ipinterface_nodeid_ipaddr_where_idx on ipInterface (nodeID, ipAddr) WHERE ipAddr != '0.0.0.0';");
        
        executeSQL("create table service (\n"
                   + "serviceID               integer not null,\n"
                   + "serviceName             varchar(32) not null,\n"
                   + "\n"
                   + "constraint pk_serviceID primary key (serviceID)\n"
                   + ");"
        );
        
        executeSQL("create table ifServices (\n"
                   + "nodeID                  integer not null,\n"
                   + "ipAddr                  varchar(16) not null,\n"
                   + "ifIndex                 integer,\n"
                   + "serviceID               integer not null,\n"
                   + "\n"
                   + "constraint ifServices_ipaddr_check CHECK ( ipAddr != '0.0.0.0' ),\n"
//                 + "CONSTRAINT ipinterface_fkey1 FOREIGN KEY (nodeID,ipAddr,ifIndex) REFERENCES ipInterface (nodeID, ipAddr,ifIndex) ON DELETE CASCADE ON UPDATE CASCADE,\n"
                   + "CONSTRAINT ipinterface_fkey1 FOREIGN KEY (nodeID,ipAddr) REFERENCES ipInterface (nodeID, ipAddr) ON DELETE CASCADE ON UPDATE CASCADE,\n"
                   + "constraint fk_nodeID3 foreign key (nodeID) references node ON DELETE CASCADE,\n"
                   + "constraint fk_serviceID1 foreign key (serviceID) references service ON DELETE CASCADE\n"
                   + ");"
        );

        executeSQL("create unique index ifservices_nodeid_ipaddr_svc on ifservices(nodeID, ipAddr, serviceId);");

        // Data
        executeSQL("INSERT INTO node ( nodeId, nodeCreateTime) VALUES ( 1, now() )");
        executeSQL("INSERT INTO snmpInterface ( nodeId, ipAddr, snmpIfIndex) VALUES ( 1, '1.2.3.4', 1 )");
        executeSQL("INSERT INTO snmpInterface ( nodeId, ipAddr, snmpIfIndex) VALUES ( 1, '1.2.3.6', -100 )");
        executeSQL("INSERT INTO snmpInterface ( nodeId, ipAddr, snmpIfIndex) VALUES ( 1, '0.0.0.0', 2 )");
        executeSQL("INSERT INTO snmpInterface ( nodeId, ipAddr, snmpIfIndex) VALUES ( 1, '0.0.0.0', 3 )");
        executeSQL("INSERT INTO ipInterface ( nodeId, ipAddr, ifIndex ) VALUES ( 1, '1.2.3.4', 1 )");
        executeSQL("INSERT INTO ipInterface ( nodeId, ipAddr, ifIndex ) VALUES ( 1, '1.2.3.5', null )");
        executeSQL("INSERT INTO ipInterface ( nodeId, ipAddr, ifIndex ) VALUES ( 1, '1.2.3.6', -100 )");
        executeSQL("INSERT INTO ipInterface ( nodeId, ipAddr, ifIndex ) VALUES ( 1, '0.0.0.0', 2 )");
        executeSQL("INSERT INTO ipInterface ( nodeId, ipAddr, ifIndex ) VALUES ( 1, '0.0.0.0', 3 )");
        executeSQL("INSERT INTO service ( serviceID, serviceName ) VALUES ( 1, 'COFFEE-READY' )");
        executeSQL("INSERT INTO service ( serviceID, serviceName ) VALUES ( 2, 'TEA-READY' )");
        executeSQL("INSERT INTO ifServices ( nodeID, ipAddr, ifIndex, serviceID ) VALUES ( 1, '1.2.3.4', 1, 1 )");
        executeSQL("INSERT INTO ifServices ( nodeID, ipAddr, ifIndex, serviceID ) VALUES ( 1, '1.2.3.5', null, 1 )");
        executeSQL("INSERT INTO ifServices ( nodeID, ipAddr, ifIndex, serviceID ) VALUES ( 1, '1.2.3.6', -100, 1 )");
//        executeSQL("INSERT INTO ifServices ( nodeID, ipAddr, ifIndex, serviceID ) VALUES ( 1, '1.2.3.6', null, 2 )");
        /*
        executeSQL("INSERT INTO outages ( outageId, nodeId, ipAddr, ifLostService, serviceID ) "
                   + "VALUES ( nextval('outageNxtId'), 1, '1.2.3.4', now(), 1 )");
        executeSQL("INSERT INTO outages ( outageId, nodeId, ipAddr, ifLostService, serviceID ) "
                   + "VALUES ( nextval('outageNxtId'), 1, '1.2.3.5', now(), 1 )");
        executeSQL("INSERT INTO outages ( outageId, nodeId, ipAddr, ifLostService, serviceID ) "
                   + "VALUES ( nextval('outageNxtId'), 1, '1.2.3.6', now(), 1 )");
//        executeSQL("INSERT INTO outages ( outageId, nodeId, ipAddr, ifLostService, serviceID ) "
//                   + "VALUES ( nextval('outageNxtId'), 1, '1.2.3.6', now(), 2 )");
 */
 

    }

}
