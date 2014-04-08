package ca.ualberta.team10projectw2014.network;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents a search response from ElasticSearch. 
 * Taken from https://github.com/rayzhangcl/ESDemo
 * 
 * @version      1                (current version number of program)
 */
public class ElasticSearchSearchResponse<T> {
    int took;
    boolean timed_out;
    transient Object _shards;
    /**
	 * @uml.property  name="hits"
	 * @uml.associationEnd  
	 */
    Hits<T> hits;
    boolean exists;    
    /**
	 * @return
	 * @uml.property  name="hits"
	 */
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits.getHits();        
    }
    
    /** 
     * Gets the pulled hits from the server
     * 
	 * Edited by @author sgiang and dvyee
     * 
     * @return Collection<T>
     */
    public Collection<T> getSources() {
        Collection<T> out = new ArrayList<T>();
        for (ElasticSearchResponse<T> essrt : getHits()) {
            out.add( essrt.getSource() );
        }
        return out;
    }
    public String toString() {
        return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits);     
    }
}
