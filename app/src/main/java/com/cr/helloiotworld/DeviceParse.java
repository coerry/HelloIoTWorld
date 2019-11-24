package main.java.com.cr.helloiotworld;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeviceParse {

    protected XmlPullParserFactory xmlPullParserFactory;
    protected XmlPullParser parser;

    private final String ipXmlPath = "http://192.168.2.160:3161";

    public String idXmlPath = "";

    private final String xmlPath = ipXmlPath + "/devices/";

    public String fullXmlPath = "";

    public DeviceParse() {
        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            parser = xmlPullParserFactory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        BackgroundAsyncTask backgroundAsyncTask = new BackgroundAsyncTask();
        backgroundAsyncTask.execute(xmlPath);

    }

    public String getFullXmlPath() {
        return ipXmlPath + "/devices/" + idXmlPath;
    }

    protected class BackgroundAsyncTask extends AsyncTask<String, Void, String>{
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
            idXmlPath = s;
            fullXmlPath = ipXmlPath + "/devices/" + idXmlPath + "/inventory/";
        }
        private String getLoadedXmlValues(XmlPullParser parser) throws XmlPullParserException, IOException {
            int eventType = parser.getEventType();
            String name = null;
            Entity mEntity = new Entity();
            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    name = parser.getName();
                    if(name.equals("id")){
                        mEntity.id = parser.nextText();
                    }
                }
                eventType = parser.next();
            }
            return mEntity.id;
        }
        public class Entity{
            public String id;
        }
    }
}
