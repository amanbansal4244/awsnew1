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
 and the attributes that you want to return. In this example, the index is PrecipIndex, which has a partition key of Date 
 and a sort key of Precipitation. 
 
 The index query returns all of the weather data for a particular date, where the precipitation is greater than zero.
 
 Note : The attribute name Date is a DynamoDB reserved word.
  	    Therefore, we must use an expression attribute name as a placeholder in the KeyConditionExpression.
 */
public class _3LoadDataGlobalSecondaryIndex {

	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDB = new DynamoDB(client);

	static Table table = dynamoDB.getTable("WeatherData");
	static Index index = table.getIndex("PrecipIndex");
	 public static String tableName = "WeatherData";
	 
	public static void main(String[] args) throws Exception {
		

		loadData();
	}
	
	public static void loadData() {

        System.out.println("Loading data into table " + tableName + "...");

        // IssueId, Title,
        // Description,
        // CreateDate, LastUpdateDate, DueDate,
        // Priority, Status

        putItem("A-101", "Compilation error", "Can't compile Project X - bad version number. What does this mean?",
            "2013-11-01", "2013-11-02", "2013-11-10", 1, "Assigned");

        putItem("A-102", "Can't read data file", "The main data file is missing, or the permissions are incorrect",
            "2013-11-01", "2013-11-04", "2013-11-30", 2, "In progress");

        putItem("A-103", "Test failure", "Functional test of Project X produces errors", "2013-11-01", "2013-11-02",
            "2013-11-10", 1, "In progress");

        putItem("A-104", "Compilation error", "Variable 'messageCount' was not initialized.", "2013-11-15",
            "2013-11-16", "2013-11-30", 3, "Assigned");

        putItem("A-105", "Network issue", "Can't ping IP address 127.0.0.1. Please fix this.", "2013-11-15",
            "2013-11-16", "2013-11-19", 5, "Assigned");

    }

    public static void putItem(

        String issueId, String title, String description, String createDate, String lastUpdateDate, String dueDate,
        Integer priority, String status) {

        Table table = dynamoDB.getTable(tableName);

        Item item = new Item().withPrimaryKey("IssueId", issueId).withString("Title", title)
            .withString("Description", description).withString("CreateDate", createDate)
            .withString("LastUpdateDate", lastUpdateDate).withString("DueDate", dueDate)
            .withNumber("Priority", priority).withString("Status", status);

        table.putItem(item);
    }
}