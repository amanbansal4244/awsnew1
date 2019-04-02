package gloablSecondaryIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

/*
 As per Amazon documentation:

	“The data returned from a Query or Scan operation is limited to 1 MB; 
	this means that if you scan a table that has more than 1 MB of data, you’ll need to perform another
	 Scan operation to continue to the next 1 MB of data in the table. If your query for specific attributes that match 
	 values that amount to more than 1 MB of data, you’ll need to perform another Query request for the next 1 MB of data. 
	 The second query request uses a starting point (ExclusiveStartKey) based on the key of the last returned value (LastEvaluatedKey) 
	 so you can progressively query or scan for new data in 1 MB increments.
	 The LastEvaluatedKey is null when the entire Query or Scan result set is complete (i.e. the operation processed the “last page”).”
 */
public class _5PaginationGlobalSecondaryIndex_Using_PK {

	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDB = new DynamoDB(client);

	static Table table = dynamoDB.getTable("Data");
	static Index index = table.getIndex("RECORD_STATUS_IDIndex");
	public static String tableName = "Data";

	public static void main(String[] args) throws Exception {

		String value = "India";

		Condition hashKeyCondition = new Condition().withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(value));

		Map<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put("Location", hashKeyCondition);// Location is column name which is PK

		Map<String, AttributeValue> lastEvaluatedKey = null;
		do {
			QueryRequest queryRequest = new QueryRequest().withTableName(tableName).withKeyConditions(keyConditions)
					.withLimit(1).withExclusiveStartKey(lastEvaluatedKey);

			QueryResult result = client.query(queryRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {
				System.out.println("item: " + item);
			}
			lastEvaluatedKey = result.getLastEvaluatedKey();
		} while (lastEvaluatedKey != null && lastEvaluatedKey.isEmpty());

		/*
		 * HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		 * Condition condition = new Condition()
		 * .withComparisonOperator(ComparisonOperator.LT.toString())
		 * .withAttributeValueList(new AttributeValue().withN("100"));
		 * scanFilter.put("Location", condition); Boolean lastEval = true; int count =
		 * 0; ScanRequest scanRequest = new
		 * ScanRequest(tableName).withScanFilter(scanFilter); while(lastEval) {
		 * ScanResult scanResult = ((AmazonDynamoDB) dynamoDB).scan(scanRequest); count
		 * += scanResult.getCount(); System.out.println("Page Size: " +
		 * scanResult.getCount()); System.out.println("Total count = " + count); if
		 * (scanResult.getLastEvaluatedKey() != null) lastEval =
		 * scanResult.getLastEvaluatedKey().isEmpty() == false; else lastEval = false;
		 * if (lastEval) {
		 * scanRequest.setExclusiveStartKey(scanResult.getLastEvaluatedKey()); } }
		 */}
}