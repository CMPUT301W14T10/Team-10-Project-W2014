package ca.ualberta.team10projectw2014.controller;

public interface DataController<T> {
	T loadFromFile();
	void saveToFile(T list);
}