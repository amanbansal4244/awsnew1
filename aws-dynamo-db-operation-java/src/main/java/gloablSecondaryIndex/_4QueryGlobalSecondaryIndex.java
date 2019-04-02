package gloablSecondaryIndex;

import java.util.HashMap;
import java.util.Iterator;
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

/*
 You can use Query on a global secondary index, in much the same way you Query a table. 
 You need to specify the index name, the query criteria for the index partition key and sort key (if present), 
 and the attributes that you want to return. In this example, the index is RECORD_STATUS_IDIndex, which has a partition key of RECORD_STATUS_ID 
 and a sort key of Precipitation. 
 
 The index query returns all of the  data for a particular RECORD_STATUS_ID, where the precipitation is zero.
 
 Note : The attribute name Date is a DynamoDB reserved word.
  	    Therefore, we must use an expression attribute name as a placeholder in the KeyConditionExpression.
 */
public class _4QueryGlobalSecondaryIndex {

	public static void main(String[] args) throws Exception {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("Data");
		Index index = table.getIndex("RECORD_STATUS_IDIndex");

		/**** Query with only Secondary index****/
		QuerySpec spec = new QuerySpec().withKeyConditionExpression("#rId = :v_status_id")
				.withNameMap(new NameMap().with("#rId", "RECORD_STATUS_ID"))
				.withValueMap(new ValueMap().withString(":v_status_id", "1"));
		
		
		/**** Query with Secondary index and other field ****/
		/*QuerySpec spec = new QuerySpec().withKeyConditionExpression("#rId = :v_status_id and Precipitation = :v_precip")
				.withNameMap(new NameMap().with("#rId", "RECORD_STATUS_ID"))
				.withValueMap(new ValueMap().withString(":v_status_id", "1").withNumber(":v_precip", 0));*/

		
		ItemCollection<QueryOutcome> items = index.query(spec);
		Iterator<Item> iter = items.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().toJSONPretty());
		}
	}
}