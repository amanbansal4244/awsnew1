package PrimaryKey;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

public class _7_1UpdateDynamoDbTable {

	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDB = new DynamoDB(client);

	static String tableName = "aman";
	static Table table = dynamoDB.getTable(tableName);

	public static void main(String[] args) throws Exception {
		updateColumnValueBasedOnPK("111", "yyy");
		// updateConditionallyWithExpression("111","newValue1","y");
		// updateConditionallyWithAttributeEntries("111", "newValue2", "y");
		// addUpdateCounter("111");
		// updateAndIncreaseCounter("111", "yyy");
	}

	/**
	 * 
	 * @param Id
	 *            PK of the table
	 * @param updatedNewValue
	 *            value to be updated
	 */
	public static void updateColumnValueBasedOnPK(String Id, String updatedNewValue) {

		/***
		 * Key of this map is the column name of the DB Id -> PK of the table
		 * specification_name -> Column name of DB
		 ***/
		Map<String, AttributeValue> attributeValues = new HashMap<>();
		attributeValues.put("Id", new AttributeValue().withS(Id));
		attributeValues.put("specification_name", new AttributeValue().withS(updatedNewValue));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest().withTableName(tableName)
				.addKeyEntry("Id", new AttributeValue().withS(Id)).addAttributeUpdatesEntry("specification_name",
						new AttributeValueUpdate().withValue(new AttributeValue().withS(updatedNewValue)));

		UpdateItemResult updateItemResult = client.updateItem(updateItemRequest);
		System.out.println("Printing item after update to new attribute...");
		System.out.println(updateItemResult);
	}

	/**
	 * 
	 * @param Id_PK
	 *            PK of the table
	 * @param updatedNewValue
	 *            value to be updated
	 * @param prefix
	 *            column value which updated when column value matched with given
	 *            prefix
	 */
	public static void updateConditionallyWithExpression(String Id_PK, String updatedNewValue, String prefix) {

		/***
		 * Key of this map is the column name of the DB Id -> PK of the table
		 ***/
		Map<String, AttributeValue> key = new HashMap<>();
		key.put("Id", new AttributeValue().withS(Id_PK));

		/***
		 * prefix -> not the column name of DB, This is the value which will be used to
		 * match with condition. specification_name -> This is the column name of the
		 * DB.
		 ***/
		Map<String, AttributeValue> attributeValues = new HashMap<>();
		attributeValues.put(":prefix", new AttributeValue().withS(prefix));
		attributeValues.put(":specificationNewValue", new AttributeValue().withS(updatedNewValue));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest().withTableName(tableName).withKey(key)
				/**
				 * specification_name : column name of DB and specificationNewValue : value to
				 * be updated
				 **/
				.withUpdateExpression("set specification_name = :specificationNewValue")
				/**
				 * update the 'specification_name' column value when 'specification_name' column
				 * value begin with 'prefix'
				 **/
				.withConditionExpression("begins_with(specification_name,:prefix)")
				.withExpressionAttributeValues(attributeValues);

		UpdateItemResult updateItemResult = client.updateItem(updateItemRequest);
		System.out.println("Printing item after conditional update to new attribute...");
		System.out.println(updateItemResult);
	}

	/**
	 * Concept: We can proceed on to more advanced statements using conditional
			 * updates. Conditional updates can help us in many cases, such as handling
			 * concurrent updates.
	 */

	/**
	 * Or by specifying attributes what we did in above method
	 * 'updateConditionallyWithExpression'
	 * 
	 * @param Id_PK
	 *            PK of the table
	 * @param updatedNewValue
	 *            value to be updated
	 * @param prefix
	 *            column value which updated when column value matched with given
	 *            prefix
	 */
	public static void updateConditionallyWithAttributeEntries(String Id_PK, String updatedNewValue, String prefix) {
		Map<String, AttributeValue> key = new HashMap<>();
		key.put("Id", new AttributeValue().withS(Id_PK));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest().withTableName(tableName).withKey(key)
				.addAttributeUpdatesEntry("specification_name",
						new AttributeValueUpdate().withValue(new AttributeValue().withS(updatedNewValue))
								.withAction(AttributeAction.PUT))
				.addExpectedEntry("specification_name",
						new ExpectedAttributeValue().withValue(new AttributeValue().withS(prefix))
								.withComparisonOperator(ComparisonOperator.BEGINS_WITH));

		UpdateItemResult updateItemResult = client.updateItem(updateItemRequest);
		System.out.println("Printing item after conditional update to new attribute...");
		System.out.println(updateItemResult);

	}

	/**
	 * Concept : Another feature is atomic counters. We can issue updates to a
	 * DynamoDB item and increase the attribute values. We will add an extra field
	 * called count. Also, we will add another update function, which once called
	 * will update the field specified but will also increase the counter attribute.
	 * Thus, the counter attribute will represent how many times an update was
	 * performed on a specific item.
	 */

	public static void addUpdateCounter(String Id_PK) {
		Map<String, AttributeValue> key = new HashMap<>();
		key.put("Id", new AttributeValue().withS(Id_PK));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest().withTableName(tableName).withKey(key)
				/** New column will be added as 'counter' with value '0' **/
				.addAttributeUpdatesEntry("counter", new AttributeValueUpdate()
						.withValue(new AttributeValue().withN("0")).withAction(AttributeAction.PUT));

		UpdateItemResult updateItemResult = client.updateItem(updateItemRequest);
		System.out.println("Printing item after update counter to new attribute...");
		System.out.println(updateItemResult);
	}

	public static void updateAndIncreaseCounter(String Id_PK, String updatedNewValue) {
		Map<String, AttributeValue> key = new HashMap<>();
		key.put("Id", new AttributeValue().withS(Id_PK));

		UpdateItemRequest updateItemRequest = new UpdateItemRequest().withTableName(tableName).withKey(key)
				.addAttributeUpdatesEntry("specification_name",
						new AttributeValueUpdate().withValue(new AttributeValue().withS(updatedNewValue))
								.withAction(AttributeAction.PUT))
				.addAttributeUpdatesEntry("counter", new AttributeValueUpdate()
						.withValue(new AttributeValue().withN("1")).withAction(AttributeAction.ADD));

		UpdateItemResult updateItemResult = client.updateItem(updateItemRequest);
		System.out.println("Printing item after update counter to new attribute...");
		System.out.println(updateItemResult);
	}
}
