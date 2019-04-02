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

/*
 * You can use the query method to retrieve data from a table. You must specify a partition key value. The sort key is optional.
 * The primary key for the Movies table is composed of the following:
 * 
 * 		year – The partition key. The attribute type is number.
 * 		title – The sort key. The attribute type is string.
 * 
 * To find all movies released during a year, you need to specify only the year. 
 * You can also provide the title to retrieve a subset of movies based on some condition (on the sort key); 
 * 
 * for example, to find movies released in 2014 that have a title starting with the letter "A".
 * In addition to query, there is also a scan method that can retrieve all of the table data.
 * 
 * The code included in this step performs the following queries:
 * 		Retrieve all movies released in the year 1985.
 * 		Retrieve all movies released in the year 1992, with a title beginning with the letter "A" through the letter "L".
 */
public class _8MoviesQuery {

    public static void main(String[] args) throws Exception {

     	/** Provide the AWS region which your DynamoDB table is hosted. */
    		AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard()
    				.withRegion(Regions.US_EAST_1).build();
    		
    		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);
    		
    		Table DYNAMO_TABLE_NAME = DYNAMO_DB.getTable("Movies");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#yr", "year");// we map actual column name 'year' field to temporary  '#yr'

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":yyyy", 1985);// its like a where condition, we store actual column name 'year' value '1985' to field to temporary ':yyyy'

        QuerySpec querySpec = new QuerySpec().
        		withKeyConditionExpression("#yr = :yyyy"). // this means : where year = 1985
        		withNameMap(nameMap)
            .withValueMap(valueMap);//A ValueMap to define the actual values for the KeyConditionExpression place holders.

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            System.out.println("Movies from 1985");
            items = DYNAMO_TABLE_NAME.query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.getNumber("year") + ": " + item.getString("title"));
            }

        }
        catch (Exception e) {
            System.err.println("Unable to query movies from 1985");
            System.err.println(e.getMessage());
        }

        valueMap.put(":yyyy", 1992);
        valueMap.put(":letter1", "A");
        valueMap.put(":letter2", "L");

        querySpec.withProjectionExpression("#yr, title, info.genres, info.actors[0]")
            .withKeyConditionExpression("#yr = :yyyy and title between :letter1 and :letter2")
            .withNameMap(nameMap)
            .withValueMap(valueMap);

        try {
            System.out.println("Movies from 1992 - titles A-L, with genres and lead actor");
            items = DYNAMO_TABLE_NAME.query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.getNumber("year") + ": " + item.getString("title") + " " + item.getMap("info"));
            }

        }
        catch (Exception e) {
            System.err.println("Unable to query movies from 1992:");
            System.err.println(e.getMessage());
        }
    }
}
