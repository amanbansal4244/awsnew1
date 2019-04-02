package PrimaryKey;

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
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class _2LoadMoviesData {

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB DYNAMO_DB_CLIENT = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
		DynamoDB DYNAMO_DB = new DynamoDB(DYNAMO_DB_CLIENT);

		Table DYNAMO_TABLE_NAME = DYNAMO_DB.getTable("Movies");

		File file = new File(new File("").getAbsolutePath(), "src/main/resources/" + "moviedata.json");
		JsonParser parser = new JsonFactory().createParser(file);

		JsonNode rootNode = new ObjectMapper().readTree(parser);
		Iterator<JsonNode> iter = rootNode.iterator();

		ObjectNode currentNode;

		while (iter.hasNext()) {
			currentNode = (ObjectNode) iter.next();
			int year = currentNode.path("year").asInt();
			String title = currentNode.path("title").asText();

			try {
				DYNAMO_TABLE_NAME.putItem(new Item().withPrimaryKey("year", year, "title", title).withJSON("info",
						currentNode.path("info").toString()));
				System.out.println("PutItem succeeded: " + year + " " + title);

			} catch (Exception e) {
				System.err.println("Unable to add movie: " + year + " " + title);
				System.err.println(e.getMessage());
				break;
			}
		}
		parser.close();
	}
}