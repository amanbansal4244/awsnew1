package gloablSecondaryIndex;

import java.io.File;
import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndexDescription;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*To get information about global secondary indexes on a table, use DescribeTable.
For each index, you can access its name, key schema, and projected attributes.*/

public class _2DescribeTableWithGlobalSecondaryIndex {

	public static void main(String[] args) throws Exception {AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	DynamoDB dynamoDB = new DynamoDB(client);

	Table table = dynamoDB.getTable("Data");
	TableDescription tableDesc = table.describe();
	    

	Iterator<GlobalSecondaryIndexDescription> gsiIter = tableDesc.getGlobalSecondaryIndexes().iterator();
	while (gsiIter.hasNext()) {
	    GlobalSecondaryIndexDescription gsiDesc = gsiIter.next();
	    System.out.println("Info for index "
	         + gsiDesc.getIndexName() + ":");

	    Iterator<KeySchemaElement> kseIter = gsiDesc.getKeySchema().iterator();
	    while (kseIter.hasNext()) {
	        KeySchemaElement kse = kseIter.next();
	        System.out.printf("\t%s: %s\n", kse.getAttributeName(), kse.getKeyType());
	    }
	    Projection projection = gsiDesc.getProjection();
	    System.out.println("\tThe projection type is: "
	        + projection.getProjectionType());
	    if (projection.getProjectionType().toString().equals("INCLUDE")) {
	        System.out.println("\t\tThe non-key projected attributes are: "
	            + projection.getNonKeyAttributes());
	    }
	}}
}