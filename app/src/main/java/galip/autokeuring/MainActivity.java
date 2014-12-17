package galip.autokeuring;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;
import android.app.Fragment;
import android.app.FragmentManager;


public class MainActivity extends Activity {

    public static final String PRFERENCE_FILE = "AUTOKEURING";
    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


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
}
