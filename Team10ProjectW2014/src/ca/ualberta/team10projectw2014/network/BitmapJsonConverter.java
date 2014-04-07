package ca.ualberta.team10projectw2014.network;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class provides custom base64 serialization / deserialization for Bitmaps.
 * @author zjullion
 * @version      1                (current version number of program)
 * Algorithm taken from: http://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string
 */
public class BitmapJsonConverter implements JsonDeserializer<Bitmap>,
		JsonSerializer<Bitmap> {

	
	@Override
	public JsonElement serialize(Bitmap src, Type typeOfSrc, JsonSerializationContext context) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		src = Bitmap.createScaledBitmap(src, 100, 100, true);
		src.compress(Bitmap.CompressFormat.JPEG, 30, stream);
		String base64Encoded = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
		return new JsonPrimitive(base64Encoded);
	}

	
	@Override
	public Bitmap deserialize(JsonElement src, Type typeOfSrc, JsonDeserializationContext context) 
			throws JsonParseException {
		String base64Encoded = src.getAsJsonPrimitive().getAsString();
		byte[] data = Base64.decode(base64Encoded, Base64.NO_WRAP);
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
}