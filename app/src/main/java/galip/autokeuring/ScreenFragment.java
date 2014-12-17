package galip.autokeuring;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScreenFragment extends Fragment {

    public static final String MENU_POSITION = "menu_number";

    public ScreenFragment()
    {

    }
///aj
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View resultView = null;
        int MenuSelection = getArguments().getInt(MENU_POSITION);

        if (MenuSelection == 0)
        {
            resultView = inflater.inflate(R.layout.welcomescreen, container, false);

        }else if(MenuSelection == 1)
        {
            resultView = inflater.inflate(R.layout.apkstartscreen, container, false);
        }
        else if(MenuSelection == 3)
        {
            resultView = inflater.inflate(R.layout.appinfoscreen, container, false);
        }else
        {
            resultView = inflater.inflate(R.layout.unknownscreen, container, false);
        }

        return resultView;
    }
}
