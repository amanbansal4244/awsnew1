package PrimaryKey;

import java.util.HashMap;
import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

public class RetriveItemsFromDynamoDB_Query {
	/** Provide the AWS region which your DynamoDB table is hosted. */
	private static final Region AWS_REGION = Region.getRegion(Regions.US_EAST_1);
	private static final String CLIENT_REGION = "us-east-1";
	private static AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard().build();
	private static DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);
	/** The DynamoDB table name. */
	private static final String DYNAMO_TABLE_NAME = "def_specification";
	
	public static void main(String[] args) throws Exception {

		/*AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();
*/
	

		//DynamoDB dynamoDB = new DynamoDB(client);

		Table table = DYNAMO_DB.getTable(DYNAMO_TABLE_NAME);

		HashMap<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("#yr", "year");

		HashMap<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put(":yyyy", 1985);

		QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#yr = :yyyy").withNameMap(nameMap)
				.withValueMap(valueMap);

		ItemCollection<QueryOutcome> items = null;
		Iterator<Item> iterator = null;
		Item item = null;

		try {
			System.out.println("Movies from 1985");
			items = table.query(querySpec);

			iterator = items.iterator();
			while (iterator.hasNext()) {
				item = iterator.next();
				System.out.println(item.getNumber("year") + ": " + item.getString("title"));
			}

		} catch (Exception e) {
			System.err.println("Unable to query movies from 1985");
			System.err.println(e.getMessage());
		}

		valueMap.put(":yyyy", 1992);
		valueMap.put(":letter1", "A");
		valueMap.put(":letter2", "L");

		querySpec.withProjectionExpression("#yr, title, info.genres, info.actors[0]")
				.withKeyConditionExpression("#yr = :yyyy and title between :letter1 and :letter2").withNameMap(nameMap)
				.withValueMap(valueMap);

		try {
			System.out.println("Movies from 1992 - titles A-L, with genres and lead actor");
			items = table.query(querySpec);

			iterator = items.iterator();
			while (iterator.hasNext()) {
				item = iterator.next();
				System.out.println(item.getNumber("year") + ": " + item.getString("title") + " " + item.getMap("info"));
			}

		} catch (Exception e) {
			System.err.println("Unable to query movies from 1992:");
			System.err.println(e.getMessage());
		}
	}
}