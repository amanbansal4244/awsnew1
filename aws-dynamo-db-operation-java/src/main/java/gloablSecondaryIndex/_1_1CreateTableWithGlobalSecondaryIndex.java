package gloablSecondaryIndex;

import java.util.ArrayList;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

/*
 * The following Java code snippet demonstrates the preceding steps. 
 * The snippet creates a table (WeatherData) with a global secondary index (PrecipIndex). 
 * The index partition key is Date and its sort key is Precipitation. All of the table attributes are projected 
 * into the index. Users can query this index to obtain weather data for a particular date, optionally sorting the data by precipitation amount.

Note that since Precipitation is not a key attribute for the table, it is not required; however, 
WeatherData items without Precipitation will not appear in PrecipIndex.
 */
public class _1_1CreateTableWithGlobalSecondaryIndex {

	public static void main(String[] args) throws Exception {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		DynamoDB dynamoDB = new DynamoDB(client);
		 String tableName = "Issues2";
        // Attribute definitions
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

        attributeDefinitions.add(new AttributeDefinition().withAttributeName("IssueId").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Title").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("CreateDate").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("DueDate").withAttributeType("S"));

        // Key schema for table
        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName("IssueId").withKeyType(KeyType.HASH)); // Partition key
        tableKeySchema.add(new KeySchemaElement().withAttributeName("Title").withKeyType(KeyType.RANGE)); // Sort key

        // Initial provisioned throughput settings for the indexes
        ProvisionedThroughput ptIndex = new ProvisionedThroughput().withReadCapacityUnits(1L)
            .withWriteCapacityUnits(1L);

     // TitleIndex
        GlobalSecondaryIndex titleIndex = new GlobalSecondaryIndex().withIndexName("TitleIndex")
            .withProvisionedThroughput(ptIndex)
            .withKeySchema(new KeySchemaElement().withAttributeName("Title").withKeyType(KeyType.HASH), // Partition
                                                                                                        // key
                new KeySchemaElement().withAttributeName("IssueId").withKeyType(KeyType.RANGE)) // Sort
                                                                                                // key
            .withProjection(new Projection().withProjectionType("KEYS_ONLY"));

        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                .withProvisionedThroughput(
                    new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1))
                .withAttributeDefinitions(attributeDefinitions).withKeySchema(tableKeySchema)
                .withGlobalSecondaryIndexes(titleIndex);

            System.out.println("Creating table " + tableName + "...");
            dynamoDB.createTable(createTableRequest);

	}
}