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
 * The snippet creates a table (Data) with a global secondary index (RECORD_STATUS_IDIndex). 
 * The index partition key is RECORD_STATUS_ID and its sort key is Precipitation. All of the table attributes are projected 
 * into the index. Users can query this index to obtain weather data for a particular date,
 *  optionally sorting the data by precipitation amount.

Note that since Precipitation is not a key attribute for the table, it is not required; however, 
Data items without Precipitation will not appear in RECORD_STATUS_IDIndex.
 */
public class _1CreateTableWithGlobalSecondaryIndex {

	public static void main(String[] args) throws Exception {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		DynamoDB dynamoDB = new DynamoDB(client);

		// Attribute definitions
		ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

		attributeDefinitions.add(new AttributeDefinition().withAttributeName("Location").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("RECORD_STATUS_ID").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("Precipitation").withAttributeType("N"));

		// Table key schema
		ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
		tableKeySchema.add(new KeySchemaElement().withAttributeName("Location").withKeyType(KeyType.HASH)); // Partition key
																											
		tableKeySchema.add(new KeySchemaElement().withAttributeName("RECORD_STATUS_ID").withKeyType(KeyType.RANGE)); // Sort key

		// PrecipIndex
		GlobalSecondaryIndex precipIndex = new GlobalSecondaryIndex().withIndexName("RECORD_STATUS_IDIndex")
				.withProvisionedThroughput(
						new ProvisionedThroughput().withReadCapacityUnits((long) 10).withWriteCapacityUnits((long) 1))
				.withProjection(new Projection().withProjectionType(ProjectionType.ALL));

		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();

		indexKeySchema.add(new KeySchemaElement().withAttributeName("RECORD_STATUS_ID").withKeyType(KeyType.HASH)); // Partition key
		indexKeySchema.add(new KeySchemaElement().withAttributeName("Precipitation").withKeyType(KeyType.RANGE)); // Sort key

		precipIndex.setKeySchema(indexKeySchema);

		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName("Data")
				.withProvisionedThroughput(
						new ProvisionedThroughput().withReadCapacityUnits((long) 5).withWriteCapacityUnits((long) 1))
				.withAttributeDefinitions(attributeDefinitions).withKeySchema(tableKeySchema)
				.withGlobalSecondaryIndexes(precipIndex);

		Table table = dynamoDB.createTable(createTableRequest);
		System.out.println(table.getDescription());
	}
}