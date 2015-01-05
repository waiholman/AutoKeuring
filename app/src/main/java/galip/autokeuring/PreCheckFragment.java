package galip.autokeuring;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PreCheckFragment extends Fragment {

    public static final String MENU_POSITION = "menu_number";
    public static final String CATEGORY_SELECTION = "category_number";

    public String ContentText = "";
    public String StepText = "";
    public String CategoryText = "";

    public PreCheckFragment()
    {
    }
//mark
    public PreCheckFragment(String Header, String ContentText, String StepText)
    {
        this.CategoryText = Header;
        this.ContentText = ContentText;
        this.StepText = StepText;
    }

    public void changeContentFragmentText(View v)
    {
        TextView HeaderView = (TextView) v.findViewById(R.id.txtCategory);
        HeaderView.setText(CategoryText);
        TextView ContentView = (TextView) v.findViewById(R.id.txtApkCheckStep_Information);
        ContentView.setText(ContentText);
        TextView NavigationView = (TextView) v.findViewById(R.id.txtStep);
        NavigationView.setText(StepText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View resultView = null;
        String CategorySelection = getArguments().getString(CATEGORY_SELECTION);

        //if(CategorySelection == "Banden") {
            resultView =  inflater.inflate(R.layout.apkcheckstep_fragment, container, false);
        //}

        changeContentFragmentText(resultView);

        return resultView;
    }
}
