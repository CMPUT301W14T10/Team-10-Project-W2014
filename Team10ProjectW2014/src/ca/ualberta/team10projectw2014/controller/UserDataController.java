package ca.ualberta.team10projectw2014.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import android.content.Context;
import ca.ualberta.team10projectw2014.UserModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserDataController implements DataController<UserModel> {
	private Context fileContext;
    private String FILE_NAME;

	public UserDataController(Context fileContext, String fileName) {
		super();
		this.fileContext = fileContext;
		this.FILE_NAME = fileName;
	}

	public void saveToFile(UserModel list){
	    try {
	        FileOutputStream fos = fileContext.openFileOutput(FILE_NAME,
	                    Context.MODE_PRIVATE);
	        OutputStreamWriter osw = new OutputStreamWriter(fos);
	        Gson gson = new Gson();
	        Type fooType = new TypeToken<UserModel>() {}.getType();
	        gson.toJson(list, fooType, osw);
	        osw.close();
	        fos.close();
	    } catch (FileNotFoundException e)
	        {
	            e.printStackTrace();
	    } catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	}

	public UserModel loadFromFile(){
	    FileInputStream fis;
	    UserModel list = new UserModel(fileContext);
	        try
	        {
	            fis = fileContext.openFileInput(FILE_NAME);
	            InputStreamReader isr = new InputStreamReader(fis);
	            Gson gson = new Gson();
	            
	            Type fooType = new TypeToken<UserModel>() {}.getType();
	            UserModel list_temp = gson.fromJson(isr, fooType);
	            if(list_temp != null)
	            	list = list_temp;
	            isr.close();
	            fis.close();
	        } catch (FileNotFoundException e)
	            {
	                e.printStackTrace();
	        } catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	    
	    return list;
	}
}