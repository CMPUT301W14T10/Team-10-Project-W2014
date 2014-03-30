package ca.ualberta.team10projectw2014.models;
import static org.elasticsearch.node.NodeBuilder.*;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

/**
 * This class handles the user's Internet connection and fetches things
 * such as user comments from the database.
 * @author Cole Fudge <cfudge@ualberta.ca>
 * @version     1                (current version number of program)
 */
public class NetworkConnectionModel {
	@SuppressWarnings("unused")
	private String saveUrl;
	// on startup

	Node node = nodeBuilder().client(true).node();
	Client client = node.client();

	// on shutdown
}