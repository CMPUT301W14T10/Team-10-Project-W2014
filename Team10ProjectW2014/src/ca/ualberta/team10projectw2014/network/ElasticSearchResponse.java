package ca.ualberta.team10projectw2014.network;

/**
 * This class represents a response from ElasticSearch.
 * Taken from https://github.com/rayzhangcl/ESDemo
 * 
 * @version      1                (current version number of program)
 */
public class ElasticSearchResponse<T> {
    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;
    public T getSource() {
        return _source;
    }
}
