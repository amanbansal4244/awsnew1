package PrimaryKey;

import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

/*
 * The scan method reads every item in the entire table, and returns all the data in the table.
 *  You can provide an optional filter_expression so that only the items matching your criteria are returned. 
 *  However, the filter is applied only after the entire table has been scanned.

	The following program scans the entire Movies table, which contains approximately 5,000 items.
 	The scan specifies the optional filter to retrieve only the movies from the 1950s (approximately 100 items),
  	and discard all the others.
 */
public class _9MoviesScan {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);

		Table DYNAMO_TABLE_NAME = DYNAMO_DB.getTable("Movies");

		ScanSpec scanSpec = new ScanSpec().withProjectionExpression("#yr, title, info.rating")
				.withFilterExpression("#yr between :start_yr and :end_yr")
				.withNameMap(new NameMap().with("#yr", "year"))
				.withValueMap(new ValueMap().withNumber(":start_yr", 1950).withNumber(":end_yr", 1959));

		try {
			ItemCollection<ScanOutcome> items = DYNAMO_TABLE_NAME.scan(scanSpec);

			Iterator<Item> iter = items.iterator();
			while (iter.hasNext()) {
				Item item = iter.next();
				System.out.println(item.toString());
			}

		} catch (Exception e) {
			System.err.println("Unable to scan the table:");
			System.err.println(e.getMessage());
		}
	}
}