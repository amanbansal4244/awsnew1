package PrimaryKey;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

/*
 * You can use the getItem method to read the item from the Movies table. 
 * You must specify the primary key values, so you can read any item from Movies if you know its year and title.
 */
public class _4ReadMoviesItem_Using_getItem {

    public static void main(String[] args) throws Exception {

    	AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);

		Table DYNAMO_TABLE_NAME = DYNAMO_DB.getTable("Movies");
		
        int year = 2015;
        String title = "The Big New Movie";

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("year", year, "title", title);

        try {
            System.out.println("Attempting to read the item...");
            Item outcome = DYNAMO_TABLE_NAME.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);

        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + year + " " + title);
            System.err.println(e.getMessage());
        }

    }
}