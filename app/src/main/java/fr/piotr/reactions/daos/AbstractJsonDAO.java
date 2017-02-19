package fr.piotr.reactions.daos;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by piotr_000 on 21/01/2017.
 *
 */

abstract class AbstractJsonDAO {

    void saveFile(Context context, String json, String filename){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String loadJsonFromFile(Context context, String filename){
        String json="";
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(filename);
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            json = new String(bytes, StandardCharsets.UTF_8);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}
