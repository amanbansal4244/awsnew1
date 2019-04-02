// Copyright 2012-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
package PrimaryKey;

import java.util.Arrays;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

/*
 * You can use the updateItem method to modify an existing item.
 *  You can update values of existing attributes, add new attributes, or remove attributes.
 *  
 *  In this example, you perform the following updates:
 *  		Change the value of the existing attributes (rating, plot).
 *  		Add a new list attribute (actors) to the existing info map.
 *  		
 *  		The item changes from this:

			{
			   year: 2015,
			   title: "The Big New Movie",
			   info: { 
			        plot: "Nothing happens at all.",
			        rating: 0
			   }
			}
			
			
		To the following:
			
			{
			   year: 2015,
			   title: "The Big New Movie",
			   info: { 
			           plot: "Everything happens all at once.",
			           rating: 5.5,
			           actors: ["Larry", "Moe", "Curly"]
			   }
			}
 */
public class _5UpdateMoviesItem {

    public static void main(String[] args) throws Exception {

    	AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
    		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);

    		Table DYNAMO_TABLE_NAME = DYNAMO_DB.getTable("Movies");
    		

        int year = 2015;
        String title = "The Big New Movie";

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("year", year, "title", title)
            .withUpdateExpression("set info.rating = :r, info.plot=:p, info.actors=:a")
            .withValueMap(new ValueMap().withNumber(":r", 5.5).withString(":p", "Everything happens all at once.")
                .withList(":a", Arrays.asList("Larry", "Moe", "Curly")))
            .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = DYNAMO_TABLE_NAME.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("Unable to update item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}