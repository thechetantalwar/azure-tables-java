package com.thinknyx.azure.cosmosdb.tablesample;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTableClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Properties;

/**
 * Manages the storage table client
 */
class TableClientProvider {

    /**
     * Validates the connection string and returns the storage table client.
     * The connection string must be in the Azure connection string format.
     *
     * @return The newly created CloudTableClient object
     */
    private static Properties prop;

    static {
        // Retrieve the connection string
        prop = new Properties();
        try {
            InputStream propertyStream = TableBasics.class.getClassLoader().getResourceAsStream("config.properties");
            if (propertyStream != null) {
                prop.load(propertyStream);
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException | IOException e) {
            System.out.println("\nFailed to load config.properties file.");
            throw new RuntimeException();
        }
    }

    static CloudTableClient getTableClientReference() throws RuntimeException, IOException, URISyntaxException, InvalidKeyException {
        CloudStorageAccount storageAccount;
        try {
            storageAccount = CloudStorageAccount.parse(prop.getProperty("StorageConnectionString"));
        } catch (IllegalArgumentException | URISyntaxException e) {
            System.out.println("\nConnection string specifies an invalid URI.");
            System.out.println("Please confirm the connection string is in the Azure connection string format.");
            throw e;
        } catch (InvalidKeyException e) {
            System.out.println("\nConnection string specifies an invalid key.");
            System.out.println("Please confirm the AccountName and AccountKey in the connection string are valid.");
            throw e;
        }

        return storageAccount.createCloudTableClient();
    }

    public static boolean isAzureCosmosdbTable() {
        if (prop != null) {
            String connectionString = prop.getProperty("StorageConnectionString");
            return connectionString != null && connectionString.contains("table.cosmosdb");
        }
        return false;
    }
}
