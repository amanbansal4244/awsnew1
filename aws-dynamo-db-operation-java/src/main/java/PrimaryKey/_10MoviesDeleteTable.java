package PrimaryKey;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

public class _10MoviesDeleteTable {

	public static void main(String[] args) throws Exception {
		AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);

		Table DYNAMO_TABLE_NAME = DYNAMO_DB.getTable("Movies");

		try {
			System.out.println("Attempting to delete table; please wait...");
			DYNAMO_TABLE_NAME.delete();
			DYNAMO_TABLE_NAME.waitForDelete();
			System.out.print("Success.");

		} catch (Exception e) {
			System.err.println("Unable to delete table: ");
			System.err.println(e.getMessage());
		}
	}
}