package galip.autokeuring;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;
import android.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    public static final String PRFERENCE_FILE = "AUTOKEURING";
    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    //Navigation
    public String Category = null;
    public int Step = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        SharedPreferences preferences = getSharedPreferences(PRFERENCE_FILE, MODE_PRIVATE);
        boolean disclaimer_agreed = preferences.getBoolean("disclaimer_agreed", false);

        if (!disclaimer_agreed)
        {
            setContentView(R.layout.disclaimer);
            return;
        }

        setContentView(R.layout.mainactivity);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerTitles = getResources().getStringArray(R.array.navigation_titles);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.fragment_navigation_item, mDrawerTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout.openDrawer(mDrawerList);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDisagreeClick(View v)
    {
        finish();
    }

    public void onAgreeClick(View v)
    {
        SharedPreferences preferences = getSharedPreferences(PRFERENCE_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("disclaimer_agreed", true).commit();
        setContentView(R.layout.mainactivity);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new ScreenFragment();
        Bundle args = new Bundle();
        args.putInt(ScreenFragment.MENU_POSITION, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void onCategoryClick(View v)
    {
        Step = 1;

        if (v == findViewById(R.id.lblCategoryBanden))
        {
            Category = "Banden";
            selectCategory("Banden");

        }else if(v == findViewById(R.id.lblCategorySchade))
        {
            Category = "Schade";
            selectCategory("Schade");

        }else if(v == findViewById(R.id.lblCategoryVerlichting))
        {
            Category = "Verlichting";
            selectCategory("Verlichting");
        }else if(v == findViewById(R.id.lblCategoryDeuren))
        {
            Category = "Deuren";
            selectCategory("Deuren");
        }else if(v == findViewById(R.id.lblCategorymotorkap))
        {
            Category = "Motorkap";
            selectCategory("Motorkap");
        }else if(v == findViewById(R.id.lblCategoryUitlaat))
        {
            Category = "Uitlaat";
            selectCategory("Uitlaat");
        }else if(v == findViewById(R.id.lblCategoryInAuto))
        {
            Category = "InAuto";
            selectCategory("InAuto");
        }else if(v == findViewById(R.id.lblCategoryHandrem))
        {
            Category = "Handrem";
            selectCategory("Handrem");
        }
    }

    private String selectCategory(String category)
    {
        String content = "";


        //Get data to show
        try
        {
            content = GetContentFromXML(category, Step);
        }catch(Exception e)
        {
            String asd = e.getMessage();
        }

        if (content.equals(""))
        {
            return content;
        }

        //
        PreCheckFragment fragment = new PreCheckFragment(category, content, String.valueOf(Step));
        Bundle args = new Bundle();
        args.putString(ScreenFragment.CATEGORY_SELECTION, category);
        fragment.setArguments(args);

        //fragment.changeContentFragmentText("aaa", "bbb", "ccc");

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();


        return content;


    }

    public String GetContentFromXML(String contentName, int page) throws XmlPullParserException, IOException
    {
        Resources res = this.getResources();
        InputStream in = res.openRawResource(R.raw.apkcontent);;
        String content = "";


        //1. Find
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            parser.setInput(in, null);
            parser.nextTag();

            content = readContent(parser, contentName, page);
        } finally {
             in.close();
        }

        return content;
    }

    public static final String ns = null;

    private String readContent(XmlPullParser parser, String contentName, int page) throws XmlPullParserException, IOException {
        String content = "";
        String aaaa = "";
        String adwada = parser.getName();
        parser.require(XmlPullParser.START_TAG, ns, "Content");

        while (content.equals("")) {
            parser.nextTag();
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("category")) {
                aaaa = parser.getAttributeValue(null, "name");
                //entries.add(readEntry(parser));
                content = readCategory(parser, contentName, page);
            } else {
                skip(parser);
            }
        }
        return content;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readCategory(XmlPullParser parser,String contentName, int page) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "category");
        String categoryName = parser.getAttributeValue(null, "name");
        String content = "";
        String currentPageNr;

        if (categoryName.equalsIgnoreCase(contentName))
        {
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("page")) {
                    currentPageNr = parser.getAttributeValue(null, "number");
                    if (currentPageNr.equals(String.valueOf(page)))
                    {
                        content = readPage(parser);
                    }else
                    {
                        skip(parser);
                    }
                }  else {
                    skip(parser);
                }
            }
        }


        return content;
    }

    private String readPage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "page");
        String content = "";

        while (parser.nextTag() != XmlPullParser.END_TAG) {
            String asd = parser.getName();
            int parsenr = parser.next();
            content = parser.getText();
        }
        return content;
    }

    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
        }
        return result;
    }

    public void onNextButtonClick(View v)
    {
        Step++;
        if (selectCategory(Category) == "")
        {
            Step--;
        }
    }

    public void onPreviousClick(View v)
    {
        if (Step == 1)
        {
            return;
        }

        Step--;
        selectCategory(Category);
    }
}
