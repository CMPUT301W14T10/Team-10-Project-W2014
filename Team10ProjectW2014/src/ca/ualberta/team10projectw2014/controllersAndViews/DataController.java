package ca.ualberta.team10projectw2014.controllersAndViews;

public interface DataController<T> {
	T loadFromFile();
	void saveToFile(T list);
}