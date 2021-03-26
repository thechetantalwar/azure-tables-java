//----------------------------------------------------------------------------------
// Microsoft Developer & Platform Evangelism
//
// Copyright (c) Microsoft Corporation. All rights reserved.
//
// THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
// EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES
// OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
//----------------------------------------------------------------------------------
// The example companies, organizations, products, domain names,
// e-mail addresses, logos, people, places, and events depicted
// herein are fictitious.  No association with any real company,
// organization, product, domain name, email address, logo, person,
// places, or events is intended or should be inferred.
//----------------------------------------------------------------------------------
package com.thinknyx.azure.cosmosdb.tablesample;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.UUID;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;

/**
 * This sample illustrates basic usage of the Azure table storage service.
 */
public class TableBasics {

    protected static CloudTableClient tableClient = null;
    protected static CloudTable table1 = null;
    protected final static String tableNamePrefix = "thinknyxlab";

    /**
     * Azure Storage Table Sample
     *
     * @throws Exception
     */
    public void runSamples() throws Exception {

        System.out.println("Azure Storage Table sample - Starting.");

        try {
            // Create a table client for interacting with the table service
            tableClient = TableClientProvider.getTableClientReference();

            // Create a new table with a randomized name
            String tableName1 = tableNamePrefix + UUID.randomUUID().toString().replace("-", "");
            System.out.println(String.format("\nCreate a table with name \"%s\"", tableName1));
            table1 = createTable(tableClient, tableName1);
            System.out.println("\tSuccessfully created the table.");

            // Create a sample entities for use
            CustomerEntity customer1 = new CustomerEntity("Harp", "Walter");
            customer1.setEmail("walter@contoso.com");
            customer1.setHomePhoneNumber("425-555-0101");

            // Create and insert new customer entities
            System.out.println("\nInsert the new entities.");
            table1.execute(TableOperation.insert(customer1));
            System.out.println("\tSuccessfully inserted the new entities.");

            // Demonstrate how to read the entity using a point query
            System.out.println("\nRead the inserted entitities using point queries.");
            customer1 = table1.execute(TableOperation.retrieve("Harp", "Walter", CustomerEntity.class)).getResultAsType();
            if (customer1 != null) {
                System.out.println(String.format("\tCustomer: %s,%s\t%s\t%s\t%s", customer1.getPartitionKey(), customer1.getRowKey(), customer1.getEmail(), customer1.getHomePhoneNumber(), customer1.getWorkPhoneNumber()));
            }

            // Demonstrate how to update and merge the entity
            System.out.println("\nUpdate an existing entity by adding the work phone number and merging with the existing entity.");
            CustomerEntity mergeCustomer = new CustomerEntity(customer1.getPartitionKey(), customer1.getRowKey());
            mergeCustomer.setEtag(customer1.getEtag());
            mergeCustomer.setWorkPhoneNumber("425-555-0105");
            // Note the new entity does not have the home phone number or the email set, but the merged entity should retain the old one
            table1.execute(TableOperation.merge(mergeCustomer));
            System.out.println("\tSuccessfully updated the existing entity.");

            // Display the updated entity
            System.out.println("\nRead the updated entities.");
            customer1 = table1.execute(TableOperation.retrieve("Harp", "Walter", CustomerEntity.class)).getResultAsType();
            if (customer1 != null) {
                System.out.println(String.format("\tCustomer: %s,%s\t%s\t%s\t%s", customer1.getPartitionKey(), customer1.getRowKey(), customer1.getEmail(), customer1.getHomePhoneNumber(), customer1.getWorkPhoneNumber()));
            }

        }
        catch (Throwable t) {
            PrintHelper.printException(t);
        }
        finally {

        }

        System.out.println("\nAzure Storage Table sample - Completed.\n");
    }



    private static CloudTable createTable(CloudTableClient tableClient, String tableName) throws StorageException, RuntimeException, IOException, InvalidKeyException, IllegalArgumentException, URISyntaxException, IllegalStateException {

        // Create a new table
        CloudTable table = tableClient.getTableReference(tableName);
        try {
            if (table.createIfNotExists() == false) {
                throw new IllegalStateException(String.format("Table with name \"%s\" already exists.", tableName));
            }
        }
        catch (StorageException s) {
            if (s.getCause() instanceof java.net.ConnectException) {
                System.out.println("Caught connection exception from the client. If running with the default configuration please make sure you have started the storage emulator.");
            }
            throw s;
        }

        return table;
    }




}
