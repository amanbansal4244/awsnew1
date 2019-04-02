package PrimaryKey;

import java.util.Arrays;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

/*
 * LINK : https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.01.html
 * 
 * year – The partition key(Primary Key). The ScalarAttributeType is N for number.
 * title – The sort key. The ScalarAttributeType is S for string.
 * 
 * Note:
 * 		You set the endpoint to indicate that you are creating the table in DynamoDB on your computer.
 * 		In the createTable call, you specify table name, primary key attributes, and its data types.
 * 		The ProvisionedThroughput parameter is required, but the downloadable version of DynamoDB ignores it.
 * 		 (Provisioned throughput is beyond the scope of this exercise.)
 */
public class _1CreateMoviesTable {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

		/*AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1")).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "Movies";*/

		/** Provide the AWS region which your DynamoDB table is hosted. */
		final Region AWS_REGION = Region.getRegion(Regions.US_EAST_1);
		final String CLIENT_REGION = "us-east-1";
		//AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard().build();
		//DYNAMO_DB_CLIENT.setRegion(AWS_REGION);
		AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);

		/** The DynamoDB table name. */
		final String DYNAMO_TABLE_NAME = "Movies";

		try {
			System.out.println("Attempting to create table; please wait...");
			Table table = DYNAMO_DB.createTable(DYNAMO_TABLE_NAME,
					Arrays.asList(new KeySchemaElement("year", KeyType.HASH), // Partition key
							new KeySchemaElement("title", KeyType.RANGE)), // Sort key
					Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.N),
							new AttributeDefinition("title", ScalarAttributeType.S)),
					new ProvisionedThroughput(10L, 10L));
			table.waitForActive();
			System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

		} catch (Exception e) {
			System.err.println("Unable to create table: ");
			System.err.println(e.getMessage());
		}

	}
}