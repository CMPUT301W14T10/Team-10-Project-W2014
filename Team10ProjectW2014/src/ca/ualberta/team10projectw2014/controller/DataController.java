package ca.ualberta.team10projectw2014.controller;

import java.util.ArrayList;
import java.util.List;

public interface DataController<T> {
	ArrayList<T> loadFromFile();
	void saveToFile(ArrayList<T> list);
}