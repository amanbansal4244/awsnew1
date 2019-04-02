package PrimaryKey;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

/*
 * The following program shows how to use UpdateItem with a condition.
 *  If the condition evaluates to true, the update succeeds; otherwise, the update is not performed.
 *  
 *  	In this case, the movie item is updated only if there are more than three actors.
 */
public class _7UpdateMoviesItem_Conditionally {

    public static void main(String[] args) throws Exception {

    	AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);

		Table DYNAMO_TABLE_NAME = DYNAMO_DB.getTable("Movies");

        int year = 2015;
        String title = "The Big New Movie";

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(new PrimaryKey("year", year, "title", title))
            .withUpdateExpression("remove info.actors[0]")
            .withConditionExpression("size(info.actors) > :num")
            .withValueMap(new ValueMap().withNumber(":num", 2))
            .withReturnValues(ReturnValue.UPDATED_NEW);

        // Conditional update (we expect this to fail)
        try {
            System.out.println("Attempting a conditional update...");
            UpdateItemOutcome outcome = DYNAMO_TABLE_NAME.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("Unable to update item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}