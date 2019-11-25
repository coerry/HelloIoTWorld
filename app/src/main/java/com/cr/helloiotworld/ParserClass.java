package main.java.com.cr.helloiotworld;

import android.os.AsyncTask;
import android.os.Handler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class ParserClass {

    protected XmlPullParserFactory xmlPullParserFactory;
    protected XmlPullParser parser;
    private String xmlPath = "";
    private DeviceParse deviceParse;
    private Integer[] tagId = {0,0,0,0,0};

    public ParserClass() {

        deviceParse = new DeviceParse();
        xmlPath = deviceParse.getFullXmlPath();

        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            parser = xmlPullParserFactory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        Timer myTimer = new Timer(); // Создаем таймер
        final Handler uiHandler = new Handler();

        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                final String result = xmlPath;
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String s = result + "/inventory/";
                        if (i != 0) {
                            BackgroundAsyncTask backgroundAsyncTask = new BackgroundAsyncTask();
                            backgroundAsyncTask.execute(s);

                        }
                    }
                });
            };
        }, 0L, 60L * 20);

    }

    public Integer[] getTags(){
        return tagId;
    }

    private class BackgroundAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String ...params) {
            URL url = null;

            String returnedResult = "";
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                parser.setInput(is, null);
                returnedResult = getLoadedXmlValues(parser);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return returnedResult;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
        private String getLoadedXmlValues(XmlPullParser parser) throws XmlPullParserException, IOException {

            for(int i = 0; i < tagId.length; i++){
                tagId[i] = 0;
            }

            int eventType = parser.getEventType();
            String name = null;
            String tagName = null;
            String totalString = "";
            int num_id = 0;
            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    name = parser.getName();
                    if(name.equals("epc")){
                        tagName = parser.nextText();
                        if (tagName.equals("000000000000000000000001")){
                            tagId[0] = 1;
                        }
                        if (tagName.equals("000000000000000000000002")){
                            tagId[1] = 1;
                        }
                        if (tagName.equals("000000000000000000000003")){
                            tagId[2] = 1;
                        }
                        if (tagName.equals("000000000000000000000004")){
                            tagId[3] = 1;
                        }
                        if (tagName.equals("000000000000000000000005")){
                            tagId[4] = 1;
                        }
                    }
                }
                eventType = parser.next();
            }
            return totalString;
        }
    }
}
